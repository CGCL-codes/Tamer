package de.jassda.jabyba.jdwp.delegate;

import de.jassda.jabyba.jdwp.Packet;

public class ArrayReference {

    public enum Delegate implements IDelegate {

        Length {

            public Packet handle(Packet cmd) {
                return length != null ? length.handleArrayReference$Length(cmd) : null;
            }
        }
        , GetValues {

            public Packet handle(Packet cmd) {
                return getValues != null ? getValues.handleArrayReference$GetValues(cmd) : null;
            }
        }
        , SetValues {

            public Packet handle(Packet cmd) {
                return setValues != null ? setValues.handleArrayReference$SetValues(cmd) : null;
            }
        }
        ;

        public abstract Packet handle(Packet cmd);

        public SetValues setValues = null;

        public GetValues getValues = null;

        public Length length = null;
    }

    public interface Length {

        public Packet handleArrayReference$Length(Packet cmd);
    }

    public interface GetValues {

        public Packet handleArrayReference$GetValues(Packet cmd);
    }

    public interface SetValues {

        public Packet handleArrayReference$SetValues(Packet cmd);
    }
}
