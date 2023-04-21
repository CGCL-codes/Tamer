package fortunata.jspwikiplugins.omemo;

import com.ecyrd.jspwiki.WikiContext;
import com.ecyrd.jspwiki.plugin.PluginException;
import com.ecyrd.jspwiki.plugin.WikiPlugin;
import fortunata.FortunataPlugin;
import fortunata.fswapps.omemo.InfoExtractor;
import fortunata.fswapps.omemo.Omemo;
import fortunata.fswapps.omemo.OntologyWikiPageName;
import fortunata.util.FileDownloader;
import fortunata.util.NiceDate;
import org.apache.log4j.Logger;
import java.io.*;
import java.util.Date;
import java.util.Map;

/**
 * Creates wiki pages for the added ontology. Also generates the semantic data, the results-destination wiki page, and
 * the checks wiki page.
 */
public class AddOnto extends FortunataPlugin implements WikiPlugin {

    private static Logger log = Logger.getLogger(AddOnto.class);

    private Omemo omemo = new Omemo();

    public String execute(WikiContext context, Map parameterMap) throws PluginException {
        String wikiTextMessage;
        try {
            String alias = FormManager.getFieldNoEmplyValue("alias", parameterMap, "alias");
            String altURL = FormManager.getFieldNoEmplyValue("altURL", parameterMap, "URL");
            String ns = FormManager.getFieldNoEmplyValue("ns", parameterMap, "namespace");
            String ontLang = FormManager.getFieldNoEmplyValue("ontLang", parameterMap, "ontology language");
            String versionDate = FormManager.getFieldNoEmplyValue("versionDate", parameterMap, "version");
            ns = (ns.endsWith("/") || ns.endsWith("#")) ? ns : ns + "/";
            omemo.addFormData(alias, altURL, ns, ontLang, versionDate);
            log.info("creating local file");
            String description = createLocalFileForRemoteOntologyFile(context, alias, versionDate, altURL);
            createWikiPages(context);
            createWikiPageListIfNeeded(context, Omemo.listWikiPageName);
            omemo.writeSemanticData(context);
            wikiTextMessage = "%%information " + nl + description + ". " + wnl + "Information added to [" + Omemo.listWikiPageName + "]" + nl + "%%";
            return renderWikiText(context, wikiTextMessage);
        } catch (Throwable e) {
            throw new PluginException(renderWikiText(context, e.getMessage()));
        }
    }

    /**
     * Download the file refered by altURL and store it locally. Some causes rise exceptions: (1) no internet,
     * (2) remote and local files differ in content, (3) problems creating local files
     * @param context
     * @param alias
     * @param versionDate
     * @param altURL
     * @return  a description of the process
     * @throws PluginException if any problem is detected
     */
    private String createLocalFileForRemoteOntologyFile(WikiContext context, String alias, String versionDate, String altURL) throws PluginException {
        OntologyWikiPageName owpn = new OntologyWikiPageName(alias.toUpperCase(), versionDate);
        String rdfFileFullFileName = getWorkDir(context) + File.separator + owpn.toFileName();
        String checkString;
        File f = new File(rdfFileFullFileName);
        FileDownloader fdl = null;
        if (!FileDownloader.canWeConnectToInternet()) {
            throw new PluginException("No Internet available");
        }
        try {
            fdl = new FileDownloader(altURL);
            if (!f.exists()) {
                fdl.copyToFile(rdfFileFullFileName);
                checkString = "Ontology file downloaded and stored locally";
            } else {
                File fok = new File(rdfFileFullFileName);
                Date lastMod = new Date(fok.lastModified());
                if (fdl.hasSameContent(rdfFileFullFileName)) {
                    checkString = "Requested ontology is already stored locally (with same content). It was downloaded " + niceDateIntervalTillNow(lastMod) + " ago";
                } else {
                    log.warn("Ontology file at " + altURL + " differs in contents from local file " + rdfFileFullFileName + ". Check information consistency");
                    throw new PluginException("Requested ontology already exists with different content. Operation rejected");
                }
            }
        } catch (Exception e) {
            throw new PluginException(e.getMessage());
        }
        return checkString;
    }

    private void createWikiPages(WikiContext context) throws PluginException {
        OntologyWikiPageName owpn = new OntologyWikiPageName(omemo.getFormDataAlias().toUpperCase(), omemo.getFormDataVersionDate());
        String wikiPageFullFileName = WikiPageName2FullFileName(context, owpn.toString());
        String rdfFileNameWithPath = getWorkDir(context) + File.separator + owpn.toFileName();
        FileOutputStream fos = null;
        FileInputStream fis = null;
        try {
            fos = new FileOutputStream(wikiPageFullFileName);
            fis = new FileInputStream(rdfFileNameWithPath);
            InfoExtractor infoe = new InfoExtractor(fis, omemo.getFormDataNS(), omemo.getFormDataOntLang());
            infoe.writePage(getWorkDir(context), owpn, Omemo.checksWikiPageName);
            fis.close();
            fos.close();
        } catch (Exception e) {
            log.error("Can not read local rdf file or can not write wiki page");
            throw new PluginException("Error creating wiki pages. See logs");
        }
    }

    private void createWikiPageListIfNeeded(WikiContext context, String wikiPageName) throws PluginException {
        String wikiPageFullFileName = WikiPageName2FullFileName(context, wikiPageName);
        File file = new File(wikiPageFullFileName);
        if (file.exists() == false) {
            try {
                FileOutputStream fos = new FileOutputStream(file);
                PrintStream ps = new PrintStream(fos);
                ps.print("[{Image src='" + ImageSemData + "' align='center' link='" + getURLBase(context) + omemo.getIndividualsFileName() + "'}]" + "[{FormSet form='ExternalOntsForm'}]\n" + "[{FormOpen form='ExternalOntsForm'}]\n" + "[{FormOutput form='ExternalOntsForm' handler='fortunata.jspwikiplugins.omemo.OnListOperation'}]\n" + "[{FormInput type='submit' name='doGeneration' value='Regenerate'}] " + "[View last generation incidences|" + Omemo.checksWikiPageName + "]\n" + "[Add a new ontology|" + Omemo.pluginGenerateListString + "\n" + "[{FormInput type='submit' name='doCheckAvailability ' value='Check selected'}] " + "[{FormClose}]");
                ps.close();
                fos.close();
            } catch (IOException e) {
                log.error("Can not create wiki page " + wikiPageName);
                throw new PluginException("Can not create " + wikiPageName);
            }
        }
    }

    public static String niceDateIntervalTillNow(Date from) {
        return NiceDate.niceDateInterval(from, new Date());
    }
}
