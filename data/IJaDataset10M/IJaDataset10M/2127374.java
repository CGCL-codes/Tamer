package edu.hawaii.ics.csdl.jupiter.ui.view.editor;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import edu.hawaii.ics.csdl.jupiter.ReviewException;
import edu.hawaii.ics.csdl.jupiter.ReviewI18n;
import edu.hawaii.ics.csdl.jupiter.ReviewPlugin;
import edu.hawaii.ics.csdl.jupiter.event.ReviewEvent;
import edu.hawaii.ics.csdl.jupiter.event.ReviewIssueModelEvent;
import edu.hawaii.ics.csdl.jupiter.file.FileResource;
import edu.hawaii.ics.csdl.jupiter.model.reviewissue.ReviewIssue;
import edu.hawaii.ics.csdl.jupiter.model.reviewissue.ReviewIssueModel;
import edu.hawaii.ics.csdl.jupiter.model.reviewissue.ReviewIssueModelManager;
import edu.hawaii.ics.csdl.jupiter.ui.menu.UndoReviewIssueManager;
import edu.hawaii.ics.csdl.jupiter.ui.view.table.ReviewTableView;
import edu.hawaii.ics.csdl.jupiter.ui.view.table.ReviewTableViewAction;
import edu.hawaii.ics.csdl.jupiter.util.JupiterLogger;
import edu.hawaii.ics.csdl.jupiter.util.ResourceBundleKey;

/**
 * Provides the action for the <code>ReviewEditorView</code>.
 * 
 * @author Takuya Yamashita
 * @version $Id: ReviewEditorViewAction.java 177 2010-06-23 01:21:27Z jsakuda $
 */
public class ReviewEditorViewAction {

    /** Jupiter logger */
    private static JupiterLogger log = JupiterLogger.getLogger();

    /** The go to action to go back to the previous review issues */
    public static final Action UNDO;

    /** The go to action to jump to the specified source code */
    public static final Action GOTO;

    /** The next action to forward the list of the view table. */
    public static final Action NEXT;

    /** The previous action to backward the list of the view table. */
    public static final Action PREVIOUS;

    /** The save action to save the current editing issue. */
    public static final Action SAVE;

    /** The clear action to clear all fields in the current editing issue. */
    public static final Action CLEAR;

    static {
        UNDO = new Action("", Action.AS_DROP_DOWN_MENU) {

            public void run() {
            }

            public IMenuCreator getMenuCreator() {
                return new IMenuCreator() {

                    /** Ignore to implement this method. */
                    public void dispose() {
                    }

                    /**
           * Returns the menu for this pull down action.
           * 
           * @param parent the parent of <code>Control</code> instance.
           * @return The <code>Menu</code> instance to be filled with the set of the review mode.
           */
                    public Menu getMenu(Control parent) {
                        return UndoReviewIssueManager.getInstance().createPulldownMenu(new Menu(parent));
                    }

                    /**
           * Ignore to implement this method.
           * 
           * @param menu The <code>Menu</code> instance.
           * @return The <code>Menu</code> instance.
           */
                    public Menu getMenu(Menu menu) {
                        return null;
                    }
                };
            }
        };
        String undoLabelKey = "ReviewTableView.action.label.undo";
        UNDO.setText(ReviewI18n.getString(undoLabelKey));
        String undoToolTipKey = "ReviewTableView.action.toolTip.undo";
        UNDO.setToolTipText(ReviewI18n.getString(undoToolTipKey));
        ISharedImages sharedImage = PlatformUI.getWorkbench().getSharedImages();
        UNDO.setImageDescriptor(ReviewPlugin.createImageDescriptor("icons/undo.gif"));
        GOTO = new Action() {

            public void run() {
                ReviewEditorView view = ReviewEditorView.getInstance();
                if (view != null) {
                    ReviewIssue reviewIssue = view.getReviewIssue();
                    String targetFile = reviewIssue.getTargetFile();
                    if (!targetFile.equals("")) {
                        IProject project = FileResource.getProject(reviewIssue.getReviewIFile());
                        IFile targetIFile = project.getFile(targetFile);
                        String lineNumberString = reviewIssue.getLine();
                        boolean isNumber = ((lineNumberString != null) && !lineNumberString.equals(""));
                        int lineNumber = Integer.parseInt((isNumber) ? lineNumberString : "0");
                        FileResource.goToLine(targetIFile, lineNumber);
                    }
                }
                int type = ReviewEvent.TYPE_COMMAND;
                int kind = ReviewEvent.KIND_GOTO;
                ReviewPlugin.getInstance().notifyListeners(type, kind);
            }
        };
        String gotoLabelKey = "ReviewTableView.action.label.goto";
        GOTO.setText(ReviewI18n.getString(gotoLabelKey));
        String gotoToolTipKey = "ReviewTableView.action.toolTip.goto";
        GOTO.setToolTipText(ReviewI18n.getString(gotoToolTipKey));
        sharedImage = PlatformUI.getWorkbench().getSharedImages();
        GOTO.setImageDescriptor(ReviewPlugin.createImageDescriptor("icons/goto.gif"));
        NEXT = new Action() {

            public void run() {
                processSave();
                ReviewTableView view = ReviewTableView.getActiveView();
                Table table = view.getTable();
                int index = table.getSelectionIndex();
                int length = table.getItemCount();
                log.debug("selection index: " + index);
                if (index != -1 && index < length - 1) {
                    table.select(index + 1);
                    ReviewTableViewAction.NOTIFY_EDITOR.run();
                    int type = ReviewEvent.TYPE_COMMAND;
                    int kind = ReviewEvent.KIND_NEXT;
                    ReviewPlugin.getInstance().notifyListeners(type, kind);
                }
            }
        };
        String nextLabelKey = "Next";
        NEXT.setText(ReviewI18n.getString(nextLabelKey));
        String nextToolTipKey = "Next";
        NEXT.setToolTipText(ReviewI18n.getString(nextToolTipKey));
        NEXT.setImageDescriptor(ReviewPlugin.createImageDescriptor("icons/down.gif"));
        PREVIOUS = new Action() {

            public void run() {
                processSave();
                ReviewTableView view = ReviewTableView.getActiveView();
                Table table = view.getTable();
                int index = table.getSelectionIndex();
                int length = table.getItemCount();
                if (index != -1 && index > 0) {
                    table.select(index - 1);
                    ReviewTableViewAction.NOTIFY_EDITOR.run();
                    int type = ReviewEvent.TYPE_COMMAND;
                    int kind = ReviewEvent.KIND_PREVIOUS;
                    ReviewPlugin.getInstance().notifyListeners(type, kind);
                }
            }
        };
        String previousLabelKey = "Previous";
        PREVIOUS.setText(ReviewI18n.getString(previousLabelKey));
        String previousToolTipKey = "Previous";
        PREVIOUS.setToolTipText(ReviewI18n.getString(previousToolTipKey));
        PREVIOUS.setImageDescriptor(ReviewPlugin.createImageDescriptor("icons/up.gif"));
        SAVE = new Action() {

            public void run() {
                log.debug("save was called.");
                processSave();
                int type = ReviewEvent.TYPE_COMMAND;
                int kind = ReviewEvent.KIND_SAVE;
                ReviewPlugin.getInstance().notifyListeners(type, kind);
            }
        };
        String addLabelKey = "Save";
        SAVE.setText(ReviewI18n.getString(addLabelKey));
        String addToolTipKey = "Save";
        SAVE.setToolTipText(ReviewI18n.getString(addToolTipKey));
        SAVE.setImageDescriptor(ReviewPlugin.createImageDescriptor("icons/save.gif"));
        CLEAR = new Action() {

            public void run() {
                ReviewEditorView editorView = ReviewEditorView.getInstance();
                editorView.clearAllFields();
                int type = ReviewEvent.TYPE_COMMAND;
                int kind = ReviewEvent.KIND_CLEAR;
                ReviewPlugin.getInstance().notifyListeners(type, kind);
            }
        };
        String clearLabelKey = "Clear";
        CLEAR.setText(ReviewI18n.getString(clearLabelKey));
        String clearToolTipKey = "Clear";
        CLEAR.setToolTipText(ReviewI18n.getString(clearToolTipKey));
        CLEAR.setImageDescriptor(ReviewPlugin.createImageDescriptor("icons/clear.gif"));
    }

    /**
   * Processes saving an issue.
   * 
   * @return <code>true</code> if successfully saved. <code>false</code> otherwise.
   */
    private static boolean processSave() {
        ReviewEditorView editorView = ReviewEditorView.getActiveView();
        if (editorView == null) {
            return false;
        }
        ReviewIssue savingReviewIssue = editorView.getReviewIssue();
        String activeTabNameKey = editorView.getActiveTabNameKey();
        if (savingReviewIssue == null) {
            return false;
        }
        ReviewIssueModelManager reviewIssueModelManager = ReviewIssueModelManager.getInstance();
        ReviewIssueModel reviewIssueModel = reviewIssueModelManager.getCurrentModel();
        if (reviewIssueModel.contains(savingReviewIssue.getIssueId())) {
            ReviewIssue originalReviewIssue = reviewIssueModel.get(savingReviewIssue.getIssueId());
            if (!originalReviewIssue.contentEquals(savingReviewIssue)) {
                try {
                    originalReviewIssue.setReviewIssue(savingReviewIssue);
                    reviewIssueModel.notifyListeners(ReviewIssueModelEvent.EDIT);
                } catch (ReviewException e) {
                    e.printStackTrace();
                }
            }
        } else {
            reviewIssueModel.add(savingReviewIssue);
            ReviewTableView view = ReviewTableView.getActiveView();
            if (view == null) {
                view = ReviewTableView.bringViewToTop();
            }
            view.getViewer().refresh();
            Table table = view.getTable();
            table.select(0);
            updateNextPreviousIcon();
            reviewIssueModel.notifyListeners(ReviewIssueModelEvent.ADD);
        }
        return true;
    }

    /**
   * Checks if the <code>ReviewIssue</code> instance is valid. Throws
   * <code>CodeReviewException</code> with error message if it is not valid.
   * 
   * @param codeReview the <code>ReviewIssue</code> instance.
   * @param activeTabNameKey the active tab name key.
   * @throws ReviewException if the instance is not valid.
   */
    private static void validateCodeReview(ReviewIssue codeReview, String activeTabNameKey) throws ReviewException {
        String message = "";
        if (activeTabNameKey.equals(ResourceBundleKey.PHASE_INFIVIDUAL)) {
            if (codeReview.getType().getKey().equals(ResourceBundleKey.ITEM_KEY_UNSET)) {
                message += "\n" + ReviewI18n.getString(ResourceBundleKey.COLUMN_HEADER_TYPE);
            }
            if (codeReview.getSeverity().getKey().equals(ResourceBundleKey.ITEM_KEY_UNSET)) {
                message += "\n" + ReviewI18n.getString(ResourceBundleKey.COLUMN_HEADER_SEVERITY);
            }
            if (!message.equals("")) {
                throw new ReviewException(message);
            }
        } else if (activeTabNameKey.equals(ResourceBundleKey.PHASE_TEAM)) {
            if (codeReview.getAssignedTo().equals("")) {
                message += "\n" + ReviewI18n.getString(ResourceBundleKey.COLUMN_HEADER_ASSGINED_TO);
            }
            if (codeReview.getResolution().getKey().equals(ResourceBundleKey.ITEM_KEY_UNSET)) {
                message += "\n" + ReviewI18n.getString(ResourceBundleKey.COLUMN_HEADER_RESOLUTION);
            }
            if (!message.equals("")) {
                throw new ReviewException(message);
            }
        }
    }

    /**
   * Updates the next and previous icon.
   */
    public static void updateNextPreviousIcon() {
        ReviewTableView tableView = ReviewTableView.getActiveView();
        if (tableView != null) {
            Table table = tableView.getTable();
            int index = table.getSelectionIndex();
            if (index != -1) {
                int length = table.getItemCount();
                NEXT.setEnabled(index < length - 1);
                PREVIOUS.setEnabled(index > 0);
            }
        }
    }
}
