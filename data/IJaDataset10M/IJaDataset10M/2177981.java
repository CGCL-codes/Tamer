package org.jowidgets.spi.impl.swt.common.image;

import java.io.IOException;
import java.net.URL;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.jowidgets.spi.impl.image.IImageFactory;

public class SwtImageLoader implements IImageFactory<Image> {

    private final URL url;

    public SwtImageLoader(final URL url) {
        super();
        this.url = url;
    }

    @Override
    public Image createImage() {
        try {
            return new Image(Display.getCurrent(), url.openStream());
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
}
