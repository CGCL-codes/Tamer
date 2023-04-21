package demos.nehe.lesson33;

import com.sun.opengl.util.BufferUtil;
import demos.common.ResourceRetriever;
import javax.media.opengl.GL;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

class TGALoader {

    private static final ByteBuffer uTGAcompare;

    private static final ByteBuffer cTGAcompare;

    static {
        byte[] uncompressedTgaHeader = new byte[] { 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        byte[] compressedTgaHeader = new byte[] { 0, 0, 10, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        uTGAcompare = BufferUtil.newByteBuffer(uncompressedTgaHeader.length);
        uTGAcompare.put(uncompressedTgaHeader);
        uTGAcompare.flip();
        cTGAcompare = BufferUtil.newByteBuffer(compressedTgaHeader.length);
        cTGAcompare.put(compressedTgaHeader);
        cTGAcompare.flip();
    }

    public static void loadTGA(Texture texture, String filename) throws IOException {
        ByteBuffer header = BufferUtil.newByteBuffer(12);
        ReadableByteChannel in = Channels.newChannel(ResourceRetriever.getResourceAsStream(filename));
        readBuffer(in, header);
        if (uTGAcompare.equals(header)) {
            loadUncompressedTGA(texture, in);
        } else if (cTGAcompare.equals(header)) {
            loadCompressedTGA(texture, in);
        } else {
            in.close();
            throw new IOException("TGA file be type 2 or type 10 ");
        }
    }

    private static void readBuffer(ReadableByteChannel in, ByteBuffer buffer) throws IOException {
        while (buffer.hasRemaining()) {
            in.read(buffer);
        }
        buffer.flip();
    }

    private static void loadUncompressedTGA(Texture texture, ReadableByteChannel in) throws IOException {
        TGA tga = new TGA();
        readBuffer(in, tga.header);
        texture.width = (unsignedByteToInt(tga.header.get(1)) << 8) + unsignedByteToInt(tga.header.get(0));
        texture.height = (unsignedByteToInt(tga.header.get(3)) << 8) + unsignedByteToInt(tga.header.get(2));
        texture.bpp = unsignedByteToInt(tga.header.get(4));
        tga.width = texture.width;
        tga.height = texture.height;
        tga.bpp = texture.bpp;
        if ((texture.width <= 0) || (texture.height <= 0) || ((texture.bpp != 24) && (texture.bpp != 32))) {
            throw new IOException("Invalid texture information");
        }
        if (texture.bpp == 24) texture.type = GL.GL_RGB; else texture.type = GL.GL_RGBA;
        tga.bytesPerPixel = (tga.bpp / 8);
        tga.imageSize = (tga.bytesPerPixel * tga.width * tga.height);
        texture.imageData = BufferUtil.newByteBuffer(tga.imageSize);
        readBuffer(in, texture.imageData);
        for (int cswap = 0; cswap < tga.imageSize; cswap += tga.bytesPerPixel) {
            byte temp = texture.imageData.get(cswap);
            texture.imageData.put(cswap, texture.imageData.get(cswap + 2));
            texture.imageData.put(cswap + 2, temp);
        }
    }

    private static void loadCompressedTGA(Texture texture, ReadableByteChannel fTGA) throws IOException {
        TGA tga = new TGA();
        readBuffer(fTGA, tga.header);
        texture.width = (unsignedByteToInt(tga.header.get(1)) << 8) + unsignedByteToInt(tga.header.get(0));
        texture.height = (unsignedByteToInt(tga.header.get(3)) << 8) + unsignedByteToInt(tga.header.get(2));
        texture.bpp = unsignedByteToInt(tga.header.get(4));
        tga.width = texture.width;
        tga.height = texture.height;
        tga.bpp = texture.bpp;
        if ((texture.width <= 0) || (texture.height <= 0) || ((texture.bpp != 24) && (texture.bpp != 32))) {
            throw new IOException("Invalid texture information");
        }
        if (texture.bpp == 24) texture.type = GL.GL_RGB; else texture.type = GL.GL_RGBA;
        tga.bytesPerPixel = (tga.bpp / 8);
        tga.imageSize = (tga.bytesPerPixel * tga.width * tga.height);
        texture.imageData = BufferUtil.newByteBuffer(tga.imageSize);
        texture.imageData.position(0);
        texture.imageData.limit(texture.imageData.capacity());
        int pixelcount = tga.height * tga.width;
        int currentpixel = 0;
        int currentbyte = 0;
        ByteBuffer colorbuffer = BufferUtil.newByteBuffer(tga.bytesPerPixel);
        do {
            int chunkheader;
            try {
                ByteBuffer chunkHeaderBuffer = ByteBuffer.allocate(1);
                fTGA.read(chunkHeaderBuffer);
                chunkHeaderBuffer.flip();
                chunkheader = unsignedByteToInt(chunkHeaderBuffer.get());
            } catch (IOException e) {
                throw new IOException("Could not read RLE header");
            }
            if (chunkheader < 128) {
                chunkheader++;
                for (short counter = 0; counter < chunkheader; counter++) {
                    readBuffer(fTGA, colorbuffer);
                    texture.imageData.put(currentbyte, colorbuffer.get(2));
                    texture.imageData.put(currentbyte + 1, colorbuffer.get(1));
                    texture.imageData.put(currentbyte + 2, colorbuffer.get(0));
                    if (tga.bytesPerPixel == 4) {
                        texture.imageData.put(currentbyte + 3, colorbuffer.get(3));
                    }
                    currentbyte += tga.bytesPerPixel;
                    currentpixel++;
                    if (currentpixel > pixelcount) {
                        throw new IOException("Too many pixels read");
                    }
                }
            } else {
                chunkheader -= 127;
                readBuffer(fTGA, colorbuffer);
                for (short counter = 0; counter < chunkheader; counter++) {
                    texture.imageData.put(currentbyte, colorbuffer.get(2));
                    texture.imageData.put(currentbyte + 1, colorbuffer.get(1));
                    texture.imageData.put(currentbyte + 2, colorbuffer.get(0));
                    if (tga.bytesPerPixel == 4) {
                        texture.imageData.put(currentbyte + 3, colorbuffer.get(3));
                    }
                    currentbyte += tga.bytesPerPixel;
                    currentpixel++;
                    if (currentpixel > pixelcount) {
                        throw new IOException("Too many pixels read");
                    }
                }
            }
        } while (currentpixel < pixelcount);
    }

    private static int unsignedByteToInt(byte b) {
        return (int) b & 0xFF;
    }
}
