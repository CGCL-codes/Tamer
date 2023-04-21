package com.flagstone.transform;

import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

/**
FSDefineImage is used to define an image compressed using the lossless zlib compression algorithm.

<p>The class supports colour-mapped images where the image data contains an index into a colour table or images where the image data specifies the colour directly.</p>

<table class="datasheet">

<tr><th align="left" colspan="2">Attributes</th></tr>

<tr><td><a name="FSDefineImage_0">type</a></td>
<td>Identifies the data structure when it is encoded. Read-only.</td>
</tr>

<tr>
<td><a name="FSDefineImage_1">identifier</a></td>
<td>A unique identifier, in the range 1..65535, that is used to reference the image from other objects.</td>
</tr>

<tr>
<td><a name="FSDefineImage_2">width</a></td>
<td>Width of the image in pixels, NOT twips. 1 pixel = 20 twips.</td>
</tr>

<tr>
<td><a name="FSDefineImage_3">height</a></td>
<td>Height of the image in pixels, NOT twips.  1 pixel = 20 twips.</td>
</tr>

<tr>
<td><a name="FSDefineImage_4">pixelSize</a></td>
<td>The number of bits per pixel, either 16 or 24.</td>
</tr>

<tr>
<td><a name="FSDefineImage_4">tableSize</a></td>
<td>The number of entries in the colour table - which is compressed as part of the image. Each entry in the colour table contains one byte each for the red, green and blue colour channels. The colour table is only used for indexed colour images. The table is not used when the colour is specified directly in each pixel.</td>
</tr>

<tr>
<td><a name="FSDefineImage_5">compressedData</a></td>
<td>An array of bytes containing the zlib compressed colour table and image.</td>
</tr>

</table>

<p>For colour-mapped images the colour table contains up to 256, 24-bit colours. The image contains one byte for each pixel which is an index into the table to specify the colour for that pixel. The colour table and the image data are compressed as a single block, with the colour table placed before the image.</p>

<p>For images where the colour is specified directly, the image data contains either 16 or 24 bit colour values. For 16-bit colour values the most significant bit is zero followed by three, 5-bit fields for the red, green and blue channels:</p>

<pre>
            +-+--------+--------+--------+
            |0|   Red  |  Green |  Blue  |
            +-+--------+--------+--------+
           15                            0
</pre>

<p>Four bytes are used to represent 24-bit colours. The first byte is always set to zero and the following bytes contain the colour values for the red, green and blue colour channels.</p>

<p>The number of bytes in each row of an image must be aligned to a 32-bit word boundary. For example if an image if an icon is 25 pixels wide, then for an 8-bit colour mapped image an additional three bytes (0x00) must be used to pad each row; for a 16-bit direct mapped colour image an additional two bytes must be used as padding.</p>

<p>The image data is stored in zlib compressed form within the object. For colour-mapped images the compressed data contains the colour table followed by the image data. The colour table is omitted for direct-mapped images.</p>

<h1 class="datasheet">History</h1>

<p>The FSDefineImage class represents the DefineBitsLossless tag from the Macromedia Flash (SWF) File Format Specification. It was introduced in Flash 2.</p>
 */
public class FSDefineImage extends FSDefineObject {

    private int width = 0;

    private int height = 0;

    private int pixelSize = 8;

    private int tableSize = 0;

    private byte[] compressedData = null;

    /**
     * Construct an FSDefineImage object, initalizing it with values decoded
     * from an encoded object.
     *
     * @param coder an FSCoder containing the binary data.
     */
    public FSDefineImage(FSCoder coder) {
        super(DefineImage, 0);
        extendLength = true;
        decode(coder);
    }

    /** Constructs an FSDefineImage object defining a colour-mapped image.

        @param anIdentifier    the unique identifier for this object
        @param width the width of the image.
        @param height the height of the image.
        @param tableSize the number of entries in the colour table in the compressed data. Each entry is 24 bits.
        @param compressedBytes the zlib compressed colour table and image data.
        */
    public FSDefineImage(int anIdentifier, int width, int height, int tableSize, byte[] compressedBytes) {
        super(DefineImage, anIdentifier);
        extendLength = true;
        setWidth(width);
        setHeight(height);
        setPixelSize(8);
        setTableSize(tableSize);
        setCompressedData(compressedBytes);
    }

    /** Constructs an FSDefineImage object defining an image that specifies the colour directly.

        @param anIdentifier    the unique identifier for this object
        @param width the width of the image.
        @param height the height of the image.
        @param compressedBytes the zlib compressed image data.
        @param aPixelSize the size of each pixel, either 16 or 24 bits.
        */
    public FSDefineImage(int anIdentifier, int width, int height, byte[] compressedBytes, int aPixelSize) {
        super(DefineImage, anIdentifier);
        extendLength = true;
        setWidth(width);
        setHeight(height);
        setPixelSize(aPixelSize);
        setTableSize(0);
        setCompressedData(compressedBytes);
    }

    /**
     * Constructs an FSDefineImage object by copying values from an existing
     * object.
     *
     * @param obj an FSDefineImage object.
     */
    public FSDefineImage(FSDefineImage obj) {
        super(obj);
        width = obj.width;
        height = obj.height;
        pixelSize = obj.pixelSize;
        tableSize = obj.tableSize;
        compressedData = Transform.clone(obj.compressedData);
    }

    /** Gets the width of the image.

        @return the width of the image.
        */
    public int getWidth() {
        return width;
    }

    /** Gets the height of the image.

        @return the height of the image in pixels.
        */
    public int getHeight() {
        return height;
    }

    /** Gets the number of bits used to represent each pixel. Either 8, 16 or 24 bits. The pixel size is 8-bits for colour-mapped images and 16 or 24 bits for images where the colour is specified directly.

        @return the number of bits per pixel: 8, 16 or 24.
        */
    public int getPixelSize() {
        return pixelSize;
    }

    /** Gets the number of entries in the colour table encoded the compressed image. For images where the colour is specified directly in the image then the table size is zero.

        @return the number of entries in the colour table.
        */
    public int getTableSize() {
        return tableSize;
    }

    /** Gets the compressed colour table and image.

        @return an array of bytes containing the compressed colour table and image.
        */
    public byte[] getCompressedData() {
        return compressedData;
    }

    /** Sets the width of the image

        @param aNumber the width of the image.
        */
    public void setWidth(int aNumber) {
        width = aNumber;
    }

    /** Sets the height of the image.

        @param aNumber the height of the image in pixels.
        */
    public void setHeight(int aNumber) {
        height = aNumber;
    }

    /** Sets the size of the pixel in bits: 8, 16 or 32. The pixel size is 8-bits for colour-mapped images and 16 or 24 bits for images where the colour is specified directly.

        @param aNumber the size of each pixel in bits: 8, 16 or 24.
        */
    public void setPixelSize(int aNumber) {
        pixelSize = aNumber;
    }

    /** Sets the number of entries in the colour table in the compressed image. For images where the colour is specified directly in the image then the table size should be zero.

        @param aNumber the number of entries in the colour table in the compressed image.
        */
    public void setTableSize(int aNumber) {
        tableSize = aNumber;
    }

    /** Sets the compressed image data using compressed data.

        @param bytes byte array containing zlib compressed colour table and image.
        */
    public void setCompressedData(byte[] bytes) {
        compressedData = bytes;
    }

    public Object clone() {
        FSDefineImage anObject = (FSDefineImage) super.clone();
        anObject.compressedData = Transform.clone(compressedData);
        return anObject;
    }

    public boolean equals(Object anObject) {
        boolean result = false;
        if (super.equals(anObject)) {
            FSDefineImage typedObject = (FSDefineImage) anObject;
            result = pixelSize == typedObject.pixelSize;
            result = result && width == typedObject.width;
            result = result && height == typedObject.height;
            result = result && tableSize == typedObject.tableSize;
            try {
                result = result && Transform.equals(unzip(compressedData), unzip(typedObject.compressedData));
            } catch (DataFormatException e) {
                result = false;
            }
        }
        return result;
    }

    private byte[] unzip(byte[] bytes) throws DataFormatException {
        byte[] data = new byte[width * height * 8];
        int count = 0;
        Inflater inflater = new Inflater();
        inflater.setInput(bytes);
        count = inflater.inflate(data);
        byte[] uncompressedData = new byte[count];
        System.arraycopy(data, 0, uncompressedData, 0, count);
        return uncompressedData;
    }

    public void appendDescription(StringBuffer buffer, int depth) {
        buffer.append(name());
        if (depth > 0) {
            buffer.append(": { ");
            Transform.append(buffer, "pixelSize", pixelSize);
            Transform.append(buffer, "width", width);
            Transform.append(buffer, "height", height);
            Transform.append(buffer, "tableSize", tableSize);
            Transform.append(buffer, "compressedData", "<data>");
            buffer.append("}");
        }
    }

    public int length(FSCoder coder) {
        super.length(coder);
        length += 5;
        length += (pixelSize == 8) ? 1 : 0;
        length += compressedData.length;
        return length;
    }

    public void encode(FSCoder coder) {
        coder.beginObject(name());
        super.encode(coder);
        switch(pixelSize) {
            case 8:
                coder.writeWord(3, 1);
                break;
            case 16:
                coder.writeWord(4, 1);
                break;
            case 24:
                coder.writeWord(5, 1);
                break;
        }
        coder.writeWord(width, 2);
        coder.writeWord(height, 2);
        if (pixelSize == 8) coder.writeWord(tableSize - 1, 1);
        coder.writeBytes(compressedData);
        coder.endObject(name());
    }

    public void decode(FSCoder coder) {
        super.decode(coder);
        switch(coder.readWord(1, false)) {
            case 3:
                pixelSize = 8;
                break;
            case 4:
                pixelSize = 16;
                break;
            case 5:
                pixelSize = 24;
                break;
        }
        width = coder.readWord(2, false);
        height = coder.readWord(2, false);
        if (pixelSize == 8) {
            tableSize = coder.readWord(1, false) + 1;
            compressedData = new byte[length - 8];
            coder.readBytes(compressedData);
        } else {
            compressedData = new byte[length - 7];
            coder.readBytes(compressedData);
        }
        coder.endObject(name());
    }
}
