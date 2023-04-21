package net.sourceforge.parser.mkv.type;

import java.io.IOException;
import net.sourceforge.parser.util.ByteStream;
import net.sourceforge.parser.mkv.Utils;

/**
 * @author aza_sf@yahoo.com
 *
 * @version $Revision: $
 */
public class Video extends Master {

    private static final long serialVersionUID = -2075290534812917114L;

    public int FlagInterlaced = 0;

    public int StereoMode = 0;

    public int PixelWidth;

    public int PixelHeight;

    public int PixelCropBottom = 0;

    public int PixelCropTop = 0;

    public int PixelCropLeft = 0;

    public int PixelCropRight = 0;

    public int DisplayWidth = 0;

    public int DisplayHeight;

    public int DisplayUnit = 0;

    public int AspectRatioType = 0;

    public byte[] ColourSpace;

    public float GammaValue = 0;

    public float FrameRate;

    public Video(long ebml, long size, long position) {
        super(ebml, size, position);
    }

    @Override
    public void readData(ByteStream stream) throws IOException {
        int next = 0;
        long stream_position = stream.position();
        while (stream.position() - size != stream_position) {
            next <<= 8;
            next |= stream.read();
            int t1 = (next & 0xff);
            int t2 = (next & 0xffff);
            int t3 = (next & 0xffffff);
            int s = 0;
            if (t3 == Types.COLOUR_SPACE) {
                s = (int) Utils.getSize(stream);
                ColourSpace = new byte[s];
                stream.read(ColourSpace);
            } else if (t3 == Types.GAMMA_VALUE) {
                s = (int) Utils.getSize(stream);
                GammaValue = Float.intBitsToFloat((int) Utils.bytesToLong(stream, s));
            } else if (t3 == Types.FRAME_RATE) {
                s = (int) Utils.getSize(stream);
                FrameRate = Float.intBitsToFloat((int) Utils.bytesToLong(stream, s));
            } else if (t2 == Types.STEREO_MODE) {
                s = (int) Utils.getSize(stream);
                StereoMode = (int) Utils.bytesToLong(stream, s);
            } else if (t2 == Types.PIXEL_CROP_BOTTOM) {
                s = (int) Utils.getSize(stream);
                PixelCropBottom = (int) Utils.bytesToLong(stream, s);
            } else if (t2 == Types.PIXEL_CROP_TOP) {
                s = (int) Utils.getSize(stream);
                PixelCropTop = (int) Utils.bytesToLong(stream, s);
            } else if (t2 == Types.PIXEL_CROP_LEFT) {
                s = (int) Utils.getSize(stream);
                PixelCropLeft = (int) Utils.bytesToLong(stream, s);
            } else if (t2 == Types.PIXEL_CROP_RIGHT) {
                s = (int) Utils.getSize(stream);
                PixelCropRight = (int) Utils.bytesToLong(stream, s);
            } else if (t2 == Types.DISPLAY_WIDTH) {
                s = (int) Utils.getSize(stream);
                DisplayWidth = (int) Utils.bytesToLong(stream, s);
            } else if (t2 == Types.DISPLAY_HEIGHT) {
                s = (int) Utils.getSize(stream);
                DisplayHeight = (int) Utils.bytesToLong(stream, s);
            } else if (t2 == Types.DISPLAY_UNIT) {
                s = (int) Utils.getSize(stream);
                DisplayUnit = (int) Utils.bytesToLong(stream, s);
            } else if (t2 == Types.ASPECT_RATIO_TYPE) {
                s = (int) Utils.getSize(stream);
                AspectRatioType = (int) Utils.bytesToLong(stream, s);
            } else if (t1 == Types.PIXEL_WIDTH) {
                s = (int) Utils.getSize(stream);
                PixelWidth = (int) Utils.bytesToLong(stream, s);
            } else if (t1 == Types.PIXEL_HEIGHT) {
                s = (int) Utils.getSize(stream);
                PixelHeight = (int) Utils.bytesToLong(stream, s);
            } else if (t1 == Types.FLAG_INTERLACED) {
                s = (int) Utils.getSize(stream);
                FlagInterlaced = (int) Utils.bytesToLong(stream, s);
            }
        }
    }
}
