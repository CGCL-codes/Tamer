package net.sourceforge.oradoc.dao;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import net.sourceforge.oradoc.db.AutoCompleteContext;
import net.sourceforge.oradoc.db.AutoCompleteItem;
import net.sourceforge.oradoc.db.DataProviderException;
import net.sourceforge.oradoc.db.DataProviderFactory;
import net.sourceforge.oradoc.db.IDataProvider;

public class OraView extends OraElement {

    protected String entityName;

    private Set<OraColumn> columns;

    public OraView(String entityName) throws DataProviderException, InterruptedException {
        this.entityName = entityName;
        IDataProvider provider = DataProviderFactory.getInstance().getProvider();
        AutoCompleteContext searchAcContext = new AutoCompleteContext();
        searchAcContext.setObjName(entityName);
        List<AutoCompleteItem> tableItems = provider.getObjectList(searchAcContext);
        if (tableItems.size() != 0) {
            AutoCompleteItem tableItem = tableItems.get(0);
            name = escapeHTML(tableItem.getObjName());
            parseComment(tableItem.getDoc());
            text = escapeHTML(tableItem.getText());
            iconFilename = tableItem.getIconFilename();
        }
    }

    public boolean hasColumns() throws DataProviderException, InterruptedException {
        return getColumns() != null && getColumns().size() != 0;
    }

    public Set<OraColumn> getColumns() throws DataProviderException, InterruptedException {
        if (columns == null) {
            columns = new TreeSet<OraColumn>(new Comparator<OraColumn>() {

                public int compare(OraColumn o1, OraColumn o2) {
                    return 1;
                }
            });
            IDataProvider provider = DataProviderFactory.getInstance().getProvider();
            AutoCompleteContext searchAcContext = new AutoCompleteContext();
            searchAcContext.setObjName(entityName);
            List<AutoCompleteItem> items = provider.getObjectItemList(searchAcContext);
            int columnNr = 0;
            for (AutoCompleteItem item : items) {
                columnNr++;
                String type = item.getObjType();
                if (AutoCompleteItem.TYPE_COLUMN.equalsIgnoreCase(type)) {
                    columns.add(new OraColumn(item));
                } else if (AutoCompleteItem.TYPE_PK_COLUMN.equalsIgnoreCase(type)) {
                    columns.add(new OraPKColumn(item));
                } else if (AutoCompleteItem.TYPE_FK_COLUMN.equalsIgnoreCase(type)) {
                    columns.add(new OraFKColumn(item));
                }
            }
        }
        return columns;
    }
}
