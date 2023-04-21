package com.document.tag.api.image;

import com.document.tag.api.converter.PixelPosition;
import com.document.tag.api.converter.PositionHelper;
import com.document.tag.configuration.generated.Configuration;
import com.document.tag.configuration.generated.TagConfiguration;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.MessageFormat;

/**
 * <Description>.
 *
 * @author Manuel Martins
 */
public class ImageUtils {

    /**
     * Generates the final image and returns it on the output stream.
     *
     * @param inputStream          the original image input stream.
     * @param outputStream         the output stream to pass the generated image plus tag.
     * @param matrix               the QR Code matrix.
     * @param configuration        the tag configuration.
     * @param generalConfiguration the general configuration for the generated image.
     * @throws IOException thrown by {@link ImageIO}.
     */
    public void tagImage(final InputStream inputStream, final OutputStream outputStream, final BitMatrix matrix, final TagConfiguration configuration, final Configuration.General generalConfiguration) throws IOException {
        final BufferedImage tag = MatrixToImageWriter.toBufferedImage(matrix);
        final BufferedImage image = ImageIO.read(inputStream);
        final Graphics g = image.getGraphics();
        final PixelPosition position = PositionHelper.resolvePosition(configuration, image, tag);
        g.drawImage(tag, position.getX().intValue(), position.getY().intValue(), Color.WHITE, null);
        g.dispose();
        final String imageType = MessageFormat.format(".{0}", generalConfiguration.getImageType().value());
        ImageIO.write(image, imageType, outputStream);
    }

    /**
     * Generates the final image and returns it on the output stream.
     *
     * @param inputStream          the original image input stream.
     * @param filePath             the file with the image
     * @param matrix               the QR Code matrix.
     * @param configuration        the tag configuration.
     * @param generalConfiguration the general configuration for the generated image.
     * @return file the file.
     * @throws IOException thrown by {@link ImageIO}.
     */
    public File tagImage(final InputStream inputStream, final String filePath, final BitMatrix matrix, final TagConfiguration configuration, final Configuration.General generalConfiguration) throws IOException {
        final BufferedImage tag = MatrixToImageWriter.toBufferedImage(matrix);
        final BufferedImage image = ImageIO.read(inputStream);
        final Graphics g = image.getGraphics();
        final PixelPosition position = PositionHelper.resolvePosition(configuration, image, tag);
        g.drawImage(tag, position.getX().intValue(), position.getY().intValue(), Color.WHITE, null);
        g.dispose();
        final String imageType = MessageFormat.format(".{0}", generalConfiguration.getImageType().value());
        File file = new File(filePath);
        ImageIO.write(image, imageType, file);
        return file;
    }
}
