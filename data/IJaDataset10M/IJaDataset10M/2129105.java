package edu.url.lasalle.campus.scorm2004rte.test;

import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Stack;
import java.util.StringTokenizer;
import edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.Abstract_Item;
import edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.Leaf_Item;
import edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.Root_Item;
import edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.Elements.SequencingRules.ruleActionType;
import edu.url.lasalle.campus.scorm2004rte.server.DataBase.GestorBD;
import edu.url.lasalle.campus.scorm2004rte.server.DataBase.DynamicData.ADLNav;
import edu.url.lasalle.campus.scorm2004rte.server.DataBase.DynamicData.CMIDataModel;
import edu.url.lasalle.campus.scorm2004rte.server.DataBase.DynamicData.ClusterDataModel;
import edu.url.lasalle.campus.scorm2004rte.server.DataBase.DynamicData.ClusterMap;
import edu.url.lasalle.campus.scorm2004rte.server.DataBase.DynamicData.TreeAnnotations;
import edu.url.lasalle.campus.scorm2004rte.server.DataBase.DynamicData.UserObjective;
import edu.url.lasalle.campus.scorm2004rte.system.Constants;
import edu.url.lasalle.campus.scorm2004rte.system.CourseAdministrator;
import edu.url.lasalle.campus.scorm2004rte.system.CourseManager;
import edu.url.lasalle.campus.scorm2004rte.system.ParserHandler;
import edu.url.lasalle.campus.scorm2004rte.system.Constants.SequencingStatus;
import junit.framework.TestCase;

/**
* $Id: CourseManagerTest.java,v 1.18 2008/01/10 06:58:29 ecespedes Exp $
* <b>T�tol:</b> CourseManagerTest<br><br>
* <b>Descripci�:</b> JUnit Test Class.<br><br> 
*
* @author NomAutor / Enginyeria La Salle / eMail
* @version Versi� $Revision: 1.18 $ $Date: 2008/01/10 06:58:29 $
* $Log: CourseManagerTest.java,v $
* Revision 1.18  2008/01/10 06:58:29  ecespedes
* �ltimes modificacions de l'overallSequencingProcess.
*
* Revision 1.17  2007/12/28 16:35:27  ecespedes
* Implementat l'OverallSequencingProcess.
*
* Revision 1.16  2007/12/27 15:02:24  ecespedes
* Reunificant totes les crides del seq�enciament.
*
* Revision 1.15  2007/12/21 17:12:44  ecespedes
* Implementant CourseManager.OverallSequencingProcess
*
* Revision 1.14  2007/12/20 20:45:53  ecespedes
* Implementat l'Objective Map
*
* Revision 1.13  2007/12/17 15:27:48  ecespedes
* Fent MapInfo. Bug en els Leaf_Items
*
* Revision 1.12  2007/12/14 12:56:13  ecespedes
* Solucionat bugs i problemes derivats del nou concepte 'ObjectiveCluster'
*
* Revision 1.11  2007/12/13 15:25:12  ecespedes
* Problemes amb el sistema d'arbre de clusters.
* Falla l'ObjectiveStatusKnown.
*
* Revision 1.10  2007/12/11 16:00:43  ecespedes
* Suprimint par�metres: ObjectiveInterface el par�metre RollupRule.
*
* Revision 1.9  2007/12/11 15:29:27  ecespedes
* Arreglant bugs i optimitzant solucions.
*
* Revision 1.8  2007/12/10 22:03:32  ecespedes
* Implementant les funcions per buscar un item concret i retornar-lo
* al CourseManager perqu� el tracti.
*
* Revision 1.7  2007/12/10 11:50:27  ecespedes
* Comen�ant a juntar les peces del proc�s de seq�enciament.
*
* Revision 1.6  2007/12/09 22:32:16  ecespedes
* Els objectius dels clusters es tracten i es guarden de manera diferent.
*
* Revision 1.5  2007/12/05 13:36:59  ecespedes
* Arreglat bug limitConditions i PreConditions
*
* Revision 1.4  2007/12/04 15:47:14  ecespedes
* limitConditions implementat i preConditions testejat.
*
* Revision 1.3  2007/12/03 17:58:51  ecespedes
* Implementat un attemptCount que ens servir� per l'attemptLimit.
*
* Revision 1.2  2007/11/29 15:54:35  ecespedes
* Implementant les SequencingRules (part 1)
*
* Revision 1.1  2007/11/27 15:34:25  ecespedes
* Arreglats bugs relacionats amb el Rollup. Creat nou joc de testos.
*
*/
public class CourseManagerTest extends TestCase {

    /**
	 * �s per no fer una relaci� amb el SequencingStatus.Unknown.
	 * int Type = 0
	 */
    private static final int UNKNOWN = 0;

    /**
	 * �s per no fer una relaci� amb el SequencingStatus.Passed.
	 * int Type = 1
	 */
    private static final int PASSED = 1;

    /**
	 * �s per no fer una relaci� amb el SequencingStatus.Failed.
	 * int Type = 2
	 */
    private static final int FAILED = 2;

    /**
	 * Aquest ser� el valor inicial o en cas d'error s'assignar� 
	 * aquest valor a les variables. De manera que el seq�enciament
	 * quan rebi un -1 el que far� ser� fer un rollup i actualitzar
	 * aquesta variable. 
	 * 
	 * int Type = -1
	 */
    private static final int NULL = -1;

    /**
	 * nouHandler ser� l'inst�ncia del parsejador. 
	 */
    private ParserHandler nouHandler;

    /**
	 * nouHandler ser� l'inst�ncia que utilitzarem en els testos. 
	 */
    private CourseManager cManager;

    /**
	 * cUseObject contindr� les dades de l'usuari amb el que treballarem.
	 */
    private UserObjective cUseObject = null;

    private Date dataInicial = null;

    /**
	 * private Constant String Type.
	 * T� un seq�enciament linial modificat perqu� tingui una mica m�s
	 * de gr�cia.
	 */
    private static final String ARGS4 = "D:\\SCORM\\SCORM2004_Examples_Photoshop_1_1\\" + "Photoshop_Linear_SEQ-MODIFIED20071119\\imsmanifest.xml";

    /**
	 * Configura el ParserHandler i el CourseManager.
	 * @throws Exception :
	 */
    protected void setUp() throws Exception {
        dataInicial = new Date();
        nouHandler = new ParserHandler(ARGS4);
    }

    protected void tearDown() throws Exception {
        Date dataFinal = new Date();
        long elapsedTime = dataFinal.getTime() - dataInicial.getTime();
        System.out.println("Total time elapsed: " + elapsedTime + " ms");
    }

    /**
	 * Ser� el test principal des d'on ho farem totes les crides.
	 */
    public final void testCourseManager() {
        System.out.println("\n\n\tpublic final void testCourseManager()");
        printMemoryStatus();
        Hashtable<Integer, CourseManager> courseManagers = new Hashtable<Integer, CourseManager>();
        for (int i = 0; i < nouHandler.getNumOfOrganizations(); i++) {
            GestorBD nouGestor = GestorBD.getInstance();
            CourseManager nouCourseManager = null;
            try {
                cUseObject = CourseAdministrator.getInstance().getUserObjective(nouHandler.getDataAccessID(), i);
                nouCourseManager = CourseAdministrator.getInstance().getCourseManagerInstance(cUseObject.dataAccessID, cUseObject.organizationID);
                entryUserObjectiveTestData(cUseObject, nouCourseManager);
                nouGestor.saveUserData(1, 1, cUseObject);
                System.out.println("[OK] Loaded From DataBase-> " + cUseObject.organizationName);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("[" + i + "]Users[1]:" + nouCourseManager.getCurrentNumberUsers());
            assertNotNull(nouCourseManager);
            courseManagers.put(i, nouCourseManager);
        }
        cManager = courseManagers.get(0);
        printTreeAnnotations(cUseObject);
        System.out.println("\t\t------------ Start Test ------------");
        System.out.println("\tCourseManager Information:\n\t" + "\t[" + cManager.getIdentifier() + "]\t" + cManager.getMetadata().identifier + "\t" + cManager.getOrganizationCluster().getIdentifier());
        System.out.println("\n################## TEST 1 ##################" + "\nTreballem amb l'Organitzaci�");
        if (!test1()) {
            System.out.println("\tTEST 1 -> FAILED");
        } else {
            System.out.println("\tTEST 1 -> PASSED");
        }
        System.out.println("\n################## TEST 2 ##################" + "\nTreballem amb l'Organitzaci�");
        if (!test2()) {
            System.out.println("\tTEST 2 -> FAILED");
        } else {
            System.out.println("\tTEST 2 -> PASSED");
        }
        System.out.println("\n################## TEST 3 ##################" + "\nTreballem amb el MODULE1");
        if (!test3()) {
            System.out.println("\tTEST 3 -> FAILED");
        } else {
            System.out.println("\tTEST 3 -> PASSED");
        }
        System.out.println("\n################## TEST 4 ##################" + "\nTreballem amb l'EXAM3");
        if (!test4()) {
            System.out.println("\tTEST 4 -> FAILED");
        } else {
            System.out.println("\tTEST 4 -> PASSED");
        }
        System.out.println("\n################## TEST 5 ##################" + "\nTreballem amb l'INTRO");
        if (!test5()) {
            System.out.println("\tTEST 5 -> FAILED");
        } else {
            System.out.println("\tTEST 5 -> PASSED");
        }
        System.out.println("\n################## TEST 6 ##################" + "\nTreballem amb l'INTRO");
        if (!test6()) {
            System.out.println("\tTEST 6 -> FAILED");
        } else {
            System.out.println("\tTEST 6 -> PASSED");
        }
        System.out.println("\n################## TEST 7 ##################" + "\nBusquem el QUESTION9A");
        if (!test7()) {
            System.out.println("\tTEST 7 -> FAILED");
        } else {
            System.out.println("\tTEST 7 -> PASSED");
        }
        System.out.println("\n################## TEST 8 ##################" + "\nBusquem el QUESTION9A");
        if (!test8()) {
            System.out.println("\tTEST 8 -> FAILED");
        } else {
            System.out.println("\tTEST 8 -> PASSED");
        }
        System.out.println("\t\t------------ End Test ------------");
        System.out.println("END-testParserHandler()");
        System.out.println("Intentem guanyar mem�ria...");
        cManager.removeAllTreeOrganization();
        printMemoryStatus();
    }

    /**
	 * Farem unes crides per saber l'objectiveMeasure des 
	 * de l'organitzaci�.
	 * 
	 * @return boolean Type: Per saber si hi ha un error o no.
	 */
    private boolean test1() {
        Root_Item org = (Root_Item) cManager.getOrganizationCluster();
        Double resultMeasure = org.objectiveMeasure(cUseObject);
        if (resultMeasure == null) {
            return false;
        } else {
            System.out.println("\t\ttest1 reply:" + resultMeasure);
        }
        return true;
    }

    /**
	 * Farem unes crides per saber l'estat del seq�enciament
	 * (attempted, completed, satisfied, measure, etc) des
	 * de l'organitzaci�.
	 * 
	 * @return boolean Type: Per saber si hi ha un error o no.
	 */
    private boolean test2() {
        Root_Item org = (Root_Item) cManager.getOrganizationCluster();
        SequencingStatus resultAttmpt = org.attempted(cUseObject);
        if (resultAttmpt == null) {
            return false;
        } else {
            System.out.println("\ttest2->attempted:" + resultAttmpt.toString());
        }
        SequencingStatus resultCmplt = org.completed(cUseObject, true);
        if (resultCmplt == null) {
            return false;
        } else {
            System.out.println("\ttest2->completed:" + resultCmplt.toString());
        }
        SequencingStatus resultStisf = org.satisfied(cUseObject, true);
        if (resultStisf == null) {
            return false;
        } else {
            System.out.println("\ttest2->satisfied:" + resultStisf.toString());
        }
        SequencingStatus resultObjMKn = org.objectiveMeasureKnown(cUseObject);
        if (resultObjMKn == null) {
            return false;
        } else {
            System.out.println("\ttest2->objectiveMeasureKnown:" + resultObjMKn.toString());
        }
        SequencingStatus resultObjSKn = org.objectiveStatusKnown(cUseObject);
        if (resultObjSKn == null) {
            return false;
        } else {
            System.out.println("\ttest2->objectiveStatusKnown:" + resultObjSKn.toString());
        }
        return true;
    }

    /**
	 * Farem unes crides per saber l'estat del seq�enciament
	 * (attempted, completed, satisfied, measure, etc) per�
	 * aquest cop des d'un item on els estats sigui diferents.
	 * 
	 * El cluster estar� format per ASSETS.
	 * 
	 * @return boolean Type: Per saber si hi ha un error o no.
	 */
    private boolean test3() {
        Root_Item org = (Root_Item) cManager.getOrganizationCluster();
        boolean retBool = true;
        Root_Item module1 = null;
        for (Iterator it = org.getChildrenIterator(); it.hasNext(); ) {
            Abstract_Item abstSon = (Abstract_Item) it.next();
            if (abstSon.getIdentifier().equals("MODULE1")) {
                module1 = (Root_Item) abstSon;
                break;
            }
        }
        SequencingStatus resultAttmpt = module1.attempted(cUseObject);
        if (resultAttmpt == null) {
            retBool = false;
            System.out.println("\ttest3->attempted: NULL");
        } else {
            System.out.println("\ttest3->attempted:" + resultAttmpt.toString());
        }
        SequencingStatus resultObjMKn = module1.objectiveMeasureKnown(cUseObject);
        if (resultObjMKn == null) {
            retBool = false;
            System.out.println("\ttest3->objectiveMeasureKnown: NULL");
        } else {
            System.out.println("\ttest3->objectiveMeasureKnown:" + resultObjMKn.toString());
        }
        SequencingStatus resultObjSKn = module1.objectiveStatusKnown(cUseObject);
        if (resultObjSKn == null) {
            retBool = false;
            System.out.println("\ttest3->objectiveStatusKnown: NULL");
        } else {
            System.out.println("\ttest3->objectiveStatusKnown:" + resultObjSKn.toString());
        }
        SequencingStatus resultStisf = module1.satisfied(cUseObject, true);
        if (resultStisf == null) {
            retBool = false;
            System.out.println("\ttest3->satisfied: NULL");
        } else {
            System.out.println("\ttest3->satisfied:" + resultStisf.toString());
        }
        SequencingStatus resultCmplt = module1.completed(cUseObject, true);
        if (resultCmplt == null) {
            retBool = false;
            System.out.println("\ttest3->completed: NULL");
        } else {
            System.out.println("\ttest3->completed:" + resultCmplt.toString());
        }
        return retBool;
    }

    /**
	 * Farem unes crides per saber l'estat del seq�enciament
	 * (attempted, completed, satisfied, measure, etc) per�
	 * aquest cop des d'un item on els estats sigui diferents.
	 * 
	 * El cluster estar� format per SCO's.
	 * 
	 * @return boolean Type: Per saber si hi ha un error o no.
	 */
    private boolean test4() {
        Root_Item org = (Root_Item) cManager.getOrganizationCluster();
        boolean retBool = true;
        Root_Item exammenu = null;
        for (Iterator it = org.getChildrenIterator(); it.hasNext(); ) {
            Abstract_Item abstSon = (Abstract_Item) it.next();
            if (abstSon.getIdentifier().equals("EXAMMENU")) {
                exammenu = (Root_Item) abstSon;
                break;
            }
        }
        Root_Item exam = null;
        for (Iterator it = exammenu.getChildrenIterator(); it.hasNext(); ) {
            Abstract_Item abstSon = (Abstract_Item) it.next();
            if (abstSon.getIdentifier().equals("EXAM")) {
                exam = (Root_Item) abstSon;
                break;
            }
        }
        Root_Item exam3 = null;
        for (Iterator it = exam.getChildrenIterator(); it.hasNext(); ) {
            Abstract_Item abstSon = (Abstract_Item) it.next();
            if (abstSon.getIdentifier().equals("EXAM3")) {
                exam3 = (Root_Item) abstSon;
                break;
            }
        }
        SequencingStatus resultAttmpt = exam3.attempted(cUseObject);
        if (resultAttmpt == null) {
            retBool = false;
            System.out.println("\ttest4->attempted: NULL");
        } else {
            System.out.println("\ttest4->attempted:" + resultAttmpt.toString());
        }
        SequencingStatus resultObjMKn = exam3.objectiveMeasureKnown(cUseObject);
        if (resultObjMKn == null) {
            retBool = false;
            System.out.println("\ttest4->objectiveMeasureKnown: NULL");
        } else {
            System.out.println("\ttest4->objectiveMeasureKnown:" + resultObjMKn.toString());
        }
        SequencingStatus resultObjSKn = exam3.objectiveStatusKnown(cUseObject);
        if (resultObjSKn == null) {
            retBool = false;
            System.out.println("\ttest4->objectiveStatusKnown: NULL");
        } else {
            System.out.println("\ttest4->objectiveStatusKnown:" + resultObjSKn.toString());
        }
        SequencingStatus resultStisf = exam3.satisfied(cUseObject, true);
        if (resultStisf == null) {
            retBool = false;
            System.out.println("\ttest4->satisfied: NULL");
        } else {
            System.out.println("\ttest4->satisfied:" + resultStisf.toString());
        }
        SequencingStatus resultCmplt = exam3.completed(cUseObject, true);
        if (resultCmplt == null) {
            retBool = false;
            System.out.println("\ttest4->completed: NULL");
        } else {
            System.out.println("\ttest4->completed:" + resultCmplt.toString());
        }
        Double resultMeasure = exam3.objectiveMeasure(cUseObject);
        if (resultCmplt == null) {
            retBool = false;
            System.out.println("\ttest4->objectiveMeasure: NULL");
        } else {
            System.out.println("\ttest4->objectiveMeasure:" + resultMeasure);
        }
        return retBool;
    }

    /**
	 * Comprovarem el limitConditions.
	 * @return boolean : True si funciona, False si falla
	 */
    private boolean test5() {
        boolean retBool = true;
        Root_Item org = (Root_Item) cManager.getOrganizationCluster();
        System.out.println("\t[TOC1] -> limitCondition ? -> " + org.getSequencing().limitCondition(cUseObject));
        Abstract_Item intro = null;
        for (Iterator it = org.getChildrenIterator(); it.hasNext(); ) {
            Abstract_Item abstSon = (Abstract_Item) it.next();
            if (abstSon.getIdentifier().equals("INTRO")) {
                intro = (Abstract_Item) abstSon;
                break;
            }
        }
        System.out.println("\t[INTRO] -> limitCondition ? -> " + intro.getSequencing().limitCondition(cUseObject));
        return retBool;
    }

    /**
	 * Comprovarem les preConditionRules.
	 * @return boolean : True si funciona, False si falla
	 */
    private boolean test6() {
        boolean retBool = true;
        Root_Item org = (Root_Item) cManager.getOrganizationCluster();
        Abstract_Item intro = null;
        for (Iterator it = org.getChildrenIterator(); it.hasNext(); ) {
            Abstract_Item abstSon = (Abstract_Item) it.next();
            if (abstSon.getIdentifier().equals("INTRO")) {
                intro = (Abstract_Item) abstSon;
                break;
            }
        }
        ruleActionType tipusReturn = intro.getSequencing().preConditionRule(cUseObject);
        if (tipusReturn == null) {
            System.out.println("\t[INTRO] -> preConditionRule -> NULL");
        } else {
            System.out.println("\t[INTRO] -> preConditionRule -> " + tipusReturn.toString());
        }
        printTreeAnnotations(cUseObject);
        return retBool;
    }

    /**
	 * En aquest test emularem el que seria la part de
	 * rebre SCO. Aix� comporta els pasos de:
	 * 
	 * PAS 1: Descobrir el cam� �ptim.
	 * PAS 2: "Accedir" a l'Item amb el cam� descobert.
	 * PAS 3: Actualitzar les dades: CMIDataModel (si 
	 * 		hasMapInfo actualitzarem el GlobalObjective).
	 * PAS 4: Actualitzar els ClusterMaps del pare->avi->...->
	 * 		organitzaci� de tal manera que nom�s s'actualitzi
	 * 		per la branca que hem accedit.
	 * PAS 5: Comprovar si t� PostCondition o exitCondition.
	 * PAS 5.1: Si t� PostCondici� analitzarem de que �s tracta i
	 * 		buscarem el seg�ent item.
	 * PAS 5.2: Si no t� PostCondici� mirarem el ADLNav a veur�
	 * 		que hi tenim. 
	 * 
	 * L'Item ser� el QUESTION9A.
	 * @return boolean : True si funciona, False si falla
	 */
    private boolean test7() {
        boolean retBool = true;
        cUseObject.dataModel.get("QUESTION9A").firstAttemptedTime = null;
        cUseObject.dataModel.get("QUESTION9A").activityAttemptCount = 0;
        cUseObject.dataModel.get("QUESTION9A").totalTime = "PT3M";
        cUseObject.dataModel.get("QUESTION9A").completionStatus = "completed";
        cUseObject.dataModel.get("QUESTION9A").successStatus = "passed";
        cUseObject.dataModel.get("QUESTION9A").scoreScaled = "0.75";
        cUseObject.dataModel.get("QUESTION9A").lastAttemptedTime = null;
        cUseObject.treeAnnotations.get("QUESTION9A").isAccessed = false;
        cUseObject.adlnav = new ADLNav();
        cUseObject.adlnav.request = "{target=QUESTION8}choice";
        Stack<String> route = cManager.searchRouteMap(cUseObject, "QUESTION9A");
        if (route == null) {
            retBool = false;
            System.out.println("\t[CouseManager] -> searchRouteMap -> NULL");
        } else {
            System.out.println("\t[CouseManager] -> searchRouteMap -> " + route.toString());
        }
        Leaf_Item lItem = cManager.searchMyItem(cUseObject, route);
        if (lItem == null) {
            retBool = false;
            System.out.println("\t[CouseManager] -> searchMyItem -> NULL");
        } else {
            System.out.println("\t[CouseManager] -> searchMyItem -> " + "(SCO? " + lItem.getIsSCO() + ") " + lItem.getIdentifier() + ", Title = '" + lItem.getTitle() + "';");
        }
        CMIDataModel question9A = cUseObject.dataModel.get("QUESTION9A");
        if (question9A.activityAttemptCount == 0) {
            question9A.firstAttemptedTime = new Date();
        }
        question9A.lastAttemptedTime = new Date();
        question9A.activityAttemptCount++;
        cUseObject.treeAnnotations.get("QUESTION9A").isAccessed = true;
        lItem.overallRollupProcess(cUseObject);
        Leaf_Item nouItem = null;
        ruleActionType rAT = lItem.getSequencing().postConditionRule(cUseObject);
        if (rAT != null) {
            System.out.println("\t[CouseManager] -> postConditionRule -> " + rAT.toString());
            if (rAT.equals(ruleActionType._continue)) {
                nouItem = lItem.nextItem(cUseObject, lItem.getIdentifier());
            } else if (rAT.equals(ruleActionType.previous)) {
                nouItem = lItem.previousItem(cUseObject, lItem.getIdentifier());
            } else if (rAT.equals(ruleActionType.retry)) {
                if (lItem.father != null) {
                    nouItem = ((Root_Item) lItem.father).firstLeafItem(cUseObject);
                } else {
                    System.out.println("[ERROR] Ens hem trobat que '" + lItem.getIdentifier() + "' no te pare!!");
                }
            } else if (rAT.equals(ruleActionType.retryAll)) {
                nouItem = ((Root_Item) cManager.getOrganizationCluster()).firstLeafItem(cUseObject);
            } else if (rAT.equals(ruleActionType.exitParent)) {
                if (lItem.father != null) {
                    nouItem = lItem.father.nextItem(cUseObject, lItem.father.getIdentifier());
                } else {
                    System.out.println("[ERROR] Ens hem trobat que '" + lItem.getIdentifier() + "' no te pare!!");
                }
            } else if (rAT.equals(ruleActionType.exitAll)) {
                System.out.println("[INFO] ExitALL!!!");
            } else {
                if (Constants.DEBUG_ERRORS) {
                    System.out.println("[ERROR] No he reconegut l'acci� :'" + rAT.toString() + "'.\nFarem com si no hi hagu�s" + " cap postCondici�.");
                }
            }
        } else {
            System.out.println("\t[CouseManager] -> postConditionRule -> NULL");
            if (cUseObject.equals("continue")) {
                nouItem = lItem.nextItem(cUseObject, lItem.getIdentifier());
            } else if (cUseObject.adlnav.request.equals("previous")) {
                nouItem = lItem.previousItem(cUseObject, lItem.getIdentifier());
            } else if (cUseObject.adlnav.request.equals("exit") || cUseObject.adlnav.request.equals("exitAll") || cUseObject.adlnav.request.equals("abandon") || cUseObject.adlnav.request.equals("abandonAll") || cUseObject.adlnav.request.equals("suspendAll")) {
            } else if (cUseObject.adlnav.request.equals("_none_")) {
                cUseObject.adlnav.lmsException = "Hem rebut un _none_ " + "com a request!";
            } else {
                String idNewItem = null;
                StringTokenizer tokenizer = new StringTokenizer(cUseObject.adlnav.request, "{");
                String token = null;
                if (tokenizer.hasMoreTokens()) {
                    token = tokenizer.nextToken();
                    StringTokenizer tokenizer2 = new StringTokenizer(token, "=");
                    if (tokenizer2.hasMoreTokens()) {
                        token = tokenizer2.nextToken();
                        if (token.equals("target")) {
                            if (tokenizer2.hasMoreTokens()) {
                                token = tokenizer2.nextToken();
                                tokenizer2 = new StringTokenizer(token, "}");
                                if (tokenizer2.hasMoreTokens()) {
                                    idNewItem = tokenizer2.nextToken();
                                }
                                if (tokenizer2.hasMoreTokens()) {
                                    token = tokenizer2.nextToken();
                                    if (!token.equals("choice")) {
                                        idNewItem = null;
                                    }
                                    System.out.print("\n");
                                    System.out.print("\ttype: " + token);
                                    System.out.print("\ttarget: " + idNewItem + "\n");
                                }
                            }
                        }
                    }
                }
                if (idNewItem == null) {
                    cUseObject.adlnav.lmsException = "Identificador " + "d'Item incorrecte!";
                } else {
                    Stack<String> routeNI = cManager.searchRouteMap(cUseObject, idNewItem);
                    boolean anyError = false;
                    if (routeNI == null) {
                        anyError = true;
                    } else {
                        if (routeNI.size() == 0) {
                            retBool = false;
                            System.out.println("\t[CouseManager] -> " + "searchRouteMap -> NULL");
                            anyError = true;
                        } else {
                            System.out.println("\t[CouseManager] -> " + "searchRouteMap -> " + routeNI.toString());
                        }
                    }
                    if (!anyError) {
                        nouItem = cManager.searchMyItem(cUseObject, routeNI);
                        if (nouItem == null) {
                            retBool = false;
                            System.out.println("\t[CouseManager] -> " + "searchMyItem -> NULL");
                        } else {
                            System.out.println("\t[CouseManager] -> " + "searchMyItem -> (SCO? " + nouItem.getIsSCO() + ") " + nouItem.getIdentifier() + ", Title = '" + nouItem.getTitle() + "';");
                        }
                    } else {
                        cUseObject.adlnav.lmsException = "Identificador " + "d'Item incorrecte!";
                    }
                }
            }
        }
        if (nouItem == null) {
            retBool = false;
        } else {
            System.out.println("\tEl nou item ser�: " + nouItem.getIdentifier());
        }
        return retBool;
    }

    /**
	 * En aquest test emularem el que seria la part de
	 * rebre SCO. Aix� comporta els pasos de:
	 * 
	 * PAS 1: Descobrir el cam� �ptim.
	 * PAS 2: "Accedir" a l'Item amb el cam� descobert.
	 * PAS 3: Actualitzar les dades: CMIDataModel (si 
	 * 		hasMapInfo actualitzarem el GlobalObjective).
	 * PAS 4: Actualitzar els ClusterMaps del pare->avi->...->
	 * 		organitzaci� de tal manera que nom�s s'actualitzi
	 * 		per la branca que hem accedit.
	 * PAS 5: Comprovar si t� PostCondition o exitCondition.
	 * PAS 5.1: Si t� PostCondici� analitzarem de que �s tracta i
	 * 		buscarem el seg�ent item.
	 * PAS 5.2: Si no t� PostCondici� mirarem el ADLNav a veur�
	 * 		que hi tenim. 
	 * 
	 * L'Item ser� el QUESTION9A.
	 * @return boolean : True si funciona, False si falla
	 */
    private boolean test8() {
        cUseObject.dataModel.get("QUESTION9A").firstAttemptedTime = null;
        cUseObject.dataModel.get("QUESTION9A").activityAttemptCount = 0;
        cUseObject.dataModel.get("QUESTION9A").totalTime = "PT3M";
        cUseObject.dataModel.get("QUESTION9A").completionStatus = "completed";
        cUseObject.dataModel.get("QUESTION9A").successStatus = "passed";
        cUseObject.dataModel.get("QUESTION9A").scoreScaled = "0.75";
        cUseObject.dataModel.get("QUESTION9A").lastAttemptedTime = null;
        cUseObject.treeAnnotations.get("QUESTION9A").isAccessed = false;
        cUseObject.adlnav = new ADLNav();
        cUseObject.adlnav.request = "{target=QUESTION8}choice";
        cUseObject.currentItem = "QUESTION9A";
        String href = cManager.overallSequencingProcess(cUseObject);
        if (href == null) {
            return false;
        } else {
            System.out.println("\tHref: " + href);
            return true;
        }
    }

    /***
	 * Omplim dades aleat�ries per fer un test i saber com va l'Usuari.
	 * @param cUserObject : El userObjective de l'usuari.
	 * @param nouCourseManager : CourseManager Type
	 */
    private void entryUserObjectiveTestData(final UserObjective cUserObject, final CourseManager nouCourseManager) {
        UserObjective rUObject = cUserObject;
        Calendar calendar = new GregorianCalendar();
        rUObject.treeAnnotations.get("TOC1").isAccessed = true;
        calendar.add(Calendar.DATE, -20);
        rUObject.dataModel.get("INTRO").firstAttemptedTime = calendar.getTime();
        rUObject.dataModel.get("INTRO").activityAttemptCount = 8;
        rUObject.dataModel.get("INTRO").totalTime = "PT8M";
        calendar.add(Calendar.DATE, +1);
        rUObject.dataModel.get("INTRO").lastAttemptedTime = calendar.getTime();
        rUObject.treeAnnotations.get("INTRO").isAccessed = true;
        Stack<String> route = nouCourseManager.searchRouteMap(cUseObject, "INTRO");
        Leaf_Item lItem = nouCourseManager.searchMyItem(cUserObject, route);
        lItem.overallRollupProcess(rUObject);
        System.out.println("@@@@@@ INTRO @@@@@@");
        printClusterElements(rUObject.clusterTree);
        calendar.add(Calendar.DATE, -20);
        rUObject.dataModel.get("LESSON1").firstAttemptedTime = calendar.getTime();
        rUObject.dataModel.get("LESSON1").activityAttemptCount = 1;
        rUObject.dataModel.get("LESSON1").totalTime = "PT1M";
        calendar.add(Calendar.DATE, +1);
        rUObject.dataModel.get("LESSON1").lastAttemptedTime = calendar.getTime();
        rUObject.treeAnnotations.get("LESSON1").isAccessed = true;
        route = null;
        route = nouCourseManager.searchRouteMap(rUObject, "LESSON1");
        lItem = null;
        lItem = nouCourseManager.searchMyItem(cUserObject, route);
        lItem.overallRollupProcess(rUObject);
        System.out.println("@@@@@@ LESSON1 @@@@@@");
        printClusterElements(rUObject.clusterTree);
        rUObject.dataModel.get("LESSON2").firstAttemptedTime = calendar.getTime();
        rUObject.dataModel.get("LESSON2").activityAttemptCount = 2;
        rUObject.dataModel.get("LESSON2").totalTime = "PT2M";
        calendar.add(Calendar.DATE, +1);
        rUObject.dataModel.get("LESSON2").lastAttemptedTime = calendar.getTime();
        rUObject.treeAnnotations.get("LESSON2").isAccessed = true;
        route = null;
        route = nouCourseManager.searchRouteMap(rUObject, "LESSON2");
        lItem = null;
        lItem = nouCourseManager.searchMyItem(cUserObject, route);
        lItem.overallRollupProcess(rUObject);
        System.out.println("@@@@@@ LESSON2 @@@@@@");
        printClusterElements(rUObject.clusterTree);
        rUObject.dataModel.get("LESSON3").firstAttemptedTime = calendar.getTime();
        rUObject.dataModel.get("LESSON3").activityAttemptCount = 7;
        rUObject.dataModel.get("LESSON3").totalTime = "PT7M";
        calendar.add(Calendar.DATE, +1);
        rUObject.dataModel.get("LESSON3").lastAttemptedTime = calendar.getTime();
        rUObject.treeAnnotations.get("LESSON3").isAccessed = true;
        route = null;
        route = nouCourseManager.searchRouteMap(rUObject, "LESSON3");
        lItem = null;
        lItem = nouCourseManager.searchMyItem(cUserObject, route);
        lItem.overallRollupProcess(rUObject);
        System.out.println("@@@@@@ LESSON3 @@@@@@");
        printClusterElements(rUObject.clusterTree);
        rUObject.dataModel.get("LESSON4").firstAttemptedTime = calendar.getTime();
        rUObject.dataModel.get("LESSON4").activityAttemptCount = 1;
        rUObject.dataModel.get("LESSON4").totalTime = "PT1M";
        calendar.add(Calendar.DATE, +1);
        rUObject.dataModel.get("LESSON4").lastAttemptedTime = calendar.getTime();
        rUObject.treeAnnotations.get("LESSON4").isAccessed = true;
        route = null;
        route = nouCourseManager.searchRouteMap(rUObject, "LESSON4");
        lItem = null;
        lItem = nouCourseManager.searchMyItem(cUserObject, route);
        lItem.overallRollupProcess(rUObject);
        System.out.println("@@@@@@ LESSON4 @@@@@@");
        printClusterElements(rUObject.clusterTree);
        rUObject.treeAnnotations.get("MODULE1").isAccessed = true;
        rUObject.dataModel.get("LESSON5").firstAttemptedTime = null;
        rUObject.dataModel.get("LESSON5").activityAttemptCount = 0;
        rUObject.dataModel.get("LESSON5").lastAttemptedTime = null;
        rUObject.dataModel.get("LESSON6").firstAttemptedTime = null;
        rUObject.dataModel.get("LESSON6").activityAttemptCount = 0;
        rUObject.dataModel.get("LESSON6").lastAttemptedTime = null;
        rUObject.dataModel.get("LESSON7").firstAttemptedTime = null;
        rUObject.dataModel.get("LESSON7").activityAttemptCount = 0;
        rUObject.dataModel.get("LESSON7").lastAttemptedTime = null;
        rUObject.treeAnnotations.get("MODULE2").isAccessed = true;
        rUObject.dataModel.get("LESSON7B").firstAttemptedTime = calendar.getTime();
        rUObject.dataModel.get("LESSON7B").activityAttemptCount = 8;
        rUObject.dataModel.get("LESSON7B").totalTime = "PT8M";
        calendar.add(Calendar.DATE, +1);
        rUObject.dataModel.get("LESSON7B").lastAttemptedTime = calendar.getTime();
        rUObject.treeAnnotations.get("LESSON7B").isAccessed = true;
        route = null;
        route = nouCourseManager.searchRouteMap(rUObject, "LESSON7B");
        lItem = null;
        lItem = nouCourseManager.searchMyItem(cUserObject, route);
        lItem.overallRollupProcess(rUObject);
        System.out.println("@@@@@@ LESSON7B @@@@@@");
        printClusterElements(rUObject.clusterTree);
        rUObject.dataModel.get("LESSON8").firstAttemptedTime = null;
        rUObject.dataModel.get("LESSON8").activityAttemptCount = 0;
        rUObject.dataModel.get("LESSON8").lastAttemptedTime = null;
        rUObject.dataModel.get("LESSON9").firstAttemptedTime = null;
        rUObject.dataModel.get("LESSON9").activityAttemptCount = 0;
        rUObject.dataModel.get("LESSON9").lastAttemptedTime = null;
        rUObject.treeAnnotations.get("MODULE3").isAccessed = true;
        rUObject.dataModel.get("QUESTION1").firstAttemptedTime = calendar.getTime();
        rUObject.dataModel.get("QUESTION1").activityAttemptCount = 2;
        rUObject.dataModel.get("QUESTION1").totalTime = "PT2M";
        rUObject.dataModel.get("QUESTION1").cmiObjectives.get("1LEAFOBJJ").completionStatus = "completed";
        rUObject.dataModel.get("QUESTION1").cmiObjectives.get("1LEAFOBJJ").successStatus = "passed";
        rUObject.dataModel.get("QUESTION1").cmiObjectives.get("1LEAFOBJJ").scoreScaled = "0.7";
        rUObject.dataModel.get("QUESTION1").cmiObjectives.get("2LEAFOBJJ").completionStatus = "completed";
        rUObject.dataModel.get("QUESTION1").cmiObjectives.get("2LEAFOBJJ").successStatus = "passed";
        rUObject.dataModel.get("QUESTION1").cmiObjectives.get("2LEAFOBJJ").scoreScaled = "1";
        calendar.add(Calendar.DATE, +1);
        rUObject.dataModel.get("QUESTION1").lastAttemptedTime = calendar.getTime();
        rUObject.treeAnnotations.get("QUESTION1").isAccessed = true;
        route = null;
        route = nouCourseManager.searchRouteMap(rUObject, "QUESTION1");
        lItem = null;
        lItem = nouCourseManager.searchMyItem(cUserObject, route);
        lItem.overallRollupProcess(rUObject);
        System.out.println("@@@@@@ QUESTION1 @@@@@@");
        printClusterElements(rUObject.clusterTree);
        printGlobalElements(rUObject.globalObjectiveMap);
        rUObject.dataModel.get("QUESTION2").firstAttemptedTime = calendar.getTime();
        rUObject.dataModel.get("QUESTION2").activityAttemptCount = 1;
        rUObject.dataModel.get("QUESTION2").totalTime = "PT1M";
        rUObject.dataModel.get("QUESTION2").completionStatus = "incomplete";
        rUObject.dataModel.get("QUESTION2").successStatus = "passed";
        rUObject.dataModel.get("QUESTION2").scoreScaled = "0.2";
        calendar.add(Calendar.DATE, +1);
        rUObject.dataModel.get("QUESTION2").lastAttemptedTime = calendar.getTime();
        rUObject.treeAnnotations.get("QUESTION2").isAccessed = true;
        route = null;
        route = nouCourseManager.searchRouteMap(rUObject, "QUESTION2");
        lItem = null;
        lItem = nouCourseManager.searchMyItem(cUserObject, route);
        lItem.overallRollupProcess(rUObject);
        System.out.println("@@@@@@ QUESTION2 @@@@@@");
        printClusterElements(rUObject.clusterTree);
        rUObject.dataModel.get("QUESTION3").firstAttemptedTime = null;
        rUObject.dataModel.get("QUESTION3").activityAttemptCount = 0;
        rUObject.dataModel.get("QUESTION3").completionStatus = "unknown";
        rUObject.dataModel.get("QUESTION3").successStatus = "unknown";
        rUObject.dataModel.get("QUESTION3").lastAttemptedTime = null;
        rUObject.dataModel.get("QUESTION4").firstAttemptedTime = null;
        rUObject.dataModel.get("QUESTION4").activityAttemptCount = 0;
        rUObject.dataModel.get("QUESTION4").completionStatus = "unknown";
        rUObject.dataModel.get("QUESTION4").successStatus = "unknown";
        rUObject.dataModel.get("QUESTION4").lastAttemptedTime = null;
        rUObject.dataModel.get("QUESTION5").firstAttemptedTime = null;
        rUObject.dataModel.get("QUESTION5").activityAttemptCount = 0;
        rUObject.dataModel.get("QUESTION5").completionStatus = "unknown";
        rUObject.dataModel.get("QUESTION5").successStatus = "unknown";
        rUObject.dataModel.get("QUESTION5").lastAttemptedTime = null;
        rUObject.dataModel.get("QUESTION6").firstAttemptedTime = null;
        rUObject.dataModel.get("QUESTION6").activityAttemptCount = 0;
        rUObject.dataModel.get("QUESTION6").completionStatus = "unknown";
        rUObject.dataModel.get("QUESTION6").successStatus = "unknown";
        rUObject.dataModel.get("QUESTION6").lastAttemptedTime = null;
        rUObject.dataModel.get("QUESTION7").firstAttemptedTime = calendar.getTime();
        rUObject.dataModel.get("QUESTION7").activityAttemptCount = 2;
        rUObject.dataModel.get("QUESTION7").totalTime = "PT2M";
        rUObject.dataModel.get("QUESTION7").completionStatus = "completed";
        rUObject.dataModel.get("QUESTION7").successStatus = "passed";
        rUObject.dataModel.get("QUESTION7").scoreScaled = "1";
        calendar.add(Calendar.DATE, +1);
        rUObject.dataModel.get("QUESTION7").lastAttemptedTime = calendar.getTime();
        rUObject.treeAnnotations.get("QUESTION7").isAccessed = true;
        route = null;
        route = nouCourseManager.searchRouteMap(rUObject, "QUESTION7");
        lItem = null;
        lItem = nouCourseManager.searchMyItem(cUserObject, route);
        lItem.overallRollupProcess(rUObject);
        System.out.println("@@@@@@ QUESTION7 @@@@@@");
        printClusterElements(rUObject.clusterTree);
        rUObject.dataModel.get("QUESTION8").firstAttemptedTime = calendar.getTime();
        rUObject.dataModel.get("QUESTION8").activityAttemptCount = 3;
        rUObject.dataModel.get("QUESTION8").totalTime = "PT2M";
        rUObject.dataModel.get("QUESTION8").completionStatus = "completed";
        rUObject.dataModel.get("QUESTION8").successStatus = "passed";
        rUObject.dataModel.get("QUESTION8").scoreScaled = "1";
        calendar.add(Calendar.DATE, +1);
        rUObject.dataModel.get("QUESTION8").lastAttemptedTime = calendar.getTime();
        rUObject.treeAnnotations.get("QUESTION8").isAccessed = true;
        route = null;
        route = nouCourseManager.searchRouteMap(rUObject, "QUESTION8");
        lItem = null;
        lItem = nouCourseManager.searchMyItem(cUserObject, route);
        lItem.overallRollupProcess(rUObject);
        System.out.println("@@@@@@ QUESTION8 @@@@@@");
        printClusterElements(rUObject.clusterTree);
        printGlobalElements(rUObject.globalObjectiveMap);
        rUObject.dataModel.get("QUESTION9").firstAttemptedTime = calendar.getTime();
        rUObject.dataModel.get("QUESTION9").activityAttemptCount = 3;
        rUObject.dataModel.get("QUESTION9").totalTime = "PT3M";
        rUObject.dataModel.get("QUESTION9").completionStatus = "completed";
        rUObject.dataModel.get("QUESTION9").successStatus = "passed";
        rUObject.dataModel.get("QUESTION9").scoreScaled = "0.75";
        calendar.add(Calendar.DATE, +1);
        rUObject.dataModel.get("QUESTION9").lastAttemptedTime = calendar.getTime();
        rUObject.treeAnnotations.get("QUESTION9").isAccessed = true;
        route = null;
        route = nouCourseManager.searchRouteMap(rUObject, "QUESTION9");
        lItem = null;
        lItem = nouCourseManager.searchMyItem(cUserObject, route);
        lItem.overallRollupProcess(rUObject);
        System.out.println("@@@@@@ QUESTION9 @@@@@@");
        printClusterElements(rUObject.clusterTree);
        printGlobalElements(rUObject.globalObjectiveMap);
        rUObject.dataModel.get("QUESTION9A").firstAttemptedTime = calendar.getTime();
        rUObject.dataModel.get("QUESTION9A").activityAttemptCount = 3;
        rUObject.dataModel.get("QUESTION9A").totalTime = "PT3M";
        rUObject.dataModel.get("QUESTION9A").completionStatus = "completed";
        rUObject.dataModel.get("QUESTION9A").successStatus = "passed";
        rUObject.dataModel.get("QUESTION9A").scoreScaled = "0.75";
        calendar.add(Calendar.DATE, +1);
        rUObject.dataModel.get("QUESTION9A").lastAttemptedTime = calendar.getTime();
        rUObject.treeAnnotations.get("QUESTION9A").isAccessed = true;
        route = null;
        route = nouCourseManager.searchRouteMap(rUObject, "QUESTION9A");
        lItem = null;
        lItem = nouCourseManager.searchMyItem(cUserObject, route);
        lItem.overallRollupProcess(rUObject);
        System.out.println("@@@@@@ QUESTION9A @@@@@@");
        printClusterElements(rUObject.clusterTree);
        rUObject.dataModel.get("QUESTION9B").firstAttemptedTime = calendar.getTime();
        rUObject.dataModel.get("QUESTION9B").activityAttemptCount = 3;
        rUObject.dataModel.get("QUESTION9B").totalTime = "PT3M";
        rUObject.dataModel.get("QUESTION9B").completionStatus = "completed";
        rUObject.dataModel.get("QUESTION9B").successStatus = "passed";
        rUObject.dataModel.get("QUESTION9B").scoreScaled = "0.75";
        calendar.add(Calendar.DATE, +1);
        rUObject.dataModel.get("QUESTION9B").lastAttemptedTime = calendar.getTime();
        rUObject.treeAnnotations.get("QUESTION9B").isAccessed = true;
        route = null;
        route = nouCourseManager.searchRouteMap(rUObject, "QUESTION9B");
        lItem = null;
        lItem = nouCourseManager.searchMyItem(cUserObject, route);
        lItem.overallRollupProcess(rUObject);
        System.out.println("@@@@@@ QUESTION9B @@@@@@");
        printClusterElements(rUObject.clusterTree);
        rUObject.treeAnnotations.get("EXAM").isAccessed = true;
        rUObject.treeAnnotations.get("EXAMMENU").isAccessed = true;
        rUObject.treeAnnotations.get("EXAM1").isAccessed = true;
        rUObject.treeAnnotations.get("EXAM2").isAccessed = true;
        rUObject.treeAnnotations.get("EXAM3").isAccessed = true;
    }

    /**
	 * Funci� que nom�s serveix per printar el TreeAnnotations.
	 * 
	 * @param usObj : UserObjective Type
	 */
    private void printTreeAnnotations(final UserObjective usObj) {
        System.out.println("\t--------- TreeAnnotations [" + usObj.treeAnnotations.size() + "] ---------");
        for (Iterator tAIterator = usObj.treeAnnotations.keySet().iterator(); tAIterator.hasNext(); ) {
            String keyString = (String) tAIterator.next();
            TreeAnnotations current = usObj.treeAnnotations.get(keyString);
            System.out.println("[[" + current.itemID + "] " + "currentView: " + current.currentView.toString() + " isAccessed: " + current.isAccessed + " sons: " + current.sons.toString() + "' ]");
        }
    }

    private void printItem(final UserObjective usObj, final String id, final String mostraNumeracio, final int count, final int level) {
        int nouCont = count;
        int nouLevel = level;
        System.out.print("\n");
        for (int i = 1; i <= level; i++) {
            System.out.print("\t");
        }
        System.out.print(mostraNumeracio + ".");
        System.out.print(count);
        System.out.print("[[" + usObj.treeAnnotations.get(id).itemID + "] " + "currentView: " + usObj.treeAnnotations.get(id).currentView.toString() + " isAccessed: " + usObj.treeAnnotations.get(id).isAccessed + " sons: " + usObj.treeAnnotations.get(id).sons.toString() + "' ]");
        nouLevel++;
        nouCont = 0;
        if (usObj.treeAnnotations.get(id).sons.size() > 0) {
            for (Iterator tAIterator = usObj.treeAnnotations.get(id).sons.iterator(); tAIterator.hasNext(); ) {
                String nou = (String) tAIterator.next();
                printItem(usObj, nou, mostraNumeracio + "." + count, ++nouCont, nouLevel);
            }
        }
    }

    /**
	 * Ens printar� la mem�ria total i la que ens queda disponible.
	 *
	 */
    private void printMemoryStatus() {
        System.out.println("[Total Memory: " + Runtime.getRuntime().totalMemory() + "] [Free Memory: " + Runtime.getRuntime().freeMemory() + "] [Max Memory: " + Runtime.getRuntime().maxMemory() + "] [Num CPU: " + Runtime.getRuntime().availableProcessors() + "]");
    }

    /**
	 * Printem i fem una copia de la HashTable!!!
	 * 
	 * @param currentHashMap : �s la taula temporal que volem copiar. 
	 */
    private void printClusterElements(final Hashtable<String, ClusterMap> currentHashMap) {
        if (currentHashMap != null) {
            Enumeration<String> claus = currentHashMap.keys();
            Enumeration<ClusterMap> elements = currentHashMap.elements();
            System.out.println("\t##### ClusterTree #####");
            while (claus.hasMoreElements()) {
                String novaClau = claus.nextElement();
                ClusterMap nouElement = elements.nextElement();
                System.out.println("[" + novaClau + "] ->" + nouElement.objective.size());
                Enumeration<String> clausObjective = nouElement.objective.keys();
                Iterator<ClusterDataModel> clusterDMobjective = nouElement.objective.values().iterator();
                while (clausObjective.hasMoreElements()) {
                    String noClau = clausObjective.nextElement();
                    ClusterDataModel nDM = clusterDMobjective.next();
                    System.out.println("\t->" + noClau + "\n" + "\tattempted: '" + convertIntStr(nDM.attempted) + "'\tcompleted: '" + convertIntStr(nDM.completed) + "'\tsatisfied: '" + convertIntStr(nDM.satisfied) + "'\tMeasure: '" + nDM.objectiveMeasure + "'");
                }
            }
        }
    }

    /**
	 * Printem els Objectives Map que tenim en el sistema (que s�n els
	 * objectes globals).
	 * 
	 * @param nouElement : ClusterMap Type
	 */
    private void printGlobalElements(final ClusterMap nouElement) {
        System.out.println("####### Global Objectives (" + nouElement.objective.size() + ") #######");
        Enumeration<String> clausObjective = nouElement.objective.keys();
        Iterator<ClusterDataModel> clusterDMobjective = nouElement.objective.values().iterator();
        while (clausObjective.hasMoreElements()) {
            String noClau = clausObjective.nextElement();
            ClusterDataModel nDM = clusterDMobjective.next();
            System.out.println("\t->" + noClau + "\n" + "'\tsatisfied: '" + convertIntStr(nDM.satisfied) + "'\tMeasure: '" + nDM.objectiveMeasure + "'");
        }
    }

    /**
	 * Fem una conversi� del valor num�ric al seu equivalent
	 * d'String perqu� sigui m�s entenedor a l'hora de printar-ho.
	 * 
	 * @param number : int Type
	 * @return String Type
	 */
    private String convertIntStr(final int number) {
        switch(number) {
            case NULL:
                return "NULL";
            case PASSED:
                return "PASSED";
            case FAILED:
                return "FAILED";
            case UNKNOWN:
                return "UNKNOWN";
            default:
                return "[ERROR]!";
        }
    }
}
