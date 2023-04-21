package org.wwweeeportal.util.ws.rs.parameter;

import org.springframework.core.convert.*;
import org.wwweeeportal.util.*;

/**
 * An {@link Character} parameter.
 * 
 * @see StringUtil#STRING_CHARACTER_CONVERTER
 */
public class CharacterParam extends AbstractConvertedParam<Character> {

    /**
   * Construct a new <code>CharacterParam</code>.
   * 
   * @param value The String value of this parameter.
   * @throws ConversionException If there was a problem during the conversion.
   */
    public CharacterParam(final String value) throws ConversionException {
        super(StringUtil.STRING_CHARACTER_CONVERTER, value);
        return;
    }
}
