package org.geogurus.gas.objects;

import org.geogurus.mapserver.objects.Symbol;

/**
 * A bean representing a GAS symbology: MS Symbol + size to set to the class
 * @author nicolas
 */
public class SymbologyBean {

    /** the size at which the Symbol must be rendered to represent this symbology */
    private int size;

    private Symbol symbol;

    /** the optional overlaySymbol for this symbology */
    private Symbol overlaySymbol;

    /** the size at which the OverlaySymbol must be rendered to represent this symbology */
    private int overlaySize;

    /** the symbol icon */
    private String icon;

    private String id;

    public SymbologyBean() {
    }

    public SymbologyBean(String id, int size, int overlaySize, Symbol symbol, Symbol overlaySymbol, String icon) {
        this.size = size;
        this.overlaySize = overlaySize;
        this.symbol = symbol;
        this.overlaySymbol = overlaySymbol;
        this.id = id;
        this.icon = icon;
    }

    public SymbologyBean(String id) {
        this.id = id;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Symbol getSymbol() {
        return symbol;
    }

    public void setSymbol(Symbol symbol) {
        this.symbol = symbol;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Symbol getOverlaySymbol() {
        return overlaySymbol;
    }

    public void setOverlaySymbol(Symbol overlaySymbol) {
        this.overlaySymbol = overlaySymbol;
    }

    public int getOverlaySize() {
        return overlaySize;
    }

    public void setOverlaySize(int overlaySize) {
        this.overlaySize = overlaySize;
    }
}
