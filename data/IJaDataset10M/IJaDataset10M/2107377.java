package net.sourceforge.ondex.core.sql2.entities;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.sourceforge.ondex.core.EvidenceType;
import net.sourceforge.ondex.core.ONDEXConcept;
import net.sourceforge.ondex.core.ONDEXEntity;
import net.sourceforge.ondex.core.ONDEXView;
import net.sourceforge.ondex.core.base.ONDEXViewImpl;
import net.sourceforge.ondex.core.sql2.SQL2Graph;
import net.sourceforge.ondex.core.sql2.metadata.SQL2EvidenceType;
import net.sourceforge.ondex.core.util.SparseBitSet;
import net.sourceforge.ondex.exception.type.AccessDeniedException;
import net.sourceforge.ondex.exception.type.NullValueException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public abstract class SQL2Entity implements ONDEXEntity {

    /**
	 * Pure SQL implementation of ONDEXEntity
	 * 
	 * Needs error catching, otherwise should be done.
	 * 
	 * @author sckuo
	 */
    protected int id;

    protected SQL2Graph sg;

    protected String tableName;

    public SQL2Entity(SQL2Graph s, int k, String table) {
        id = k;
        sg = s;
        tableName = table;
    }

    public String type() {
        return tableName;
    }

    public long getSID() {
        return sg.getSID();
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void addContext(ONDEXConcept ac) throws AccessDeniedException, NullValueException {
        try {
            PreparedStatement createRowItem = sg.getConnection().prepareStatement("insert into context (sid, id, elementType, context) values (?,?,?,?)");
            createRowItem.setLong(1, sg.getSID());
            createRowItem.setInt(2, id);
            createRowItem.setString(3, tableName);
            createRowItem.setInt(4, ac.getId());
            createRowItem.execute();
            createRowItem.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean removeContext(ONDEXConcept ac) {
        try {
            PreparedStatement deleteContext = sg.getConnection().prepareStatement("delete from context where (sid,id,elementType) = (?,?,?) and context = ?");
            deleteContext.setLong(1, sg.getSID());
            deleteContext.setInt(2, id);
            deleteContext.setString(3, tableName);
            deleteContext.setInt(4, ac.getId());
            int rowCountAffected = deleteContext.executeUpdate();
            deleteContext.close();
            if (rowCountAffected > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean removeEvidenceType(EvidenceType evidencetype) {
        try {
            PreparedStatement deleteEvidenceType = sg.getConnection().prepareStatement("delete from evidence where (sid, id, elementType, evidence_id) = (?,?,?,?)");
            deleteEvidenceType.setLong(1, sg.getSID());
            deleteEvidenceType.setInt(2, id);
            deleteEvidenceType.setString(3, tableName);
            deleteEvidenceType.setString(4, evidencetype.getId());
            deleteEvidenceType.execute();
            deleteEvidenceType.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void addEvidenceType(EvidenceType evidencetype) {
        try {
            PreparedStatement createRowItem = sg.getConnection().prepareStatement("insert into evidence (sid, id, elementType, evidence_id) values (?,?,?,?)");
            createRowItem.setLong(1, sg.getSID());
            createRowItem.setInt(2, id);
            createRowItem.setString(3, tableName);
            createRowItem.setString(4, evidencetype.getId());
            createRowItem.execute();
            createRowItem.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Set<EvidenceType> getEvidence() {
        Set<EvidenceType> ets = new HashSet<EvidenceType>();
        try {
            PreparedStatement getETs = sg.getConnection().prepareStatement("select evidence_id from evidence where (sid, id) = (?,?) and elementType = ?");
            getETs.setLong(1, getSID());
            getETs.setInt(2, id);
            getETs.setString(3, tableName);
            ResultSet rs = getETs.executeQuery();
            while (rs.next()) {
                ets.add(new SQL2EvidenceType(sg, rs.getString(1)));
            }
            rs.close();
            getETs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new HashSet<EvidenceType>(ets);
    }

    @Override
    public ONDEXView<ONDEXConcept> getContext() {
        IntOpenHashSet is = new IntOpenHashSet();
        try {
            PreparedStatement getContext = sg.getConnection().prepareStatement("select context.context from context where (sid, id) = (?,?) and elementType = ?");
            getContext.setLong(1, getSID());
            getContext.setInt(2, id);
            getContext.setString(3, tableName);
            ResultSet rs = getContext.executeQuery();
            while (rs.next()) {
                is.add(rs.getInt(1));
            }
            rs.close();
            getContext.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        SparseBitSet sbs = new SparseBitSet(is);
        return new ONDEXViewImpl<ONDEXConcept>(sg, ONDEXConcept.class, sbs);
    }
}
