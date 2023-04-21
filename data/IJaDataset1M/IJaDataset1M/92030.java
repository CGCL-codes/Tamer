package potlatch;

import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.openrdf.model.impl.URIImpl;
import org.tokaf.QueryFinder;
import org.tokaf.datasearcher.SesameRepositorySearcher;

public class EditRDFSesamePanel extends SimpleEditRDFSesamePanel {

    /**
	 * 
	 */
    private static final long serialVersionUID = -7985764120936312113L;

    protected JPanel jPanelAddEntity;

    protected JTextField jTextFieldAddEntity;

    protected JButton jButtonAddEntity;

    protected String type = null;

    public EditRDFSesamePanel(boolean readOnly, SesameRepositorySearcher data, QueryFinder queryFinder, String[] names, String type, String namespace, String user, String ratingPredicate, String computedRatingPredicate) {
        super(readOnly, data, queryFinder, names, type, namespace, user, ratingPredicate, computedRatingPredicate);
        initialize();
        this.type = type;
        super.setSubjectClass(type);
    }

    /**
	 * This method initializes this
	 * 
	 */
    private void initialize() {
    }

    /**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
    protected JPanel getJPanelAddEntity() {
        if (jPanelAddEntity == null) {
            jPanelAddEntity = new JPanel();
            jPanelAddEntity.add(getJTextFieldAddEntity(), null);
            jPanelAddEntity.add(getJButtonAddEntity(), null);
            if (isReadOnly()) {
                jPanelAddEntity.setPreferredSize(new java.awt.Dimension(400, 0));
                jPanelAddEntity.setMaximumSize(new java.awt.Dimension(400, 0));
            }
        }
        return jPanelAddEntity;
    }

    /**
	 * This method initializes jTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    private JTextField getJTextFieldAddEntity() {
        if (jTextFieldAddEntity == null) {
            jTextFieldAddEntity = new JTextField();
            jTextFieldAddEntity.setToolTipText("Zadejte n�zev nov�ho zdroje");
            jTextFieldAddEntity.setPreferredSize(new java.awt.Dimension(150, 20));
        }
        return jTextFieldAddEntity;
    }

    /**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getJButtonAddEntity() {
        if (jButtonAddEntity == null) {
            jButtonAddEntity = new JButton();
            jButtonAddEntity.setText("P�idat nov� objekt");
            jButtonAddEntity.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    URIImpl s = new URIImpl(namespace, jTextFieldAddEntity.getText());
                    URIImpl p = new URIImpl("http://www.w3.org/1999/02/22-rdf-syntax-ns#", "type");
                    URIImpl o = new URIImpl(type);
                    ((RDFReifiedTripleTableModel) jTableVylety.getModel()).addTriple(s, p, o, false);
                    jComboBoxSubject.addItem(s.getLocalName());
                    jComboBoxSubject.setSelectedItem(s.getLocalName());
                }
            });
        }
        return jButtonAddEntity;
    }

    protected JPanel getJPanelGetVylety() {
        if (jPanelGetVylety == null) {
            jPanelGetVylety = new JPanel();
            jPanelGetVylety.setPreferredSize(new java.awt.Dimension(400, 70));
            jPanelGetVylety.setLayout(new GridLayout(0, 1));
            jPanelGetVylety.add(getJPanelNewRelation(), null);
            jPanelGetVylety.add(getJPanelAddEntity(), null);
            if (isReadOnly()) jPanelGetVylety.setPreferredSize(new java.awt.Dimension(400, 0));
        }
        return jPanelGetVylety;
    }
}
