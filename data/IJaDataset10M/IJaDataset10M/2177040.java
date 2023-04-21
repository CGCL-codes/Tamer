package ar.com.kyol.jet.client;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.Timer;

/**
 * The Class Print.
 */
@SuppressWarnings("deprecation")
public class Print {

    /** If true, use a Timer instead of DeferredCommand to print the internal fram. */
    public static boolean USE_TIMER = false;

    /** Time in seconds to wait before printing the internal frame when using Timer. */
    public static int TIMER_DELAY = 2;

    /**
     * It.
     */
    public static native void it();

    /**
     * It.
     *
     * @param obj the obj
     */
    public static void it(UIObject obj) {
        it("", obj);
    }

    /**
     * It.
     *
     * @param element the element
     */
    public static void it(Element element) {
        it("", element);
    }

    /**
     * It.
     *
     * @param style the style
     * @param obj the obj
     */
    public static void it(String style, UIObject obj) {
        it(style, obj.getElement());
    }

    /**
     * It.
     *
     * @param style the style
     * @param element the element
     */
    public static void it(String style, Element element) {
        it("", style, element);
    }

    /**
     * It.
     *
     * @param docType the doc type
     * @param style the style
     * @param element the element
     */
    public static void it(String docType, String style, Element element) {
        it(docType, style, DOM.toString(element));
    }

    /**
     * It.
     *
     * @param docType the doc type
     * @param style the style
     * @param it the it
     */
    public static void it(String docType, String style, String it) {
        it(docType + "<html>" + "<head>" + "<meta http-equiv=\"Content-Type\"		content=\"text/html; charset=utf-8\">" + "<meta http-equiv=\"Content-Style-Type\"	content=\"text/css\">" + style + "</head>" + "<body>" + it + "</body>" + "</html>");
    }

    /**
     * It.
     *
     * @param html the html
     */
    public static void it(String html) {
        try {
            buildFrame(html);
            if (USE_TIMER) {
                Timer timer = new Timer() {

                    public void run() {
                        printFrame();
                    }
                };
                timer.schedule(TIMER_DELAY * 1000);
            } else {
                DeferredCommand.addCommand(new Command() {

                    public void execute() {
                        printFrame();
                    }
                });
            }
        } catch (Throwable exc) {
            Window.alert(exc.getMessage());
        }
    }

    /**
     * Builds the frame.
     *
     * @param html the html
     */
    public static native void buildFrame(String html);

    /**
     * Prints the frame.
     */
    public static native void printFrame();
}
