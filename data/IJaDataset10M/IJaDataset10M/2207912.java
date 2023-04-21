package com.crypticbit.ipa.io.parser.plist;

import java.text.SimpleDateFormat;

public interface PListPrimitive extends PListContainer {

    static final SimpleDateFormat ISO_8601_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    public Object getPrimitive();
}
