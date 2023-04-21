package mswing.table;

import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;

/**
 * TableColumnModel par d�faut des tables utilis� pour d�finir le modelIndex � columnCount
 * (0,1,2...).
 *
 * @author Emeric Vernat
 */
class MDefaultTableColumnModel extends DefaultTableColumnModel {

    private static final long serialVersionUID = 1L;

    private static final MTableHeaderRenderer TABLE_HEADER_RENDERER = new MTableHeaderRenderer();

    private static final int DEFAULT_TABLE_COLUMN_PREFERRED_WIDTH = new TableColumn().getPreferredWidth();

    /**
	 * Constructeur.
	 */
    MDefaultTableColumnModel() {
        super();
    }

    /**
	 * Ajoute une colonne (en d�finissant le modelIndex � columnCount).
	 *
	 * @param tableColumn javax.swing.table.TableColumn
	 */
    public void addColumn(TableColumn tableColumn) {
        tableColumn.setModelIndex(getColumnCount());
        tableColumn.setHeaderRenderer(TABLE_HEADER_RENDERER);
        if (tableColumn.getPreferredWidth() == DEFAULT_TABLE_COLUMN_PREFERRED_WIDTH) {
            tableColumn.setPreferredWidth(tableColumn.getWidth());
        }
        super.addColumn(tableColumn);
    }

    /**
	 * D�finit les colonnes par un tableau d'identifiers (correspondant � des getters) et de
	 * headerValues. <br>
	 * Ex: table.setHeaders(new String[][] { {"nom","Nom"} {"prenom","Pr�nom"} } <br>
	 * Contrairement � la composition visuelle, setHeaders ne permet pas de d�finir les largeurs.
	 * Mais selon le param�trage de MUtilitiesTable.ADJUST_COLUMN_WIDTHS (true par d�faut), les
	 * largeurs s'adaptent automatiquement selon les donn�es � chaque appel de la m�thode setList. <br>
	 * Pour red�finir la largeur (pr�f�r�e) d'une colonne, utiliser
	 * table.getColumn("identifier").setPreferredWidth(100). Cette largeur est de 75 par d�faut.
	 *
	 * @param headers String[][]
	 */
    public void setHeaders(String[][] headers) {
        TableColumn tableColumn;
        for (int i = getColumnCount() - 1; i > -1; i--) {
            tableColumn = getColumn(i);
            removeColumn(tableColumn);
        }
        if (headers == null) {
            return;
        }
        final int length = headers.length;
        for (int i = 0; i < length; i++) {
            mswing.MAssertion.preCondition(headers[i].length == 2, "Chaque header d'une MTable doit �tre de la forme {\"identifier\", \"headerValue\"}");
        }
        for (int i = 0; i < length; i++) {
            tableColumn = new TableColumn(i);
            tableColumn.setIdentifier(headers[i][0]);
            tableColumn.setHeaderValue(headers[i][1]);
            addColumn(tableColumn);
        }
    }
}
