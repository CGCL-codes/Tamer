package com.pl.itsense.ftsm.common;

import java.util.List;

public interface ISymbolInfoFileReader {

    List<ISymbolInfo> readSymbolInfos(String fileName, ISymbolInfoProvider provider);

    List<ISymbolInfo> readSymbolInfos(String fileName, ISymbolInfoProvider provider, int fromIndex, int toIndex);
}
