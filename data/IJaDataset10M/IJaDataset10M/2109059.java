package gatchan.phpparser.project.itemfinder;

import common.gui.itemfinder.ItemFinder;
import gatchan.phpparser.project.Project;
import gatchan.phpparser.project.ProjectManager;
import gatchan.phpparser.sidekick.PHPSideKickParser;
import net.sourceforge.phpdt.internal.compiler.ast.ClassDeclaration;
import net.sourceforge.phpdt.internal.compiler.ast.ClassHeader;
import net.sourceforge.phpdt.internal.compiler.ast.MethodDeclaration;
import net.sourceforge.phpdt.internal.compiler.ast.PHPDocument;
import net.sourceforge.phpdt.internal.compiler.parser.Outlineable;
import org.gjt.sp.jedit.Buffer;
import org.gjt.sp.jedit.View;
import org.gjt.sp.jedit.io.VFSManager;
import org.gjt.sp.jedit.jEdit;
import org.gjt.sp.jedit.textarea.JEditTextArea;
import org.gjt.sp.util.Log;
import javax.swing.*;
import java.util.ArrayList;

/**
 * @author Matthieu Casanova
 */
public class PHPItemFinder implements ItemFinder<PHPItem> {

    public static final int CLASS_MODE = PHPItem.CLASS;

    public static final int METHOD_MODE = PHPItem.METHOD;

    public static final int INTERFACE_MODE = PHPItem.INTERFACE;

    public static final int FIELD_MODE = PHPItem.FIELD;

    public static final int ALL_MODE = CLASS_MODE ^ METHOD_MODE ^ INTERFACE_MODE ^ FIELD_MODE;

    public static final int PROJECT_SCOPE = 0;

    public static final int FILE_SCOPE = 1;

    private final PHPItemCellRenderer listCellRenderer;

    private final ProjectManager projectManager;

    private final SimpleListModel listModel;

    private int scope;

    private View view;

    private String label;

    private String lastSearch;

    public PHPItemFinder() {
        listCellRenderer = new PHPItemCellRenderer();
        projectManager = ProjectManager.getInstance();
        listModel = new SimpleListModel();
    }

    public void init(View view, int mode, int scope) {
        this.view = view;
        this.scope = scope;
        lastSearch = null;
        listModel.clear();
        listModel.setMode(mode);
        if (mode == CLASS_MODE) {
            label = jEdit.getProperty("gatchan-phpparser.itemfinder.searchLabel.class");
        } else if (mode == INTERFACE_MODE) {
            label = jEdit.getProperty("gatchan-phpparser.itemfinder.searchLabel.interface");
        } else if (mode == FIELD_MODE) {
            label = jEdit.getProperty("gatchan-phpparser.itemfinder.searchLabel.field");
        } else if (mode == METHOD_MODE) {
            label = jEdit.getProperty("gatchan-phpparser.itemfinder.searchLabel.method");
        } else {
            label = jEdit.getProperty("gatchan-phpparser.itemfinder.searchLabel.default");
        }
    }

    public String getLabel() {
        return "PHP Item Finder";
    }

    public int getWidth() {
        return 400;
    }

    public ListModel getModel() {
        return listModel;
    }

    public ListCellRenderer getListCellRenderer() {
        return listCellRenderer;
    }

    public void updateList(String s) {
        long start = System.currentTimeMillis();
        Project project = projectManager.getProject();
        if (project != null) {
            QuickAccessItemFinder quickAccess = project.getQuickAccess();
            String searchText = s.toLowerCase();
            listCellRenderer.setSearchString(searchText);
            java.util.List itemContaining;
            int currentSearchLength = searchText.length();
            if (currentSearchLength != 0 && ((scope == PROJECT_SCOPE && QuickAccessItemFinder.getIndexLength() > currentSearchLength) || (lastSearch == null || lastSearch.length() == 0 || currentSearchLength < lastSearch.length() || !searchText.startsWith(lastSearch)))) {
                if (scope == PROJECT_SCOPE) {
                    long quickAccessStart = System.currentTimeMillis();
                    itemContaining = new ArrayList(quickAccess.getItemContaining(searchText));
                    Log.log(Log.DEBUG, QuickAccessItemFinder.class, System.currentTimeMillis() - quickAccessStart + " ms");
                } else {
                    Buffer buffer = jEdit.getActiveView().getBuffer();
                    PHPDocument document = (PHPDocument) buffer.getProperty(PHPSideKickParser.PHPDOCUMENT_PROPERTY);
                    itemContaining = new ArrayList();
                    if (document != null) {
                        for (int i = 0; i < document.size(); i++) {
                            Outlineable o = document.get(i);
                            if (o instanceof ClassDeclaration) {
                                ClassDeclaration classDeclaration = (ClassDeclaration) o;
                                ClassHeader classHeader = classDeclaration.getClassHeader();
                                itemContaining.addAll(classHeader.getMethodsHeaders());
                                itemContaining.add(classHeader);
                            } else if (o instanceof MethodDeclaration) {
                                MethodDeclaration methodDeclaration = (MethodDeclaration) o;
                                itemContaining.add(methodDeclaration.getMethodHeader());
                            }
                        }
                    }
                }
                if (itemContaining.isEmpty()) {
                    listModel.clear();
                } else {
                    listModel.setList(itemContaining, searchText);
                }
            } else {
                listModel.filter(searchText);
            }
            lastSearch = searchText;
        }
        long end = System.currentTimeMillis();
        Log.log(Log.DEBUG, this, (end - start) + "ms");
    }

    public void selectionMade(final PHPItem item) {
        if (item != null) {
            String path = item.getPath();
            final Buffer buffer = jEdit.openFile(view, path);
            VFSManager.runInAWTThread(new Runnable() {

                public void run() {
                    JEditTextArea textArea = jEdit.getActiveView().getTextArea();
                    int caretPosition = buffer.getLineStartOffset(item.getBeginLine() - 1) + item.getBeginColumn() - 1;
                    textArea.moveCaretPosition(caretPosition);
                    Log.log(Log.MESSAGE, this, "Moving to line " + (item.getBeginLine() - 1) + ' ' + caretPosition);
                }
            });
        }
    }
}
