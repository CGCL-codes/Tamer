package org.sgx.raphael4gwt.test;

import org.sgx.raphael4gwt.raphael.Circle;
import org.sgx.raphael4gwt.raphael.Paper;
import org.sgx.raphael4gwt.raphael.Rect;
import org.sgx.raphael4gwt.raphael.Set;
import org.sgx.raphael4gwt.raphael.Shape;
import org.sgx.raphael4gwt.raphael.base.Attrs;
import org.sgx.raphael4gwt.raphael.event.ForEachCallback;
import org.sgx.raphael4gwt.test.gallery.GalleryUtil;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;

/**
 * testing some common and simple usescase for creating sets
 * @author sg
 */
public class SetSimpleTest1 extends Test {

    @Override
    public void test() {
        paper.setStart();
        Circle eye1 = paper.circle(20, 20, 20);
        eye1.attr(Attrs.create().fill("yellow").stroke("red").strokeWidth(4));
        Circle eye2 = paper.circle(80, 20, 18);
        eye2.attr(Attrs.create().fill("#4488ee").stroke("brown").strokeWidth(5));
        Rect mouth1 = paper.rect(20, 80, 80, 20, 3);
        mouth1.attr(Attrs.create().fill("#aa5599").stroke("blue").strokeWidth(5));
        final Set face1 = paper.setFinish();
        Timer t = new Timer() {

            public void run() {
                Window.alert("proceed to see the green face");
                face1.forEach(new ForEachCallback() {

                    @Override
                    public boolean call(Shape shape, int i) {
                        shape.attr(Attrs.create().stroke("green"));
                        return true;
                    }

                    ;
                });
            }
        };
        t.schedule(1000);
    }

    public String[] getTags() {
        return new String[] { GalleryUtil.TAG_SET };
    }

    public SetSimpleTest1(Paper paper, int paperWidth, int paperHeight) {
        super(paper, paperWidth, paperHeight);
        this.name = "Set test1";
        this.description = "Simple raphael Set shaps usage demo";
    }

    @Override
    public String getJavaClassSource() {
        return TestResources.INSTANCE.SetSimpleTest1().getText();
    }
}
