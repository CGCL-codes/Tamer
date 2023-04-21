package net.sourceforge.texture.threads;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import net.sourceforge.texture.boundary.WaveformCanvas;
import utils.stream.CustomPipedInputStream;

public class WaveformGenerator extends StreamsProcessor {

    private static final Color pastelAzure = new Color(102, 153, 255);

    private WaveformCanvas waveformCanvas;

    private ByteArrayInputStream bufferInputStream;

    private byte[] sampleByteArray;

    private int bufferSize;

    private int numSamples;

    private int sampleSizeInByte;

    private Method getValueMethod;

    private int width;

    private int yMax;

    private double xScale;

    private double yScale;

    private int binaryXScaleFactor;

    private double samplePosition;

    private long sampleValue;

    private int oldX;

    private int oldY;

    private int newX;

    private int newY;

    private BufferedImage waveformBufferedImage;

    private Graphics2D g2D;

    private ImageWriter imageWriter;

    private ImageOutputStream imageOutputStream;

    public WaveformGenerator(CustomPipedInputStream customPipedInputStream, long streamLength, int bufferSize, Method getValueMethod, int sampleSizeInByte, int numSamples, int yMax, int width, int height, double xScale, double yScale, int binaryXScaleFactor, int index, WaveformCanvas waveformCanvas) throws FileNotFoundException {
        super(new CustomPipedInputStream[] { customPipedInputStream }, null, new long[] { streamLength }, bufferSize, waveformCanvas);
        this.waveformCanvas = waveformCanvas;
        this.numSamples = numSamples;
        this.sampleSizeInByte = sampleSizeInByte;
        this.getValueMethod = getValueMethod;
        this.bufferSize = bufferSize;
        this.sampleByteArray = new byte[this.sampleSizeInByte];
        this.width = width;
        this.yMax = yMax;
        this.xScale = xScale;
        this.yScale = yScale;
        this.binaryXScaleFactor = binaryXScaleFactor;
        this.oldX = 0;
        this.oldY = 0;
        this.newX = 0;
        this.newY = 0;
        int waveformBufferedImageWidth = (int) Math.ceil(this.numSamples * Math.scalb(1, this.binaryXScaleFactor));
        this.waveformBufferedImage = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().createCompatibleImage(waveformBufferedImageWidth, height, Transparency.BITMASK);
        this.g2D = this.waveformBufferedImage.createGraphics();
        this.g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        this.g2D.translate(0, (int) (this.yMax * yScale));
        this.g2D.setColor(Color.gray);
        this.g2D.drawLine(0, 0, waveformBufferedImageWidth, 0);
        this.g2D.setColor(pastelAzure);
        try {
            Iterator<ImageWriter> imageWritersIterator = ImageIO.getImageWritersByFormatName("png");
            this.imageWriter = imageWritersIterator.next();
            this.imageOutputStream = ImageIO.createImageOutputStream(new FileOutputStream("/home/dave/test.png"));
            this.imageWriter.setOutput(this.imageOutputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected int getXForValue(int value) {
        return (int) Math.round(value * Math.scalb(1, this.binaryXScaleFactor));
    }

    protected int getYForValue(long value) {
        return (int) Math.round(-value * this.yScale);
    }

    @Override
    protected boolean dataProcessing(int inputStreamIndex) throws IOException {
        try {
            this.bufferInputStream = new ByteArrayInputStream(this.bufferMatrix[inputStreamIndex], 0, this.cumulativeBytesRead[inputStreamIndex]);
            if (this.oldX == 0) {
                this.bufferInputStream.read(this.sampleByteArray);
                this.oldY = this.getYForValue(((Number) this.getValueMethod.invoke(null, new Object[] { this.sampleByteArray })).longValue());
            }
            int samplePosition = 0;
            while ((this.bufferInputStream.read(this.sampleByteArray)) != -1) {
                this.sampleValue = ((Number) this.getValueMethod.invoke(null, new Object[] { this.sampleByteArray })).longValue();
                this.newX = this.getXForValue(samplePosition++);
                this.newY = this.getYForValue(this.sampleValue);
                this.g2D.drawLine(this.oldX, this.oldY, this.newX, this.newY);
                this.oldX = this.newX;
                this.oldY = this.newY;
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    protected void dataProcessed() {
        this.g2D.dispose();
        try {
            this.imageWriter.write(this.waveformBufferedImage);
            this.imageWriter.dispose();
            this.imageOutputStream.close();
            this.waveformCanvas.drawWaveformGraph(this.waveformBufferedImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
