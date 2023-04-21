package genorm;

import java.util.Properties;
import org.dom4j.Element;

public interface GenPlugin {

    /**
		Called to initialize the plugin
		@param pluginElement The &lt;plugin&gt; is passed so other options and 
		parameters can be parsed by the plugin
		@param config Configuration options that were provided in the XML or from
		the command line
	*/
    public void init(Element pluginElement, Properties config);
}
