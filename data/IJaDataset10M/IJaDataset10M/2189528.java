package es.gavab.jmh.analysis;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import es.gavab.jmh.analysis.table.AnalysisException;
import es.gavab.jmh.experiment.IMExecutions;
import es.gavab.jmh.experiment.IMExecutionsSaverLoader;
import es.gavab.jmh.tablecreator.Alias;

/**
 * @author Patxi Gortázar
 * 
 */
public class Analysis {

    private File resultsDir;

    private final List<Analyzer> analyzers;

    private final Pattern expDate = Pattern.compile("(\\d+)-(\\d+)-(\\d+)_(\\d+)-(\\d+)-(\\d+)");

    private Locale locale;

    private List<Alias> rowAliases;

    private List<Alias> colAliases;

    public Analysis(File resultsDir, List<Analyzer> analyzers) {
        this.resultsDir = resultsDir;
        this.analyzers = analyzers;
        this.locale = Locale.getDefault();
    }

    public void executeAnalysis() throws AnalysisException {
        List<File> experiments = extractDirsRecursively(resultsDir);
        if (experiments.isEmpty()) {
            experiments.add(resultsDir);
        }
        int index = 1;
        for (File experimentDir : experiments) {
            System.out.println("---" + index + "/" + experiments.size() + " Experiment: " + experimentDir);
            String expName = experimentDir.getName();
            Matcher m = expDate.matcher(expName);
            if (m.find()) {
                Calendar cal = Calendar.getInstance(Locale.getDefault());
                cal.set(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)), Integer.parseInt(m.group(3)), Integer.parseInt(m.group(4)), Integer.parseInt(m.group(5)), Integer.parseInt(m.group(6)));
                System.out.println(cal.toString());
            }
            try {
                List<IMExecutions> imExecutionsList = IMExecutionsSaverLoader.getInstance().loadIMExecutionsList(experimentDir);
                for (Analyzer analyzer : analyzers) {
                    analyzer.setIMExecutionsList(imExecutionsList);
                    analyzer.buildAnalysis();
                }
            } catch (IOException e) {
                throw new AnalysisException("Couldn't load experiment data", e);
            } catch (ClassNotFoundException e) {
                throw new AnalysisException(e);
            }
            index++;
        }
    }

    /**
	 * @param resultsDir2
	 * @return
	 */
    private List<File> extractDirsRecursively(File dir) {
        File[] subdirs = dir.listFiles(new FileFilter() {

            public boolean accept(File f) {
                return f.isDirectory();
            }
        });
        List<File> dirs = new ArrayList<File>();
        for (File subdir : subdirs) {
            List<File> subsubdirs = extractDirsRecursively(subdir);
            if (subsubdirs.isEmpty()) {
                dirs.add(subdir);
            } else {
                dirs.addAll(subsubdirs);
            }
        }
        return dirs;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        for (Analyzer analyzer : analyzers) {
            analyzer.setLocale(locale);
        }
        this.locale = locale;
    }

    public void setRowAliases(List<Alias> rowAliases) {
        this.rowAliases = rowAliases;
        for (Analyzer analyzer : analyzers) {
            analyzer.setRowAliases(rowAliases);
        }
    }

    public void setColAliases(List<Alias> colAliases) {
        this.colAliases = colAliases;
        for (Analyzer analyzer : analyzers) {
            analyzer.setColAliases(colAliases);
        }
    }
}
