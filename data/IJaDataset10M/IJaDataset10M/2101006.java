package com.bugfree4j.per.common.config;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.digester.Digester;

/**
 * Digester����XMLʾ�����
 * <p>
 * 
 * @author walker(walker@skyinn.org)
 *         <p>
 *         <strong>�ο����ף�</strong>
 *         <p>
 *         <a href="http://www.onjava.com/pub/a/onjava/2002/10/23/digester.html"
 *         target="_blank">Learning and Using Jakarta Digester</a>
 *         <p>
 *         <a
 *         href="http://developer.ccidnet.com/pub/disp/Article?columnID=340&articleID=33259&pageNO=1"
 *         target="_blank">��Digester��XML�����ļ�����</a>
 */
public class HelloDigester {

    private static List currentActions = new ArrayList();

    protected String registrations[] = { "-//Apache Software Foundation//DTD Struts Configuration 1.0//EN", "/org/apache/struts/resources/struts-config_1_0.dtd", "-//Apache Software Foundation//DTD Struts Configuration 1.1//EN", "/org/apache/struts/resources/struts-config_1_1.dtd", "-//Sun Microsystems, Inc.//DTD Web Application 2.2//EN", "/org/apache/struts/resources/web-app_2_2.dtd", "-//Sun Microsystems, Inc.//DTD Web Application 2.3//EN", "/org/apache/struts/resources/web-app_2_3.dtd" };

    private String configFile;

    public void setConfigFile(final String configFile) {
        this.configFile = configFile;
    }

    /**
	 * ���Actionӳ�䡣 ��������������˴����ActionMapping����Ϣ���ھ���Ӧ���пɽ������ϵͳActionӳ�伯���С�
	 * 
	 * @param actionMapping
	 *            Actionӳ��
	 */
    public void addActionMapping(final ActionMapping actionMapping) {
        String action = new String(actionMapping.getPath());
        currentActions.add(action);
    }

    public List getModuleAction(String strutsConfigDir) {
        Digester digester = new Digester();
        digester.setValidating(true);
        URL url = this.getClass().getResource(registrations[3]);
        digester.register(registrations[2], url.toString());
        digester.addObjectCreate("struts-config/action-mappings", HelloDigester.class);
        digester.addObjectCreate("struts-config/action-mappings/action", ActionMapping.class);
        digester.addSetProperties("struts-config/action-mappings/action", "attribute", "attribute");
        digester.addSetProperties("struts-config/action-mappings/action", "input", "input");
        digester.addSetProperties("struts-config/action-mappings/action", "name", "name");
        digester.addSetProperties("struts-config/action-mappings/action", "path", "path");
        digester.addSetProperties("struts-config/action-mappings/action", "scope", "scope");
        digester.addSetProperties("struts-config/action-mappings/action", "type", "type");
        digester.addSetNext("struts-config/action-mappings/action", "addActionMapping");
        try {
            List filelist = null;
            filelist = DirFileList.getModuleConfigList(strutsConfigDir);
            for (int i = 0; i < filelist.size(); i++) {
                ModuleConfig mc = (ModuleConfig) filelist.get(i);
                currentActions = new ArrayList();
                digester.parse(mc.getFilename());
                mc.setActions(currentActions.toArray());
            }
            for (int i = 0; i < filelist.size(); i++) {
                ModuleConfig mc = (ModuleConfig) filelist.get(i);
                if (mc.getName().equals("config")) {
                    mc.setName("ROOT");
                }
                Object[] actions = mc.getActions();
                for (int j = 0; j < actions.length; j++) {
                }
            }
            System.out.println("----�ܹ���" + filelist.size() + " ��ģ��");
            return filelist;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List getModuleActionMapping(String strutsConfigDir) {
        HelloDigester hd = new HelloDigester();
        return hd.getModuleAction(strutsConfigDir);
    }

    public static void main(String[] args) {
        getModuleActionMapping("");
    }
}
