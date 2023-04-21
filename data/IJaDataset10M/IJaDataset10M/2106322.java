package data.stdforms.twotableformsheet;

import data.*;
import data.stdforms.*;
import sale.*;
import users.*;

/**
  * MoveStrategy for a Catalog source and destination.
  *
  * @author Steffen Zschaler
  * @version 2.0 20/08/1999
  * @since v2.0
  */
public class CCStrategy extends MoveStrategy {

    /**
    * Get the sub-process that will move items from the source to the destination.
    *
    * @param p the process into which the sub-process wil be embedded.
    * @param sp the SalesPoint, if any, at which the FormSheet is being displayed.
    * @param cSource the source Catalog.
    * @param cDest the destination Catalog.
    * @param db the DataBasket relative to which to perform the operation.
    * @param ci the CatalogItem that is selected in the source.
    * @param ttfs the FormSheet that triggers the process.
    *
    * @override Never
    */
    public Transition getMoveToDestProcess(SaleProcess p, SalesPoint sp, Catalog cSource, Catalog cDest, DataBasket db, CatalogItem ci, TwoTableFormSheet ttfs) {
        return new GateChangeTransition(getCheckMoveToDestGate(p, sp, cSource, cDest, db, ci, ttfs));
    }

    /**
    * Get the first gate of the sub-process that will move items from the source to the destination.
    *
    * <p>This Gate will check whether the move is allowable, and if so, will trigger a Transition that
    * performs it.</p>
    *
    * @return {@link #getCheckMoveGate}.
    *
    * @param p the process into which the sub-process wil be embedded.
    * @param sp the SalesPoint, if any, at which the FormSheet is being displayed.
    * @param cSource the source Catalog.
    * @param cDest the destination Catalog.
    * @param db the DataBasket relative to which to perform the operation.
    * @param ci the CatalogItem that is selected in the source.
    * @param ttfs the FormSheet that triggers the process.
    *
    * @override Never Instead, override {@link #checkMove} and/or {@link #moveImpl}.
    */
    protected Gate getCheckMoveToDestGate(SaleProcess p, SalesPoint sp, Catalog cSource, Catalog cDest, DataBasket db, CatalogItem ci, TwoTableFormSheet ttfs) {
        return getCheckMoveGate(p, sp, cSource, cDest, db, ci, ttfs);
    }

    /**
    * Get the sub-process that will move items from the destination to the source.
    *
    * @param p the process into which the sub-process wil be embedded.
    * @param sp the SalesPoint, if any, at which the FormSheet is being displayed.
    * @param cSource the source Catalog.
    * @param cDest the destination Catalog.
    * @param db the DataBasket relative to which to perform the operation.
    * @param ci the CatalogItem that is selected in the destination.
    * @param ttfs the FormSheet that triggers the process.
    *
    * @override Never
    */
    public Transition getMoveToSourceProcess(SaleProcess p, SalesPoint sp, Catalog cSource, Catalog cDest, DataBasket db, CatalogItem ci, TwoTableFormSheet ttfs) {
        return new GateChangeTransition(getCheckMoveToSourceGate(p, sp, cSource, cDest, db, ci, ttfs));
    }

    /**
    * Get the first gate of the sub-process that will move items from the destination to the source.
    *
    * <p>This Gate will check whether the move is allowable, and if so, will trigger a Transition that
    * performs it.</p>
    *
    * @return {@link #getCheckMoveGate}.
    *
    * @param p the process into which the sub-process wil be embedded.
    * @param sp the SalesPoint, if any, at which the FormSheet is being displayed.
    * @param cSource the source Catalog.
    * @param cDest the destination Catalog.
    * @param db the DataBasket relative to which to perform the operation.
    * @param ci the CatalogItem that is selected in the destination.
    * @param ttfs the FormSheet that triggers the process.
    *
    * @override Never Instead, override {@link #checkMove} and/or {@link #moveImpl}.
    */
    protected Gate getCheckMoveToSourceGate(SaleProcess p, SalesPoint sp, Catalog cSource, Catalog cDest, DataBasket db, CatalogItem ci, TwoTableFormSheet ttfs) {
        return getCheckMoveGate(p, sp, cDest, cSource, db, ci, ttfs);
    }

    /**
    * Get the first gate of a sub-process that will move items from one Catalog into another.
    *
    * <p>This Gate will check whether the move is allowable, and if so, will trigger a Transition that
    * performs it.</p>
    *
    * @param p the process into which the sub-process wil be embedded.
    * @param sp the SalesPoint, if any, at which the FormSheet is being displayed.
    * @param cSource the source Catalog.
    * @param cDest the destination Catalog.
    * @param db the DataBasket relative to which to perform the operation.
    * @param ci the CatalogItem that is to be moved from the source into the destination Catalog.
    * @param ttfs the FormSheet that triggers the process.
    *
    * @override Never Instead, override {@link #checkMove} and/or {@link #moveImpl}.
    */
    protected Gate getCheckMoveGate(SaleProcess p, final SalesPoint sp, final Catalog cSource, final Catalog cDest, final DataBasket db, final CatalogItem ci, final TwoTableFormSheet ttfs) {
        return new Gate() {

            public Transition getNextTransition(SaleProcess p, User u) throws InterruptedException {
                int nCheckReturn = checkMove(p, sp, cSource, cDest, db, ci);
                if (nCheckReturn == 0) {
                    return new Transition() {

                        public Gate perform(SaleProcess p, User u) {
                            moveImpl(p, sp, cSource, cDest, db, ci);
                            return ttfs.getGate();
                        }
                    };
                } else {
                    error(p, nCheckReturn);
                    return new GateChangeTransition(ttfs.getGate());
                }
            }
        };
    }

    /**
    * Move the indicated item from the source Catalog into the destination Catalog. You can assume that you are
    * in a {@link Transition}.
    *
    * @param p the process into which the sub-process wil be embedded.
    * @param sp the SalesPoint, if any, at which the FormSheet is being displayed.
    * @param cSource the source Catalog.
    * @param cDest the destination Catalog.
    * @param db the DataBasket relative to which to perform the operation.
    * @param ci the CatalogItem that is to be moved.
    *
    * @override Sometimes
    */
    protected void moveImpl(SaleProcess p, SalesPoint sp, Catalog cSource, Catalog cDest, DataBasket db, CatalogItem ci) {
        try {
            cSource.remove(ci, db);
            cDest.add(ci, db);
        } catch (data.events.VetoException ve) {
            error(p, REMOVE_VETO_EXCEPTION);
        } catch (DuplicateKeyException dke) {
            error(p, DUPLICATE_KEY_EXCEPTION);
        } catch (DataBasketConflictException dbce) {
            error(p, DATABASKET_CONFLICT_ERROR);
        }
    }

    /**
    * Check whether the indicated move is allowable. If so, return 0, otherwise return a non-zero error value
    * that can be passed on to {@link sale.stdforms.FormSheetStrategy#error}. You can assume that you are at a {@link Gate}.
    *
    * @param p the process into which the sub-process wil be embedded.
    * @param sp the SalesPoint, if any, at which the FormSheet is being displayed.
    * @param cSource the source Catalog.
    * @param cDest the destination Catalog.
    * @param db the DataBasket relative to which to perform the operation.
    * @param ci the CatalogItem that is to be moved.
    *
    * @override Sometimes The default implementation returns 0.
    */
    protected int checkMove(SaleProcess p, SalesPoint sp, Catalog cSource, Catalog cDest, DataBasket db, CatalogItem ci) throws InterruptedException {
        return 0;
    }
}
