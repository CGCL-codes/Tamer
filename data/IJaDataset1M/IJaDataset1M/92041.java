package tafat.metamodel.connection;

import java.text.ParseException;
import tafat.engine.conversion.UnitConversor;

public class CellularCDMAFull extends CellularCDMA {

    public double speedsRange;

    public CellularCDMAFull() {
    }

    public void setDefaultValues() throws ParseException {
        super.setDefaultValues();
        speedsRange = 0.0;
    }

    public void loadAttribute(String name, String value) throws ParseException {
        if (name.equals("speedsRange")) {
            speedsRange = UnitConversor.parse(value, "kbps");
        } else {
            super.loadAttribute(name, value);
        }
    }

    public String toString() {
        String result = "";
        result += "<feature name=\"speedsRange\" value=\"" + speedsRange + "\"/>\n";
        return result;
    }
}
