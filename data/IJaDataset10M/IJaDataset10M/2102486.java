package vademecum.protocol;

/**
 * This class contains of integers coding the types
 * of ProtComponents. An additional method reduces
 * redundant code.
 *
 */
public class ProtCompConst {

    /**
	 * type representing {@link vademecum.protocol.comment.CommentPane}
	 */
    public static final int COMMENT = 0;

    /**
	 * type representing {@link vademecum.protocol.ProtComponent.ResultPane}
	 */
    public static final int RESULTPANE = 1;

    /**
	 * type representing {@link vademecum.protocol.ProtComponent.OrderPane},
	 * type = chapter
	 */
    public static final int ORDERPANE_CHAPTER = 2;

    /**
	 * type representing {@link vademecum.protocol.ProtComponent.OrderPane},
	 * type = section
	 */
    public static final int ORDERPANE_SECTION = 3;

    /**
	 * type representing {@link vademecum.protocol.ProtComponent.ProgressPane}
	 */
    public static final int PROGRESS_PANE = 4;

    /**
	 * Check if type is a OrderPane or not
	 * @param type The type to check
	 * @return true, if OrderPane_Chapter or OrderPane_Section, else false
	 */
    public static boolean isOrderPane(int type) {
        if (type == ORDERPANE_CHAPTER || type == ORDERPANE_SECTION) {
            return true;
        } else {
            return false;
        }
    }
}
