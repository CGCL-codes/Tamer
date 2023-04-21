package org.webguitoolkit.ui.controls.dialog;

import java.io.PrintWriter;
import java.io.StringWriter;
import org.apache.commons.lang.StringUtils;
import org.webguitoolkit.ui.base.WebGuiFactory;
import org.webguitoolkit.ui.controls.Page;
import org.webguitoolkit.ui.controls.container.Canvas;
import org.webguitoolkit.ui.controls.container.ICanvas;
import org.webguitoolkit.ui.controls.container.IHtmlElement;
import org.webguitoolkit.ui.controls.event.ClientEvent;
import org.webguitoolkit.ui.controls.event.IActionListener;
import org.webguitoolkit.ui.controls.form.IButton;
import org.webguitoolkit.ui.controls.form.ILabel;
import org.webguitoolkit.ui.controls.layout.ITableLayout;
import org.webguitoolkit.ui.controls.util.Window2ActionAdapter;

public class DialogUtil {

    /**
	 * 
	 */
    public static final String DIALOG_IMG_ID = "DIALOG_IMG_ID_";

    public static final String CONFIRMATION_DIALOG_OK_BUTTON_ID = "CONFIRMATION_DIALOG_OK_BUTTON_ID";

    public static final String ERROR = "error";

    public static final String INFO = "info";

    public static final String WARN = "warn";

    /**
	 * function for sending messages to the client.
	 * 
	 * @param page
	 *            the page element for the message
	 * @param msg
	 *            the message string
	 * @param msgType
	 *            the message type, error, info or warning
	 * @param customOk
	 *            the action listener for handle the ok button
	 */
    public static void sendMessage(Page page, String msg, String msgType, IActionListener customOk) {
        WebGuiFactory factory = WebGuiFactory.getInstance();
        DynamicDialog dialog = new DynamicDialog(page);
        ICanvas infoCanvas = dialog.getWindow();
        addControls(infoCanvas, msg, msgType, customOk);
        page.getContext().moveDown(infoCanvas.getId() + ".centerDiv");
    }

    /**
	 * simple info box without icon
	 */
    public static void infoBox(Page body, String msg, IActionListener customOk) {
        sendMessage(body, msg, null, customOk);
    }

    /**
	 * this function places the message box directly into the context,
	 * useful for functions in the post dispatch phase.
	 * Maybe we can remove this method somehow.
	 * 
	 * @param page
	 *            the page element for the message
	 * @param msg
	 *            the message string
	 * @param msgType
	 *            the message type, error, info or warning
	 * @param customOk
	 *            the action listener for handle the ok button
	 */
    public static void sendMessageDirectly(Page page, String msg, String msgType, IActionListener customOk) {
        page.getContext().appendHtml(page.getId(), "");
        WebGuiFactory factory = WebGuiFactory.getInstance();
        ICanvas infoCanvas = factory.createCanvas(page);
        infoCanvas.setDisplayMode(Canvas.DISPLAY_MODE_WINDOW_MODAL);
        infoCanvas.setDragable(true);
        addControls(infoCanvas, msg, msgType, customOk);
        page.getContext().moveDown(infoCanvas.getId() + ".centerDiv");
        StringWriter sw = new StringWriter();
        PrintWriter out = new PrintWriter(sw);
        ((Canvas) infoCanvas).drawCanvas(out);
        page.getContext().appendHtml(page.getId(), sw.toString());
    }

    ;

    protected static void addControls(final ICanvas infoCanvas, String msgKey, String msgType, IActionListener customOk) {
        WebGuiFactory factory = WebGuiFactory.getInstance();
        infoCanvas.setDragable(true);
        infoCanvas.setDisplayMode(Canvas.DISPLAY_MODE_WINDOW_MODAL);
        infoCanvas.setCssClass("wgtPopup wgtAlertBox");
        if (StringUtils.isNotEmpty(msgType)) infoCanvas.setTitle("msgbox." + msgType + "@" + msgType.substring(0, 1).toUpperCase() + msgType.substring(1));
        if (customOk == null) {
            customOk = new IActionListener() {

                public void onAction(ClientEvent event) {
                    infoCanvas.setVisible(false);
                    infoCanvas.getPage().getContext().removeNode(infoCanvas.getId());
                    infoCanvas.remove();
                }
            };
        }
        infoCanvas.setWindowActionListener(new Window2ActionAdapter(customOk));
        ITableLayout tableLayout = factory.createTableLayout(infoCanvas);
        tableLayout.getEcsTable().setStyle("width: 100%;");
        if (StringUtils.isNotEmpty(msgType)) {
            IHtmlElement img = factory.createHtmlElement(tableLayout, "img", DIALOG_IMG_ID + msgType);
            img.setAttribute("src", "./images/wgt/icons/msg_icon_" + msgType + ".gif");
            tableLayout.getCurrentCell().setStyle("text-align: center; width: 40px;");
        }
        ILabel infoLabel = factory.createLabel(tableLayout, msgKey);
        tableLayout.getCurrentCell().setStyle("text-align: left;");
        tableLayout.newLine();
        IButton infoButton = factory.createButton(tableLayout, null, "button.ok@Ok", "button.ok@Ok", customOk, CONFIRMATION_DIALOG_OK_BUTTON_ID);
        if (StringUtils.isNotEmpty(msgType)) tableLayout.getCurrentCell().setColSpan(2);
        tableLayout.getCurrentCell().setStyle("text-align: center;");
        infoButton.focus();
    }
}
