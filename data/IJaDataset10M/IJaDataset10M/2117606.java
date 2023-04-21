package atai.questions.regGraph;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import ingenias.exception.NullEntity;
import ingenias.generator.browser.*;
import atai.questions.*;
import atai.questions.reg.*;

public class ATQuestionGraph_1_06_01_B extends ATQuestionGraph {

    /**
	 * @param questionBrowser
	 */
    public ATQuestionGraph_1_06_01_B(Browser questionBrowser) {
        super(ATQuestion_1_06_01_B.getInstance(), questionBrowser);
    }

    /**
	 * A method that uses the browser to get the potential answers
	 * to this question.
	 * @todo
	 * FALTAN POSIBILIDADES
	 */
    public void solvePotentialAnswers() {
        ATView.initializeBrowser(this.getQuestionBrowser());
        ATView basicView = ATView.getTheInstance();
        List<Map<String, String>> potentialAnswersTmp = new Vector<Map<String, String>>();
        List<Map<String, AttributedElement>> potentialAnswersElements = new Vector<Map<String, AttributedElement>>();
        List<String> potentialActors = basicView.getEntitiesForATRole(ATEntity.Subject);
        List<String> potentialObjectives = basicView.getEntitiesForATRole(ATEntity.Objective);
        List<String> potentialArtifacts = basicView.getEntitiesForATRole(ATEntity.Artifact);
        GraphEntity oneActor = null;
        GraphEntity oneKeyGoal = null;
        GraphEntity oneComponentGoal = null;
        GraphEntity oneComponent = null;
        GraphRelationship onePursue1 = null;
        GraphRelationship onePursue2 = null;
        GraphRelationship oneEssential = null;
        GraphEntity[] entities = this.getQuestionBrowser().getAllEntities();
        for (int i_1 = 0; i_1 < potentialActors.size(); i_1++) {
            oneActor = null;
            for (int j = 0; oneActor == null && j < entities.length; j++) if (entities[j].getID().equalsIgnoreCase(potentialActors.get(i_1))) oneActor = entities[j];
            for (int i_2 = 0; i_2 < potentialObjectives.size(); i_2++) {
                oneKeyGoal = null;
                for (int j = 0; oneKeyGoal == null && j < entities.length; j++) if (entities[j].getID().equalsIgnoreCase(potentialObjectives.get(i_2))) oneKeyGoal = entities[j];
                onePursue1 = this.isConnected(oneActor, oneKeyGoal, ATRelation.Pursue);
                if (onePursue1 != null) {
                    for (int i_3 = 0; i_3 < potentialObjectives.size(); i_3++) {
                        oneComponentGoal = null;
                        for (int j = 0; oneComponentGoal == null && j < entities.length; j++) if (entities[j].getID().equalsIgnoreCase(potentialObjectives.get(i_3))) oneComponentGoal = entities[j];
                        oneEssential = this.isConnected(oneKeyGoal, oneComponentGoal, ATRelation.Essential);
                        if (oneEssential != null) {
                            for (int i_4 = 0; i_4 < potentialArtifacts.size(); i_4++) {
                                oneComponent = null;
                                for (int j = 0; oneComponent == null && j < entities.length; j++) if ((i_1 != i_4) && entities[j].getID().equalsIgnoreCase(potentialArtifacts.get(i_4))) oneComponent = entities[j];
                                if (oneComponent != null) onePursue2 = this.isConnected(oneComponent, oneComponentGoal, ATRelation.Pursue); else onePursue2 = null;
                                if (onePursue1 != null && onePursue2 != null && oneEssential != null) {
                                    Map<String, String> mapTmp = new HashMap<String, String>();
                                    Map<String, AttributedElement> graphTmp = new HashMap<String, AttributedElement>();
                                    mapTmp.put("Actor", oneActor.getID());
                                    graphTmp.put("Actor", oneActor);
                                    mapTmp.put("Pursue: Actor -> Key Goal", onePursue1.getID());
                                    graphTmp.put("Pursue: Actor -> Key Goal", onePursue1);
                                    mapTmp.put("Key Goal", oneKeyGoal.getID());
                                    graphTmp.put("Key Goal", oneKeyGoal);
                                    mapTmp.put("Essential: Key Goal -> Component Goal", oneEssential.getID());
                                    graphTmp.put("Essential: Key Goal -> Component Goal", oneEssential);
                                    mapTmp.put("Component Goal", oneComponentGoal.getID());
                                    graphTmp.put("Component Goal", oneComponentGoal);
                                    mapTmp.put("Pursue: Component -> Component Goal", onePursue2.getID());
                                    graphTmp.put("Pursue: Component -> Component Goal", onePursue2);
                                    mapTmp.put("Component", oneComponent.getID());
                                    graphTmp.put("Component", oneComponent);
                                    this.completeRoles(mapTmp);
                                    potentialAnswersTmp.add(mapTmp);
                                    potentialAnswersElements.add(graphTmp);
                                }
                            }
                        }
                    }
                }
            }
        }
        this.setPotentialAnswers(potentialAnswersTmp);
        this.setPotentialAnswersElements(potentialAnswersElements);
        this.completePotentialAnswers(potentialAnswersTmp.size());
    }
}
