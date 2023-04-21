package jvc.web.action;

import jvc.web.action.BaseAction;
import jvc.web.action.ActionContent;
import jvc.util.db.*;
import java.util.*;
import jvc.web.module.*;
import jvc.util.*;
import org.nfunk.jep.*;
import org.nfunk.jep.function.script.*;

class GetCheckAction implements BaseAction {

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
        String name = input.getParam("name");
        StringBuffer result = new StringBuffer("var v=new Array();");
        result.append("for (i=0;i<this.elements.length;i++){");
        result.append("var names =this.elements[i].name.split('.');  ");
        result.append(" if(names.length>1){ ");
        result.append("if(this.elements[i].checked){");
        result.append("if(v[names[1].toLowerCase()+'.checksize']==null){");
        result.append(" v[names[1].toLowerCase()+'.checksize']=1;v[this.elements[i].name.toLowerCase()+'.checksize']=1;}");
        result.append(" else{v[names[1].toLowerCase()+'.checksize']++;v[this.elements[i].name.toLowerCase()+'.checksize']++}};");
        result.append(" v[names[1].toLowerCase()]=this.elements[i].value;");
        result.append(" v[this.elements[i].name.toLowerCase()]=this.elements[i].value;   }} ");
        result.append("var msg;var errorFlag=false;var errMsg='';");
        Validator validator = input.getCurPage().getValidator(name);
        JEP myParser = new JEP(true);
        myParser.addFunction("len", new Len());
        myParser.addFunction("double", new CDouble());
        myParser.addFunction("integer", new CInteger());
        final Iterator allPropertyKeysIterator = AppUtils.Validator.keySet().iterator();
        while (allPropertyKeysIterator.hasNext()) {
            String key = (String) allPropertyKeysIterator.next();
            String value = AppUtils.Validator.getProperty(key);
            myParser.addFunction(key, new RegExp(value));
        }
        myParser.setAllowUndeclared(true);
        for (Iterator it = validator.getExpressionList().iterator(); it.hasNext(); ) {
            Expression expression = (Expression) it.next();
            try {
                Node node = myParser.parse(expression.getValue().toLowerCase());
                for (Iterator itvar = myParser.getSymbolTable().keySet().iterator(); itvar.hasNext(); ) {
                    String varname = itvar.next().toString();
                    myParser.setVarValue(varname, "v['" + varname + "']");
                }
                String temp = myParser.evaluate(node).toString();
                result.append(" msg = '" + expression.getMsg() + "\\r\\n';if((" + temp + ")==false) {errMsg+=msg;errorFlag=true ;} ");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        result.append("if(errorFlag){alert(errMsg);return false;}");
        result.append(" return true;");
        output.setParam("checkform", result.toString());
        output.setParam("checkform." + name, result.toString());
        return input.getParam("success");
    }
}
