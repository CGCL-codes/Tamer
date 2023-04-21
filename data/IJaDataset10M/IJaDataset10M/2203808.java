package org.simulare;

import java.util.*;

/**
 * Defines the monitor for name changes.
 */
public interface NameListener extends EventListener {

    void nameChanged(NameEvent e);
}
