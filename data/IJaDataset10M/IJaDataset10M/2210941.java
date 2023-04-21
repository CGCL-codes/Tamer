package co.edu.unal.ungrid.services.client.applet.atlas.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import co.edu.unal.ungrid.core.Dimension2D;
import co.edu.unal.ungrid.image.ViewType;
import co.edu.unal.ungrid.services.client.applet.atlas.model.curve.ControlPointCurve;
import co.edu.unal.ungrid.services.client.applet.atlas.AtlasServiceFactory;
import co.edu.unal.ungrid.services.server.db.instance.DerbyDatabase;
import co.edu.unal.ungrid.services.server.db.instance.DerbyRecord;

public class SliceCache implements Serializable {

    private static final long serialVersionUID = 1L;

    public SliceCache() {
        m_nVersion = VERSION;
        m_ht = new Hashtable<Double, Slice>();
    }

    public void addSlice(final Slice slice) {
        assert slice != null;
        m_ht.put(slice.getIndex(), slice);
    }

    public Slice getSlice(double fPos) {
        return m_ht.get(fPos);
    }

    public Collection<Slice> getSlices() {
        return m_ht.values();
    }

    public ArrayList<Slice> slicesWithCurves() {
        ArrayList<Slice> sa = new ArrayList<Slice>();
        for (Slice slice : m_ht.values()) {
            if (slice.numCurves() > 0) {
                sa.add(slice);
            }
        }
        return sa;
    }

    public ControlPointCurve getCurve(int id) {
        ControlPointCurve curve = null;
        for (Slice slice : m_ht.values()) {
            curve = slice.getCurveWithId(id);
            if (curve != null) {
                break;
            }
        }
        return curve;
    }

    public boolean removeCurve(int id) {
        boolean bDeleted = false;
        for (Slice slice : m_ht.values()) {
            bDeleted = slice.removeCurveWithId(id);
            if (bDeleted) {
                break;
            }
        }
        return bDeleted;
    }

    public Slice getPrevSlice(Slice slice) {
        Slice sPrev = null;
        double fPrev = Double.MIN_VALUE;
        for (Double f : m_ht.keySet()) {
            if (fPrev < f && f < slice.getIndex()) {
                fPrev = f;
                sPrev = m_ht.get(fPrev);
            }
        }
        return sPrev;
    }

    public Slice getNextSlice(Slice slice) {
        Slice sNext = null;
        double fNext = Double.MAX_VALUE;
        for (Double f : m_ht.keySet()) {
            if (slice.getIndex() < f && f < fNext) {
                fNext = f;
                sNext = m_ht.get(fNext);
            }
        }
        return sNext;
    }

    public Slice getPrevSliceWithCurves(Slice slice) {
        Slice sPrev = null;
        double fPrev = Double.MAX_VALUE;
        for (Slice s : m_ht.values()) {
            if (s.numCurves() > 0) {
                double f = s.getIndex();
                if (slice.getIndex() < f && f < fPrev) {
                    fPrev = f;
                    sPrev = s;
                }
            }
        }
        return sPrev;
    }

    public Slice getNextSliceWithCurves(Slice slice) {
        Slice sNext = null;
        double fNext = Double.MIN_VALUE;
        for (Slice s : m_ht.values()) {
            if (s.numCurves() > 0) {
                double f = s.getIndex();
                if (fNext < f && f < slice.getIndex()) {
                    fNext = f;
                    sNext = s;
                }
            }
        }
        return sNext;
    }

    private byte[] buildDataArray() {
        byte[] ba = null;
        AtlasDocument doc = AtlasServiceFactory.getDocument();
        Segmentation segm = doc.getRefSegmentation();
        if (segm != null) {
            ba = new byte[segm.getVolWidth() * segm.getVolHeight() * segm.getVolSlices()];
        }
        return ba;
    }

    public byte[] buildSegmentation() {
        byte[] ba = null;
        for (Slice slice : m_ht.values()) {
            if (slice.numCurves() > 0) {
                if (ba == null) {
                    ba = buildDataArray();
                }
                slice.fillSegmentation(ba);
            }
        }
        return ba;
    }

    public void deleteAllCurves() {
        for (Slice slice : m_ht.values()) {
            slice.deleteAllCurves();
        }
    }

    public boolean dump(final String sDbName, final String sTable, int vt) {
        boolean bOk = true;
        for (Slice slice : getSlices()) {
            bOk = slice.dump(sDbName, sTable, vt);
            if (!bOk) {
                break;
            }
        }
        return bOk;
    }

    public void load(final String sDbName, final String sTable, ViewType vt, Dimension2D<Integer> dim) {
        DerbyDatabase db = DerbyDatabase.getInstance();
        String sQuery = "select distinct slice from " + sTable + " where vtype=" + vt.ordinal();
        ArrayList<DerbyRecord> lst = db.select(sDbName, sQuery);
        for (DerbyRecord r : lst) {
            Slice slice = new Slice(vt, dim.getWidth().intValue(), dim.getHeight().intValue(), r.getDouble(0));
            if (slice.load(sDbName, sTable)) {
                addSlice(slice);
            }
        }
    }

    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.writeInt(m_nVersion);
        oos.writeObject(m_ht);
    }

    @SuppressWarnings("unchecked")
    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        m_nVersion = ois.readInt();
        m_ht = (Hashtable<Double, Slice>) ois.readObject();
        if (m_nVersion > 1) {
        } else {
        }
        m_nVersion = VERSION;
    }

    private int m_nVersion;

    private Hashtable<Double, Slice> m_ht;

    private static final int VERSION = 1;
}
