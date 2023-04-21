package deduced.javagenerator.controller.list.implementation;

import assertion.AssertUtility;
import deduced.PropertyInstance;
import deduced.generator.*;
import deduced.javaconverter.*;
import deduced.javagenerator.*;
import deduced.javagenerator.controller.list.interfacegen.JavaListControllerInterfaceFiller;

/**
 * <p>
 * Title: JavaModelStructureGenerator
 * </p>
 * <p>
 * Description: JavaModelStructureGenerator
 * </p>
 */
public class JavaListControllerImplementationFiller extends JavaListControllerInterfaceFiller {

    public JavaListControllerImplementationFiller() {
    }

    /**
     * (non-Javadoc)
     * 
     * @see deduced.generator.GeneratorStep#generateStep()
     */
    public void generateStep() {
        callFillCreatedObject();
    }

    public void fillCreatedObject(ObjectConverter converter, Object key, Object value, PropertyInstance instance) {
        if (!fillListControllerImplementation(converter, value, instance)) {
            AssertUtility.fail("Failed to fill object: " + instance.getName());
        }
    }

    public boolean fillListControllerImplementation(ObjectConverter converter, Object value, PropertyInstance instance) {
        Object sourceObject = converter.getSourceObject();
        if (instance == JavaListControllerImplementationConverter.IMPLEMENTATION_CLASS_INSTANCE.getPropertyInstance()) {
            convertListImplementationClass((ClassTypeData) sourceObject, (JavaClassTypeData) value);
        } else if (instance == JavaListControllerImplementationConverter.FIND_BY_METHOD_INSTANCE.getPropertyInstance()) {
            convertFindByMethod((InstanceData) sourceObject, (JavaMethod) value);
            addFindRequiredType((JavaMethod) value);
        } else if (instance == JavaListControllerImplementationConverter.FIND_LIST_BY_METHOD_INSTANCE.getPropertyInstance()) {
            convertFindListByMethod((InstanceData) sourceObject, (JavaMethod) value);
            addFindRequiredType((JavaMethod) value);
        } else {
            return false;
        }
        return true;
    }

    /**
     * @param data
     * @param method
     */
    private void addFindRequiredType(JavaMethod method) {
        JavaClassTypeData listControllerClass = (JavaClassTypeData) method.getParent().getParent();
        ClassTypeData sourceClass = (ClassTypeData) getConversionModel().getCreatedObjectConverter(listControllerClass).getSourceObject();
        JavaClassTypeData modelInterface = (JavaClassTypeData) getConversionModel().findConvertedObject(sourceClass, JavaModelInterfaceConverter.INTERFACE_TYPE_INSTANCE.getKey());
        method.getRequiredTypeList().addPropertyValue(modelInterface);
    }

    /**
     * @param data
     * @param javaClass
     */
    private void convertListImplementationClass(ClassTypeData data, JavaClassTypeData javaClass) {
        String name = getNamingParameters().getClassNameAdjuster().adjustString(data.getName() + getNamingParameters().getListControllerSuffix() + getNamingParameters().getImplementationSuffix());
        javaClass.setName(name);
        javaClass.setAccessLevel(JavaAccessLevel.PUBLIC.getLiteral());
        Object listInterface = findConvertedObject(data, JavaListControllerInterfaceConverter.INTERFACE_CLASS_INSTANCE.getKey());
        javaClass.getInterfaceList().addPropertyValue(listInterface);
        javaClass.setParentClass(BasicJavaTypeControllers.DIRECT_PROPERTY_COLLECTION_CONTROLLER);
    }
}
