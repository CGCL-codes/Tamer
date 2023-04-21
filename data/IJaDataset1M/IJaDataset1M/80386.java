package com.anthonyeden.lib.log;

/**	Logger implementations should implement this interface.  This is
	an internal interface with methods which should not be exposed to
	the end user.
	
	<p>All logger implementation must implement this interface.  In
	addition they must include two configure methods with the following
	signatures:
	
	<ul>
	<li><code>public static void configure()</code>
	<li><code>public static void configure(File file)</code>
	</ul>
    
    <b>This class is deprecated.</b>  All EdenLib classes now use the
    Apache Jakarta Commons logging library.
	
	@author Anthony Eden
    @deprecated
*/
public interface LoggerInternal extends Logger {

    /**	Initialize the logger with the given identifier.  The identifier
		can be any string and may or may not be included in the final
		log stream (depending on the implementation.)
		
		@param identifier The identifier
	*/
    public void init(String identifier);
}
