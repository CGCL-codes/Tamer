package org.pcomeziantou.volleylife.swing;

import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.pcomeziantou.volleylife.model.UserPreferences;
import org.pcomeziantou.volleylife.model.universe.Universe;
import org.pcomeziantou.volleylife.model.universe.biosphere.Equipe;
import org.pcomeziantou.volleylife.model.universe.biosphere.Joueur;
import org.pcomeziantou.volleylife.viewcontroller.EquipeController;
import org.pcomeziantou.volleylife.viewcontroller.View;

public class EquipeTable extends JTable implements View {

    private static final long serialVersionUID = 1L;

    public EquipeTable(Equipe equipe, UserPreferences preferences, EquipeController equipeController) {
        String[] columnNames = getColumnNames();
        setModel(new EquipeTableModel(equipe, columnNames));
    }

    /**
	 * Returns localized column names.
	 */
    private String[] getColumnNames() {
        String[] columnNames = { "name", "test" };
        return columnNames;
    }

    /**
	 * Sets column renderers.
	 */
    @SuppressWarnings("unused")
    private void setColumnRenderers(UserPreferences preferences) {
    }

    private static class EquipeTableModel extends DefaultTableModel {

        private static final long serialVersionUID = 1L;

        @SuppressWarnings("unused")
        private List<Joueur> joueurs;

        public EquipeTableModel(Equipe equipe, String[] columnNames) {
            super(columnNames, 0);
        }

        public void update() {
        }

        public void setSelectedObject(Object etre) {
        }

        ;

        public void setUniverse(Universe universe) {
        }

        ;
    }
}
