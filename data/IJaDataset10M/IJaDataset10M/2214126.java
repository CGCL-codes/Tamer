package org.mshillin.servlet.action;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import org.apache.velocity.context.Context;
import org.mshillin.util.StringCompare;

public abstract class CommandSearch extends Command {

    public static int DEFAULT_LIST_SIZE = 20;

    protected String currSortDir;

    protected String currSortCol;

    protected String subAction;

    protected String startIndex;

    /** ****************************************************
 * Get the List of items, using the PS Client classes.
 ***************************************************** */
    protected abstract List getList() throws Exception;

    /** ****************************************************
 * A default sort implementation.
 * Sorts the list items based on their toString() value.
 * Support for sorting on the item members must be implemented in the
 * sub-class.
 ***************************************************** */
    protected List sortList(String sortCol, String sortDir) {
        List list = null;
        Object obj = request.getSession().getAttribute("list");
        if (obj != null) {
            list = (List) obj;
            Collections.sort(list, new StringCompare(sortDir));
        }
        return list;
    }

    /** ************************************************
 * Get the list from the Session, then get a subList
 * staring at the specified index.
 * @param idx - Start index
 * @param ctx - Current Context
 ************************************************* */
    protected List getSubList(int idx, Context ctx) {
        List list = null;
        Object obj = request.getSession().getAttribute("list");
        if (obj != null) {
            list = (List) obj;
            list = getSubList(list, idx, ctx);
        }
        return list;
    }

    /** ************************************************
 * Return a portion of the list starting at <code>startIndex</code>.
 * The number of items is obtained from ControllerServlet
 * (loaded as a property)
 * <br>
 *  Also sets flags in the Context for back/prev operations.
 *  @param list - Complete list of Objects
 *  @param startIndex - Starting index
 *  @param ctx - Current Context
 *
 * @return Sublist ofthe provided list.
 ************************************************* */
    protected List getSubList(List list, int startIndex, Context ctx) {
        int listSize = list.size();
        ctx.put("count", Integer.toString(listSize));
        ctx.put("hasResults", Boolean.TRUE);
        int endIndex = startIndex + DEFAULT_LIST_SIZE;
        if (startIndex > (DEFAULT_LIST_SIZE - 1)) {
            ctx.put("showPrev", Boolean.TRUE);
        }
        if (endIndex < listSize) {
            ctx.put("showNext", Boolean.TRUE);
        }
        if (endIndex > listSize) {
            endIndex = listSize;
        }
        ctx.put("startIndex", Integer.toString((startIndex + 1)));
        ctx.put("endIndex", Integer.toString((endIndex)));
        ctx.put("currSortDir", currSortDir);
        ctx.put("currSortCol", currSortCol);
        return list.subList(startIndex, endIndex);
    }

    /** ****************************************************
 * Handle a Next or a Previous request.
 * Get a subList from the list in the session.
 * After this method returns, the Context is fully populated
 * and the template can be returned to the client.
 ***************************************************** */
    protected void handlePrevNext(String subAction, Context ctx) {
        int index = 0;
        if (subAction.equals("next")) {
            try {
                String currIndex = request.getParameter("startIndex");
                index = Integer.parseInt(currIndex);
            } catch (Exception e) {
            }
        } else if (subAction.equals("prev")) {
            try {
                String currIndex = request.getParameter("startIndex");
                index = Integer.parseInt(currIndex);
                index = index - DEFAULT_LIST_SIZE - 1;
            } catch (Exception e) {
            }
        }
        List subList = getSubList(index, ctx);
        ctx.put("listall", subList);
    }

    /** ****************************************************
 * Prepare the list to be shown to the client.
 * Get a sub list if necessary and set all of the appropriate
 * values in the context (to allow paging).
 ***************************************************** */
    protected void prepareList(List list, String startIndex, Context ctx) {
        if (list == null || list.size() < 1) {
            ctx.put("hasResults", Boolean.FALSE);
        } else {
            int listSize = list.size();
            if (startIndex == null) {
                startIndex = "0";
            }
            if (listSize > DEFAULT_LIST_SIZE) {
                int fromIndex = Integer.parseInt(startIndex);
                ctx.put("listall", getSubList(list, fromIndex, ctx));
            } else {
                startIndex = "1";
                ctx.put("count", Integer.toString(listSize));
                ctx.put("hasResults", Boolean.TRUE);
                ctx.put("listall", list);
                ctx.put("startIndex", startIndex);
                ctx.put("endIndex", Integer.toString(listSize));
            }
        }
    }

    /** ****************************************************
 * Set the appropriate SortTag in the Context to
 * the correct direction for subsequent searches.
 * @param sortDir - Requested sort Direction
 * @param sortTagName - Tag name for the current sort
 * @param ctx - Current Context.
 ***************************************************** */
    protected void setSortTag(String sortDir, String sortTagName, Context ctx) {
        if (sortDir != null || sortDir.length() > 0) {
            if (sortDir.equalsIgnoreCase("Ascending")) {
                ctx.put(sortTagName, "Descending");
            } else {
                ctx.put(sortTagName, "Ascending");
            }
        }
        ctx.put("currSortDir", sortDir);
        ctx.put("currSortCol", sortTagName);
    }
}
