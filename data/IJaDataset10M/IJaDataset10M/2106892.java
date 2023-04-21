package stamina.kernel.beans;

import java.util.Enumeration;

/**This interface defines the behaviour of a generic Bean Container.
 * The container itself is a bean, this way it allows the construction
 * of 'Bean Hierarchies'
 */
interface Container extends Bean {

    public static final String SEPARATOR = "::";

    public static final String ID = "cntr";

    public static final String DECODING_PATTERN = "/::/";

    /**Should set a bean into the container
      * @param beanName the name, by which the bean is known to the container,
      * the name is retrieved from the bean itself      
     */
    public void setBean(Bean bean);

    /**Should remove a bean into the container
     */
    public void removeBean(String beanName);

    /**Should get the named bean from the container
      * @param beanName the name, by which the bean is known to the container,
      * the name is retrieved from the bean itself      
     */
    public Bean getBean(String beanName);

    /**Should query the containter for a bean with <code>beanName</code>
      * @param beanName the name, by which the bean is known to the container,
      * the name is retrieved from the bean itself      
      * @return true, if the <code>beanName</code> 'lives' in the container
      */
    public boolean contains(String beanName);

    /**Should return the names of all of the stored beans.
      */
    public Enumeration getBeanNames();
}
