package cn.edu.thss.iise.beehivez.server.index.petrinetindex.nullindex;

import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.TreeSet;
import org.processmining.framework.models.petrinet.PetriNet;
import org.processmining.importing.pnml.PnmlImport;
import org.processmining.mining.petrinetmining.PetriNetResult;
import cn.edu.thss.iise.beehivez.server.datamanagement.DataManager;
import cn.edu.thss.iise.beehivez.server.datamanagement.pojo.PetrinetObject;
import cn.edu.thss.iise.beehivez.server.graph.isomorphism.Ullman4PetriNet;
import cn.edu.thss.iise.beehivez.server.index.petrinetindex.PetriNetIndex;
import cn.edu.thss.iise.beehivez.server.index.ProcessQueryResult;
import cn.edu.thss.iise.beehivez.server.util.PetriNetUtil;

/**
 * @author JinTao used for search time comparation
 * 
 */
public class NullIndex extends PetriNetIndex {

    @Override
    public void addProcessModel(Object pno) {
    }

    @Override
    public void close() {
    }

    @Override
    public boolean create() {
        return true;
    }

    @Override
    public void delProcessModel(Object pno) {
    }

    public TreeSet<ProcessQueryResult> getPetriNet(Object o) {
        TreeSet<ProcessQueryResult> ret = new TreeSet<ProcessQueryResult>();
        if (o instanceof PetriNet) {
            PetriNet q = (PetriNet) o;
            DataManager dm = DataManager.getInstance();
            String strSelectPetriNet = "select process_id, pnml from petrinet";
            ResultSet rs = dm.executeSelectSQL(strSelectPetriNet, 0, Integer.MAX_VALUE, dm.getFetchSize());
            try {
                while (rs.next()) {
                    PetriNet c = null;
                    if (dm.getDBName().equalsIgnoreCase("postgresql") || dm.getDBName().equalsIgnoreCase("mysql")) {
                        String str = rs.getString("pnml");
                        byte[] temp = str.getBytes();
                        c = PetriNetUtil.getPetriNetFromPnmlBytes(temp);
                    } else if (dm.getDBName().equalsIgnoreCase("derby")) {
                        InputStream in = rs.getAsciiStream("pnml");
                        PnmlImport pnml = new PnmlImport();
                        PetriNetResult result = (PetriNetResult) pnml.importFile(in);
                        c = result.getPetriNet();
                        result.destroy();
                        in.close();
                    } else {
                        System.out.println(dm.getDBName() + " unsupported");
                        System.exit(-1);
                    }
                    long process_id = rs.getLong("process_id");
                    if (Ullman4PetriNet.subGraphIsomorphism(q, c)) {
                        ret.add(new ProcessQueryResult(process_id, 1));
                    }
                    c.destroyPetriNet();
                }
                java.sql.Statement stmt = rs.getStatement();
                rs.close();
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

    @Override
    public boolean open() {
        return true;
    }

    @Override
    public boolean supportGraphQuery() {
        return true;
    }

    @Override
    public boolean supportTextQuery() {
        return false;
    }

    @Override
    public boolean destroy() {
        return false;
    }

    public static void main(String[] args) {
    }

    @Override
    public float getStorageSizeInMB() {
        return 0;
    }

    @Override
    public boolean supportSimilarQuery() {
        return false;
    }

    @Override
    public TreeSet<ProcessQueryResult> getProcessModels(Object o, float similarity) {
        return getPetriNet(o);
    }

    @Override
    public boolean supportSimilarLabel() {
        return true;
    }
}
