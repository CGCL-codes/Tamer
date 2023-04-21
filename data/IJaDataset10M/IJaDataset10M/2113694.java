package net.jmatrix.eproperties;

/** 
 * Represents a dynamic string value.
 *
 */
public class StringValue implements Value<String> {

    String persistentValue = null;

    EProperties owner = null;

    public StringValue(String s, EProperties p) {
        persistentValue = s;
        owner = p;
    }

    public String toString() {
        return getRuntimeValue().toString();
    }

    public String getPersistentValue() {
        return persistentValue;
    }

    public Object getRuntimeValue() {
        return SubstitutionProcessor.processSubstitution(persistentValue, owner, Object.class);
    }

    @Override
    public void setOwner(EProperties p) {
        owner = p;
    }
}
