package net.sourceforge.nattable.support;

import java.util.concurrent.locks.ReentrantReadWriteLock;
import net.sourceforge.nattable.NatTable;
import net.sourceforge.nattable.util.ConflationQueue;
import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Display;

/**
 * Author : Andy Tsoi<br>
 * Created Date : Jun 10, 2008<br>
 * Description: Table Handler class to manage update event of natTable and
 * redraw the screen 3 times per second.
 */
public class ConflationSupport {

    class ConflationEvent {

        private int start = -1;

        private int end = -1;

        private ConflationEvent(int start, int end) {
            this.start = start;
            this.end = end;
        }

        public int getStart() {
            return start;
        }

        public int getEnd() {
            return end;
        }

        public void reset() {
            start = -1;
            end = -1;
        }

        public boolean isEmpty() {
            return start == -1 && end == -1;
        }
    }

    public static final Logger log = Logger.getLogger(ConflationSupport.class);

    private NatTable natTable;

    private String ADD_ID;

    private String REMOVE_ID;

    private String UPDATE_ID;

    private ReentrantReadWriteLock updatelock = new ReentrantReadWriteLock();

    private ReentrantReadWriteLock insertlock = new ReentrantReadWriteLock();

    private ReentrantReadWriteLock deletelock = new ReentrantReadWriteLock();

    private ConflationEvent updateEvent;

    private ConflationEvent insertEvent;

    private ConflationEvent deleteEvent;

    private Runnable updateRunnable = new Runnable() {

        public void run() {
            try {
                updatelock.writeLock().lock();
                if (updateEvent != null) {
                    final int start = updateEvent.start;
                    final int end = updateEvent.end;
                    updateEvent.reset();
                    Display.getDefault().asyncExec(new Runnable() {

                        public void run() {
                            if (!natTable.isDisposed()) natTable.redrawUpdatedBodyRow(start, end);
                        }
                    });
                }
            } catch (Exception e) {
                log.error(e);
            } finally {
                updatelock.writeLock().unlock();
            }
        }
    };

    private Runnable insertRunnable = new Runnable() {

        public void run() {
            try {
                insertlock.writeLock().lock();
                if (insertEvent != null) {
                    final int start = insertEvent.start;
                    final int end = insertEvent.end;
                    insertEvent.reset();
                    Display.getDefault().asyncExec(new Runnable() {

                        public void run() {
                            if (!natTable.isDisposed()) natTable.redrawInsertedBodyRow(start, end);
                        }
                    });
                }
            } catch (Exception e) {
                log.error(e);
            } finally {
                insertlock.writeLock().unlock();
            }
        }
    };

    private Runnable deleteRunnable = new Runnable() {

        public void run() {
            try {
                deletelock.writeLock().lock();
                if (deleteEvent != null) {
                    final int start = deleteEvent.start;
                    final int end = deleteEvent.end;
                    deleteEvent.reset();
                    Display.getDefault().asyncExec(new Runnable() {

                        public void run() {
                            if (!natTable.isDisposed()) natTable.redrawDeletedBodyRow(start, end);
                        }
                    });
                }
            } catch (Exception e) {
                log.error(e);
            } finally {
                deletelock.writeLock().unlock();
            }
        }
    };

    public ConflationSupport(NatTable natTable) {
        this.natTable = natTable;
    }

    public void redrawInsertedRow(int fromRow, int toRow) {
        try {
            insertlock.writeLock().lock();
            if (ADD_ID == null) {
                ADD_ID = natTable.getNatTableModel().getModelID() + ".ADD";
            }
            if (insertEvent != null && !insertEvent.isEmpty()) {
                insertEvent = new ConflationEvent(insertEvent.start < fromRow ? insertEvent.start : fromRow, insertEvent.end > toRow ? insertEvent.end : toRow);
            } else {
                insertEvent = new ConflationEvent(fromRow, toRow);
            }
        } catch (Exception e) {
            log.error(e);
        } finally {
            insertlock.writeLock().unlock();
            ConflationQueue.getInstance().addRunnable(ADD_ID, insertRunnable);
        }
    }

    public void redrawDeletedRow(int fromRow, int toRow) {
        try {
            deletelock.writeLock().lock();
            if (REMOVE_ID == null) {
                REMOVE_ID = natTable.getNatTableModel().getModelID() + ".REMOVE";
            }
            if (deleteEvent != null && !deleteEvent.isEmpty()) {
                deleteEvent = new ConflationEvent(deleteEvent.start < fromRow ? deleteEvent.start : fromRow, deleteEvent.end > toRow ? deleteEvent.end : toRow);
            } else {
                deleteEvent = new ConflationEvent(fromRow, toRow);
            }
        } catch (Exception e) {
            log.error(e);
        } finally {
            deletelock.writeLock().unlock();
            ConflationQueue.getInstance().addRunnable(REMOVE_ID, deleteRunnable);
        }
    }

    public void redrawUpdatedRow(int fromRow, int toRow) {
        try {
            updatelock.writeLock().lock();
            if (UPDATE_ID == null) {
                UPDATE_ID = natTable.getNatTableModel().getModelID() + ".UPDATE";
            }
            if (updateEvent != null && !updateEvent.isEmpty()) {
                updateEvent = new ConflationEvent(updateEvent.start < fromRow ? updateEvent.start : fromRow, updateEvent.end > toRow ? updateEvent.end : toRow);
            } else {
                updateEvent = new ConflationEvent(fromRow, toRow);
            }
        } catch (Exception e) {
            log.error(e);
        } finally {
            updatelock.writeLock().unlock();
            ConflationQueue.getInstance().addRunnable(UPDATE_ID, updateRunnable);
        }
    }
}
