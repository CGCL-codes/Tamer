package org.modelibra.util;

import java.io.IOException;
import java.util.PropertyResourceBundle;

/**
 * @author Dzenan Ridjanovic
 * @version 2005-05-27
 */
public class TextRes_fr extends PropertyResourceBundle {

    TextRes_fr() throws IOException {
        super(TextRes_fr.class.getResourceAsStream("TextRes_fr.properties"));
    }
}
