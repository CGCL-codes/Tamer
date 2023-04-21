package unitee;

/**
 * Provides an interface to fetch test parameter values from Unitee's framework. 
 * A normal TestCase would use any combination of the get() methods. The set()
 * , remove() and getParameterNames() , getAllParameterNames() are to be used 
 * by Unitee's framework and tools.
 * @author Ishai Asa
 */
public interface TestParameters {

    /**
	 * Returns all parameters of all chained TestParameters.
	 */
    public String[] getAllParameterNames();

    /**
	 * Returns all local parameters for this test.
	 */
    public String[] getParameterNames();

    public byte[] getByteArray(String name) throws ParameterException;

    public double getDouble(String name) throws ParameterException;

    public long getLong(String name) throws ParameterException;

    public Class getParameterType(String name);

    public String getPassword(String name) throws ParameterException;

    public String getString(String name) throws ParameterException;

    public void removeParameter(String name);

    public void setByteArray(String name, byte[] data);

    public void setDouble(String name, double value);

    public void setLong(String name, long value);

    public void setPassword(String name, String password);

    public void setString(String name, String value);
}
