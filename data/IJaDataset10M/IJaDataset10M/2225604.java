package com.google.code.donkirkby;

import java.awt.Graphics2D;

public class StrokeImageFacade {

    private PaintController controller;

    private Graphics2D paintContext;

    private PaintWriter paintWriter;

    public StrokeImageFacade() {
        paintWriter = new PaintWriter();
        paintContext = paintWriter.createGraphicsContext();
        controller = new PaintController();
    }

    public void generateImage(String strokeOrderData, String filenameRoot) {
        controller.setStrokeOrderData(strokeOrderData);
        controller.paint(paintContext);
        paintWriter.setWidth(controller.getWidth());
        paintWriter.setHeight(controller.getHeight());
        paintWriter.write(filenameRoot + ".svg", filenameRoot + ".png");
    }
}
