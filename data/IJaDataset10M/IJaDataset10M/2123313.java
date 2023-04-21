package uk.ac.lkl.migen.system.expresser;

import java.util.Collection;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import uk.ac.lkl.common.util.JREXMLUtilities;
import uk.ac.lkl.common.util.event.UpdateEvent;
import uk.ac.lkl.common.util.event.UpdateListener;
import uk.ac.lkl.common.util.expression.Expression;
import uk.ac.lkl.common.util.value.IntegerValue;
import uk.ac.lkl.migen.system.ExpresserLauncher;
import uk.ac.lkl.migen.system.expresser.model.Attribute;
import uk.ac.lkl.migen.system.expresser.model.AttributeHandle;
import uk.ac.lkl.migen.system.expresser.model.ColorResourceAttributeHandle;
import uk.ac.lkl.migen.system.expresser.model.ExpresserModel;
import uk.ac.lkl.migen.system.expresser.model.ValueSource;
import uk.ac.lkl.migen.system.expresser.model.event.AttributeChangeEvent;
import uk.ac.lkl.migen.system.expresser.model.event.AttributeChangeListener;
import uk.ac.lkl.migen.system.expresser.model.event.CreationEvent;
import uk.ac.lkl.migen.system.expresser.model.event.CreationEventListener;
import uk.ac.lkl.migen.system.expresser.model.event.CreationEventManager;
import uk.ac.lkl.migen.system.expresser.model.event.ObjectEvent;
import uk.ac.lkl.migen.system.expresser.model.event.ObjectListener;
import uk.ac.lkl.migen.system.expresser.model.event.TiedNumberCreationEvent;
import uk.ac.lkl.migen.system.expresser.model.shape.block.BlockShape;
import uk.ac.lkl.migen.system.expresser.model.tiednumber.TiedNumberExpression;
import uk.ac.lkl.migen.system.server.User;
import uk.ac.lkl.migen.system.server.UserSet;
import uk.ac.lkl.migen.system.util.MiGenUtilities;

/**
 * Produces a log of events in the Kaleidoscope Common Format
 * 
 * 
 * @author Ken Kahn
 *
 */
public class KaleidoscopeCommonFormatLogger {

    /**
     * The model that this logger is watching.
     * 
     */
    private ExpresserModel model;

    protected Document document;

    private Element actionsElement;

    /**
     * 
     * @param model The model that this logger is watching.
     */
    public KaleidoscopeCommonFormatLogger(ExpresserModel model) {
        this.model = model;
        try {
            document = JREXMLUtilities.createDocument();
            Element interactiondataElement = document.createElement("interactiondata");
            document.appendChild(interactiondataElement);
            Element preambleElement = document.createElement("preamble");
            interactiondataElement.appendChild(preambleElement);
            Element usersElement = createUsersElement();
            preambleElement.appendChild(usersElement);
            actionsElement = document.createElement("actions");
            interactiondataElement.appendChild(actionsElement);
        } catch (ParserConfigurationException e) {
            MiGenUtilities.printError("Failed to create XML document.");
            e.printStackTrace();
            return;
        }
        addListeners();
    }

    protected void actionElementAdded(Element actionElement) {
        actionsElement.appendChild(actionElement);
    }

    protected Element createUsersElement() {
        Element usersElement = document.createElement("users");
        UserSet userSet = ExpresserLauncher.getUserSet();
        List<User> users = userSet.getUsers();
        for (User user : users) {
            Element userElement = document.createElement("user");
            userElement.setAttribute("role", "student");
            String firstname = user.getFirstname();
            userElement.setAttribute("firstname", firstname);
            userElement.setAttribute("lastname", user.getLastname());
            userElement.setAttribute("id", user.getUsername());
            usersElement.appendChild(userElement);
        }
        return usersElement;
    }

    /**
     * Add all relevant listeners to the model.
     * 
     */
    protected void addListeners() {
        AttributeChangeListener<BlockShape> attributeChangeListener = new AttributeChangeListener<BlockShape>() {

            public void attributesChanged(AttributeChangeEvent<BlockShape> e) {
                BlockShape shape = e.getObject();
                Collection<Attribute<?>> attributes = e.getChangedAttributes();
                for (Attribute<?> attribute : attributes) {
                    Element actionElement = createActionElementShapeAttributeChange(attribute, shape);
                    actionElementAdded(actionElement);
                }
            }
        };
        AttributeChangeListener<BlockShape> globalAllocationExpressionListener = new AttributeChangeListener<BlockShape>() {

            public void attributesChanged(AttributeChangeEvent<BlockShape> e) {
                Collection<Attribute<?>> attributes = e.getChangedAttributes();
                for (Attribute<?> attribute : attributes) {
                    AttributeHandle<?> handle = attribute.getHandle();
                    if (handle instanceof ColorResourceAttributeHandle) {
                        ColorResourceAttributeHandle colorResourceAttributeHandle = (ColorResourceAttributeHandle) handle;
                        Object currentValue = attribute.getValue();
                        Object previousValue = attribute.getPreviousValue();
                        if (currentValue.equals(previousValue)) {
                            continue;
                        }
                        String description = new String("(" + model.getId() + ") ");
                        description += "Global attribute '" + attribute.getID() + "'";
                        description += " ('" + colorResourceAttributeHandle.getColor() + "')";
                        description += " has changed from '";
                        description += previousValue;
                        description += "' to '";
                        description += currentValue;
                        description += "'.";
                        Element actionElement = addActionElement("modify", "global_color_attribute_change", description);
                        Element modelElement = createModelElement(model.getId().toString(), actionElement);
                        Element propertiesElement = addProperties(modelElement);
                        addPropertyFromAttribute(attribute, attribute.getValueSource(), propertiesElement);
                        actionElementAdded(actionElement);
                    }
                }
            }
        };
        UpdateListener<Expression<IntegerValue>> totalAllocationExpressionListener = new UpdateListener<Expression<IntegerValue>>() {

            @Override
            public void objectUpdated(UpdateEvent<Expression<IntegerValue>> e) {
                Expression<IntegerValue> expression = e.getSource();
                String description = new String("(" + model.getId() + ") ");
                description += "Total allocation expression";
                description += " has changed to '";
                description += expression.toString();
                description += "'.";
                Element actionElement = addActionElement("modify", "global_color_attribute_change", description);
                Element modelElement = createModelElement(model.getId().toString(), actionElement);
                Element propertiesElement = addProperties(modelElement);
                addProperty("total allocation", expression, propertiesElement);
                actionElementAdded(actionElement);
            }
        };
        ObjectListener objectListener = new ObjectListener() {

            public void objectAdded(ObjectEvent e) {
                BlockShape shape = e.getObject();
                actionElementAdded(actionElementForShapeCreation(CommonFormatEventType.CREATION, shape, null, null));
            }

            public void objectRemoved(ObjectEvent e) {
                BlockShape shape = e.getObject();
                String description = "(" + model.getId() + ") Object " + shape.getID() + " removed from the model.";
                Element actionElement = addActionElementForShape(shape, "remove", "indicator", null, description);
                actionElementAdded(actionElement);
            }
        };
        UpdateListener<BlockShape> objectUpdateListener = new UpdateListener<BlockShape>() {

            public void objectUpdated(UpdateEvent<BlockShape> e) {
            }
        };
        UpdateListener<BlockShape> objectSelectionUpdateListener = new UpdateListener<BlockShape>() {

            public void objectUpdated(UpdateEvent<BlockShape> e) {
            }
        };
        model.addAttributeChangeListener(attributeChangeListener);
        model.addObjectListener(objectListener);
        model.addObjectUpdateListener(objectUpdateListener);
        model.addSelectionUpdateListener(objectSelectionUpdateListener);
        model.addGlobalAllocationExpressionListener(globalAllocationExpressionListener);
        model.addTotalAllocationExpressionListener(totalAllocationExpressionListener);
        CreationEventManager.addCreationEventListener(new CreationEventListener() {

            public void processEvent(CreationEvent<?> e) {
                if (e instanceof TiedNumberCreationEvent) {
                    TiedNumberExpression<IntegerValue> tiedNumber = ((TiedNumberCreationEvent) e).getTiedNumber();
                    String description = "New tied number " + tiedNumber.getId().toString();
                    description += " (" + tiedNumber.getIdString() + ")";
                    description += " value is " + tiedNumber.getValue();
                    Element actionElement = addActionElement("create", "createTiedNumber", description);
                    createTiedNumberElement(tiedNumber, actionElement);
                    addTiedNumberListeners(tiedNumber);
                    actionElementAdded(actionElement);
                }
            }
        });
    }

    private void addTiedNumberListeners(final TiedNumberExpression<IntegerValue> tiedNumber) {
        UpdateListener<TiedNumberExpression<IntegerValue>> lockStatusUpdateListener = new UpdateListener<TiedNumberExpression<IntegerValue>>() {

            @Override
            public void objectUpdated(UpdateEvent<TiedNumberExpression<IntegerValue>> e) {
                boolean locked = tiedNumber.isLocked();
                String description = "Tied number " + tiedNumber.getId().toString();
                description += " (" + tiedNumber.getIdString() + ")";
                description += " lock status is now " + locked;
                Element actionElement = addActionElement("modify", "change_tied_number_lock_status", description);
                Element tiedNumberElement = createTiedNumberElement(tiedNumber, actionElement);
                Element propertiesElement = addProperties(tiedNumberElement);
                addProperty("lock_status", Boolean.toString(locked), propertiesElement);
                actionElementAdded(actionElement);
            }
        };
        tiedNumber.addLockStatusUpdateListener(lockStatusUpdateListener);
        UpdateListener<TiedNumberExpression<IntegerValue>> nameFieldUpdateListener = new UpdateListener<TiedNumberExpression<IntegerValue>>() {

            @Override
            public void objectUpdated(UpdateEvent<TiedNumberExpression<IntegerValue>> e) {
                String name = tiedNumber.getName();
                String description = "Tied number " + tiedNumber.getId().toString();
                description += " (" + tiedNumber.getIdString() + ")";
                description += " name is now " + name;
                Element actionElement = addActionElement("modify", "change_tied_number_name", description);
                Element tiedNumberElement = createTiedNumberElement(tiedNumber, actionElement);
                Element propertiesElement = addProperties(tiedNumberElement);
                addProperty("tied_number_name", name, propertiesElement);
                actionElementAdded(actionElement);
            }
        };
        tiedNumber.addNameFieldUpdateListener(nameFieldUpdateListener);
        UpdateListener<Expression<IntegerValue>> valueUpdateListener = new UpdateListener<Expression<IntegerValue>>() {

            @Override
            public void objectUpdated(UpdateEvent<Expression<IntegerValue>> e) {
                IntegerValue value = tiedNumber.getValue();
                String description = "Tied number " + tiedNumber.getId().toString();
                description += " (" + tiedNumber.getIdString() + ")";
                description += " value is now " + value;
                Element actionElement = addActionElement("modify", "change_tied_number_value", description);
                Element tiedNumberElement = createTiedNumberElement(tiedNumber, actionElement);
                Element propertiesElement = addProperties(tiedNumberElement);
                addProperty("tied_number_value", value.toString(), propertiesElement);
                actionElementAdded(actionElement);
            }
        };
        tiedNumber.addUpdateListener(valueUpdateListener);
    }

    public Element createActionElement() {
        Element actionElement = document.createElement("action");
        actionElement.setAttribute("time", Long.toString(System.currentTimeMillis()));
        Element usersElement = createUsersElement();
        actionElement.appendChild(usersElement);
        return actionElement;
    }

    protected Element createActionTypeElement(Element actionElement) {
        Element actionTypeElement = document.createElement("actiontype");
        actionElement.appendChild(actionTypeElement);
        actionTypeElement.setAttribute("logged", "true");
        actionTypeElement.setAttribute("succeed", "true");
        return actionTypeElement;
    }

    protected Element createObjectElement(String shapeId, String shapeType, Element actionElement) {
        Element objectElement = document.createElement("object");
        actionElement.appendChild(objectElement);
        objectElement.setAttribute("id", shapeId);
        objectElement.setAttribute("type", shapeType);
        return objectElement;
    }

    protected Element createModelElement(String id, Element actionElement) {
        Element objectElement = document.createElement("object");
        actionElement.appendChild(objectElement);
        objectElement.setAttribute("id", id);
        objectElement.setAttribute("type", "model");
        return objectElement;
    }

    protected Element createTiedNumberElement(TiedNumberExpression<IntegerValue> tiedNumber, Element actionElement) {
        Element objectElement = document.createElement("object");
        actionElement.appendChild(objectElement);
        objectElement.setAttribute("type", "tied_number");
        objectElement.setAttribute("id", tiedNumber.getIdString());
        objectElement.setAttribute("value", tiedNumber.getValue().toString());
        return objectElement;
    }

    public void addDescription(String description, Element contentElement) {
        Element descriptionElement = document.createElement("description");
        contentElement.appendChild(descriptionElement);
        CDATASection cDATASection = document.createCDATASection(description);
        descriptionElement.appendChild(cDATASection);
    }

    public Element addActionElementForShape(BlockShape shape, String classification, String type, String[] attributeNameValues, String description) {
        Element actionElement = createActionElement();
        Element[] elements = addActionElement(classification, type, attributeNameValues, description, false, actionElement);
        String shapeId = shape.getId().toString();
        String shapeType = shape.getIdName();
        Element objectElement = elements[0];
        objectElement.setAttribute("id", shapeId);
        objectElement.setAttribute("type", shapeType);
        return actionElement;
    }

    private Element addActionElementForExpression(String id, String classification, String type, String[] attributeNameValues, String description) {
        Element actionElement = createActionElement();
        addActionElement(classification, type, attributeNameValues, description, false, actionElement);
        createObjectElement(id, "expression", actionElement);
        return actionElement;
    }

    @Deprecated
    public Element addActionElement(String classification, String type, String description) {
        Element actionElement = createActionElement();
        Element actionTypeElement = createActionTypeElement(actionElement);
        actionTypeElement.setAttribute("classification", classification);
        actionTypeElement.setAttribute("type", type);
        addDescription(description, actionElement);
        return actionElement;
    }

    public Element[] addActionElement(String classification, String type, String[] attributeNameValues, String description, boolean testing, Element actionElement) {
        Element actionTypeElement = createActionTypeElement(actionElement);
        actionTypeElement.setAttribute("classification", classification);
        actionTypeElement.setAttribute("type", type);
        actionTypeElement.setAttribute("logged", Boolean.toString(!testing));
        Element objectElement = document.createElement("object");
        actionElement.appendChild(objectElement);
        Element objectPropertiesElement = document.createElement("properties");
        objectElement.appendChild(objectPropertiesElement);
        objectPropertiesElement.appendChild(createPropertyElement("TOOL", "eXpresser"));
        Element contentElement = document.createElement("content");
        actionElement.appendChild(contentElement);
        addDescription(description, contentElement);
        Element contentPropertiesElement = document.createElement("properties");
        contentElement.appendChild(contentPropertiesElement);
        contentPropertiesElement.appendChild(createPropertyElement("TOOL", "eXpresser"));
        contentPropertiesElement.appendChild(createPropertyElement("INDICATOR_TYPE", "activity"));
        for (int i = 0; i < attributeNameValues.length; i += 2) {
            if (attributeNameValues[i + 1] != null) {
                Element propertyElement = createPropertyElement(attributeNameValues[i], attributeNameValues[i + 1]);
                contentPropertiesElement.appendChild(propertyElement);
                propertyElement = createPropertyElement(attributeNameValues[i], attributeNameValues[i + 1]);
                objectPropertiesElement.appendChild(propertyElement);
            }
        }
        Element[] result = { objectElement, objectPropertiesElement, contentPropertiesElement };
        return result;
    }

    public Element addPropertyFromAttribute(Attribute<?> attribute, ValueSource<?> valueSource, Element propertiesElement) {
        return addProperty(attribute.getName(), valueSource.getExpression(), propertiesElement);
    }

    public Element addProperty(String name, Expression<?> expression, Element propertiesElement) {
        return addProperty(name, expression.toString(), propertiesElement);
    }

    public Element addProperty(String name, String value, Element propertiesElement) {
        Element propertyElement = document.createElement("property");
        propertiesElement.appendChild(propertyElement);
        propertyElement.setAttribute("name", name);
        propertyElement.setAttribute("value", value);
        return propertyElement;
    }

    public Element addProperties(Element objectElement) {
        Element propertiesElement = document.createElement("properties");
        objectElement.appendChild(propertiesElement);
        return propertiesElement;
    }

    protected Element createActionElementShapeAttributeChange(Attribute<?> attribute, BlockShape shape) {
        ValueSource<?> valueSource = attribute.getValueSource();
        if (valueSource == null) {
            return null;
        }
        String shapeId = shape.getId().toString();
        String shapeType = shape.getIdName();
        String description = new String();
        description += "Attribute '" + attribute.getID() + "'";
        description += " ('" + attribute.getName() + "')";
        description += " has changed from '";
        description += attribute.getPreviousValue();
        description += "' to '";
        description += attribute.getValue();
        description += "'.";
        Element actionElement = addActionElementForShape(shape, "modify", "indicator", null, description);
        Element objectElement = createObjectElement(shapeId, shapeType, actionElement);
        Element propertiesElement = addProperties(objectElement);
        addPropertyFromAttribute(attribute, valueSource, propertiesElement);
        return actionElement;
    }

    /**
     * @param eventType - CommonFormatEventType (either CREATION, UPDATE, or DELETION)
     * @param shape
     * @param attributeNameValues 
     * @return XML Common Format action Element for creating, updating, or deleting a shape
     */
    public Element actionElementForShapeCreation(CommonFormatEventType eventType, BlockShape shape, String userName, String[] attributeNameValues) {
        String description = "";
        switch(eventType) {
            case CREATION:
                description = MiGenUtilities.getLocalisedMessage("UserCreatedObject");
                break;
            case UPDATE:
                description = MiGenUtilities.getLocalisedMessage("UserUpdatedObject");
                break;
            case DELETION:
                description = MiGenUtilities.getLocalisedMessage("UserRemovedObject");
                break;
        }
        description = description.replace("***user***", userName);
        description = description.replace("***object***", shape.getDescription(true));
        switch(eventType) {
            case CREATION:
                return addActionElementForShape(shape, "CREATE", "indicator", attributeNameValues, description);
            case UPDATE:
                return addActionElementForShape(shape, "MODIFY", "indicator", attributeNameValues, description);
            case DELETION:
                return addActionElementForShape(shape, "DELETE", "indicator", attributeNameValues, description);
        }
        return null;
    }

    /**
     * @param created - true if expression just created
     * @param id
     * @param expressionString -- String version of an expression (typically in HTML)
     * @return XML Common Format action Element for creating or updating an expression
     */
    public Element actionElementForExpressionCreationOrUpdate(boolean created, String id, String expressionString) {
        String description = "expression with id " + id + ". Expression is " + expressionString;
        if (created) {
            return addActionElementForExpression(id, "create", "indicator", null, "Created " + description);
        } else {
            return addActionElementForExpression(id, "modify", "indicator", null, "Updated " + description);
        }
    }

    public Element createPropertyElement(String attributeName, String attributeValue) {
        Element propertyElement = document.createElement("property");
        propertyElement.setAttribute("name", attributeName);
        propertyElement.setAttribute("value", attributeValue);
        return propertyElement;
    }
}
