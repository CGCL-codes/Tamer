package edu.yale.csgp.vitapad.graph;

import java.util.EventObject;

/**
 * An object encapsulating removals from and additions to the graph.
 * It is comprised of the source of the request, the type of request
 * and the object that is to be added/removed.
 * 
 * @see edu.yale.graph.GraphListener
 * @see edu.yale.graph.DecorationListener
 */
public class _GraphEvent extends EventObject {

    /**
     * The command issued when a vertex wishes to be added.
     */
    public static final int PRE_ADD_VERT = 1001;

    /**
     * The command issued when a vertex wishes to be removed.
     */
    public static final int PRE_DEL_VERT = 1002;

    /**
     * The command issued when an edge wishes to be added.
     */
    public static final int PRE_ADD_EDGE = 1003;

    /**
     * The command issued when an edge vertex wishes to be removed.
     */
    public static final int PRE_DEL_EDGE = 1004;

    /**
     * The command issued when a decoration wishes to be added.
     */
    public static final int PRE_ADD_DEC = 1005;

    /**
     * The command issued when a decoration wishes to be removed.
     */
    public static final int PRE_DEL_DEC = 1006;

    /**
     * The command issued after a vertex has been added.
     */
    public static final int AFT_ADD_VERT = 1007;

    /**
     * The command issued after a vertex has been removed.
     */
    public static final int AFT_DEL_VERT = 1008;

    /**
     * The command issued after an edge has been added.
     */
    public static final int AFT_ADD_EDGE = 1009;

    /**
     * The command issued after an edge has been removed.
     */
    public static final int AFT_DEL_EDGE = 1010;

    /**
     * The command issued after a decoration has been added.
     */
    public static final int AFT_ADD_DEC = 1011;

    /**
     * The command issued after a decoration has been removed.
     */
    public static final int AFT_DEL_DEC = 1012;

    private int command;

    private GraphElement element;

    public static final int PRE_REP_VERT = 1013;

    public static final int POST_REP_VERT = 1014;

    public static final int POST_REP_EDGE = 1016;

    public static final int PRE_REP_EDGE = 1015;

    public static final int PRE_REP_DEC = 1018;

    public static final int POST_REP_DEC = 1017;

    /**
     * Constructs an request specifying the source of the request, the
     * request itself and the object of the request.
     * 
     * @param source
     *            The object requesting that an element be added or
     *            removed.
     * @param command
     *            The command that is being issued.
     * @param element
     *            The element that is to be added or removed
     */
    public _GraphEvent(Object source, int command, GraphElement element) {
        super(source);
        this.command = command;
        this.element = element;
    }

    /**
     * Returns the command the request is issuing.
     */
    public int getCommand() {
        return command;
    }

    /**
     * Returns the element that is to be added or removed.
     */
    public GraphElement getElement() {
        return element;
    }

    /**
     * @param string
     */
    public void setCommand(int command) {
        this.command = command;
    }

    /**
     * @param element
     */
    public void setElement(GraphElement element) {
        this.element = element;
    }
}
