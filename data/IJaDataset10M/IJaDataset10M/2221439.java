package de.uni_leipzig.lots.webfrontend.views.datatable;

import de.uni_leipzig.lots.common.objects.Right;
import de.uni_leipzig.lots.common.objects.task.Filter;
import de.uni_leipzig.lots.common.objects.training.TrainingPaper;
import de.uni_leipzig.lots.webfrontend.utils.DataTableWriter;
import de.uni_leipzig.lots.webfrontend.views.ColumnType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.Comparator;

/**
 * @author Alexander Kiel
 * @version $Id: FilterDataTableTemplate.java,v 1.8 2007/10/23 06:30:11 mai99bxd Exp $
 */
public class FilterDataTableTemplate extends BaseDataTableTemplate<Filter, Long> {

    private transient NumberFormat numberFormatter;

    public FilterDataTableTemplate() {
        super(Filter.class, Long.class);
    }

    @Override
    public void init() {
        super.init();
        numberFormatter = NumberFormat.getInstance(locale);
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
                out.write(bundle.getString("header.trainingpaper.size"));
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
                if (out.supportsAppendNumber()) {
                    out.append(filter.getTrainingPapers().size());
                } else {
                    Collection<TrainingPaper> trainingPapers = aclManager.filter(userContainer, Right.show, filter.getTrainingPapers(), TrainingPaper.class);
                    out.write(numberFormatter.format(trainingPapers.size()));
                }
                break;
            case 2:
                if (RenderTargetType.XHTML == renderTargetType && out.supportsAppendActions()) {
                    if (aclManager.hasRightOn(userContainer, Right.edit, filter)) {
                        out.appendAction("edit", filter);
                    }
                    if (aclManager.hasRightOn(userContainer, Right.delete, filter)) {
                        out.appendAction("delete", filter);
                    }
                }
                break;
            default:
                throw new IndexOutOfBoundsException();
        }
    }

    @Nullable
    public Comparator<Filter> getComparator(int index) {
        switch(index) {
            case 0:
                return nameComparator;
            case 1:
                return TRAINING_PAPER_SIZE_COMPARATOR;
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
                return String.valueOf(filter.getTrainingPapers().size());
            case 2:
                return null;
            default:
                throw new IndexOutOfBoundsException();
        }
    }

    private final transient Comparator<Filter> nameComparator = new Comparator<Filter>() {

        public int compare(Filter filter1, Filter filter2) {
            String name1 = filter1.getName();
            String name2 = filter2.getName();
            return collator.compare(name1, name2);
        }
    };

    private static final Comparator<Filter> TRAINING_PAPER_SIZE_COMPARATOR = new Comparator<Filter>() {

        public int compare(Filter filter1, Filter filter2) {
            int size1 = filter1.getTrainingPapers().size();
            int size2 = filter2.getTrainingPapers().size();
            return (size1 < size2) ? -1 : (size1 > size2) ? 1 : 0;
        }
    };
}
