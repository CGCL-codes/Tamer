package net.sourceforge.etsysync.utils.etsy.api.commands;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Date;
import java.util.Map;
import net.sourceforge.etsysync.utils.etsy.api.resources.EtsyObject;
import net.sourceforge.etsysync.utils.etsy.api.resources.FavoriteUser;
import junit.framework.TestCase;

public class FindAllUserFavoredByTest extends TestCase {

    private String expectedUserId;

    private Integer expectedLimit;

    private Integer expectedOffset;

    private FindAllUserFavoredBy commandWithParameters;

    public void setUp() {
        this.expectedUserId = "TestUser";
        this.expectedLimit = 15;
        this.expectedOffset = 0;
        this.commandWithParameters = new FindAllUserFavoredBy(expectedUserId);
        this.commandWithParameters.setLimit(expectedLimit);
        this.commandWithParameters.setOffset(expectedOffset);
    }

    public void testParameters() {
        Map<String, String> expectedMap = new HashMap<String, String>();
        expectedMap.put("limit", this.expectedLimit.toString());
        expectedMap.put("offset", this.expectedOffset.toString());
        assertEquals(expectedLimit, this.commandWithParameters.getLimit());
        assertEquals(expectedOffset, this.commandWithParameters.getOffset());
        assertEquals(expectedMap, this.commandWithParameters.getParameters());
    }

    public void testImutable() {
        FindAllUserFavoredBy immutable = this.commandWithParameters;
        immutable.setLimit(42);
        immutable.setOffset(87);
        assertEquals(expectedLimit, immutable.getLimit());
        assertEquals(expectedOffset, immutable.getOffset());
    }

    public void testResultCount() {
        int expectedCount = 1;
        StringReader jsonResponseTestFavoriteUser = new StringReader("{\"count\":1,\"results\":[{\"favorite_user_id\":61639436,\"user_id\":10356724,\"target_user_id\":10316168,\"creation_tsz\":1277872624}],\"params\":{\"user_id\":\"10316168\",\"limit\":25,\"offset\":0},\"type\":\"FavoriteUser\"}");
        FindAllUserFavoredBy command = new FindAllUserFavoredBy("");
        command.getEtsyObjectList(jsonResponseTestFavoriteUser);
        assertEquals(expectedCount, command.getResultCount());
    }

    public void testSingleResult() {
        StringReader jsonResponseTestFavoriteUser = new StringReader("{\"count\":1,\"results\":[{\"favorite_user_id\":61639436,\"user_id\":10356724,\"target_user_id\":10316168,\"creation_tsz\":1277872624}],\"params\":{\"user_id\":\"10316168\",\"limit\":25,\"offset\":0},\"type\":\"FavoriteUser\"}");
        FavoriteUser expectedFavoriteUser = new FavoriteUser(61639436, 10356724, 10316168, new Date(1277872624000L));
        List<EtsyObject> expectedFavoriteUserList = new ArrayList<EtsyObject>();
        expectedFavoriteUserList.add(expectedFavoriteUser);
        List<FavoriteUser> testFavoriteUserList = new FindAllUserFavoredBy("").getEtsyObjectList(jsonResponseTestFavoriteUser);
        assertEquals(expectedFavoriteUserList, testFavoriteUserList);
    }

    public void testSingleResultWithoutOptionalUserID() {
        StringReader jsonResponseTestFavoriteUser = new StringReader("{\"count\":1, \"results\":[{\"favorite_user_id\":61639436, \"target_user_id\":10316168, \"creation_tsz\":1277872624}],\"params\":{ \"user_id\":\"10316168\",\"limit\":25, \"offset\":0}, \"type\":\"FavoriteUser\"}");
        FavoriteUser expectedFavoriteUser = new FavoriteUser(61639436, null, 10316168, new Date(1277872624000L));
        List<EtsyObject> expectedFavoriteUserList = new ArrayList<EtsyObject>();
        expectedFavoriteUserList.add(expectedFavoriteUser);
        List<FavoriteUser> testFavoriteUserList = new FindAllUserFavoredBy("").getEtsyObjectList(jsonResponseTestFavoriteUser);
        assertEquals(expectedFavoriteUserList, testFavoriteUserList);
    }

    public void testingMultiple() {
        StringReader jsonResponseTestFavoriteUser = new StringReader("{\"count\":2829,\"results\":[" + "{\"favorite_user_id\":67404443," + "\"user_id\":10543376," + "\"target_user_id\":5373849," + "\"creation_tsz\":1285393797}," + "{\"favorite_user_id\":67397416," + "\"user_id\":7057932," + "\"target_user_id\":5373849," + "\"creation_tsz\":1285384352}," + "{\"favorite_user_id\":67344878," + "\"user_id\":5755575," + "\"target_user_id\":5373849," + "\"creation_tsz\":1285319637}" + "]," + "\"params\":" + "{\"user_id\":\"BethMillner\"," + "\"limit\":25," + "\"offset\":0}," + "\"type\":\"FavoriteUser\"}");
        FavoriteUser expectedFavoriteUserOne = new FavoriteUser(67404443, 10543376, 5373849, new Date(1285393797000L));
        FavoriteUser expectedFavoriteUserTwo = new FavoriteUser(67397416, 7057932, 5373849, new Date(1285384352000L));
        FavoriteUser expectedFavoriteUserThree = new FavoriteUser(67344878, 5755575, 5373849, new Date(1285319637000L));
        List<EtsyObject> expectedFavoriteUserList = new ArrayList<EtsyObject>();
        expectedFavoriteUserList.add(expectedFavoriteUserOne);
        expectedFavoriteUserList.add(expectedFavoriteUserTwo);
        expectedFavoriteUserList.add(expectedFavoriteUserThree);
        List<FavoriteUser> testingFavoriteUserList = new FindAllUserFavoredBy("").getEtsyObjectList(jsonResponseTestFavoriteUser);
        assertEquals(expectedFavoriteUserList, testingFavoriteUserList);
    }

    public void testFavoriteUserMoreThan25() {
        StringReader jsonResponseTestFavoriteUser = new StringReader("{\"count\":2829,\"results\":" + "[{\"favorite_user_id\":67404443,\"user_id\":10543376,\"target_user_id\":5373849,\"creation_tsz\":1285393797}," + "{\"favorite_user_id\":67397416,\"user_id\":7057932,\"target_user_id\":5373849,\"creation_tsz\":1285384352}," + "{\"favorite_user_id\":67387324,\"user_id\":5181473,\"target_user_id\":5373849,\"creation_tsz\":1285372826}," + "{\"favorite_user_id\":67354694,\"user_id\":66297,\"target_user_id\":5373849,\"creation_tsz\":1285338408}," + "{\"favorite_user_id\":67346907,\"user_id\":5740076,\"target_user_id\":5373849,\"creation_tsz\":1285326101}," + "{\"favorite_user_id\":67345963,\"target_user_id\":5373849,\"creation_tsz\":1285323190}," + "{\"favorite_user_id\":67345906,\"user_id\":5126270,\"target_user_id\":5373849,\"creation_tsz\":1285322968}," + "{\"favorite_user_id\":67345841,\"target_user_id\":5373849,\"creation_tsz\":1285322803}," + "{\"favorite_user_id\":67345821,\"user_id\":9873297,\"target_user_id\":5373849,\"creation_tsz\":1285322760}," + "{\"favorite_user_id\":67345766,\"user_id\":10916202,\"target_user_id\":5373849,\"creation_tsz\":1285322594}," + "{\"favorite_user_id\":67345646,\"user_id\":10297944,\"target_user_id\":5373849,\"creation_tsz\":1285322220}," + "{\"favorite_user_id\":67345602,\"user_id\":10367664,\"target_user_id\":5373849,\"creation_tsz\":1285322069}," + "{\"favorite_user_id\":67345581,\"user_id\":8939470,\"target_user_id\":5373849,\"creation_tsz\":1285321987}," + "{\"favorite_user_id\":67345579,\"user_id\":10996146,\"target_user_id\":5373849,\"creation_tsz\":1285321978}," + "{\"favorite_user_id\":67345514,\"user_id\":7946150,\"target_user_id\":5373849,\"creation_tsz\":1285321774}," + "{\"favorite_user_id\":67345424,\"user_id\":5741160,\"target_user_id\":5373849,\"creation_tsz\":1285321475}," + "{\"favorite_user_id\":67345423,\"user_id\":8961166,\"target_user_id\":5373849,\"creation_tsz\":1285321473}," + "{\"favorite_user_id\":67345420,\"user_id\":9657997,\"target_user_id\":5373849,\"creation_tsz\":1285321453}," + "{\"favorite_user_id\":67345404,\"user_id\":5388083,\"target_user_id\":5373849,\"creation_tsz\":1285321413}," + "{\"favorite_user_id\":67345329,\"user_id\":7881085,\"target_user_id\":5373849,\"creation_tsz\":1285321119}," + "{\"favorite_user_id\":67345312,\"user_id\":8359442,\"target_user_id\":5373849,\"creation_tsz\":1285321077}," + "{\"favorite_user_id\":67345146,\"user_id\":8629383,\"target_user_id\":5373849,\"creation_tsz\":1285320474}," + "{\"favorite_user_id\":67345116,\"user_id\":8450061,\"target_user_id\":5373849,\"creation_tsz\":1285320376}," + "{\"favorite_user_id\":67345062,\"user_id\":6650,\"target_user_id\":5373849,\"creation_tsz\":1285320221}," + "{\"favorite_user_id\":67344878,\"user_id\":5755575,\"target_user_id\":5373849,\"creation_tsz\":1285319637}]," + "\"params\":{\"user_id\":\"BethMillner\",\"limit\":25,\"offset\":0},\"type\":\"FavoriteUser\"}");
        FavoriteUser expectedFavoriteUser1 = new FavoriteUser(67404443, 10543376, 5373849, new Date(1285393797000L));
        FavoriteUser expectedFavoriteUser2 = new FavoriteUser(67397416, 7057932, 5373849, new Date(1285384352000L));
        FavoriteUser expectedFavoriteUser3 = new FavoriteUser(67387324, 5181473, 5373849, new Date(1285372826000L));
        FavoriteUser expectedFavoriteUser4 = new FavoriteUser(67354694, 66297, 5373849, new Date(1285338408000L));
        FavoriteUser expectedFavoriteUser5 = new FavoriteUser(67346907, 5740076, 5373849, new Date(1285326101000L));
        FavoriteUser expectedFavoriteUser6 = new FavoriteUser(67345963, null, 5373849, new Date(1285323190000L));
        FavoriteUser expectedFavoriteUser7 = new FavoriteUser(67345906, 5126270, 5373849, new Date(1285322968000L));
        FavoriteUser expectedFavoriteUser8 = new FavoriteUser(67345841, null, 5373849, new Date(1285322803000L));
        FavoriteUser expectedFavoriteUser9 = new FavoriteUser(67345821, 9873297, 5373849, new Date(1285322760000L));
        FavoriteUser expectedFavoriteUser10 = new FavoriteUser(67345766, 10916202, 5373849, new Date(1285322594000L));
        FavoriteUser expectedFavoriteUser11 = new FavoriteUser(67345646, 10297944, 5373849, new Date(1285322220000L));
        FavoriteUser expectedFavoriteUser12 = new FavoriteUser(67345602, 10367664, 5373849, new Date(1285322069000L));
        FavoriteUser expectedFavoriteUser13 = new FavoriteUser(67345581, 8939470, 5373849, new Date(1285321987000L));
        FavoriteUser expectedFavoriteUser14 = new FavoriteUser(67345579, 10996146, 5373849, new Date(1285321978000L));
        FavoriteUser expectedFavoriteUser15 = new FavoriteUser(67345514, 7946150, 5373849, new Date(1285321774000L));
        FavoriteUser expectedFavoriteUser16 = new FavoriteUser(67345424, 5741160, 5373849, new Date(1285321475000L));
        FavoriteUser expectedFavoriteUser17 = new FavoriteUser(67345423, 8961166, 5373849, new Date(1285321473000L));
        FavoriteUser expectedFavoriteUser18 = new FavoriteUser(67345420, 9657997, 5373849, new Date(1285321453000L));
        FavoriteUser expectedFavoriteUser19 = new FavoriteUser(67345404, 5388083, 5373849, new Date(1285321413000L));
        FavoriteUser expectedFavoriteUser20 = new FavoriteUser(67345329, 7881085, 5373849, new Date(1285321119000L));
        FavoriteUser expectedFavoriteUser21 = new FavoriteUser(67345312, 8359442, 5373849, new Date(1285321077000L));
        FavoriteUser expectedFavoriteUser22 = new FavoriteUser(67345146, 8629383, 5373849, new Date(1285320474000L));
        FavoriteUser expectedFavoriteUser23 = new FavoriteUser(67345116, 8450061, 5373849, new Date(1285320376000L));
        FavoriteUser expectedFavoriteUser24 = new FavoriteUser(67345062, 6650, 5373849, new Date(1285320221000L));
        FavoriteUser expectedFavoriteUser25 = new FavoriteUser(67344878, 5755575, 5373849, new Date(1285319637000L));
        List<EtsyObject> expectedFavoriteUserList = new ArrayList<EtsyObject>();
        expectedFavoriteUserList.add(expectedFavoriteUser1);
        expectedFavoriteUserList.add(expectedFavoriteUser2);
        expectedFavoriteUserList.add(expectedFavoriteUser3);
        expectedFavoriteUserList.add(expectedFavoriteUser4);
        expectedFavoriteUserList.add(expectedFavoriteUser5);
        expectedFavoriteUserList.add(expectedFavoriteUser6);
        expectedFavoriteUserList.add(expectedFavoriteUser7);
        expectedFavoriteUserList.add(expectedFavoriteUser8);
        expectedFavoriteUserList.add(expectedFavoriteUser9);
        expectedFavoriteUserList.add(expectedFavoriteUser10);
        expectedFavoriteUserList.add(expectedFavoriteUser11);
        expectedFavoriteUserList.add(expectedFavoriteUser12);
        expectedFavoriteUserList.add(expectedFavoriteUser13);
        expectedFavoriteUserList.add(expectedFavoriteUser14);
        expectedFavoriteUserList.add(expectedFavoriteUser15);
        expectedFavoriteUserList.add(expectedFavoriteUser16);
        expectedFavoriteUserList.add(expectedFavoriteUser17);
        expectedFavoriteUserList.add(expectedFavoriteUser18);
        expectedFavoriteUserList.add(expectedFavoriteUser19);
        expectedFavoriteUserList.add(expectedFavoriteUser20);
        expectedFavoriteUserList.add(expectedFavoriteUser21);
        expectedFavoriteUserList.add(expectedFavoriteUser22);
        expectedFavoriteUserList.add(expectedFavoriteUser23);
        expectedFavoriteUserList.add(expectedFavoriteUser24);
        expectedFavoriteUserList.add(expectedFavoriteUser25);
        List<FavoriteUser> testFavoriteUserList = new FindAllUserFavoredBy("").getEtsyObjectList(jsonResponseTestFavoriteUser);
        for (int n = 0; n < expectedFavoriteUserList.size(); n++) {
            assertEquals(String.format("difference in user at intex:[%s]", n), expectedFavoriteUserList.get(n), testFavoriteUserList.get(n));
        }
    }
}
