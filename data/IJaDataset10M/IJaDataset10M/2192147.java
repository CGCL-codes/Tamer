package bee.entities;

import bee.core.*;
import java.util.List;

/**
 * This class contains all effects which can be applied among entities.
 * NOTE: the initial effect parameters are only the defaults. They can be fine-tuned on entity basis.
 * @author boto
 */
public class Effects {

    private static Effects instance = new Effects();

    public static final String EFFECT_NAME_LAME = "Lame";

    public static final String EFFECT_NAME_DAMAGE = "Damage";

    public static final String EFFECT_NAME_HEALTH = "Health";

    public static final String EFFECT_NAME_JUICE = "Juice";

    public static final String EFFECT_NAME_STEAL_JUICE = "StealJuice";

    public static final String EFFECT_NAME_HIVE = "Hive";

    /**
     * Given an effect name create an effect object.
     */
    public static EntityEffect create(String name) {
        if (name.equals(EFFECT_NAME_LAME)) return instance.new Lame(); else if (name.equals(EFFECT_NAME_DAMAGE)) return instance.new Damage(); else if (name.equals(EFFECT_NAME_HEALTH)) return instance.new Health(); else if (name.equals(EFFECT_NAME_JUICE)) return instance.new Juice(); else if (name.equals(EFFECT_NAME_STEAL_JUICE)) return instance.new StealJuice(); else if (name.equals(EFFECT_NAME_HIVE)) return instance.new Hive();
        Log.error(instance.getClass().getSimpleName() + ": effect with name " + name + " not supported");
        return null;
    }

    /**
     * Lame effect.
     */
    public class Lame extends EntityEffect {

        public Float duration = 5.0f;

        public Float speedfactor = 0.5f;

        public Lame() {
            super(EFFECT_NAME_LAME);
            List<Parameter> params = getParameters();
            params.add(new Parameter("duration", duration));
            params.add(new Parameter("speedfactor", speedfactor));
        }
    }

    /**
     * Damage effect.
     */
    public class Damage extends EntityEffect {

        public Float amount = 10.0f;

        public Damage() {
            super(EFFECT_NAME_DAMAGE);
            List<Parameter> params = getParameters();
            params.add(new Parameter("amount", amount));
        }
    }

    /**
     * Health effect.
     */
    public class Health extends EntityEffect {

        public Float amount = 5.0f;

        public Health() {
            super(EFFECT_NAME_HEALTH);
            List<Parameter> params = getParameters();
            params.add(new Parameter("amount", amount));
        }
    }

    /**
     * Gather juice effect.
     */
    public class Juice extends EntityEffect {

        public Float amount = 5.0f;

        public Juice() {
            super(EFFECT_NAME_JUICE);
            List<Parameter> params = getParameters();
            params.add(new Parameter("amount", amount));
        }
    }

    /**
     * Gather juice effect.
     */
    public class StealJuice extends EntityEffect {

        public Float amount = 1.0f;

        public StealJuice() {
            super(EFFECT_NAME_STEAL_JUICE);
            List<Parameter> params = getParameters();
            params.add(new Parameter("amount", amount));
        }
    }

    /**
     * Hive effect allowing to bring back collected honey.
     */
    public class Hive extends EntityEffect {

        public Float amount = 5.0f;

        public Hive() {
            super(EFFECT_NAME_HIVE);
            List<Parameter> params = getParameters();
            params.add(new Parameter("amount", amount));
        }
    }
}
