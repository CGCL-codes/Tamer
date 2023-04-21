package bussiness;

import connection.Pool;
import connection.Red;
import connection.RedAdicional;
import connection.RedPublica;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author necronet
 */
public class RedLogic {

    public static int insert(RedPublica publica, Connection conection) {
        int retorno = -1;
        try {
            PreparedStatement prep = conection.prepareStatement("insert into red(pool_idpool,red_idred,red,mascara,publica) values(retorno_IdPool(?),?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            prep.setString(1, publica.getPoolIdpool().getNombre());
            if (publica.getRedIdredAdicional() == null) prep.setObject(2, null); else prep.setInt(2, publica.getRedIdredAdicional().getId());
            prep.setString(3, publica.getRed());
            prep.setString(4, publica.getMascara());
            prep.setString(5, "1");
            prep.execute();
            ResultSet rs = prep.getGeneratedKeys();
            rs.next();
            retorno = ((Long) rs.getObject(1)).intValue();
            prep.close();
        } catch (SQLException ex) {
            Logger.getLogger(PoolLogic.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    public static int insert(RedAdicional adicional, Connection conection) {
        int retorno = -1;
        try {
            PreparedStatement prep = conection.prepareStatement("insert into red(pool_idpool,red_idred,red,mascara,publica) values(retorno_IdPool(?),?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            prep.setString(1, adicional.getPoolIdpool().getNombre());
            if (adicional.getRedIdredAdicional() == null) prep.setObject(2, null); else prep.setInt(2, adicional.getRedIdredAdicional().getId());
            prep.setString(3, adicional.getRed());
            prep.setString(4, adicional.getMascara());
            prep.setString(5, "0");
            prep.execute();
            ResultSet rs = prep.getGeneratedKeys();
            rs.next();
            retorno = ((Long) rs.getObject(1)).intValue();
            prep.close();
        } catch (SQLException ex) {
            Logger.getLogger(PoolLogic.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    public static List<Red> buscar(String where, Connection conection, String... args) {
        int i = 1;
        List<Red> retorno = null;
        try {
            retorno = new ArrayList<Red>();
            String sql = "select * from vw_red_pool " + where;
            PreparedStatement prep = conection.prepareStatement(sql);
            for (String arg : args) {
                prep.setString(i++, arg);
            }
            ResultSet rs = prep.executeQuery();
            while (rs.next()) {
                Red red = new Red();
                red.setId(rs.getInt("idred"));
                red.setRed(rs.getString("red"));
                red.setMascara(rs.getString("mascara"));
                Pool pool = new Pool();
                pool.setNombre(rs.getString("pool"));
                red.setPoolIdpool(pool);
                red.setPublica(rs.getBoolean("publica"));
                retorno.add(red);
            }
            rs.close();
            prep.close();
        } catch (SQLException ex) {
            Logger.getLogger(PoolLogic.class.getName()).log(Level.SEVERE, null, ex);
        }
        Collections.sort(retorno);
        return retorno;
    }

    public static int countIPs(Red red, Connection conection) {
        int retorno = -1;
        try {
            PreparedStatement prep = conection.prepareStatement(" select count(*) as 'ips' from ip where red_idred=?");
            prep.setInt(1, red.getId());
            ResultSet rs = prep.executeQuery();
            if (rs.next()) retorno = rs.getInt("ips");
            rs.close();
            prep.close();
        } catch (SQLException ex) {
            Logger.getLogger(PoolLogic.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    public static Red hasSubnet(Red red, Connection conection) {
        try {
            SortedSet<Red> subredes = new TreeSet<Red>();
            PreparedStatement prep = conection.prepareStatement("select * from red where red_idred=?");
            prep.setInt(1, red.getId());
            ResultSet rs = prep.executeQuery();
            RedAdicional subred = null;
            while (rs.next()) {
                subred = new RedAdicional();
                subred.setId(rs.getInt("idred"));
                subred.setMascara(rs.getString("mascara"));
                subred.setRed(rs.getString("red"));
                String sqlsubred = "select disponibilidad " + "from vw_redadicional_disponibilidad where subred " + "is not null and idadicional='" + subred.getId() + "'";
                ResultSet rs2 = conection.createStatement().executeQuery(sqlsubred);
                if (rs2.next()) {
                    Boolean disponible = !rs2.getBoolean(1);
                    subred.setDisponible(disponible);
                }
                rs2.close();
                subredes.add(subred);
            }
            red.setSubred(subredes);
            rs.close();
            prep.close();
            return red;
        } catch (SQLException ex) {
            Logger.getLogger(PoolLogic.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static Red hasSubnet(Red red, Connection conection, boolean publica) {
        try {
            SortedSet<Red> subredes = new TreeSet<Red>();
            PreparedStatement prep = conection.prepareStatement("select * from red where red_idred=?");
            prep.setInt(1, red.getId());
            ResultSet rs = prep.executeQuery();
            RedPublica subred = null;
            System.out.println(red.getId());
            while (rs.next()) {
                subred = new RedPublica();
                subred.setId(rs.getInt("idred"));
                subred.setMascara(rs.getString("mascara"));
                subred.setRed(rs.getString("red"));
                subredes.add(subred);
            }
            red.setSubred(subredes);
            rs.close();
            prep.close();
            return red;
        } catch (SQLException ex) {
            Logger.getLogger(PoolLogic.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
