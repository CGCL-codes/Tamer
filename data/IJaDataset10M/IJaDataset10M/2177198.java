package fr.lip6.sma.simulacion.simcafe;

import java.awt.FlowLayout;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import fr.lip6.sma.simulacion.app.Bundle;
import fr.lip6.sma.simulacion.app.ImagePanel;

/**
 * Classe pour afficher le prix du sac de cereza.
 *
 * @author Paul Guyot <paulguyot@acm.org>
 * @version $Revision: 1.11 $
 *
 * @see "aucun test d�fini."
 */
public class CerezaPricePanel extends JPanel implements PropertyChangeListener {

    /**
	 * R�f�rence sur le mod�le.
	 */
    private final CerezaPrice mModel;

    /**
	 * R�f�rence sur l'�tiquette contenant le prix.
	 */
    private final JLabel mCerezaPriceLabel;

    /**
	 * Constructeur � partir d'un mod�le.
	 *
	 * @param inModel	mod�le pour construire cette vue.
	 */
    public CerezaPricePanel(CerezaPrice inModel) {
        mModel = inModel;
        inModel.addPropertyChangeListener(this);
        setSize(84, 20);
        setLayout(new FlowLayout(FlowLayout.CENTER, 2, 2));
        final JLabel oneCrossLabel = new JLabel();
        oneCrossLabel.setFont(new Font("Serif", Font.PLAIN, 12));
        oneCrossLabel.setText("1x");
        oneCrossLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        oneCrossLabel.setHorizontalTextPosition(SwingConstants.RIGHT);
        add(oneCrossLabel);
        final ImagePanel theImagePanel = new ImagePanel(Bundle.getImage("artwork/widgets/simcafe/icon-bag1.png"));
        add(theImagePanel);
        final JLabel colonLabel = new JLabel();
        colonLabel.setFont(new Font("Serif", Font.PLAIN, 12));
        colonLabel.setText(":");
        colonLabel.setHorizontalAlignment(SwingConstants.LEFT);
        colonLabel.setHorizontalTextPosition(SwingConstants.LEFT);
        add(colonLabel);
        mCerezaPriceLabel = new JLabel();
        mCerezaPriceLabel.setFont(new Font("Serif", Font.PLAIN, 12));
        mCerezaPriceLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        mCerezaPriceLabel.setHorizontalTextPosition(SwingConstants.RIGHT);
        add(mCerezaPriceLabel);
        setToolTipText("CerezaPrice_Key");
        updateValue();
    }

    /**
	 * Met � jour la vue suite � un changement dans la valeur.
	 */
    private void updateValue() {
        mCerezaPriceLabel.setText("$" + mModel.getPrice());
    }

    /**
	 * M�thode appel�e lorsque la valeur change.
	 *
	 * @param inEvent	�v�nement du changement.
	 */
    public final void propertyChange(PropertyChangeEvent inEvent) {
        final Runnable updateValueTask = new Runnable() {

            public void run() {
                updateValue();
            }
        };
        if (SwingUtilities.isEventDispatchThread()) {
            updateValueTask.run();
        } else {
            SwingUtilities.invokeLater(updateValueTask);
        }
    }
}
