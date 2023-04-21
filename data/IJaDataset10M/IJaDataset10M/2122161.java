package fr.insa.rennes.pelias.pcreator.editors.chains.figures;

import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import fr.insa.rennes.pelias.framework.InputType;
import fr.insa.rennes.pelias.pcreator.editors.ChainEditorConstants;

public class SubChainInputFigure extends ImageFigure {

    /**
	 * Description de l'entrée à mettre en tooltip
	 */
    private Label description = new Label();

    public SubChainInputFigure() {
        XYLayout layout = new XYLayout();
        setLayoutManager(layout);
        setSize(new Dimension(ChainEditorConstants.TAILLE_ENTREE_SORTIE, ChainEditorConstants.TAILLE_ENTREE_SORTIE));
        setToolTip(description);
    }

    public void setDescription(String desc) {
        description.setText(desc);
    }

    public void setLayout(Rectangle layout) {
        getParent().setConstraint(this, layout);
    }

    public void setType(InputType type) {
        if (type == InputType.UserParameter) {
            setImage(ChainEditorConstants.ICON_CALL_PARAM);
        } else if (type == InputType.Batch) {
            setImage(ChainEditorConstants.ICON_CALL_BATCH);
        }
    }
}
