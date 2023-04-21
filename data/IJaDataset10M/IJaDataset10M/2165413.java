package de.uni_leipzig.lots.webfrontend.datatable.template;

import de.uni_leipzig.lots.common.objects.task.Task;
import de.uni_leipzig.lots.common.objects.task.Theme;
import static de.uni_leipzig.lots.common.util.StringUtil.join;
import de.uni_leipzig.lots.webfrontend.datatable.renderer.DataTableWriter;
import de.uni_leipzig.lots.webfrontend.views.ColumnType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author Alexander Kiel
 * @version $Id: TaskDataTableTemplate.java,v 1.9 2007/10/23 06:30:10 mai99bxd Exp $
 */
public class TaskDataTableTemplate extends BaseDataTableTemplate<Task> {

    private transient DateFormat dateFormatter;

    private static final long serialVersionUID = -1621433448385143942L;

    public TaskDataTableTemplate() {
        super(Task.class);
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
        return "Task";
    }

    @NotNull
    public ColumnType getColumnType(int index) {
        switch(index) {
            case 0:
                return ColumnType.STRING;
            case 1:
                return ColumnType.NOT_SORTABLE;
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
                out.write(bundle.getString("header.themes"));
                break;
            case 2:
                break;
            default:
                throw new IndexOutOfBoundsException();
        }
    }

    public void writeColumnData(@NotNull DataTableWriter out, int index, @NotNull Task task, @NotNull RenderTargetType renderTargetType) throws IOException {
        switch(index) {
            case 0:
                if (RenderTargetType.XHTML == renderTargetType && out.supportsAppendActions()) {
                    String title = bundle.getString("message.id") + ": " + task.getTaskId() + ", " + bundle.getString("message.lastchange") + ": " + dateFormatter.format(task.getLastChange());
                    out.appendAction("show", task, title, task.getTitle());
                } else {
                    out.write(task.getTitle());
                }
                break;
            case 1:
                List<Theme> themes = new ArrayList<Theme>(task.getThemes());
                Collections.sort(themes, themeNameComparator);
                if (RenderTargetType.XHTML == renderTargetType && out.supportsAppendActions()) {
                    out.write("<ul class=\"comma-line\">");
                    for (Theme theme : themes) {
                        out.write("<li>");
                        out.appendAction("show", theme, theme.getName());
                        out.write("</li>");
                    }
                    out.write("</ul>");
                } else {
                    out.write(join(themes, ", "));
                }
                break;
            case 2:
                if (RenderTargetType.XHTML == renderTargetType && out.supportsAppendActions()) {
                    out.appendAction("edit", task);
                    out.appendAction("delete", task);
                }
                break;
            default:
                throw new IndexOutOfBoundsException();
        }
    }

    @Nullable
    public Comparator<Task> getComparator(int index) {
        switch(index) {
            case 0:
                return titleComparator;
            case 1:
                return null;
            case 2:
                return null;
            default:
                throw new IndexOutOfBoundsException();
        }
    }

    private final transient Comparator<Task> titleComparator = new Comparator<Task>() {

        public int compare(Task task1, Task task2) {
            String name1 = task1.getTitle();
            String name2 = task2.getTitle();
            return collator.compare(name1, name2);
        }
    };

    private final transient Comparator<Theme> themeNameComparator = new Comparator<Theme>() {

        public int compare(Theme theme1, Theme theme2) {
            String name1 = theme1.getName();
            String name2 = theme2.getName();
            return collator.compare(name1, name2);
        }
    };
}
