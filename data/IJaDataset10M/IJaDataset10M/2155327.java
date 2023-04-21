package org.xaware.ide.xadev.gui.mapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.ListIterator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Item;
import org.xaware.ide.xadev.gui.BaseDNDTreeHandler;
import org.xaware.ide.xadev.gui.DNDHandler;
import org.xaware.ide.xadev.gui.DNDListHandler;
import org.xaware.ide.xadev.gui.DNDTableHandler;
import org.xaware.ide.xadev.gui.DNDTableViewer;
import org.xaware.ide.xadev.gui.DNDTreeHandler;
import org.xaware.ide.xadev.gui.DNDTreeViewer;
import org.xaware.ide.xadev.gui.actions.FindMappedElementsAction;

/**
 * A static class providing helper methods for performing mapped elements search.
 * 
 * @author satishk
 * @version 1.0
 */
public class MapperHelper {

    /** integer constant for zero. */
    public static final int ZERO_INDEX = 0;

    /** integer constant for -1 */
    public static final int NEGATIVE_INDEX = -1;

    /** Constant for empty ArrayList. */
    public static ArrayList EMPTY_ARRAYLIST = new ArrayList();

    /** MapperProcessor instance to find the mapped elements. */
    private static MapperProcessor currentMapperProcessor;

    /** Number of trials for re-obtaining new mappers if disposed. */
    private static int numberOfAttemptsToRefreshMappers = 0;

    /**
     * If the value is true sorting of mapped nodes is enabled, if it is false the sorting is disabled.
     */
    private static boolean sortingEnabled = true;

    /** Bold font instance. */
    private static final Font boldFont = new Font(Display.getCurrent(), Display.getCurrent().getSystemFont().getFontData()[0].getName(), Display.getCurrent().getSystemFont().getFontData()[0].getHeight(), SWT.BOLD);

    /**
     * returns the current mapper processor instance.
     * 
     * @return currentMapperProcessor currentProcessor instance.
     */
    public static MapperProcessor getCurrentMapperProcessor() {
        return currentMapperProcessor;
    }

    /**
     * sets the value of current mapper processor
     * 
     * @param processor
     *            processor that finds the mappings.
     */
    public static void setCurrentMapperProcessor(final MapperProcessor processor) {
        currentMapperProcessor = processor;
    }

    /**
     * sets the value of currrent mapper processor and rebuilds the mappings if the rebuilMappings parameter is true.
     * 
     * @param processor
     *            processor that assists in finding the mappings.
     * @param rebuildMappings
     *            If true the mappings are rebuilt else not.
     */
    public static void setCurrentMapperProcessor(final MapperProcessor processor, final boolean rebuildMappings) {
        currentMapperProcessor = processor;
        if (currentMapperProcessor != null && rebuildMappings) {
            currentMapperProcessor.rebuildMappingsHashTable();
        }
    }

    /**
     * Determines whether color needs to be changed or not.
     * 
     * @param element
     *            element to be checked in the list of mappedObjects
     * @param mappedObjects
     *            list of mapped Objects.
     * 
     * @return boolean value indicating wheter the property needs to be changed or not.
     */
    public static boolean changeProperty(final Object element, final ArrayList mappedObjects) {
        if ((!mappedObjects.isEmpty())) {
            return MapperHelper.findItem(mappedObjects, element);
        }
        return false;
    }

    /**
     * returns the bold font for the given element
     * 
     * @return Font Bold font.
     */
    public static Font getBoldFont() {
        return boldFont;
    }

    /**
     * calls the refresh method depending on the type of handler.
     * 
     * @param handler
     *            handler(tree/table/list) that needs to be refreshed.
     * @param allMappedItems
     *            list of all the mappedItems in the handler.
     * @param searchResultItems
     *            items list resulting from mappings search.
     * 
     * @return booelean value indicating if the mappings are refreshed and the mapper is focused.
     */
    public static boolean refreshMappingsAsPerType(final Object handler, final ArrayList allMappedItems, final ArrayList searchResultItems) {
        boolean focused = false;
        numberOfAttemptsToRefreshMappers = 0;
        if (handler != null && !refreshAllMappersIfDisposed(handler)) {
            focused = ((DNDHandler) handler).refreshMappings(allMappedItems, searchResultItems);
        }
        return focused;
    }

    /**
     * All the mappers are re-obtained and refreshed, if any mapper handler is disposed.
     * 
     * @param handler
     *            mapper handler instance.
     * 
     * @return boolean indication if any mapper handler is disposed.
     */
    private static boolean refreshAllMappersIfDisposed(final Object handler) {
        boolean disposed = false;
        if (handler != null && handler instanceof DNDHandler) {
            disposed = ((DNDHandler) handler).getControl().isDisposed();
        }
        if (disposed) {
            ++numberOfAttemptsToRefreshMappers;
            if (numberOfAttemptsToRefreshMappers == 1) {
                getCurrentMapperProcessor().refreshHandlerObjects();
                return refreshAllMappersIfDisposed(handler);
            }
        }
        return disposed;
    }

    /**
     * Finds the item in the mapper (tree/table/List handler) and returns it. returns null if item is not found.
     * 
     * @param mapperObject
     *            mapper object in which the item should be found.
     * @param item
     *            Item that should be found.
     * 
     * @return returns item if found, else returns null.
     * 
     */
    public static Object findItemAsPerType(final Object mapperObject, final Object item) {
        if (mapperObject != null) {
            revealItemAsPerType(mapperObject, item);
            if (mapperObject instanceof DNDTreeHandler) {
                return ((DNDTreeViewer) ((DNDTreeHandler) mapperObject).getTreeViewer()).findTreeItem(item);
            } else if (mapperObject instanceof DNDTableHandler) {
                return ((DNDTableViewer) ((DNDTableHandler) mapperObject).getTableViewer()).findTableItem(item);
            } else if (mapperObject instanceof DNDListHandler) {
                return ((DNDTableViewer) ((DNDListHandler) mapperObject).getListViewer()).findTableItem(item);
            }
        }
        return null;
    }

    /**
     * reveals the item as per type of the handler.
     * 
     * @param mapperObject
     *            mapper object in which the item should be found.
     * @param item
     *            Item that should be revealed.
     */
    private static void revealItemAsPerType(final Object mapperObject, final Object item) {
        if (mapperObject instanceof DNDHandler) {
            ((DNDHandler) mapperObject).getViewer().reveal(item);
        }
    }

    /**
     * Sorts the list of nodes. comparision is done based on which node occurs first in the table/tree/list.
     * 
     * @param mappedNodes
     *            list of nodes to be sorted.
     * @param mapperHandler
     *            mapper handler instance.
     * 
     */
    public static ArrayList sortMappedNodes(final ArrayList mappedNodes, final Object mapperHandler) {
        if (sortingEnabled && !mappedNodes.isEmpty()) {
            final DNDListHandler inputParamHandler = MapperHelper.getCurrentMapperProcessor().getInputParamHandler();
            final Comparator comparator = new Comparator() {

                public int compare(Object obj1, Object obj2) {
                    if (!(obj1 instanceof String) && !(obj2 instanceof String)) {
                        if (mapperHandler instanceof DNDTableHandler) {
                            DNDTableViewer tableViewer = (DNDTableViewer) ((DNDTableHandler) mapperHandler).getTableViewer();
                            return compare(tableViewer.getTable().indexOf(tableViewer.findTableItem(obj1)), tableViewer.getTable().indexOf(tableViewer.findTableItem(obj2)));
                        } else if (mapperHandler instanceof DNDTreeHandler || mapperHandler instanceof BaseDNDTreeHandler) {
                            DNDTreeViewer treeViewer = null;
                            if (mapperHandler instanceof DNDTreeHandler) {
                                treeViewer = (DNDTreeViewer) ((DNDTreeHandler) mapperHandler).getTreeViewer();
                            } else if (mapperHandler instanceof BaseDNDTreeHandler) {
                                treeViewer = ((BaseDNDTreeHandler) mapperHandler).getTreeViewer();
                            }
                            if (treeViewer != null) {
                                return compare(treeViewer.findTreeItem(obj1).getBounds().y, treeViewer.findTreeItem(obj2).getBounds().y);
                            }
                            return 1;
                        } else {
                            return 1;
                        }
                    } else if (obj1 instanceof String && obj2 instanceof String) {
                        DNDTableViewer viewer = null;
                        if (mapperHandler instanceof DNDListHandler && mapperHandler != inputParamHandler) {
                            viewer = (DNDTableViewer) ((DNDListHandler) mapperHandler).getListViewer();
                        } else if (inputParamHandler != null) {
                            viewer = (DNDTableViewer) inputParamHandler.getListViewer();
                        }
                        if (viewer != null) {
                            return compare(viewer.getTable().indexOf(viewer.findTableItem(obj1)), viewer.getTable().indexOf(viewer.findTableItem(obj2)));
                        }
                        return compare((String) obj1, (String) obj2);
                    } else if (obj1 instanceof String) {
                        return 1;
                    } else {
                        return -1;
                    }
                }

                public int compare(int index1, int index2) {
                    return index1 - index2;
                }

                public int compare(String str1, String str2) {
                    return str1.compareTo(str2);
                }
            };
            Collections.sort(mappedNodes, comparator);
        }
        return mappedNodes;
    }

    /**
     * This method is called when mouse hover event fired. Mappings search functionality will be invoked on the item on
     * which mouse hover event is fired.
     * 
     * @param mapperHandler
     *            mapper on which mouse hover event is fired.
     * @param event
     *            mouse hover event.
     */
    public static void searchMappingsWhenHovered(final Object mapperHandler, final MouseEvent event) {
        Object item = null;
        if (mapperHandler instanceof DNDTreeHandler) {
            item = ((DNDTreeHandler) mapperHandler).getTree().getItem(new Point(event.x, event.y));
        } else if (mapperHandler instanceof DNDTableHandler) {
            item = ((DNDTableHandler) mapperHandler).getTableViewer().getTable().getItem(new Point(event.x, event.y));
        } else if (mapperHandler instanceof DNDListHandler) {
            item = ((DNDListHandler) mapperHandler).getListViewer().getTable().getItem(new Point(event.x, event.y));
        }
        if (item == null) {
            return;
        }
        final Object selectedItem = ((Item) item).getData();
        new FindMappedElementsAction(true, event.data).searchMappings(mapperHandler, MapperHelper.getCurrentMapperProcessor(), selectedItem);
    }

    /**
     * Checks whether the current element is present in the given list of mappedObjects.
     * 
     * @param mappedObjects
     *            list of mappedObjects.
     * @param element
     *            element to be checked.
     * 
     * @return boolean true if the list of mappedObjects contains the given element else false.
     */
    public static boolean findItem(final ArrayList mappedObjects, final Object element) {
        for (final ListIterator iter = mappedObjects.listIterator(); iter.hasNext(); ) {
            final Object nextItem = iter.next();
            if (nextItem.hashCode() == element.hashCode()) {
                if (nextItem instanceof String && element instanceof String && !(nextItem == element)) {
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    /**
     * sets the sort variable to true or false.
     * 
     * @param sort
     *            boolean variable.
     */
    public static void setSortEnabled(final boolean sort) {
        sortingEnabled = sort;
    }
}
