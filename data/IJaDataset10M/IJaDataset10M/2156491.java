package de.definitives.creoleconverter.document;

import de.definitives.creoleconverter.renderer.Renderer;

/**
 * Represents a table.
 *
 * @author Christian Helmbold
 */
public class Table extends Container implements BlockElement {

    public void renderWith(Renderer renderer) {
        renderer.render(this);
    }
}
