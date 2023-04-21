package deduced.javagenerator.model.factory;

import java.util.Iterator;
import java.util.List;
import assertion.AssertUtility;
import deduced.PropertyInstance;
import deduced.generator.*;
import deduced.javaconverter.*;
import deduced.javagenerator.*;
import deduced.javagenerator.codeblock.MemberMethod;
import deduced.javagenerator.implementation.MethodParameterImplementation;
import deduced.javagenerator.model.JavaModelConstants;

/**
 * <p>
 * Title: JavaModelStructureGenerator
 * </p>
 * <p>
 * Description: JavaModelStructureGenerator
 * </p>
 */
public class JavaFactoryFiller extends GeneratorStep {

    private JavaNamingParameters _namingParameters;

    public JavaFactoryFiller() {
    }

    /**
     * (non-Javadoc)
     * 
     * @see deduced.generator.GeneratorStep#generateStep()
     */
    public void generateStep() {
        callFillCreatedObject();
    }

    /**
     * @return Returns the namingParameters.
     */
    public JavaNamingParameters getNamingParameters() {
        return _namingParameters;
    }

    /**
     * @param namingParameters
     *            The namingParameters to set.
     */
    public void setNamingParameters(JavaNamingParameters namingParameters) {
        _namingParameters = namingParameters;
    }

    public void fillCreatedObject(ObjectConverter converter, Object key, Object value, PropertyInstance instance) {
        if (!fillModelFramework(converter, value, instance)) {
            AssertUtility.fail("Failed to fill object: " + instance.getName());
        }
    }

    /**
     * @param converter
     * @param value
     * @param instance
     * @return
     */
    private boolean fillModelFramework(ObjectConverter converter, Object value, PropertyInstance instance) {
        Object sourceObject = converter.getSourceObject();
        if (instance == JavaModelConverter.INTERNAL_LOAD_FRAMEWORK_METHOD_INSTANCE.getPropertyInstance()) {
            convertInternalLoadFrameworkMethod((ProjectData) sourceObject, (JavaMethod) value);
        } else if (instance == JavaModelConverter.FACTORY_SINGLETON_MEMBER_INSTANCE.getPropertyInstance()) {
            convertFactorySingletonMember((ProjectData) sourceObject, (JavaMember) value);
        } else if (instance == JavaModelConverter.FACTORY_GET_INSTANCE_METHOD_INSTANCE.getPropertyInstance()) {
            convertFactoryGetInstanceMethod((ProjectData) sourceObject, (MemberMethod) value);
        } else if (instance == JavaModelConverter.FACTORY_CLASS_INSTANCE.getPropertyInstance()) {
            convertFactoryClass((ProjectData) sourceObject, (JavaClassTypeData) value);
        } else if (instance == JavaModelConverter.CONFIGURE_FACTORY_METHOD_INSTANCE.getPropertyInstance()) {
            convertConfigureFactoryMethod((JavaMethod) value);
        } else {
            return false;
        }
        return true;
    }

    /**
     * @param method
     */
    private void convertConfigureFactoryMethod(JavaMethod method) {
        method.setAccessLevel(JavaAccessLevel.PUBLIC.getLiteral());
        method.setIsStatic(true);
        method.setName(getNamingParameters().getMethodNameAdjuster().adjustString("configure factory"));
        method.setReturnType(BasicJavaTypes.VOID_TYPE);
        method.setStereotype(JavaModelConstants.MODEL_FACTORY_CONFIGURATION_METHOD_STEREOTYPE);
        MethodParameter param = (MethodParameter) method.getParameterList().addPropertyInstance(null, null, new MethodParameterImplementation());
        param.setName(getNamingParameters().getVariableNameAdjuster().adjustString("factory"));
        param.setParameterType(BasicJavaTypeControllers.CONTROLLER_FACTORY);
    }

    /**
     * @param data
     * @param javaClass
     */
    private void convertFactoryClass(ProjectData data, JavaClassTypeData javaClass) {
        javaClass.setIsFinal(true);
        javaClass.setAccessLevel(JavaAccessLevel.PUBLIC.getLiteral());
        javaClass.setName(getNamingParameters().getClassNameAdjuster().adjustString(data.getName() + " model factory configuration"));
        javaClass.setParentClass(BasicJavaTypeControllers.FRAMEWORK_LOADER_TYPE);
    }

    /**
     * @param data
     * @param method
     */
    private void convertFactoryGetInstanceMethod(ProjectData data, MemberMethod method) {
        method.setAccessLevel(JavaAccessLevel.PUBLIC.getLiteral());
        method.setIsStatic(true);
        method.setName(getNamingParameters().getMethodNameAdjuster().adjustString("get instance"));
        method.setReturnType((JavaTypeData) findConvertedObject(data, JavaModelConverter.FACTORY_CLASS_INSTANCE.getKey()));
        method.setStereotype(JavaCodeBlockConstants.GETTER_METHOD_CODE_BLOCK);
        JavaMember member = (JavaMember) findConvertedObject(data, JavaModelConverter.FACTORY_SINGLETON_MEMBER_INSTANCE.getKey());
        method.setMember(member);
    }

    /**
     * @param data
     * @param member
     */
    private void convertFactorySingletonMember(ProjectData data, JavaMember member) {
        member.setAccessLevel(JavaAccessLevel.PUBLIC.getLiteral());
        member.setIsStatic(true);
        member.setName(getNamingParameters().getStaticNameAdjuster().adjustString("instance"));
        member.setTypeData((JavaTypeData) findConvertedObject(data, JavaModelConverter.FACTORY_CLASS_INSTANCE.getKey()));
        member.setStereotype(JavaCodeBlockConstants.INITIALIZED_MEMBER_CODE_BLOCK);
    }

    /**
     * @param data
     * @param method
     */
    private void convertInternalLoadFrameworkMethod(ProjectData data, JavaMethod method) {
        method.setAccessLevel(JavaAccessLevel.PUBLIC.getLiteral());
        method.setName(getNamingParameters().getMethodNameAdjuster().adjustString("internal load framework"));
        method.setReturnType(BasicJavaTypes.VOID_TYPE);
        method.setStereotype(JavaModelConstants.MODEL_FACTORY_LOAD_FRAMEWORK_PARAMETER_METHOD_STEREOTYPE);
        MethodParameter param = (MethodParameter) method.getParameterList().addPropertyInstance(null, null, new MethodParameterImplementation());
        param.setName(getNamingParameters().getVariableNameAdjuster().adjustString("deducedFramework"));
        param.setParameterType(BasicJavaTypeControllers.DEDUCED_FRAMEWORK_CONTROLLER_TYPE);
        addLoadFrameworkPackageRequiredTypes(method, data);
    }

    /**
     * @param method
     * @param javaPackage
     */
    private void addLoadFrameworkPackageRequiredTypes(JavaMethod method, ProjectData data) {
        JavaGeneratorUtility.addRequiredType(method, BasicJavaTypeControllers.PROPERTY_PACKAGE_CONTROLLER_TYPE);
        List classList = ModelUtility.getClassList(data);
        Iterator typeIt = classList.iterator();
        while (typeIt.hasNext()) {
            ClassTypeData subClass = (ClassTypeData) typeIt.next();
            addLoadFrameworkTypeRequiredTypes(method, subClass);
        }
    }

    /**
     * @param method
     * @param subClass
     */
    private void addLoadFrameworkTypeRequiredTypes(JavaMethod method, ClassTypeData subClass) {
        if (subClass instanceof EnumerationClassData) {
            JavaGeneratorUtility.addRequiredType(method, BasicJavaTypeControllers.ENUMERATION_PROPERTY_TYPE_CONTROLLER_TYPE);
        } else {
            JavaGeneratorUtility.addRequiredType(method, BasicJavaTypeControllers.PROPERTY_COLLECTION_TYPE_CONTROLLER_TYPE);
        }
        if (ModelUtility.classHasParents(subClass)) {
            JavaGeneratorUtility.addRequiredType(method, BasicJavaTypes.LIST_TYPE_DATA);
            JavaGeneratorUtility.addRequiredType(method, BasicJavaTypes.ARRAY_LIST_TYPE_DATA);
        }
        JavaTypeData implementationType = (JavaTypeData) findConvertedObject(subClass, JavaModelImplementationConverter.IMPLEMENTATION_TYPE_INSTANCE.getKey());
        JavaTypeData interfaceType = (JavaTypeData) findConvertedObject(subClass, JavaModelInterfaceConverter.INTERFACE_TYPE_INSTANCE.getKey());
        JavaGeneratorUtility.addRequiredType(method, implementationType);
        JavaGeneratorUtility.addRequiredType(method, interfaceType);
        Iterator it = subClass.getPropertyList().iteratorByValue();
        while (it.hasNext()) {
            InstanceData subInstance = (InstanceData) it.next();
            TypeData typeData = subInstance.getTypeData();
            if (typeData == null) {
                throw new GeneratorException("Failed to generate model, Instance \"" + subInstance.getName() + "\" of object \"" + subClass.getName() + "\" has no type.");
            }
            addInstanceRequiredType(method, subInstance);
        }
    }

    /**
     * @param instance
     * @param codeBlock
     */
    public void addInstanceRequiredType(JavaMethod method, InstanceData instance) {
        if (instance.getCardinality() == InstanceCardinality.LIST.getLiteral()) {
            JavaGeneratorUtility.addRequiredType(method, BasicJavaTypes.PROPERTY_LIST_TYPE);
        } else if (instance.getCardinality() == InstanceCardinality.MAP.getLiteral()) {
            JavaGeneratorUtility.addRequiredType(method, BasicJavaTypes.PROPERTY_MAP_TYPE);
        }
        TypeData typeData = instance.getTypeData();
        JavaMember typeMember = (JavaMember) findConvertedObject(typeData, JavaModelInterfaceConverter.TYPE_MEMBER_INSTANCE.getKey());
        JavaGeneratorUtility.addRequiredType(method, (JavaTypeData) typeMember.getParent().getParent());
    }
}
