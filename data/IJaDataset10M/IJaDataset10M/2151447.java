package org.dag.dmj.data.floor;

import java.io.*;
import org.dag.dmj.*;
import org.dag.dmj.data.*;

public class GameWinData extends FloorData {

    public String endanim, endsound;

    public GameWinData(String ea, String es) {
        super();
        mapchar = 'W';
        endanim = ea;
        endsound = es;
    }

    public String toString() {
        return "GameWin (" + endanim + ", " + endsound + ")";
    }

    public void save(ObjectOutputStream so) throws IOException {
        super.save(so);
        so.writeUTF(endanim);
        so.writeUTF(endsound);
    }
}
