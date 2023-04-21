package org.fest.swing.keystroke;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.KeyStroke;
import static java.awt.event.KeyEvent.*;

/**
 * Understands a default mapping of characters and <code>{@link KeyStroke}</code>s.
 * 
 * @author Alex Ruiz
 */
public class DefaultKeyStrokeMappingProvider implements KeyStrokeMappingProvider {

    /**
   * Returns the default mapping of characters and <code>{@link KeyStroke}</code>s. This provider will only return
   * the mappings for following keys:
   * <ul>
   * <li>Escape</li>
   * <li>Backspace</li>
   * <li>Delete</li>
   * <li>Enter</li>
   * </ul>
   * @return the default mapping of characters and <code>KeyStroke</code>s
   */
    public Collection<KeyStrokeMapping> keyStrokeMappings() {
        List<KeyStrokeMapping> mappings = new ArrayList<KeyStrokeMapping>();
        mappings.add(new KeyStrokeMapping('\b', VK_BACK_SPACE, NO_MASK));
        mappings.add(new KeyStrokeMapping('', VK_DELETE, NO_MASK));
        mappings.add(new KeyStrokeMapping('', VK_ESCAPE, NO_MASK));
        mappings.add(new KeyStrokeMapping('\n', VK_ENTER, NO_MASK));
        mappings.add(new KeyStrokeMapping('\r', VK_ENTER, NO_MASK));
        return mappings;
    }
}
