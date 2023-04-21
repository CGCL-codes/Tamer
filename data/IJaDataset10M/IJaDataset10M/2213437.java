package nl.utwente.ewi.hmi.deira.iam.vsiam;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.logging.Logger;
import natlang.rdg.documentplanner.DocumentPlanner;
import natlang.rdg.lexicalchooser.LexicalChooser;
import natlang.rdg.microplanner.MicroPlanner;
import natlang.rdg.model.RSGraph;
import natlang.rdg.surfacerealizer.SurfaceRealizer;
import nl.utwente.ewi.hmi.deira.generic.Module;
import nl.utwente.ewi.hmi.deira.mmm.MMM;
import nl.utwente.ewi.hmi.deira.queue.VSEvent;

public class VSAnalysis extends Module {

    private int eventCounter = 0;

    /** Event counter to maintain a unique event id for dispatch. */
    private nl.utwente.ewi.hmi.deira.mmm.MMM mmm;

    public boolean startStory;

    private boolean run = true;

    private boolean processStory = true;

    private int mode;

    private String storyFile = "../etc/Stories/test.txt";

    private static Logger log = Logger.getLogger("deira.vsiam");

    /** < Logging instance */
    public static final int MODE_NARRATOR = 0;

    public static final int MODE_FILE = 1;

    /**
	 * @param mmm
	 */
    public VSAnalysis(MMM mmm, int mode) {
        super("DEIRA VSIAM");
        this.mmm = mmm;
        this.mode = mode;
    }

    /**
	 * Returns a fresh unused event id. (increases eventCounter by 1 to do this)
	 * 
	 * @return a fresh unused event id.
	 */
    public int getNewEventID() {
        eventCounter++;
        return eventCounter;
    }

    /**
     */
    public void close() {
        run = false;
    }

    /**
	 */
    public void run() {
        runLoop: while (run) {
            while (!this.startStory) {
                synchronized (this) {
                    try {
                        this.wait(1000);
                        if (!run) {
                            break runLoop;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            switch(mode) {
                case VSAnalysis.MODE_NARRATOR:
                    log.info("VSIAM: Running Narrator...");
                    this.runNarratorProcessing();
                    break;
                case VSAnalysis.MODE_FILE:
                default:
                    log.info("VSIAM: Running text reading...");
                    this.runFileProcessing(storyFile);
                    break;
            }
            this.startStory = false;
        }
    }

    private void runNarratorProcessing() {
        boolean dpresult = true;
        DocumentPlanner documentplanner = new DocumentPlanner();
        MicroPlanner microplanner = new MicroPlanner();
        long time = 0;
        float importance = 0.6f;
        float decay = -1.0f;
        dpresult = documentplanner.transform();
        if (dpresult) {
            RSGraph dpoutput = documentplanner.getGraph();
            microplanner.setGraph(dpoutput);
            RSGraph mcoutput = microplanner.transform();
            com.hp.hpl.jena.ontology.OntModel ontmodel = documentplanner.getModel();
            LexicalChooser lc = microplanner.getLexicalChooser();
            SurfaceRealizer sr = new SurfaceRealizer(ontmodel);
            sr.setGraph(mcoutput);
            sr.setLexicalChooser(lc);
            synchronized (this) {
                String currentitem;
                try {
                    if (sr.transform()) {
                        Iterator<String> it = sr.getResult();
                        while (it.hasNext() && run && processStory) {
                            currentitem = it.next();
                            time = System.currentTimeMillis();
                            VSEvent sentence = new VSEvent(currentitem, eventCounter, time, "sentence", "", importance, decay);
                            mmm.processEvent(sentence);
                            this.wait(currentitem.length() * 50);
                        }
                    } else {
                        log.severe("VSIAM: Surface transformation failed!");
                    }
                } catch (Exception e) {
                    log.severe("VSIAM: Surface transformation failed!");
                    e.printStackTrace();
                }
            }
        }
    }

    private void runFileProcessing(String inputFile) {
        long time = 0;
        float importance = 0.6f;
        float decay = -1.0f;
        String currentitem;
        synchronized (this) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                currentitem = reader.readLine();
                while (currentitem != null && run && processStory) {
                    if (!currentitem.equals("")) {
                        time = System.currentTimeMillis();
                        VSEvent sentence = new VSEvent(currentitem, eventCounter, time, "sentence", "", importance, decay);
                        mmm.processEvent(sentence);
                        this.wait(currentitem.length() * 50);
                    }
                    currentitem = reader.readLine();
                }
            } catch (IOException e) {
                log.severe(MessageFormat.format("VSIAM: Read from file [{0}] failed!", inputFile));
            } catch (InterruptedException e) {
                log.severe("VSIAM: Thread interrupted!");
            }
        }
    }

    public String getModuleName() {
        return "VSIAM";
    }

    public void switchMode(int mode) {
        this.mode = mode;
        String modeString = ((mode == MODE_NARRATOR) ? "narrator" : "text");
        log.info("VSIAM: Mode switched to: " + modeString);
    }

    public void setFile(String file) {
        this.storyFile = file;
    }

    public void stopStory() {
        this.processStory = false;
        this.startStory = false;
    }

    public void startStory() {
        this.processStory = true;
        this.startStory = true;
        this.notifyAll();
    }
}
