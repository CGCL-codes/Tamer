package kohary.datamodel.actions;

import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import kohary.datamodel.DatamodelCreator;
import kohary.datamodel.MainFrame;
import kohary.datamodel.PropertiesPanel;
import kohary.datamodel.Selection;
import kohary.datamodel.dapi.Attribute;
import kohary.datamodel.dapi.DataType;
import kohary.datamodel.dapi.Element;

/**
 *
 * @author Godric
 */
public class SetDataTypeAction extends Action {

    public SetDataTypeAction() {
        String name = "Set datatype";
        putValue(NAME, name);
        putValue(SHORT_DESCRIPTION, name);
        putValue(SHORT_DESCRIPTION, "ComboBox");
    }

    public void actionPerformed(ActionEvent e) {
        Selection selection = DatamodelCreator.getInstance().getMainFrame().getModellingBoard().getCanvas().getSelection();
        MainFrame mainFrame = DatamodelCreator.getInstance().getMainFrame();
        Element selectedElement = selection.get(0);
        Attribute attribute = (Attribute) DatamodelCreator.getInstance().getDataModelSelectManager().getCurrentDataModel().getAttributeByElement(selectedElement);
        PropertiesPanel properties = new PropertiesPanel();
        DataType s = (DataType) JOptionPane.showInputDialog(mainFrame, "Current datatype for this attribute is:\n", "Set datatype", JOptionPane.PLAIN_MESSAGE, null, DataType.values(), attribute.getInput().getDataType());
        attribute.getInput().setDataType(s);
    }
}
