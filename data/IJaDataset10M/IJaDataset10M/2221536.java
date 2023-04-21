package jvc.web.action;

import jvc.web.action.BaseAction;
import jvc.web.action.ActionContent;
import jvc.util.db.MyDB;
import java.lang.reflect.*;
import java.util.*;

public class CustomAction implements BaseAction {

    /**
	 * Method Excute
	 *
	 *
	 * @param input
	 * @param output
	 * @param mydb
	 *
	 * @return
	 *
	 */
    public String Excute(ActionContent input, ActionContent output, MyDB mydb) {
        try {
            String ClassName = input.getParam("ClassName");
            String strMethod = input.getParam("Method");
            String resultname = input.getParam("name");
            String prestr = input.getParam("prestr");
            if (prestr.equals("")) prestr = "param";
            prestr += ".";
            List classlist = new ArrayList();
            List valuelist = new ArrayList();
            Object[] params = input.getParamNames();
            for (int i = 0; i < params.length; i++) {
                if (params[i].toString().startsWith(prestr)) {
                    output.setParam(params[i].toString(), input.getParam(params[i].toString()));
                    String[] p = params[i].toString().split("[.]");
                    String fieldvalue = input.getParam(params[i].toString());
                    String fieldtype = "str";
                    if (p.length >= 3) fieldtype = p[2];
                    if (fieldtype.equals("str")) {
                        classlist.add(String.class);
                        valuelist.add(fieldvalue);
                    }
                    if (fieldtype.equals("int")) {
                        classlist.add(int.class);
                        valuelist.add(new Integer(fieldvalue));
                    }
                    if (fieldtype.equals("boolean")) {
                        classlist.add(boolean.class);
                        valuelist.add(new Boolean(fieldvalue));
                    }
                    if (fieldtype.equals("double")) {
                        classlist.add(double.class);
                        valuelist.add(new Double(fieldvalue));
                    }
                }
            }
            Object callobj = Class.forName(ClassName).newInstance();
            Class[] classes = new Class[valuelist.size()];
            for (int iclass = 0; iclass < valuelist.size(); iclass++) {
                classes[iclass] = (Class) classlist.toArray()[iclass];
            }
            Method method = callobj.getClass().getDeclaredMethod(strMethod, classes);
            output.setParam(resultname, method.invoke(callobj, valuelist.toArray()));
            return input.getParam("success");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return input.getParam("fault");
    }

    public static void main(String[] arg) {
        ActionContent input = new ActionContent();
        ActionContent output = new ActionContent();
        input.setParam("ClassName", "jvc.util.FileUtils");
        input.setParam("method", "getFileName");
        input.setParam("param.1", "c:\\tem\\aa.cc");
        input.setParam("name", "a");
        CustomAction c = new CustomAction();
        c.Excute(input, output, null);
        System.out.println(output.getParam("a"));
    }
}
