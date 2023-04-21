package swf9.types;

import java.util.ArrayList;
import swf9.types.SHAPERECORD;
import swf9.types.ENDSHAPERECORD;

public class SHAPE {

    public int NumFillBits;

    public int NumLineBits;

    public Object[] ShapeRecords;

    public int length = 0;

    public SHAPE() {
    }

    public SHAPE(byte[] data, int offset, Object type) {
        getNumFillLineBits(data, offset);
        ShapeRecords = getShapeRecords(data, offset + 1, type);
    }

    public int getNumFillBits(byte[] data, int offset) {
        return (data[offset] >> 4) & 0x0F;
    }

    public int getNumLineBits(byte[] data, int offset) {
        return data[offset] & 0x0F;
    }

    public void getNumFillLineBits(byte[] data, int offset) {
        NumFillBits = (data[offset] >> 4) & 0x0F;
        NumLineBits = data[offset] & 0x0F;
    }

    public Object[] getShapeRecords(byte[] data, int offset, Object type) {
        int o = offset;
        getNumFillLineBits(data, offset);
        offset++;
        ArrayList<SHAPERECORD> records = new ArrayList<SHAPERECORD>();
        int offsetBit = 0;
        while (true) {
            SHAPERECORD rec = new SHAPERECORD(data, offset, offsetBit);
            if (rec.isEndShapeRecord()) {
                records.add(new ENDSHAPERECORD());
                break;
            } else if (!rec.isEdgeShapeRecord()) {
                STYLECHANGERECORD styleChange = new STYLECHANGERECORD(data, offset, offsetBit, NumFillBits, NumLineBits, type);
                if (styleChange.StateNewStyles == 1) {
                    NumFillBits = styleChange.NumFillBits;
                    NumLineBits = styleChange.NumLineBits;
                }
                records.add(styleChange);
                offset = styleChange.nextByte;
                offsetBit = styleChange.nextBit;
            } else if (rec.isStraightEdgeRecord()) {
                STRAIGHTEDGERECORD straightEdge = new STRAIGHTEDGERECORD(data, offset, offsetBit);
                records.add(straightEdge);
                offset = straightEdge.nextByte;
                offsetBit = straightEdge.nextBit;
            } else if (rec.isCurvedEdgeRecord()) {
                CURVEDEDGERECORD curvedEdge = new CURVEDEDGERECORD(data, offset, offsetBit);
                records.add(curvedEdge);
                offset = curvedEdge.nextByte;
                offsetBit = curvedEdge.nextBit;
            }
        }
        length += offset - o;
        return records.toArray();
    }
}
