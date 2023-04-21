package jmri.jmrix.ecos;

import java.util.Enumeration;
import java.util.List;
import java.util.ArrayList;
import java.util.Hashtable;
import jmri.Turnout;
import jmri.jmrix.ecos.utilities.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Component;
import java.util.ResourceBundle;

/**
 * Implement turnout manager for Ecos systems.
 * <P>
 * System names are "UTnnn", where nnn is the turnout number without padding.
 *
 * @author	Bob Jacobsen Copyright (C) 2001, 2008
 * @version	$Revision: 1.19 $
 */
public class EcosTurnoutManager extends jmri.managers.AbstractTurnoutManager implements EcosListener {

    public EcosTurnoutManager(EcosSystemConnectionMemo memo) {
        adaptermemo = memo;
        prefix = adaptermemo.getSystemPrefix();
        tc = adaptermemo.getTrafficController();
        tc.addEcosListener(this);
        EcosMessage m = new EcosMessage("request(11, view)");
        tc.sendEcosMessage(m, this);
        m = new EcosMessage("queryObjects(11, addrext)");
        tc.sendEcosMessage(m, this);
        this.addPropertyChangeListener(this);
    }

    EcosTrafficController tc;

    EcosSystemConnectionMemo adaptermemo;

    private Hashtable<Integer, EcosTurnout> _tecos = new Hashtable<Integer, EcosTurnout>();

    String prefix;

    public String getSystemPrefix() {
        return prefix;
    }

    public Turnout createNewTurnout(String systemName, String userName) {
        int addr;
        try {
            addr = Integer.valueOf(systemName.substring(getSystemPrefix().length() + 1)).intValue();
        } catch (java.lang.NumberFormatException e) {
            log.error("failed to convert systemName " + systemName + " to a turnout address");
            return null;
        }
        Turnout t = new EcosTurnout(addr, getSystemPrefix(), tc, this);
        t.setUserName(userName);
        return t;
    }

    public void reply(EcosReply m) {
        EcosTurnout et;
        int start;
        int end;
        String msg = m.toString();
        String[] lines = msg.split("\n");
        if (lines[lines.length - 1].contains("<END 0 (OK)>")) {
            if (lines[0].startsWith("<REPLY queryObjects(11)>")) {
                checkTurnoutList(lines);
            } else if (lines[0].startsWith("<REPLY queryObjects(11, addr)>")) {
                log.debug("found " + (lines.length - 2) + " turnout objects");
                for (int i = 1; i < lines.length - 1; i++) {
                    if (lines[i].contains("addr[")) {
                        int object = GetEcosObjectNumber.getEcosObjectNumber(lines[i], null, " ");
                        if ((20000 <= object) && (object < 30000)) {
                            int addr = GetEcosObjectNumber.getEcosObjectNumber(lines[i], "[", "]");
                            log.debug("Found turnout object " + object + " addr " + addr);
                            if (addr > 0) {
                                Turnout t = getTurnout(prefix + "T" + addr);
                                if (t == null) {
                                    et = (EcosTurnout) provideTurnout(prefix + "T" + addr);
                                    et.setObjectNumber(object);
                                    _tecos.put(object, et);
                                }
                            }
                        } else if ((30000 <= object) && (object < 40000)) {
                            log.debug("Found route object " + object);
                            Turnout t = getTurnout(prefix + "T" + object);
                            if (t == null) {
                                et = (EcosTurnout) provideTurnout(prefix + "T" + object);
                                et.setObjectNumber(object);
                                _tecos.put(object, et);
                            }
                        }
                        if ((20000 <= object) && (object < 40000)) {
                            EcosMessage em = new EcosMessage("request(" + object + ",view)");
                            tc.sendEcosMessage(em, null);
                            em = new EcosMessage("get(" + object + ",state)");
                            tc.sendEcosMessage(em, null);
                        }
                    }
                }
            } else if (lines[0].startsWith("<REPLY get(")) {
                int object = GetEcosObjectNumber.getEcosObjectNumber(lines[0], "(", ",");
                if ((20000 <= object) && (object < 40000)) {
                    et = (EcosTurnout) getByEcosObject(object);
                    if (lines[0].contains("state")) {
                        et.reply(m);
                        if (et.getExtended() != 0) {
                            EcosTurnout etx = (EcosTurnout) provideTurnout(et.getSlaveAddress());
                            etx.reply(m);
                        }
                    } else if (lines[0].contains("symbol")) {
                        int symbol = GetEcosObjectNumber.getEcosObjectNumber(lines[1], "[", "]");
                        et.setExtended(symbol);
                        et.setTurnoutOperation(jmri.TurnoutOperationManager.getInstance().getOperation("NoFeedback"));
                        if ((symbol == 2) || (symbol == 4)) {
                            EcosTurnout etx = (EcosTurnout) provideTurnout(et.getSlaveAddress());
                            etx.setExtended(symbol);
                            etx.setTurnoutOperation(jmri.TurnoutOperationManager.getInstance().getOperation("NoFeedback"));
                            switch(symbol) {
                                case 2:
                                    et.setComment("Three Way Point with " + et.getSlaveAddress());
                                    break;
                                case 4:
                                    et.setComment("Double Slip with " + et.getSlaveAddress());
                                    break;
                            }
                        }
                        EcosMessage em = new EcosMessage("get(" + object + ",state)");
                        tc.sendEcosMessage(em, null);
                    } else if (lines[0].contains("addrext")) {
                        turnoutAddressDetails(lines[1]);
                    } else {
                        String name = null;
                        for (int i = 1; i < lines.length - 1; i++) {
                            if (lines[i].contains("name")) {
                                start = lines[i].indexOf("[") + 2;
                                end = lines[i].indexOf("]") - 1;
                                if ((name != null) && (start != end)) name = name + " " + lines[i].substring(start, end); else if (name == null) name = lines[i].substring(start, end);
                            }
                        }
                        if (name != null) et.setUserName(name);
                    }
                }
            } else if (lines[0].startsWith("<EVENT 11>")) {
                if (lines[1].contains("msg[LIST_CHANGED]")) {
                    log.debug("We have received notification of a change in the Turnout list");
                    EcosMessage mout = new EcosMessage("queryObjects(11)");
                    tc.sendEcosMessage(mout, this);
                }
            } else if (lines[0].startsWith("<EVENT")) {
                int object = GetEcosObjectNumber.getEcosObjectNumber(lines[0], " ", ">");
                if ((20000 <= object) && (object < 40000)) {
                    log.debug("Forwarding on State change for " + object);
                    et = _tecos.get(object);
                    if (et != null) {
                        et.reply(m);
                        if (et.getExtended() != 0) {
                            log.debug("This is also an extended turnout so forwarding on change to " + et.getSlaveAddress());
                            EcosTurnout etx = (EcosTurnout) provideTurnout(et.getSlaveAddress());
                            etx.reply(m);
                        }
                    }
                }
            } else if (lines[0].startsWith("<REPLY queryObjects(11, addrext)>")) {
                for (int i = 1; i < lines.length - 1; i++) {
                    if (lines[i].contains("addrext[")) {
                        turnoutAddressDetails(lines[i]);
                    }
                }
            }
        } else log.debug("Message received from Ecos is in error");
    }

    protected boolean addingTurnouts = false;

    private void turnoutAddressDetails(String lines) {
        addingTurnouts = true;
        EcosTurnout et;
        int start;
        int end;
        int object = GetEcosObjectNumber.getEcosObjectNumber(lines, null, " ");
        if ((20000 <= object) && (object < 30000)) {
            start = lines.indexOf('[') + 1;
            end = lines.indexOf(']');
            String turnoutadd = stripChar(lines.substring(start, end));
            String[] straddr = turnoutadd.split(",");
            log.debug("Number of Address for this device is " + straddr.length);
            if (straddr.length <= 2) {
                if (straddr.length == 2) {
                    if (!straddr[0].equals(straddr[1])) log.debug("Addresses are not the same, we shall use the first address listed.");
                }
                int addr = Integer.parseInt(straddr[0]);
                if (addr > 0) {
                    Turnout t = getTurnout(prefix + "T" + addr);
                    if (t == null) {
                        et = (EcosTurnout) provideTurnout(prefix + "T" + addr);
                        et.setObjectNumber(object);
                        _tecos.put(object, et);
                        EcosMessage em = new EcosMessage("request(" + object + ",view)");
                        tc.sendEcosMessage(em, null);
                        em = new EcosMessage("get(" + object + ",state)");
                        tc.sendEcosMessage(em, null);
                        em = new EcosMessage("get(" + object + ", name1, name2, name3)");
                        tc.sendEcosMessage(em, null);
                    }
                }
            } else if (straddr.length == 4) {
                log.debug("We have a two address object.");
                if (!straddr[0].equals(straddr[1])) log.debug("First Pair of Addresses are not the same, we shall use the first address");
                if (!straddr[2].equals(straddr[3])) log.debug("Second Pair of Addresses are not the same, we shall use the first address");
                int addr = Integer.parseInt(straddr[0]);
                int addr2 = Integer.parseInt(straddr[2]);
                if (addr > 0) {
                    Turnout t = getTurnout(prefix + "T" + addr);
                    if (t == null) {
                        et = (EcosTurnout) provideTurnout(prefix + "T" + addr);
                        et.setObjectNumber(object);
                        et.setSlaveAddress(addr2);
                        _tecos.put(object, et);
                        EcosMessage em = new EcosMessage("get(" + object + ",symbol)");
                        tc.sendEcosMessage(em, this);
                        em = new EcosMessage("request(" + object + ",view)");
                        tc.sendEcosMessage(em, this);
                        em = new EcosMessage("get(" + object + ", name1, name2, name3)");
                        tc.sendEcosMessage(em, this);
                    }
                }
                if (addr2 > 0) {
                    Turnout t = getTurnout(prefix + "T" + addr2);
                    if (t == null) {
                        et = (EcosTurnout) provideTurnout(prefix + "T" + addr2);
                        et.setMasterObjectNumber(false);
                        et.setObjectNumber(object);
                        et.setComment("Extended address linked with turnout " + getSystemPrefix() + "T" + straddr[0]);
                    }
                }
            }
        } else if ((30000 <= object) && (object < 40000)) {
            log.debug("Found route object " + object);
            Turnout t = getTurnout(prefix + "T" + object);
            if (t == null) {
                et = (EcosTurnout) provideTurnout(prefix + "T" + object);
                et.setObjectNumber(object);
                _tecos.put(object, et);
                EcosMessage em = new EcosMessage("get(" + object + ",state)");
                tc.sendEcosMessage(em, null);
                em = new EcosMessage("get(" + object + ", name1, name2, name3)");
                tc.sendEcosMessage(em, null);
            }
        }
        addingTurnouts = false;
    }

    void checkTurnoutList(String[] ecoslines) {
        final EcosPreferences p = adaptermemo.getPreferenceManager();
        String[] jmrilist = getEcosObjectArray();
        boolean nomatch = true;
        int intTurnout = 0;
        String strECOSTurnout = null;
        for (int i = 0; i < jmrilist.length; i++) {
            nomatch = true;
            String strJMRITurnout = jmrilist[i];
            intTurnout = Integer.parseInt(strJMRITurnout);
            for (int k = 1; k < ecoslines.length - 1; k++) {
                strECOSTurnout = ecoslines[k].replaceAll("[\\n\\r]", "");
                if (strECOSTurnout.equals(strJMRITurnout)) {
                    nomatch = false;
                    break;
                }
            }
            if (nomatch) {
                final EcosTurnout et = (EcosTurnout) getByEcosObject(intTurnout);
                _tecos.remove(intTurnout);
                if (p.getRemoveTurnoutsFromJMRI() == 0x02) {
                    _tecos.remove(et.getObject());
                    deregister(et);
                } else if (p.getRemoveTurnoutsFromJMRI() == 0x00) {
                    final JDialog dialog = new JDialog();
                    dialog.setTitle("Delete Turnout");
                    dialog.setLocationRelativeTo(null);
                    dialog.setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);
                    JPanel container = new JPanel();
                    container.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                    container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
                    JLabel question = new JLabel("A Turnout " + et.getDisplayName() + " has been deleted on the ECOS");
                    question.setAlignmentX(Component.CENTER_ALIGNMENT);
                    container.add(question);
                    question = new JLabel("Do you want to remove this turnout from JMRI");
                    question.setAlignmentX(Component.CENTER_ALIGNMENT);
                    container.add(question);
                    final JCheckBox remember = new JCheckBox("Remember this setting for next time?");
                    remember.setFont(remember.getFont().deriveFont(10f));
                    remember.setAlignmentX(Component.CENTER_ALIGNMENT);
                    JButton yesButton = new JButton("Yes");
                    JButton noButton = new JButton("No");
                    JPanel button = new JPanel();
                    button.setAlignmentX(Component.CENTER_ALIGNMENT);
                    button.add(yesButton);
                    button.add(noButton);
                    container.add(button);
                    noButton.addActionListener(new ActionListener() {

                        public void actionPerformed(ActionEvent e) {
                            if (remember.isSelected()) {
                                p.setRemoveTurnoutsFromJMRI(0x01);
                            }
                            dialog.dispose();
                        }
                    });
                    yesButton.addActionListener(new ActionListener() {

                        final ResourceBundle rb = ResourceBundle.getBundle("jmri.jmrit.beantable.BeanTableBundle");

                        public void actionPerformed(ActionEvent e) {
                            if (remember.isSelected()) {
                                p.setRemoveTurnoutsFromJMRI(0x02);
                            }
                            int count = et.getNumPropertyChangeListeners() - 1;
                            if (log.isDebugEnabled()) log.debug("Delete with " + count);
                            if ((!noWarnDelete) && (count > 0)) {
                                String msg = java.text.MessageFormat.format(rb.getString("DeletePrompt") + "\n" + rb.getString("ReminderInUse"), new Object[] { et.getSystemName(), "" + count });
                                int val = javax.swing.JOptionPane.showOptionDialog(null, msg, rb.getString("WarningTitle"), javax.swing.JOptionPane.YES_NO_CANCEL_OPTION, javax.swing.JOptionPane.QUESTION_MESSAGE, null, new Object[] { rb.getString("ButtonYes"), rb.getString("ButtonYesPlus"), rb.getString("ButtonNo") }, rb.getString("ButtonNo"));
                                if (val == 2) {
                                    _tecos.remove(et.getObject());
                                    deregister(et);
                                    dialog.dispose();
                                    return;
                                }
                                if (val == 1) {
                                    noWarnDelete = true;
                                }
                            }
                            deleteEcosTurnout(et);
                            dialog.dispose();
                        }
                    });
                    container.add(remember);
                    container.setAlignmentX(Component.CENTER_ALIGNMENT);
                    container.setAlignmentY(Component.CENTER_ALIGNMENT);
                    dialog.getContentPane().add(container);
                    dialog.pack();
                    dialog.setModal(true);
                    dialog.setVisible(true);
                } else {
                    _tecos.remove(et.getObject());
                }
            }
        }
        int turnout;
        for (int i = 1; i < ecoslines.length - 1; i++) {
            String tmpturn = ecoslines[i].replaceAll("[\\n\\r]", "");
            turnout = Integer.parseInt(tmpturn);
            if (getByEcosObject(turnout) == null) {
                EcosMessage mout = new EcosMessage("get(" + turnout + ", addrext)");
                tc.sendEcosMessage(mout, this);
            }
        }
    }

    boolean noWarnDelete = false;

    public String stripChar(String s) {
        String allowed = ",0123456789";
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            if (allowed.indexOf(s.charAt(i)) >= 0) result.append(s.charAt(i));
        }
        return result.toString();
    }

    public void message(EcosMessage m) {
    }

    @Override
    public void propertyChange(java.beans.PropertyChangeEvent e) {
        if ((e.getPropertyName().equals("length")) && (!addingTurnouts)) {
            final EcosPreferences p = adaptermemo.getPreferenceManager();
            EcosTurnout et;
            String[] ecoslist = this.getEcosObjectArray();
            String[] jmrilist = getSystemNameArray();
            for (int i = 0; i < jmrilist.length; i++) {
                if (jmrilist[i].startsWith(prefix + "T")) {
                    et = (EcosTurnout) getBySystemName(jmrilist[i]);
                    if (et.getObject() == 0) {
                    }
                }
            }
            for (int i = 0; i < ecoslist.length; i++) {
                et = (EcosTurnout) getByEcosObject(Integer.parseInt(ecoslist[i]));
                int address = et.getNumber();
                if (getBySystemName(prefix + "T" + address) == null) {
                    if (p.getRemoveTurnoutsFromEcos() == 0x02) {
                        RemoveObjectFromEcos removeObjectFromEcos = new RemoveObjectFromEcos();
                        removeObjectFromEcos.removeObjectFromEcos("" + et.getObject(), tc);
                        deleteEcosTurnout(et);
                    } else {
                        final EcosTurnout etd = et;
                        final JDialog dialog = new JDialog();
                        dialog.setTitle("Remove Turnout From ECoS?");
                        dialog.setLocation(300, 200);
                        dialog.setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);
                        JPanel container = new JPanel();
                        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
                        container.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                        JLabel question = new JLabel("Do you also want to remove turnout " + etd.getSystemName() + " from the Ecos");
                        question.setAlignmentX(Component.CENTER_ALIGNMENT);
                        container.add(question);
                        final JCheckBox remember = new JCheckBox("Remember this setting for next time?");
                        remember.setFont(remember.getFont().deriveFont(10f));
                        remember.setAlignmentX(Component.CENTER_ALIGNMENT);
                        remember.setVisible(true);
                        JButton yesButton = new JButton("Yes");
                        JButton noButton = new JButton("No");
                        JPanel button = new JPanel();
                        button.setAlignmentX(Component.CENTER_ALIGNMENT);
                        button.add(yesButton);
                        button.add(noButton);
                        container.add(button);
                        noButton.addActionListener(new ActionListener() {

                            public void actionPerformed(ActionEvent e) {
                                if (remember.isSelected()) {
                                    p.setRemoveTurnoutsFromEcos(0x01);
                                }
                                dialog.dispose();
                            }
                        });
                        yesButton.addActionListener(new ActionListener() {

                            public void actionPerformed(ActionEvent e) {
                                if (remember.isSelected()) {
                                    p.setRemoveTurnoutsFromEcos(0x02);
                                }
                                RemoveObjectFromEcos removeObjectFromEcos = new RemoveObjectFromEcos();
                                removeObjectFromEcos.removeObjectFromEcos("" + etd.getObject(), tc);
                                deleteEcosTurnout(etd);
                                dialog.dispose();
                            }
                        });
                        container.add(remember);
                        container.setAlignmentX(Component.CENTER_ALIGNMENT);
                        container.setAlignmentY(Component.CENTER_ALIGNMENT);
                        dialog.getContentPane().add(container);
                        dialog.pack();
                        dialog.setModal(true);
                        dialog.setVisible(true);
                    }
                }
            }
        }
        super.propertyChange(e);
    }

    public void deleteEcosTurnout(EcosTurnout et) {
        addingTurnouts = true;
        deregister(et);
        et.dispose();
        EcosMessage em = new EcosMessage("release(" + et.getObject() + ",view)");
        tc.sendEcosMessage(em, this);
        _tecos.remove(et.getObject());
        addingTurnouts = false;
    }

    @Override
    public void dispose() {
        Enumeration<Integer> en = _tecos.keys();
        EcosMessage em;
        while (en.hasMoreElements()) {
            int ecosObject = en.nextElement();
            em = new EcosMessage("release(" + ecosObject + ",view)");
            tc.sendEcosMessage(em, this);
        }
        if (jmri.InstanceManager.configureManagerInstance() != null) jmri.InstanceManager.configureManagerInstance().deregister(this);
        _tecos.clear();
    }

    public List<String> getEcosObjectList() {
        String[] arr = new String[_tecos.size()];
        List<String> out = new ArrayList<String>();
        Enumeration<Integer> en = _tecos.keys();
        int i = 0;
        while (en.hasMoreElements()) {
            arr[i] = "" + en.nextElement();
            i++;
        }
        jmri.util.StringUtil.sort(arr);
        for (i = 0; i < arr.length; i++) out.add(arr[i]);
        return out;
    }

    public String[] getEcosObjectArray() {
        String[] arr = new String[_tecos.size()];
        Enumeration<Integer> en = _tecos.keys();
        int i = 0;
        while (en.hasMoreElements()) {
            arr[i] = "" + en.nextElement();
            i++;
        }
        java.util.Arrays.sort(arr);
        return arr;
    }

    public Turnout getByEcosObject(int ecosObject) {
        return _tecos.get(ecosObject);
    }

    public void refreshItems() {
        EcosMessage m = new EcosMessage("request(11, view)");
        tc.sendEcosMessage(m, this);
    }

    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(EcosTurnoutManager.class.getName());
}
