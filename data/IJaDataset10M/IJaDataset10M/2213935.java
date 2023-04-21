package assistedinject;

import com.google.inject.TypeLiteral;
import java.util.List;
import java.util.ArrayList;
import java.lang.reflect.Type;

/**
 * A list of {@link TypeLiteral}s to match an injectable Constructor's assited
 * parameter types to the corresponding factory method.
 *
 * @author <a href="jmourits@gmail.com">Jerome Mourits</a>
 * @author <a href="jesse@swank.ca">Jesse Wilson</a>
 */
class ParameterListKey {

    private final List<TypeLiteral> paramList;

    public ParameterListKey(List<TypeLiteral> paramList) {
        this.paramList = paramList;
    }

    public ParameterListKey(Type[] types) {
        paramList = new ArrayList<TypeLiteral>();
        for (Type type : types) {
            paramList.add(TypeLiteral.get(type));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ParameterListKey)) {
            return false;
        }
        ParameterListKey other = (ParameterListKey) o;
        return paramList.equals(other.paramList);
    }

    @Override
    public int hashCode() {
        return paramList.hashCode();
    }

    @Override
    public String toString() {
        return paramList.toString();
    }
}
