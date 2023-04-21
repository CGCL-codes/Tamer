package vaani.swar;

import edu.cmu.sphinx.frontend.util.Microphone;
import edu.cmu.sphinx.jsapi.JSGFGrammar;
import edu.cmu.sphinx.recognizer.Recognizer;
import edu.cmu.sphinx.result.Result;
import edu.cmu.sphinx.util.props.ConfigurationManager;
import java.io.IOException;
import java.net.URL;
import javax.speech.recognition.GrammarException;
import javax.speech.recognition.RuleGrammar;
import javax.speech.recognition.RuleParse;
import vaani.swar.grammar.ApplicationGrammarConstructor;
import vaani.swar.grammar.InstantMessageGrammarConstructor;

/**
 *
 * @author sourcemorph
 */
public class SpeechHandler {

    private static Recognizer recognizer;

    private static Microphone microphone;

    private static JSGFGrammar jsgfGrammarManager;

    public static void init() {
        URL url = SpeechHandler.class.getResource("grammar/jsgf.config.xml");
        ConfigurationManager cm = new ConfigurationManager(url);
        recognizer = (Recognizer) cm.lookup("recognizer");
        microphone = (Microphone) cm.lookup("microphone");
        jsgfGrammarManager = (JSGFGrammar) cm.lookup("jsgfGrammar");
        recognizer.allocate();
        InstantMessageGrammarConstructor.constructGrammar();
        ApplicationGrammarConstructor.constructGrammar();
    }

    public static boolean isAvailable() {
        if (recognizer == null || microphone == null || jsgfGrammarManager == null) {
            return false;
        }
        return true;
    }

    public static String execute() throws IOException, GrammarException {
        if (microphone.startRecording()) {
            String rec = loadAndRecognize("grammar.instantmessage");
            if (rec != null) {
                return rec;
            } else {
                rec = loadAndRecognize("application");
                return rec;
            }
        } else {
            System.out.println("Error: could not connect to microphone");
        }
        return null;
    }

    private static String loadAndRecognize(String grammarName) throws IOException, GrammarException {
        jsgfGrammarManager.loadJSGF(grammarName);
        return recognizeAndReport();
    }

    private static String recognizeAndReport() throws GrammarException {
        Result result = recognizer.recognize();
        String bestResult = result.getBestFinalResultNoFiller();
        RuleGrammar ruleGrammar = jsgfGrammarManager.getRuleGrammar();
        RuleParse ruleParse = ruleGrammar.parse(bestResult, null);
        if (ruleParse != null) {
            System.out.println("\n  " + bestResult + "\n");
            return bestResult;
        }
        return null;
    }

    public static void stop() {
        if (recognizer != null) {
            recognizer.deallocate();
        }
    }

    public static void main(String[] args) {
        init();
        try {
            execute();
        } catch (GrammarException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
