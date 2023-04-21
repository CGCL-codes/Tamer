package com.fujitsu.arcon.njs.priest;

import java.util.Vector;
import org.unicore.ajo.LinkMapLevel;
import org.unicore.ajo.LinkTask;
import org.unicore.sets.PortfolioEnumeration;
import com.fujitsu.arcon.njs.actions.EKnownAction;
import com.fujitsu.arcon.njs.actions.IncarnatedPortfolio;

/**
 * Incarnate and execute Link Tasks
 *
 * @author Sven van den Berghe, fujitsu
 *
 * @version $Revision: 1.2 $ $Date: 2004/06/30 13:45:30 $
 *
 **/
public class Link {

    public static class Seminary extends Task.Seminary {

        public Seminary() {
            super();
            reader = new Reader(this);
        }
    }

    public static class Priest extends Task.Priest {

        private static String[] name_tokens = { "OBJECT_FILE", "LIBRARY", "MAP_FILE" };

        private static int MAP = 2;

        private static String[] oo_tokens = { "ON", "OFF" };

        private static int ON = 0;

        private static int OFF = 1;

        MultiField.Deacon naming;

        MultiField.Deacon version;

        MultiField.Deacon verbose;

        MultiField.Deacon map_level;

        MultiField.Deacon debug;

        MultiField.Deacon profile;

        StringDeacon.C exec_name;

        StringDeacon.C object_names;

        StringDeacon.C library_names;

        public Priest(String name) {
            super(name);
            parser = new OutcomeParser.Execute();
            naming = new MultiField.Deacon(name_tokens, "Link naming");
            version = new MultiField.Deacon(oo_tokens, "Link version");
            verbose = new MultiField.Deacon(oo_tokens, "Link verbose");
            map_level = new MultiField.Deacon(new String[] { "NONE", "ON", "FULL" }, "Link map_level");
            debug = new MultiField.Deacon(oo_tokens, "Link debug");
            profile = new MultiField.Deacon(oo_tokens, "Link profile");
            exec_name = new StringDeacon.Process();
            object_names = new StringDeacon.HostList();
            library_names = new StringDeacon.HostList();
        }

        public boolean isBatchTargetSystemOnly() {
            return true;
        }

        /**
		 * Incarnate the Given Task
		 *
		 **/
        public void incarnate(EKnownAction eka) {
            LinkTask task = (LinkTask) eka.getAction();
            String checked = getExternalChecker().check(task);
            if (checked != null) {
                eka.failedIncarnation("Task not acceptable: " + checked);
                return;
            }
            StringBuffer script = new StringBuffer();
            Vector contexts = makeContextSet(task);
            setEnvVars(script, eka);
            IncarnatedPortfolio executablep;
            try {
                executablep = eka.getRequestedUspace().createNew(task.getExecutable().getId());
            } catch (com.fujitsu.arcon.njs.interfaces.NJSException ipex) {
                eka.failedIncarnation("Executable portfolio; " + ipex.getMessage());
                return;
            }
            String exec_file_name = task.getExecutable().getName();
            if (exec_file_name == null || exec_file_name.equals("")) exec_file_name = "exec";
            fillInfoFile(script, exec_file_name, executablep.getContentsFileName());
            StringBuffer command = new StringBuffer();
            try {
                synchronized (this) {
                    exec_name.setProcessing(new PPer.add(exec_file_name));
                    if (task.getObjects() == null) {
                        eka.failedIncarnation("No object files list");
                        return;
                    }
                    PortfolioEnumeration pe = task.getObjects().elements();
                    if (!pe.hasMoreElements()) {
                        eka.failedIncarnation("No object files in list");
                        return;
                    }
                    String object_names_string = "";
                    while (pe.hasMoreElements()) {
                        try {
                            object_names_string += eka.getRequestedUspace().createExisting(pe.nextElement().getId()).getContentsFileName() + " ";
                        } catch (com.fujitsu.arcon.njs.interfaces.NJSException ipex) {
                            eka.failedIncarnation("Objects portfolio; " + ipex.getMessage());
                            return;
                        }
                    }
                    object_names.setString(object_names_string.trim());
                    String library_names_string = "";
                    if (task.getLibraries() != null && task.getLibraries().size() > 0) {
                        pe = task.getLibraries().elements();
                        while (pe.hasMoreElements()) {
                            try {
                                library_names_string += eka.getRequestedUspace().createExisting(pe.nextElement().getId()).getContentsFileName() + " ";
                            } catch (com.fujitsu.arcon.njs.interfaces.NJSException ipex) {
                                eka.failedIncarnation("Libraries portfolio; " + ipex.getMessage());
                                return;
                            }
                        }
                    } else {
                        library_names_string = "EMPTY";
                    }
                    library_names.setString(library_names_string.trim());
                    map_level.setIndex(task.getMapLevel());
                    if (!task.getMapLevel().equals(LinkMapLevel.NONE)) {
                        IncarnatedPortfolio mapp;
                        try {
                            mapp = eka.getRequestedUspace().createNew(task.getMap().getId());
                        } catch (com.fujitsu.arcon.njs.interfaces.NJSException ipex) {
                            eka.failedIncarnation("Map portfolio; " + ipex.getMessage());
                            return;
                        }
                        naming.setIndex(MAP);
                        String map_name = naming.incarnate("", "", null, null);
                        if (map_name.startsWith(".")) {
                            fillInfoFile(script, exec_name.getString() + map_name, mapp.getContentsFileName());
                        } else {
                            script.append("echo " + map_name + " > " + mapp.getContentsFileName() + " \n");
                        }
                    }
                    version.setIndex((task.isVersionOn() ? ON : OFF));
                    verbose.setIndex((task.isVerboseOn() ? ON : OFF));
                    debug.setIndex((task.linkForDebug() ? ON : OFF));
                    profile.setIndex((task.linkForProfile() ? ON : OFF));
                    command.append(invocation.incarnate("", contexts));
                }
            } finally {
                exec_name.setString("");
                object_names.setString("");
                library_names.setString("");
            }
            StringBuffer end_stuff = new StringBuffer();
            if (task.getCommandLine() != null) end_stuff.append(task.getCommandLine() + " ");
            try {
                doStdin(end_stuff, task, eka.getRequestedUspace());
                doStderr(end_stuff, script, task, eka.getRequestedUspace());
                doStdout(end_stuff, script, task, eka.getRequestedUspace());
            } catch (com.fujitsu.arcon.njs.interfaces.NJSException nex) {
                eka.failedIncarnation(nex);
                return;
            }
            int marker_loc = command.indexOf("UC_MARK_DF");
            if (marker_loc > -1) {
                command.replace(marker_loc, marker_loc + "UC_MARK_DF".length(), end_stuff.toString());
            } else {
                command.append(end_stuff.toString());
            }
            script.append(command.toString());
            String incarnation = script.toString();
            eka.setIncarnation(incarnation);
            target_system.consign(eka);
        }

        public Object clone() throws CloneNotSupportedException {
            Priest result = (Priest) super.clone();
            result.exec_name = (StringDeacon.Process) exec_name.clone();
            result.invocation.substitute(exec_name, result.exec_name);
            result.object_names = (StringDeacon.HostList) object_names.clone();
            result.invocation.substitute(object_names, result.object_names);
            result.library_names = (StringDeacon.HostList) library_names.clone();
            result.invocation.substitute(library_names, result.library_names);
            result.version = (MultiField.Deacon) version.clone();
            result.invocation.substitute(version, result.version);
            result.verbose = (MultiField.Deacon) verbose.clone();
            result.invocation.substitute(verbose, result.verbose);
            result.debug = (MultiField.Deacon) debug.clone();
            result.invocation.substitute(debug, result.debug);
            result.profile = (MultiField.Deacon) profile.clone();
            result.invocation.substitute(profile, result.profile);
            result.naming = (MultiField.Deacon) naming.clone();
            result.invocation.substitute(naming, result.naming);
            result.map_level = (MultiField.Deacon) map_level.clone();
            result.invocation.substitute(map_level, result.map_level);
            result.invocation.resetDeaconReferences();
            return result;
        }
    }

    public static class Reader extends Task.Reader {

        public Reader(Seminary s) {
            super(s);
            newPriest();
            addContext("MakeReturnCodeDecision");
        }

        public void newPriest() {
            priest = new Priest("Link-");
            putKeyWord("EXECUTABLE", ((Link.Priest) priest).exec_name);
            putKeyWord("OBJECTS", ((Link.Priest) priest).object_names);
            putKeyWord("LIBRARIES", ((Link.Priest) priest).library_names);
            putKeyWord("VERSION", ((Link.Priest) priest).version);
            putKeyWord("VERBOSE", ((Link.Priest) priest).verbose);
            putKeyWord("MAPLEVEL", ((Link.Priest) priest).map_level);
            putKeyWord("DEBUG", ((Link.Priest) priest).debug);
            putKeyWord("PROFILE", ((Link.Priest) priest).profile);
            putDefineSection("NAMING", ((Link.Priest) priest).naming.getReader());
            putDefineSection("MAPLEVEL", ((Link.Priest) priest).map_level.getReader());
            putDefineSection("VERSION", ((Link.Priest) priest).version.getReader());
            putDefineSection("VERBOSE", ((Link.Priest) priest).verbose.getReader());
            putDefineSection("DEBUG", ((Link.Priest) priest).debug.getReader());
            putDefineSection("PROFILE", ((Link.Priest) priest).profile.getReader());
        }
    }
}
