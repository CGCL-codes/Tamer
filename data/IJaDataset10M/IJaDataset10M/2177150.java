package es.nutroptima.soft.model.factories;

import es.nutroptima.soft.database.NConnector;
import es.nutroptima.soft.model.MyVItem;
import es.nutroptima.soft.model.MyVTitulo;
import es.nutroptima.soft.model.Producto;
import es.nutroptima.soft.model.UnidadPeso;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Miguel Angel López Montellano <mlopez@nutroptima.es>
 */
public class ItemsFactory {

    private static final ItemsFactory instance = new ItemsFactory();

    private List<MyVTitulo> myvTitulos;

    private List<UnidadPeso> unidadesPeso;

    private void loadMyVTitulo() throws SQLException, ClassNotFoundException {
        this.myvTitulos = new ArrayList<MyVTitulo>();
        Logger.getLogger(ItemsFactory.class.getName()).info("Load productos del usuario");
        Connection conn = NConnector.getInstance().getConn();
        Statement stat = conn.createStatement();
        ResultSet rs = stat.executeQuery("select * from myvTitulo order by id asc;");
        while (rs.next()) {
            MyVTitulo titulo = new MyVTitulo(rs.getInt("id"), rs.getString("titulo"));
            this.myvTitulos.add(titulo);
        }
    }

    private void loadUnidadesPeso() throws SQLException, ClassNotFoundException {
        this.unidadesPeso = new ArrayList<UnidadPeso>();
        Logger.getLogger(ItemsFactory.class.getName()).info("Load productos del usuario");
        Connection conn = NConnector.getInstance().getConn();
        Statement stat = conn.createStatement();
        ResultSet rs = stat.executeQuery("select * from unidadPeso order by id asc;");
        while (rs.next()) {
            UnidadPeso unidad = new UnidadPeso();
            unidad.setId(rs.getInt("id"));
            unidad.setTitulo(rs.getString("titulo"));
            unidadesPeso.add(unidad);
        }
    }

    public ItemsFactory() {
        try {
            loadMyVTitulo();
            loadUnidadesPeso();
        } catch (SQLException ex) {
            Logger.getLogger(ItemsFactory.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ItemsFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static ItemsFactory getInstance() {
        return instance;
    }

    public void loadItems(Producto p) throws ClassNotFoundException, SQLException {
        List<MyVItem> items = new ArrayList<MyVItem>();
        Logger.getLogger(ItemsFactory.class.getName()).info("Load productos del usuario");
        Connection conn = NConnector.getInstance().getConn();
        Statement stat = conn.createStatement();
        ResultSet rs = stat.executeQuery("select * from myvItem where idProducto=" + p.getId() + " ;");
        while (rs.next()) {
            MyVItem item = new MyVItem(p);
            item.setCantidad(rs.getDouble("cantidad"));
            item.setId(rs.getInt("id"));
            item.setTitulo(this.myvTitulos.get(rs.getInt("idMyVItem")));
            item.setUnidad(this.unidadesPeso.get(rs.getInt("idUnidad")));
            item.setActualizado(false);
            items.add(item);
        }
        p.setItems(items);
    }

    public int nextID() throws ClassNotFoundException, SQLException {
        Logger.getLogger(ItemsFactory.class.getName()).info("Load productos del usuario");
        Connection conn = NConnector.getInstance().getConn();
        Statement stat = conn.createStatement();
        ResultSet rs = stat.executeQuery("select max(id) from myvItem;");
        return rs.getInt(1) + 1;
    }

    public List<MyVTitulo> getMyvTitulos() {
        return myvTitulos;
    }

    public List<UnidadPeso> getUnidadesPeso() {
        return unidadesPeso;
    }
}
