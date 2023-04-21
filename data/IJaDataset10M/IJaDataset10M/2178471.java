package calclipse.core.gui.skin;

import java.util.Collection;

/**
 * This interface represents a GUI
 * that supports customized graphics,
 * such as themes.
 * A skinnable must be registered with the
 * {@link SkinManager} before a
 * {@link Skin} can be applied to it.
 * @author T. Sommerland
 */
public interface Skinnable {

    String getSelector();

    Collection<? extends Stylable> getStylables();
}
