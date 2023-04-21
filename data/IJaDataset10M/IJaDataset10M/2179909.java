package org.peertrust.inference.prolog.yprolog;

import java.applet.Applet;
import java.io.File;
import java.util.StringTokenizer;
import java.util.Vector;
import org.apache.log4j.Logger;
import org.peertrust.config.Configurable;
import org.peertrust.exception.ConfigurationException;
import org.peertrust.exception.InferenceEngineException;
import org.peertrust.inference.InferenceEngine;
import org.peertrust.inference.LogicAnswer;
import org.peertrust.inference.LogicQuery;
import org.peertrust.inference.prolog.parser.*;
import org.peertrust.meta.Proof;
import org.peertrust.meta.Tree;
import org.peertrust.net.Peer;
import yprolog.*;

/**
 * <p>
 * 
 * </p><p>
 * $Id: YPrologEngine.java,v 1.6 2006/04/24 12:01:28 dolmedilla Exp $
 * <br/>
 * Date: 19-Jan-2006
 * <br/>
 * Last changed: $Date: 2006/04/24 12:01:28 $
 * by $Author: dolmedilla $
 * </p>
 * @author Daniel Olmedilla
 */
public class YPrologEngine implements InferenceEngine, Configurable {

    private static Logger log = Logger.getLogger(YPrologEngine.class);

    final String FILE_SEPARATOR = ":";

    private final String VALIDATE_PREDICATE = "validateProof";

    YProlog _engine;

    private String _prologFiles;

    private String _rdfFiles;

    private String _baseFolder;

    private boolean _debugMode;

    public YPrologEngine() {
        super();
        log.debug("$Id: YPrologEngine.java,v 1.6 2006/04/24 12:01:28 dolmedilla Exp $");
    }

    public void init() throws ConfigurationException {
        log.debug("Initializing " + this.getClass().getName());
        _engine = new YProlog();
        try {
            if (_debugMode) insert("debug_extra");
            if (_prologFiles != null) {
                StringTokenizer filesString;
                String tmp;
                filesString = new StringTokenizer(_prologFiles, FILE_SEPARATOR);
                while (filesString.hasMoreTokens()) {
                    tmp = _baseFolder + filesString.nextToken();
                    log.debug("Loading file " + tmp + " into the inference engine");
                    consultFile(tmp);
                    log.debug("File " + tmp + " loaded");
                }
            }
        } catch (InferenceEngineException e) {
            throw new ConfigurationException(e);
        }
        if (_rdfFiles != null) log.warn("PeerTrust using YProlog does not yet support RDF files");
    }

    public LogicAnswer[] processTree(LogicQuery logicQuery) throws InferenceEngineException {
        log.debug("Process logic query: " + logicQuery.getGoal() + " - " + logicQuery.getSubgoals());
        checkEngine();
        String query = "tree(" + logicQuery.getGoal() + "," + logicQuery.getSubgoals() + "," + logicQuery.getRequester() + ")";
        log.debug("Query: " + query);
        Vector results = _engine.queryToTable("processTree(" + query + ",Result)", 0, true);
        if (results == null) {
            log.debug("No answers");
            return null;
        }
        log.debug("Receiving from engine " + results.size() + " results");
        String[] result;
        PrologTerm term;
        LogicAnswer[] answers = new LogicAnswer[results.size()];
        for (int i = 0; i < results.size(); i++) {
            log.debug("Results " + i);
            result = (String[]) results.elementAt(i);
            try {
                for (int j = 0; j < result.length; j++) log.debug("Term: " + result[j]);
                term = PrologTools.getTerm(result[1]);
            } catch (ParseException e) {
                throw new InferenceEngineException(e);
            }
            if ((term == null) || (!(term instanceof PrologCompoundTerm))) throw new InferenceEngineException("Resulting value '" + term + "' is wrong"); else {
                PrologCompoundTerm t = (PrologCompoundTerm) term;
                String delegator = t.getArg(5).getText();
                if (delegator.equals("nil")) delegator = null;
                answers[i] = new LogicAnswer(t.getArg(0).getText(), t.getArg(4).getText(), t.getArg(1).getText(), t.getArg(2).getText(), t.getArg(3).getText(), delegator);
                log.debug("Answer: " + answers[i]);
            }
        }
        return answers;
    }

    public void unifyTree(Tree tree, String unifiedGoal) throws InferenceEngineException {
        String query = "old(" + tree.getGoal() + "," + tree.getLastExpandedGoal() + "," + tree.getResolvent() + ")";
        log.debug("Unify new query: " + unifiedGoal + " and old query: " + query);
        checkEngine();
        log.debug("unification(" + query + "," + unifiedGoal + ",Result)");
        String[] result = _engine.queryToTable("unification(" + query + "," + unifiedGoal + ",Result)");
        if (result == null) {
            log.debug("No answers");
            throw new InferenceEngineException("Error unifying " + query + " and " + unifiedGoal);
        }
        PrologTerm term;
        try {
            term = PrologTools.getTerm(result[0]);
        } catch (ParseException e) {
            throw new InferenceEngineException(e);
        }
        if ((term == null) || (!(term instanceof PrologCompoundTerm))) throw new InferenceEngineException("Resulting value '" + term + "' is wrong"); else {
            log.debug("Term: " + term);
            PrologCompoundTerm t = (PrologCompoundTerm) term;
            tree.setLastExpandedGoal(null);
            tree.setGoal(t.getArg(0).getText());
            tree.setResolvent(t.getArg(2).getText());
        }
    }

    public boolean execute(String query) throws InferenceEngineException {
        checkEngine();
        String result = _engine.queryToString(query);
        if (result == null) return false; else return true;
    }

    public boolean validate(String goal, Peer prover, Proof proof) throws InferenceEngineException {
        log.debug("Validating Goal " + goal + " at " + prover.getAlias() + " with proof \n\t" + proof);
        checkEngine();
        String query = VALIDATE_PREDICATE + "(" + goal + "," + prover.getAlias() + "," + proof.toString() + ")";
        log.debug("Validate: " + query);
        return execute(query);
    }

    public void insert(String clause) throws InferenceEngineException {
        checkEngine();
        _engine.consult(clause + ".");
    }

    public void setApplet(Applet applet) throws InferenceEngineException {
        log.warn("YProlog does not distinguish between normal or applet mode");
    }

    public void checkEngine() throws InferenceEngineException {
        if (_engine == null) throw new InferenceEngineException("Engine not initialized");
    }

    /**
	 * @return Returns the _baseFolder.
	 */
    public String getBaseFolder() {
        return _baseFolder;
    }

    /**
	 * @param folder The _baseFolder to set.
	 */
    public void setBaseFolder(String folder) {
        _baseFolder = folder;
    }

    public boolean getDebugMode() {
        return _debugMode;
    }

    public void setDebugMode(boolean debug) throws InferenceEngineException {
        _debugMode = debug;
    }

    public void setPrologFiles(String files) {
        _prologFiles = files;
    }

    public String getPrologFiles() {
        return _prologFiles;
    }

    public void setRdfFiles(String files) {
        _rdfFiles = files;
    }

    public String getRdfFiles() {
        return _rdfFiles;
    }

    public void consultFile(String fileName) throws InferenceEngineException {
        File f = new File(fileName);
        if (!f.exists()) throw new InferenceEngineException("File " + fileName + " does not exist in " + System.getProperty("user.dir"));
        _engine.consultFile(fileName);
    }

    public static void main(String[] args) {
        YProlog engine = new YProlog();
        engine.consult("append([],L,L).");
        engine.consult("append([A|B],L,[A|Tail]) :- append(B,L,Tail).");
        String query = "asserta(a(1))";
        String value = engine.queryToString(query);
        if (value != null) log.debug(value.toString());
        String[] result = engine.queryToTable(query);
        if (result != null) for (int i = 0; i < result.length; i++) log.debug("Result " + i + ":" + result[i]);
        Vector results = engine.queryToTable(query, 0, true);
        if (results != null) for (int i = 0; i < results.size(); i++) {
            log.debug("Results " + i);
            result = (String[]) results.elementAt(i);
            for (int j = 0; j < result.length; j++) log.debug("Result" + j + ": " + result[j]);
        }
    }
}
