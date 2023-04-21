package ho.module.ifa.gif;

import java.awt.*;
import java.io.*;
import java.util.Vector;

/** This is the central class of a JDK 1.1 compatible GIF encoder that, AFAIK,
 *  supports more features of the extended GIF spec than any other Java open
 *  source encoder.  Some sections of the source are lifted or adapted from Jef
 *  Poskanzer's <cite>Acme GifEncoder</cite> (so please see the
 *  <a href="../readme.txt">readme</a> containing his notice), but much of it,
 *  including nearly all of the present class, is original code.  My main
 *  motivation for writing a new encoder was to support animated GIFs, but the
 *  package also adds support for embedded textual comments.
 *  <p>
 *  There are still some limitations.  For instance, animations are limited to
 *  a single global color table.  But that is usually what you want anyway, so
 *  as to avoid irregularities on some displays.  (So this is not really a
 *  limitation, but a "disciplinary feature" :)  Another rather more serious
 *  restriction is that the total number of RGB colors in a given input-batch
 *  mustn't exceed 256.  Obviously, there is an opening here for someone who
 *  would like to add a color-reducing preprocessor.
 *  <p>
 *  The encoder, though very usable in its present form, is at bottom only a
 *  partial implementation skewed toward my own particular needs.  Hence a
 *  couple of caveats are in order.  (1) During development it was in the back
 *  of my mind that an encoder object should be reusable - i.e., you should be
 *  able to make multiple calls to encode() on the same object, with or without
 *  intervening frame additions or changes to options.  But I haven't reviewed
 *  the code with such usage in mind, much less tested it, so it's likely I
 *  overlooked something.  (2) The encoder classes aren't thread safe, so use
 *  caution in a context where access is shared by multiple threads.  (Better
 *  yet, finish the library and re-release it :)
 *  <p>
 *  There follow a couple of simple examples illustrating the most common way to
 *  use the encoder, i.e., to encode AWT Image objects created elsewhere in the
 *  program.  Use of some of the most popular format options is also shown,
 *  though you will want to peruse the API for additional features.
 *
 *  <p>
 *  <strong>Animated GIF Example</strong>
 *  <pre>
 *  import net.jmge.gif.Gif89Encoder;
 *  // ...
 *  void writeAnimatedGIF(Image[] still_images,
 *                        String annotation,
 *                        boolean looped,
 *                        double frames_per_second,
 *                        OutputStream out) throws IOException
 *  {
 *    Gif89Encoder gifenc = new Gif89Encoder();
 *    for (int i = 0; i < still_images.length; ++i)
 *      gifenc.addFrame(still_images[i]);
 *    gifenc.setComments(annotation);
 *    gifenc.setLoopCount(looped ? 0 : 1);
 *    gifenc.setUniformDelay((int) Math.round(100 / frames_per_second));
 *    gifenc.encode(out);
 *  }
 *  </pre>
 *
 *  <strong>Static GIF Example</strong>
 *  <pre>
 *  import net.jmge.gif.Gif89Encoder;
 *  // ...
 *  void writeNormalGIF(Image img,
 *                      String annotation,
 *                      int transparent_index,  // pass -1 for none
 *                      boolean interlaced,
 *                      OutputStream out) throws IOException
 *  {
 *    Gif89Encoder gifenc = new Gif89Encoder(img);
 *    gifenc.setComments(annotation);
 *    gifenc.setTransparentIndex(transparent_index);
 *    gifenc.getFrameAt(0).setInterlaced(interlaced);
 *    gifenc.encode(out);
 *  }
 *  </pre>
 *
 * @version 0.90 beta (15-Jul-2000)
 * @author J. M. G. Elliott (tep@jmge.net)
 * @see Gif89Frame
 * @see DirectGif89Frame
 * @see IndexGif89Frame
 */
public class Gif89Encoder {

    private Dimension dispDim = new Dimension(0, 0);

    private GifColorTable colorTable;

    private int bgIndex = 0;

    private int loopCount = 1;

    private String theComments;

    private Vector vFrames = new Vector();

    /** Use this default constructor if you'll be adding multiple frames
   *  constructed from RGB data (i.e., AWT Image objects or ARGB-pixel arrays).
   */
    public Gif89Encoder() {
        colorTable = new GifColorTable();
    }

    /** Like the default except that it also adds a single frame, for conveniently
   *  encoding a static GIF from an image.
   *
   * @param static_image
   *   Any Image object that supports pixel-grabbing.
   * @exception IOException
   *   See the addFrame() methods.   
   */
    public Gif89Encoder(Image static_image) throws IOException {
        this();
        addFrame(static_image);
    }

    /** This constructor installs a user color table, overriding the detection of
   *  of a palette from ARBG pixels.
   *
   *  Use of this constructor imposes a couple of restrictions:
   *  (1) Frame objects can't be of type DirectGif89Frame
   *  (2) Transparency, if desired, must be set explicitly.
   *
   * @param colors
   *   Array of color values; no more than 256 colors will be read, since that's
   *   the limit for a GIF.
   */
    public Gif89Encoder(Color[] colors) {
        colorTable = new GifColorTable(colors);
    }

    /** Convenience constructor for encoding a static GIF from index-model data.
   *  Adds a single frame as specified.
   *
   * @param colors
   *   Array of color values; no more than 256 colors will be read, since
   *   that's the limit for a GIF.
   * @param width
   *   Width of the GIF bitmap.
   * @param height
   *   Height of same.
   * @param ci_pixels
   *   Array of color-index pixels no less than width * height in length.
   * @exception IOException
   *   See the addFrame() methods.   
   */
    public Gif89Encoder(Color[] colors, int width, int height, byte ci_pixels[]) throws IOException {
        this(colors);
        addFrame(width, height, ci_pixels);
    }

    /** Get the number of frames that have been added so far.
   *
   * @return
   *  Number of frame items.
   */
    public int getFrameCount() {
        return vFrames.size();
    }

    /** Get a reference back to a Gif89Frame object by position. 
   *
   * @param index
   *   Zero-based index of the frame in the sequence.
   * @return
   *   Gif89Frame object at the specified position (or null if no such frame).   
   */
    public Gif89Frame getFrameAt(int index) {
        return isOk(index) ? (Gif89Frame) vFrames.elementAt(index) : null;
    }

    /** Add a Gif89Frame frame to the end of the internal sequence.  Note that
   *  there are restrictions on the Gif89Frame type: if the encoder object was
   *  constructed with an explicit color table, an attempt to add a
   *  DirectGif89Frame will throw an exception.
   *
   * @param gf
   *   An externally constructed Gif89Frame.
   * @exception IOException
   *   If Gif89Frame can't be accommodated.  This could happen if either (1) the
   *   aggregate cross-frame RGB color count exceeds 256, or (2) the Gif89Frame
   *   subclass is incompatible with the present encoder object.
   */
    public void addFrame(Gif89Frame gf) throws IOException {
        accommodateFrame(gf);
        vFrames.addElement(gf);
    }

    /** Convenience version of addFrame() that takes a Java Image, internally
   *  constructing the requisite DirectGif89Frame.
   *
   * @param image
   *   Any Image object that supports pixel-grabbing.
   * @exception IOException
   *   If either (1) pixel-grabbing fails, (2) the aggregate cross-frame RGB
   *   color count exceeds 256, or (3) this encoder object was constructed with
   *   an explicit color table.  
   */
    public void addFrame(Image image) throws IOException {
        addFrame(new DirectGif89Frame(image));
    }

    /** The index-model convenience version of addFrame().
   *
   * @param width
   *   Width of the GIF bitmap.
   * @param height
   *   Height of same.
   * @param ci_pixels
   *   Array of color-index pixels no less than width * height in length.
   * @exception IOException
   *   Actually, in the present implementation, there aren't any unchecked
   *   exceptions that can be thrown when adding an IndexGif89Frame
   *   <i>per se</i>.  But I might add some pedantic check later, to justify the
   *   generality :)
   */
    public void addFrame(int width, int height, byte ci_pixels[]) throws IOException {
        addFrame(new IndexGif89Frame(width, height, ci_pixels));
    }

    /** Like addFrame() except that the frame is inserted at a specific point in
   *  the sequence rather than appended. 
   *
   * @param index
   *   Zero-based index at which to insert frame.
   * @param gf
   *   An externally constructed Gif89Frame.
   * @exception IOException
   *   If Gif89Frame can't be accommodated.  This could happen if either (1)
   *   the aggregate cross-frame RGB color count exceeds 256, or (2) the
   *   Gif89Frame subclass is incompatible with the present encoder object.
   */
    public void insertFrame(int index, Gif89Frame gf) throws IOException {
        accommodateFrame(gf);
        vFrames.insertElementAt(gf, index);
    }

    /** Set the color table index for the transparent color, if any.
   *
   * @param index
   *   Index of the color that should be rendered as transparent, if any.
   *   A value of -1 turns off transparency.  (Default: -1)
   */
    public void setTransparentIndex(int index) {
        colorTable.setTransparent(index);
    }

    /** Sets attributes of the multi-image display area, if applicable.
   *
   * @param dim
   *   Width/height of display.  (Default: largest detected frame size)
   * @param background
   *   Color table index of background color.  (Default: 0)
   * @see Gif89Frame#setPosition
   */
    public void setLogicalDisplay(Dimension dim, int background) {
        dispDim = new Dimension(dim);
        bgIndex = background;
    }

    /** Set animation looping parameter, if applicable.
   *
   * @param count
   *   Number of times to play sequence.  Special value of 0 specifies
   *   indefinite looping.  (Default: 1)  
   */
    public void setLoopCount(int count) {
        loopCount = count;
    }

    /** Specify some textual comments to be embedded in GIF.
   *
   * @param comments
   *   String containing ASCII comments.
   */
    public void setComments(String comments) {
        theComments = comments;
    }

    /** A convenience method for setting the "animation speed".  It simply sets
   *  the delay parameter for each frame in the sequence to the supplied value.
   *  Since this is actually frame-level rather than animation-level data, take
   *  care to add your frames before calling this method.
   *
   * @param interval
   *   Interframe interval in centiseconds.
   */
    public void setUniformDelay(int interval) {
        for (int i = 0; i < vFrames.size(); ++i) ((Gif89Frame) vFrames.elementAt(i)).setDelay(interval);
    }

    /** After adding your frame(s) and setting your options, simply call this
   * method to write the GIF to the passed stream.  Multiple calls are
   * permissible if for some reason that is useful to your application.  (The
   * method simply encodes the current state of the object with no thought
   * to previous calls.)
   *
   * @param out
   *   The stream you want the GIF written to.
   * @exception IOException
   *   If a write error is encountered.
   */
    public void encode(OutputStream out) throws IOException {
        int nframes = getFrameCount();
        boolean is_sequence = nframes > 1;
        colorTable.closePixelProcessing();
        Put.ascii("GIF89a", out);
        writeLogicalScreenDescriptor(out);
        colorTable.encode(out);
        if (is_sequence && loopCount != 1) writeNetscapeExtension(out);
        if (theComments != null && theComments.length() > 0) writeCommentExtension(out);
        for (int i = 0; i < nframes; ++i) ((Gif89Frame) vFrames.elementAt(i)).encode(out, is_sequence, colorTable.getDepth(), colorTable.getTransparent());
        out.write((int) ';');
        out.flush();
    }

    /** A simple driver to test the installation and to demo usage.  Put the JAR
   * on your classpath and run ala
   * <blockquote>java net.jmge.gif.Gif89Encoder {filename}</blockquote>
   * The filename must be either (1) a JPEG file with extension 'jpg', for
   * conversion to a static GIF, or (2) a file containing a list of GIFs and/or
   * JPEGs, one per line, to be combined into an animated GIF.  The output will
   * appear in the current directory as 'gif89out.gif'.
   * <p>
   * (N.B. This test program will abort if the input file(s) exceed(s) 256 total
   * RGB colors, so in its present form it has no value as a generic JPEG to GIF
   * converter.  Also, when multiple files are input, you need to be wary of the
   * total color count, regardless of file type.)
   *
   * @param args
   *   Command-line arguments, only the first of which is used, as mentioned
   *   above. 
   */
    public static void main(String[] args) {
        try {
            Toolkit tk = Toolkit.getDefaultToolkit();
            OutputStream out = new BufferedOutputStream(new FileOutputStream("gif89out.gif"));
            if (args[0].toUpperCase().endsWith(".JPG")) new Gif89Encoder(tk.getImage(args[0])).encode(out); else {
                BufferedReader in = new BufferedReader(new FileReader(args[0]));
                Gif89Encoder ge = new Gif89Encoder();
                String line;
                while ((line = in.readLine()) != null) ge.addFrame(tk.getImage(line.trim()));
                ge.setLoopCount(0);
                ge.encode(out);
                in.close();
            }
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }

    private void accommodateFrame(Gif89Frame gf) throws IOException {
        dispDim.width = Math.max(dispDim.width, gf.getWidth());
        dispDim.height = Math.max(dispDim.height, gf.getHeight());
        colorTable.processPixels(gf);
    }

    private void writeLogicalScreenDescriptor(OutputStream os) throws IOException {
        Put.leShort(dispDim.width, os);
        Put.leShort(dispDim.height, os);
        os.write(0xf0 | colorTable.getDepth() - 1);
        os.write(bgIndex);
        os.write(0);
    }

    private void writeNetscapeExtension(OutputStream os) throws IOException {
        os.write((int) '!');
        os.write(0xff);
        os.write(11);
        Put.ascii("NETSCAPE2.0", os);
        os.write(3);
        os.write(1);
        Put.leShort(loopCount > 1 ? loopCount - 1 : 0, os);
        os.write(0);
    }

    private void writeCommentExtension(OutputStream os) throws IOException {
        os.write((int) '!');
        os.write(0xfe);
        int remainder = theComments.length() % 255;
        int nsubblocks_full = theComments.length() / 255;
        int nsubblocks = nsubblocks_full + (remainder > 0 ? 1 : 0);
        int ibyte = 0;
        for (int isb = 0; isb < nsubblocks; ++isb) {
            int size = isb < nsubblocks_full ? 255 : remainder;
            os.write(size);
            Put.ascii(theComments.substring(ibyte, ibyte + size), os);
            ibyte += size;
        }
        os.write(0);
    }

    private boolean isOk(int frame_index) {
        return frame_index >= 0 && frame_index < vFrames.size();
    }
}

class GifColorTable {

    private int[] theColors = new int[256];

    private int colorDepth;

    private int transparentIndex = -1;

    private int ciCount = 0;

    private ReverseColorMap ciLookup;

    GifColorTable() {
        ciLookup = new ReverseColorMap();
    }

    GifColorTable(Color[] colors) {
        int n2copy = Math.min(theColors.length, colors.length);
        for (int i = 0; i < n2copy; ++i) theColors[i] = colors[i].getRGB();
    }

    int getDepth() {
        return colorDepth;
    }

    int getTransparent() {
        return transparentIndex;
    }

    void setTransparent(int color_index) {
        transparentIndex = color_index;
    }

    void processPixels(Gif89Frame gf) throws IOException {
        if (gf instanceof DirectGif89Frame) filterPixels((DirectGif89Frame) gf); else trackPixelUsage((IndexGif89Frame) gf);
    }

    void closePixelProcessing() {
        colorDepth = computeColorDepth(ciCount);
    }

    void encode(OutputStream os) throws IOException {
        int palette_size = 1 << colorDepth;
        for (int i = 0; i < palette_size; ++i) {
            os.write(theColors[i] >> 16 & 0xff);
            os.write(theColors[i] >> 8 & 0xff);
            os.write(theColors[i] & 0xff);
        }
    }

    private void filterPixels(DirectGif89Frame dgf) throws IOException {
        if (ciLookup == null) throw new IOException("RGB frames require palette autodetection");
        int[] argb_pixels = (int[]) dgf.getPixelSource();
        byte[] ci_pixels = dgf.getPixelSink();
        int npixels = argb_pixels.length;
        for (int i = 0; i < npixels; ++i) {
            int argb = argb_pixels[i];
            if ((argb >>> 24) < 0x80) if (transparentIndex == -1) transparentIndex = ciCount; else if (argb != theColors[transparentIndex]) {
                ci_pixels[i] = (byte) transparentIndex;
                continue;
            }
            int color_index = ciLookup.getPaletteIndex(argb & 0xffffff);
            if (color_index == -1) {
                if (ciCount == 256) throw new IOException("can't encode as GIF (> 256 colors)");
                theColors[ciCount] = argb;
                ciLookup.put(argb & 0xffffff, ciCount);
                ci_pixels[i] = (byte) ciCount;
                ++ciCount;
            } else ci_pixels[i] = (byte) color_index;
        }
    }

    private void trackPixelUsage(IndexGif89Frame igf) {
        byte[] ci_pixels = (byte[]) igf.getPixelSource();
        int npixels = ci_pixels.length;
        for (int i = 0; i < npixels; ++i) if (ci_pixels[i] >= ciCount) ciCount = ci_pixels[i] + 1;
    }

    private int computeColorDepth(int colorcount) {
        if (colorcount <= 2) return 1;
        if (colorcount <= 4) return 2;
        if (colorcount <= 16) return 4;
        return 8;
    }
}

class ReverseColorMap {

    private static class ColorRecord {

        int rgb;

        int ipalette;

        ColorRecord(int rgb, int ipalette) {
            this.rgb = rgb;
            this.ipalette = ipalette;
        }
    }

    private static final int HCAPACITY = 2053;

    private ColorRecord[] hTable = new ColorRecord[HCAPACITY];

    int getPaletteIndex(int rgb) {
        ColorRecord rec;
        for (int itable = rgb % hTable.length; (rec = hTable[itable]) != null && rec.rgb != rgb; itable = ++itable % hTable.length) ;
        if (rec != null) return rec.ipalette;
        return -1;
    }

    void put(int rgb, int ipalette) {
        int itable;
        for (itable = rgb % hTable.length; hTable[itable] != null; itable = ++itable % hTable.length) ;
        hTable[itable] = new ColorRecord(rgb, ipalette);
    }
}
