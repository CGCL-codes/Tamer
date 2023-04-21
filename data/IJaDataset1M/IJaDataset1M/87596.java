package ch.bbv.mda.generators;

import static ch.bbv.mda.cartridges.OrmConstants.COLUMN_NAME;
import java.io.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationConverter;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import ch.bbv.application.VersionId;
import ch.bbv.dog.validation.Validator;
import ch.bbv.mda.*;
import ch.bbv.mda.cartridges.dotNet.NetCartridgeConstants;
import ch.bbv.mda.generators.CartridgeInfo.Platform;
import ch.bbv.mda.operators.FileOperators;
import ch.bbv.mda.operators.ModelOperators;
import ch.bbv.utilities.PropertyUtilities;
import ch.bbv.utilities.ReflectUtilities;
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.message.SimpleValidationMessage;

/**
 * The default cartridge implementation provides a set of utility methods to
 * simplify the definition of specific meta data handlers. The cartridge has
 * knowledge about the following constructs.
 * <ul>
 * <li>The cartridge walks through packages, classes, properties and indexed
 * properties. It knows the difference between class, abstract class and
 * interfaces.</li>
 * <li>The cartridge distinguishes between regular , reference code and
 * primitive type properties.</li>
 * <li>The cartridge explores the model to find all ancestors of a class or an
 * interface.</li>
 * </ul>
 * 
 * @author Marcel Baumann
 * @version $Revision: 1.31 $
 */
public abstract class CartridgeImpl implements Cartridge, NetCartridgeConstants {

    /**
   * Extension for Java source file.
   */
    public static final String JAVA_EXTENSION = ".java";

    /**
   * Velocity context key to identify the object stored in the context.
   */
    public static final String CONFIGURATION_KEY = "configuration";

    /**
   * Velocity context key to identify the object stored in the context.
   */
    public static final String CARTRIDGE_KEY = "cartridge";

    /**
   * Velocity context key to identify the object stored in the context.
   */
    public static final String UTILITIES_KEY = "utilities";

    /**
   * Velocity context key to identify the object stored in the context.
   */
    public static final String MODEL_KEY = "model";

    /**
   * Velocity context key to identify the object stored in the context.
   */
    public static final String PACKAGE_KEY = "package";

    /**
   * Velocity context key to identify the object stored in the context.
   */
    public static final String CLASSES_KEY = "classes";

    /**
   * Velocity context key to identify the object stored in the context.
   */
    public static final String CLASS_KEY = "class";

    /**
   * Velocity context key to identify the object stored in the context.
   */
    public static final String PROPERTIES_KEY = "properties";

    /**
   * Velocity context key to identify the object stored in the context.
   */
    public static final String AGGREGATIONS_KEY = "aggregations";

    /**
   * Velocity context key to identify the object stored in the context.
   */
    public static final String NESTEDCLASSES_KEY = "nestedClasses";

    /**
   * logger for all instances of the class.
   */
    private static Log log = LogFactory.getLog(CartridgeImpl.class);

    /**
   * List of all cartridges the cartridge is dependant of.
   */
    private List<Cartridge> cartridges;

    /**
   * Properties for the code generation.
   */
    private Properties properties;

    /**
   * Name of the cartridge.
   */
    private String name;

    /**
   * Version of the cartridge.
   */
    private VersionId version;

    /**
   * Abbreviation of the cartridge.
   */
    private String abbreviation;

    /**
   * The configuration of the cartridge based on a set of properties files.
   */
    private CompositeConfiguration configuration;

    /**
   * Operators for the model.
   */
    private ModelOperators modelOperators;

    /**
   * Constructor for the class.
   * 
   * @param name
   *          name of the cartridge.
   * @param abbreviation
   *          abbreviation of the cartridge.
   * @param version
   *          version of the catridge.
   */
    public CartridgeImpl(String name, String abbreviation, VersionId version) {
        this.name = name;
        this.abbreviation = abbreviation;
        this.version = version;
        this.cartridges = new ArrayList<Cartridge>();
        this.configuration = new CompositeConfiguration();
        this.modelOperators = new ModelOperators();
    }

    public String getName() {
        return name;
    }

    public VersionId getVersion() {
        return version;
    }

    public String getPrefix() {
        return abbreviation;
    }

    public List<Cartridge> getUsedCartridges() {
        return Collections.unmodifiableList(cartridges);
    }

    public boolean checkPathProperties() {
        return true;
    }

    public List getAllProperties() {
        return Collections.EMPTY_LIST;
    }

    public List<Stereotype> getAllStereotypes() {
        return Collections.emptyList();
    }

    public void registerRules(Validator validator) {
    }

    public void preProcessModel(MetaModel model, ValidationResult messages) {
    }

    public void processModel(MetaModel model, ValidationResult messages) {
    }

    public void postProcessModel(MetaModel model, ValidationResult messages) {
    }

    public void preProcessDatatype(MetaDatatype datatype, ValidationResult messages) {
    }

    public void preProcessPackage(MetaPackage metaPackage, ValidationResult messages) {
    }

    public void processPackage(MetaPackage metaPackage, ValidationResult messages) {
    }

    public void postProcessPackage(MetaPackage metaPackage, ValidationResult messages) {
    }

    public void preProcessClass(MetaClass clazz, ValidationResult messages) {
    }

    public void processClass(MetaClass clazz, ValidationResult messages) {
    }

    /**
   * Initializes the cartridges with the given properties. The XML cartridge
   * configuration file must be in the loader path of velocity in the directory
   * named with the prefix of the cartridge.
   * 
   * @param properties
   *          properties used to initialize the cartridge.
   */
    public void initialize(Properties properties) {
        this.properties = properties;
        configuration.addConfiguration(ConfigurationConverter.getConfiguration(properties));
        try {
            StringBuilder buffer = new StringBuilder();
            buffer.append(configuration.getProperty(RESSOURCE_FOLDER)).append("/").append(Platform.getPath(getPlatform())).append("/").append(getPrefix()).append("/").append("mappings.xml");
            configuration.addConfiguration(new XMLConfiguration(buffer.toString()));
            addDependencies();
            addUndefinedProperties();
        } catch (ConfigurationException e) {
            log.error("Configuration file not found", e);
        }
    }

    public List<Class> getClasses() {
        List<Class> classes = new ArrayList<Class>();
        StringBuilder buffer = new StringBuilder();
        buffer.append("configuration.tags.tag");
        for (int i = 0; ; i++) {
            String tag = PropertyUtilities.getAttributeAt(configuration, buffer, i, "name");
            if (tag == null) {
                break;
            }
            String context = PropertyUtilities.getAttributeAt(configuration, buffer, i, "context");
            Class clazz = convertContext(context);
            if (!classes.contains(clazz)) {
                classes.add(clazz);
            }
        }
        return classes;
    }

    public List<TagDefinition> getTaggedValueTypes(Class clazz) {
        List<TagDefinition> types = new ArrayList<TagDefinition>();
        StringBuilder buffer = new StringBuilder();
        buffer.append("configuration.tags.tag");
        for (int i = 0; ; i++) {
            String tag = PropertyUtilities.getAttributeAt(configuration, buffer, i, "name");
            if (tag == null) {
                break;
            }
            String type = PropertyUtilities.getAttributeAt(configuration, buffer, i, "type");
            String context = PropertyUtilities.getAttributeAt(configuration, buffer, i, "context");
            String defaultValue = PropertyUtilities.getAttributeAt(configuration, buffer, i, "default-value");
            if (clazz == convertContext(context)) {
                types.add(new TagDefinition(tag, defaultValue, convertType(type)));
            }
        }
        return types;
    }

    public List<TagDefinition> getAllTaggedValueTypes() {
        List<TagDefinition> types = new ArrayList<TagDefinition>();
        StringBuilder buffer = new StringBuilder();
        buffer.append("configuration.tags.tag");
        for (int i = 0; ; i++) {
            String tag = PropertyUtilities.getAttributeAt(configuration, buffer, i, "name");
            if (tag == null) {
                break;
            }
            String type = PropertyUtilities.getAttributeAt(configuration, buffer, i, "type");
            String defaultValue = PropertyUtilities.getAttributeAt(configuration, buffer, i, "default-value");
            types.add(new TagDefinition(tag, defaultValue, convertType(type)));
        }
        return types;
    }

    public List<TagDefinition> getObsoleteTaggedValueTypes(Class clazz) {
        List<TagDefinition> types = new ArrayList<TagDefinition>();
        StringBuilder buffer = new StringBuilder();
        buffer.append("configuration.obsolete-tags.obsolete-tag");
        for (int i = 0; ; i++) {
            String tag = PropertyUtilities.getAttributeAt(configuration, buffer, i, "name");
            if (tag == null) {
                break;
            }
            String type = PropertyUtilities.getAttributeAt(configuration, buffer, i, "type");
            String context = PropertyUtilities.getAttributeAt(configuration, buffer, i, "context");
            if (clazz == convertContext(context)) {
                types.add(new TagDefinition(tag, convertType(type)));
            }
        }
        return types;
    }

    public Properties getContext() {
        return properties;
    }

    /**
   * Returns the configuration of cartridge.
   * 
   * @return the properties of the configuration of the cartridge.
   */
    public CompositeConfiguration getConfiguration() {
        return configuration;
    }

    /**
   * Adds a cartridge being used in the cartridge to create the context.
   * 
   * @param cartridge
   *          cartridge being used.
   * @pre cartridge != null
   */
    protected void addCartridge(Cartridge cartridge) {
        cartridges.add(cartridge);
    }

    /**
   * Process a velocity template for a Java artifact.
   * 
   * @param root
   *          root directory where the artifact should be stored.
   * @param packageName
   *          qualified package name of the artifact.
   * @param classname
   *          name of the file containing the artifact.
   * @param template
   *          velocity template to execute.
   * @param context
   *          velocity context of the template.
   * @param messages
   *          ValidationResults is the container where additional messages for
   *          the user are added.
   * @throws IOException
   *           An exception occured when trying to create the file.
   * @pre (root != null) && (packageName != null) && (classname != null) &&
   *      (template != null) && (context != null)
   */
    protected void processJavaTemplate(File root, String packageName, String classname, Template template, VelocityContext context, ValidationResult messages) throws IOException {
        processTemplate(root, packageName, classname + JAVA_EXTENSION, template, context, messages);
    }

    /**
   * Processes a velocity template for an artifact. The new artifact is only
   * created if it is different from the old one to minimize spurious updates.
   * 
   * @param root
   *          root directory where the artifact should be stored.
   * @param packageName
   *          qualified package name of the artifact.
   * @param filename
   *          name of the file containing the artifact.
   * @param template
   *          velocity template to execute.
   * @param context
   *          velocity context of the template.
   * @param messages
   *          ValidationResults is the container where additional messages for
   *          the user are added.
   * @throws IOException
   *           An exception occured when trying to create the file.
   * @pre (root != null) && (packageName != null) && (filename != null) &&
   *      (template != null) && (context != null)
   */
    protected void processTemplate(File root, String packageName, String filename, Template template, VelocityContext context, ValidationResult messages) throws IOException {
        assert (root != null) && (packageName != null) && (filename != null) && (template != null) && (context != null);
        File tempFile = File.createTempFile("pmMDA", null, null);
        tempFile.deleteOnExit();
        PrintWriter writer = new PrintWriter(new BufferedOutputStream(new FileOutputStream(tempFile)));
        writer.print(processTemplate(template, context, messages));
        writer.close();
        File file = FileOperators.createFile(root, packageName, filename);
        logTemplateProcessing(this, template, file, "{0} Catridge processing template {1} and generating file: {2}");
        String messageText = "{0} Catridge processing template {1} and generating file: {2}";
        if (!FileOperators.equals(tempFile, file)) {
            System.gc();
            file.delete();
            if (log.isInfoEnabled()) {
                log.info("writing file with new artifact content " + file.getCanonicalPath());
            }
            FileUtils.copyFile(tempFile, file);
        } else {
            messageText += " - output file has not been changed.";
        }
        Object[] arguments = { this.getPrefix(), template.getName(), file.getAbsoluteFile() };
        SimpleValidationMessage message = new SimpleValidationMessage(MessageFormat.format(messageText, arguments));
        messages.add(message);
        System.gc();
        tempFile.delete();
    }

    /**
   * Creates a print writer for a Java class artifact.
   * 
   * @param root
   *          root directory where the artifact should be stored.
   * @param packageName
   *          qualified package name of the artifact.
   * @param classname
   *          name of the Java class.
   * @return the requested print writer.
   * @throws IOException
   *           An exception occured when trying to create the file.
   * @pre (root != null) && (packageName != null) && (classname != null)
   */
    protected PrintWriter createJavaWriter(File root, String packageName, String classname) throws IOException {
        return FileOperators.createWriter(root, packageName, classname + JAVA_EXTENSION);
    }

    /**
   * Retrieves the template with the given name and language extension.
   * 
   * @param name
   *          name of the template to retrieve.
   * @param language
   *          language handled in the template.
   * @return the requested template if found otherwise null.
   * @pre (name != null) && (language != null)
   */
    protected Template retrieveTemplate(String name, String language) {
        Template template = null;
        StringBuilder buffer = new StringBuilder();
        if (getPlatform() != null) {
            buffer.append(Platform.getPath(getPlatform()) + "/");
        }
        buffer.append(getPrefix()).append("/").append(name.toLowerCase());
        if (language != null) {
            buffer.append("-").append(language);
        }
        buffer.append(".vm");
        try {
            template = Velocity.getTemplate(buffer.toString());
        } catch (ParseErrorException e) {
            log.error(e);
        } catch (ResourceNotFoundException e) {
            log.error(e);
        } catch (Exception e) {
            log.error(e);
        }
        return template;
    }

    protected Template retrieveTemplate(String name, String language, String error) {
        Template template = null;
        try {
            template = retrieveTemplate(name, null);
        } catch (Exception e) {
            log.fatal(error, e);
        }
        return template;
    }

    /**
   * Processes the given velocity template with the given context.
   * 
   * @param template
   *          template to process.
   * @param context
   *          context of the template to process.
   * @param messages
   *          ValidationResults is the container where additional messages for
   *          the user are added.
   * @return the result of the template processing as string.
   * @pre template != null && context != null
   */
    protected String processTemplate(Template template, VelocityContext context, ValidationResult messages) {
        assert (template != null) && (context != null);
        StringWriter writer = new StringWriter();
        try {
            template.merge(context, writer);
        } catch (MethodInvocationException e) {
            log.error(e);
        } catch (ParseErrorException e) {
            log.error(e);
        } catch (ResourceNotFoundException e) {
            log.error(e);
        } catch (Exception e) {
            log.error(e);
        }
        return writer.toString();
    }

    /**
   * Creates the context for a model.
   * 
   * @param model
   *          model which context should be created.
   * @return the requested velocity context.
   */
    protected VelocityContext createContext(MetaModel model) {
        VelocityContext context = new VelocityContext();
        context.put(UTILITIES_KEY, modelOperators);
        context.put(CARTRIDGE_KEY, this);
        context.put(CONFIGURATION_KEY, getContext());
        context.put(MODEL_KEY, model);
        return context;
    }

    /**
   * Creates the context for a package.
   * 
   * @param metaPackage
   *          package to process.
   * @return the requested velocity context.
   */
    protected VelocityContext createContext(MetaPackage metaPackage) {
        VelocityContext context = new VelocityContext();
        context.put(UTILITIES_KEY, modelOperators);
        context.put(CARTRIDGE_KEY, this);
        context.put(CONFIGURATION_KEY, getContext());
        context.put(MODEL_KEY, metaPackage.getModel());
        context.put(PACKAGE_KEY, metaPackage);
        context.put(CLASSES_KEY, metaPackage.getClasses());
        return context;
    }

    /**
   * Generates the context for a class, all its properties, imports, and own
   * context. All properties are eligible.
   * 
   * @param clazz
   *          class to process.
   * @return velocity velocity context for the meta class.
   */
    protected VelocityContext createContext(MetaClass clazz) {
        VelocityContext context = new VelocityContext();
        context.put(UTILITIES_KEY, modelOperators);
        context.put(CARTRIDGE_KEY, this);
        context.put(CONFIGURATION_KEY, getContext());
        context.put(MODEL_KEY, clazz.getModel());
        context.put(PACKAGE_KEY, clazz.getContext());
        context.put(CLASS_KEY, clazz);
        context.put(PROPERTIES_KEY, ModelOperators.getTransitiveFieldsFor(clazz));
        context.put(AGGREGATIONS_KEY, ModelOperators.getTransitiveIndexedFieldsFor(clazz));
        context.put(NESTEDCLASSES_KEY, clazz.getNestedClasses());
        return context;
    }

    /**
   * Expands an identifier with its prefix and postfix.
   * 
   * @param prefixKey
   *          key of the prefix defined in the context.
   * @param identifier
   *          identifier to expand.
   * @param postfixKey
   *          key of the prefix defined in the context.
   * @return expanded identifier.
   */
    protected String expandIdentifier(String prefixKey, String identifier, String postfixKey) {
        Properties context = getContext();
        StringBuilder buffer = new StringBuilder();
        if (prefixKey != null) {
            buffer.append(context.getProperty(prefixKey));
        }
        buffer.append(identifier);
        if (postfixKey != null) {
            buffer.append(context.getProperty(postfixKey));
        }
        return buffer.toString();
    }

    /**
   * Returns the mapping for the type to the target language. A mapping exists
   * only for datatypes.
   * 
   * @param type
   *          type to map to a language construct.
   * @param language
   *          language to map to. A cartridge can define multiple mappings of
   *          datatypes and model types to target programming language.
   * @param concrete
   *          flag indicating if the implementation class should be retrieve or
   *          not.
   * @return the requested mapping if found otherwise the qualified name of the
   *         type.
   * @pre (type != null) && (language != null)
   */
    public String translateDatatype(MetaDatatype type, String language, boolean concrete) {
        assert (type != null) && (language != null);
        String typename = type.getQualifiedName();
        return PropertyUtilities.getInfoOfType(configuration, typename, language, null, "type", concrete);
    }

    /**
   * Returns the mapping for the type to the target language. A mapping exists
   * only for datatypes.
   * 
   * @param type
   *          type to map to a language construct.
   * @param language
   *          language to map to. A cartridge can define multiple mappings of
   *          datatypes and model types to target programming language.
   * @param dialect
   *          of language (e.g. "mssqlserver" for "sql" language).
   * @param concrete
   *          flag indicating if the implementation class should be retrieve or
   *          not.
   * @return the requested mapping if found otherwise the qualified name of the
   *         type.
   * @pre (type != null) && (language != null)
   */
    public String translateDatatype(MetaDatatype type, String language, String dialect, boolean concrete) {
        String typename = type.getQualifiedName();
        return PropertyUtilities.getInfoOfType(configuration, typename, language, dialect, "type", concrete);
    }

    /**
   * Adds all properties defined in the configuration file of the cartridge and
   * not already defined in the application property file.
   */
    private void addUndefinedProperties() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("configuration.properties.property");
        for (int i = 0; ; i++) {
            String property = PropertyUtilities.getAttributeAt(configuration, buffer, i, "name");
            if (property == null) {
                break;
            }
            String value = PropertyUtilities.getAttributeAt(configuration, buffer, i, "default-value");
            String propertyValue = properties.getProperty(property);
            if (propertyValue == null) {
                properties.setProperty(property, value);
            }
        }
    }

    private void addDependencies() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("configuration.uses.cartridge");
        CartridgeFactory factory = CartridgeFactory.getInstance();
        for (int i = 0; ; i++) {
            String prefix = PropertyUtilities.getAttributeAt(configuration, buffer, i, "name");
            if (prefix == null) {
                break;
            }
            addCartridge(factory.getCartridge(prefix));
        }
    }

    /**
   * Converts the mapping type to the class representing it.
   * 
   * @param type
   *          type to convert.
   * @return the class representing the type if found otherwise null.
   * @pre type != null
   */
    private Class convertType(String type) {
        Class clazz = null;
        if (ReflectUtilities.PRIMITIVES_TYPES.containsKey(type)) {
            clazz = ReflectUtilities.PRIMITIVES_TYPES.get(type);
        } else {
            try {
                clazz = Class.forName(type);
            } catch (ClassNotFoundException e) {
                log.error("Class not found for name " + type, e);
            }
        }
        assert (clazz != null);
        return clazz;
    }

    /**
   * Converts the mapping context to the class representing it
   * 
   * @param context
   *          context to convert.
   * @return the class representing the context if defined otherwise null.
   * @pre context != null
   */
    private Class convertContext(String context) {
        Class clazz = null;
        if ("model".equals(context)) {
            clazz = MetaModel.class;
        } else if ("package".equals(context)) {
            clazz = MetaPackage.class;
        } else if ("class".equals(context)) {
            clazz = MetaClass.class;
        } else if ("view".equals(context)) {
            clazz = MetaView.class;
        } else if ("property".equals(context)) {
            clazz = MetaProperty.class;
        } else if ("indexed-property".equals(context)) {
            clazz = MetaIndexedProperty.class;
        }
        return clazz;
    }

    /**
   * Returns true if the property should be generated, meaning that the
   * persistence tag is available and true.
   * 
   * @param metaProperty
   *          property which persistency flag should be checked.
   * @return true if the element should be generated otherwise false.
   */
    protected boolean isPersistent(MetaProperty metaProperty) {
        TaggedValue tv = metaProperty.getTaggedValue(UmlConstants.PERSISTENCE_TAG);
        if (tv != null && tv.getValue().equalsIgnoreCase("true")) {
            return true;
        }
        tv = metaProperty.getTaggedValue(COLUMN_NAME);
        if (tv != null && tv.getValue() != null && !tv.getValue().equals("")) {
            return true;
        }
        return false;
    }

    /**
   * Returns the root file defined in the property with the given key.
   * 
   * @param key
   *          key of the property containing the root directory.
   * @param defaultKey
   *          default key of the property containing the root directory.
   * @return The root file if defined otherwise null.
   */
    protected final File getRoot(String key, String defaultKey) {
        Properties properties = getContext();
        String value = properties.getProperty(key);
        File root;
        if ((value == null) || (value.trim().length() == 0)) {
            value = properties.getProperty(defaultKey);
        }
        if ((value == null) || (value.trim().length() == 0)) {
            root = null;
        } else {
            root = mkdirs(value);
        }
        return root;
    }

    /**
   * Opens the directory file with the given name. If necessary creates the
   * necessary directories if the directory does not exist in the file system
   * 
   * @param directory
   *          name of the directory to create
   * @return the requested directory as file.
   */
    protected File mkdirs(String directory) {
        File dir = new File(directory);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    protected boolean getBooleanProperty(String property) {
        return "true".equalsIgnoreCase(properties.getProperty(property));
    }

    /**
   * Logs information about processing a template.
   * 
   * @param cartridge
   *          cartridge being applied
   * @param template
   *          template being processed
   * @param file
   *          file beeing generated
   * @param message
   *          message to log
   */
    private void logTemplateProcessing(Cartridge cartridge, Template template, File file, String message) {
        if (log.isInfoEnabled()) {
            Object[] arguments = { cartridge.getPrefix(), template.getName(), file.getAbsoluteFile() };
            log.info(MessageFormat.format(message, arguments));
        }
    }
}
