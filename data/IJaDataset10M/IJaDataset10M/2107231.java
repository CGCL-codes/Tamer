package com.germinus.xpression.portlet.cms.rdf;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import javax.portlet.PortletPreferences;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.clapper.util.html.HTMLUtil;
import com.germinus.xpression.cms.CMSRuntimeException;
import com.germinus.xpression.cms.categories.Categories;
import com.germinus.xpression.cms.categories.Category;
import com.germinus.xpression.cms.categories.CategoryManager;
import com.germinus.xpression.cms.categories.CategoryNotFoundException;
import com.germinus.xpression.cms.contents.Content;
import com.germinus.xpression.cms.contents.ContentIF;
import com.germinus.xpression.cms.contents.ContentManager;
import com.germinus.xpression.cms.contents.ContentNotFoundException;
import com.germinus.xpression.cms.contents.MalformedContentException;
import com.germinus.xpression.cms.contents.ScribeDynaBean;
import com.germinus.xpression.cms.model.EducativeProposal;
import com.germinus.xpression.cms.model.Link;
import com.germinus.xpression.cms.model.NewsNoteData;
import com.germinus.xpression.cms.model.PremiereData;
import com.germinus.xpression.cms.util.ManagerRegistry;
import com.germinus.xpression.content_editor.CMSCommonData;
import com.germinus.xpression.groupware.GroupwareUser;
import com.germinus.xpression.groupware.action.GroupwareHelper;
import com.germinus.xpression.groupware.util.LiferayHelper;
import com.germinus.xpression.i18n.I18NString;
import com.liferay.portal.service.persistence.PortletPreferencesPK;
import com.liferay.portal.util.PortalUtil;

public class RDFServlet extends HttpServlet {

    private static final long serialVersionUID = 5068929609396030559L;

    private static Log log = LogFactory.getLog(RDFServlet.class);

    private static LiferayHelper liferayHelper = GroupwareHelper.getLiferayHelper();

    private List<ContentIF> contents;

    private Template location = null;

    private Template body = null;

    private Template datatemplate = null;

    private Template enlace = null;

    private Template enlacetrailer = null;

    private Template creator = null;

    private Template filetemplate = null;

    private Template mainnewsimage = null;

    private Template secondnewsimage = null;

    private Template keywords = null;

    private Template image = null;

    private Template eventcategory = null;

    private Template sponsor = null;

    private Template duration = null;

    private Template head = null;

    private Template avaiable = null;

    private Template valid = null;

    private Template news = null;

    private Template newsend = null;

    private Template newsend2 = null;

    private Template category = null;

    private Template foot = null;

    private Template educativeproposal = null;

    private Template educativeproposallink = null;

    private Template educativeproposalend = null;

    private Template educativeproposalreference = null;

    private Template endbody = null;

    private Template premiere = null;

    private Template premiereend = null;

    private Template endbodypremiere = null;

    private Template event = null;

    private Template eventend = null;

    private Template eventend2 = null;

    private Template announce = null;

    private Template announceend = null;

    private Template announceend2 = null;

    private Template discipline = null;

    private Template consignees = null;

    private Template knowledge = null;

    private Template imagesumary = null;

    private static PortletPreferences preferen = null;

    private static ContentManager contentManager = ManagerRegistry.getContentManager();

    public void init(ServletConfig conf) throws ServletException {
        super.init(conf);
        log.info("Xpression server activates RDFServlet");
    }

    /**
	 * Builds a PublishManager from contents of a portlet and writes the RDF
	 * generated to Writer object of the response object.
	 */
    public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        PublishManagerRdf publishManager = null;
        log.info("Servlet called");
        res.setContentType("application/rdf+xml");
        String path = req.getPathInfo();
        byte[] rdfDocumentBytes = null;
        RDFParams params = RDFParams.getFromPath(path, req, getServletContext());
        GroupwareUser user;
        VelocityEngine ve = new VelocityEngine();
        Properties p = new Properties();
        String str = req.getRequestURL().toString();
        int pos = str.indexOf(".rdf");
        String contentid = str.substring(pos + 4);
        p.setProperty("file.resource.loader.path", getServletContext().getRealPath("/html/portlet/stored_search/velocity"));
        try {
            ve.init(p);
        } catch (Exception e) {
            e.printStackTrace();
        }
        initTemplates(ve);
        user = GroupwareHelper.getUser(req);
        PortletPreferencesPK pk = new PortletPreferencesPK(params.portletName, params.layoutId, params.ownerId);
        log.info("Portlet preferences :" + pk);
        try {
            publishManager = new PublishManagerRdf(user, preferen, params.groupId, params.layoutId, params.portletName, params.companyId, res.getCharacterEncoding());
            contents = publishManager.getContents();
            OutputStream output = res.getOutputStream();
            int size = publishManager.getContents().size();
            if (size > 0) {
                for (int i = 0; i < publishManager.getContents().size(); i++) {
                    VelocityContext context = new VelocityContext();
                    StringWriter writer = new StringWriter();
                    ContentIF item = (ContentIF) contents.get(i);
                    if (item.getId().equals(contentid)) {
                        head.merge(context, writer);
                        chooseContent(req, size, i, context, writer, item);
                        if (i != ((size) - 1)) {
                            foot.merge(context, writer);
                        }
                        rdfDocumentBytes = writer.toString().getBytes();
                        output.write(rdfDocumentBytes);
                        output.flush();
                        break;
                    }
                    if (contentid.equals("")) {
                        if (i == 0) {
                            head.merge(context, writer);
                        }
                        chooseContent(req, size, i, context, writer, item);
                        rdfDocumentBytes = writer.toString().getBytes();
                        output.write(rdfDocumentBytes);
                        output.flush();
                    }
                }
                output.close();
            } else {
                VelocityContext context = new VelocityContext();
                StringWriter writer = new StringWriter();
                head.merge(context, writer);
                foot.merge(context, writer);
                rdfDocumentBytes = writer.toString().getBytes();
                output.write(rdfDocumentBytes);
                output.flush();
                output.close();
            }
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        } catch (ParseErrorException e) {
            e.printStackTrace();
        } catch (MethodInvocationException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setPref(PortletPreferences pref) {
        preferen = pref;
    }

    private void chooseContent(HttpServletRequest req, int size, int i, VelocityContext context, StringWriter writer, ContentIF item) throws IOException {
        if (getNewsType(item).equals("cms.event.name")) {
            contructenvents(item, context, writer, req, item.getId());
        }
        if (getNewsType(item).equals("cms.announce.name")) {
            contructannouces(item, context, writer, req, item.getId());
        }
        if (item.getContentData().getClass().getSimpleName().equals("PremiereData")) {
            contructpremiere(item, context, writer, req, item.getId());
        }
        if (item.getContentData().getClass().getSimpleName().equals("NewsNoteData")) {
            contructnews(item, context, writer, req, item.getId());
        }
        if (i == ((size) - 1)) {
            foot.merge(context, writer);
        }
    }

    private void contructnews(ContentIF item, VelocityContext context, StringWriter writer, HttpServletRequest req, String id) {
        try {
            NewsNoteData data = (NewsNoteData) item.getContentData();
            Content contenido = contentManager.getContentById(item.getId());
            EducativeProposal proposal[] = data.getProposals();
            String str = contenido.getMainLocale();
            int pos = str.indexOf("_");
            String locale = str.substring(0, pos);
            String databody = data.getBody();
            String title = item.getName();
            String subtitle = data.getSubtitle().getFullText();
            String keyword = data.getKeywords();
            Date credate = null;
            Date publicationdate = null;
            Date expirationdate = null;
            String textomainimage = data.getMainImageSubtitle();
            String textosecondimage = data.getSecondaryImageSubtitle();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String dateString = null;
            String expirationdate2 = null;
            String publicationdate2 = null;
            try {
                credate = item.getCreationDate();
                dateString = formatter.format(credate);
            } catch (RuntimeException e) {
                dateString = null;
            }
            try {
                expirationdate = item.getExpirationDate();
                expirationdate2 = formatter.format(expirationdate);
            } catch (RuntimeException e) {
                expirationdate2 = null;
            }
            try {
                publicationdate = item.getPublicationDate();
                publicationdate2 = formatter.format(publicationdate);
            } catch (RuntimeException e) {
                publicationdate2 = null;
            }
            if (databody == null) {
                databody = "";
            }
            if (title == null) {
                title = "";
            }
            if (subtitle == null) {
                subtitle = "";
            }
            if (keyword == null) {
                keyword = "";
            }
            if (textomainimage == null) {
                textomainimage = "";
            }
            if (textosecondimage == null) {
                textosecondimage = "";
            }
            databody = HTMLUtil.textFromHTML(databody);
            databody = HTMLUtil.escapeHTML(databody);
            title = HTMLUtil.textFromHTML(title);
            title = HTMLUtil.escapeHTML(title);
            subtitle = HTMLUtil.textFromHTML(subtitle);
            subtitle = HTMLUtil.escapeHTML(subtitle);
            keyword = HTMLUtil.textFromHTML(keyword);
            keyword = HTMLUtil.escapeHTML(keyword);
            textomainimage = HTMLUtil.textFromHTML(textomainimage);
            textomainimage = HTMLUtil.escapeHTML(textomainimage);
            textosecondimage = HTMLUtil.textFromHTML(textosecondimage);
            textosecondimage = HTMLUtil.escapeHTML(textosecondimage);
            id = req.getRequestURL() + id;
            if (id.contains("http://www.educa.madrid.org:8080")) {
                id = id.replace(":8080", "");
            }
            context.put("expirationdate", expirationdate2);
            context.put("publicationdate", publicationdate2);
            context.put("locale", locale);
            context.put("id", id);
            context.put("body", replace(databody));
            context.put("titulo", replace(title));
            context.put("autor", replace(item.getAuthorId()));
            context.put("subtitulo", replace(subtitle));
            context.put("creationdate", dateString);
            news.merge(context, writer);
            Set<Categories> contenidos3 = CMSCommonData.getSelectedCategories(contenido, "knowledgeAreas", "cms.field.knowledgeAreas");
            for (Iterator it = contenidos3.iterator(); it.hasNext(); ) {
                Category n = (Category) it.next();
                context.put("knowledge", n.getName());
                knowledge.merge(context, writer);
            }
            Set<Categories> contenidos = CMSCommonData.getSelectedCategories(contenido, "discipline", "discipline");
            for (Iterator it = contenidos.iterator(); it.hasNext(); ) {
                Category n = (Category) it.next();
                context.put("discipline", n.getName());
                discipline.merge(context, writer);
            }
            Set<Categories> contenidos2 = CMSCommonData.getSelectedCategories(contenido, "consignees", "cms.field.consignees");
            for (Iterator it = contenidos2.iterator(); it.hasNext(); ) {
                Category n = (Category) it.next();
                context.put("consignees", n.getName());
                consignees.merge(context, writer);
            }
            if (publicationdate2 != null) {
                avaiable.merge(context, writer);
            }
            if (expirationdate2 != null) {
                valid.merge(context, writer);
            }
            newsend.merge(context, writer);
            Set<Categories> contenidos23 = CMSCommonData.getSelectedCategories(contenido, "newsNotes", "cms.field.categories");
            for (Iterator it = contenidos23.iterator(); it.hasNext(); ) {
                Category n = (Category) it.next();
                context.put("category", n.getName());
                category.merge(context, writer);
            }
            if (!keyword.equals("")) {
                context.put("keywords", replace(keyword));
                keywords.merge(context, writer);
            }
            if (!data.getMainImage().getURLPath().equals("notConfigured")) {
                context.put("mainimage", PortalUtil.getPortalURL(req, false) + req.getContextPath() + "/files/" + data.getMainImage().getURLPath());
                context.put("textomainimage", textomainimage);
                mainnewsimage.merge(context, writer);
            }
            if (!data.getSecondaryImage().getURLPath().equals("notConfigured")) {
                context.put("secondimage", PortalUtil.getPortalURL(req, false) + req.getContextPath() + "/files/" + data.getSecondaryImage().getURLPath());
                context.put("textosecondimage", textosecondimage);
                secondnewsimage.merge(context, writer);
            }
            if (!data.getAnimation().getURLPath().equals("notConfigured")) {
                context.put("animation", PortalUtil.getPortalURL(req, false) + req.getContextPath() + "/files/" + data.getAnimation().getURLPath());
                newsend2.merge(context, writer);
            }
            int count = 0;
            int contador = 0;
            proposal(context, educativeproposalend, educativeproposalreference, educativeproposal, educativeproposallink, endbodypremiere, endbody, writer, proposal, count, contador);
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        } catch (ParseErrorException e) {
            e.printStackTrace();
        } catch (MethodInvocationException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void contructpremiere(ContentIF item, VelocityContext context, StringWriter writer, HttpServletRequest req, String id) {
        try {
            PremiereData data = (PremiereData) item.getContentData();
            Content contenido = contentManager.getContentById(item.getId());
            EducativeProposal proposal[] = data.getProposals();
            String databody = data.getText();
            String title = item.getName();
            Date publicationdate = null;
            Date expirationdate = null;
            if (databody == null) {
                databody = "";
            }
            if (title == null) {
                title = "";
            }
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            databody = HTMLUtil.textFromHTML(databody);
            databody = HTMLUtil.escapeHTML(databody);
            title = HTMLUtil.textFromHTML(title);
            title = HTMLUtil.escapeHTML(title);
            id = req.getRequestURL() + id;
            if (id.contains("http://www.educa.madrid.org:8080")) {
                id = id.replace(":8080", "");
            }
            String str = contenido.getMainLocale();
            int pos = str.indexOf("_");
            String locale = str.substring(0, pos);
            String expirationdate2 = null;
            String publicationdate2 = null;
            String dateString = null;
            try {
                dateString = formatter.format(data.getPremiereDate());
            } catch (RuntimeException e) {
                dateString = null;
            }
            try {
                expirationdate = item.getExpirationDate();
                expirationdate2 = formatter.format(expirationdate);
            } catch (RuntimeException e) {
                expirationdate2 = null;
            }
            try {
                publicationdate = item.getPublicationDate();
                publicationdate2 = formatter.format(publicationdate);
            } catch (RuntimeException e) {
                publicationdate2 = null;
            }
            context.put("expirationdate", expirationdate2);
            context.put("publicationdate", publicationdate2);
            context.put("locale", locale);
            context.put("id", id);
            context.put("title", replace(title));
            context.put("user", item.getAuthorId());
            premiere.merge(context, writer);
            Set<Categories> contenidos3 = CMSCommonData.getSelectedCategories(contenido, "knowledgeAreas", "cms.field.knowledgeAreas");
            for (Iterator it = contenidos3.iterator(); it.hasNext(); ) {
                Category n = (Category) it.next();
                context.put("knowledge", n.getName());
                knowledge.merge(context, writer);
            }
            Set<Categories> contenidos = CMSCommonData.getSelectedCategories(contenido, "discipline", "discipline");
            for (Iterator it = contenidos.iterator(); it.hasNext(); ) {
                Category n = (Category) it.next();
                context.put("discipline", n.getName());
                discipline.merge(context, writer);
            }
            Set<Categories> contenidos2 = CMSCommonData.getSelectedCategories(contenido, "consignees", "cms.field.consignees");
            for (Iterator it = contenidos2.iterator(); it.hasNext(); ) {
                Category n = (Category) it.next();
                context.put("consignees", n.getName());
                consignees.merge(context, writer);
            }
            if (publicationdate2 != null) {
                avaiable.merge(context, writer);
            }
            if (expirationdate2 != null) {
                valid.merge(context, writer);
            }
            premiereend.merge(context, writer);
            try {
                if (!data.getPlace().equals("")) {
                    context.put("local", data.getPlace());
                    location.merge(context, writer);
                }
            } catch (RuntimeException e) {
                log.error(e);
            }
            context.put("body", replace(databody));
            body.merge(context, writer);
            if (dateString != null) {
                context.put("data", dateString);
                datatemplate.merge(context, writer);
            }
            try {
                if (!data.getWebPageURL().equals("")) {
                    context.put("enlace", data.getWebPageURL());
                    enlace.merge(context, writer);
                }
            } catch (RuntimeException e) {
                log.error(e);
            }
            try {
                if (!data.getTrailerURL().equals("")) {
                    context.put("enlacetrailer", data.getTrailerURL());
                    enlacetrailer.merge(context, writer);
                }
            } catch (RuntimeException e) {
                log.error(e);
            }
            if (!data.getImage().getURLPath().equals("notConfigured")) {
                context.put("image", PortalUtil.getPortalURL(req, false) + req.getContextPath() + "/files/" + data.getImage().getURLPath());
                context.put("imagetext", data.getImageSubtitle());
                image.merge(context, writer);
            }
            if (!data.getSummaryImage().getURLPath().equals("notConfigured")) {
                context.put("imagesumary", PortalUtil.getPortalURL(req, false) + req.getContextPath() + "/files/" + data.getSummaryImage().getURLPath());
                context.put("textoimagensumary", data.getSummaryImageSubtitle());
                imagesumary.merge(context, writer);
            }
            int count = 0;
            int contador = 0;
            proposal2(context, educativeproposalend, educativeproposalreference, educativeproposal, educativeproposallink, endbodypremiere, endbody, writer, proposal, count, contador);
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        } catch (ParseErrorException e) {
            e.printStackTrace();
        } catch (MethodInvocationException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void contructenvents(ContentIF item, VelocityContext context, StringWriter writer, HttpServletRequest req, String id) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            ContentIF content = contentManager.getContentById(item.getId());
            Content contenido = contentManager.getContentById(item.getId());
            Set<Category> categoriesSet = new HashSet(getCategoriesFromIds(content.getCategories()));
            String q[] = categoriesSet.toString().split(",");
            String category[] = null;
            int z;
            for (z = 0; z < q.length; z++) {
                boolean dd = q[z].startsWith("label=category.event");
                if (dd == true) {
                    break;
                }
            }
            if (z < q.length) {
                category = q[z + 1].split("=");
            }
            String x[] = PropertyUtils.getNestedProperty(content, "contentData.file1").toString().split(",");
            String imagen[] = x[0].split("=");
            String description = getValues(item, "manifest.metadata.lom.general.description");
            String title = getValues(item, "manifest.metadata.lom.general.title");
            Date publicationdate = null;
            Date expirationdate = null;
            String local = null;
            String duracion = null;
            String patrocinador = null;
            try {
                local = PropertyUtils.getNestedProperty(content, "contentData.performPlace").toString();
            } catch (RuntimeException e) {
            }
            try {
                duracion = PropertyUtils.getNestedProperty(content, "contentData.duration").toString();
            } catch (RuntimeException e) {
            }
            try {
                patrocinador = PropertyUtils.getNestedProperty(content, "contentData.sponsor").toString();
            } catch (RuntimeException e) {
            }
            String expirationdate2 = null;
            String publicationdate2 = null;
            String start2 = null;
            String end2 = null;
            if (description == null) {
                description = "";
            }
            if (title == null) {
                title = "";
            }
            description = HTMLUtil.textFromHTML(description);
            description = HTMLUtil.escapeHTML(description);
            title = HTMLUtil.textFromHTML(title);
            title = HTMLUtil.escapeHTML(title);
            id = req.getRequestURL() + id;
            if (id.contains("http://www.educa.madrid.org:8080")) {
                id = id.replace(":8080", "");
            }
            String str = contenido.getMainLocale();
            int pos = str.indexOf("_");
            String locale = str.substring(0, pos);
            try {
                expirationdate = item.getExpirationDate();
                expirationdate2 = formatter.format(expirationdate);
            } catch (RuntimeException e) {
                expirationdate2 = null;
            }
            try {
                publicationdate = item.getPublicationDate();
                publicationdate2 = formatter.format(publicationdate);
            } catch (RuntimeException e) {
                publicationdate2 = null;
            }
            try {
                start2 = formatter.format(PropertyUtils.getNestedProperty(content, "contentData.beginDate"));
            } catch (RuntimeException e) {
                start2 = null;
            }
            try {
                end2 = formatter.format(PropertyUtils.getNestedProperty(content, "contentData.endDate"));
            } catch (RuntimeException e) {
                end2 = null;
            }
            context.put("expirationdate", expirationdate2);
            context.put("publicationdate", publicationdate2);
            context.put("locale", locale);
            context.put("id", id);
            context.put("title", replace(title));
            context.put("user", item.getAuthorId());
            context.put("datestart", start2);
            context.put("dateend", end2);
            context.put("description", replace(description));
            event.merge(context, writer);
            Set<Categories> contenidos3 = CMSCommonData.getSelectedCategories(contenido, "knowledgeAreas", "cms.field.knowledgeAreas");
            for (Iterator it = contenidos3.iterator(); it.hasNext(); ) {
                Category n = (Category) it.next();
                context.put("knowledge", n.getName());
                knowledge.merge(context, writer);
            }
            Set<Categories> contenidos = CMSCommonData.getSelectedCategories(contenido, "discipline", "discipline");
            for (Iterator it = contenidos.iterator(); it.hasNext(); ) {
                Category n = (Category) it.next();
                context.put("discipline", n.getName());
                discipline.merge(context, writer);
            }
            Set<Categories> contenidos2 = CMSCommonData.getSelectedCategories(contenido, "consignees", "cms.field.consignees");
            for (Iterator it = contenidos2.iterator(); it.hasNext(); ) {
                Category n = (Category) it.next();
                context.put("consignees", n.getName());
                consignees.merge(context, writer);
            }
            if (publicationdate2 != null) {
                avaiable.merge(context, writer);
            }
            if (expirationdate2 != null) {
                valid.merge(context, writer);
            }
            eventend.merge(context, writer);
            try {
                if (!local.equals("")) {
                    context.put("local", local);
                    location.merge(context, writer);
                }
            } catch (RuntimeException e) {
                log.error(e);
            }
            try {
                if (!duracion.equals("")) {
                    context.put("duration", duracion);
                    duration.merge(context, writer);
                }
            } catch (RuntimeException e) {
                log.error(e);
            }
            try {
                if (!patrocinador.equals("")) {
                    context.put("sponsor", patrocinador);
                    sponsor.merge(context, writer);
                }
            } catch (RuntimeException e) {
                log.error(e);
            }
            if (category != null) {
                context.put("category", category[1]);
                eventcategory.merge(context, writer);
            }
            if (!imagen[1].equals("notConfigured")) {
                context.put("image", PortalUtil.getPortalURL(req, false) + req.getContextPath() + "/files/" + imagen[1]);
                context.put("imagetext", PropertyUtils.getNestedProperty(content, "contentData.subtitle"));
                image.merge(context, writer);
            }
            eventend2.merge(context, writer);
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        } catch (ParseErrorException e) {
            e.printStackTrace();
        } catch (MethodInvocationException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void contructannouces(ContentIF item, VelocityContext context, StringWriter writer, HttpServletRequest req, String id) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            ContentIF content = contentManager.getContentById(item.getId());
            Content contenido = contentManager.getContentById(item.getId());
            String x[] = PropertyUtils.getNestedProperty(content, "contentData.image").toString().split(",");
            String image2[] = x[0].split("=");
            String y[] = PropertyUtils.getNestedProperty(content, "contentData.file").toString().split(",");
            String file[] = y[0].split("=");
            String description = null;
            String title = null;
            try {
                description = getValues(item, "manifest.metadata.lom.general.description");
            } catch (RuntimeException e) {
            }
            try {
                title = getValues(item, "manifest.metadata.lom.general.title");
            } catch (RuntimeException e) {
            }
            Date publicationdate = null;
            Date expirationdate = null;
            String expirationdate2 = null;
            String publicationdate2 = null;
            if (description == null) {
                description = "";
            }
            if (title == null) {
                title = "";
            }
            description = HTMLUtil.textFromHTML(description);
            description = HTMLUtil.escapeHTML(description);
            title = HTMLUtil.textFromHTML(title);
            title = HTMLUtil.escapeHTML(title);
            id = req.getRequestURL() + id;
            if (id.contains("http://www.educa.madrid.org:8080")) {
                id = id.replace(":8080", "");
            }
            String str = contenido.getMainLocale();
            int pos = str.indexOf("_");
            String locale = str.substring(0, pos);
            try {
                expirationdate = item.getExpirationDate();
                expirationdate2 = formatter.format(expirationdate);
            } catch (RuntimeException e) {
                expirationdate2 = null;
            }
            try {
                publicationdate = item.getPublicationDate();
                publicationdate2 = formatter.format(publicationdate);
            } catch (RuntimeException e) {
                publicationdate2 = null;
            }
            context.put("expirationdate", expirationdate2);
            context.put("publicationdate", publicationdate2);
            context.put("locale", locale);
            context.put("id", id);
            context.put("title", replace(title));
            context.put("user", item.getAuthorId());
            context.put("description", replace(description));
            announce.merge(context, writer);
            Set<Categories> contenidos3 = CMSCommonData.getSelectedCategories(contenido, "knowledgeAreas", "cms.field.knowledgeAreas");
            for (Iterator it = contenidos3.iterator(); it.hasNext(); ) {
                Category n = (Category) it.next();
                context.put("knowledge", n.getName());
                knowledge.merge(context, writer);
            }
            Set<Categories> contenidos = CMSCommonData.getSelectedCategories(contenido, "discipline", "discipline");
            for (Iterator it = contenidos.iterator(); it.hasNext(); ) {
                Category n = (Category) it.next();
                context.put("discipline", n.getName());
                discipline.merge(context, writer);
            }
            Set<Categories> contenidos2 = CMSCommonData.getSelectedCategories(contenido, "consignees", "cms.field.consignees");
            for (Iterator it = contenidos2.iterator(); it.hasNext(); ) {
                Category n = (Category) it.next();
                context.put("consignees", n.getName());
                consignees.merge(context, writer);
            }
            if (publicationdate2 != null) {
                avaiable.merge(context, writer);
            }
            if (expirationdate2 != null) {
                valid.merge(context, writer);
            }
            announceend.merge(context, writer);
            if (!PropertyUtils.getNestedProperty(content, "contentData.author").equals("")) {
                context.put("creator", PropertyUtils.getNestedProperty(content, "contentData.author"));
                creator.merge(context, writer);
            }
            if (!image2[1].equals("notConfigured")) {
                context.put("image", PortalUtil.getPortalURL(req, false) + req.getContextPath() + "/files/" + image2[1]);
                context.put("imagetext", PropertyUtils.getNestedProperty(content, "contentData.imageSubtitle"));
                image.merge(context, writer);
            }
            if (!file[1].equals("notConfigured")) {
                context.put("file", PortalUtil.getPortalURL(req, false) + req.getContextPath() + "/files/" + file[1]);
                context.put("filetext", PropertyUtils.getNestedProperty(content, "contentData.fileAlternativeText"));
                filetemplate.merge(context, writer);
            }
            announceend2.merge(context, writer);
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        } catch (ParseErrorException e) {
            e.printStackTrace();
        } catch (MethodInvocationException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initTemplates(VelocityEngine ve) {
        try {
            avaiable = ve.getTemplate("avaiable.vm");
            body = ve.getTemplate("body.vm");
            datatemplate = ve.getTemplate("data.vm");
            enlace = ve.getTemplate("enlace.vm");
            enlacetrailer = ve.getTemplate("enlacetrailer.vm");
            imagesumary = ve.getTemplate("imagesumary.vm");
            creator = ve.getTemplate("creator.vm");
            filetemplate = ve.getTemplate("file.vm");
            mainnewsimage = ve.getTemplate("mainnewsimage.vm");
            secondnewsimage = ve.getTemplate("secondnewsimage.vm");
            keywords = ve.getTemplate("keywords.vm");
            image = ve.getTemplate("image.vm");
            eventcategory = ve.getTemplate("eventcategory.vm");
            sponsor = ve.getTemplate("sponsor.vm");
            duration = ve.getTemplate("duration.vm");
            location = ve.getTemplate("location.vm");
            valid = ve.getTemplate("valid.vm");
            head = ve.getTemplate("head.vm");
            news = ve.getTemplate("news.vm");
            newsend = ve.getTemplate("newsend.vm");
            newsend2 = ve.getTemplate("newsend2.vm");
            category = ve.getTemplate("category.vm");
            foot = ve.getTemplate("foot.vm");
            educativeproposal = ve.getTemplate("educativeproposal.vm");
            educativeproposallink = ve.getTemplate("educativeproposallink.vm");
            premiere = ve.getTemplate("premiere.vm");
            premiereend = ve.getTemplate("premiereend.vm");
            endbody = ve.getTemplate("endbody.vm");
            endbodypremiere = ve.getTemplate("endbodypremiere.vm");
            educativeproposalend = ve.getTemplate("educativeproposalend.vm");
            educativeproposalreference = ve.getTemplate("educativeproposalreference.vm");
            event = ve.getTemplate("event.vm");
            eventend = ve.getTemplate("eventend.vm");
            eventend2 = ve.getTemplate("eventend2.vm");
            announce = ve.getTemplate("announce.vm");
            announceend = ve.getTemplate("announceend.vm");
            announceend2 = ve.getTemplate("announceend2.vm");
            discipline = ve.getTemplate("discipline.vm");
            consignees = ve.getTemplate("consignees.vm");
            knowledge = ve.getTemplate("knowledge.vm");
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        } catch (ParseErrorException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getNewsType(ContentIF item) {
        ScribeDynaBean type = null;
        try {
            type = (ScribeDynaBean) item.getContentData();
        } catch (RuntimeException e) {
            return "";
        }
        return type.getDynaClass().getName();
    }

    private String replace(String data) {
        String data2 = data;
        data2 = data2.replace("&amp;", "_");
        return data2;
    }

    private String getValues(ContentIF item, String value) throws ContentNotFoundException, MalformedContentException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        ContentIF content = contentManager.getContentById(item.getId());
        I18NString iString = (I18NString) PropertyUtils.getNestedProperty(content, value);
        return iString.getLocalizedString();
    }

    private int proposal(VelocityContext context, Template educativeproposalend, Template educativeproposalreference, Template educativeproposal, Template educativeproposallink, Template endbodypremiere, Template endbody, StringWriter writer, EducativeProposal[] proposal, int count, int contador) throws IOException {
        while (!proposal[count].isEmpty()) {
            context.put("educativeProposallink", "http://educa.madrid.org/educativeProposal/" + count);
            educativeproposallink.merge(context, writer);
            count++;
        }
        endbody.merge(context, writer);
        count = 0;
        while (!proposal[count].isEmpty()) {
            String Title = HTMLUtil.textFromHTML(proposal[count].getTitle());
            Title = HTMLUtil.escapeHTML(Title);
            String Body = HTMLUtil.textFromHTML(proposal[count].getBody());
            Body = HTMLUtil.escapeHTML(Body);
            String GeneralGoals = HTMLUtil.textFromHTML(proposal[count].getGeneralGoals());
            GeneralGoals = HTMLUtil.escapeHTML(GeneralGoals);
            String SpecificGoals = HTMLUtil.textFromHTML(proposal[count].getSpecificGoals());
            SpecificGoals = HTMLUtil.escapeHTML(SpecificGoals);
            String ConceptualContents = HTMLUtil.textFromHTML(proposal[count].getConceptualContents());
            ConceptualContents = HTMLUtil.escapeHTML(ConceptualContents);
            String ActitudinalContents = HTMLUtil.textFromHTML(proposal[count].getActitudinalContents());
            ActitudinalContents = HTMLUtil.escapeHTML(ActitudinalContents);
            String ProcedimentalContents = HTMLUtil.textFromHTML(proposal[count].getProcedimentalContents());
            ProcedimentalContents = HTMLUtil.escapeHTML(ProcedimentalContents);
            String EvaluationGoals = HTMLUtil.textFromHTML(proposal[count].getEvaluationGoals());
            EvaluationGoals = HTMLUtil.escapeHTML(EvaluationGoals);
            String Activities = HTMLUtil.textFromHTML(proposal[count].getActivities());
            Activities = HTMLUtil.escapeHTML(Activities);
            String Resources = HTMLUtil.textFromHTML(proposal[count].getResources());
            Resources = HTMLUtil.escapeHTML(Resources);
            context.put("educativeProposallink", "http://educa.madrid.org/educativeProposal/" + count);
            context.put("proposaltitulo", Title);
            context.put("proposalbody", Body);
            context.put("geralobjectivesproposal", GeneralGoals);
            context.put("specificobjectivesproposal", SpecificGoals);
            context.put("conceptualproposal", ConceptualContents);
            context.put("actitudinalesproposal", ActitudinalContents);
            context.put("procedimentalesproposal", ProcedimentalContents);
            context.put("evaluacionproposal", EvaluationGoals);
            context.put("actividadesproposal", Activities);
            context.put("contenidosproposal", Resources);
            educativeproposal.merge(context, writer);
            Link link[] = proposal[count].getLinks();
            while (!link[contador].isEmpty()) {
                context.put("url", link[contador].getUrl());
                context.put("urltext", link[contador].getText());
                educativeproposalreference.merge(context, writer);
                contador++;
                if (contador == 4) break;
            }
            count++;
            educativeproposalend.merge(context, writer);
        }
        return count;
    }

    private int proposal2(VelocityContext context, Template educativeproposalend, Template educativeproposalreference, Template educativeproposal, Template educativeproposallink, Template endbodypremiere, Template endbody, StringWriter writer, EducativeProposal[] proposal, int count, int contador) throws IOException {
        while (!proposal[count].isEmpty()) {
            context.put("educativeProposallink", "http://educa.madrid.org/educativeProposal/" + count);
            educativeproposallink.merge(context, writer);
            count++;
        }
        endbodypremiere.merge(context, writer);
        count = 0;
        while (!proposal[count].isEmpty()) {
            String Title = HTMLUtil.textFromHTML(proposal[count].getTitle());
            Title = HTMLUtil.escapeHTML(Title);
            String Body = HTMLUtil.textFromHTML(proposal[count].getBody());
            Body = HTMLUtil.escapeHTML(Body);
            String GeneralGoals = HTMLUtil.textFromHTML(proposal[count].getGeneralGoals());
            GeneralGoals = HTMLUtil.escapeHTML(GeneralGoals);
            String SpecificGoals = HTMLUtil.textFromHTML(proposal[count].getSpecificGoals());
            SpecificGoals = HTMLUtil.escapeHTML(SpecificGoals);
            String ConceptualContents = HTMLUtil.textFromHTML(proposal[count].getConceptualContents());
            ConceptualContents = HTMLUtil.escapeHTML(ConceptualContents);
            String ActitudinalContents = HTMLUtil.textFromHTML(proposal[count].getActitudinalContents());
            ActitudinalContents = HTMLUtil.escapeHTML(ActitudinalContents);
            String ProcedimentalContents = HTMLUtil.textFromHTML(proposal[count].getProcedimentalContents());
            ProcedimentalContents = HTMLUtil.escapeHTML(ProcedimentalContents);
            String EvaluationGoals = HTMLUtil.textFromHTML(proposal[count].getEvaluationGoals());
            EvaluationGoals = HTMLUtil.escapeHTML(EvaluationGoals);
            String Activities = HTMLUtil.textFromHTML(proposal[count].getActivities());
            Activities = HTMLUtil.escapeHTML(Activities);
            String Resources = HTMLUtil.textFromHTML(proposal[count].getResources());
            Resources = HTMLUtil.escapeHTML(Resources);
            context.put("educativeProposallink", "http://educa.madrid.org/educativeProposal/" + count);
            context.put("proposaltitulo", Title);
            context.put("proposalbody", Body);
            context.put("geralobjectivesproposal", GeneralGoals);
            context.put("specificobjectivesproposal", SpecificGoals);
            context.put("conceptualproposal", ConceptualContents);
            context.put("actitudinalesproposal", ActitudinalContents);
            context.put("procedimentalesproposal", ProcedimentalContents);
            context.put("evaluacionproposal", EvaluationGoals);
            context.put("actividadesproposal", Activities);
            context.put("contenidosproposal", Resources);
            educativeproposal.merge(context, writer);
            Link link[] = proposal[count].getLinks();
            while (!link[contador].isEmpty()) {
                context.put("url", link[contador].getUrl());
                context.put("urltext", link[contador].getText());
                educativeproposalreference.merge(context, writer);
                contador++;
                if (contador == 4) break;
            }
            count++;
            educativeproposalend.merge(context, writer);
        }
        return count;
    }

    static class RDFParams {

        String portletName;

        String layoutId;

        String ownerId;

        String companyId;

        String groupId;

        public static RDFParams getFromPath(String path, HttpServletRequest request, ServletContext ctx) {
            StringTokenizer st = new StringTokenizer(path, "/", false);
            if (st.countTokens() < 2) {
                throw new IllegalArgumentException("Not all 2 needed parameters have been provided");
            }
            RDFParams result = new RDFParams();
            result.companyId = ctx.getInitParameter("company_id");
            result.layoutId = st.nextToken();
            StringTokenizer groupAndLayoutTokenizer = new StringTokenizer(result.layoutId, ".");
            result.groupId = groupAndLayoutTokenizer.nextToken();
            result.ownerId = liferayHelper.getOwnerId(null, result.groupId);
            StringTokenizer strportlet = new StringTokenizer(st.nextToken(), ".", false);
            result.portletName = strportlet.nextToken();
            result.validate();
            return result;
        }

        private void validate() {
            if ((portletName == null) || (layoutId == null)) {
                throw new IllegalArgumentException("Not all 2 needed parameters have been provided");
            }
        }
    }

    public static List<Category> getCategoriesFromIds(Collection<String> categoryIds) throws CMSRuntimeException {
        List<Category> categories = new ArrayList<Category>();
        CategoryManager categoryManager = ManagerRegistry.getCategoryManager();
        for (String categoryId : categoryIds) {
            Category category;
            try {
                category = categoryManager.getCategoryById(categoryId);
                categories.add(category);
            } catch (CategoryNotFoundException e) {
                log.warn("Category [" + categoryId + "] not found");
            } catch (CMSRuntimeException e) {
                String errorMessage = "Error obtaining categroy [" + categoryId + "]: ";
                log.error(errorMessage, e);
                throw new CMSRuntimeException(errorMessage, e);
            }
        }
        return categories;
    }
}
