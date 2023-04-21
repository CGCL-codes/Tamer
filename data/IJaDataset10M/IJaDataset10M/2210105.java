package javango.db;

public class PaginatorWidget<T> {

    public static final String TEXT_NEXT = "Next";

    public static final String TEXT_FIRST = "First";

    public static final String TEXT_PREV = "Prev";

    public static final String TEXT_LAST = "Last";

    public static final String TEXT_NORESULTS = "No Results";

    public static final String TEXT_NBSP = "&nbsp;";

    public static final String TEXT_SHOWING = "Showing";

    private final String query_string;

    private final int count;

    private final int numPages;

    private final int perPage;

    private final Page<T> page;

    public PaginatorWidget(Paginator<T> paginator, int pagenumber, String query_string) {
        this(paginator.getCount(), paginator.getNumPages(), pagenumber, paginator.getPerPage(), paginator.getPage(pagenumber), query_string);
    }

    public PaginatorWidget(int count, int numPages, int pagenumber, int perPage, Page<T> page, String query_string) {
        this.numPages = numPages;
        this.count = count;
        this.perPage = perPage;
        this.query_string = query_string;
        this.page = page;
    }

    public String asTable() {
        StringBuilder out = new StringBuilder();
        out.append("<table width='90%'>\n<tr>");
        displayFirst(out, count);
        displayPrev(out, page);
        displayDescription(out, page, count, perPage);
        displayNext(out, page);
        displayLast(out, count, numPages);
        out.append("</tr>\n</table>\n");
        return out.toString();
    }

    /**
	 * displays the
	 * <td> with the correct link and description. If both params are null the
	 * description will not be hyper linked.
	 * 
	 * @param desc
	 * @param page
	 *            Integer The first row (null to keep the current first row)
	 * @param max
	 *            Integer The max number of rows per page (null to keep the
	 *            current value)
	 */
    private void showLink(StringBuilder out, String desc, Integer page) {
        out.append("<td align='center'>");
        if (page != null) {
            out.append(String.format("<a href=\"./?p=%d&%s\">", page, query_string == null ? "" : query_string));
            out.append(desc);
            out.append("</a>");
        } else {
            out.append(desc);
        }
        out.append("</td>");
    }

    private void displayFirst(StringBuilder out, int count) {
        if (count == 0) {
            showLink(out, TEXT_FIRST, null);
        } else {
            showLink(out, TEXT_FIRST, 1);
        }
    }

    private void displayPrev(StringBuilder out, Page<T> pagination) {
        if (!pagination.getHasPrevious()) {
            showLink(out, TEXT_PREV, null);
        } else {
            showLink(out, TEXT_PREV, pagination.getPreviousPageNumber());
        }
    }

    private void displayNext(StringBuilder out, Page<T> pagination) {
        if (!pagination.getHasNext()) {
            showLink(out, TEXT_NEXT, null);
        } else {
            showLink(out, TEXT_NEXT, pagination.getNextPageNumber());
        }
    }

    private void displayLast(StringBuilder out, int count, int numPages) {
        if (count == 0) {
            showLink(out, TEXT_LAST, null);
        } else {
            showLink(out, TEXT_LAST, numPages);
        }
    }

    private void displayDescription(StringBuilder out, Page<T> page, int count, int perPage) {
        int first = page.getStartIndex();
        int max = perPage;
        int total = count;
        out.append("<td width='75%' align='center'>");
        if (total == 0) {
            out.append(TEXT_NORESULTS);
        } else if (first == 0 && max == 0 && total == 0) {
            out.append(TEXT_NBSP);
        } else if (first != 0 && max != 0 && total != 0) {
            int end = page.getEndIndex();
            if (end > total) end = total;
            out.append(TEXT_SHOWING + " " + first + " - " + end + " of " + total);
        } else if (first != 0 && total != 0) {
            out.append(TEXT_SHOWING + " " + first + " - " + total + " of " + total);
        } else {
            out.append("&nbsp;");
        }
        out.append("</td>");
    }

    public String getQuery_string() {
        return query_string;
    }
}
