package org.freehold.servomaster.device.impl.serial;

import org.freehold.servomaster.device.model.AbstractMeta;

/**
 * Serial controller metadata.
 *
 * <p>
 *
 * All serial controllers share at least two features: they are not
 * disconnectable and they support <code>controller/protocol/serial</code>;
 * and one property: <code>controller/protocol/serial/speed</code>, defaults
 * to 2400.
 *
 * <p>
 *
 * <strong>NOTE:</strong> Though the serial controllers are declared not
 * disconnectable, in reality I'm yet to see a controller that gives any
 * feedback. For a driver, it doesn't make a difference if there's a device
 * connected to a serial port or not. Well, if such a device ever appears,
 * this implementation will not break it - all you have to do is override
 * the feature in the device meta class.
 *
 * @author Copyright &copy; <a href="mailto:vt@freehold.crocodile.org">Vadim Tkachenko</a> 2001-2005
 * @version $Id: SerialMeta.java,v 1.2 2006-12-14 09:17:08 vtt Exp $
 */
public abstract class SerialMeta extends AbstractMeta {

    protected SerialMeta() {
        features.put("controller/allow_disconnect", new Boolean(false));
        features.put("controller/protocol/serial", new Boolean(true));
        properties.put("controller/protocol/serial/speed", "2400");
    }
}
