package abbot.swt.hierarchy;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.widgets.Caret;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.swt.widgets.Decorations;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tracker;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;
import abbot.swt.Log;
import abbot.swt.display.DisplayUtil;
import abbot.swt.display.Result;
import abbot.swt.hierarchy.text.WidgetFormatter;
import abbot.swt.tester.ActionFailedException;
import abbot.swt.utilities.Insist;
import abbot.swt.utilities.SWTUtil;

/**
 * An implementation of a {@link Hierarchy} of {@link Widget}s.
 */
public class WidgetHierarchyImpl extends HierarchyImpl<Widget> implements Hierarchy<Widget> {

    /**
	 * We cache a single instance as the default (the one for the default
	 * {@link Display}) for performance and to avoid unnecessary garbage
	 * creation.
	 */
    private static Hierarchy<Widget> Default;

    /**
	 * @return the default {@link Widget} {@link Hierarchy} (i.e., the one for
	 *         the default {@link Display})
	 */
    private static synchronized Hierarchy<Widget> getDefault() {
        if (Default == null) Default = new WidgetHierarchyImpl(Display.getDefault());
        return new WidgetHierarchyImpl(Display.getDefault());
    }

    /**
	 * Gets a {@link Widget} {@link Hierarchy} for a {@link Display}.
	 * 
	 * @param display
	 *            a {@link Display}
	 * @return the default {@link Widget} {@link Hierarchy} if
	 *         <code>display</code> is the default {@link Display}, a new
	 *         {@link Widget} {@link Hierarchy} for <code>display</code>
	 *         otherwise
	 */
    public static Hierarchy<Widget> getHierarchy(Display display) {
        if (display == Display.getDefault()) return getDefault();
        return new WidgetHierarchyImpl(Display.getDefault());
    }

    /**
	 * The {@link Display} the receiver is associated with.
	 */
    private final Display display;

    /**
	 * Constructs a new {@link Widget} {@link Hierarchy} for a {@link Display}.
	 * 
	 * @param display
	 *            a {@link Display}
	 */
    protected WidgetHierarchyImpl(Display display) {
        super();
        this.display = display;
    }

    /** @see Hierarchy#getRoots() */
    public Collection<Widget> getRoots() {
        Widget[] shells = DisplayUtil.syncExec(display, new Result<Widget[]>() {

            public Widget[] result() {
                return display.getShells();
            }
        });
        if (shells.length > 0) return Arrays.asList(shells);
        return Collections.emptyList();
    }

    /** @see HierarchyImpl */
    public Widget getParent(final Widget widget) {
        Insist.notNull(widget, "widget");
        return DisplayUtil.syncExec(widget.getDisplay(), new Result<Widget>() {

            public Widget result() {
                if (widget instanceof Control) return ((Control) widget).getParent();
                if (widget instanceof Caret) return ((Caret) widget).getParent();
                if (widget instanceof Menu) return ((Menu) widget).getParent();
                if (widget instanceof ScrollBar) return ((ScrollBar) widget).getParent();
                if (widget instanceof CoolItem) return ((CoolItem) widget).getParent();
                if (widget instanceof MenuItem) return ((MenuItem) widget).getParent();
                if (widget instanceof TabItem) return ((TabItem) widget).getParent();
                if (widget instanceof TableColumn) return ((TableColumn) widget).getParent();
                if (widget instanceof TableItem) return ((TableItem) widget).getParent();
                if (widget instanceof ToolItem) return ((ToolItem) widget).getParent();
                if (widget instanceof TreeColumn) return ((TreeColumn) widget).getParent();
                if (widget instanceof TreeItem) return ((TreeItem) widget).getParent();
                if (widget instanceof DragSource) return ((DragSource) widget).getControl().getParent();
                if (widget instanceof DropTarget) return ((DropTarget) widget).getControl().getParent();
                if (widget instanceof Tracker) Log.warning("cannot get a Tracker's parent");
                return null;
            }
        });
    }

    public Collection<Widget> getChildren(final Widget widget) {
        Insist.notNull(widget, "widget");
        if (widget.isDisposed()) return Collections.emptyList();
        return DisplayUtil.syncExec(widget.getDisplay(), new Result<List<Widget>>() {

            public List<Widget> result() {
                if (widget.isDisposed()) return Collections.emptyList();
                List<Widget> children = new ArrayList<Widget>();
                if (widget instanceof Shell) {
                    add(children, ((Shell) widget).getMenuBar());
                    if (!SWTUtil.IsCarbon) {
                        try {
                            Field menusField = Decorations.class.getDeclaredField("menus");
                            menusField.setAccessible(true);
                            Menu[] menus = (Menu[]) menusField.get(widget);
                            if (menus != null) {
                                for (Menu menu : menus) {
                                    if (menu != null) children.add(menu);
                                }
                            }
                        } catch (SecurityException e) {
                            throw new ActionFailedException(e);
                        } catch (NoSuchFieldException e) {
                            throw new ActionFailedException(e);
                        } catch (IllegalArgumentException e) {
                            throw new ActionFailedException(e);
                        } catch (IllegalAccessException e) {
                            throw new ActionFailedException(e);
                        }
                    }
                }
                if (widget instanceof Control) add(children, ((Control) widget).getMenu());
                if (widget instanceof Scrollable) {
                    Scrollable scrollable = (Scrollable) widget;
                    add(children, scrollable.getVerticalBar());
                    add(children, scrollable.getHorizontalBar());
                }
                if (widget instanceof TreeItem) add(children, ((TreeItem) widget).getItems());
                if (widget instanceof Menu) add(children, ((Menu) widget).getItems());
                if (widget instanceof MenuItem) add(children, ((MenuItem) widget).getMenu());
                if (widget instanceof Composite) {
                    add(children, ((Composite) widget).getChildren());
                    if (widget instanceof ToolBar) {
                        add(children, ((ToolBar) widget).getItems());
                    }
                    if (widget instanceof Table) {
                        Table table = (Table) widget;
                        add(children, table.getItems());
                        add(children, table.getColumns());
                    }
                    if (widget instanceof Tree) {
                        Tree tree = (Tree) widget;
                        add(children, tree.getColumns());
                        add(children, tree.getItems());
                    }
                    if (widget instanceof CoolBar) add(children, ((CoolBar) widget).getItems());
                    if (widget instanceof TabFolder) add(children, ((TabFolder) widget).getItems());
                    if (widget instanceof CTabFolder) add(children, ((CTabFolder) widget).getItems());
                }
                return children;
            }

            private void add(List<Widget> children, Widget newChild) {
                if (newChild != null) children.add(newChild);
            }

            private void add(List<Widget> children, Widget[] newChildren) {
                if (newChildren.length > 0) Collections.addAll(children, newChildren);
            }
        });
    }

    @Override
    protected String toString(final Widget widget) {
        return WidgetFormatter.format(widget);
    }
}
