package uk.ac.lkl.migen.system.server.converter;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import uk.ac.lkl.common.util.JREXMLUtilities;
import uk.ac.lkl.common.util.expression.Expression;
import uk.ac.lkl.common.util.reflect.GenericClass;
import uk.ac.lkl.common.util.restlet.RuntimeRestletException;
import uk.ac.lkl.common.util.restlet.XMLConversionContext;
import uk.ac.lkl.common.util.restlet.XMLConverter;
import uk.ac.lkl.common.util.value.IntegerValue;
import uk.ac.lkl.migen.system.expresser.model.Attribute;
import uk.ac.lkl.migen.system.expresser.model.AttributeHandle;
import uk.ac.lkl.migen.system.expresser.model.ColorAllocationAttribute;
import uk.ac.lkl.migen.system.expresser.model.ColorResourceAttributeHandle;
import uk.ac.lkl.migen.system.expresser.model.ExpressionValueSource;
import uk.ac.lkl.migen.system.expresser.model.shape.block.BlockShape;
import uk.ac.lkl.migen.system.expresser.model.shape.block.PatternShape;
import uk.ac.lkl.migen.type.IntegerAttributeType;
import uk.ac.lkl.migen.type.IntegerColorAllocationAttributeType;
import uk.ac.lkl.migen.type.IntegerExpressionType;

/**
 * Converts Attribute<IntegerValue> to XML
 * 
 * @author Ken Kahn
 * 
 */
public class AttributeXMLConverter extends XMLConverter<Attribute<IntegerValue>> {

    @Override
    public Attribute<IntegerValue> convertToObject(Element element, XMLConversionContext context) {
        AttributeHandle<IntegerValue> handle;
        String name = element.getAttribute("name");
        if (name.equals("iterations")) {
            handle = PatternShape.ITERATIONS;
        } else if (name.equals("inc x")) {
            handle = PatternShape.getIncrementHandle(BlockShape.X, true);
        } else if (name.equals("inc y")) {
            handle = PatternShape.getIncrementHandle(BlockShape.Y, true);
        } else {
            throw new RuntimeRestletException("No handler for attributes named " + name + " in " + JREXMLUtilities.nodeToString(element));
        }
        Element valueElement = JREXMLUtilities.getChildWithTagName(element, "Value");
        if (valueElement == null) {
            throw new RuntimeRestletException("Expected a child to be a Value element " + JREXMLUtilities.nodeToString(element));
        }
        Expression<IntegerValue> expressionValue = context.convertToObject(GenericClass.getGeneric(IntegerExpressionType.class), valueElement);
        return new Attribute<IntegerValue>(handle, new ExpressionValueSource<IntegerValue>(expressionValue));
    }

    @Override
    protected void convertToXML(Document document, Element element, Attribute<IntegerValue> attribute, XMLConversionContext context) {
        AttributeHandle<IntegerValue> handle = attribute.getHandle();
        if (handle instanceof ColorResourceAttributeHandle) {
            ColorAllocationAttribute<IntegerValue> colorAllocationAttribute = new ColorAllocationAttribute<IntegerValue>(handle, attribute.getValueSource());
            Element colorAllocationAttributeElement = context.convertToXML(document, GenericClass.getGeneric(IntegerColorAllocationAttributeType.class), colorAllocationAttribute);
            element.appendChild(colorAllocationAttributeElement);
            return;
        }
        element.setAttribute("name", handle.getName());
        Element valueElement = context.convertToXML(document, GenericClass.getGeneric(IntegerExpressionType.class), attribute.getValueSource().getExpression(), "Value");
        element.appendChild(valueElement);
    }

    @Override
    public GenericClass<Attribute<IntegerValue>> getEntityClass() {
        return GenericClass.getGeneric(IntegerAttributeType.class);
    }
}
