package com.iver.cit.jdwglib.dwg;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.util.Vector;
import com.iver.cit.jdwglib.dwg.objects.DwgArc;
import com.iver.cit.jdwglib.dwg.objects.DwgAttdef;
import com.iver.cit.jdwglib.dwg.objects.DwgAttrib;
import com.iver.cit.jdwglib.dwg.objects.DwgBlock;
import com.iver.cit.jdwglib.dwg.objects.DwgBlockControl;
import com.iver.cit.jdwglib.dwg.objects.DwgBlockHeader;
import com.iver.cit.jdwglib.dwg.objects.DwgCircle;
import com.iver.cit.jdwglib.dwg.objects.DwgEllipse;
import com.iver.cit.jdwglib.dwg.objects.DwgEndblk;
import com.iver.cit.jdwglib.dwg.objects.DwgInsert;
import com.iver.cit.jdwglib.dwg.objects.DwgLayer;
import com.iver.cit.jdwglib.dwg.objects.DwgLayerControl;
import com.iver.cit.jdwglib.dwg.objects.DwgLine;
import com.iver.cit.jdwglib.dwg.objects.DwgLinearDimension;
import com.iver.cit.jdwglib.dwg.objects.DwgLwPolyline;
import com.iver.cit.jdwglib.dwg.objects.DwgMText;
import com.iver.cit.jdwglib.dwg.objects.DwgPoint;
import com.iver.cit.jdwglib.dwg.objects.DwgPolyline2D;
import com.iver.cit.jdwglib.dwg.objects.DwgPolyline3D;
import com.iver.cit.jdwglib.dwg.objects.DwgSeqend;
import com.iver.cit.jdwglib.dwg.objects.DwgSolid;
import com.iver.cit.jdwglib.dwg.objects.DwgSpline;
import com.iver.cit.jdwglib.dwg.objects.DwgText;
import com.iver.cit.jdwglib.dwg.objects.DwgVertex2D;
import com.iver.cit.jdwglib.dwg.objects.DwgVertex3D;
import com.iver.cit.gvsig.fmap.drivers.dgn.ByteUtils;
import freenet.support.HexUtil;

/**
 * The DwgFileV15Reader reads the DWG version 15 format
 * 
 * @author jmorell
 */
public class DwgFileV15Reader extends DwgFileReader {

    private DwgFile dwgFile;

    /**
	 * Reads the DWG version 15 format
	 * 
	 * @param dwgFile Represents the DWG file that we want to read
     * @throws IOException When DWG file path is wrong
	 */
    public void read(DwgFile dwgFile) throws IOException {
        System.out.println("DwgFileV15Reader.read() executed ...");
        this.dwgFile = dwgFile;
        File f = new File(dwgFile.getFileName());
        FileInputStream fis = new FileInputStream(f);
        FileChannel fc = fis.getChannel();
        long s = fc.size();
        ByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, s);
        readDwgSectionOffsets(bb);
        try {
            readDwgObjectOffsets(bb);
        } catch (Exception e) {
            System.out.println("Error leyendo offsets y classes. Posible corrupci�n en" + "el DWG file ...");
        }
        long t1 = System.currentTimeMillis();
        readDwgObjects(bb);
        long t2 = System.currentTimeMillis();
        System.out.println("Tiempo empleado por readDwgObjects() = " + (t2 - t1));
    }

    private void readDwgSectionOffsets(ByteBuffer bb) {
        bb.position(19);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        short codePage = bb.getShort();
        int count = bb.getInt();
        for (int i = 0; i < count; i++) {
            byte rec = bb.get();
            int seek = bb.getInt();
            int size = bb.getInt();
            if (rec == 0) {
                dwgFile.addDwgSectionOffset("HEADERS", seek, size);
            } else if (rec == 1) {
                dwgFile.addDwgSectionOffset("CLASSES", seek, size);
            } else if (rec == 2) {
                dwgFile.addDwgSectionOffset("OBJECTS", seek, size);
            } else if (rec == 3) {
                dwgFile.addDwgSectionOffset("UNKNOWN", seek, size);
            } else if (rec == 4) {
                dwgFile.addDwgSectionOffset("R14DATA", seek, size);
            } else if (rec == 5) {
                dwgFile.addDwgSectionOffset("R14REC5", seek, size);
            } else {
                System.out.println("ERROR: C�digo de n�mero de registro no soportado: " + rec);
            }
        }
    }

    private void readDwgObjectOffsets(ByteBuffer bb) throws Exception {
        int offset = dwgFile.getDwgSectionOffset("OBJECTS");
        bb.position(offset);
        while (true) {
            bb.order(ByteOrder.BIG_ENDIAN);
            short size = bb.getShort();
            if (size == 2) break;
            bb.order(ByteOrder.LITTLE_ENDIAN);
            byte[] dataBytes = new byte[size];
            for (int i = 0; i < dataBytes.length; i++) {
                dataBytes[i] = bb.get();
            }
            int[] data = DwgUtil.bytesToMachineBytes(dataBytes);
            int lastHandle = 0;
            int lastLoc = 0;
            int bitPos = 0;
            int bitMax = (size - 2) * 8;
            while (bitPos < bitMax) {
                Vector v = DwgUtil.getModularChar(data, bitPos);
                bitPos = ((Integer) v.get(0)).intValue();
                lastHandle = lastHandle + ((Integer) v.get(1)).intValue();
                v = DwgUtil.getModularChar(data, bitPos);
                bitPos = ((Integer) v.get(0)).intValue();
                lastLoc = lastLoc + ((Integer) v.get(1)).intValue();
                dwgFile.addDwgObjectOffset(lastHandle, lastLoc);
            }
        }
    }

    private void readDwgClasses(ByteBuffer bb) throws Exception {
        int offset = dwgFile.getDwgSectionOffset("CLASSES");
        bb.position(offset + 16);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        int size = bb.getInt();
        byte[] dataBytes = new byte[size];
        for (int i = 0; i < dataBytes.length; i++) {
            dataBytes[i] = bb.get();
        }
        int[] data = DwgUtil.bytesToMachineBytes(dataBytes);
        for (int i = 0; i < data.length; i++) {
            data[i] = (byte) ByteUtils.getUnsigned((byte) data[i]);
        }
        bb.position(bb.position() + 2 + 16);
        int maxbit = size * 8;
        int bitPos = 0;
        while ((bitPos + 8) < maxbit) {
            Vector v = DwgUtil.getBitShort(data, bitPos);
            bitPos = ((Integer) v.get(0)).intValue();
            v = DwgUtil.getBitShort(data, bitPos);
            bitPos = ((Integer) v.get(0)).intValue();
            v = DwgUtil.getTextString(data, bitPos);
            bitPos = ((Integer) v.get(0)).intValue();
            v = DwgUtil.getTextString(data, bitPos);
            bitPos = ((Integer) v.get(0)).intValue();
            v = DwgUtil.getTextString(data, bitPos);
            bitPos = ((Integer) v.get(0)).intValue();
            v = DwgUtil.testBit(data, bitPos);
            bitPos = ((Integer) v.get(0)).intValue();
            v = DwgUtil.getBitShort(data, bitPos);
            bitPos = ((Integer) v.get(0)).intValue();
        }
    }

    private void readDwgObjects(ByteBuffer bb) {
        for (int i = 0; i < dwgFile.getDwgObjectOffsets().size(); i++) {
            DwgObjectOffset doo = (DwgObjectOffset) dwgFile.getDwgObjectOffsets().get(i);
            DwgObject obj = readDwgObject(bb, doo.getOffset());
            if (obj != null) {
                dwgFile.addDwgObject(obj);
            }
        }
    }

    private DwgObject readDwgObject(ByteBuffer bb, int offset) {
        try {
            bb.position(offset);
            int size = DwgUtil.getModularShort(bb);
            bb.order(ByteOrder.LITTLE_ENDIAN);
            byte[] dataBytes = new byte[size];
            String[] dataMachValString = new String[size];
            int[] data = new int[size];
            for (int i = 0; i < size; i++) {
                dataBytes[i] = bb.get();
                dataMachValString[i] = HexUtil.bytesToHex(new byte[] { dataBytes[i] });
                Integer dataMachValShort = Integer.decode("0x" + dataMachValString[i]);
                data[i] = dataMachValShort.byteValue();
                data[i] = ByteUtils.getUnsigned((byte) data[i]);
            }
            int bitPos = 0;
            Vector v = DwgUtil.getBitShort(data, bitPos);
            bitPos = ((Integer) v.get(0)).intValue();
            int type = ((Integer) v.get(1)).intValue();
            DwgObject obj = new DwgObject();
            if (type == 0x11) {
                obj = new DwgArc();
                obj.setGraphicsFlag(true);
            } else if (type == 0x12) {
                obj = new DwgCircle();
                obj.setGraphicsFlag(true);
            } else if (type == 0x13) {
                obj = new DwgLine();
                obj.setGraphicsFlag(true);
            } else if (type == 0x1B) {
                obj = new DwgPoint();
                obj.setGraphicsFlag(true);
            } else if (type == 0x0F) {
                obj = new DwgPolyline2D();
                obj.setGraphicsFlag(true);
            } else if (type == 0x10) {
                obj = new DwgPolyline3D();
                obj.setGraphicsFlag(true);
            } else if (type == 0x0A) {
                obj = new DwgVertex2D();
                obj.setGraphicsFlag(true);
            } else if (type == 0x0B) {
                obj = new DwgVertex3D();
                obj.setGraphicsFlag(true);
            } else if (type == 0x6) {
                obj = new DwgSeqend();
                obj.setGraphicsFlag(true);
            } else if (type == 0x1) {
                obj = new DwgText();
                obj.setGraphicsFlag(true);
            } else if (type == 0x2) {
                obj = new DwgAttrib();
                obj.setGraphicsFlag(true);
            } else if (type == 0x3) {
                obj = new DwgAttdef();
                obj.setGraphicsFlag(true);
            } else if (type == 0x4) {
                obj = new DwgBlock();
                obj.setGraphicsFlag(true);
            } else if (type == 0x5) {
                obj = new DwgEndblk();
                obj.setGraphicsFlag(true);
            } else if (type == 0x30) {
                obj = new DwgBlockControl();
                obj.setGraphicsFlag(false);
            } else if (type == 0x31) {
                obj = new DwgBlockHeader();
                obj.setGraphicsFlag(false);
            } else if (type == 0x32) {
                obj = new DwgLayerControl();
                obj.setGraphicsFlag(false);
            } else if (type == 0x33) {
                obj = new DwgLayer();
                obj.setGraphicsFlag(false);
            } else if (type == 0x7) {
                obj = new DwgInsert();
                obj.setGraphicsFlag(true);
            } else if (type == 0x2C) {
                obj = new DwgMText();
                obj.setGraphicsFlag(true);
            } else if (type == 0x1F) {
                obj = new DwgSolid();
                obj.setGraphicsFlag(true);
            } else if (type == 0x23) {
                obj = new DwgEllipse();
                obj.setGraphicsFlag(true);
            } else if (type == 0x24) {
                obj = new DwgSpline();
                obj.setGraphicsFlag(true);
            } else if (type == 0x15) {
                obj = new DwgLinearDimension();
                obj.setGraphicsFlag(true);
            } else if (type == 0x4D) {
                obj = new DwgLwPolyline();
                obj.setGraphicsFlag(true);
            } else if (type == 0x4E) {
                obj = new DwgLwPolyline();
                obj.setGraphicsFlag(true);
            } else if (type == 0x4F) {
                obj = new DwgLwPolyline();
                obj.setGraphicsFlag(true);
            } else if (type == 0x50) {
                obj = new DwgLwPolyline();
                obj.setGraphicsFlag(true);
            } else if (type == 0x51) {
                obj = new DwgLwPolyline();
                obj.setGraphicsFlag(true);
            } else if (type == 0x52) {
                obj = new DwgLwPolyline();
                obj.setGraphicsFlag(true);
            } else if (type == 0x53) {
                obj = new DwgLwPolyline();
                obj.setGraphicsFlag(true);
            } else {
            }
            obj.setType(type);
            v = DwgUtil.getRawLong(data, bitPos);
            bitPos = ((Integer) v.get(0)).intValue();
            int objBSize = ((Integer) v.get(1)).intValue();
            obj.setSizeInBits(objBSize);
            Vector entityHandle = new Vector();
            v = DwgUtil.getHandle(data, bitPos);
            bitPos = ((Integer) v.get(0)).intValue();
            for (int i = 1; i < v.size(); i++) {
                entityHandle.add(v.get(i));
            }
            obj.setHandle(DwgUtil.handleBinToHandleInt(entityHandle));
            v = DwgUtil.readExtendedData(data, bitPos);
            bitPos = ((Integer) v.get(0)).intValue();
            Vector extData = (Vector) v.get(1);
            obj.setExtendedData(extData);
            boolean gflag = false;
            gflag = obj.isGraphicsFlag();
            if (gflag) {
                v = DwgUtil.testBit(data, bitPos);
                bitPos = ((Integer) v.get(0)).intValue();
                boolean val = ((Boolean) v.get(1)).booleanValue();
                if (val) {
                    v = DwgUtil.getRawLong(data, bitPos);
                    bitPos = ((Integer) v.get(0)).intValue();
                    size = ((Integer) v.get(1)).intValue();
                    int bgSize = size * 8;
                    Integer giData = (Integer) DwgUtil.getBits(data, bgSize, bitPos);
                    obj.setGraphicData(giData.intValue());
                    bitPos = bitPos + bgSize;
                }
            }
            readSpecificObject(obj, data, bitPos);
            return obj;
        } catch (Exception e) {
            System.out.println("Exception capturada. Probablemente se ha encontrado un" + "objeto con type non fixed");
            return null;
        }
    }

    private void readSpecificObject(DwgObject obj, int[] data, int bitPos) throws Exception {
        if (obj.getType() == 0x11) {
            ((DwgArc) obj).readDwgArcV15(data, bitPos);
        } else if (obj.getType() == 0x12) {
            ((DwgCircle) obj).readDwgCircleV15(data, bitPos);
        } else if (obj.getType() == 0x13) {
            ((DwgLine) obj).readDwgLineV15(data, bitPos);
        } else if (obj.getType() == 0x1B) {
            ((DwgPoint) obj).readDwgPointV15(data, bitPos);
        } else if (obj.getType() == 0x0F) {
            ((DwgPolyline2D) obj).readDwgPolyline2DV15(data, bitPos);
        } else if (obj.getType() == 0x10) {
            ((DwgPolyline3D) obj).readDwgPolyline3DV15(data, bitPos);
        } else if (obj.getType() == 0x0A) {
            ((DwgVertex2D) obj).readDwgVertex2DV15(data, bitPos);
        } else if (obj.getType() == 0x0B) {
            ((DwgVertex3D) obj).readDwgVertex3DV15(data, bitPos);
        } else if (obj.getType() == 0x6) {
            ((DwgSeqend) obj).readDwgSeqendV15(data, bitPos);
        } else if (obj.getType() == 0x1) {
            ((DwgText) obj).readDwgTextV15(data, bitPos);
        } else if (obj.getType() == 0x2) {
            ((DwgAttrib) obj).readDwgAttribV15(data, bitPos);
        } else if (obj.getType() == 0x3) {
            ((DwgAttdef) obj).readDwgAttdefV15(data, bitPos);
        } else if (obj.getType() == 0x4) {
            ((DwgBlock) obj).readDwgBlockV15(data, bitPos);
        } else if (obj.getType() == 0x5) {
            ((DwgEndblk) obj).readDwgEndblkV15(data, bitPos);
        } else if (obj.getType() == 0x30) {
            ((DwgBlockControl) obj).readDwgBlockControlV15(data, bitPos);
        } else if (obj.getType() == 0x31) {
            ((DwgBlockHeader) obj).readDwgBlockHeaderV15(data, bitPos);
        } else if (obj.getType() == 0x32) {
            ((DwgLayerControl) obj).readDwgLayerControlV15(data, bitPos);
        } else if (obj.getType() == 0x33) {
            ((DwgLayer) obj).readDwgLayerV15(data, bitPos);
        } else if (obj.getType() == 0x7) {
            ((DwgInsert) obj).readDwgInsertV15(data, bitPos);
        } else if (obj.getType() == 0x2C) {
            ((DwgMText) obj).readDwgMTextV15(data, bitPos);
        } else if (obj.getType() == 0x1F) {
            ((DwgSolid) obj).readDwgSolidV15(data, bitPos);
        } else if (obj.getType() == 0x23) {
            ((DwgEllipse) obj).readDwgEllipseV15(data, bitPos);
        } else if (obj.getType() == 0x24) {
            ((DwgSpline) obj).readDwgSplineV15(data, bitPos);
        } else if (obj.getType() == 0x14) {
        } else if (obj.getType() == 0x15) {
        } else if (obj.getType() == 0x16) {
        } else if (obj.getType() == 0x17) {
        } else if (obj.getType() == 0x18) {
        } else if (obj.getType() == 0x19) {
        } else if (obj.getType() == 0x1A) {
        } else if (obj.getType() == 0x4D) {
            ((DwgLwPolyline) obj).readDwgLwPolylineV15(data, bitPos);
        } else if (obj.getType() == 0x4E) {
            ((DwgLwPolyline) obj).readDwgLwPolylineV15(data, bitPos);
        } else if (obj.getType() == 0x4F) {
            ((DwgLwPolyline) obj).readDwgLwPolylineV15(data, bitPos);
        } else if (obj.getType() == 0x50) {
            ((DwgLwPolyline) obj).readDwgLwPolylineV15(data, bitPos);
        } else if (obj.getType() == 0x51) {
            ((DwgLwPolyline) obj).readDwgLwPolylineV15(data, bitPos);
        } else if (obj.getType() == 0x52) {
            ((DwgLwPolyline) obj).readDwgLwPolylineV15(data, bitPos);
        } else if (obj.getType() == 0x53) {
            ((DwgLwPolyline) obj).readDwgLwPolylineV15(data, bitPos);
        } else {
        }
    }
}
