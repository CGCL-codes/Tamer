package ch.bbv.mda.persistence.mdr;

import java.lang.reflect.Modifier;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.omg.uml.UmlPackage;
import org.omg.uml.foundation.core.AssociationEnd;
import org.omg.uml.foundation.core.Attribute;
import org.omg.uml.foundation.core.Classifier;
import org.omg.uml.foundation.core.DataType;
import org.omg.uml.foundation.core.Generalization;
import org.omg.uml.foundation.core.Interface;
import org.omg.uml.foundation.core.Operation;
import org.omg.uml.foundation.core.Parameter;
import org.omg.uml.foundation.core.Stereotype;
import org.omg.uml.foundation.core.TagDefinition;
import org.omg.uml.foundation.core.TaggedValue;
import org.omg.uml.foundation.core.UmlAssociation;
import org.omg.uml.foundation.core.UmlClass;
import org.omg.uml.foundation.datatypes.Multiplicity;
import org.omg.uml.foundation.datatypes.MultiplicityRange;
import org.omg.uml.foundation.datatypes.ParameterDirectionKindEnum;
import org.omg.uml.foundation.datatypes.VisibilityKind;
import org.omg.uml.foundation.datatypes.VisibilityKindEnum;
import ch.bbv.mda.persistence.PersistenceException;

public class MdrFactory {

    /**
   * Logger of the class.
   */
    private static Log log = LogFactory.getLog(MdrFactory.class);

    /**
   * Create an AssociationClass
   * 
   * @param context
   * @return
   */
    public static UmlAssociation createAssociation(UmlPackage context) {
        return context.getCore().getUmlAssociation().createUmlAssociation();
    }

    /**
   * Create an AssiciationEnd
   * 
   * @param context
   * @return
   */
    public static AssociationEnd createAssociationEnd(UmlPackage context) {
        return context.getCore().getAssociationEnd().createAssociationEnd();
    }

    /**
   * Creates an Attribute and associates it with a UmlClass.
   * 
   * @param attrName
   *          The attribute name.
   * @param modifier
   *          The visibility of the attribute.
   * @param type
   *          The attribute type.
   * @param umlClz
   *          The UmlClass instance.
   * @return The Attribute instance.
   */
    public static Attribute createAttribute(UmlPackage context, String attrName, int modifier, Classifier type, Classifier umlClz) {
        final Attribute attr = context.getModelManagement().getCore().getAttribute().createAttribute(attrName, mapModifier(modifier), false, null, null, null, null, null, null);
        attr.setType(type);
        attr.setOwner(umlClz);
        return attr;
    }

    /**
   * Creates a UmlClass.
   * 
   * @param clzName
   *          The class name.
   * @param modifier
   *          The visibility of the class.
   * @return The UmlClass instance.
   */
    public static UmlClass createClass(UmlPackage context, String clzName, int modifier) {
        return createClass(context, clzName, modifier, false);
    }

    /**
   * Creates a UmlClass.
   * 
   * @param clzName
   *          The class name.
   * @param modifier
   *          The visibility of the class.
   * @param abstr
   *          True when the class is abstract, false otherwise.
   * @return The UmlClass instance.
   */
    public static UmlClass createClass(UmlPackage context, String clzName, int modifier, boolean abstr) {
        return context.getCore().getUmlClass().createUmlClass(clzName, mapModifier(modifier), false, false, false, abstr, false);
    }

    /**
   * Creates a UmlDataType.
   * 
   * @param typeName
   *          The datatype name.
   * @return The UmlClass instance.
   */
    public static DataType createDatatype(UmlPackage context, String typeName) {
        return context.getCore().getDataType().createDataType(typeName, VisibilityKindEnum.VK_PUBLIC, false, false, false, false);
    }

    /**
   * Creates a Data Type
   * 
   * @param name
   *          The name of the datatype
   * @return The Datatype instance.
   */
    public static MdrUmlDataType createMdrUmlDataType(UmlPackage context, String name) {
        final DataType dataType = context.getCore().getDataType().createDataType();
        dataType.setName(name);
        MdrUmlDataType mdrDataType = new MdrUmlDataType(dataType);
        return mdrDataType;
    }

    /**
   * Creates a Generalization between the class and its superclass.
   * 
   * @param clz
   *          The class.
   * @param superClz
   *          The super class.
   * @return The Generalization instance.
   */
    public static Generalization createGeneralization(UmlPackage context, Classifier clz, Classifier superClz) {
        final Generalization gen = context.getCore().getGeneralization().createGeneralization("extends", VisibilityKindEnum.VK_PUBLIC, false, null);
        gen.setChild(clz);
        gen.setParent(superClz);
        return gen;
    }

    /**
   * Creates an Interface.
   * 
   * @param itfName
   *          The interface name.
   * @param modifier
   *          The visibility of the interface.
   * @return The Interface instance.
   */
    public static Interface createInterface(UmlPackage context, String itfName, int modifier) {
        return context.getCore().getInterface().createInterface(itfName, mapModifier(modifier), false, false, false, false);
    }

    /**
   * Create a Multiplicity instance
   * 
   * @param context
   * @return
   */
    public static Multiplicity createMultiplicity(UmlPackage context) {
        return context.getDataTypes().getMultiplicity().createMultiplicity();
    }

    /**
   * Create a Multiplicity Instance with Range
   * 
   * @param context
   * @param lowerBound
   * @param upperBound
   * @return
   */
    public static Multiplicity createMultiplicity(UmlPackage context, int lowerBound, int upperBound) {
        Multiplicity multiplicity = createMultiplicity(context);
        MultiplicityRange range = createMultiplicityRange(context);
        range.setLower(lowerBound);
        range.setUpper(upperBound);
        multiplicity.getRange().add(range);
        return multiplicity;
    }

    /**
   * Creates a MultiplicityRange
   * 
   * @param context
   * @return
   */
    public static MultiplicityRange createMultiplicityRange(UmlPackage context) {
        return context.getDataTypes().getMultiplicityRange().createMultiplicityRange();
    }

    /**
   * Creates an Operation and associates it with a UmlClass.
   * 
   * @param opName
   *          The operation name.
   * @param modifier
   *          The visibility of the operation.
   * @param umlClz
   *          The UmlClass instance.
   * @return The Operation instance.
   */
    public static Operation createOperation(UmlPackage context, String opName, int modifier, Classifier umlClz) {
        final Operation op = context.getModelManagement().getCore().getOperation().createOperation(opName, mapModifier(modifier), false, null, false, null, false, false, false, null);
        op.setOwner(umlClz);
        return op;
    }

    /**
   * Creates a UmlPackage.
   * 
   * @param pkgName
   *          The package name.
   * @return The UmlPackage instance.
   */
    public static org.omg.uml.modelmanagement.UmlPackage createPackage(UmlPackage context, String pkgName) {
        return context.getModelManagement().getUmlPackage().createUmlPackage(pkgName, VisibilityKindEnum.VK_PUBLIC, false, false, false, false);
    }

    /**
   * Creates a Parameter and associates it with an Operation.
   * 
   * @param paraName
   *          The parameter name.
   * @param type
   *          The operation return type.
   * @param op
   *          The Operation instance.
   * @return The Parameter instance.
   */
    @SuppressWarnings("unchecked")
    public static Parameter createParameter(UmlPackage context, String paraName, Classifier type, Operation op) {
        final List<Parameter> parameters = op.getParameter();
        final Parameter parameter = context.getModelManagement().getCore().getParameter().createParameter(paraName, VisibilityKindEnum.VK_PUBLIC, false, null, null);
        parameters.add(parameter);
        parameter.setType(type);
        return parameter;
    }

    /**
   * Creates a realization between the class and an interface.
   * 
   * @param clz
   *          The class.
   * @param itf
   *          The interface.
   * @return The Realization instance.
   */
    public static Generalization createRealization(UmlPackage context, Classifier clz, Classifier itf) {
        final Generalization gen = context.getCore().getGeneralization().createGeneralization("implements", VisibilityKindEnum.VK_PUBLIC, false, null);
        gen.setChild(clz);
        gen.setParent(itf);
        return gen;
    }

    /**
   * Creates a return type Parameter and associates it with an Operation.
   * 
   * @param type
   *          The operation return type.
   * @param op
   *          The Operation instance.
   * @return The Parameter instance.
   * @throws PersistenceException
   *           when the repository has not been initialized.
   */
    public static Parameter createReturnType(UmlPackage context, Classifier type, Operation op) throws PersistenceException {
        for (Object parameterObj : op.getParameter()) {
            Parameter param = (Parameter) parameterObj;
            if (param.getKind() != null && param.getKind().equals(ParameterDirectionKindEnum.PDK_RETURN)) {
                throw new PersistenceException("Operation '" + op.getName() + "' already has a return type!", null);
            }
        }
        final Parameter parameter = createParameter(context, null, type, op);
        parameter.setKind(ParameterDirectionKindEnum.PDK_RETURN);
        return parameter;
    }

    /**
   * Creates a Stereotype
   * 
   * @param name
   *          The name of the Stereotype
   * @return The Stereotype instance.
   */
    public static Stereotype createStereotype(UmlPackage context, String name) {
        assert (name != null);
        final Stereotype stereotype = context.getCore().getStereotype().createStereotype();
        stereotype.setName(name);
        return stereotype;
    }

    /**
   * Creates a tagged Value
   * 
   * @param element
   *          The MdrUmlElement
   * @param name
   *          The name of the TaggedValue
   * @return The TaggedValue instance.
   */
    public static TaggedValue createTaggedValue(UmlPackage context, String name) {
        TaggedValue taggedValue = context.getCore().getTaggedValue().createTaggedValue(name, VisibilityKindEnum.VK_PUBLIC, false, null);
        TagDefinition tagDefinition = context.getCore().getTagDefinition().createTagDefinition();
        tagDefinition.setName(name);
        taggedValue.setType(tagDefinition);
        return taggedValue;
    }

    /**
   * @param modifier
   * @return
   */
    private static VisibilityKind mapModifier(int modifier) {
        VisibilityKind vis = null;
        if (modifier == 0) {
            vis = VisibilityKindEnum.VK_PACKAGE;
        } else if (Modifier.isPrivate(modifier)) {
            vis = VisibilityKindEnum.VK_PRIVATE;
        } else if (Modifier.isProtected(modifier)) {
            vis = VisibilityKindEnum.VK_PROTECTED;
        } else if (Modifier.isPublic(modifier)) {
            vis = VisibilityKindEnum.VK_PUBLIC;
        } else {
            log.warn("Unknown modifier: " + Modifier.toString(modifier));
        }
        return vis;
    }
}
