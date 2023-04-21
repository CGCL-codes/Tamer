package pl.rmalinowski.gwt2swf.client.ui;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

public class SWFCallableWidget extends SWFWidget {

    public SWFCallableWidget() {
        super();
    }

    public SWFCallableWidget(SWFParams desc) {
        super(desc);
    }

    private native JavaScriptObject _call(Element flashObject, String methodName);

    private native void _callForFlashInfo(Element flashObject);

    public JavaScriptObject call(String methodName) {
        return _call(DOM.getElementById(super.getSwfId()), methodName);
    }
}
