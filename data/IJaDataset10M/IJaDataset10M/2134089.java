package l1j.server.server.templates;

import java.util.Calendar;

public class L1Castle {

    public L1Castle(int id, String name) {
        _id = id;
        _name = name;
    }

    private int _id;

    public int getId() {
        return _id;
    }

    private String _name;

    public String getName() {
        return _name;
    }

    private Calendar _warTime;

    public Calendar getWarTime() {
        return _warTime;
    }

    public void setWarTime(Calendar i) {
        _warTime = i;
    }

    private int _taxRate;

    public int getTaxRate() {
        return _taxRate;
    }

    public void setTaxRate(int i) {
        _taxRate = i;
    }

    private int _publicMoney;

    public int getPublicMoney() {
        return _publicMoney;
    }

    public void setPublicMoney(int i) {
        _publicMoney = i;
    }
}
