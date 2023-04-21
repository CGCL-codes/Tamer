package com.liferay.portal.captcha;

import com.liferay.util.PwdGenerator;
import java.util.Properties;
import nl.captcha.text.TextProducer;

/**
 * <a href="PinNumberTextProducer.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class PinNumberTextProducer implements TextProducer {

    public PinNumberTextProducer() {
    }

    public void setProperties(Properties props) {
    }

    public String getText() {
        return PwdGenerator.getPinNumber();
    }
}
