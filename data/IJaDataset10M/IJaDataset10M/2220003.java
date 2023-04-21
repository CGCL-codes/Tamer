package votebox;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * The view that is shown for a booth that is not currently voting - mainly shows
 * that booth's label
 * @author cshaw
 */
@SuppressWarnings("serial")
public class VoteBoxInactiveUI extends JFrame implements IVoteBoxInactiveUI {

    public static final String FONTNAME = "Sans";

    private VoteBox votebox;

    private JLabel numLbl;

    private JLabel statusLbl;

    /**
     * Constructs a new VoteBoxInactiveUI
     * @param vb the votebox booth
     */
    public VoteBoxInactiveUI(VoteBox vb) {
        super("VoteBox");
        setSize(1024, 768);
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        votebox = vb;
        JLabel titleLbl = new JLabel("This VoteBox is machine number:");
        titleLbl.setFont(new Font(FONTNAME, Font.PLAIN, 24));
        c.gridx = 0;
        c.gridy = 0;
        add(titleLbl, c);
        numLbl = new JLabel();
        vb.registerForLabelChanged(new Observer() {

            public void update(Observable o, Object arg) {
                updateLabel();
            }
        });
        updateLabel();
        numLbl.setFont(new Font(FONTNAME, Font.BOLD, 300));
        c.gridy = 1;
        c.insets = new Insets(50, 0, 50, 0);
        add(numLbl, c);
        statusLbl = new JLabel("Waiting for authorization...");
        statusLbl.setFont(new Font(FONTNAME, Font.PLAIN, 24));
        c.gridy = 2;
        c.insets = new Insets(0, 0, 0, 0);
        add(statusLbl, c);
    }

    private void updateLabel() {
        if (votebox.getLabel() == 0) numLbl.setText("-"); else numLbl.setText(Integer.toString(votebox.getLabel()));
    }
}
