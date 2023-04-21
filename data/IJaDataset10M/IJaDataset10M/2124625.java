package de.uni_leipzig.lots.webfrontend.views.datatable;

import de.uni_leipzig.lots.common.objects.task.Filter;
import de.uni_leipzig.lots.webfrontend.utils.DataTableWriter;
import de.uni_leipzig.lots.webfrontend.views.ColumnType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Comparator;
import java.util.Date;

/**
 * @author Alexander Kiel
 * @version $Id: RecentlyChangedFilterDataTableTemplate.java,v 1.5 2007/10/23 06:30:09 mai99bxd Exp $
 */
public class RecentlyChangedFilterDataTableTemplate extends BaseDataTableTemplate<Filter, Long> {

    private transient DateFormat dateFormatter;

    public RecentlyChangedFilterDataTableTemplate() {
        super(Filter.class, Long.class);
    }

    @Override
    public void init() {
        super.init();
        dateFormatter = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, locale);
    }

    @Override
    @SuppressWarnings({ "AssignmentToNull" })
    public void release() {
        super.release();
        dateFormatter = null;
    }

    public int getNumberOfColumns() {
        return 3;
    }

    @NotNull
    public String getDataType() {
        return "Filter";
    }

    @NotNull
    public ColumnType getColumnType(int index) {
        switch(index) {
            case 0:
                return ColumnType.STRING;
            case 1:
                return ColumnType.INT;
            case 2:
                return ColumnType.ACTIONS;
            default:
                throw new IndexOutOfBoundsException();
        }
    }

    @Nullable
    public String getColumnCssClass(int index) {
        return null;
    }

    public void writeColumnHeader(@NotNull DataTableWriter out, int index) throws IOException {
        switch(index) {
            case 0:
                out.write(bundle.getString("header.name"));
                break;
            case 1:
                out.write(bundle.getString("header.lastchange"));
                break;
            case 2:
                break;
            default:
                throw new IndexOutOfBoundsException();
        }
    }

    public void writeColumnData(@NotNull DataTableWriter out, int index, @NotNull Filter filter, @NotNull RenderTargetType renderTargetType) throws IOException {
        switch(index) {
            case 0:
                if (RenderTargetType.XHTML == renderTargetType && out.supportsAppendActions()) {
                    out.appendAction("show", filter, filter.getName());
                } else {
                    out.write(filter.getName());
                }
                break;
            case 1:
                if (out.supportsAppendDate()) {
                    out.append(filter.getLastChange());
                } else {
                    out.write(dateFormatter.format(filter.getLastChange()));
                }
                break;
            case 2:
                if (RenderTargetType.XHTML == renderTargetType && out.supportsAppendActions()) {
                    out.appendAction("edit", filter);
                    out.appendAction("delete", filter);
                }
        }
    }

    @Nullable
    public Comparator<Filter> getComparator(int index) {
        switch(index) {
            case 0:
                return nameComparator;
            case 1:
                return LAST_CHANGE_COMPARATOR;
            case 2:
                return null;
            default:
                throw new IndexOutOfBoundsException();
        }
    }

    @Nullable
    public String getColumnCollationKey(int index, @NotNull Filter filter) {
        switch(index) {
            case 0:
                return toCollationKey(filter.getName());
            case 1:
                return toCollationKey(filter.getLastChange());
            case 2:
                return null;
            default:
                throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public DataTableSortSetting getDefaultSortSetting() {
        return new DataTableSortSetting(1, false);
    }

    private final transient Comparator<Filter> nameComparator = new Comparator<Filter>() {

        public int compare(Filter filter1, Filter filter2) {
            String name1 = filter1.getName();
            String name2 = filter2.getName();
            return collator.compare(name1, name2);
        }
    };

    protected static final Comparator<Filter> LAST_CHANGE_COMPARATOR = new Comparator<Filter>() {

        public int compare(Filter filter1, Filter filter2) {
            Date lastChange1 = filter1.getLastChange();
            Date lastChange2 = filter2.getLastChange();
            return lastChange1.compareTo(lastChange2);
        }
    };
}
