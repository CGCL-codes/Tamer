package de.uni_leipzig.lots.webfrontend.views.datatable;

import de.uni_leipzig.lots.common.objects.Group;
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
 * @version $Id: GroupDataTableTemplate.java,v 1.7 2007/10/23 06:30:10 mai99bxd Exp $
 */
public class GroupDataTableTemplate extends BaseDataTableTemplate<Group, Long> {

    private transient DateFormat dateFormatter;

    public GroupDataTableTemplate() {
        super(Group.class, Long.class);
    }

    @Override
    public void init() {
        super.init();
        dateFormatter = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, locale);
    }

    public int getNumberOfColumns() {
        return 5;
    }

    @NotNull
    public String getDataType() {
        return "Group";
    }

    @NotNull
    public ColumnType getColumnType(int index) {
        switch(index) {
            case 0:
                return ColumnType.STRING;
            case 1:
                return ColumnType.INT;
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
                out.write(bundle.getString("header.name"));
                break;
            case 1:
                out.write(bundle.getString("header.size"));
                break;
            case 2:
                out.write(bundle.getString("header.databases.size"));
                break;
            case 3:
                out.write(bundle.getString("header.creation"));
                break;
            case 4:
                break;
            default:
                throw new IndexOutOfBoundsException();
        }
    }

    public void writeColumnData(@NotNull DataTableWriter out, int index, @NotNull Group group, @NotNull RenderTargetType renderTargetType) throws IOException {
        switch(index) {
            case 0:
                if (RenderTargetType.XHTML == renderTargetType && out.supportsAppendActions()) {
                    out.appendAction("show", group, group.getName());
                } else {
                    out.write(group.getName());
                }
                break;
            case 1:
                if (out.supportsAppendNumber()) {
                    out.append(group.getMembers().size());
                } else {
                    out.write(String.valueOf(group.getMembers().size()));
                }
                break;
            case 2:
                if (out.supportsAppendNumber()) {
                    out.append(group.getDatabases().size());
                } else {
                    out.write(String.valueOf(group.getDatabases().size()));
                }
                break;
            case 3:
                if (out.supportsAppendDate()) {
                    out.append(group.getCreation());
                } else {
                    out.write(dateFormatter.format(group.getCreation()));
                }
                break;
            case 4:
                if (RenderTargetType.XHTML == renderTargetType && out.supportsAppendActions()) {
                    out.appendAction("edit", group);
                    out.appendAction("delete", group);
                }
                break;
        }
    }

    @Nullable
    public Comparator<Group> getComparator(int index) {
        switch(index) {
            case 0:
                return nameComparator;
            case 1:
                return SIZE_COMPARATOR;
            case 2:
                return DATABASE_SIZE_COMPARATOR;
            case 3:
                return CREATION_COMPARATOR;
            case 4:
                return null;
            default:
                throw new IndexOutOfBoundsException();
        }
    }

    @Nullable
    public String getColumnCollationKey(int index, @NotNull Group group) {
        switch(index) {
            case 0:
                return toCollationKey(group.getName());
            case 1:
                return String.valueOf(group.getMembers().size());
            case 2:
                return String.valueOf(group.getDatabases().size());
            case 3:
                return toCollationKey(group.getCreation());
            case 4:
                return null;
            default:
                throw new IndexOutOfBoundsException();
        }
    }

    private final transient Comparator<Group> nameComparator = new Comparator<Group>() {

        public int compare(Group group1, Group group2) {
            String name1 = group1.getName();
            String name2 = group2.getName();
            return collator.compare(name1, name2);
        }
    };

    private static final Comparator<Group> SIZE_COMPARATOR = new Comparator<Group>() {

        public int compare(Group group1, Group group2) {
            int size1 = group1.getMembers().size();
            int size2 = group2.getMembers().size();
            return (size1 < size2) ? -1 : (size1 > size2) ? 1 : 0;
        }
    };

    private static final Comparator<Group> DATABASE_SIZE_COMPARATOR = new Comparator<Group>() {

        public int compare(Group group1, Group group2) {
            int size1 = group1.getDatabases().size();
            int size2 = group2.getDatabases().size();
            return (size1 < size2) ? -1 : (size1 > size2) ? 1 : 0;
        }
    };

    protected static final Comparator<Group> CREATION_COMPARATOR = new Comparator<Group>() {

        public int compare(Group group1, Group group2) {
            Date creation1 = group1.getCreation();
            Date creation2 = group2.getCreation();
            return creation1.compareTo(creation2);
        }
    };
}
