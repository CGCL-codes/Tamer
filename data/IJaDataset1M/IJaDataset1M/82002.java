package cu.edu.cujae.biowh.dataset;

import cu.edu.cujae.biowh.dataset.controller.WidTableJpaController;
import cu.edu.cujae.biowh.parser.utility.controller.exceptions.NonexistentEntityException;
import cu.edu.cujae.biowh.parser.utility.controller.exceptions.PreexistingEntityException;
import cu.edu.cujae.biowh.dataset.entities.WidTable;
import cu.edu.cujae.biowh.logger.VerbLogger;

/**
 * This Class is the WID Factory to handled the WID value
 * @author rvera
 * @version 1.0
 * @since Jun 17, 2011
 */
public class WIDFactory {

    private static WidTable wid = null;

    private WIDFactory() {
    }

    /**
     * Increase the WID value by one
     */
    public static void increaseWid() {
        setWid(getWid() + 1);
    }

    /**
     * Return the WID value
     * @return a Long object with the WID value
     */
    public static Long getWid() {
        return wid.getPreviousWID();
    }

    /**
     * Set the WID value
     * @param wid a Long object with the WID value
     */
    public static void setWid(Long wid) {
        WIDFactory.wid.setPreviousWID(wid);
    }

    /**
     * Retrieve the WID entity from the database
     */
    public static void getWIDFromDataBase() {
        WidTableJpaController instance = new WidTableJpaController(DataSetFactory.getWHEntityManager());
        wid = instance.findWidTable(1L);
        if (wid == null) {
            wid = new WidTable(1L, 1000000L);
            try {
                instance.create(wid);
            } catch (PreexistingEntityException ex) {
                VerbLogger.setLevel(VerbLogger.ERROR);
                VerbLogger.println(WIDFactory.class.getName(), ex.toString());
                VerbLogger.setLevel(VerbLogger.getInitialLevel());
            } catch (Exception ex) {
                VerbLogger.setLevel(VerbLogger.ERROR);
                VerbLogger.println(WIDFactory.class.getName(), ex.toString());
                VerbLogger.setLevel(VerbLogger.getInitialLevel());
            }
        }
        VerbLogger.println(WIDFactory.class.getName(), "Global WID = " + wid.getPreviousWID());
    }

    /**
     * Update the WID value into the database
     */
    public static void updateWIDTable() {
        WidTableJpaController instance = new WidTableJpaController(DataSetFactory.getWHEntityManager());
        try {
            VerbLogger.println(WIDFactory.class.getName(), "Setting Global WID = " + wid.getPreviousWID());
            instance.edit(wid);
        } catch (NonexistentEntityException ex) {
            VerbLogger.setLevel(VerbLogger.ERROR);
            VerbLogger.println(WIDFactory.class.getName(), ex.toString());
            VerbLogger.setLevel(VerbLogger.getInitialLevel());
        } catch (Exception ex) {
            VerbLogger.setLevel(VerbLogger.ERROR);
            VerbLogger.println(WIDFactory.class.getName(), ex.toString());
            VerbLogger.setLevel(VerbLogger.getInitialLevel());
        }
    }
}
