package fortunata.jspwikiplugins.helloWorld;

import com.ecyrd.jspwiki.WikiContext;
import com.ecyrd.jspwiki.plugin.PluginException;
import com.ecyrd.jspwiki.plugin.WikiPlugin;
import fortunata.FortunataPlugin;
import fortunata.fswapps.helloWorld.HelloWorldSWApp;
import java.util.Map;

/**
 * The most basic Fortunata-based application
 */
public class HelloWorld extends FortunataPlugin implements WikiPlugin {

    private HelloWorldSWApp hwSWApp = new HelloWorldSWApp();

    public String execute(WikiContext context, Map parameterMap) throws PluginException {
        hwSWApp.writeSemanticData(context);
        String wikiTextMessage = "Information published!";
        return renderWikiText(context, wikiTextMessage, MSG_TYPE_OK);
    }
}
