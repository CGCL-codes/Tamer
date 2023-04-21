package fr.itris.glips.rtda.action;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import org.w3c.dom.*;
import fr.itris.glips.rtda.*;
import fr.itris.glips.rtda.animaction.*;
import fr.itris.glips.rtda.animaction.Action;
import fr.itris.glips.rtda.components.picture.*;
import fr.itris.glips.rtda.jwidget.*;
import fr.itris.glips.rtda.test.*;

/**
 * the superclass of the send command actions
 * @author Jordi SUC
 */
public abstract class AbstractSendCommand extends Action {

    /**
	 * the string names
	 */
    protected static String tagToWriteName = "tagToWrite", referenceTagName = "referenceTag", invalidValueName = "invalidValue", defaultValueName = "defaultValue", returnToInitialValueMethodName = "returnToInitialValueMethod", autoName = "auto", mouseUpEventName = "mouseUpEvent", valueName = "value", commandToSendName = "commandToSend", usedAttName = "used";

    /**
	 * the constructor of the class
	 * @param picture the svg picture this action is linked to
	 * @param projectName the name of the project this action is linked to
	 * @param parent the top level container
	 * @param component the component on which the action is registered
	 * @param actionObject the object to which the action applies, if it is not the provided component
	 * @param actionElement the dom element defining the action
	 * @param jwidgetRuntime the jwidget runtime object, if this action is linked to a jwidget
	 */
    public AbstractSendCommand(SVGPicture picture, String projectName, Container parent, JComponent component, Object actionObject, Element actionElement, JWidgetRuntime jwidgetRuntime) {
        super(picture, projectName, parent, component, actionObject, actionElement, jwidgetRuntime);
        computeRightsForTagsModif();
    }

    /**
	 * initializes this action object
	 */
    protected void initializeAction() {
        dataNames.add(actionElement.getAttribute(tagToWriteName));
        dataNames.add(actionElement.getAttribute(referenceTagName));
        if (picture.getMainDisplay().isTestVersion()) {
            LinkedList<String> referenceTagValues = new LinkedList<String>();
            LinkedList<String> tagToWriteValues = new LinkedList<String>();
            if (actionElement.hasChildNodes()) {
                Element childElement = null;
                for (Node child = actionElement.getFirstChild(); child != null; child = child.getNextSibling()) {
                    if (child instanceof Element) {
                        childElement = (Element) child;
                        if (childElement.getAttribute(usedAttName).equals(Boolean.toString(true))) {
                            referenceTagValues.add(childElement.getAttribute(valueName));
                            tagToWriteValues.add(childElement.getAttribute(commandToSendName));
                        }
                    }
                }
            }
            tagToWriteValues.add(actionElement.getAttribute(defaultValueName));
            tagToWriteValues.add(actionElement.getAttribute(invalidValueName));
            TestTagInformation info = new TestTagInformation(picture, actionElement.getAttribute(tagToWriteName), tagToWriteValues);
            dataNamesToTestTagInfo.put(actionElement.getAttribute(tagToWriteName), info);
            info = new TestTagInformation(picture, actionElement.getAttribute(referenceTagName), referenceTagValues);
            dataNamesToTestTagInfo.put(actionElement.getAttribute(referenceTagName), info);
        }
        initializeAuthorizationTag();
    }

    @Override
    public void performAction(Object evt) {
        if (isEntitled() && isAuthorized && showConfirmationDialog()) {
            String tagValue = "";
            Object oldObj = getData(actionElement.getAttribute(tagToWriteName));
            String currentTagToWriteValue = null;
            if (oldObj != null) {
                currentTagToWriteValue = oldObj.toString();
            }
            Object obj = getData(actionElement.getAttribute(referenceTagName));
            if (obj != null) {
                String referenceTagValue = obj.toString();
                if (actionElement.hasChildNodes() && !referenceTagValue.equals("")) {
                    Element childElement = null;
                    String value = "";
                    for (Node child = actionElement.getFirstChild(); child != null; child = child.getNextSibling()) {
                        if (child instanceof Element) {
                            childElement = (Element) child;
                            value = AnimationsToolkit.normalizeEnumeratedValue(childElement.getAttribute(valueName));
                            if (referenceTagValue.equals(value)) {
                                tagValue = childElement.getAttribute(commandToSendName);
                                break;
                            }
                        }
                    }
                }
                if (tagValue.equals("")) {
                    tagValue = AnimationsToolkit.normalizeEnumeratedValue(actionElement.getAttribute(defaultValueName));
                }
            } else {
                tagValue = AnimationsToolkit.normalizeEnumeratedValue(actionElement.getAttribute(invalidValueName));
            }
            if (tagValue != null) {
                tagValue = AnimationsToolkit.normalizeEnumeratedValue(tagValue);
            }
            putTagValue(actionElement.getAttribute(tagToWriteName), tagValue);
            String returnToInitialValue = actionElement.getAttribute(returnToInitialValueMethodName);
            if (returnToInitialValue.equals(mouseUpEventName)) {
                final String fcurrentTagToWriteValue = currentTagToWriteValue;
                component.addMouseListener(new MouseAdapter() {

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        ((JComponent) e.getSource()).removeMouseListener(this);
                        putTagValue(actionElement.getAttribute(tagToWriteName), fcurrentTagToWriteValue);
                    }
                });
            } else if (!returnToInitialValue.equals(autoName)) {
                double timer = 0;
                try {
                    timer = Double.parseDouble(returnToInitialValue);
                } catch (Exception ex) {
                    timer = Double.NaN;
                }
                if (!Double.isNaN(timer)) {
                    final String lastValue = currentTagToWriteValue;
                    final String tagToWrite = actionElement.getAttribute(tagToWriteName);
                    TimerTask timerTask = new TimerTask() {

                        @Override
                        public void run() {
                            putTagValue(tagToWrite, lastValue);
                        }
                    };
                    AnimationsHandler.getTimer().schedule(timerTask, (long) (timer * 1000));
                }
            }
        }
    }

    @Override
    public Runnable dataChanged(DataEvent evt) {
        super.dataChanged(evt);
        checkIsAuthorized();
        return null;
    }
}
