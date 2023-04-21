package org.hip.vif.bom.impl.test;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Collection;
import java.util.Properties;
import java.util.Vector;
import javax.naming.NamingException;
import junit.framework.TestCase;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.hip.db.provider.mysql.MySqlProvider;
import org.hip.kernel.bitmap.IDPosition;
import org.hip.kernel.bitmap.IDPositions;
import org.hip.kernel.bom.DomainObject;
import org.hip.kernel.bom.GeneralDomainObject;
import org.hip.kernel.bom.KeyObject;
import org.hip.kernel.bom.QueryResult;
import org.hip.kernel.bom.impl.KeyObjectImpl;
import org.hip.kernel.exc.DefaultExceptionWriter;
import org.hip.kernel.exc.VException;
import org.hip.kernel.persistency.PersistencyManager;
import org.hip.kernel.provider.ProviderRegistry;
import org.hip.kernel.sys.VSys;
import org.hip.vif.bom.BookmarkHome;
import org.hip.vif.bom.CompletionAuthorReviewerHome;
import org.hip.vif.bom.CompletionHistoryHome;
import org.hip.vif.bom.CompletionHome;
import org.hip.vif.bom.GroupAdminHome;
import org.hip.vif.bom.GroupHome;
import org.hip.vif.bom.LinkMemberRoleHome;
import org.hip.vif.bom.LinkPermissionRole;
import org.hip.vif.bom.LinkPermissionRoleHome;
import org.hip.vif.bom.Member;
import org.hip.vif.bom.MemberHistoryHome;
import org.hip.vif.bom.MemberHome;
import org.hip.vif.bom.ParticipantHome;
import org.hip.vif.bom.Permission;
import org.hip.vif.bom.PermissionHome;
import org.hip.vif.bom.PersonHome;
import org.hip.vif.bom.Question;
import org.hip.vif.bom.QuestionAuthorReviewerHome;
import org.hip.vif.bom.QuestionHierarchyHome;
import org.hip.vif.bom.QuestionHome;
import org.hip.vif.bom.SubscriptionHome;
import org.hip.vif.bom.impl.BOMHelper;
import org.hip.vif.bom.impl.JoinAuthorReviewerToCompletionHome;
import org.hip.vif.bom.impl.JoinAuthorReviewerToQuestionHome;
import org.hip.vif.bom.impl.JoinCompletionToMemberHome;
import org.hip.vif.bom.impl.JoinCompletionToQuestionHome;
import org.hip.vif.bom.impl.JoinParticipantToMemberHome;
import org.hip.vif.bom.impl.JoinQuestionToAuthorReviewerHome;
import org.hip.vif.bom.impl.JoinQuestionToChildAndAuthorHome;
import org.hip.vif.bom.impl.JoinQuestionToChildHome;
import org.hip.vif.bom.impl.JoinSubscriptionToMemberHome;
import org.hip.vif.bom.impl.MemberImpl;
import org.hip.vif.bom.impl.WorkflowAwareContribution;
import org.hip.vif.bom.search.VIFMemberIndexer;
import org.hip.vif.exc.BOMChangeValueException;
import org.hip.vif.servlets.VIFContext;

/**
 * Utility class for testing purpose.
 * Creating and deleting of entries in viftest-DB.
 * 
 * @author: Benno Luthiger
 */
public class DataHouseKeeper {

    private static DataHouseKeeper singleton = new DataHouseKeeper();

    public static final String KEY_PERMISSION_ID = "ID";

    public static final String KEY_PERMISSION_LABEL = "Label";

    public static final String KEY_PERMISSION_DESCR = "Description";

    public static final String KEY_LINK_PERMISSION_ID = "PermissionID";

    public static final String KEY_LINK_ROLE_ID = "RoleID";

    public static final String PERMISSION_LABEL_1 = "testPermission1";

    public static final String PERMISSION_LABEL_2 = "testPermission2";

    private static final String MEMBER_USER_ID = "TestUsr-DHK";

    private static final String DFT_NAME = "NameT";

    private static final String GROUP_ID = "TestGroup";

    private static final String SIMPLE_HOME_NAME = "org.hip.vif.bom.impl.test.Test2DomainObjectHomeImpl";

    private static final String KEY_PROPERTY_URL = "org.hip.vif.db.url";

    private static final String KEY_PROPERTY_DOCROOT = "org.hip.vif.docs.root";

    private static final String INDEX_DIR = "vifindex" + File.separator;

    private static final String MEMBERS_INDEX_DB = "members";

    private static final String MEMBERS_INDEX = INDEX_DIR + MEMBERS_INDEX_DB;

    private static final String CONTENT_INDEX_DB = "content";

    private static final String CONTENT_INDEX = INDEX_DIR + CONTENT_INDEX_DB;

    private Test2DomainObjectHomeImpl simpleHome = null;

    private String urlDB = null;

    private Directory membersDir;

    private Directory contentDir;

    /**
	 * Constructor for DataHouseKeeper.
	 * @throws IOException 
	 */
    private DataHouseKeeper() {
        super();
        ProviderRegistry.getInstance().addProvider(new MySqlProvider());
        File lLocation = new File("");
        VSys.setContextPath(lLocation.getAbsolutePath());
    }

    public static DataHouseKeeper getInstance() {
        return singleton;
    }

    /**
	 * Redirects the document root and creates a member index.
	 * This method has to be called before a member object is created or manipulated,
	 * for that no Exception is thrown caused by a nonexisting index.
	 * 
	 * @throws VException
	 * @throws SQLException
	 * @throws IOException
	 */
    public void redirectDocRoot() throws Exception {
        Properties lProperties = VSys.getVSysProperties();
        lProperties.setProperty(KEY_PROPERTY_DOCROOT, ".");
        VSys.setVSysProperties(lProperties);
        VIFMemberIndexer lIndexer = new VIFMemberIndexer();
        lIndexer.refreshIndex();
    }

    public MemberHome getMemberHome() throws Exception {
        return (MemberHome) VSys.homeManager.getHome(MemberImpl.HOME_CLASS_NAME);
    }

    public LinkMemberRoleHome getLinkMemberRoleHome() {
        return (LinkMemberRoleHome) BOMHelper.getLinkMemberRoleHome();
    }

    public MemberHistoryHome getMemberHistoryHome() {
        return (MemberHistoryHome) BOMHelper.getMemberHistoryHome();
    }

    public LinkPermissionRoleHome getLinkPermissionRoleHome() {
        return (LinkPermissionRoleHome) BOMHelper.getLinkPermissionRoleHome();
    }

    public PermissionHome getPermissionHome() {
        return (PermissionHome) BOMHelper.getPermissionHome();
    }

    public GroupHome getGroupHome() {
        return (GroupHome) BOMHelper.getGroupHome();
    }

    public GroupAdminHome getGroupAdminHome() {
        return (GroupAdminHome) BOMHelper.getGroupAdminHome();
    }

    public ParticipantHome getParticipantHome() {
        return (ParticipantHome) BOMHelper.getParticipantHome();
    }

    public QuestionHome getQuestionHome() {
        return (QuestionHome) BOMHelper.getQuestionHome();
    }

    public CompletionHome getCompletionHome() {
        return (CompletionHome) BOMHelper.getCompletionHome();
    }

    public CompletionHistoryHome getCompletionHistoryHome() {
        return (CompletionHistoryHome) BOMHelper.getCompletionHistoryHome();
    }

    public CompletionAuthorReviewerHome getCompletionAuthorReviewerHome() {
        return (CompletionAuthorReviewerHome) BOMHelper.getCompletionAuthorReviewerHome();
    }

    public QuestionAuthorReviewerHome getQuestionAuthorReviewerHome() {
        return (QuestionAuthorReviewerHome) BOMHelper.getQuestionAuthorReviewerHome();
    }

    public QuestionHierarchyHome getQuestionHierarchyHome() {
        return (QuestionHierarchyHome) BOMHelper.getQuestionHierarchyHome();
    }

    public PersonHome getPersonHome() {
        return (PersonHome) BOMHelper.getPersonHome();
    }

    public SubscriptionHome getSubscriptionHome() {
        return (SubscriptionHome) BOMHelper.getSubscriptionHome();
    }

    public BookmarkHome getBookmarkHome() {
        return (BookmarkHome) BOMHelper.getBookmarkHome();
    }

    public JoinQuestionToAuthorReviewerHome getJoinQuestionToAuthorReviewerHome() {
        return (JoinQuestionToAuthorReviewerHome) BOMHelper.getJoinQuestionToAuthorReviewerHome();
    }

    public JoinQuestionToChildHome getJoinQuestionToChildHome() {
        return (JoinQuestionToChildHome) BOMHelper.getJoinQuestionToChildHome();
    }

    public JoinAuthorReviewerToQuestionHome getJoinAuthorReviewerToQuestionHome() {
        return (JoinAuthorReviewerToQuestionHome) BOMHelper.getJoinAuthorReviewerToQuestionHome();
    }

    public JoinAuthorReviewerToCompletionHome getJoinAuthorReviewerToCompletionHome() {
        return (JoinAuthorReviewerToCompletionHome) BOMHelper.getJoinAuthorReviewerToCompletionHome();
    }

    public JoinCompletionToQuestionHome getJoinCompletionToQuestionHome() {
        return (JoinCompletionToQuestionHome) BOMHelper.getJoinCompletionToQuestionHome();
    }

    public JoinCompletionToMemberHome getJoinCompletionToMemberHome() {
        return (JoinCompletionToMemberHome) BOMHelper.getJoinCompletionToMemberHome();
    }

    public JoinQuestionToChildAndAuthorHome getJoinQuestionToChildAndAuthorHome() {
        return (JoinQuestionToChildAndAuthorHome) BOMHelper.getJoinQuestionToChildAndAuthorHome();
    }

    public JoinParticipantToMemberHome getJoinParticipantToMemberHome() {
        return (JoinParticipantToMemberHome) BOMHelper.getJoinParticipantToMemberHome();
    }

    public JoinSubscriptionToMemberHome getJoinSubscriptionToMemberHome() {
        return (JoinSubscriptionToMemberHome) BOMHelper.getJoinSubscriptionToMemberHome();
    }

    public void deleteAllFromMember() throws SQLException {
        deleteAllFrom("tblMember");
    }

    public void deleteAllFromMemberHistory() throws SQLException {
        deleteAllFrom("tblMemberHistory");
    }

    public void deleteAllFromLinkMemberRole() throws SQLException {
        deleteAllFrom("tblLinkMemberRole");
    }

    public void deleteAllFromLinkPermissionRole() throws SQLException {
        deleteAllFrom("tblLinkPermissionRole");
    }

    public void deleteAllFromPermission() throws SQLException {
        deleteAllFrom("tblPermission");
    }

    public void deleteAllFromGroup() throws SQLException {
        deleteAllFrom("tblGroup");
    }

    public void deleteAllFromGroupAdmin() throws SQLException {
        deleteAllFrom("tblGroupAdmin");
    }

    public void deleteAllFromParticipant() throws SQLException {
        deleteAllFrom("tblParticipant");
    }

    public void deleteAllFromQuestion() throws SQLException {
        deleteAllFrom("tblQuestion");
    }

    public void deleteAllFromQuestionHistory() throws SQLException {
        deleteAllFrom("tblQuestionHistory");
    }

    public void deleteAllFromQuestionHierarchy() throws SQLException {
        deleteAllFrom("tblQuestionHierarchy");
    }

    public void deleteAllFromCompletion() throws SQLException {
        deleteAllFrom("tblCompletion");
    }

    public void deleteAllFromCompletionHistory() throws SQLException {
        deleteAllFrom("tblCompletionHistory");
    }

    public void deleteAllFromQuestionAuthorReviewer() throws SQLException {
        deleteAllFrom("tblQuestionAuthorReviewer");
    }

    public void deleteAllFromCompletionAuthorReviewer() throws SQLException {
        deleteAllFrom("tblCompletionAuthorReviewer");
    }

    public void deleteAllFromTextAuthorReviewer() throws SQLException {
        deleteAllFrom("tblTextAuthorReviewer");
    }

    public void deleteAllFromPersonAuthorReviewer() throws SQLException {
        deleteAllFrom("tblPersonAuthorReviewer");
    }

    public void deleteAllFromPerson() throws SQLException {
        deleteAllFrom("tblPerson");
    }

    public void deleteAllFromSubscription() throws SQLException {
        deleteAllFrom("tblSubscription");
    }

    public void deleteAllFromBookmark() throws SQLException {
        deleteAllFrom("tblBookmark");
    }

    public void deleteAllInAll() {
        try {
            deleteAllFromMember();
            deleteAllFromMemberHistory();
            deleteAllFromLinkMemberRole();
            deleteAllFromPermission();
            deleteAllFromLinkPermissionRole();
            deleteAllFromGroup();
            deleteAllFromGroupAdmin();
            deleteAllFromParticipant();
            deleteAllFromQuestion();
            deleteAllFromQuestionHistory();
            deleteAllFromQuestionHierarchy();
            deleteAllFromCompletion();
            deleteAllFromCompletionHistory();
            deleteAllFromQuestionAuthorReviewer();
            deleteAllFromCompletionAuthorReviewer();
            deleteAllFromTextAuthorReviewer();
            deleteAllFromPersonAuthorReviewer();
            deleteAllFromPerson();
            deleteAllFromSubscription();
            deleteAllFromBookmark();
        } catch (SQLException exc) {
            TestCase.fail(exc.getMessage());
        }
    }

    private void deleteAllFrom(String inTableName) throws SQLException {
        try {
            Connection lConnection = PersistencyManager.singleton.getConnection();
            Statement lStatement = lConnection.createStatement();
            lStatement.execute("DELETE FROM " + inTableName);
            lStatement.close();
            lConnection.close();
        } catch (Exception exc) {
            throw new SQLException(exc.getMessage());
        }
    }

    /**
	 * Creates a member entry
	 * 
	 * @param inNr String
	 * @return String member ID
	 * @throws BOMChangeValueException
	 */
    public String createMember(String inNr) throws Exception {
        return createMember(inNr, inNr + ".mail@test");
    }

    /**
	 * Creates a member entry with the specified user id and mail address
	 * 
	 * @param inNr User id
	 * @param inMail Mail address
	 * @return String The ID of the new member
	 * @throws BOMChangeValueException
	 */
    public String createMember(String inNr, String inMail) throws Exception {
        try {
            Member lMember = (Member) getMemberHome().create();
            Long outMemberID = lMember.ucNew(MEMBER_USER_ID + inNr, "NameT" + inNr, "VornameT" + inNr, "StrasseT", "PLZ-T", "StadtT", "", "", inMail, "1", "de", "123", new String[] { VIFContext.ROLE_ID_SU, VIFContext.ROLE_ID_GROUP_ADMIN });
            return outMemberID.toString();
        } catch (VException exc) {
            throw new BOMChangeValueException(exc.getMessage());
        }
    }

    /**
	 * Creates a member and returns his id
	 * 
	 * @return java.lang.String
	 * @throws BOMChangeValueException
	 */
    public String createMember() throws Exception {
        return createMember("1");
    }

    /**
	 * Creates two members and returns their ids
	 * 
	 * @return java.lang.String[]
	 * @throws BOMChangeValueException
	 */
    public String[] create2Members() throws Exception {
        String[] outIDs = new String[2];
        outIDs[0] = createMember("1");
        outIDs[1] = createMember("2");
        return outIDs;
    }

    /**
	 * Creates three members and returns their ids
	 * 
	 * @return java.lang.String[]
	 * @throws BOMChangeValueException
	 */
    public String[] create3Members() throws Exception {
        String[] outIDs = new String[3];
        outIDs[0] = createMember("1");
        outIDs[1] = createMember("2");
        outIDs[2] = createMember("3");
        return outIDs;
    }

    /**
	 * Creates a member entry and two associated role entries.
	 * 
	 * @return java.math.BigDecimal ID of created member entry
	 */
    public BigDecimal createMember2Roles() throws Exception {
        return createMember2Roles(DFT_NAME);
    }

    /**
	 * Creates a member entry with the specified member name and 
	 * two associated role entries.
	 * 
	 * @return java.math.BigDecimal ID of created member entry
	 */
    public BigDecimal createMember2Roles(String inName) throws Exception {
        return createMemberRoles(inName, new String[] { VIFContext.ROLE_ID_SU, VIFContext.ROLE_ID_GROUP_ADMIN });
    }

    /**
	 * Creates a member entry with the specified member name and 
	 * the specified role entries.
	 * 
	 * @param inName String
	 * @param inRoles String[]
	 * @return java.math.BigDecimal ID of created member entry
	 * @throws BOMChangeValueException
	 */
    public BigDecimal createMemberRoles(String inName, String[] inRoles) throws Exception {
        return createMemberRoles(MEMBER_USER_ID, inName, inRoles);
    }

    /**
	 * Creates a member entry with the specified user id and member name and 
	 * the specified role entries.
	 * 
	 * @param inUserID String
	 * @param inName String
	 * @param inRoles String[]
	 * @return java.math.BigDecimal ID of created member entry
	 * @throws BOMChangeValueException
	 */
    public BigDecimal createMemberRoles(String inUserID, String inName, String[] inRoles) throws Exception {
        try {
            Member lMember = (Member) getMemberHome().create();
            Long outMemberID = lMember.ucNew(inUserID, inName, "VornameT", "StrasseT", "PLZ-T", "StadtT", "", "", "mail@test", "1", "de", "123", inRoles);
            return new BigDecimal(outMemberID.doubleValue());
        } catch (VException exc) {
            throw new BOMChangeValueException(exc.getMessage());
        }
    }

    /**
	 * Creates a member entry with the specified member name and 
	 * one associated role.
	 * 
	 * @return java.math.BigDecimal ID of created member entry
	 */
    public BigDecimal createMember1Role(String inUserID, String inName) throws Exception {
        try {
            Member lMember = (Member) getMemberHome().create();
            lMember.ucNew(inUserID, inName, "VornameT2", "StrasseT2", "PLZ-T2", "StadtT2", "", "", "mail.2@test", "0", "de", "321", new String[] { VIFContext.ROLE_ID_PARTICIPANT });
            KeyObject lKey = new KeyObjectImpl();
            lKey.setValue(MemberHome.KEY_USER_ID, MEMBER_USER_ID);
            lMember = (Member) getMemberHome().findByKey(lKey);
            return (BigDecimal) lMember.get(MemberHome.KEY_ID);
        } catch (VException exc) {
            throw new BOMChangeValueException(exc.getMessage());
        }
    }

    public String getExpectedName() {
        return DFT_NAME;
    }

    public String getExpectedUserID() {
        return MEMBER_USER_ID;
    }

    public void checkForEmptyTable() {
        try {
            if (getMemberHome().getCount() > 0) TestCase.fail("Don't test with non empty member table!");
            if (getLinkMemberRoleHome().getCount() > 0) TestCase.fail("Don't test with non empty link table!");
        } catch (Exception exc) {
            TestCase.fail(exc.getMessage());
        }
    }

    /**
	 * Creates two permissions (P1, P2).
	 * Associates both permissions to SU, Group-Admin only has P1 associated.
	 */
    public IDPositions create2Permissions3Links() {
        IDPositions outPositions = new IDPositions();
        try {
            Permission lPermission = (Permission) getPermissionHome().create();
            lPermission.set(KEY_PERMISSION_LABEL, PERMISSION_LABEL_1);
            lPermission.set(KEY_PERMISSION_DESCR, "Test permission 1");
            lPermission.insert(true);
            KeyObject lKey = new KeyObjectImpl();
            lKey.setValue(KEY_PERMISSION_LABEL, PERMISSION_LABEL_1);
            BigDecimal lPermissionID1 = (BigDecimal) getPermissionHome().findByKey(lKey).get(KEY_PERMISSION_ID);
            lPermission = (Permission) getPermissionHome().create();
            lPermission.set(KEY_PERMISSION_LABEL, PERMISSION_LABEL_2);
            lPermission.set(KEY_PERMISSION_DESCR, "Test permission 2");
            lPermission.insert(true);
            lKey = new KeyObjectImpl();
            lKey.setValue(KEY_PERMISSION_LABEL, PERMISSION_LABEL_2);
            BigDecimal lPermissionID2 = (BigDecimal) getPermissionHome().findByKey(lKey).get(KEY_PERMISSION_ID);
            LinkPermissionRole lLink = (LinkPermissionRole) getLinkPermissionRoleHome().create();
            lLink.set(KEY_LINK_PERMISSION_ID, lPermissionID1);
            lLink.set(KEY_LINK_ROLE_ID, new Integer(VIFContext.ROLE_ID_SU));
            lLink.insert(true);
            outPositions.add(new IDPosition(String.valueOf(lPermissionID1), VIFContext.ROLE_ID_SU));
            lLink.setVirgin();
            lLink.set(KEY_LINK_PERMISSION_ID, lPermissionID1);
            lLink.set(KEY_LINK_ROLE_ID, new Integer(VIFContext.ROLE_ID_GROUP_ADMIN));
            lLink.insert(true);
            outPositions.add(new IDPosition(String.valueOf(lPermissionID1), VIFContext.ROLE_ID_GROUP_ADMIN));
            lLink.setVirgin();
            lLink.set(KEY_LINK_PERMISSION_ID, lPermissionID2);
            lLink.set(KEY_LINK_ROLE_ID, new Integer(VIFContext.ROLE_ID_SU));
            lLink.insert(true);
            outPositions.add(new IDPosition(String.valueOf(lPermissionID2), VIFContext.ROLE_ID_SU));
        } catch (VException exc) {
            TestCase.fail(exc.getMessage());
        } catch (SQLException exc) {
            TestCase.fail(exc.getMessage());
        }
        return outPositions;
    }

    /**
	 * Creates two members (M1, M2) and two permissions (P1, P2).
	 * M1 has SU-Role, M2 has Group-Admin-Role
	 * SU has both permissions associated, Group-Admin only P1
	 * 
	 * @return Long[] IDs of created members
	 */
    public Long[] create2MembersAndRoleAndPermissions() throws Exception {
        String lUserID1 = "authorization1";
        String lUserID2 = "authorization2";
        Long[] outMemberIDs = new Long[2];
        try {
            Member lMember = (Member) getMemberHome().create();
            lMember.ucNew(lUserID1, "Author 1", "VornameT2", "StrasseT2", "PLZ-T2", "StadtT2", "", "", "mail.2@test", "0", "de", "321", new String[] { VIFContext.ROLE_ID_SU });
            KeyObject lKey = new KeyObjectImpl();
            lKey.setValue(MemberHome.KEY_USER_ID, lUserID1);
            lMember = (Member) getMemberHome().findByKey(lKey);
            outMemberIDs[0] = new Long(((BigDecimal) lMember.get(MemberHome.KEY_ID)).longValue());
            lMember = (Member) getMemberHome().create();
            lMember.ucNew(lUserID2, "Author 2", "VornameT2", "StrasseT2", "PLZ-T2", "StadtT2", "", "", "mail.2@test", "0", "de", "321", new String[] { VIFContext.ROLE_ID_GROUP_ADMIN });
            lKey = new KeyObjectImpl();
            lKey.setValue(MemberHome.KEY_USER_ID, lUserID2);
            lMember = (Member) getMemberHome().findByKey(lKey);
            outMemberIDs[1] = new Long(((BigDecimal) lMember.get(MemberHome.KEY_ID)).longValue());
            create2Permissions3Links();
            return outMemberIDs;
        } catch (VException exc) {
            throw new BOMChangeValueException(exc.getMessage());
        }
    }

    private String createGroup(String inNr) throws BOMChangeValueException {
        try {
            return getGroupHome().createNew(GROUP_ID + inNr, "Group Nr. " + inNr, "1", "3", "10", false).toString();
        } catch (VException exc) {
            throw new BOMChangeValueException(exc.getMessage());
        } catch (SQLException exc) {
            throw new BOMChangeValueException(exc.getMessage());
        }
    }

    /**
	 * Creates one group and returns its id
	 * 
	 * @return java.lang.String
	 * @throws BOMChangeValueException
	 */
    public String createGroup() throws BOMChangeValueException {
        return createGroup("1");
    }

    /**
	 * Creates two groups and returns their ids
	 * 
	 * @return java.lang.String[]
	 * @throws BOMChangeValueException
	 */
    public String[] create2Groups() throws BOMChangeValueException {
        String[] outIDs = new String[2];
        outIDs[0] = createGroup("1");
        outIDs[1] = createGroup("2");
        return outIDs;
    }

    /**
	 * Creates five groups and returns their ids
	 * 
	 * @return java.lang.String[]
	 * @throws BOMChangeValueException
	 */
    public String[] create5Groups() throws BOMChangeValueException {
        String[] outIDs = new String[5];
        outIDs[0] = createGroup("1");
        outIDs[1] = createGroup("2");
        outIDs[2] = createGroup("3");
        outIDs[3] = createGroup("4");
        outIDs[4] = createGroup("5");
        return outIDs;
    }

    /**
	 * Create a new entry to design a group admin for the specified group.
	 * 
	 * @param inGroupID String
	 * @param inMemberID Long
	 */
    public void createGroupAdmin(String inGroupID, Long inMemberID) {
        try {
            DomainObject lObject = getGroupAdminHome().create();
            lObject.set(GroupAdminHome.KEY_GROUP_ID, new Integer(inGroupID));
            lObject.set(GroupAdminHome.KEY_MEMBER_ID, inMemberID);
            lObject.insert(true);
        } catch (Exception exc) {
            TestCase.fail(exc.getMessage());
        }
    }

    /**
	 * Creates a Question and returns its ID.
	 * 
	 * @param inQuestion
	 * @param inDecimal
	 * @return String the Question's ID
	 * @throws BOMChangeValueException
	 */
    public String createQuestion(String inQuestion, String inDecimal) throws BOMChangeValueException {
        return createQuestion(inQuestion, inDecimal, new BigDecimal(23), true);
    }

    /**
	 * Creates a Question with specified group and returns its ID.
	 * 
	 * @param inQuestion
	 * @param inDecimal
	 * @param inGroupID
	 * @param isRoot
	 * @return String the Question's ID
	 * @throws BOMChangeValueException
	 */
    public String createQuestion(String inQuestion, String inDecimal, BigDecimal inGroupID, boolean isRoot) throws BOMChangeValueException {
        return createQuestion(inQuestion, inDecimal, inGroupID, WorkflowAwareContribution.S_PRIVATE, isRoot);
    }

    /**
	 * Creates a Question with specified state and returns its ID.
	 * 
	 * @param inQuestion
	 * @param inDecimal
	 * @param inGroupID
	 * @param inState
	 * @param isRoot
	 * @return String the Question's ID
	 * @throws BOMChangeValueException
	 */
    public String createQuestion(String inQuestion, String inDecimal, BigDecimal inGroupID, int inState, boolean isRoot) throws BOMChangeValueException {
        Integer lRoot = isRoot ? QuestionHome.IS_ROOT : QuestionHome.NOT_ROOT;
        try {
            DomainObject lQuestion = getQuestionHome().create();
            lQuestion.set(QuestionHome.KEY_QUESTION, inQuestion);
            lQuestion.set(QuestionHome.KEY_REMARK, "Remark");
            lQuestion.set(QuestionHome.KEY_QUESTION_DECIMAL, inDecimal);
            lQuestion.set(QuestionHome.KEY_GROUP_ID, inGroupID);
            lQuestion.set(QuestionHome.KEY_ROOT_QUESTION, lRoot);
            lQuestion.set(QuestionHome.KEY_STATE, new Integer(inState));
            lQuestion.insert(true);
            KeyObject lKey = new KeyObjectImpl();
            lKey.setValue(QuestionHome.KEY_QUESTION, inQuestion);
            lKey.setValue(QuestionHome.KEY_QUESTION_DECIMAL, inDecimal);
            lQuestion = (Question) getQuestionHome().findByKey(lKey);
            return ((BigDecimal) lQuestion.get(QuestionHome.KEY_ID)).toString();
        } catch (VException exc) {
            throw new BOMChangeValueException(exc.getMessage());
        } catch (SQLException exc) {
            throw new BOMChangeValueException(exc.getMessage());
        }
    }

    /**
	 * Creates a subscription.
	 * 
	 * @param inQuestionID
	 * @param inMemberID
	 * @param inLocal
	 * @throws VException
	 * @throws SQLException
	 */
    public void createSubscription(BigDecimal inQuestionID, BigDecimal inMemberID, boolean inLocal) throws VException, SQLException {
        DomainObject lObject = getSubscriptionHome().create();
        lObject.set(SubscriptionHome.KEY_QUESTIONID, inQuestionID);
        lObject.set(SubscriptionHome.KEY_MEMBERID, inMemberID);
        lObject.set(SubscriptionHome.KEY_LOCAL, new Integer(inLocal ? 1 : 0));
        lObject.insert(true);
    }

    /**
	 * Creates a Completion and returns its ID.
	 * 
	 * @param inCompletion
	 * @param inQuestionID
	 * @return String ID
	 * @throws VException
	 * @throws SQLException
	 */
    public String createCompletion(String inCompletion, String inQuestionID) throws VException, SQLException {
        return createCompletion(inCompletion, inQuestionID, WorkflowAwareContribution.S_OPEN);
    }

    /**
	 * Creates a Completion with specified state and returns its ID.
	 * 
	 * @param inCompletion
	 * @param inQuestionID
	 * @param inState int
	 * @return String ID
	 * @throws VException
	 * @throws SQLException
	 */
    public String createCompletion(String inCompletion, String inQuestionID, int inState) throws VException, SQLException {
        DomainObject lCompletion = getCompletionHome().create();
        lCompletion.set(CompletionHome.KEY_COMPLETION, inCompletion);
        lCompletion.set(CompletionHome.KEY_QUESTION_ID, new Integer(inQuestionID));
        lCompletion.set(CompletionHome.KEY_STATE, inState);
        lCompletion.insert(true);
        KeyObject lKey = new KeyObjectImpl();
        lKey.setValue(CompletionHome.KEY_COMPLETION, inCompletion);
        return getCompletionHome().findByKey(lKey).get(CompletionHome.KEY_ID).toString();
    }

    /**
	 * Creates an author or reviewer entry in the QuestionAuthorReviewer table.
	 * 
	 * @param inQuestionID BigDecimal
	 * @param inMemberID Integer
	 * @param isAuthor boolean
	 */
    public void createQuestionProducer(BigDecimal inQuestionID, Long inMemberID, boolean isAuthor) throws BOMChangeValueException {
        try {
            if (isAuthor) {
                ((QuestionAuthorReviewerHome) getQuestionAuthorReviewerHome()).setAuthor(inMemberID, inQuestionID);
            } else {
                ((QuestionAuthorReviewerHome) getQuestionAuthorReviewerHome()).setReviewer(inMemberID, inQuestionID);
            }
        } catch (VException exc) {
            throw new BOMChangeValueException(exc.getMessage());
        } catch (SQLException exc) {
            throw new BOMChangeValueException(exc.getMessage());
        }
    }

    /**
	 * Creates an author or reviewer entry in the CompletionAuthorReviewer table.
	 * 
	 * @param inCompletionID
	 * @param inMemberID
	 * @param isAuthor
	 * @throws BOMChangeValueException
	 */
    public void createCompletionProducer(BigDecimal inCompletionID, Long inMemberID, boolean isAuthor) throws BOMChangeValueException {
        try {
            if (isAuthor) {
                ((CompletionAuthorReviewerHome) getCompletionAuthorReviewerHome()).setAuthor(inMemberID, inCompletionID);
            } else {
                ((CompletionAuthorReviewerHome) getCompletionAuthorReviewerHome()).setReviewer(inMemberID, inCompletionID);
            }
        } catch (VException exc) {
            throw new BOMChangeValueException(exc.getMessage());
        } catch (SQLException exc) {
            throw new BOMChangeValueException(exc.getMessage());
        }
    }

    public void createQuestionHierachy(String inParentID, String inChildID) throws VException, SQLException {
        getQuestionHierarchyHome().ucNew(new BigDecimal(inParentID), new BigDecimal(inChildID));
    }

    /**
	 * Returns the home for the test table.
	 * 
	 * @return org.hip.vif.bom.impl.test.Test2DomainObjectHomeImpl
	 */
    public Test2DomainObjectHomeImpl getSimpleHome() {
        if (simpleHome == null) simpleHome = (Test2DomainObjectHomeImpl) VSys.homeManager.getHome(SIMPLE_HOME_NAME);
        return simpleHome;
    }

    /**
	 * Creates a new entry with the specified name in the test table.
	 * 
	 * @param inName java.lang.String
	 */
    public void createTestEntry(String inName) throws SQLException {
        try {
            DomainObject lNew = getSimpleHome().create();
            lNew.set("Name", inName);
            lNew.set("Firstname", "Evan");
            lNew.set("Mail", "dummy1@aktion-hip.ch");
            lNew.set("Sex", new Integer(1));
            lNew.set("Amount", new Float(12.45));
            lNew.set("Double", new Float(13.11));
            lNew.insert(true);
            lNew.release();
        } catch (VException exc) {
            TestCase.fail(exc.getMessage());
        }
    }

    /**
	 * Deletes all entries from the test table.
	 */
    public void deleteAllFromTest() {
        Connection lConnection = null;
        Statement lStatement = null;
        try {
            lConnection = PersistencyManager.singleton.getConnection();
            lStatement = lConnection.createStatement();
            lStatement.execute("DELETE FROM tblTest");
        } catch (SQLException exc) {
            TestCase.fail(exc.getMessage());
        } catch (NamingException exc) {
            TestCase.fail(exc.getMessage());
        } finally {
            try {
                if (lStatement != null) lStatement.close();
                if (lConnection != null) lConnection.close();
            } catch (SQLException exc) {
                TestCase.fail(exc.getMessage());
            }
        }
    }

    /**
	 * Returns the result of a query with the specified SQL statement.
	 * 
	 * @param inSQL java.lang.String
	 * @return java.sql.ResultSet
	 */
    public ResultSet executeQuery(String inSQL) {
        Connection lConnection = null;
        Statement lStatement = null;
        ResultSet lResultSet = null;
        try {
            lConnection = PersistencyManager.singleton.getConnection();
            lStatement = lConnection.createStatement();
            lResultSet = lStatement.executeQuery(inSQL);
        } catch (SQLException exc) {
            TestCase.fail(exc.getMessage());
        } catch (NamingException exc) {
            TestCase.fail(exc.getMessage());
        } finally {
            try {
                if (lConnection != null) lConnection.close();
            } catch (SQLException exc) {
                TestCase.fail(exc.getMessage());
            }
        }
        return lResultSet;
    }

    public boolean isDBMySQL() {
        return isDBType("mysql");
    }

    public boolean isDBOracle() {
        return isDBType("oracle");
    }

    public boolean isDBPostgreSQL() {
        return isDBType("psql");
    }

    private boolean isDBType(String inDBTypeName) {
        return getDBUrl().indexOf(inDBTypeName) >= 0;
    }

    private String getDBUrl() {
        if (urlDB == null) {
            try {
                urlDB = VSys.getVSysProperties().getProperty(KEY_PROPERTY_URL);
            } catch (IOException exc) {
                TestCase.fail(exc.getMessage());
            }
        }
        return urlDB;
    }

    /**
	 * Checks the <code>QueryResult</code> against the specified array.
	 * Both the elements and the lenght of the result set are evaluated.
	 * 
	 * @param inExpected String[] containing the expected values
	 * @param inResult QueryResult the result set to check
	 * @param inColumn String the column to evaluate within the result set
	 * @param inAssert String the assert string.
	 */
    public void checkQueryResult(String[] inExpected, QueryResult inResult, String inColumn, String inAssert) {
        try {
            Collection<String> lExpected = new Vector<String>(Arrays.asList(inExpected));
            int i = 0;
            while (inResult.hasMoreElements()) {
                GeneralDomainObject lObject = inResult.nextAsDomainObject();
                TestCase.assertTrue(inAssert, lExpected.contains(lObject.get(inColumn).toString()));
                i++;
            }
            TestCase.assertEquals(inAssert + " (count)", inExpected.length, i);
        } catch (Exception exc) {
            TestCase.fail(exc.getMessage());
        }
    }

    /**
	 * Deletes the test index directory.
	 * 
	 * @throws IOException
	 * @throws SQLException 
	 */
    public void deleteIndex() throws IOException, SQLException {
        deleteIndexDir(new File(getMembersIndexDirName()));
        deleteIndexDir(new File(getContentIndexDirName()));
    }

    private void deleteIndexDir(File inIndexDir) {
        if (inIndexDir.exists()) {
            File[] lTempFiles = inIndexDir.listFiles();
            for (int i = 0; i < lTempFiles.length; i++) {
                lTempFiles[i].delete();
            }
            inIndexDir.deleteOnExit();
        }
    }

    public Directory getMembersIndexDir() throws IOException {
        if (membersDir == null) {
            membersDir = FSDirectory.getDirectory(new File(getMembersIndexDirName()));
        }
        return membersDir;
    }

    private String getMembersIndexDirName() throws IOException {
        return new File(".").getCanonicalFile().getParentFile().getCanonicalPath() + File.separator + MEMBERS_INDEX;
    }

    public Directory getContentIndexDir() throws IOException {
        if (contentDir == null) {
            contentDir = FSDirectory.getDirectory(new File(getContentIndexDirName()));
        }
        return contentDir;
    }

    private String getContentIndexDirName() throws IOException {
        return new File(".").getCanonicalFile().getParentFile().getCanonicalPath() + File.separator + CONTENT_INDEX;
    }

    /**
	 * Sets the <code>DefaultExceptionWriter</code>s log swtch to false, thus disabling print out of error messages.
	 * 
	 * @throws Exception
	 */
    public static void disableErrorPrintout() throws Exception {
        Properties lProperties = VSys.getVSysProperties();
        lProperties.setProperty(DefaultExceptionWriter.LOG_SWITCH, "false");
        VSys.setVSysProperties(lProperties);
    }

    /**
	 * Create an error log file with the specified name.
	 * 
	 * @param inLogFileName String (fully qualified or relative).
	 * @return File
	 * @throws IOException
	 */
    public static File createErrorLog(String inLogFileName) throws IOException {
        File out = new File(inLogFileName);
        if (out.exists()) {
            out.delete();
        }
        if (!out.exists()) {
            out.createNewFile();
        }
        return out;
    }

    /**
	 * Create an error log file "error0.log".
	 * 
	 * @return File
	 * @throws IOException
	 */
    public static File createErrorLog() throws IOException {
        return createErrorLog(VSys.getLogPath() + "error0.log");
    }

    /**
	 * Deletes all log files in the directory of the specified log file.
	 * 
	 * @param inFile File
	 */
    public static void deleteErrorLog(File inFile) {
        if (inFile == null || !inFile.exists()) return;
        File[] lFiles = inFile.getParentFile().listFiles();
        for (File lFile : lFiles) {
            if (lFile.isFile()) {
                if (lFile.getName().endsWith(".log") || lFile.getName().endsWith(".log.lck")) {
                    if (!lFile.delete()) {
                        lFile.deleteOnExit();
                    }
                }
            }
        }
    }
}
