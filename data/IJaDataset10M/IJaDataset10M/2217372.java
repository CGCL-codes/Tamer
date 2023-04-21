package com.jeronimo.eko.chatsample;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.jeronimo.eko.core.EkoRuntimeException;
import com.jeronimo.eko.core.config.ISConfigurationStore;
import com.jeronimo.eko.core.config.impl.PropertiesConfigurationStore;
import com.jeronimo.eko.core.guice.EkoCoreGuiceModule;
import com.jeronimo.eko.p2p.guice.EkoP2PGuiceModule;
import com.jeronimo.eko.webui.guice.GuiceContextListener;

/**
 * @author J�r�me Bonnet
 */
public class ChatContextListener extends GuiceContextListener {

    static {
        System.setProperty("simplelog.dev.debug", "true");
    }

    ISConfigurationStore configStore;

    public ChatContextListener() {
        File configFile = new File("config.properties");
        try {
            configStore = new PropertiesConfigurationStore(configFile);
        } catch (FileNotFoundException e) {
            throw new EkoRuntimeException("cannot find file " + configFile.getAbsolutePath(), e);
        } catch (IOException e) {
            throw new EkoRuntimeException(e);
        }
    }

    @Override
    public Module[] getModules() {
        return new Module[] { new EkoCoreGuiceModule(), new EkoP2PGuiceModule(), new AbstractModule() {

            @Override
            protected void configure() {
                bind(ISConfigurationStore.class).toInstance(configStore);
                bind(com.icesoft.faces.async.render.RenderManager.class);
                bind(StartPageBean.class);
                bind(ChatBean.class);
            }
        } };
    }
}
