package TwitterSimpleClient.EntityCondivise;

public final class lavoriHelper {

    public static void write(IceInternal.BasicStream __os, java.util.ArrayList<String> __v) {
        if (__v == null) {
            __os.writeSize(0);
        } else {
            __os.writeSize(__v.size());
            for (String __elem : __v) {
                __os.writeString(__elem);
            }
        }
    }

    public static java.util.ArrayList<String> read(IceInternal.BasicStream __is) {
        java.util.ArrayList<String> __v;
        __v = new java.util.ArrayList<String>();
        final int __len0 = __is.readSize();
        __is.startSeq(__len0, 1);
        for (int __i0 = 0; __i0 < __len0; __i0++) {
            String __elem;
            __elem = __is.readString();
            __v.add(__elem);
            __is.checkSeq();
            __is.endElement();
        }
        __is.endSeq(__len0);
        return __v;
    }
}
