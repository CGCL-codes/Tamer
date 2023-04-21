package com.ibm.wala.cast.js.html;

public class MutableFileMapping extends FileMapping {

    void map(int line, IncludedPosition originalPos) {
        lineNumberToFileAndLine.put(line, originalPos);
    }
}
