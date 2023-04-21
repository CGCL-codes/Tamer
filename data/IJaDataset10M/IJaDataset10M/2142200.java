package com.google.gson;

import java.io.IOException;

/**
 * A class representing a Json {@code null} value.
 *
 * @author Inderjeet Singh
 * @author Joel Leitch
 * @since 1.2
 */
public final class JsonNull extends JsonElement {

    private static final JsonNull INSTANCE = new JsonNull();

    /**
   * Creates a new JsonNull object.
   */
    public JsonNull() {
    }

    @Override
    protected void toString(Appendable sb, Escaper escaper) throws IOException {
        sb.append("null");
    }

    /**
   * All instances of JsonNull have the same hash code since they are indistinguishable
   */
    @Override
    public int hashCode() {
        return JsonNull.class.hashCode();
    }

    /**
   * All instances of JsonNull are the same
   */
    @Override
    public boolean equals(Object other) {
        return other instanceof JsonNull;
    }

    /**
   * Creation method used to return an instance of a {@link JsonNull}.  To reduce the memory
   * footprint, a single object has been created for this class; therefore the same instance is
   * being returned for each invocation of this method. This method is kept private since we 
   * prefer the users to use {@link JsonNull#JsonNull()} which is similar to how other JsonElements
   * are created. Note that all instances of JsonNull return true for {@link #equals(Object)} 
   * when compared to each other.
   *
   * @return a instance of a {@link JsonNull}
   */
    static JsonNull createJsonNull() {
        return INSTANCE;
    }
}
