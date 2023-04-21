package kohary.datamodel;

import java.awt.Color;
import java.util.Set;
import kohary.datamodel.dapi.Element;
import kohary.datamodel.dapi.Text;

/**
 *
 * @author Godric
 */
public class Selection extends Elements {

    @Override
    public void clear() {
        ModellingBoard modelingBoard = DatamodelCreator.getInstance().getMainFrame().getModellingBoard();
        for (Element element : this) {
            modelingBoard.getCanvas().getDrawingOptions().getColorSetting().remove(element);
        }
        modelingBoard.getFontSelectorPanel().setVisible(false);
        super.clear();
        setEnableRemoveAction();
    }

    @Override
    public void replaceWith(Element elementR) {
        for (Element element : this) {
            DatamodelCreator.getInstance().getMainFrame().getModellingBoard().getCanvas().getDrawingOptions().getColorSetting().remove(element);
        }
        super.clear();
        add(elementR);
        DatamodelCreator.getInstance().getMainFrame().getModellingBoard().getCanvas().getDrawingOptions().getColorSetting().put(elementR, new Color(250, 250, 250));
        setEnableRemoveAction();
    }

    @Override
    public void add(Element element) {
        ModellingBoard modelingBoard = DatamodelCreator.getInstance().getMainFrame().getModellingBoard();
        if (super.isEmpty() && element instanceof Text) {
            modelingBoard.getFontSelectorPanel().setTextElement((Text) element);
            modelingBoard.revalidate();
        } else {
            modelingBoard.getFontSelectorPanel().setVisible(false);
        }
        super.add(element);
        modelingBoard.getCanvas().getDrawingOptions().getColorSetting().put(element, new Color(250, 250, 250));
        setEnableRemoveAction();
    }

    @Override
    public void addAll(Set<? extends Element> elements) {
        ModellingBoard modelingBoard = DatamodelCreator.getInstance().getMainFrame().getModellingBoard();
        modelingBoard.getFontSelectorPanel().setVisible(false);
        super.addAll(elements);
        setEnableRemoveAction();
        for (Element element : elements) {
            modelingBoard.getCanvas().getDrawingOptions().getColorSetting().put(element, new Color(250, 250, 250));
        }
    }

    private void setEnableRemoveAction() {
        if (super.isEmpty()) DatamodelCreator.getInstance().getMainFrame().deleteAction.setEnabled(false); else {
            DatamodelCreator.getInstance().getMainFrame().deleteAction.setEnabled(true);
        }
    }
}
