package cn.vlabs.duckling.dct.services.dml.html2dml;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.jdom.Element;
import org.jdom.JDOMException;
import cn.vlabs.duckling.dct.services.dmldata.dao.DmlDataFormProvider;
import cn.vlabs.duckling.dct.services.dmldata.dao.DmlDataProvider;
import cn.vlabs.duckling.dct.services.dmldata.entity.DmlDataForm;

/**
 * Introduction Here.
 * @date 2010-3-8
 * @author 狄
 */
public class ParseHtmlForm extends AbstractParseHtmlElement {

    private String strFormTable = "";

    public void printAttribute(Element e, Html2DmlEngine html2dmlengine) {
        Map map = new ForgetNullValuesLinkedHashMap();
        map.put("method", e.getAttributeValue("method"));
        map.put("dmldesc", e.getAttributeValue("dmldesc"));
        map.put("sqlsrc1", e.getAttributeValue("sqlsrc1"));
        map.put("sqlsrc2", e.getAttributeValue("sqlsrc2"));
        map.put("sqlsrc3", e.getAttributeValue("sqlsrc3"));
        map.put("changepage", e.getAttributeValue("changepage"));
        DmlDataFormProvider dmldataformmanager = html2dmlengine.getVwbcontext().getSite().getDmlDataFormProvider();
        map.put("id", map.get("dmldesc"));
        boolean bformid = dmldataformmanager.searchFormById((String) map.get("dmldesc"));
        if (!bformid) {
            DmlDataForm dmldataform = new DmlDataForm();
            dmldataform.setFormId((String) map.get("id"));
            dmldataform.setDmldesc((String) map.get("dmldesc"));
            if (html2dmlengine.getVwbcontext().isUseDData()) dmldataformmanager.insertFormInfo(dmldataform);
        }
        html2dmlengine.setFormid((String) map.get("id"));
        boolean bFormTable = dmldataformmanager.isNotExistTable((String) map.get("id"));
        html2dmlengine.setBFormTable(bFormTable);
        if (map.size() > 0) {
            for (Iterator ito = map.entrySet().iterator(); ito.hasNext(); ) {
                Map.Entry entry = (Map.Entry) ito.next();
                if (!entry.getValue().equals("")) {
                    html2dmlengine.getM_out().print(" " + entry.getKey() + "=\"" + entry.getValue() + "\"");
                }
            }
        }
    }

    @Override
    public void printElement(Element e, Html2DmlEngine html2dmlengine) {
        List newlist = new ArrayList();
        html2dmlengine.setListCreateFormTable(newlist);
        html2dmlengine.getM_out().print("<form");
        printAttribute(e, html2dmlengine);
        if (html2dmlengine.getPreType() > 0) {
            html2dmlengine.getM_out().print(">");
        } else {
            html2dmlengine.getM_out().println(">");
        }
        try {
            h2d.getChildren(e, html2dmlengine);
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (JDOMException e1) {
            e1.printStackTrace();
        }
        if (html2dmlengine.getPreType() > 0) {
            html2dmlengine.getM_out().print("</form>");
        } else {
            html2dmlengine.getM_out().println("</form>");
        }
        if (html2dmlengine.getVwbcontext().isUseDData()) createOrAlteTable(html2dmlengine);
    }

    /**
	 * Brief Intro Here
	 * @param
	 */
    private void createOrAlteTable(Html2DmlEngine html2dmlengine) {
        DmlDataProvider dmldatamanager = html2dmlengine.getVwbcontext().getSite().getDmlDataProvider();
        StringBuffer strcreateformtable = new StringBuffer("");
        String strsplit = html2dmlengine.getStrCreateFormTable();
        if (!html2dmlengine.isBFormTable()) {
            strcreateformtable.append("CREATE TABLE `" + html2dmlengine.getFormid() + "`(");
            strcreateformtable.append("`id` int(11) NOT NULL auto_increment,");
            List iList = html2dmlengine.getListCreateFormTable();
            for (int i = 0; i < iList.size(); i++) {
                String type = iList.get(i).getClass().getSimpleName();
                if ("String".equals(type)) {
                    strcreateformtable.append("`" + iList.get(i) + "` varchar(255) default NULL,");
                } else {
                    strcreateformtable.append("`" + ((List) iList.get(i)).get(0) + "`  float default NULL,");
                }
                System.out.println("iList.get(i)=" + iList.get(i) + "|" + iList.get(i).getClass().getSimpleName());
            }
            strcreateformtable.append(" PRIMARY KEY  (`id`))ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=REDUNDANT;");
            System.out.println((strcreateformtable.toString()));
            dmldatamanager.createDmlDataTable(strcreateformtable.toString());
        } else {
            List iList = html2dmlengine.getListCreateFormTable();
            for (int i = 0; i < iList.size(); i++) {
                String type = iList.get(i).getClass().getSimpleName();
                if ("String".equals(type)) {
                    String straltertable = "ALTER TABLE `" + html2dmlengine.getFormid() + "` ADD column `" + iList.get(i) + "` varchar(255) default NULL;";
                    dmldatamanager.createDmlDataTable(straltertable);
                } else {
                    String straltertable = "ALTER TABLE `" + html2dmlengine.getFormid() + "` ADD column `" + ((List) iList.get(i)).get(0) + "`  float default NULL;";
                    dmldatamanager.createDmlDataTable(straltertable);
                }
                System.out.println("iList.get(i)=" + iList.get(i) + "|" + iList.get(i).getClass().getSimpleName());
            }
        }
        html2dmlengine.setFormid(null);
    }
}
