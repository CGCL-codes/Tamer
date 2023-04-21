package Maquettes_Flo_Gilles;

import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

public class PanelSelectionAnneesEtudes extends JPanel {

    private JTree treeFormEtAnneesEtude;

    /**
	 * tree datamodel
	 */
    private DefaultMutableTreeNode racine;

    public PanelSelectionAnneesEtudes() {
        super();
        initGUI();
    }

    private void initGUI() {
        {
            this.setLayout(null);
            {
                racine = new DefaultMutableTreeNode("Polytech");
                DefaultMutableTreeNode formationIG = new DefaultMutableTreeNode("IG");
                {
                    DefaultMutableTreeNode yearIG3 = new DefaultMutableTreeNode("IG3");
                    DefaultMutableTreeNode yearIG4 = new DefaultMutableTreeNode("IG4");
                    DefaultMutableTreeNode yearIG5 = new DefaultMutableTreeNode("IG5");
                    formationIG.add(yearIG3);
                    formationIG.add(yearIG4);
                    formationIG.add(yearIG5);
                }
                DefaultMutableTreeNode formationERII = new DefaultMutableTreeNode("ERII");
                {
                    DefaultMutableTreeNode yearERII3 = new DefaultMutableTreeNode("ERII3");
                    DefaultMutableTreeNode yearERII4 = new DefaultMutableTreeNode("ERII4");
                    DefaultMutableTreeNode yearERII5 = new DefaultMutableTreeNode("ERII5");
                    formationERII.add(yearERII3);
                    formationERII.add(yearERII4);
                    formationERII.add(yearERII5);
                }
                racine.add(formationIG);
                racine.add(formationERII);
                treeFormEtAnneesEtude = new JTree(racine);
                this.add(treeFormEtAnneesEtude);
                treeFormEtAnneesEtude.setBounds(130, 96, 242, 162);
            }
        }
    }
}
