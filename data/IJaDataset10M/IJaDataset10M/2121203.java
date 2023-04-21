package org.mbari.vars.mini;

import com.google.inject.Inject;
import javax.swing.ImageIcon;
import org.mbari.vars.services.DataCreationService;

/**
 *
 * @author brian
 */
public class AddEatingAssociationButton extends AddAssociationButton {

    @Inject
    public AddEatingAssociationButton(DataCreationService dataCreationService) {
        super(dataCreationService);
        final AddAssociationAction action = getAddAssociationAction();
        action.setLinkName("eating");
        action.setToConcept("marine organism");
        action.setLinkValue("nil");
        setToolTipText("Note that an animal is eating");
        setIcon(new ImageIcon(getClass().getResource("/images/32px/Food-Chain.png")));
        setEnabled(false);
    }
}
