package cu.edu.cujae.biowh.gui.ontology;

import cu.edu.cujae.biowh.gui.component.table.AbstractListViewListTableDialog;
import cu.edu.cujae.biowh.parser.ontology.entities.OntologySubset;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * This class create the Ontology SubSet List View
 *
 * @author rvera
 * @version 0.1
 * @since May 24, 2011, 3:53:09 PM
 */
public class OntologySubsetView extends AbstractListViewListTableDialog<OntologySubset> {

    /**
     * Create the Ontology SubSet List object
     * @param parent the Frame from which the dialog is displayed
     * @param modal specifies whether dialog blocks user input to other top-level windows when shown. If true, the modality type property is set to DEFAULT_MODALITY_TYPE, otherwise the dialog is modeless
     * @param collection The collection with the entity <T> to be visualized
     */
    public OntologySubsetView(Frame parent, boolean modal, Collection<OntologySubset> collection) {
        super(parent, modal, collection);
    }

    @Override
    public List<Object> getCollectionColumns(OntologySubset data) {
        ArrayList<Object> column = new ArrayList<>();
        column.add(false);
        column.add(data.getId());
        column.add(data.getName());

        return column;
    }

    @Override
    public OntologySubset getCollectionElementFromTableRow(int row) {
        return null;
    }

    @Override
    public void showEntityClass() {
    }

    @Override
    public String[] createJTableHeader() {
        return new String[]{"", "Id", "Name"};
    }
}
