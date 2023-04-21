package sf2.test;

import java.util.Arrays;
import java.util.HashMap;
import sf2.core.KeyWrap;

public class ArraysTest {

    public static void main(String[] args) {
        byte[] b1 = KeyWrap.hash("Test".getBytes());
        byte[] b2 = KeyWrap.hash("Test".getBytes());
        HashMap<KeyWrap, Integer> map = new HashMap<KeyWrap, Integer>();
        System.err.println("Hash code: b1=" + Arrays.hashCode(b1) + ", " + Arrays.hashCode(b2));
        System.err.println("Equals: " + Arrays.equals(b1, b2));
        KeyWrap k1 = new KeyWrap(b1);
        KeyWrap k2 = new KeyWrap(b2);
        map.put(k1, 100);
        System.err.println(map.get(k2));
    }
}
