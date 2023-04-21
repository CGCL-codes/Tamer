package java.awt.dnd;

/**
 * @author Michael Koch (konqueror@gmx.de)
 * @since 1.2
 *
 * Written using JDK 1.4.1 Online API
 * Status: JDK 1.4 complete
 */
public class DragSourceDropEvent extends DragSourceEvent {

    /**
   * Compatible with JDK 1.2+
   */
    private static final long serialVersionUID = -5571321229470821891L;

    private final int dropAction;

    private final boolean dropSuccess;

    public DragSourceDropEvent(DragSourceContext context) {
        super(context);
        this.dropAction = 0;
        this.dropSuccess = false;
    }

    public DragSourceDropEvent(DragSourceContext context, int dropAction, boolean dropSuccess) {
        super(context);
        this.dropAction = dropAction;
        this.dropSuccess = dropSuccess;
    }

    public DragSourceDropEvent(DragSourceContext context, int dropAction, boolean dropSuccess, int x, int y) {
        super(context, x, y);
        this.dropAction = dropAction;
        this.dropSuccess = dropSuccess;
    }

    public int getDropAction() {
        return dropAction & ((DragSourceContext) source).getSourceActions();
    }

    public boolean getDropSuccess() {
        return dropSuccess;
    }
}
