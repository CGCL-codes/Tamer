package com.jeronimo.eko.core.config;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * This exception is thrown when the configuration manager has failed to configure all the instances<br>
 * @author J�r�me Bonnet
 */
@SuppressWarnings("serial")
public class ConfigManagerFailedExecption extends Exception {

    Set<MissingKey> missingConfigKeys = new HashSet<MissingKey>();

    Set<InvalidKey> invalidConfigKeys = new HashSet<InvalidKey>();

    static String CRLF = System.getProperty("line.separator");

    public ConfigManagerFailedExecption(String message, Collection<MissingKey> missingKeys, Collection<InvalidKey> invalidKeys) {
        super(message);
        addMissingKeys(missingKeys);
        addInvalidKeys(invalidKeys);
    }

    void addMissingKey(MissingKey missingKey) {
        missingConfigKeys.add(missingKey);
    }

    void addInvalidKey(InvalidKey invalidKey) {
        invalidConfigKeys.add(invalidKey);
    }

    void addMissingKeys(Collection<MissingKey> missingKeys) {
        missingConfigKeys.addAll(missingKeys);
    }

    void addInvalidKeys(Collection<InvalidKey> invalidKeys) {
        invalidConfigKeys.addAll(invalidKeys);
    }

    public Set<MissingKey> getMissingKeys() {
        return Collections.unmodifiableSet(missingConfigKeys);
    }

    public Set<InvalidKey> getInvalidKeys() {
        return Collections.unmodifiableSet(invalidConfigKeys);
    }

    @Override
    public String getMessage() {
        return getMessage(false);
    }

    public String getMessage(boolean withStackTraces) {
        StringBuilder sb = new StringBuilder(super.getMessage());
        sb.append(CRLF);
        if (missingConfigKeys.isEmpty() == false) {
            sb.append("Missing Config keys:" + CRLF);
            sb.append("---------------------" + CRLF);
            for (MissingKey mk : missingConfigKeys) {
                sb.append(mk.toString() + CRLF);
            }
            sb.append(CRLF);
        }
        if (invalidConfigKeys.isEmpty() == false) {
            sb.append("Invalid Config keys:" + CRLF);
            sb.append("---------------------" + CRLF);
            for (InvalidKey ik : invalidConfigKeys) {
                sb.append(ik.toString() + CRLF);
                if (withStackTraces) {
                    StringWriter sw = new StringWriter();
                    ik.originalException.printStackTrace(new PrintWriter(sw));
                    sb.append(sw.getBuffer().toString() + CRLF);
                }
            }
            sb.append(CRLF);
        }
        return sb.toString();
    }

    public static class MissingKey {

        String key;

        String originKey;

        Class<?> originClass;

        String originMethod;

        public MissingKey(String key, Class<?> originClass, String originKey, String originMethod) {
            super();
            this.key = key;
            this.originClass = originClass;
            this.originKey = originKey;
            this.originMethod = originMethod;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((key == null) ? 0 : key.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;
            if (getClass() != obj.getClass()) return false;
            MissingKey other = (MissingKey) obj;
            if (key == null) {
                if (other.key != null) return false;
            } else if (!key.equals(other.key)) return false;
            return true;
        }

        public String getKeyLocationMessage() {
            StringBuilder sb = new StringBuilder("'" + key + "' ");
            if (originKey.equals(key) == false) sb.append(" ( originally '" + originKey + "') ");
            sb.append(" from " + originClass.getCanonicalName() + ":" + originMethod);
            return sb.toString();
        }

        @Override
        public String toString() {
            return "Missing configuration key " + getKeyLocationMessage();
        }
    }

    public static class InvalidKey extends MissingKey {

        String invalidValue;

        ConfigKeyInvalidValueException originalException;

        public InvalidKey(String key, Class<?> originClass, String originKey, String originMethod, String invalidValue, ConfigKeyInvalidValueException originalException) {
            super(key, originClass, originKey, originMethod);
            this.invalidValue = invalidValue;
            this.originalException = originalException;
        }

        @Override
        public int hashCode() {
            return System.identityHashCode(this);
        }

        @Override
        public boolean equals(Object obj) {
            return this == obj;
        }

        @Override
        public String toString() {
            return "invalid value '" + invalidValue + "' for key " + getKeyLocationMessage() + "  --> " + originalException.getMessage();
        }
    }
}
