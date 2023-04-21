package com.softaria.spkiller.classfinder;

import java.io.IOException;

public interface ClassCollector {

    void collect(String path, InputStreamGetter isGetter) throws IOException;
}
