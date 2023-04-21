package hoplugins.teamAnalyzer.ui.lineup;

import hoplugins.teamAnalyzer.SystemManager;
import hoplugins.teamAnalyzer.ui.RatingBox;
import hoplugins.teamAnalyzer.ui.TeamLineupData;
import javax.swing.JPanel;

/**
 * Main formation Panel class that delegator to subclasses the issue of building the gui
 *
 * @author <a href=mailto:draghetto@users.sourceforge.net>Massimiliano Amato</a>
 */
public class FormationPanel extends JPanel {

    /**
	 * 
	 */
    private static final long serialVersionUID = 2989171587658994483L;

    /** Left Attack RatingBox */
    protected RatingBox leftAtt = new RatingBox();

    /** Left Defence RatingBox */
    protected RatingBox leftDef = new RatingBox();

    /** Central Attack RatingBox */
    protected RatingBox midAtt = new RatingBox();

    /** Central Defence RatingBox */
    protected RatingBox midDef = new RatingBox();

    /** Midfield RatingBox */
    protected RatingBox midfield = new RatingBox();

    /** Right Attack RatingBox */
    protected RatingBox rightAtt = new RatingBox();

    /** Right Defence RatingBox */
    protected RatingBox rightDef = new RatingBox();

    /** User Team Lineup Data */
    protected TeamLineupData myTeam = new TeamLineupData();

    /** Opponent Team Lineup Data */
    protected TeamLineupData opponentTeam = new TeamLineupData();

    /**
     * Creates a new FormationPanel object.
     */
    public FormationPanel() {
        setLayout(SystemManager.getConfig().isLineup(), SystemManager.getConfig().isMixedLineup());
    }

    /**
     * Returns user team lineup data
     *
     * @return Own lineup data
     */
    public TeamLineupData getMyTeam() {
        return myTeam;
    }

    /**
     * Returns opponent team lineup data
     *
     * @return Opponents lineup data
     */
    public TeamLineupData getOpponentTeam() {
        return opponentTeam;
    }

    /**
     * Refresh the panel
     *
     * @param compare if both teams must be shown
     * @param type standard (false) or mixed layout (true)
     */
    public void reload(boolean compare, boolean type) {
        setLayout(compare, type);
        midfield.reload((int) myTeam.getMidfield(), (int) opponentTeam.getMidfield());
        rightDef.reload((int) myTeam.getLeftAttack(), (int) opponentTeam.getRightDefence());
        leftDef.reload((int) myTeam.getRightAttack(), (int) opponentTeam.getLeftDefence());
        midDef.reload((int) myTeam.getMiddleAttack(), (int) opponentTeam.getMiddleDefence());
        rightAtt.reload((int) myTeam.getLeftDefence(), (int) opponentTeam.getRightAttack());
        leftAtt.reload((int) myTeam.getRightDefence(), (int) opponentTeam.getLeftAttack());
        midAtt.reload((int) myTeam.getMiddleDefence(), (int) opponentTeam.getMiddleAttack());
    }

    /**
     * Sets the Layout of the panel, by delegating to the proper class
     *
     * @param compare true if both teams must be shown
     * @param mixedLIneup true if mixedlineup has to be used, false if not
     */
    private void setLayout(boolean compare, boolean mixedLIneup) {
        LineupStylePanel panel;
        if (mixedLIneup) {
            panel = new MixedLineupPanel(this);
        } else {
            panel = new StandardLineupPanel(this);
        }
        if (compare) {
            panel.initCompare();
        } else {
            panel.initSingle();
        }
        removeAll();
        setOpaque(false);
        add(panel);
    }
}
