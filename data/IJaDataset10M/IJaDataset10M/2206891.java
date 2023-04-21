package edu.ufpa.ppgcc.visualpseudo.gui.components;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.table.AbstractTableModel;
import edu.ufpa.ppgcc.visualpseudo.semantic.SymbolEntry;
import edu.ufpa.ppgcc.visualpseudo.semantic.SymbolTable;
import edu.ufpa.ppgcc.visualpseudo.types.TokenType;

/**
 * @author Administrador
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SymbolTableModel extends AbstractTableModel {

    public static final int SCOPO_COLUMN = 0;

    public static final int SIMBOLO_COLUMN = 1;

    public static final int TIPO_COLUMN = 2;

    public static final int VALOR_COLUMN = 3;

    public static final String SCOPE_NAME = "SCOPO";

    public static final String SYMBOL_NAME = "S�MBOLO";

    public static final String TYPE_NAME = "TIPO";

    public static final String VALUE_NAME = "VALOR";

    private SymbolTable tableGlobal = null;

    private Map<String, SymbolTable> tablesLocal = null;

    private List<String> scopos = null;

    private List<SymbolEntry> dados = null;

    private String nomes[] = { SCOPE_NAME, SYMBOL_NAME, TYPE_NAME, VALUE_NAME };

    public SymbolTableModel() {
        super();
        this.scopos = new ArrayList();
        this.dados = new ArrayList();
    }

    public void setTables(SymbolTable tableGlobal, Map<String, SymbolTable> tablesLocal) {
        this.tableGlobal = tableGlobal;
        this.tablesLocal = tablesLocal;
    }

    public void clear() {
        scopos.clear();
        dados.clear();
    }

    public void refresh() {
        clear();
        for (SymbolEntry symbol : tableGlobal.getDados()) {
            scopos.add("global");
            dados.add(symbol);
        }
        for (Map.Entry<String, SymbolTable> table : tablesLocal.entrySet()) {
            for (SymbolEntry symbol : table.getValue().getDados()) {
                scopos.add(table.getKey());
                dados.add(symbol);
            }
        }
    }

    public int getColumnCount() {
        return nomes.length;
    }

    public int getRowCount() {
        return dados.size();
    }

    public String getColumnName(int col) {
        return nomes[col];
    }

    public boolean isCellEditable(int lin, int col) {
        return false;
    }

    private String repr(SymbolEntry entry, Object t[], String interv, Integer level) {
        String result = "";
        for (int i = 0; i < t.length; i++) {
            if (t[i] instanceof Object[]) result += repr(entry, (Object[]) t[i], interv + (i + entry.getOffsets()[level]) + ",", level + 1); else result += "[" + interv + (i + entry.getOffsets()[level]) + "]=" + ((entry.getSubTipo().equals(TokenType.STRING)) ? "'" + t[i].toString() + "'" : t[i].toString()) + ";";
        }
        return result;
    }

    public Object getValueAt(int lin, int col) {
        SymbolEntry entry = dados.get(lin);
        switch(col) {
            case SCOPO_COLUMN:
                return scopos.get(lin);
            case SIMBOLO_COLUMN:
                return entry.getNome();
            case TIPO_COLUMN:
                if (entry.getTipo().equals(TokenType.VECTOR)) {
                    String valor = "";
                    int level = 0;
                    Object[] value = (Object[]) entry.getValor();
                    for (int i = 0; i < entry.getOffsets().length; i++) {
                        int inicio = entry.getOffsets()[i];
                        int length = value.length;
                        if (((Object[]) entry.getValor())[level] instanceof Object[]) value = (Object[]) ((Object[]) entry.getValor())[level++]; else value = (Object[]) entry.getValor();
                        valor += inicio + ".." + (inicio + length - 1) + ",";
                    }
                    return entry.getTipo() + "[" + valor.substring(0, valor.length() - 1) + "]";
                } else return entry.getTipo();
            case VALOR_COLUMN:
                if (entry.getTipo().equals(TokenType.VECTOR)) {
                    String valor = repr(entry, (Object[]) entry.getValor(), "", 0);
                    return valor.substring(0, valor.length() - 1);
                } else return (entry.getTipo().equals(TokenType.STRING)) ? "'" + entry.getValor() + "'" : entry.getValor();
            default:
                return null;
        }
    }
}
