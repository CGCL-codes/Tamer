package pl.org.minions.stigma.client.ui.swing.game.actions.items;

import java.awt.event.ActionEvent;
import pl.org.minions.stigma.client.Client;
import pl.org.minions.stigma.client.requests.PickUpRequest;
import pl.org.minions.utils.i18n.Translated;

/**
 * ItemAction implementation for picking items up.
 */
public class PickUpItemAction extends ItemAction {

    private static final long serialVersionUID = 1L;

    @Translated
    private static String NAME = "Pick up";

    @Translated
    private static String DESCRIPTION = "Pick up the item";

    /**
     * Creates a new instance of PickUpItemAction.
     */
    public PickUpItemAction() {
        super(NAME, DESCRIPTION);
    }

    /** {@inheritDoc} */
    @Override
    public void actionPerformed(ActionEvent e) {
        Client.globalInstance().getPlayerController().playerRequest(new PickUpRequest(getItem().getId()));
    }
}
