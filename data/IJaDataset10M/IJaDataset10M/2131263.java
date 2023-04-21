package com.iver.cit.jdwglib.dwg.readers.objreaders.v1314;

import java.util.List;
import com.iver.cit.jdwglib.dwg.CorruptedDwgEntityException;
import com.iver.cit.jdwglib.dwg.DwgObject;
import com.iver.cit.jdwglib.dwg.DwgUtil;
import com.iver.cit.jdwglib.dwg.objects.DwgVertexPFaceFace;

public class DwgVertexPFaceFaceReader1314 extends AbstractDwg1314Reader {

    public void readSpecificObj(int[] data, int offset, DwgObject dwgObj) throws RuntimeException, CorruptedDwgEntityException {
        if (!(dwgObj instanceof DwgVertexPFaceFace)) throw new RuntimeException("ArcReader 14 solo puede leer DwgVertexPFaceFace");
        DwgVertexPFaceFace v = (DwgVertexPFaceFace) dwgObj;
        int bitPos = offset;
        bitPos = headTailReader.readObjectHeader(data, offset, v);
        List val = DwgUtil.getBitShort(data, bitPos);
        bitPos = ((Integer) val.get(0)).intValue();
        int v1 = ((Integer) val.get(1)).intValue();
        val = DwgUtil.getBitShort(data, bitPos);
        bitPos = ((Integer) val.get(0)).intValue();
        int v2 = ((Integer) val.get(1)).intValue();
        val = DwgUtil.getBitShort(data, bitPos);
        bitPos = ((Integer) val.get(0)).intValue();
        int v3 = ((Integer) val.get(1)).intValue();
        val = DwgUtil.getBitShort(data, bitPos);
        bitPos = ((Integer) val.get(0)).intValue();
        int v4 = ((Integer) val.get(1)).intValue();
        v.setVerticesidx(new int[] { v1, v2, v3, v4 });
        bitPos = headTailReader.readObjectTailer(data, bitPos, v);
    }
}
