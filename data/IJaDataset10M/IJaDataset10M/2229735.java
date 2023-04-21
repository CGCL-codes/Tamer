package eibstack.layer7;

public interface A_DataConnlessService {

    public void setListener(Listener l);

    public int propertyValue_ReadReq(int da, int pr, int hc, int objIdx, int propID, int startIdx, int noElems, boolean waitL2Con);

    public int propertyValue_FReadReq(int da, int pr, int hc, int objIdx, int propID, long startIdx, int noElems, boolean waitL2Con);

    public int propertyValue_WriteReq(int da, int pr, int hc, int objIdx, int propID, int startIdx, int noElems, byte[] data, boolean waitL2Con);

    public int propertyValue_FWriteReq(int da, int pr, int hc, int objIdx, int propID, long startIdx, int noElems, byte[] data, boolean waitL2Con);

    public int propertyDescr_ReadReq(int da, int pr, int hc, int objIdx, int propID, int propIdx, boolean waitL2Con);

    public int propertyValue_ReadRes(int da, int pr, int hc, int objIdx, int propID, int startIdx, int noElems, byte[] data, boolean waitL2Con);

    public int propertyValue_FReadRes(int da, int pr, int hc, int objIdx, int propID, long startIdx, int noElems, byte[] data, boolean waitL2Con);

    public int propertyValue_FWriteRes(int da, int pr, int hc, int objIdx, int propID, long startIdx, int noElems, byte[] data, boolean waitL2Con);

    public int propertyDescr_ReadRes(int da, int pr, int hc, int objIdx, int propID, int propIdx, int type, int maxNoElems, int readLevel, int writeLevel, boolean waitL2Con);

    public interface Listener {

        public void propertyValue_ReadInd(int sa, int pr, int hc, int objIdx, int propID, int startIdx, int noElems);

        public void propertyValue_FReadInd(int sa, int pr, int hc, int objIdx, int propID, long startIdx, int noElems);

        public void propertyValue_WriteInd(int sa, int pr, int hc, int objIdx, int propID, int startIdx, int noElems, byte[] data);

        public void propertyValue_FWriteInd(int sa, int pr, int hc, int objIdx, int propID, long startIdx, int noElems, byte[] data);

        public void propertyDescr_ReadInd(int sa, int pr, int hc, int objIdx, int propID, int propIdx);

        public void propertyValue_ReadCon(int sa, int pr, int hc, int objIdx, int propID, int startIdx, int noElems, byte[] data);

        public void propertyValue_FReadCon(int sa, int pr, int hc, int objIdx, int propID, long startIdx, int noElems, byte[] data);

        public void propertyValue_FWriteCon(int sa, int pr, int hc, int objIdx, int propID, long startIdx, int noElems, byte[] data);

        public void propertyDescr_ReadCon(int sa, int pr, int hc, int objIdx, int propID, int propIdx, int type, int maxNoElems, int readLevel, int writeLevel);
    }
}
