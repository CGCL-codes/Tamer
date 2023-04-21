package org.jfonia.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.jfonia.images.ImageConstants;
import org.jfonia.images.ScaledImageIcon;
import org.jfonia.language.DescriptionConstants;
import org.jfonia.language.LabelConstants;
import org.jfonia.language.LanguageResource;

/**
 *
 * @author Rik Bauwens
 */
public class NotationModeAction extends AbstractAction {

    protected NotationModeAction(int iconMaxSize) {
        super(LanguageResource.getInstance().getLabel(LabelConstants.NOTATION_MODE), new ScaledImageIcon(ImageConstants.NOTATION_MODE, LanguageResource.getInstance().getDescription(DescriptionConstants.NOTATION_MODE)).setMaximumSide(iconMaxSize).getImageIcon());
    }

    public void actionPerformed(ActionEvent e) {
    }
}
