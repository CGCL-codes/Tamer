package tafat.metamodel.entity;

import java.text.ParseException;
import tafat.engine.conversion.UnitConversor;

public class RefrigeratorSimple extends Refrigerator {

    public enum VoltageLevel {

        LOW(0), MEDIUM(1), HIGH(2);

        private double value;

        VoltageLevel(double val) {
            value = val;
        }

        public double getValue() {
            return value;
        }
    }

    public double nominalVoltage;

    public double ratedPower;

    public double reactance;

    public double resistance;

    public VoltageLevel voltageLevel;

    public double activePower;

    public double reactivePower;

    public RefrigeratorSimple() {
    }

    public void setDefaultValues() throws ParseException {
        super.setDefaultValues();
        nominalVoltage = 230;
        ratedPower = 100;
        reactance = 0.0;
        resistance = 0.0;
        voltageLevel = VoltageLevel.LOW;
        activePower = 0.0;
        reactivePower = 0.0;
    }

    public void loadAttribute(String name, String value) throws ParseException {
        if (name.equals("nominalVoltage")) {
            nominalVoltage = UnitConversor.parse(value, "V");
        } else if (name.equals("ratedPower")) {
            ratedPower = UnitConversor.parse(value, "VA");
        } else if (name.equals("reactance")) {
            reactance = UnitConversor.parse(value, "ohms");
        } else if (name.equals("resistance")) {
            resistance = UnitConversor.parse(value, "ohms");
        } else if (name.equals("voltageLevel")) {
            voltageLevel = VoltageLevel.valueOf(value.toUpperCase());
        } else if (name.equals("activePower")) {
            activePower = UnitConversor.parse(value, "W");
        } else if (name.equals("reactivePower")) {
            reactivePower = UnitConversor.parse(value, "VAr");
        } else {
            super.loadAttribute(name, value);
        }
    }

    public String toString() {
        String result = "";
        result += "<feature name=\"nominalVoltage\" value=\"" + nominalVoltage + "\"/>\n";
        result += "<feature name=\"ratedPower\" value=\"" + ratedPower + "\"/>\n";
        result += "<feature name=\"reactance\" value=\"" + reactance + "\"/>\n";
        result += "<feature name=\"resistance\" value=\"" + resistance + "\"/>\n";
        result += "<feature name=\"voltageLevel\" value=\"" + voltageLevel.toString() + "\"/>\n";
        result += "<feature name=\"activePower\" value=\"" + activePower + "\"/>\n";
        result += "<feature name=\"reactivePower\" value=\"" + reactivePower + "\"/>\n";
        return result;
    }
}
