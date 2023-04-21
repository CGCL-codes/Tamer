package org.openscience.cdk.io.setting;

import org.openscience.cdk.exception.CDKException;

/**
 * An class for a reader setting which must be of type String.
 *
 * @cdk.module io
 * @cdk.githash
 *
 * @author Egon Willighagen <egonw@sci.kun.nl>
 */
public class StringIOSetting extends IOSetting {

    public StringIOSetting(String name, int level, String question, String defaultSetting) {
        super(name, level, question, defaultSetting);
    }

    /**
     * Sets the setting for a certain question. The setting
     * is of type String, and any string is accepted.
     */
    public void setSetting(String setting) throws CDKException {
        super.setSetting(setting);
    }
}
