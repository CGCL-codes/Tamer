package org.jabusuite.webclient.admin.user;

import javax.naming.NamingException;
import nextapp.echo2.app.ContentPane;
import org.jabusuite.webclient.controls.JbsExtent;
import org.jabusuite.webclient.controls.container.JbsGrid;
import nextapp.echo2.app.Insets;
import nextapp.echo2.app.Label;
import nextapp.echo2.app.event.ActionEvent;
import nextapp.echo2.app.event.ActionListener;
import org.jabusuite.core.users.JbsUser;
import org.jabusuite.core.users.session.JbsUsersRemote;
import org.jabusuite.core.utils.JbsBaseObject;
import org.jabusuite.webclient.admin.company.CompanyEditor;
import org.jabusuite.webclient.admin.user.groups.GroupCombo;
import org.jabusuite.webclient.admin.user.groups.GroupEditor;
import org.jabusuite.webclient.admin.user.groups.PnUserGroupList;
import org.jabusuite.webclient.controls.JbsButton;
import org.jabusuite.webclient.controls.JbsTextField;
import org.jabusuite.webclient.dataediting.DlgState;
import org.jabusuite.webclient.dataediting.PnEditJbsObject;
import org.jabusuite.webclient.datalist.FmJbsBaseObjectList;
import org.jabusuite.client.utils.ClientTools;
import org.jabusuite.webclient.main.JbsL10N;
import java.util.LinkedHashSet;
import org.jabusuite.core.companies.JbsCompany;
import org.jabusuite.core.users.EJbsUser;
import org.jabusuite.core.utils.EJbsObject;
import org.jabusuite.logging.Logger;
import org.jabusuite.tools.text.StringUtils;
import org.jabusuite.webclient.address.contact.JbsContactSelectField;
import org.jabusuite.webclient.controls.JbsCheckbox;
import org.jabusuite.webclient.main.ClientGlobals;
import org.jabusuite.webclient.modules.SelModule;

/**
 * Edit-Panel for <code>JbsUser</code>
 * @author hilwers
 * @date 11.02.2007
 *
 */
public class PnUserEdit extends PnEditJbsObject {

    private static final long serialVersionUID = -4472129789558100195L;

    Logger logger = Logger.getLogger(PnUserEdit.class);

    protected JbsTextField txUserName;

    protected JbsTextField txAddInfo;

    protected GroupEditor groupEditor;

    protected JbsTextField txPassword;

    protected GroupCombo cbbMainGroup;

    protected CompanyEditor companyEditor;

    protected JbsContactSelectField contactSelectField;

    protected JbsButton btnAddGroup, btnDeleteGroup;

    protected JbsCheckbox cbCanChangePassword;

    protected SelModule selStartModule;

    public PnUserEdit() {
        super(DlgState.dsInsert);
        this.setUser(new JbsUser());
    }

    public PnUserEdit(DlgState state, JbsUser user) {
        super(state);
        this.setUser(user);
    }

    @Override
    protected void createComponents() {
        txUserName = new JbsTextField();
        txAddInfo = new JbsTextField();
        txPassword = new JbsTextField();
        cbCanChangePassword = new JbsCheckbox();
        groupEditor = new GroupEditor();
        cbbMainGroup = new GroupCombo();
        companyEditor = new CompanyEditor();
        btnAddGroup = new JbsButton("+");
        btnAddGroup.addActionListener(new ActionListener() {

            private static final long serialVersionUID = 1L;

            public void actionPerformed(ActionEvent arg0) {
                FmJbsBaseObjectList fmSelectGroup = new FmJbsBaseObjectList(JbsL10N.getString("JbsUserGroup.selectGroup"));
                PnUserGroupList pnUserGroupList = new PnUserGroupList();
                pnUserGroupList.setToolPaneVisible(ClientGlobals.getUser().isRoot());
                fmSelectGroup.setPnList(pnUserGroupList);
                fmSelectGroup.showForm();
                fmSelectGroup.addActionListener(new ActionListener() {

                    private static final long serialVersionUID = 1L;

                    public void actionPerformed(ActionEvent arg0) {
                        System.out.println(arg0.getActionCommand());
                    }
                });
            }
        });
        btnDeleteGroup = new JbsButton("-");
        contactSelectField = new JbsContactSelectField();
        selStartModule = new SelModule();
    }

    @Override
    protected void getControlData() {
        if (this.getUser() != null) {
            this.getUser().setUserName(this.txUserName.getText());
            this.getUser().setAddInfo(this.txAddInfo.getText());
            this.getUser().setMainGroup(this.cbbMainGroup.getSelectedGroup());
            this.getUser().setGroups(this.groupEditor.getUserGroups());
            this.getUser().setCompanies(this.companyEditor.getCompanies());
            this.getUser().setContact(this.contactSelectField.getSelectedContact());
            if (!this.txPassword.getText().trim().equals("")) {
                this.getUser().setPassword(StringUtils.encodeMD5(this.txPassword.getText()));
            }
            this.getUser().setCanChangePassword(this.cbCanChangePassword.isSelected());
            this.getUser().setStartModule(this.selStartModule.getSelectedModule());
        }
    }

    @Override
    protected void initPanel() {
        ContentPane cpStdData = new ContentPane();
        JbsGrid grdMain = new JbsGrid(2);
        grdMain.setInsets(new Insets(5, 5));
        grdMain.setColumnWidth(0, new JbsExtent(300));
        grdMain.add(new Label(JbsL10N.getString("JbsUser.userName")));
        grdMain.add(txUserName);
        grdMain.add(new Label(JbsL10N.getString("JbsUser.password")));
        grdMain.add(txPassword);
        grdMain.add(new Label(JbsL10N.getString("JbsUser.addInfo")));
        grdMain.add(txAddInfo);
        grdMain.add(new Label(JbsL10N.getString("JbsUser.mainGroup")));
        grdMain.add(cbbMainGroup);
        grdMain.add(new Label(JbsL10N.getString("JbsUser.groupList")));
        grdMain.add(groupEditor);
        if (ClientGlobals.getUser().isRoot()) {
            grdMain.add(new Label(JbsL10N.getString("JbsUser.companyList")));
            grdMain.add(companyEditor);
        }
        grdMain.add(new Label(JbsL10N.getString("JbsUser.contact")));
        grdMain.add(this.contactSelectField);
        grdMain.add(new Label(JbsL10N.getString("JbsUser.canChangePassword")));
        grdMain.add(this.cbCanChangePassword);
        grdMain.add(new Label(JbsL10N.getString("JbsUser.startModule")));
        grdMain.add(this.selStartModule);
        cpStdData.add(grdMain);
        this.add(cpStdData);
    }

    @Override
    protected void setControlData() {
        if (this.getUser() != null) {
            this.txUserName.setText(this.getUser().getUserName());
            this.txAddInfo.setText(this.getUser().getAddInfo());
            this.groupEditor.setUserGroups(this.getUser().getGroups());
            this.cbbMainGroup.selectGroup(this.getUser().getMainGroup());
            this.txPassword.setText("");
            this.companyEditor.setCompanies(this.getUser().getCompanies());
            this.contactSelectField.setSelectedContact(this.getUser().getContact());
            this.cbCanChangePassword.setSelected(this.getUser().isCanChangePassword());
            this.selStartModule.setSelectedModule(this.getUser().getStartModule());
        }
    }

    public JbsUser fetchUser(long userId) {
        System.out.println("Fetching user " + String.valueOf(userId));
        JbsUser user;
        try {
            JbsUsersRemote jbsUsers = (JbsUsersRemote) ClientTools.getRemoteBean(JbsUsersRemote.class);
            user = jbsUsers.findUser(userId);
        } catch (Exception e) {
            user = null;
            System.out.println("User with id " + String.valueOf(userId) + " not found.");
        }
        return user;
    }

    @Override
    public void doSave() throws EJbsObject {
        System.out.println("Saving data...");
        try {
            JbsUsersRemote jbsUsers = (JbsUsersRemote) ClientTools.getRemoteBean(JbsUsersRemote.class);
            super.doSave();
            if ((this.getDlgState() == DlgState.dsInsert) && (jbsUsers.findUser(this.getUser().getUserName()) != null)) {
                throw new EJbsUser(EJbsUser.ET_EXISTS);
            }
            if (this.getUser().getCompanies() == null) this.getUser().setCompanies(new LinkedHashSet<JbsCompany>());
            if (this.getUser().getCompanies().isEmpty()) this.getUser().getCompanies().add(ClientGlobals.getCompany());
            if (this.getDlgState() == DlgState.dsInsert) {
                System.out.println("Adding new entity " + this.getUser().getId() + ".");
                jbsUsers.createUser(this.getUser());
            } else if (this.getDlgState() == DlgState.dsEdit) {
                System.out.println("Saving existing entity " + this.getUser().getId() + ".");
                jbsUsers.updateUser(this.getUser());
            }
            System.out.println("Entity saved.");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Set's the user that is edited in this panel
     * @param user
     */
    public void setUser(JbsUser user) {
        this.setJbsBaseObject(user);
    }

    public JbsUser getUser() {
        return (JbsUser) this.getJbsBaseObject();
    }

    @Override
    public void setJbsBaseObject(JbsBaseObject jbsBaseObject) {
        JbsUser user = null;
        if (jbsBaseObject != null) {
            user = this.fetchUser(jbsBaseObject.getId());
        }
        if (user != null) {
            super.setJbsBaseObject(user);
        } else {
            super.setJbsBaseObject(jbsBaseObject);
        }
    }
}
