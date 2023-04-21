package ho.module.teamAnalyzer.ui;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * This is an empty panel to display a lineup
 */
public class TeamLineupPanel {

    /** The Panels and Label */
    private JLabel m_jlTeamName = new JLabel();

    private JPanel m_clLinkeAussenVerteidiger = new JPanel();

    private JPanel m_clLinkeFluegel = new JPanel();

    private JPanel m_clLinkeInnenVerteidiger = new JPanel();

    private JPanel m_clLinkeMittelfeld = new JPanel();

    private JPanel m_clLinkerSturm = new JPanel();

    private JPanel m_clRechteAussenVerteidiger = new JPanel();

    private JPanel m_clRechteFluegel = new JPanel();

    private JPanel m_clRechteInnenVerteidiger = new JPanel();

    private JPanel m_clRechteMittelfeld = new JPanel();

    private JPanel m_clRechterSturm = new JPanel();

    private JPanel m_clTorwart = new JPanel();

    private double leftAttack;

    private double leftDefence;

    private double middleAttack;

    private double middleDefence;

    private double midfield;

    private double rightAttack;

    private double rightDefence;

    /**
     * Get the Panel Keeper
     *
     * @return TODO Missing Return Method Documentation
     */
    public JPanel getKeeperPanel() {
        return m_clTorwart;
    }

    /**
     * TODO Missing Method Documentation
     *
     * @param i TODO Missing Method Parameter Documentation
     */
    public void setLeftAttack(double i) {
        leftAttack = i;
    }

    public double getLeftAttack() {
        return leftAttack;
    }

    /**
     * Get the Panel Left Central Defender
     *
     * @return TODO Missing Return Method Documentation
     */
    public JPanel getLeftCentralDefenderPanel() {
        return m_clLinkeInnenVerteidiger;
    }

    /**
     * TODO Missing Method Documentation
     *
     * @param i TODO Missing Method Parameter Documentation
     */
    public void setLeftDefence(double i) {
        leftDefence = i;
    }

    /**
     * TODO Missing Method Documentation
     *
     * @return TODO Missing Return Method Documentation
     */
    public double getLeftDefence() {
        return leftDefence;
    }

    /**
     * Get the Panel Left Forward
     *
     * @return TODO Missing Return Method Documentation
     */
    public JPanel getLeftForwardPanel() {
        return m_clLinkerSturm;
    }

    /**
     * Get the Panel Left Midfield
     *
     * @return TODO Missing Return Method Documentation
     */
    public JPanel getLeftMidfieldPanel() {
        return m_clLinkeMittelfeld;
    }

    /**
     * Get the Panel Left Wing
     *
     * @return TODO Missing Return Method Documentation
     */
    public JPanel getLeftWingPanel() {
        return m_clLinkeFluegel;
    }

    /**
     * Get the Panel Left Wingback
     *
     * @return TODO Missing Return Method Documentation
     */
    public JPanel getLeftWingbackPanel() {
        return m_clLinkeAussenVerteidiger;
    }

    /**
     * TODO Missing Method Documentation
     *
     * @param i TODO Missing Method Parameter Documentation
     */
    public void setMiddleAttack(double i) {
        middleAttack = i;
    }

    /**
     * TODO Missing Method Documentation
     *
     * @return TODO Missing Return Method Documentation
     */
    public double getMiddleAttack() {
        return middleAttack;
    }

    /**
     * TODO Missing Method Documentation
     *
     * @param i TODO Missing Method Parameter Documentation
     */
    public void setMiddleDefence(double i) {
        middleDefence = i;
    }

    /**
     * TODO Missing Method Documentation
     *
     * @return TODO Missing Return Method Documentation
     */
    public double getMiddleDefence() {
        return middleDefence;
    }

    /**
     * TODO Missing Method Documentation
     *
     * @param i TODO Missing Method Parameter Documentation
     */
    public void setMidfield(double i) {
        midfield = i;
    }

    /**
     * TODO Missing Method Documentation
     *
     * @return TODO Missing Return Method Documentation
     */
    public double getMidfield() {
        return midfield;
    }

    /**
     * TODO Missing Method Documentation
     *
     * @param i TODO Missing Method Parameter Documentation
     */
    public void setRightAttack(double i) {
        rightAttack = i;
    }

    /**
     * TODO Missing Method Documentation
     *
     * @return TODO Missing Return Method Documentation
     */
    public double getRightAttack() {
        return rightAttack;
    }

    /**
     * Get the Panel Right Centraldefender
     *
     * @return TODO Missing Return Method Documentation
     */
    public JPanel getRightCentralDefenderPanel() {
        return m_clRechteInnenVerteidiger;
    }

    /**
     * TODO Missing Method Documentation
     *
     * @param i TODO Missing Method Parameter Documentation
     */
    public void setRightDefence(double i) {
        rightDefence = i;
    }

    /**
     * TODO Missing Method Documentation
     *
     * @return TODO Missing Return Method Documentation
     */
    public double getRightDefence() {
        return rightDefence;
    }

    /**
     * Get the Panel Right Forward
     *
     * @return TODO Missing Return Method Documentation
     */
    public JPanel getRightForwardPanel() {
        return m_clRechterSturm;
    }

    /**
     * Get the Panel Right Midfield
     *
     * @return TODO Missing Return Method Documentation
     */
    public JPanel getRightMidfieldPanel() {
        return m_clRechteMittelfeld;
    }

    /**
     * Get the Panel Right Wing
     *
     * @return TODO Missing Return Method Documentation
     */
    public JPanel getRightWingPanel() {
        return m_clRechteFluegel;
    }

    /**
     * Get the Panel Right Wingback
     *
     * @return TODO Missing Return Method Documentation
     */
    public JPanel getRightWingbackPanel() {
        return m_clRechteAussenVerteidiger;
    }

    /**
     * Set the Name of the Team
     *
     * @param teamname TODO Missing Constructuor Parameter Documentation
     */
    public void setTeamName(String teamname) {
        m_jlTeamName.setText(teamname);
    }

    /**
     * Get the Name of the Team
     *
     * @return TODO Missing Return Method Documentation
     */
    public JLabel getTeamPanel() {
        return m_jlTeamName;
    }
}
