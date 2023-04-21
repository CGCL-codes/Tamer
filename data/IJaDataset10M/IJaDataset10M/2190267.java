package com.google.code.linkedinapi.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import com.google.code.linkedinapi.client.constant.LanguageCodes;
import com.google.code.linkedinapi.client.constant.RelationshipCodes;
import com.google.code.linkedinapi.client.constant.TestConstants;
import com.google.code.linkedinapi.client.enumeration.CompanyField;
import com.google.code.linkedinapi.client.enumeration.ConnectionModificationType;
import com.google.code.linkedinapi.client.enumeration.FacetField;
import com.google.code.linkedinapi.client.enumeration.JobField;
import com.google.code.linkedinapi.client.enumeration.NetworkUpdateType;
import com.google.code.linkedinapi.client.enumeration.ProductField;
import com.google.code.linkedinapi.client.enumeration.ProfileField;
import com.google.code.linkedinapi.client.enumeration.ProfileType;
import com.google.code.linkedinapi.client.enumeration.SearchParameter;
import com.google.code.linkedinapi.client.enumeration.SearchSortOrder;
import com.google.code.linkedinapi.client.oauth.LinkedInAccessToken;
import com.google.code.linkedinapi.schema.Companies;
import com.google.code.linkedinapi.schema.Company;
import com.google.code.linkedinapi.schema.CompanySearch;
import com.google.code.linkedinapi.schema.Connections;
import com.google.code.linkedinapi.schema.FacetType;
import com.google.code.linkedinapi.schema.Job;
import com.google.code.linkedinapi.schema.JobBookmarks;
import com.google.code.linkedinapi.schema.JobFunctionCode;
import com.google.code.linkedinapi.schema.JobSearch;
import com.google.code.linkedinapi.schema.Jobs;
import com.google.code.linkedinapi.schema.Likes;
import com.google.code.linkedinapi.schema.Network;
import com.google.code.linkedinapi.schema.People;
import com.google.code.linkedinapi.schema.PeopleSearch;
import com.google.code.linkedinapi.schema.Person;
import com.google.code.linkedinapi.schema.Products;
import com.google.code.linkedinapi.schema.UpdateComments;
import com.google.code.linkedinapi.schema.VisibilityType;

/**
 * @author Nabeel Mukhtar
 *
 */
public abstract class LinkedInApiClientTest extends TestCase {

    /** Field description */
    protected LinkedInApiClientFactory factory;

    /** Field description */
    protected LinkedInAccessToken accessToken;

    protected LinkedInApiClient client;

    /** Field description */
    protected static final String RESOURCE_MISSING_MESSAGE = "Please define a test %s in TestConstants.properties file.";

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        assertNotNull(String.format(RESOURCE_MISSING_MESSAGE, "Consumer Key"), TestConstants.LINKED_IN_TEST_CONSUMER_KEY);
        assertNotNull(String.format(RESOURCE_MISSING_MESSAGE, "Consumer Secret"), TestConstants.LINKED_IN_TEST_CONSUMER_SECRET);
        factory = LinkedInApiClientFactory.newInstance(TestConstants.LINKED_IN_TEST_CONSUMER_KEY, TestConstants.LINKED_IN_TEST_CONSUMER_SECRET);
        assertNotNull(String.format(RESOURCE_MISSING_MESSAGE, "Access Token Key"), TestConstants.LINKED_IN_TEST_ACCESS_TOKEN_KEY);
        assertNotNull(String.format(RESOURCE_MISSING_MESSAGE, "Access Token Secret"), TestConstants.LINKED_IN_TEST_ACCESS_TOKEN_SECRET);
        accessToken = new LinkedInAccessToken(TestConstants.LINKED_IN_TEST_ACCESS_TOKEN_KEY, TestConstants.LINKED_IN_TEST_ACCESS_TOKEN_SECRET);
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        factory = null;
        accessToken = null;
    }

    /**
	 * Test method for {@link com.google.code.linkedinapi.client.impl.LinkedInApiJaxbClient#getConnectionsById(java.lang.String)}.
	 */
    @Test
    public void testGetConnectionsByIdString() {
        final String id = TestConstants.LINKED_IN_TEST_ID;
        assertNotNullOrEmpty(String.format(RESOURCE_MISSING_MESSAGE, "ID"), id);
        Connections connections = client.getConnectionsById(id);
        assertNotNull("Connections should never be null.", connections);
    }

    /**
	 * Test method for {@link com.google.code.linkedinapi.client.impl.LinkedInApiJaxbClient#getConnectionsById(java.lang.String, java.util.Set)}.
	 */
    @Test
    public void testGetConnectionsByIdStringSetOfProfileField() {
        final String id = TestConstants.LINKED_IN_TEST_ID;
        assertNotNullOrEmpty(String.format(RESOURCE_MISSING_MESSAGE, "ID"), id);
        Connections connections = client.getConnectionsById(id, EnumSet.allOf(ProfileField.class));
        assertNotNull("Connections should never be null.", connections);
    }

    /**
	 * Test method for {@link com.google.code.linkedinapi.client.impl.LinkedInApiJaxbClient#getConnectionsByUrl(java.lang.String)}.
	 */
    @Test
    public void testGetConnectionsByUrlString() {
        final String url = TestConstants.LINKED_IN_TEST_URL;
        assertNotNullOrEmpty(String.format(RESOURCE_MISSING_MESSAGE, "URL"), url);
        Connections connections = client.getConnectionsByUrl(url);
        assertNotNull("Connections should never be null.", connections);
    }

    /**
	 * Test method for {@link com.google.code.linkedinapi.client.impl.LinkedInApiJaxbClient#getConnectionsByUrl(java.lang.String, java.util.Set)}.
	 */
    @Test
    public void testGetConnectionsByUrlStringSetOfProfileField() {
        final String url = TestConstants.LINKED_IN_TEST_URL;
        assertNotNullOrEmpty(String.format(RESOURCE_MISSING_MESSAGE, "URL"), url);
        Connections connections = client.getConnectionsByUrl(url, EnumSet.allOf(ProfileField.class));
        assertNotNull("Connections should never be null.", connections);
    }

    /**
	 * Test method for {@link com.google.code.linkedinapi.client.impl.LinkedInApiJaxbClient#getConnectionsForCurrentUser()}.
	 */
    @Test
    public void testGetConnectionsForCurrentUser() {
        Connections connections = client.getConnectionsForCurrentUser();
        assertNotNull("Connections should never be null.", connections);
    }

    /**
	 * Test method for {@link com.google.code.linkedinapi.client.impl.LinkedInApiJaxbClient#getConnectionsForCurrentUser(java.util.Set)}.
	 */
    @Test
    public void testGetConnectionsForCurrentUserSetOfProfileField() {
        Connections connections = client.getConnectionsForCurrentUser(EnumSet.allOf(ProfileField.class));
        assertNotNull("Connections should never be null.", connections);
    }

    /**
	 * Test method for {@link com.google.code.linkedinapi.client.impl.LinkedInApiJaxbClient#getConnectionsForCurrentUser(Set, int, int, Date, ConnectionModificationType)}.
	 */
    @Test
    public void testGetConnectionsForCurrentUserSetIntIntDateConnectionModificationType() {
        Connections connections = client.getConnectionsForCurrentUser(getLastWeekDate(), ConnectionModificationType.ALL);
        assertNotNull("Connections should never be null.", connections);
        connections = client.getConnectionsForCurrentUser(EnumSet.allOf(ProfileField.class), 1, 5, getLastWeekDate(), ConnectionModificationType.UPDATED);
        assertNotNull("Connections should never be null.", connections);
    }

    /**
	 * Test method for {@link com.google.code.linkedinapi.client.impl.LinkedInApiJaxbClient#getNetworkUpdates()}.
	 */
    @Test
    public void testGetNetworkUpdates() {
        Network network = client.getNetworkUpdates();
        assertNotNull("Network Updates should never be null.", network);
    }

    /**
	 * Test method for {@link com.google.code.linkedinapi.client.impl.LinkedInApiJaxbClient#getNetworkUpdates(int, int)}.
	 */
    @Test
    public void testGetNetworkUpdatesIntInt() {
        Network network = client.getNetworkUpdates(1, 5);
        assertNotNull("Network Updates should never be null.", network);
    }

    /**
	 * Test method for {@link com.google.code.linkedinapi.client.impl.LinkedInApiJaxbClient#getNetworkUpdates(java.util.Date, java.util.Date)}.
	 */
    @Test
    public void testGetNetworkUpdatesDateDate() {
        Network network = client.getNetworkUpdates(getLastWeekDate(), getCurrentDate());
        assertNotNull("Network Updates should never be null.", network);
    }

    /**
	 * Test method for {@link com.google.code.linkedinapi.client.impl.LinkedInApiJaxbClient#getNetworkUpdates(java.util.Set)}.
	 */
    @Test
    public void testGetNetworkUpdatesSetOfNetworkUpdateType() {
        Network network = client.getNetworkUpdates(EnumSet.of(NetworkUpdateType.SHARED_ITEM, NetworkUpdateType.CONNECTION_UPDATE));
        assertNotNull("Network Updates should never be null.", network);
    }

    /**
	 * Test method for {@link com.google.code.linkedinapi.client.impl.LinkedInApiJaxbClient#getNetworkUpdates(java.util.Set, int, int)}.
	 */
    @Test
    public void testGetNetworkUpdatesIntIntSetOfNetworkUpdateType() {
        Network network = client.getNetworkUpdates(EnumSet.of(NetworkUpdateType.SHARED_ITEM, NetworkUpdateType.CONNECTION_UPDATE), 1, 5);
        assertNotNull("Network Updates should never be null.", network);
    }

    /**
	 * Test method for {@link com.google.code.linkedinapi.client.impl.LinkedInApiJaxbClient#getNetworkUpdates(java.util.Set, java.util.Date, java.util.Date)}.
	 */
    @Test
    public void testGetNetworkUpdatesDateDateSetOfNetworkUpdateType() {
        Network network = client.getNetworkUpdates(EnumSet.of(NetworkUpdateType.SHARED_ITEM, NetworkUpdateType.CONNECTION_UPDATE), getLastWeekDate(), getCurrentDate());
        assertNotNull("Network Updates should never be null.", network);
    }

    /**
	 * Test method for {@link com.google.code.linkedinapi.client.impl.LinkedInApiJaxbClient#getNetworkUpdates(java.util.Set, int, int, java.util.Date, java.util.Date)}.
	 */
    @Test
    public void testGetNetworkUpdatesIntIntDateDateSetOfNetworkUpdateType() {
        Network network = client.getNetworkUpdates(EnumSet.of(NetworkUpdateType.SHARED_ITEM, NetworkUpdateType.CONNECTION_UPDATE), 5, 1, getLastWeekDate(), getCurrentDate());
        assertNotNull("Network Updates should never be null.", network);
    }

    /**
	 * Test method for {@link com.google.code.linkedinapi.client.impl.LinkedInApiJaxbClient#getNetworkUpdateComments(java.lang.String)}.
	 */
    @Test
    public void testGetNetworkUpdateCommentsString() {
        final String networkUpdateId = TestConstants.LINKED_IN_TEST_NETWORK_UPDATE_ID;
        assertNotNullOrEmpty(String.format(RESOURCE_MISSING_MESSAGE, "Network Update ID"), networkUpdateId);
        UpdateComments updateComments = client.getNetworkUpdateComments(networkUpdateId);
        assertNotNull("Network Update Comments should never be null.", updateComments);
    }

    /**
	 * Test method for {@link com.google.code.linkedinapi.client.impl.LinkedInApiJaxbClient#getNetworkUpdateComments(java.lang.String)}.
	 */
    @Test
    public void testGetNetworkUpdateLikesString() {
        final String networkUpdateId = TestConstants.LINKED_IN_TEST_NETWORK_UPDATE_ID;
        assertNotNullOrEmpty(String.format(RESOURCE_MISSING_MESSAGE, "Network Update ID"), networkUpdateId);
        Likes likes = client.getNetworkUpdateLikes(networkUpdateId);
        assertNotNull("Network Update Likes should never be null.", likes);
    }

    /**
	 * Test method for {@link com.google.code.linkedinapi.client.impl.LinkedInApiJaxbClient#getProfileById(java.lang.String, com.google.code.linkedinapi.client.enumeration.ProfileType)}.
	 */
    @Test
    public void testGetProfileByIdString() {
        final String id = TestConstants.LINKED_IN_TEST_ID;
        assertNotNullOrEmpty(String.format(RESOURCE_MISSING_MESSAGE, "ID"), id);
        Person profile = client.getProfileById(id);
        assertNotNull("Profile should never be null.", profile);
    }

    /**
	 * Test method for {@link com.google.code.linkedinapi.client.impl.LinkedInApiJaxbClient#getProfileById(java.lang.String, com.google.code.linkedinapi.client.enumeration.ProfileType, java.util.Set)}.
	 */
    @Test
    public void testGetProfileByIdStringSetOfProfileField() {
        final String id = TestConstants.LINKED_IN_TEST_ID;
        assertNotNullOrEmpty(String.format(RESOURCE_MISSING_MESSAGE, "ID"), id);
        Person profile = client.getProfileById(id, EnumSet.allOf(ProfileField.class));
        assertNotNull("Profile should never be null.", profile);
    }

    /**
	 * Test method for {@link com.google.code.linkedinapi.client.impl.LinkedInApiJaxbClient#getProfileByUrl(java.lang.String, com.google.code.linkedinapi.client.enumeration.ProfileType)}.
	 */
    @Test
    public void testGetProfileByUrlStringProfileType() {
        final String url = TestConstants.LINKED_IN_TEST_URL;
        assertNotNullOrEmpty(String.format(RESOURCE_MISSING_MESSAGE, "URL"), url);
        Person profile = client.getProfileByUrl(url, ProfileType.STANDARD);
        assertNotNull("Profile should never be null.", profile);
    }

    /**
	 * Test method for {@link com.google.code.linkedinapi.client.impl.LinkedInApiJaxbClient#getProfileByUrl(java.lang.String, com.google.code.linkedinapi.client.enumeration.ProfileType, java.util.Set)}.
	 */
    @Test
    public void testGetProfileByUrlStringProfileTypeSetOfProfileField() {
        final String url = TestConstants.LINKED_IN_TEST_URL;
        assertNotNullOrEmpty(String.format(RESOURCE_MISSING_MESSAGE, "URL"), url);
        Person profile = client.getProfileByUrl(url, ProfileType.STANDARD, EnumSet.allOf(ProfileField.class));
        assertNotNull("Profile should never be null.", profile);
    }

    /**
	 * Test method for {@link com.google.code.linkedinapi.client.impl.LinkedInApiJaxbClient#getProfileForCurrentUser()}.
	 */
    @Test
    public void testGetProfileForCurrentUser() {
        Person profile = client.getProfileForCurrentUser();
        assertNotNull("Profile should never be null.", profile);
    }

    /**
	 * Test method for {@link com.google.code.linkedinapi.client.impl.LinkedInApiJaxbClient#getProfileForCurrentUser(java.util.Set)}.
	 */
    @Test
    public void testGetProfileForCurrentUserSetOfProfileField() {
        Person profile = client.getProfileForCurrentUser(EnumSet.allOf(ProfileField.class));
        assertNotNull("Profile should never be null.", profile);
    }

    /**
	 * Test method for {@link com.google.code.linkedinapi.client.impl.LinkedInApiJaxbClient#postComment(java.lang.String, java.lang.String)}.
	 */
    @Test
    public void testPostComment() {
        final String networkUpdateId = TestConstants.LINKED_IN_TEST_NETWORK_UPDATE_ID;
        final String commentText = TestConstants.LINKED_IN_TEST_NETWORK_UPDATE_COMMENT;
        assertNotNullOrEmpty(String.format(RESOURCE_MISSING_MESSAGE, "Network Update ID"), networkUpdateId);
        assertNotNullOrEmpty(String.format(RESOURCE_MISSING_MESSAGE, "Comment Text"), commentText);
        client.postComment(networkUpdateId, commentText);
    }

    /**
	 * Test method for {@link com.google.code.linkedinapi.client.impl.LinkedInApiJaxbClient#postNetworkUpdate(java.lang.String)}.
	 */
    @Test
    public void testPostNetworkUpdate() {
        final String networkUpdateText = TestConstants.LINKED_IN_TEST_NETWORK_UPDATE_TEXT;
        assertNotNullOrEmpty(String.format(RESOURCE_MISSING_MESSAGE, "Network Update Text"), networkUpdateText);
        client.postNetworkUpdate(networkUpdateText);
    }

    /**
	 * Test method for {@link com.google.code.linkedinapi.client.impl.LinkedInApiJaxbClient#searchPeople()}.
	 */
    @Test
    public void testSearchPeople() {
        People people = client.searchPeople();
        assertNotNull("People should never be null.", people);
    }

    /**
	 * Test method for {@link com.google.code.linkedinapi.client.impl.LinkedInApiJaxbClient#searchPeople(java.util.Map)}.
	 */
    @Test
    public void testSearchPeopleMapOfSearchParameterString() {
        final String searchParameters = TestConstants.LINKED_IN_TEST_SEARCH_PARAMS;
        assertNotNullOrEmpty(String.format(RESOURCE_MISSING_MESSAGE, "Search Parameters"), searchParameters);
        People people = client.searchPeople(getSearchParametersMap(searchParameters));
        assertNotNull("People should never be null.", people);
    }

    /**
	 * Test method for {@link com.google.code.linkedinapi.client.impl.LinkedInApiJaxbClient#searchPeople(java.util.Map, int, int)}.
	 */
    @Test
    public void testSearchPeopleMapOfSearchParameterStringIntInt() {
        final String searchParameters = TestConstants.LINKED_IN_TEST_SEARCH_PARAMS;
        assertNotNullOrEmpty(String.format(RESOURCE_MISSING_MESSAGE, "Search Parameters"), searchParameters);
        People people = client.searchPeople(getSearchParametersMap(searchParameters), 1, 5);
        assertNotNull("People should never be null.", people);
    }

    /**
	 * Test method for {@link com.google.code.linkedinapi.client.impl.LinkedInApiJaxbClient#searchPeople(java.util.Map, com.google.code.linkedinapi.client.enumeration.SearchSortOrder)}.
	 */
    @Test
    public void testSearchPeopleMapOfSearchParameterStringSearchSortOrder() {
        final String searchParameters = TestConstants.LINKED_IN_TEST_SEARCH_PARAMS;
        assertNotNullOrEmpty(String.format(RESOURCE_MISSING_MESSAGE, "Search Parameters"), searchParameters);
        People people = client.searchPeople(getSearchParametersMap(searchParameters), SearchSortOrder.RELEVANCE);
        assertNotNull("People should never be null.", people);
    }

    /**
	 * Test method for {@link com.google.code.linkedinapi.client.impl.LinkedInApiJaxbClient#searchPeople(java.util.Map, int, int, com.google.code.linkedinapi.client.enumeration.SearchSortOrder)}.
	 */
    @Test
    public void testSearchPeopleMapOfSearchParameterStringIntIntSearchSortOrder() {
        final String searchParameters = TestConstants.LINKED_IN_TEST_SEARCH_PARAMS;
        assertNotNullOrEmpty(String.format(RESOURCE_MISSING_MESSAGE, "Search Parameters"), searchParameters);
        People people = client.searchPeople(getSearchParametersMap(searchParameters), 1, 5, SearchSortOrder.RELEVANCE);
        assertNotNull("People should never be null.", people);
    }

    @Test
    public void testSearchPeopleMapOfSearchParameterStringListOfParameterOfFacetTypeString() {
        final String searchParameters = TestConstants.LINKED_IN_TEST_SEARCH_PARAMS;
        assertNotNullOrEmpty(String.format(RESOURCE_MISSING_MESSAGE, "Search Parameters"), searchParameters);
        People people = client.searchPeople(getSearchParametersMap(searchParameters), getPeopleFacets());
        assertNotNull("People should never be null.", people);
    }

    @Test
    public void testSearchPeopleMapOfSearchParameterStringSetOfProfileFieldSetOfFacetFieldListOfParameterOfFacetTypeString() {
        final String searchParameters = TestConstants.LINKED_IN_TEST_SEARCH_PARAMS;
        assertNotNullOrEmpty(String.format(RESOURCE_MISSING_MESSAGE, "Search Parameters"), searchParameters);
        PeopleSearch people = client.searchPeople(getSearchParametersMap(searchParameters), EnumSet.of(ProfileField.FIRST_NAME, ProfileField.LAST_NAME, ProfileField.ID, ProfileField.HEADLINE), EnumSet.of(FacetField.NAME, FacetField.CODE, FacetField.BUCKET_NAME, FacetField.BUCKET_CODE, FacetField.BUCKET_COUNT), getPeopleFacets());
        assertNotNull("People should never be null.", people);
    }

    @Test
    public void testSearchPeopleMapOfSearchParameterStringSetOfProfileFieldSetOfFacetFieldIntIntSearchSortOrderListOfParameterOfFacetTypeString() {
        final String searchParameters = TestConstants.LINKED_IN_TEST_SEARCH_PARAMS;
        assertNotNullOrEmpty(String.format(RESOURCE_MISSING_MESSAGE, "Search Parameters"), searchParameters);
        PeopleSearch people = client.searchPeople(getSearchParametersMap(searchParameters), EnumSet.of(ProfileField.FIRST_NAME, ProfileField.LAST_NAME, ProfileField.ID, ProfileField.HEADLINE), EnumSet.of(FacetField.NAME, FacetField.CODE, FacetField.BUCKET_NAME, FacetField.BUCKET_CODE, FacetField.BUCKET_COUNT), 1, 5, getPeopleFacets());
        assertNotNull("People should never be null.", people);
    }

    /**
	 * Test method for {@link com.google.code.linkedinapi.client.impl.LinkedInApiJaxbClient#sendInviteByEmail(java.lang.String, String, String, java.lang.String, java.lang.String)}.
	 */
    @Test
    public void testSendInviteByEmailStringStringString() {
        final String inviteText = TestConstants.LINKED_IN_TEST_INVITE_TEXT;
        final String inviteSubject = TestConstants.LINKED_IN_TEST_INVITE_SUBJECT;
        final String inviteRecepient = TestConstants.LINKED_IN_TEST_INVITE_EMAIL;
        final String firstName = TestConstants.LINKED_IN_TEST_INVITE_FIRST_NAME;
        final String lastName = TestConstants.LINKED_IN_TEST_INVITE_LAST_NAME;
        assertNotNullOrEmpty(String.format(RESOURCE_MISSING_MESSAGE, "First Name"), firstName);
        assertNotNullOrEmpty(String.format(RESOURCE_MISSING_MESSAGE, "Last Name"), lastName);
        assertNotNullOrEmpty(String.format(RESOURCE_MISSING_MESSAGE, "Invite Text"), inviteText);
        assertNotNullOrEmpty(String.format(RESOURCE_MISSING_MESSAGE, "Invite Subject"), inviteSubject);
        assertNotNullOrEmpty(String.format(RESOURCE_MISSING_MESSAGE, "Invite Recepient"), inviteRecepient);
        client.sendInviteByEmail(inviteRecepient, firstName, lastName, inviteSubject, inviteText);
    }

    /**
	 * Test method for {@link com.google.code.linkedinapi.client.impl.LinkedInApiJaxbClient#sendInviteById(java.lang.String, String, String, java.lang.String, java.lang.String)}.
	 */
    @Test
    public void testSendInviteByIdStringStringString() {
        final String inviteText = TestConstants.LINKED_IN_TEST_INVITE_TEXT;
        final String inviteSubject = TestConstants.LINKED_IN_TEST_INVITE_SUBJECT;
        final String inviteRecepient = TestConstants.LINKED_IN_TEST_INVITE_RECEPIENT_ID;
        final String authHeader = TestConstants.LINKED_IN_TEST_INVITE_AUTH_HEADER;
        assertNotNullOrEmpty(String.format(RESOURCE_MISSING_MESSAGE, "Invite Text"), inviteText);
        assertNotNullOrEmpty(String.format(RESOURCE_MISSING_MESSAGE, "Invite Subject"), inviteSubject);
        assertNotNullOrEmpty(String.format(RESOURCE_MISSING_MESSAGE, "Invite Recepient"), inviteRecepient);
        assertNotNullOrEmpty(String.format(RESOURCE_MISSING_MESSAGE, "Auth Header"), authHeader);
        client.sendInviteById(inviteRecepient, inviteSubject, inviteText, authHeader);
    }

    /**
	 * Test method for {@link com.google.code.linkedinapi.client.impl.LinkedInApiJaxbClient#sendMessage(java.util.List, java.lang.String, java.lang.String)}.
	 */
    @Test
    public void testSendMessage() {
        final String messageText = TestConstants.LINKED_IN_TEST_MESSAGE_TEXT;
        final String messageSubject = TestConstants.LINKED_IN_TEST_MESSAGE_SUBJECT;
        final String messageRecepients = TestConstants.LINKED_IN_TEST_MESSAGE_RECEPIENT_IDS;
        assertNotNullOrEmpty(String.format(RESOURCE_MISSING_MESSAGE, "Message Text"), messageText);
        assertNotNullOrEmpty(String.format(RESOURCE_MISSING_MESSAGE, "Message Subject"), messageSubject);
        assertNotNullOrEmpty(String.format(RESOURCE_MISSING_MESSAGE, "Message Recepient"), messageRecepients);
        client.sendMessage(getRecepientIdsList(messageRecepients), messageSubject, messageText);
    }

    /**
	 * Test method for {@link com.google.code.linkedinapi.client.impl.LinkedInApiJaxbClient#updateCurrentStatus(java.lang.String)}.
	 */
    @Test
    public void testUpdateCurrentStatus() {
        final String statusText = TestConstants.LINKED_IN_TEST_STATUS_TEXT;
        assertNotNullOrEmpty(String.format(RESOURCE_MISSING_MESSAGE, "Status Text"), statusText);
        client.updateCurrentStatus(statusText);
    }

    /**
	 * Test method for {@link com.google.code.linkedinapi.client.impl.LinkedInApiJaxbClient#deleteCurrentStatus()}.
	 */
    @Test
    public void testDeleteCurrentStatus() {
        client.deleteCurrentStatus();
    }

    /**
	 * Test method for {@link com.google.code.linkedinapi.client.impl.LinkedInApiJaxbClient#postComment(java.lang.String, java.lang.String)}.
	 */
    @Test
    public void testLikePost() {
        final String networkUpdateId = TestConstants.LINKED_IN_TEST_NETWORK_UPDATE_ID;
        assertNotNullOrEmpty(String.format(RESOURCE_MISSING_MESSAGE, "Network Update ID"), networkUpdateId);
        client.likePost(networkUpdateId);
    }

    @Test
    public void testPostShare() {
        final String shareText = TestConstants.LINKED_IN_TEST_SHARE_TEXT;
        final String shareUrl = TestConstants.LINKED_IN_TEST_SHARE_URL;
        assertNotNullOrEmpty(String.format(RESOURCE_MISSING_MESSAGE, "Share Text"), shareText);
        assertNotNullOrEmpty(String.format(RESOURCE_MISSING_MESSAGE, "Share URL"), shareUrl);
        client.postShare(shareText, shareText, shareUrl, null, VisibilityType.CONNECTIONS_ONLY);
    }

    @Test
    public void testBookmarkJob() {
        assertNotNullOrEmpty(String.format(RESOURCE_MISSING_MESSAGE, "Job ID"), TestConstants.LINKED_IN_TEST_JOB_ID);
        client.bookmarkJob(TestConstants.LINKED_IN_TEST_JOB_ID);
    }

    @Test
    public void testCloseJob() {
        fail("Not yet implemented");
    }

    @Test
    public void testGetJobBookmarksSetOfJobField() {
        JobBookmarks jobBookmarks = client.getJobBookmarks(EnumSet.allOf(JobField.class));
        assertNotNull("Job Bookmarks should not be null.", jobBookmarks);
    }

    @Test
    public void testGetJobByIdStringSetOfJobField() {
        assertNotNullOrEmpty(String.format(RESOURCE_MISSING_MESSAGE, "Job ID"), TestConstants.LINKED_IN_TEST_JOB_ID);
        Job job = client.getJobById(TestConstants.LINKED_IN_TEST_JOB_ID, EnumSet.allOf(JobField.class));
        assertNotNull("Job should not be null.", job);
    }

    @Test
    public void testGetJobSuggestionsSetOfJobField() {
        Jobs jobSuggestions = client.getJobSuggestions(EnumSet.allOf(JobField.class));
        assertNotNull("Job suggestions should not be null.", jobSuggestions);
    }

    @Test
    public void testPostJob() {
        fail("Not yet implemented");
    }

    @Test
    public void testSearchJobsMapOfSearchParameterStringSetOfJobFieldIntIntSearchSortOrder() {
        final String searchParameters = TestConstants.LINKED_IN_TEST_SEARCH_PARAMS;
        assertNotNullOrEmpty(String.format(RESOURCE_MISSING_MESSAGE, "Search Parameters"), searchParameters);
        JobSearch jobSearch = client.searchJobs(getSearchParametersMap(searchParameters), EnumSet.allOf(JobField.class), EnumSet.allOf(FacetField.class), 1, 5, getJobFacets());
        assertNotNull("Job Search should never be null.", jobSearch);
    }

    @Test
    public void testSearchJobsMapOfSearchParameterStringSetOfJobFieldIntIntSearchSortOrderListOfParameterOfFacetTypeString() {
        fail("Not yet implemented");
    }

    @Test
    public void testSearchJobsMapOfSearchParameterStringSetOfJobFieldSetOfFacetFieldIntIntSearchSortOrder() {
        fail("Not yet implemented");
    }

    @Test
    public void testSearchJobsMapOfSearchParameterStringSetOfJobFieldSetOfFacetFieldIntIntSearchSortOrderListOfParameterOfFacetTypeString() {
        fail("Not yet implemented");
    }

    @Test
    public void testUnbookmarkJob() {
        assertNotNullOrEmpty(String.format(RESOURCE_MISSING_MESSAGE, "Job ID"), TestConstants.LINKED_IN_TEST_JOB_ID);
        client.unbookmarkJob(TestConstants.LINKED_IN_TEST_JOB_ID);
    }

    @Test
    public void testUpdateJob() {
        fail("Not yet implemented");
    }

    @Test
    public void testFollowCompany() {
        assertNotNullOrEmpty(String.format(RESOURCE_MISSING_MESSAGE, "Company ID"), TestConstants.LINKED_IN_TEST_COMPANY_ID);
        client.followCompany(TestConstants.LINKED_IN_TEST_COMPANY_ID);
    }

    @Test
    public void testGetCompaniesByEmailDomainStringSetOfCompanyField() {
        assertNotNullOrEmpty(String.format(RESOURCE_MISSING_MESSAGE, "Company Email Domain"), TestConstants.LINKED_IN_TEST_COMPANY_EMAIL_DOMAIN);
        Companies companies = client.getCompaniesByEmailDomain(TestConstants.LINKED_IN_TEST_COMPANY_EMAIL_DOMAIN, EnumSet.allOf(CompanyField.class));
        assertNotNull("Companies should not be null or empty.", companies);
    }

    @Test
    public void testGetCompanyByIdStringSetOfCompanyField() {
        assertNotNullOrEmpty(String.format(RESOURCE_MISSING_MESSAGE, "Company ID"), TestConstants.LINKED_IN_TEST_COMPANY_ID);
        Company company = client.getCompanyById(TestConstants.LINKED_IN_TEST_COMPANY_ID, EnumSet.allOf(CompanyField.class));
        assertNotNull("Company should not be null or empty.", company);
    }

    @Test
    public void testGetCompanyByUniversalNameStringSetOfCompanyField() {
        assertNotNullOrEmpty(String.format(RESOURCE_MISSING_MESSAGE, "Company Name"), TestConstants.LINKED_IN_TEST_COMPANY_NAME);
        Company company = client.getCompanyByUniversalName(TestConstants.LINKED_IN_TEST_COMPANY_NAME, EnumSet.allOf(CompanyField.class));
        assertNotNull("Company should not be null or empty.", company);
    }

    @Test
    public void testGetCompanyProductsStringSetOfProductFieldIntInt() {
        assertNotNullOrEmpty(String.format(RESOURCE_MISSING_MESSAGE, "Company ID"), TestConstants.LINKED_IN_TEST_COMPANY_ID);
        Products products = client.getCompanyProducts(TestConstants.LINKED_IN_TEST_COMPANY_ID, EnumSet.allOf(ProductField.class), 1, 5);
        assertNotNull("Products should not be null or empty.", products);
    }

    @Test
    public void testGetFollowedCompaniesSetOfCompanyField() {
        Companies companies = client.getFollowedCompanies(EnumSet.allOf(CompanyField.class));
        assertNotNull("Companies should not be null or empty.", companies);
    }

    @Test
    public void testGetSuggestedCompaniesSetOfCompanyField() {
        Companies companies = client.getSuggestedCompanies(EnumSet.allOf(CompanyField.class));
        assertNotNull("Companies should not be null or empty.", companies);
    }

    @Test
    public void testSearchCompaniesMapOfSearchParameterStringSetOfCompanyFieldIntIntSearchSortOrder() {
        final String searchParameters = TestConstants.LINKED_IN_TEST_SEARCH_PARAMS;
        assertNotNullOrEmpty(String.format(RESOURCE_MISSING_MESSAGE, "Search Parameters"), searchParameters);
        CompanySearch companySearch = client.searchCompanies(getSearchParametersMap(searchParameters), EnumSet.allOf(CompanyField.class), EnumSet.allOf(FacetField.class), 1, 5, getCompanyFacets());
        assertNotNull("Company Search should never be null.", companySearch);
    }

    @Test
    public void testSearchCompaniesMapOfSearchParameterStringSetOfCompanyFieldIntIntSearchSortOrderListOfParameterOfFacetTypeString() {
        fail("Not yet implemented");
    }

    @Test
    public void testSearchCompaniesMapOfSearchParameterStringSetOfCompanyFieldSetOfFacetFieldIntIntSearchSortOrder() {
        fail("Not yet implemented");
    }

    @Test
    public void testSearchCompaniesMapOfSearchParameterStringSetOfCompanyFieldSetOfFacetFieldIntIntSearchSortOrderListOfParameterOfFacetTypeString() {
        fail("Not yet implemented");
    }

    @Test
    public void testUnfollowCompany() {
        assertNotNullOrEmpty(String.format(RESOURCE_MISSING_MESSAGE, "Company ID"), TestConstants.LINKED_IN_TEST_COMPANY_ID);
        client.unfollowCompany(TestConstants.LINKED_IN_TEST_COMPANY_ID);
    }

    /**
	 * 
	 */
    protected Date getCurrentDate() {
        return new Date();
    }

    /**
	 * 
	 */
    protected Date getLastWeekDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -7);
        return calendar.getTime();
    }

    /**
	 * 
	 */
    protected Map<SearchParameter, String> getSearchParametersMap(String searchParameters) {
        Map<SearchParameter, String> map = new EnumMap<SearchParameter, String>(SearchParameter.class);
        String[] entries = searchParameters.split(",");
        for (String entry : entries) {
            String[] tuple = entry.split("=");
            if (tuple.length == 2) {
                map.put(SearchParameter.fromString(tuple[0]), tuple[1]);
            }
        }
        return map;
    }

    /**
	 * 
	 */
    protected List<String> getRecepientIdsList(String messageRecepients) {
        return Arrays.asList(messageRecepients.split(","));
    }

    /**
	 * 
	 */
    protected static void assertNotNullOrEmpty(String message, String value) {
        assertNotNull(message, value);
        assertFalse(message, "".equals(value));
    }

    /**
	 * 
	 */
    protected List<Parameter<FacetType, String>> getPeopleFacets() {
        List<Parameter<FacetType, String>> facets = new ArrayList<Parameter<FacetType, String>>();
        facets.add(new Parameter<FacetType, String>(FacetType.NETWORK, RelationshipCodes.OUT_OF_NETWORK_CONNECTIONS));
        facets.add(new Parameter<FacetType, String>(FacetType.NETWORK, RelationshipCodes.SECOND_DEGREE_CONNECTIONS));
        facets.add(new Parameter<FacetType, String>(FacetType.LANGUAGE, LanguageCodes.ENGLISH));
        return facets;
    }

    protected List<Parameter<FacetType, String>> getCompanyFacets() {
        List<Parameter<FacetType, String>> facets = new ArrayList<Parameter<FacetType, String>>();
        facets.add(new Parameter<FacetType, String>(FacetType.NETWORK, RelationshipCodes.OUT_OF_NETWORK_CONNECTIONS));
        facets.add(new Parameter<FacetType, String>(FacetType.NETWORK, RelationshipCodes.SECOND_DEGREE_CONNECTIONS));
        return facets;
    }

    protected List<Parameter<FacetType, String>> getJobFacets() {
        List<Parameter<FacetType, String>> facets = new ArrayList<Parameter<FacetType, String>>();
        facets.add(new Parameter<FacetType, String>(FacetType.JOB_FUNCTION, JobFunctionCode.INFORMATION_TECHNOLOGY.value()));
        facets.add(new Parameter<FacetType, String>(FacetType.JOB_FUNCTION, JobFunctionCode.ENGINEERING.value()));
        return facets;
    }
}
