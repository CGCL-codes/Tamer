package de.uni_leipzig.lots.webfrontend.datatable.template;

import de.uni_leipzig.lots.common.objects.task.TaskCollection;
import de.uni_leipzig.lots.webfrontend.datatable.renderer.DataTableWriter;
import de.uni_leipzig.lots.webfrontend.views.ColumnType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Comparator;

/**
 * @author Alexander Kiel
 * @version $Id: TaskCollectionDataTableTemplate.java,v 1.9 2007/10/23 06:30:11 mai99bxd Exp $
 */
public class TaskCollectionDataTableTemplate extends BaseDataTableTemplate<TaskCollection> {

    private transient DateFormat dateFormatter;

    private static final long serialVersionUID = 5301499797688915399L;

    public TaskCollectionDataTableTemplate() {
        super(TaskCollection.class);
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
        return "TaskCollection";
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
                out.write(bundle.getString("header.title"));
                break;
            case 1:
                out.write(bundle.getString("header.numberOfTasks"));
                break;
            case 2:
                break;
            default:
                throw new IndexOutOfBoundsException();
        }
    }

    public void writeColumnData(@NotNull DataTableWriter out, int index, @NotNull TaskCollection taskCollection, @NotNull RenderTargetType renderTargetType) throws IOException {
        switch(index) {
            case 0:
                if (RenderTargetType.XHTML == renderTargetType && out.supportsAppendActions()) {
                    String title = bundle.getString("message.id") + ": " + taskCollection.getId() + ", " + bundle.getString("message.lastchange") + ": " + dateFormatter.format(taskCollection.getLastChange());
                    out.appendAction("show", taskCollection, title, taskCollection.getTitle());
                } else {
                    out.write(taskCollection.getTitle());
                }
                break;
            case 1:
                out.write(String.valueOf(taskCollection.getTasks().size()));
                break;
            case 2:
                if (RenderTargetType.XHTML == renderTargetType && out.supportsAppendActions()) {
                    out.appendAction("edit", taskCollection);
                    out.appendAction("delete", taskCollection);
                    out.appendAction("deleteDeep", taskCollection);
                    out.appendAction("handout", taskCollection);
                    out.appendAction("export", taskCollection);
                }
                break;
            default:
                throw new IndexOutOfBoundsException();
        }
    }

    @Nullable
    public Comparator<TaskCollection> getComparator(int index) {
        switch(index) {
            case 0:
                return titleComparator;
            case 1:
                return NUMBER_OF_TASKS_COMPARATOR;
            case 2:
                return null;
            default:
                throw new IndexOutOfBoundsException();
        }
    }

    private final transient Comparator<TaskCollection> titleComparator = new Comparator<TaskCollection>() {

        public int compare(TaskCollection taskCollection1, TaskCollection taskCollection2) {
            String name1 = taskCollection1.getTitle();
            String name2 = taskCollection2.getTitle();
            return collator.compare(name1, name2);
        }
    };

    private static final Comparator<TaskCollection> NUMBER_OF_TASKS_COMPARATOR = new Comparator<TaskCollection>() {

        public int compare(TaskCollection taskCollection1, TaskCollection taskCollection2) {
            int size1 = taskCollection1.getTasks().size();
            int size2 = taskCollection2.getTasks().size();
            return (size1 < size2) ? -1 : (size1 > size2) ? 1 : 0;
        }
    };
}
