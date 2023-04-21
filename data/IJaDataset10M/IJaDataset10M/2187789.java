package de.definitives.creoleconverter.document;

import de.definitives.creoleconverter.renderer.Renderer;

/**
 * Represents italics text.
 *
 * @author Christian Helmbold
 */
public class Italics extends Container {

    public void renderWith(Renderer renderer) {
        renderer.render(this);
    }
}
