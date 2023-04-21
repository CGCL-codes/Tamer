package de.schwarzrot.jobs.processing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import de.schwarzrot.app.errors.ApplicationException;
import de.schwarzrot.jobs.processing.support.AbstractXProcessStep;
import de.schwarzrot.rec.domain.Recording;

/**
 * implementation of a {@code ProcessStep}, usable to create an ISO image from
 * the authored files
 * 
 * @author <a href="mailto:rmantey@users.sourceforge.net">Reinhard Mantey</a>
 * 
 */
public class CutStep extends AbstractXProcessStep {

    private static final String PREFIX = "CUT";

    private static Pattern STREAM_PAT = Pattern.compile("^Stream:\\s+'([^']+)'$");

    public CutStep(ProcessDefinition parent, String workRoot) {
        super(parent, workRoot);
        processedFiles = new TreeSet<File>();
    }

    @Override
    protected void cleanup() {
    }

    protected void cutRecording(Recording rec, int recNum) {
        List<String> cmdArgs = new ArrayList<String>();
        File workDir = new File(getWorkRoot(), String.format(RECDIR_MASK, recNum));
        String fnCutMarks = null;
        if (!rec.isDVD() || (rec.getCutMarks() != null && rec.getCutMarks().size() > 1)) {
            fnCutMarks = getProcessDefinition().getRecordingHandler().writePjxCutmarks(rec, workDir);
        }
        cmdArgs.add(sysInfo.getJava().getAbsolutePath());
        cmdArgs.add("-Djava.awt.headless=true");
        cmdArgs.add("-jar");
        cmdArgs.add(getConfig().getProjectXPath());
        cmdArgs.add("-out");
        cmdArgs.add(workDir.getAbsolutePath());
        if (getConfig().getXIni() != null) {
            cmdArgs.add("-ini");
            cmdArgs.add(getConfig().getXIni());
        }
        if ("TS".compareTo(getJob().getTarget()) == 0) {
            cmdArgs.add("-tots");
        } else {
            cmdArgs.add("-tovdr");
        }
        if (getJob().getMaxSize() != null && getJob().getMaxSize() > 0) {
            cmdArgs.add("-split");
            cmdArgs.add(getJob().getMaxSize().toString());
        }
        if (fnCutMarks != null) {
            cmdArgs.add("-cut");
            cmdArgs.add(fnCutMarks);
        }
        for (int i = 1; i < 99999; i++) {
            File vdrSrc = rec.getVideoFile(i);
            if (vdrSrc.exists()) cmdArgs.add(vdrSrc.getAbsolutePath()); else break;
        }
        execute(cmdArgs);
    }

    @Override
    protected String getPrefix() {
        return PREFIX;
    }

    @Override
    protected boolean postCheck(int recNum) {
        long sizeTotal = 0l;
        if (processedBytes == 0 || processedFiles.size() < 1) processLog(recNum);
        for (File cur : processedFiles) sizeTotal += cur.length();
        boolean rv = sizeTotal > 0 && sizeTotal >= processedBytes;
        if (!rv) log("Oups, sum of processed filed did not match: was " + sizeTotal + " - should be " + processedBytes);
        return rv;
    }

    @Override
    protected boolean preCheck(int recNum) {
        boolean rv = true;
        Recording rec = getProcessDefinition().getSubjects().get(recNum);
        File check = rec.getVideoFile(1);
        log("check file [" + check.getAbsolutePath() + "] from recording " + rec.getName());
        if (rec.getCutPath().exists()) {
            log("recording " + rec.getPath() + " is already cut! Can't proceed!");
            rv = false;
        } else if (!check.exists() || check.length() < 1) {
            log("missing file >" + check.getAbsolutePath() + "<");
            rv = false;
        }
        return rv;
    }

    protected void processLog(int recNum) {
        File workDir = new File(getWorkRoot(), String.format(RECDIR_MASK, recNum));
        NumberFormat nf = NumberFormat.getInstance();
        File logFile = null;
        for (File any : workDir.listFiles()) {
            if (any.getName().endsWith("_log.txt")) {
                logFile = any;
                break;
            }
        }
        if (logFile != null) {
            processedFiles.clear();
            processedBytes = 0l;
            String line = null;
            BufferedReader br;
            try {
                br = new BufferedReader(new FileReader(logFile));
                while ((line = br.readLine()) != null) {
                    if (line.startsWith("Stream:")) {
                        Matcher m = STREAM_PAT.matcher(line);
                        if (m.matches()) {
                            processedFiles.add(new File(m.group(1)));
                            line = br.readLine();
                            if (line.startsWith("=>")) {
                                String[] parts = line.split("\\s+");
                                Number tmp = nf.parse(parts[1]);
                                processedBytes = tmp.longValue();
                            }
                        }
                    }
                }
            } catch (Throwable t) {
                log("failed to process logfile", t);
            }
        }
    }

    @Override
    protected void workOut(int recNum) {
        Recording rec = getProcessDefinition().getSubjects().get(recNum);
        if (rec.isHDRecording()) throw new ApplicationException("SD-cutting does not support HD-recordings!"); else cutRecording(rec, recNum);
        processLog(recNum);
    }

    private Collection<File> processedFiles;

    private long processedBytes;
}
