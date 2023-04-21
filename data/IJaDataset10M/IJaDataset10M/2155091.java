package atai.questions.reg;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import atai.questions.*;

public class ATQuestion_1_06_01_A extends ATQuestion {

    /**
	 * Area of the question.
	 */
    private static String area = "1";

    /**
	 * Aspect of the question.
	 */
    private static String aspect = "06";

    /**
	 * Id of the question.
	 */
    private static String id = "01_A";

    /**
	 * General textual description of the question.
	 */
    private static String description = "The component will introduce changes " + "in the current work practices of the actor and his environment. " + "Among existing goals, what ones do you consider that must not suffer modifications?";

    /**
	 * The question of this class.
	 */
    private static ATGraph question;

    /**
	 * The question with the variable elements.
	 * The final statement of the question is:
	 * textualQuestion.get(0) + textualQuestion.get(1) + textualQuestion.get(2)...
	 * where pair indexes are fixed and odd ones are the name of variable elements
	 * of the question from <b>questionVariables</b>.
	 */
    private static List<String> textualQuestion;

    /**
	 * The singleton instance of this class.
	 */
    static ATQuestion_1_06_01_A theInstance = null;

    static {
        Map<String, ATEntity> entityTypes = new HashMap<String, ATEntity>();
        entityTypes.put("Group", ATEntity.Community);
        entityTypes.put("Key Goal", ATEntity.Objective);
        entityTypes.put("Component Goal", ATEntity.Objective);
        entityTypes.put("Component", ATEntity.Artifact);
        Map<String, ATRelation> relationTypes = new HashMap<String, ATRelation>();
        relationTypes.put("Pursue: Group -> Key Goal", ATRelation.Pursue);
        relationTypes.put("Pursue: Component -> Component Goal", ATRelation.Pursue);
        relationTypes.put("Essential: Key Goal -> Component Goal", ATRelation.Essential);
        Map<String, List<String>> roles = new HashMap<String, List<String>>();
        Map<String, String> roleTypes = new HashMap<String, String>();
        for (String relationName : relationTypes.keySet()) {
            List<String> relationRoles = new Vector<String>();
            relationRoles.add(relationName + "::" + Graph.ROLE_ORIGIN);
            relationRoles.add(relationName + "::" + Graph.ROLE_TARGET);
            roles.put(relationName, relationRoles);
            roleTypes.put(relationName + "::" + Graph.ROLE_ORIGIN, Graph.ROLE_ORIGIN);
            roleTypes.put(relationName + "::" + Graph.ROLE_TARGET, Graph.ROLE_TARGET);
        }
        Map<String, String> players = new HashMap<String, String>();
        players.put("Pursue: Group -> Key Goal" + "::" + Graph.ROLE_ORIGIN, "Group");
        players.put("Pursue: Group -> Key Goal" + "::" + Graph.ROLE_TARGET, "Key Goal");
        players.put("Pursue: Component -> Component Goal" + "::" + Graph.ROLE_ORIGIN, "Component");
        players.put("Pursue: Component -> Component Goal" + "::" + Graph.ROLE_TARGET, "Component Goal");
        players.put("Essential: Key Goal -> Component Goal" + "::" + Graph.ROLE_ORIGIN, "Key Goal");
        players.put("Essential: Key Goal -> Component Goal" + "::" + Graph.ROLE_TARGET, "Component Goal");
        List<String> variables = new Vector<String>();
        for (String entityName : entityTypes.keySet()) variables.add(entityName);
        question = new ATGraph(ATGraph.LANGUAGE_ATGRAPH, entityTypes, relationTypes, roles, roleTypes, players, variables);
        textualQuestion = new Vector<String>();
        textualQuestion.add("For the");
        textualQuestion.add("Group");
        textualQuestion.add(", it is essential the");
        textualQuestion.add("Key Goal");
        textualQuestion.add("being supported. The");
        textualQuestion.add("Component");
        textualQuestion.add("can consider this including in its design the purpose of");
        textualQuestion.add("Component Goal");
        textualQuestion.add(".");
    }

    protected ATQuestion_1_06_01_A() {
        super();
        this.setArea(ATQuestion_1_06_01_A.area);
        this.setAspect(ATQuestion_1_06_01_A.aspect);
        this.setId(ATQuestion_1_06_01_A.id);
        this.setDescription(ATQuestion_1_06_01_A.description);
        this.setQuestion(ATQuestion_1_06_01_A.question);
        this.setTextualQuestion(ATQuestion_1_06_01_A.textualQuestion);
    }

    public static ATQuestion_1_06_01_A getInstance() {
        if (ATQuestion_1_06_01_A.theInstance == null) ATQuestion_1_06_01_A.theInstance = new ATQuestion_1_06_01_A();
        return ATQuestion_1_06_01_A.theInstance;
    }
}
