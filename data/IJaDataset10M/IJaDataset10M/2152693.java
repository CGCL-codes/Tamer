package com.enerjy.analyzer.java.rules.testfiles.T0012;

import java.util.List;

class PTest02<E> {

    <T> void clear(List<T> dest, List<? extends T> src) {
        dest.clear();
        src.clear();
    }
}
