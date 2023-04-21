package com.google.gwt.dev.ui;

/**
 * Base class for any UI event that has a callback.
 *
 * @param <C> callback type
 */
public abstract class UiEvent<C extends UiCallback> {

    /**
   * Type token for a UI event.
   * 
   * <p>Any UiEvent subclasses must have exactly one corresponding Type instance
   * created.
   *
   * @param <C> callback type
   */
    public static class Type<C> {

        private final String name;

        protected Type(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
