package org.travelfusion.xmlclient.ri.xobject.misc;

import java.io.Serializable;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author Jesse McLaughlin (nzjess@gmail.com)
 */
public class XCity implements Serializable, IsSerializable {

    private static final long serialVersionUID = 1L;

    private String code;

    private String name;

    private XCountry country;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public XCountry getCountry() {
        return country;
    }

    public void setCountry(XCountry country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return name + " (" + code + ") - " + country;
    }
}
