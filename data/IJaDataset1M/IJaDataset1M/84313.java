package com.vsetec.mety.core;

import com.vsetec.mety.*;
import org.apache.bsf.BSFException;
import org.apache.bsf.BSFManager;

/**
 *
 * @author fedd
 */
public class Script {

    static {
        BSFManager.registerScriptingEngine("javascript", "org.apache.bsf.engines.javascript.JavaScriptEngine", null);
    }

    protected static Met execute(String lang, String scriptName, String body, Met... params) {
        try {
            BSFManager _bsf = new BSFManager();
            _bsf.declareBean("parameters", params, Met[].class);
            Object obj = _bsf.eval(lang, scriptName, 1, 1, body);
            Met ret;
            if (!(obj instanceof Met)) {
                ret = Source.SRC.getMet(obj);
            } else {
                ret = (Met) obj;
            }
            return ret;
        } catch (BSFException ex) {
            throw new MetException(ex);
        }
    }
}
