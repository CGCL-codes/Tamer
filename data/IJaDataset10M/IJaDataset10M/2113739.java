package org.easybi.data.xmlimpl;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.easybi.data.DataConstants;
import org.easybi.data.datafilter.PredicateControler;
import org.easybi.data.defaultimpl.DefaultTabularDataImpl;
import org.easybi.data.structure.TabularData;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

/**
 * ����ཫ����ṹ��xmlת��Ϊtabulardata��ʵ���ϣ����涼��DefaultImpl
 * <table>
 *      <th>
 *          <td type="string">name</td>
 *          <td type="number">age</td>
 *          <td type="number">salary</td>
 *      </th>
 *      <td>
 *          <td>Steve</td>
 *          <td>30</td>
 *          <td>100</td>
 *      </td>
 *      <td>
 *          <td>fanzi</td>
 *          <td>28</td>
 *          <td>200</td>
 *      </td>
 *      <td>
 *          <td>����</td>
 *          <td>44</td>
 *          <td>88</td>
 *      </td>
 *  </table>
 *  ת�����Ϊ��
 *  Map key=age
 *      Map key=Steve-->30��
 *          key=fanzi-->28,
 *          ...
 *  Map key=salary
 *      Map key=Steve-->100, 
 *      	key=fanzi-->100,
 *      	...
 * @author steve
 *
 */
public class XmlTabularDataImpl implements TabularData {

    Logger logger = Logger.getLogger(XmlTabularDataImpl.class);

    final String loggerCN = "XmlTabularDataImpl, ";

    final String TH = "th";

    final String TABLE = "table";

    final String TR = "tr";

    final String TD = "td";

    final String TYPE = "type";

    Element root = null;

    String fileName = "";

    String sourceString = "";

    /**
     * ��һ��ȫ·���ļ��еõ�
     * @param fileFullPath
     */
    public XmlTabularDataImpl(String fileFullPath) {
        this.logger.debug(this.loggerCN + "xml file path: " + fileFullPath);
        this.fileName = fileFullPath;
        this.reloadData();
    }

    /**
     * ��д��xml���ַ��еõ�
     * @param xmlString
     */
    public XmlTabularDataImpl(String xmlString, boolean isString) {
        this.sourceString = xmlString;
        this.reloadData();
    }

    /**
     * �������������������ΪDefaultTabularData
     */
    public Object getValue() {
        return this.createTabularData(null);
    }

    /**
     * �����ط����������Ŀ
     * @param ���validateIdsΪnull����ȫ������
     * @return
     */
    private TabularData createTabularData(List validateIds) {
        DefaultTabularDataImpl defaultTabular = new DefaultTabularDataImpl();
        try {
            Object[] obj = this.root.getChild(this.TH).getChildren().toArray();
            if (validateIds == null) {
                for (int i = 1; i < obj.length; i++) {
                    String row = ((Element) obj[i]).getText();
                    String type = ((Element) obj[i]).getAttributeValue(this.TYPE);
                    for (Iterator itr = this.root.getChildren(this.TR).iterator(); itr.hasNext(); ) {
                        Element ele = (Element) itr.next();
                        String col = ((Element) ele.getChildren().toArray()[0]).getText();
                        String val = ((Element) ele.getChildren().toArray()[i]).getText();
                        defaultTabular.setData(row, col, createDefaultTabularData(val, type));
                    }
                }
            } else {
                for (int i = 1; i < obj.length; i++) {
                    String row = ((Element) obj[i]).getText();
                    String type = ((Element) obj[i]).getAttributeValue(this.TYPE);
                    for (Iterator itr = this.root.getChildren(this.TR).iterator(); itr.hasNext(); ) {
                        Element ele = (Element) itr.next();
                        String col = ((Element) ele.getChildren().toArray()[0]).getText();
                        if (validateIds.contains(col)) {
                            String val = ((Element) ele.getChildren().toArray()[i]).getText();
                            defaultTabular.setData(row, col, createDefaultTabularData(val, type));
                        }
                    }
                }
            }
        } catch (RuntimeException e) {
            this.logger.error(this.loggerCN + "getValue(), Exception.");
            this.logger.error(e.getMessage());
        }
        return defaultTabular;
    }

    private DefaultTabularDataImpl createDefaultTabularData(String val, String type) {
        DefaultTabularDataImpl theValue = null;
        if (DataConstants.TABULARTYPE_CALENDAR.equalsIgnoreCase(type)) {
            theValue = new DefaultTabularDataImpl(val, "yyyy-MM-dd");
        } else if (DataConstants.TABULARTYPE_NUMBER.equalsIgnoreCase(type)) {
            theValue = new DefaultTabularDataImpl(Double.parseDouble(val));
        } else {
            theValue = new DefaultTabularDataImpl(val);
        }
        return theValue;
    }

    /**
     * getData("age","steve")
     */
    public TabularData getData(String row, String col) {
        try {
            Object[] obj = this.root.getChild(this.TH).getChildren().toArray();
            int i = 0;
            String type = DataConstants.TABULARTYPE_STRING;
            for (i = 0; i < obj.length; i++) {
                if (row.equals(((Element) obj[i]).getText())) {
                    type = ((Element) obj[i]).getAttributeValue(this.TYPE);
                    break;
                }
            }
            if (i < obj.length) {
                for (Iterator itr = this.root.getChildren(this.TR).iterator(); itr.hasNext(); ) {
                    Element trElement = (Element) itr.next();
                    Object[] tdObjArray = trElement.getChildren().toArray();
                    if (col.equals(((Element) tdObjArray[0]).getText())) {
                        return createDefaultTabularData(((Element) tdObjArray[i]).getText(), type);
                    }
                }
            }
        } catch (RuntimeException e) {
            this.logger.error(this.loggerCN + "getData(), Exception.");
            this.logger.error(e.getMessage());
        }
        return null;
    }

    public int getSize() {
        try {
            return this.root.getChildren().size();
        } catch (RuntimeException e) {
            this.logger.error(this.loggerCN + "getSize(), Exception");
        }
        return 0;
    }

    public int getColSize(String row) {
        try {
            return this.root.getChild(this.TH).getChildren().size();
        } catch (RuntimeException e) {
            this.logger.error(this.loggerCN + "getColSize(), Exception");
        }
        return 0;
    }

    /**
     * getRow("age")
     */
    public HashMap getRow(String row) {
        HashMap resultMap = null;
        try {
            Object[] obj = this.root.getChild(this.TH).getChildren().toArray();
            int i = 0;
            String type = DataConstants.TABULARTYPE_STRING;
            for (i = 0; i < obj.length; i++) {
                if (row.equals(((Element) obj[i]).getText())) {
                    type = ((Element) obj[i]).getAttributeValue(this.TYPE);
                    break;
                }
            }
            if (i < obj.length) {
                resultMap = new HashMap();
                for (Iterator itr = this.root.getChildren(this.TR).iterator(); itr.hasNext(); ) {
                    Element trElement = (Element) itr.next();
                    Object[] tdObjArray = trElement.getChildren().toArray();
                    String key = ((Element) tdObjArray[0]).getText();
                    resultMap.put(key, createDefaultTabularData(((Element) tdObjArray[i]).getText(), type));
                }
            }
        } catch (RuntimeException e) {
            this.logger.error(this.loggerCN + "getData(), Exception.");
            this.logger.error(e.getMessage());
        }
        return resultMap;
    }

    /**
     * ��Զ����Tabular
     */
    public String getType() {
        return DataConstants.TABULARTYPE_TABULAR;
    }

    public List getList(List filterList) {
        List resultList = new ArrayList();
        Object[] headerEle = this.root.getChild(this.TH).getChildren().toArray();
        for (Iterator itr = this.root.getChildren(this.TR).iterator(); itr.hasNext(); ) {
            Element ele = (Element) itr.next();
            Object[] tmp = ele.getChildren().toArray();
            HashMap resultMap = new HashMap();
            for (int i = 0; i < tmp.length; i++) {
                if (i < headerEle.length) {
                    String value = ((Element) tmp[i]).getText();
                    String key = ((Element) headerEle[i]).getText();
                    String valueType = ((Element) headerEle[i]).getAttributeValue(this.TYPE);
                    resultMap.put(key, this.createDefaultTabularData(value, valueType).getValue());
                }
            }
            resultList.add(resultMap);
        }
        if (filterList != null) {
            PredicateControler controller = new PredicateControler(filterList);
            CollectionUtils.filter(resultList, controller.getPredicate());
        }
        return resultList;
    }

    public TabularData doFilter(List filterList) {
        List filteredList = this.getList(filterList);
        String ID = ((Element) this.root.getChild(this.TH).getChildren().toArray()[0]).getText();
        ArrayList validateId = new ArrayList();
        for (Iterator itr = filteredList.iterator(); itr.hasNext(); ) {
            validateId.add(((HashMap) itr.next()).get(ID));
        }
        this.logger.debug(this.loggerCN + validateId.toString());
        return this.createTabularData(validateId);
    }

    /**
     * �����ж��Ƿ���Ҫreload�ļ���Ȼ���ٴ���reloadString�����
     * �ͻ���Ӧ�ø��������ÿ���Ӧ��������Ŀ
     */
    public void reloadData() {
        SAXBuilder builder = new SAXBuilder();
        Document document = null;
        try {
            if (!"".equals(this.fileName)) {
                File file = new File(this.fileName);
                this.logger.debug(this.loggerCN + "xml fullpath: " + file.getAbsolutePath());
                document = builder.build(file);
            } else if (!"".equals(this.sourceString)) {
                document = builder.build(new StringReader(this.sourceString));
            }
        } catch (JDOMException e) {
            this.logger.error(this.loggerCN + "reloadData() error, JDOMException: " + e.getMessage());
        } catch (IOException e) {
            this.logger.error(this.loggerCN + "reloadData() error, IOException: " + e.getMessage());
        }
        if (document != null) {
            this.root = document.getRootElement();
        } else {
            this.logger.error(this.loggerCN + "reloadData() error, both fileName & sourceString null.");
        }
    }

    /**
     * ����xmlTabularData��˵����Զ������Ƕ�׵�
     */
    public boolean isEmbeded() {
        return false;
    }

    /**
     * ��д������ǰ�ģ�Ҳд������
     */
    public void writeMyself() {
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("-------------------" + this.loggerCN + "XML -----------------");
            if (this.root != null) {
                this.root.detach();
                Document doc = new Document(this.root);
                XMLOutputter outputter = new XMLOutputter();
                this.logger.debug("------raw xml:");
                this.logger.debug(outputter.outputString(doc));
                this.logger.debug("-------TabularData result:");
                DefaultTabularDataImpl defaultT = (DefaultTabularDataImpl) this.getValue();
                defaultT.writeMyself();
            } else {
                this.logger.debug("Root Element is null");
            }
            this.logger.debug("-----------------------------Done----------------------------");
        }
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getSourceString() {
        return sourceString;
    }

    public void setSourceString(String sourceString) {
        this.sourceString = sourceString;
    }
}
