package net.sourceforge.javo2.definition;

import generated.GlobalConfig;
import generated.JavoConfig;
import generated.NestedProperty;
import generated.ValueObject;
import java.io.File;
import java.util.Collection;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import net.sourceforge.javo2.compiler.configuration.Configuration;
import net.sourceforge.javo2.compiler.configuration.ConfigurationDefaults;
import net.sourceforge.javo2.compiler.definition.validator.vo.GlobalConfigValueObject;
import net.sourceforge.javo2.compiler.definition.validator.vo.NoSuchValueObjectException;
import net.sourceforge.javo2.compiler.exception.DefinitionValidationException;
import net.sourceforge.javo2.compiler.exception.ParameterValidationException;
import net.sourceforge.javo2.configuration.file.IGlobalConfigurationParser;
import net.sourceforge.javo2.configuration.file.impl.GlobalConfigurationParser;
import net.sourceforge.javo2.utils.file.FileAdapter;
import net.sourceforge.javo2.utils.file.FileUtils;
import net.sourceforge.javo2.utils.file.FileWrapper;
import net.sourceforge.javo2.utils.validator.AndValidator;
import net.sourceforge.javo2.utils.validator.CompoundValidator;
import net.sourceforge.javo2.utils.validator.IValidator;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * @author Nicolás Di Benedetto
 * {@link mailto:nikodb@users.sourceforge.net nikodb@users.sourceforge.net }.<br>
 * Created on 26/09/2007.<br>
 * This class is intended to parse and validate JAVO2's definition file.
 */
public class DefinitionProcessor {

    /**
	 * Logging facility instance.
	 */
    private static Logger logger = Logger.getLogger(DefinitionProcessor.class);

    /**
	 * The FQN for the package where JAXB generated files are located.
	 */
    private static final String DEFAULT_JAXB_CONTEX = "generated";

    /**
	 * JAXB context package.
	 */
    private String jaxbContext = null;

    /**
	 * The Global-Configuration parser instance.
	 */
    private IGlobalConfigurationParser globalConfigurationParser = null;

    /**
	 * The definition validator instance.
	 */
    private IValidator<GlobalConfigValueObject> definitionValidator = null;

    /**
	 * A reference to the {@link GlobalConfig} instance used to configure VOs. 
	 */
    private static GlobalConfig globalConfig = null;

    /**
	 * A reference to the {@link GlobalConfigValueObject} to provide facility accessors.
	 */
    private static GlobalConfigValueObject globalConfigValueObject = null;

    /**
	 * Parameterized constructor. 
	 * @param jaxbContext the JAXB context package to set up the processor.
	 * This property is immutable since this definition processor is bound
	 * to a specific JAXB context.
	 */
    public DefinitionProcessor(String jaxbContext) {
        this.jaxbContext = jaxbContext;
    }

    /**
	 * Getter for the attribute globalConfigurationParser.
	 * @return Returns the attribute globalConfigurationParser.
	 */
    public IGlobalConfigurationParser getGlobalConfigurationParser() {
        return (null == globalConfigurationParser) ? globalConfigurationParser = new GlobalConfigurationParser() : globalConfigurationParser;
    }

    /**
	 * Setter for the attribute globalConfigurationParser.
	 * @param globalConfigurationParser the value to assign to the attribute globalConfigurationParser.
	 */
    public void setGlobalConfigurationParser(IGlobalConfigurationParser globalConfigurationParser) {
        this.globalConfigurationParser = globalConfigurationParser;
    }

    /**
	 * Processes the framework definition file's Global Configuration portion.
	 * @param definitionFile the XML file containing the framework's definition file.
	 * @return the Configuration object instance properly initialized.
	 */
    public Configuration processGlobalConfig(File definitionFile) {
        Configuration fileConfiguration = null;
        JavoConfig rootElement = unmarshallXML(definitionFile);
        if (null != rootElement.getGlobalConfig()) {
            fileConfiguration = this.getGlobalConfigurationParser().parse(rootElement.getGlobalConfig());
            fileConfiguration.setConfigFile(definitionFile);
        }
        return fileConfiguration;
    }

    /**
	 * Unmarshalls the given XML file and returns the rootElement. 
	 * @param file the file to unmarshall.
	 * @return the root element for the file.
	 */
    private JavoConfig unmarshallXML(File file) {
        JAXBContext jaxbContext = null;
        Unmarshaller unmarshaller = null;
        JavoConfig rootElement = null;
        try {
            jaxbContext = JAXBContext.newInstance(this.getJaxbContext());
            unmarshaller = jaxbContext.createUnmarshaller();
            rootElement = (JavoConfig) unmarshaller.unmarshal(file);
        } catch (JAXBException e) {
            Logger.getLogger(this.getClass()).fatal(e);
        }
        if (null == globalConfig) {
            globalConfig = rootElement.getGlobalConfig();
        }
        return rootElement;
    }

    /**
	 * Merges two configurations into a single one. Original configurations are not modified.
	 * @param overridenConfiguration the configuration instance with lower priority, 
	 * this means that overlapping attributes from this instance will be overridden.  
	 * @param overridingConfiguration the configuration instance with higher priority, 
	 * this means that overlapping attributes from this instance will prevail.
	 * @return a configuration instance with merged attributes. 
	 * @throws ParameterValidationException in case of a misconfiguration.
	 */
    public Configuration mergeDefinitionsAndValidate(final Configuration overridenConfiguration, final Configuration overridingConfiguration) throws ParameterValidationException {
        Configuration mergedConfiguration = new Configuration();
        if (!overridenConfiguration.getGenerateClass() && !overridenConfiguration.getGenerateJava() && !overridingConfiguration.getGenerateClass() && !overridingConfiguration.getGenerateJava()) {
            throw new ParameterValidationException("Neither Class or Source generation were specified.\n" + "Please provide target and/or source configuration elements on the XML or the apropriate command line parameters.");
        }
        if (overridingConfiguration.getGenerateClass() || overridenConfiguration.getGenerateClass()) {
            mergedConfiguration.setGenerateClass(true);
            if (null != overridingConfiguration.getOutputClassDir()) {
                logger.info("Using overriden (CLI) property to configure output directory for class files.");
                mergedConfiguration.setOutputClassDir(overridingConfiguration.getOutputClassDir());
            } else if (null != overridenConfiguration.getOutputClassDir()) {
                logger.info("Using parsed (XML) property to configure output directory for class files.");
                mergedConfiguration.setOutputClassDir(overridenConfiguration.getOutputClassDir());
            } else {
                logger.warn("Class generation were specified but neither XML nor command line specify output directory. " + "Using default value ('" + ConfigurationDefaults.DEFAULT_TARGET_DIR + "').");
                File outputTargetDir = new File(ConfigurationDefaults.DEFAULT_TARGET_DIR);
                mergedConfiguration.setOutputClassDir(outputTargetDir);
            }
        }
        if (overridingConfiguration.getGenerateJava() || overridenConfiguration.getGenerateJava()) {
            mergedConfiguration.setGenerateJava(true);
            if (null != overridingConfiguration.getOutputSrcDir()) {
                logger.info("Using overriden (CLI) property to configure output directory for source files.");
                mergedConfiguration.setOutputSrcDir(overridingConfiguration.getOutputSrcDir());
            } else if (null != overridenConfiguration.getOutputSrcDir()) {
                logger.info("Using parsed (XML) property to configure output directory for source files.");
                mergedConfiguration.setOutputSrcDir(overridenConfiguration.getOutputSrcDir());
            } else {
                logger.warn("Source generation were specified but neither XML nor command line specify output directory. " + "Using default value ('" + ConfigurationDefaults.DEFAULT_SRC_DIR + "').");
                File outputSourceDir = new File(ConfigurationDefaults.DEFAULT_SRC_DIR);
                mergedConfiguration.setOutputSrcDir(outputSourceDir);
            }
        }
        if (null != overridingConfiguration.getDefaultPackage()) {
            logger.info("Using overriden (CLI) property to configure dafault package.");
            mergedConfiguration.setDefaultPackage(overridingConfiguration.getDefaultPackage());
        } else if (null != overridenConfiguration.getDefaultPackage()) {
            logger.info("Using parsed (XML) property to configure dafault package.");
            mergedConfiguration.setDefaultPackage(overridenConfiguration.getDefaultPackage());
        } else {
            logger.warn("No package definition found. Using default value ('" + ConfigurationDefaults.DEFAULT_PACKAGE + "')");
            mergedConfiguration.setDefaultPackage(ConfigurationDefaults.DEFAULT_PACKAGE);
        }
        mergedConfiguration.setConfigFile(overridingConfiguration.getConfigFile());
        return mergedConfiguration;
    }

    /**
	 * Validates the definition file.
	 * @param definitionFile the file to validate.
	 * @return a list with all the value objects.
	 * @throws DefinitionValidationException in case of an invalid definition. 
	 */
    public List<ValueObject> validateDefinition(File definitionFile) throws DefinitionValidationException {
        JavoConfig rootElement = this.unmarshallXML(definitionFile);
        String strIncludeFiles = rootElement.getGlobalConfig().getIncludeFiles();
        Collection<FileWrapper> includeFiles = FileAdapter.adaptAll(FileUtils.getFiles(strIncludeFiles, definitionFile.getParentFile().getAbsolutePath()));
        String strExcludeFiles = rootElement.getGlobalConfig().getExcludeFiles();
        includeFiles.removeAll(FileAdapter.adaptAll(FileUtils.getFiles(strExcludeFiles, definitionFile.getParentFile().getAbsolutePath())));
        includeFiles.remove(FileAdapter.adapt(definitionFile));
        for (File file : includeFiles) {
            JavoConfig fileRootElement = this.unmarshallXML(file);
            if (null == fileRootElement) {
                throw new DefinitionValidationException("No entries found for definition file <" + file.getAbsolutePath() + ">.");
            }
            logger.info("Reading additional definition file <" + file.getName() + ">.");
            rootElement.getValueObject().addAll(fileRootElement.getValueObject());
        }
        DefinitionProcessor.globalConfigValueObject = new GlobalConfigValueObject(rootElement.getValueObject(), rootElement.getGlobalConfig());
        if (!this.getDefinitionValidator().validate(DefinitionProcessor.globalConfigValueObject)) {
            throw new DefinitionValidationException("Definition file <'" + definitionFile.getAbsolutePath() + "'> contains invalid entries:\n\t" + this.getDefinitionValidator().getInvalidReason());
        }
        return rootElement.getValueObject();
    }

    /**
	 * Resolves the FQCN for a given VO (i.e., the FQCN for the VO's interface).
	 * @param valueObject the value object with the attributes to compute.
	 * @param globalConfig the globalConfig object with the default package definition.
	 * @return the calculated FQCN for the definition.
	 */
    public static String getTargetFQCN(final ValueObject valueObject, final GlobalConfig globalConfig) {
        return getTargetFQCN(valueObject.getPackage(), valueObject.getInterfaceName(), globalConfig);
    }

    /**
	 * Resolves the FQCN for a given VO (i.e., the FQCN for the VO's interface).
	 * @param nestedProperty the nested property object with the attributes to compute.
	 * @param globalConfig the globalConfig object with the default package definition.
	 * @return the calculated FQCN for the definition.
	 */
    public static String getTargetFQCN(final NestedProperty nestedProperty, final GlobalConfig globalConfig) {
        return getTargetFQCN(nestedProperty.getPackage(), nestedProperty.getInterfaceName(), globalConfig);
    }

    /**
	 * Resolves the FQCN for a given VO (i.e., the FQCN for the VO's interface).
	 * @param strPackage the package that hosts the interface.
	 * @param interfaceName the name of the interface to qualify.
	 * @param globalConfig the globalConfig object with the default package definition.
	 * @return the calculated FQCN for the definition.
	 */
    private static String getTargetFQCN(final String strPackage, final String interfaceName, final GlobalConfig globalConfig) {
        if (StringUtils.contains(interfaceName, ".")) {
            return interfaceName;
        }
        StringBuffer fqcn = new StringBuffer();
        if (null != strPackage) {
            if (strPackage.startsWith(".")) {
                fqcn.insert(0, globalConfig.getDefaultPackage());
                fqcn.append(strPackage);
            } else {
                fqcn.insert(0, strPackage);
            }
        } else {
            fqcn.insert(0, globalConfig.getDefaultPackage());
        }
        fqcn.append(".").append(interfaceName);
        return fqcn.toString();
    }

    /**
	 * Resolves the target FQCN for a given VO.
	 * @param valueObject the value object with the attributes to compute.
	 * @return the calculated FQCN for the definition.
	 */
    public static String getTargetFQCN(final ValueObject valueObject) {
        DefinitionProcessor.validateGlobalConfigInitialized();
        return getTargetFQCN(valueObject, DefinitionProcessor.globalConfig);
    }

    /**
	 * Resolves the target FQCN for a given VO.
	 * @param nestedProperty the value object with the attributes to compute.
	 * @return the calculated FQCN for the definition.
	 */
    public static String getTargetFQCN(final NestedProperty nestedProperty) {
        DefinitionProcessor.validateGlobalConfigInitialized();
        return getTargetFQCN(nestedProperty, DefinitionProcessor.globalConfig);
    }

    /**
	 * Validates whether the global configuration element is being used 
	 * prior to be initialized or not and notifies in case of failure.  
	 */
    private static void validateGlobalConfigInitialized() {
        if (null == DefinitionProcessor.globalConfig) {
            String error = "globalConfig has not been initialized yet! You must run either validateDefinition in " + "at least one instance of this class in order to get the DefinitionProcessor engine started!";
            logger.error(error);
            throw new RuntimeException(error);
        }
    }

    /**
	 * Getter for the attribute definitionValidator.
	 * @return Returns the attribute definitionValidator.
	 */
    public IValidator<GlobalConfigValueObject> getDefinitionValidator() {
        if (null == this.definitionValidator) {
            logger.warn("Validators not injected by container! Returning default implementation.");
            CompoundValidator<GlobalConfigValueObject> andValidator = new AndValidator<GlobalConfigValueObject>();
            this.definitionValidator = andValidator;
        }
        return this.definitionValidator;
    }

    /**
	 * Setter for the attribute definitionValidator.
	 * @param definitionValidator the value to assign to the attribute definitionValidator.
	 */
    public void setDefinitionValidator(IValidator<GlobalConfigValueObject> definitionValidator) {
        this.definitionValidator = definitionValidator;
    }

    /**
	 * Getter for the attribute jaxbContext.
	 * @return Returns the attribute jaxbContext.
	 */
    protected String getJaxbContext() {
        return (null == this.jaxbContext) ? this.jaxbContext = DEFAULT_JAXB_CONTEX : this.jaxbContext;
    }

    /**
	 * This method provides lookup facility to locate VO definitions given its id. 
	 * @param valueObjectId the Id of the value object to locate.
	 * @return the value object for the given definition.
	 */
    public static ValueObject getValueObjectById(String valueObjectId) {
        ValueObject result = null;
        if (null == DefinitionProcessor.globalConfigValueObject) {
            String error = "DefinitionProcessor engine not initialized! validateDefinition needs " + "to be called in at lest one instance of this class in order to initialize the engine.";
            logger.fatal(error);
            throw new RuntimeException(error);
        }
        try {
            result = DefinitionProcessor.globalConfigValueObject.getValueObjectById(valueObjectId);
        } catch (NoSuchValueObjectException e) {
            String error = "Error while performing lookup for VO <" + valueObjectId + ">. There is no such element on the definition mapping.";
            logger.fatal(error, e);
            throw new RuntimeException(error);
        }
        return result;
    }

    /**
	 * Resolves the FQCN of a child nested property.
	 * @param nestedProperty the parent property that holds this child property.
	 * @param child the property to get its FQCN.
	 * @return the FQCN of the given child property.
	 */
    public static String getTargetFQCN(NestedProperty nestedProperty, NestedProperty child) {
        if (StringUtils.contains(child.getName(), ".")) {
            return child.getName();
        }
        String parentFQCN = getTargetFQCN(nestedProperty);
        String parentPackage = StringUtils.substring(parentFQCN, 0, StringUtils.lastIndexOf(parentFQCN, "."));
        return parentPackage + "." + child.getName() + "." + child.getInterfaceName();
    }

    /**
	 * @param valueObject
	 * @return
	 */
    public static String getPackage(ValueObject valueObject) {
        String parentFQCN = getTargetFQCN(valueObject);
        return StringUtils.substring(parentFQCN, 0, StringUtils.lastIndexOf(parentFQCN, "."));
    }
}
