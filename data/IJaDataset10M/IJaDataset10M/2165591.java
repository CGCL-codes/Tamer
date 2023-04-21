package org.amplafi.json;

import java.io.Serializable;

/**
 * @author patmoore
 *
 */
public interface JsonConstruct extends Serializable {

    /**
         * JSONObject.NULL is equivalent to the value that JavaScript calls null,
         * whilst Java's null is equivalent to the value that JavaScript calls
         * undefined.
         */
    public static final class Null implements Serializable {

        /**
             * There is only intended to be a single instance of the NULL object,
             * so the clone method returns itself.
             * @return     NULL.
             */
        @Override
        protected final Object clone() {
            return this;
        }

        /**
             * A Null object is equal to the null value and to itself.
             * @param object    An object to test for nullness.
             * @return true if the object parameter is the JSONObject.NULL object
             *  or null.
             */
        @Override
        public boolean equals(Object object) {
            return object == null || object == this || object instanceof Null;
        }

        /**
             * Get the "null" string value.
             * @return The string "null".
             */
        @Override
        public String toString() {
            return "null";
        }
    }

    /**
     *
     * @return true if there is no data present.
     */
    public boolean isEmpty();
}
