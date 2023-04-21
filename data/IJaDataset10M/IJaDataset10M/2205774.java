package samples;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class IntToStringAdapter extends XmlAdapter<String, Integer> {

    public Integer unmarshal(String value) {
        return new Integer(value);
    }

    public String marshal(Integer value) {
        return value.toString();
    }
}
