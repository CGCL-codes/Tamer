package com.exult.android.shapeinf;

import java.io.PushbackInputStream;
import java.io.InputStream;
import com.exult.android.ShapeInfo;
import com.exult.android.EUtil;

public class PaperdollNpc extends BaseInfo {

    boolean isFemale;

    boolean translucent;

    short bodyShape;

    short bodyFrame;

    short headShape;

    short headFrame;

    short headFrameHelm;

    short armsShape;

    short armsFrame[] = new short[3];

    private boolean readNew(InputStream in, int version, boolean patch, int game, ShapeInfo info) {
        PushbackInputStream txtin = (PushbackInputStream) in;
        int sexflag = EUtil.ReadInt(txtin);
        if (sexflag == -0xff) {
            info.setNpcPaperdollInfo(null);
            setInvalid(true);
            return true;
        }
        isFemale = sexflag != 0;
        translucent = EUtil.ReadInt(txtin) != 0;
        bodyShape = (short) EUtil.ReadInt(txtin);
        bodyFrame = (short) EUtil.ReadInt(txtin);
        headShape = (short) EUtil.ReadInt(txtin);
        headFrame = (short) EUtil.ReadInt(txtin);
        headFrameHelm = (short) EUtil.ReadInt(txtin);
        armsShape = (short) EUtil.ReadInt(txtin);
        armsFrame[0] = (short) EUtil.ReadInt(txtin);
        armsFrame[1] = (short) EUtil.ReadInt(txtin);
        armsFrame[2] = (short) EUtil.ReadInt(txtin);
        if (version < 3) info.setGumpData(EUtil.ReadInt(txtin, -1), -1);
        info.setNpcPaperdollInfo(this);
        return true;
    }

    @Override
    public boolean read(InputStream in, int version, boolean patch, int game, ShapeInfo info) {
        return (new PaperdollNpc()).readNew(in, version, patch, game, info);
    }
}
