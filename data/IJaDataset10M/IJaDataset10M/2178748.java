package org.xmlvm.demo.fireworks;

import Compatlib.System.Collections.Generics.List;
import Compatlib.System.Windows.Rect;
import Compatlib.System.Windows.Size;
import Compatlib.System.Windows.Controls.Image;
import Compatlib.System.Windows.Controls.Panel;

public class FireworksPanel extends Panel {

    List<Spark> sparks = new List<Spark>();

    @Override
    protected Size MeasureOverride(Size availableSize) {
        Environment.windowHeight = (int) availableSize.getHeight();
        Environment.windowWidth = (int) availableSize.getWidth();
        return availableSize;
    }

    @Override
    protected Size ArrangeOverride(Size finalSize) {
        for (int i = 0; i < sparks.getCount(); i++) {
            Spark spark = sparks.__access(i);
            Image image = spark.getImage();
            float x = spark.getX();
            float y = spark.getY();
            image.Arrange(new Rect(x, y, Const.IMAGE_SIZE, Const.IMAGE_SIZE));
        }
        return finalSize;
    }

    public void addSpark(Spark spark) {
        sparks.Add(spark);
    }
}
