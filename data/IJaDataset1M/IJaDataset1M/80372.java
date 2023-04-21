package alchemy.libs;

import alchemy.core.HashLibrary;
import alchemy.util.UTFReader;
import java.io.IOException;

/**
 * Native UI library for Alchemy.
 * @author Sergey Basalaev
 * @version 0.1.0
 */
public class LibUI01 extends HashLibrary {

    public LibUI01() throws IOException {
        UTFReader r = new UTFReader(getClass().getResourceAsStream("/libui01.symbols"));
        String name;
        int index = 0;
        while ((name = r.readLine()) != null) {
            putFunc(new LibUI01Func(name, index));
            index++;
        }
        r.close();
        lock();
    }
}
