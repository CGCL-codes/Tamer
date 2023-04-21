package onepoint.project.modules.custom_attribute;

import java.util.Date;
import java.util.Map;
import java.util.Set;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import onepoint.persistence.OpSiteObject;
import onepoint.project.modules.project.OpAttachment;
import onepoint.project.modules.report.Report;
import onepoint.project.modules.report.ReportMethod;

/**
 * Interface to access custom attributes.
 * All OpObjects that can store custom attributes implement this interface.
 *
 * @author dfreis
 */
@Report
@XmlType(name = "customizableObject")
@XmlAccessorType(XmlAccessType.NONE)
public class OpCustomizableObject extends OpSiteObject implements OpCustomizable {

    private OpCustomValuePage customValuePage = null;

    private Set<OpCustomValuePage> customValuePages = null;

    private Map<String, OpCustomAttribute> customTypeMap;

    public Object getObject(String name) throws IllegalArgumentException {
        return OpCustomizableAdapter.getObject(this, name);
    }

    public void setObject(String name, Object value) throws IllegalArgumentException {
        OpCustomizableAdapter.setObject(this, name, value);
    }

    @ReportMethod(inMemory = true)
    public Boolean getBoolean(String name) throws IllegalArgumentException {
        return OpCustomizableAdapter.getBoolean(this, name);
    }

    public void setBoolean(String name, Boolean value) throws IllegalArgumentException {
        OpCustomizableAdapter.setBoolean(this, name, value);
    }

    @ReportMethod(inMemory = true)
    public Long getNumber(String name) throws IllegalArgumentException {
        return OpCustomizableAdapter.getNumber(this, name);
    }

    public void setNumber(String name, Long value) throws IllegalArgumentException {
        OpCustomizableAdapter.setNumber(this, name, value);
    }

    @ReportMethod(inMemory = true)
    public Double getDecimal(String name) throws IllegalArgumentException {
        return OpCustomizableAdapter.getDecimal(this, name);
    }

    public void setDecimal(String name, Double value) throws IllegalArgumentException {
        OpCustomizableAdapter.setDecimal(this, name, value);
    }

    @ReportMethod(inMemory = true)
    public Date getDate(String name) throws IllegalArgumentException {
        return OpCustomizableAdapter.getDate(this, name);
    }

    public void setDate(String name, Date value) throws IllegalArgumentException {
        OpCustomizableAdapter.setDate(this, name, value);
    }

    @ReportMethod(inMemory = true)
    public String getText(String name) throws IllegalArgumentException {
        return OpCustomizableAdapter.getText(this, name);
    }

    @ReportMethod(inMemory = true)
    public OpCustomTextValue getMemo(String name) throws IllegalArgumentException {
        return OpCustomizableAdapter.getMemo(this, name);
    }

    @ReportMethod(inMemory = true)
    public OpAttachment getAttachment(String name) throws IllegalArgumentException {
        return OpCustomizableAdapter.getAttachment(this, name);
    }

    @ReportMethod(inMemory = true)
    public String getChoice(String name) throws IllegalArgumentException {
        return OpCustomizableAdapter.getChoice(this, name);
    }

    public void setText(String name, String value) throws IllegalArgumentException {
        OpCustomizableAdapter.setText(this, name, value);
    }

    public void setMemo(String name, OpCustomTextValue value) throws IllegalArgumentException {
        OpCustomizableAdapter.setMemo(this, name, value);
    }

    public void setAttachment(String name, OpAttachment value) throws IllegalArgumentException {
        OpCustomizableAdapter.setAttachment(this, name, value);
    }

    public void setChoice(String name, String value) throws IllegalArgumentException {
        OpCustomizableAdapter.setChoice(this, name, value);
    }

    public void addCustomValuePage(OpCustomValuePage cvp) {
        OpCustomizableAdapter.addCustomValuePage(this, cvp);
    }

    public void removeCustomValuePage(OpCustomValuePage cvp) {
        OpCustomizableAdapter.removeCustomValuePage(this, cvp);
    }

    public Set<OpCustomValuePage> getCustomValuePages() {
        return customValuePages;
    }

    public void setCustomValuePages(Set<OpCustomValuePage> pages) {
        customValuePages = pages;
    }

    public OpCustomValuePage getCustomValuePage() {
        return customValuePage;
    }

    public void setCustomValuePage(OpCustomValuePage customValuePage) {
        this.customValuePage = customValuePage;
    }

    public Map<String, OpCustomAttribute> getCustomTypeMap() {
        return customTypeMap;
    }

    public void setCustomTypeMap(Map<String, OpCustomAttribute> customTypeMap) {
        this.customTypeMap = customTypeMap;
    }
}
