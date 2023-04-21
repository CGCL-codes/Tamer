package Code.Basic.ObjectIO;

import java.util.HashMap;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ObjectCoder {

    HashMap<String, Object> myMap;

    public static byte[] encodeKeyedObject(Object object) {
        byte[] temp = null;
        if (object instanceof ObjectCoder) {
            ObjectCoder coder = new ObjectCoder();
            ((ObjectCoding) object).encodeObjectWithCoder(coder);
            temp = coder.getData();
        }
        return temp;
    }

    public static void decodeKeyedObject(Object object, byte[] data) {
        if (object instanceof ObjectCoding) {
            ObjectCoder coder = new ObjectCoder(data);
            ((ObjectCoding) object).decodeObjectWithCoder(coder);
        }
    }

    private ObjectCoder() {
        myMap = new HashMap<String, Object>();
    }

    private ObjectCoder(byte[] data) {
        InputStream in = new ByteArrayInputStream(data);
        ObjectInputStream ois = null;
        myMap = null;
        try {
            ois = new ObjectInputStream(in);
            myMap = (HashMap<String, Object>) ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private byte[] getData() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(out);
            oos.writeObject(myMap);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }

    /********************/
    public void encodeByteWithKey(String key, byte a) {
        myMap.put(key, new Byte(a));
    }

    public void encodeShortWithKey(String key, short a) {
        myMap.put(key, new Short(a));
    }

    public void encodeIntWithKey(String key, int a) {
        myMap.put(key, new Integer(a));
    }

    public void encodeLongWithKey(String key, long a) {
        myMap.put(key, new Long(a));
    }

    public void encodeFloatWithKey(String key, float a) {
        myMap.put(key, new Float(a));
    }

    public void encodeDoubleWithKey(String key, double a) {
        myMap.put(key, new Double(a));
    }

    public void encodeCharacterWithKey(String key, char a) {
        myMap.put(key, new Character(a));
    }

    public void encodeStringWithKey(String key, String a) {
        myMap.put(key, a);
    }

    public void encodeObjectWithKey(String key, Object a) {
        if (a instanceof ObjectCoding) {
            byte[] temp = ObjectCoder.encodeKeyedObject(a);
            myMap.put(key, temp);
        }
    }

    public void encodeByteArrayWithKey(String key, byte[] a) {
        myMap.put(key, a);
    }

    public void encodeShortArrayWithKey(String key, short[] a) {
        myMap.put(key, a);
    }

    public void encodeIntArrayWithKey(String key, int[] a) {
        myMap.put(key, a);
    }

    public void encodeLongArrayWithKey(String key, long[] a) {
        myMap.put(key, a);
    }

    public void encodeFloatArrayWithKey(String key, float[] a) {
        myMap.put(key, a);
    }

    public void encodeDoubleArrayWithKey(String key, double[] a) {
        myMap.put(key, a);
    }

    /********************/
    public byte decodeByteWithKey(String key) {
        Byte temp = (Byte) (myMap.get(key));
        return temp.byteValue();
    }

    public short decodeShortWithKey(String key) {
        Short temp = (Short) (myMap.get(key));
        return temp.shortValue();
    }

    public int decodeIntWithKey(String key) {
        Integer temp = (Integer) (myMap.get(key));
        return temp.intValue();
    }

    public long decodeLongWithKey(String key) {
        Long temp = (Long) (myMap.get(key));
        return temp.longValue();
    }

    public float decodeFloatWithKey(String key) {
        Float temp = (Float) (myMap.get(key));
        return temp.floatValue();
    }

    public double decodeDoubleWithKey(String key) {
        Double temp = (Double) (myMap.get(key));
        return temp.doubleValue();
    }

    public char decodeCharacterWithKey(String key) {
        Character temp = (Character) (myMap.get(key));
        return temp.charValue();
    }

    public String decodeStringWithKey(String key) {
        return (String) (myMap.get(key));
    }

    public void decodeObjectWithKey(Object object, String key) {
        if (object instanceof ObjectCoding) {
            byte[] tempData = (byte[]) (myMap.get(key));
            ObjectCoder.decodeKeyedObject(object, tempData);
        }
    }

    public byte[] decodeByteArrayWithKey(String key) {
        return (byte[]) (myMap.get(key));
    }

    public short[] decodeShortArrayWithKey(String key) {
        return (short[]) (myMap.get(key));
    }

    public int[] decodeIntArrayWithKey(String key) {
        return (int[]) (myMap.get(key));
    }

    public long[] decodeLongArrayWithKey(String key) {
        return (long[]) (myMap.get(key));
    }

    public float[] decodeFloatArrayWithKey(String key) {
        return (float[]) (myMap.get(key));
    }

    public double[] decodeDoubleArrayWithKey(String key) {
        return (double[]) (myMap.get(key));
    }
}
