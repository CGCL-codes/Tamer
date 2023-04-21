package org.expasy.jpl.tools.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.collections15.Transformer;
import org.apache.commons.io.FileUtils;
import org.expasy.jpl.commons.app.InteractiveInputScanner;
import org.expasy.jpl.commons.app.InteractiveInputScannerImpl;
import org.expasy.jpl.commons.app.JPLTerminalApplication;
import org.expasy.jpl.commons.base.io.FileFinder;
import org.expasy.jpl.commons.base.io.JPLFile;
import org.expasy.jpl.commons.base.string.StringUtils;
import org.expasy.jpl.commons.base.task.TerminalProgressBar;

/**
 * <h3>Use Cases</h3>
 * 
 * <pre>
 *  // backup then edit all java source files from java/src (backup files tracked in javasrc-path.back)
 *  java -jar WordRenamer.jar -rd java/src --backup javasrc-path.back
 *  
 *  // revert to the previous state
 *  java -jar WordRenamer.jar --revert backup backup_path.txt
 * 
 *  // backup then edit all xml source files from xdoc/
 *  java -jar WordRenamer.jar -rd JPLForrest/src/content/xdoc -f *.xml --backup backup_path.txt
 *  
 *  // finishing by removing all backup files
 *  java -jar WordRenamer.jar --clean backup_path.txt
 * 
 * <h3>Propositions</h3>
 * Propose other func:
 * --log where all actions will be logged
 * --interactive need confirmation before applying previous actions
 * the app prompts the user for whether to proceed with the entire operation.  
 * If the response is not affirmative, the entire command is aborted.
 * </pre>
 * 
 * @author nikitin
 * 
 * @version 1.0
 * 
 */
public class JPLRefUpdater implements JPLTerminalApplication {

    private static final Pattern IMPORT_PATTERN = Pattern.compile("^import\\s+([^;]+);$");

    private static final Pattern JPL_PATTERN = Pattern.compile("(JPL[^\\s.{}()<>,&]+)");

    private static final Transformer<List<String>, List<String>> FILTER = new Transformer<List<String>, List<String>>() {

        @Override
        public List<String> transform(List<String> files) {
            List<String> l = new ArrayList<String>();
            for (String file : files) {
                JPLFile jplFile = new JPLFile(file);
                if (jplFile.getExtension().equals(".backup")) {
                    l.add(file);
                }
            }
            return l;
        }
    };

    private JPLUpdaterParameters params;

    private List<File> targetFiles;

    /** old simple class name -> old package name */
    private Map<String, String> oldSimpleClass2PackageNames;

    /** old simple class name -> class renaming/package-moving */
    private Map<String, String> oldSimpleClass2RelativeRenaming;

    /** renaming of class names */
    private Map<String, String> old2NewSimpleClassNames;

    /** renaming of imports */
    private Map<String, String> old2NewAbsoluteImportNames;

    /** progress bar */
    private TerminalProgressBar pb;

    public JPLRefUpdater(JPLUpdaterParameters params) {
        this.params = params;
        oldSimpleClass2PackageNames = new HashMap<String, String>();
        oldSimpleClass2RelativeRenaming = new HashMap<String, String>();
        old2NewAbsoluteImportNames = new HashMap<String, String>();
        old2NewSimpleClassNames = new HashMap<String, String>();
    }

    public static void main(String[] args) {
        JPLRefUpdater app = new JPLRefUpdater(new JPLUpdaterParameters(JPLRefUpdater.class, args));
        app.params.setHeader(app.getClass().getSimpleName() + " v" + app.params.getVersion() + " developed by Fred Nikitin.\n" + "Copyright (c) 2012 Proteome Informatics Group in Swiss " + "Institute of Bioinformatics.\n");
        try {
            if (app.params.isBackupSelectedFiles()) {
                if (app.params.isClearBackupFiles()) {
                    System.err.println("disabling backup and clear");
                    app.params.setBackupSelectedFiles(false);
                    app.params.setClearBackupFiles(false);
                }
            }
            if (app.params.isVerbose()) {
                System.out.println(app.params.getParamInfos());
            }
            app.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<File> selectFiles() throws IOException {
        List<File> list = new ArrayList<File>();
        for (String dir : params.getDirectories()) {
            list.addAll(FileFinder.getFiles(dir, params.getFilenamePattern(), params.isRecursiveSearch()));
        }
        return list;
    }

    /**
	 * Delete all backuped files.
	 * 
	 * @throws IOException
	 */
    private void clear() throws IOException {
        List<String> backupPaths = FileUtils.readLines(params.getTracedFile());
        System.out.println("clearing all temporary files...");
        pb = TerminalProgressBar.newInstance(0, backupPaths.size());
        int count = 0;
        for (String backupPath : backupPaths) {
            FileUtils.deleteQuietly(new File(backupPath));
            pb.setValue(++count);
        }
    }

    /**
	 * Revert from "file.back" to "file"
	 * 
	 * @throws IOException
	 */
    private void revert() throws IOException {
        List<String> filePaths = FileUtils.readLines(params.getTracedFile());
        filePaths = FILTER.transform(filePaths);
        System.out.println("reverting original files...");
        pb = TerminalProgressBar.newInstance(0, filePaths.size());
        int count = 0;
        for (String filePath : filePaths) {
            File backupFile = new File(filePath);
            if (backupFile.exists()) {
                FileUtils.copyFile(backupFile, revertFromBackupFile(backupFile));
                FileUtils.deleteQuietly(backupFile);
            } else {
                System.err.println(filePath + ": file not found");
            }
            pb.setValue(++count);
        }
    }

    private File createBackupFile(File file) {
        return createFile(file, "backup");
    }

    private File createTempFile(File file) {
        return createFile(file, "tmp");
    }

    private File createFile(File file, String ext) {
        String filename = file.getAbsolutePath();
        String basename = JPLFile.getBaseName(filename);
        String dirname = JPLFile.getDirName(filename);
        String newBasename = basename + "." + ext;
        return new File(dirname + File.separatorChar + newBasename);
    }

    private File revertFromBackupFile(File file) {
        String filename = file.getAbsolutePath();
        String basename = JPLFile.getBaseName(filename);
        String dirname = JPLFile.getDirName(filename);
        int li = basename.lastIndexOf('.');
        String newBasename = basename.substring(0, li);
        return new File(dirname + File.separatorChar + newBasename);
    }

    /**
	 * Copy the modified version of the given file as temporary file.
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
    private File copyAndModifyFile(File file) throws IOException {
        String line;
        boolean hasFileBeenEdited = false;
        InputStreamReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        File tmpFile = createTempFile(file);
        PrintWriter pw = new PrintWriter(tmpFile);
        int lineNum = 0;
        while ((line = br.readLine()) != null) {
            lineNum++;
            if (line.matches("^\\s*$")) {
                pw.append(line);
                pw.append("\n");
                continue;
            }
            Matcher matcher = IMPORT_PATTERN.matcher(line);
            if (matcher.find()) {
                String importName = matcher.group(1);
                if (old2NewAbsoluteImportNames.containsKey(importName)) {
                    pw.append("import ");
                    pw.append(old2NewAbsoluteImportNames.get(importName));
                    pw.append(";");
                    hasFileBeenEdited = true;
                } else {
                    pw.append(line);
                }
                pw.append("\n");
                continue;
            }
            matcher = JPL_PATTERN.matcher(line);
            StringBuilder sb = new StringBuilder();
            int preMatchIdx = 0;
            int postMatchIdx = 0;
            while (matcher.find()) {
                String jplClassName = matcher.group(1);
                preMatchIdx = matcher.start(1);
                sb.append(line.substring(postMatchIdx, preMatchIdx));
                if (old2NewSimpleClassNames.containsKey(jplClassName)) {
                    sb.append(old2NewSimpleClassNames.get(jplClassName));
                    hasFileBeenEdited = true;
                } else {
                    sb.append(jplClassName);
                    System.err.println("Line " + lineNum + ": cannot find new name for " + jplClassName + " in file " + file);
                }
                postMatchIdx = matcher.end(1);
            }
            sb.append(line.substring(postMatchIdx));
            pw.append(sb.toString());
            pw.append("\n");
        }
        pw.flush();
        pw.close();
        if (!hasFileBeenEdited) {
            FileUtils.deleteQuietly(tmpFile);
            return null;
        }
        return tmpFile;
    }

    private void parseRules() throws FileNotFoundException, IOException {
        FileReader fr = new FileReader(params.getRuleFile());
        BufferedReader br = new BufferedReader(fr);
        String line = null;
        String currentPackage = "";
        while ((line = br.readLine()) != null) {
            if (line.length() == 0 || line.charAt(0) == '#' || line.matches("^\\s$")) {
                continue;
            }
            if (line.charAt(0) == '>') {
                currentPackage = line.substring(1).trim();
            } else {
                String[] fields = line.split(":");
                String oldClassName = fields[0].trim();
                oldSimpleClass2PackageNames.put(oldClassName, currentPackage);
                oldSimpleClass2RelativeRenaming.put(oldClassName, fields[1].trim());
            }
        }
    }

    private void loadNewAbsoluteClassName() {
        for (String oldName : oldSimpleClass2PackageNames.keySet()) {
            loadNewAbsoluteClassName(oldName, oldSimpleClass2RelativeRenaming.get(oldName));
        }
    }

    private void loadNewAbsoluteClassName(String oldSimpleName, String old2newRelativePath) {
        List<String> newPathFields = new ArrayList<String>(Arrays.asList(old2newRelativePath.split("\\/")));
        int numOfClasses = mapOldToNewNames(oldSimpleName, newPathFields.get(newPathFields.size() - 1));
        String packageName = oldSimpleClass2PackageNames.get(oldSimpleName);
        String oldAbsoluteName = packageName + "." + oldSimpleName;
        List<String> oldPathFields = Arrays.asList(oldAbsoluteName.split("\\."));
        int oldLastIndex = oldPathFields.size() - (numOfClasses);
        int newFirstIndex = 0;
        if (newPathFields.size() > 1) {
            if (newPathFields.get(0).equals("")) {
                newPathFields.remove(0);
                String newAbsoluteName = StringUtils.join(newPathFields, ".");
                old2NewAbsoluteImportNames.put(oldAbsoluteName, newAbsoluteName);
                return;
            } else {
                for (String field : newPathFields) {
                    if (field.equals("..")) {
                        oldLastIndex--;
                        newFirstIndex++;
                    } else {
                        break;
                    }
                }
            }
        }
        oldPathFields = oldPathFields.subList(0, oldLastIndex);
        newPathFields = newPathFields.subList(newFirstIndex, newPathFields.size());
        String newPackagePath1 = StringUtils.join(oldPathFields, ".");
        String newPackagePath2 = StringUtils.join(newPathFields, ".");
        String newPath = newPackagePath1 + "." + newPackagePath2;
        old2NewAbsoluteImportNames.put(oldAbsoluteName, newPath);
    }

    private int mapOldToNewNames(String oldSimpleName, String newName) {
        String[] oldClasses = oldSimpleName.split("\\.");
        String[] newClasses = newName.split("\\.");
        if (oldClasses.length != newClasses.length) {
            throw new IllegalArgumentException("invalid renaming " + oldSimpleName + " to " + newName);
        }
        for (int i = 0; i < oldClasses.length; i++) {
            old2NewSimpleClassNames.put(oldClasses[i], newClasses[i]);
        }
        return oldClasses.length;
    }

    public JPLUpdaterParameters getParameters() {
        return params;
    }

    public void run() throws Exception {
        if (!params.isRevertOriState() && !params.isClearBackupFiles()) {
            parseRules();
            loadNewAbsoluteClassName();
            targetFiles = selectFiles();
            System.out.println("replacing all occurrences...");
            pb = TerminalProgressBar.newInstance(0, targetFiles.size());
            List<String> files = new ArrayList<String>();
            int count = 0;
            File backupFile = null;
            for (File selectedFile : targetFiles) {
                if (params.isBackupSelectedFiles()) {
                    backupFile = createBackupFile(selectedFile);
                    if (!backupFile.exists()) {
                        FileUtils.copyFile(selectedFile, backupFile);
                    }
                    files.add(backupFile.getAbsolutePath());
                }
                File tmpFile = copyAndModifyFile(selectedFile);
                if (tmpFile != null) {
                    FileUtils.copyFile(tmpFile, selectedFile);
                    if (params.isBackupSelectedFiles()) {
                        files.add(tmpFile.getAbsolutePath());
                    } else {
                        FileUtils.deleteQuietly(tmpFile);
                    }
                } else {
                    if (params.isBackupSelectedFiles()) {
                        FileUtils.deleteQuietly(backupFile);
                        files.remove(files.size() - 1);
                    }
                }
                pb.setValue(++count);
            }
            if (files.size() > 0) {
                FileUtils.writeLines(params.getTracedFile(), files);
            }
        } else {
            if (params.isRevertOriState()) {
                params.setClearBackupFiles(true);
                boolean isRevert = true;
                if (params.isInteractive()) {
                    InteractiveInputScanner scanner = new InteractiveInputScannerImpl.Builder().prompt("are you really want to revert ? [Ny]").inputPattern(InteractiveInputScannerImpl.YES_NO_PATTERN).defaultInput("n").build();
                    String userInput = scanner.waitInput();
                    if (!userInput.matches("^[yY]$")) {
                        isRevert = false;
                    }
                }
                if (isRevert) {
                    revert();
                } else {
                    System.err.println("revert operation cancelled");
                    System.exit(0);
                }
                params.setInteractive(false);
            }
            if (params.isClearBackupFiles()) {
                boolean isClear = true;
                if (params.isInteractive()) {
                    InteractiveInputScanner scanner = new InteractiveInputScannerImpl.Builder().prompt("are you really want to clear temporary files ? [Ny]").inputPattern(InteractiveInputScannerImpl.YES_NO_PATTERN).defaultInput("n").build();
                    String userInput = scanner.waitInput();
                    if (!userInput.matches("^[yY]$")) {
                        isClear = false;
                    }
                }
                if (isClear) {
                    clear();
                } else {
                    System.err.println("clear operation cancelled");
                }
            }
        }
    }
}
