package org.proteinshader.graphics.typography;

import org.proteinshader.graphics.exceptions.*;
import javax.media.opengl.*;
import java.util.*;
import java.io.*;

/*******************************************************************************
Reads a set of glyphs from a ".glf" file that was created with the
glFont program (Version 2.0) written by Brad Fish
(brad.fish@gmail.com).

<br/><br/>
The glFont program can be downloaded from
<a href="http://students.cs.byu.edu/~bfish/glfont.php">
http://students.cs.byu.edu/~bfish/glfont.php</a> (cited January 2007).

<br/><br/>
glFont is a Win32 program that creates a texture file containing a
user-specified range of characters in a Windows TrueType bitmap font
selected from a menu.  Brad Fish also wrote a C++ class, glFont.cpp
(Copyright (c) 1998-2002 Brad Fish), that can be used to read in a
'.glf' file to create an OpenGL texture object and then map characters
from the texture onto OpenGL quads.

<br/><br/>
Because the ProteinShader program needs to map text onto curved
surfaces, a somewhat different approach needs to be taken.  After
reading in a '.glf' file, the bitmap for each individual character
will be stored in a Glyph object as a 2D array of bytes.  These
Glyph objects will be used by a TextLabelFactory to create TextLabel
objects that hold a 2D array of bytes with the characters for an
AminoAcid label (e.g., 'A 121' for alanine 121).  To render the label
onto the surface of a segment of a tube or ribbon, a byte array will
be plugged in to the OpenGL glTexSubImage2D() function in order to
replace the center region of an existing OpenGL texture object.  This
modified texture object can then be used to map the text label onto
any curved surface that has texture coordinates.
*******************************************************************************/
public class GLF2GlyphSetFactory implements GlyphSetFactory {

    /** The '.glf' image uses 2 bytes per pixel. */
    public static final int BYTES_PER_PIXEL = 2;

    /** The '.glf' format is GL.GL_LUMINANCE_ALPHA. */
    public static final int FORMAT = GL.GL_LUMINANCE_ALPHA;

    /** The '.glf' data type is GL.GL_UNSIGNED_BYTE. */
    public static final int DATA_TYPE = GL.GL_UNSIGNED_BYTE;

    private boolean m_debug;

    /***************************************************************************
    Constructs a GLF2GlyphSetFactory.
    ***************************************************************************/
    public GLF2GlyphSetFactory() {
        m_debug = false;
    }

    /***************************************************************************
    Setting debug to true will activate several print statements that
    are useful for troubleshooting.

    @param debug  boolean value to turn debugging on or off.
    ***************************************************************************/
    public void setDebug(boolean debug) {
        m_debug = debug;
    }

    /***************************************************************************
    Reads information from a '.glf' file and constructs a GlyphSet
    object.

    <br/><br/>
    The Properties object given as an argument must hold values for
    the keys filename, fontname, typeface, fontSize, and
    bytesPerPixel.  Here is an example:

    <br/><br/>
    filename=Verdana18Bold.glf    <br/>
    fontname=Verdana              <br/>
    typeface=Bold                 <br/>
    fontSize=18                   <br/>
    bytesPerPixel=2               <br/>

    @param p  a Properties object is a type of hash.
    @return  A GlyphSet that holds Glyph objects.
    ***************************************************************************/
    public GlyphSet readGlyphSet(Properties p) throws GlyphException {
        GlyphSet glyphSet = new GlyphSet(getFilename(p), getFontname(p), getTypeface(p), getFontSize(p), BYTES_PER_PIXEL, FORMAT, DATA_TYPE);
        readFile(glyphSet, p);
        return glyphSet;
    }

    /***************************************************************************
    Reads the bitmap from the '.glf" input file and uses it to create
    Glyph objects to add to the GlyphSet.

    @param glyphSet  the GlyphSet object to add Glyphs to.
    @param p         the Properties object from a config file.
    ***************************************************************************/
    private void readFile(GlyphSet glyphSet, Properties p) throws GlyphException {
        String filename = glyphSet.getFilename();
        try {
            FileInputStream fis = new FileInputStream(filename);
            BufferedInputStream input = new BufferedInputStream(fis);
            GLF2Header header = readHeader(input, filename);
            if (m_debug) {
                debugPrint(header);
            }
            addGlyphs(glyphSet, header, input, filename);
            processBitmap(glyphSet, header, input, filename);
            input.close();
            if (m_debug) {
                debugPrint(glyphSet);
            }
        } catch (FileNotFoundException e) {
            String config = p.getProperty("configFilename");
            throw new GlyphConfigException(config, "The font file " + filename + " could not be found.");
        } catch (IOException e) {
            String config = p.getProperty("configFilename");
            throw new GlyphConfigException(config, "An IO error occurred while reading " + filename + ".");
        }
    }

    /***************************************************************************
    Reads the first HEADER_BYTES number of bytes from the input file
    and uses them to create a GLF2Header object.

    @param input  the input stream to read bytes from.
    @param filename  the name of the file that is being read from.
    @throws GlyphException  if the file does not have enough bytes.
    @throws IOException  if an IO error occurs while reading the file.
    ***************************************************************************/
    private GLF2Header readHeader(BufferedInputStream input, String filename) throws GlyphException, IOException {
        byte[] bytes = new byte[GLF2Header.HEADER_BYTES];
        if (input.read(bytes) != GLF2Header.HEADER_BYTES) {
            throw new GlyphException("File " + filename + "is not a readable '.glf' file.");
        }
        int bitMapWidth = getInt(bytes, GLF2Header.WIDTH_INDEX), bitMapHeight = getInt(bytes, GLF2Header.HEIGHT_INDEX), startChar = getInt(bytes, GLF2Header.START_INDEX), endChar = getInt(bytes, GLF2Header.END_INDEX);
        return new GLF2Header(filename, bitMapWidth, bitMapHeight, startChar, endChar);
    }

    /***************************************************************************
    This helper method for readFile() reads the region of the '.glf'
    file that contains information on the bitmap coordinates for each
    character in the set.

    <br/><br/>
    This region of the '.glf' file is after the header, but before the
    actual bitmap that makes up the bulk of the file.  Each Glyph
    object created by this method has info on the location of the
    glyph within the bitmap, but the actual bytes of the bitmap have
    not been extracted yet (the bitmap region of the '.glf' file will
    be read after this current region with glyph coordinates has been
    processed).

    @param glyphSet  The GlyphSet to add Glyph objects to.
    @param header    holds info from the header of the '.glf' file.
    @param input     the input stream to read bytes from.
    @param filename  the name of the '.glf' file.
    @throws GlyphException  if the file does not have enough bytes.
    @throws IOException  if an IO error occurs while reading the file.
    ***************************************************************************/
    private void addGlyphs(GlyphSet glyphSet, GLF2Header header, BufferedInputStream input, String filename) throws GlyphException, IOException {
        byte[] bytes = new byte[GLF2Header.CHAR_INFO_BYTES];
        int startChar = header.getStartChar(), endChar = header.getEndChar(), bitmapWidth = header.getBitmapWidth(), bitmapHeight = header.getBitmapHeight();
        for (int ch = startChar; ch <= endChar; ++ch) {
            if (input.read(bytes) == GLF2Header.CHAR_INFO_BYTES) {
                addGlyph(glyphSet, ch, bytes, bitmapWidth, bitmapHeight);
            } else {
                throw new GlyphException("File " + filename + "is not a readable '.glf' file.");
            }
        }
    }

    /***************************************************************************
    This helper method for addGlyphs() creates a Glyph and adds it to
    the GlyphSet after calculating the bitmap xy-coordinates for the
    upper left corner and lower right corner of the rectangular region
    that the current glyph is contained in.

    @param glyphSet  the GlyphSet to the Glyph to.
    @param ch        the character (as an int) the glyph represents.
    @param bytes     the '.glf' file bytes with info on the glyph.
    @param bitmapWidth  the width of the bitmap in pixels.
    @param bitmapHeight the height of the bitmap in pixels.
    ***************************************************************************/
    private void addGlyph(GlyphSet glyphSet, int ch, byte[] bytes, float bitmapWidth, float bitmapHeight) {
        float x1 = getFloat(bytes, GLF2Header.X1_INDEX), y1 = getFloat(bytes, GLF2Header.Y1_INDEX);
        float x2 = getFloat(bytes, GLF2Header.X2_INDEX), y2 = getFloat(bytes, GLF2Header.Y2_INDEX);
        glyphSet.addGlyph(new Glyph((char) ch + "", (int) (x1 * bitmapWidth), (int) (y1 * bitmapHeight), (int) (x2 * bitmapWidth), (int) (y2 * bitmapHeight), GLF2Header.BYTES_PER_PIXEL));
    }

    /***************************************************************************
    Reads the bitmap region of the '.glf' file and then transfers
    the bytes to the Glyph objects.

    <br/><br/>
    This method is called by readFile() after the header and
    glyph-coordinates regions of the file have been read.

    @param glyphSet  The GlyphSet to add Glyph objects to.
    @param input     the input stream to read bytes from.
    @param filename  the name of the '.glf' file.
    @throws GlyphException  if the file does not have enough bytes.
    @throws IOException  if an IO error occurs while reading the file.
    ***************************************************************************/
    private void processBitmap(GlyphSet glyphSet, GLF2Header header, BufferedInputStream input, String filename) throws GlyphException, IOException {
        int width = header.getBitmapWidth() * 2, height = header.getBitmapHeight();
        byte[][] bitmap = new byte[height][width];
        for (int row = 0; row < height; ++row) {
            if (input.read(bitmap[row]) != width) {
                throw new GlyphException("File " + filename + "is not a readable '.glf' file.");
            }
        }
        Iterator<Glyph> iter = glyphSet.iteratorGlyphs();
        while (iter.hasNext()) {
            iter.next().extractGlyphImage(bitmap);
        }
    }

    /***************************************************************************
    This helper method for createGlyphSet() obtains the filename from
    the Properties object.

    @param p  the Properties hash.
    @throws GlyphException  if the attribute cannot be found.
    ***************************************************************************/
    private String getFilename(Properties p) throws GlyphConfigException {
        return getAttribute(p, "fontDirectory") + getAttribute(p, "filename");
    }

    /***************************************************************************
    This helper method for createGlyphSet() obtains the fontname from
    the Properties object.

    @param p  the Properties hash.
    @throws GlyphException  if the attribute cannot be found.
    ***************************************************************************/
    private String getFontname(Properties p) throws GlyphConfigException {
        return getAttribute(p, "fontname");
    }

    /***************************************************************************
    This helper method for createGlyphSet() obtains the typeface from
    the Properties object.

    @param p  the Properties hash.
    @throws GlyphException  if the attribute cannot be found.
    ***************************************************************************/
    private String getTypeface(Properties p) throws GlyphConfigException {
        return getAttribute(p, "typeface");
    }

    /***************************************************************************
    This helper method for createGlyphSet() obtains the fontSize from
    the Properties object.

    @param p  the Properties hash.
    @throws GlyphException  if the attribute cannot be found or is
                            not a number of 4 or greater.
    ***************************************************************************/
    private int getFontSize(Properties p) throws GlyphConfigException {
        String fontSize = getAttribute(p, "fontSize");
        try {
            int size = Integer.parseInt(fontSize);
            if (size < 4) {
                String config = p.getProperty("configFilename");
                throw new GlyphConfigException(config, "A font size cannot be less than 4.");
            }
            return size;
        } catch (NumberFormatException e) {
            String config = p.getProperty("configFilename");
            throw new GlyphConfigException(config, "The fontSize is not a number.");
        }
    }

    /***************************************************************************
    Obtains the requested attribute from the Properties object (or
    throws an exception if it cannot be found).

    @param p  the Properties hash.
    @param attribute  the name of the attribute to look for.
    @throws GlyphException  if the attribute cannot be found.
    ***************************************************************************/
    private String getAttribute(Properties p, String attribute) throws GlyphConfigException {
        String s = p.getProperty(attribute);
        if (s == null || s.length() < 1) {
            String config = p.getProperty("configFilename");
            throw new GlyphConfigException(config, "missing " + s);
        }
        return s;
    }

    /***************************************************************************
    Converts 4 bytes into an integer by using the bitshift operator.
    The conversion takes into account that '.glf' files use unsigned
    bytes (0 to 255) and little-endian byte order, but Java uses
    signed bytes (-128 to 127) and big-endian byte order.

    @param bytes  the array to read bytes from.
    @param startIndex  the index position of the first of the 4 bytes.
    @return  The integer created by packing the 4 bytes into an int.
    ***************************************************************************/
    private int getInt(byte[] bytes, int startIndex) {
        int x0 = startIndex, x1 = startIndex + 1, x2 = startIndex + 2, x3 = startIndex + 3;
        int b0 = (bytes[x0] < 0) ? bytes[x0] + 256 : bytes[x0], b1 = (bytes[x1] < 0) ? bytes[x1] + 256 : bytes[x1], b2 = (bytes[x2] < 0) ? bytes[x2] + 256 : bytes[x2], b3 = (bytes[x3] < 0) ? bytes[x3] + 256 : bytes[x3];
        int i = b3;
        i = (i << 8) | b2;
        i = (i << 8) | b1;
        i = (i << 8) | b0;
        return i;
    }

    /***************************************************************************
    Converts 4 bytes into a float by first using getInt() of this
    class and then the intBitsToFloat( int bits ) method of class
    Float.

    @param bytes  the array to read bytes from.
    @param startIndex  the index position of the first of the 4 bytes.
    @param  The float created from 4 bytes.
    ***************************************************************************/
    private float getFloat(byte[] bytes, int startIndex) {
        int bits = getInt(bytes, startIndex);
        return Float.intBitsToFloat(bits);
    }

    /***************************************************************************
    Prints the data from the header if the '.glf' file.

    @param header  the header to print.
    ***************************************************************************/
    private void debugPrint(GLF2Header header) {
        System.out.println("\nFile " + header.getFilename() + ":\n\n" + "bitmap width  = " + header.getBitmapWidth() + "\n" + "bitmap height = " + header.getBitmapHeight() + "\n" + "first char = " + header.getStartChar() + "\n" + "last char  = " + header.getEndChar() + "\n");
    }

    /***************************************************************************
    Prints info on a GlyphSet.

    @param glyphSet  the GlyphSet to print.
    ***************************************************************************/
    private void debugPrint(GlyphSet glyphSet) {
        Iterator<Glyph> iter = glyphSet.iteratorGlyphs();
        while (iter.hasNext()) {
            Glyph glyph = iter.next();
            System.out.println("\nGlyph: '" + glyph.getName() + "'\n" + "x1 = " + glyph.getX1() + "\n" + "y1 = " + glyph.getY1() + "\n" + "x2 = " + glyph.getX2() + "\n" + "y2 = " + glyph.getY2() + "\n" + "bytesPerPixel = " + glyph.getBytesPerPixel() + "\n");
            System.out.println("Bitmap:\n");
            byte[][] image = glyph.getImage();
            for (int row = image.length - 1; row >= 0; --row) {
                for (int col = 0; col < image[row].length; ++col) {
                    int b = image[row][col];
                    b = (b < 0) ? (b + 256) : b;
                    System.out.printf("%03d ", b);
                }
                System.out.println();
            }
            System.out.println();
        }
    }
}
