package org.nex.ts.test.smp;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import org.nex.ts.TopicSpacesException;
import org.nex.ts.server.common.model.Ticket;
import org.nex.ts.server.common.model.Environment;
import org.nex.ts.server.ibis.model.CommonImportJSONParser;
import org.nex.ts.smp.SubjectMapProvider;
import org.nex.ts.smp.api.IMapDataProvider;
import org.nex.ts.smp.api.ISubjectMap;
import junit.framework.TestCase;

/**
 * @author park
 *
 */
public class IBISImportNodeTest extends TestCase {

    private SubjectMapProvider smp;

    private IMapDataProvider database;

    private ISubjectMap map;

    private Environment environment;

    private String username;

    /**
	 * @param name
	 * @param s
	 */
    public IBISImportNodeTest(String name, SubjectMapProvider s) {
        super(name);
        smp = s;
        database = smp.getMapDatabase();
        map = smp.getSubjectMap();
        environment = Environment.getInstance();
        username = "jackpark";
        try {
            setUp();
        } catch (Exception e) {
            smp.logError("IBISImportNodeTest error ", e);
        }
    }

    protected void setUp() throws Exception {
        super.setUp();
        smp.logDebug("IBISImportNodeTest.setUp-");
        Ticket credentials = environment.getUserManager().getTicket(username);
        putIBISConversationJSON(getConversation(), credentials);
        smp.logDebug("IBISImportNodeTest.setUp NEW");
        putNodeJSON(getNewNode(), credentials);
        smp.logDebug("IBISImportNodeTest.setUp UPDATE");
        updateNodeJSON(getUpdateNode(), credentials);
        tearDown();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        smp.logDebug("IBISImportNodeTest.tearDown");
        smp.shutDown();
        System.out.println("IBISImportNodeTest game over.");
    }

    /**
	 * Note: a different author "Joe Sixpack" is inserted to test BaseModel.findOrCreateAuthor
	 * @return
	 */
    private String getConversation() {
        String result = "{ \"url\" : \"http://topicspaces.org/mediawiki/index.php/IBIS_37\"," + " \"type\" : \"2\"," + " \"label\" : \"Evolving Bloomer\"," + " \"details\" : \"Here is the first conversation with this new tool that seeks to explore the many different ways in which Bloomer can become a model for future evolution of hypermedia-based collective sensemaking platforms. This conversation anticipates philosophical, theoretical, technical, and pragmatic issues.\"," + " \"author\" : \"jackpark\"," + " \"nodeId\" : \"IBIS_40\"," + " \"childNodes\": [" + "{ \"url\" : \"http://topicspaces.org/mediawiki/index.php/IBIS_38\"," + "  \"type\" : \"3\"," + "  \"label\" : \"What are the philosophical issues related to online collaboration? \"," + "  \"details\" : \"The question anticipates a possibly large range of issues and philosophical ideas. A Google query on collaboration philosophy  returned more than 8 million hits; a more-refined query might return fewer but more valuable hits.\"," + "  \"author\" : \"Jack Park\"," + "  \"nodeId\" : \"IBIS_41\"" + "}," + "{ \"url\" : \"http://topicspaces.org/mediawiki/index.php/IBIS_39\"," + "  \"type\" : \"3\"," + "  \"label\" : \"What are the theoretical issues related to online, mediated collaboration?\"," + "  \"details\" : \"A Google query on mediated collaboration returns more than one million hits. That serves as a teaser to suggest that theoretical issues, just from the concept of mediated collaboration, is a rich area. But, our own development processes related to the Bloomer project suggest numerous first topics on which to hold conversations.\"," + "  \"author\" : \"Joe Sixpack\"," + "  \"nodeId\" : \"IBIS_42\"" + "}" + " ]}";
        return result;
    }

    private String getNewNode() {
        String result = "{ " + "     \"parentNodeLocator\": \"IBIS_40\"," + "	  \"parentNodeType\": \"2\"," + "	  \"url\" : \"http://topicspaces.org/mediawiki/index.php/IBIS_47\"," + "	  \"type\" : \"3\"," + "	  \"label\" : \"What are the core issues related to online collaboration? \"," + "	  \"details\" : \"The question anticipates mayhem.\"," + "	  \"author\" : \"Joe Sixpack\"," + "	  \"nodeId\" : \"IBIS_47\"" + "}";
        return result;
    }

    private String getUpdateNode() {
        String result = "{ " + "	  \"details\" : \"The question anticipates a possibly large range of issues and philosophical ideas.\"," + "	  \"author\" : \"jackpark\"," + "  \"type\" : \"3\"," + "	  \"nodeId\" : \"IBIS_41\"" + "}";
        return result;
    }

    private String putNodeJSON(String jsonString, Ticket credentials) throws TopicSpacesException {
        String errorMessage = "";
        try {
            JSONObject jo = jsonString2JSONObject(jsonString);
            CommonImportJSONParser p = new CommonImportJSONParser(environment);
            environment.logDebug("IBISImportNodeTest.updateNodeJSON " + p + " " + jo);
            p.parseNode(jo, credentials);
        } catch (Exception e) {
            errorMessage = "WS_IBISHelper.updateNodeJSON error " + e.getMessage();
            environment.logError(errorMessage);
        }
        return errorMessage;
    }

    private String updateNodeJSON(String jsonString, Ticket credentials) throws TopicSpacesException {
        String errorMessage = "";
        try {
            JSONObject jo = jsonString2JSONObject(jsonString);
            CommonImportJSONParser p = new CommonImportJSONParser(environment);
            p.updateNode(jo, credentials);
        } catch (Exception e) {
            errorMessage = "WS_IBISHelper.updateNodeJSON error " + e.getMessage();
            environment.logError(errorMessage);
        }
        return errorMessage;
    }

    private String putIBISConversationJSON(String jsonConversation, Ticket credentials) throws TopicSpacesException {
        String errorMessage = "";
        try {
            JSONObject jo = jsonString2JSONObject(jsonConversation);
            CommonImportJSONParser p = new CommonImportJSONParser(environment);
            p.parseConversation(jo, credentials);
        } catch (Exception e) {
            errorMessage = "WS_IBISHelper.putIBISConversationJSON error " + e.getMessage();
            environment.logError(errorMessage);
        }
        return errorMessage;
    }

    private JSONObject jsonString2JSONObject(String jsonString) {
        JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON(jsonString);
        environment.logDebug("BaseModel.jsonString2JSONObject " + jsonObject);
        return jsonObject;
    }
}
