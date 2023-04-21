package org.sinaxe.context;

import org.sinaxe.SinaxeErrorHandler;
import org.sinaxe.base64.Base64;
import org.jaxen.Function;
import org.jaxen.Context;
import org.jaxen.FunctionCallException;
import java.util.List;
import java.net.URL;
import java.io.ByteArrayOutputStream;
import java.io.BufferedInputStream;

public class XPathBase64EncodeFileFunction extends SinaxeXPathFunction {

    public static final String name = "base64_encode_file";

    public XPathBase64EncodeFileFunction() {
    }

    public String getName() {
        return name;
    }

    public Object call(Context context, List args) throws FunctionCallException {
        checkArgs(args, 1);
        Function stringFunction = getFunction(context, "string");
        String filename = (String) stringFunction.call(context, args);
        try {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            URL url = cl.getResource(filename);
            if (url == null) url = new URL(filename);
            BufferedInputStream input = new BufferedInputStream(url.openStream());
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            Base64.encode(input, output);
            return output.toString();
        } catch (Exception e) {
            SinaxeErrorHandler.warning(name + "() failed to open file '" + filename + "'!", e);
        }
        return null;
    }
}
