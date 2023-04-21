package uk.org.ogsadai.extension;

/**
 * Interface used to instantiate an Object based on the provided class name
 * from the activity input.
 * 
 * @author The OGSA-DAI Project Team
 */
public interface CreateObjectFromInputValueActivity {

    /**
     * Gets the input attributes associated with the given input name.
     * 
     * @param inputClassName
     *            input name.
     * @return The created Object.
     */
    public Object getObject(String inputClassName);
}
