package patho.textmining;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.HashSet;
import org.postgresql.util.PSQLException;
import patho.textmining.gui.*;
import patho.textmining.icd.DictionaryNode;
import patho.textmining.icd.PriorityCodeTuple;
import patho.textmining.receptor.ReceptorEntry;

/**
 * TextMining Base Program Class
 * Control Class for the Textmining Program
 *
 * @author  Reihs Robert
 * @author  Sauer Stefan
 * @version 2.0
 */
public class TextMining {

    private LogViewer log_ = new LogViewer();

    private DictionaryNode dictionaryRoot_;

    private DictionaryNode localDictionaryRoot_;

    private DiseaseStorage storage_;

    private UpdateStorage storageUpdate_;

    private patho.textmining.staging.StagingParser parser_staging_ = null;

    private patho.textmining.receptor.ReceptorParser parser_receptor_ = null;

    private HashSet<String> cmd_args_ = null;

    private boolean eval_;

    private String evalString_ = "";

    private DiseaseEntry lastDiseaseEntry_ = null;

    private HashMap<String, String> lastStaging_ = null;

    private HashMap<String, ReceptorEntry> lastReceptor_ = null;

    private PriorityCodeTuple lastPriorityCodeTuple_ = null;

    /**
   * TextMining Base Program
   * This will load and set up everything
   * @param command line arguments
   */
    public TextMining(String[] args) {
        cmd_args_ = new HashSet<String>();
        for (String t : args) cmd_args_.add(t);
        parser_staging_ = new patho.textmining.staging.StagingParser(cmd_args_, log_);
        parser_receptor_ = new patho.textmining.receptor.ReceptorParser(cmd_args_, log_);
        if (cmd_args_.contains("-GUI")) log_.setVisible(true); else log_.setVisible(false);
        boolean isLocal = false;
        try {
            log_.out_printTime(false, false);
            log_.out_println("creating dictionary...");
            dictionaryRoot_ = new DictionaryNode(log_, true);
            log_.out_printTime(false, false);
            log_.out_println("creating dictionary...done");
        } catch (org.postgresql.util.PSQLException ex) {
            System.err.println("TextMining::TextMining - PSQLException Error: " + ex.getMessage());
            System.exit(-1);
        }
        log_.out_printTime(false, false);
        log_.out_println("creating disease storage...");
        storage_ = new DiseaseStorage(log_);
        storageUpdate_ = new UpdateStorage(log_);
        log_.out_printTime(false, false);
        log_.out_println("creating disease storage...done");
    }

    /**
   * Start commando
   */
    public void start() {
        patho.generic.PrintTime pt = new patho.generic.PrintTime();
        boolean multithread = false;
        if (!multithread) {
            System.out.println(pt.getTime(true));
            threadWorker(0);
            System.out.println(pt.getTime(true));
        } else {
            Thread t1 = new Thread(new Runnable() {

                public void run() {
                    threadWorker(1);
                }

                ;
            });
            Thread t2 = new Thread(new Runnable() {

                public void run() {
                    threadWorker(2);
                }

                ;
            });
            Thread t3 = new Thread(new Runnable() {

                public void run() {
                    threadWorker(3);
                }

                ;
            });
            t1.setName("TextMining Thread 1");
            t2.setName("TextMining Thread 2");
            t3.setName("TextMining Thread 3");
            t1.start();
            t2.start();
            t3.start();
            try {
                t1.join();
                t2.join();
                t3.join();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void start(int disease_id) {
        eval_ = true;
        patho.generic.PrintTime pt = new patho.generic.PrintTime();
        storage_.enablePartialLoad(new int[] { disease_id });
        dictionaryRoot_.enableEval();
        System.out.println(pt.getTime(true));
        threadWorker(0);
        System.out.println(pt.getTime(true));
    }

    public patho.evaldisplay.EvalStringNode getEvalStringNode() {
        return dictionaryRoot_.getEvalStringNode();
    }

    public DiseaseEntry getLastDiseaseEntry() {
        return lastDiseaseEntry_;
    }

    public PriorityCodeTuple getLastPriorityCodeTuple() {
        return lastPriorityCodeTuple_;
    }

    public HashMap<String, String> getLastStaging() {
        return lastStaging_;
    }

    /**
   *  ThreadWorker - Code Encoder
   *  is designed to work in threads (but not now!)
   *  @param Unique Worker identification number
   */
    private void threadWorker(int id) {
        DiseaseEntry de = null;
        synchronized (storage_) {
            de = storage_.getNextEntry();
        }
        while (de != null) {
            if (cmd_args_.contains("-ICD")) {
                de.preparTextForICD();
                if (eval_) {
                    dictionaryRoot_.clearEval();
                }
                dictionaryRoot_.parse(null, de, 0);
                PriorityCodeTuple tuple3 = dictionaryRoot_.getCodesForDiseaseID(de.id);
                if (eval_) {
                    lastDiseaseEntry_ = de;
                    lastPriorityCodeTuple_ = tuple3;
                } else {
                    synchronized (storageUpdate_) {
                        storageUpdate_.updateICD(de, tuple3);
                    }
                }
            }
            if (!parser_staging_.isDisabled()) {
                de.preparedTextForStaging();
                parser_staging_.setUp(de);
                parser_staging_.parse();
                synchronized (storageUpdate_) {
                    if (eval_) lastStaging_ = parser_staging_.getStagingForDiseaseID();
                    storageUpdate_.updateStaging(de, parser_staging_.getStagingForDiseaseID());
                }
            }
            if (!parser_receptor_.isDisabled()) {
                de.preparedTextForRezeptor();
                parser_receptor_.setUp(de);
                parser_receptor_.parse();
                synchronized (storageUpdate_) {
                    if (eval_) lastReceptor_ = parser_receptor_.getReceptorForDiseaseID();
                    storageUpdate_.updateReceptor(de, parser_receptor_.getReceptorForDiseaseID());
                }
            }
            synchronized (storage_) {
                de = storage_.getNextEntry();
            }
        }
        synchronized (storageUpdate_) {
            storageUpdate_.updateDB();
        }
    }
}
