package testingapplication.table;

import com.softaspects.jsf.component.table.Definitions;
import com.softaspects.jsf.component.table.Table;
import javax.faces.context.FacesContext;
import java.io.Serializable;

public class ExampleRowSelectionScopeSelectorManagedBean implements Serializable {

    private String currentScope = "Table";

    private Table findTable() {
        FacesContext context = FacesContext.getCurrentInstance();
        return (Table) context.getViewRoot().findComponent("tableSelectedRowsOperations:tableSelectedRowsOperationsForm").findComponent("table2");
    }

    public String getCurrentScope() {
        return currentScope;
    }

    public void setCurrentScope(String currentScope) {
        this.currentScope = currentScope;
    }

    public String changeRowSelectionScope() {
        Table table = findTable();
        if (isCurrentScopePage()) {
            table.setRowSelectionScope(Definitions.ROW_SELECTION_SCOPE_TABLE);
            setCurrentScope("Table");
        } else {
            table.setRowSelectionScope(Definitions.ROW_SELECTION_SCOPE_PAGE);
            setCurrentScope("Page");
        }
        return null;
    }

    public boolean isCurrentScopePage() {
        return "page".equalsIgnoreCase(currentScope);
    }
}
