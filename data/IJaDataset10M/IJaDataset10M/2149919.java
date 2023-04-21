package tafat.metamodel.entity;

import java.text.ParseException;
import tafat.engine.conversion.UnitConversor;

public class MillMock extends Mill {

    public enum MockType {

        CONSUMER(0), PRODUCER(1), PROSUMER(2);

        private double value;

        MockType(double val) {
            value = val;
        }

        public double getValue() {
            return value;
        }
    }

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

    public double installedPower;

    public MockType mockType;

    public double nominalVoltage;

    public double reactance;

    public double resistance;

    public VoltageLevel voltageLevel;

    public double activePower;

    public int customerCount;

    public int personCount;

    public int producerCount;

    public double reactivePower;

    public MillMock() {
    }

    public void setDefaultValues() throws ParseException {
        super.setDefaultValues();
        installedPower = 100;
        mockType = MockType.CONSUMER;
        nominalVoltage = 230;
        reactance = 0.0;
        resistance = 0.0;
        voltageLevel = VoltageLevel.LOW;
        activePower = 0.0;
        customerCount = 0;
        personCount = 0;
        producerCount = 0;
        reactivePower = 0.0;
    }

    public void loadAttribute(String name, String value) throws ParseException {
        if (name.equals("installedPower")) {
            installedPower = UnitConversor.parse(value, "VA");
        } else if (name.equals("mockType")) {
            mockType = MockType.valueOf(value.toUpperCase());
        } else if (name.equals("nominalVoltage")) {
            nominalVoltage = UnitConversor.parse(value, "V");
        } else if (name.equals("reactance")) {
            reactance = UnitConversor.parse(value, "ohms");
        } else if (name.equals("resistance")) {
            resistance = UnitConversor.parse(value, "ohms");
        } else if (name.equals("voltageLevel")) {
            voltageLevel = VoltageLevel.valueOf(value.toUpperCase());
        } else if (name.equals("activePower")) {
            activePower = UnitConversor.parse(value, "W");
        } else if (name.equals("customerCount")) {
            customerCount = Integer.parseInt(value);
        } else if (name.equals("personCount")) {
            personCount = Integer.parseInt(value);
        } else if (name.equals("producerCount")) {
            producerCount = Integer.parseInt(value);
        } else if (name.equals("reactivePower")) {
            reactivePower = UnitConversor.parse(value, "VAr");
        } else {
            super.loadAttribute(name, value);
        }
    }

    public String toString() {
        String result = "";
        result += "<feature name=\"installedPower\" value=\"" + installedPower + "\"/>\n";
        result += "<feature name=\"mockType\" value=\"" + mockType.toString() + "\"/>\n";
        result += "<feature name=\"nominalVoltage\" value=\"" + nominalVoltage + "\"/>\n";
        result += "<feature name=\"reactance\" value=\"" + reactance + "\"/>\n";
        result += "<feature name=\"resistance\" value=\"" + resistance + "\"/>\n";
        result += "<feature name=\"voltageLevel\" value=\"" + voltageLevel.toString() + "\"/>\n";
        result += "<feature name=\"activePower\" value=\"" + activePower + "\"/>\n";
        result += "<feature name=\"customerCount\" value=\"" + customerCount + "\"/>\n";
        result += "<feature name=\"personCount\" value=\"" + personCount + "\"/>\n";
        result += "<feature name=\"producerCount\" value=\"" + producerCount + "\"/>\n";
        result += "<feature name=\"reactivePower\" value=\"" + reactivePower + "\"/>\n";
        return result;
    }
}
