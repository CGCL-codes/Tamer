package shieh.pnn;

import java.util.Hashtable;
import java.util.Arrays;
import shieh.pnn.core.Param;
import shieh.pnn.jgraph.JPNNApp;
import shieh.pnn.wm.Research;
import static shieh.pnn.jgraph.action.JProjectAction.*;
import static shieh.pnn.wm.Research.*;

/**
 * The Main Class
 * @author Danke
 *
 */
public class PhaseNeuralNetwork {

    public static String batchCommands[] = new String[] { NAME_RUNDRMORTHO, NAME_RUNDRMLEARNEDORTHO, NAME_RUNDRMMATHORTHO, NAME_OPTDRMMATHORTHO, NAME_RUNSEQENCODE, NAME_RUNDRMMATHSPAN, NAME_CHKDRMMATHORTHO, NAME_RUNDRMART, NAME_RUNDRMDUAL, NAME_OPTDRMDUAL, NAME_TRAINDRMDUAL, NAME_RUNDRMDUALNW, NAME_RUNDRMDUALWORD, NAME_OPTDUALCONFUSION, NAME_RUNDRMDUALUNTRAIN, NAME_OPTDRMDUALPSE, NAME_OPTDUALCONFUSION, NAME_RUNDRMDUALLISTLEN };

    public static void main(String[] args) {
        if (args.length == 0) {
            try {
                new JPNNApp().createApplication(null, new Hashtable());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
        String cmd = args[0];
        if (!Arrays.asList(batchCommands).contains(cmd) || cmd.equals("help")) {
            System.out.printf("Available commands: %s\n", Arrays.asList(batchCommands));
            System.exit(-1);
        }
        System.out.printf("Default parameters \n");
        Param.print();
        for (int i = 1; i < args.length; i++) {
            String part[] = args[i].split("=");
            if (part.length == 2) {
                Param.setParam(part[0], Double.parseDouble(part[1]));
                System.out.printf("%s = %f\n", part[0], Double.parseDouble(part[1]));
            }
        }
        if (cmd.equals(NAME_RUNDRMORTHO)) Research.doRunDRMOrtho(); else if (cmd.equals(NAME_RUNDRMLEARNEDORTHO)) Research.doRunDRMLearnOrtho(); else if (cmd.equals(NAME_RUNDRMMATHORTHO)) Research.doRunDRMMathOrtho(); else if (cmd.equals(NAME_OPTDRMMATHORTHO)) Research.doOptDRMMathOrtho(); else if (cmd.equals(NAME_RUNDRMART)) Research.doRunDRMArticulate(); else if (cmd.equals(NAME_RUNDRMMATHSPAN)) Research.doRunDRMMathSpan(); else if (cmd.equals(NAME_CHKDRMMATHORTHO)) Research.doChkDRMMathOrtho(); else if (cmd.equals(NAME_RUNSEQENCODE)) doRunSeqEncode(); else if (cmd.equals(NAME_RUNDRMDUAL)) Research.doRunDual(args[1], args[2], args.length >= 4 ? args[3] : null); else if (cmd.equals(NAME_RUNDRMDUALUNTRAIN)) Research.doRunDualUntrained(args[1], args[2], args.length >= 4 ? args[3] : null); else if (cmd.equals(NAME_OPTDRMDUAL)) Research.doOptDual(args[1], args[2]); else if (cmd.equals(NAME_TRAINDRMDUAL)) Research.doTrainDual(); else if (cmd.equals(NAME_OPTDRMDUALPSE)) Research.doOptDualPSE(); else if (cmd.equals(NAME_RUNDRMDUALNW)) Research.doRunDualNonword(); else if (cmd.equals(NAME_RUNDRMDUALWORD)) Research.doRunDualWord(); else if (cmd.equals(NAME_OPTDUALCONFUSION)) Research.doOptDualConfusion(); else if (cmd.equals(NAME_RUNDRMDUALLISTLEN)) Research.doRunDualListLen(args[1], args[2]);
    }
}
