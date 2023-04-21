package jeplus;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import jeplus.agent.EPlusAgent;
import jeplus.agent.EPlusAgentLocal;
import jeplus.data.RandomSource;

/**
 * Main entry point to jEPlus. Options to redirect err stream to jeplus.err file
 * @author yi zhang
 * @version 1.3
 * @since 0.1
 */
public class Main {

    /** 
     * Flag for redirecting error stream
     */
    public static final boolean REDIRECT_ERR = true;

    /**
     * Default agent to use
     */
    protected static EPlusAgent DefaultAgent = new EPlusAgentLocal(null);

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (REDIRECT_ERR) {
            try {
                System.setErr(new PrintStream(new FileOutputStream("jeplus.err")));
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
        }
        System.setProperty("line.separator", "\n");
        new Main().mainFunction(args);
    }

    /** 
     * Instance main function so it can be inherited
     * @param args Program arguments
     */
    public void mainFunction(String[] args) {
        try {
            if (args.length <= 0) {
                JEPlusFrameMain.startGUI(new JEPlusFrameMain());
            } else {
                if (args[0].toLowerCase().equals("-batch")) {
                    if (args.length >= 2) {
                        JEPlusProject project = new JEPlusProject(args[1]);
                        EPlusBatch batch = new EPlusBatch(null, project);
                        DefaultAgent.setSettings(project.getExecSettings());
                        batch.setAgent(DefaultAgent);
                        EPlusBatchInfo info = batch.validateProject();
                        System.err.println(info.getValidationErrorsText());
                        if (info.isValidationSuccessful()) {
                            batch.buildJobs();
                            EPlusBatch sampleBatch = batch;
                            if (args.length >= 3) {
                                int njobs = Integer.parseInt(args[2]);
                                sampleBatch = new EPlusBatch(batch, njobs, RandomSource.getRandomGenerator());
                            }
                            sampleBatch.start();
                            do {
                                try {
                                    Thread.sleep(5000);
                                } catch (Exception ex) {
                                }
                            } while (sampleBatch.isSimulationRunning());
                            System.out.println("All jobs finished. jEPlus terminated normally.");
                        } else {
                            System.err.println("jEPlus cannot execute the jobs. Please make sure the project is valid.");
                        }
                    } else {
                        showCommandLineInstruction();
                    }
                } else if (args[0].toLowerCase().equals("-job")) {
                    if (args.length == 2 || (args.length == 3 && args[2].equals("-all"))) {
                        JEPlusProject project = new JEPlusProject(args[1]);
                        EPlusBatch batch = new EPlusBatch(null, project);
                        DefaultAgent.setSettings(project.getExecSettings());
                        batch.setAgent(DefaultAgent);
                        EPlusBatchInfo info = batch.validateProject();
                        System.err.println(info.getValidationErrorsText());
                        if (info.isValidationSuccessful()) {
                            batch.buildJobs();
                            batch.start();
                            do {
                                try {
                                    Thread.sleep(5000);
                                } catch (Exception ex) {
                                }
                            } while (batch.isSimulationRunning());
                            System.out.println("All jobs finished. jEPlus terminated normally.");
                        } else {
                            System.err.println("jEPlus cannot execute the jobs. Please make sure the project is valid.");
                        }
                    } else if (args.length == 4) {
                        JEPlusProject project = new JEPlusProject(args[1]);
                        EPlusBatch batch = new EPlusBatch(null, project);
                        DefaultAgent.setSettings(project.getExecSettings());
                        batch.setAgent(DefaultAgent);
                        EPlusBatchInfo info = batch.validateProject();
                        System.err.println(info.getValidationErrorsText());
                        if (info.isValidationSuccessful()) {
                            EPlusBatch sampleBatch = batch;
                            if (args[2].equalsIgnoreCase("-sample")) {
                                batch.buildJobs();
                                int njobs = Integer.parseInt(args[3]);
                                sampleBatch = new EPlusBatch(batch, njobs, RandomSource.getRandomGenerator());
                            } else {
                                String typestr = args[2].substring(1).toUpperCase();
                                sampleBatch.prepareJobSet(EPlusBatch.JobStringType.valueOf(typestr), args[3]);
                            }
                            sampleBatch.start();
                            do {
                                try {
                                    Thread.sleep(5000);
                                } catch (Exception ex) {
                                }
                            } while (sampleBatch.isSimulationRunning());
                            System.out.println("All simulation jobs finished. jEPlus terminated normally.");
                        } else {
                            System.err.println("jEPlus cannot execute the jobs. Please make sure that the project is valid.");
                        }
                    } else {
                        showCommandLineInstruction();
                    }
                } else if (args[0].equalsIgnoreCase("-gui")) {
                    JEPlusFrameMain frame = new JEPlusFrameMain();
                    frame.enableWriteObjMenuItem();
                    JEPlusFrameMain.startGUI(frame);
                    frame.setDefaultCloseOperation(JEPlusFrameMain.DO_NOTHING_ON_CLOSE);
                    frame.setFrameCloseOperation(JEPlusFrameMain.DISPOSE_ON_CLOSE);
                } else {
                    showCommandLineInstruction();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Command-line instructions
     */
    public void showCommandLineInstruction() {
        System.out.println(JEPlusFrameMain.getVersionInfo() + " (C) 2010, 2011, Yi Zhang, yizhang@dmu.ac.uk");
        System.out.println("Usage: ");
        System.out.println("  GUI mode:       java [-Xmx1000m] -jar jEPlus.jar");
        System.out.println("  Batch mode*:    java [-Xmx1000m] -jar jEPlus.jar -batch {project_file_name.jep} [n random jobs]");
        System.out.println("  Job modes:      ");
        System.out.println("              all:  java [-Xmx1000m] -jar jEPlus.jar -job {project_file_name.jep} [-all]");
        System.out.println("    random sample:  java [-Xmx1000m] -jar jEPlus.jar -job {project_file_name.jep} -sample {sample_size}");
        System.out.println("       job list 1:  java [-Xmx1000m] -jar jEPlus.jar -job {project_file_name.jep} -index {job_index_string_1;...}");
        System.out.println("       job list 2:  java [-Xmx1000m] -jar jEPlus.jar -job {project_file_name.jep} -value {job_value_string_1;...}");
        System.out.println("       job list 3:  java [-Xmx1000m] -jar jEPlus.jar -job {project_file_name.jep} -id {job_id_1;...}");
        System.out.println("    job list file:  java [-Xmx1000m] -jar jEPlus.jar -job {project_file_name.jep} -file {job_list_file_name}");
        System.out.println("where,");
        System.out.println("  {...} required arguement, to be replaced with your own values");
        System.out.println("  [...] optional arguement, to be replaced with your own values");
        System.out.println("  '|'   separates alternative options");
        System.out.println("  '*'   batch mode is deprecated");
        System.out.println("For example,");
        System.out.println("  \"java -Xmx1000m -jar jEPlus.jar -job my_project.jep\"  executes all jobs of the project in my_project.jep;");
        System.out.println("  \"java -Xmx1000m -jar jEPlus.jar -job my_project.jep -sample 1000\"  executes 1000 randomly selected jobs of the project in my_project.jep;");
        System.out.println("  \"java -jar jEPlus.jar -job my_project.jep -id G_X-T_4-W_0-P1_11-P2_1-P3_1-P4_0;G_X-T_3-W_0-P1_0-P2_1-P3_1-P4_0\"  executes 2 selected jobs (with the job IDs)of a project with 6 or fewer parameters;");
        System.out.println("  \"java -jar jEPlus.jar -job my_project.jep -id X000000134;X000010928;X000009933\"  executes 3 selected jobs (with the job IDs)of the project with more than 6 parameters;");
        System.out.println("  \"java -jar jEPlus.jar -job my_project.jep -index job0,0,0,12,2,4,0;job1,0,0,12,2,4,1;job2,0,1,12,2,4,0\"  creates three jobs with alt_value indexes and run them;");
        System.out.println("  \"java -jar jEPlus.jar -job my_project.jep -value job0,0,0,135,non-reflective,CAV,24.5\"  creates one jobs with the specified parameter value and run it.");
        System.out.println("  \"java -jar jEPlus.jar -job my_project.jep -file my_job_list.txt\"  creates and executes jobs from the job strings list in the specified file. The job strings in the file must be in -value format and separated by either ';' or end-of-line.");
    }
}
