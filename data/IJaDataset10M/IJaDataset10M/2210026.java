package ajaxnet4j;

/** Represents an IJavaScriptConverter. */
public interface IJavaScriptConverter {

    /** Returns every type that can be used with this converter to serialize an object. */
    Class[] getSerializableTypes();

    /** Returns every type that can be used with this converter to deserialize an JSON string. */
    Class[] getDeserializableTypes();

    /** Initializes the converter.
	 * This method will be called when the application is starting 
	 * and any converter is loaded. */
    void Initialize();

    /** Render the JavaScript code for prototypes or any other JavaScript method
	 *  needed from this converter on the client-side. */
    String getClientScript();

    /** Converts a .NET object into a JSON string. 
	 * @param o The object to convert. 
	 * @return Returns a JSON string. */
    String Serialize(Object o);

    /** Converts an IJavaScriptObject into an NET object. 
	 * @param o The IJavaScriptObject object to convert.
	 * @param c The type to convert the object to.
	 * @return Returns a .NET object. */
    Object Deserialize(IJavaScriptObject o, Class<?> c);
}
