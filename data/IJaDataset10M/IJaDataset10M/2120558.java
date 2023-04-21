package geo.streetaddress.us;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.builder.ToStringBuilder;

class USAddress {

    public enum StreetComponentEnum {

        STREET_PREFIX, STREET_NUM, STREET_NAME, STREET_TYPE, STREET_SUFFIX, STREET_UNIT
    }

    private Map<StreetComponentEnum, String> _streetComponentMap = new HashMap<StreetComponentEnum, String>();

    private Map<StreetComponentEnum, String> _intersectionStreetComponentMap;

    private String _city;

    private String _state;

    private String _zip;

    public String getCity() {
        return _city;
    }

    public void setCity(String city) {
        _city = city;
    }

    public String getState() {
        return _state;
    }

    public void setState(String state) {
        _state = state;
    }

    public String getZip() {
        return _zip;
    }

    public void setZip(String zip) {
        _zip = zip;
    }

    public boolean isIntersection() {
        return _intersectionStreetComponentMap == null;
    }

    public Map<StreetComponentEnum, String> getStreetComponentMap() {
        return _streetComponentMap;
    }

    public void setStreetComponentMap(Map<StreetComponentEnum, String> streetComponentMap) {
        _streetComponentMap = streetComponentMap;
    }

    public Map<StreetComponentEnum, String> getIntersectionStreetComponentMap() {
        return _intersectionStreetComponentMap;
    }

    public void setIntersectionStreetComponentMap(Map<StreetComponentEnum, String> intersectionStreetComponentMap) {
        _intersectionStreetComponentMap = intersectionStreetComponentMap;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
