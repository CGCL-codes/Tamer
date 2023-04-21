package GUI.GUIReviewers.bidding.search.conflicts;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import Repository.Entities.IConflict;

/**
 * Creates the Conflicts Panel.
 * Shows conflicts between abstracts bidding.
 * @author G01
 * @version 0.5.1
 * @since 0.4
 *
 */
public class ConflictPanel extends JPanel {

    private static final long serialVersionUID = 5769095731527012763L;

    private JLabel paperIdLabel;

    private JLabel expertIdLabel;

    private JLabel motiveLabel;

    private JTextPane motivePane;

    /**
	 * Class constructor.
	 * @author G01
	 */
    public ConflictPanel(IConflict conflict) {
        super();
        this.paperIdLabel = new JLabel("PaperId: " + conflict.getPaperId() + ".");
        this.expertIdLabel = new JLabel("ExpertId: " + conflict.getExpertId() + ".");
        this.motiveLabel = new JLabel("Motive:");
        this.motivePane = new JTextPane();
        this.motivePane.setText(conflict.getMotive());
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(paperIdLabel);
        this.add(expertIdLabel);
        this.add(motiveLabel);
        this.add(motivePane);
        this.setBorder(BorderFactory.createEtchedBorder());
    }
}
