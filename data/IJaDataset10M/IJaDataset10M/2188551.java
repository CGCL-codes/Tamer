package telas.componentes;

import java.util.Collection;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;

public class TableModelSistema extends AbstractTableModel {

    private static final long serialVersionUID = 7250798161162815956L;

    private Vector<Object> descricaoColunas = new Vector<Object>();

    private Vector<Object> listaDados = new Vector<Object>();

    public TableModelSistema(Collection<String[]> descricaoColunas, Collection<Object[]> listaDados) {
        for (Object objetoDado : listaDados) {
            this.listaDados.add(objetoDado);
        }
        for (Object[] objetoColuna : descricaoColunas) {
            this.descricaoColunas.add(objetoColuna[0]);
        }
    }

    /**
	 * Método responsável por retornar a quantidade de colunas
	 * 
	 * @return columncount : int
	 */
    public int getColumnCount() {
        return descricaoColunas.size();
    }

    /**
	 * Método responsável por retornar o nome da coluna desejada.
	 * 
	 * @param col : int - indice da coluna.
	 * @return descricaoColuna : String - Descrição da Coluna no índice informado.
	 */
    public String getColumnName(int col) {
        return descricaoColunas.get(col).toString();
    }

    /**
	 * Método responsável por retornar a quantidade de linhas.
	 * 
	 * @return rowCount : int - Quantidade de linhas.
	 */
    public int getRowCount() {
        return listaDados.size();
    }

    /**
	 * Método responsável por retornar o valor informados pelo índice.
	 * 
	 * @param
	 * 		arg0 : int - Linha desejada.
	 * 		arg1 : int - Coluna desejada.
	 * 
	 * @return object : Object - Valor desejado.
	 */
    public Object getValueAt(int arg0, int arg1) {
        return ((Object[]) listaDados.get(arg0))[arg1];
    }
}
