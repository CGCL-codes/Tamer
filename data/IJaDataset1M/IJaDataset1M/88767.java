package com.arykow.applications.ugabe.standalone;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import com.arykow.applications.ugabe.client.IntVector;

class IntVectorStateSaveLoad implements StateSaveLoad<IntVector> {

    public void stateSaveLoad(boolean save, int version, DataOutputStream dostream, DataInputStream distream, IntVector intVector) throws IOException {
        {
            if ((save)) dostream.writeInt((int) intVector.length); else intVector.length = distream.readInt();
        }
        ;
        if ((!save)) intVector.data = new int[Math.max(1, intVector.length * 2)];
        {
            for (int index = 0; index < (intVector.length); ++index) {
                if ((save)) dostream.writeInt((int) intVector.data[index]); else intVector.data[index] = distream.readInt();
            }
            ;
        }
        ;
    }
}
