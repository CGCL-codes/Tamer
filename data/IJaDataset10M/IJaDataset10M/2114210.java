package org.cumt.misc;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;
import java.awt.image.SinglePixelPackedSampleModel;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Icon used in model objets. 
 * This is more serialization-friendly due buffered images are not serializable.
 * @author <a href="cdescalzi2001@yahoo.com.ar">Carlos Descalzi</a>
 */
public class ModelIcon implements Icon, Serializable {

    private static final Log LOG = LogFactory.getLog(ModelIcon.class);

    private static final long serialVersionUID = 3802701859960927111L;

    private URL url;

    private int[] pixels = null;

    private int width;

    private int height;

    private int size;

    private int dataType;

    private int scanlineStride;

    private int[] bitMasks;

    private transient BufferedImage image;

    public static ModelIcon fromImage(URL location) throws IOException {
        ModelIcon icon = new ModelIcon();
        icon.setImage(ImageIO.read(location));
        icon.setUrl(location);
        return icon;
    }

    public static ModelIcon fromBase64(String base64) throws IOException {
        ModelIcon icon = new ModelIcon();
        byte[] data = Base64.decodeBase64(base64.getBytes());
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        DataInputStream dataInput = new DataInputStream(input);
        icon.url = new URL(dataInput.readUTF());
        int[] pixels = new int[dataInput.readInt()];
        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = dataInput.readInt();
        }
        icon.pixels = pixels;
        icon.width = dataInput.readInt();
        icon.height = dataInput.readInt();
        icon.size = dataInput.readInt();
        icon.dataType = dataInput.readInt();
        icon.scanlineStride = dataInput.readInt();
        int[] bitMasks = new int[dataInput.readInt()];
        for (int i = 0; i < bitMasks.length; i++) {
            bitMasks[i] = dataInput.readInt();
        }
        icon.bitMasks = bitMasks;
        return icon;
    }

    public String toBase64() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        DataOutputStream dataOutput = new DataOutputStream(output);
        try {
            dataOutput.writeUTF(url.toString());
            dataOutput.writeInt(pixels.length);
            for (int element : pixels) {
                dataOutput.writeInt(element);
            }
            dataOutput.writeInt(width);
            dataOutput.writeInt(height);
            dataOutput.writeInt(size);
            dataOutput.writeInt(dataType);
            dataOutput.writeInt(scanlineStride);
            dataOutput.writeInt(bitMasks.length);
            for (int element : bitMasks) {
                dataOutput.writeInt(element);
            }
        } catch (IOException ex) {
            LOG.error("Unexpected error", ex);
        }
        return new String(Base64.encodeBase64Chunked(output.toByteArray()));
    }

    public void paintIcon(Component component, Graphics g, int x, int y) {
        g.drawImage(getImage(), x, y, null);
    }

    public int getIconWidth() {
        return getImage().getWidth();
    }

    public int getIconHeight() {
        return getImage().getHeight();
    }

    private BufferedImage getImage() {
        if (image == null) {
            image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            DataBufferInt buffer = new DataBufferInt(pixels, size);
            SinglePixelPackedSampleModel sampleModel = new SinglePixelPackedSampleModel(dataType, width, height, scanlineStride, bitMasks);
            WritableRaster raster = WritableRaster.createWritableRaster(sampleModel, buffer, new Point(0, 0));
            image.setData(raster);
        }
        return image;
    }

    private void setImage(BufferedImage image) {
        if (image.getWidth() > 16 || image.getHeight() > 16) {
            this.image = new BufferedImage(16, 16, BufferedImage.TYPE_INT_RGB);
            this.image.getGraphics().drawImage(image, 0, 0, 16, 16, null);
        } else {
            this.image = image;
        }
        WritableRaster raster = this.image.getRaster();
        if (raster.getDataBuffer().getDataType() != DataBuffer.TYPE_INT) {
            BufferedImage newImage = new BufferedImage(this.image.getWidth(), this.image.getHeight(), BufferedImage.TYPE_INT_RGB);
            newImage.getGraphics().drawImage(this.image, 0, 0, null);
            this.image = newImage;
            raster = this.image.getRaster();
        }
        DataBufferInt buffer = (DataBufferInt) raster.getDataBuffer();
        pixels = buffer.getData();
        size = buffer.getSize();
        width = this.image.getWidth();
        height = this.image.getHeight();
        SinglePixelPackedSampleModel sampleModel = (SinglePixelPackedSampleModel) raster.getSampleModel();
        dataType = sampleModel.getDataType();
        scanlineStride = sampleModel.getScanlineStride();
        bitMasks = sampleModel.getBitMasks();
    }

    public URL getUrl() {
        return url;
    }

    private void setUrl(URL url) {
        this.url = url;
    }

    public Object clone() {
        ModelIcon modelIcon = new ModelIcon();
        modelIcon.url = url;
        modelIcon.pixels = pixels;
        modelIcon.width = width;
        modelIcon.height = height;
        modelIcon.size = size;
        modelIcon.dataType = dataType;
        modelIcon.bitMasks = bitMasks;
        return modelIcon;
    }
}
