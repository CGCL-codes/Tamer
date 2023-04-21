package com.global360.sketchpadbpmn.graphic.symbol;

public class FilledCancel extends Cancel {

    public FilledCancel() {
        this.name = "FilledCancel";
        this.type = Symbol.FILLED_CANCEL;
    }

    @Override
    public void makeShapes() {
        addFill(makeShape());
    }
}
