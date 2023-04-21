package com.kisgergely.gwt.canvas.test;

import com.kisgergely.gwt.canvas.client.Canvas;
import com.kisgergely.gwt.canvas.client.CanvasDrawingStyle;
import com.kisgergely.gwt.canvas.client.CanvasRenderingContext2D;
import com.kisgergely.gwt.tester.client.ClientTestCase;
import com.kisgergely.gwt.tester.client.ClientTestCaseContext;

public class CanvasRadialGradientTest extends ClientTestCase {

    public CanvasRadialGradientTest() {
        super("Canvas Radial Gradient Test");
    }

    public void doTest(ClientTestCaseContext testCtx) {
        Canvas c = new Canvas(350, 350);
        log("Canvas created");
        assertNotNull(c, "Canvas");
        CanvasRenderingContext2D ctx = c.getContext2D();
        log("Context acquired");
        assertNotNull(ctx, "Ctx2d");
        if (ctx != null) {
            log("Setting output widget");
            assertNotNull(testCtx.getOutputPanel(), "GetOutputPanel");
            testCtx.getOutputPanel().add(c);
            testCtx.getOutputPanel().setSize("" + c.getOffsetWidth(), "" + c.getOffsetHeight());
            log("Output panel set");
            CanvasDrawingStyle radgrad = ctx.createRadialGradient(45, 45, 10, 52, 50, 30);
            radgrad.addColorStop(0, "#A7D30C");
            radgrad.addColorStop((float) 0.9, "#019F62");
            radgrad.addColorStop(1, "rgba(1,159,98,0)");
            CanvasDrawingStyle radgrad2 = ctx.createRadialGradient(105, 105, 20, 112, 120, 50);
            radgrad2.addColorStop(0, "#FF5F98");
            radgrad2.addColorStop((float) 0.75, "#FF0188");
            radgrad2.addColorStop(1, "rgba(255,1,136,0)");
            CanvasDrawingStyle radgrad3 = ctx.createRadialGradient(95, 15, 15, 102, 20, 40);
            radgrad3.addColorStop(0, "#00C9FF");
            radgrad3.addColorStop((float) 0.8, "#00B5E2");
            radgrad3.addColorStop(1, "rgba(0,201,255,0)");
            CanvasDrawingStyle radgrad4 = ctx.createRadialGradient(0, 150, 50, 0, 140, 90);
            radgrad4.addColorStop(0, "#F4F201");
            radgrad4.addColorStop((float) 0.8, "#E4C700");
            radgrad4.addColorStop(1, "rgba(228,199,0,0)");
            log("Created radial gradients");
            ctx.setFillStyle(radgrad4);
            ctx.fillRect(0, 0, 150, 150);
            ctx.setFillStyle(radgrad3);
            ctx.fillRect(0, 0, 150, 150);
            ctx.setFillStyle(radgrad2);
            ctx.fillRect(0, 0, 150, 150);
            ctx.setFillStyle(radgrad);
            ctx.fillRect(0, 0, 150, 150);
            log("draw shapes");
        }
    }
}
