package com.ericdaugherty.mail.server.persistence;

import com.ericdaugherty.mail.server.configuration.ConfigurationManager;
import com.ericdaugherty.mail.server.persistence.localDelivery.SimpleFileIOProcessor;
import com.ericdaugherty.mail.server.persistence.localDelivery.TestingFileIOProcessor;

/**
 *
 * @author Andreas Kyrmegalos
 */
public class LocalDeliveryFactory {

    private static LocalDeliveryFactory instance = null;

    private boolean localTestingMode;

    private LocalDeliveryFactory() {
        localTestingMode = ConfigurationManager.getInstance().isLocalTestingMode();
    }

    public static LocalDeliveryFactory getInstance() {
        if (instance == null) {
            instance = new LocalDeliveryFactory();
        }
        return instance;
    }

    public LocalDeliveryProcessor getLocalDeliveryProccessor() {
        return !localTestingMode ? new SimpleFileIOProcessor() : new TestingFileIOProcessor();
    }
}
