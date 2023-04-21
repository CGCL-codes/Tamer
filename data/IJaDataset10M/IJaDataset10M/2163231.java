package fr.lip6.sma.simulacion.simcommod;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import fr.lip6.sma.simulacion.app.Bundle;
import fr.lip6.sma.simulacion.app.Configuration;
import fr.lip6.sma.simulacion.app.ImagePanel;
import fr.lip6.sma.simulacion.app.LocalizedWindow;
import fr.lip6.sma.simulacion.server.Agent;
import fr.lip6.sma.simulacion.server.AgentException;

/**
 * Classe pour la vue permettant les interactions avec la carte.
 * 
 * @author Patrick Taillandier <patrick.taillandier@gmail.com>
 * @version $Revision: 1.27 $
 * 
 * @see "aucun test d�fini."
 */
public class GovernmentMIPanel extends JPanel implements PropertyChangeListener, ActionListener {

    /**
	 * R�f�rence sur le mod�le.
	 */
    private final GovernmentModel mModel;

    /**
	 * R�f�rence sur la configuration.
	 */
    private final Configuration mConfig;

    /**
	 * R�f�rence sur la liste des agents.
	 */
    private final Set mAgentSet;

    /**
	 * R�f�rence sur le bouton de validation.
	 */
    private final JButton mValidate;

    /**
	 * R�f�rence sur le panel du bouton de validation.
	 */
    private final JPanel mButton;

    /**
	 * R�f�rence sur le champs texte.
	 */
    private final JTextArea mText;

    /**
	 * Label du champs texte.
	 */
    private JLabel mTextLabel;

    /**
	 * Scroll du champs texte.
	 */
    private JScrollPane mScrollPane;

    /**
	 * R�f�rence sur l'�l�ment contenant les infos sur les bonus et les malus
	 * (pouvant scroller).
	 */
    private JScrollPane mScrollInfo;

    /**
	 * R�f�rence sur l'�tiquette avec la phase.
	 */
    private JTextArea mInstructionLabel;

    /**
	 * R�f�rence sur l'�l�ment contenant les infos sur les bonus et les malus.
	 */
    private JPanel mInfoPanel;

    /**
	 * R�f�rence sur la fen�tre localis�e
	 */
    private final LocalizedWindow mTranslation;

    /**
	 * Permet de savoir si on est dans le premier tour
	 */
    private boolean mFirstTurn;

    /**
	 * R�f�rence sur l'�tiquette avec le nombre de joueur pr�t � passer � la
	 * phase suivante.
	 */
    private final JTextArea mAgentNbLabel;

    /**
	 * Nombre de joueur pr�t � passer � la phase suivante.
	 */
    private int mPlayersNb;

    /**
	 * Constructeur � partir d'un mod�le.
	 * 
	 * @param inModel	mod�le pour construire cette vue.
	 * @param inMap		carte pour qui va �tre mise � jour.
	 * @param inConfig	configuration du jeu.
	 * @param inWin		fen�tre pour la traduction.
	 * @param inAgentSet	ensemble des agents.
	 */
    public GovernmentMIPanel(GovernmentModel inModel, GovernmentVisuGrid inMap, Configuration inConfig, LocalizedWindow inWin, Set inAgentSet) {
        mModel = inModel;
        mFirstTurn = true;
        mAgentSet = inAgentSet;
        mTranslation = inWin;
        mConfig = inConfig;
        setSize(230, 500);
        setLayout(null);
        mInstructionLabel = new JTextArea();
        mInstructionLabel.setSize(200, 30);
        mInstructionLabel.setFont(new Font("Serif", Font.BOLD, 10));
        mInstructionLabel.setForeground(Color.black);
        mInstructionLabel.setLocation(0, 0);
        mInstructionLabel.setLineWrap(true);
        mInstructionLabel.setWrapStyleWord(true);
        mInstructionLabel.setOpaque(false);
        mInstructionLabel.setEditable(false);
        mInstructionLabel.setText(mTranslation.localizeString("GameMasterInstruction_Key"));
        add(mInstructionLabel);
        mButton = new JPanel();
        mButton.setBounds(0, 200, 200, 30);
        mValidate = new JButton();
        mValidate.setOpaque(true);
        mValidate.setFont(new Font("Serif", Font.BOLD, 12));
        mValidate.setForeground(Color.black);
        mValidate.addActionListener(this);
        mValidate.setText(mTranslation.localizeString("NextPhase_Key"));
        mButton.add(mValidate, BorderLayout.CENTER);
        add(mButton);
        mAgentNbLabel = new JTextArea();
        mAgentNbLabel.setBounds(0, 40, 200, 25);
        mAgentNbLabel.setVisible(true);
        mAgentNbLabel.setFont(new Font("Serif", Font.BOLD, 10));
        mAgentNbLabel.setLineWrap(true);
        mAgentNbLabel.setWrapStyleWord(true);
        mAgentNbLabel.setOpaque(false);
        mAgentNbLabel.setEditable(false);
        add(mAgentNbLabel);
        mTextLabel = new JLabel();
        mTextLabel.setSize(100, 15);
        mTextLabel.setFont(new Font("Serif", Font.BOLD, 12));
        mTextLabel.setForeground(Color.black);
        mTextLabel.setLocation(0, 150);
        mTextLabel.setOpaque(false);
        mTextLabel.setText(mTranslation.localizeString("ManagementText_Key"));
        mTextLabel.setVisible(false);
        add(mTextLabel);
        mText = new JTextArea();
        mScrollPane = new JScrollPane(mText);
        mScrollPane.setBounds(0, 170, 195, 28);
        mScrollPane.setPreferredSize(new Dimension(195, 28));
        mScrollPane.setOpaque(true);
        mScrollPane.setVisible(false);
        mText.setSize(165, 28);
        mText.setLineWrap(true);
        mText.setWrapStyleWord(true);
        mText.setOpaque(true);
        add(mScrollPane);
        mModel.addPropertyChangeListener(this);
    }

    /**
	 * G�re les infos sur les malus et les bonus des cases.
	 */
    public final void updateInfo() {
        if (SwingUtilities.isEventDispatchThread()) {
            doUpdateInfo();
        } else {
            final Runnable theEffectCode = new Runnable() {

                public void run() {
                    doUpdateInfo();
                }
            };
            SwingUtilities.invokeLater(theEffectCode);
        }
    }

    /**
	 * G�re les infos sur les malus et les bonus des cases.
	 */
    private void doUpdateInfo() {
        Map theInfos;
        theInfos = mModel.getTotalValuesForCell(mModel.getCurrentCell().getCellID());
        if (mScrollInfo != null) {
            remove(mScrollInfo);
        }
        mInfoPanel = new JPanel();
        mInfoPanel.setLayout(null);
        if (theInfos != null) {
            mInfoPanel.setSize(160, theInfos.size() * 43);
            mInfoPanel.setPreferredSize(new Dimension(160, theInfos.size() * 43));
        } else {
            mInfoPanel.setSize(0, 0);
            mInfoPanel.setPreferredSize(new Dimension(0, 0));
        }
        mInfoPanel.setOpaque(false);
        mScrollInfo = new JScrollPane(mInfoPanel);
        mScrollInfo.setSize(210, 75);
        mScrollInfo.setPreferredSize(new Dimension(210, 75));
        mScrollInfo.setLocation(0, 70);
        mScrollInfo.setOpaque(false);
        mScrollInfo.setVisible(true);
        add(mScrollInfo);
        mInfoPanel.removeAll();
        if (theInfos != null) {
            int indexInfo = 0;
            final Iterator theInfoIter = theInfos.keySet().iterator();
            while (theInfoIter.hasNext()) {
                final String theAgentName = (String) theInfoIter.next();
                final String theImagePath = mConfig.getAgentProperty(theAgentName, "avatar_mini");
                final ImagePanel theImagePanel = new ImagePanel(Bundle.getImage(theImagePath));
                theImagePanel.setBounds(0, indexInfo * 42, 40, 40);
                mInfoPanel.add(theImagePanel);
                final JLabel theAvatarLabel = new JLabel();
                theAvatarLabel.setBounds(50, indexInfo * 42 + 10, 60, 18);
                theAvatarLabel.setPreferredSize(new Dimension(60, 18));
                theAvatarLabel.setForeground(Color.black);
                theAvatarLabel.setText(theAgentName);
                theAvatarLabel.setVisible(true);
                theAvatarLabel.setOpaque(false);
                theAvatarLabel.setFont(new Font("Serif", Font.BOLD, 10));
                mInfoPanel.add(theAvatarLabel);
                final int theValue = Integer.parseInt(theInfos.get(theAgentName) + "");
                if (theValue > 0) {
                    final ImagePanel theBonusPanel = createBonusPanel(indexInfo, theValue);
                    mInfoPanel.add(theBonusPanel);
                } else if (theValue < 0) {
                    final ImagePanel theMalusPanel = createMalusPanel(indexInfo, theValue);
                    mInfoPanel.add(theMalusPanel);
                }
                indexInfo++;
            }
        }
        mInfoPanel.repaint();
    }

    /**
	 * Cr�e un panneau pour un bonus.
	 *
	 * @param inIndexInfo	indice de ce panneau.
	 * @param inValue	valeur du bonus.
	 * @return un panneau qui afffiche le bonus.
	 */
    private ImagePanel createBonusPanel(int inIndexInfo, int inValue) {
        final ImagePanel theBonusPanel;
        final JLabel theBonusLabel;
        theBonusPanel = new ImagePanel(Bundle.getImage("artwork/simcommod/bonus_info.gif"));
        theBonusPanel.setBounds(110, inIndexInfo * 42, 80, 35);
        theBonusLabel = new JLabel();
        theBonusLabel.setSize(60, 25);
        theBonusLabel.setLocation(24, 7);
        theBonusLabel.setText("+" + inValue);
        theBonusLabel.setVisible(true);
        theBonusLabel.setOpaque(false);
        theBonusLabel.setFont(new Font("Serif", Font.BOLD, 20));
        theBonusLabel.setForeground(Color.BLACK);
        theBonusPanel.add(theBonusLabel);
        return theBonusPanel;
    }

    /**
	 * Cr�e un panneau pour un malus.
	 *
	 * @param inIndexInfo	indice de ce panneau.
	 * @param inValue		valeur du malus (n�gative)
	 * @return un panneau qui affiche le malus.
	 */
    private ImagePanel createMalusPanel(int inIndexInfo, int inValue) {
        final ImagePanel theMalusPanel;
        final JLabel theMalusLabel;
        theMalusPanel = new ImagePanel(Bundle.getImage("artwork/simcommod/malus_info.gif"));
        theMalusPanel.setLocation(110, inIndexInfo * 42);
        theMalusPanel.setSize(80, 35);
        theMalusLabel = new JLabel();
        theMalusLabel.setSize(60, 25);
        theMalusLabel.setLocation(27, 7);
        theMalusLabel.setText("" + inValue);
        theMalusLabel.setOpaque(false);
        theMalusLabel.setVisible(true);
        theMalusLabel.setFont(new Font("Serif", Font.BOLD, 20));
        theMalusLabel.setForeground(Color.WHITE);
        theMalusPanel.add(theMalusLabel);
        return theMalusPanel;
    }

    /**
	 * M�thode appel�e lorsque l'on appuie sur le bouton.
	 * 
	 * @param inEvent
	 *            �v�nement du changement.
	 */
    public void actionPerformed(ActionEvent inEvent) {
        mValidate.setEnabled(false);
        final Iterator theAgentsIter = mAgentSet.iterator();
        Map theParams;
        while (theAgentsIter.hasNext()) {
            theParams = new Hashtable();
            final Agent theAgent = (Agent) theAgentsIter.next();
            final String theAgentClass = theAgent.getAgentClass();
            if (theAgentClass.equals("Harvester") || theAgentClass.equals("Environmentalist")) {
                if (mModel.getGamePhase() == SimCommodModel.PHASE_NEGOTIATION) {
                    theParams.put("phase", "Validation");
                    theParams.put("time", 100 + "");
                    try {
                        theAgent.executeAsync("updatePhase", theParams);
                    } catch (AgentException anException) {
                        anException.printStackTrace();
                    }
                } else {
                    theParams.put("phase", "Wait");
                    theParams.put("time", mModel.getWaitTime() + "");
                    try {
                        theAgent.executeAsync("updatePhase", theParams);
                    } catch (AgentException anException) {
                        anException.printStackTrace();
                    }
                }
            }
        }
        if (mModel.getGamePhase() == SimCommodModel.PHASE_MANAGEMENT) {
            if (mInfoPanel != null) {
                mInfoPanel.removeAll();
            }
            if (!mText.getText().equals("")) {
                mModel.setText(mTranslation.localizeString("MessageFrom_Key") + mText.getText());
            }
            mText.setText("");
            mModel.setGamePhase(SimCommodModel.PHASE_WAIT);
        }
        if (mModel.getGamePhase() == SimCommodModel.PHASE_NEGOTIATION) {
            mModel.setGamePhase(SimCommodModel.PHASE_VALIDATION);
        }
    }

    /**
	 * M�thode appel�e lorsque la valeur change.
	 * 
	 * @param inEvent
	 *            �v�nement du changement.
	 */
    public final void propertyChange(PropertyChangeEvent inEvent) {
        final String thePropertyName = inEvent.getPropertyName();
        if (thePropertyName.equals(SimCommodModel.PHASE_PROPERTY_NAME)) {
            updatePhase();
        } else if (thePropertyName.equals(SimCommodModel.READY_PLAYERS_PROPERTY_NAME)) {
            updateReady();
        } else if (mModel.getCurrentCell() != null) {
            updateInfo();
        }
    }

    /**
	 * Mise � jour du nombre de joueurs pr�ts.
	 */
    private void updateReady() {
        if (SwingUtilities.isEventDispatchThread()) {
            doUpdateReady();
        } else {
            final Runnable theEffectCode = new Runnable() {

                public void run() {
                    doUpdateReady();
                }
            };
            SwingUtilities.invokeLater(theEffectCode);
        }
    }

    /**
	 * Mise � jour du nombre de joueurs pr�ts.
	 */
    private void doUpdateReady() {
        if (mModel.getGamePhase() == SimCommodModel.PHASE_NEGOTIATION) {
            mAgentNbLabel.setText("");
        } else {
            mAgentNbLabel.setText(mTranslation.localizeString("ReadyPlayersNb:_Key") + mModel.countPlayersReady() + "/" + mPlayersNb);
        }
    }

    /**
	 * Mise � jour de la phase.
	 */
    private void updatePhase() {
        if (SwingUtilities.isEventDispatchThread()) {
            doUpdatePhase();
        } else {
            final Runnable theEffectCode = new Runnable() {

                public void run() {
                    doUpdatePhase();
                }
            };
            SwingUtilities.invokeLater(theEffectCode);
        }
    }

    /**
	 * Mise � jour de la phase.
	 */
    private void doUpdatePhase() {
        mValidate.setEnabled(true);
        if (mModel.getGamePhase() == SimCommodModel.PHASE_MANAGEMENT) {
            mInstructionLabel.setText(mTranslation.localizeString("ManagementInstruction_Key"));
            mValidate.setText(mTranslation.localizeString("Government_Key"));
            mTextLabel.setVisible(true);
            mScrollPane.setVisible(true);
            mFirstTurn = false;
        } else if (mModel.getGamePhase() == SimCommodModel.PHASE_HARVESTING) {
            mInstructionLabel.setText(mTranslation.localizeString("GameMasterInstruction_Key"));
            mValidate.setText(mTranslation.localizeString("NextPhase_Key"));
            if (!mFirstTurn) {
                mTextLabel.setVisible(false);
                mScrollPane.setVisible(false);
                if (mScrollInfo != null) {
                    remove(mScrollInfo);
                }
                repaint();
            }
        }
        switch(mModel.getGamePhase()) {
            case SimCommodModel.PHASE_HARVESTING:
                mPlayersNb = mModel.getHarvesterNbTot();
                break;
            case SimCommodModel.PHASE_PROPELABORATION:
            case SimCommodModel.PHASE_VALIDATION:
            case SimCommodModel.PHASE_HARVESTINGRESULTS:
            case SimCommodModel.PHASE_RESULT:
                mPlayersNb = mModel.getEnvironmentalistNbTot() + mModel.getHarvesterNbTot();
                break;
            default:
                break;
        }
        if (mModel.getGamePhase() == SimCommodModel.PHASE_NEGOTIATION) {
            mAgentNbLabel.setText("");
        } else {
            mAgentNbLabel.setText(mTranslation.localizeString("ReadyPlayersNb:_Key") + mModel.countPlayersReady() + "/" + mPlayersNb);
        }
        mAgentNbLabel.repaint();
    }
}
