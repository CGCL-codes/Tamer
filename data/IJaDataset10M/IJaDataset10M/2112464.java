package com.iver.cit.jdwglib.dwg.readers.objreaders.v15;

import java.util.ArrayList;
import com.iver.cit.jdwglib.dwg.CorruptedDwgEntityException;
import com.iver.cit.jdwglib.dwg.DwgObject;
import com.iver.cit.jdwglib.dwg.DwgUtil;
import com.iver.cit.jdwglib.dwg.objects.DwgCircle;

/**
 * @author alzabord
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DwgCircleReader15 extends AbstractDwg15Reader {

    public void readSpecificObj(int[] data, int offset, DwgObject dwgObj) throws RuntimeException, CorruptedDwgEntityException {
        if (!(dwgObj instanceof DwgCircle)) throw new RuntimeException("ArcReader 15 solo puede leer DwgCircle");
        DwgCircle circle = (DwgCircle) dwgObj;
        int bitPos = offset;
        bitPos = headTailReader.readObjectHeader(data, bitPos, dwgObj);
        ArrayList v = DwgUtil.getBitDouble(data, bitPos);
        bitPos = ((Integer) v.get(0)).intValue();
        double x = ((Double) v.get(1)).doubleValue();
        v = DwgUtil.getBitDouble(data, bitPos);
        bitPos = ((Integer) v.get(0)).intValue();
        double y = ((Double) v.get(1)).doubleValue();
        v = DwgUtil.getBitDouble(data, bitPos);
        bitPos = ((Integer) v.get(0)).intValue();
        double z = ((Double) v.get(1)).doubleValue();
        double[] coord = new double[] { x, y, z };
        circle.setCenter(coord);
        v = DwgUtil.getBitDouble(data, bitPos);
        bitPos = ((Integer) v.get(0)).intValue();
        double val = ((Double) v.get(1)).doubleValue();
        circle.setRadius(val);
        v = DwgUtil.testBit(data, bitPos);
        bitPos = ((Integer) v.get(0)).intValue();
        boolean flag = ((Boolean) v.get(1)).booleanValue();
        if (flag) {
            val = 0.0;
        } else {
            v = DwgUtil.getBitDouble(data, bitPos);
            bitPos = ((Integer) v.get(0)).intValue();
            val = ((Double) v.get(1)).doubleValue();
        }
        circle.setThickness(val);
        v = DwgUtil.testBit(data, bitPos);
        bitPos = ((Integer) v.get(0)).intValue();
        flag = ((Boolean) v.get(1)).booleanValue();
        if (flag) {
            x = y = 0.0;
            z = 1.0;
        } else {
            v = DwgUtil.getBitDouble(data, bitPos);
            bitPos = ((Integer) v.get(0)).intValue();
            x = ((Double) v.get(1)).doubleValue();
            v = DwgUtil.getBitDouble(data, bitPos);
            bitPos = ((Integer) v.get(0)).intValue();
            y = ((Double) v.get(1)).doubleValue();
            v = DwgUtil.getBitDouble(data, bitPos);
            bitPos = ((Integer) v.get(0)).intValue();
            z = ((Double) v.get(1)).doubleValue();
        }
        coord = new double[] { x, y, z };
        circle.setExtrusion(coord);
        bitPos = headTailReader.readObjectTailer(data, bitPos, dwgObj);
    }
}
