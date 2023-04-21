package be.pwnt.jflow.demo;

import java.io.IOException;
import be.pwnt.jflow.Shape;
import be.pwnt.jflow.shape.Picture;

public class Configuration extends be.pwnt.jflow.Configuration {

    public Configuration() {
        shapes = new Shape[9];
        for (int i = 0; i < shapes.length; i++) {
            try {
                shapes[i] = new Picture(getClass().getResource("img/pic" + (i + 1) + ".jpg"));
            } catch (IOException e) {
            }
        }
    }
}
