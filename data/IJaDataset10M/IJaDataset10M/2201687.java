package net.kodveus.kumanifest.operation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import net.kodveus.gui.araclar.VeriSinif;
import net.kodveus.kumanifest.database.DBManager;
import net.kodveus.kumanifest.interfaces.OperationInterface;
import net.kodveus.kumanifest.jdo.Cargo;
import net.kodveus.kumanifest.jdo.Commodity;
import net.kodveus.kumanifest.jdo.Pack;
import net.kodveus.kumanifest.utility.LogHelper;

public class CargoOperation implements OperationInterface {

    private static CargoOperation instance;

    private CargoOperation() {
    }

    public static CargoOperation getInstance() {
        if (instance == null) {
            instance = new CargoOperation();
        }
        return instance;
    }

    private void rsToCargo(ResultSet rs, Cargo cargo) throws SQLException {
        cargo.setCargoId(rs.getLong("cargoId"));
        cargo.setCargoType(rs.getString("cargoType"));
        cargo.setCommodity((Commodity) CommodityOperation.getInstance().get(rs.getLong("commodityId")));
        cargo.setContainerId(rs.getLong("containerId"));
        cargo.setImco(rs.getString("imco"));
        cargo.setPack((Pack) PackOperation.getInstance().get(rs.getLong("packId")));
        cargo.setUnno(rs.getString("unno"));
        cargo.setNetWeight(rs.getDouble("netWeight"));
        cargo.setPackTotal(rs.getDouble("packTotal"));
    }

    public long create(VeriSinif vs) {
        Cargo cargo = (Cargo) vs;
        String sql = "INSERT INTO cargo (cargoType, commodityId, containerId, imco, packId, unno,netWeight,packTotal) VALUES(";
        sql += "'" + cargo.getCargoType() + "'," + cargo.getCommodity().getCommodityId() + "," + cargo.getContainerId() + ",'" + cargo.getImco() + "'," + cargo.getPack().getPackId() + ",'" + cargo.getUnno() + "'," + cargo.getNetWeight() + "," + cargo.getPackTotal() + ")";
        try {
            return DBManager.getInstance().insert(sql);
        } catch (Exception e) {
            LogHelper.getInstance().exception(e);
            return 0;
        }
    }

    public boolean delete(VeriSinif vs) {
        Cargo cargo = (Cargo) vs;
        return delete(cargo.getCargoId());
    }

    public boolean delete(Long id) {
        String sql = "DELETE FROM cargo WHERE cargoId=" + id;
        try {
            return DBManager.getInstance().executeUpdate(sql);
        } catch (Exception e) {
            LogHelper.getInstance().exception(e);
            return false;
        }
    }

    public boolean update(VeriSinif vs) {
        Cargo cargo = (Cargo) vs;
        String sql = "UPDATE cargo SET";
        sql += " cargoType='" + cargo.getCargoType() + "', commodityId=" + cargo.getCommodity().getCommodityId() + ", containerId=" + cargo.getContainerId() + ", imco='" + cargo.getImco() + "', packId=" + cargo.getPack().getPackId() + ", unno='" + cargo.getUnno() + "', packTotal=" + cargo.getPackTotal() + ", netWeight=" + cargo.getNetWeight() + " WHERE cargoId=" + cargo.getCargoId();
        try {
            return DBManager.getInstance().executeUpdate(sql);
        } catch (Exception e) {
            LogHelper.getInstance().exception(e);
            return false;
        }
    }

    public VeriSinif get(Long id) {
        Cargo cargo = new Cargo();
        cargo.setCargoId(id);
        ArrayList<Cargo> list = ara(cargo);
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    public ArrayList<Cargo> ara(VeriSinif vs) {
        ArrayList<Cargo> al = new ArrayList<Cargo>();
        try {
            Cargo cargo = (Cargo) vs;
            String sql = "SELECT * FROM cargo WHERE 1=1";
            if (cargo.getCargoId() != null) {
                sql += " AND cargoId=" + cargo.getCargoId();
            }
            if (cargo.getCargoType() != null && !cargo.getCargoType().equals("")) {
                sql += " AND cargoType='" + cargo.getCargoType() + "'";
            }
            if (cargo.getCommodity() != null) {
                sql += " AND commodityId=" + cargo.getCommodity().getCommodityId();
            }
            if (cargo.getContainerId() != null) {
                sql += " AND containerId=" + cargo.getContainerId();
            }
            if (cargo.getImco() != null && !cargo.getImco().equals("")) {
                sql += " AND imco='" + cargo.getImco() + "'";
            }
            if (cargo.getPack() != null) {
                sql += " AND packId=" + cargo.getPack().getPackId();
            }
            if (cargo.getPackTotal() != null) {
                sql += " AND packTotal=" + cargo.getPackTotal();
            }
            if (cargo.getNetWeight() != null) {
                sql += " AND netWeight=" + cargo.getNetWeight();
            }
            if (cargo.getUnno() != null && !cargo.getUnno().equals("")) {
                sql += " AND unno='" + cargo.getUnno() + "'";
            }
            ResultSet rs = DBManager.getInstance().executeQuery(sql);
            while (rs.next()) {
                cargo = new Cargo();
                rsToCargo(rs, cargo);
                al.add(cargo);
            }
        } catch (Exception e) {
            LogHelper.getInstance().exception(e);
        }
        return al;
    }

    public VeriSinif next(Long id) {
        Cargo cargo = null;
        String sql = "SELECT * FROM cargo WHERE cargoId>" + id + " ORDER BY cargoId";
        try {
            ResultSet rs = DBManager.getInstance().executeQuery(sql);
            if (rs.next()) {
                cargo = new Cargo();
                rsToCargo(rs, cargo);
            }
        } catch (Exception e) {
            LogHelper.getInstance().exception(e);
            cargo = null;
        }
        return cargo;
    }

    public VeriSinif previous(Long id) {
        Cargo cargo = null;
        String sql = "SELECT * FROM cargo WHERE cargoId<" + id + " ORDER BY cargoId DESC";
        try {
            ResultSet rs = DBManager.getInstance().executeQuery(sql);
            if (rs.next()) {
                cargo = new Cargo();
                rsToCargo(rs, cargo);
            }
        } catch (Exception e) {
            LogHelper.getInstance().exception(e);
            cargo = null;
        }
        return cargo;
    }
}
