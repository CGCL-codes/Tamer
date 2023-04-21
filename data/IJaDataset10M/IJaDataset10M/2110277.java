package FuenteDeDatos;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataTextV2 extends javax.swing.JTextField implements Observer {

    private String campo;

    private DataSource data = null;

    private List<String> Campos = null;

    public void conectar() {
        this.getData().addObserver(this);
        this.getCampos();
    }

    public void update(Observable o, Object o1) {
        if (this.Campos.get(1) == null) {
            List<String> x = null;
            try {
                ResultSetMetaData rsMetaData = (ResultSetMetaData) data.rs.getMetaData();
                if (rsMetaData != null) {
                    int ncol = rsMetaData.getColumnCount();
                    for (int i = 1; i < ncol + 1; i++) {
                        x.add(rsMetaData.getColumnLabel(i));
                    }
                }
            } catch (SQLException excepcionSql) {
            }
        } else {
            this.setText("");
            try {
                this.setText(this.data.rs.getString(this.getCampo()));
            } catch (SQLException ex) {
                this.setText("");
            }
        }
    }

    public String getCampo() {
        return campo;
    }

    /**
     * @param campo the campo to set
     */
    public void setCampo(String campo) {
        this.campo = campo;
    }

    public DataSource getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(DataSource data) {
        this.data = data;
        if (data != null) {
            this.conectar();
        }
    }

    /**
     * @return the Campos
     */
    public List<String> getCampos() {
        return this.Campos;
    }

    /**
     * @param Campos the Campos to set
     */
    public void setCampos(List<String> Campos) {
        this.Campos = Campos;
    }
}
