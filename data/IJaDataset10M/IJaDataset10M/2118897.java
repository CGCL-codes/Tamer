package libomv.mapgenerator;

public interface FieldType {

    public static final int U8 = 0;

    public static final int U16 = 1;

    public static final int U32 = 2;

    public static final int U64 = 3;

    public static final int S8 = 4;

    public static final int S16 = 5;

    public static final int S32 = 6;

    public static final int S64 = 7;

    public static final int F32 = 8;

    public static final int F64 = 9;

    public static final int UUID = 10;

    public static final int BOOL = 11;

    public static final int Vector3 = 12;

    public static final int Vector3d = 13;

    public static final int Vector4 = 14;

    public static final int Quaternion = 15;

    public static final int IPADDR = 16;

    public static final int IPPORT = 17;

    public static final int Variable = 18;

    public static final int Fixed = 19;

    public static final int Single = 20;

    public static final int Multiple = 21;

    public static String[] TypeNames = { "U8", "U16", "U32", "U64", "S8", "S16", "S32", "S64", "F32", "F64", "LLUUID", "BOOL", "LLVector3", "LLVector3d", "LLVector4", "LLQuaternion", "IPADDR", "IPPORT", "Variable", "Fixed", "Single", "Multiple" };
}
