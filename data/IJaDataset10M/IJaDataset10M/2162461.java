package eu.xtreemos.xconsole.blocks;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class XConsoleHandle {

    protected Object invokeOwner;

    protected String command;

    protected Method method;

    protected Object result;

    protected Class<?>[] types;

    protected String doc;

    protected String usage;

    private String setDoc(String doc) {
        String d = "";
        try {
            d = URLDecoder.decode(doc, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return d;
    }

    public XConsoleHandle(Object owner, String command, String methodName, String parTypes, String doc) {
        this.invokeOwner = owner;
        this.command = command;
        this.doc = setDoc(doc);
        this.usage = "usage: " + this.command + " " + parTypes;
        StringTokenizer st = new StringTokenizer(parTypes, ", ");
        this.types = null;
        this.types = new Class[st.countTokens()];
        int pos = 0;
        while (st.hasMoreTokens()) {
            String className = "";
            try {
                className = st.nextToken();
                Class<?> c = Class.forName(className, true, ClassLoader.getSystemClassLoader());
                this.types[pos] = c;
                pos++;
            } catch (ClassNotFoundException e) {
                System.err.println("ERROR: could not resolve class for " + className);
                e.printStackTrace();
            }
        }
        try {
            if (this.types.length > 0) {
                this.method = this.invokeOwner.getClass().getMethod(methodName, this.types);
            } else {
                this.method = this.invokeOwner.getClass().getMethod(methodName, null);
            }
            this.usage += "\n\treturns: " + this.method.getReturnType();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public void execute(ArrayList<?> params) {
        System.out.println("GOT PARAMS: " + params + " size " + params.size());
        try {
            if (params.size() == 0 || params.get(0) == "") {
                this.result = this.method.invoke(this.invokeOwner, null);
            } else {
                this.result = this.method.invoke(this.invokeOwner, params.toArray());
            }
        } catch (Exception e) {
            System.err.println("Error executing XConsole command " + this.command);
            e.printStackTrace();
        }
    }

    public Object getResult() {
        return this.result;
    }

    public String command() {
        return this.command;
    }

    public String getDocumentation() {
        return this.doc;
    }

    public String getUsageDoc() {
        return this.usage;
    }
}
