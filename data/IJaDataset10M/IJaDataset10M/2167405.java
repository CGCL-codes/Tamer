package am.model.dao;

import org.json.simple.JSONObject;

/**
 *
 * @author E.Fil
 */
public class InvoiceableType extends BaseDao {

    Integer idInvoiceableType;

    String Name;

    String Description;

    public String getDescription() {
        return Description;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public Integer getIdInvoiceableType() {
        return idInvoiceableType;
    }

    public void setIdInvoiceableType(Integer idInvoiceableType) {
        this.idInvoiceableType = idInvoiceableType;
    }

    @Override
    public JSONObject toJSONObject() {
        jsonObj.put("idInvoiceableType", getIdInvoiceableType());
        jsonObj.put("Name", getName());
        jsonObj.put("Description", getDescription());
        return jsonObj;
    }
}
