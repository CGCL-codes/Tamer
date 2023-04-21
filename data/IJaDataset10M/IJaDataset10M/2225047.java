package ch.elca.el4ant.taskdefs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.EnumeratedAttribute;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.util.ClasspathUtils;
import ch.elca.el4ant.model.ProjectRepository;
import ch.elca.el4ant.model.PropertyGenerator;
import ch.elca.el4ant.propgen.AppendPropertyGenerator;

/**
 * This task executes all registered property generators in the project
 * repository and write the resulting file. The property value can be defined
 * with <code>value</code> attribute or directly in task CDATA section.
 *
 * <script type="text/javascript">printFileStatus
 *   ("$URL: http://el4ant.svn.sourceforge.net/svnroot/el4ant/trunk/buildsystem/core/java/ch/elca/el4ant/taskdefs/ProjectPropertyTask.java $",
 *   "$Revision: 344 $", "$Date: 2006-04-19 10:43:21 -0400 (Wed, 19 Apr 2006) $", "$Author: yma $"
 * );</script>
 *
 * @author Yves Martin (YMA)
 * @version $Revision: 344 $
 */
public class ProjectPropertyTask extends Task {

    /** Append property action. */
    public static final PropertyAction APPEND = new PropertyAction(PropertyAction.APPEND);

    /** Register property action. */
    public static final PropertyAction REGISTER = new PropertyAction(PropertyAction.REGISTER);

    /** Output property action. */
    public static final PropertyAction OUTPUT = new PropertyAction(PropertyAction.OUTPUT);

    /** Property file comment. */
    private static final String PROPERTY_HEADER = "EL4Ant project properties";

    /** ProjectProperty default action. */
    private PropertyAction action = APPEND;

    /** Generator class name (register action). */
    private String generatorClassName;

    /** Project proerties file to write (output action). */
    private String file;

    /** ClassPath where the class can be found (register action). */
    private Path path;

    /** Property name to append (append action). */
    private String property;

    /** Value to append to the property (append action). */
    private String value;

    /** Separator to use (append action). */
    private String separator = PropertyGenerator.LIST_SEPARATOR;

    /**
     * Ant enumeration for property actions.
     */
    public static final class PropertyAction extends EnumeratedAttribute {

        /** register property action. */
        public static final String REGISTER = "register";

        /** append property action. */
        public static final String APPEND = "append";

        /** output property action. */
        public static final String OUTPUT = "output";

        /**
         * Default constructor.
         */
        public PropertyAction() {
            super();
        }

        /**
         * Creates a new <code>PropertyAction</code> instance from its name.
         *
         * @param value action name
         */
        public PropertyAction(String value) {
            super();
            setValue(value);
        }

        /**
         * Returns the possible values of the enumeration.
         *
         * @return array of possible values
         */
        public String[] getValues() {
            return new String[] { REGISTER, APPEND, OUTPUT };
        }
    }

    /**
     * Set the Action value.
     * @param newAction The new Action value.
     */
    public void setAction(PropertyAction newAction) {
        this.action = newAction;
    }

    /**
     * Set the class name of the generator to register.
     *
     * @param generator the generator class name
     */
    public void setClass(String generator) {
        generatorClassName = generator;
    }

    /**
     * The name of the file to import.
     * @param newFile the name of the file
     */
    public void setFile(String newFile) {
        this.file = newFile;
    }

    /**
     * Set the Property value.
     * @param newProperty The new Property value.
     */
    public void setProperty(String newProperty) {
        this.property = newProperty;
    }

    /**
     * Set the Value value.
     * @param newValue The new Value value.
     */
    public void setValue(String newValue) {
        this.value = newValue;
    }

    /**
     * Set the value from the CDATA text.
     *
     * @param data the text to append to the attribute value
     */
    public void addText(String data) {
        if (value == null) {
            value = data;
        } else {
            value += data;
        }
    }

    /**
     * Set the Separator value.
     * @param newSeparator The new Separator value.
     */
    public void setSeparator(String newSeparator) {
        this.separator = newSeparator;
    }

    /**
     * Set the ClassPath where the class may be found.
     *
     * @param jarPath the classpath
     */
    public void setClassPath(Path jarPath) {
        this.path = jarPath;
    }

    /**
     * Check for a condition and failed if not true.
     *
     * @param condition a <code>boolean</code> value to test
     * @param message the failure message
     */
    private void ensure(boolean condition, String message) {
        if (!condition) {
            log(message, Project.MSG_ERR);
            throw new BuildException(message);
        }
    }

    /**
     * Create and register the defined generator.
     */
    private void registerGenerator() {
        ensure(generatorClassName != null, getTaskName() + " requires 'class' attribute for action " + action);
        Object genInstance = null;
        ClassLoader loader = null;
        if (path == null) {
            loader = this.getClass().getClassLoader();
        } else {
            loader = new AntClassLoader(this.getClass().getClassLoader(), getProject(), path, true);
        }
        genInstance = ClasspathUtils.newInstance(generatorClassName, loader);
        if (!(genInstance instanceof PropertyGenerator)) {
            String message = generatorClassName + " does not implement the PropertyGenerator interface";
            throw new BuildException(message);
        }
        ProjectRepository.getInstance().addPropertyGenerator((PropertyGenerator) genInstance);
        log("PropertyGenerator " + generatorClassName + " registered.", Project.MSG_VERBOSE);
    }

    /**
     * Create and register an append generator.
     */
    private void registerAppendGenerator() {
        ensure(property != null, getTaskName() + " requires 'property' attribute for action " + action);
        ensure(value != null, getTaskName() + " requires 'value' attribute for action " + action);
        AppendPropertyGenerator apg = new AppendPropertyGenerator();
        apg.setProperty(property);
        apg.setValue(value);
        apg.setSeparator(separator);
        ProjectRepository.getInstance().addPropertyGenerator(apg);
        log("AppendPropertyGenerator registered for " + property, Project.MSG_VERBOSE);
    }

    /**
     * Check dependencies for a generator.
     *
     * @param runGen <code>Set</code> of already run generators
     * @param aGenerator a <code>PropertyGenerator</code> to check
     * @return <code>true</code> if the generator can be run
     */
    private boolean checkDependencies(Set runGen, PropertyGenerator aGenerator) {
        boolean result = true;
        Iterator depIt = aGenerator.getDependencies().iterator();
        while (result && depIt.hasNext()) {
            String aDep = (String) depIt.next();
            if (!runGen.contains(aDep)) {
                result = false;
            }
        }
        return result;
    }

    /**
     * Generate project properties and output them.
     */
    private void outputProperties() {
        ensure(file != null, getTaskName() + " requires 'file' attribute for action " + action);
        ProjectRepository pr = ProjectRepository.getInstance();
        File propFile = pr.resolveFile(file);
        log("Generating project properties...", Project.MSG_DEBUG);
        Properties result = pr.getProperties();
        List propertyGenerators = pr.getPropertyGenerators();
        Set runGenerators = new HashSet();
        boolean atLeastOneGeneration = true;
        while (propertyGenerators.size() > 0 && atLeastOneGeneration) {
            atLeastOneGeneration = false;
            Iterator genIt = propertyGenerators.iterator();
            while (genIt.hasNext()) {
                PropertyGenerator aGen = (PropertyGenerator) genIt.next();
                if (!checkDependencies(runGenerators, aGen)) {
                    continue;
                }
                log("Generating properties with " + aGen, Project.MSG_DEBUG);
                aGen.setProjectLogger(getProject(), this);
                if (aGen.generate(result)) {
                    atLeastOneGeneration = true;
                    genIt.remove();
                    runGenerators.add(aGen.getGeneratorName());
                }
            }
        }
        if (propertyGenerators.size() > 0) {
            String message = getTaskName() + " failed because of non satisfied property dependencies.";
            throw new BuildException(message);
        }
        log("Writing project properties into " + propFile, Project.MSG_VERBOSE);
        try {
            result.store(new FileOutputStream(propFile), PROPERTY_HEADER);
        } catch (IOException ioe) {
            throw new BuildException("Fail to write property file " + propFile, ioe);
        }
    }

    /** Execution define action. */
    public void execute() {
        ensure(action != null, getTaskName() + " requires 'action' attribute");
        if (PropertyAction.REGISTER.equals(action.getValue())) {
            registerGenerator();
        } else if (PropertyAction.APPEND.equals(action.getValue())) {
            registerAppendGenerator();
        } else if (PropertyAction.OUTPUT.equals(action.getValue())) {
            outputProperties();
        }
    }
}
