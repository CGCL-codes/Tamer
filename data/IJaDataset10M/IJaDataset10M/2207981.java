package br.ufmg.lcc.pcollecta.view.binding;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.el.ValueExpression;
import javax.faces.component.UIData;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.faces.convert.DateTimeConverter;
import javax.faces.convert.NumberConverter;
import org.apache.log4j.Logger;
import org.richfaces.component.html.HtmlColumn;
import org.richfaces.component.html.HtmlColumnGroup;
import org.richfaces.component.html.HtmlDataTable;
import br.ufmg.lcc.arangi.commons.BasicException;
import br.ufmg.lcc.arangi.commons.StringHelper;
import br.ufmg.lcc.arangi.controller.bean.ControllerBean;
import br.ufmg.lcc.arangi.view.FacesBeanHelper;
import br.ufmg.lcc.pcollecta.commons.PCollectaConstantes;
import br.ufmg.lcc.pcollecta.commons.XMLSerialization;
import br.ufmg.lcc.pcollecta.dto.SerializableData;

/**
 * 
 * @author Salazar
 *
 */
public class PCollectaXMLFileDataScroller {

    private static Logger log = Logger.getLogger(PCollectaXMLFileDataScroller.class);

    private UIData table;

    private List<String> columnList;

    private List<String> typesList;

    private List<Object[]> fieldList;

    private Integer begin;

    private Integer quantity;

    private Long totalQuantity;

    private Long harvastId;

    private Long summaryId;

    private String dataImageFolder;

    public UIData getTable() {
        return table;
    }

    public void setTable(UIData table) throws BasicException {
        this.table = table;
        display();
    }

    public List<String> getColumnList() {
        if (columnList == null) columnList = new ArrayList<String>();
        return columnList;
    }

    public void setColumnList(List<String> columnList) {
        this.columnList = columnList;
    }

    public List<Object[]> getFieldList() {
        if (fieldList == null) fieldList = new ArrayList<Object[]>();
        return fieldList;
    }

    public void setFieldList(List<Object[]> fieldList) {
        this.fieldList = fieldList;
    }

    public List<String> getTypesList() {
        if (typesList == null) typesList = new ArrayList<String>();
        return typesList;
    }

    public void setTypesList(List<String> typesList) {
        this.typesList = typesList;
    }

    public Integer getBegin() {
        return begin;
    }

    public void setBegin(Integer begin) {
        this.begin = begin;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Long getHarvestId() {
        return harvastId;
    }

    public void setHarvastId(Long harvastId) {
        this.harvastId = harvastId;
    }

    public Long getSummaryId() {
        return summaryId;
    }

    public void setSummaryId(Long summaryId) {
        this.summaryId = summaryId;
    }

    public String getDataImageFolder() {
        return dataImageFolder;
    }

    public void setDataImageFolder(String dataImageFolder) {
        this.dataImageFolder = dataImageFolder;
    }

    public Long getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(Long totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    /**
	 * Navigate in the XML file
	 * @throws BasicException 
	 */
    public void dataScroller() throws BasicException {
        dataScroller(getDataImageFolder());
    }

    /**
	 * Navigate in the XML file
	 * @throws BasicException 
	 * @throws BasicException 
	 */
    public void dataScroller(String folder) throws BasicException {
        SerializableData data = null;
        try {
            data = retrieveData(folder);
        } catch (BasicException e) {
            setColumnList(null);
            setFieldList(null);
            setTypesList(null);
            setTotalQuantity(new Long(0));
            if (table != null) {
                table.getFacets().put("header", new HtmlColumnGroup());
            } else {
                table = new HtmlDataTable();
                table.getFacets().put("header", new HtmlColumnGroup());
            }
            throw e;
        }
        setColumnList(data.getHeader());
        setFieldList(data.getData());
        setTypesList(data.getTypes());
        setTotalQuantity(data.getQuantity());
        if (table != null) {
            display();
        }
    }

    /**
	 * Navigate in the XML file
	 * @throws BasicException 
	 * @throws BasicException 
	 */
    public void emptyDataScroller() {
        setColumnList(null);
        setFieldList(null);
        setTypesList(null);
        setTotalQuantity(new Long(0));
        if (table != null) {
            table.getChildren().clear();
        } else {
            table = new HtmlDataTable();
        }
    }

    private void display() {
        configureRows();
    }

    private void configureHeaders() {
        ControllerBean cBean = FacesBeanHelper.getBeanByExpression("#{arangiControllerBean}", ControllerBean.class);
        List<String> columnList = getColumnList();
        HtmlColumnGroup htmlColumnGroup = new HtmlColumnGroup();
        HtmlColumn htmlColumn = new HtmlColumn();
        HtmlOutputText label = new HtmlOutputText();
        long count = 0;
        for (String string : columnList) {
            htmlColumn = new HtmlColumn();
            htmlColumn.setId("data_" + count);
            label = new HtmlOutputText();
            label.setValue(string);
            label.setId("label_" + count);
            htmlColumn.getChildren().add(label);
            htmlColumnGroup.getChildren().add(htmlColumn);
            count++;
        }
        table.getFacets().put("header", htmlColumnGroup);
    }

    private void configureRows() {
        ControllerBean cBean = FacesBeanHelper.getBeanByExpression("#{arangiControllerBean}", ControllerBean.class);
        List<Object[]> fieldList = getFieldList();
        HtmlColumn htmlColumn = null;
        HtmlOutputText label = null;
        HtmlOutputText header = null;
        ValueExpression exp = null;
        String expression = "";
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Locale userLocale = facesContext.getExternalContext().getRequestLocale();
        List<String> columnList = getColumnList();
        if (!fieldList.isEmpty()) {
            table.getChildren().clear();
            Object[] values = fieldList.get(0);
            for (int i = 0; i < values.length; i++) {
                htmlColumn = new HtmlColumn();
                htmlColumn.setId("data2_" + i);
                label = new HtmlOutputText();
                label.setId("label2_" + i);
                expression = "#{item[" + i + "]}";
                header = new HtmlOutputText();
                header.setValue(columnList.get(i));
                header.setId("label_" + i);
                htmlColumn.setHeader(header);
                if (typesList.get(i).equals(XMLSerialization.Lit.DATE)) {
                    DateTimeConverter convert = new DateTimeConverter();
                    convert.setLocale(userLocale);
                    convert.setDateStyle("medium");
                    convert.setTimeStyle("medium");
                    convert.setType("both");
                    label.setConverter(convert);
                    exp = FacesBeanHelper.createValueExpression(expression, Date.class);
                } else if (typesList.get(i).equals(XMLSerialization.Lit.DECIMAL)) {
                    NumberConverter convert = new NumberConverter();
                    convert.setLocale(userLocale);
                    convert.setPattern("###,###,###,###,##0.00");
                    label.setConverter(convert);
                    exp = FacesBeanHelper.createValueExpression(expression, Double.class);
                } else {
                    exp = FacesBeanHelper.createValueExpression(expression, String.class);
                }
                label.setValueExpression("value", exp);
                htmlColumn.getChildren().add(label);
                table.getChildren().add(htmlColumn);
            }
        }
    }

    /**
	 * Retrieve data from XML file base on navigate component
	 * @param cBean
	 * @return
	 * @throws BasicException
	 */
    private SerializableData retrieveData(String folder) throws BasicException {
        XMLSerialization xml = new XMLSerialization();
        SerializableData data = new SerializableData();
        data.setHarvestId(getHarvestId());
        data.setSummaryId(getSummaryId());
        if (xml.hasFile(data, folder)) {
            xml.prepareRead(data, folder);
            updateScrollerValues(data);
            xml.readRecordFromFile(getBegin(), getQuantity());
            xml.finalizeRead();
        } else {
            throw BasicException.errorHandling("Data file don't exist.", "msgErrorValidateApprovalDontExistFile", StringHelper.EMPTY_STRING_VECTOR, log);
        }
        List<String> filterColumnList = new ArrayList<String>();
        List<String> columnList = data.getHeader();
        for (String obj : columnList) {
            filterColumnList.add(obj);
        }
        data.setHeader(filterColumnList);
        return data;
    }

    /**
	 * Retrieve data from navigate component
	 * @param requestItem
	 * @param data
	 * @return
	 */
    private void updateScrollerValues(SerializableData data) {
        Integer begin = getBegin();
        begin = (begin != null) ? begin : XMLSerialization.Default.BEGIN;
        Integer quantity = getQuantity();
        quantity = (quantity != null) ? quantity : XMLSerialization.Default.QUANTITY;
        String button = FacesBeanHelper.getBeanByScope(PCollectaConstantes.DataScroller.PARAMETER, PCollectaConstantes.ApplicationScope.PARAMETER, String.class);
        button = (button != null) ? button : "";
        if (button.equals(PCollectaConstantes.DataScroller.NEXT)) {
            Long dataQuantity = data.getQuantity();
            if (quantity + begin < dataQuantity) {
                begin = quantity + begin;
            }
        } else if (button.equals(PCollectaConstantes.DataScroller.PREVIOUS)) {
            begin = begin - quantity;
            if (begin < 0) {
                begin = 0;
            }
        } else if (button.equals(PCollectaConstantes.DataScroller.FIRST)) {
            begin = 0;
        } else if (button.equals(PCollectaConstantes.DataScroller.LAST)) {
            Long dataQuantity = data.getQuantity();
            if (dataQuantity > quantity) {
                begin = dataQuantity.intValue() - quantity;
            } else {
                begin = 0;
            }
        }
        setBegin(begin);
        setQuantity(quantity);
    }
}
