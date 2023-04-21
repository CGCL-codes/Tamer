package de.uni_leipzig.lots.webfrontend.views.datatable;

import de.uni_leipzig.lots.common.objects.Role;
import de.uni_leipzig.lots.webfrontend.views.ColumnType;
import de.uni_leipzig.lots.webfrontend.utils.DataTableWriter;
import de.uni_leipzig.lots.server.persist.UserRepository;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Required;
import java.io.IOException;
import java.util.Comparator;

/**
 * @author Alexander Kiel
 * @version $Id: RoleDataTableTemplate.java,v 1.7 2007/10/23 06:30:08 mai99bxd Exp $
 */
public class RoleDataTableTemplate extends BaseDataTableTemplate<Role, String> {

    private transient UserRepository userRepository;

    public RoleDataTableTemplate() {
        super(Role.class, String.class);
    }

    @Required
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public int getNumberOfColumns() {
        return 2;
    }

    @NotNull
    public String getDataType() {
        return "Role";
    }

    @NotNull
    public ColumnType getColumnType(int index) {
        switch(index) {
            case 0:
                return ColumnType.NOT_SORTABLE;
            case 1:
                return ColumnType.NOT_SORTABLE;
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
            default:
                throw new IndexOutOfBoundsException();
        }
    }

    public void writeColumnData(@NotNull DataTableWriter out, int index, @NotNull Role role, @NotNull RenderTargetType renderTargetType) throws IOException {
        switch(index) {
            case 0:
                if (RenderTargetType.XHTML == renderTargetType && out.supportsAppendActions()) {
                    out.appendAction("show", role, role.getLocalizedName(locale));
                } else {
                    out.write(role.getLocalizedName(locale));
                }
                break;
            case 1:
                if (out.supportsAppendNumber()) {
                    out.append(userRepository.getNumberOfWithRole(role));
                } else {
                    out.write(String.valueOf(userRepository.getNumberOfWithRole(role)));
                }
        }
    }

    public Comparator<Role> getComparator(int index) {
        switch(index) {
            case 0:
                return nameComparator;
            case 1:
                return sizeComparator;
            default:
                throw new IndexOutOfBoundsException();
        }
    }

    @Nullable
    public String getColumnCollationKey(int index, @NotNull Role role) {
        switch(index) {
            case 0:
                return toCollationKey(role.getLocalizedName(locale));
            case 1:
                return String.valueOf(userRepository.getNumberOfWithRole(role));
            default:
                throw new IndexOutOfBoundsException();
        }
    }

    private final transient Comparator<Role> nameComparator = new Comparator<Role>() {

        public int compare(Role role1, Role role2) {
            String name1 = role1.getLocalizedName(locale);
            String name2 = role2.getLocalizedName(locale);
            return collator.compare(name1, name2);
        }
    };

    private final transient Comparator<Role> sizeComparator = new Comparator<Role>() {

        public int compare(Role role1, Role role2) {
            int size1 = userRepository.getNumberOfWithRole(role1);
            int size2 = userRepository.getNumberOfWithRole(role2);
            return (size1 < size2) ? -1 : (size1 > size2) ? 1 : 0;
        }
    };
}
