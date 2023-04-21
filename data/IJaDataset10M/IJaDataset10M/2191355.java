package io;

import java.nio.charset.*;
import java.util.*;
import static net.mindview.util.Print.*;

public class AvailableCharSets {

    public static void main(String[] args) {
        SortedMap<String, Charset> charSets = Charset.availableCharsets();
        Iterator<String> it = charSets.keySet().iterator();
        while (it.hasNext()) {
            String csName = it.next();
            printnb(csName);
            Iterator aliases = charSets.get(csName).aliases().iterator();
            if (aliases.hasNext()) printnb(": ");
            while (aliases.hasNext()) {
                printnb(aliases.next());
                if (aliases.hasNext()) printnb(", ");
            }
            print();
        }
    }
}
