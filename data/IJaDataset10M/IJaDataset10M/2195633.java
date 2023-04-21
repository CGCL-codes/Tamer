package com.golemgame.mvc.golems;

import java.util.Collection;
import com.golemgame.mvc.DataType;
import com.golemgame.mvc.DoubleType;
import com.golemgame.mvc.EnumType;
import com.golemgame.mvc.PropertyStore;
import com.golemgame.mvc.golems.BatteryInterpreter.ThresholdType;
import com.golemgame.mvc.golems.functions.FunctionSettingsInterpreter;
import com.golemgame.mvc.golems.functions.PolynomialFunctionInterpreter;

public class ModifierInterpreter extends StandardFunctionalInterpreter {

    public static enum ModifierSwitchType {

        On("Turn Off/On"), Invert("Invert Signal"), Pause("Hold Value"), Multiply("Multiply Value");

        private String description;

        private ModifierSwitchType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    public static final String FUNCTION_SETTINGS = "function.settings";

    public static final String MODIFIER_TYPE = "switchType";

    public static final String THRESHOLD_TYPE = "threshold.type";

    public static final String THRESHOLD = "threshold";

    @Override
    public Collection<String> enumerateKeys(Collection<String> keys) {
        keys.add(THRESHOLD_TYPE);
        keys.add(THRESHOLD);
        keys.add(MODIFIER_TYPE);
        keys.add(FUNCTION_SETTINGS);
        return super.enumerateKeys(keys);
    }

    private static final EnumType defaultSwitch = new EnumType(ModifierSwitchType.On);

    private static final EnumType defaultThresholdType = new EnumType(ThresholdType.GREATER_EQUAL);

    @Override
    public DataType getDefaultValue(String key) {
        if (key.equals(FUNCTION_SETTINGS)) return defaultStore;
        if (key.equals(MODIFIER_TYPE)) return defaultSwitch;
        if (key.equals(THRESHOLD)) return defaultFloat;
        if (key.equals(THRESHOLD_TYPE)) return defaultThresholdType;
        return super.getDefaultValue(key);
    }

    public ModifierInterpreter() {
        this(new PropertyStore());
    }

    public ModifierInterpreter(PropertyStore store) {
        super(store);
        store.setClassName(GolemsClassRepository.MODIFIER_CLASS);
    }

    public PropertyStore getFunctionStore() {
        if (!getStore().hasProperty(FUNCTION_SETTINGS)) {
            FunctionSettingsInterpreter settings = new FunctionSettingsInterpreter(getStore().getPropertyStore(FUNCTION_SETTINGS));
            PolynomialFunctionInterpreter interp = new PolynomialFunctionInterpreter(settings.getFunction());
            interp.setCoefficient(0, new DoubleType(1));
            interp.setCoefficient(1, new DoubleType(0));
        }
        return getStore().getPropertyStore(FUNCTION_SETTINGS);
    }

    public void setFunctionStore(PropertyStore functionStore) {
        getStore().setProperty(FUNCTION_SETTINGS, functionStore);
    }

    public void setThresholdType(ThresholdType threshold) {
        getStore().setProperty(THRESHOLD_TYPE, threshold);
    }

    public ThresholdType getThresholdType() {
        return getStore().getEnum(THRESHOLD_TYPE, ThresholdType.GREATER_EQUAL);
    }

    public ModifierSwitchType getSwitchType() {
        return getStore().getEnum(MODIFIER_TYPE, ModifierSwitchType.On);
    }

    public void setSwitchType(ModifierSwitchType key) {
        getStore().setProperty(MODIFIER_TYPE, key);
    }

    public float getThreshold() {
        return getStore().getFloat(THRESHOLD, 0f);
    }

    public void setThreshold(float key) {
        getStore().setProperty(THRESHOLD, key);
    }
}
