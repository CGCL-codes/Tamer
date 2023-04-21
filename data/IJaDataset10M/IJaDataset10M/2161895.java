package graphics;

/**
 * AbstractCallable stellt eine naive Verfahrensweise zur Verfuegung, OpenGL
 * display list names zu erhalten und freizugeben.
 * 
 * @author Daniel
 * 
 */
public abstract class AbstractCallable implements Callable {

    private int list = 0;

    public AbstractCallable() {
        this.list = GlobalGL.getGL().glGenLists(1);
    }

    protected int getList() {
        return list;
    }

    /**
     * Falls es eine display list gibt, rufe sie auf.
     */
    public void call() {
        if (list != 0) {
            GlobalGL.getGL().glCallList(list);
        }
    }

    /**
     * Gebe diese display list wieder frei.
     */
    public void release() {
        GlobalGL.getGL().glDeleteLists(list, 1);
        list = 0;
    }
}
