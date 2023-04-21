package org.webguitoolkit.ui.sandbox.controls.form;

import java.io.PrintWriter;
import java.io.StringWriter;
import org.apache.commons.lang.StringUtils;
import org.apache.ecs.html.IMG;
import org.apache.ecs.html.Span;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;
import org.webguitoolkit.ui.ajax.IContext;
import org.webguitoolkit.ui.controls.event.ClientEvent;
import org.webguitoolkit.ui.controls.event.IServerEventListener;
import org.webguitoolkit.ui.controls.event.ServerEvent;
import org.webguitoolkit.ui.controls.form.Button;
import org.webguitoolkit.ui.controls.util.JSUtil;
import org.webguitoolkit.ui.controls.util.TextService;
import org.webguitoolkit.ui.http.ResourceServlet;

/**
 * @deprecated
 * @author bkl
 * 
 * add addWgtJS("glossbutton.js"); for page required!
 *
 */
public class Button2Zero extends Button {

    private String src;

    protected int iconWidth = -1;

    protected int iconHeight = -1;

    private int displayMode = 0;

    private boolean block = false;

    public static final int DISPLAY_MODE_HORIZONTAL = 0;

    public static final int DISPLAY_MODE_VERTICAL = 1;

    /**
	 * output HTML for button
	 */
    protected void endHTML(PrintWriter out) {
        Span span = new Span();
        Table table = new Table();
        TR top = new TR();
        TD topLeft = new TD();
        TD topCenter = new TD();
        TD topRight = new TD();
        TR middle = new TR();
        TD midLeft = new TD();
        TD midCenter = new TD();
        TD midRight = new TD();
        TR bottom = new TR();
        TD botLeft = new TD();
        TD botCenter = new TD();
        TD botRight = new TD();
        Span textContainer = new Span();
        org.apache.ecs.html.Button button = new org.apache.ecs.html.Button();
        boolean disabled = getContext().processBool(getId() + ".disabled");
        table.addElement(top);
        top.addElement(topLeft);
        top.addElement(topCenter);
        top.addElement(topRight);
        table.addElement(middle);
        middle.addElement(midLeft);
        middle.addElement(midCenter);
        middle.addElement(midRight);
        table.addElement(bottom);
        bottom.addElement(botLeft);
        bottom.addElement(botCenter);
        bottom.addElement(botRight);
        IMG spacer = new IMG("./images/wgt/1.gif");
        spacer.setWidth(1);
        spacer.setHeight(1);
        topLeft.addElement(new IMG("./images/wgt/1.gif"));
        topCenter.addElement(new IMG("./images/wgt/1.gif"));
        topRight.addElement(new IMG("./images/wgt/1.gif"));
        midLeft.addElement(new IMG("./images/wgt/1.gif"));
        midRight.addElement(new IMG("./images/wgt/1.gif"));
        botLeft.addElement(new IMG("./images/wgt/1.gif"));
        botCenter.addElement(new IMG("./images/wgt/1.gif"));
        botRight.addElement(new IMG("./images/wgt/1.gif"));
        table.setCellPadding(0);
        table.setCellSpacing(0);
        table.setBorder(0);
        topLeft.setClass("btn_lt");
        topCenter.setClass("btn_ct");
        topRight.setClass("btn_rt");
        midLeft.setClass("btn_lm");
        midCenter.setClass("btn_cm");
        midRight.setClass("btn_rm");
        botLeft.setClass("btn_lb");
        botCenter.setClass("btn_cb");
        botRight.setClass("btn_rb");
        button.setClass("btn_btn");
        button.setID(getId() + "_btn");
        midCenter.addElement(button);
        textContainer.setID(id4Text());
        textContainer.setClass("btn_txt");
        if (StringUtils.isNotEmpty(getSrc())) {
            if (StringUtils.isEmpty(this.getLabel())) {
                button.setStyle("width:" + iconWidth + "px;height:" + iconHeight + "px;background: transparent url(" + getSrc() + ") no-repeat; background-position: center;");
            } else {
                button.setStyle("background: transparent url(" + getSrc() + ") no-repeat;");
            }
        }
        if (StringUtils.isNotEmpty(this.getLabel())) {
            if (StringUtils.isNotEmpty(getSrc()) && iconWidth > -1) {
                if (displayMode == DISPLAY_MODE_VERTICAL) {
                    String s = button.getAttribute("style");
                    s = s + "background-position: center top;padding-top:" + String.valueOf(iconHeight) + "px;";
                    button.setStyle(s);
                } else {
                    textContainer.setStyle("padding-left: " + String.valueOf(iconWidth) + "px;");
                }
            }
            textContainer.addElement(this.getLabel());
            button.addElement(textContainer);
        }
        if (this.hasStyle()) {
            table.setStyle(this.getStyleAsString());
        }
        midCenter.setID(getId());
        button.setOnClick(JSUtil.jsFireEvent(getId(), ClientEvent.TYPE_ACTION) + " return false;");
        if (getConfirmMsg() != null) {
            button.setOnClick(JSUtil.jsConfirm(TextService.getString(getConfirmMsg()), JSUtil.jsFireEvent(getId(), ClientEvent.TYPE_ACTION)));
        } else {
            button.setOnClick(JSUtil.jsFireEvent(getId(), ClientEvent.TYPE_ACTION) + " return false;");
        }
        span.addElement(table);
        span.setID(getId() + "_sspan");
        if (block && isVisible()) {
            span.setStyle("display:block;");
        }
        span.output(out);
        this.initJaveScript();
    }

    public void setDisabled(boolean disabled) {
        IContext ctx = getContext();
        String dis = disabled ? "true" : null;
        ctx.setAttribute(getId(), "disabled", dis);
        String btnsytlye = disabled ? "btn_cm_dis" : "btn_cm";
        String btnTextSytlye = disabled ? "btn_txt_dis" : "btn_txt";
        ctx.setAttribute(getId(), "class", btnsytlye);
        ctx.setAttribute(id4Text(), "class", btnTextSytlye);
        this.initJaveScript();
    }

    public void setActive(boolean active) {
        IContext ctx = getContext();
        String act = active ? "true" : null;
        ctx.setAttribute(getId(), "active", act);
        String btnsytlye = active ? "btn_cm_act" : "btn_cm";
        ctx.setAttribute(getId(), "class", btnsytlye);
        this.initJaveScript();
    }

    public void initJaveScript() {
        getContext().sendJavaScript("initButton2Zero_js", "initGlossButton('" + getId() + "');");
    }

    /**
	 * set the path to the image and specify the offset for the label
	 */
    public void setSrc(String src, int iconWidth, int iconHeigth) {
        this.src = src;
        this.iconWidth = iconWidth;
        this.iconHeight = iconHeigth;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getSrc() {
        return src;
    }

    public void setVisible(boolean vis) {
        getContext().makeVisible(getId() + "_sspan", vis);
        processSetVisible(vis);
        this.initJaveScript();
    }

    public boolean isVisible() {
        return getContext().getValueAsBool(getId() + "_sspan" + IContext.DOT_VIS, true);
    }

    protected void init() {
        super.init();
        getPage().addHeaderJS(ResourceServlet.SERVLET_URL_PATTERN + "/glossbutton.js");
    }

    /**
	 * Change default behaviour from display:inline to display:block
	 * 
	 * @param block
	 */
    public void setDisplayBlock(boolean block) {
        this.block = block;
    }

    /**
	 * return the state of disabled of the button.
	 */
    public boolean isDisabled() {
        return getContext().getValueAsBool(getId() + ".disabled", false);
    }

    public void dispatch(ClientEvent event) {
        if (this.isDisabled()) {
            return;
        } else {
            super.dispatch(event);
        }
    }

    /**
	 * @return the displayMode
	 */
    public int getDisplayMode() {
        return displayMode;
    }

    /**
	 * @param displayMode
	 *            the displayMode to set
	 */
    public void setDisplayMode(int displayMode) {
        this.displayMode = displayMode;
    }

    /**
	 * redraws itself and all its subcomponents
	 * this is done by reevaluating the markerString and sending it as outer HTML
	 * this works only correct on components, which have exactly one node in HTML.
	 * override if your components doesn't comply.
	 */
    public void redraw() {
        getContext().outerHtml(getId() + "_sspan", "");
        getPage().registerListener(ServerEvent.EVENT_POSTDISPATCH, new IServerEventListener() {

            public void handle(ServerEvent event) {
                StringWriter stringWriter = new StringWriter();
                PrintWriter out = new PrintWriter(stringWriter);
                drawInternal(out);
                getContext().outerHtml(getId() + "_sspan", stringWriter.toString());
                getPage().removeListener(ServerEvent.EVENT_POSTDISPATCH, this);
            }
        });
    }

    protected void endHTML(PrintWriter out, String imgSrc, String text, boolean mode3D) {
    }

    public void setAlignment(int pos) {
    }
}
