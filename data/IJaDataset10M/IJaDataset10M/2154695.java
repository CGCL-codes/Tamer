package sMySQLappTemplate.Core;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.rowset.CachedRowSet;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;
import sMySQLappTemplate.Exceptions.*;
import sMySQLappTemplate.GUI.*;

public abstract class AppCoreTemplate {

    protected FeatureTemplate[] features;

    protected ComModule comLink;

    protected int loginAttemps = 3;

    protected AppCoreTemplate() {
        this.comLink = new ComModule();
        this.initData();
        this.initGUI();
        this.initFeatures();
        ConnectionCfg defaultConnection = null;
        try {
            defaultConnection = new ConnectionCfg("localhost", "3306", "mydb");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        new LoginWindow(this, defaultConnection);
        if (!comLink.isConnectionEstablished()) System.exit(2);
    }

    /**
	 * <b> TryLogin </b> <br>
	 * Comprueba que la conexion sea viable, tanto en parametros 
	 * de conexion como en nombre y pass de usuario. <br>
	 * De serlo, establece la configuracion de conexion y retorna
	 * normalmente. En caso contrario tira exepcion. <br>
	 * <br>
	 * @param who - Instancia de UserAccount 
	 * @param where - Instancia de ConnectionURL
	 * <br>
	 * <br>
	 * @throws InvalidUserOrPass
	 * @throws InvalidPortNumber
	 * @throws InvalidHost
	 * @throws InvalidDataBase
	 */
    public void TryLogin(UserAccount who, ConnectionCfg where) throws InvalidUserOrPass, InvalidPortNumber, InvalidHost, InvalidDataBase {
        if (loginAttemps > 1) {
            try {
                comLink.establishConnection(who, where);
            } catch (InvalidUserOrPass e) {
                this.loginAttemps--;
                throw e;
            }
        } else {
            System.exit(1);
        }
    }

    protected abstract void initData();

    protected abstract void initGUI();

    /**
	 * <b>initFeatures</b><br>
	 *  Metodo llamado por el constructor, debe ser implementado por
	 *  la aplicacion concreta a fin de cargar las funcionalidades
	 *  deseadas. <br>   
	 */
    protected abstract void initFeatures();

    /**
	 * <b>sendConsult</b><br>
	 * Envia una consulta a la base de datos y retorna el 
	 * resultado de la misma.<br>
	 * Acceso directo a la base de datos: usar con precaucion
	 * <br>
	 * @param SQLquery - La consulta a realizar. <br>
	 * <br>
	 * @return rowSet resultado de la consulta. <br>
	 * <br>
	 * @throws SQLException
	 */
    public CachedRowSet sendConsult(String SQLquery) throws SQLException {
        try {
            return comLink.execConsult(SQLquery);
        } catch (UntestedConnectionException u) {
            u.printStackTrace();
        }
        return null;
    }

    /**
	 * <b>sendCommand</b><br>
	 * Envia un comando a la base de datos.<br>
	 * Acceso directo a la base de datos: usar con precaucion
	 * <br>
	 * @param SQLquery - El comando a ejecutar. <br>
	 * <br>
	 * @throws SQLException
	 */
    public void sendCommand(String SQLquery) throws SQLException {
        try {
            comLink.execCommand(SQLquery);
        } catch (UntestedConnectionException e) {
            e.printStackTrace();
        }
    }

    /**
	 * <b>getValue</b><br>
	 * Recibe una consulta SQL.
	 * Retorna el primer objeto del result set, o null
	 * en caso de que se trate de un resultado vacio.
	 * Usado adecuadamente para recuperar valores unitarios.<br>
	 * Castear con cuidado y manejo de exepciones.
	 * <br>
	 * @param SQLquery - La consulta a realizar. <br>
	 * <br>
	 * @return Object resultado de la consulta. <br>
	 * <br>
	 * @throws SQLException
	 */
    public Object getValue(String SQLquery) throws SQLException {
        CachedRowSet rs = sendConsult(SQLquery);
        if (rs.next()) return rs.getObject(1); else return null;
    }

    /**
	 * <b>getList</b><br>
	 * Recibe una consulta SQL.
	 * Retorna una lista de Objetos conteniendo las filas de la
	 * primer columna de la tabla SQL resultado de ejecutar la
	 * la consulta dada.<br>
	 * Util para rellenar ComboBoxes.
	 * <br>
	 * @param SQLquery - La consulta a realizar. <br>
	 * <br>
	 * @return Lista de Strings. <br>
	 * <br>
	 * @throws SQLException
	 */
    public List<Object> getList(String SQLquery) throws SQLException {
        List<Object> resultList = new ArrayList<Object>();
        CachedRowSet crs = sendConsult(SQLquery);
        while (crs.next()) {
            resultList.add(crs.getObject(1));
        }
        return resultList;
    }

    /**
	 * <b>populateTable</b><br>
	 * Carga la tabla objetivo con el resultado una consulta SQL
	 * de ser posible, throws exception en caso contrario. <br>
	 * El orden de carga de las columnas sera el mismo
	 * en el que se reciba el resultado y la cantidad de
	 * columnas que se cargaran sera igual a la cantidad
	 * de columnas del modelo de tabla pasado. <br>
	 * Altere el orden y cantidad de los campos pedidos en la 
	 * consulta SQL para ajustar esto.
	 * <br>
	 * @param data - TableModel objetivo para rellenar 
	 * @param SQLquery - La consulta a realizar. <br>
	 * <br>
	 * @throws SQLException
	 */
    public void populateTable(DefaultTableModel data, String SQLquery) throws SQLException {
        CachedRowSet crs = this.sendConsult(SQLquery);
        int columnNumber = data.getColumnCount();
        data.setRowCount(0);
        int row = 0;
        int column = 0;
        while (crs.next()) {
            data.setRowCount(row + 1);
            for (column = 0; column < columnNumber; column++) {
                data.setValueAt(crs.getObject(column + 1), row, column);
            }
            row++;
        }
    }

    /**
	 * <b>populateComboBox</b><br>
	 * Carga el combo box objetivo con el resultado una consulta SQL
	 * de ser posible, throws exception en caso contrario. <br>
	 * En todos los casos se cargaran los items recuperados de la
	 * primer columna del result set
	 * <br>
	 * @param data - JComboBox objetivo para rellenar 
	 * @param SQLquery - La consulta a realizar. <br>
	 * <br>
	 * @throws SQLException
	 */
    public void populateComboBox(DefaultComboBoxModel data, String SQLquery) throws SQLException {
        CachedRowSet crs = this.sendConsult(SQLquery);
        data.removeAllElements();
        while (crs.next()) data.addElement(crs.getObject(1));
    }

    /**
	 * <b>populateList</b><br>
	 * Carga la lista objetivo con el resultado una consulta SQL
	 * de ser posible, throws exception en caso contrario. <br>
	 * En todos los casos se cargaran los items recuperados de la
	 * primer columna del result set
	 * <br>
	 * @param data - JList objetivo para rellenar 
	 * @param SQLquery - La consulta a realizar. <br>
	 * <br>
	 * @throws SQLException 
	 * @throws SQLException
	 */
    public void populateList(DefaultListModel data, String SQLquery) throws SQLException {
        CachedRowSet crs = this.sendConsult(SQLquery);
        data.removeAllElements();
        while (crs.next()) data.addElement(crs.getObject(1));
    }

    /**
	 * <b>registerNewButtonFor</b><br>
	 * Se utiliza para agregar los botones que activan las funcionalidades.
	 * los nucleos de aplicacion que extiendan esta clase deberan proveer
	 * metodos publicos para registrar botones en las distintas areas validas
	 * de dichas aplicaciones.<br>
	 * El control del look and feel deberia ser de la aplicacion no 
	 * de las funcionalidades, asi que no se agregan los componentes
	 * en si, sino una descripcion de como se los quiere
	 * aunque en esta implementacion esto no es mandatorio 
	 * <br>
	 * @param comm - Command que se ejecutara la clickear sobre el boton.
	 * @param where - JPanel valido de la ventana de aplicacion donde se agregara el boton.
	 * @param iconPath - String representando una direccion URI del icono del boton.
	 * @param buttonLabel - String conteniendo la etiqueta que mostrara el boton.
	 * <br>
	 */
    @SuppressWarnings("unchecked")
    protected void registerNewButtonFor(Command comm, JPanel where, String iconPath, String buttonLabel) throws InvalidButtonLocation {
        if (where == null) throw new InvalidButtonLocation();
        MyButton newButton = new MyButton(iconPath, buttonLabel);
        newButton.addMouseListener(new FeatureLauncher(comm));
        where.add(newButton);
    }

    /**
	 * <b>FeatureLauncher</b><br>
	 * Clase interna que extiende al oyente del mouse
	 * utilizada para captura el comportamiento por el 
	 * cual al clickear sobre el item al que se le asocie
	 * se activa la funcionalidad marcada. <br>
	 * El constructor de esta clase recibe un Command para
	 * ejecutar.
	 */
    protected class FeatureLauncher extends MouseAdapter {

        @SuppressWarnings("unchecked")
        protected Command comm;

        @SuppressWarnings("unchecked")
        public FeatureLauncher(Command comm) {
            this.comm = comm;
        }

        public void mouseClicked(MouseEvent e) {
            if (e.getComponent().isEnabled()) comm.ExecCommand();
        }
    }
}
