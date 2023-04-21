package UCM;

import Views.ViewDisplayEditFlight;

/**
 *
 * @author Joshua King
 */
public class UCDisplayEditFlight implements UCController {

    private ViewDisplayEditFlight view;

    /**
     * Initialises the view.
     * @param view
     */
    public UCDisplayEditFlight(ViewDisplayEditFlight view) {
        this.view = view;
    }

    /**
     * Precondition: user is logged in as manager
     * 1. Users clicks on the edit flight.
     * 2. System displays edit flight view.
     * Postcondition: user is on edit flight page.
     */
    public void run() {
        view.setEditFlightVisible();
    }
}
