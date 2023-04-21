package com.crypticbit.ipa.io.parser.plist;

import com.crypticbit.ipa.results.ParsedData;

public interface PListResults<T> extends ParsedData, PListHeader {

    public T getEntry();
}
