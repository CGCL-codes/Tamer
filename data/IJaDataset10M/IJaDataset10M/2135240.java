package hu.scytha.action;

import hu.scytha.action.delete.DeleteAction;
import hu.scytha.filesearch.FileSearchAction;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.Separator;

public final class ActionFactory {

    private static ViewerAction fViewerAction = new ViewerAction();

    private static EditAction fEditAction = new EditAction();

    private static MakeDirAction fMakeDirAction = new MakeDirAction();

    private static PermissionAction fPermissionAction = new PermissionAction();

    private static ChangeOwnerAction fChangeOwnerAction = new ChangeOwnerAction();

    private static SymlinkAction fSymlinkAction = new SymlinkAction();

    private static DeleteAction fDeleteAction = new DeleteAction();

    private static FileSearchAction fFileSearchAction = new FileSearchAction();

    private static SelectAllAction fSelectAllAction = new SelectAllAction();

    private static DeselectAllAction fDeselectAllAction = new DeselectAllAction();

    private static InvertSelectionAction fInvertSelectionAction = new InvertSelectionAction();

    private static SelectionSameExtensionsAction fSameSelectionAction = new SelectionSameExtensionsAction();

    private static Separator fSeparator = new Separator();

    public static final int VIEWER = 1;

    public static final int EDIT = 2;

    public static final int MAKE_DIR = 3;

    public static final int PERMISSION = 4;

    public static final int CHANGE_OWNER = 5;

    public static final int SYMLINK = 6;

    public static final int DELETE = 7;

    public static final int FILE_SEARCH = 8;

    public static final int SELECT_ALL = 9;

    public static final int DESELECT_ALL = 10;

    public static final int INVERT_SELECTION = 11;

    public static final int SAME_EXT_SELECTION = 12;

    public static Action getAction(int actionKey) {
        switch(actionKey) {
            case VIEWER:
                return fViewerAction;
            case EDIT:
                return fEditAction;
            case MAKE_DIR:
                return fMakeDirAction;
            case PERMISSION:
                return fPermissionAction;
            case CHANGE_OWNER:
                return fChangeOwnerAction;
            case SYMLINK:
                return fSymlinkAction;
            case DELETE:
                return fDeleteAction;
            case FILE_SEARCH:
                return fFileSearchAction;
            case SELECT_ALL:
                return fSelectAllAction;
            case DESELECT_ALL:
                return fDeselectAllAction;
            case INVERT_SELECTION:
                return fInvertSelectionAction;
            case SAME_EXT_SELECTION:
                return fSameSelectionAction;
            default:
                return null;
        }
    }

    public static Separator getSeparator() {
        return fSeparator;
    }
}
