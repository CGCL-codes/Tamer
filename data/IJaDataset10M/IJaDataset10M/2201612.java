package uk.ac.lkl.expresser.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import uk.ac.lkl.common.util.expression.Expression;
import uk.ac.lkl.common.util.expression.LocatedExpression;
import uk.ac.lkl.common.util.expression.ValueExpression;
import uk.ac.lkl.common.util.expression.operation.IntegerAdditionOperation;
import uk.ac.lkl.common.util.expression.operation.IntegerMultiplicationOperation;
import uk.ac.lkl.common.util.expression.operation.IntegerSubtractionOperation;
import uk.ac.lkl.common.util.value.IntegerValue;
import uk.ac.lkl.migen.system.expresser.model.Attribute;
import uk.ac.lkl.migen.system.expresser.model.AttributeHandle;
import uk.ac.lkl.migen.system.expresser.model.AvailableTileColors;
import uk.ac.lkl.migen.system.expresser.model.ColorAllocationAttribute;
import uk.ac.lkl.migen.system.expresser.model.ColorResourceAttributeHandle;
import uk.ac.lkl.migen.system.expresser.model.ExpresserModel;
import uk.ac.lkl.migen.system.expresser.model.ExpresserModelImpl;
import uk.ac.lkl.migen.system.expresser.model.ExpressionValueSource;
import uk.ac.lkl.migen.system.expresser.model.ModelColor;
import uk.ac.lkl.migen.system.expresser.model.Palette;
import uk.ac.lkl.migen.system.expresser.model.shape.block.BasicShape;
import uk.ac.lkl.migen.system.expresser.model.shape.block.BlockShape;
import uk.ac.lkl.migen.system.expresser.model.shape.block.GroupShape;
import uk.ac.lkl.migen.system.expresser.model.shape.block.ModelGroupShape;
import uk.ac.lkl.migen.system.expresser.model.shape.block.PatternShape;
import uk.ac.lkl.migen.system.expresser.model.tiednumber.TiedNumberExpression;
import uk.ac.lkl.migen.system.expresser.model.tiednumber.TiedNumberExpressionProxy;
import uk.ac.lkl.migen.system.expresser.model.tiednumber.UnspecifiedTiedNumberExpression;
import uk.ac.lkl.migen.system.task.TaskIdentifier;
import uk.ac.lkl.migen.system.task.goal.Goal;
import uk.ac.lkl.migen.system.util.StaticNumberObjectUtilities;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

/**
 * This was created on 6 May by copying and editing all the relevant XMLConverters in MiGen
 * They were edited to eliminate the XMLConversionContext and to use the GWT XML library.
 * Note they are using a GWT-friendly subset of the MiGen XMLUtilities
 * 
 * @author Ken Kahn
 *
 */
public class XMLConverter {

    private static final String TILE = "tile";

    private static final String BUILDINGBLOCK = "buildingblock";

    public static ExpresserModel xmlToExpresserModel(Element element, EventManager eventManager) {
        return xmlToExpresserModel(element, eventManager, new IdsToObjectsMapping());
    }

    public static ExpresserModel xmlToExpresserModel(Element element, EventManager eventManager, IdsToObjectsMapping idsToObjects) {
        Object previouslyEncounteredObject = idsToObjects.getPreviouslyEncounteredObject(element);
        if (previouslyEncounteredObject != null) {
            return (ExpresserModel) previouslyEncounteredObject;
        }
        Element paletteElement = XMLUtilities.getChildWithTagName(element, "Palette");
        Palette palette;
        if (paletteElement != null) {
            palette = xmlToPalette(paletteElement, eventManager, idsToObjects);
        } else {
            palette = AvailableTileColors.getDefaultPalette();
        }
        ExpresserModel model = new ExpresserModelImpl(palette);
        String name = element.getAttribute("name");
        model.setName(name);
        String taskIdentifierString = element.getAttribute("taskIdentifier");
        if (taskIdentifierString != null && !taskIdentifierString.isEmpty()) {
            try {
                int taskIdentifierId = Integer.parseInt(taskIdentifierString);
                TaskIdentifier taskIdentifier = TaskIdentifier.getStaticInstance(TaskIdentifier.class, taskIdentifierId);
                if (taskIdentifier != null) {
                    model.setTaskIdentifier(taskIdentifier);
                }
            } catch (NumberFormatException e) {
                Utilities.severe("Expected the following task identifier to be an integer: " + taskIdentifierString);
                e.printStackTrace();
            }
        }
        Element shapeListElement = XMLUtilities.getChildWithTagName(element, "BlockShapeList");
        if (shapeListElement != null) {
            List<BlockShape> shapes = xmlToBlockShapeList(shapeListElement, eventManager, idsToObjects);
            for (BlockShape shape : shapes) {
                model.addObject(shape);
                shape.setCorrectExpressionsUpToDate(false);
            }
        }
        Element taskVariablesElement = XMLUtilities.getChildWithTagName(element, "ExpressionList");
        if (taskVariablesElement != null) {
            List<Expression<IntegerValue>> expressions = xmlToExpressionList(taskVariablesElement, eventManager, idsToObjects);
            for (Expression<?> expression : expressions) {
                if (expression instanceof TiedNumberExpression) {
                    model.addTaskVariable((TiedNumberExpression<?>) expression);
                }
            }
        }
        Element attributeListElement = XMLUtilities.getChildWithTagName(element, "ColorAllocationList");
        if (attributeListElement != null) {
            List<Attribute<IntegerValue>> attributes = xmlToColorAllocationList(attributeListElement, eventManager, idsToObjects);
            ModelGroupShape modelAsAGroup = model.getModelAsAGroup();
            for (Attribute<?> attribute : attributes) {
                modelAsAGroup.addAttribute(attribute);
            }
        }
        Element totalAllocationExpressionElement = XMLUtilities.getChildWithTagName(element, "TotalAllocationExpression");
        if (totalAllocationExpressionElement != null) {
            Expression<IntegerValue> totalAllocationExpression = xmlToExpression(totalAllocationExpressionElement, eventManager, idsToObjects);
            model.setTotalAllocationExpression(totalAllocationExpression);
        }
        Element locatedExpressionListElement = XMLUtilities.getChildWithTagName(element, "LocatedExpressionList");
        if (locatedExpressionListElement != null) {
            List<LocatedExpression<IntegerValue>> locatedExpressions = xmlToLocatedExpressionList(locatedExpressionListElement, eventManager, idsToObjects);
            for (LocatedExpression<IntegerValue> locatedExpression : locatedExpressions) {
                model.addLocatedExpression(locatedExpression);
            }
        }
        Element autosavedNamesElement = XMLUtilities.getChildWithTagName(element, "StringList");
        if (autosavedNamesElement != null) {
            List<String> autosavedNames = xmlToStringList(autosavedNamesElement, idsToObjects);
            model.setAutosavedNames(new ArrayList<String>(autosavedNames));
        }
        idsToObjects.encountered(element, model);
        return model;
    }

    @SuppressWarnings("unchecked")
    private static Expression<IntegerValue> xmlToExpression(Element element, EventManager eventManager, IdsToObjectsMapping idsToObjects) {
        Object previouslyEncounteredObject = idsToObjects.getPreviouslyEncounteredObject(element);
        if (previouslyEncounteredObject != null) {
            return (Expression<IntegerValue>) previouslyEncounteredObject;
        }
        String type = element.getAttribute("type");
        Expression<IntegerValue> expression;
        if (type.equals("Value")) {
            expression = new ValueExpression<IntegerValue>(new IntegerValue(XMLUtilities.getIntegerAttribute(element, "value")));
        } else if (type.equals("TiedNumber")) {
            expression = xmlToTiedNumber(element, eventManager, idsToObjects);
        } else if (type.equals("UnspecifiedTiedNumber")) {
            return new UnspecifiedTiedNumberExpression<IntegerValue>();
        } else if (type.equals("Operation")) {
            String operationName = element.getAttribute("name");
            Element expressionListElement = XMLUtilities.getChildWithTagName(element, "ExpressionList");
            if (expressionListElement == null) {
                throw new RuntimeException("Expected a ListExpression child in " + XMLUtilities.nodeToString(element));
            }
            List<Expression<IntegerValue>> operands = xmlToExpressionList(expressionListElement, eventManager, idsToObjects);
            ArrayList<Expression<IntegerValue>> integerOperands = new ArrayList<Expression<IntegerValue>>();
            for (Expression<IntegerValue> subExpression : operands) {
                integerOperands.add(subExpression);
            }
            if (operationName.equals("add")) {
                expression = new IntegerAdditionOperation(integerOperands);
            } else if (operationName.equals("multiply")) {
                expression = new IntegerMultiplicationOperation(integerOperands);
            } else if (operationName.equals("subtract")) {
                expression = new IntegerSubtractionOperation(integerOperands);
            } else {
                throw new RuntimeException("Expected operation to be one of add, multiply, or subtract. Not " + operationName);
            }
        } else {
            throw new RuntimeException("Expected type to be one of Value, TiedNumber, or Operation. Not " + type);
        }
        idsToObjects.encountered(element, expression);
        return expression;
    }

    @SuppressWarnings("unchecked")
    private static TiedNumberExpression<IntegerValue> xmlToTiedNumber(Element element, EventManager eventManager, IdsToObjectsMapping idsToObjects) {
        Object previouslyEncounteredObject = idsToObjects.getPreviouslyEncounteredObject(element);
        if (previouslyEncounteredObject != null) {
            return (TiedNumberExpression<IntegerValue>) previouslyEncounteredObject;
        }
        String idString = element.getAttribute("idString");
        Element originalElement = XMLUtilities.getChildWithTagName(element, "Original");
        TiedNumberExpression<IntegerValue> original = null;
        if (originalElement != null) {
            original = xmlToTiedNumber(originalElement, eventManager, idsToObjects);
        }
        TiedNumberExpression<IntegerValue> tiedNumberExpression = TiedNumberExpression.getKnownTiedNumber(idString);
        int value = XMLUtilities.getIntegerAttribute(element, "value");
        if (original != null) {
            if (tiedNumberExpression == null) {
                tiedNumberExpression = new TiedNumberExpressionProxy<IntegerValue>(new IntegerValue(value), original, idString);
            } else {
                tiedNumberExpression.setOriginal(original);
            }
        } else if (tiedNumberExpression == null) {
            if (value == BlockShape.IMPOSSIBLE_ALLOCATION) {
                tiedNumberExpression = new UnspecifiedTiedNumberExpression<IntegerValue>(new IntegerValue(value), idString);
            } else {
                tiedNumberExpression = new TiedNumberExpression<IntegerValue>(new IntegerValue(value), idString);
            }
        } else {
            tiedNumberExpression.setValue(new IntegerValue(value));
        }
        Integer displayMode = XMLUtilities.getIntegerAttribute(element, "displayMode");
        tiedNumberExpression.setDisplayMode(displayMode);
        Boolean playing = XMLUtilities.getBooleanAttribute(element, "playing");
        tiedNumberExpression.setPlaying(playing);
        Boolean lockedAttribute = XMLUtilities.getBooleanAttribute(element, "locked", null);
        if (lockedAttribute == null) {
            lockedAttribute = !XMLUtilities.getBooleanAttribute(element, "changeable");
        }
        tiedNumberExpression.setLocked(lockedAttribute);
        Boolean keyAvailableAttribute = XMLUtilities.getBooleanAttribute(element, "keyAvailable", null);
        if (keyAvailableAttribute == null) {
            keyAvailableAttribute = XMLUtilities.getBooleanAttribute(element, "unlockable");
        }
        tiedNumberExpression.setKeyAvailable(keyAvailableAttribute);
        Boolean named = XMLUtilities.getBooleanAttribute(element, "named", null);
        String name = element.getAttribute("name");
        if (name == null) {
            name = "";
        }
        String unnamedName = TiedNumberExpression.getUnnamedName();
        if (named == null) {
            if (name.equals(unnamedName) || name.equals("name")) {
                tiedNumberExpression.setName(unnamedName);
                tiedNumberExpression.setNamed(false);
            } else {
                tiedNumberExpression.setName(name);
            }
        } else {
            tiedNumberExpression.setName(name);
        }
        idsToObjects.encountered(element, tiedNumberExpression);
        if (eventManager != null) {
            eventManager.updateTiedNumber(tiedNumberExpression);
        }
        return tiedNumberExpression;
    }

    static List<BlockShape> xmlToBlockShapeList(Element shapeListElement, EventManager eventManager, IdsToObjectsMapping idsToObjects) {
        List<BlockShape> blockShapeList = new ArrayList<BlockShape>();
        List<Element> childElements = XMLUtilities.getChildElements(shapeListElement);
        for (Element element : childElements) {
            blockShapeList.add(xmlToBlockShape(element, eventManager, idsToObjects));
        }
        return blockShapeList;
    }

    private static BlockShape xmlToBlockShape(Element element, EventManager eventManager, IdsToObjectsMapping idsToObjects) {
        Object previouslyEncounteredObject = idsToObjects.getPreviouslyEncounteredObject(element);
        if (previouslyEncounteredObject != null) {
            return (BlockShape) previouslyEncounteredObject;
        }
        String type = element.getAttribute("type");
        BlockShape blockShape;
        if (type != null && type.equals("PatternShape")) {
            PatternShape patternShape = new PatternShape();
            Element baseShapeElement = XMLUtilities.getChildWithTagName(element, "BaseShape");
            BlockShape baseShape = xmlToBlockShape(baseShapeElement, eventManager, idsToObjects);
            if (baseShape == null) {
                throw new RuntimeException("Expected to find a BaseShape element in " + XMLUtilities.nodeToString(element));
            }
            patternShape.setShape(baseShape);
            String treatAsType = element.getAttribute("treatAsType");
            if (treatAsType != null) {
                if (treatAsType.equals(BUILDINGBLOCK)) {
                    patternShape.setBuildingBlockStatus(PatternShape.TREAT_AS_A_BUILDING_BLOCK);
                } else if (treatAsType.equals(TILE)) {
                    patternShape.setTreatAsTileIfRepeatsTileOnce(true);
                }
            }
            blockShape = patternShape;
        } else if (type != null && type.equals("GroupShape")) {
            GroupShape groupShape = new GroupShape();
            Element subShapesElement = XMLUtilities.getChildWithTagName(element, "BlockShapeList");
            if (subShapesElement == null) {
                throw new RuntimeException("Expected to find a BlockShapeList element in " + XMLUtilities.nodeToString(element));
            }
            List<BlockShape> subShapes = xmlToBlockShapeList(subShapesElement, eventManager, idsToObjects);
            groupShape.addShapes(subShapes);
            Element constructionExpressionCoefficientsElement = XMLUtilities.getChildWithTagName(element, "TiedNumberList");
            if (constructionExpressionCoefficientsElement != null) {
                List<TiedNumberExpression<IntegerValue>> constructionExpressionCoefficients = xmlToTiedNumberList(constructionExpressionCoefficientsElement, eventManager, idsToObjects);
                groupShape.setConstructionExpressionCoefficients(constructionExpressionCoefficients);
            }
            blockShape = groupShape;
        } else if (type != null && type.equals("BasicShape")) {
            BasicShape basicShape = new BasicShape();
            Element colorElement = XMLUtilities.getChildWithTagName(element, "Color");
            if (colorElement == null) {
                throw new RuntimeException("Expected to find a Color element in " + element.toString());
            }
            ModelColor color = xmlToColor(colorElement, eventManager, idsToObjects);
            basicShape.setColor(color);
            blockShape = basicShape;
        } else {
            throw new RuntimeException("Expected to type be PatternShape, GroupShape, or BasicShape in " + XMLUtilities.nodeToString(element));
        }
        Element attributeListElement = XMLUtilities.getChildWithTagName(element, "AttributeList");
        if (attributeListElement != null) {
            List<Attribute<IntegerValue>> attributes = xmlToAttributeList(attributeListElement, eventManager, idsToObjects);
            for (Attribute<IntegerValue> attribute : attributes) {
                blockShape.addAttribute(attribute);
            }
        }
        Element colorAllocationListElement = XMLUtilities.getChildWithTagName(element, "ColorAllocationList");
        if (colorAllocationListElement != null) {
            List<Attribute<IntegerValue>> attributes = xmlToColorAllocationList(colorAllocationListElement, eventManager, idsToObjects);
            for (Attribute<IntegerValue> attribute : attributes) {
                blockShape.addAttribute(attribute);
            }
        }
        int x = XMLUtilities.getIntegerAttribute(element, "x");
        int y = XMLUtilities.getIntegerAttribute(element, "y");
        blockShape.moveTo(new IntegerValue(x), new IntegerValue(y));
        Boolean positive = XMLUtilities.getBooleanAttribute(element, "positive", true);
        if (positive == false) {
            blockShape.setPositive(false);
        }
        String uniqueId = element.getAttribute("uniqueId");
        if (uniqueId != null && !uniqueId.isEmpty()) {
            blockShape.setUniqueId(uniqueId);
        }
        idsToObjects.encountered(element, blockShape);
        return blockShape;
    }

    private static List<Attribute<IntegerValue>> xmlToColorAllocationList(Element colorAllocationListElement, EventManager eventManager, IdsToObjectsMapping idsToObjects) {
        List<Attribute<IntegerValue>> attributeList = new ArrayList<Attribute<IntegerValue>>();
        List<Element> childElements = XMLUtilities.getChildElements(colorAllocationListElement);
        for (Element element : childElements) {
            attributeList.add(xmlToColorAllocation(element, eventManager, idsToObjects));
        }
        return attributeList;
    }

    @SuppressWarnings("unchecked")
    private static Attribute<IntegerValue> xmlToColorAllocation(Element element, EventManager eventManager, IdsToObjectsMapping idsToObjects) {
        Object previouslyEncounteredObject = idsToObjects.getPreviouslyEncounteredObject(element);
        if (previouslyEncounteredObject != null) {
            return (Attribute<IntegerValue>) previouslyEncounteredObject;
        }
        Element colorElement = XMLUtilities.getChildWithTagName(element, "Color");
        if (colorElement == null) {
            throw new RuntimeException("Expected a child to be a Color element " + XMLUtilities.nodeToString(element));
        }
        ModelColor modelColor = xmlToColor(colorElement, eventManager, idsToObjects);
        ColorResourceAttributeHandle handle = BlockShape.colorResourceAttributeHandle(modelColor);
        if (handle == null) {
            throw new RuntimeException("Could not find the handle for the color " + XMLUtilities.nodeToString(colorElement));
        }
        Element valueElement = XMLUtilities.getChildWithTagName(element, "Value");
        if (valueElement == null) {
            throw new RuntimeException("Expected a child to be a Value element " + XMLUtilities.nodeToString(element));
        }
        Expression<IntegerValue> expressionValue = xmlToExpression(valueElement, eventManager, idsToObjects);
        ColorAllocationAttribute<IntegerValue> colorAllocationAttribute = new ColorAllocationAttribute<IntegerValue>(handle, new ExpressionValueSource<IntegerValue>(expressionValue));
        idsToObjects.encountered(element, colorAllocationAttribute);
        return colorAllocationAttribute;
    }

    static List<Attribute<IntegerValue>> xmlToAttributeList(Element attributeListElement, EventManager eventManager, IdsToObjectsMapping idsToObjects) {
        List<Attribute<IntegerValue>> attributeList = new ArrayList<Attribute<IntegerValue>>();
        List<Element> childElements = XMLUtilities.getChildElements(attributeListElement);
        for (Element element : childElements) {
            attributeList.add(xmlToAttribute(element, eventManager, idsToObjects));
        }
        return attributeList;
    }

    @SuppressWarnings("unchecked")
    static Attribute<IntegerValue> xmlToAttribute(Element element, EventManager eventManager, IdsToObjectsMapping idsToObjects) {
        Object previouslyEncounteredObject = idsToObjects.getPreviouslyEncounteredObject(element);
        if (previouslyEncounteredObject != null) {
            return (Attribute<IntegerValue>) previouslyEncounteredObject;
        }
        AttributeHandle<IntegerValue> handle;
        String name = element.getAttribute("name");
        if (name != null && name.equals("iterations")) {
            handle = PatternShape.ITERATIONS;
        } else if (name != null && name.equals("inc x")) {
            handle = PatternShape.getIncrementHandle(BlockShape.X, true);
        } else if (name != null && name.equals("inc y")) {
            handle = PatternShape.getIncrementHandle(BlockShape.Y, true);
        } else {
            throw new RuntimeException("No handler for attributes named " + name + " in " + XMLUtilities.nodeToString(element));
        }
        Element valueElement = XMLUtilities.getChildWithTagName(element, "Value");
        if (valueElement == null) {
            throw new RuntimeException("Expected a child to be a Value element " + XMLUtilities.nodeToString(element));
        }
        Expression<IntegerValue> expressionValue = xmlToExpression(valueElement, eventManager, idsToObjects);
        Attribute<IntegerValue> attribute = new Attribute<IntegerValue>(handle, new ExpressionValueSource<IntegerValue>(expressionValue));
        idsToObjects.encountered(element, attribute);
        return attribute;
    }

    static List<Expression<IntegerValue>> xmlToExpressionList(Element expressionsElement, EventManager eventManager, IdsToObjectsMapping idsToObjects) {
        List<Expression<IntegerValue>> attributeList = new ArrayList<Expression<IntegerValue>>();
        List<Element> childElements = XMLUtilities.getChildElements(expressionsElement);
        for (Element element : childElements) {
            attributeList.add(xmlToExpression(element, eventManager, idsToObjects));
        }
        return attributeList;
    }

    static List<String> xmlToStringList(Element stringListElement, IdsToObjectsMapping idsToObjects) {
        List<String> attributeList = new ArrayList<String>();
        List<Element> childElements = XMLUtilities.getChildElements(stringListElement);
        for (Element element : childElements) {
            attributeList.add(xmlToString(element, idsToObjects));
        }
        return attributeList;
    }

    static String xmlToString(Element element, IdsToObjectsMapping idsToObjects) {
        Object previouslyEncounteredObject = idsToObjects.getPreviouslyEncounteredObject(element);
        if (previouslyEncounteredObject != null) {
            return (String) previouslyEncounteredObject;
        }
        String string = element.getAttribute("value");
        idsToObjects.encountered(element, string);
        return string;
    }

    static List<LocatedExpression<IntegerValue>> xmlToLocatedExpressionList(Element locatedExpressionListElement, EventManager eventManager, IdsToObjectsMapping idsToObjects) {
        List<LocatedExpression<IntegerValue>> attributeList = new ArrayList<LocatedExpression<IntegerValue>>();
        List<Element> childElements = XMLUtilities.getChildElements(locatedExpressionListElement);
        for (Element element : childElements) {
            attributeList.add(xmlToLocatedExpression(element, eventManager, idsToObjects));
        }
        return attributeList;
    }

    @SuppressWarnings("unchecked")
    public static LocatedExpression<IntegerValue> xmlToLocatedExpression(Element element, EventManager eventManager, IdsToObjectsMapping idsToObjects) {
        Object previouslyEncounteredObject = idsToObjects.getPreviouslyEncounteredObject(element);
        if (previouslyEncounteredObject != null) {
            return (LocatedExpression<IntegerValue>) previouslyEncounteredObject;
        }
        Integer x = XMLUtilities.getIntegerAttribute(element, "x");
        Integer y = XMLUtilities.getIntegerAttribute(element, "y");
        Element expressionElement = XMLUtilities.getChildWithTagName(element, "Expression");
        if (expressionElement != null) {
            Expression<IntegerValue> expression = xmlToExpression(expressionElement, eventManager, idsToObjects);
            LocatedExpression<IntegerValue> locatedExpression = new LocatedExpression<IntegerValue>(expression, x, y);
            idsToObjects.encountered(element, locatedExpression);
            String uniqueId = element.getAttribute("uniqueId");
            if (uniqueId != null && !uniqueId.isEmpty()) {
                locatedExpression.setUniqueId(uniqueId);
            }
            return locatedExpression;
        }
        return null;
    }

    private static Collection<ModelColor> xmlToColorList(Element colorsElement, EventManager eventManager, IdsToObjectsMapping idsToObjects) {
        List<ModelColor> attributeList = new ArrayList<ModelColor>();
        List<Element> childElements = XMLUtilities.getChildElements(colorsElement);
        for (Element element : childElements) {
            attributeList.add(xmlToColor(element, eventManager, idsToObjects));
        }
        return attributeList;
    }

    private static ModelColor xmlToColor(Element element, EventManager eventManager, IdsToObjectsMapping idsToObjects) {
        Object previouslyEncounteredObject = idsToObjects.getPreviouslyEncounteredObject(element);
        if (previouslyEncounteredObject != null) {
            return (ModelColor) previouslyEncounteredObject;
        }
        int red = Integer.parseInt(element.getAttribute("red"));
        int green = Integer.parseInt(element.getAttribute("green"));
        int blue = Integer.parseInt(element.getAttribute("blue"));
        int alpha = Integer.parseInt(element.getAttribute("alpha"));
        boolean negative = Boolean.parseBoolean(element.getAttribute("negative"));
        String name = element.getAttribute("name");
        ModelColor modelColor = new ModelColor(red, green, blue, alpha, negative, name);
        idsToObjects.encountered(element, modelColor);
        return modelColor;
    }

    private static List<TiedNumberExpression<IntegerValue>> xmlToTiedNumberList(Element tiedNumberListElement, EventManager eventManager, IdsToObjectsMapping idsToObjects) {
        List<TiedNumberExpression<IntegerValue>> attributeList = new ArrayList<TiedNumberExpression<IntegerValue>>();
        List<Element> childElements = XMLUtilities.getChildElements(tiedNumberListElement);
        for (Element element : childElements) {
            attributeList.add(xmlToTiedNumber(element, eventManager, idsToObjects));
        }
        return attributeList;
    }

    static Palette xmlToPalette(Element element, EventManager eventManager, IdsToObjectsMapping idsToObjects) {
        Object previouslyEncounteredObject = idsToObjects.getPreviouslyEncounteredObject(element);
        if (previouslyEncounteredObject != null) {
            return (Palette) previouslyEncounteredObject;
        }
        Element colorsElement = XMLUtilities.getChildWithTagName(element, "ModelColorList");
        if (colorsElement == null) {
            throw new RuntimeException("Expected to find a ModelColorList element in " + XMLUtilities.nodeToString(element));
        }
        Palette palette = new Palette(xmlToColorList(colorsElement, eventManager, idsToObjects));
        idsToObjects.encountered(element, palette);
        return palette;
    }

    public static List<Goal> xmlToGoalList(Element goalListElement, IdsToObjectsMapping idsToObjectsMapping) {
        ArrayList<Goal> goals = new ArrayList<Goal>();
        NodeList childNodes = goalListElement.getChildNodes();
        int length = childNodes.getLength();
        for (int i = 0; i < length; i++) {
            Node node = childNodes.item(i);
            if (node instanceof Element) {
                Integer value = XMLUtilities.getIntegerAttribute((Element) node, "value");
                Goal instance = StaticNumberObjectUtilities.get(Goal.class, value);
                if (instance != null) {
                    goals.add(instance);
                }
            }
        }
        return goals;
    }
}
