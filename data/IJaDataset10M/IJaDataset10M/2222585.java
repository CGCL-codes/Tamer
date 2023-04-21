package jpen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

public class PButton extends TypedValuedClass<PButton.Type, Boolean> implements java.io.Serializable {

    public static final long serialVersionUID = 1l;

    public enum Type {

        LEFT(TypeGroup.MOUSE), CENTER(TypeGroup.MOUSE), RIGHT(TypeGroup.MOUSE), /**
		Used whenever the {@link Pen} {@link PLevel.Type#PRESSURE} level value changes from/to 0.
		*/
        ON_PRESSURE(TypeGroup.LEVEL), CONTROL(TypeGroup.MODIFIER), SHIFT(TypeGroup.MODIFIER), ALT(TypeGroup.MODIFIER), CUSTOM(TypeGroup.UNDEFINED);

        private final TypeGroup group;

        Type(TypeGroup group) {
            this.group = group;
            group.types.add(this);
        }

        public TypeGroup getGroup() {
            return group;
        }

        public static final List<Type> ALL_VALUES = Collections.unmodifiableList(Arrays.asList(values()));

        public static final List<Type> VALUES = TypedClass.createStandardTypes(ALL_VALUES);
    }

    public enum TypeGroup {

        /**
		Standard mouse buttons.
		*/
        MOUSE, /**
		Level condition buttons.
		*/
        LEVEL, /**
		Modifier buttons.
		*/
        MODIFIER, UNDEFINED;

        private Collection<Type> types = new ArrayList<Type>();

        private Collection<Type> typesAccess;

        public Collection<Type> getTypes() {
            if (typesAccess == null) {
                types = EnumSet.copyOf(types);
                typesAccess = Collections.unmodifiableCollection(types);
            }
            return typesAccess;
        }
    }

    public PButton(Type type, Boolean value) {
        this(type.ordinal(), value);
    }

    public PButton(int typeNumber, Boolean value) {
        super(typeNumber, value);
    }

    @Override
    final List<Type> getAllTypes() {
        return Type.ALL_VALUES;
    }
}
