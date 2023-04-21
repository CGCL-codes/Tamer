package com.rugl.config;

import com.rugl.input.KeyPress;
import com.ryanm.config.serial.ConfiguratorCodec;
import com.ryanm.config.serial.ParseException;

/**
 * @author ryanm
 */
public class KeyPressCodec implements ConfiguratorCodec<KeyPress> {

    @Override
    public KeyPress decode(String encoded, Class type) throws ParseException {
        assert type.equals(getType());
        KeyPress kp = KeyPress.fromString(encoded);
        if (kp == null) {
            throw new ParseException("No key names recognised");
        }
        return kp;
    }

    @Override
    public String encode(KeyPress value) {
        return value.toString();
    }

    @Override
    public String getDescription() {
        return "A \"+\"-separated list of key names";
    }

    @Override
    public Class getType() {
        return KeyPress.class;
    }
}
