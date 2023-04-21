package com.lizardtech.djvu.anno;

import com.lizardtech.djvu.*;
import java.util.*;

/**
 * Implements text map areas.
 */
public class Text extends Rect {

    /** Tag name for this map type. */
    public static final String TEXT_TAG = "text";

    /**
   * Creates a new Oval object.
   */
    public Text() {
    }

    /**
   * Query the map type.
   *
   * @return MAP_TEXT
   */
    public int getMapType() {
        return MAP_TEXT;
    }

    /**
   * Creates an instance of Text with the options interherited from the
   * specified reference.
   * 
   * @param ref Object to interherit DjVuOptions from.
   * 
   * @return a new instance of Text.
   */
    public static Text createText(final DjVuInterface ref) {
        final DjVuOptions options = ref.getDjVuOptions();
        return (Text) create(options, options.getAnnoTextClass(), Text.class);
    }

    /**
   * Returns "text"
   *
   * @return TEXT_TAG
   */
    public String getShapeName() {
        return TEXT_TAG;
    }
}
