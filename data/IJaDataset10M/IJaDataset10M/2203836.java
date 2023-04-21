package net.sourceforge.dbtoolbox.validator;

import java.util.Collection;
import java.util.HashSet;
import net.sourceforge.dbtoolbox.model.ForeignKeyConstraintMD;
import net.sourceforge.dbtoolbox.model.IndexMD;
import net.sourceforge.dbtoolbox.model.TableMD;
import net.sourceforge.dbtoolbox.model.TabularColumnMD;

/**
 * Check that foreign key references index
 */
public class ReferencesIndexForeignKeyMDValidator extends AbstractMDValidator<ForeignKeyConstraintMD> {

    public ReferencesIndexForeignKeyMDValidator() {
        super("ReferencesIndexForeignKey", ForeignKeyConstraintMD.class);
    }

    /**
     *
     */
    private boolean containsColumns = false;

    public boolean isContainsColumns() {
        return containsColumns;
    }

    public void setContainsColumns(boolean containsColumns) {
        this.containsColumns = containsColumns;
    }

    public void validate(ForeignKeyConstraintMD foreignKey, Collection<MDValidatorMessage> validatorMessages) {
        TableMD refTable = foreignKey.getReferencedTable();
        boolean validated = false;
        for (IndexMD refIndex : refTable.getIndexes()) {
            Collection<TabularColumnMD> refFKColumns = new HashSet<TabularColumnMD>(foreignKey.getReferencedColumns());
            Collection<TabularColumnMD> refIndexColumns = new HashSet<TabularColumnMD>(refIndex.getColumns());
            if (containsColumns) {
                if (refIndexColumns.containsAll(refFKColumns)) {
                    validated = true;
                    break;
                }
            } else {
                if (refIndexColumns.equals(refFKColumns)) {
                    validated = true;
                    break;
                }
            }
        }
        if (!validated) validatorMessages.add(createMessage(foreignKey, "Foreign key referenced columns doesn't match with any index"));
    }
}
