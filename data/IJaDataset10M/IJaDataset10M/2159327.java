package irrigator.device;

import java.io.Serializable;

public interface Component extends Serializable {

    public abstract String getManufacturer();

    public abstract String getPartNumber();

    public abstract String getDescription();

    public abstract double getPressureCoefficient();
}
