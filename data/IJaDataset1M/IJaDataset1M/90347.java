package hoplugins.teamAnalyzer.ui.component;

import hoplugins.Commons;
import hoplugins.commons.ui.NumberTextField;
import hoplugins.commons.utils.PluginProperty;
import plugins.IMatchDetails;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * A panel that allows the user to download a new match from HO
 *
 * @author <a href=mailto:draghetto@users.sourceforge.net>Massimiliano Amato</a>
 */
public class DownloadPanel extends JPanel {

    /**
	 * 
	 */
    private static final long serialVersionUID = -3212179990708350342L;

    /** Download Button */
    JButton downloadButton = new JButton(PluginProperty.getString("Download"));

    /** Description label */
    JLabel jLabel1 = new JLabel();

    /** Status label */
    JLabel status = new JLabel();

    /** The matchid text field */
    NumberTextField matchId = new NumberTextField(10);

    /**
     * Constructs a new instance.
     */
    public DownloadPanel() {
        jbInit();
    }

    /**
     * Initializes the state of this instance.
     */
    private void jbInit() {
        jLabel1.setText(PluginProperty.getString("Download.Desc"));
        setLayout(new BorderLayout());
        add(jLabel1, BorderLayout.NORTH);
        add(downloadButton, BorderLayout.CENTER);
        add(matchId, BorderLayout.WEST);
        add(status, BorderLayout.SOUTH);
        downloadButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int id = matchId.getValue();
                if (id == 0) {
                    status.setText(PluginProperty.getString("Download.Error"));
                    return;
                }
                if (Commons.getModel().getHelper().downloadMatchData(id)) {
                    IMatchDetails md = Commons.getModel().getMatchDetails(id);
                    if (md.getFetchDatum() != null) {
                        status.setText(PluginProperty.getString("Download.Ok"));
                        matchId.setText("");
                    } else {
                        status.setText(PluginProperty.getString("Download.Error"));
                    }
                }
            }
        });
    }
}
