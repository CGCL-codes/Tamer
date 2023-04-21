package com.dinim.test.physics2dlibrary;

import java.awt.Image;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.dinim.graphics.GraphicsContextSwingImpl;
import com.dinim.graphics.IGraphicsContext;
import com.dinim.object.Object2D;
import com.dinim.object.Rectangle2D;
import com.dinim.object.Star2D;
import com.dinim.test.TestApplication;

public class IntersectionOfTwoRectsTest extends TestApplication {

    List objects = new ArrayList();

    public static void main(String[] args) {
        IntersectionOfTwoRectsTest iotrt = new IntersectionOfTwoRectsTest();
        iotrt.showForm();
        while (true) {
            iotrt.draw();
            for (int i = 0; i < 50000; i++) {
                Thread.yield();
            }
        }
    }

    public IntersectionOfTwoRectsTest() {
        objects.add(new Rectangle2D(20, 20, 100, 100));
        objects.add(new Star2D(80, 40, 100, 100, 20));
        objects.add(new Rectangle2D(20, 20, 100, 100));
        objects.add(new Star2D(100, 140, 100, 100, 20));
        objects.add(new Rectangle2D(120, 120, 100, 100));
        objects.add(new Star2D(130, 40, 100, 100, 20));
    }

    public void draw() {
        Image imagebuffer = this.getC().createImage(width, height);
        imagebuffer.getGraphics();
        IGraphicsContext gcc = new GraphicsContextSwingImpl(imagebuffer.getGraphics());
        boolean switcher = true;
        for (int i = 0; i < objects.size(); i++) {
            Object2D obj = (Object2D) objects.get(i);
            if (switcher) {
                obj.rotate(1, obj.getCenterPoint());
            } else {
                if (i == 0) {
                    obj.rotate(1, ((Object2D) objects.get(objects.size() - 1)).getCenterPoint());
                } else {
                    obj.rotate(1, ((Object2D) objects.get(i - 1)).getCenterPoint());
                }
            }
            switcher = !switcher;
            obj.draw(gcc);
        }
        this.getC().getGraphics().drawImage(imagebuffer, 0, 0, this.getC());
    }

    public void paintSomething(IGraphicsContext gc) {
    }
}
