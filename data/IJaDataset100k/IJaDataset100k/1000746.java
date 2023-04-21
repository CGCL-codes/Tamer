package de.uni_leipzig.lots.webfrontend.datatable.template;

import de.uni_leipzig.lots.server.services.AclManager;
import de.uni_leipzig.lots.webfrontend.container.UserContainer;
import de.uni_leipzig.lots.webfrontend.datatable.renderer.DataTableWriter;
import de.uni_leipzig.lots.webfrontend.views.ColumnType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.io.IOException;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Locale;

/**
 * A template for rendering data tables.
 * <p/>
 * Classes which implement this interface are not thread save. A renderer should create a template before each
 * rendering.
 *
 * @author Alexander Kiel
 * @version $Id: DataTableTemplate.java,v 1.10 2007/10/23 06:30:11 mai99bxd Exp $
 */
public interface DataTableTemplate<E> extends Serializable {

    /**
     * Sets the locale for the actual rendering.
     * <p/>
     * The output and sorting may use the locale.
     *
     * @param locale the locale to use for the actual rendering
     */
    void setLocale(@NotNull Locale locale);

    /**
     * Sets the <tt>UserContainer</tt> of the current user.
     * <p/>
     * The <tt>UserContainer</tt> can be used for authorisation.
     *
     * @param userContainer the <tt>UserContainer</tt> of the current user
     */
    void setUserContainer(@NotNull UserContainer userContainer);

    void setAclManager(@NotNull AclManager aclManager);

    /**
     * Returns the number of columns which the data table should have.
     * <p/>
     * All methods with a index parameter will return meaningful data for a index which is smaller than the
     * number of columns and will throw a {@link IndexOutOfBoundsException IndexOutOfBoundsException} if the
     * index is equal or greater than the number of columns.
     *
     * @return the number of columns which the data table should have
     */
    int getNumberOfColumns();

    /**
     * Returns the name or an id of this template or object type name.
     * <p/>
     * TODO: Noch nicht ganz klar, wie die Methode heißen soll und was der Name bedeuten soll.
     *
     * @return the name or an id of this template or object type name.
     */
    @NotNull
    String getDataType();

    @NotNull
    Class<E> getEntityType();

    /**
     * Returns the type of the column with the given index.
     * <p/>
     * See {@link ColumnType} for more information.
     *
     * @param index the index of the column
     * @return the type of the column with the given index
     * @throws IndexOutOfBoundsException if the index is out of range (index &lt; 0 || index &gt;= {@link
     *                                   #getNumberOfColumns()})
     */
    @NotNull
    ColumnType getColumnType(int index) throws IndexOutOfBoundsException;

    /**
     * Returns the custom CSS class of the column with the given index.
     *
     * @param index the index of the column
     * @return the custom CSS class of the column with the given index or <tt>null</tt> if the column has no
     *         custom CSS class
     * @throws IndexOutOfBoundsException if the index is out of range (index &lt; 0 || index &gt;= {@link
     *                                   #getNumberOfColumns()})
     */
    @Nullable
    String getColumnCssClass(int index) throws IndexOutOfBoundsException;

    /**
     * Writes the header of the column with the given index to the given writer.
     *
     * @param out   the writer to which the header should be written
     * @param index the index of the column
     * @throws IOException               the <tt>IOException</tt> of the writer
     * @throws IndexOutOfBoundsException if the index is out of range (index &lt; 0 || index &gt;= {@link
     *                                   #getNumberOfColumns()})
     */
    void writeColumnHeader(@NotNull DataTableWriter out, int index) throws IOException, IndexOutOfBoundsException;

    /**
     * Writes the data of the column with the given index to the given writer.
     *
     * @param out              the writer to which the header should be written
     * @param index            the index of the column
     * @param entity           the entity which fills the row
     * @param renderTargetType the type for which the output should be rendered
     * @throws IOException               the <tt>IOException</tt> of the writer
     * @throws IndexOutOfBoundsException if the index is out of range (index &lt; 0 || index &gt;= {@link
     *                                   #getNumberOfColumns()})
     */
    void writeColumnData(@NotNull DataTableWriter out, int index, @NotNull E entity, @NotNull RenderTargetType renderTargetType) throws IOException, IndexOutOfBoundsException;

    @NotNull
    String getId(int rowIndex) throws IndexOutOfBoundsException;

    /**
     * Returns <tt>true</tt> if the row of the given entity should included in the output.
     *
     * @param entity           the entity which row should included or not
     * @param renderTargetType the type for which the output sould be rendered
     * @return <tt>true</tt> if the row of the given entity should included in the output; <tt>false</tt>
     *         otherwise
     */
    boolean isIncludeRow(@NotNull E entity, @NotNull RenderTargetType renderTargetType);

    /**
     * Returns the comparator of the column with the given index.
     * <p/>
     * Should return <tt>null</tt> if the column is not sortable. Not sortable columns have to have the type
     * which is {@link de.uni_leipzig.lots.webfrontend.views.ColumnType#isSortable() not sortable}.
     *
     * @param index the index of the column
     * @return the comparator of the column with the given index or <tt>null</tt> if the column is not
     *         sortable
     * @throws IndexOutOfBoundsException if the index is out of range (index &lt; 0 || index &gt;= {@link
     *                                   #getNumberOfColumns()})
     */
    @Nullable
    Comparator<E> getComparator(int index) throws IndexOutOfBoundsException;

    @Nullable
    DataTableSortSetting getDefaultSortSetting();
}
