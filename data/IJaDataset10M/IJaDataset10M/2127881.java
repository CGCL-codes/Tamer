package org.rendersnake;

import static org.rendersnake.HtmlAttributesFactory.id;
import java.io.IOException;

public class SamplePage implements Renderable {

    public void renderOn(HtmlCanvas canvas) throws IOException {
        canvas.div(id("mine"))._div();
    }
}
