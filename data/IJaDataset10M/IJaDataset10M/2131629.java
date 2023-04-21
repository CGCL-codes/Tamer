package com.iver.cit.jdwglib.dwg.readers.objreaders.v1314;

import java.util.List;
import com.iver.cit.jdwglib.dwg.CorruptedDwgEntityException;
import com.iver.cit.jdwglib.dwg.DwgObject;
import com.iver.cit.jdwglib.dwg.DwgUtil;
import com.iver.cit.jdwglib.dwg.objects.DwgVertexPFace;

public class DwgVertexPFaceReader1314 extends AbstractDwg1314Reader {

    public void readSpecificObj(int[] data, int offset, DwgObject dwgObj) throws RuntimeException, CorruptedDwgEntityException {
        if (!(dwgObj instanceof DwgVertexPFace)) throw new RuntimeException("ArcReader 14 solo puede leer DwgVertexPFace");
        DwgVertexPFace v = (DwgVertexPFace) dwgObj;
        int bitPos = offset;
        bitPos = headTailReader.readObjectHeader(data, bitPos, v);
        List val = DwgUtil.getRawChar(data, bitPos);
        bitPos = ((Integer) val.get(0)).intValue();
        int flags = ((Integer) val.get(1)).intValue();
        v.setFlags(flags);
        val = DwgUtil.getBitDouble(data, bitPos);
        bitPos = ((Integer) val.get(0)).intValue();
        double x = ((Double) val.get(1)).doubleValue();
        val = DwgUtil.getBitDouble(data, bitPos);
        bitPos = ((Integer) val.get(0)).intValue();
        double y = ((Double) val.get(1)).doubleValue();
        val = DwgUtil.getBitDouble(data, bitPos);
        bitPos = ((Integer) val.get(0)).intValue();
        double z = ((Double) val.get(1)).doubleValue();
        v.setPoint(new double[] { x, y, z });
        bitPos = headTailReader.readObjectTailer(data, bitPos, v);
    }
}
