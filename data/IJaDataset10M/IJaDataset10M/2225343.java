package sts.gui.results;

import sts.hibernate.*;
import sts.framework.*;
import sts.framework.manager.*;
import sts.gui.bindings.*;
import kellinwood.meshi.form.*;
import kellinwood.meshi.manager.*;
import kellinwood.meshi.autocomplete.*;
import kellinwood.hibernate_util.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import sts.gui.*;
import net.sf.hibernate.*;
import sts.framework.manager.EntryManager;

/**
 *
 * @author ken
 */
public class UnifiedEntryResultForm extends javax.swing.JDialog implements AutocompleteListener {

    ResultBindings resultBindings;

    EntryBindings entryBindings;

    BoatBindings boatBindings;

    BoatParameterBindings dockSlipBindings;

    ContactBindings skipperBindings;

    PhoneNumberBindings dayPhoneBindings;

    PhoneNumberBindings evePhoneBindings;

    PhoneNumberBindings cellPhoneBindings;

    EmailAddressBindings emailBindings;

    Entry entry;

    JPanel leftFieldsPanel;

    JPanel rightFieldsPanel;

    JPanel rightResultFieldsPanel;

    JPanel leftResultFieldsPanel;

    boolean creatingEntry = false;

    PhoneNumber day = null;

    PhoneNumber eve = null;

    PhoneNumber cell = null;

    EmailAddress email = null;

    Crewmember cmSkipper;

    /** Creates new form CreateEntryForm */
    public UnifiedEntryResultForm(java.awt.Frame parent) {
        super(parent, true);
        setTitle("Add New Entry");
        Regatta regatta = Framework.onlyInstance().getRegatta();
        Boat boat = new Boat(null, null, new HashMap(), new ArrayList());
        ArrayList crew = new ArrayList();
        ArrayList phoneList = new ArrayList();
        ArrayList emailList = new ArrayList();
        Contact skipper = new Contact(true, phoneList, emailList, new ArrayList());
        Entry e = new Entry(null, 0.0, false, null, null, null, false, regatta, null, boat, crew, new ArrayList());
        cmSkipper = new Crewmember(0, "Skipper", skipper, e);
        crew.add(cmSkipper);
        creatingEntry = true;
        Result r = new Result();
        r.setDisposition("DNC");
        commonInit(e, r);
    }

    /** Creates new form CreateEntryForm */
    public UnifiedEntryResultForm(java.awt.Frame parent, Result result) {
        super(parent, true);
        setTitle("Edit Entry");
        commonInit(result.getEntry(), result);
    }

    protected void findAndSetSkipper(List<Crewmember> crew) {
        for (Crewmember cm : crew) {
            if (cm.getRole().toUpperCase().startsWith("SKIP")) {
                setSkipper(cm.getContact());
                return;
            }
        }
    }

    protected void setSkipper(Contact skipper) {
        cmSkipper.setContact(skipper);
        skipperBindings.setEntity(skipper);
        for (PhoneNumber pn : (List<PhoneNumber>) skipper.getPhoneNumbers()) {
            if (pn.getDescription() == null || pn.getDescription().length() == 0) continue;
            char c = pn.getDescription().charAt(0);
            if (c == 'd' || c == 'D' || c == 'w' || c == 'W') {
                dayPhoneBindings.setEntity(pn);
                day = pn;
            }
            if (c == 'e' || c == 'E' || c == 'h' || c == 'H') {
                evePhoneBindings.setEntity(pn);
                eve = pn;
            }
            if (c == 'c' || c == 'C' || c == 'm' || c == 'M') {
                cellPhoneBindings.setEntity(pn);
                cell = pn;
            }
        }
        for (EmailAddress ea : (List<EmailAddress>) skipper.getEmailAddresss()) {
            emailBindings.setEntity(ea);
            email = ea;
            break;
        }
    }

    protected void commonInit(Entry entry, Result result) {
        this.entry = entry;
        entryBindings = new EntryBindings(entry);
        entryBindings.setExceptionHandler(ErrorDialog.onlyInstance());
        boatBindings = new BoatBindings(entry.getBoat());
        boatBindings.setExceptionHandler(ErrorDialog.onlyInstance());
        try {
            BoatManager.onlyInstance().getBoatBoatParameters(entry.getBoat());
        } catch (Exception x) {
            ErrorDialog.handle(x);
        }
        if (entry.getId() != null) {
            try {
                HibernateUtil.reassociate(EntryManager.onlyInstance().getEntryCrewmembersReassociator(entry));
            } catch (Exception x) {
                sts.gui.ErrorDialog.handle(x);
            }
        }
        skipperBindings = new ContactBindings();
        BoatParameter dockSlip = (BoatParameter) entry.getBoat().getBoatParameters().get("dock/slip");
        if (dockSlip == null) {
            dockSlip = new BoatParameter();
            dockSlip.setBoat(entry.getBoat());
            dockSlip.setName("dock/slip");
            entry.getBoat().getBoatParameters().put(dockSlip.getName(), dockSlip);
        }
        dockSlipBindings = new BoatParameterBindings(dockSlip);
        dockSlipBindings.setExceptionHandler(ErrorDialog.onlyInstance());
        dayPhoneBindings = new PhoneNumberBindings();
        evePhoneBindings = new PhoneNumberBindings();
        cellPhoneBindings = new PhoneNumberBindings();
        emailBindings = new EmailAddressBindings();
        List<Crewmember> crew = (List<Crewmember>) entry.getCrewmembers();
        cmSkipper = crew.get(0);
        findAndSetSkipper(crew);
        List leftFields = new LinkedList();
        List rightFields = new LinkedList();
        leftFields.add(boatBindings.getBoatNameEditor());
        leftFields.add(boatBindings.getUssaClassNameEditor());
        boatBindings.getUssaClassNameEditor().putClientProperty(AbstractBindings.EDITOR_DISPLAY_PROPERTY_NAME, "Boat Type");
        leftFields.add(boatBindings.getSailNumberEditor());
        entryBindings.getHandicapEditor().putClientProperty(AbstractBindings.EDITOR_DISPLAY_PROPERTY_NAME, "Default Handicap");
        leftFields.add(entryBindings.getHandicapEditor());
        leftFields.add(entryBindings.getNonSpinnakerEditor());
        entryBindings.getFleetEditor().putClientProperty(AbstractBindings.EDITOR_DISPLAY_PROPERTY_NAME, "Default Fleet");
        leftFields.add(entryBindings.getFleetEditor());
        leftFields.add(dockSlipBindings.getValueEditor());
        dockSlipBindings.getValueEditor().putClientProperty(AbstractBindings.EDITOR_DISPLAY_PROPERTY_NAME, "Dock/Slip");
        rightFields.add(skipperBindings.getNameEditor());
        skipperBindings.getNameEditor().putClientProperty(AbstractBindings.EDITOR_DISPLAY_PROPERTY_NAME, "Skipper");
        rightFields.add(skipperBindings.getClubInitialsEditor());
        rightFields.add(dayPhoneBindings.getPhoneNumberEditor());
        dayPhoneBindings.getPhoneNumberEditor().putClientProperty(AbstractBindings.EDITOR_DISPLAY_PROPERTY_NAME, "Day Phone");
        rightFields.add(evePhoneBindings.getPhoneNumberEditor());
        evePhoneBindings.getPhoneNumberEditor().putClientProperty(AbstractBindings.EDITOR_DISPLAY_PROPERTY_NAME, "Eve Phone");
        rightFields.add(cellPhoneBindings.getPhoneNumberEditor());
        cellPhoneBindings.getPhoneNumberEditor().putClientProperty(AbstractBindings.EDITOR_DISPLAY_PROPERTY_NAME, "Cell Phone");
        rightFields.add(this.emailBindings.getEmailAddressEditor());
        rightFields.add(boatBindings.getNotesEditor());
        boatBindings.getBoatNameEditor().addAutocompleteListener(this);
        boatBindings.getSailNumberEditor().addAutocompleteListener(this);
        boatBindings.getUssaClassNameEditor().addAutocompleteListener(this);
        skipperBindings.getNameEditor().addAutocompleteListener(this);
        leftFieldsPanel = new SpringCompactGridPanel(leftFields);
        rightFieldsPanel = new SpringCompactGridPanel(rightFields);
        if (result.getHandicap() == null && result.getEntry() != null) result.setHandicap(result.getEntry().getHandicap());
        resultBindings = new ResultBindings(result, "Use Default");
        resultBindings.setExceptionHandler(ErrorDialog.onlyInstance());
        List leftResultFields = new LinkedList();
        List rightResultFields = new LinkedList();
        leftResultFields.add(resultBindings.getFinishTimeEditor());
        resultBindings.getFinishTimeEditor().getTextField().addFocusListener(new FocusAdapter() {

            public void focusLost(FocusEvent evt) {
                try {
                    Date time = resultBindings.getFinishTimeEditor().getTime();
                    if (time != null) {
                        resultBindings.getFinishTimeEditor().setTime(time);
                        if ("DNC".equals(resultBindings.getDispositionEditor().getSelectedItem())) {
                            resultBindings.getDispositionEditor().setSelectedItem("OK");
                        }
                    }
                } catch (Exception x) {
                    x.printStackTrace();
                }
            }
        });
        resultBindings.getFinishPlaceEditor().setEnabled(false);
        resultBindings.getCorrectedPlaceEditor().setEnabled(false);
        resultBindings.getScoreEditor().setEnabled(false);
        leftResultFields.add(resultBindings.getDispositionEditor());
        leftResultFields.add(resultBindings.getSkipperEditor());
        leftResultFields.add(resultBindings.getElapsedTimeEditor());
        leftResultFields.add(resultBindings.getAllowanceEditor());
        leftResultFields.add(resultBindings.getCorrectedTimeEditor());
        rightResultFields.add(resultBindings.getFinishDateEditor());
        rightResultFields.add(resultBindings.getFleetEditor());
        rightResultFields.add(resultBindings.getHandicapEditor());
        rightResultFields.add(resultBindings.getFinishPlaceEditor());
        rightResultFields.add(resultBindings.getCorrectedPlaceEditor());
        rightResultFields.add(resultBindings.getScoreEditor());
        leftResultFieldsPanel = new SpringCompactGridPanel(leftResultFields);
        rightResultFieldsPanel = new SpringCompactGridPanel(rightResultFields);
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {
        generalPanel = new javax.swing.JPanel();
        generalTitledPanel = new javax.swing.JPanel();
        leftBorderPanel = new javax.swing.JPanel();
        leftPanel = leftFieldsPanel;
        rightBorderPanel = new javax.swing.JPanel();
        rightPanel = rightFieldsPanel;
        resultPanel = new javax.swing.JPanel();
        resultTitledPanel = new javax.swing.JPanel();
        leftWidgetBorderPanel = new javax.swing.JPanel();
        leftWidgetPanel = leftResultFieldsPanel;
        rightWidgetBorderPanel = new javax.swing.JPanel();
        rightWidgetPanel = rightResultFieldsPanel;
        buttonPanel = new javax.swing.JPanel();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        generalPanel.setLayout(new java.awt.BorderLayout());
        generalPanel.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(10, 10, 10, 10)));
        generalTitledPanel.setLayout(new java.awt.GridLayout(0, 2, 10, 0));
        generalTitledPanel.setBorder(new javax.swing.border.TitledBorder("General"));
        leftBorderPanel.setLayout(new java.awt.BorderLayout());
        leftBorderPanel.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(10, 10, 10, 10)));
        leftBorderPanel.add(leftPanel, java.awt.BorderLayout.NORTH);
        generalTitledPanel.add(leftBorderPanel);
        rightBorderPanel.setLayout(new java.awt.BorderLayout());
        rightBorderPanel.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(10, 10, 10, 10)));
        rightBorderPanel.add(rightPanel, java.awt.BorderLayout.NORTH);
        generalTitledPanel.add(rightBorderPanel);
        generalPanel.add(generalTitledPanel, java.awt.BorderLayout.CENTER);
        getContentPane().add(generalPanel, java.awt.BorderLayout.NORTH);
        resultPanel.setLayout(new java.awt.BorderLayout());
        resultPanel.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(1, 10, 10, 5)));
        resultTitledPanel.setLayout(new java.awt.GridLayout(0, 2));
        resultTitledPanel.setBorder(new javax.swing.border.TitledBorder("This Race"));
        leftWidgetBorderPanel.setLayout(new java.awt.BorderLayout());
        leftWidgetPanel.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(10, 10, 10, 10)));
        leftWidgetBorderPanel.add(leftWidgetPanel, java.awt.BorderLayout.NORTH);
        resultTitledPanel.add(leftWidgetBorderPanel);
        rightWidgetBorderPanel.setLayout(new java.awt.BorderLayout());
        rightWidgetPanel.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(10, 10, 10, 10)));
        rightWidgetBorderPanel.add(rightWidgetPanel, java.awt.BorderLayout.NORTH);
        resultTitledPanel.add(rightWidgetBorderPanel);
        resultPanel.add(resultTitledPanel, java.awt.BorderLayout.CENTER);
        getContentPane().add(resultPanel, java.awt.BorderLayout.CENTER);
        okButton.setText("OK");
        okButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(okButton);
        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(cancelButton);
        getContentPane().add(buttonPanel, java.awt.BorderLayout.SOUTH);
        pack();
    }

    private void doCreateOrUpdates(Result r, final Boat b, final Entry e, final List<Crewmember> crew, final List<Object> deleted) throws HibernateException {
        final Result rf1 = r;
        HibernateUtil.exec(new TransactionalLogic() {

            public void execute(Session session) throws HibernateException {
                if (rf1.getId() != null) ResultManager.onlyInstance().update(rf1);
                BoatManager.onlyInstance().createOrUpdate(b);
                List bp = new ArrayList();
                bp.addAll(b.getBoatParameters().values());
                BoatParameterManager.onlyInstance().createUpdateDelete(bp, null);
                EntryManager.onlyInstance().createOrUpdate(e);
                for (Object o : deleted) {
                    MFManager mgr = (MFManager) MFManager.lookupManager(o.getClass());
                    mgr.delete(o);
                }
                for (Crewmember cm : crew) {
                    cm.setEntry(e);
                    ContactManager.onlyInstance().createOrUpdate(cm.getContact());
                    int i = 0;
                    for (EmailAddress ea : (List<EmailAddress>) cm.getContact().getEmailAddresss()) {
                        ea.setListIndex(i++);
                        EmailAddressManager.onlyInstance().createOrUpdate(ea);
                    }
                    i = 0;
                    for (PhoneNumber pn : (List<PhoneNumber>) cm.getContact().getPhoneNumbers()) {
                        pn.setListIndex(i++);
                        PhoneNumberManager.onlyInstance().createOrUpdate(pn);
                    }
                }
                CrewmemberManager.onlyInstance().createUpdateDelete(crew, new ArrayList<Crewmember>());
            }
        });
        if (r.getId() != null) return;
        if (e.getId() == null) {
            System.out.println("Oops, entry.getId() is null!");
            return;
        }
        Race race = Framework.onlyInstance().getActiveRace();
        Result result = null;
        if (race != null) {
            try {
                for (Result rx : RaceManager.onlyInstance().getRaceResults(race)) {
                    if (e.getId().equals(rx.getEntry().getId())) {
                        result = rx;
                    }
                }
            } catch (net.sf.hibernate.HibernateException x) {
                sts.gui.ErrorDialog.handle(x);
            }
        }
        if (result == null) {
            System.out.println("Oops, result is null!");
            return;
        }
        if (r.getFinishTime() != null) result.setFinishTime(r.getFinishTime());
        if (r.getFinishDate() != null) result.setFinishDate(r.getFinishDate());
        if (r.getDisposition() != null) result.setDisposition(r.getDisposition());
        if (r.getHandicap() != null) result.setHandicap(r.getHandicap());
        if (r.getFleet() != null) result.setFleet(r.getFleet());
        final Result rf2 = result;
        HibernateUtil.exec(new TransactionalLogic() {

            public void execute(Session session) throws HibernateException {
                ResultManager.onlyInstance().update(rf2);
            }
        });
    }

    private void processPhoneNumber(PhoneNumber pn, PhoneNumberBindings pnBindings, String description, Contact skipper, List<Object> delete) throws MissingRequiredValueException, InvalidValueFormatException {
        if (pnBindings.getPhoneNumberEditor().getText().length() != 0) {
            PhoneNumber pnNew = pnBindings.getEntity();
            if (pnNew.getId() == null) {
                pnNew.setListIndex(skipper.getPhoneNumbers().size());
                skipper.getPhoneNumbers().add(pnNew);
                pnNew.setContact(skipper);
                pnNew.setDescription(description);
            }
        } else if (pn != null) delete.add(pn);
    }

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            Boat b = boatBindings.getEntity();
            Entry e = entryBindings.getEntity();
            Contact skipper = skipperBindings.getEntity();
            List<Object> delete = new ArrayList<Object>();
            if (skipper.getName() == null || skipper.getName().length() == 0) throw new MissingRequiredValueException("Skipper");
            e.setSkipper(skipper.getName());
            dockSlipBindings.getEntity();
            processPhoneNumber(day, dayPhoneBindings, "day", skipper, delete);
            processPhoneNumber(eve, evePhoneBindings, "eve", skipper, delete);
            processPhoneNumber(cell, cellPhoneBindings, "cell", skipper, delete);
            if (emailBindings.getEmailAddressEditor().getText().length() != 0) {
                EmailAddress ea = emailBindings.getEntity();
                if (!skipper.getEmailAddresss().contains(ea)) {
                    ea.setListIndex(skipper.getEmailAddresss().size());
                    skipper.getEmailAddresss().add(ea);
                    ea.setContact(skipper);
                }
            } else if (email != null) delete.add(email);
            if (creatingEntry) {
                for (Entry ex : (List<Entry>) e.getRegatta().getEntries()) {
                    if (ex.getBoat().getBoatName().equals(b.getBoatName())) throw new IllegalArgumentException(b.getBoatName() + " is already entered in this regatta.");
                }
            }
            e.setBoat(b);
            e.setRegatta(Framework.onlyInstance().getRegatta());
            Result r = resultBindings.getEntity();
            if (r.getSkipper().equals("")) r.setSkipper(null);
            doCreateOrUpdates(r, b, e, e.getCrewmembers(), delete);
            setVisible(false);
        } catch (IllegalArgumentException x) {
            JOptionPane.showMessageDialog(UnifiedEntryResultForm.this, x.getMessage());
        } catch (MissingRequiredValueException x) {
            JOptionPane.showMessageDialog(UnifiedEntryResultForm.this, "Missing required value: " + x.getMessage());
        } catch (InvalidValueFormatException x) {
            JOptionPane.showMessageDialog(UnifiedEntryResultForm.this, "Invalid format for value: " + x.getMessage());
        } catch (Exception x) {
            ErrorDialog.handle(x);
        }
    }

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {
        setVisible(false);
    }

    public static void createEntry(java.awt.Frame parent) {
        UnifiedEntryResultForm me = new UnifiedEntryResultForm(parent);
        ScreenLocationManager.configureDialog(me, 800, 650);
        kellinwood.meshi.form.FormUtils.setModalDialogVisible(me);
    }

    public static void editEntry(java.awt.Frame parent, Result result) {
        UnifiedEntryResultForm me = new UnifiedEntryResultForm(parent, result);
        ScreenLocationManager.configureDialog(me, 800, 650);
        kellinwood.meshi.form.FormUtils.setModalDialogVisible(me);
    }

    public static void main(String[] args) {
        try {
            HibernateUtil.initHibernate();
        } catch (Exception x) {
            ErrorDialog.handle(x);
        }
        UnifiedEntryResultForm me = new UnifiedEntryResultForm(new JFrame());
        me.setSize(600, 400);
        Main.center(me).setVisible(true);
        System.exit(0);
    }

    public void autocompleted(AutocompleteEvent evt) {
        if (evt.getSelected() instanceof Boat) {
            Boat boat = (Boat) evt.getSelected();
            entry.setBoat(boat);
            boatBindings.setEntity(boat);
            try {
                BoatManager.onlyInstance().getBoatBoatParameters(boat);
                BoatParameter dockSlip = (BoatParameter) boat.getBoatParameters().get("dock/slip");
                if (dockSlip != null) dockSlipBindings.setEntity(dockSlip);
            } catch (Exception x) {
                ErrorDialog.handle(x);
            }
            boolean setSkipper = skipperBindings.getNameEditor().getText() == null || "".equals(skipperBindings.getNameEditor().getText());
            try {
                List<Entry> boatEntries = HibernateUtil.find("from Entry e where e.boat = ? order by e.creation desc", boat, Hibernate.entity(Boat.class));
                if (boatEntries.size() > 0) {
                    Entry recentEntry = boatEntries.get(0);
                    entryBindings.getHandicapEditor().setText(recentEntry.getHandicap().toString());
                    if (setSkipper) {
                        HibernateUtil.reassociate(EntryManager.onlyInstance().getEntryCrewmembersReassociator(recentEntry));
                        findAndSetSkipper((List<Crewmember>) recentEntry.getCrewmembers());
                    }
                }
            } catch (Exception x) {
                sts.gui.ErrorDialog.handle(x);
            }
        } else if (evt.getSelected() instanceof UssaClass) {
            UssaClass uc = (UssaClass) evt.getSelected();
            boatBindings.getUssaClassCodeEditor().setText(uc.getUssaCode());
            boatBindings.getUssaClassNameEditor().setText(uc.getUssaName());
        } else if (evt.getSelected() instanceof Contact) {
            setSkipper((Contact) evt.getSelected());
        }
    }

    private javax.swing.JPanel buttonPanel;

    private javax.swing.JButton cancelButton;

    private javax.swing.JPanel generalPanel;

    private javax.swing.JPanel generalTitledPanel;

    private javax.swing.JPanel leftBorderPanel;

    private javax.swing.JPanel leftPanel;

    private javax.swing.JPanel leftWidgetBorderPanel;

    private javax.swing.JPanel leftWidgetPanel;

    private javax.swing.JButton okButton;

    private javax.swing.JPanel resultPanel;

    private javax.swing.JPanel resultTitledPanel;

    private javax.swing.JPanel rightBorderPanel;

    private javax.swing.JPanel rightPanel;

    private javax.swing.JPanel rightWidgetBorderPanel;

    private javax.swing.JPanel rightWidgetPanel;
}
