package ch.bbv.mda.cartridges.dotNet;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import ch.bbv.application.VersionId;
import ch.bbv.mda.MetaClass;
import ch.bbv.mda.MetaElement;
import ch.bbv.mda.MetaIndexedProperty;
import ch.bbv.mda.MetaModel;
import ch.bbv.mda.MetaPackage;
import ch.bbv.mda.MetaProperty;
import ch.bbv.mda.cartridges.BoConstants;
import ch.bbv.mda.cartridges.dotNet.validation.RulesFactory;
import ch.bbv.mda.generators.Cartridge;
import ch.bbv.mda.generators.CartridgeImplNet;
import ch.bbv.mda.operators.FileOperators;
import ch.bbv.mda.ui.TaggedValuesView;
import com.jgoodies.validation.ValidationResult;

/**
 * Cartridge which generates the CSharp source code for the pmMDA.NET framework.<br/>
 * Currently the following artifacts are supported:
 * <ul>
 * <li> The data object with its constructors, fields and properties is
 * generated. The code is well indented and fully documented following the <a
 * href="http://msdn.microsoft.com/library/default.asp?url=/library/en-us/csref/html/vcorixmldocumentation.asp">
 * xml documentation conventions</a>. </li>
 * <li> A property is identified as a reference code if it implements the
 * reference code or hierarchical reference code interface. </li>
 * <li> A lightweight representation of each data object having the lightweight
 * flag set is generated. Lighweight objects are used for navigation and display
 * purposes when it would be too expensive to send the complete graph of data
 * object instances. Typical usage is displaying a navigation tree in the user
 * interface. </li>
 * <li> Helper classes are generated to provide a complete implementation of the
 * visitor pattern on all data object classes defined in the package. The
 * generated visitor can visit data objects defined in other packages but do not
 * generate specialized methods for their descendents. </li>
 * </ul>
 * 
 * @author Mikkala Pedersen
 */
public class DogNetCartridge extends CartridgeImplNet implements Cartridge, NetCartridgeConstants {

    public static final String COLLECTION_TYPE_TAG = ".net-collection-type";

    public static final String IGNORE_ELEMENT_TAG = ".net-ignore";

    /**
   * Attribute key. The value specifies whether a property or indexed property
   * is an element of the lightwight object
   */
    public static final String LIGHTWEIGHT_ATTRIBUTE = ".net-lightweight";

    /**
   * Attribute key. The value specifies the name of the package used for
   * interfaces
   */
    public static final String INTERFACES_PACKAGE_ATTRIBUTE = ".net-interfaces-package";

    /**
   * Attribute key. The value specifies the package name used for data objects.
   */
    public static final String DATA_OBJECTS_PACKAGE_ATTRIBUTE = ".net-data-object-package";

    /**
   * Attribute key. The value specifies the package name used for lightweight
   * objects.
   */
    public static final String LIGHTWEIGHT_OBJECTS_PACKAGE_ATTRIBUTE = ".net-lightweight-object-package";

    /**
   * Attribute key. The value specifies whether a property or indexed property
   * is an element of the lightweight object.
   */
    public static final String LIGHTWEIGHT_OBJECT_ATTRIBUTE = ".net-lightweight-object";

    /**
   * Attribute key. The value specifies the interface name of a data object.
   */
    public static final String INTERFACE_CLASS_NAME_ATTRIBUTE = ".net-interface-name";

    /**
   * Attribute key. The value specifies the data object class name of a data
   * object.
   */
    public static final String DATA_OBJECT_CLASS_NAME_ATTRIBUTE = ".net-data-object-class";

    /**
   * Attribute key. The value specifies the lightweight class name of a data
   * object.
   */
    public static final String LIGHTWEIGHT_OBJECT_CLASS_NAME_ATTRIBUTE = ".net-lightweight-object-class";

    /**
   * The name of the .NET cartridge
   */
    public static final String PREFIX = "DotNetDog";

    public static final String CSHARP_EXTENSION = ".cs";

    private static Log log = LogFactory.getLog(DogNetCartridge.class);

    /**
   * Root directory of the generated source files.
   */
    private File sourceRootDirectory;

    private Template templateDogNETBoClasses;

    private Template templateDogNETLwClasses;

    private Template templateDogNETLwVisitorInterface;

    private Template templateDogNETLwVisitorClass;

    private Template templateDogNETReferenceCode;

    private Template templateDogNETBoMetaData;

    /**
   * Meta package which is currently processed.
   */
    private MetaPackage metaPackage;

    /**
   * Meta model which is currently processed.
   */
    private MetaModel metaModel;

    /**
   * Instances a new insatnce of the DogNetCartridge class.
   */
    public DogNetCartridge() {
        super("Dog.NET Cartridge", PREFIX, new VersionId(0, 1, 0));
    }

    /**
   * Sets the cartridge context for the cartridge. The context contains all user
   * specific configuration values.
   * 
   * @param properties
   *          new cartridge context properties.
   * @pre (properties != null)
   */
    public void initialize(Properties properties) {
        super.initialize(properties);
        TaggedValuesView.registerReferenceCode(CollectionTypeCode.class, CollectionTypeCode.getCodes(), false, false);
        (new RulesFactory()).registerAllRules();
        templateDogNETBoClasses = retrieveTemplate(DOG_NET_DO_VM, null, "Could not find the velocity template for the data object classes generation.");
        templateDogNETLwClasses = retrieveTemplate(DOG_NET_LW_VM, null, "Could not find the velocity template for the lightweight data object classes generation.");
        templateDogNETLwVisitorInterface = retrieveTemplate(DOG_NET_LW_VISITOR_INTERFACE_VM, null, "Could not find the velocity template for the lightweight visitor interface generation.");
        templateDogNETLwVisitorClass = retrieveTemplate(DOG_NET_LW_VISITOR_CLASS_VM, null, "Could not find the velocity template for the lightweight visitor class generation.");
        templateDogNETReferenceCode = retrieveTemplate(DOG_NET_CODES_VM, null, "Could not find the velocity template for the reference code generation.");
        templateDogNETBoMetaData = retrieveTemplate(DOG_NET_DO_FACTORY_VM, null, "Could not find the velocity template for the data object meta data code generation.");
    }

    /**
   * Preprocesses the project and computes all the attributes used in the
   * cartridge for the model. The context of the model is also computed.
   * 
   * @param model
   *          MDA model to process.
   * @param messages
   *          ValidationResults is the container where additional messages for
   *          the user are added.
   * @pre model != null
   */
    @Override
    public void preProcessModel(MetaModel model, ValidationResult messages) {
        super.preProcessModel(model, messages);
        this.metaModel = model;
    }

    /**
   * Preprocesses the package and computes all the attributes used in the
   * cartridge for the package. The context of the package is also computed.
   * 
   * @param metaPackage
   *          MDA package to process.
   * @param messages
   *          ValidationResults is the container where additional messages for
   *          the user are added.
   * @pre metaPackage != null
   */
    @Override
    public void preProcessPackage(MetaPackage metaPackage, ValidationResult messages) {
        if (IsIgnored(metaPackage)) {
            return;
        }
        Properties context = getContext();
        StringBuffer buffer = new StringBuffer();
        String qualifiedName = metaPackage.getQualifiedName();
        buffer.append(qualifiedName).append(context.getProperty(INTERFACE_PACKAGE_POSTFIX_KEY));
        metaPackage.addAttribute(INTERFACES_PACKAGE_ATTRIBUTE, buffer.toString());
        buffer.setLength(0);
        buffer.append(qualifiedName).append(context.getProperty(DATA_OBJECTS_PACKAGE_POSTFIX_KEY));
        metaPackage.addAttribute(DATA_OBJECTS_PACKAGE_ATTRIBUTE, buffer.toString());
        buffer.setLength(0);
        buffer.append(qualifiedName).append(context.getProperty(LIGHTWEIGHT_OBJECTS_PACKAGE_POSTFIX_KEY));
        metaPackage.addAttribute(LIGHTWEIGHT_OBJECTS_PACKAGE_ATTRIBUTE, buffer.toString());
        metaPackage.addAttribute(LIGHTWEIGHT_OBJECT_ATTRIBUTE, Boolean.toString(isLightweight(metaPackage)));
        super.preProcessPackage(metaPackage, messages);
    }

    /**
   * Preprocesses the class and computes all the attributes used in the
   * cartridge for the class. The context of the class is also computed.
   * 
   * @param clazz
   *          MDA class to process.
   * @param messages
   *          ValidationResults is the container where additional messages for
   *          the user are added.
   * @pre clazz != null
   */
    @Override
    public void preProcessClass(MetaClass clazz, ValidationResult messages) {
        if (IsIgnored(clazz)) {
            return;
        }
        Properties context = getContext();
        String name = clazz.getName();
        StringBuffer buffer = new StringBuffer();
        buffer.append(context.getProperty(INTERFACE_CLASS_PREFIX_KEY)).append(name).append(context.getProperty(INTERFACE_CLASS_POSTFIX_KEY));
        clazz.addAttribute(INTERFACE_CLASS_NAME_ATTRIBUTE, buffer.toString());
        buffer.setLength(0);
        buffer.append(context.getProperty(DATA_OBJECTS_CLASS_PREFIX_KEY)).append(name).append(context.getProperty(DATA_OBJECTS_CLASS_POSTFIX_KEY));
        clazz.addAttribute(DATA_OBJECT_CLASS_NAME_ATTRIBUTE, buffer.toString());
        buffer.setLength(0);
        buffer.append(context.getProperty(LIGHTWEIGHT_OBJECTS_CLASS_PREFIX_KEY)).append(name).append(context.getProperty(LIGHTWEIGHT_OBJECTS_CLASS_POSTFIX_KEY));
        clazz.addAttribute(LIGHTWEIGHT_OBJECT_CLASS_NAME_ATTRIBUTE, buffer.toString());
        boolean lightweightClass = false;
        if (isLightweight(clazz)) {
            lightweightClass = true;
        }
        clazz.addAttribute(LIGHTWEIGHT_ATTRIBUTE, Boolean.toString(lightweightClass));
        for (MetaProperty property : clazz.getProperties()) {
            property.addAttribute(LIGHTWEIGHT_ATTRIBUTE, property.valueOfTag(BoConstants.BO_LIGHTWEIGHT));
        }
        for (MetaIndexedProperty indexedProperty : clazz.getIndexedProperties()) {
            indexedProperty.addAttribute(LIGHTWEIGHT_ATTRIBUTE, indexedProperty.valueOfTag(BoConstants.BO_LIGHTWEIGHT));
        }
        super.preProcessClass(clazz, messages);
    }

    /**
   * Processes the model. The context contains the project information. Makes
   * sure the output directory exists.
   * 
   * @param model
   *          MDA model to process.
   * @param messages
   *          ValidationResults is the container where additional messages for
   *          the user are added.
   * @pre model != null
   */
    @Override
    public void processModel(MetaModel model, ValidationResult messages) {
        sourceRootDirectory = new File(getContext().getProperty(ROOT_FOLDER_KEY));
        if (!sourceRootDirectory.exists()) {
            sourceRootDirectory.mkdirs();
        }
        File metaDataDirectory = new File(getContext().getProperty(META_DATA_ROOT_FOLDER_KEY));
        if (!metaDataDirectory.exists()) {
            metaDataDirectory.mkdirs();
        }
        VelocityContext context = createContext(model);
        try {
            processTemplate(metaDataDirectory, null, "MetaDataObjectFactory" + CSHARP_EXTENSION, templateDogNETBoMetaData, context, messages);
        } catch (IOException ioe) {
            String msg = "Could not generate the data object meta data.";
            log.fatal(msg, ioe);
            messages.addError(msg);
        }
    }

    /**
   * Postprocesses the model. The context contains the project information.
   * 
   * @param model
   *          MDA model to process.
   * @param messages
   *          ValidationResults is the container where additional messages for
   *          the user are added.
   * @pre model != null
   */
    @Override
    public void postProcessModel(MetaModel model, ValidationResult messages) {
        this.metaModel = null;
        super.postProcessModel(model, messages);
    }

    /**
   * Processes the package. The context contains the project information.
   * 
   * @param metaPackage
   *          MDA package to process.
   * @param messages
   *          ValidationResults is the container where additional messages for
   *          the user are added.
   * @pre model != null
   */
    @Override
    public void processPackage(MetaPackage metaPackage, ValidationResult messages) {
        if (IsIgnored(metaPackage)) {
            return;
        }
        this.metaPackage = metaPackage;
        super.processPackage(metaPackage, messages);
    }

    /**
   * Postprocesses the given package. Generates the CSharp code for:
   * <ul>
   * <li>Intreface for Visitor which traverse the data objects.</li>
   * <li>Visitor class which traverse the data objects.</li>
   * <li>Type factory for registration.</li>
   * </ul>
   * 
   * @param metaPackage
   *          MDA package to process.
   * @param messages
   *          ValidationResults is the container where additional messages for
   *          the user are added.
   * @pre metaPackage != null
   */
    @Override
    public void postProcessPackage(MetaPackage metaPackage, ValidationResult messages) {
        if (IsIgnored(metaPackage)) {
            return;
        }
        boolean hasDataObjects = false;
        for (MetaClass clazz : metaPackage.getClasses()) {
            hasDataObjects = clazz.hasStereotype(BoConstants.DATA_OBJECT_STEREOTYPE);
            if (hasDataObjects) {
                break;
            }
        }
        if (hasDataObjects) {
            VelocityContext context = createContext(metaPackage);
            try {
                processTemplate(sourceRootDirectory, metaPackage.getAttribute(LIGHTWEIGHT_OBJECTS_PACKAGE_ATTRIBUTE), "ILwVisitor" + CSHARP_EXTENSION, templateDogNETLwVisitorInterface, context, messages);
            } catch (IOException ioe) {
                log.fatal("Could not generate the visitor interface for the lightweight objects.", ioe);
            }
            try {
                processTemplate(sourceRootDirectory, metaPackage.getAttribute(LIGHTWEIGHT_OBJECTS_PACKAGE_ATTRIBUTE), "LwVisitorFunctor" + CSHARP_EXTENSION, templateDogNETLwVisitorClass, context, messages);
            } catch (IOException ioe) {
                log.fatal("Could not generate the lightweight visitor functor class.", ioe);
            }
        }
    }

    /**
   * Processes the given class. Generates the CSharp code for:
   * <ul>
   * <li>Data object class</li>
   * </ul>
   * 
   * @param clazz
   *          MDA clas to process.
   * @param messages
   *          ValidationResults is the container where additional messages for
   *          the user are added.
   * @pre clazz != null
   */
    @Override
    public void processClass(MetaClass clazz, ValidationResult messages) {
        if (IsIgnored(clazz)) {
            return;
        }
        if (clazz.isNestedClass()) {
            return;
        }
        if (!clazz.hasStereotype(BoConstants.DATA_OBJECT_STEREOTYPE) && !clazz.hasStereotype(BoConstants.REFERENCE_CODE_STEREOTYPE)) {
            return;
        }
        generateDataObjectClass(clazz, messages);
        super.processClass(clazz, messages);
    }

    /**
   * Generates the velocity context for a class, all its properties, imports,
   * and own context. All properties are eligible.
   * 
   * @param clazz
   *          class to process.
   * @return velocity context for the meta class.
   */
    protected VelocityContext createContext(MetaClass clazz) {
        VelocityContext context = super.createContext(clazz);
        context.put("netHelper", new NetCartridgeHelper(this, this.metaModel));
        return context;
    }

    protected VelocityContext createContext(MetaModel model) {
        VelocityContext context = super.createContext(model);
        context.put("netHelper", new NetCartridgeHelper(this, this.metaModel));
        return context;
    }

    protected VelocityContext createContext(MetaPackage pack) {
        VelocityContext context = super.createContext(pack);
        context.put("netHelper", new NetCartridgeHelper(this, this.metaModel));
        return context;
    }

    private Map<String, List<String>> extractUserCode(MetaClass clazz) throws IOException {
        Map<String, List<String>> blocks = new HashMap<String, List<String>>();
        File source = FileOperators.createFile(sourceRootDirectory, clazz.retrievePackage().getQualifiedName(), clazz.getAttribute(DATA_OBJECT_CLASS_NAME_ATTRIBUTE) + CSHARP_EXTENSION);
        blocks.put(USING_BLOCK, FileOperators.extractSourceCode(source, USING_BLOCK_BEGIN, USING_BLOCK_END));
        blocks.put(DECLARATIONS_BLOCK, FileOperators.extractSourceCode(source, DECLARATIONS_BLOCK_BEGIN, DECLARATIONS_BLOCK_END));
        blocks.put(INITIALIZATIONS_BLOCK, FileOperators.extractSourceCode(source, INITIALIZATIONS_BLOCK_BEGIN, INITIALIZATIONS_BLOCK_END));
        blocks.put(SERIAL_INITIALIZATIONS_BLOCK, FileOperators.extractSourceCode(source, SERIAL_INITIALIZATIONS_BLOCK_BEGIN, SERIAL_INITIALIZATIONS_BLOCK_END));
        blocks.put(CONSTRUCTORS_BLOCK, FileOperators.extractSourceCode(source, CONSTRUCTORS_BLOCK_BEGIN, CONSTRUCTORS_BLOCK_END));
        blocks.put(DEFINITIONS_BLOCK, FileOperators.extractSourceCode(source, DEFINITIONS_BLOCK_BEGIN, DEFINITIONS_BLOCK_END));
        return blocks;
    }

    private void generateDataObjectClass(MetaClass clazz, ValidationResult messages) {
        VelocityContext context = createContext(clazz);
        try {
            Map<String, List<String>> blocks = extractUserCode(clazz);
            context.put(BoConstants.CODE_BLOCKS, blocks);
            if (clazz.hasStereotype(BoConstants.DATA_OBJECT_STEREOTYPE)) {
                processTemplate(sourceRootDirectory, metaPackage.getAttribute(DATA_OBJECTS_PACKAGE_ATTRIBUTE), clazz.getAttribute(DATA_OBJECT_CLASS_NAME_ATTRIBUTE) + CSHARP_EXTENSION, templateDogNETBoClasses, context, messages);
                if (isLightweight(clazz)) {
                    processTemplate(sourceRootDirectory, metaPackage.getAttribute(LIGHTWEIGHT_OBJECTS_PACKAGE_ATTRIBUTE), clazz.getAttribute(LIGHTWEIGHT_OBJECT_CLASS_NAME_ATTRIBUTE) + CSHARP_EXTENSION, templateDogNETLwClasses, context, messages);
                }
            } else if (clazz.hasStereotype(BoConstants.REFERENCE_CODE_STEREOTYPE)) {
                processTemplate(sourceRootDirectory, metaPackage.getAttribute(DATA_OBJECTS_PACKAGE_ATTRIBUTE), clazz.getAttribute(DATA_OBJECT_CLASS_NAME_ATTRIBUTE) + CSHARP_EXTENSION, templateDogNETReferenceCode, context, messages);
            }
        } catch (IOException ioe) {
            log.equals(ioe);
            ioe.printStackTrace();
        }
    }

    /**
   * Returns whether a meta class is ignored
   * 
   * @param metaClass
   *          The meta class to check
   * @return True if the meta class is ignored. Otherwise false.
   */
    public static boolean IsIgnored(MetaClass metaClass) {
        if (metaClass.booleanValueOfTag(IGNORE_ELEMENT_TAG)) {
            return true;
        }
        return IsIgnored(metaClass.retrievePackage());
    }

    /**
   * Returns whether a meta package is ignored
   * 
   * @param metaPackage
   *          The meta package to check.
   * @return True if the meta package is ignored. Otherwise false.
   */
    public static boolean IsIgnored(MetaPackage metaPackage) {
        if (metaPackage.booleanValueOfTag(IGNORE_ELEMENT_TAG)) {
            return true;
        }
        MetaElement context = metaPackage.getContext();
        if (context instanceof MetaPackage) {
            return IsIgnored((MetaPackage) context);
        }
        return false;
    }

    /**
   * Test whether a meta package contains lightweight classes
   * 
   * @param metaPackage
   *          The meta package to check for lightweight classes
   * @return True if the meta package contains at least one lightweight class.
   */
    private boolean isLightweight(MetaPackage metaPackage) {
        assert (metaPackage != null);
        for (MetaClass clazz : metaPackage.getClasses()) {
            if (isLightweight(clazz)) {
                return true;
            }
        }
        return false;
    }

    /**
   * Returns whether a class has its lightweight representation.<br/> The class
   * is lightweight if at least one of the following points is true: - an
   * attribute is lightweight - at least one lightweight class derives
   * (indirectly) from this class - at least one lightweight class references
   * this class (as property or indexed property)
   * 
   * @param clazz
   *          Class to check whether it has a lightweight representation.
   * @return true if the class has a lightweight representation. Otherweise
   *         false.
   */
    private boolean isLightweight(MetaClass clazz) {
        assert (clazz != null);
        for (MetaProperty property : clazz.getProperties()) {
            if (property.booleanValueOfTag(BoConstants.BO_LIGHTWEIGHT)) {
                return true;
            }
        }
        for (MetaIndexedProperty property : clazz.getIndexedProperties()) {
            if (property.booleanValueOfTag(BoConstants.BO_LIGHTWEIGHT)) {
                return true;
            }
        }
        MetaModel model = (MetaModel) clazz.getRoot();
        NetCartridgeHelper helper = new NetCartridgeHelper(this, model);
        List<MetaClass> derivedClasses = helper.getDerivedClasses(clazz);
        for (MetaClass derivedClass : derivedClasses) {
            if (isLightweight(derivedClass)) {
                return true;
            }
        }
        Collection referencingClasses = model.getClassReferences(clazz);
        for (Iterator i = referencingClasses.iterator(); i.hasNext(); ) {
            MetaClass referencedClass = (MetaClass) i.next();
            for (MetaProperty property : referencedClass.getProperties()) {
                MetaClass propertyClass = model.findClass(property.getTypeName());
                if (clazz.equals(propertyClass)) {
                    if (property.booleanValueOfTag(BoConstants.BO_LIGHTWEIGHT)) {
                        return true;
                    }
                }
            }
            for (MetaIndexedProperty property : referencedClass.getIndexedProperties()) {
                MetaClass propertyClass = model.findClass(property.getTypeName());
                if (clazz.equals(propertyClass)) {
                    if (property.booleanValueOfTag(BoConstants.BO_LIGHTWEIGHT)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
