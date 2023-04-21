package deduced.javagenerator.controller.interfacegen;

import java.util.Iterator;
import assertion.AssertUtility;
import deduced.PropertyInstance;
import deduced.controller.PropertyCollectionController;
import deduced.generator.*;
import deduced.javaconverter.*;
import deduced.javagenerator.*;
import deduced.javagenerator.controller.JavaControllerConstants;
import deduced.javagenerator.controller.list.interfacegen.JavaListControllerInterfaceFiller;
import deduced.javagenerator.implementation.MethodParameterImplementation;
import deduced.javagenerator.model.interfacegen.JavaModelInterfaceFiller;

/**
 * <p>
 * Title: JavaModelStructureGenerator
 * </p>
 * <p>
 * Description: JavaModelStructureGenerator
 * </p>
 */
public class JavaControllerInterfaceFiller extends JavaGeneratorStep {

    private JavaListControllerInterfaceFiller _listFiller;

    public JavaControllerInterfaceFiller() {
    }

    /**
     * (non-Javadoc)
     * 
     * @see deduced.generator.GeneratorStep#generateStep()
     */
    public void generateStep() {
        _listFiller = new JavaListControllerInterfaceFiller();
        _listFiller.setNamingParameters(getNamingParameters());
        _listFiller.setProjectGenerator(getProjectGenerator());
        callFillCreatedObject();
    }

    public void fillCreatedObject(ObjectConverter converter, Object key, Object value, PropertyInstance instance) {
        if (!fillControllerInterface(converter, value, instance) && !_listFiller.fillListControllerInterface(converter, value, instance)) {
            AssertUtility.fail("Failed to fill object: " + instance.getName());
        }
    }

    public boolean fillControllerInterface(ObjectConverter converter, Object value, PropertyInstance instance) {
        Object sourceObject = converter.getSourceObject();
        if (instance == JavaControllerInterfaceConverter.CHILD_CONTROLLER_GETTER_METHOD_INSTANCE.getPropertyInstance()) {
            convertChildControllerGetterMethod((InstanceData) sourceObject, (JavaMethod) value);
        } else if (instance == JavaControllerInterfaceConverter.INTERFACE_CLASS_INSTANCE.getPropertyInstance()) {
            convertInterfaceClass((ClassTypeData) sourceObject, (JavaClassTypeData) value);
        } else if (instance == JavaControllerInterfaceConverter.SETTER_METHOD_INSTANCE.getPropertyInstance()) {
            convertSetterMethod((InstanceData) sourceObject, (JavaMethod) value);
        } else if (instance == JavaControllerInterfaceConverter.TYPED_GETTER_METHOD_INSTANCE.getPropertyInstance()) {
            convertTypedGetterMethod((ClassTypeData) sourceObject, (JavaMethod) value);
        } else {
            return false;
        }
        return true;
    }

    /**
     * @param data
     * @param method
     */
    public void convertTypedGetterMethod(ClassTypeData data, JavaMethod method) {
        JavaClassTypeData javaClass = (JavaClassTypeData) findConvertedObject(data, JavaModelInterfaceConverter.INTERFACE_TYPE_INSTANCE.getKey());
        method.setName("getControlled" + javaClass.getName());
        method.setAccessLevel(JavaAccessLevel.PUBLIC.getLiteral());
        method.setReturnType(javaClass);
        method.setStereotype(JavaControllerConstants.CONTROLLER_TYPED_MODEL_GETTER_METHOD_STEREOTYPE);
    }

    /**
     * @param data
     * @param method
     */
    public void convertSetterMethod(InstanceData data, JavaMethod method) {
        String setterMethodName = JavaModelInterfaceFiller.getSetterMethodName(getNamingParameters(), data);
        String setterVariable = JavaModelInterfaceFiller.getSetterVariableName(getNamingParameters(), data);
        JavaTypeData setType = JavaGeneratorUtility.getInstanceJavaType(getProjectGenerator().getConversionModel(), data);
        JavaNativeTypeData nativeType = null;
        if (setType instanceof JavaNativeTypeData) {
            nativeType = (JavaNativeTypeData) setType;
        }
        JavaTypeData returnType = null;
        if (nativeType != null) {
            returnType = BasicJavaTypes.VOID_TYPE;
        } else {
            returnType = setType;
        }
        method.setName(setterMethodName);
        method.setReturnType(returnType);
        method.setAccessLevel(JavaAccessLevel.PUBLIC.getLiteral());
        method.setStereotype(JavaControllerConstants.CONTROLLER_PROPERTY_SETTER_METHOD_STEREOTYPE);
        MethodParameter setterParameter = (MethodParameter) method.getParameterList().addPropertyValue(new MethodParameterImplementation());
        setterParameter.setName(setterVariable);
        setterParameter.setParameterType(setType);
        if (ModelUtility.isOwnershipFlagAvailable(data)) {
            MethodParameter ownershipParameter = (MethodParameter) method.getParameterList().addPropertyValue(new MethodParameterImplementation());
            ownershipParameter.setName("isInstanceOwned");
            ownershipParameter.setParameterType(BasicJavaTypes.NATIVE_BOOLEAN_TYPE);
        }
    }

    /**
     * @param data
     * @param data2
     */
    public void convertInterfaceClass(ClassTypeData data, JavaClassTypeData javaClass) {
        String name = getNamingParameters().getClassNameAdjuster().adjustString(data.getName() + getNamingParameters().getControllerSuffix());
        javaClass.setName(name);
        javaClass.setIsInterface(true);
        javaClass.setAccessLevel(JavaAccessLevel.PUBLIC.getLiteral());
        if (data.getParentClassList().getSize() == 0) {
            javaClass.getInterfaceList().addPropertyValue(BasicJavaTypes.getJavaType(PropertyCollectionController.class));
        } else {
            Iterator it = data.getParentClassList().iteratorByValue();
            while (it.hasNext()) {
                ClassTypeData parent = (ClassTypeData) it.next();
                Object findConvertedObject = findConvertedObject(parent, JavaControllerInterfaceConverter.INTERFACE_CLASS_INSTANCE.getKey());
                javaClass.getInterfaceList().addPropertyValue(findConvertedObject);
            }
        }
    }

    /**
     * @param data
     * @param method
     */
    public void convertChildControllerGetterMethod(InstanceData data, JavaMethod method) {
        TypeData type = data.getTypeData();
        String getterMethodName = JavaModelInterfaceFiller.getGetterMethodName(getNamingParameters(), getConversionModel(), data) + "Controller";
        JavaClassTypeData controllerType = null;
        JavaTypeData typeOfInstance = JavaGeneratorUtility.getInstanceJavaType(getConversionModel(), data);
        if (data.getCardinality() != InstanceCardinality.SINGLE.getLiteral()) {
            controllerType = (JavaClassTypeData) findConvertedObject(type, JavaListControllerInterfaceConverter.INTERFACE_CLASS_INSTANCE.getKey());
        } else {
            controllerType = (JavaClassTypeData) findConvertedObject(type, JavaControllerInterfaceConverter.INTERFACE_CLASS_INSTANCE.getKey());
        }
        if (controllerType == null) {
            throw new NullPointerException("failed to find controller for : " + typeOfInstance.getName());
        }
        JavaTypeData returnType = controllerType;
        method.setName(getterMethodName);
        method.setReturnType(returnType);
        method.setAccessLevel(JavaAccessLevel.PUBLIC.getLiteral());
        method.setStereotype(JavaControllerConstants.CONTROLLER_CHILD_CONTROLLER_GETTER_METHOD_STEREOTYPE);
    }
}
