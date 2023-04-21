package be.gnx.dukono.time;

import java.math.BigDecimal;

public class JD extends Time {

    public static final JD MJDEpoch = new JD();

    static {
        MJDEpoch.setTicks(BigDecimal.valueOf(-24000005, 1));
    }

    public TAI toTAI() {
        return toUTC().toTAI();
    }

    public UTC toUTC() {
        BigDecimal ticks = (getTicks().subtract(BigDecimal.ZERO)).multiply(BigDecimal.valueOf(24)).multiply(BigDecimal.valueOf(60)).multiply(BigDecimal.valueOf(60));
        UTC utc = new UTC();
        utc.setTicks(ticks);
        utc.setEpoch(getEpoch());
        return utc;
    }
}
