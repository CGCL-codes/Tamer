package FuenteDeDatos;

import java.sql.SQLException;
import java.util.Observable;
import java.util.Observer;

public final class DataTextField extends javax.swing.JTextField implements Observer {

    private String campo;

    private DataSource data = null;

    public void conectar() {
        this.getData().addObserver(this);
    }

    public void update(Observable o, Object o1) {
        this.setText("");
        try {
            this.setText(this.data.rs.getString(this.getCampo()));
        } catch (SQLException ex) {
            this.setText("");
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
}
