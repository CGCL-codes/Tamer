package com.butnet.myframe.creator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.butnet.myframe.template.Template;
import com.butnet.myframe.template.TemplateNode;
import com.butnet.myframe.template.TemplateReader;
import com.butnet.myframe.template.impl.TemplateLexerImpl;
import com.butnet.myframe.template.impl.TemplateReaderImpl;

public class Creator2 {

    private static Template formTemplate = null;

    private static Template actionTemplate = null;

    private static Template idaoTemplate = null;

    private static Template daoTemplate = null;

    private static Template daoFactoryTemplate = null;

    private static Template iserviceTemplate = null;

    private static Template serviceTemplate = null;

    private static Template serviceFactoryTemplate = null;

    private static Template applicationContextHibernateTemplate = null;

    private static Template applicationContextDaoTemplate = null;

    private static Template applicationContextStrutsTemplate = null;

    private static Template strutsConfigTemplate = null;

    private static Template databasePropertiesTemplate = null;

    private static Template webXmlTemplate = null;

    private static Template listJspTemplate = null;

    private static Template createJspTemplate = null;

    private static String formPackage = null;

    private static File formDir = null;

    private static String actionPackage = null;

    private static File actionDir = null;

    private static String iservicePackage = null;

    private static File iserviceDir = null;

    private static String servicePackage = null;

    private static File serviceDir = null;

    private static String serviceFactoryClassName = null;

    private static String idaoPackage = null;

    private static File idaoDir = null;

    private static String daoPackage = null;

    private static File daoDir = null;

    private static String daoFactoryClassName = null;

    private static String projectName = null;

    private static File projectSrcDir = null;

    public static Template getServiceFactoryTemplate() {
        if (serviceFactoryTemplate == null) {
            InputStream in = Creator2.class.getResourceAsStream("/com/butnet/myframe/creator/template/ServiceFactory.tpl");
            TemplateReader r = new TemplateReaderImpl(new TemplateLexerImpl());
            serviceFactoryTemplate = r.read(in);
            try {
                in.close();
            } catch (IOException e) {
                serviceFactoryTemplate = null;
                e.printStackTrace();
            }
        }
        return serviceFactoryTemplate;
    }

    public static Template getServiceTemplate() {
        if (serviceTemplate == null) {
            InputStream in = Creator2.class.getResourceAsStream("/com/butnet/myframe/creator/template/Service.tpl");
            TemplateReader r = new TemplateReaderImpl(new TemplateLexerImpl());
            serviceTemplate = r.read(in);
            try {
                in.close();
            } catch (IOException e) {
                serviceTemplate = null;
                e.printStackTrace();
            }
        }
        return serviceTemplate;
    }

    public static Template getIserviceTemplate() {
        if (iserviceTemplate == null) {
            InputStream in = Creator2.class.getResourceAsStream("/com/butnet/myframe/creator/template/IService.tpl");
            TemplateReader r = new TemplateReaderImpl(new TemplateLexerImpl());
            iserviceTemplate = r.read(in);
            try {
                in.close();
            } catch (IOException e) {
                iserviceTemplate = null;
                e.printStackTrace();
            }
        }
        return iserviceTemplate;
    }

    private static TemplateNode getApplicationContextDaoTemplate() {
        if (applicationContextDaoTemplate == null) {
            InputStream in = Creator2.class.getResourceAsStream("/com/butnet/myframe/creator/template/xml/applicationContext-dao.xml.tpl");
            TemplateReader r = new TemplateReaderImpl(new TemplateLexerImpl());
            applicationContextDaoTemplate = r.read(in);
            try {
                in.close();
            } catch (IOException e) {
                applicationContextDaoTemplate = null;
                e.printStackTrace();
            }
        }
        return applicationContextDaoTemplate;
    }

    private static TemplateNode getApplicationContextStrutsTemplate() {
        if (applicationContextStrutsTemplate == null) {
            InputStream in = Creator2.class.getResourceAsStream("/com/butnet/myframe/creator/template/xml/applicationContext-struts.xml.tpl");
            TemplateReader r = new TemplateReaderImpl(new TemplateLexerImpl());
            applicationContextStrutsTemplate = r.read(in);
            try {
                in.close();
            } catch (IOException e) {
                applicationContextStrutsTemplate = null;
                e.printStackTrace();
            }
        }
        return applicationContextStrutsTemplate;
    }

    private static TemplateNode getStrutsConfigTemplate() {
        if (strutsConfigTemplate == null) {
            InputStream in = Creator2.class.getResourceAsStream("/com/butnet/myframe/creator/template/xml/struts-config.xml.tpl");
            TemplateReader r = new TemplateReaderImpl(new TemplateLexerImpl());
            strutsConfigTemplate = r.read(in);
            try {
                in.close();
            } catch (IOException e) {
                strutsConfigTemplate = null;
                e.printStackTrace();
            }
        }
        return strutsConfigTemplate;
    }

    private static TemplateNode getDatabasePropertiesTemplate() {
        if (databasePropertiesTemplate == null) {
            InputStream in = Creator2.class.getResourceAsStream("/com/butnet/myframe/creator/template/conf/database.properties.tpl");
            TemplateReader r = new TemplateReaderImpl(new TemplateLexerImpl());
            databasePropertiesTemplate = r.read(in);
            try {
                in.close();
            } catch (IOException e) {
                databasePropertiesTemplate = null;
                e.printStackTrace();
            }
        }
        return databasePropertiesTemplate;
    }

    private static TemplateNode getWebXmlTemplate() {
        if (webXmlTemplate == null) {
            InputStream in = Creator2.class.getResourceAsStream("/com/butnet/myframe/creator/template/xml/web.xml.tpl");
            TemplateReader r = new TemplateReaderImpl(new TemplateLexerImpl());
            webXmlTemplate = r.read(in);
            try {
                in.close();
            } catch (IOException e) {
                webXmlTemplate = null;
                e.printStackTrace();
            }
        }
        return webXmlTemplate;
    }

    private static TemplateNode getApplicationContextHibernateTemplate() {
        if (applicationContextHibernateTemplate == null) {
            InputStream in = Creator2.class.getResourceAsStream("/com/butnet/myframe/creator/template/xml/applicationContext-hibernate.xml.tpl");
            TemplateReader r = new TemplateReaderImpl(new TemplateLexerImpl());
            applicationContextHibernateTemplate = r.read(in);
            try {
                in.close();
            } catch (IOException e) {
                applicationContextHibernateTemplate = null;
                e.printStackTrace();
            }
        }
        return applicationContextHibernateTemplate;
    }

    public static Template getListJspTemplate() {
        if (listJspTemplate == null) {
            InputStream in = Creator2.class.getResourceAsStream("/com/butnet/myframe/creator/template/jsp/list.jsp.tpl");
            TemplateReader r = new TemplateReaderImpl(new TemplateLexerImpl());
            listJspTemplate = r.read(in);
            try {
                in.close();
            } catch (IOException e) {
                listJspTemplate = null;
                e.printStackTrace();
            }
        }
        return listJspTemplate;
    }

    public static Template getCreateJspTemplate() {
        if (createJspTemplate == null) {
            InputStream in = Creator2.class.getResourceAsStream("/com/butnet/myframe/creator/template/jsp/create.jsp.tpl");
            TemplateReader r = new TemplateReaderImpl(new TemplateLexerImpl());
            createJspTemplate = r.read(in);
            try {
                in.close();
            } catch (IOException e) {
                createJspTemplate = null;
                e.printStackTrace();
            }
        }
        return createJspTemplate;
    }

    public static Template getDaoFactoryTemplate() {
        if (daoFactoryTemplate == null) {
            InputStream in = Creator2.class.getResourceAsStream("/com/butnet/myframe/creator/template/DaoFactory.tpl");
            TemplateReader r = new TemplateReaderImpl(new TemplateLexerImpl());
            daoFactoryTemplate = r.read(in);
            try {
                in.close();
            } catch (IOException e) {
                daoFactoryTemplate = null;
                e.printStackTrace();
            }
        }
        return daoFactoryTemplate;
    }

    public static Template getDaoTemplate() {
        if (daoTemplate == null) {
            InputStream in = Creator2.class.getResourceAsStream("/com/butnet/myframe/creator/template/Dao.tpl");
            TemplateReader r = new TemplateReaderImpl(new TemplateLexerImpl());
            daoTemplate = r.read(in);
            try {
                in.close();
            } catch (IOException e) {
                daoTemplate = null;
                e.printStackTrace();
            }
        }
        return daoTemplate;
    }

    public static Template getIdaoTemplate() {
        if (idaoTemplate == null) {
            InputStream in = Creator2.class.getResourceAsStream("/com/butnet/myframe/creator/template/IDao.tpl");
            TemplateReader r = new TemplateReaderImpl(new TemplateLexerImpl());
            idaoTemplate = r.read(in);
            try {
                in.close();
            } catch (IOException e) {
                idaoTemplate = null;
                e.printStackTrace();
            }
        }
        return idaoTemplate;
    }

    public static ClassInfo getClassInfo(Class<?> cls) {
        ClassInfo ci = new ClassInfo();
        ci.setPackage(cls.getPackage().getName());
        ci.setName(cls.getSimpleName());
        Method[] ms = cls.getMethods();
        for (Method m : ms) {
            if (m.getName().startsWith("get") && !m.getName().equals("getClass")) {
                FieldInfo fi = new FieldInfo(m.getReturnType().getName(), m.getName().substring(3));
                ci.getFields().add(fi);
            }
        }
        return ci;
    }

    public static Template getFormTemplate() {
        if (formTemplate == null) {
            InputStream in = Creator2.class.getResourceAsStream("/com/butnet/myframe/creator/template/Form.tpl");
            TemplateReader r = new TemplateReaderImpl(new TemplateLexerImpl());
            formTemplate = r.read(in);
            try {
                in.close();
            } catch (IOException e) {
                formTemplate = null;
                e.printStackTrace();
            }
        }
        return formTemplate;
    }

    public static Template getActionTemplate() {
        if (actionTemplate == null) {
            InputStream in = Creator2.class.getResourceAsStream("/com/butnet/myframe/creator/template/Action.tpl");
            TemplateReader r = new TemplateReaderImpl(new TemplateLexerImpl());
            actionTemplate = r.read(in);
            try {
                in.close();
            } catch (IOException e) {
                actionTemplate = null;
                e.printStackTrace();
            }
        }
        return actionTemplate;
    }

    /**
	 * 生成项目框架的所有类
	 * dao接口,dao实现,service接口,service实现,form,action,daoFactory,serviceFactory
	 * 
	 * @param project
	 * @param srcDir
	 * @param config 
	 * @throws IOException
	 */
    public static void create(String project, File srcDir, List<Class<?>> poClasses, Map<String, Object> config) throws IOException {
        projectName = project;
        projectSrcDir = srcDir;
        main(poClasses, config);
    }

    public static Object readConfigXml(File file) {
        System.out.println("Read Config File: " + file);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        InputStream in = null;
        Document dom = null;
        try {
            db = dbf.newDocumentBuilder();
            in = new FileInputStream(file);
            dom = db.parse(in);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        } finally {
            try {
                if (in != null) in.close();
            } catch (Exception e) {
            }
        }
        if (dom == null) return null;
        Element root = dom.getDocumentElement();
        return getChildValues(root);
    }

    @SuppressWarnings("unchecked")
    private static Object getChildValues(Node parent) {
        TextValueMap<String, Object> values = new TextValueMap<String, Object>();
        NamedNodeMap atts = parent.getAttributes();
        for (int i = 0; atts != null && i < atts.getLength(); i++) {
            Node node = atts.item(i);
            values.put(node.getNodeName(), node.getNodeValue());
        }
        NodeList childs = parent.getChildNodes();
        List<String> childNames = new LinkedList<String>();
        List<Object> childValues = new LinkedList<Object>();
        for (int i = 0; childs != null && i < childs.getLength(); i++) {
            Node n = childs.item(i);
            if (n.getNodeType() == Node.TEXT_NODE) continue;
            if (n.getNodeType() == Node.COMMENT_NODE) continue;
            childNames.add(n.getNodeName());
            childValues.add(getChildValues(n));
        }
        if (childNames.size() == 0) {
            String text = parent.getTextContent();
            values.setTextValue(text);
            return values;
        }
        boolean isSame = true;
        for (int i = 1; i < childNames.size(); i++) {
            if (!childNames.get(i - 1).equals(childNames.get(i))) {
                isSame = false;
                break;
            }
            if (!childValues.get(i - 1).getClass().equals(childValues.get(i).getClass())) {
                isSame = false;
                break;
            }
        }
        if (isSame) {
            if (values.size() == 0) return childValues;
            if (childNames.size() > 0) values.put(childNames.get(0), childValues);
            return values;
        } else {
            for (int i = 0; i < childNames.size(); i++) {
                String nodeName = childNames.get(i);
                if (values.containsKey(nodeName)) {
                    Object obj = values.get(nodeName);
                    if (obj instanceof List) ((List<Object>) obj).add(childValues.get(i)); else {
                        List<Object> tmp = new LinkedList<Object>();
                        tmp.add(obj);
                        tmp.add(childValues.get(i));
                        values.put(nodeName, tmp);
                    }
                } else {
                    values.put(nodeName, childValues.get(i));
                }
            }
        }
        return values;
    }

    @SuppressWarnings("unchecked")
    public static void main(String args[]) {
        Object tmp = null;
        if (args == null || args.length == 0) tmp = readConfigXml(new File("creator.xml")); else tmp = readConfigXml(new File(args[0]));
        System.out.println(tmp);
        if (tmp == null) {
            System.out.println("读取配置文件失败");
            return;
        }
        if (!(tmp instanceof Map<?, ?>)) {
            System.out.println("配置文件失败错误");
            return;
        }
        Map<String, Object> config = (Map<String, Object>) tmp;
        tmp = config.get("projectPackageName");
        if (tmp == null) {
            System.out.println("缺少配置属性: projectPackageName");
            return;
        }
        String projectPackageName = tmp.toString();
        tmp = config.get("srcDir");
        if (tmp == null) {
            System.out.println("缺少配置属性: srcDir");
            return;
        }
        String srcDir = tmp.toString();
        tmp = config.get("models");
        if (tmp == null) {
            System.out.println("缺少配置属性: models");
            return;
        }
        if (!(tmp instanceof List<?>)) {
            System.out.println("属性: models 不是有效数据");
            return;
        }
        List<Object> models = (List<Object>) tmp;
        List<Class<?>> pc = new LinkedList<Class<?>>();
        for (int i = 0; i < models.size(); i++) {
            try {
                Class<?> cls = Class.forName(models.get(i).toString());
                pc.add(cls);
            } catch (Exception e) {
                System.out.println("类: " + models.get(i) + " 不存在");
            }
        }
        File src = new File(srcDir);
        if (!src.exists()) {
            System.out.println("源文件目录: " + srcDir + " 不存在");
            return;
        }
        try {
            create(projectPackageName, src, pc, config);
        } catch (Exception e) {
            System.out.println("创建失败");
            e.printStackTrace(System.err);
        }
    }

    private static File getFile(File p, String n) {
        String[] ns = n.split("[\\.\\\\/]");
        File file = p;
        for (int i = 0; i < ns.length; i++) {
            ns[i] = ns[i].trim();
            if (ns[i].length() == 0) continue;
            file = new File(file, ns[i]);
        }
        return file;
    }

    private static void main(List<Class<?>> poClasses, Map<String, Object> config) throws IOException {
        {
            projectSrcDir.mkdirs();
            formPackage = projectName + ".web.form";
            formDir = getFile(projectSrcDir, projectName + "\\web\\form");
            formDir.mkdirs();
            actionPackage = projectName + ".web.action";
            actionDir = getFile(projectSrcDir, projectName + "\\web\\action");
            actionDir.mkdirs();
            serviceFactoryClassName = projectName + ".service.ServiceFactory";
            daoPackage = projectName + ".dao.impl";
            daoFactoryClassName = projectName + ".dao.DaoFactory";
            daoDir = getFile(projectSrcDir, projectName + "\\dao\\impl");
            daoDir.mkdirs();
            idaoPackage = projectName + ".dao";
            idaoDir = getFile(projectSrcDir, projectName + "\\dao");
            idaoDir.mkdirs();
            iservicePackage = projectName + ".service";
            iserviceDir = getFile(projectSrcDir, projectName + "\\service");
            iserviceDir.mkdirs();
            servicePackage = projectName + ".service.impl";
            serviceDir = getFile(projectSrcDir, projectName + "\\service\\impl");
            serviceDir.mkdirs();
        }
        List<Class<?>> poClassList = poClasses;
        ClassInfo serviceFactory = new ClassInfo();
        ClassInfo daoFactory = new ClassInfo();
        List<String> daoClasses = new LinkedList<String>();
        List<String> serviceClasses = new LinkedList<String>();
        List<String> models = new LinkedList<String>();
        List<String> actionNames = new LinkedList<String>();
        List<String> actionClasses = new LinkedList<String>();
        List<String> formNames = new LinkedList<String>();
        List<String> formClasses = new LinkedList<String>();
        for (int i = 0; i < poClassList.size(); i++) {
            Class<?> modelClass = poClassList.get(i);
            models.add(modelClass.getName().replaceAll("\\.", "/"));
            ClassInfo ci = getClassInfo(modelClass);
            ci.setPackage(formPackage);
            PrintWriter out = new PrintWriter(new File(formDir, ci.getName() + "Form.java"));
            getFormTemplate().putData("class", ci);
            getFormTemplate().translate(out);
            out.close();
            File modelWebDir = new File(projectSrcDir.getParent(), "WebContent/jsp/" + ci.getFname() + 's');
            if (!modelWebDir.exists()) modelWebDir.mkdirs();
            out = new PrintWriter(new File(modelWebDir, "list.jsp"));
            getListJspTemplate().putData("class", ci);
            getListJspTemplate().putData("modelClass", modelClass.getName());
            getListJspTemplate().putData("forwardJspUrl", config.get("forwardJspUrl"));
            getListJspTemplate().translate(out);
            out.close();
            out = new PrintWriter(new File(modelWebDir, "create.jsp"));
            getCreateJspTemplate().putData("class", ci);
            getCreateJspTemplate().putData("modelClass", modelClass.getName());
            getCreateJspTemplate().putData("forwardJspUrl", config.get("forwardJspUrl"));
            getCreateJspTemplate().putData("actionPath", "action");
            getCreateJspTemplate().translate(out);
            out.close();
            formNames.add((ci.getName().charAt(0) + "").toLowerCase() + ci.getName().substring(1));
            formClasses.add(formPackage + "." + ci.getName() + "Form");
            out = new PrintWriter(new File(actionDir, ci.getName() + "Action.java"));
            ci.getImports().clear();
            ci.getAttributes().clear();
            ci.getImports().add(serviceFactoryClassName);
            ci.getImports().add(ci.getPackage() + "." + ci.getName() + "Form");
            ci.getImports().add(poClassList.get(i).getName());
            ci.setPackage(actionPackage);
            ci.getAttributes().put("model", ci.getName());
            ci.getAttributes().put("modelName", (ci.getName().charAt(0) + "").toLowerCase() + ci.getName().substring(1));
            getActionTemplate().putData("class", ci);
            getActionTemplate().translate(out);
            out.close();
            actionNames.add((ci.getName().charAt(0) + "").toLowerCase() + ci.getName().substring(1));
            actionClasses.add(ci.getPackage() + "." + ci.getName() + "Action");
            ci.setPackage(daoPackage);
            ci.getImports().clear();
            ci.getAttributes().clear();
            ci.getImports().add(poClassList.get(i).getName());
            ci.getImports().add(idaoPackage + ".I" + ci.getName() + "DAO");
            out = new PrintWriter(new File(daoDir, ci.getName() + "DAO.java"));
            getDaoTemplate().putData("class", ci);
            getDaoTemplate().translate(out);
            out.close();
            ci.setPackage(idaoPackage);
            ci.getImports().clear();
            ci.getAttributes().clear();
            ci.getImports().add(poClassList.get(i).getName());
            out = new PrintWriter(new File(idaoDir, "I" + ci.getName() + "DAO.java"));
            getIdaoTemplate().putData("class", ci);
            getIdaoTemplate().translate(out);
            out.close();
            daoFactory.getImports().add(ci.getPackage() + ".I" + ci.getName() + "DAO");
            daoFactory.getFields().add(new FieldInfo("I" + ci.getName() + "DAO", ci.getName() + "DAO"));
            daoClasses.add(ci.getPackage() + ".impl." + ci.getName() + "DAO");
            ci.setPackage(iservicePackage);
            ci.getImports().clear();
            ci.getAttributes().clear();
            ci.getImports().add(modelClass.getName());
            out = new PrintWriter(new File(iserviceDir, "I" + ci.getName() + "Service.java"));
            getIserviceTemplate().putData("class", ci);
            getIserviceTemplate().translate(out);
            out.close();
            serviceFactory.getImports().add(ci.getPackage() + ".I" + ci.getName() + "Service");
            serviceFactory.getFields().add(new FieldInfo("I" + ci.getName() + "Service", ci.getName() + "Service"));
            serviceClasses.add(ci.getPackage() + ".impl." + ci.getName() + "Service");
            ci.setPackage(servicePackage);
            ci.getImports().clear();
            ci.getAttributes().clear();
            ci.getFields().clear();
            ci.getImports().add(modelClass.getName());
            ci.getImports().add(idaoPackage + ".I" + ci.getName() + "DAO");
            ci.getImports().add(daoFactoryClassName);
            ci.getImports().add(iservicePackage + ".I" + ci.getName() + "Service");
            ci.getFields().add(new FieldInfo("DaoFactory", "daoFactory"));
            out = new PrintWriter(new File(serviceDir, ci.getName() + "Service.java"));
            getServiceTemplate().putData("class", ci);
            getServiceTemplate().translate(out);
            out.close();
        }
        daoFactory.setPackage(idaoPackage);
        PrintWriter out = new PrintWriter(new File(idaoDir, "DaoFactory.java"));
        getDaoFactoryTemplate().putData("class", daoFactory);
        getDaoFactoryTemplate().translate(out);
        out.close();
        out = new PrintWriter(new File(projectSrcDir, "applicationContext-dao.xml"));
        daoFactory.getAttributes().put("daoPackage", daoPackage);
        daoFactory.getAttributes().put("daos", daoClasses);
        getApplicationContextDaoTemplate().putData("class", daoFactory);
        getApplicationContextDaoTemplate().translate(out);
        out.close();
        out = new PrintWriter(new File(projectSrcDir, "applicationContext-hibernate.xml"));
        daoFactory.getAttributes().put("models", models);
        getApplicationContextHibernateTemplate().putData("class", daoFactory);
        getApplicationContextHibernateTemplate().translate(out);
        out.close();
        serviceFactory.setPackage(iservicePackage);
        out = new PrintWriter(new File(projectSrcDir, "applicationContext-struts.xml"));
        serviceFactory.getAttributes().put("services", serviceClasses);
        serviceFactory.getAttributes().put("actions", actionNames);
        serviceFactory.getAttributes().put("actionClasses", actionClasses);
        getApplicationContextStrutsTemplate().putData("class", serviceFactory);
        getApplicationContextStrutsTemplate().translate(out);
        out.close();
        serviceFactory.setPackage(iservicePackage);
        out = new PrintWriter(new File(projectSrcDir.getParent(), "WebContent/WEB-INF/struts-config.xml"));
        serviceFactory.getAttributes().put("forms", formNames);
        serviceFactory.getAttributes().put("formClasses", formClasses);
        getStrutsConfigTemplate().putData("class", serviceFactory);
        getStrutsConfigTemplate().translate(out);
        out.close();
        serviceFactory.setPackage(iservicePackage);
        out = new PrintWriter(new File(iserviceDir, "ServiceFactory.java"));
        getServiceFactoryTemplate().putData("class", serviceFactory);
        getServiceFactoryTemplate().translate(out);
        out.close();
        out = new PrintWriter(new File(projectSrcDir, "database.properties"));
        getDatabasePropertiesTemplate().putData("config", config);
        getDatabasePropertiesTemplate().translate(out);
        out.close();
        out = new PrintWriter(new File(projectSrcDir.getParent(), "WebContent/WEB-INF/web.xml"));
        getWebXmlTemplate().putData("forwardJspUrl", config.get("forwardJspUrl"));
        getWebXmlTemplate().translate(out);
        out.close();
        copyLibs();
        copyStrutsMessage();
        copyApplicationContextResouse();
    }

    private static void copyApplicationContextResouse() {
        InputStream in = null;
        FileOutputStream out = null;
        try {
            in = Creator.class.getResourceAsStream("/com/butnet/myframe/res/applicationContext-resouse.xml");
            if (in == null) return;
            out = new FileOutputStream(new File(projectSrcDir, "applicationContext-resouse.xml"));
            copy(in, out);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        } finally {
            try {
                if (in != null) in.close();
                if (out != null) out.close();
            } catch (Exception e) {
            }
        }
    }

    private static void copyStrutsMessage() {
        InputStream in = null;
        FileOutputStream out = null;
        try {
            in = Creator.class.getResourceAsStream("/com/butnet/myframe/res/MessageResources.properties");
            if (in == null) return;
            out = new FileOutputStream(new File(projectSrcDir, "MessageResources.properties"));
            copy(in, out);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        } finally {
            try {
                if (in != null) in.close();
                if (out != null) out.close();
            } catch (Exception e) {
            }
        }
    }

    private static void copy(InputStream in, OutputStream out) throws IOException {
        if (in == null) return;
        byte[] buf = new byte[1024 * 10];
        int num;
        while ((num = in.read(buf)) != -1) {
            out.write(buf, 0, num);
        }
    }

    private static void copyLibs() {
        InputStream in = null;
        BufferedReader reader = null;
        List<String> libs = new LinkedList<String>();
        try {
            in = Creator.class.getResourceAsStream("/com/butnet/myframe/creator/libs.conf");
            if (in == null) return;
            reader = new BufferedReader(new InputStreamReader(in));
            String line = null;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("#") || line.startsWith("!") || line.startsWith("//")) continue;
                libs.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace(System.err);
        } finally {
            try {
                if (reader != null) reader.close();
                if (in != null) in.close();
            } catch (Exception e) {
            }
        }
        File libRoot = new File(new File(projectSrcDir.getParent(), "WebContent"), "WEB-INF/lib");
        libRoot.mkdirs();
        for (String lib : libs) {
            copyLib(lib, libRoot);
        }
    }

    private static void copyLib(String lib, File libDir) {
        InputStream in = null;
        FileOutputStream out = null;
        File file = new File(libDir, lib);
        try {
            in = Creator.class.getResourceAsStream("/com/butnet/myframe/res/lib/" + lib);
            if (in == null) return;
            out = new FileOutputStream(file);
            byte[] buf = new byte[1024 * 3];
            int num;
            while ((num = in.read(buf)) != -1) {
                out.write(buf, 0, num);
            }
        } catch (Exception e) {
            e.printStackTrace(System.err);
        } finally {
            try {
                if (in != null) in.close();
                if (out != null) out.close();
            } catch (Exception e) {
            }
        }
    }
}
