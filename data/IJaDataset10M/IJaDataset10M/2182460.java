package com.iver.cit.jdwglib.dwg.readers.objreaders.v2004;

import java.util.List;
import com.iver.cit.jdwglib.dwg.CorruptedDwgEntityException;
import com.iver.cit.jdwglib.dwg.DwgObject;
import com.iver.cit.jdwglib.dwg.DwgUtil;
import com.iver.cit.jdwglib.dwg.objects.DwgVertexMesh;

public class DwgVertexMeshReader2004 extends AbstractDwg2004Reader {

    public void readSpecificObj(int[] data, int offset, DwgObject dwgObj) throws RuntimeException, CorruptedDwgEntityException {
        if (!(dwgObj instanceof DwgVertexMesh)) throw new RuntimeException("DwgVertexMeshReader2004 solo puede leer DwgVertexMesh");
        DwgVertexMesh v = (DwgVertexMesh) dwgObj;
        int bitPos = offset;
        bitPos = readObjectHeader(data, bitPos, v);
        List val = DwgUtil.getRawChar(data, bitPos);
        bitPos = ((Integer) val.get(0)).intValue();
        int flag = ((Integer) val.get(1)).intValue();
        v.setFlags(flag);
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
        bitPos = readObjectTailer(data, bitPos, v);
    }
}
