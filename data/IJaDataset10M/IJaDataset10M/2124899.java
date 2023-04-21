package com.nhncorp.usf.macro.method;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import com.nhncorp.lucy.common.util.ResourceLoader;
import com.nhncorp.lucy.common.util.SchemaEntityResolver;
import com.nhncorp.lucy.common.util.XMLDOMUtil;
import com.nhncorp.usf.core.config.AttributeInfo;
import com.nhncorp.usf.core.config.AttributeInfo.ATTRIBUTE_NAME;

/**
 * @author Web Platform Development Team
 */
public class LibraryMethodLoader implements MethodInfoLoader {

    static Log log = LogFactory.getLog(LibraryMethodLoader.class);

    public static final String SUFFIX_TEMPLATE_METHOD_MODEL = ".tmm";

    private List<MethodInfo> methodInfoList = new ArrayList<MethodInfo>();

    private ClassLoader classLoader;

    static SchemaEntityResolver entityResolver = new SchemaEntityResolver();

    static {
        String schemaVersion = AttributeInfo.getInstance().getAttribute(ATTRIBUTE_NAME.schemaVersion);
        Properties urlMap = new Properties();
        String url = null;
        String location = "META-INF/schema/usf-macro-method.xsd";
        if ("1.0".equals(schemaVersion)) {
            url = "http://dev.naver.com/xsd/usf/1.0/usf-macro-method.xsd";
        } else {
            url = "http://dev.naver.com/xsd/usf/usf-macro-method.xsd";
        }
        log.debug("TemplateMethod Schema URL : " + url);
        urlMap.put(url, location);
        entityResolver.setSchemaProperty(urlMap);
    }

    /**
     * Instantiates a new library method loader.
     */
    public LibraryMethodLoader() {
    }

    /**
     * Instantiates a new library method loader.
     *
     * @param classLoader the class loader
     */
    public LibraryMethodLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    /**
     * Load.
     *
     * @return the list< method info>
     */
    public List<MethodInfo> load() {
        log.debug("Template MethodInfo Loading...(library)");
        Enumeration<URL> resourceUrls = null;
        try {
            if (classLoader == null) {
                ResourceLoader resourceLoader = new ResourceLoader();
                resourceUrls = resourceLoader.getUrls("META-INF");
            } else {
                resourceUrls = classLoader.getResources("META-INF");
            }
        } catch (IOException e) {
            log.error("cannot loaded META-INF Resource", e);
        }
        if (resourceUrls == null) {
            return methodInfoList;
        }
        while (resourceUrls.hasMoreElements()) {
            URL url = resourceUrls.nextElement();
            String jarFilename = getFileName(url.getFile());
            if (jarFilename == null) {
                continue;
            }
            try {
                getMethodInfo(jarFilename);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return methodInfoList;
    }

    /**
     *
     * @param node the Node
     * @return the Method Infomation
     */
    public List<MethodInfo> load(Node node) {
        return this.load();
    }

    /**
     * 주어진 파일이름으로 부터 jar파일의 경로를 찾아낸다
     *
     * @param fullName classpath에서 찾은 META-INF의 경로
     * @return the file name
     */
    private String getFileName(String fullName) {
        int pos = fullName.lastIndexOf(".jar");
        if (pos < 0) {
            return null;
        }
        File file = null;
        try {
            String fileName = fullName.substring(0, pos + 4);
            fileName = fileName.replaceAll(" ", "%20");
            file = new File(new URI(fileName));
        } catch (URISyntaxException e) {
            log.error(e);
        }
        return file == null ? null : file.getAbsolutePath();
    }

    /**
     * .jar파일로 부터 MethodInfo 생성
     *
     * @param jarFilename jar 파일경로
     * @throws IOException the IOException
     */
    private void getMethodInfo(String jarFilename) throws IOException {
        JarFile jar = new JarFile(jarFilename);
        Enumeration<JarEntry> entries = jar.entries();
        InputStream is = null;
        while (entries.hasMoreElements()) {
            JarEntry jarEntry = entries.nextElement();
            if (jarEntry.getName().endsWith(SUFFIX_TEMPLATE_METHOD_MODEL)) {
                log.debug(".tmm File : " + jarEntry.getName());
                try {
                    is = jar.getInputStream(jarEntry);
                    processTmm(is);
                } catch (IOException ioe) {
                    log.debug("fail to parsing .tmm file : " + jarEntry.getName());
                } finally {
                    if (is != null) {
                        is.close();
                    }
                }
            }
        }
    }

    /**
     * .tmm 파일 설정을 읽어 MethodInfo를 생성한다
     *
     * @param is jar에서 가져온 .tmm파일의 InputStream
     * @return
     * @throws Exception
     */
    @SuppressWarnings("deprecation")
    private void processTmm(InputStream is) {
        try {
            Document document = XMLDOMUtil.loading(is, true, entityResolver);
            Node root = document.getDocumentElement();
            String namespace = XMLDOMUtil.getValue(root, "@namespace");
            Iterator<Node> elements = XMLDOMUtil.getNodeIterator(root, "usf-macro:method");
            while (elements.hasNext()) {
                Node methodNode = elements.next();
                String methodName = XMLDOMUtil.getValue(methodNode, "@name");
                String className = XMLDOMUtil.getValue(methodNode, "@class");
                log.debug("generate methodinfo : " + namespace + "/" + methodName + "/" + className);
                MethodInfo methodInfo = new MethodInfo(methodName, className, namespace);
                this.methodInfoList.add(methodInfo);
            }
        } catch (Exception e) {
            log.warn("methodInfo generate fail", e);
        }
    }
}
