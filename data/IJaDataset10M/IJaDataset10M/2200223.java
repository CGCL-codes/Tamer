package rcscene.validation.gui;

import java.io.Serializable;
import rcscene.ActionWeights;
import rcscene.ObjectWeights;
import rcscene.validation.SceneStats;
import rcscene.validation.Validator;

public class RoboTester implements Serializable {

    private static final long serialVersionUID = 1L;

    private Validator val;

    private String scene1;

    private String scene2;

    private int sceneSel;

    private int distCal;

    private int numBest;

    private ObjectWeights oWeights;

    private ActionWeights aWeights;

    private final int INIT_VALUE = -1;

    /** 
	 * The first constructor makes it possible for objects 
	 * of type RoboTester to be declared without having
	 * all parameters of the second constructor 
	 */
    public RoboTester() {
        scene1 = null;
        scene2 = null;
        sceneSel = INIT_VALUE;
        distCal = INIT_VALUE;
        numBest = INIT_VALUE;
        oWeights = null;
        aWeights = null;
    }

    /** Second constructor - uses user inputs to instanciate class variables */
    public RoboTester(String scene1, String scene2, int sceneSel, int distCal, int numBest, ObjectWeights oWeights, ActionWeights aWeights) {
        this.scene1 = scene1;
        this.scene2 = scene2;
        this.sceneSel = sceneSel;
        this.distCal = distCal;
        this.numBest = numBest;
        this.oWeights = oWeights;
        this.aWeights = aWeights;
    }

    /** Runs tests through validator */
    public SceneStats runTest(String path, boolean arg1) {
        if (validTest()) {
            val = new Validator(scene1, scene2, sceneSel, distCal, numBest, oWeights, aWeights, arg1);
            if (arg1 && path != null) val.setLogPath(path);
            return val.runValidation();
        }
        return null;
    }

    /**
	 * Makes sure that the test parameters are correct 
	 * and returns a boolean: true if valid
	 */
    public boolean validTest() {
        if (scene1 == null || scene2 == null || oWeights == null || aWeights == null || sceneSel == -1 || distCal == -1 || numBest == -1) {
            System.out.println("One or more parameters are not set. Aborting.");
            return false;
        } else {
            return true;
        }
    }

    /** The printing format for each test */
    public String toString() {
        return "Scene1: " + scene1 + "\nScene2: " + scene2 + "\nDistCal: " + distCal + "\nSceneSel: " + sceneSel + "\nNumBest: " + numBest + "\nObjectWeights: " + oWeights + "\nActionWeights: " + aWeights;
    }

    public void setScene1(String scene1) {
        this.scene1 = scene1;
    }

    public String getScene1() {
        return scene1;
    }

    public void setScene2(String scene2) {
        this.scene2 = scene2;
    }

    public String getScene2() {
        return scene2;
    }

    public void setSceneSel(int sceneSel) {
        this.sceneSel = sceneSel;
    }

    public int getSceneSel() {
        return sceneSel;
    }

    public void setDistCal(int distCal) {
        this.distCal = distCal;
    }

    public int getDistCal() {
        return distCal;
    }

    public void setNumBest(int numBest) {
        this.numBest = numBest;
    }

    public int getNumBest() {
        return numBest;
    }

    public void setOWeights(ObjectWeights oWeights) {
        this.oWeights = oWeights;
    }

    public ObjectWeights getOWeights() {
        return oWeights;
    }

    public void setAWeights(ActionWeights aWeights) {
        this.aWeights = aWeights;
    }

    public ActionWeights getAWeights() {
        return aWeights;
    }
}
