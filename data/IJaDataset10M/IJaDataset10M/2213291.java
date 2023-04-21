package weka.gui.beans;

import weka.gui.PropertySheetPanel;
import java.awt.BorderLayout;
import java.beans.Customizer;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javax.swing.JPanel;

/**
 * GUI customizer for the train test split maker bean
 *
 * @author <a href="mailto:mhall@cs.waikato.ac.nz">Mark Hall</a>
 * @version $Revision: 1.4 $
 */
public class TrainTestSplitMakerCustomizer extends JPanel implements Customizer {

    /** for serialization */
    private static final long serialVersionUID = -1684662340241807260L;

    private PropertyChangeSupport m_pcSupport = new PropertyChangeSupport(this);

    private PropertySheetPanel m_splitEditor = new PropertySheetPanel();

    public TrainTestSplitMakerCustomizer() {
        setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 5, 5, 5));
        setLayout(new BorderLayout());
        add(m_splitEditor, BorderLayout.CENTER);
        add(new javax.swing.JLabel("TrainTestSplitMakerCustomizer"), BorderLayout.NORTH);
    }

    /**
   * Set the TrainTestSplitMaker to be customized
   *
   * @param object an <code>Object</code> value
   */
    public void setObject(Object object) {
        m_splitEditor.setTarget((TrainTestSplitMaker) object);
    }

    /**
   * Add a property change listener
   *
   * @param pcl a <code>PropertyChangeListener</code> value
   */
    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        m_pcSupport.addPropertyChangeListener(pcl);
    }

    /**
   * Remove a property change listener
   *
   * @param pcl a <code>PropertyChangeListener</code> value
   */
    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        m_pcSupport.removePropertyChangeListener(pcl);
    }
}
