package net.sf.doolin.sdo.factory.json;

import net.sf.doolin.sdo.DOException;
import net.sf.doolin.sdo.DOKey;

public class DOJsonFieldNotFoundException extends DOException {

    public DOJsonFieldNotFoundException(String name) {
        super(DOKey.JsonFieldNotFound, name);
    }
}
