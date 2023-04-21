package progranet.ganesa.model;

import java.io.Serializable;
import progranet.ganesa.persistence.PersistentBinary;

public class Binary extends PersistentBinary implements progranet.omg.customlibrary.Binary, Serializable {

    private static final long serialVersionUID = -116027554203586344L;

    public Binary() {
    }

    public int hashCode() {
        return this.getId() == null ? -1 : this.getId().hashCode();
    }

    public boolean equals(java.lang.Object o) {
        if (!(o instanceof Binary)) return false;
        Binary b = (Binary) o;
        return b.getId() != null && b.getId().equals(this.getId()) && ((b.getContent() == null && this.getContent() == null) || (b.getContent() != null && b.getContent().equals(this.getContent())));
    }
}
