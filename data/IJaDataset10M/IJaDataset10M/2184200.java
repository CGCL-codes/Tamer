package org.zamia;

import java.util.HashSet;
import javax.swing.event.EventListenerList;
import org.zamia.ZamiaException.ExCat;
import org.zamia.util.HashSetArray;
import org.zamia.util.ZStack;
import org.zamia.vhdl.ast.VHDLNode;
import org.zamia.zdb.ZDB;

/**
 * Error manager
 * 
 * stores, sorts and filters error messages
 * 
 * @author Guenter Bartsch
 * 
 */
public class ERManager {

    protected static final ZamiaLogger logger = ZamiaLogger.getInstance();

    protected static final ExceptionLogger el = ExceptionLogger.getInstance();

    private static final String ERRORS_OBJ_NAME = "AllErrors";

    private static final String ERROR_IDX = "ErrorIdx";

    private HashSetArray<Long> fErrors;

    private final ZamiaProject fZPrj;

    private final ZDB fZDB;

    private EventListenerList fObservers = new EventListenerList();

    private boolean fQuiet = false;

    @SuppressWarnings("unchecked")
    public ERManager(ZamiaProject aZPrj) {
        fZPrj = aZPrj;
        fZDB = fZPrj.getZDB();
        fErrors = (HashSetArray<Long>) fZDB.getNamedObject(ERRORS_OBJ_NAME);
        if (fErrors == null) {
            fErrors = new HashSetArray<Long>();
            fZDB.createNamedObject(ERRORS_OBJ_NAME, fErrors);
        }
    }

    public synchronized int getNumErrors() {
        return fErrors.size();
    }

    public synchronized ZamiaException getError(int aIdx) {
        Long dbid = fErrors.get(aIdx);
        if (dbid == null) {
            return null;
        }
        return (ZamiaException) fZDB.load(dbid.longValue());
    }

    @SuppressWarnings("unchecked")
    public synchronized ZamiaException getError(SourceFile aSF, int aIdx) {
        String path = aSF.getAbsolutePath();
        HashSetArray<Long> errs = (HashSetArray<Long>) fZDB.getIdxObj(ERROR_IDX, path);
        if (errs == null) {
            return null;
        }
        return (ZamiaException) fZDB.load(errs.get(aIdx));
    }

    @SuppressWarnings("unchecked")
    public synchronized void addError(ZamiaException aMsg) {
        long dbid = fZDB.store(aMsg);
        fErrors.add(dbid);
        SourceLocation location = aMsg.getLocation();
        if (location == null) {
            logger.error("ERManager: Unlocated error: %s", aMsg.getMessage());
            return;
        }
        SourceFile sf = location.fSF;
        String path = sf.getAbsolutePath();
        long errsDBID = fZDB.getIdx(ERROR_IDX, path);
        HashSetArray<Long> errs = null;
        if (errsDBID != 0) {
            errs = (HashSetArray<Long>) fZDB.load(errsDBID);
        } else {
            errs = new HashSetArray<Long>();
            errsDBID = fZDB.store(errs);
            fZDB.putIdx(ERROR_IDX, path, errsDBID);
        }
        errs.add(dbid);
        fZDB.update(errsDBID, errs);
        logger.debug("ERManager: Error added: %s", aMsg.toString());
        notifyErrorAdded(aMsg);
        int nlines = 12;
        int line = location.fLine - nlines / 2;
        if (line < 0) {
            line = 0;
        }
        if (FSCache.getInstance().exists(sf, true)) {
            while (line < location.fLine + nlines / 2) {
                String str = sf.extractLine(line);
                if (line == location.fLine) {
                    logger.debug("ERManager:    *** %5d: %s", line, str);
                } else {
                    logger.debug("ERManager:        %5d: %s", line, str);
                }
                line++;
            }
        }
        StackTraceElement[] st = aMsg.getStackTrace();
        if (st != null) {
            int n = st.length;
            for (int i = 0; i < n; i++) {
                logger.debug("ERManager:    Stack trace: %3d: %s", i, st[i]);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public synchronized void removeErrors(SourceFile aSF) {
        String path = aSF.getAbsolutePath();
        boolean changed = false;
        long dbidErr = fZDB.getIdx(ERROR_IDX, path);
        HashSetArray<Long> errs = null;
        if (dbidErr != 0) {
            errs = (HashSetArray<Long>) fZDB.load(dbidErr);
            int n = errs.size();
            for (int i = 0; i < n; i++) {
                Long err = errs.get(i);
                if (err != null) {
                    fZDB.delete(err.longValue());
                    fErrors.remove(err);
                    changed = true;
                }
            }
            fZDB.delete(dbidErr);
            fZDB.delIdx(ERROR_IDX, path);
        }
        if (changed) {
            notifyErrorsChanged(aSF);
        }
    }

    public synchronized void clean() {
        fErrors = new HashSetArray<Long>();
        fZDB.createNamedObject(ERRORS_OBJ_NAME, fErrors);
        fZDB.delAllIdx(ERROR_IDX);
        notifyCleaned();
    }

    @SuppressWarnings("unchecked")
    public synchronized int getNumErrors(SourceFile aSF) {
        String path = aSF.getAbsolutePath();
        HashSetArray<Long> errs = (HashSetArray<Long>) fZDB.getIdxObj(ERROR_IDX, path);
        if (errs == null) return 0;
        return errs.size();
    }

    @SuppressWarnings("unchecked")
    public synchronized void removeErrors(SourceFile aSF, ExCat aCat) {
        String path = aSF.getAbsolutePath();
        boolean changed = false;
        long dbidErr = fZDB.getIdx(ERROR_IDX, path);
        HashSetArray<Long> errs = null;
        if (dbidErr != 0) {
            errs = (HashSetArray<Long>) fZDB.load(dbidErr);
            int i = 0;
            while (i < errs.size()) {
                Long err = errs.get(i);
                long dbid = err.longValue();
                ZamiaException msg = (ZamiaException) fZDB.load(dbid);
                if (msg != null && msg.getCat() == aCat) {
                    fZDB.delete(dbid);
                    fErrors.remove(err);
                    errs.remove(i);
                    changed = true;
                } else {
                    i++;
                }
            }
            fZDB.update(dbidErr, errs);
        }
        if (changed) {
            notifyErrorsChanged(aSF);
        }
    }

    /**
	 * remove all errors related to the given statement recursively
	 * 
	 * @param aAO
	 * @param aCat
	 */
    @SuppressWarnings("unchecked")
    public synchronized void removeErrors(VHDLNode aAO, ExCat aCat) {
        SourceLocation location = aAO.getLocation();
        if (location == null) {
            return;
        }
        SourceFile sf = location.fSF;
        String path = sf.getAbsolutePath();
        long dbidErr = fZDB.getIdx(ERROR_IDX, path);
        HashSetArray<Long> errs = null;
        if (dbidErr != 0) {
            errs = (HashSetArray<Long>) fZDB.load(dbidErr);
            ZStack<VHDLNode> stack = new ZStack<VHDLNode>();
            stack.push(aAO);
            HashSet<VHDLNode> done = new HashSet<VHDLNode>();
            while (!stack.isEmpty()) {
                VHDLNode ao = stack.pop();
                if (done.contains(ao)) {
                    continue;
                }
                done.add(ao);
                int n = ao.getNumChildren();
                for (int i = 0; i < n; i++) {
                    VHDLNode child = ao.getChild(i);
                    if (child != null) {
                        stack.push(child);
                    }
                }
                location = ao.getLocation();
                int i = 0;
                while (i < errs.size()) {
                    Long err = errs.get(i);
                    long dbid = err.longValue();
                    ZamiaException msg = (ZamiaException) fZDB.load(dbid);
                    if (msg == null || msg.getCat() != aCat || msg.getLocation() == null || !msg.getLocation().equals(location)) {
                        i++;
                        continue;
                    }
                    fZDB.delete(dbid);
                    fErrors.remove(err);
                    errs.remove(i);
                }
            }
            fZDB.update(dbidErr, errs);
        }
        notifyErrorsChanged(sf);
    }

    @SuppressWarnings("unchecked")
    public synchronized void removeErrors(ExCat aCat) {
        HashSetArray<SourceFile> changedSFs = new HashSetArray<SourceFile>();
        int i = 0;
        while (i < fErrors.size()) {
            Long dbid = fErrors.get(i);
            if (dbid == null) {
                i++;
                continue;
            }
            ZamiaException err = (ZamiaException) fZDB.load(dbid.longValue());
            if (err == null || err.getCat() != aCat) {
                i++;
                continue;
            }
            fErrors.remove(i);
            SourceLocation location = err.getLocation();
            SourceFile sf = location.fSF;
            changedSFs.add(sf);
            String path = sf.getAbsolutePath();
            long errsDBID = fZDB.getIdx(ERROR_IDX, path);
            HashSetArray<Long> errs = null;
            if (errsDBID != 0) {
                errs = (HashSetArray<Long>) fZDB.load(errsDBID);
                int j = 0;
                while (j < errs.size()) {
                    Long dbid2 = errs.get(j);
                    if (dbid2 == null) {
                        j++;
                        continue;
                    }
                    ZamiaException err2 = (ZamiaException) fZDB.load(dbid2.longValue());
                    if (err2 == null || err2.getCat() != aCat) {
                        j++;
                        continue;
                    }
                    errs.remove(j);
                }
                fZDB.update(errsDBID, errs);
            }
        }
        int n = changedSFs.size();
        for (i = 0; i < n; i++) {
            SourceFile sf = changedSFs.get(i);
            if (sf == null) {
                continue;
            }
            notifyErrorsChanged(sf);
        }
    }

    public void addObserver(ErrorObserver o) {
        fObservers.add(ErrorObserver.class, o);
    }

    public void removeObserver(ErrorObserver o) {
        fObservers.remove(ErrorObserver.class, o);
    }

    private void notifyErrorsChanged(SourceFile aSF) {
        if (isQuiet()) {
            return;
        }
        Object[] listeners = fObservers.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ErrorObserver.class) {
                ((ErrorObserver) listeners[i + 1]).notifyErrorsChanged(fZPrj, aSF);
            }
        }
    }

    private void notifyErrorsChanged() {
        if (isQuiet()) {
            return;
        }
        Object[] listeners = fObservers.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ErrorObserver.class) {
                ((ErrorObserver) listeners[i + 1]).notifyErrorsChanged(fZPrj);
            }
        }
    }

    private void notifyErrorAdded(ZamiaException aMsg) {
        if (isQuiet()) {
            return;
        }
        Object[] listeners = fObservers.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ErrorObserver.class) {
                ((ErrorObserver) listeners[i + 1]).notifyErrorAdded(fZPrj, aMsg);
            }
        }
    }

    private void notifyCleaned() {
        if (isQuiet()) {
            return;
        }
        Object[] listeners = fObservers.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ErrorObserver.class) {
                ((ErrorObserver) listeners[i + 1]).notifyCleaned(fZPrj);
            }
        }
    }

    public void setQuiet(boolean quiet) {
        fQuiet = quiet;
    }

    public boolean isQuiet() {
        return fQuiet;
    }

    @SuppressWarnings("unchecked")
    public synchronized void zdbChanged() {
        fErrors = (HashSetArray<Long>) fZDB.getNamedObject(ERRORS_OBJ_NAME);
        notifyErrorsChanged();
    }
}
