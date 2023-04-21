package fr.lip6.sma.simulacion.simbar3.agents;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import fr.lip6.sma.simulacion.app.Configuration;
import fr.lip6.sma.simulacion.server.Agent;
import fr.lip6.sma.simulacion.server.AgentException;
import fr.lip6.sma.simulacion.server.AgentListClient;
import fr.lip6.sma.simulacion.server.AgentListManager;
import fr.lip6.sma.simulacion.server.LocalAgent;
import fr.lip6.sma.simulacion.server.LocalLogAgentImpl;
import fr.lip6.sma.simulacion.server.Player;
import fr.lip6.sma.simulacion.simbar3.EnvironmentApplication;
import fr.lip6.sma.simulacion.simbar3.EnvironmentModel;

/**
 * Classe pour l'environnement du bar El Farol dans SimBar3. Cet agent g�re les
 * interactions entre joueurs (directes, �coute flottante, interaction par
 * marqueurs), le temps, et les mouvements des joueurs pour affichage.
 * 
 * @author �ric Platon <platon@nii.ac.jp>
 * @version $Revision: 1.32 $
 * 
 * @see "aucun test d�fini."
 */
public class Environment extends LocalLogAgentImpl implements LocalAgent, AgentListClient {

    /**
     * Classe de l'agent.
     */
    public static final String CLASS = "Environment";

    /**
     * Param�tre de simulation 1/5: mode de port�e de la perception des marqueur
     * d'agents: true: port�e limit�e (c.f. le champs "mPerceptionRange").
     * false: port�e limit�e
     */
    private final boolean mLimitedRangeMode;

    /**
     * Param�tre de simulation 2/5: pr�sence du marqueur d'�quipe:
     */
    private final boolean mTeamTag;

    /**
     * Param�tre de simulation 3/5: pr�sence du marqueur de score individuel:
     */
    private final boolean mScoreTag;

    /**
     * Param�tre de simulation 4/5: mode d'�coute flottante:
     */
    private final boolean mOverhearingMode;

    /**
     * Param�tre de simulation 5/5: port�e de la perception des marqueur
     * d'agents: Le "diam�tre" du p�rim�tre � l'int�rieur duquel un agent peut
     * percevoir les marqueurs de autres. Ce diam�tre est en g�n�ral un carr�
     * NxN avec N=mPerceptionRange (d�pend de la topologie de l'environnement.
     * 
     * Valeur: 0 pour port�e infinie, diff�rente de 0 autrement DOIT ETRE
     * POSITIF
     */
    private final int mPerceptionRange;

    /**
     * Liste des habitu�s du bar. Les �l�ments sont des Agent.
     */
    private final Set mBarRegulars;

    /**
     * Mod�le de l'environnement.
     */
    private EnvironmentModel mModel;

    /**
	 * L'agent du bar.
	 */
    private Agent mBarAgent;

    /**
     * R�f�rence sur l'application.
     */
    private EnvironmentApplication mApplication;

    /**
     * Constructeur � partir d'un nom et de propri�t�s.
     * 
     * @param inAgentListManager
     *            gestionnaire de la liste des agents.
     * @param inAgentName
     *            nom de l'agent.
     * @param inProperties
     *            propri�t�s de l'agent.
     */
    public Environment(AgentListManager inAgentListManager, String inAgentName, Map inProperties) {
        super(inAgentListManager, inAgentName, inProperties);
        mLimitedRangeMode = Boolean.getBoolean(getProperty("limitedRangeMode"));
        mTeamTag = Boolean.getBoolean(getProperty("teamTag"));
        mScoreTag = Boolean.getBoolean(getProperty("scoreTag"));
        mOverhearingMode = Boolean.getBoolean(getProperty("overhearingMode"));
        mPerceptionRange = Integer.parseInt(getProperty("perceptionRange"));
        mBarRegulars = new HashSet();
    }

    /**
     * Accesseur sur la classe de l'agent.
     * 
     * @return la classe de l'agent.
     */
    public final String getAgentClass() {
        return CLASS;
    }

    /**
     * S�lecteur sur l'application. Cette m�thode ne doit �tre appel�e qu'une
     * fois.
     * 
     * @param inApplication
     *            application � laquelle on appartient.
     */
    public final void bindToApp(EnvironmentApplication inApplication) {
        assert mApplication == null;
        mApplication = inApplication;
        setupModel(mApplication.getConfiguration());
    }

    /**
     * Configure l'agent � partir de la configuration.
     * Cr�e le mod�le de l'environnement.
     * 
     * @param inConfiguration	configuration du jeu.
     */
    private void setupModel(Configuration inConfiguration) {
        final AgentListManager theAgentListManager = getAgentListManager();
        mModel = new EnvironmentModel(null, theAgentListManager, inConfiguration);
        theAgentListManager.addListener(this);
        theAgentListManager.registerLocalAgent(this);
    }

    /**
     * S�lecteur de la configuration de l'environnement La m�thode retourne les
     * 5 param�tres de simulations d�finis au d�but de cette classe.
     * 
     * @return une table des param�tres de configuration de l'environnement.
     */
    public final Map getConfig() {
        final Map config = new Hashtable();
        config.put("mLimitedRangeMode", Boolean.toString(mLimitedRangeMode));
        config.put("mTeamTag", Boolean.toString(mTeamTag));
        config.put("mScoreTag", Boolean.toString(mScoreTag));
        config.put("mOverhearingMode", Boolean.toString(mOverhearingMode));
        config.put("mPerceptionRange", Integer.toString(mPerceptionRange));
        return config;
    }

    /**
     * Ex�cute une op�ration (localement ou � travers le r�seau).
     * 
     * @param inOperationName
     *            nom de l'op�ration � ex�cuter.
     * @param inParams
     *            param�tres de l'op�ration.
     * @return le r�sultat de l'op�ration (sous forme de cha�ne).
     * @throws AgentException
     *             si un probl�me est survenu dans l'ex�cution de la m�thode de
     *             l'agent (par exemple si l'op�ration n'existe pas)
     */
    public final String doExecute(String inOperationName, Map inParams) throws AgentException {
        String theResult;
        if (inOperationName.equals("moveAgent")) {
            theResult = moveAgent(inParams);
        } else if (inOperationName.equals("changeAgentTeam")) {
            theResult = changeAgentTeam(inParams);
        } else if (inOperationName.equals("setAttendees")) {
            theResult = setAttendees(inParams);
        } else {
            theResult = super.doExecute(inOperationName, inParams);
        }
        return theResult;
    }

    /**
     * M�thode appel�e pour d�placer un agent.
     * On accepte le d�placement s'il ne conduit pas � trop d'agents sur
     * une case.
     * 
	 * @param inParams	param�tres pour la commande (vient de execute)
	 * @return r�sultat de la commande (pour execute)
	 * @see #doExecute
	 */
    private String moveAgent(Map inParams) {
        final String movingAgentName = (String) inParams.get("agentName");
        final String originalPosition = (String) inParams.get("from");
        final String destination = (String) inParams.get("to");
        String theResult = "OK";
        if (mModel.moveAgent(movingAgentName, originalPosition, destination)) {
            propagateAgentMove(movingAgentName, originalPosition, destination);
        } else {
            final String actualPosition = mModel.getAgentPositionAsString(movingAgentName);
            if (actualPosition != null) {
                informAgentOfMove(movingAgentName, originalPosition, actualPosition);
            }
            theResult = "NO";
        }
        return theResult;
    }

    /**
     * Method called by an agent when it wants to change its team.
     * 
	 * @param inParams	parameters of the command (from <code>execute</code>)
	 * @return result of the command (for <code>execute</code>)
	 * @see #doExecute
	 */
    private String changeAgentTeam(Map inParams) {
        final String agentName = (String) inParams.get("agentName");
        final String newTeam = (String) inParams.get("team");
        String theResult = "OK";
        if (mModel.changeAgentTeam(agentName, newTeam)) {
            final String thePosition = mModel.getAgentPositionAsString(agentName);
            propagateAgentMove(agentName, thePosition, thePosition);
        } else {
            theResult = "NO";
        }
        return theResult;
    }

    /**
     * M�thode appel�e pour d�placer les habitu�s qui vont au bar dans
     * le bar.
     * 
	 * @param inParams	param�tres pour la commande (vient de execute)
	 * @return r�sultat de la commande (pour execute)
	 * @see #doExecute
	 */
    private String setAttendees(Map inParams) {
        final List theAgents = (List) inParams.get("list");
        final Set pastAttendees = mModel.getAgentsInBar();
        Iterator iterator = theAgents.iterator();
        while (iterator.hasNext()) {
            final String agentName = (String) iterator.next();
            final String oldPosition = mModel.getAgentPositionAsString(agentName);
            if (oldPosition == null) {
                System.out.println("Ignore agent " + agentName + " among the attendees since we don't know about it.");
            } else {
                mModel.moveAgentToBar(agentName);
                final String newPosition = mModel.getAgentPositionAsString(agentName);
                informAgentOfMove(agentName, oldPosition, newPosition);
                propagateAgentMove(agentName, oldPosition, newPosition);
            }
        }
        pastAttendees.removeAll(theAgents);
        iterator = pastAttendees.iterator();
        while (iterator.hasNext()) {
            final String agentName = (String) iterator.next();
            final String oldPosition = mModel.getAgentPositionAsString(agentName);
            if (oldPosition == null) {
                System.err.println("Ignore agent " + agentName + " among the non-attendees since we don't know about it.");
            } else {
                mModel.moveAgentHome(agentName);
                final String newPosition = mModel.getAgentPositionAsString(agentName);
                informAgentOfMove(agentName, oldPosition, newPosition);
                propagateAgentMove(agentName, oldPosition, newPosition);
            }
        }
        return "OK";
    }

    /**
     * M�thode appel�e lorsque l'enregistrement a r�ussi.
     * 
     * @param inLocalAgent
     *            agent enregistr� avec succ�s.
     */
    public void registrationSucceeded(LocalAgent inLocalAgent) {
        listChanged();
    }

    /**
     * M�thode appel�e lorsque l'enregistrement a �chou�.
     * 
     * @param inLocalAgent
     *            agent qui n'a pas pu �tre enregistr�.
     */
    public final void registrationFailed(LocalAgent inLocalAgent) {
        System.exit(1);
    }

    /**
     * M�thode appel�e lorsque la liste a �t� modifi�e.
     */
    public final void listChanged() {
        final Set theAgentSet = getAgentListManager().getAgentSet();
        Set theNewAgentsSet;
        Set theVanishedAgentsSet;
        synchronized (mBarRegulars) {
            theNewAgentsSet = new HashSet(theAgentSet);
            theNewAgentsSet.removeAll(mBarRegulars);
            if (mBarAgent != null) {
                theNewAgentsSet.remove(mBarAgent);
            }
            theVanishedAgentsSet = new HashSet(mBarRegulars);
            if (mBarAgent != null) {
                theVanishedAgentsSet.add(mBarAgent);
            }
            theVanishedAgentsSet.removeAll(theAgentSet);
        }
        final Iterator theNewAgentsIter = theNewAgentsSet.iterator();
        while (theNewAgentsIter.hasNext()) {
            final Agent theAgent = (Agent) theNewAgentsIter.next();
            if (theAgent.getAgentClass().equals(Player.CLASS)) {
                registerBarRegular((Player) theAgent);
            } else if (theAgent.getAgentClass().equals(SantaFeBar.CLASS)) {
                registerBarAgent(theAgent);
            }
        }
        final Iterator theVanishedAgentsIter = theVanishedAgentsSet.iterator();
        while (theVanishedAgentsIter.hasNext()) {
            final Agent theAgent = (Agent) theVanishedAgentsIter.next();
            if (theAgent.getAgentClass().equals(Player.CLASS)) {
                unregisterBarRegular((Player) theAgent);
            } else if (theAgent.getAgentClass().equals(SantaFeBar.CLASS)) {
                unregisterBarAgent(theAgent);
            }
        }
    }

    /**
     * Enregistre un agent.
     * 
     * @param inPlayer
     *            nom de l'habitu� � enregistrer.
     */
    private void registerBarRegular(Player inPlayer) {
        final Map theParams = new Hashtable();
        theParams.put("name", getAgentName());
        theParams.put("class", getAgentClass());
        final String theRegularName = inPlayer.getAgentName();
        mModel.addAgent(theRegularName);
        final String thePosition = mModel.getAgentPositionAsString(theRegularName);
        final String theTeam = mModel.getAgentTeamAsString(theRegularName);
        theParams.put("yourLocation", thePosition);
        theParams.put("yourTeam", theTeam);
        boolean succeeded = false;
        try {
            inPlayer.execute("registerEnvironment", theParams);
            succeeded = true;
        } catch (AgentException anException) {
            anException.printStackTrace();
            mModel.removeAgent(theRegularName);
        }
        if (succeeded) {
            propagateAgentMove(theRegularName, null, thePosition);
            tellNewcomerAboutRegularsAndTheBar(inPlayer);
            synchronized (mBarRegulars) {
                mBarRegulars.add(inPlayer);
            }
            if (mApplication != null) {
                mApplication.playerArrived(theRegularName);
            }
        }
    }

    /**
	 * Indique � un nouveau o� se trouve les agents.
	 * 
	 * @param inNewComer	le nouvel agent.
	 */
    private void tellNewcomerAboutRegularsAndTheBar(Agent inNewComer) {
        synchronized (mBarRegulars) {
            final Iterator theAgentsIter = mBarRegulars.iterator();
            while (theAgentsIter.hasNext()) {
                final Agent theAgent = (Agent) theAgentsIter.next();
                final String theAgentName = theAgent.getAgentName();
                sendAgentEnvironmentData(inNewComer, theAgentName);
            }
        }
        if (mBarAgent != null) {
            final String theBarName = mBarAgent.getAgentName();
            sendAgentEnvironmentData(inNewComer, theBarName);
        }
    }

    /**
     * Enregistre le bar.
     * 
     * @param inBarAgent agent du bar.
     */
    private void registerBarAgent(Agent inBarAgent) {
        if (mBarAgent != null) {
            throw new IllegalStateException("Only one bar is allowed in the game");
        }
        final Map theParams = new Hashtable();
        theParams.put("name", getAgentName());
        theParams.put("class", getAgentClass());
        final String theBarName = inBarAgent.getAgentName();
        mModel.addAgent(theBarName);
        final String thePosition = mModel.getAgentPositionAsString(theBarName);
        theParams.put("yourLocation", thePosition);
        boolean succeeded = false;
        try {
            inBarAgent.execute("registerEnvironment", theParams);
            succeeded = true;
        } catch (AgentException anException) {
            anException.printStackTrace();
            mModel.removeAgent(theBarName);
        }
        if (succeeded) {
            propagateAgentMove(theBarName, null, thePosition);
            mBarAgent = inBarAgent;
            if (mApplication != null) {
                mApplication.playerArrived(theBarName);
            }
        }
    }

    /**
     * D�senregistre le bar.
     * 
     * @param inBarAgent agent du bar.
     */
    private void unregisterBarAgent(Agent inBarAgent) {
        if (mBarAgent == null) {
            throw new IllegalStateException("No bar was present in the game");
        }
        final String theBarName = inBarAgent.getAgentName();
        final String theOldPosition = mModel.getAgentPositionAsString(theBarName);
        mModel.removeAgent(theBarName);
        propagateAgentMove(theBarName, theOldPosition, null);
        mBarAgent = null;
    }

    /**
     * Indique le d�placement d'un agent aux autres agents.
     * 
     * @param inAgentName		agent qui a boug�.
     * @param inOldPosition	ancienne position de l'agent ou <code>null</code>.
     * @param inNewPosition	nouvelle position de l'agent ou <code>null</code>.
     */
    private void propagateAgentMove(String inAgentName, String inOldPosition, String inNewPosition) {
        final Map theParams = new Hashtable();
        theParams.put("agentName", inAgentName);
        if (inOldPosition != null) {
            theParams.put("from", inOldPosition);
        }
        if (inNewPosition != null) {
            theParams.put("to", inNewPosition);
        }
        final String theTeam = mModel.getAgentTeamAsString(inAgentName);
        if (theTeam != null) {
            theParams.put("team", theTeam);
        }
        synchronized (mBarRegulars) {
            final Iterator theAgentsIter = mBarRegulars.iterator();
            while (theAgentsIter.hasNext()) {
                final Agent theAgent = (Agent) theAgentsIter.next();
                if (!theAgent.getAgentName().equals(inAgentName)) {
                    try {
                        theAgent.executeAsync("agentEnvironmentDataUpdated", theParams);
                    } catch (AgentException anException) {
                        anException.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * Indique � un agent qu'il s'est d�plac�.
     * 
     * @param inAgentName		agent qui a boug�.
     * @param inOldPosition	ancienne position de l'agent ou <code>null</code>.
     * @param inNewPosition	nouvelle position de l'agent ou <code>null</code>.
     */
    private void informAgentOfMove(String inAgentName, String inOldPosition, String inNewPosition) {
        final Map theParams = new Hashtable();
        theParams.put("name", getAgentName());
        theParams.put("class", getAgentClass());
        if (inOldPosition != null) {
            theParams.put("from", inOldPosition);
        }
        if (inNewPosition != null) {
            theParams.put("to", inNewPosition);
            final String theTeam = mModel.getAgentTeamAsString(inAgentName);
            if (theTeam != null) {
                theParams.put("team", theTeam);
            }
        }
        Agent theTargetAgent = null;
        synchronized (mBarRegulars) {
            final Iterator theAgentsIter = mBarRegulars.iterator();
            while (theAgentsIter.hasNext()) {
                final Agent theAgent = (Agent) theAgentsIter.next();
                if (theAgent.getAgentName().equals(inAgentName)) {
                    theTargetAgent = theAgent;
                    break;
                }
            }
        }
        if (theTargetAgent != null) {
            try {
                theTargetAgent.executeAsync("positionChanged", theParams);
            } catch (AgentException anException) {
                anException.printStackTrace();
            }
        }
    }

    /**
     * Envoie � un agent des informations sur un autre agent.
     * 
     * @param inRecipient		agent � qui envoyer les informations.
     * @param inAgentName		agent dont on envoie les informations.
     */
    private void sendAgentEnvironmentData(Agent inRecipient, String inAgentName) {
        final Map theParams = new Hashtable();
        theParams.put("agentName", inAgentName);
        final String thePosition = mModel.getAgentPositionAsString(inAgentName);
        theParams.put("to", thePosition);
        final String theTeam = mModel.getAgentTeamAsString(inAgentName);
        if (theTeam != null) {
            theParams.put("team", theTeam);
        }
        try {
            inRecipient.executeAsync("agentEnvironmentDataUpdated", theParams);
        } catch (AgentException anException) {
            anException.printStackTrace();
        }
    }

    /**
     * Supprime un agent de la liste des agents. Pr�vient l'application.
     * 
     * @param inPlayer
     *            nom de l'habitu� � enlever de la liste.
     */
    private void unregisterBarRegular(Player inPlayer) {
        final String theRegularName = inPlayer.getAgentName();
        final String theOldPosition = mModel.getAgentPositionAsString(theRegularName);
        mModel.removeAgent(theRegularName);
        propagateAgentMove(theRegularName, theOldPosition, null);
        synchronized (mBarRegulars) {
            mBarRegulars.remove(inPlayer);
        }
        if (mApplication != null) {
            mApplication.playerLeft(theRegularName);
        }
    }
}
