package org.progeeks.mapview.wms;

import java.awt.Color;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.imageio.stream.*;
import org.progeeks.util.Inspector;
import org.progeeks.mapview.GeoRectangle;

/**
 *  Requests a specific type of image from the WMS server
 *  for the configured parameters.
 *
 *  @version   $Revision: 3948 $
 *  @author    Paul Speed
 */
public class GetMapImageRequest extends AbstractGetMapRequest<BufferedImage> {

    public GetMapImageRequest() {
        super("image/png");
    }

    public GetMapImageRequest(String layers) {
        super(layers, "image/png");
    }

    protected void appendRequestParms(StringBuilder sb) {
        super.appendRequestParms(sb);
    }

    public BufferedImage readResponse(InputStream in) throws IOException {
        in = new BufferedInputStream(in, 8192);
        try {
            ImageInputStream imgStream = ImageIO.createImageInputStream(in);
            BufferedImage result = ImageIO.read(imgStream);
            return result;
        } finally {
            in.close();
        }
    }
}
