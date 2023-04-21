package fr.soleil.mambo.actions.view;

import java.awt.event.ActionEvent;
import java.util.Enumeration;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.Action;
import fr.soleil.mambo.components.renderers.VCTreeRenderer;
import fr.soleil.mambo.components.view.ExpressionTree;
import fr.soleil.mambo.components.view.VCAttributesPropertiesTree;
import fr.soleil.mambo.containers.view.dialogs.AttributesPlotPropertiesPanel;
import fr.soleil.mambo.containers.view.dialogs.AttributesPlotPropertiesTab;
import fr.soleil.mambo.containers.view.dialogs.ExpressionTab;
import fr.soleil.mambo.data.view.ExpressionAttribute;
import fr.soleil.mambo.data.view.ViewConfiguration;
import fr.soleil.mambo.data.view.ViewConfigurationAttribute;
import fr.soleil.mambo.data.view.ViewConfigurationAttributePlotProperties;
import fr.soleil.mambo.data.view.ViewConfigurationAttributeProperties;
import fr.soleil.mambo.data.view.ViewConfigurationAttributes;

public class VCSetAxisAttributesGroupProperties extends AbstractAction {

    private boolean expression;

    /**
     * @param name
     */
    public VCSetAxisAttributesGroupProperties(String name, boolean _expression) {
        putValue(Action.NAME, name);
        putValue(Action.SHORT_DESCRIPTION, name);
        this.expression = _expression;
    }

    public void actionPerformed(ActionEvent actionEvent) {
        setAttributsAxisChoiceAction();
    }

    public void setAttributsAxisChoiceAction() {
        if (expression) {
            ExpressionTree tree = ExpressionTree.getInstance();
            Vector attributes = tree.getSelectedAttributes();
            if (attributes == null) {
                return;
            }
            AttributesPlotPropertiesPanel panel = ExpressionTab.getInstance().getPropertiesPanel();
            int axisChoice = panel.getAxisChoice();
            boolean hidden = panel.isHidden();
            Enumeration enumeration = attributes.elements();
            while (enumeration.hasMoreElements()) {
                ExpressionAttribute next = (ExpressionAttribute) enumeration.nextElement();
                ViewConfigurationAttributePlotProperties properties = next.getProperties();
                properties.setAxisChoice(axisChoice);
                properties = null;
                next = null;
            }
        } else {
            VCAttributesPropertiesTree tree = VCAttributesPropertiesTree.getInstance();
            Vector attributes = tree.getListOfAttributesToSet();
            if (attributes == null) {
                return;
            }
            AttributesPlotPropertiesPanel panel = AttributesPlotPropertiesTab.getInstance().getPropertiesPanel();
            int axisChoice = panel.getAxisChoice();
            boolean hidden = panel.isHidden();
            ViewConfiguration currentVC = ViewConfiguration.getCurrentViewConfiguration();
            ViewConfigurationAttributes currentVCAttributes = currentVC.getAttributes();
            Enumeration enumeration = attributes.elements();
            while (enumeration.hasMoreElements()) {
                ViewConfigurationAttribute next = (ViewConfigurationAttribute) enumeration.nextElement();
                ViewConfigurationAttribute currArribute = (ViewConfigurationAttribute) currentVCAttributes.getAttribute(next.getCompleteName());
                ViewConfigurationAttributePlotProperties currAttribPlotProp = currArribute.getProperties().getPlotProperties();
                ViewConfigurationAttributePlotProperties nextPlotProperties = new ViewConfigurationAttributePlotProperties(currAttribPlotProp.getViewType(), axisChoice, currAttribPlotProp.getBar(), currAttribPlotProp.getCurve(), currAttribPlotProp.getMarker(), currAttribPlotProp.getTransform(), hidden);
                ViewConfigurationAttributeProperties nextProperties = new ViewConfigurationAttributeProperties();
                nextProperties.setPlotProperties(nextPlotProperties);
                next.setProperties(nextProperties);
                next.setFactor(currArribute.getFactor());
                currentVCAttributes.addAttribute(next);
            }
            tree.setCellRenderer(new VCTreeRenderer());
        }
    }
}
