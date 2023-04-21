package net.sourceforge.ondex.ovtk2.ui.editor.conceptgds;

import java.awt.GridLayout;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.swing.JLayeredPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import net.sourceforge.ondex.core.Attribute;
import net.sourceforge.ondex.core.AttributeName;
import net.sourceforge.ondex.core.ONDEXConcept;
import net.sourceforge.ondex.core.ONDEXGraph;
import net.sourceforge.ondex.ovtk2.ui.editor.util.AttributeNameComparator;
import net.sourceforge.ondex.ovtk2.ui.editor.util.Util;

/**
 * Panel to display all concept Attribute of all concepts in the graph.
 *
 * @author taubertj
 */
public class ConceptGDSPanel extends JLayeredPane {

    /**
     * generated
     */
    private static final long serialVersionUID = 536997163313647823L;

    /**
     * JTable displaying lists of concept Attribute
     */
    JTable table;

    /**
     * Initialises table data with the given ONDEXGraph.
     *
     * @param graph ONDEXGraph to extract concept Attribute from
     */
    public ConceptGDSPanel(ONDEXGraph graph) {
        Set<AttributeName> ans = new HashSet<AttributeName>();
        for (ONDEXConcept concept : graph.getConcepts()) {
            for (Attribute attribute : concept.getAttributes()) {
                ans.add(attribute.getOfType());
            }
        }
        AttributeName[] anArray = ans.toArray(new AttributeName[0]);
        Arrays.sort(anArray, new AttributeNameComparator());
        Set<ONDEXConcept> view = graph.getConcepts();
        Object[][] data = new Object[view.size()][];
        Iterator<ONDEXConcept> viewI = view.iterator();
        for (int j = 0; viewI.hasNext(); j++) {
            ONDEXConcept concept = viewI.next();
            Object[] row = new Object[anArray.length + 1];
            row[0] = concept;
            for (int i = 0; i < anArray.length; i++) {
                row[i + 1] = concept.getAttribute(anArray[i]);
            }
            data[j] = row;
        }
        String[] header = new String[anArray.length + 1];
        header[0] = "id";
        for (int i = 0; i < anArray.length; i++) {
            header[i + 1] = anArray[i].getId();
        }
        DefaultTableModel model = new DefaultTableModel();
        model.setDataVector(data, header);
        table = new ConceptGDSTable(model, graph);
        this.setLayout(new GridLayout(1, 1));
        this.add(new JScrollPane(table));
        Util.calcColumnWidths(table, 150);
    }

    /**
     * Returns the internal JTable.
     *
     * @return JTable
     */
    public JTable getTable() {
        return table;
    }
}
