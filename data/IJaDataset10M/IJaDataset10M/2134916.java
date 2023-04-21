package gov.nasa.jpf.jvm;

public class JPF_java_io_ObjectInputStream {

    public static int latestUserDefinedLoader____Ljava_lang_ClassLoader_2(MJIEnv env, int clsRef) {
        return MJIEnv.NULL;
    }

    public static void bytesToDoubles___3BI_3DII__(MJIEnv env, int clsRef, int baRef, int bOff, int daRef, int dOff, int nDoubles) {
        int imax = dOff + nDoubles;
        int j = bOff;
        for (int i = dOff; i < imax; i++) {
            byte b0 = env.getByteArrayElement(baRef, j++);
            byte b1 = env.getByteArrayElement(baRef, j++);
            byte b2 = env.getByteArrayElement(baRef, j++);
            byte b3 = env.getByteArrayElement(baRef, j++);
            byte b4 = env.getByteArrayElement(baRef, j++);
            byte b5 = env.getByteArrayElement(baRef, j++);
            byte b6 = env.getByteArrayElement(baRef, j++);
            byte b7 = env.getByteArrayElement(baRef, j++);
            long l = 0x00000000000000ff & b7;
            l <<= 8;
            l |= 0x00000000000000ff & b6;
            l <<= 8;
            l |= 0x00000000000000ff & b5;
            l <<= 8;
            l |= 0x00000000000000ff & b4;
            l <<= 8;
            l |= 0x00000000000000ff & b3;
            l <<= 8;
            l |= 0x00000000000000ff & b2;
            l <<= 8;
            l |= 0x00000000000000ff & b1;
            l <<= 8;
            l |= 0x00000000000000ff & b0;
            double d = Double.longBitsToDouble(l);
            env.setDoubleArrayElement(daRef, i, d);
        }
    }
}
