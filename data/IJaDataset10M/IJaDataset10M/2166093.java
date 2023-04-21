package model;

public class PhoneNumberEditable extends PhoneNumberWrapper {

    private String countryCode;

    private String areaCode;

    private String numberCode;

    public PhoneNumberEditable() {
        countryCode = null;
        areaCode = null;
        numberCode = null;
    }

    public PhoneNumberEditable(String a, String b, String c) {
        countryCode = a;
        areaCode = b;
        numberCode = c;
    }

    @Override
    public boolean existsAreaCode() {
        if (areaCode == null || areaCode.trim().length() == 0) return false;
        return true;
    }

    @Override
    public boolean existsCountryCode() {
        if (countryCode == null || countryCode.trim().length() == 0) return false;
        return true;
    }

    @Override
    public boolean existsNumberCode() {
        if (numberCode == null || numberCode.trim().length() == 0) return false;
        return true;
    }

    @Override
    public String getAreaCode() {
        return this.areaCode;
    }

    @Override
    public String getCountryCode() {
        return this.countryCode;
    }

    @Override
    public String getNumberCode() {
        return this.numberCode;
    }

    @Override
    public void setAreaCode(String code) {
        this.areaCode = code;
    }

    @Override
    public void setCountryCode(String code) {
        this.countryCode = code;
    }

    @Override
    public void setNumberCode(String code) {
        this.numberCode = code;
    }

    @Override
    public String print() {
        return "+" + getCountryCode() + "-" + getAreaCode() + "-" + getNumberCode();
    }
}
