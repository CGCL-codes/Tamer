package es.optsicom.lib.analysis;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import es.optsicom.lib.experiment.IMExecutions;
import es.optsicom.lib.experiment.IMExecutionsSaverLoader;

public class ChangeInstanceInExperiments {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        new ChangeInstanceInExperiments(new File("../es.optsicom.problem.mdp.approx/Experiments/Web20min/MDG-IIa"), new File("../es.optsicom.problem.mdp.approx/Experiments/Web20min/MDG-IIa_b"), "MDG-IIb", "MDG-IIa").doChange();
    }

    private File resultsDir;

    private File newDir;

    private String replace;

    private String instanceIdRegExp;

    public ChangeInstanceInExperiments(File resultsDir, File newDir, String instanceIdRegExp, String replace) {
        this.resultsDir = resultsDir;
        this.newDir = newDir;
        this.instanceIdRegExp = instanceIdRegExp;
        this.replace = replace;
    }

    public void doChange() {
        newDir.mkdirs();
        File[] instanceFiles = resultsDir.listFiles(new FileFilter() {

            public boolean accept(File pathname) {
                return pathname.isFile();
            }
        });
        for (File instanceFile : instanceFiles) {
            System.out.println("File: " + instanceFile);
            try {
                IMExecutions imExecs = IMExecutionsSaverLoader.getInstance().loadIMExecutions(new File(resultsDir, instanceFile.getName()));
                String id = imExecs.getInstanceDescription().getId();
                String newId = id.replaceAll(this.instanceIdRegExp, replace);
                System.out.println("Id:" + id + " -> " + newId);
                imExecs.getInstanceDescription().setId(newId);
                IMExecutionsSaverLoader.getInstance().saveIMExecutions(newDir, imExecs);
            } catch (FileNotFoundException e) {
                System.out.println("Couldn't find instance " + instanceFile + " in folder " + resultsDir);
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
