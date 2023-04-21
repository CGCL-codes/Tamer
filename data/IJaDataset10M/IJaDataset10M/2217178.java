package tracer.imaging;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import tracer.basicdatatypes.Color;

/**
 * Reads and writes a PPM file from/to a stream.
 * @author martin
 *
 */
public class PPMBitmapReaderWriter implements IBitmapReaderWriter {

    /**
	 * Maximal line lenght bevor line break in PPM format.
	 */
    private static final int MAX_PPM_LINE_LENGTH = 60;

    /**
	 * Used for integer to byte conversion.
	 */
    private static final int BYTE_TO_INT_OFFSET = 128;

    /**
	 * Used for defining the PPM file.
	 */
    private static final int PPM_DEPTH_8BIT = 255;

    /**
	 * Used for writing PPM in ASCII Format.
	 */
    public static final int PPM3_FILEFORMAT = 0;

    /**
	 * Used for writing PPM in binary Format.
	 */
    public static final int PPM6_FILEFORMAT = 1;

    /**
	 * Default PPM Format (Binary).
	 */
    public static final int PPM_DEFAULT = PPMBitmapReaderWriter.PPM6_FILEFORMAT;

    /**
	 * Creates a new PPMBitmapReaderWriter Object.
	 */
    public PPMBitmapReaderWriter() {
    }

    /**
	 * @param input The InputStream containing the PPM file.
	 * @return The Bitmap read from the PPM file
	 * @throws IOException Input/Output Exception
	 * @see tracer.imaging.IBitmapReaderWriter#readBitmap(
	 * java.io.InputStream)
	 */
    public final Bitmap readBitmap(final InputStream input) throws IOException {
        Bitmap result;
        int type = -1;
        int depth = -1;
        type = this.readPPMMagicByte(input);
        result = this.readPPMImageDimensions(input);
        depth = this.readPPMImageDepth(input);
        if (depth == PPM_DEPTH_8BIT) {
            if (type == PPMBitmapReaderWriter.PPM3_FILEFORMAT) {
                this.readPPM3Data(input, result);
                return result;
            } else if (type == PPMBitmapReaderWriter.PPM6_FILEFORMAT) {
                this.readPPM6Data(input, result);
                return result;
            }
        }
        return null;
    }

    /**
	 * Reads a PPM3 Datafield to a bitmap.
	 * @param input The input stream
	 * @param bitmap The bitmap
	 * @throws IOException Input/Output Exception
	 */
    private void readPPM3Data(final InputStream input, final Bitmap bitmap) throws IOException {
        assert (bitmap != null);
        assert (input != null);
        int c;
        String currrentColor = "";
        int xOffset = 0;
        int yOffset = 0;
        int cOffset = 0;
        byte tmpR = 0;
        byte tmpG = 0;
        byte tmpB = 0;
        while ((c = input.read()) != -1) {
            c = this.removeComments(c, input);
            if (yOffset < bitmap.getHeight()) {
                if (Character.isWhitespace(c) && currrentColor.length() != 0) {
                    switch(cOffset) {
                        case 0:
                            tmpR = (byte) (Integer.parseInt(currrentColor) - BYTE_TO_INT_OFFSET);
                            break;
                        case 1:
                            tmpG = (byte) (Integer.parseInt(currrentColor) - BYTE_TO_INT_OFFSET);
                            break;
                        case 2:
                            tmpB = (byte) (Integer.parseInt(currrentColor) - BYTE_TO_INT_OFFSET);
                            break;
                        default:
                            break;
                    }
                    currrentColor = "";
                    if (cOffset == 2) {
                        bitmap.setPixel(xOffset, yOffset, new Color(tmpR, tmpG, tmpB));
                        xOffset++;
                        if (xOffset >= bitmap.getWidth()) {
                            xOffset = 0;
                            yOffset++;
                        }
                        cOffset = 0;
                    } else {
                        cOffset++;
                    }
                } else if (!Character.isWhitespace(c)) {
                    currrentColor += (char) c;
                }
            }
        }
    }

    /**
	 * Reads a PPM6 data stream to a bitmap.
	 * @param input The input stream
	 * @param bitmap The bitmap
	 * @throws IllegalArgumentException
	 * @throws IOException Input/Output Exception
	 */
    private void readPPM6Data(final InputStream input, final Bitmap bitmap) throws IOException {
        int c;
        int xOffset = 0;
        int yOffset = 0;
        int cOffset = 0;
        byte tmpR = 0;
        byte tmpG = 0;
        byte tmpB = 0;
        while ((c = input.read()) != -1) {
            if (yOffset < bitmap.getHeight()) {
                switch(cOffset) {
                    case 0:
                        tmpR = (byte) (c - BYTE_TO_INT_OFFSET);
                        break;
                    case 1:
                        tmpG = (byte) (c - BYTE_TO_INT_OFFSET);
                        break;
                    case 2:
                        tmpB = (byte) (c - BYTE_TO_INT_OFFSET);
                        break;
                    default:
                        break;
                }
                if (cOffset == 2) {
                    bitmap.setPixel(xOffset, yOffset, new Color(tmpR, tmpG, tmpB));
                    xOffset++;
                    if (xOffset >= bitmap.getWidth()) {
                        xOffset = 0;
                        yOffset++;
                    }
                    cOffset = 0;
                } else {
                    cOffset++;
                }
            }
        }
    }

    /**
	 * Searches for the image depth and returns it.
	 * @param input The input stream
	 * @return The image depth
	 * @throws IOException Input/Output Exception
	 */
    private int readPPMImageDepth(final InputStream input) throws IOException {
        int c = input.read();
        String depth = "";
        while ((c != -1) && !Character.isWhitespace(c)) {
            c = this.removeComments(c, input);
            depth += (char) c;
            c = input.read();
        }
        return Integer.parseInt(depth);
    }

    /**
	 * Searches for the image dimensions and creates the image.
	 * @param input The input stream
	 * @return The new image with the read size
	 * @throws IOException Input/Output Exception
	 */
    private Bitmap readPPMImageDimensions(final InputStream input) throws IOException {
        int imageHeight = -1;
        int imageWidth = -1;
        int c;
        String size = "";
        while ((c = input.read()) != -1) {
            c = this.removeComments(c, input);
            if (Character.isWhitespace(c) && size.length() > 0) {
                if (imageWidth == -1) {
                    imageWidth = Integer.parseInt(size);
                } else {
                    imageHeight = Integer.parseInt(size);
                    break;
                }
                size = "";
            } else if (!Character.isWhitespace(c)) {
                size += (char) c;
            }
        }
        if (imageHeight != -1 && imageWidth != -1) {
            return new Bitmap(imageWidth, imageHeight);
        }
        return null;
    }

    /**
	 * Sets the InputStream pointer to the next character which is not a
	 * commented Symbol.
	 * @param c The current input
	 * @param input The stream
	 * @return returns next (not commented) symbol
	 * @throws IOException Input/Output Exception
	 */
    private int removeComments(final int c, final InputStream input) throws IOException {
        if ((char) c == '#') {
            try {
                this.readPPMFindNextLine(input);
            } catch (IOException e) {
                e.printStackTrace();
            }
            int nc = input.read();
            return removeComments(nc, input);
        } else {
            return c;
        }
    }

    /**
	 * Reads the "magic-bytes" at the beginning of the file.
	 * @param input The input stream
	 * @return The found PPM-Version (see static constants for further
	 *         information)
	 * @throws IOException Input/Output Exception
	 */
    private int readPPMMagicByte(final InputStream input) throws IOException {
        int c = input.read();
        boolean found = false;
        boolean foundP = false;
        int statusFound = -1;
        while (!found && c != -1) {
            c = this.removeComments(c, input);
            if ((char) c == 'P') {
                foundP = true;
            } else if (foundP) {
                if (statusFound == -1) {
                    if ((char) c == '3') {
                        readPPMFindNextLine(input);
                        return PPMBitmapReaderWriter.PPM3_FILEFORMAT;
                    } else if ((char) c == '6') {
                        readPPMFindNextLine(input);
                        return PPMBitmapReaderWriter.PPM6_FILEFORMAT;
                    } else {
                        foundP = false;
                    }
                } else {
                    if ((char) c == '\n') {
                        return statusFound;
                    }
                }
            }
            c = input.read();
        }
        return -1;
    }

    /**
	 * Searches for the next end of line and discards all characters until
	 * then.
	 * @param input The input stream
	 * @throws IOException Input/Output Exception
	 */
    private void readPPMFindNextLine(final InputStream input) throws IOException {
        int c;
        while ((c = input.read()) != -1) {
            if ((char) c == '\n') {
                return;
            }
        }
    }

    /**
	 * Writes a Bitmap to a PPM formatted stream.
	 * @param stream The OutputStream where the PPM format is written to
	 * @param bitmap The Bitmap which gets written
	 * @throws IOException Input/Output Exception
	 * @see tracer.imaging.IBitmapReaderWriter#writeBitmap(
	 * OutputStream, Bitmap)
	 */
    public final void writeBitmap(final OutputStream stream, final Bitmap bitmap) throws IOException {
        this.writeBitmap(stream, bitmap, PPMBitmapReaderWriter.PPM_DEFAULT);
    }

    /**
	 * Writes a bitmap with a specified PPM-Version to a stream. Possible
	 * versions can be found as static constants PPM_*.
	 * @param stream The output stream
	 * @param bitmap The bitmap which should be written to the stream.
	 * @param pPMFileVersion The preferred PPM-Version. Possible versions
	 *        can be found as static constants PPM_* in this class.
	 * @throws IOException Input/Output Exception
	 */
    public final void writeBitmap(final OutputStream stream, final Bitmap bitmap, final int pPMFileVersion) throws IOException {
        OutputStreamWriter oswriter = new OutputStreamWriter(stream);
        if (pPMFileVersion == PPMBitmapReaderWriter.PPM6_FILEFORMAT) {
            oswriter.write("P6\n");
            oswriter.write(bitmap.getWidth() + " " + bitmap.getHeight() + "\n");
            oswriter.write("255\n");
            oswriter.flush();
            for (int y = 0; y < bitmap.getHeight(); y++) {
                for (int x = 0; x < bitmap.getWidth(); x++) {
                    Color color = bitmap.getPixel(x, y);
                    stream.write((int) color.getRed() + BYTE_TO_INT_OFFSET);
                    stream.write((int) color.getGreen() + BYTE_TO_INT_OFFSET);
                    stream.write((int) color.getBlue() + BYTE_TO_INT_OFFSET);
                }
            }
            oswriter.write("\n");
            oswriter.close();
        } else if (pPMFileVersion == PPMBitmapReaderWriter.PPM3_FILEFORMAT) {
            oswriter.write("P3\n");
            oswriter.write(bitmap.getWidth() + " " + bitmap.getHeight() + "\n");
            oswriter.write("255\n");
            int lineLength = 0;
            for (int y = 0; y < bitmap.getHeight(); y++) {
                for (int x = 0; x < bitmap.getWidth(); x++) {
                    Color color = bitmap.getPixel(x, y);
                    if (color != null) {
                        String tmp = "" + Integer.toString((int) color.getRed() + BYTE_TO_INT_OFFSET) + " " + Integer.toString((int) color.getGreen() + BYTE_TO_INT_OFFSET) + " " + Integer.toString((int) color.getBlue() + BYTE_TO_INT_OFFSET) + " ";
                        oswriter.write(tmp);
                        lineLength += tmp.length();
                        if (lineLength > MAX_PPM_LINE_LENGTH) {
                            lineLength = 0;
                            oswriter.write("\n");
                        }
                    }
                }
                lineLength = 0;
                oswriter.write("\n");
                oswriter.flush();
            }
            oswriter.write("\n");
            oswriter.flush();
            oswriter.close();
        }
    }

    /**
	 * Returns the format as a String.
	 * @return The format as String
	 * @see tracer.imaging.IBitmapReaderWriter#getFormatIdentifier()
	 */
    public final String getFormatIdentifier() {
        return "PPM";
    }
}
