package org.synthful.gwt.javascript.client;

import org.synthful.gwt.javascript.client.DomUtils;
import com.google.gwt.core.client.JavaScriptObject;

/**
 * 
 * @author Icecream
 * JSON Remote Script Call inserts a <script src='datasrc'> tag
 * in the client document.<br>
 * 
 * It defines a callback function on the client document.<br>
 * 
 * The server datasrc has to be in proper javascript format, with the data
 * enclosed within the callback function, so that on loading into the client
 * document, the callback function would execute with the enclosed json data
 * as its argument. 
 * 
 * Example javascript data: 
 * <pre>
 * hello( {json data});
 * </pre>
 *
 * alternatively: 
 * <pre>
 * var x = {json data};
 * hello(x);
 * </pre>
 * 
 * Which means a call need be about json but executing a complete javascript.
 *  
 */
public abstract class JsonRemoteScriptCall {

    public JsonRemoteScriptCall() {
    }

    /**
     * use this call if dynamic remote data source and therefore able to
     * dynamically code the callback js function at the end of the json stream.
     *
     * This procedure would generate a unique js function name and
     * send the server with reqestURI?callBackParameter=uniquelyGeneratedJsFunctionName
     * so that the server-side would expect
     * <ul>
     * <li> to grab uniquelyGeneratedJsFunctionName thro callBackParameter,
     * <li> and then generate the json,
     * <li> assign a var to that json, var whatever={json structure}
     * <li> append to the end of the json the JS functon call:<br/>
     * 
     * uniquelyGeneratedJsFunctionName(whatever);
     * 
     */
    public void callByCallBackParameter(String url, String callBackParameter) {
        String uniqCallbackName = "jsonRSC" + this.hashCode();
        url = url + '?' + callBackParameter + '=' + uniqCallbackName;
        call(url, uniqCallbackName);
    }

    /**
     * use this call if remote data source is static file and the callback
     * has be hard-coded at the end of the json file,
     * where you predetermine the name of the callbackJsFunctionName,</br>
     * 
     * and made sure you appended that you assigned the json structure to a var
     * and then appended the function call</br>
     *   callbackJsFunctionName(whatever);</br>
     * to the end of the json structure.</br>
     * Then bridgeCallbackNames would map that callbackJsFunctionName to
     * the GWT method name onJsonRSCResponse.</br>
     * DomUtils.jsAddScript would read the jsonp and insert it as a script into the local DOM,
     * resulting in the json being converted into a JavascriptObject.
     * Then eval would call callbackJsFunctionName with the JavascriptObject as argument,
     * which in effect is calling onJsonRSCResponse with the JavascriptObject as argument.
     */
    public void call(String url, String callbackJsFunctionName) {
        bridgeCallbackNames(this, callbackJsFunctionName);
        DomUtils.jsAddScript(url);
    }

    private static native void bridgeCallbackNames(JsonRemoteScriptCall hdlr, String callbackName);

    public abstract void onJsonRSCResponse(JavaScriptObject jso);
}
