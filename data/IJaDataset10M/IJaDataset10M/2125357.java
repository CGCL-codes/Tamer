package tafat.metamodel.entity;

import java.text.ParseException;
import tafat.engine.conversion.UnitConversor;
import java.util.Date;
import tafat.engine.DateParser;

public class IndustrialEstateFull extends IndustrialEstate {

    public String address;

    public double areaFloor;

    public Date constructionDate = new Date();

    public Date renovationDate = new Date();

    public IndustrialEstateFull() {
    }

    public void setDefaultValues() throws ParseException {
        super.setDefaultValues();
        address = "";
        areaFloor = 100;
        constructionDate = DateParser.parseDate("01/01/1970");
        renovationDate = DateParser.parseDate("01/01/1970");
    }

    public void loadAttribute(String name, String value) throws ParseException {
        if (name.equals("address")) {
            address = value;
        } else if (name.equals("areaFloor")) {
            areaFloor = UnitConversor.parse(value, "m^2");
        } else if (name.equals("constructionDate")) {
            constructionDate = DateParser.parseDate(value);
        } else if (name.equals("renovationDate")) {
            renovationDate = DateParser.parseDate(value);
        } else {
            super.loadAttribute(name, value);
        }
    }

    public String toString() {
        String result = "";
        result += "<feature name=\"address\" value=\"" + address + "\"/>\n";
        result += "<feature name=\"areaFloor\" value=\"" + areaFloor + "\"/>\n";
        result += "<feature name=\"constructionDate\" value=\"" + constructionDate.toString() + "\"/>\n";
        result += "<feature name=\"renovationDate\" value=\"" + renovationDate.toString() + "\"/>\n";
        return result;
    }
}
