package net.sourceforge.mazix.persistence.dao.impl.xml.dom.levels.squares.empty;

import static net.sourceforge.mazix.persistence.constants.DOMXMLConstants.EMPTY_TAG;
import java.util.HashSet;
import java.util.Set;
import net.sourceforge.mazix.components.exception.PersistenceException;
import net.sourceforge.mazix.persistence.dao.impl.xml.dom.levels.squares.XMLDOMAbstractSquareDAO;
import net.sourceforge.mazix.persistence.dao.levels.squares.empty.EmptySquareDAO;
import net.sourceforge.mazix.persistence.dto.levels.LevelDTO;
import net.sourceforge.mazix.persistence.dto.levels.squares.AbstractSquareDTO;
import net.sourceforge.mazix.persistence.dto.levels.squares.empty.EmptySquareDTO;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * The class which manages and parses an XML document which describes game level empty squares,
 * which is defined in the {@code levelSeries.xsd} file.
 * 
 * @author Benjamin Croizet (<a href="mailto:graffity2199@yahoo.fr>graffity2199@yahoo.fr</a>)
 * @since 0.8
 * @version 0.8
 */
public class XMLDOMEmptySquareDAO extends XMLDOMAbstractSquareDAO implements EmptySquareDAO {

    /** Serial version UID. */
    private static final long serialVersionUID = 4193547906074973997L;

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
    public XMLDOMEmptySquareDAO(final String seriesFilePath, final String seriesSchemaFileName) {
        super(seriesFilePath, seriesSchemaFileName);
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.8
     */
    @Override
    public final void createOrUpdateEmptySquare(final EmptySquareDTO emptySquareDTO) throws PersistenceException {
        assert emptySquareDTO != null : "emptySquareDTO is null";
        createOrUpdateSquare(emptySquareDTO);
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.8
     */
    @Override
    public final AbstractSquareDTO createSpecificSquareDTO(final int levelId, final String levelSeriesFileName, final int x, final int y, final Node squareNode) {
        assert levelSeriesFileName != null : "levelSeriesFileName is null";
        assert squareNode != null : "squareNode is null";
        return new EmptySquareDTO(levelId, levelSeriesFileName, x, y);
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.8
     */
    @Override
    public final void deleteAllEmptySquares(final LevelDTO levelDTO) throws PersistenceException {
        assert levelDTO != null : "levelDTO is null";
        deleteAllSquares(levelDTO, getSquareTagName());
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.8
     */
    @Override
    public final void deleteEmptySquare(final EmptySquareDTO emptySquareDTO) throws PersistenceException {
        assert emptySquareDTO != null : "emptySquareDTO is null";
        deleteSquare(emptySquareDTO, getSquareTagName());
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.8
     */
    @Override
    public final Set<EmptySquareDTO> findAllEmptySquares(final LevelDTO levelDTO) throws PersistenceException {
        assert levelDTO != null : "levelDTO is null";
        return findAllEmptySquares(levelDTO.getLevelSeriesFileName(), levelDTO.getLevelId());
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.8
     */
    @Override
    public final Set<EmptySquareDTO> findAllEmptySquares(final String levelSeriesFileName, final int levelId) throws PersistenceException {
        assert levelSeriesFileName != null : "levelSeriesFileName is null";
        final Set<EmptySquareDTO> emptySquareDTOSet = new HashSet<EmptySquareDTO>();
        final Set<AbstractSquareDTO> squareDTOSet = findAllSquares(levelSeriesFileName, levelId, getSquareTagName());
        for (final AbstractSquareDTO squareDTO : squareDTOSet) {
            emptySquareDTOSet.add((EmptySquareDTO) squareDTO);
        }
        return emptySquareDTOSet;
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.8
     */
    @Override
    public final EmptySquareDTO findEmptySquare(final LevelDTO levelDTO, final int x, final int y) throws PersistenceException {
        assert levelDTO != null : "levelDTO is null";
        return findEmptySquare(levelDTO.getLevelSeriesFileName(), levelDTO.getLevelId(), x, y);
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.8
     */
    @Override
    public final EmptySquareDTO findEmptySquare(final String levelSeriesFileName, final int levelId, final int x, final int y) throws PersistenceException {
        assert levelSeriesFileName != null : "levelSeriesFileName is null";
        return (EmptySquareDTO) findSquare(levelSeriesFileName, levelId, x, y, getSquareTagName());
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.8
     */
    @Override
    protected final String getSquareTagName() {
        return EMPTY_TAG;
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.8
     */
    @Override
    protected final Element updateSpecificSquareElement(final AbstractSquareDTO squareDTO, final Element squareElement) {
        assert squareDTO instanceof EmptySquareDTO : "squareDTO isn't a EmptySquareDTO";
        assert squareElement != null : "squareElement is null";
        return squareElement;
    }
}
