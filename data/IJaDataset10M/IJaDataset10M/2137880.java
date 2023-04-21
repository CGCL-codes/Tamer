package net.sourceforge.mazix.persistence.dao.impl.xml.dom.levels.squares.switchdoor;

import static net.sourceforge.mazix.components.color.HTMLColor.DEFAULT_COLOR;
import static net.sourceforge.mazix.persistence.constants.DOMXMLConstants.COLOR_TAG;
import static net.sourceforge.mazix.persistence.constants.DOMXMLConstants.SWITCH_DOOR_TAG;
import static net.sourceforge.mazix.persistence.dao.impl.xml.dom.utils.DOMUtils.readAttributeAsColor;
import java.util.HashSet;
import java.util.Set;
import net.sourceforge.mazix.components.color.HTMLColor;
import net.sourceforge.mazix.components.exception.PersistenceException;
import net.sourceforge.mazix.persistence.dao.impl.xml.dom.levels.squares.XMLDOMAbstractSquareDAO;
import net.sourceforge.mazix.persistence.dao.levels.squares.switchdoor.SwitchDoorSquareDAO;
import net.sourceforge.mazix.persistence.dto.levels.LevelDTO;
import net.sourceforge.mazix.persistence.dto.levels.squares.AbstractSquareDTO;
import net.sourceforge.mazix.persistence.dto.levels.squares.switchdoor.SwitchDoorSquareDTO;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * The class which manages and parses an XML document which describes game level switch door
 * squares, which is defined in the {@code levelSeries.xsd} file.
 * 
 * @author Benjamin Croizet (<a href="mailto:graffity2199@yahoo.fr>graffity2199@yahoo.fr</a>)
 * @since 0.8
 * @version 0.8
 */
public class XMLDOMSwitchDoorSquareDAO extends XMLDOMAbstractSquareDAO implements SwitchDoorSquareDAO {

    /** Serial version UID. */
    private static final long serialVersionUID = -2968284077749920878L;

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
    public XMLDOMSwitchDoorSquareDAO(final String seriesFilePath, final String seriesSchemaFileName) {
        super(seriesFilePath, seriesSchemaFileName);
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.8
     */
    @Override
    public final void createOrUpdateSwitchDoorSquare(final SwitchDoorSquareDTO switchDoorSquareDTO) throws PersistenceException {
        assert switchDoorSquareDTO != null : "switchDoorSquareDTO is null";
        createOrUpdateSquare(switchDoorSquareDTO);
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
        final HTMLColor color = readAttributeAsColor(squareNode, COLOR_TAG, DEFAULT_COLOR);
        return new SwitchDoorSquareDTO(levelId, levelSeriesFileName, x, y, color);
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.8
     */
    @Override
    public final void deleteAllSwitchDoorSquares(final LevelDTO levelDTO) throws PersistenceException {
        assert levelDTO != null : "levelDTO is null";
        deleteAllSquares(levelDTO, getSquareTagName());
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.8
     */
    @Override
    public final void deleteSwitchDoorSquare(final SwitchDoorSquareDTO switchDoorSquareDTO) throws PersistenceException {
        assert switchDoorSquareDTO != null : "switchDoorSquareDTO is null";
        deleteSquare(switchDoorSquareDTO, getSquareTagName());
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.8
     */
    @Override
    public final Set<SwitchDoorSquareDTO> findAllSwitchDoorSquares(final LevelDTO levelDTO) throws PersistenceException {
        assert levelDTO != null : "levelDTO is null";
        return findAllSwitchDoorSquares(levelDTO.getLevelSeriesFileName(), levelDTO.getLevelId());
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.8
     */
    @Override
    public final Set<SwitchDoorSquareDTO> findAllSwitchDoorSquares(final String levelSeriesFileName, final int levelId) throws PersistenceException {
        assert levelSeriesFileName != null : "levelSeriesFileName is null";
        final Set<SwitchDoorSquareDTO> switchDoorSquareDTOSet = new HashSet<SwitchDoorSquareDTO>();
        final Set<AbstractSquareDTO> squareDTOSet = findAllSquares(levelSeriesFileName, levelId, getSquareTagName());
        for (final AbstractSquareDTO squareDTO : squareDTOSet) {
            switchDoorSquareDTOSet.add((SwitchDoorSquareDTO) squareDTO);
        }
        return switchDoorSquareDTOSet;
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.8
     */
    @Override
    public final SwitchDoorSquareDTO findSwitchDoorSquare(final LevelDTO levelDTO, final int x, final int y) throws PersistenceException {
        assert levelDTO != null : "levelDTO is null";
        return findSwitchDoorSquare(levelDTO.getLevelSeriesFileName(), levelDTO.getLevelId(), x, y);
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.8
     */
    @Override
    public final SwitchDoorSquareDTO findSwitchDoorSquare(final String levelSeriesFileName, final int levelId, final int x, final int y) throws PersistenceException {
        assert levelSeriesFileName != null : "levelSeriesFileName is null";
        return (SwitchDoorSquareDTO) findSquare(levelSeriesFileName, levelId, x, y, getSquareTagName());
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.8
     */
    @Override
    protected final String getSquareTagName() {
        return SWITCH_DOOR_TAG;
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.8
     */
    @Override
    protected final Element updateSpecificSquareElement(final AbstractSquareDTO squareDTO, final Element squareElement) {
        assert squareDTO instanceof SwitchDoorSquareDTO : "squareDTO isn't a SwitchDoorSquareDTO";
        assert squareElement != null : "squareElement is null";
        final SwitchDoorSquareDTO switchDoorSquareDTO = (SwitchDoorSquareDTO) squareDTO;
        squareElement.setAttribute(COLOR_TAG, switchDoorSquareDTO.getColor().toString());
        return squareElement;
    }
}
