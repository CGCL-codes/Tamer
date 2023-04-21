package net.sf.ninjakore.networking;

import java.util.HashMap;
import java.util.Map;

public class TokenizerBuilder {

    public static Tokenizer lookUpTable() {
        Map<Short, Short> lengths = new HashMap<Short, Short>();
        lengths.put((short) 0x64, (short) 55);
        lengths.put((short) 0x65, (short) 17);
        lengths.put((short) 0x66, (short) 3);
        TokenizingStrategy strategy = new LookUpTableStrategy(lengths);
        return new Tokenizer(strategy);
    }
}
