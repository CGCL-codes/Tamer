package net.sf.gap.mc.qagesa.agents.fuzzy;

/**
 *
 * @author Giovanni Novelli
 */
public class QualityLossFactory {

    public static enum Type {

        DELAY_RAW
    }

    ;

    public static AbstractQualityLoss create(Type aType) {
        AbstractQualityLoss ql;
        switch(aType) {
            case DELAY_RAW:
                ql = new DelayQualityLoss();
                break;
            default:
                ql = new DelayQualityLoss();
                break;
        }
        return ql;
    }
}
