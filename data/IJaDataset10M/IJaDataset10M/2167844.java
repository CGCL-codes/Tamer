package bma.bricks.otter.publish.engine.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.StringWriter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import bma.bricks.idisk.IDiskUtil;
import bma.bricks.idisk.manager.IDiskManager;
import bma.bricks.io.IOUtil;
import bma.bricks.io.InputStreamSource;
import bma.bricks.json.JSONObject;
import bma.bricks.json.JSONUtil;
import bma.bricks.json.impl.JSONObjectImpl;
import bma.bricks.lang.DateTimeUtil;
import bma.bricks.lang.ExceptionUtil;
import bma.bricks.mis.MIS;
import bma.bricks.mis.MISAddress;
import bma.bricks.mis.MISInvokeProfile;
import bma.bricks.mis.impl.MISInvokeBodyImpl;
import bma.bricks.module.MMUtil;
import bma.bricks.module.ModuleManager;
import bma.bricks.otter.model.feature.PublishResult;
import bma.bricks.otter.publish.engine.bridge.method.LoadEntityMethod;
import bma.bricks.otter.publish.engine.bridge.method.PropertyMethod;
import bma.bricks.otter.publish.engine.bridge.method.QueryEntityMethod;
import bma.bricks.otter.publish.engine.bridge.method.SubStringMethod;
import bma.bricks.otter.publish.engine.bridge.method.URLMethod;
import bma.bricks.otter.publish.engine.bridge.wrap.GetPropertyObjectWrapper;
import bma.bricks.otter.publish.engine.bridge.wrap.JSONObjectWrapper;
import bma.bricks.otter.publish.engine.bridge.wrap.LinkedObjectWrapper;
import bma.bricks.otter.publish.engine.mis.PublishRequestHandler;
import bma.bricks.otter.publish.url.form.PublishUrlForm;
import bma.bricks.otter.publish.url.manager.PublishUrlManager;
import bma.bricks.util.T;
import bma.bricks.util.TO;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateModel;

/**
 * �����������
 * 
 * @author zrb
 * 
 */
public class PublishEngineService {

    public static final String PREV_PAGE_NAME = "_prev";

    public static final String INDEX_PAGE_NAME = "index.html";

    private static final long CLEAR_PREVIEW_TIME = 1 * DateTimeUtil.HOUR;

    public static Log log = LogFactory.getLog(PublishEngineService.class);

    private static PublishEngineService instance = new PublishEngineService();

    public static PublishEngineService getInstance() {
        return instance;
    }

    private Configuration config = new Configuration();

    public Configuration getConfiguration() {
        return this.config;
    }

    /**
	 * ��ӹ������ȥ����
	 * 
	 * @param name
	 * @param tm
	 */
    public void setSharedVariable(String name, TemplateModel tm) {
        this.config.setSharedVariable(name, tm);
    }

    public static String toPreviewUrl(String url) {
        StringBuffer buf = new StringBuffer(url);
        int index = buf.lastIndexOf(".");
        buf.insert(index, "_pre");
        return buf.toString();
    }

    public static String toIndexUrl(String url) {
        return url + INDEX_PAGE_NAME;
    }

    public static String toPageUrl(String url, boolean preview) {
        if (T.empty(url)) {
            return url;
        }
        if (url.endsWith("/")) {
            url = toIndexUrl(url);
        }
        if (preview) {
            return toPreviewUrl(url);
        }
        return url;
    }

    public void saveUrl(String eid, String type, int num, String url, String title) {
        PublishUrlForm form = new PublishUrlForm();
        form.setEntityId(eid);
        form.setTitle(title);
        form.setType(type);
        form.setUrlNum(num);
        form.setUrl(url);
        PublishUrlManager.getInstance().save(form);
    }

    /**
	 * ����ҳ�浽idsik
	 * 
	 * @param templateName
	 *            ģ�����
	 * @param rootMap
	 *            ģ��Ҫʹ�õ������
	 * @param url
	 *            �����ĵ�ַ
	 * @return
	 */
    public PublishResult publish2disk(String templateName, Map<String, Object> rootMap, String url, boolean preview) {
        String pageData = PublishEngineService.getInstance().processTemplate(templateName, rootMap);
        if (preview) {
            url = toPreviewUrl(url);
        }
        save2disk(url, pageData, preview);
        return PublishResult.done(url, pageData);
    }

    public void save2disk(String url, String pageData, boolean preview) {
        String diskPath = IDiskUtil.url2disk(url);
        InputStreamSource in = PublishEngineUtil.createInputStreamSource(pageData, PublishEngineService.getInstance().getEncoding());
        IDiskManager.getInstance().write(diskPath, in);
        if (preview) {
            invokePreviewRequest(url);
        }
    }

    public boolean removeUrl(String url) {
        String diskPath = IDiskUtil.url2disk(url);
        return IDiskManager.getInstance().delete(diskPath);
    }

    private void invokePreviewRequest(String url) {
        JSONObject request = new JSONObjectImpl();
        JSONUtil.set(request, PublishEngine.PROFILE_URL, url);
        MISInvokeBodyImpl ibody = new MISInvokeBodyImpl();
        ibody.setAddress(MISAddress.create(null, PublishRequestHandler.ID, "clearTemp"));
        ibody.setId(url);
        ibody.setBody(request);
        MISInvokeProfile.setScheduleTime(ibody, new Date(System.currentTimeMillis() + CLEAR_PREVIEW_TIME));
        MISInvokeProfile.setOverride(ibody, true);
        try {
            MIS.getInstance().invoke(ibody, null, null);
        } catch (Exception e) {
            log.error("invoke clear preview[" + url + "] fail", e);
        }
    }

    /**
	 * �����������ݿ�,��ҳ���,�����ַ�
	 * 
	 * @param block
	 * @param rootMap
	 * @return
	 */
    public String processTemplate(String templateName, Map<String, Object> rootMap) {
        if (log.isDebugEnabled()) {
            log.debug("process template [" + templateName + "]");
        }
        StringWriter writer = new StringWriter();
        try {
            Template freeMarker = this.config.getTemplate(templateName);
            freeMarker.process(rootMap, writer);
            writer.flush();
            return writer.toString();
        } catch (Exception e) {
            if (log.isWarnEnabled()) {
                log.warn("process [" + templateName + "] fail");
            }
            throw ExceptionUtil.handle(e);
        } finally {
            IOUtil.close(writer);
        }
    }

    public void config(TO to, ModuleManager manager) {
        File cfgFile = MMUtil.getModuleFile(manager, to.stringValue("freemarkerConfig", "publish_freemarker.properties"));
        if (cfgFile != null && cfgFile.exists()) {
            FileInputStream in = null;
            try {
                in = new FileInputStream(cfgFile);
                this.config.setSettings(in);
            } catch (Exception e) {
                throw ExceptionUtil.handle(e);
            } finally {
                IOUtil.close(in);
            }
        }
        setSharedVariable("PROP", new PropertyMethod());
        setSharedVariable("LOAD", new LoadEntityMethod());
        setSharedVariable("QUERY", new QueryEntityMethod());
        setSharedVariable("URL", new URLMethod());
        setSharedVariable("SUBSTR", new SubStringMethod());
        LinkedObjectWrapper ow = LinkedObjectWrapper.getInstance();
        GetPropertyObjectWrapper gow = new GetPropertyObjectWrapper();
        gow.setThisWrap(ow);
        ow.addWrapper(gow);
        JSONObjectWrapper jow = new JSONObjectWrapper();
        jow.setThisWrap(ow);
        ow.addWrapper(jow);
        this.config.setObjectWrapper(ow);
        this.config.setTemplateLoader(new PublishTemplateLoader());
    }

    public String getEncoding() {
        return "GBK";
    }

    public String buildPager(List<String> urllist, int cur, Map<String, Object> rootMap) {
        String urls = urllist.get(cur);
        rootMap.put("��ҳ����", urllist);
        rootMap.put("��ǰҳ��", cur);
        rootMap.put("���ҳ��", urllist.size());
        rootMap.put("���ҳ������", urllist.get(urllist.size() - 1));
        rootMap.put("��ǰҳ������", urllist.get(0));
        rootMap.put("��һҳ������", urllist.get(cur - 1 < 0 ? 0 : cur - 1));
        rootMap.put("��һҳ������", urllist.get(cur + 1 >= urllist.size() ? urllist.size() - 1 : cur + 1));
        return urls;
    }

    public PublishResult publishPage2disk(String templateContent, Map<String, Object> rootMap, String url, boolean preview) {
        String pageData = processTemplateContent(templateContent, rootMap);
        if (preview) {
            url = toPreviewUrl(url);
        }
        save2disk(url, pageData, preview);
        return PublishResult.done(url, pageData);
    }

    public String processTemplateContent(String templateContent, Map<String, Object> rootMap) {
        if (T.empty(templateContent)) {
            return null;
        }
        StringWriter writer = new StringWriter();
        try {
            Template freeMarker = new Template("templateName", new java.io.StringReader(templateContent), this.config);
            freeMarker.process(rootMap, writer);
            writer.flush();
            return writer.toString();
        } catch (Exception e) {
            throw ExceptionUtil.handle(e);
        } finally {
            IOUtil.close(writer);
        }
    }
}
