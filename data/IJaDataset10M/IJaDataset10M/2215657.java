package dataImport.model;

public class ObjectDescription extends Description {

    private static final long serialVersionUID = 1535346971649023816L;

    private Object objectRelatedTo;

    public ObjectDescription(final String id, final State state, final Text text, final Object objectRelatedTo) {
        super(id, state, text);
        this.objectRelatedTo = objectRelatedTo;
    }

    public Object getObjectRelatedTo() {
        return this.objectRelatedTo;
    }

    public void setObjectRelatedTo(Object objectRelatedTo) {
        this.objectRelatedTo = objectRelatedTo;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((this.objectRelatedTo == null) ? 0 : this.objectRelatedTo.hashCode());
        return result;
    }

    @Override
    public boolean equals(java.lang.Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        ObjectDescription other = (ObjectDescription) obj;
        if (this.objectRelatedTo == null) {
            if (other.objectRelatedTo != null) {
                return false;
            }
        } else if (!this.objectRelatedTo.equals(other.objectRelatedTo)) {
            return false;
        }
        return true;
    }
}
