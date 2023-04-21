package net.sf.hippopotam.util.message.spring;

import java.util.Locale;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;

final class NullMessageSource {

    static final MessageSource instance = new MessageSource() {

        public String getMessage(String key, Object[] objects, String string1, Locale locale) {
            return key;
        }

        public String getMessage(String key, Object[] objects, Locale locale) throws NoSuchMessageException {
            return key;
        }

        public String getMessage(MessageSourceResolvable messageSourceResolvable, Locale locale) throws NoSuchMessageException {
            return messageSourceResolvable.getDefaultMessage();
        }
    };
}
