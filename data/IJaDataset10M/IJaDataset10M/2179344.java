package com.iver.cit.jdwglib.dwg.readers.objreaders.v15;

import com.iver.cit.jdwglib.dwg.CorruptedDwgEntityException;
import com.iver.cit.jdwglib.dwg.DwgObject;

/**
 * @author alzabord
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DwgSeqEndReader15 extends AbstractDwg15Reader {

    public void readSpecificObj(int[] data, int offset, DwgObject dwgObj) throws RuntimeException, CorruptedDwgEntityException {
        int bitPos = offset;
        bitPos = headTailReader.readObjectHeader(data, bitPos, dwgObj);
        bitPos = headTailReader.readObjectTailer(data, bitPos, dwgObj);
    }
}
