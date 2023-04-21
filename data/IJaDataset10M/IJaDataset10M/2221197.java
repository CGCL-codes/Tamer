package org.vramework.vow.model;

import org.vramework.commons.resources.DefaultMessage;
import org.vramework.commons.resources.MessageHelper;
import org.vramework.commons.resources.VMessages;
import org.vramework.commons.resources.VResourceBundle;

/**
 * @see VMessages
 * @see VResourceBundle
 * @author tmahring
 */
public class ModelErrors extends DefaultMessage {

    public static ModelErrors AssocationFieldMustHaveTypeCollection = new ModelErrors();

    static {
        MessageHelper.initClassFields(ModelErrors.class);
    }
}
