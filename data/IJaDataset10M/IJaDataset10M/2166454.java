package edu.url.lasalle.campus.scorm2004rte.server.validator.concreteParsers;

import java.util.Iterator;
import java.util.Vector;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import org.w3c.dom.Node;
import edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.Elements.Objective;
import edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.Elements.ObjectiveCluster;
import edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.Elements.ObjectiveHandler;
import edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.Elements.Sequencing;
import edu.url.lasalle.campus.scorm2004rte.server.DataBase.DynamicData.CMIDataModel;
import edu.url.lasalle.campus.scorm2004rte.server.DataBase.DynamicData.CMIObjectives;
import edu.url.lasalle.campus.scorm2004rte.server.DataBase.DynamicData.ClusterDataModel;
import edu.url.lasalle.campus.scorm2004rte.server.DataBase.DynamicData.ClusterMap;
import edu.url.lasalle.campus.scorm2004rte.system.Constants;
import edu.url.lasalle.campus.scorm2004rte.server.validator.DOMTreeUtility;
import edu.url.lasalle.campus.scorm2004rte.server.validator.concreteParsers.sequencingChilds.*;

/**
 * $Id: ParseSequencing.java,v 1.15 2008/01/08 11:50:21 ecespedes Exp $
 * 
 * Aquesta classe parseja el seq�enciament i crida les subclasses corresponents
 * a cada element del seq�enciament (controlMode, Objectives, 
 * ConditionRule, etc).
 * 
 * @author ecespedes
 * @version Versi� $Revision: 1.15 $ $Date: 2008/01/08 11:50:21 $
 * $Log: ParseSequencing.java,v $
 * Revision 1.15  2008/01/08 11:50:21  ecespedes
 * Ultimes modificacions de l'OverallSequencingProcess
 *
 * Revision 1.14  2008/01/07 15:59:22  ecespedes
 * Implementat 'deliveryControls' i el 'hideLMSUI' del Presentation.
 *
 * Revision 1.13  2007/12/17 15:27:48  ecespedes
 * Fent MapInfo. Bug en els Leaf_Items
 *
 * Revision 1.12  2007/12/09 22:32:16  ecespedes
 * Els objectius dels clusters es tracten i es guarden de manera diferent.
 *
 * Revision 1.11  2007/12/04 15:47:14  ecespedes
 * limitConditions implementat i preConditions testejat.
 *
 * Revision 1.10  2007/11/29 15:54:35  ecespedes
 * Implementant les SequencingRules (part 1)
 *
 * Revision 1.9  2007/11/27 15:34:25  ecespedes
 * Arreglats bugs relacionats amb el Rollup. Creat nou joc de testos.
 *
 * Revision 1.8  2007/11/19 07:34:15  ecespedes
 * Millores en els RollupRules.
 *
 * Revision 1.7  2007/11/15 15:24:04  ecespedes
 * Implementat el ObjectiveInterface en els Leaf_Item.
 *
 * Revision 1.6  2007/11/14 12:33:36  ecespedes
 * Implementada l'interface: ObjectiveInterface, de manera que ara totes
 * les classes vinculades al seq�enciament l'implementen.
 *
 * Revision 1.5  2007/11/13 15:59:59  ecespedes
 * Treballant sobre el sistema per "linkar" els Objectives de l'estructura
 * Sequencing de l'arbre amb els Objectives de l'usuari.
 * Millorat TreeAnnotations (step 2 de 3)
 *
 * Revision 1.4  2007/10/30 14:07:42  ecespedes
 * Arreglat tots els bugs relacionats amb l'UserObjective.
 * Comen�ant a crear el sistema de gesti� dels cursos:
 * - CourseAdministrator i CourseManager.
 *
 */
public class ParseSequencing {

    /**
	 * Constant Type, "controlMode".
	 */
    private static final String CONTROLMODE = "controlMode";

    /**
	 * Constant Type, "limitConditions".
	 */
    private static final String LIMITCONDITIONS = "limitConditions";

    /**
	 * Constant Type, "rollupRules".
	 */
    private static final String ROLLUPRULES = "rollupRules";

    /**
	 * Constant Type, "randomizationControls".
	 */
    private static final String RANDOMIZATIONCONTROLS = "randomizationControls";

    /**
	 * Constant Type, "auxiliaryResources".
	 */
    private static final String AUXILIARYRESOURCES = "auxiliaryResources";

    /**
	 * Constant Type, "deliveryControls".
	 */
    private static final String DELIVERYCONTROLS = "deliveryControls";

    /**
	 * Constant Type, "rollupConsiderations".
	 */
    private static final String ROLLUPCONSIDERATIONS = "rollupConsiderations";

    /**
	 * Constant Type, "sequencingRules".
	 */
    private static final String SEQUENCINGRULES = "sequencingRules";

    /**
	 * La variable que identifica l'Item en el que estem.
	 * L'hi hem de fer arribar a l'Objective.
	 */
    private String itemID;

    /**
	 * Aquesta variable ens indicar� si REALMENT hem trobat 
	 * alguna cosa (etiqueta, regla, rollup, etc) de seq�enciament,
	 * o si pel contrari estem retornant el seq�enciament per 
	 * defecte.
	 */
    private boolean hasReallyANYSequencing = false;

    /**
	 * Guardarem totes les regles del seq�enciament dintre de la 
	 * classe Sequencing. 
	 */
    public Sequencing sequencing = new Sequencing();

    /**
	 * Ens servir� per indicar si t� Objectives o no.
	 */
    private boolean hasObjectives = false;

    /**
	 * (ParseObjectives Type).
	 */
    private ParseObjectives parseObjectives;

    /**
	 * �s el parsejador del seq�enciament.
	 * 
	 * @param currentItemID : String Type; L'identificador de l'Item "pare".
	 * @param sequencingNode : (Node Type)
	 * @param cmiDataModel : (CMIDataModel Type)
	 */
    public ParseSequencing(final Node sequencingNode, final CMIDataModel cmiDataModel, final ClusterMap clusterMap, final String currentItemID, final boolean isLeafItem, final ClusterMap globalMap) {
        itemID = currentItemID;
        String iD = DOMTreeUtility.getAttributeValue(sequencingNode, Constants.ID);
        if (iD.length() == 0) {
            if (Constants.DEBUG_WARNINGS_LOW && Constants.DEBUG_SEQUENCING) {
                System.out.println("Atribut ID no trobat.");
            }
        } else {
            hasReallyANYSequencing = true;
            sequencing.ID = iD;
        }
        String iDRef = DOMTreeUtility.getAttributeValue(sequencingNode, Constants.IDREF);
        if (iDRef.length() == 0) {
            if (Constants.DEBUG_WARNINGS_LOW && Constants.DEBUG_SEQUENCING) {
                System.out.println("[WARNING]Atribut IDRef no trobat.");
            }
        } else {
            hasReallyANYSequencing = true;
            sequencing.IDRef = iDRef;
        }
        if (Constants.DEBUG_INFO || Constants.DEBUG_SEQUENCING) {
            System.out.println("[Sequencing\tID:" + iD + "\tIDRef:" + iDRef);
        }
        Vector seqInSequencing = DOMTreeUtility.getALLNodes(sequencingNode);
        if (seqInSequencing != null) {
            for (Iterator sequencingIterator = seqInSequencing.iterator(); sequencingIterator.hasNext(); ) {
                Node ssNode = (Node) sequencingIterator.next();
                if (ssNode == null) {
                    if (Constants.DEBUG_ERRORS || Constants.DEBUG_SEQUENCING) {
                        System.out.println("[ERROR] " + "a l'agafar un fill dintre del Sequencing!!");
                    }
                } else {
                    hasReallyANYSequencing = true;
                    analizeNode(cmiDataModel, clusterMap, isLeafItem, ssNode, globalMap);
                }
            }
        }
    }

    /**
	 * Analitza els "fills" del seq�enciament i 
	 * crida als parsejadors concrets.
	 * @param childNode : (Node Type)
	 */
    private void analizeNode(final CMIDataModel cmiDataModel, final ClusterMap clusterMap, final boolean isLeafItem, final Node childNode, final ClusterMap globalMap) {
        String childName = childNode.getLocalName();
        if (Constants.DEBUG_INFO || Constants.DEBUG_SEQUENCING) {
            System.out.println("\t[Sequencing child]: " + childName);
        }
        if (childName.equals(CONTROLMODE)) {
            ParseControlMode controlMode = new ParseControlMode(childNode);
            sequencing.adlseqControlMode = controlMode.adlseqControlMode;
        } else if (childName.equals(Constants.OBJECTIVES)) {
            sequencing.objectiveHandler = new ObjectiveHandler();
            parseObjectives = new ParseObjectives(childNode, itemID);
            if (isLeafItem) {
                sequencing.objectiveHandler = parseObjectives.getObjectiveHandler();
            } else {
                ObjectiveCluster tmpObjCluster = new ObjectiveCluster();
                Objective tmpObjective = (Objective) parseObjectives.getObjectiveHandler().getPrimaryObjective();
                tmpObjCluster.itemID = tmpObjective.itemID;
                tmpObjCluster.ObjectiveID = tmpObjective.ObjectiveID;
                tmpObjCluster.minNormalizedMeasure = tmpObjective.minNormalizedMeasure;
                tmpObjCluster.satisfiedByMeasure = tmpObjective.satisfiedByMeasure;
                tmpObjCluster.hasMapInfo = tmpObjective.hasMapInfo;
                tmpObjCluster.mapInfo = tmpObjective.mapInfo;
                if (tmpObjCluster.mapInfo != null) {
                    if (tmpObjCluster.mapInfo.targetObjectiveID != null) {
                        globalMap.objective.put(tmpObjCluster.mapInfo.targetObjectiveID, new ClusterDataModel());
                    }
                }
                sequencing.objectiveHandler.setPrimaryObjective(tmpObjCluster);
                for (Iterator it = parseObjectives.getObjectiveHandler().getObjectivesIterator(); it.hasNext(); ) {
                    ObjectiveCluster tmpSC = new ObjectiveCluster();
                    Objective tmpSO = (Objective) it.next();
                    tmpSC.itemID = tmpSO.itemID;
                    tmpSC.ObjectiveID = tmpSO.ObjectiveID;
                    tmpSC.minNormalizedMeasure = tmpSO.minNormalizedMeasure;
                    tmpSC.satisfiedByMeasure = tmpSO.satisfiedByMeasure;
                    tmpSC.hasMapInfo = tmpSO.hasMapInfo;
                    tmpSC.mapInfo = tmpSO.mapInfo;
                    sequencing.objectiveHandler.addObjective(tmpSC.ObjectiveID, tmpSC);
                }
            }
            hasObjectives = true;
            CMIObjectives nouCMIObjectives = null;
            if (isLeafItem) {
                Objective tmpObjective = (Objective) sequencing.objectiveHandler.getPrimaryObjective();
                nouCMIObjectives = new CMIObjectives();
                nouCMIObjectives.id = tmpObjective.ObjectiveID;
                cmiDataModel.cmiObjectives.put(nouCMIObjectives.id, nouCMIObjectives);
            } else {
                ObjectiveCluster tmpObjective = (ObjectiveCluster) sequencing.objectiveHandler.getPrimaryObjective();
                ClusterDataModel tmpClusterDM = new ClusterDataModel();
                clusterMap.objective.put(tmpObjective.ObjectiveID, tmpClusterDM);
            }
            for (Iterator iteratorObjectives = sequencing.objectiveHandler.getObjectivesIterator(); iteratorObjectives.hasNext(); ) {
                if (isLeafItem) {
                    Objective tmpObjective = (Objective) iteratorObjectives.next();
                    nouCMIObjectives = new CMIObjectives();
                    nouCMIObjectives.id = tmpObjective.ObjectiveID;
                    cmiDataModel.cmiObjectives.put(nouCMIObjectives.id, nouCMIObjectives);
                    if (tmpObjective.mapInfo != null) {
                        if (tmpObjective.mapInfo.targetObjectiveID != null) {
                            globalMap.objective.put(tmpObjective.mapInfo.targetObjectiveID, new ClusterDataModel());
                        }
                    }
                } else {
                    ObjectiveCluster tmpObjective = (ObjectiveCluster) iteratorObjectives.next();
                    ClusterDataModel tmpClusterDM = new ClusterDataModel();
                    clusterMap.objective.put(tmpObjective.ObjectiveID, tmpClusterDM);
                    if (tmpObjective.mapInfo != null) {
                        if (tmpObjective.mapInfo.targetObjectiveID != null) {
                            globalMap.objective.put(tmpObjective.mapInfo.targetObjectiveID, new ClusterDataModel());
                        }
                    }
                }
            }
        } else if (childName.equals(SEQUENCINGRULES)) {
            ParseConditionRule nouParseConditionRule = new ParseConditionRule(childNode, sequencing);
            sequencing = nouParseConditionRule.getSequencingObject();
        } else if (childName.equals(ROLLUPRULES)) {
            ParseRollup nouParseRollup = new ParseRollup(childNode, sequencing);
            sequencing = nouParseRollup.getSequencingObject();
        } else if (childName.equals(LIMITCONDITIONS)) {
            String attemptLimit = DOMTreeUtility.getAttributeValue(childNode, "attemptLimit");
            if (attemptLimit.length() == 0) {
                if (Constants.DEBUG_WARNINGS_LOW && Constants.DEBUG_SEQUENCING) {
                    System.out.println("Atribut attemptLimit no trobat.");
                }
            } else {
                sequencing.attemptLimit = new Integer(attemptLimit).intValue();
            }
            String attemptAbsoluteDurationLimit = DOMTreeUtility.getAttributeValue(childNode, "attemptAbsoluteDurationLimit");
            if (attemptAbsoluteDurationLimit.length() == 0) {
                if (Constants.DEBUG_WARNINGS_LOW && Constants.DEBUG_SEQUENCING) {
                    System.out.println("Atribut " + "attemptAbsoluteDurationLimit no trobat.");
                }
            } else {
                try {
                    sequencing.attemptAbsoluteDurationLimit = DatatypeFactory.newInstance().newDuration(attemptAbsoluteDurationLimit);
                } catch (DatatypeConfigurationException e) {
                    if (Constants.DEBUG_ERRORS) {
                        System.out.println("[ERROR] ParseSequencing:" + e.getMessage());
                    }
                    sequencing.attemptAbsoluteDurationLimit = null;
                }
            }
            if (Constants.DEBUG_INFO || Constants.DEBUG_SEQUENCING) {
                System.out.println("\t[LimitConditions-START]\n\t\t" + "attemptLimit: " + sequencing.attemptLimit + "\n\t\t" + "attemptAbsoluteDurationLimit: " + sequencing.attemptAbsoluteDurationLimit + "\n\t" + "[LimitConditions-END]");
            }
        } else if (childName.equals(RANDOMIZATIONCONTROLS)) {
            ParseRandomizeControls nouParseRandomizeControls = new ParseRandomizeControls(childNode, sequencing);
            if (!nouParseRandomizeControls.getIsAllCorrect()) {
                if (Constants.DEBUG_ERRORS || Constants.DEBUG_SEQUENCING) {
                    System.out.println("[ERROR] en RandomizeControls");
                }
            } else {
                sequencing = nouParseRandomizeControls.sequencing;
                if (Constants.DEBUG_INFO || Constants.DEBUG_SEQUENCING) {
                    System.out.println("\t[RandomizationControls-START]\n\t\t" + "reorderChildren: " + sequencing.reorderChildren + "\n\t\t" + "randomizationTiming: " + sequencing.randomizationTiming.toString() + "\n\t\t" + "selectCount: " + sequencing.selectCount + "\n\t" + "[RandomizationControls-END]");
                }
            }
        } else if (childName.equals(ROLLUPCONSIDERATIONS)) {
            ParseRollupConsiderations nouParseRollupConsiderations = new ParseRollupConsiderations(childNode);
            if (nouParseRollupConsiderations.getIsAllCorrect()) {
                sequencing.rollupConsiderations = nouParseRollupConsiderations.rollupConsiderations;
                if (Constants.DEBUG_INFO || Constants.DEBUG_SEQUENCING) {
                    System.out.println("\t[RollupConsiderations-START]\n\t\t" + "measureSatisfactionIfActive: " + sequencing.rollupConsiderations.measureSatisfactionIfActive + "\n\t\t" + "requiredForCompleted: " + sequencing.rollupConsiderations.requiredForCompleted.toString() + "\n\t\t" + "requiredForIncomplete: " + sequencing.rollupConsiderations.requiredForIncomplete.toString() + "\n\t\t" + "requiredForSatisfied: " + sequencing.rollupConsiderations.requiredForSatisfied.toString() + "\n\t\t" + "requiredForNotSatisfied: " + sequencing.rollupConsiderations.requiredForNotSatisfied.toString() + "\n\t" + "[RollupConsiderations-END]");
                }
            } else {
                if (Constants.DEBUG_ERRORS || Constants.DEBUG_SEQUENCING) {
                    System.out.println("[ERROR] L'Element " + "RollupConsiderations ha retornat un " + "error inesperat.");
                }
            }
        } else if (childName.equals(DELIVERYCONTROLS)) {
            String tracked = DOMTreeUtility.getAttributeValue(childNode, "tracked");
            if (tracked.length() == 0) {
                if (Constants.DEBUG_WARNINGS_LOW) {
                    System.out.println("[WARNING]Atribut tracked no trobat.");
                }
            } else {
                hasReallyANYSequencing = true;
                sequencing.tracked = tracked.equals(Constants.FALSE) ? false : true;
            }
            if (Constants.DEBUG_INFO || Constants.DEBUG_SEQUENCING) {
                System.out.println("\t[DeliveryControls]\n\t\t" + "tracked: " + sequencing.tracked);
            }
        } else if (childName.equals(AUXILIARYRESOURCES)) {
            if (Constants.DEBUG_WARNINGS || Constants.DEBUG_SEQUENCING) {
                System.out.println("\t[WARNING] '" + childName + "' s'ignorar�.");
            }
        } else {
            if (Constants.DEBUG_WARNINGS || Constants.DEBUG_SEQUENCING) {
                System.out.println("\t[WARNING] El Node '" + childName + "' no l'estem controlant.");
            }
        }
    }

    /**
	 * Ens indicar� si t� o no t� IDRef. 
	 * (default false).
	 * 
	 * @return boolean : 
	 */
    public final boolean hasIDRef() {
        if (sequencing.IDRef != null) {
            if (sequencing.IDRef.length() > 0) {
                return true;
            }
        }
        return false;
    }

    /**
	 * Ens indicar� si t� o no t� identificador. 
	 * (default false).
	 * 
	 * @return boolean 
	 */
    public final boolean hasID() {
        if (sequencing.ID != null) {
            if (sequencing.ID.length() > 0) {
                return true;
            }
        }
        return false;
    }

    /**
	 * Ens indicar� si t� o no t� Objectius. 
	 * (default false).
	 * 
	 * @return boolean 
	 */
    public final boolean hasObjectives() {
        return hasObjectives;
    }

    /**
	 * Aquesta funci� ens indicar� si REALMENT hem trobat 
	 * alguna cosa (etiqueta, regla, rollup, etc) de seq�enciament,
	 * o si pel contrari estem retornant el seq�enciament per 
	 * defecte.
	 * 
	 * @return boolean : {True = hem trobat algo | False = res}
	 */
    public final boolean hasReallyANYSequencing() {
        return hasReallyANYSequencing;
    }
}
