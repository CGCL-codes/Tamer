package org.qsari.effectopedia.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.qsari.effectopedia.base.Describable;
import org.qsari.effectopedia.base.EffectopediaObject;
import org.qsari.effectopedia.core.Effectopedia;
import org.qsari.effectopedia.core.objects.Pathway;
import org.qsari.effectopedia.core.objects.PathwayElement;
import org.qsari.effectopedia.gui.util.MultiIndexSortedList;
import org.qsari.effectopedia.gui.util.ValueComparator;
import org.qsari.effectopedia.history.EditHistoryAction;
import org.qsari.effectopedia.history.SourceID;
import org.qsari.effectopedia.history.SourceTrace;
import org.qsari.effectopedia.history.Stamp;
import org.qsari.effectopedia.history.Stamps;
import org.qsari.effectopedia.system.ActionTypes;

public class DataSourceMerge {

    public DataSourceMerge(DataSource A, DataSource B) {
        this.A = A;
        this.B = B;
        this.map = new HashMap<SourceID, EffectopediaObjectUnion>();
        buildMap();
    }

    public class EffectopediaObjectUnion {

        public EffectopediaObjectUnion(SourceID sourceID) {
            this.sourceId = sourceID;
        }

        public final void setA(ArrayList<EditHistoryAction> A, long IdA) {
            this.A = A;
            this.IDA = IdA;
            compare();
        }

        public final void setB(ArrayList<EditHistoryAction> B, long IdB) {
            this.B = B;
            this.IDB = IdB;
            compare();
        }

        private void compare() {
            if (A == null) if (B == null) status = EQUAL; else status = MORE_OF_B; else if (B == null) status = MORE_OF_A; else status = deepCompare();
        }

        private int deepCompare() {
            if (A.size() > B.size()) return MORE_OF_A; else if (A.size() < B.size()) return MORE_OF_B; else {
                Stamps stamps = Effectopedia.EFFECTOPEDIA.getStamps();
                for (int i = 0; i < A.size(); i++) {
                    Stamp stampIdA = stamps.get((int) A.get(i).getStampId());
                    Stamp stampIdB = stamps.get((int) B.get(i).getStampId());
                    if (!stampIdA.equals(stampIdB)) return DIFFERENT;
                }
                return EQUAL;
            }
        }

        public int getStatus() {
            return this.status;
        }

        public final long getIDA() {
            return IDA;
        }

        public final long getIDB() {
            return IDB;
        }

        public Class<? extends EffectopediaObject> getObjectClassA() {
            if ((A != null) && (A.size() > 0)) return A.get(0).getObjectClass();
            return null;
        }

        public Class<? extends EffectopediaObject> getObjectClassB() {
            if ((B != null) && (B.size() > 0)) return B.get(0).getObjectClass();
            return null;
        }

        public long getTimeStampIDA() {
            if ((A != null) && (A.size() > 0)) return A.get(0).getStampId();
            return 0L;
        }

        public long getTimeStampIDB() {
            if ((B != null) && (B.size() > 0)) return B.get(0).getStampId();
            return 0L;
        }

        public EffectopediaObject getEffectopediaObjectA() {
            if ((A != null) && (A.size() > 0)) return DataSourceMerge.this.A.getLiveObject(A.get(0).getObjectClass(), IDA);
            return null;
        }

        public EffectopediaObject getEffectopediaObjectB() {
            if ((B != null) && (B.size() > 0)) return DataSourceMerge.this.B.getLiveObject(B.get(0).getObjectClass(), IDB);
            return null;
        }

        public SourceID getSourceID() {
            return sourceId;
        }

        private int status = EQUAL;

        private ArrayList<EditHistoryAction> A = null;

        private ArrayList<EditHistoryAction> B = null;

        private long IDA = 0;

        private long IDB = 0;

        public final SourceID sourceId;

        public static final int EQUAL = 1;

        public static final int MORE_OF_A = 2;

        public static final int MORE_OF_B = 4;

        public static final int DIFFERENT = 8;

        public static final int ALL = EQUAL | MORE_OF_A | MORE_OF_B | DIFFERENT;

        public static final int ALL_DIFFERENT = MORE_OF_A | MORE_OF_B | DIFFERENT;
    }

    public static class IDComparator implements ValueComparator<EffectopediaObjectUnion> {

        public int compareToValue(EffectopediaObjectUnion element, Object value) {
            if (element.getIDA() != 0) return ((Long) element.getIDA()).compareTo((Long) value); else return ((Long) element.getIDB()).compareTo((Long) value);
        }

        public int compare(EffectopediaObjectUnion o1, EffectopediaObjectUnion o2) {
            if (o1.getIDA() != 0) return ((Long) o1.getIDA()).compareTo((Long) o2.getIDA()); else return ((Long) o1.getIDB()).compareTo((Long) o2.getIDB());
        }
    }

    public static class TimeStampIDComparator implements ValueComparator<EffectopediaObjectUnion> {

        public int compareToValue(EffectopediaObjectUnion element, Object value) {
            if (element.getTimeStampIDA() != 0) return ((Long) element.getTimeStampIDA()).compareTo((Long) value); else return ((Long) element.getTimeStampIDB()).compareTo((Long) value);
        }

        public int compare(EffectopediaObjectUnion o1, EffectopediaObjectUnion o2) {
            if (o1.getTimeStampIDA() != 0) return ((Long) o1.getTimeStampIDA()).compareTo((Long) o2.getTimeStampIDA()); else return ((Long) o1.getTimeStampIDB()).compareTo((Long) o2.getTimeStampIDB());
        }
    }

    public static class ObjectNameComparator implements ValueComparator<EffectopediaObjectUnion> {

        public int compareToValue(EffectopediaObjectUnion element, Object value) {
            EffectopediaObject eo = element.getEffectopediaObjectA();
            if (eo == null) eo = element.getEffectopediaObjectB();
            if (eo != null) if (eo instanceof PathwayElement) if ((((PathwayElement) eo).getTitle().equals("")) && (eo instanceof Describable)) return ((Describable) eo).getGenericDescription().compareTo(value.toString()); else return ((PathwayElement) eo).getTitle().compareTo(value.toString()); else if (eo instanceof Pathway) return ((Pathway) eo).getTitle().compareTo(value.toString());
            return 0;
        }

        public int compare(EffectopediaObjectUnion o1, EffectopediaObjectUnion o2) {
            EffectopediaObject eo1 = o1.getEffectopediaObjectA();
            EffectopediaObject eo2 = o2.getEffectopediaObjectA();
            if (eo1 == null) {
                eo1 = o1.getEffectopediaObjectB();
                eo2 = o2.getEffectopediaObjectB();
            }
            if ((eo1 != null) && (eo2 != null)) if ((eo1 instanceof PathwayElement) && (eo2 instanceof PathwayElement)) if ((((PathwayElement) eo1).getTitle().equals("")) && (eo1 instanceof Describable) && (((PathwayElement) eo2).getTitle().equals("")) && (eo2 instanceof Describable)) return ((Describable) eo1).getGenericDescription().compareTo(((Describable) eo2).getGenericDescription()); else return ((PathwayElement) eo1).getTitle().compareTo(((PathwayElement) eo2).getTitle()); else if ((eo1 instanceof Pathway) && (eo2 instanceof Pathway)) return ((Pathway) eo1).getTitle().compareTo(((Pathway) eo2).getTitle());
            return 0;
        }
    }

    public static class ClassNameComparator implements ValueComparator<EffectopediaObjectUnion> {

        public int compareToValue(EffectopediaObjectUnion element, Object value) {
            Class<?> c = element.getObjectClassA();
            if (c == null) c = element.getObjectClassB();
            if (c != null) return c.getName().compareTo(value.toString());
            return 0;
        }

        public int compare(EffectopediaObjectUnion o1, EffectopediaObjectUnion o2) {
            Class<?> c1 = o1.getObjectClassA();
            Class<?> c2 = o2.getObjectClassA();
            if (c1 == null) {
                c1 = o1.getObjectClassB();
                c2 = o2.getObjectClassB();
            }
            if ((c1 != null) && (c2 != null)) return c1.getName().compareTo(c2.getName());
            return 0;
        }
    }

    private void buildMap() {
        HashMap<Long, ArrayList<EditHistoryAction>> historyMapA = A.getEditHisotry().getObjectHistoryMap();
        HashMap<Long, ArrayList<EditHistoryAction>> historyMapB = B.getEditHisotry().getObjectHistoryMap();
        HashMap<Long, SourceTrace> sourceTraceA = A.getLivePathwayElements().getMap();
        HashMap<Long, SourceTrace> sourceTraceB = B.getLivePathwayElements().getMap();
        Iterator<Map.Entry<Long, SourceTrace>> it = sourceTraceA.entrySet().iterator();
        while (it.hasNext()) {
            SourceTrace sourceTrace = it.next().getValue();
            SourceID sourceID = sourceTrace.getSourceID();
            EffectopediaObjectUnion eoUnion = new EffectopediaObjectUnion(sourceID);
            long internalID = sourceTrace.getInternalID();
            eoUnion.setA(historyMapA.get(internalID), internalID);
            map.put(sourceID, eoUnion);
        }
        it = sourceTraceB.entrySet().iterator();
        while (it.hasNext()) {
            SourceTrace sourceTrace = it.next().getValue();
            SourceID sourceID = sourceTrace.getSourceID();
            EffectopediaObjectUnion eoUnion = map.get(sourceID);
            if (eoUnion == null) {
                eoUnion = new EffectopediaObjectUnion(sourceID);
                map.put(sourceID, eoUnion);
            }
            long internalID = sourceTrace.getInternalID();
            eoUnion.setB(historyMapB.get(internalID), internalID);
        }
    }

    @SuppressWarnings("unchecked")
    public MultiIndexSortedList<EffectopediaObjectUnion> getThose(int withStatusMask) {
        ValueComparator<EffectopediaObjectUnion>[] valueComparators = new ValueComparator[] { new IDComparator(), new TimeStampIDComparator(), new ClassNameComparator(), new ObjectNameComparator() };
        MultiIndexSortedList<DataSourceMerge.EffectopediaObjectUnion> those = new MultiIndexSortedList<EffectopediaObjectUnion>(valueComparators);
        Iterator<Map.Entry<SourceID, EffectopediaObjectUnion>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            EffectopediaObjectUnion eoUnion = it.next().getValue();
            if ((eoUnion.getStatus() & withStatusMask) != 0) those.add(eoUnion);
        }
        return those;
    }

    public void replaceA(EffectopediaObjectUnion eou) {
        if (eou == null) return;
        replace(eou, eou.getEffectopediaObjectB(), eou.getEffectopediaObjectA(), B, A);
    }

    public void replaceAllA() {
        Iterator<Map.Entry<SourceID, EffectopediaObjectUnion>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            EffectopediaObjectUnion eou = it.next().getValue();
            if (eou.getStatus() != EffectopediaObjectUnion.EQUAL) replace(eou, eou.getEffectopediaObjectB(), eou.getEffectopediaObjectA(), B, A);
        }
    }

    public void replaceB(EffectopediaObjectUnion eou) {
        if (eou == null) return;
        replace(eou, eou.getEffectopediaObjectA(), eou.getEffectopediaObjectB(), A, B);
    }

    public void replaceAllB() {
        Iterator<Map.Entry<SourceID, EffectopediaObjectUnion>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            EffectopediaObjectUnion eou = it.next().getValue();
            if (eou.getStatus() != EffectopediaObjectUnion.EQUAL) replace(eou, eou.getEffectopediaObjectA(), eou.getEffectopediaObjectB(), A, B);
        }
    }

    private void replace(EffectopediaObjectUnion eou, EffectopediaObject eoSource, EffectopediaObject eoDestintation, DataSource source, DataSource destination) {
        if (eoSource != null) {
            if (eoDestintation != null) {
                destination.modifyObject(eoDestintation.getClass(), eoDestintation, DSM_REPLACE_OJECT_AID);
                EffectopediaObject temp = new EffectopediaObject();
                eoDestintation.cloneIDs(temp);
                eoDestintation = createCopy(eoSource, source, destination);
                temp.cloneIDs(eoDestintation);
                destination.add(eoDestintation.getClass(), eoDestintation);
            } else {
                eoDestintation = createCopy(eoSource, source, destination);
                eoDestintation.autoSetId();
                eoDestintation.setExternalID(0);
                destination.addLiveObject(eoDestintation, eou.getSourceID());
            }
            eou.status = EffectopediaObjectUnion.EQUAL;
        }
    }

    private EffectopediaObject createCopy(EffectopediaObject ofObject, DataSource source, DataSource destination) {
        EffectopediaObject result = ofObject.clone();
        HashMap<Long, EffectopediaObject> contained = new HashMap<Long, EffectopediaObject>();
        result.getContainedIDs(contained);
        Iterator<Map.Entry<Long, EffectopediaObject>> iterator = contained.entrySet().iterator();
        while (iterator.hasNext()) {
            EffectopediaObject eo = iterator.next().getValue();
            if (eo == null) continue;
            SourceTrace st = source.getLivePathwayElements().getSourceTraceByID(eo.getID());
            EffectopediaObjectUnion eou = (st != null) ? map.get(st.getSourceID()) : null;
            if (eou != null) {
            } else if (!eo.isDefaultID()) {
                eo.setIDandExternalIDtoZero();
                eo.autoSetId();
                destination.bringToLive(eo.getClass(), eo);
            }
        }
        result.setIDandExternalIDtoZero();
        return result;
    }

    public int size() {
        return map.size();
    }

    public final DataSource getA() {
        return A;
    }

    public final DataSource getB() {
        return B;
    }

    private HashMap<SourceID, EffectopediaObjectUnion> map;

    private final DataSource A;

    private final DataSource B;

    public static final int DSM_REPLACE_OJECT_AID = ActionTypes.REGISTERED.newActionType("replace", "replace object version with a one from another datasource");
}
