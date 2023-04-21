package net.sourceforge.algomusic.testing;

import java.io.File;
import net.sourceforge.algomusic.generators.*;
import net.sourceforge.algomusic.support.Globals;

public class SerialTester {

    public static void main(String[] args) {
        try {
            SerialGenerator gen = new SerialGenerator("serial.mid", new File(Globals.SHORTCUT + "series/series1.txt"));
            gen.generate(20, 2);
            gen.output();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
