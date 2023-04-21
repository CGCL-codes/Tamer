package com.fujitsu.arcon.njs.priest;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.Vector;
import org.unicore.ajo.AbstractTask;
import org.unicore.ajo.ExecuteTask;
import org.unicore.ajo.Option;
import org.unicore.ajo.Portfolio;
import org.unicore.ajo.ScriptType;
import org.unicore.outcome.AbstractActionStatus;
import org.unicore.outcome.AbstractTask_Outcome;
import org.unicore.resources.Application;
import org.unicore.resources.Context;
import org.unicore.resources.Resource;
import org.unicore.resources.SoftwareResource;
import org.unicore.sets.OptionEnumeration;
import org.unicore.sets.ResourceEnumeration;
import com.fujitsu.arcon.njs.NJSGlobals;
import com.fujitsu.arcon.njs.actions.EKnownAction;
import com.fujitsu.arcon.njs.actions.IncarnatedPortfolio;
import com.fujitsu.arcon.njs.actions.MappedStorage;
import com.fujitsu.arcon.njs.logger.Logger;
import com.fujitsu.arcon.njs.logger.LoggerManager;
import com.fujitsu.arcon.njs.utils.NJSThreads;

/**
 *
 * Common and generic behaviour for the incarnation and execution of all
 * AbstractTasks that run on a target system (not in NJS).
 *
 * @author Sven van den Berghe, fujitsu 
 *
 * @version $Revision: 1.5 $ $Date: 2006/02/16 14:12:13 $
 *
 **/
public abstract class Task {

    public abstract static class Seminary implements com.fujitsu.arcon.njs.priest.Seminary {

        public Seminary() {
            students = new ArrayList(11);
        }

        /**
		 * The set of all Priests that are defined as able to
		 * incarnate and execute the concrete AbstractTask type.
		 * These are differentiated by being executed on different
		 * hosts (and so contain a different instance of an Target System)
		 * they may or may not have the same incarnation rules.
		 *
		 * This is an ordered set so that we can be deterministic
		 * about the matching algorithm wrt order in the IDB
		 *
		 **/
        protected List students;

        public void add(com.fujitsu.arcon.njs.priest.Priest priest) {
            students.add(priest);
        }

        public Iterator iterator() {
            return students.iterator();
        }

        protected Reader reader;

        public MissalReader getReader() {
            return reader;
        }

        /**
		 * Return any old Priest
		 *
		 **/
        public com.fujitsu.arcon.njs.priest.Priest getPriest() {
            synchronized (this) {
                if (students.size() == 0) reader.endAction(null);
            }
            return (Priest) students.get(0);
        }

        /**
		 * Get a Priest that will incarnate and execute this Task.
		 *
		 * V4 No Hosts anymore. Selection is purely on requested resources
		 * following task selection. If there are multiple TSIs, then 
		 * select by being only one for the task (but other resources must fit
		 * - don't know how limits get to the user though? - or by setting
		 * specific SoftwareResources.
		 *
		 **/
        public com.fujitsu.arcon.njs.priest.Priest getPriest(EKnownAction eka) throws com.fujitsu.arcon.njs.interfaces.NJSException {
            AbstractTask task = (AbstractTask) eka.getAction();
            synchronized (this) {
                if (students.size() == 0) {
                    reader.endAction(null);
                }
            }
            Priest p = null;
            if (task.getResources() == null || !(task.getResources().elements().hasMoreElements())) {
                p = (Priest) students.get(0);
                eka.logTrace("Selecting the default TSI <" + p.getTargetSystem().getName() + ">");
            } else {
                Iterator i = students.iterator();
                while (i.hasNext()) {
                    Priest candidate = (Priest) i.next();
                    if (candidate.getTargetSystem().acceptsResources(task.getResources())) {
                        p = candidate;
                        break;
                    }
                }
                if (p == null) {
                    throw new com.fujitsu.arcon.njs.interfaces.NJSException("There are no hosts on this Vsite that can provide all the resources requested");
                }
                eka.logTrace("Selecting TSI <" + p.getTargetSystem().getName() + ">");
            }
            p.incarnateMappedStorage(eka);
            return p;
        }
    }

    public static class Priest implements com.fujitsu.arcon.njs.priest.Priest {

        public static String CAT = "/bin/cat ";

        public static String CP = "/bin/cp -r ";

        public static String FIND = "/bin/find ";

        public static String LS = "/bin/ls ";

        public static String LSA = "/bin/ls -A ";

        public static String LN = "/bin/ln -s ";

        public static String MKDIR = "/bin/mkdir -p -m700 ";

        public static String MV = "/bin/mv ";

        public static String PRINTF = "/usr/bin/printf ";

        public static String RM = "/bin/rm ";

        public static String SH = "/bin/sh ";

        public static String SHV = "";

        public static String SED = "/bin/sed ";

        public static String TR = "/usr/bin/tr -s ";

        public static String TOUCH = "/usr/bin/touch ";

        public static String MKFIFO = "/bin/mkfifo -m600 ";

        public static String LIMIT = "ulimit ";

        public static String getPFContents(String pf) {
            return "`" + catPFContents(pf) + "`";
        }

        public static String catPFContents(String pf) {
            return " " + CAT + pf + " | " + TR + "\"\\012\" \" \" ";
        }

        public static String doSed(String orig, String sub) {
            return SED + "-e \"s/" + orig + "/" + sub + "/g\"";
        }

        protected InvocationLineDeacon invocation;

        public InvocationLineDeacon getInvocation() {
            return invocation;
        }

        private String name;

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        protected Logger logger;

        protected Priest(String name) {
            this.name = name;
            invocation = new InvocationLineDeacon("From Task level");
            logger = LoggerManager.get("Priests");
        }

        public boolean isBatchTargetSystemOnly() {
            return true;
        }

        /**
		 * Which TSI to use when incarnating and executing
		 *
		 **/
        protected TargetSystem target_system;

        protected void setTargetSystem(TargetSystem ab) {
            target_system = ab;
        }

        public TargetSystem getTargetSystem() {
            return target_system;
        }

        public void incarnateMappedStorage(EKnownAction eka) throws com.fujitsu.arcon.njs.interfaces.NJSException {
            if (eka.hasStorageErrors()) throw new com.fujitsu.arcon.njs.interfaces.NJSException(eka.getStorageErrorMessages());
            target_system.incarnateMappedStorage(eka.getRequestedUspace());
            target_system.incarnateMappedStorage(eka.getRequestedAlternativeUspace());
            target_system.incarnateMappedStorage(eka.getRequestedSpool());
            target_system.incarnateMappedStorage(eka.getRequestedStorage());
        }

        private com.fujitsu.arcon.njs.interfaces.ExecuteTaskChecker external_checker;

        /**
		 * Return an instance of the (possibly local) class
		 * that implements the checking policy on ExecuteTasks.
		 *
		 **/
        protected com.fujitsu.arcon.njs.interfaces.ExecuteTaskChecker getExternalChecker() {
            if (external_checker == null) {
                String class_name = System.getProperty("njs.checker_class", "com.fujitsu.arcon.njs.priest.CheckerImpl");
                Class new_class = null;
                try {
                    new_class = Class.forName(class_name);
                } catch (ClassNotFoundException ex) {
                    logger.severe("ExecuteTask Checker class <" + class_name + "> could not be found. \n" + "CLASSPATH OK? The class name fully qualified?");
                    com.fujitsu.arcon.njs.NJSGlobals.goToLimbo();
                }
                try {
                    external_checker = (com.fujitsu.arcon.njs.interfaces.ExecuteTaskChecker) new_class.newInstance();
                    external_checker.init(target_system.getResources());
                } catch (Exception ex) {
                    logger.severe("Problem creating an instance of the ExecuteTask Checker class <" + class_name + ">", ex);
                    com.fujitsu.arcon.njs.NJSGlobals.goToLimbo();
                }
                logger.config("External class used for ExecuteTask checking <" + class_name + ">");
            }
            return external_checker;
        }

        public void incarnate(EKnownAction eka) {
        }

        public void simpleQuery(EKnownAction eka) throws com.fujitsu.arcon.njs.interfaces.NJSException {
            AbstractActionStatus new_status = target_system.query(eka);
            if (new_status.isEquivalent(AbstractActionStatus.DONE)) {
                if (eka.getOutcome() instanceof AbstractTask_Outcome) ((AbstractTask_Outcome) eka.getOutcome()).setIncarnationStatus(null);
            } else {
                if (eka.getOutcome() instanceof AbstractTask_Outcome) ((AbstractTask_Outcome) eka.getOutcome()).setIncarnationStatus(new_status);
            }
        }

        /**
		 * Check on the status of an executing Task. This is where
		 * we get the first indication that the execution is complete
		 * and so can try to fecth the Outcome and complete.
		 *
		 **/
        public void query(EKnownAction eka) throws com.fujitsu.arcon.njs.interfaces.NJSException {
            AbstractActionStatus new_status = target_system.query(eka);
            if (new_status.isEquivalent(AbstractActionStatus.DONE)) {
                if (fetchOutcome(eka)) {
                    if (eka.wasAborted()) {
                        eka.setStatus(AbstractActionStatus.KILLED);
                    }
                }
                if (eka.getOutcome() instanceof AbstractTask_Outcome) ((AbstractTask_Outcome) eka.getOutcome()).setIncarnationStatus(null);
            } else {
                eka.setStatus(new_status);
                if (eka.getOutcome() instanceof AbstractTask_Outcome) ((AbstractTask_Outcome) eka.getOutcome()).setIncarnationStatus(new_status);
            }
            if (Thread.currentThread() instanceof NJSThreads.Thread) ((NJSThreads.Thread) Thread.currentThread()).checkForPause();
        }

        /**
		 * Abort execution of the Task. 
		 *
		 **/
        public void abort(EKnownAction eka) throws com.fujitsu.arcon.njs.interfaces.NJSException {
            target_system.abort(eka);
        }

        /**
		 * Cancel execution of the Task. 
		 *
		 **/
        public void cancel(EKnownAction eka) throws com.fujitsu.arcon.njs.interfaces.NJSException {
            target_system.cancel(eka);
        }

        /**
		 * Hold execution of the Task. 
		 *
		 **/
        public void hold(EKnownAction eka, boolean hold_action) throws com.fujitsu.arcon.njs.interfaces.NJSException {
            target_system.hold(eka, hold_action);
        }

        /**
		 * Resume execution of the Task. 
		 *
		 **/
        public void resume(EKnownAction eka) throws com.fujitsu.arcon.njs.interfaces.NJSException {
            target_system.resume(eka);
        }

        /**
		 * Get the Outcome of executing a task from the TargetSystem
		 * if this is successful, then proces it and set the status etc
		 *
		 **/
        private boolean fetchOutcome(EKnownAction eka) throws com.fujitsu.arcon.njs.interfaces.NJSException {
            boolean ok = target_system.fetchExecutionResults(eka);
            if (eka.wasCancelled() || eka.wasAborted()) return true;
            if (ok) {
                parser.process(eka);
            } else {
                eka.logTrace("Did not fetch execution results, continuing execution");
            }
            return ok;
        }

        protected OutcomeParser.I parser;

        /**
		 * Extract the unique matching string of the requested software resources
		 * from the Task's resources into a Vector (old Context processing)
		 *
		 **/
        protected Vector makeContextSet(AbstractTask task) {
            Vector v = new Vector();
            if (task.getResources() == null) return v;
            ResourceEnumeration re = task.getResources().elements();
            while (re.hasMoreElements()) {
                Resource r = re.nextElement();
                if (r instanceof SoftwareResource) {
                    v.addElement(Task.makeSWRName((SoftwareResource) r));
                }
            }
            return v;
        }

        /**
		 * Create the script lines to set the task's Environment vars
		 *
		 **/
        protected void setEnvVars(StringBuffer script, EKnownAction eka) {
            ExecuteTask task = (ExecuteTask) eka.getAction();
            script.append("UC_DECISION_FILE=" + eka.getUspace().getDirectory() + ".decision_" + Integer.toHexString(task.getId().getValue()) + ";export UC_DECISION_FILE\n");
            if (task.getEnvironmentVariables() != null) {
                OptionEnumeration e = task.getEnvironmentVariables().elements();
                while (e.hasMoreElements()) {
                    Option o = e.nextElement();
                    script.append(o.getToken().trim() + "=" + (o.getValue() == null ? "true" : o.getValue().trim()) + "; export " + o.getToken().trim() + "\n");
                }
            }
        }

        /**
		 * Handle Stdin (if user wants) -- call at right time though
		 *
		 **/
        protected void doStdin(StringBuffer command, ExecuteTask task, MappedStorage.WP ms) throws com.fujitsu.arcon.njs.interfaces.NJSException {
            Portfolio p = task.getStdin();
            if (p != null && !p.getName().equals("")) {
                try {
                    command.append("< `" + CAT + ms.createExisting(p.getId()).getContentsFileName() + "` ");
                } catch (com.fujitsu.arcon.njs.interfaces.NJSException nex) {
                    throw new com.fujitsu.arcon.njs.interfaces.NJSException("Source Portfolio for stdin redirection not found <" + p.getName() + ">");
                }
            }
        }

        /**
		 * Handle Stdout (if user wants) -- call at right time though
		 **/
        protected void doStdout(StringBuffer command, StringBuffer script, ExecuteTask task, MappedStorage.WP ms) throws com.fujitsu.arcon.njs.interfaces.NJSException {
            if (task.stdoutIsRedirected()) {
                IncarnatedPortfolio ipf;
                try {
                    ipf = ms.createNew(task.getStdout().getId());
                } catch (com.fujitsu.arcon.njs.interfaces.NJSException ipex) {
                    throw new com.fujitsu.arcon.njs.interfaces.NJSException("stdout Portfolio is already in the Uspace <" + task.getStdout().getName() + "/" + Integer.toHexString(task.getStdout().getId().getValue()) + ">");
                }
                String file_name = task.getStdoutFileName();
                if (file_name == null || file_name.equals("")) {
                    file_name = "stdout_" + Integer.toHexString(task.getId().getValue());
                }
                file_name = file_name.trim().replace(' ', '_');
                fillInfoFile(script, file_name, ipf.getContentsFileName());
                command.append("> " + file_name + " ");
            }
        }

        /**
		 * Handle Stderr (if user wants) -- call at right time though
		 **/
        protected void doStderr(StringBuffer command, StringBuffer script, ExecuteTask task, MappedStorage.WP us) throws com.fujitsu.arcon.njs.interfaces.NJSException {
            if (task.stderrIsRedirected()) {
                IncarnatedPortfolio ipf;
                try {
                    ipf = us.createNew(task.getStderr().getId());
                } catch (com.fujitsu.arcon.njs.interfaces.NJSException ipex) {
                    throw new com.fujitsu.arcon.njs.interfaces.NJSException("stderr Portfolio is already in the Uspace <" + task.getStderr().getName() + "/" + Integer.toHexString(task.getStdout().getId().getValue()) + ">");
                }
                String file_name = task.getStderrFileName();
                if (file_name == null || file_name.equals("")) {
                    file_name = "stderr_" + Integer.toHexString(task.getId().getValue());
                }
                file_name = file_name.trim().replace(' ', '_');
                fillInfoFile(script, file_name, ipf.getContentsFileName());
                command.append("2> " + file_name + " ");
            }
        }

        /**
		 * Place the String into an info file
		 * (the incarnation)
		 *
		 **/
        protected void fillInfoFile(StringBuffer script, String contents, String info_file) {
            script.append(CAT + ">" + info_file + " << ENDOFINFO\n");
            script.append(contents.trim() + "\n");
            script.append("ENDOFINFO\n");
        }

        public Object clone() throws CloneNotSupportedException {
            Priest dolly = (Priest) super.clone();
            dolly.invocation = (InvocationLineDeacon) invocation.clone();
            return dolly;
        }
    }

    public abstract static class Reader implements MissalReader {

        protected Priest priest;

        protected Missal.Dictionary my_tokens;

        protected Vector contexts;

        protected Missal.Dictionary keywords;

        protected Seminary seminary;

        private Logger logger;

        public Reader(Seminary s) {
            logger = LoggerManager.get("MissalReaders");
            seminary = s;
            my_tokens = new Missal.Dictionary();
            my_tokens.put("INVOCATION", new MissalReader() {

                public void readDefinition(Missal m, String c) {
                    invokeAction(m);
                }
            });
            my_tokens.put("APPLICATION", new MissalReader() {

                public void readDefinition(Missal m, String c) {
                    applicationAction(m);
                }
            });
            my_tokens.put("SOFTWARE_RESOURCE", new MissalReader() {

                public void readDefinition(Missal m, String c) {
                    softwareResourceAction(m);
                }
            });
            my_tokens.put("NAME", new MissalReader() {

                public void readDefinition(Missal m, String c) {
                    nameAction(m);
                }
            });
            my_tokens.put("DEFINITION_FOR", new MissalReader() {

                public void readDefinition(Missal m, String c) {
                    definitionForAction(m);
                }
            });
            my_tokens.put("END", new MissalReader() {

                public void readDefinition(Missal m, String c) {
                    endAction(m);
                }
            });
            contexts = new Vector(11);
            contexts.addElement("STANDARD");
            keywords = new Missal.Dictionary();
        }

        /**
		 * MissalReader promise
		 *
		 **/
        public void readDefinition(Missal missal, String context) {
            missal.changeTokens(my_tokens);
        }

        /**
		 * Got to the end of this Task's definiton
		 *
		 **/
        public void endAction(Missal missal) {
            if (priest instanceof User.Priest || priest instanceof Fortran.Priest || priest instanceof Link.Priest) {
                if (((Task.Priest) priest).getInvocation().contains("MakeReturnCodeDecision")) {
                    logger.config("Got code for placing return codes into decisions from IDB, not using default");
                } else {
                    ((Task.Priest) priest).getInvocation().add(new StringDeacon.Standard(((Task.Priest) priest).getInvocation()), "MakeReturnCodeDecision");
                    ((Task.Priest) priest).getInvocation().add(StringDeacon.makeUser("UC_MARK_DF\nthe_last=$?; echo $the_last > $UC_DECISION_FILE; the_end=1;exit $the_last\n"), "MakeReturnCodeDecision");
                }
            }
            if (tss == null) initialiseTSS();
            for (int i = 0; i < tss.length; i++) {
                Iterator it = seminary.iterator();
                while (it.hasNext()) {
                    if (tss[i] == ((Priest) it.next()).getTargetSystem()) {
                        logger.severe("Duplicate definition of a task in the IDB <" + priest.getName() + "> for TSI <" + tss[i].getName() + ">");
                        (new Exception()).printStackTrace();
                        com.fujitsu.arcon.njs.NJSGlobals.goToLimbo();
                    }
                }
                Priest new_p;
                try {
                    new_p = (Priest) priest.clone();
                } catch (CloneNotSupportedException cnex) {
                    new_p = null;
                    logger.severe("Cannot clone a Priest!! Help.");
                    com.fujitsu.arcon.njs.NJSGlobals.goToLimbo();
                }
                new_p.setTargetSystem(tss[i]);
                new_p.setName(priest.getName() + tss[i].getName());
                seminary.add(new_p);
                Iterator ii = tss[i].getSoftwareResources();
                while (ii.hasNext()) {
                    removeContext(Task.makeSWRName((SoftwareResource) ii.next()));
                }
            }
            tss = null;
            newPriest();
            if (missal != null) missal.forgetTokens();
        }

        /**
		 * Make a new Priest definition, this must be implemented
		 * by each sub-class so that its own type of
		 * Priest is instantiated
		 *
		 **/
        public abstract void newPriest();

        public void nameAction(Missal missal) {
            String name = missal.getWord();
            priest.setName(name);
        }

        private TargetSystem[] tss;

        /**
		 * Process the list of TSI that can execute
		 * the Task when incarnated in the way that
		 * this Priest is initialised to.
		 *
		 * The introducing token must appear before any
		 * use of any context. It should be required, this
		 * is enforced by raising an error on the END action.
		 *
		 * Format:
		 *
		 * DEFINITION_FOR host1 host2 ..... END
		 *
		 *  - special keyword "ALL" defines for all TargetSystems
		 *  - empty list implies default TargetSystem (as does "DEFAULT")
		 *
		 **/
        public void definitionForAction(Missal missal) {
            List ei_names = new ArrayList();
            String next_word = missal.getWord();
            while (!next_word.toUpperCase().equals("END")) {
                if (next_word.toUpperCase().equals("ALL")) {
                    Iterator i = TargetSystem.getTargetSystems();
                    while (i.hasNext()) {
                        String s = ((TargetSystem) i.next()).getName();
                        if (!ei_names.contains(s)) ei_names.add(s);
                    }
                } else {
                    if (!ei_names.contains(next_word)) ei_names.add(next_word);
                }
                next_word = missal.getWord();
            }
            if (ei_names.size() == 0) {
                ei_names.add("DEFAULT");
            }
            tss = new TargetSystem[ei_names.size()];
            for (int i = 0; i < tss.length; i++) {
                TargetSystem e = TargetSystem.getTargetSystem((String) ei_names.get(i));
                if (e == null) {
                    logger.severe("A Task definition is executed by an undefined Target System <" + ei_names.get(i) + ">");
                    com.fujitsu.arcon.njs.NJSGlobals.goToLimbo();
                } else {
                    tss[i] = e;
                }
                Iterator ii = e.getSoftwareResources();
                while (ii.hasNext()) {
                    addContext(Task.makeSWRName((SoftwareResource) ii.next()));
                }
            }
        }

        /**
		 * Process changes for a SoftwareResource (Context), take over from the 
		 * missal for a while
		 *
		 **/
        public void softwareResourceAction(Missal missal) {
            String swr_name = missal.getWord();
            if (!contexts.contains(swr_name)) {
                logger.warning("Unrecognised name in SOFTWARE_RESOURCE <" + swr_name + ">");
            }
            String next_word = missal.getWord();
            while (!next_word.equals("END")) {
                MissalReader mr = (MissalReader) my_tokens.get(next_word);
                if (mr == null) {
                    logger.warning("Unrecognised field name <" + next_word + "> in SOFTWARE_RESOURCE <" + swr_name + ">");
                } else {
                    mr.readDefinition(missal, swr_name);
                }
                next_word = missal.getWord();
            }
        }

        /**
		 * Add a define section to this processing i.e. where a 
		 * field gets localisation (replacing if exists)
		 *
		 **/
        public void putDefineSection(String token, MissalReader reader) {
            my_tokens.put(token, reader);
        }

        /**
		 * Add an allowed Context for this task
		 *
		 **/
        public void addContext(String new_context) {
            if (!contexts.contains(new_context)) {
                contexts.addElement(new_context);
            }
        }

        public void removeContext(String old_context) {
            contexts.remove(old_context);
        }

        /**
		 * Add a keyword to be recognised in the INVOCATION section
		 * together with the Priest to be invoked on incarnation
         * (replacing if already exists)
		 *
		 **/
        public void putKeyWord(String name, FieldDeacon priest) {
            keywords.put(name, priest);
        }

        Stack app_stack = new Stack();

        /**
		 * Read a complete application definition. Create an Application
		 * resource for it and put into resource set (allow version and
		 * read meta data from file and/or verbatim). Put an invocation
		 * line into the main task's for this. Allow recursive definition
		 * of parts.
		 *
		 **/
        public void applicationAction(Missal missal) {
            StringTokenizer st = new StringTokenizer(missal.getToLineEnd());
            String name = (st.hasMoreTokens() ? st.nextToken() : null);
            String version = (st.hasMoreTokens() ? st.nextToken() : null);
            if (name == null) {
                logger.severe("APPLICATION keyword (within RUN) requires a name.");
                NJSGlobals.goToLimbo();
            } else {
                try {
                    Application a = (Application) app_stack.peek();
                    name = a.getName() + ":" + name;
                } catch (EmptyStackException ibex) {
                }
            }
            app_stack.push(new Application((String) null, name, version));
            if (tss == null) initialiseTSS();
            for (int i = 0; i < tss.length; i++) {
                tss[i].addSoftwareResource((Application) app_stack.peek());
            }
            addContext(Task.makeSWRName((Application) app_stack.peek()));
            Missal.Dictionary my_tokens = new Missal.Dictionary();
            my_tokens.put("FILE", new MissalReader() {

                public void readDefinition(Missal m, String c) {
                    appFileAction(m);
                }
            });
            my_tokens.put("DATA", new MissalReader() {

                public void readDefinition(Missal m, String c) {
                    appDataAction(m);
                }
            });
            my_tokens.put("DESCRIPTION", new MissalReader() {

                public void readDefinition(Missal m, String c) {
                    appDescriptionAction(m);
                }
            });
            my_tokens.put("INVOCATION", new MissalReader() {

                public void readDefinition(Missal m, String c) {
                    appInvokeAction(m);
                }
            });
            my_tokens.put("APPLICATION", new MissalReader() {

                public void readDefinition(Missal m, String c) {
                    applicationAction(m);
                }
            });
            my_tokens.put("END", new MissalReader() {

                public void readDefinition(Missal m, String c) {
                    appEndAction(m);
                }
            });
            missal.changeTokens(my_tokens);
        }

        /**
		 * Process missal having read INVOCATION within an APPLICATION. Call standard
		 * INVOCATION processing for the outer task so that this goes into the invocation
		 * stream but we need to set the context from the app or part being defined.
		 *
		 * INTERACTIVE is allowed.
		 *
		 **/
        public void appInvokeAction(Missal missal) {
            String context_name = (Task.makeSWRName((Application) app_stack.peek()));
            invokeAction(context_name, missal);
        }

        public void appEndAction(Missal missal) {
            try {
                app_stack.pop();
                missal.forgetTokens();
            } catch (EmptyStackException ibex) {
                logger.severe("APPLICATION section (within RUN) ended with nothing?");
                NJSGlobals.goToLimbo();
            }
        }

        public void appDataAction(Missal missal) {
            Application a = (Application) app_stack.peek();
            if (a.getMetaData() == null) {
                a.setMetaData(missal.getVerbatim());
            } else {
                a.setMetaData(a.getMetaData() + missal.getVerbatim());
            }
        }

        public void appFileAction(Missal missal) {
            Application a = (Application) app_stack.peek();
            String od;
            if (a.getMetaData() == null) {
                od = "";
            } else {
                od = a.getMetaData();
            }
            try {
                File md_file = NJSGlobals.getFullPathFile(missal.getWord());
                logger.config("Reading APPLICATION (in RUN) meta-data for <" + a.getName() + "> from <" + md_file + ">");
                if (!md_file.canRead()) {
                    logger.severe("APPLICATION keyword (in RUN), cannot find or read meta data file: " + md_file);
                    com.fujitsu.arcon.njs.NJSGlobals.goToLimbo();
                }
                byte[] metadata = new byte[(int) md_file.length()];
                (new FileInputStream(md_file)).read(metadata);
                ((Application) app_stack.peek()).setMetaData(od + new String(metadata));
            } catch (Exception ex) {
                logger.severe("APPLICATION keyword (in RUN), problems reading meta-data file: " + ex);
                com.fujitsu.arcon.njs.NJSGlobals.goToLimbo();
            }
        }

        public void appDescriptionAction(Missal missal) {
            ((Application) app_stack.peek()).setDescription(missal.getVerbatim());
        }

        private void initialiseTSS() {
            tss = new TargetSystem[] { TargetSystem.getTargetSystem() };
            if (tss[0] == null) {
                logger.severe("Must define the EXECUTION_TSI before defining actions");
                com.fujitsu.arcon.njs.NJSGlobals.goToLimbo();
            }
            ;
            Iterator ii = tss[0].getSoftwareResources();
            while (ii.hasNext()) {
                addContext(Task.makeSWRName((SoftwareResource) ii.next()));
            }
        }

        public void invokeAction(String context, Missal missal) {
            if (tss == null) initialiseTSS();
            String[] str = missal.getVerbatimPlus();
            String missal_invocation = str[0];
            String preceding = str[1];
            if (preceding.length() > 0) {
                StringTokenizer st = new StringTokenizer(preceding);
                if (st.hasMoreTokens()) {
                    if (st.nextToken().toLowerCase().startsWith("i")) {
                        ((Task.Priest) priest).getInvocation().add(StringDeacon.makeUser("#TSI_PREFER_INTERACTIVE true\n"), context);
                        for (int i = 0; i < tss.length; i++) {
                            Iterator ii = tss[i].getSoftwareResources();
                            while (ii.hasNext()) {
                                SoftwareResource sr = (SoftwareResource) ii.next();
                                String cname = Task.makeSWRName(sr);
                                if (cname.equals(context)) {
                                    sr.setDescription("Indicates interactive execution");
                                }
                            }
                        }
                    }
                }
            }
            processInvocation(missal_invocation, context);
        }

        public void invokeAction(Missal missal) {
            if (tss == null) initialiseTSS();
            String[] str = missal.getVerbatimPlus();
            String missal_invocation = str[0];
            String preceding = str[1];
            String context = "STANDARD";
            if (preceding.length() > 0) {
                StringTokenizer st = new StringTokenizer(preceding);
                if (st.hasMoreTokens()) {
                    context = st.nextToken();
                    if (!contexts.contains(context) && !context.equalsIgnoreCase("END_PROCESS")) {
                        logger.severe("Unknown software resource for an INVOCATION <" + context + ">");
                        com.fujitsu.arcon.njs.NJSGlobals.goToLimbo();
                    }
                    for (int i = 0; i < tss.length; i++) {
                        java.util.Enumeration e = ScriptType.elements();
                        while (e.hasMoreElements()) {
                            String s = e.nextElement().toString().replace(' ', '_').toUpperCase();
                            if (context.equalsIgnoreCase(s)) tss[i].addSoftwareResource(new Context("Standard script supported by NJS", s, null));
                        }
                        if (st.hasMoreTokens()) {
                            if (st.nextToken().toLowerCase().startsWith("i")) {
                                ((Task.Priest) priest).getInvocation().add(StringDeacon.makeUser("#TSI_PREFER_INTERACTIVE true\n"), context);
                                Iterator ii = tss[i].getSoftwareResources();
                                while (ii.hasNext()) {
                                    SoftwareResource sr = (SoftwareResource) ii.next();
                                    String cname = Task.makeSWRName(sr);
                                    if (cname.equals(context)) {
                                        sr.setDescription("Indicates interactive execution");
                                    }
                                }
                            }
                        }
                    }
                }
            }
            processInvocation(missal_invocation, context);
        }

        private void processInvocation(String missal_invocation, String context) {
            int new_line = missal_invocation.indexOf("-\n");
            while (new_line != -1) {
                missal_invocation = missal_invocation.substring(0, new_line) + " " + missal_invocation.substring(new_line + 2);
                new_line = missal_invocation.indexOf("-\n");
            }
            String verbatim = "";
            while (missal_invocation != "") {
                FieldDeacon fp = null;
                String token = null;
                int i = missal_invocation.indexOf('<');
                if (i != -1) {
                    verbatim += missal_invocation.substring(0, i);
                    missal_invocation = missal_invocation.substring(i + 1);
                    i = missal_invocation.indexOf('>');
                    if (i != -1) {
                        token = missal_invocation.substring(0, i);
                        if (i < missal_invocation.length() - 1) {
                            missal_invocation = missal_invocation.substring(i + 1);
                        } else {
                            missal_invocation = "";
                        }
                    } else {
                        verbatim += "<" + missal_invocation;
                        missal_invocation = "";
                    }
                } else {
                    verbatim += missal_invocation;
                    missal_invocation = "";
                }
                if (token == null) {
                    ((Task.Priest) priest).getInvocation().add(StringDeacon.makeUser(verbatim), context);
                } else {
                    fp = makeFromKeyWord(token);
                    if (fp == null) {
                        verbatim += "<";
                        missal_invocation = token + ">" + missal_invocation;
                    } else {
                        ((Task.Priest) priest).getInvocation().add(StringDeacon.makeUser(verbatim), context);
                        verbatim = "";
                        ((Task.Priest) priest).getInvocation().add(fp, context);
                    }
                }
            }
        }

        private FieldDeacon makeFromKeyWord(String token) {
            boolean do_escape = false;
            if (token.charAt(token.length() - 1) == '%') {
                if (token.length() > 1) {
                    token = token.substring(0, token.length() - 1);
                    do_escape = true;
                } else {
                    logger.severe("Malformed invocation section token <" + token + ">");
                    com.fujitsu.arcon.njs.NJSGlobals.goToLimbo();
                }
            }
            FieldDeacon fp = (FieldDeacon) keywords.get(token);
            if (token.startsWith("STANDARD")) {
                int index = token.indexOf('/');
                if (index == -1) index = token.indexOf('+');
                String action = "";
                if (index != -1) {
                    action = token.substring(index);
                }
                StringDeacon.Standard sps = new StringDeacon.Standard(((Task.Priest) priest).getInvocation());
                if (action.startsWith("/")) {
                    if (action.endsWith("/")) {
                        try {
                            StringTokenizer sst = new StringTokenizer(action, "/");
                            String original = sst.nextToken();
                            String replace = sst.nextToken();
                            if (sst.hasMoreTokens()) throw new Exception();
                            sps.setProcessing(new PPer.sub(original, replace));
                        } catch (Exception ex) {
                            logger.severe("Badly formed substitution for STANDARD keyword<" + token + ">");
                            com.fujitsu.arcon.njs.NJSGlobals.goToLimbo();
                        }
                    } else {
                        logger.severe("Badly formed substitution for STANDARD keyword <" + token + ">");
                        com.fujitsu.arcon.njs.NJSGlobals.goToLimbo();
                    }
                } else if (action.startsWith("+")) {
                    sps.setProcessing(new PPer.add(action.substring(1)));
                }
                fp = sps;
            }
            if (fp != null && do_escape) {
                if (fp instanceof StringDeacon.C) {
                    ((StringDeacon.C) fp).setProcessing(new PPer.Escaper());
                } else {
                    logger.severe("Cannot apply shell escaping to this invocation keyword <" + token + ">");
                    com.fujitsu.arcon.njs.NJSGlobals.goToLimbo();
                }
            }
            return fp;
        }
    }

    /**
	 * Return a string that names the Software resource. This
	 * name is the one that is used in the IDB to refer to the
	 * SoftwareResource.
	 *
	 **/
    public static final String makeSWRName(SoftwareResource s) {
        if (s.getVersion() == null || s.getVersion().equals("")) {
            return s.getName();
        } else {
            return s.getName() + "-" + s.getVersion();
        }
    }
}
