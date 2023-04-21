package org.softsmithy.lib.swing;

import java.util.*;
import org.softsmithy.lib.util.TypesafeEnum;

/**
 * A typesafe enum class. Defines some icon types. <br>
 * Note: This class may change package in a future version.
 *
 * @author    Florian Brunner
 */
public class IconType extends TypesafeEnum {

    /**
   * Creates a new icon type.
   *
   * @param name  the name of this icon type
   */
    private IconType(String name) {
        super(name);
    }

    /**
   * Large icon type.
   */
    public static final IconType LARGE_ICON = new IconType("largeIcon");

    /**
   * Small icon type.
   */
    public static final IconType SMALL_ICON = new IconType("smallIcon");

    /**
   * No icon type.
   */
    public static final IconType NO_ICON = new IconType("noIcon");

    private static final IconType[] PRIVATE_VALUES = { LARGE_ICON, SMALL_ICON, NO_ICON };

    /**
   * A list of all defined icon types.
   */
    public static final List VALUES = Collections.unmodifiableList(Arrays.asList(PRIVATE_VALUES));
}
