package org.sgodden.echo.ext20.buttons;

import org.sgodden.echo.ext20.Button;

/**
 * A button representing an action to
 * remove something from something.
 * @author Lloyd Colling
 */
@SuppressWarnings("serial")
public class RemoveButton extends Button {

    public RemoveButton() {
        super();
        setActionCommand("REMOVE");
        setCssClass("remove-button");
    }
}
