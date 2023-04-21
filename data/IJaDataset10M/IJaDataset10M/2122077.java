package kohary.datamodel.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import kohary.datamodel.DatamodelCreator;
import kohary.datamodel.commands.Command;
import kohary.datamodel.commands.Commands;
import kohary.datamodel.commands.DeleteAttributeCommand;
import kohary.datamodel.commands.DeleteDesignCommand;
import kohary.datamodel.dapi.Attribute;
import kohary.datamodel.dapi.DImage;
import kohary.datamodel.dapi.DataModel;
import kohary.datamodel.dapi.Design;
import kohary.datamodel.dapi.Element;
import kohary.datamodel.util.GraphicsTools;

/**
 *
 * @author Godric
 */
public class DeleteAction extends Action {

    public DeleteAction() {
        String name = "Delete";
        putValue(NAME, name);
        putValue(SHORT_DESCRIPTION, name);
        putValue(SMALL_ICON, GraphicsTools.getIcon("delete16.gif"));
        putValue(SHORT_DESCRIPTION, "");
        putValue(MNEMONIC_KEY, KeyEvent.VK_D);
        setEnabled(false);
    }

    public void actionPerformed(ActionEvent e) {
        List<Attribute> attributes = DatamodelCreator.getInstance().getDataModelSelectManager().getCurrentDataModel().getAttributes();
        Set<Element> elements = DatamodelCreator.getInstance().getMainFrame().getModellingBoard().getCanvas().getSelection().getElements();
        DataModel datamodel = DatamodelCreator.getInstance().getDataModelSelectManager().getCurrentDataModel();
        if ((attributes != null) && (elements != null) && (datamodel != null)) {
            List<Command> commands = new ArrayList<Command>();
            for (Element element : elements) {
                if (element instanceof Design) {
                    commands.add((Command) new DeleteDesignCommand(datamodel, (Design) element));
                } else {
                    for (Attribute attribute : attributes) {
                        if (attribute.getInput().equals(element)) {
                            commands.add(new DeleteAttributeCommand(attribute, datamodel));
                        }
                    }
                }
            }
            DatamodelCreator.getInstance().getUndoManager().executeCommand(new Commands(commands));
        }
        DatamodelCreator.getInstance().getMainFrame().getModellingBoard().getCanvas().repaint();
    }
}
