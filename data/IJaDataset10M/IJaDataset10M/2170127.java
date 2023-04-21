package pcgen.cdom.primitive;

import java.util.Collection;
import pcgen.cdom.base.Converter;
import pcgen.cdom.base.PrimitiveCollection;
import pcgen.cdom.converter.NegateFilterConverter;
import pcgen.cdom.enumeration.GroupingState;
import pcgen.core.PlayerCharacter;

public class NegatingPrimitive<T> implements PrimitiveCollection<T> {

    private final PrimitiveCollection<T> primitive;

    public NegatingPrimitive(PrimitiveCollection<T> prim) {
        if (prim == null) {
            throw new IllegalArgumentException("PrimitiveCollection cannot be null");
        }
        primitive = prim;
    }

    @Override
    public <R> Collection<R> getCollection(PlayerCharacter pc, Converter<T, R> c) {
        return primitive.getCollection(pc, new NegateFilterConverter<T, R>(c));
    }

    @Override
    public Class<? super T> getReferenceClass() {
        return primitive.getReferenceClass();
    }

    /**
	 * Returns the GroupingState for this CompoundAndChoiceSet. The
	 * GroupingState indicates how this CompoundAndChoiceSet can be combined
	 * with other PrimitiveChoiceSets.
	 * 
	 * @return The GroupingState for this CompoundAndChoiceSet.
	 */
    @Override
    public GroupingState getGroupingState() {
        return primitive.getGroupingState().negate();
    }

    /**
	 * Returns a representation of this CompoundAndChoiceSet, suitable for
	 * storing in an LST file.
	 * 
	 * @return A representation of this CompoundAndChoiceSet, suitable for
	 *         storing in an LST file.
	 */
    @Override
    public String getLSTformat(boolean useAny) {
        return "!" + primitive.getLSTformat(useAny);
    }

    @Override
    public int hashCode() {
        return primitive.hashCode() - 1;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof NegatingPrimitive) && ((NegatingPrimitive<?>) obj).primitive.equals(primitive);
    }
}
