package net.zylk.kerozain.portlet.record.model;

public class SecretPhone implements SecretBase {

    public String number = null;

    public String pin = null;

    public String puk = null;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getPuk() {
        return puk;
    }

    public void setPuk(String puk) {
        this.puk = puk;
    }
}
