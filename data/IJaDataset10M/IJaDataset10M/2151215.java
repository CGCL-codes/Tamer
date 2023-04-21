package net.sourceforge.mazix.persistence.dao.impl.xml.jaxb.levels.squares;

import static java.util.logging.Logger.getLogger;
import static net.sourceforge.mazix.components.constants.CharacterConstants.HYPHEN_CHAR;
import static net.sourceforge.mazix.components.constants.CommonFileConstants.XML_EXTENSION;
import static net.sourceforge.mazix.components.utils.log.LogUtils.buildLogString;
import static net.sourceforge.mazix.persistence.constants.JAXBXMLConstants.SQUARE_CLASSES_LIST;
import static net.sourceforge.mazix.persistence.constants.log.ErrorConstants.LEVEL_NOT_FOUND_ERROR;
import static net.sourceforge.mazix.persistence.constants.log.InfoConstants.ITEM_NOT_FOUND_INFO;
import static net.sourceforge.mazix.persistence.constants.log.InfoConstants.SQUARE_CREATION_INFO;
import static net.sourceforge.mazix.persistence.constants.log.InfoConstants.SQUARE_NOT_FOUND_INFO;
import static net.sourceforge.mazix.persistence.constants.log.InfoConstants.SQUARE_UPDATE_INFO;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import javax.xml.bind.JAXBException;
import net.sourceforge.mazix.components.exception.PersistenceException;
import net.sourceforge.mazix.persistence.dao.impl.xml.jaxb.XMLJAXBDTOFactory;
import net.sourceforge.mazix.persistence.dao.impl.xml.jaxb.XMLJAXBHelperDAO;
import net.sourceforge.mazix.persistence.dao.impl.xml.jaxb.levels.generated.Level;
import net.sourceforge.mazix.persistence.dao.impl.xml.jaxb.levels.generated.Series;
import net.sourceforge.mazix.persistence.dto.levels.LevelDTO;
import net.sourceforge.mazix.persistence.dto.levels.items.AbstractItemDTO;
import net.sourceforge.mazix.persistence.dto.levels.squares.AbstractSquareDTO;
import org.xml.sax.SAXException;

/**
 * @author Benjamin Croizet (<a href="mailto:graffity2199@yahoo.fr>graffity2199@yahoo.fr</a>)
 * @since 0.8
 * @version 0.8
 */
public abstract class XMLJAXBAbstractSquareDAO extends XMLJAXBSquareDAO {

    /** Serial version UID. */
    private static final long serialVersionUID = 4962793683451804626L;

    /** The class logger. */
    private static final transient Logger LOGGER = getLogger(XMLJAXBAbstractSquareDAO.class.getName());

    /**
     * Full constructor.
     * 
     * @param seriesFilePath
     *            the file path directory where to find the file to perform data access operation,
     *            mustn't be <code>null</code>.
     * @param seriesSchemaFileName
     *            the file pathname to perform data access operation, mustn't be <code>null</code>.
     * @since 0.8
     */
    public XMLJAXBAbstractSquareDAO(final String seriesFilePath, final String seriesSchemaFileName) {
        super(seriesFilePath, seriesSchemaFileName);
    }

    /**
     * @param levelSeriesFileName
     * @param levelId
     * @param x
     * @param y
     * @param jaxbSquareObject
     * @return
     * @throws PersistenceException
     * @since 0.8
     */
    public final AbstractItemDTO createItemDTOFromJAXBSquareObject(final String levelSeriesFileName, final int levelId, final int x, final int y, final Object jaxbSquareObject) throws PersistenceException {
        assert levelSeriesFileName != null : "levelSeriesFileName is null";
        assert jaxbSquareObject != null : "jaxbSquareObject is null";
        final Object jaxbItemObject = findItem(jaxbSquareObject);
        AbstractItemDTO abstractItemDTO = null;
        if (jaxbItemObject == null) {
            LOGGER.info(buildLogString(ITEM_NOT_FOUND_INFO, levelSeriesFileName + HYPHEN_CHAR + levelId + HYPHEN_CHAR + x + HYPHEN_CHAR + y));
        } else {
            final XMLJAXBDTOFactory dtoFactory = new XMLJAXBDTOFactory();
            abstractItemDTO = dtoFactory.createItemDTOFromJAXBItemObject(levelSeriesFileName, levelId, x, y, jaxbItemObject);
        }
        return abstractItemDTO;
    }

    /**
     * @param squareClassName
     * @param abstractSquareDTO
     * @return
     * @since 0.8
     */
    private Object createJAXBSquareObject(final AbstractSquareDTO abstractSquareDTO) {
        assert abstractSquareDTO != null : "abstractSquareDTO is null";
        Object jaxbSquareObject = createSpecificSquareObject(abstractSquareDTO.getX(), abstractSquareDTO.getY());
        jaxbSquareObject = updateSpecificSquareObject(abstractSquareDTO, jaxbSquareObject);
        return jaxbSquareObject;
    }

    /**
     * @param jaxbSquareObject
     * @param jaxbItemObject
     * @param itemClassesList
     * @throws PersistenceException
     * @since 0.8
     */
    public abstract void createOrUpdateItemFromJAXBSquareObject(final Object jaxbSquareObject, final Object jaxbItemObject, final List<Class<?>> itemClassesList) throws PersistenceException;

    /**
     * @param abstractSquareDTO
     * @throws PersistenceException
     * @since 0.8
     */
    protected final void createOrUpdateSquare(final AbstractSquareDTO abstractSquareDTO) throws PersistenceException {
        assert abstractSquareDTO != null : "abstractSquareDTO is null";
        try {
            createOrUpdateSquare(abstractSquareDTO, getFilePath(), getFileName(), getJAXBHelper());
        } catch (final JAXBException e) {
            throw new PersistenceException(e);
        } catch (final SAXException e) {
            throw new PersistenceException(e);
        } catch (final PersistenceException e) {
            throw new PersistenceException(e);
        }
    }

    /**
     * @param abstractSquareDTO
     * @param seriesFilePath
     *            the file path directory where to find the file to perform data access operation,
     *            mustn't be <code>null</code>.
     * @param seriesSchemaFileName
     * @param jaxbHelper
     * @throws SAXException
     * @throws JAXBException
     * @throws PersistenceException
     * @since 0.8
     */
    private void createOrUpdateSquare(final AbstractSquareDTO abstractSquareDTO, final String seriesFilePath, final String seriesSchemaFileName, final XMLJAXBHelperDAO jaxbHelper) throws JAXBException, SAXException, PersistenceException {
        assert abstractSquareDTO != null : "abstractSquareDTO is null";
        assert seriesFilePath != null : "seriesFilePath is null";
        assert seriesSchemaFileName != null : "seriesSchemaFileName is null";
        assert jaxbHelper != null : "jaxbHelper is null";
        final String levelSeriesFilePath = seriesFilePath + abstractSquareDTO.getLevelSeriesFileName() + XML_EXTENSION;
        final Series series = (Series) jaxbHelper.unmarshal(levelSeriesFilePath, seriesFilePath + seriesSchemaFileName);
        final Level level = findLevel(series, abstractSquareDTO.getLevelId());
        if (level == null) {
            throw new PersistenceException(buildLogString(LEVEL_NOT_FOUND_ERROR, abstractSquareDTO.getLevelSeriesFileName() + HYPHEN_CHAR + abstractSquareDTO.getLevelId()));
        } else {
            createOrUpdateSquare(level, abstractSquareDTO);
            jaxbHelper.marshal(series, seriesSchemaFileName, levelSeriesFilePath);
        }
    }

    /**
     * @param level
     * @param abstractSquareDTO
     * @param squareClassName
     * @since 0.8
     */
    private void createOrUpdateSquare(final Level level, final AbstractSquareDTO abstractSquareDTO) {
        assert level != null : "level is null";
        assert abstractSquareDTO != null : "abstractSquareDTO is null";
        final Object jaxbSquareObject = findSquare(abstractSquareDTO.getLevelSeriesFileName(), level, abstractSquareDTO.getX(), abstractSquareDTO.getY(), SQUARE_CLASSES_LIST);
        final Object newJaxbSquareObject = createJAXBSquareObject(abstractSquareDTO);
        if (jaxbSquareObject == null) {
            LOGGER.info(buildLogString(SQUARE_NOT_FOUND_INFO, abstractSquareDTO));
            LOGGER.info(buildLogString(SQUARE_CREATION_INFO, abstractSquareDTO));
            level.getEmptyOrWallOrDeparture().add(newJaxbSquareObject);
        } else {
            LOGGER.info(buildLogString(SQUARE_UPDATE_INFO, abstractSquareDTO));
            final int squareIndex = level.getEmptyOrWallOrDeparture().indexOf(jaxbSquareObject);
            level.getEmptyOrWallOrDeparture().set(squareIndex, newJaxbSquareObject);
        }
    }

    /**
     * @param levelId
     * @param levelSeriesFileName
     * @param jaxbSquareObject
     * @return
     * @since 0.8
     */
    public abstract AbstractSquareDTO createSpecificSquareDTO(int levelId, String levelSeriesFileName, Object jaxbSquareObject);

    /**
     * @param x
     * @param y
     * @return
     * @since 0.8
     */
    protected abstract Object createSpecificSquareObject(int x, int y);

    /**
     * @param jaxbSquareObject
     * @param itemClassesList
     * @throws PersistenceException
     * @since 0.8
     */
    public abstract void deleteAllItems(Object jaxbSquareObject, List<Class<?>> itemClassesList) throws PersistenceException;

    /**
     * @param levelDTO
     * @param squareClassName
     * @throws PersistenceException
     * @since 0.8
     */
    public final void deleteAllSquares(final LevelDTO levelDTO, final Class<?> squareClassName) throws PersistenceException {
        assert levelDTO != null : "levelDTO is null";
        assert squareClassName != null : "squareClassName is null";
        final List<Class<?>> squareClassesList = new ArrayList<Class<?>>();
        squareClassesList.add(squareClassName);
        deleteAllSquares(levelDTO, squareClassesList);
    }

    /**
     * @param abstractSquareDTO
     * @param squareClassName
     * @throws PersistenceException
     * @since 0.8
     */
    protected final void deleteSquare(final AbstractSquareDTO abstractSquareDTO, final Class<?> squareClassName) throws PersistenceException {
        assert abstractSquareDTO != null : "abstractSquareDTO is null";
        final List<Class<?>> squareClassesList = new ArrayList<Class<?>>();
        squareClassesList.add(squareClassName);
        deleteSquare(abstractSquareDTO, squareClassesList);
    }

    /**
     * @param levelSeriesFileName
     * @param levelId
     * @param squareClassName
     * @return
     * @throws PersistenceException
     * @since 0.8
     */
    protected final Set<AbstractSquareDTO> findAllSquares(final String levelSeriesFileName, final int levelId, final Class<?> squareClassName) throws PersistenceException {
        assert levelSeriesFileName != null : "levelSeriesFileName is null";
        assert squareClassName != null : "squareClassName is null";
        final List<Class<?>> squareClassesList = new ArrayList<Class<?>>();
        squareClassesList.add(squareClassName);
        return findAllSquares(levelSeriesFileName, levelId, squareClassesList);
    }

    /**
     * @param jaxbSquareObject
     * @return
     * @throws PersistenceException
     * @since 0.8
     */
    public abstract Object findItem(Object jaxbSquareObject) throws PersistenceException;

    /**
     * @param levelSeriesFileName
     * @param levelId
     * @param x
     * @param y
     * @param squareClassName
     * @return
     * @throws PersistenceException
     * @since 0.8
     */
    protected final AbstractSquareDTO findSquare(final String levelSeriesFileName, final int levelId, final int x, final int y, final Class<?> squareClassName) throws PersistenceException {
        assert levelSeriesFileName != null : "levelSeriesFileName is null";
        final List<Class<?>> squareClassesList = new ArrayList<Class<?>>();
        squareClassesList.add(squareClassName);
        return findSquare(levelSeriesFileName, levelId, x, y, squareClassesList);
    }

    /**
     * @return
     * @since 0.8
     */
    protected abstract Class<?> getSquareClassName();

    /**
     * @param abstractSquareDTO
     * @param jaxbSquareObject
     * @return
     * @since 0.8
     */
    protected abstract Object updateSpecificSquareObject(AbstractSquareDTO abstractSquareDTO, Object jaxbSquareObject);
}
