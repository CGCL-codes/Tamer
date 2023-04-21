package org.open.force.connector;

import java.io.Serializable;
import java.util.Calendar;
import org.open.force.common.OFProps;
import org.open.force.sforce.SoapBindingStub;

public class OFConnection implements Serializable {

    private static final long serialVersionUID = 1L;

    private SoapBindingStub binding;

    private Calendar expires;

    private OFProps props;

    public boolean connected() {
        if (System.currentTimeMillis() < expires.getTimeInMillis()) {
            return false;
        }
        return true;
    }

    public SoapBindingStub getBinding() {
        return binding;
    }

    public void setBinding(SoapBindingStub binding) {
        this.binding = binding;
    }

    public Calendar getExpires() {
        return expires;
    }

    public void setExpires(Calendar expires) {
        this.expires = expires;
    }

    public OFProps getProps() {
        return props;
    }

    public void setProps(OFProps props) {
        this.props = props;
    }
}
