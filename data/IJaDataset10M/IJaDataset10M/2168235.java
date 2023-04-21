package org.dcm4cheri.image;

class ConfigurationException extends RuntimeException {

    ConfigurationException(String msg) {
        super(msg);
    }

    ConfigurationException(String msg, Exception x) {
        super(msg, x);
    }
}
