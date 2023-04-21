package org.sss.presentation.zk.compile.impl;

import org.sss.module.eibs.common.FormatPrint;
import org.sss.module.eibs.compile.CompileException;
import org.sss.presentation.zk.compile.IZulCompile;
import org.sss.presentation.zk.compile.ZulCompileContext;

/**
 * @author Jason.Hoo (latest modification by $Author: hujianxin $)
 * @version $Revision: 707 $ $Date: 2012-04-08 11:25:57 -0400 (Sun, 08 Apr 2012) $
 */
public class ComboboxCompile implements IZulCompile {

    public void compile(ZulCompileContext context) throws CompileException {
        FormatPrint print = context.getPrint();
        StringBuffer sb = new StringBuffer("<combobox readonly=\"true\"");
        sb.append(context.compileId(context.getId())).append(context.compileApply()).append(context.compileStyle());
        sb.append(context.compileDisabled()).append(">");
        print.println(sb.toString());
        print.incIndex();
        context.printFieldUrl();
        context.printCodetablePattern();
        print.decIndex();
        print.println("</combobox>");
    }
}
