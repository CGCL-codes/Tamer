package net.sourceforge.copernicus.client.view.figures;

import net.sourceforge.copernicus.client.model.gef.GefInstance;
import org.eclipse.draw2d.Label;
import org.eclipse.gef.EditPart;
import org.eclipse.swt.graphics.Image;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

public class AssociatorsCompartmentFigure extends EntryCompartmentFigure {

    private static I18n i18n = I18nFactory.getI18n(AssociatorsCompartmentFigure.class);

    private Image associationImage;

    private CollapseControl collapseControl;

    private Label collapsedLabel;

    public AssociatorsCompartmentFigure(EditPart editPart) {
        super(editPart);
        associationImage = createImage("/icons/link_point.gif");
        collapseControl = new CollapseControl(editPart);
        collapsedLabel = new Label(associationImage);
        collapsedLabel.setForegroundColor(DEFAULT_LABEL_COLOR);
    }

    public void setModel(GefInstance model) {
        removeAll();
        collapseControl.setCollapsed(model.isAssociatorsCollapsed());
        if (!model.isAssociatorsCollapsed()) {
            add(collapseControl);
        } else {
            int acount = model.getAssociatorsCount();
            collapsedLabel.setText(i18n.trn("{0} link", "{0} links", acount, new Integer(acount)));
            add(new HorizontalCompartment(getEditPart(), collapseControl, collapsedLabel));
        }
    }
}
