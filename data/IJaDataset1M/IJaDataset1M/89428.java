package de.ios.kontor.cl.company;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import de.ios.framework.gui.*;
import de.ios.framework.basic.*;
import de.ios.kontor.sv.address.co.*;
import de.ios.kontor.sv.main.co.*;
import de.ios.kontor.utils.*;

/**
 * View to enter the data for a contact person.
 *
 * @author js (Joachim Schaaf)
 * @version $Id: ContactPersonView.java,v 1.1.1.1 2004/03/24 23:01:59 nanneb Exp $
 */
public class ContactPersonView extends KontorView {

    /** Do some debugging */
    public static final boolean debug = true;

    /** The name of this View */
    public static final String VIEW_NAME = "Kontaktpersonenerfassung";

    /** The default field length */
    protected static int FIELD_LENGTH = 25;

    /** The default field length (long fields) */
    protected static int LFIELD_LENGTH = 90;

    /** Maximal length of field (see database). */
    protected static final int REMARK_MAX = 250;

    protected static String MALE;

    protected static String FEMALE;

    /** true = new contact person, false = modify c.p. */
    protected boolean isNew = true;

    /** The contact person */
    protected ContactPerson contactPerson;

    protected Label titleL;

    protected Label name1L;

    protected Label name2L;

    protected Label sexL;

    protected Label birthdayL;

    protected Label jobL;

    protected Label departmentL;

    protected Label phoneL;

    protected Label mobileL;

    protected Label faxL;

    protected Label emailL;

    protected Label wwwL;

    protected Label remarkL;

    protected StringField title = new StringField(FIELD_LENGTH);

    protected StringField name1 = new StringField(FIELD_LENGTH);

    protected StringField name2 = new StringField(FIELD_LENGTH);

    protected Choice sex = new Choice();

    protected DateField birthday = new DateField(10);

    protected StringField job = new StringField(FIELD_LENGTH);

    protected StringField department = new StringField(FIELD_LENGTH);

    protected StringField phone = new StringField(FIELD_LENGTH);

    protected StringField mobile = new StringField(FIELD_LENGTH);

    protected StringField fax = new StringField(FIELD_LENGTH);

    protected StringField email = new StringField(FIELD_LENGTH);

    protected StringField www = new StringField(FIELD_LENGTH);

    protected StringArea remark = new StringArea(4, 37);

    protected Choice titleC = new Choice();

    protected Choice jobC = new Choice();

    protected Choice departmentC = new Choice();

    protected Button modifyB;

    protected Button abortB;

    protected PositionLayout PL;

    /** The partner to which the contactperson should belong */
    protected BusinessPartner businessPartner = null;

    /** Our parent View. This is a ContactPersonsView (a View).  */
    protected KontorView parentView = null;

    /**
   * Default constructor
   */
    public ContactPersonView() {
        super(null);
        if (debug) Debug.println(Debug.INFO, this, "default constructor");
    }

    /**
   * Constructor for a new contact person
   * @param k the Kontor server
   * @param c the Partner object
   * @param pv the parent View object (needed for call back method)
   */
    public ContactPersonView(KontorSession k, BusinessPartner c, KontorView pv) throws java.rmi.RemoteException, KontorException {
        super(k);
        if (debug) Debug.println(Debug.INFO, this, "constructor...");
        businessPartner = c;
        parentView = pv;
    }

    /**
   * ContactPersonView(): constructor to modify a contact person
   * @param k the Kontor server
   * @param c the BusinessPartner object
   * @param pv the parent View object (needed for call back method)
   * @param cp an existing contact person
   */
    public ContactPersonView(KontorSession k, BusinessPartner c, KontorView pv, ContactPerson cp) throws java.rmi.RemoteException, KontorException {
        this(k, c, pv);
        if (debug) Debug.println(Debug.INFO, this, "constructor...");
        contactPerson = cp;
        isNew = false;
    }

    /**
   * "init" Applet simulation.
   */
    public void kvInit() {
        if (debug) Debug.println(Debug.INFO, this, "init()...");
        super.kvInit();
        createDialog();
    }

    /**
   * "start" Applet simulation.
   */
    public void kvStart() {
        if (debug) Debug.println(Debug.INFO, this, "start()...");
        super.kvStart();
        if (!isNew) setData(contactPerson); else initFields();
        title.requestFocus();
    }

    /**
   * "stop" Applet simulation.
   */
    public void kvStop() {
        if (debug) Debug.println(Debug.INFO, this, "stop()...");
        try {
            parentView.setData();
        } catch (Throwable t) {
            showExceptionDialog(getDesc("error"), getDesc("err_act_cp"), t, false);
        }
        super.kvStop();
    }

    /**
   * "destroy" Applet simulation.
   */
    public void kvDestroy() {
        if (debug) Debug.println(Debug.INFO, this, "destroy()...");
        super.kvDestroy();
    }

    /**
   * destroy
   */
    public void destroy() {
        if (debug) Debug.println(Debug.INFO, this, "destroy()...");
        parentView = null;
        super.destroy();
    }

    /**
   * Initialize the input fields; set some useful data.
   */
    protected void initFields() {
    }

    /**
   * Build the dialog interface
   */
    public void createDialog() {
        MALE = getDesc("male");
        FEMALE = getDesc("female");
        titleL = new Label(getDesc("title"));
        name1L = new Label(getDesc("lname"));
        name2L = new Label(getDesc("fname"));
        sexL = new Label(getDesc("sex"));
        birthdayL = new Label(getDesc("birthday"));
        jobL = new Label(getDesc("job"));
        departmentL = new Label(getDesc("department"));
        phoneL = new Label(getDesc("phone"));
        mobileL = new Label(getDesc("mobile"));
        faxL = new Label(getDesc("fax"));
        emailL = new Label(getDesc("email"));
        wwwL = new Label(getDesc("www"));
        remarkL = new Label(getDesc("remark"));
        modifyB = new Button(getDesc("add_button"));
        abortB = new Button(getDesc("cancel"));
        removeAll();
        setLayout(new BorderLayout());
        CardView cards = new CardView();
        add("Center", cards);
        Panel buttonPanel = new Panel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        add("South", buttonPanel);
        Panel contactPPanel = new Panel();
        cards.addCard(getViewName(), contactPPanel);
        contactPPanel.setLayout(new PositionLayout(10, 10, 8, 2, true));
        PL = (PositionLayout) contactPPanel.getLayout();
        contactPPanel.add("", titleL);
        contactPPanel.add("", name1L);
        contactPPanel.add("", name2L);
        contactPPanel.add("", sexL);
        contactPPanel.add("", birthdayL);
        contactPPanel.add("", jobL);
        contactPPanel.add("", departmentL);
        contactPPanel.add("", mobileL);
        contactPPanel.add("", phoneL);
        contactPPanel.add("", faxL);
        contactPPanel.add("", emailL);
        contactPPanel.add("", wwwL);
        contactPPanel.add("", remarkL);
        contactPPanel.add("", title);
        contactPPanel.add("", titleC);
        contactPPanel.add("", name1);
        contactPPanel.add("", name2);
        contactPPanel.add("", sex);
        sex.add(MALE);
        sex.add(FEMALE);
        contactPPanel.add("", birthday);
        birthday.setAutoFormat(true);
        contactPPanel.add("", job);
        contactPPanel.add("", jobC);
        contactPPanel.add("", department);
        contactPPanel.add("", departmentC);
        contactPPanel.add("", phone);
        contactPPanel.add("", fax);
        contactPPanel.add("", mobile);
        contactPPanel.add("", email);
        contactPPanel.add("", www);
        contactPPanel.add("", remark);
        titleC.add(getDesc("phd"));
        titleC.add(getDesc("prof"));
        jobC.add(getDesc("bleader"));
        jobC.add(getDesc("leader"));
        jobC.add(getDesc("user"));
        departmentC.add(getDesc("accounting"));
        departmentC.add(getDesc("managment"));
        departmentC.add(getDesc("EDV"));
        PL.defineAutoPositioning(titleL, null, null, null, null, -1, -1, PL.LEFT, PL.UP);
        PL.defineAutoPositioning(name1L, null, null, null, titleL, -1, -1, PL.LEFT, PL.NONE);
        PL.defineAutoPositioning(name2L, null, null, null, name1L, -1, -1, PL.LEFT, PL.NONE);
        PL.defineAutoPositioning(sexL, null, null, null, name2L, -1, -1, PL.LEFT, PL.NONE);
        PL.defineAutoPositioning(birthdayL, null, null, null, sexL, -1, -1, PL.LEFT, PL.NONE);
        PL.defineAutoPositioning(jobL, null, null, null, birthdayL, -1, -1, PL.LEFT, PL.NONE);
        PL.defineAutoPositioning(departmentL, null, null, null, jobL, -1, -1, PL.LEFT, PL.NONE);
        PL.defineAutoPositioning(phoneL, null, null, null, departmentL, -1, -1, PL.LEFT, PL.NONE);
        PL.defineAutoPositioning(faxL, null, null, null, phoneL, -1, -1, PL.LEFT, PL.NONE);
        PL.defineAutoPositioning(mobileL, null, null, null, faxL, -1, -1, PL.LEFT, PL.NONE);
        PL.defineAutoPositioning(emailL, null, null, null, mobileL, -1, -1, PL.LEFT, PL.NONE);
        PL.defineAutoPositioning(wwwL, null, null, null, emailL, -1, -1, PL.LEFT, PL.NONE);
        PL.defineAutoPositioning(remarkL, null, null, null, wwwL, -1, -1, PL.LEFT, PL.NONE);
        PL.defineAutoPositioning(title, titleL, null, null, null, -1, -1, PL.NONE, PL.NONE);
        PL.defineAutoPositioning(titleC, title, null, null, null, -1, -1, PL.NONE, PL.NONE);
        PL.defineAutoPositioning(name1, name1L, title, null, null, -1, -1, PL.NONE, PL.NONE);
        PL.defineAutoPositioning(name2, name2L, name1, null, null, -1, -1, PL.NONE, PL.NONE);
        PL.defineAutoPositioning(sex, sexL, name2, null, null, -1, -1, PL.NONE, PL.NONE);
        PL.defineAutoPositioning(birthday, birthdayL, sex, null, null, -1, -1, PL.NONE, PL.NONE);
        PL.defineAutoPositioning(job, jobL, birthday, null, null, -1, -1, PL.NONE, PL.NONE);
        PL.defineAutoPositioning(jobC, job, null, null, null, -1, -1, PL.NONE, PL.NONE);
        PL.defineAutoPositioning(department, departmentL, job, null, null, -1, -1, PL.NONE, PL.NONE);
        PL.defineAutoPositioning(departmentC, department, null, null, null, -1, -1, PL.NONE, PL.NONE);
        PL.defineAutoPositioning(phone, phoneL, department, null, null, -1, -1, PL.NONE, PL.NONE);
        PL.defineAutoPositioning(fax, faxL, phone, null, null, -1, -1, PL.NONE, PL.NONE);
        PL.defineAutoPositioning(mobile, mobileL, fax, null, null, -1, -1, PL.NONE, PL.NONE);
        PL.defineAutoPositioning(email, emailL, mobile, null, null, -1, -1, PL.NONE, PL.NONE);
        PL.defineAutoPositioning(www, wwwL, email, null, null, -1, -1, PL.NONE, PL.NONE);
        PL.defineAutoPositioning(remark, null, remarkL, null, null, -1, -1, PL.LEFT, PL.NONE);
        buttonPanel.add(modifyB);
        modifyB.setActionCommand("modify");
        buttonPanel.add(abortB);
        abortB.setActionCommand("abort");
        modifyB.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (debug) Debug.println(Debug.INFO, this, "actionPerformed(): " + e.getActionCommand());
                modify();
            }
        });
        abortB.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (debug) Debug.println(Debug.INFO, this, "actionPerformed(): " + e.getActionCommand());
                ContactPersonView.this.doDestroy();
            }
        });
        titleC.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                title.setValue((String) e.getItem());
            }
        });
        jobC.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                if (sex.getSelectedItem().compareTo(MALE) == 0) job.setValue((String) e.getItem()); else job.setValue((String) e.getItem() + "in");
            }
        });
        departmentC.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                department.setValue((String) e.getItem());
            }
        });
    }

    /**
   * Do the modification (store/update). The fields should be checked first.
   * @see contactPersonOK().
   */
    protected void doModification() {
        try {
            parentView.showStatus(getDesc("save_cp"));
            if (isNew) {
                businessPartner.addContactPerson(title.getValue(), name1.getValue(), name2.getValue(), new Boolean(sex.getSelectedItem() == FEMALE), birthday.getDate(), phone.getValue(), mobile.getValue(), fax.getValue(), email.getValue(), www.getValue(), job.getValue(), department.getValue(), remark.getValue());
            } else {
                getData();
                contactPerson.storeModified(businessPartner);
            }
            parentView.showStatus();
            ContactPersonView.this.doDestroy();
        } catch (KontorException e) {
            showExceptionDialog(getDesc("error"), getDesc("err_save_cp"), e, false);
        } catch (Exception e) {
            showExceptionDialog(getDesc("error"), getDesc("err_save_cp"), e, false);
        }
    }

    /**
   * Add or change a contact person.
   */
    public void modify() {
        setBusy();
        if (contactPersonOK()) {
            doModification();
        } else {
            new MessageDialog(parentFrame).alert1(getDesc("error"), getDesc("err_modify"), getDesc("close"));
        }
        setReady();
    }

    /**
   * Checks, if all contact person fields are filled.
   * @returns true, if successful.
   */
    protected boolean contactPersonOK() {
        boolean result = true;
        String remarkS = "";
        if (remark.getValue() != null) remarkS = remark.getValue().trim();
        if (remarkS.length() > REMARK_MAX) {
            if (parentView != null) parentView.showStatus(getDesc("long_remark"), true);
            if (remarkS.length() > REMARK_MAX) remarkS = remarkS.substring(1, REMARK_MAX);
        }
        remark.setValue(remarkS);
        if (name1.getValue() == null) result = false;
        return result;
    }

    /**
   * Set the data-object for this view.
   *
   * @param cp the contact person
   */
    public void setData(ContactPerson cp) {
        try {
            Person p = cp.getPerson();
            Address adr = p.getDefaultAddress();
            title.setValue(p.getTitle());
            name1.setValue(p.getName());
            name2.setValue(p.getFirstName());
            sex.select((p.getSex() == 1) ? FEMALE : MALE);
            birthday.setDate(p.getBirthday());
            phone.setValue(adr.getTelephone());
            mobile.setValue(adr.getMobile());
            fax.setValue(adr.getFax());
            email.setValue(adr.getEmail());
            www.setValue(adr.getWWW());
            job.setValue(cp.getPosition());
            department.setValue(cp.getDepartment());
            remark.setValue(p.getRemark());
        } catch (Throwable t) {
            showExceptionDialog(getDesc("error"), getDesc("err_acc_person"), t, false);
        }
    }

    /**
   * Set the data-object for this view.
   */
    public void setData() {
        try {
            if (debug) Debug.println(Debug.INFO, this, "setData()...");
            throw new KontorException("getData: not implemented!");
        } catch (KontorException e) {
            showExceptionDialog("KontorException", "not implemented", e, false);
        }
    }

    /**
   * Get the data-object for this view; stores the entered 
   * data in the variable "contactPerson".
   */
    public void getData() {
        try {
            if (debug) Debug.println(Debug.INFO, this, "getData()...");
            contactPerson.setPosition(job.getValue());
            contactPerson.setDepartment(department.getValue());
            Person p = contactPerson.getPerson();
            p.setTitle(title.getValue());
            p.setName(name1.getValue());
            p.setFirstName(name2.getValue());
            p.setSex((sex.getSelectedItem().compareTo(MALE) == 0) ? 0 : 1);
            p.setBirthday(birthday.getDate());
            p.setRemark(remark.getValue());
            Address adr = p.getDefaultAddress();
            adr.setTelephone(phone.getValue());
            adr.setFax(fax.getValue());
            adr.setMobile(mobile.getValue());
            adr.setEmail(email.getValue());
            adr.setWWW(www.getValue());
            businessPartner = (BusinessPartner) p;
        } catch (KontorException e) {
            showExceptionDialog(getDesc("kontor_exc"), getDesc("err_acc_cp"), e, false);
        } catch (java.rmi.RemoteException e) {
            showExceptionDialog(getDesc("remote_exc"), getDesc("err_acc_cp"), e, false);
        }
    }

    /**
   * @return the name of the this View
   */
    public String getViewName() {
        return getDesc("contactperson_viewname");
    }
}
