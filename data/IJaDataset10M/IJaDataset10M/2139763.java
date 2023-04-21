package net.sourceforge.mazix.persistence.dao.impl.xml.dom.levels.items.lantern;

import static net.sourceforge.mazix.persistence.constants.DOMXMLConstants.LANTERN_TAG;
import static net.sourceforge.mazix.persistence.constants.DOMXMLConstants.LIGHTNING_TAG;
import static net.sourceforge.mazix.persistence.constants.ObjectConstants.DEFAULT_LANTERN_LIGHTING;
import static net.sourceforge.mazix.persistence.dao.impl.xml.dom.utils.DOMUtils.readAttributeAsInteger;
import net.sourceforge.mazix.components.exception.PersistenceException;
import net.sourceforge.mazix.persistence.dao.impl.xml.dom.levels.items.XMLDOMAbstractItemDAO;
import net.sourceforge.mazix.persistence.dao.levels.items.lantern.LanternItemDAO;
import net.sourceforge.mazix.persistence.dto.levels.LevelDTO;
import net.sourceforge.mazix.persistence.dto.levels.items.AbstractItemDTO;
import net.sourceforge.mazix.persistence.dto.levels.items.lantern.LanternItemDTO;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * The class which manages and parses an XML document which describes game level lantern items,
 * which is defined in the {@code levelSeries.xsd} file.
 * 
 * @author Benjamin Croizet (<a href="mailto:graffity2199@yahoo.fr>graffity2199@yahoo.fr</a>)
 * @since 0.8
 * @version 0.8
 */
public class XMLDOMLanternItemDAO extends XMLDOMAbstractItemDAO implements LanternItemDAO {

    /** Serial version UID. */
    private static final long serialVersionUID = -3523642209774690506L;

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
    public XMLDOMLanternItemDAO(final String seriesFilePath, final String seriesSchemaFileName) {
        super(seriesFilePath, seriesSchemaFileName);
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.8
     */
    @Override
    public final void createOrUpdateLanternItem(final LanternItemDTO lanternItemDTO) throws PersistenceException {
        assert lanternItemDTO != null : "lanternItemDTO is null";
        createOrUpdateItem(lanternItemDTO);
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.8
     */
    @Override
    public final AbstractItemDTO createSpecificItemDTO(final int levelId, final String levelSeriesFileName, final int x, final int y, final Node itemNode) {
        assert levelSeriesFileName != null : "levelSeriesFileName is null";
        assert itemNode != null : "itemNode is null";
        final int lightning = readAttributeAsInteger(itemNode, LIGHTNING_TAG, DEFAULT_LANTERN_LIGHTING);
        return new LanternItemDTO(levelId, levelSeriesFileName, x, y, lightning);
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.8
     */
    @Override
    public final void deleteLanternItem(final LanternItemDTO lanternItemDTO) throws PersistenceException {
        assert lanternItemDTO != null : "lanternItemDTO is null";
        deleteItem(lanternItemDTO, getItemTagName());
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.8
     */
    @Override
    public final LanternItemDTO findLanternItem(final LevelDTO levelDTO, final int x, final int y) throws PersistenceException {
        assert levelDTO != null : "levelDTO is null";
        return findLanternItem(levelDTO.getLevelSeriesFileName(), levelDTO.getLevelId(), x, y);
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.8
     */
    @Override
    public final LanternItemDTO findLanternItem(final String levelSeriesFileName, final int levelId, final int x, final int y) throws PersistenceException {
        assert levelSeriesFileName != null : "levelSeriesFileName is null";
        return (LanternItemDTO) findItem(levelSeriesFileName, levelId, x, y, getItemTagName());
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.8
     */
    @Override
    protected final String getItemTagName() {
        return LANTERN_TAG;
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.8
     */
    @Override
    protected final Element updateSpecificItemElement(final AbstractItemDTO itemDTO, final Element itemElement) {
        assert itemDTO instanceof LanternItemDTO : "itemDTO isn't a LanternItemDTO";
        assert itemElement != null : "itemElement is null";
        final LanternItemDTO lanternItemDTO = (LanternItemDTO) itemDTO;
        itemElement.setAttribute(LIGHTNING_TAG, Integer.toString(lanternItemDTO.getLighting()));
        return itemElement;
    }
}
