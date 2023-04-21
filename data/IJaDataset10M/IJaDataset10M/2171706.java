package com.pinae.mufasa.wsdl.resources;

import com.pinae.mufasa.wsdl.Constant;
import com.pinae.nala.xb.resource.Namespace;

public class Input extends com.pinae.nala.xb.NalaObject {

    private String name;

    private String message;

    private Body body;

    public Input() {
        setNamespaces(new Namespace(Constant.WSDL_PREFIX, Constant.WSDL_URI));
    }

    public Input(String name, String message) {
        super();
        setNamespaces(new Namespace(Constant.WSDL_PREFIX, Constant.WSDL_URI));
        this.name = name;
        this.message = message;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public Body getBody() {
        return body;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
