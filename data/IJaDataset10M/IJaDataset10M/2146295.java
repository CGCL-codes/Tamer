package lt.ktu.scheduler.base;

import java.io.Serializable;

public abstract class AbstractBase implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -5219726585304972261L;

    public abstract int getId();

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + getId();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        AbstractBase other = (AbstractBase) obj;
        if (getId() != other.getId()) return false;
        return true;
    }
}
