package org.zkoss.zktest.bind.issue;

import org.zkoss.bind.BindContext;
import org.zkoss.bind.Binder;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Default;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zul.Label;

public class F00633 {

    String value1;

    public F00633() {
    }

    public String getValue1() {
        return value1;
    }

    @NotifyChange
    public void setValue1(String value1) {
        this.value1 = value1;
    }

    @Command
    @NotifyChange("value1")
    public void cmd1() {
        value1 = "doCommand1";
    }

    @NotifyChange("value1")
    @Command
    public void cmd2() {
        value1 = "doCommand2";
    }

    @NotifyChange("value1")
    @Command
    public void cmd3(BindContext ctx, Binder binder) {
        value1 = "doCommand3 " + ctx.getComponent().getId() + " " + (binder.getViewModel() == this);
    }

    @NotifyChange("value1")
    @Command
    public void cmd4(@Default("3") Integer arg1, BindContext ctx, Binder binder, @Default("false") Boolean arg2, String arg3) {
        value1 = "doCommand4 " + arg1 + " " + arg2 + " " + arg3 + " " + ctx.getComponent().getId() + " " + (binder.getViewModel() == this);
    }

    @NotifyChange("value1")
    @Command
    public void cmd5(@BindingParam("arg1") Integer arg1, BindContext ctx, Binder binder, @BindingParam("arg2") Boolean arg2, @BindingParam("arg3") String arg3) {
        value1 = "doCommand5 " + arg1 + " " + arg2 + " " + arg3 + " " + ctx.getComponent().getId() + " " + (binder.getViewModel() == this);
    }

    @NotifyChange("value1")
    @Command
    public void cmd6(@BindingParam("arg1") @Default("9") Integer arg1, @ContextParam(ContextType.BIND_CONTEXT) BindContext ctx, @ContextParam(ContextType.BINDER) Binder binder, @BindingParam("arg2") @Default("true") Boolean arg2, @Default("ABCD") @BindingParam("arg3") String arg3) {
        value1 = "doCommand6 " + arg1 + " " + arg2 + " " + arg3 + " " + ctx.getComponent().getId() + " " + (binder.getViewModel() == this);
    }

    @NotifyChange("value1")
    @Command({ "cmd7", "cmd8", "cmd9" })
    public void doCommandX(@BindingParam("arg1") @Default("9") Integer arg1, @BindingParam("arg2") @Default("true") Boolean arg2, @Default("ABCD") @BindingParam("arg3") String arg3, BindContext ctx) {
        value1 = "doCommandX " + arg1 + " " + arg2 + " " + arg3 + " " + ctx.getCommandName();
    }

    @Command({ "cmdA" })
    public void doCommandA(@BindingParam("label") Label label, @BindingParam("unknow") Object obj) {
        StringBuffer sb = new StringBuffer();
        sb.append("object is ");
        if (obj instanceof Component) {
            sb.append(((Component) obj).getId());
        } else if (obj instanceof Desktop) {
            sb.append("desktop");
        }
        label.setValue(sb.toString());
    }

    @NotifyChange("value1")
    @Command("create")
    public void onCreate(@BindingParam("label") Label label) {
        value1 = "onCreate 1";
        label.setValue("onCreate 2");
    }
}
