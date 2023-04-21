package net.sf.hippopotam.util.message.spring;

import net.sf.hippopotam.util.message.MessageProvider;
import net.sf.hippopotam.util.message.MessageProviderFactory;

public class SpringMessageProviderFactory implements MessageProviderFactory {

    @Override
    public MessageProvider createMessageProvider() {
        return new SpringMessageProvider();
    }
}
