package blueprint4j.utils;

import java.math.*;

public interface BindBigDecimalInterface extends BindFieldInterface {

    public BigDecimal get();

    public void set(BigDecimal value);

    public int getUnits();

    public int getFraction();
}
