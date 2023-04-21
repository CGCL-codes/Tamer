package net.infordata.ifw2.web.view;

import java.io.IOException;
import java.io.Writer;
import java.text.NumberFormat;
import java.text.ParseException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.infordata.ifw2.web.ctrl.FlowContext;
import net.infordata.ifw2.web.ctrl.IFlow;
import net.infordata.ifw2.web.ctrl.IFlowAsDialog;
import net.infordata.ifw2.web.dec.IDialogDecorator;
import net.infordata.ifw2.web.util.TxtResponseWrapper;
import org.apache.ecs.html.Div;

/**
 * It is used to render a dialog (ie an {@link IFlow} with an {@link IFlowAsDialog}
 * personality.<br>
 * Do not use it direcly, use 
 * {@link FlowContext#modalDialog(IFlowAsDialog, net.infordata.ifw2.web.ctrl.IDialogCallback)} instead.
 * @author valentino.proietti
 */
public class DialogRenderer implements IRenderer {

    private static final long serialVersionUID = 1L;

    private final IFlowAsDialog ivDialog;

    private IRenderer ivFooterRenderer = new JSPRenderer("@ifw2.modalFooter");

    public DialogRenderer(IFlowAsDialog dialog) {
        if (dialog == null) throw new IllegalArgumentException();
        ivDialog = dialog;
    }

    @Override
    public void render(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final RendererContext ctx = RendererContext.get();
        ContentPart cp = Util.wrapBegin(ivDialog.getId(), false, false);
        final TxtResponseWrapper rw = new TxtResponseWrapper(response);
        ctx.pushFlow(ivDialog.getFlow());
        Writer wt = rw.getWriter();
        ivDialog.onFrameRendering();
        String titleId = ctx.idToExtId(cp.getUniqueId() + "title");
        String contentId = ctx.idToExtId(cp.getUniqueId() + "content");
        IDialogDecorator decorator = ivDialog.getDecorator();
        {
            Div titleDiv = decorator.getTitleDiv(ivDialog);
            titleDiv.setID(titleId);
            String oldClass = titleDiv.getAttribute("class");
            titleDiv.setClass("TITLE" + (oldClass == null ? "" : " " + oldClass));
            new ECSRenderer(titleDiv).render(request, rw);
        }
        {
            Div contentDiv = decorator.getContentDiv(ivDialog);
            contentDiv.setID(contentId);
            String oldStyle = contentDiv.getAttribute("style");
            {
                String hh = (ivDialog.isMaximized()) ? "" + (ctx.getBrowserHeight() - 20 * 2) + "px" : ivDialog.getHeight();
                if (oldStyle == null) oldStyle = "";
                contentDiv.setStyle((hh == null ? oldStyle : oldStyle + ";height:" + hh));
            }
            String oldClass = contentDiv.getAttribute("class");
            contentDiv.setClass((ivDialog.isContentScrollable() ? "CONTENT SCROLLABLE" : "CONTENT") + (oldClass == null ? "" : " " + oldClass));
            new ECSRenderer(contentDiv).render(request, rw);
            ivFooterRenderer.render(request, rw);
        }
        wt.write("<script>");
        wt.write("{");
        String uniqueId = ctx.idToExtId(cp.getUniqueId());
        wt.write("var dlg=document.getElementById('" + uniqueId + "');");
        String style;
        if (ivDialog.isMaximized()) {
            int bww = ctx.getBrowserWidth();
            int bhh = ctx.getBrowserHeight();
            int w = bww - 20;
            int h = bhh - 20;
            String width = ivDialog.getWidth();
            if (width != null) {
                if (width.endsWith("px")) {
                    int wdt;
                    try {
                        wdt = NumberFormat.getIntegerInstance().parse(width).intValue();
                    } catch (ParseException ex) {
                        throw new RuntimeException(ex);
                    }
                    if (wdt > bww) width = bww + "px";
                }
            }
            style = "style='" + (width == null ? "width:" + w + "px;" : "width:" + width + ";") + "height:" + h + "px" + "'";
        } else {
            wt.write("$ifw.draggable('" + uniqueId + "','" + titleId + "');");
            style = "style='" + (ivDialog.getWidth() == null ? "" : "width:" + ivDialog.getWidth() + ";") + "'";
        }
        wt.write("}");
        wt.write("</script>");
        ctx.popFlow();
        char[] buf = rw.getContent();
        String attrs = "class='DIALOG IFW_POS' " + style;
        Util.wrapEnd("div", attrs, response.getWriter(), buf);
    }
}
