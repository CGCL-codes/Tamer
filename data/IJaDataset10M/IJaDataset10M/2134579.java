package alphahr.ui.event;

/**
 * @version     0.00 29 Mar 2011
 * @author      Andrey Pudov
 */
public class ViewAccountEvent extends java.util.EventObject {

    private Object source = null;

    public ViewAccountEvent(Object source) {
        super(source);
        this.source = source;
    }

    public Object getSource() {
        return source;
    }
}
