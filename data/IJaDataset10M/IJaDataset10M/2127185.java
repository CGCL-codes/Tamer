package editor.view.tab.freightTraffic.cargos.action;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import base.gui.action.UpdateAction;
import editor.model.xml.datasheet.CargoElement;
import editor.model.xml.datasheet.ShipperElement;
import editor.view.UpdateView;
import editor.view.action.AbstractLocaleAction;

/**
 * Abstract class for removing a cargo element from a shipper. Abstract because the cargo can
 * be a distributed or a receiving cargo.
 */
public abstract class AbstractRemoveCargoAction extends AbstractLocaleAction {

    private static final String ACTION_NAME = "action.remove_cargo";

    private final UpdateView oFrame;

    private final ShipperElement oShipper;

    private final CargoElement oCargo;

    /**
     * Constructor
     *
     * @param frame   the view which must be updated after removing the cargo
     * @param shipper the shipper from which the cargo is removed
     * @param cargo   the cargo to be removed
     */
    public AbstractRemoveCargoAction(final UpdateView frame, final ShipperElement shipper, final CargoElement cargo) {
        super(ACTION_NAME);
        oFrame = frame;
        oShipper = shipper;
        oCargo = cargo;
    }

    /**
     * @return the view
     */
    protected final UpdateView getView() {
        return oFrame;
    }

    /**
     * @return the shipper
     */
    protected final ShipperElement getShipper() {
        return oShipper;
    }

    /**
     * @return the cargo
     */
    protected final CargoElement getCargo() {
        return oCargo;
    }

    /**
     * updates the text to the current locale
     *
     * @see UpdateAction Interface
     */
    @Override
    public final void updateLocale() {
        super.updateLocale();
        putValue(Action.MNEMONIC_KEY, null);
    }

    /**
     * abstract function to be implemented by subclasses
     *
     * @param e ActionEvent
     */
    public abstract void actionPerformed(final ActionEvent e);
}
