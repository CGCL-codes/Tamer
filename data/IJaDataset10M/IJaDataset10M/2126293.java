package ingenias.editor.entities;

import java.util.*;

public class OrganizationModelDataEntity extends ModelDataEntity {

    public java.lang.String Description;

    public OrganizationModelDataEntity(String id) {
        super(id);
    }

    public java.lang.String getDescription() {
        return Description;
    }

    public void setDescription(java.lang.String Description) {
        this.Description = Description;
    }

    public void fromMap(Map ht) {
        super.fromMap(ht);
        if (ht.get("Description") instanceof String) this.setDescription(ht.get("Description").toString());
    }

    public void toMap(Map ht) {
        super.toMap(ht);
        if (this.getDescription() instanceof String) ht.put("Description", this.getDescription().toString());
    }
}
