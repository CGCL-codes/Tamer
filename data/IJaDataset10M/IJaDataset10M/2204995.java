package org.wikiup.servlet.imp.context.util;

import org.wikiup.core.inf.Gettable;
import org.wikiup.util.CryptUtil;

public class DESCryptGetter implements Gettable<String> {

    public String get(String name) {
        int pos = name.indexOf(',');
        return pos != -1 ? CryptUtil.DESEncrypt(name.substring(0, pos), name.substring(pos + 1)) : null;
    }
}
