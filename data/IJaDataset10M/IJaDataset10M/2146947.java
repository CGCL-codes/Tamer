package net.sourceforge.freejava.text.util;

import java.util.Map.Entry;
import net.sourceforge.freejava.types.Bits;
import net.sourceforge.freejava.types.util.Strings;

public class CharFeatureModelTest {

    public static void main(String[] args) {
        for (Entry<String, byte[]> e : CharFeatureModel.octf.entrySet()) {
            System.out.print("octf." + e.getKey() + " = ");
            byte[] val = e.getValue();
            System.out.println(new String(val));
        }
        for (Entry<String, Bits> e : CharFeatureModel.bitf.entrySet()) {
            System.out.print("bitf." + e.getKey() + " = ");
            Bits val = e.getValue();
            int[] iv = val.toIntArray(false);
            System.out.println(Strings.join(",", iv));
        }
    }
}
