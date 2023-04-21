package uk.ac.ncl.cs.instantsoap.commandlineprocessor.commandLine;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

/**
 * The data needed to construct a command-line.
 * <p/>
 * Command-line templates are typically parameterised over variables
 * where as concrete command-lines are parameterised over strings.
 *
 * @author Matthew Pocock
 */
@XmlRootElement(name = "commandLine")
public class CommandLine<Value> {

    private String workingDirectory;

    private String executable;

    private List<Argument<Value>> arguments;

    /**
     * Create a new instance with an empty list of arguments and a null executable
     * and working directory.
     */
    public CommandLine() {
        arguments = new ArrayList<Argument<Value>>();
    }

    /**
     * Create a new instance with the specified property values.
     *
     * @param workingDirectory  the working directory for the excutable
     * @param executable  the name of the executable to run
     * @param arguments  a <code>List</code> of <code>Argument</code>s
     */
    public CommandLine(String workingDirectory, String executable, List<Argument<Value>> arguments) {
        this.workingDirectory = workingDirectory;
        this.executable = executable;
        this.arguments = arguments;
    }

    /**
     * Create a new instance with the specified property values.
     *
     * @param workingDirectory  the working directory for the excutable
     * @param executable  the name of the executable to run
     * @param arguments   <code>Argument</code>s for the command line
     */
    public CommandLine(String workingDirectory, String executable, Argument<Value>... arguments) {
        this.workingDirectory = workingDirectory.trim();
        this.executable = executable.trim();
        this.arguments = Arrays.asList(arguments);
    }

    /**
     * Get the working directory that the application will run relative to.
     *
     * @return the working directory name
     */
    @XmlElement(name = "workingDirectory")
    public String getWorkingDirectory() {
        return workingDirectory;
    }

    /**
     * Set the working directory that the application will run relative to.
     *
     * @param workingDirectory  the new working directory name
     */
    public void setWorkingDirectory(String workingDirectory) {
        this.workingDirectory = workingDirectory.trim();
    }

    /**
     * Get the name of the executable. This may be relative to the
     * working directory, in the path, or absolute.
     *
     * @return the executable name
     */
    @XmlElement(name = "executable")
    public String getExecutable() {
        return executable;
    }

    /**
     * Set the name of the executable.
     *
     * @param executable  the name of the executable
     */
    public void setExecutable(String executable) {
        this.executable = executable.trim();
    }

    /**
     * Get the arguments list.
     *
     * @return the <code>List</code> of <code>Argument</code>s defining this
     *    command-line
     */
    @XmlElementWrapper(name = "arguments")
    @XmlElement(name = "argument")
    public List<Argument<Value>> getArguments() {
        return arguments;
    }

    /**
     * Set the arguments list.
     *
     * @param arguments  the new <code>List</code> of <code>Argument</code>s
     */
    public void setArguments(List<Argument<Value>> arguments) {
        this.arguments = arguments;
    }

    /**
     * Add an argument to the end of the arguments list.
     *
     * @param argument  the <code>Argument</code> to add
     */
    public void addArgument(Argument<Value> argument) {
        this.arguments.add(argument);
    }

    /**
     * Remove the first occurance of an argument from the arguments list.
     *
     * @param argument  the <code>Argument</code> to remove
     */
    public void removeArgument(Argument<Value> argument) {
        this.arguments.remove(argument);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CommandLine that = (CommandLine) o;
        if (arguments != null ? !arguments.equals(that.arguments) : that.arguments != null) {
            return false;
        }
        if (executable != null ? !executable.equals(that.executable) : that.executable != null) {
            return false;
        }
        if (workingDirectory != null ? !workingDirectory.equals(that.workingDirectory) : that.workingDirectory != null) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int result;
        result = (workingDirectory != null ? workingDirectory.hashCode() : 0);
        result = 31 * result + (executable != null ? executable.hashCode() : 0);
        result = 31 * result + (arguments != null ? arguments.hashCode() : 0);
        return result;
    }

    public String toString() {
        return this.getClass() + " { workingDirectory: " + workingDirectory + " executable: " + executable + " arguments: " + arguments + " }";
    }
}
