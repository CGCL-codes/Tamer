package randoop.experiments;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class Randoop100 {

    private static ExperimentBase exp;

    private static PrintStream err;

    public static void main(String[] args2) throws IOException {
        if (args2.length != 1) {
            throw new IllegalArgumentException("No experiment string specified.");
        }
        String[] args = args2[0].split("-");
        assert args.length == 4;
        assert args[0].equals("randoop100");
        assert args[1].length() == 4;
        String ttss = args[1];
        boolean objectcache = false;
        boolean component_based = false;
        String tt = ttss.substring(0, 2);
        if (tt.equals("om")) {
        } else if (tt.equals("fd")) {
            objectcache = true;
            component_based = true;
        } else {
            throw new IllegalArgumentException("Invalid experiment string:" + args[1]);
        }
        String ss = ttss.substring(2, 4);
        String expFileName = "experiments/" + ss + ".experiment";
        File expFile = new File(expFileName);
        if (!expFile.exists()) {
            String msg = "Experiment file does not exist: " + expFile.getAbsolutePath();
            throw new IllegalArgumentException(msg);
        }
        exp = new ExperimentBase(expFile.getAbsolutePath());
        String timeLim = args[2];
        String seed = args[3];
        Integer.parseInt(timeLim);
        Integer.parseInt(seed);
        FileOutputStream fos = new FileOutputStream("log.txt");
        err = new PrintStream(fos);
        System.out.println("========== Calling Randoop: " + exp.classDirAbs);
        List<String> randoop = new ArrayList<String>();
        randoop.add("java");
        randoop.add("-ea");
        randoop.add(Command.javaHeapSize);
        randoop.add("-classpath");
        randoop.add(exp.covInstSourcesDir + ":" + exp.classPath);
        randoop.add("randoop.main.Main");
        randoop.add("gentests");
        randoop.add("--output-tests=fail");
        randoop.add("--junit-output-dir=randoop100");
        randoop.add("--timelimit=" + timeLim);
        randoop.add("--coverage-instrumented-classes=" + exp.covInstClassListFile);
        randoop.add("--classlist=" + exp.targetClassListFile);
        if (exp.methodOmitPattern != null && !exp.methodOmitPattern.trim().equals("")) {
            randoop.add("--omitmethods=" + exp.methodOmitPattern);
        }
        randoop.add("--junit-classname=" + ttss + seed);
        randoop.add("--forbid-null=true");
        randoop.add("--stats-coverage=true");
        randoop.add("--usethreads=true");
        randoop.add("--component-based=" + component_based);
        if (component_based) {
            randoop.add("--alias-ratio=0.5");
            if (objectcache) {
                randoop.add("--use-object-cache");
            }
        }
        randoop.add("--maxsize=50");
        randoop.add("--randomseed=" + seed);
        randoop.add("--expfile=randoop100/randoop100" + ttss + seed + ".data");
        ExperimentBase.printCommand(randoop, false, true);
        int retval = Command.exec(randoop.toArray(new String[0]), System.out, err, "", false, Integer.MAX_VALUE, null);
        if (retval != 0) {
            System.out.println("Command exited with error code " + retval);
            System.out.println("File log.txt contains output of stderr.");
            System.exit(1);
        }
    }
}
