package fortunata.fswapps.omemo.util;

import com.ecyrd.jspwiki.WikiContext;
import com.ecyrd.jspwiki.plugin.PluginException;
import com.ecyrd.jspwiki.plugin.WikiPlugin;
import fortunata.FortunataPlugin;
import org.apache.log4j.Logger;
import java.util.Map;

/**
 * User: Mariano Rico
 * Date: 17-ene-2006
 * Time: 13:43:37
 */
public class AnalyzerRDFHookForm implements WikiPlugin {

    private static Logger log = Logger.getLogger(AnalyzerRDFHookForm.class);

    public static String nl = System.getProperty("line.separator");

    public String execute(WikiContext context, Map ParameterMap) throws PluginException {
        String ErrorStringNP = "No parameters given";
        if (ParameterMap == null) {
            return ErrorStringNP;
        }
        AnalyzerRDFStart.setGenerate(true);
        String wikiPageName = AnalyzerRDFStart.getChecksWikiPageName();
        String addenda = "";
        if (wikiPageName != null) {
            addenda = " See incidences in [" + wikiPageName + "]";
        }
        String wikiText = "%%information " + nl + "Generation done." + addenda + nl + "%%";
        return FortunataPlugin.renderWikiText(context, wikiText);
    }
}
