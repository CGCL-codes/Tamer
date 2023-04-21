package fr.michaelm.jump.drivers.dxf;

import java.io.RandomAccessFile;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Map;
import java.util.LinkedHashMap;

public class DxfTABLE_LAYER_ITEM extends DxfTABLE_ITEM {

    private int colorNumber;

    private String lineType;

    public DxfTABLE_LAYER_ITEM(String name, int flags) {
        super(name, flags);
        this.colorNumber = 0;
        this.lineType = "DEFAULT";
    }

    public DxfTABLE_LAYER_ITEM(String name, int flags, int colorNumber, String lineType) {
        super(name, flags);
        this.colorNumber = colorNumber;
        this.lineType = lineType;
    }

    public String getLineType() {
        return lineType;
    }

    public void setLineType(String lineType) {
        this.lineType = lineType;
    }

    public int getcolorNumber() {
        return colorNumber;
    }

    public void setColorNumber(int colorNumber) {
        this.colorNumber = colorNumber;
    }

    public static Map readTable(RandomAccessFile raf) throws IOException {
        DxfTABLE_LAYER_ITEM item = new DxfTABLE_LAYER_ITEM("DEFAULT", 0);
        Map table = new LinkedHashMap();
        try {
            DxfGroup group;
            while (null != (group = DxfGroup.readGroup(raf)) && !group.equals(ENDTAB)) {
                if (group.equals(LAYER)) {
                    item = new DxfTABLE_LAYER_ITEM("DEFAULT", 0);
                } else if (group.getCode() == 2) {
                    item.setName(group.getValue());
                    table.put(item.getName(), item);
                } else if (group.getCode() == 5) {
                } else if (group.getCode() == 100) {
                } else if (group.getCode() == 70) {
                    item.setFlags(group.getIntValue());
                } else if (group.getCode() == 62) {
                    item.setColorNumber(group.getIntValue());
                } else if (group.getCode() == 6) {
                    item.setLineType(group.getValue());
                } else {
                }
            }
        } catch (IOException ioe) {
            throw ioe;
        }
        return table;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer(super.toString());
        sb.append(DxfGroup.toString(62, colorNumber));
        sb.append(DxfGroup.toString(6, lineType));
        return sb.toString();
    }
}
