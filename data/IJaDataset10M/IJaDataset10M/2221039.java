package net.etherstorm.jOpenRPG.nodehandlers;

import org.jdom.Element;
import javax.swing.event.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import net.etherstorm.jOpenRPG.JTextCtrlToolbar;
import java.util.*;
import net.etherstorm.jOpenRPG.commlib.ChatMessage;
import net.etherstorm.jOpenRPG.actions.DefaultAction;

/**
 * Class declaration
 *
 *
 * @author $Author: tedberg $
 * @version $Revision: 352 $
 */
public class listbox_handler extends TriStateNodehandler {

    public static final int DROPDOWN_STATE = 0;

    public static final int LISTBOX_STATE = 1;

    public static final int RADIO_STATE = 2;

    public static final int CHECKLIST_STATE = 3;

    /**
	 * Constructor declaration
	 *
	 *
	 * @param elem
	 *
	 */
    public listbox_handler(Element elem) {
        super(elem);
    }

    /**
	 * Method declaration
	 *
	 *
	 *
	 *
	 */
    public int getDisplayState() {
        String state = myElement.getChild("list").getAttributeValue("type");
        try {
            return Integer.parseInt(state);
        } catch (Exception ex) {
            handleException(ex);
            return 0;
        }
    }

    /**
	 * Method declaration
	 *
	 *
	 * @param state
	 *
	 *
	 */
    public void setDisplayState(int state) {
        myElement.getChild("list").setAttribute("type", String.valueOf(state));
        if (usePanel != null) {
            ((UsePanel) usePanel).updateInterface();
        }
    }

    /**
	 * Method declaration
	 *
	 *
	 *
	 *
	 */
    public int getSize() {
        return myElement.getChild("list").getChildren("option").size();
    }

    /**
	 * Method declaration
	 *
	 *
	 * @param index
	 *
	 *
	 *
	 */
    Element getElementAt(int index) {
        return (Element) myElement.getChild("list").getChildren("option").get(index);
    }

    /**
	 * Method declaration
	 *
	 *
	 * @param index
	 *
	 *
	 *
	 */
    public String getItemAt(int index) {
        return getElementAt(index).getText();
    }

    /**
	 * Method declaration
	 *
	 *
	 * @param index
	 *
	 *
	 *
	 */
    public boolean isSelected(int index) {
        return getElementAt(index).getAttributeValue("selected").equals("1");
    }

    /**
	 * Method declaration
	 *
	 *
	 * @param index
	 * @param value
	 *
	 *
	 */
    public void setSelected(int index, boolean value) {
        getElementAt(index).setAttribute("selected", value ? "1" : "0");
    }

    /**
	 * Method declaration
	 *
	 *
	 *
	 */
    public void unselectAll() {
        for (int loop = 0; loop < getSize(); loop++) {
            setSelected(loop, false);
        }
    }

    /**
	 * Method declaration
	 *
	 *
	 *
	 */
    public void ensureOneSelectedIndex() {
        boolean hit = false;
        for (int loop = 0; loop < getSize(); loop++) {
            if (!hit) {
                if (isSelected(loop)) {
                    hit = true;
                }
            } else {
                if (isSelected(loop)) {
                    setSelected(loop, false);
                }
            }
        }
        if (!hit && getSize() > 0) {
            setSelected(0, true);
        }
    }

    /**
	 * Method declaration
	 *
	 *
	 *
	 *
	 */
    public String getAsText() {
        StringBuffer buf = new StringBuffer();
        for (int loop = 0; loop < getSize(); loop++) {
            buf.append(getItemAt(loop) + "\n");
        }
        return buf.toString();
    }

    /**
	 * Method declaration
	 *
	 *
	 * @param text
	 *
	 *
	 */
    public void setAsText(String text) {
        java.util.List list = myElement.getChild("list").getChildren("option");
        list.clear();
        StringTokenizer tok = new StringTokenizer(text, "\n");
        while (tok.hasMoreTokens()) {
            Element e = new Element("option").setAttribute("selected", "0");
            e.setText(tok.nextToken());
            myElement.getChild("list").addContent(e);
        }
        setDisplayState(getDisplayState());
    }

    /**
	 * Method declaration
	 *
	 *
	 *
	 *
	 */
    public int getSelectedIndex() {
        for (int loop = 0; loop < getSize(); loop++) {
            if (isSelected(loop)) {
                return loop;
            }
        }
        return -1;
    }

    /**
	 * Method declaration
	 *
	 *
	 *
	 *
	 */
    public AbstractUsePanel getUsePanel() {
        if (usePanel == null) {
            usePanel = new UsePanel();
        }
        return usePanel;
    }

    /**
	 * Method declaration
	 *
	 *
	 *
	 *
	 */
    public AbstractEditPanel getEditPanel() {
        if (editPanel == null) {
            editPanel = new EditPanel();
        }
        return editPanel;
    }

    /**
	 * Method declaration
	 *
	 *
	 *
	 */
    public void sendToChat() {
        toChat(new ChatMessage());
    }

    /**
	 * Method declaration
	 *
	 *
	 *
	 */
    public void whisperToChat() {
        ChatMessage cm = new ChatMessage();
        cm.setWhisper();
        toChat(cm);
    }

    /**
	 * Method declaration
	 *
	 *
	 * @param cm
	 *
	 *
	 */
    void toChat(ChatMessage cm) {
        StringBuffer buf = new StringBuffer();
        for (int loop = 0; loop < getSize(); loop++) {
            if (isSelected(loop)) {
                buf.append(getItemAt(loop) + ", ");
            }
        }
        buf.setLength(buf.toString().length() - 2);
        System.out.println(buf.toString());
        cm.setMessage(core.parseDieRolls(buf.toString()));
        cm.send();
    }

    /**
	 * Method declaration
	 *
	 *
	 * @param value
	 *
	 *
	 */
    void setShowToolbar(boolean value) {
        myElement.getChild("list").setAttribute("send_button", value ? "1" : "0");
        if (usePanel != null) {
            ((UsePanel) usePanel).updateInterface();
        }
    }

    /**
	 * Method declaration
	 *
	 *
	 *
	 *
	 */
    boolean getShowToolbar() {
        try {
            return myElement.getChild("list").getAttributeValue("send_button").equals("1");
        } catch (Exception ex) {
            return false;
        }
    }

    /**
	 * Method declaration
	 *
	 *
	 *
	 *
	 */
    public String toHtml() {
        StringBuffer buf = new StringBuffer();
        buf.append(super.toHtml());
        for (int loop = 0; loop < getSize(); loop++) {
            buf.append("<br>" + getItemAt(loop));
        }
        buf.append("<br>");
        return referenceManager.getCore().parseDieRolls(buf.toString());
    }

    /**
	 * Class declaration
	 *
	 *
	 * @author $Author: tedberg $
	 * @version $Revision: 352 $
	 */
    public class UsePanel extends AbstractUsePanel {

        listbox_handler_model dataModel = new listbox_handler_model();

        ListInterface list;

        JToolBar tb;

        /**
		 * Constructor declaration
		 *
		 *
		 */
        public UsePanel() {
            super();
            tb = new JToolBar();
            tb.add(new SendToChatAction());
            tb.add(new WhisperToChatAction());
            add(tb, BorderLayout.NORTH);
            updateInterface();
        }

        /**
		 * Method declaration
		 *
		 *
		 *
		 */
        public void updateInterface() {
            if (list != null) {
                remove((JPanel) list);
            }
            list = null;
            switch(getDisplayState()) {
                case DROPDOWN_STATE:
                    list = new DropDownPanel();
                    break;
                case RADIO_STATE:
                    list = new RadioButtonPanel();
                    break;
                default:
                    list = new ListPanel();
            }
            add((JPanel) list, BorderLayout.CENTER);
            list.updateData();
            ((JPanel) list).setVisible(false);
            ((JPanel) list).setVisible(true);
            tb.setVisible(getShowToolbar());
        }

        /**
		 * Method declaration
		 *
		 *
		 *
		 */
        public void doSendToChat() {
            listbox_handler.this.sendToChat();
        }

        /**
		 * Method declaration
		 *
		 *
		 *
		 */
        public void doWhisperToChat() {
            listbox_handler.this.whisperToChat();
        }

        /**
		 * Class declaration
		 *
		 *
		 * @author $Author: tedberg $
		 * @version $Revision: 352 $
		 */
        public class SendToChatAction extends DefaultAction {

            /**
			 * Constructor declaration
			 *
			 *
			 */
            public SendToChatAction() {
                initProperties("SendTextAction");
            }

            /**
			 * Method declaration
			 *
			 *
			 * @param evt
			 *
			 *
			 */
            public void actionPerformed(ActionEvent evt) {
                super.actionPerformed(evt);
                doSendToChat();
            }
        }

        /**
		 * Class declaration
		 *
		 *
		 * @author $Author: tedberg $
		 * @version $Revision: 352 $
		 */
        public class WhisperToChatAction extends DefaultAction {

            /**
			 * Constructor declaration
			 *
			 *
			 */
            public WhisperToChatAction() {
                initProperties("WhisperTextAction");
            }

            /**
			 * Method declaration
			 *
			 *
			 * @param evt
			 *
			 *
			 */
            public void actionPerformed(ActionEvent evt) {
                super.actionPerformed(evt);
                doWhisperToChat();
            }
        }
    }

    /**
	 * Class declaration
	 *
	 *
	 * @author $Author: tedberg $
	 * @version $Revision: 352 $
	 */
    public class EditPanel extends AbstractEditPanel implements ActionListener {

        JComboBox mode;

        JTextArea data;

        JButton updateList;

        JCheckBox showToolbar;

        /**
		 * Constructor declaration
		 *
		 *
		 */
        public EditPanel() {
            mode = new JComboBox(new Object[] { "Drop Down", "List Box", "Radio Buttons", "Multiple Selection List Box" });
            mode.addActionListener(this);
            mode.setSelectedIndex(getDisplayState());
            data = new JTextArea();
            data.setText(getAsText());
            showToolbar = new JCheckBox("Show Toolbar");
            showToolbar.addActionListener(this);
            showToolbar.setSelected(getShowToolbar());
            Box b = Box.createHorizontalBox();
            b.add(new JLabel("List Type"));
            b.add(Box.createHorizontalStrut(8));
            b.add(mode);
            add(b);
            add(showToolbar);
            add(new JScrollPane(data));
            updateList = new JButton("Update list");
            updateList.addActionListener(this);
            add(updateList);
        }

        /**
		 * Method declaration
		 *
		 *
		 * @param evt
		 *
		 *
		 */
        public void actionPerformed(ActionEvent evt) {
            if (evt.getSource() == mode) {
                setDisplayState(mode.getSelectedIndex());
            } else if (evt.getSource() == updateList) {
                setAsText(data.getText());
            } else if (evt.getSource() == showToolbar) {
                setShowToolbar(showToolbar.isSelected());
            }
        }
    }

    /**
	 * Interface declaration
	 *
	 *
	 * @author $Author: tedberg $
	 * @version $Revision: 352 $
	 */
    public interface ListInterface {

        /**
		 * Method declaration
		 *
		 *
		 *
		 */
        public void updateData();
    }

    /**
	 * Class declaration
	 *
	 *
	 * @author $Author: tedberg $
	 * @version $Revision: 352 $
	 */
    public class DropDownPanel extends JPanel implements ListInterface, ActionListener {

        JComboBox choices;

        /**
		 * Constructor declaration
		 *
		 *
		 */
        public DropDownPanel() {
            super(new BorderLayout());
            choices = new JComboBox();
            choices.addActionListener(this);
            add(choices, BorderLayout.CENTER);
        }

        /**
		 * Method declaration
		 *
		 *
		 *
		 */
        public void updateData() {
            choices.removeAllItems();
            ensureOneSelectedIndex();
            for (int loop = 0; loop < listbox_handler.this.getSize(); loop++) {
                choices.addItem(getItemAt(loop));
            }
            choices.setSelectedIndex(getSelectedIndex());
            System.out.println(getClass() + " data updated");
        }

        /**
		 * Method declaration
		 *
		 *
		 * @param evt
		 *
		 *
		 */
        public void actionPerformed(ActionEvent evt) {
            unselectAll();
            setSelected(choices.getSelectedIndex(), true);
        }
    }

    /**
	 * Class declaration
	 *
	 *
	 * @author $Author: tedberg $
	 * @version $Revision: 352 $
	 */
    public class RadioButtonPanel extends JPanel implements ListInterface, ActionListener {

        Box box;

        ButtonGroup bgroup;

        ArrayList buttons;

        /**
		 * Constructor declaration
		 *
		 *
		 */
        public RadioButtonPanel() {
            super(new BorderLayout());
            box = Box.createVerticalBox();
            add(box, BorderLayout.CENTER);
            buttons = new ArrayList();
        }

        /**
		 * Method declaration
		 *
		 *
		 *
		 */
        public void updateData() {
            buttons.clear();
            ensureOneSelectedIndex();
            box.removeAll();
            bgroup = null;
            bgroup = new ButtonGroup();
            for (int loop = 0; loop < listbox_handler.this.getSize(); loop++) {
                JRadioButton rb = new JRadioButton(getItemAt(loop), isSelected(loop));
                rb.addActionListener(this);
                box.add(rb);
                bgroup.add(rb);
                buttons.add(rb);
            }
            System.out.println(getClass() + " data updated");
        }

        /**
		 * Method declaration
		 *
		 *
		 * @param evt
		 *
		 *
		 */
        public void actionPerformed(ActionEvent evt) {
            int index = buttons.indexOf(evt.getSource());
            unselectAll();
            setSelected(index, true);
        }
    }

    /**
	 * Class declaration
	 *
	 *
	 * @author $Author: tedberg $
	 * @version $Revision: 352 $
	 */
    public class ListPanel extends JPanel implements ListInterface, ListSelectionListener {

        JList list;

        DefaultListModel listmodel;

        /**
		 * Constructor declaration
		 *
		 *
		 */
        public ListPanel() {
            super(new BorderLayout());
            listmodel = new DefaultListModel();
            list = new JList(listmodel);
            add(new JScrollPane(list));
            if (getDisplayState() == CHECKLIST_STATE) {
                list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            } else {
                list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                ensureOneSelectedIndex();
            }
            list.addListSelectionListener(this);
        }

        /**
		 * Method declaration
		 *
		 *
		 * @param evt
		 *
		 *
		 */
        public void valueChanged(ListSelectionEvent evt) {
            if (getDisplayState() == CHECKLIST_STATE) {
                int[] indices = list.getSelectedIndices();
                unselectAll();
                for (int loop = 0; loop < indices.length; loop++) {
                    setSelected(indices[loop], true);
                }
            } else {
                unselectAll();
                setSelected(evt.getFirstIndex(), true);
            }
        }

        /**
		 * Method declaration
		 *
		 *
		 *
		 */
        public void updateData() {
            listmodel.clear();
            ArrayList selected = new ArrayList();
            for (int loop = 0; loop < listbox_handler.this.getSize(); loop++) {
                listmodel.addElement(getItemAt(loop));
                if (isSelected(loop)) {
                    selected.add(new Integer(loop));
                }
            }
            int[] s = new int[selected.size()];
            Iterator iter = selected.iterator();
            int index = 0;
            while (iter.hasNext()) {
                s[index++] = ((Integer) iter.next()).intValue();
            }
            list.setSelectedIndices(s);
            System.out.println(getClass() + " data updated");
        }
    }

    /**
	 * Class declaration
	 *
	 *
	 * @author $Author: tedberg $
	 * @version $Revision: 352 $
	 */
    public class listbox_handler_model implements ComboBoxModel {

        ArrayList listeners;

        /**
		 * Constructor declaration
		 *
		 *
		 */
        public listbox_handler_model() {
            listeners = new ArrayList();
        }

        /**
		 * Method declaration
		 *
		 *
		 * @param l
		 *
		 *
		 */
        public void addListDataListener(ListDataListener l) {
            listeners.add(l);
        }

        /**
		 * Method declaration
		 *
		 *
		 * @param index
		 *
		 *
		 *
		 */
        public Object getElementAt(int index) {
            return listbox_handler.this.getElementAt(index);
        }

        /**
		 * Method declaration
		 *
		 *
		 *
		 *
		 */
        public int getSize() {
            return listbox_handler.this.getSize();
        }

        /**
		 * Method declaration
		 *
		 *
		 * @param l
		 *
		 *
		 */
        public void removeListDataListener(ListDataListener l) {
            listeners.remove(l);
        }

        /**
		 * Method declaration
		 *
		 *
		 *
		 *
		 */
        public Object getSelectedItem() {
            return null;
        }

        /**
		 * Method declaration
		 *
		 *
		 * @param anItem
		 *
		 *
		 */
        public void setSelectedItem(Object anItem) {
        }
    }
}
