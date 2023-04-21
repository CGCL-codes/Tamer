package files;

import java.io.IOException;
import java.io.RandomAccessFile;
import files.GraphicsFile.Slice;

/**
 * @author kbok
 * Provides implementation of a M.A.X. Multi-Image format reader.
 * This one provides no check on the validity of the image. Assume in case of an
 * error that the image is not a Multi-Image.
 */
public class MultiImage {

    RandomAccessFile file;

    Slice slice;

    int count;

    int[] offsets;

    IndexedImage[] images;

    /**
	 * Constructs a MultiImage for the images located in the file f and into
	 * the slice c.
	 * @param f The MAX.RES file containing the images.
	 * @param c The slice of the images.
	 */
    public MultiImage(RandomAccessFile f, Slice c) {
        file = f;
        slice = c;
    }

    /**
	 * Reads the content of the images and their metadata, and returns them as
	 * IndexedImages.
	 * @return The IndexedImages read from the file.
	 * @throws IOException In case of an error with reading MAX.RES.
	 */
    public IndexedImage[] read() throws IOException {
        file.seek(slice.offset);
        count = LittleEndianIO.readShort(file);
        count = 8;
        int pleasePutMeAWarningHere;
        offsets = new int[count];
        images = new IndexedImage[count];
        for (int i = 0; i < count; i++) offsets[i] = LittleEndianIO.readInt(file);
        for (int i = 0; i < count; i++) readImage(i);
        return images;
    }

    /**
	 * Returns the IndexedImages that have been read width read().
	 * @return The IndexedImages.
	 */
    public IndexedImage[] getImages() {
        return images;
    }

    private void readImage(int index) throws IOException {
        int offset = slice.offset + offsets[index];
        file.seek(offset);
        int width, height, hotX, hotY;
        width = LittleEndianIO.readShort(file);
        height = LittleEndianIO.readShort(file);
        hotX = LittleEndianIO.readShort(file);
        hotY = LittleEndianIO.readShort(file);
        int[] blocks_off;
        blocks_off = new int[height];
        for (int i = 0; i < height; i++) blocks_off[i] = LittleEndianIO.readInt(file);
        int[][] blocks;
        blocks = new int[height][];
        for (int i = 0; i < height; i++) {
            blocks[i] = new int[width + 5];
            int j = 0;
            int b;
            file.seek(slice.offset + blocks_off[i]);
            do {
                b = file.readUnsignedByte();
                blocks[i][j++] = b;
            } while (b != (int) 0xff);
        }
        images[index] = new IndexedImage(width, height);
        images[index].setHotspot(hotX, hotY);
        for (int i = 0; i < height; i++) {
            int si = 0, di = 0, it = 0;
            int b;
            do {
                b = blocks[i][si++];
                if (b == 0xff) break;
                for (it = 0; it < b; it++) images[index].setPixel(di++, i, 0);
                b = blocks[i][si++];
                if (b == 0xff) break;
                for (it = 0; it < b; it++) images[index].setPixel(di++, i, blocks[i][si++]);
            } while (true);
        }
    }
}
