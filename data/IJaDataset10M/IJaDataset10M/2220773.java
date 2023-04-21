package com.fujitsu.arcon.njs.priest;

import java.util.Vector;
import org.unicore.ajo.ExportTask;
import org.unicore.ajo.ImportTask;
import com.fujitsu.arcon.njs.actions.EKnownAction;
import com.fujitsu.arcon.njs.actions.IncarnatedPortfolio;

/**
 * A generic file copying Priest, used for Import, Export and CopyFile
 * also referred to by other "built-in" commands for the incarnation of
 * the file copying part
 *
 * @author Sven van den Berghe, fujitsu 
 *
 * @version $Revision: 1.2 $ $Date: 2004/06/30 13:45:30 $
 *
 **/
public class FileCopy {

    public static class Seminary extends Task.Seminary {

        public Seminary() {
            super();
            reader = new Reader(this);
        }
    }

    public static class Priest extends Task.Priest {

        StringDeacon.C source_deacon;

        StringDeacon.C destination_deacon;

        public Priest(String name) {
            super(name);
            source_deacon = new StringDeacon.Append();
            destination_deacon = new StringDeacon.Append();
            parser = new OutcomeParser.Export();
        }

        public boolean isBatchTargetSystemOnly() {
            return false;
        }

        public void incarnate(EKnownAction eka) {
            if (eka.getAction() instanceof ImportTask) {
                incarnateImportTask(eka);
            } else if (eka.getAction() instanceof ExportTask) {
                incarnateExportTask(eka);
            } else if (eka.getAction() instanceof org.unicore.ajo.CopyFile) {
                incarnateCopyFile(eka);
            }
        }

        public void incarnateImportTask(EKnownAction eka) {
            org.unicore.ajo.ImportTask import_task = (org.unicore.ajo.ImportTask) eka.getAction();
            int len = import_task.getFiles().length;
            if (len == 0) {
                eka.failedIncarnation("No files to import");
                return;
            }
            for (int i = 0; i < len; i++) {
                String s = import_task.getFiles()[i];
                if (s == null || s.equals("")) {
                    eka.failedIncarnation("File names must have a value at <" + i + ">");
                    return;
                }
            }
            Vector contexts = makeContextSet(import_task);
            contexts.addElement("MODIFY");
            IncarnatedPortfolio destinationp;
            try {
                destinationp = eka.getRequestedUspace().createNew(import_task.getPortfolio().getId());
            } catch (com.fujitsu.arcon.njs.interfaces.NJSException ipex) {
                eka.failedIncarnation("Imported portfolio: " + ipex.getMessage());
                return;
            }
            StringBuffer commands = new StringBuffer("# Incarnation of ImportTask\n");
            String d_dir = destinationp.getHiddenDirName();
            commands.append(Task.Priest.MKDIR + d_dir + "\n" + GeneralData.Priest.afterCommand("Hidden dir make failed"));
            String[] files_to_get = import_task.getFiles();
            for (int i = 0; i < files_to_get.length; i++) {
                String source_files = target_system.incarnateTargetFile(files_to_get[i], eka.getEffectiveStorage());
                eka.setExtraListing("IMPORTED FILE:   " + source_files + "\n");
                commands.append(createFileCopyCommands(source_files, d_dir, contexts));
            }
            if (!import_task.willOverwrite()) commands.append(Task.Priest.TOUCH + d_dir + ".UCNOOVERWRITE\n");
            commands.append(getRevealCommands(destinationp));
            eka.setIncarnation(commands.toString());
            target_system.consign(eka);
        }

        public synchronized void incarnateExportTask(EKnownAction eka) {
            org.unicore.ajo.ExportTask export_task = (org.unicore.ajo.ExportTask) eka.getAction();
            Vector contexts = makeContextSet(export_task);
            if (export_task.willOverwrite()) {
                contexts.addElement("OVERWRITE");
            } else {
                contexts.addElement("NO_OVERWRITE");
            }
            StringBuffer commands = new StringBuffer("# Incarnation of ExportTask\n");
            IncarnatedPortfolio source_files;
            try {
                source_files = eka.getRequestedUspace().createExisting(export_task.getPortfolio().getId());
            } catch (com.fujitsu.arcon.njs.interfaces.NJSException nex) {
                eka.failedIncarnation("Looking for files to export: " + nex.getMessage());
                return;
            }
            String xspace_dir = target_system.incarnateTargetFile("", eka.getEffectiveStorage());
            eka.setExtraListing("DEST DIR:        " + xspace_dir + "\n");
            commands.append(createFileCopyCommands(Task.Priest.getPFContents(source_files.getContentsFileName()), xspace_dir, contexts));
            commands.append("echo \"XSPACE_DIRECTORY " + xspace_dir + "\" \n");
            commands.append(Task.Priest.CAT + source_files.getContentsFileName() + "\n");
            commands.append("echo \"EENNDD_EXPORTED-FILE_LISTING\" \n");
            eka.setIncarnation(commands.toString());
            target_system.consign(eka);
        }

        public void incarnateCopyFile(EKnownAction eka) {
            org.unicore.ajo.CopyFile copyfile_task = (org.unicore.ajo.CopyFile) eka.getAction();
            if (copyfile_task.getFrom() == null || copyfile_task.getFrom().equals("")) {
                eka.failedIncarnation("No source file");
                return;
            }
            String destination_name = copyfile_task.getTo();
            if (destination_name == null) destination_name = "";
            String source;
            String destination;
            if (copyfile_task.copyFromUspace()) {
                source = target_system.incarnateTargetFile(copyfile_task.getFrom(), eka.getRequestedUspace());
                destination = target_system.incarnateTargetFile(destination_name, eka.getEffectiveStorage());
            } else {
                source = target_system.incarnateTargetFile(copyfile_task.getFrom(), eka.getEffectiveStorage());
                destination = target_system.incarnateTargetFile(destination_name, eka.getRequestedUspace());
            }
            eka.setExtraListing("SOURCE FILE:     " + source + "\n" + "DESTINATION:     " + destination + "\n");
            Vector contexts = makeContextSet(copyfile_task);
            if (copyfile_task.willOverwrite()) {
                contexts.add("OVERWRITE");
            } else {
                contexts.add("NO_OVERWRITE");
            }
            eka.setIncarnation("# Incarnation of CopyFile\n" + createFileCopyCommands(source, destination, contexts));
            target_system.consign(eka);
        }

        /**
		 * Incarnate the file copy commands for this Priest.
		 * <p>
		 * cds to source directory, copies files, cd to Upsace WD (?)
		 *
		 **/
        private String createFileCopyCommands(String source_spec, String destination_file, Vector contexts) {
            String result = "";
            synchronized (this) {
                source_deacon.setString(source_spec);
                destination_deacon.setString(destination_file);
                result += invocation.incarnate("", contexts);
                source_deacon.setString("");
                destination_deacon.setString("");
            }
            result += GeneralData.Priest.afterCommand("File copy failed");
            return result;
        }

        /**
		 * Returns a script to reveal the given Portfolio, assuming that it has
		 * been hidden in the standard directory. Reveal is to the containing
		 * script's cwd.
		 *
		 **/
        public static String getRevealCommands(IncarnatedPortfolio ipf) {
            String source_dir = ipf.getHiddenDirName();
            String info_name = ipf.getContentsFileName();
            String destination = "\n#Revealing a Portfolio (making visible in Uspace)\n";
            destination += "owd=`pwd`; export owd\n";
            destination += "cd " + source_dir + "\n";
            destination += "if [ -f .UCNOOVERWRITE ]; then\n" + "  rm .UCNOOVERWRITE\n" + "  for bar in `" + Task.Priest.LSA + "`; do\n" + "    if [ -d $owd/$bar ]; then\n" + "       for foo in `" + Task.Priest.FIND + "$bar`; do\n" + "       if [ -f $owd/$bar/$foo ]; then \n" + "         " + Task.Priest.PRINTF + "\"File exists $owd/$bar/$foo (no overwrite allowed)\\n\" 1>&2\n" + "         exit 1\n" + "       fi\n" + "      done\n" + "    elif [ -f $owd/$bar ]; then\n" + "      " + Task.Priest.PRINTF + "\"File exists $owd/$bar (no overwrite allowed)\\n\" 1>&2\n" + "      exit 1\n" + "    fi\n" + "  done\n" + "fi\n";
            String info_name_temp = "$owd/" + info_name + "-temp";
            destination += "for foo in `" + Task.Priest.LSA + "`\n" + "do\n" + "  if [ -d $owd/$foo ]; then \n" + "     " + Task.Priest.CP + " -f $foo $owd/$foo\n" + "     echo $foo/$foo >> " + info_name_temp + "\n" + "  elif [ -f $owd/$foo ]; then \n" + "    if [ -d $foo ]; then\n" + "      " + Task.Priest.PRINTF + "\"Reveal failed, $foo is a directory but  $owd/$foo is a file\\n\" 1>&2\n" + "      exit 1\n" + "    else\n" + "      " + Task.Priest.MV + " -f $foo $owd/$foo\n" + "      echo $foo >> " + info_name_temp + "\n" + "    fi\n" + "  else\n" + "     " + Task.Priest.MV + " -f $foo $owd/$foo\n" + "      echo $foo >> " + info_name_temp + "\n" + "  fi\n" + "done\n";
            destination += "cd $owd\n";
            destination += Task.Priest.CAT + info_name_temp + "| (" + Task.Priest.TR + "\"\\012\" \" \" ; echo \"\" ) | " + Task.Priest.doSed(" $", "") + " > " + info_name + "\n";
            destination += Task.Priest.RM + "-rf " + source_dir + "\n";
            destination += Task.Priest.RM + "-rf " + info_name_temp + "\n";
            return destination;
        }

        public Object clone() throws CloneNotSupportedException {
            Priest result = (Priest) super.clone();
            result.source_deacon = (StringDeacon.C) source_deacon.clone();
            result.invocation.substitute(source_deacon, result.source_deacon);
            result.destination_deacon = (StringDeacon.C) destination_deacon.clone();
            result.invocation.substitute(destination_deacon, result.destination_deacon);
            result.invocation.resetDeaconReferences();
            return result;
        }
    }

    public static class Reader extends Task.Reader {

        public Reader(Seminary s) {
            super(s);
            addContext("OVERWRITE");
            addContext("NO_OVERWRITE");
            newPriest();
        }

        public void newPriest() {
            priest = new Priest("FileCopy-");
            putKeyWord("SOURCE", ((FileCopy.Priest) priest).source_deacon);
            putKeyWord("DESTINATION", ((FileCopy.Priest) priest).destination_deacon);
        }
    }
}
