package de.uni_leipzig.lots.webfrontend.views.datatable;

import de.uni_leipzig.lots.common.objects.training.TrainingPaper;
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
 * @version $Id: TrainingPaperDataTableTemplate.java,v 1.7 2007/10/23 06:30:11 mai99bxd Exp $
 */
public class TrainingPaperDataTableTemplate extends BaseDataTableTemplate<TrainingPaper, Long> {

    private transient DateFormat dateFormatter;

    public TrainingPaperDataTableTemplate() {
        super(TrainingPaper.class, Long.class);
    }

    @Override
    public void init() {
        super.init();
        dateFormatter = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, locale);
    }

    @Override
    @SuppressWarnings({ "AssignmentToNull" })
    public void release() {
        super.release();
        dateFormatter = null;
    }

    public int getNumberOfColumns() {
        return 5;
    }

    @NotNull
    public String getDataType() {
        return "TrainingPaper";
    }

    @NotNull
    public ColumnType getColumnType(int index) {
        switch(index) {
            case 0:
                return ColumnType.STRING;
            case 1:
                return ColumnType.STRING;
            case 2:
                return ColumnType.INT;
            case 3:
                return ColumnType.INT;
            case 4:
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
                out.write(bundle.getString("header.group.name"));
                break;
            case 1:
                out.write(bundle.getString("header.title"));
                break;
            case 2:
                out.write(bundle.getString("header.handoutDate"));
                break;
            case 3:
                out.write(bundle.getString("header.returnDate"));
                break;
            case 4:
                break;
            default:
                throw new IndexOutOfBoundsException();
        }
    }

    public void writeColumnData(@NotNull DataTableWriter out, int index, @NotNull TrainingPaper trainingPaper, @NotNull RenderTargetType renderTargetType) throws IOException {
        long now = System.currentTimeMillis();
        switch(index) {
            case 0:
                if (RenderTargetType.XHTML == renderTargetType && out.supportsAppendActions()) {
                    out.appendAction("show", trainingPaper.getGroup(), trainingPaper.getGroup().getName());
                } else {
                    out.write(trainingPaper.getGroup().getName());
                }
                break;
            case 1:
                if (RenderTargetType.XHTML == renderTargetType && out.supportsAppendActions()) {
                    out.appendAction("show", trainingPaper, trainingPaper.getTitle());
                } else {
                    out.write(trainingPaper.getTitle());
                }
                break;
            case 2:
                if (RenderTargetType.XHTML == renderTargetType && trainingPaper.getHandoutDate().getTime() < now && trainingPaper.getReturnDate().getTime() > now) {
                    out.write("<em class=\"red\" title=\"");
                    out.write(bundle.getString("message.handoutDate"));
                    out.write("\">");
                    out.write(dateFormatter.format(trainingPaper.getHandoutDate()));
                    out.write("</em>");
                } else {
                    if (out.supportsAppendDate()) {
                        out.append(trainingPaper.getHandoutDate());
                    } else {
                        out.write(dateFormatter.format(trainingPaper.getHandoutDate()));
                    }
                }
                break;
            case 3:
                if (RenderTargetType.XHTML == renderTargetType && trainingPaper.getReturnDate().getTime() < now) {
                    out.write("<em class=\"red\" title=\"");
                    out.write(bundle.getString("message.returnDate"));
                    out.write("\">");
                    out.write(dateFormatter.format(trainingPaper.getReturnDate()));
                    out.write("</em>");
                } else {
                    if (out.supportsAppendDate()) {
                        out.append(trainingPaper.getReturnDate());
                    } else {
                        out.write(dateFormatter.format(trainingPaper.getReturnDate()));
                    }
                }
                break;
            case 4:
                if (RenderTargetType.XHTML == renderTargetType && out.supportsAppendActions()) {
                    out.appendAction("edit", trainingPaper);
                    out.appendAction("delete", trainingPaper);
                }
        }
    }

    public Comparator<TrainingPaper> getComparator(int index) {
        switch(index) {
            case 0:
                return groupNameComparator;
            case 1:
                return titleComparator;
            case 2:
                return HANDOUT_DATE_COMPARATOR;
            case 3:
                return RETURN_DATE_COMPARATOR;
            default:
                throw new IndexOutOfBoundsException();
        }
    }

    @Nullable
    public String getColumnCollationKey(int index, @NotNull TrainingPaper trainingPaper) {
        switch(index) {
            case 0:
                return toCollationKey(trainingPaper.getGroup().getName());
            case 1:
                return toCollationKey(trainingPaper.getTitle());
            case 2:
                return String.valueOf(trainingPaper.getHandoutDate().getTime());
            case 3:
                return String.valueOf(trainingPaper.getReturnDate().getTime());
            case 4:
                return null;
            default:
                throw new IndexOutOfBoundsException();
        }
    }

    private final transient Comparator<TrainingPaper> titleComparator = new Comparator<TrainingPaper>() {

        public int compare(TrainingPaper trainingPaper1, TrainingPaper trainingPaper2) {
            String name1 = trainingPaper1.getTitle();
            String name2 = trainingPaper2.getTitle();
            return collator.compare(name1, name2);
        }
    };

    private final transient Comparator<TrainingPaper> groupNameComparator = new Comparator<TrainingPaper>() {

        public int compare(TrainingPaper trainingPaper1, TrainingPaper trainingPaper2) {
            String name1 = trainingPaper1.getGroup().getName();
            String name2 = trainingPaper2.getGroup().getName();
            return collator.compare(name1, name2);
        }
    };

    private static final Comparator<TrainingPaper> HANDOUT_DATE_COMPARATOR = new Comparator<TrainingPaper>() {

        public int compare(TrainingPaper trainingPaper1, TrainingPaper trainingPaper2) {
            Date date1 = trainingPaper1.getHandoutDate();
            Date date2 = trainingPaper2.getHandoutDate();
            return date1.compareTo(date2);
        }
    };

    private static final Comparator<TrainingPaper> RETURN_DATE_COMPARATOR = new Comparator<TrainingPaper>() {

        public int compare(TrainingPaper trainingPaper1, TrainingPaper trainingPaper2) {
            Date date1 = trainingPaper1.getReturnDate();
            Date date2 = trainingPaper2.getReturnDate();
            return date1.compareTo(date2);
        }
    };
}
