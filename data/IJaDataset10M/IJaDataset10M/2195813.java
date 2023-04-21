package com.velocityme.client;

import com.velocityme.client.utility.StatelessRemoteClientSessionUtil;
import com.velocityme.entity.PermissionBean;
import com.velocityme.interfaces.*;
import com.velocityme.session.KeySessionBean;
import com.velocityme.utility.*;
import com.velocityme.valueobjects.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.ejb.CreateException;
import javax.naming.NamingException;

/**
 *
 * @author  Robert
 */
public class SetupDatabaseConstructionExample {

    private KeySession m_key;

    /** Creates a new instance of SetupDatabaseConstructionExample */
    public SetupDatabaseConstructionExample() {
    }

    public boolean login(String password) throws RemoteException, NamingException, CreateException {
        m_key = KeySessionUtil.getHome().create();
        return m_key.login("default", password, getClass().getPackage().getImplementationVersion(), "SetupDatabaseConstructionExample") == KeySessionBean.LOGIN_SUCCESS;
    }

    public void setupContactDetailTypes() throws RemoteException, InvalidKeyException, NamingException, CreateException {
        Collection contactDetailTypes = new ArrayList();
        ContactDetailTypeValue telephoneValue = new ContactDetailTypeValue();
        NotificationMechanismValue[] notificationMechanismValues = StatelessRemoteClientSessionUtil.getSession().getAllNotificationMechanismValues(m_key);
        telephoneValue.setName("Telephone");
        telephoneValue.setNotificationMechanismValue(notificationMechanismValues[0]);
        telephoneValue.setShowType(Boolean.TRUE);
        telephoneValue.setSequenceNumber(new Integer(0));
        contactDetailTypes.add(telephoneValue);
        ContactDetailTypeValue mobileValue = new ContactDetailTypeValue();
        mobileValue.setName("Mobile");
        mobileValue.setNotificationMechanismValue(notificationMechanismValues[2]);
        mobileValue.setShowType(Boolean.TRUE);
        mobileValue.setSequenceNumber(new Integer(1));
        contactDetailTypes.add(mobileValue);
        ContactDetailTypeValue address1Value = new ContactDetailTypeValue();
        address1Value.setName("Address");
        address1Value.setNotificationMechanismValue(notificationMechanismValues[0]);
        address1Value.setShowType(Boolean.TRUE);
        address1Value.setSequenceNumber(new Integer(2));
        contactDetailTypes.add(address1Value);
        ContactDetailTypeValue address2Value = new ContactDetailTypeValue();
        address2Value.setName("Address line 2");
        address2Value.setNotificationMechanismValue(notificationMechanismValues[0]);
        address2Value.setShowType(Boolean.FALSE);
        address2Value.setSequenceNumber(new Integer(3));
        contactDetailTypes.add(address2Value);
        ContactDetailTypeValue address3Value = new ContactDetailTypeValue();
        address3Value.setName("Address line 3");
        address3Value.setNotificationMechanismValue(notificationMechanismValues[0]);
        address3Value.setShowType(Boolean.FALSE);
        address3Value.setSequenceNumber(new Integer(4));
        contactDetailTypes.add(address3Value);
        ContactDetailTypeValue addressPCValue = new ContactDetailTypeValue();
        addressPCValue.setName("Address postal code");
        addressPCValue.setNotificationMechanismValue(notificationMechanismValues[0]);
        addressPCValue.setShowType(Boolean.FALSE);
        addressPCValue.setSequenceNumber(new Integer(5));
        contactDetailTypes.add(addressPCValue);
        ContactDetailTypeValue addressCountryValue = new ContactDetailTypeValue();
        addressCountryValue.setName("Address country");
        addressCountryValue.setNotificationMechanismValue(notificationMechanismValues[0]);
        addressCountryValue.setShowType(Boolean.FALSE);
        addressCountryValue.setSequenceNumber(new Integer(6));
        contactDetailTypes.add(addressCountryValue);
        StatelessRemoteClientSessionUtil.getSession().setContactDetailTypeValues(m_key, contactDetailTypes);
    }

    DepartmentValue buildingDeptValue;

    DepartmentValue plumberDeptValue;

    DepartmentValue roofingDeptValue;

    public void setupOrganisations() throws RemoteException, InvalidKeyException, PermissionDeniedException, NamingException, CreateException {
        Integer id = new Integer(0);
        NodePK organisationNodePK = StatelessRemoteClientSessionUtil.getSession().findSystemNodeByName(m_key, "Setup Organisations");
        OrganisationValue buildingValue = new OrganisationValue(id);
        buildingValue.setContactableValue(new ContactableValue(id, "Organisation"));
        buildingValue.getContactableValue().setNodeValue(new NodeValue(id, "Happy Homes Construction CC", "Master builders of high repute", "Contactable", "Organisation"));
        OrganisationPK buildingPK = StatelessRemoteClientSessionUtil.getSession().organisationCreate(m_key, organisationNodePK, buildingValue);
        buildingDeptValue = new DepartmentValue();
        buildingDeptValue.setContactableValue(new ContactableValue(id, "Department"));
        buildingDeptValue.getContactableValue().setNodeValue(new NodeValue(id, "New Installations", "", "Contactable", "Department"));
        DepartmentPK buildingDeptPK = StatelessRemoteClientSessionUtil.getSession().departmentCreate(m_key, new NodePK(buildingPK.getOrganisationId()), buildingDeptValue);
        buildingDeptValue = StatelessRemoteClientSessionUtil.getSession().getDepartmentValue(m_key, buildingDeptPK);
        OrganisationValue plumberValue = new OrganisationValue(id);
        plumberValue.setContactableValue(new ContactableValue(id, "Organisation"));
        plumberValue.getContactableValue().setNodeValue(new NodeValue(id, "Plumlite c.c.", "Immediate Emergency response", "Contactable", "Organisation"));
        OrganisationPK plumberPK = StatelessRemoteClientSessionUtil.getSession().organisationCreate(m_key, organisationNodePK, plumberValue);
        plumberDeptValue = new DepartmentValue();
        plumberDeptValue.setContactableValue(new ContactableValue(id, "Department"));
        plumberDeptValue.getContactableValue().setNodeValue(new NodeValue(id, "New Installations", "", "Contactable", "Department"));
        DepartmentPK plumberDeptPK = StatelessRemoteClientSessionUtil.getSession().departmentCreate(m_key, new NodePK(plumberPK.getOrganisationId()), plumberDeptValue);
        plumberDeptValue = StatelessRemoteClientSessionUtil.getSession().getDepartmentValue(m_key, plumberDeptPK);
        OrganisationValue roofingValue = new OrganisationValue(id);
        roofingValue.setContactableValue(new ContactableValue(id, "Organisation"));
        roofingValue.getContactableValue().setNodeValue(new NodeValue(id, "Levenbach Roofing", "", "Contactable", "Organisation"));
        OrganisationPK roofingPK = StatelessRemoteClientSessionUtil.getSession().organisationCreate(m_key, organisationNodePK, roofingValue);
        roofingDeptValue = new DepartmentValue();
        roofingDeptValue.setContactableValue(new ContactableValue(id, "Department"));
        roofingDeptValue.getContactableValue().setNodeValue(new NodeValue(id, "Domestic", "", "Contactable", "Department"));
        DepartmentPK roofingDeptPK = StatelessRemoteClientSessionUtil.getSession().departmentCreate(m_key, new NodePK(roofingPK.getOrganisationId()), roofingDeptValue);
        roofingDeptValue = StatelessRemoteClientSessionUtil.getSession().getDepartmentValue(m_key, roofingDeptPK);
    }

    PersonValue barneyBuilderPersonValue;

    PersonValue peterPlumberPersonValue;

    PersonValue derekDeveloperPersonValue;

    UserValue developerUserValue;

    UserValue buildingUserValue;

    UserValue plumberUserValue;

    public void setupPersons() throws RemoteException, InvalidKeyException, PermissionDeniedException, NamingException, CreateException, DuplicateUserException {
        Integer id = new Integer(0);
        NodePK userNodePK = StatelessRemoteClientSessionUtil.getSession().findSystemNodeByName(m_key, "Setup Users");
        NodePK defaultDepartmentNodePK = null;
        DepartmentValue defaultDepartmentValue = null;
        Collection departmentValues = StatelessRemoteClientSessionUtil.getSession().getAllDepartmentValues(m_key);
        Iterator i = departmentValues.iterator();
        while (i.hasNext()) {
            DepartmentValue departmentValue = (DepartmentValue) i.next();
            if (departmentValue.getContactableValue().getNodeValue().getName().equals("Default Department")) {
                defaultDepartmentValue = departmentValue;
                defaultDepartmentNodePK = departmentValue.getContactableValue().getNodeValue().getPrimaryKey();
            }
        }
        derekDeveloperPersonValue = new PersonValue(id, "Derek", "Developer", "");
        derekDeveloperPersonValue.setContactableValue(new ContactableValue(id, "Person"));
        derekDeveloperPersonValue.getContactableValue().setNodeValue(new NodeValue(id, "Derek Developer", "", "Contactable", "Person"));
        PersonPK developerPK = StatelessRemoteClientSessionUtil.getSession().personCreate(m_key, defaultDepartmentNodePK, derekDeveloperPersonValue);
        derekDeveloperPersonValue = StatelessRemoteClientSessionUtil.getSession().getPersonValue(m_key, developerPK);
        developerUserValue = new UserValue(id, "derek", Boolean.TRUE, Boolean.TRUE);
        developerUserValue.setPersonValue(derekDeveloperPersonValue);
        developerUserValue.setNodeValue(new NodeValue(id, "derek", "", "User", "User"));
        UserPK developerUserPK = StatelessRemoteClientSessionUtil.getSession().userCreate(m_key, userNodePK, developerUserValue, "derek");
        developerUserValue = StatelessRemoteClientSessionUtil.getSession().getUserValue(m_key, developerUserPK);
        barneyBuilderPersonValue = new PersonValue(id, "Barney", "Builder", "");
        barneyBuilderPersonValue.setContactableValue(new ContactableValue(id, "Person"));
        barneyBuilderPersonValue.getContactableValue().setNodeValue(new NodeValue(id, "Barney Builder", "", "Contactable", "Person"));
        PersonPK buildingPK = StatelessRemoteClientSessionUtil.getSession().personCreate(m_key, buildingDeptValue.getContactableValue().getNodeValue().getPrimaryKey(), barneyBuilderPersonValue);
        barneyBuilderPersonValue = StatelessRemoteClientSessionUtil.getSession().getPersonValue(m_key, buildingPK);
        buildingUserValue = new UserValue(id, "barney", Boolean.TRUE, Boolean.TRUE);
        buildingUserValue.setPersonValue(barneyBuilderPersonValue);
        buildingUserValue.setNodeValue(new NodeValue(id, "barney", "", "User", "User"));
        UserPK buildingUserPK = StatelessRemoteClientSessionUtil.getSession().userCreate(m_key, userNodePK, buildingUserValue, "barney");
        buildingUserValue = StatelessRemoteClientSessionUtil.getSession().getUserValue(m_key, buildingUserPK);
        peterPlumberPersonValue = new PersonValue(id, "Peter", "Plumber", "");
        peterPlumberPersonValue.setContactableValue(new ContactableValue(id, "Person"));
        peterPlumberPersonValue.getContactableValue().setNodeValue(new NodeValue(id, "Peter Plumber", "", "Contactable", "Person"));
        PersonPK plumberPK = StatelessRemoteClientSessionUtil.getSession().personCreate(m_key, plumberDeptValue.getContactableValue().getNodeValue().getPrimaryKey(), peterPlumberPersonValue);
        peterPlumberPersonValue = StatelessRemoteClientSessionUtil.getSession().getPersonValue(m_key, plumberPK);
        plumberUserValue = new UserValue(id, "peter", Boolean.TRUE, Boolean.TRUE);
        plumberUserValue.setPersonValue(peterPlumberPersonValue);
        plumberUserValue.setNodeValue(new NodeValue(id, "peter", "", "User", "User"));
        UserPK plumberUserPK = StatelessRemoteClientSessionUtil.getSession().userCreate(m_key, userNodePK, plumberUserValue, "peter");
        plumberUserValue = StatelessRemoteClientSessionUtil.getSession().getUserValue(m_key, plumberUserPK);
    }

    RoleValue projectManagerRoleValue;

    RoleValue ownerRoleValue;

    RoleValue contractorRoleValue;

    public void setupRoles() throws RemoteException, InvalidKeyException, PermissionDeniedException, NamingException, CreateException {
        Integer id = new Integer(0);
        NodePK roleNodePK = StatelessRemoteClientSessionUtil.getSession().findSystemNodeByName(m_key, "Setup Roles");
        Set projPermissions = new HashSet();
        projPermissions.add(new PermissionPK(new Integer(PermissionBean.ACTION_CREATE)));
        projPermissions.add(new PermissionPK(new Integer(PermissionBean.ACTION_EDIT)));
        projPermissions.add(new PermissionPK(new Integer(PermissionBean.NODE_CREATE)));
        projPermissions.add(new PermissionPK(new Integer(PermissionBean.NODE_EDIT)));
        projPermissions.add(new PermissionPK(new Integer(PermissionBean.NODE_DELETE)));
        projPermissions.add(new PermissionPK(new Integer(PermissionBean.NODE_VIEW)));
        projPermissions.add(new PermissionPK(new Integer(PermissionBean.PROJECT_CREATE)));
        projPermissions.add(new PermissionPK(new Integer(PermissionBean.PROJECT_EDIT)));
        projPermissions.add(new PermissionPK(new Integer(PermissionBean.ISSUE_CREATE)));
        projPermissions.add(new PermissionPK(new Integer(PermissionBean.ISSUE_EDIT)));
        RoleValue projValue = new RoleValue(id, Boolean.TRUE);
        projValue.setNodeValue(new NodeValue(id, "Project Manager", "The guy responsible for making it all happen", "Role", "Role"));
        RolePK projectManagerRolePK = StatelessRemoteClientSessionUtil.getSession().roleCreate(m_key, roleNodePK, projValue, projPermissions);
        projectManagerRoleValue = StatelessRemoteClientSessionUtil.getSession().getRoleValue(m_key, projectManagerRolePK);
        Set ownerPermissions = new HashSet();
        ownerPermissions.add(new PermissionPK(new Integer(PermissionBean.ISSUE_CREATE)));
        ownerPermissions.add(new PermissionPK(new Integer(PermissionBean.DEPARTMENT_CREATE)));
        ownerPermissions.add(new PermissionPK(new Integer(PermissionBean.DEPARTMENT_EDIT)));
        ownerPermissions.add(new PermissionPK(new Integer(PermissionBean.ORGANISATION_CREATE)));
        ownerPermissions.add(new PermissionPK(new Integer(PermissionBean.ORGANISATION_EDIT)));
        ownerPermissions.add(new PermissionPK(new Integer(PermissionBean.PERSON_CREATE)));
        ownerPermissions.add(new PermissionPK(new Integer(PermissionBean.PERSON_EDIT)));
        ownerPermissions.add(new PermissionPK(new Integer(PermissionBean.USER_CREATE)));
        ownerPermissions.add(new PermissionPK(new Integer(PermissionBean.USER_EDIT)));
        ownerPermissions.add(new PermissionPK(new Integer(PermissionBean.USER_CHANGE_PASSWORD)));
        ownerPermissions.add(new PermissionPK(new Integer(PermissionBean.GROUP_CREATE)));
        ownerPermissions.add(new PermissionPK(new Integer(PermissionBean.GROUP_EDIT)));
        ownerPermissions.add(new PermissionPK(new Integer(PermissionBean.CONTACT_DETAIL_TYPE_EDIT)));
        ownerPermissions.add(new PermissionPK(new Integer(PermissionBean.USER_ACCESS_CREATE)));
        ownerPermissions.add(new PermissionPK(new Integer(PermissionBean.USER_ACCESS_DELETE)));
        ownerPermissions.add(new PermissionPK(new Integer(PermissionBean.GROUP_ACCESS_CREATE)));
        ownerPermissions.add(new PermissionPK(new Integer(PermissionBean.GROUP_ACCESS_DELETE)));
        ownerPermissions.add(new PermissionPK(new Integer(PermissionBean.NODE_VIEW)));
        RoleValue ownerValue = new RoleValue(id, Boolean.TRUE);
        ownerValue.setNodeValue(new NodeValue(id, "Owner", "The person who is paying for it all", "Role", "Role"));
        RolePK ownerRolePK = StatelessRemoteClientSessionUtil.getSession().roleCreate(m_key, roleNodePK, ownerValue, ownerPermissions);
        ownerRoleValue = StatelessRemoteClientSessionUtil.getSession().getRoleValue(m_key, ownerRolePK);
        Set contractorPermissions = new HashSet();
        contractorPermissions.add(new PermissionPK(new Integer(PermissionBean.ISSUE_CREATE)));
        contractorPermissions.add(new PermissionPK(new Integer(PermissionBean.NODE_VIEW)));
        RoleValue contractorValue = new RoleValue(id, Boolean.TRUE);
        contractorValue.setNodeValue(new NodeValue(id, "Contractor", "Someone who is brought in to subcontract a task", "Role", "Role"));
        RolePK contractorRolePK = StatelessRemoteClientSessionUtil.getSession().roleCreate(m_key, roleNodePK, contractorValue, contractorPermissions);
        contractorRoleValue = StatelessRemoteClientSessionUtil.getSession().getRoleValue(m_key, contractorRolePK);
    }

    TaskStateMachineValue projectTaskTypeValue;

    public void setupProjectTaskType() throws RemoteException, InvalidKeyException, PermissionDeniedException, NamingException, CreateException {
        Iterator i = StatelessRemoteClientSessionUtil.getSession().getAllTaskStateMachineValues(m_key).iterator();
        TaskStateMachinePK projectTaskTypePK = null;
        while (i.hasNext()) {
            TaskStateMachineValue taskStateMachineValue = (TaskStateMachineValue) i.next();
            if (taskStateMachineValue.getNodeValue().getName().equals("Basic Task")) projectTaskTypePK = taskStateMachineValue.getPrimaryKey();
        }
        projectTaskTypeValue = StatelessRemoteClientSessionUtil.getSession().getTaskStateMachineValue(m_key, projectTaskTypePK);
    }

    ProjectPK buildHouseProjectPK;

    public void setupProject() throws RemoteException, InvalidKeyException, PermissionDeniedException, NamingException, CreateException {
        Integer id = new Integer(0);
        NodePK projectsNodePK = StatelessRemoteClientSessionUtil.getSession().findSystemNodeByName(m_key, "Project");
        ProjectValue projectValue = new ProjectValue(id);
        projectValue.setSponsorPersonValue(derekDeveloperPersonValue);
        projectValue.setPrimaryContactPersonValue(barneyBuilderPersonValue);
        projectValue.setTaskValue(new TaskValue(id, "Project", new Integer(0), new Byte((byte) 0), new Integer(0), new Integer(0), new Date(0), new Date(0), null, null));
        projectValue.getTaskValue().setOwnerContactableValue(derekDeveloperPersonValue.getContactableValue());
        projectValue.getTaskValue().setTaskStateMachineValue(projectTaskTypeValue);
        projectValue.getTaskValue().setNodeValue(new NodeValue(id, "Build House", "", "Task", "Project"));
        buildHouseProjectPK = StatelessRemoteClientSessionUtil.getSession().projectCreate(m_key, projectsNodePK, projectValue);
    }

    public void setupUserAccesses() throws RemoteException, NamingException, InvalidKeyException, CreateException, PermissionDeniedException {
        NodeValue projectNodeValue = StatelessRemoteClientSessionUtil.getSession().getNodeValue(m_key, new NodePK(buildHouseProjectPK.getProjectId()));
        StatelessRemoteClientSessionUtil.getSession().userAccessCreate(m_key, projectNodeValue.getPrimaryKey(), developerUserValue.getPrimaryKey(), ownerRoleValue.getPrimaryKey());
        StatelessRemoteClientSessionUtil.getSession().userAccessCreate(m_key, projectNodeValue.getPrimaryKey(), buildingUserValue.getPrimaryKey(), projectManagerRoleValue.getPrimaryKey());
        StatelessRemoteClientSessionUtil.getSession().userAccessCreate(m_key, projectNodeValue.getPrimaryKey(), plumberUserValue.getPrimaryKey(), contractorRoleValue.getPrimaryKey());
    }

    ActionPK planningActionPK;

    ActionPK buildActionPK;

    ActionPK finishingActionPK;

    ActionPK checkingActionPK;

    ActionPK followupActionPK;

    ActionPK findArchitectActionPK;

    ActionPK designHouseActionPK;

    ActionPK planningApprovalActionPK;

    ActionPK structureActionPK;

    ActionPK interiorActionPK;

    ActionPK gardenActionPK;

    ActionPK paintingActionPK;

    ActionPK carpetsActionPK;

    ActionPK foundationsActionPK;

    ActionPK wallsActionPK;

    ActionPK roofActionPK;

    ActionPK electricsActionPK;

    ActionPK plumbingActionPK;

    ActionPK kitchenActionPK;

    ActionPK bathroomActionPK;

    ActionPK cupboardsActionPK;

    ActionPK clearingPropertyActionPK;

    ActionPK markingActionPK;

    ActionPK throwingConcreteActionPK;

    ActionPK timberConstructionActionPK;

    ActionPK layTilesActionPK;

    public void setupActions() throws RemoteException, NamingException, InvalidKeyException, CreateException, PermissionDeniedException {
        Integer id = new Integer(0);
        ActionValue actionValue = new ActionValue(id);
        actionValue.setTaskValue(new TaskValue(id, "Action", new Integer(0), new Byte((byte) 0), new Integer(0), new Integer(0), new Date(0), new Date(0), null, null));
        actionValue.getTaskValue().setOwnerContactableValue(derekDeveloperPersonValue.getContactableValue());
        actionValue.getTaskValue().setTaskStateMachineValue(projectTaskTypeValue);
        actionValue.getTaskValue().setNodeValue(new NodeValue(id, "Planning", "", "Task", "Action"));
        planningActionPK = StatelessRemoteClientSessionUtil.getSession().actionCreate(m_key, new NodePK(buildHouseProjectPK.getProjectId()), actionValue);
        actionValue.getTaskValue().setNodeValue(new NodeValue(id, "Build", "", "Task", "Action"));
        buildActionPK = StatelessRemoteClientSessionUtil.getSession().actionCreate(m_key, new NodePK(buildHouseProjectPK.getProjectId()), actionValue);
        actionValue.getTaskValue().setNodeValue(new NodeValue(id, "Finishing", "", "Task", "Action"));
        finishingActionPK = StatelessRemoteClientSessionUtil.getSession().actionCreate(m_key, new NodePK(buildHouseProjectPK.getProjectId()), actionValue);
        actionValue.getTaskValue().setNodeValue(new NodeValue(id, "Checking", "", "Task", "Action"));
        checkingActionPK = StatelessRemoteClientSessionUtil.getSession().actionCreate(m_key, new NodePK(buildHouseProjectPK.getProjectId()), actionValue);
        actionValue.getTaskValue().setNodeValue(new NodeValue(id, "Followup", "", "Task", "Action"));
        followupActionPK = StatelessRemoteClientSessionUtil.getSession().actionCreate(m_key, new NodePK(buildHouseProjectPK.getProjectId()), actionValue);
        actionValue.getTaskValue().setNodeValue(new NodeValue(id, "Find architect", "", "Task", "Action"));
        findArchitectActionPK = StatelessRemoteClientSessionUtil.getSession().actionCreate(m_key, new NodePK(planningActionPK.getActionId()), actionValue);
        actionValue.getTaskValue().setNodeValue(new NodeValue(id, "Design house", "", "Task", "Action"));
        designHouseActionPK = StatelessRemoteClientSessionUtil.getSession().actionCreate(m_key, new NodePK(planningActionPK.getActionId()), actionValue);
        actionValue.getTaskValue().setNodeValue(new NodeValue(id, "Planning approval", "", "Task", "Action"));
        planningApprovalActionPK = StatelessRemoteClientSessionUtil.getSession().actionCreate(m_key, new NodePK(planningActionPK.getActionId()), actionValue);
        actionValue.getTaskValue().setNodeValue(new NodeValue(id, "Structure", "", "Task", "Action"));
        structureActionPK = StatelessRemoteClientSessionUtil.getSession().actionCreate(m_key, new NodePK(buildActionPK.getActionId()), actionValue);
        actionValue.getTaskValue().setNodeValue(new NodeValue(id, "Interior", "", "Task", "Action"));
        interiorActionPK = StatelessRemoteClientSessionUtil.getSession().actionCreate(m_key, new NodePK(buildActionPK.getActionId()), actionValue);
        actionValue.getTaskValue().setNodeValue(new NodeValue(id, "Garden", "", "Task", "Action"));
        gardenActionPK = StatelessRemoteClientSessionUtil.getSession().actionCreate(m_key, new NodePK(buildActionPK.getActionId()), actionValue);
        actionValue.getTaskValue().setNodeValue(new NodeValue(id, "Painting", "", "Task", "Action"));
        paintingActionPK = StatelessRemoteClientSessionUtil.getSession().actionCreate(m_key, new NodePK(finishingActionPK.getActionId()), actionValue);
        actionValue.getTaskValue().setNodeValue(new NodeValue(id, "Carpets", "", "Task", "Action"));
        carpetsActionPK = StatelessRemoteClientSessionUtil.getSession().actionCreate(m_key, new NodePK(finishingActionPK.getActionId()), actionValue);
        actionValue.getTaskValue().setNodeValue(new NodeValue(id, "Foundations", "", "Task", "Action"));
        foundationsActionPK = StatelessRemoteClientSessionUtil.getSession().actionCreate(m_key, new NodePK(structureActionPK.getActionId()), actionValue);
        actionValue.getTaskValue().setNodeValue(new NodeValue(id, "Walls", "", "Task", "Action"));
        wallsActionPK = StatelessRemoteClientSessionUtil.getSession().actionCreate(m_key, new NodePK(structureActionPK.getActionId()), actionValue);
        actionValue.getTaskValue().setNodeValue(new NodeValue(id, "Roof", "", "Task", "Action"));
        roofActionPK = StatelessRemoteClientSessionUtil.getSession().actionCreate(m_key, new NodePK(structureActionPK.getActionId()), actionValue);
        actionValue.getTaskValue().setNodeValue(new NodeValue(id, "Electrics", "", "Task", "Action"));
        electricsActionPK = StatelessRemoteClientSessionUtil.getSession().actionCreate(m_key, new NodePK(interiorActionPK.getActionId()), actionValue);
        actionValue.getTaskValue().setNodeValue(new NodeValue(id, "Plumbing", "", "Task", "Action"));
        plumbingActionPK = StatelessRemoteClientSessionUtil.getSession().actionCreate(m_key, new NodePK(interiorActionPK.getActionId()), actionValue);
        actionValue.getTaskValue().setNodeValue(new NodeValue(id, "Kitchen", "", "Task", "Action"));
        kitchenActionPK = StatelessRemoteClientSessionUtil.getSession().actionCreate(m_key, new NodePK(interiorActionPK.getActionId()), actionValue);
        actionValue.getTaskValue().setNodeValue(new NodeValue(id, "Bathroom", "", "Task", "Action"));
        bathroomActionPK = StatelessRemoteClientSessionUtil.getSession().actionCreate(m_key, new NodePK(interiorActionPK.getActionId()), actionValue);
        actionValue.getTaskValue().setNodeValue(new NodeValue(id, "Cupboards", "", "Task", "Action"));
        cupboardsActionPK = StatelessRemoteClientSessionUtil.getSession().actionCreate(m_key, new NodePK(interiorActionPK.getActionId()), actionValue);
        actionValue.getTaskValue().setNodeValue(new NodeValue(id, "Clearing property", "", "Task", "Action"));
        clearingPropertyActionPK = StatelessRemoteClientSessionUtil.getSession().actionCreate(m_key, new NodePK(foundationsActionPK.getActionId()), actionValue);
        actionValue.getTaskValue().setNodeValue(new NodeValue(id, "Marking and Digging", "", "Task", "Action"));
        markingActionPK = StatelessRemoteClientSessionUtil.getSession().actionCreate(m_key, new NodePK(foundationsActionPK.getActionId()), actionValue);
        actionValue.getTaskValue().setNodeValue(new NodeValue(id, "Throwing concrete", "", "Task", "Action"));
        throwingConcreteActionPK = StatelessRemoteClientSessionUtil.getSession().actionCreate(m_key, new NodePK(foundationsActionPK.getActionId()), actionValue);
        actionValue.getTaskValue().setNodeValue(new NodeValue(id, "Timber construction", "", "Task", "Action"));
        timberConstructionActionPK = StatelessRemoteClientSessionUtil.getSession().actionCreate(m_key, new NodePK(roofActionPK.getActionId()), actionValue);
        actionValue.getTaskValue().setNodeValue(new NodeValue(id, "Lay tiles", "", "Task", "Action"));
        layTilesActionPK = StatelessRemoteClientSessionUtil.getSession().actionCreate(m_key, new NodePK(roofActionPK.getActionId()), actionValue);
    }

    IssuePK largeRockIssuePK;

    IssuePK notSquareIssuePK;

    IssuePK cupboardColourIssuePK;

    IssuePK wrongCarpetIssuePK;

    IssuePK plumbingNoiseIssuePK;

    IssuePK tileGroutIssuePK;

    public void setupIssues() throws RemoteException, NamingException, InvalidKeyException, CreateException, PermissionDeniedException {
        Integer id = new Integer(0);
        IssueValue issueValue = new IssueValue(id);
        issueValue.getTaskValue().setOwnerContactableValue(barneyBuilderPersonValue.getContactableValue());
        issueValue.getTaskValue().setTaskStateMachineValue(projectTaskTypeValue);
        issueValue.getTaskValue().setNodeValue(new NodeValue(id, "Found large rock under back corner of main bedroom", "The rock is preventing the foundations from being dug according to the plans", "Task", "Issue"));
        largeRockIssuePK = StatelessRemoteClientSessionUtil.getSession().issueCreate(m_key, new NodePK(markingActionPK.getActionId()), issueValue);
        issueValue.getTaskValue().setNodeValue(new NodeValue(id, "Corner of building is not square", "The building is significantly not square so that there will be a problem tiling the roof", "Task", "Issue"));
        notSquareIssuePK = StatelessRemoteClientSessionUtil.getSession().issueCreate(m_key, new NodePK(timberConstructionActionPK.getActionId()), issueValue);
        issueValue.getTaskValue().setNodeValue(new NodeValue(id, "Can't get right colour for cupboard doors", "The beech colour is not currently available", "Task", "Issue"));
        cupboardColourIssuePK = StatelessRemoteClientSessionUtil.getSession().issueCreate(m_key, new NodePK(cupboardsActionPK.getActionId()), issueValue);
        issueValue.getTaskValue().setNodeValue(new NodeValue(id, "The wrong carpet has been fitted in the second bedroom", "Mushroom colour was requested but maroon colour has been fitted", "Task", "Issue"));
        wrongCarpetIssuePK = StatelessRemoteClientSessionUtil.getSession().issueCreate(m_key, new NodePK(carpetsActionPK.getActionId()), issueValue);
        issueValue.getTaskValue().setNodeValue(new NodeValue(id, "The kitchen plumbing knocks loudly", "This seems to occur mainly when turning on the cold tap", "Task", "Issue"));
        plumbingNoiseIssuePK = StatelessRemoteClientSessionUtil.getSession().issueCreate(m_key, new NodePK(checkingActionPK.getActionId()), issueValue);
        issueValue.getTaskValue().setNodeValue(new NodeValue(id, "Bathroom tiling not completed properly", "The grouting around the basin and toiled in the 2nd bathroom hasn't been finished properly", "Task", "Issue"));
        tileGroutIssuePK = StatelessRemoteClientSessionUtil.getSession().issueCreate(m_key, new NodePK(checkingActionPK.getActionId()), issueValue);
    }

    TransitionValue startedTransitionValue;

    TransitionValue completedTransitionValue;

    TransitionValue suspendedTransitionValue;

    TransitionValue resumedTransitionValue;

    TransitionValue newTransitionValue;

    public void performProjectTransitions() throws RemoteException, NamingException, InvalidKeyException, CreateException, PermissionDeniedException {
        TaskPK buildHouseTaskPK = new TaskPK(buildHouseProjectPK.getProjectId());
        Collection transitionValues = StatelessRemoteClientSessionUtil.getSession().getTaskLegalTransitionValues(m_key, buildHouseTaskPK);
        Iterator i = transitionValues.iterator();
        while (i.hasNext()) {
            TransitionValue transitionValue = (TransitionValue) i.next();
            if (transitionValue.getStatusValue().getNodeValue().getName().equals("Started")) startedTransitionValue = transitionValue;
        }
        StatelessRemoteClientSessionUtil.getSession().taskChangeState(m_key, buildHouseTaskPK, startedTransitionValue.getPrimaryKey(), "", new Byte((byte) 0), null, null, null);
        transitionValues = StatelessRemoteClientSessionUtil.getSession().getTaskLegalTransitionValues(m_key, buildHouseTaskPK);
        i = transitionValues.iterator();
        while (i.hasNext()) {
            TransitionValue transitionValue = (TransitionValue) i.next();
            if (transitionValue.getStatusValue().getNodeValue().getName().equals("Completed")) completedTransitionValue = transitionValue;
            if (transitionValue.getStatusValue().getNodeValue().getName().equals("Suspended")) suspendedTransitionValue = transitionValue;
        }
        StatelessRemoteClientSessionUtil.getSession().taskChangeState(m_key, buildHouseTaskPK, suspendedTransitionValue.getPrimaryKey(), "Run out of cash for paying for worker, negotiating with bank", new Byte((byte) 0), null, null, null);
        transitionValues = StatelessRemoteClientSessionUtil.getSession().getTaskLegalTransitionValues(m_key, buildHouseTaskPK);
        i = transitionValues.iterator();
        while (i.hasNext()) {
            TransitionValue transitionValue = (TransitionValue) i.next();
            if (transitionValue.getStatusValue().getNodeValue().getName().equals("Resumed")) resumedTransitionValue = transitionValue;
        }
        StatelessRemoteClientSessionUtil.getSession().taskChangeState(m_key, buildHouseTaskPK, resumedTransitionValue.getPrimaryKey(), "Managed to extend the bank loan to cover remainder of development costs", new Byte((byte) 0), null, null, null);
    }

    public void performActionTransitions() throws RemoteException, NamingException, InvalidKeyException, CreateException, PermissionDeniedException {
        StatelessRemoteClientSessionUtil.getSession().taskChangeState(m_key, new TaskPK(planningActionPK.getActionId()), startedTransitionValue.getPrimaryKey(), "", new Byte((byte) 0), null, null, null);
        StatelessRemoteClientSessionUtil.getSession().taskChangeState(m_key, new TaskPK(findArchitectActionPK.getActionId()), startedTransitionValue.getPrimaryKey(), "", new Byte((byte) 0), null, null, null);
        StatelessRemoteClientSessionUtil.getSession().taskChangeState(m_key, new TaskPK(findArchitectActionPK.getActionId()), completedTransitionValue.getPrimaryKey(), "Have taken on Peter Robson to design the house", new Byte((byte) 0), null, null, null);
        StatelessRemoteClientSessionUtil.getSession().taskChangeState(m_key, new TaskPK(designHouseActionPK.getActionId()), startedTransitionValue.getPrimaryKey(), "Peter has started work on the designs", new Byte((byte) 0), null, null, null);
        StatelessRemoteClientSessionUtil.getSession().taskChangeState(m_key, new TaskPK(designHouseActionPK.getActionId()), completedTransitionValue.getPrimaryKey(), "The plan is finished and it looks great", new Byte((byte) 0), null, null, null);
        StatelessRemoteClientSessionUtil.getSession().taskChangeState(m_key, new TaskPK(planningApprovalActionPK.getActionId()), startedTransitionValue.getPrimaryKey(), "Peter took the plans down to the municipal office this morning", new Byte((byte) 0), null, null, null);
        StatelessRemoteClientSessionUtil.getSession().taskChangeState(m_key, new TaskPK(planningApprovalActionPK.getActionId()), completedTransitionValue.getPrimaryKey(), "Finally the plans have been approved", new Byte((byte) 0), null, null, null);
        StatelessRemoteClientSessionUtil.getSession().taskChangeState(m_key, new TaskPK(planningActionPK.getActionId()), completedTransitionValue.getPrimaryKey(), "Planning stage completed", new Byte((byte) 0), null, null, null);
        Collection transitionValues = StatelessRemoteClientSessionUtil.getSession().getTaskLegalTransitionValues(m_key, new TaskPK(planningActionPK.getActionId()));
        Iterator i = transitionValues.iterator();
        while (i.hasNext()) {
            TransitionValue transitionValue = (TransitionValue) i.next();
            if (transitionValue.getStatusValue().getNodeValue().getName().equals("New")) newTransitionValue = transitionValue;
        }
        StatelessRemoteClientSessionUtil.getSession().taskChangeState(m_key, new TaskPK(buildActionPK.getActionId()), startedTransitionValue.getPrimaryKey(), "", new Byte((byte) 0), null, null, null);
        StatelessRemoteClientSessionUtil.getSession().taskChangeState(m_key, new TaskPK(structureActionPK.getActionId()), startedTransitionValue.getPrimaryKey(), "", new Byte((byte) 0), null, null, null);
        StatelessRemoteClientSessionUtil.getSession().taskChangeState(m_key, new TaskPK(foundationsActionPK.getActionId()), startedTransitionValue.getPrimaryKey(), "", new Byte((byte) 0), null, null, null);
        StatelessRemoteClientSessionUtil.getSession().taskChangeState(m_key, new TaskPK(clearingPropertyActionPK.getActionId()), startedTransitionValue.getPrimaryKey(), "", new Byte((byte) 0), null, null, null);
        StatelessRemoteClientSessionUtil.getSession().taskChangeState(m_key, new TaskPK(clearingPropertyActionPK.getActionId()), completedTransitionValue.getPrimaryKey(), "Property clearing completed", new Byte((byte) 0), null, null, null);
        StatelessRemoteClientSessionUtil.getSession().taskChangeState(m_key, new TaskPK(markingActionPK.getActionId()), startedTransitionValue.getPrimaryKey(), "", new Byte((byte) 0), null, null, null);
        StatelessRemoteClientSessionUtil.getSession().taskChangeState(m_key, new TaskPK(largeRockIssuePK.getIssueId()), startedTransitionValue.getPrimaryKey(), "Will attempt to break up rock using picks.", new Byte((byte) 0), null, null, null);
        StatelessRemoteClientSessionUtil.getSession().taskChangeState(m_key, new TaskPK(largeRockIssuePK.getIssueId()), suspendedTransitionValue.getPrimaryKey(), "No success with breaking up rock. Expert called in for tomorrow.", new Byte((byte) 0), null, null, null);
        StatelessRemoteClientSessionUtil.getSession().taskChangeState(m_key, new TaskPK(largeRockIssuePK.getIssueId()), resumedTransitionValue.getPrimaryKey(), "Expert will dynamite rock tomorrow.", new Byte((byte) 0), null, null, null);
        StatelessRemoteClientSessionUtil.getSession().taskChangeState(m_key, new TaskPK(largeRockIssuePK.getIssueId()), completedTransitionValue.getPrimaryKey(), "Rock has been dynamited and pieces removed.", new Byte((byte) 0), null, null, null);
        StatelessRemoteClientSessionUtil.getSession().taskChangeState(m_key, new TaskPK(markingActionPK.getActionId()), completedTransitionValue.getPrimaryKey(), "Digging completed after dynamiting large rock", new Byte((byte) 0), null, null, null);
        StatelessRemoteClientSessionUtil.getSession().taskChangeState(m_key, new TaskPK(throwingConcreteActionPK.getActionId()), startedTransitionValue.getPrimaryKey(), "", new Byte((byte) 0), null, null, null);
        StatelessRemoteClientSessionUtil.getSession().taskChangeState(m_key, new TaskPK(throwingConcreteActionPK.getActionId()), completedTransitionValue.getPrimaryKey(), "Foundations completed", new Byte((byte) 0), null, null, null);
        StatelessRemoteClientSessionUtil.getSession().taskChangeState(m_key, new TaskPK(foundationsActionPK.getActionId()), completedTransitionValue.getPrimaryKey(), "Foundation stage completed after dynamiting large rock", new Byte((byte) 0), null, null, null);
        StatelessRemoteClientSessionUtil.getSession().taskChangeState(m_key, new TaskPK(wallsActionPK.getActionId()), startedTransitionValue.getPrimaryKey(), "", new Byte((byte) 0), null, null, null);
        StatelessRemoteClientSessionUtil.getSession().taskChangeState(m_key, new TaskPK(wallsActionPK.getActionId()), completedTransitionValue.getPrimaryKey(), "Walls completed", new Byte((byte) 0), null, null, null);
        StatelessRemoteClientSessionUtil.getSession().taskChangeState(m_key, new TaskPK(wallsActionPK.getActionId()), newTransitionValue.getPrimaryKey(), "The end walls are not straight so the roof can't be fitted, need straightening", new Byte((byte) 0), null, null, null);
        StatelessRemoteClientSessionUtil.getSession().taskChangeState(m_key, new TaskPK(wallsActionPK.getActionId()), startedTransitionValue.getPrimaryKey(), "The end walls are being torn down and rebuilt", new Byte((byte) 0), null, null, null);
        StatelessRemoteClientSessionUtil.getSession().taskChangeState(m_key, new TaskPK(wallsActionPK.getActionId()), completedTransitionValue.getPrimaryKey(), "Walls completed 2nd time", new Byte((byte) 0), null, null, null);
        StatelessRemoteClientSessionUtil.getSession().taskChangeState(m_key, new TaskPK(roofActionPK.getActionId()), startedTransitionValue.getPrimaryKey(), "", new Byte((byte) 0), null, null, null);
        StatelessRemoteClientSessionUtil.getSession().taskChangeState(m_key, new TaskPK(timberConstructionActionPK.getActionId()), startedTransitionValue.getPrimaryKey(), "", new Byte((byte) 0), null, null, null);
        StatelessRemoteClientSessionUtil.getSession().taskChangeState(m_key, new TaskPK(notSquareIssuePK.getIssueId()), startedTransitionValue.getPrimaryKey(), "Brick layers have been notified of problem. End walls will have to be rebuilt. Starting tomorrow.", new Byte((byte) 0), null, null, null);
        StatelessRemoteClientSessionUtil.getSession().taskChangeState(m_key, new TaskPK(timberConstructionActionPK.getActionId()), suspendedTransitionValue.getPrimaryKey(), "The end walls are not square, must be rebuilt before proceeding", new Byte((byte) 0), null, null, null);
        StatelessRemoteClientSessionUtil.getSession().taskChangeState(m_key, new TaskPK(notSquareIssuePK.getIssueId()), completedTransitionValue.getPrimaryKey(), "The end walls have been torn down and rebuilt square this time!", new Byte((byte) 0), null, null, null);
        StatelessRemoteClientSessionUtil.getSession().taskChangeState(m_key, new TaskPK(timberConstructionActionPK.getActionId()), resumedTransitionValue.getPrimaryKey(), "The end walls have been rebuilt square and roof can continue", new Byte((byte) 0), null, null, null);
        StatelessRemoteClientSessionUtil.getSession().taskChangeState(m_key, new TaskPK(timberConstructionActionPK.getActionId()), completedTransitionValue.getPrimaryKey(), "Roof timbers completed", new Byte((byte) 0), null, null, null);
        StatelessRemoteClientSessionUtil.getSession().taskChangeState(m_key, new TaskPK(layTilesActionPK.getActionId()), startedTransitionValue.getPrimaryKey(), "", new Byte((byte) 0), null, null, null);
        StatelessRemoteClientSessionUtil.getSession().taskChangeState(m_key, new TaskPK(layTilesActionPK.getActionId()), completedTransitionValue.getPrimaryKey(), "Tiling completed with no hitches", new Byte((byte) 0), null, null, null);
        StatelessRemoteClientSessionUtil.getSession().taskChangeState(m_key, new TaskPK(roofActionPK.getActionId()), completedTransitionValue.getPrimaryKey(), "Roof finally completed after significant delays", new Byte((byte) 0), null, null, null);
        StatelessRemoteClientSessionUtil.getSession().taskChangeState(m_key, new TaskPK(structureActionPK.getActionId()), completedTransitionValue.getPrimaryKey(), "Structure stage finally completed after straightening end walls and roof delays", new Byte((byte) 0), null, null, null);
        StatelessRemoteClientSessionUtil.getSession().taskChangeState(m_key, new TaskPK(interiorActionPK.getActionId()), startedTransitionValue.getPrimaryKey(), "", new Byte((byte) 0), null, null, null);
        StatelessRemoteClientSessionUtil.getSession().taskChangeState(m_key, new TaskPK(electricsActionPK.getActionId()), startedTransitionValue.getPrimaryKey(), "", new Byte((byte) 0), null, null, null);
        StatelessRemoteClientSessionUtil.getSession().taskChangeState(m_key, new TaskPK(electricsActionPK.getActionId()), completedTransitionValue.getPrimaryKey(), "Electrics completed", new Byte((byte) 0), null, null, null);
        StatelessRemoteClientSessionUtil.getSession().taskChangeState(m_key, new TaskPK(plumbingActionPK.getActionId()), startedTransitionValue.getPrimaryKey(), "", new Byte((byte) 0), null, null, null);
        StatelessRemoteClientSessionUtil.getSession().taskChangeState(m_key, new TaskPK(plumbingActionPK.getActionId()), completedTransitionValue.getPrimaryKey(), "Plumbing completed", new Byte((byte) 0), null, null, null);
        StatelessRemoteClientSessionUtil.getSession().taskChangeState(m_key, new TaskPK(kitchenActionPK.getActionId()), startedTransitionValue.getPrimaryKey(), "", new Byte((byte) 0), null, null, null);
        StatelessRemoteClientSessionUtil.getSession().taskChangeState(m_key, new TaskPK(kitchenActionPK.getActionId()), completedTransitionValue.getPrimaryKey(), "Kitchen completed", new Byte((byte) 0), null, null, null);
        StatelessRemoteClientSessionUtil.getSession().taskChangeState(m_key, new TaskPK(bathroomActionPK.getActionId()), startedTransitionValue.getPrimaryKey(), "", new Byte((byte) 0), null, null, null);
        StatelessRemoteClientSessionUtil.getSession().taskChangeState(m_key, new TaskPK(bathroomActionPK.getActionId()), completedTransitionValue.getPrimaryKey(), "Bathroom completed", new Byte((byte) 0), null, null, null);
        StatelessRemoteClientSessionUtil.getSession().taskChangeState(m_key, new TaskPK(cupboardsActionPK.getActionId()), startedTransitionValue.getPrimaryKey(), "", new Byte((byte) 0), null, null, null);
        StatelessRemoteClientSessionUtil.getSession().taskChangeState(m_key, new TaskPK(cupboardColourIssuePK.getIssueId()), startedTransitionValue.getPrimaryKey(), "The normal distributer of the beech is out of stock. They will have stock in one week.", new Byte((byte) 0), null, null, null);
        StatelessRemoteClientSessionUtil.getSession().taskChangeState(m_key, new TaskPK(cupboardColourIssuePK.getIssueId()), suspendedTransitionValue.getPrimaryKey(), "A week later and no progress with distributor. Looking for a new distributor.", new Byte((byte) 0), null, null, null);
        StatelessRemoteClientSessionUtil.getSession().taskChangeState(m_key, new TaskPK(cupboardColourIssuePK.getIssueId()), resumedTransitionValue.getPrimaryKey(), "New distributer found who can source the beech colour.", new Byte((byte) 0), null, null, null);
        StatelessRemoteClientSessionUtil.getSession().taskChangeState(m_key, new TaskPK(cupboardColourIssuePK.getIssueId()), completedTransitionValue.getPrimaryKey(), "Beech cupboard doors have arrived.", new Byte((byte) 0), null, null, null);
        StatelessRemoteClientSessionUtil.getSession().taskChangeState(m_key, new TaskPK(cupboardsActionPK.getActionId()), suspendedTransitionValue.getPrimaryKey(), "Can't get hold of right colour", new Byte((byte) 0), null, null, null);
        StatelessRemoteClientSessionUtil.getSession().taskChangeState(m_key, new TaskPK(cupboardsActionPK.getActionId()), resumedTransitionValue.getPrimaryKey(), "Found a new supplier with right colour", new Byte((byte) 0), null, null, null);
        StatelessRemoteClientSessionUtil.getSession().taskChangeState(m_key, new TaskPK(cupboardsActionPK.getActionId()), completedTransitionValue.getPrimaryKey(), "Cupboards completed", new Byte((byte) 0), null, null, null);
        StatelessRemoteClientSessionUtil.getSession().taskChangeState(m_key, new TaskPK(interiorActionPK.getActionId()), completedTransitionValue.getPrimaryKey(), "Interior stage completed", new Byte((byte) 0), null, null, null);
        StatelessRemoteClientSessionUtil.getSession().taskChangeState(m_key, new TaskPK(gardenActionPK.getActionId()), startedTransitionValue.getPrimaryKey(), "", new Byte((byte) 0), null, null, null);
        StatelessRemoteClientSessionUtil.getSession().taskChangeState(m_key, new TaskPK(gardenActionPK.getActionId()), completedTransitionValue.getPrimaryKey(), "Garden stage completed", new Byte((byte) 0), null, null, null);
        StatelessRemoteClientSessionUtil.getSession().taskChangeState(m_key, new TaskPK(buildActionPK.getActionId()), completedTransitionValue.getPrimaryKey(), "Building stage (structure, interior and garden) completed", new Byte((byte) 0), null, null, null);
        StatelessRemoteClientSessionUtil.getSession().taskChangeState(m_key, new TaskPK(finishingActionPK.getActionId()), startedTransitionValue.getPrimaryKey(), "", new Byte((byte) 0), null, null, null);
        StatelessRemoteClientSessionUtil.getSession().taskChangeState(m_key, new TaskPK(paintingActionPK.getActionId()), startedTransitionValue.getPrimaryKey(), "", new Byte((byte) 0), null, null, null);
        StatelessRemoteClientSessionUtil.getSession().taskChangeState(m_key, new TaskPK(paintingActionPK.getActionId()), completedTransitionValue.getPrimaryKey(), "Painting completed", new Byte((byte) 0), null, null, null);
        StatelessRemoteClientSessionUtil.getSession().taskChangeState(m_key, new TaskPK(carpetsActionPK.getActionId()), startedTransitionValue.getPrimaryKey(), "", new Byte((byte) 0), null, null, null);
        StatelessRemoteClientSessionUtil.getSession().taskChangeState(m_key, new TaskPK(wrongCarpetIssuePK.getIssueId()), startedTransitionValue.getPrimaryKey(), "The carpeting people have acknowledged that they fitted the wrong colour. They have offered a discount if we keep it otherwise full price to replace. We have decided to replace and awaiting their return to refit", new Byte((byte) 0), null, null, null);
        StatelessRemoteClientSessionUtil.getSession().taskChangeState(m_key, new TaskPK(checkingActionPK.getActionId()), startedTransitionValue.getPrimaryKey(), "", new Byte((byte) 0), null, null, null);
        StatelessRemoteClientSessionUtil.getSession().taskChangeState(m_key, new TaskPK(plumbingNoiseIssuePK.getIssueId()), startedTransitionValue.getPrimaryKey(), "The plumber has been called to come in and check the state of the pipes.", new Byte((byte) 0), null, null, null);
        StatelessRemoteClientSessionUtil.getSession().taskChangeState(m_key, new TaskPK(plumbingNoiseIssuePK.getIssueId()), suspendedTransitionValue.getPrimaryKey(), "The plumber has listened to the problem and will come in a week to fix it", new Byte((byte) 0), null, null, null);
        StatelessRemoteClientSessionUtil.getSession().taskChangeState(m_key, new TaskPK(plumbingNoiseIssuePK.getIssueId()), resumedTransitionValue.getPrimaryKey(), "The plumber has worked on the noisy pipes. We must decide if we think they are fixed.", new Byte((byte) 0), null, null, null);
        StatelessRemoteClientSessionUtil.getSession().taskChangeState(m_key, new TaskPK(tileGroutIssuePK.getIssueId()), startedTransitionValue.getPrimaryKey(), "The tiler was called today but we couldn't get hold of him. We left a message that he should call us back.", new Byte((byte) 0), null, null, null);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String password = new String("fred");
        try {
            SetupDatabaseConstructionExample setup = new SetupDatabaseConstructionExample();
            setup.login(password);
            setup.setupContactDetailTypes();
            setup.setupOrganisations();
            setup.setupPersons();
            setup.setupRoles();
            setup.setupProjectTaskType();
            setup.setupProject();
            setup.setupUserAccesses();
            setup.setupActions();
            setup.setupIssues();
            setup.performProjectTransitions();
            setup.performActionTransitions();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
