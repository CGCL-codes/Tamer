package com.obdobion.argument;

import java.text.ParseException;

public class CmdLineCLA extends AbstractCLA<ICmdLine> {

    /**
     * @uml.property name="templateCmdLine"
     * @uml.associationEnd
     */
    public ICmdLine templateCmdLine;

    public void reset() {
    }

    @SuppressWarnings("unchecked")
    public ICmdLineArg<ICmdLine> clone() throws CloneNotSupportedException {
        CmdLineCLA clone = (CmdLineCLA) super.clone();
        clone.templateCmdLine = templateCmdLine.clone();
        return clone;
    }

    public CmdLineCLA(char keychar, String keyword) {
        super(keychar, keyword);
    }

    public CmdLineCLA(char keychar) {
        super(keychar);
    }

    public CmdLineCLA(String keyword) {
        super(keyword);
    }

    public ICmdLine convert(String valueStr, boolean caseSensitive, Object target) throws ParseException {
        ICmdLine cmdline = null;
        Object newtarget = null;
        try {
            cmdline = templateCmdLine.clone();
            newtarget = VariableAssigner.getInstance().newVariable(this, target);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
        cmdline.parse(valueStr, newtarget);
        return cmdline;
    }

    protected void exportData(StringBuilder str) {
        for (int d = 0; d < size(); d++) {
            str.append("(");
            getValue(d).export(str);
            str.append(")");
        }
    }
}
