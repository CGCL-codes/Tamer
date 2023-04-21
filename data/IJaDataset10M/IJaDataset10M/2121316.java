package ldapbeans.util.scanner;

public interface ClassFilter {

    /**
     * @param p_Class
     *            The class to filter
     * @return <code>true</code> if the class is accepted, <code>false</code>
     *         otherwise
     */
    boolean accept(Class<?> p_Class);
}
