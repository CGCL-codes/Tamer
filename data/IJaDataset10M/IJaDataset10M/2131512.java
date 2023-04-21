package net.sourceforge.mazix.persistence.dao.levels.items.lantern;

import net.sourceforge.mazix.components.exception.PersistenceException;
import org.junit.Test;

/**
 * JUnit interface test classes for all {@link LanternItemDAO} test implementations.
 * 
 * @author Benjamin Croizet (<a href="mailto:graffity2199@yahoo.fr>graffity2199@yahoo.fr</a>)
 * 
 * @since 0.8
 * @version 0.8
 */
public interface LanternItemDAOTest {

    /**
     * Test method for
     * {@link net.sourceforge.mazix.persistence.dao.levels.items.lantern.LanternItemDAO#createOrUpdateLanternItem(net.sourceforge.mazix.persistence.dto.levels.items.lantern.LanternItemDTO)}
     * .
     * 
     * @throws PersistenceException
     *             if any exception occurs when testing the method.
     */
    void testCreateOrUpdateLanternItemWithExistingSeriesWithExistingLevelWithExistingSquareWithExistingKeyItem() throws PersistenceException;

    /**
     * Test method for
     * {@link net.sourceforge.mazix.persistence.dao.levels.items.lantern.LanternItemDAO#createOrUpdateLanternItem(net.sourceforge.mazix.persistence.dto.levels.items.lantern.LanternItemDTO)}
     * .
     * 
     * @throws PersistenceException
     *             if any exception occurs when testing the method.
     */
    void testCreateOrUpdateLanternItemWithExistingSeriesWithExistingLevelWithExistingSquareWithExistingLanternItem() throws PersistenceException;

    /**
     * Test method for
     * {@link net.sourceforge.mazix.persistence.dao.levels.items.lantern.LanternItemDAO#createOrUpdateLanternItem(net.sourceforge.mazix.persistence.dto.levels.items.lantern.LanternItemDTO)}
     * .
     * 
     * @throws PersistenceException
     *             if any exception occurs when testing the method.
     */
    void testCreateOrUpdateLanternItemWithExistingSeriesWithExistingLevelWithExistingSquareWithNotExistingItem() throws PersistenceException;

    /**
     * Test method for
     * {@link net.sourceforge.mazix.persistence.dao.levels.items.lantern.LanternItemDAO#createOrUpdateLanternItem(net.sourceforge.mazix.persistence.dto.levels.items.lantern.LanternItemDTO)}
     * .
     * 
     * @throws PersistenceException
     *             if any exception occurs when testing the method.
     */
    void testCreateOrUpdateLanternItemWithExistingSeriesWithExistingLevelWithNotExistingSquare() throws PersistenceException;

    /**
     * Test method for
     * {@link net.sourceforge.mazix.persistence.dao.levels.items.lantern.LanternItemDAO#createOrUpdateLanternItem(net.sourceforge.mazix.persistence.dto.levels.items.lantern.LanternItemDTO)}
     * .
     * 
     * @throws PersistenceException
     *             if any exception occurs when testing the method.
     */
    void testCreateOrUpdateLanternItemWithExistingSeriesWithNotExistingLevel() throws PersistenceException;

    /**
     * Test method for
     * {@link net.sourceforge.mazix.persistence.dao.levels.items.lantern.LanternItemDAO#createOrUpdateLanternItem(net.sourceforge.mazix.persistence.dto.levels.items.lantern.LanternItemDTO)}
     * .
     * 
     * @throws PersistenceException
     *             if any exception occurs when testing the method.
     */
    void testCreateOrUpdateLanternItemWithNotExistingSeries() throws PersistenceException;

    /**
     * Test method for
     * {@link net.sourceforge.mazix.persistence.dao.levels.items.lantern.LanternItemDAO#deleteLanternItem(net.sourceforge.mazix.persistence.dto.levels.items.lantern.LanternItemDTO)}
     * .
     * 
     * @throws PersistenceException
     *             if any exception occurs when testing the method.
     */
    @Test(expected = PersistenceException.class)
    void testDeleteLanternItemWithExistingSeriesWithExistingLevelWithExistingSquareWithExistingKeyItem() throws PersistenceException;

    /**
     * Test method for
     * {@link net.sourceforge.mazix.persistence.dao.levels.items.lantern.LanternItemDAO#deleteLanternItem(net.sourceforge.mazix.persistence.dto.levels.items.lantern.LanternItemDTO)}
     * .
     * 
     * @throws PersistenceException
     *             if any exception occurs when testing the method.
     */
    @Test
    void testDeleteLanternItemWithExistingSeriesWithExistingLevelWithExistingSquareWithExistingLanternItem() throws PersistenceException;

    /**
     * Test method for
     * {@link net.sourceforge.mazix.persistence.dao.levels.items.lantern.LanternItemDAO#deleteLanternItem(net.sourceforge.mazix.persistence.dto.levels.items.lantern.LanternItemDTO)}
     * .
     * 
     * @throws PersistenceException
     *             if any exception occurs when testing the method.
     */
    void testDeleteLanternItemWithExistingSeriesWithExistingLevelWithExistingSquareWithNotExistingItem() throws PersistenceException;

    /**
     * Test method for
     * {@link net.sourceforge.mazix.persistence.dao.levels.items.lantern.LanternItemDAO#deleteLanternItem(net.sourceforge.mazix.persistence.dto.levels.items.lantern.LanternItemDTO)}
     * .
     * 
     * @throws PersistenceException
     *             if any exception occurs when testing the method.
     */
    void testDeleteLanternItemWithExistingSeriesWithExistingLevelWithNotExistingSquare() throws PersistenceException;

    /**
     * Test method for
     * {@link net.sourceforge.mazix.persistence.dao.levels.items.lantern.LanternItemDAO#deleteLanternItem(net.sourceforge.mazix.persistence.dto.levels.items.lantern.LanternItemDTO)}
     * .
     * 
     * @throws PersistenceException
     *             if any exception occurs when testing the method.
     */
    void testDeleteLanternItemWithExistingSeriesWithNotExistingLevel() throws PersistenceException;

    /**
     * Test method for
     * {@link net.sourceforge.mazix.persistence.dao.levels.items.lantern.LanternItemDAO#deleteLanternItem(net.sourceforge.mazix.persistence.dto.levels.items.lantern.LanternItemDTO)}
     * .
     * 
     * @throws PersistenceException
     *             if any exception occurs when testing the method.
     */
    void testDeleteLanternItemWithNotExistingSeries() throws PersistenceException;

    /**
     * Test method for
     * {@link net.sourceforge.mazix.persistence.dao.levels.items.lantern.LanternItemDAO#findLanternItem(java.lang.String, int, int, int)}
     * .
     * 
     * @throws PersistenceException
     *             if any exception occurs when testing the method.
     */
    void testFindLanternItemWithExistingSeriesWithExistingLevelWithExistingSquareWithExistingItem() throws PersistenceException;

    /**
     * Test method for
     * {@link net.sourceforge.mazix.persistence.dao.levels.items.lantern.LanternItemDAO#findLanternItem(java.lang.String, int, int, int)}
     * .
     * 
     * @throws PersistenceException
     *             if any exception occurs when testing the method.
     */
    void testFindLanternItemWithExistingSeriesWithExistingLevelWithExistingSquareWithNotExistingItem() throws PersistenceException;

    /**
     * Test method for
     * {@link net.sourceforge.mazix.persistence.dao.levels.items.lantern.LanternItemDAO#findLanternItem(java.lang.String, int, int, int)}
     * .
     * 
     * @throws PersistenceException
     *             if any exception occurs when testing the method.
     */
    void testFindLanternItemWithExistingSeriesWithExistingLevelWithNotExistingSquare() throws PersistenceException;

    /**
     * Test method for
     * {@link net.sourceforge.mazix.persistence.dao.levels.items.lantern.LanternItemDAO#findLanternItem(java.lang.String, int, int, int)}
     * .
     * 
     * @throws PersistenceException
     *             if any exception occurs when testing the method.
     */
    void testFindLanternItemWithExistingSeriesWithNotExistingLevel() throws PersistenceException;

    /**
     * Test method for
     * {@link net.sourceforge.mazix.persistence.dao.levels.items.lantern.LanternItemDAO#findLanternItem(net.sourceforge.mazix.persistence.dto.levels.LevelDTO, int, int)}
     * .
     * 
     * @throws PersistenceException
     *             if any exception occurs when testing the method.
     */
    void testFindLanternItemWithNotExistingSeries() throws PersistenceException;
}
