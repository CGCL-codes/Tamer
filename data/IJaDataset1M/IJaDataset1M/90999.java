package grammarbrowser.browser.dialog;

import grammarbrowser.parser.MutableGrammaticalRelation;
import grammarbrowser.parser.RelationModel;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.table.DefaultTableModel;

/**
 * Relation table model
 * 
 * @author Bernard Bou
 * 
 */
public class RelationsModel extends DefaultTableModel {

    private static final long serialVersionUID = 1L;

    /**
     * Column names
     */
    private static final String[] theColumnNames = { "name", "short", "hierarchy", "long", "description" };

    /**
     * Relation model
     */
    private RelationModel theRelationModel;

    /**
     * Constructor
     * 
     * @param thisRelationModel
     *                relation model
     */
    public RelationsModel(RelationModel thisRelationModel) {
        this(thisRelationModel, null, true);
    }

    /**
     * Constructor
     * 
     * @param thisRelationModel
     *                relation model
     * @param thisFilter
     *                filter
     * @param startsWith
     *                'starts with' flag
     */
    public RelationsModel(RelationModel thisRelationModel, String thisFilter, boolean startsWith) {
        theRelationModel = thisRelationModel;
        super.setDataVector(getData(thisRelationModel.theRelations, thisFilter, startsWith), convertToVector(theColumnNames));
    }

    /**
     * Get data vector
     * 
     * @param theseRelations
     *                list of grammatical relations
     * @param thisFilter
     *                filter
     * @param startsWith
     *                'start with' flag
     * @return data vector
     */
    @SuppressWarnings("unchecked")
    private final Vector<Vector<Object>> getData(List<MutableGrammaticalRelation> theseRelations, String thisFilter, boolean startsWith) {
        boolean useFilter = thisFilter != null && !thisFilter.isEmpty();
        Vector<Vector<Object>> thisVector = new Vector<Vector<Object>>();
        Pattern thisPattern = null;
        if (useFilter && !startsWith) thisPattern = Pattern.compile(thisFilter);
        for (MutableGrammaticalRelation thisRelation : theseRelations) {
            String thisShortName = thisRelation.getShortName();
            if (useFilter) {
                if (!startsWith) {
                    Matcher thisMatcher = thisPattern.matcher(thisShortName);
                    if (!thisMatcher.find()) continue;
                } else if (!thisShortName.startsWith(thisFilter)) continue;
            }
            Object[] thisRow = { thisRelation, thisRelation.getShortName(), thisRelation.getLineage(false), thisRelation.getLongName(), thisRelation.getDescription() };
            thisVector.add(convertToVector(thisRow));
        }
        return thisVector;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    /**
     * Access to relation model
     * 
     * @return relation model
     */
    public RelationModel getRelationModel() {
        return theRelationModel;
    }
}
