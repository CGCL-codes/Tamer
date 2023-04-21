package bagaturchess.engines.learning.cfg.weights.evaltune;

import java.lang.reflect.Field;
import bagaturchess.engines.learning.cfg.weights.WeightsEvaluationConfig;

public class WeightsEvaluationConfig_TUNE extends WeightsEvaluationConfig_LKG {

    public WeightsEvaluationConfig_TUNE(String[] args) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        super(args);
        for (int i = 0; i < args.length; i++) {
            String current = args[i];
            if (current == null) {
                throw new IllegalStateException("Argument is null");
            }
            if (!current.contains("=")) {
                throw new IllegalStateException("Argument does not contains '=' simbol");
            }
            String[] pair = current.split("=");
            String fieldName = pair[0];
            String stringValue = pair[1];
            Double arg_val = (Double) parseValue(stringValue);
            if (Math.abs(arg_val) < 0.05) {
                throw new IllegalStateException("arg_val=" + arg_val);
            }
            if (Math.abs(arg_val) > 1.00) {
                throw new IllegalStateException("arg_val=" + arg_val);
            }
            Double cur_val = (Double) getField(fieldName);
            double new_val = cur_val;
            if (cur_val > 0) {
                new_val += cur_val * arg_val;
            } else if (cur_val < 0) {
                new_val += cur_val * arg_val;
            } else {
                throw new IllegalStateException("arg_val=0");
            }
            setField(fieldName, new_val);
        }
    }

    private Object parseValue(String stringValue) {
        Object val = null;
        try {
            val = Integer.parseInt(stringValue);
        } catch (NumberFormatException nfe1) {
            try {
                val = Double.parseDouble(stringValue);
            } catch (NumberFormatException nfe2) {
                val = Boolean.parseBoolean(stringValue);
            }
        }
        return val;
    }

    private void setField(String fieldName, Object fieldValue) throws NoSuchFieldException, IllegalAccessException {
        Field field = this.getClass().getField(fieldName);
        field.set(this, fieldValue);
    }

    private Object getField(String fieldName) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field field = this.getClass().getField(fieldName);
        return field.get(this);
    }

    public static void main(String[] args) {
        try {
            WeightsEvaluationConfig_TUNE o = new WeightsEvaluationConfig_TUNE(new String[] { "MATERIAL_PAWN_O=15" });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
