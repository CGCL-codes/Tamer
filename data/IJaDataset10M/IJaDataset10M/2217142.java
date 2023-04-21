package com.iv.flash.api.button;

import com.iv.flash.api.*;
import com.iv.flash.util.FlashOutput;
import com.iv.flash.util.ScriptCopier;
import java.awt.geom.AffineTransform;
import java.io.PrintStream;

public class ButtonRecord extends FlashItem {

    public static final int HitTest = 0x08;

    public static final int Down = 0x04;

    public static final int Over = 0x02;

    public static final int Up = 0x01;

    private int states;

    private FlashDef def;

    private int layer;

    private AffineTransform matrix;

    private CXForm cxform;

    public ButtonRecord() {
    }

    public ButtonRecord(int states, FlashDef def, int layer, AffineTransform matrix, CXForm cxform) {
        setStates(states);
        setDef(def);
        setLayer(layer);
        setMatrix(matrix);
        setCXForm(cxform);
    }

    public void setStates(int states) {
        this.states = states;
    }

    public void setDef(FlashDef def) {
        this.def = def;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }

    public void setMatrix(AffineTransform matrix) {
        this.matrix = matrix;
    }

    public void setCXForm(CXForm cxform) {
        this.cxform = cxform;
    }

    public int getStates() {
        return states;
    }

    public FlashDef getDef() {
        return def;
    }

    public int getLayer() {
        return layer;
    }

    public AffineTransform getMatrix() {
        return matrix;
    }

    public CXForm getCXForm() {
        return cxform;
    }

    public void write(FlashOutput fob) {
        fob.writeByte(states);
        fob.writeDefID(def);
        fob.writeWord(layer);
        fob.write(matrix);
        cxform.write(fob);
    }

    public void printContent(PrintStream out, String indent) {
        out.println(indent + "   ButtonRecord: charID=" + def.getID() + " states=" + states + " layer=" + layer);
    }

    public FlashItem getCopy(ScriptCopier copier) {
        ButtonRecord br = new ButtonRecord();
        br.states = states;
        br.def = copier.copy(def);
        br.layer = layer;
        br.matrix = (AffineTransform) matrix.clone();
        br.cxform = (CXForm) cxform.getCopy(copier);
        return br;
    }
}
