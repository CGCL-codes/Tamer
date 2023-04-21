package jobjectstore.tests;

import java.io.*;
import java.lang.reflect.*;

public class SObject implements Serializable {

    private long id;

    private long hashCode;

    private String v1;

    private String v2;

    private String v3;

    private String v4;

    private String v5;

    public static String dirForObj(String baseDir, long id) {
        String hx = Long.toHexString(hash64shift(id));
        return baseDir + "SObject/" + hx.substring(0, 2) + "/" + hx.substring(2, 4) + "/" + hx.substring(4, 6) + "/";
    }

    public static long hash64shift(long key) {
        key = (~key) + (key << 21);
        key = key ^ (key >>> 25);
        key = key + (key << 6);
        key = key ^ (key >>> 13);
        key = (key + (key << 2)) + (key << 4);
        key = key ^ (key >>> 29);
        key = (key + (key << 3)) + (key << 28);
        return key;
    }

    /** Creates a new instance of SObject */
    public SObject(long id) {
        this.id = id;
        setV1("This is the v1 value for " + id);
        setV2("This is the v2 value for " + id);
        setV3("This is the v3 value for " + id);
        setV4("This is the v4 value for " + id);
        setV5("This is the v5 value for " + id);
        hashCode = hash64shift(id);
    }

    public void store() {
    }

    public long getId() {
        return id;
    }

    public long getHashCode() {
        return hashCode;
    }

    public String getV1() {
        return v1;
    }

    public void setV1(String v1) {
        this.v1 = v1;
    }

    public String getV2() {
        return v2;
    }

    public void setV2(String v2) {
        this.v2 = v2;
    }

    public String getV3() {
        return v3;
    }

    public void setV3(String v3) {
        this.v3 = v3;
    }

    public String getV4() {
        return v4;
    }

    public void setV4(String v4) {
        this.v4 = v4;
    }

    public String getV5() {
        return v5;
    }

    public void setV5(String v5) {
        this.v5 = v5;
    }
}
