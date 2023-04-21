package jpen;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PScroll extends TypedValuedClass<PScroll.Type, Integer> implements java.io.Serializable {

    public static final long serialVersionUID = 1l;

    public enum Type {

        UP, DOWN, CUSTOM;

        public static final List<Type> ALL_VALUES = Collections.unmodifiableList(Arrays.asList(values()));

        public static final List<Type> VALUES = TypedClass.createStandardTypes(ALL_VALUES);
    }

    public PScroll(int typeNumber, int value) {
        super(typeNumber, value);
    }

    @Override
    final List<Type> getAllTypes() {
        return Type.ALL_VALUES;
    }
}
