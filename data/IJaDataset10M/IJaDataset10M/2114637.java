package br.unb.syntainia.programs;

import java.util.Properties;
import br.unb.syntainia.Util;

/**
 * Specifies a set of parameters to execute an external program. For each
 * specific program a parameter class should be implemented extending this
 * class.
 * 
 * @author Rodrigo C. M. Coimbra
 */
public class ProgramRunnerParameters {

    public static final String PROGRAMS_PROPERTIES = "programs.properties";

    /** Friendly name of the program to execute. */
    private String programName;

    /** Program to execute (binary name). */
    protected String program;

    /** Command to be executed. */
    public String command;

    /** Environment variables. */
    public String[] envp;

    /** Working directory. */
    public String workDir;

    /** File where the standard output of the program should be written. */
    public String stdOutFile;

    /** File where the error output of the program should be written. */
    public String errOutFile;

    /** Input file or directory for the program. */
    public String input;

    /** Output file or directory for the program. */
    public String output;

    /**
	 * Creates a new set of parameters to execute a program.
	 */
    public ProgramRunnerParameters(String programName) {
        Properties properties = Util.loadProperties(PROGRAMS_PROPERTIES);
        if (properties.isEmpty()) {
            throw new RuntimeException("There is no programs configured!");
        }
        this.programName = programName;
        this.program = properties.getProperty(programName);
    }

    /**
	 * Builds the command to be executed. Extensions of this class must
	 * overrides this method.
	 */
    public void buildCommand() {
        if ((program != null) && !program.equals("") && ((command == null) || command.equals(""))) {
            command = program;
        }
    }

    /**
	 * Builds system context attributes to execute the program. A context can be
	 * a directory that should be created for the correct execution of a
	 * program, for example. It is recommended to call this method before
	 * execute the program. Extensions of this class should overrides this
	 * method.
	 */
    public void buildContext() {
    }

    /**
	 * @return the programName
	 */
    public String getProgramName() {
        return programName;
    }

    /**
	 * Returns the program to execute.
	 * 
	 * @return The program to execute.
	 */
    public String getProgram() {
        return program;
    }
}
