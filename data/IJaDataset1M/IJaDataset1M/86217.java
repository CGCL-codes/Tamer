package com.spring.rssReader.validator;

import com.spring.rssReader.Channel;
import com.spring.rssReader.jdbc.IChannelController;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * @author Ronald Haring
 * Date: 4-jan-2004
 * Time: 18:46:56
 * To change this template use Options | File Templates.
 */
public class ChannelValidator implements Validator {

    private IChannelController channelController;

    public ChannelValidator() {
    }

    public boolean supports(Class clazz) {
        return Channel.class.isAssignableFrom(clazz);
    }

    public void validate(Object obj, Errors errors) {
        Channel channel = (Channel) obj;
        if (channel.getUrl() == null || channel.getUrl().equals("")) {
            errors.rejectValue("url", "required", null, "url is required.");
        } else if (!channelController.isUniqueUrl(channel)) {
            errors.rejectValue("url", "notUnique", null, "url is not unique.");
        }
    }

    public IChannelController getChannelController() {
        return channelController;
    }

    public void setChannelController(IChannelController channelController) {
        this.channelController = channelController;
    }
}
