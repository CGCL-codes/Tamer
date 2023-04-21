package diet.parameters;

import java.io.File;
import java.util.Vector;
import diet.parameters.ui.SavedExperimentsAndSettingsFile;

/**
 *
 * @author Greg
 */
public class DefaultSettingsFactory {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        DefaultSettingsFactory dsf = new DefaultSettingsFactory();
        dsf.getGeneralSettings();
    }

    public void getGeneralSettings() {
        IntParameter p = new IntParameter("Port Number", 4444);
        Vector v = new Vector();
        v.addElement(p);
        ExperimentSettings expSett = new ExperimentSettings(v);
        SavedExperimentsAndSettingsFile.writeAllParameterObjects(new File("GeneralSettings.xml"), v);
    }

    public ExperimentSettings getDefaultExperimentSettings() {
        ExperimentSettings expSettings = this.getWYSIWYGDisfluencyAndReplaceSettings();
        return expSettings;
    }

    public ExperimentSettings getWYSIWYGDisfluencySettings() {
        ExperimentSettings expSett = getDefaultExperimentParameters();
        expSett.changeParameterValue("Conversation Controller", "WYSIWYG Disfluency");
        expSett.changeParameterValue("Chat tool interface", "WYSIWYG Simplex single window");
        expSett.changeParameterValue("Transcript parser enabled", "DISABLED");
        expSett.changeParameterValue("Number of rows in chat window", 30);
        expSett.changeParameterValue("Number of columns in chat window", 30);
        return expSett;
    }

    public ExperimentSettings getWYSIWYGDisfluencyAndReplaceSettings() {
        ExperimentSettings expSett = getDefaultExperimentParameters();
        expSett.changeParameterValue("Conversation Controller", "WYSIWYG Disfluency & Replace");
        expSett.changeParameterValue("Chat tool interface", "WYSIWYG Simplex single window");
        expSett.changeParameterValue("Transcript parser enabled", "DISABLED");
        expSett.changeParameterValue("Number of rows in chat window", 30);
        expSett.changeParameterValue("Number of columns in chat window", 30);
        return expSett;
    }

    public ExperimentSettings getFRAGSettings() {
        ExperimentSettings expSett = getDefaultExperimentParameters();
        expSett.changeParameterValue("Conversation Controller", "Frag CR of nouns");
        expSett.changeParameterValue("Number of participants per conversation", 3);
        Vector acknowledgments = new Vector();
        acknowledgments.addElement("ok");
        acknowledgments.addElement("ok thanks");
        acknowledgments.addElement("thnks");
        acknowledgments.addElement("ok");
        acknowledgments.addElement("ok right");
        StringListParameter slp = new StringListParameter("Acknowledgments", acknowledgments);
        expSett.addParameter(slp);
        return expSett;
    }

    public ExperimentSettings getSBARSettings() {
        ExperimentSettings expSett = getDefaultExperimentParameters();
        IntParameter pCountdownToIntervention = new IntParameter("Turns till Next intervention", 0, 10);
        expSett.addParameter(pCountdownToIntervention);
        IntParameter pCountdownToInterventionMax = new IntParameter("Turns between interventions", 10, 20);
        expSett.addParameter(pCountdownToInterventionMax);
        IntParameter minDelayBetweenTurns = new IntParameter("Min inter-turn delay", 250, 250);
        expSett.addParameter(minDelayBetweenTurns);
        IntParameter maxDelayBetweenTurns = new IntParameter("Max inter-turn delay", 600, 600);
        expSett.addParameter(maxDelayBetweenTurns);
        IntParameter avgTypingTimePerChar = new IntParameter("Avg typing time per char", 20, 20);
        expSett.addParameter(avgTypingTimePerChar);
        expSett.changeParameterValue("Conversation Controller", "Split Utterance with parse");
        expSett.changeParameterValue("Number of participants per conversation", 3);
        return expSett;
    }

    public ExperimentSettings getDefaultExperimentParameters() {
        Vector v = new Vector();
        StringParameter sp;
        StringParameterFixed spf;
        IntParameter ip;
        sp = new StringParameter("Experiment ID", "No intervention");
        sp.setDescription("This is the default simple chat tool operation: no interventions are performed");
        v.addElement(sp);
        Vector spv = new Vector();
        spv.addElement("No intervention");
        spv.addElement("Split Utterance with parse");
        spv.addElement("Frag CR of nouns");
        spv.addElement("WYSIWYG Disfluency");
        spv.addElement("WYSIWYG Intercept & Disfluency");
        spf = new StringParameterFixed("Conversation Controller", spv, "No intervention");
        spf.setDescription("Pre-installed intervention templates. There are some interdependencies that place restrictions on the values of other parameters.");
        v.addElement(spf);
        ip = new IntParameter("Number of participants per conversation", 2);
        ip.setDescription("Determines when an experiment can proceed and also determines how many chat windows to display when run locally");
        v.addElement(ip);
        spv = new Vector();
        spv.addElement("Formulate revise then send. Single or multiple windows");
        spv.addElement("WYSIWYG Simplex single window");
        spv.addElement("WYSIWYG Duplex multiple windows");
        spf = new StringParameterFixed("Chat tool interface", spv, "Formulate revise then send. Single or multiple windows");
        spf.setDescription("The different kinds of chat tool. \n \"Formulate revise then send\" is the default, tpyical of most messenger apps. It allows single and multiple windows.\n WYSIWYG Simplex single window displays turns character-by-character and strictly enforces turn-taking. \n WYSIWYG Duplex multiple windows displays turns character-by-character with each participant's contributions placed in separate windows. It is similar to UNIX chat");
        v.addElement(spf);
        ip = new IntParameter("Number of windows per chat tool", 1);
        v.addElement(ip);
        spv = new Vector();
        spv.addElement("ONEWINDOWENABLED");
        spv.addElement("ONEWINDOWDISABLED");
        spv.addElement("EACHOWNWINDOWATTOPENABLED");
        spv.addElement("EACHOWNWINDOWATTOPDISABLED");
        spv.addElement("EACHOWNWINDOWRANDOMENABLED");
        spv.addElement("EACHOWNWINDOWRANDOMDISABLED");
        spf = new StringParameterFixed("Window numbering policy", spv, "ONEWINDOWENABLED");
        spf.setDescription("This determines which window each participant sees their own and each other's contributions");
        v.addElement(spf);
        spv = new Vector();
        spv.addElement("Horizontal");
        spv.addElement("Vertical");
        spf = new StringParameterFixed("Horizontal or vertical alignment of multiple windows", spv, "Vertical");
        v.addElement(spf);
        ip = new IntParameter("Number of rows in chat window", 10);
        v.addElement(ip);
        ip = new IntParameter("Number of rows in chat text entry area", 2);
        v.addElement(ip);
        ip = new IntParameter("Number of columns in chat window", 50);
        v.addElement(ip);
        spv = new Vector();
        spv.addElement("yes");
        spv.addElement("no");
        spf = new StringParameterFixed("Display parse tree", "no", spv, "no");
        v.addElement(spf);
        ip = new IntParameter("Parser Timeout", 500, 1000);
        v.addElement(ip);
        ip = new IntParameter("Parser Max Length (chars)", 70, 70);
        v.addElement(ip);
        spv = new Vector();
        spv.addElement("Enabled");
        spv.addElement("Disabled");
        spf = new StringParameterFixed("Transcript parser enabled", spv, "Disabled");
        v.addElement(spf);
        ip = new IntParameter("Transcript parser timeout", 500);
        v.addElement(ip);
        ip = new IntParameter("Typing status timeout (msecs)", 1000);
        v.addElement(ip);
        ExperimentSettings expSett = new ExperimentSettings(v);
        for (int i = 0; i < v.size(); i++) {
            System.err.println("VERIFYING PARAMETERS ");
            Object o = v.elementAt(i);
            if (o instanceof Parameter) {
                System.err.println(i + "VERIFYING PARAMETERS " + ((Parameter) o).getID());
            } else {
                System.err.println(i + " EXITING " + o.getClass().toString() + " " + o.toString());
                System.exit(10 * -i);
            }
        }
        return expSett;
    }
}
