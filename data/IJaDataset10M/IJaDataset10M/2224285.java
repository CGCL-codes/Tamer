package net.jxta.edutella.util;

/**
 * A class for objects that can be configured by @link{EduConfig}
 * 
 * @author chs
 */
public interface Configurable {

    /**
	 * Returns an array of {@link Option}s that this Configurable
	 * understands.<br>
	 * 
	 * The longNames of these options correspond to the property
	 * names for property file configuration.
	 * 
	 * @return an array of {@link Options}s 
	 */
    public Option[] getOptions();

    /**
	  * A Configurable will be configured using properties that correspond
	  * to the names of getter/setter methods. A property <code>myapp.someValue</code> 
	  * might be used to call the <code>setSomeValue</code> method on
	  * an object. This method returns the property prefix ("<code>myapp</code>" in 
	  * this case) that is to be used for this object.
	  * 
	  * @return property prefix
	  */
    public String getPropertyPrefix();
}
