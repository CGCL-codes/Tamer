package pspdash;

import java.io.Reader;

public interface TranslationEngine {

    String translateString(String s);

    Reader translateStream(Reader s);
}
