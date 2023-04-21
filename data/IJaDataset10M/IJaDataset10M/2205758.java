package com.crypticbit.ipa.io.parser.sqlite;

import java.util.List;
import com.crypticbit.ipa.central.FileParseException;
import com.crypticbit.ipa.results.ParsedData;

public interface SqlResults extends ParsedData {

    public SqlMetaData getMetaData();

    public <T> List<T> getRecordsByInterface(Class<T> interfaceDef) throws FileParseException;
}
