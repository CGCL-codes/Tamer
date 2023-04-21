package src.backend.shapes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import src.backend.MapData;
import src.backend.StaticFunctions;
import src.backend.WrkArrayReader;
import src.backend.exception.InvalidDataException;
import src.backend.shapes.header.CollectionHeader;
import src.backend.wad.WadTool;

public class ShapesData extends MapData {

    public static final int MAXIMUM_COLLECTIONS = 1 << WadTool.DESCRIPTOR_COLLECTION_BITS;

    public static final int MAXIMUM_SHAPES_PER_COLLECTION = 1 << WadTool.DESCRIPTOR_SHAPE_BITS;

    public static final int MAXIMUM_CLUTS_PER_COLLECTION = 1 << WadTool.DESCRIPTOR_CLUT_BITS;

    private List<CollectionHeader> collectionHeaderList = new ArrayList<CollectionHeader>();

    ShapesWriter shapesIO;

    public ShapesData() {
        for (int i = 0; i < MAXIMUM_COLLECTIONS; i++) {
            collectionHeaderList.add(new CollectionHeader());
        }
    }

    public CollectionHeader getHeader(int index) {
        if (index < 0 || index >= this.collectionHeaderList.size()) {
            throw new ArrayIndexOutOfBoundsException();
        } else {
            return collectionHeaderList.get(index);
        }
    }

    public void setHeader(int index, CollectionHeader header) {
        if (index < 0 || index >= this.collectionHeaderList.size()) {
            throw new ArrayIndexOutOfBoundsException();
        }
        this.collectionHeaderList.set(index, header);
    }

    public void load(String path) throws IOException, InvalidDataException {
        System.out.println("Loading Shapes...");
        shapesIO = new ShapesWriter(path);
        if (shapesIO.getLength() < MAXIMUM_COLLECTIONS * CollectionHeader.HEADER_LENGTH) {
            JOptionPane.showMessageDialog(null, "File too small to be a Marathon Shapes file.");
            throw new IOException();
        }
        for (int i = 0; i < MAXIMUM_COLLECTIONS; i++) {
            CollectionHeader header = new CollectionHeader();
            byte data[] = new byte[CollectionHeader.HEADER_LENGTH];
            shapesIO.seek(i * CollectionHeader.HEADER_LENGTH);
            shapesIO.read(data);
            WrkArrayReader wrk = new WrkArrayReader(data);
            header.read(wrk, shapesIO);
            this.setHeader(i, header);
        }
    }
}
