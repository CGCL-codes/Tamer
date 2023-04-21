package dva.acuitytest;

import dva.DvaCheckerException;
import dva.displayer.Element;
import dva.util.DvaLogger;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author J-Chris
 */
public abstract class AcuityTest {

    /** Creates a new instance of AcuityTest */
    public AcuityTest() {
    }

    public void setName(String testName) {
        this.testName = testName;
    }

    public void setPatientdir(File patientdir) {
        this.patientdir = patientdir;
    }

    public File getPatientdir() {
        return patientdir;
    }

    public void saveAnswer(long answerTime, Element element, boolean patientAnswer, String patientAnswerStr) {
        getTestAnswers().add(new TestAnswer(answerTime, patientAnswer, element, patientAnswerStr));
    }

    public void saveAnswer(Element element, boolean patientAnswer, String patientAnswerStr) {
        getTestAnswers().add(new TestAnswer(patientAnswer, element, patientAnswerStr));
    }

    public abstract String getTestName();

    public abstract void init();

    public void setCurrentElement(Element current) {
        this.current = current;
    }

    public Element getCurrentElement() {
        return this.current;
    }

    public abstract Element getNext() throws DvaCheckerException;

    protected void setMaxStep(int maxStep) {
        this.maxStep = maxStep;
    }

    public abstract boolean isFailureFatal();

    public boolean isTestFailed() {
        return testFailed;
    }

    protected void setTestFailed(boolean testFailed) {
        this.testFailed = testFailed;
    }

    protected int getMaxStep() {
        return maxStep;
    }

    public boolean isTestDone() {
        return testDone;
    }

    protected void setTestDone(boolean testDone) {
        this.testDone = testDone;
    }

    protected Random getRandom() {
        return random;
    }

    public String getOperatorInstruction() {
        return resourceBundle.getString("message.acuitytest." + getTestName() + ".operatorinstruction", String.valueOf(treadmillSpeed));
    }

    public String toXml() {
        StringBuffer sb = new StringBuffer("<acuitytest name=\"");
        sb.append(this.getTestName());
        sb.append("\" treadmillspeed=\"");
        sb.append(this.treadmillSpeed);
        sb.append("\" date=\"");
        sb.append(formatter.format(startDate));
        sb.append("\" eye=\"");
        sb.append(this.eye);
        sb.append("\">");
        sb.append("<result value=\"");
        sb.append(this.getConvergenceValue());
        sb.append("\" stddev=\"");
        sb.append(this.getConvergenceStdDev());
        sb.append("\">");
        sb.append(this.getResultComment());
        sb.append("</result>");
        sb.append("<answers>");
        for (TestAnswer ta : testAnswers) {
            sb.append(ta.toXml());
        }
        sb.append("</answers></acuitytest>");
        sb.toString();
        return sb.toString();
    }

    public void setEye(String eye) {
        this.eye = eye;
    }

    public String getEye() {
        return this.eye;
    }

    public String getFileDesc() {
        return formatter2.format(startDate) + "_" + Math.round(this.getTreadmillSpeed()) + "kmh_" + this.eye;
    }

    public void toFile() throws AcuityTestFileCreationException {
        toFile("");
    }

    public void toFile(String params) throws AcuityTestFileCreationException {
        File actuitestfile = null;
        try {
            actuitestfile = new File(patientdir + ("/" + getFileDesc() + params + "_acuitytest-data.xml"));
            actuitestfile.createNewFile();
            DvaLogger.debug(AcuityTestManager.class, "actuitestfile:" + actuitestfile.getAbsoluteFile());
            FileUtils.writeStringToFile(actuitestfile, toXml());
        } catch (IOException ioex) {
            throw new AcuityTestFileCreationException(actuitestfile, ioex);
        }
    }

    public static SimpleDateFormat getDateFormatter() {
        return formatter;
    }

    public static SimpleDateFormat getDateFormatter2() {
        return formatter2;
    }

    private ArrayList<TestAnswer> testAnswers = new ArrayList<TestAnswer>();

    private Random random = new Random();

    private int maxStep = 20;

    private boolean testDone = false;

    private float treadmillSpeed = 0;

    private boolean testFailed = false;

    private static SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy - HH:mm:ss");

    private static SimpleDateFormat formatter2 = new SimpleDateFormat("MM-dd-yyyy_HH-mm-ss");

    private Date startDate = null;

    private Element current = null;

    private String testName = "";

    private String resultComment = "";

    private double convergenceValue = 0.0;

    private double convergenceStdDev = 0.0;

    private String eye = "EYE";

    private File patientdir = null;

    protected dva.util.MessageResources resourceBundle = new dva.util.MessageResources("dva/Bundle");

    public ArrayList<TestAnswer> getTestAnswers() {
        return testAnswers;
    }

    protected TestAnswer getLastAnswer() {
        return getTestAnswers().get(getTestAnswers().size());
    }

    public float getTreadmillSpeed() {
        return treadmillSpeed;
    }

    public void setTreadmillSpeed(float treadmillSpeed) {
        this.treadmillSpeed = treadmillSpeed;
    }

    public Date getStartDate() {
        return startDate;
    }

    public String getStartDateAsString() {
        return formatter.format(startDate);
    }

    public void setStartDate() {
        startDate = new Date();
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setResult(String resultComment) {
        this.resultComment = resultComment;
    }

    public double getConvergenceValue() {
        return convergenceValue;
    }

    public void setConvergenceValue(double convergenceValue) {
        this.convergenceValue = convergenceValue;
    }

    public double getConvergenceStdDev() {
        return convergenceStdDev;
    }

    public void setConvergenceStdDev(double convergenceStdDev) {
        this.convergenceStdDev = convergenceStdDev;
    }

    public String getResultComment() {
        return resultComment;
    }
}
