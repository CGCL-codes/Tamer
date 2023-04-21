package com.legstar.xsdc.test.cases.cultureinfo.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import com.legstar.xsdc.test.cases.cultureinfo.CultureInfoReply;

@XmlRootElement(name = "getInfoResponse", namespace = "http://cultureinfo.cases.test.xsdc.legstar.com/")
@XmlType(name = "getInfoResponse", namespace = "http://cultureinfo.cases.test.xsdc.legstar.com/")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetInfoResponse {

    @XmlElement(name = "return", namespace = "")
    private CultureInfoReply _return;

    public CultureInfoReply getReturn() {
        return this._return;
    }

    public void setReturn(CultureInfoReply _return) {
        this._return = _return;
    }
}
