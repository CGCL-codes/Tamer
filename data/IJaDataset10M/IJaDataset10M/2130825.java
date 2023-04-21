package org.jowidgets.util.maybe;

public final class Nothing {

    private static IMaybe<?> nothing = new IMaybe<Object>() {

        @Override
        public Object getValue() {
            throw new IllegalStateException("Can not get a value from nothing");
        }

        @Override
        public Object getValueOrElse(final Object defaultValue) {
            return defaultValue;
        }

        @Override
        public boolean isNothing() {
            return true;
        }

        @Override
        public boolean isSomething() {
            return false;
        }
    };

    private Nothing() {
    }

    @SuppressWarnings("unchecked")
    public static <TYPE> IMaybe<TYPE> getInstance() {
        return (IMaybe<TYPE>) nothing;
    }
}
