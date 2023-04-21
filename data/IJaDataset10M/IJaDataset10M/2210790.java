package dmi.unict.it;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;
import java.awt.image.DirectColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import quicktime.QTException;
import quicktime.QTSession;
import quicktime.qd.QDGraphics;
import quicktime.qd.QDRect;
import quicktime.std.image.CodecComponent;
import quicktime.std.image.DSequence;
import quicktime.std.image.ImageDescription;
import quicktime.std.image.Matrix;
import quicktime.std.image.QTImage;
import quicktime.std.sg.SGChannel;
import quicktime.std.sg.SGDataProc;
import quicktime.std.sg.SGVideoChannel;
import quicktime.std.sg.SequenceGrabber;
import quicktime.util.QTPointerRef;
import quicktime.util.RawEncodedImage;

public class MDWebCam {

    private ArrayList<FrameListener> frameListeners;

    private BufferedImage image;

    private boolean cicloTHREAD = true;

    private ColorModel colorModel;

    private DataBuffer db;

    private Image frame;

    private int size;

    private int[] pixelData;

    private QDRect WCsize;

    private QDGraphics WCgraphics;

    private Runnable idleCamera;

    private SequenceGrabber sg;

    private SGDataProc dp;

    private SGVideoChannel vc;

    private WritableRaster raster;

    public MDWebCam(final MDPanel pan) {
        frameListeners = new ArrayList<FrameListener>();
        try {
            this.initWC();
            WCsize = new QDRect(MDConst.cameraWidth, MDConst.cameraHeight);
            WCgraphics = new QDGraphics(WCsize);
            sg = new SequenceGrabber();
            vc = new SGVideoChannel(sg);
            sg.setGWorld(WCgraphics, null);
            vc.setBounds(WCsize);
            vc.setUsage(quicktime.std.StdQTConstants.seqGrabRecord);
            vc.setFrameRate(MDConst.FRAMERATE);
            vc.setCompressorType(MDConst.CODEC);
            size = (WCgraphics.getPixMap().getPixelData().getRowBytes() / 4) * WCsize.getHeight();
            pixelData = new int[size];
            db = new DataBufferInt(pixelData, size);
            colorModel = new DirectColorModel(32, 0x00ff0000, 0x0000ff00, 0x000000ff);
            raster = Raster.createPackedRaster(db, WCsize.getWidth(), WCsize.getHeight(), (WCgraphics.getPixMap().getPixelData().getRowBytes() / 4), MDConst.MASKS, null);
            image = new BufferedImage(colorModel, raster, false, null);
            dp = new SGDataProc() {

                private byte[] rawData = new byte[QTImage.getMaxCompressionSize(WCgraphics, WCgraphics.getBounds(), 0, quicktime.std.StdQTConstants.codecLowQuality, MDConst.CODEC, CodecComponent.anyCodec)];

                private DSequence ds;

                private Matrix idMatrix = new Matrix();

                private RawEncodedImage ri;

                public int execute(SGChannel chan, QTPointerRef dataToWrite, int offset, int chRefCon, int time, int writeType) {
                    if (chan instanceof SGVideoChannel) {
                        try {
                            ImageDescription id = vc.getImageDescription();
                            if (rawData == null) rawData = new byte[dataToWrite.getSize()];
                            ri = new RawEncodedImage(rawData);
                            dataToWrite.copyToArray(0, rawData, 0, dataToWrite.getSize());
                            if (ds == null) ds = new DSequence(id, ri, WCgraphics, WCsize, idMatrix, null, 0, quicktime.std.StdQTConstants.codecNormalQuality, CodecComponent.anyCodec); else ds.decompressFrameS(ri, quicktime.std.StdQTConstants.codecNormalQuality);
                            WCgraphics.getPixMap().getPixelData().copyToArray(0, pixelData, 0, pixelData.length);
                            pan.setImgP2(image);
                            frame = image;
                            if (frame != null) fireFrameListenerEvent();
                            return MDConst.EXIT_SUCCESS;
                        } catch (Exception err) {
                            err.printStackTrace();
                            return Math.abs(MDConst.EXIT_FAILED);
                        }
                    } else return Math.abs(MDConst.EXIT_FAILED);
                }
            };
            sg.setDataProc(dp);
            sg.setDataOutput(null, quicktime.std.StdQTConstants.seqGrabDontMakeMovie);
            sg.prepare(false, true);
            sg.startRecord();
            idleCamera = new Runnable() {

                public void run() {
                    try {
                        cicloTHREAD = true;
                        while (cicloTHREAD) {
                            sg.idleMore();
                            sg.update(null);
                            try {
                                Thread.sleep(42);
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            };
            (new Thread(idleCamera)).start();
        } catch (Exception err) {
            err.printStackTrace();
            this.destroyWC();
        }
    }

    private void initWC() {
        try {
            QTSession.open();
        } catch (QTException err) {
            err.printStackTrace();
        }
    }

    public void addFrameListener(FrameListener frameListener) {
        frameListeners.add(frameListener);
    }

    private void fireFrameListenerEvent() {
        for (int i = 0; i < frameListeners.size(); ((FrameListener) frameListeners.get(i++)).frameUpdated(frame)) ;
    }

    public void destroyWC() {
        cicloTHREAD = false;
        QTSession.close();
    }
}
