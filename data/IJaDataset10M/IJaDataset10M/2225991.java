package cn.edu.wuse.musicxml.symbol;

import java.util.EventObject;

public class KeySymbolEvent extends EventObject {

    private static final long serialVersionUID = 1L;

    private int part;

    private int staff;

    private KeySymbol symbol;

    public int getPart() {
        return part;
    }

    public void setPart(int part) {
        this.part = part;
    }

    public int getStaff() {
        return staff;
    }

    public void setStaff(int staff) {
        this.staff = staff;
    }

    public KeySymbol getSymbol() {
        return symbol;
    }

    public KeySymbolEvent(Object source, int part, int staff, KeySymbol symbol) {
        super(source);
        this.part = part;
        this.staff = staff;
        this.symbol = symbol;
    }

    public void setSymbol(KeySymbol symbol) {
        this.symbol = symbol;
    }

    public KeySymbolEvent(Object source) {
        super(source);
    }
}
