package edu.opexcavator.model.postype;

import java.util.Map;

/**
 * @author Jesica N. Fera
 *
 */
public class Adverb extends POSType {

    protected static Map<String, Type> typeTags;

    private Type type;

    public Adverb(Type type) {
        this.type = type;
    }

    /**
	 * Creates an adverb without any additional information.
	 **/
    public Adverb() {
    }

    public Type getType() {
        return type;
    }

    public static Type buildType(String typeTag) {
        return typeTags != null ? typeTags.get(typeTag.toLowerCase()) : null;
    }

    public enum Type {

        General, Negative, None
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }
}
