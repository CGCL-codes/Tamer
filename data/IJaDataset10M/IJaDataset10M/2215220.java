package com.dotmarketing.services;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.velocity.runtime.resource.ResourceManager;
import com.dotmarketing.beans.Identifier;
import com.dotmarketing.business.APILocator;
import com.dotmarketing.business.CacheLocator;
import com.dotmarketing.cache.IdentifierCache;
import com.dotmarketing.cms.factories.PublicUserFactory;
import com.dotmarketing.exception.DotHibernateException;
import com.dotmarketing.factories.IdentifierFactory;
import com.dotmarketing.factories.InodeFactory;
import com.dotmarketing.portlets.containers.model.Container;
import com.dotmarketing.portlets.contentlet.business.ContentletAPI;
import com.dotmarketing.portlets.contentlet.model.Contentlet;
import com.dotmarketing.portlets.htmlpages.model.HTMLPage;
import com.dotmarketing.portlets.languagesmanager.factories.LanguageFactory;
import com.dotmarketing.portlets.languagesmanager.model.Language;
import com.dotmarketing.util.Config;
import com.dotmarketing.util.Logger;
import com.dotmarketing.util.UtilMethods;
import com.dotmarketing.velocity.DotResourceCache;

/**
 * @author will
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class PageServices {

    public static void invalidate(HTMLPage htmlPage) {
        Identifier identifier = IdentifierFactory.getParentIdentifier(htmlPage);
        invalidate(htmlPage, identifier, false);
        invalidate(htmlPage, identifier, true);
    }

    public static void invalidate(HTMLPage htmlPage, boolean EDIT_MODE) {
        Identifier identifier = IdentifierFactory.getParentIdentifier(htmlPage);
        invalidate(htmlPage, identifier, EDIT_MODE);
    }

    @SuppressWarnings("unchecked")
    public static void invalidate(HTMLPage htmlPage, Identifier identifier, boolean EDIT_MODE) {
        removePageFile(htmlPage, identifier, EDIT_MODE);
    }

    public static InputStream buildStream(HTMLPage htmlPage, boolean EDIT_MODE) {
        Identifier identifier = IdentifierFactory.getParentIdentifier(htmlPage);
        return buildStream(htmlPage, identifier, EDIT_MODE);
    }

    @SuppressWarnings("unchecked")
    public static InputStream buildStream(HTMLPage htmlPage, Identifier identifier, boolean EDIT_MODE) {
        InputStream result;
        StringBuilder sb = new StringBuilder();
        ContentletAPI conAPI = APILocator.getContentletAPI();
        com.dotmarketing.portlets.templates.model.Template cmsTemplate = com.dotmarketing.portlets.htmlpages.factories.HTMLPageFactory.getHTMLPageTemplate(htmlPage, EDIT_MODE);
        java.util.StringTokenizer st = new java.util.StringTokenizer(String.valueOf(identifier.getURI()), "/");
        String pageChannel = null;
        if (st.hasMoreTokens()) {
            pageChannel = st.nextToken();
        }
        sb.append("#if(!$doNotSetPageInfo)\n");
        sb.append("#set ( $quote = '\"' )\n");
        sb.append("#set ($HTMLPAGE_INODE = \"" + String.valueOf(htmlPage.getInode()) + "\" )\n");
        sb.append("#set ($HTMLPAGE_IDENTIFIER = \"" + String.valueOf(htmlPage.getIdentifier()) + "\" )\n");
        sb.append("#set ($HTMLPAGE_TITLE = \"" + UtilMethods.espaceForVelocity(htmlPage.getTitle()) + "\" )\n");
        sb.append("#set ($HTMLPAGE_FRIENDLY_NAME = \"" + UtilMethods.espaceForVelocity(htmlPage.getFriendlyName()) + "\" )\n");
        sb.append("#set ($TEMPLATE_INODE = \"" + String.valueOf(cmsTemplate.getInode()) + "\" )\n");
        sb.append("#set ($HTMLPAGE_META = \"" + UtilMethods.espaceForVelocity(htmlPage.getMetadata()) + "\" )\n");
        sb.append("#set ($HTMLPAGE_META = \"#fixBreaks($HTMLPAGE_META)\")\n");
        sb.append("#set ($HTMLPAGE_SECURE = \"" + String.valueOf(htmlPage.isHttpsRequired()) + "\" )\n");
        sb.append("#set ($VTLSERVLET_URI = \"" + UtilMethods.encodeURIComponent(identifier.getURI()) + "\" )\n");
        sb.append("#set ($HTMLPAGE_REDIRECT = \"" + UtilMethods.espaceForVelocity(htmlPage.getRedirect()) + "\" )\n");
        sb.append("#set ($pageTitle = \"" + UtilMethods.espaceForVelocity(htmlPage.getTitle()) + "\" )\n");
        sb.append("#set ($pageChannel = \"" + pageChannel + "\" )\n");
        sb.append("#set ($friendlyName = \"" + UtilMethods.espaceForVelocity(htmlPage.getFriendlyName()) + "\" )\n");
        Date moddate = null;
        if (UtilMethods.isSet(htmlPage.getModDate())) {
            moddate = htmlPage.getModDate();
        } else {
            moddate = htmlPage.getStartDate();
        }
        moddate = new Timestamp(moddate.getTime());
        sb.append("#set ($HTML_PAGE_LAST_MOD_DATE= $date.toDate(\"yyyy-MM-dd HH:mm:ss.SSS\", \"" + moddate + "\"))\n");
        sb.append("#end\n");
        List identifiers = InodeFactory.getChildrenClass(cmsTemplate, Identifier.class);
        String folderPath = (!EDIT_MODE) ? "live/" : "working/";
        List languages = LanguageFactory.getLanguages();
        Iterator i = identifiers.iterator();
        while (i.hasNext()) {
            Identifier ident = (Identifier) i.next();
            Container c = null;
            if (EDIT_MODE) {
                c = (Container) IdentifierFactory.getWorkingChildOfClass(ident, Container.class);
            } else {
                c = (Container) IdentifierFactory.getLiveChildOfClass(ident, Container.class);
            }
            sb.append("#set ($container" + ident.getInode() + " = \"" + folderPath + ident.getInode() + "." + Config.getStringProperty("VELOCITY_CONTAINER_EXTENSION") + "\" )");
            String sort = (c.getSortContentletsBy() == null) ? "tree_order" : c.getSortContentletsBy();
            boolean dynamicContainer = UtilMethods.isSet(c.getLuceneQuery());
            int langCounter = 0;
            Iterator langIter = languages.iterator();
            while (langIter.hasNext()) {
                Language language = (Language) langIter.next();
                List<Contentlet> contentlets = new ArrayList<Contentlet>();
                if (!dynamicContainer) {
                    Identifier idenHtmlPage = IdentifierFactory.getIdentifierByWebAsset(htmlPage);
                    Identifier idenContainer = IdentifierFactory.getIdentifierByWebAsset(c);
                    try {
                        contentlets = conAPI.findPageContentlets(idenHtmlPage.getInode(), idenContainer.getInode(), sort, EDIT_MODE, language.getId(), PublicUserFactory.getSystemUser(), false);
                    } catch (Exception e) {
                        Logger.error(PageServices.class, "Unable to retrive contentlets on page", e);
                    }
                    Logger.debug(PageServices.class, "HTMLPage= " + htmlPage.getInode() + " Container=" + c.getInode() + " Language=" + language.getId() + " Contentlets=" + contentlets.size());
                }
                String contentletList = "";
                Iterator iter = contentlets.iterator();
                int count = 0;
                while (iter.hasNext() && count < c.getMaxContentlets()) {
                    Contentlet contentlet = (Contentlet) iter.next();
                    Identifier contentletIdentifier;
                    try {
                        contentletIdentifier = IdentifierCache.getIdentifierFromIdentifierCache(contentlet);
                    } catch (DotHibernateException dhe) {
                        contentletIdentifier = new Identifier();
                        Logger.error(PageServices.class, "Unable to rertive identifier for contentlet", dhe);
                    }
                    contentletList += "\"" + folderPath + contentletIdentifier.getInode() + "_" + language.getId() + "." + Config.getStringProperty("VELOCITY_CONTENT_EXTENSION") + "\"";
                    count++;
                    if (iter.hasNext() && count < c.getMaxContentlets()) {
                        contentletList += ",";
                    }
                }
                if (langCounter == 0) {
                    sb.append("#if ($language==\"" + language.getId() + "\")");
                } else {
                    sb.append("#elseif ($language==\"" + language.getId() + "\")");
                }
                sb.append("#set ($contentletList" + ident.getInode() + " = [" + contentletList + "] )");
                sb.append("\n#set ($totalSize" + ident.getInode() + "=" + count + ")\n");
                langCounter++;
            }
            sb.append("#end\n");
        }
        Identifier iden = IdentifierFactory.getIdentifierByWebAsset(cmsTemplate);
        sb.append("#if(!$doNotParseTemplate)\n");
        sb.append("#parse('" + folderPath + iden.getInode() + "." + Config.getStringProperty("VELOCITY_TEMPLATE_EXTENSION") + "')");
        sb.append("#end\n");
        try {
            String realFolderPath = (!EDIT_MODE) ? "live" + java.io.File.separator : "working" + java.io.File.separator;
            String velocityRootPath = Config.getStringProperty("VELOCITY_ROOT");
            String filePath = realFolderPath + identifier.getInode() + "." + Config.getStringProperty("VELOCITY_HTMLPAGE_EXTENSION");
            if (velocityRootPath.startsWith("/WEB-INF")) {
                velocityRootPath = Config.CONTEXT.getRealPath(velocityRootPath);
            }
            velocityRootPath += java.io.File.separator;
            if (Config.getBooleanProperty("SHOW_VELOCITYFILES", false)) {
                java.io.BufferedOutputStream tmpOut = new java.io.BufferedOutputStream(new java.io.FileOutputStream(new java.io.File(velocityRootPath + filePath)));
                OutputStreamWriter out = new OutputStreamWriter(tmpOut, UtilMethods.getCharsetConfiguration());
                out.write(sb.toString());
                out.flush();
                out.close();
                tmpOut.close();
            }
        } catch (Exception e) {
            Logger.error(PageServices.class, e.toString(), e);
        }
        try {
            result = new ByteArrayInputStream(sb.toString().getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e1) {
            result = new ByteArrayInputStream(sb.toString().getBytes());
            Logger.error(ContainerServices.class, e1.getMessage(), e1);
        }
        return result;
    }

    public static void unpublishPageFile(HTMLPage htmlPage) {
        Identifier identifier = IdentifierFactory.getParentIdentifier(htmlPage);
        removePageFile(htmlPage, identifier, false);
    }

    public static void removePageFile(HTMLPage htmlPage, boolean EDIT_MODE) {
        Identifier identifier = IdentifierFactory.getParentIdentifier(htmlPage);
        removePageFile(htmlPage, identifier, EDIT_MODE);
    }

    public static void removePageFile(HTMLPage htmlPage, Identifier identifier, boolean EDIT_MODE) {
        String folderPath = (!EDIT_MODE) ? "live" + java.io.File.separator : "working" + java.io.File.separator;
        String velocityRootPath = Config.getStringProperty("VELOCITY_ROOT");
        if (velocityRootPath.startsWith("/WEB-INF")) {
            velocityRootPath = Config.CONTEXT.getRealPath(velocityRootPath);
        }
        String filePath = folderPath + identifier.getInode() + "." + Config.getStringProperty("VELOCITY_HTMLPAGE_EXTENSION");
        velocityRootPath += java.io.File.separator;
        java.io.File f = new java.io.File(velocityRootPath + filePath);
        f.delete();
        DotResourceCache vc = CacheLocator.getVeloctyResourceCache();
        vc.remove(ResourceManager.RESOURCE_TEMPLATE + filePath);
    }
}
