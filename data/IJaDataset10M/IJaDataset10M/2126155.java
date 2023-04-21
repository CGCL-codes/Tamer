package tcbnet.ui;

import javax.swing.table.AbstractTableModel;

/**
 * Classe responsavel por armazenar eventos sobre transferencias.
 *
 * @author $Author: rcouto $
 * @version $Revision: 1.8 $
 */
public class TransferTableModel extends AbstractTableModel {

    private static String[] columnNames = new String[] { "Filename", "Transfered", "Filesize", "Speed", "Source", "Description", "Progress" };

    private UIController uicontrol;

    /**
     * Construtor.
     */
    public TransferTableModel(UIController uic) {
        this.uicontrol = uic;
    }

    /**
     * @return o numero de linhas da tabela.
     */
    public int getRowCount() {
        return this.uicontrol.getDownloadCount();
    }

    /**
     * @return o numero de colunas da tabela.
     */
    public int getColumnCount() {
        return TransferTableModel.columnNames.length;
    }

    /**
     * Retorna o valor de uma celula.
     *
     * @param row linha da celula.
     * @param column coluna da celula.
     * @return o valor da celula.
     */
    public Object getValueAt(int row, int column) {
        return this.uicontrol.getDownloadInfo(row, column);
    }

    /**
     * Retorna sempre falso, mesmo se uma celula e editavel.
     *
     * @param row Linha da celula.
     * @param column Coluna da celula.
     * @return retorna falso.
     */
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    /**
     * Atualizando a tabela.
     */
    public void refresh() {
        this.fireTableDataChanged();
    }

    /**
     * @return o nome da i-esima coluna
     */
    public String getColumnName(int c) {
        return TransferTableModel.columnNames[c];
    }
}
