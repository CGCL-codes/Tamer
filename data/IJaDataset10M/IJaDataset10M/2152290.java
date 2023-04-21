package xreplicator.core;

import org.snia.xam.*;
import java.io.*;
import java.util.*;
import java.util.Map.Entry;

public class RBaseAlgorithm implements RAlgorithm {

    private static final int DEFAULT_BUF_SIZE = 10000;

    private static final String DEFAULT_FILE_NAME = "Replication.alg";

    private int bufSize;

    private String fileName;

    private RStatistics statistics;

    public RBaseAlgorithm(String fileName, int bufSize) {
        this.fileName = fileName;
        this.bufSize = bufSize;
        statistics = new RStatistics();
    }

    public RBaseAlgorithm(String fileName) {
        this(fileName, DEFAULT_BUF_SIZE);
    }

    public RBaseAlgorithm(int bufSize) {
        this(DEFAULT_FILE_NAME, bufSize);
    }

    public RBaseAlgorithm() {
        this(DEFAULT_FILE_NAME, DEFAULT_BUF_SIZE);
    }

    public RStatistics synchronize(Set<XElement> storages) throws XAMException, IOException, ClassNotFoundException {
        statistics = new RStatistics();
        Collection<XUID> outXUIDs, xuids;
        HashMap<String, HashMap<String, XUIDInfo>> preSynchronize;
        HashMap<String, XUIDInfo> preStorage, preStorage2;
        HashMap<String, Set<String>> deletedXSystems = new HashMap<String, Set<String>>();
        Set<String> deletedXSets;
        Set<XElement> ininoutElements = getXElements(storages, XReplicator.MODE_INOUT, XReplicator.MODE_IN), inElements = getXElements(ininoutElements, XReplicator.MODE_IN), inoutElements = getXElements(ininoutElements, XReplicator.MODE_INOUT), outElements = getXElements(storages, XReplicator.MODE_OUT);
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName));
            preSynchronize = (HashMap<String, HashMap<String, XUIDInfo>>) ois.readObject();
            ois.close();
        } catch (Exception e) {
            preSynchronize = new HashMap<String, HashMap<String, XUIDInfo>>();
        }
        if (inElements != null) for (XElement element : inElements) {
            preStorage = preSynchronize.get(element.getXRI());
            if (preStorage != null) {
                Set<String> keySet = preStorage.keySet();
                if (keySet != null) {
                    deletedXSets = new HashSet<String>(keySet);
                    deletedXSystems.put(element.getXRI(), deletedXSets);
                }
            }
        }
        if (inoutElements != null) for (XElement element : inoutElements) {
            xuids = getXUIDs(element);
            preStorage = preSynchronize.get(element.getXRI());
            if (preStorage == null) {
                preStorage = new HashMap<String, XUIDInfo>();
                for (XUID xuid : xuids) {
                    XUIDInfo xuidInfo = new XUIDInfo();
                    XSet set = element.getXSystem().openXSet(xuid, XSet.MODE_READ_ONLY);
                    xuidInfo.timeCommit = set.getDateTime(XSet.XAM_TIME_COMMIT).getTime();
                    set.close();
                    xuidInfo.timeSynchronize = xuidInfo.timeCommit;
                    preStorage.put(xuid.toString(), xuidInfo);
                }
                preSynchronize.put(element.getXRI(), preStorage);
            } else {
                Set<String> keySet = preStorage.keySet();
                deletedXSets = new HashSet<String>(keySet);
                deletedXSystems.put(element.getXRI(), deletedXSets);
                for (XUID xuid : xuids) {
                    XUIDInfo xuidInfo = preStorage.get(xuid.toString());
                    if (xuidInfo == null) {
                        xuidInfo = new XUIDInfo();
                        XSet set = element.getXSystem().openXSet(xuid, XSet.MODE_READ_ONLY);
                        xuidInfo.timeCommit = set.getDateTime(XSet.XAM_TIME_COMMIT).getTime();
                        set.close();
                        xuidInfo.timeSynchronize = xuidInfo.timeCommit;
                        preStorage.put(xuid.toString(), xuidInfo);
                    } else {
                        XSet set = element.getXSystem().openXSet(xuid, XSet.MODE_RESTRICTED);
                        Date time = set.getDateTime(XSet.XAM_TIME_COMMIT).getTime();
                        set.close();
                        if (!time.equals(xuidInfo.timeSynchronize)) {
                            xuidInfo.timeCommit = time;
                            xuidInfo.timeSynchronize = time;
                        }
                        deletedXSets.remove(xuid.toString());
                    }
                }
            }
        }
        if ((outElements != null) && (inElements != null)) for (XElement outElement : outElements) {
            outXUIDs = getXUIDs(outElement);
            for (XElement inElement : inElements) {
                xuids = setConstrain(outXUIDs, inElement);
                preStorage = preSynchronize.get(inElement.getXRI());
                if (preStorage != null) {
                    deletedXSets = deletedXSystems.get(inElement.getXRI());
                    for (XUID xuid : xuids) {
                        XUIDInfo xuidInfo = preStorage.get(xuid.toString());
                        XSet xSet = outElement.getXSystem().openXSet(xuid, XSet.MODE_UNRESTRICTED);
                        Date timeCommit = xSet.getDateTime(XSet.XAM_TIME_COMMIT).getTime();
                        xSet.close();
                        if (xuidInfo != null) {
                            if (!timeCommit.equals(xuidInfo.timeCommit)) {
                                xuidInfo.timeSynchronize = updateXSet(outElement.getXSystem(), inElement.getXSystem(), xuid);
                                xuidInfo.timeCommit = timeCommit;
                                preStorage.put(xuid.toString(), xuidInfo);
                            }
                            deletedXSets.remove(xuid.toString());
                        } else {
                            xuidInfo = new XUIDInfo(timeCommit, copyXSet(outElement.getXSystem(), inElement.getXSystem(), xuid));
                            preStorage.put(xuid.toString(), xuidInfo);
                        }
                    }
                } else {
                    preStorage = new HashMap<String, XUIDInfo>();
                    for (XUID xuid : xuids) {
                        XSet xSet = outElement.getXSystem().openXSet(xuid, XSet.MODE_UNRESTRICTED);
                        Date timeCommit = xSet.getDateTime(XSet.XAM_TIME_COMMIT).getTime();
                        xSet.close();
                        XUIDInfo xuidInfo = new XUIDInfo(timeCommit, copyXSet(outElement.getXSystem(), inElement.getXSystem(), xuid));
                        preStorage.put(xuid.toString(), xuidInfo);
                    }
                    preSynchronize.put(inElement.getXRI(), preStorage);
                }
            }
        }
        if ((inoutElements != null) && (inElements != null)) for (XElement outElement : inoutElements) {
            preStorage = preSynchronize.get(outElement.getXRI());
            outXUIDs = getXUIDs(outElement);
            for (XElement inElement : inElements) {
                xuids = setConstrain(outXUIDs, inElement);
                preStorage2 = preSynchronize.get(inElement.getXRI());
                if (preStorage2 != null) {
                    deletedXSets = deletedXSystems.get(inElement.getXRI());
                    for (XUID xuid : xuids) {
                        XUIDInfo xuidInfo = preStorage.get(xuid.toString());
                        XUIDInfo xuidInfo2 = preStorage2.get(xuid.toString());
                        if (xuidInfo2 != null) {
                            if (!xuidInfo.timeCommit.equals(xuidInfo2.timeCommit)) {
                                xuidInfo2.timeSynchronize = updateXSet(outElement.getXSystem(), inElement.getXSystem(), xuid);
                                xuidInfo2.timeCommit = xuidInfo.timeCommit;
                                preStorage2.put(xuid.toString(), xuidInfo2);
                            }
                            deletedXSets.remove(xuid.toString());
                        } else {
                            xuidInfo2 = new XUIDInfo(xuidInfo.timeCommit, copyXSet(outElement.getXSystem(), inElement.getXSystem(), xuid));
                            preStorage2.put(xuid.toString(), xuidInfo2);
                        }
                    }
                } else {
                    preStorage2 = new HashMap<String, XUIDInfo>();
                    for (XUID xuid : xuids) {
                        XUIDInfo xuidInfo = new XUIDInfo(preStorage.get(xuid.toString()).timeCommit, copyXSet(outElement.getXSystem(), inElement.getXSystem(), xuid));
                        preStorage2.put(xuid.toString(), xuidInfo);
                    }
                    preSynchronize.put(inElement.getXRI(), preStorage2);
                }
            }
        }
        if ((outElements != null) && (inoutElements != null)) for (XElement outElement : outElements) {
            outXUIDs = getXUIDs(outElement);
            for (XElement inElement : inoutElements) {
                xuids = setConstrain(outXUIDs, inElement);
                preStorage = preSynchronize.get(inElement.getXRI());
                for (XUID xuid : xuids) {
                    XUIDInfo xuidInfo = preStorage.get(xuid.toString());
                    XSet xSet = outElement.getXSystem().openXSet(xuid, XSet.MODE_UNRESTRICTED);
                    Date timeCommit = xSet.getDateTime(XSet.XAM_TIME_COMMIT).getTime();
                    xSet.close();
                    if (xuidInfo != null) {
                        if (!timeCommit.equals(xuidInfo.timeCommit)) {
                            xuidInfo.timeSynchronize = updateXSet(outElement.getXSystem(), inElement.getXSystem(), xuid);
                            xuidInfo.timeCommit = timeCommit;
                            preStorage.put(xuid.toString(), xuidInfo);
                        }
                    } else {
                        xuidInfo = new XUIDInfo(timeCommit, copyXSet(outElement.getXSystem(), inElement.getXSystem(), xuid));
                        preStorage.put(xuid.toString(), xuidInfo);
                    }
                }
            }
        }
        if ((inoutElements != null) && inoutElements.size() > 1) for (XElement outElement : inoutElements) {
            preStorage = preSynchronize.get(outElement.getXRI());
            outXUIDs = getXUIDs(outElement);
            for (XElement inElement : inoutElements) {
                if (inElement == outElement) continue;
                xuids = setConstrain(outXUIDs, inElement);
                preStorage2 = preSynchronize.get(inElement.getXRI());
                for (XUID xuid : xuids) {
                    XUIDInfo xuidInfo = preStorage.get(xuid.toString());
                    XUIDInfo xuidInfo2 = preStorage2.get(xuid.toString());
                    if (xuidInfo2 != null) {
                        if (!xuidInfo.timeCommit.equals(xuidInfo2.timeCommit)) {
                            xuidInfo2.timeSynchronize = updateXSet(outElement.getXSystem(), inElement.getXSystem(), xuid);
                            xuidInfo2.timeCommit = xuidInfo.timeCommit;
                            preStorage2.put(xuid.toString(), xuidInfo2);
                        }
                    } else {
                        xuidInfo2 = new XUIDInfo(xuidInfo.timeCommit, copyXSet(outElement.getXSystem(), inElement.getXSystem(), xuid));
                        preStorage2.put(xuid.toString(), xuidInfo2);
                    }
                }
            }
        }
        if ((inElements != null) || !ininoutElements.isEmpty()) for (Map.Entry<String, Set<String>> entry : deletedXSystems.entrySet()) {
            preStorage = preSynchronize.get(entry.getKey());
            XSystem system = getXSystem(ininoutElements, entry.getKey());
            for (String xuid : entry.getValue()) {
                deleteXSet(system, xuid);
                preStorage.remove(xuid);
            }
        }
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName));
        oos.writeObject(preSynchronize);
        oos.close();
        return statistics;
    }

    private Set<XElement> getXElements(Set<XElement> storages, String mode) {
        Set<XElement> result = new HashSet<XElement>();
        for (XElement xElement : storages) {
            if (xElement.getMode().equals(mode)) result.add(xElement);
        }
        if (result.size() != 0) return result; else return null;
    }

    private Set<XElement> getXElements(Set<XElement> storages, String mode1, String mode2) {
        Set<XElement> result = new HashSet<XElement>();
        for (XElement xElement : storages) {
            String mode = xElement.getMode();
            if (mode.equals(mode1) || mode.equals(mode2)) result.add(xElement);
        }
        return result;
    }

    private XSystem getXSystem(Set<XElement> storages, String xri) {
        for (XElement xElement : storages) {
            if (xElement.getXRI().equals(xri)) return xElement.getXSystem();
        }
        return null;
    }

    private Collection<XUID> getXUIDs(XElement xElement) throws XAMException {
        String type = xElement.getConstraintType();
        Object o = xElement.getConstraint();
        if (XReplicator.CONSTRAINT_TYPE_XUIDS.equals(type)) {
            return (Collection<XUID>) o;
        }
        String query;
        if (XReplicator.CONSTRAINT_TYPE_QUERY.equals(type)) {
            query = (String) o;
        } else {
            query = "select \".xset.xuid\"";
        }
        return query(xElement.getXSystem(), query);
    }

    private Collection<XUID> setConstrain(Collection<XUID> xuids, XElement xElement) throws XAMException {
        Object constraint = xElement.getConstraint();
        if (constraint == null) return new HashSet<XUID>(xuids);
        Collection<XUID> result;
        if (XReplicator.CONSTRAINT_TYPE_XUIDS.equals(xElement.getConstraintType())) {
            result = (Collection<XUID>) constraint;
        } else {
            result = query(xElement.getXSystem(), (String) constraint);
        }
        Iterator<XUID> iter = result.iterator();
        while (iter.hasNext()) {
            if (!xuids.contains(iter.next())) iter.remove();
        }
        return result;
    }

    private Collection<XUID> query(XSystem xSys, String sQuery) throws XAMException {
        XSet query = xSys.createXSet(XSet.MODE_RESTRICTED);
        query.createProperty(XSet.XAM_JOB_COMMAND, false, XSet.XAM_JOB_QUERY);
        XStream queryStream = query.createXStream(XSet.XAM_JOB_QUERY_COMMAND, false, XAMLibrary.TEXT_PLAIN_MIME_TYPE);
        queryStream.write(sQuery.getBytes());
        queryStream.close();
        query.submitJob();
        boolean finished = false;
        while (!finished) {
            if (query.containsField(XSet.XAM_JOB_ERROR)) if (query.getString(XSet.XAM_JOB_ERRORHEALTH).equals(XSet.XAM_JOB_ERRORHEALTH_ERROR)) {
                query.haltJob();
                throw new QueryException();
            }
            if (query.getString(XSet.XAM_JOB_STATUS).equals(XSet.XAM_JOB_STATUS_COMPLETE)) {
                finished = true;
            }
        }
        Collection<XUID> xuids = new ArrayList<XUID>();
        queryStream = query.openXStream(XSet.XAM_JOB_QUERY_RESULTS, XStream.MODE_READ_ONLY);
        byte rawXUID[] = new byte[XUID.MAX_LENGTH];
        while (queryStream.read(rawXUID) >= 0) {
            XUID xuid = xSys.createXUID(rawXUID);
            xuids.add(xuid);
        }
        queryStream.close();
        query.close();
        return xuids;
    }

    private Date copyXSet(XSystem outXSystem, XSystem inXSystem, XUID xuid) throws XAMException {
        XSet eSet = outXSystem.openXSet(xuid, XSet.MODE_UNRESTRICTED), iSet = inXSystem.createXSet(XSet.MODE_UNRESTRICTED);
        XStream eStream = eSet.openExportXStream(), iStream = iSet.openImportXStream();
        Date timeSynchronize;
        byte[] buffer = new byte[bufSize];
        long byteReads;
        while ((byteReads = eStream.read(buffer)) > 0) {
            iStream.write(buffer, byteReads);
        }
        eStream.close();
        iStream.close();
        iSet.commit();
        timeSynchronize = iSet.getDateTime(XSet.XAM_TIME_COMMIT).getTime();
        eSet.close();
        iSet.close();
        statistics.incAdded();
        return timeSynchronize;
    }

    private Date updateXSet(XSystem outXSystem, XSystem inXSystem, XUID xuid) throws XAMException {
        inXSystem.deleteXSet(xuid);
        XSet eSet = outXSystem.openXSet(xuid, XSet.MODE_UNRESTRICTED), iSet = inXSystem.createXSet(XSet.MODE_UNRESTRICTED);
        XStream eStream = eSet.openExportXStream(), iStream = iSet.openImportXStream();
        Date timeSynchronize;
        byte[] buffer = new byte[bufSize];
        long byteReads;
        while ((byteReads = eStream.read(buffer)) > 0) {
            iStream.write(buffer, byteReads);
        }
        eStream.close();
        iStream.close();
        iSet.commit();
        timeSynchronize = iSet.getDateTime(XSet.XAM_TIME_COMMIT).getTime();
        eSet.close();
        iSet.close();
        statistics.incUpdated();
        return timeSynchronize;
    }

    private void deleteXSet(XSystem xSystem, String sXUID) throws XAMException {
        XUID xuid = xSystem.createXUID(sXUID);
        try {
            xSystem.deleteXSet(xuid);
            statistics.incDeleted();
        } catch (XAMException e) {
            if (!(e.getCause() instanceof FileNotFoundException)) throw e;
        }
    }
}

class XUIDInfo implements Serializable {

    public Date timeCommit;

    public Date timeSynchronize;

    XUIDInfo(Date timeCommit, Date timeSynchronize) {
        this.timeSynchronize = timeSynchronize;
        this.timeCommit = timeCommit;
    }

    XUIDInfo() {
    }
}
