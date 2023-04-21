package fr.ird.animat;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Une simulation qui peut �tre ex�cut�e sur une machine distante. Chaque simulation contient
 * un et un seul {@linkplain Environment environnement}.
 *
 * @version $Id: Simulation.java,v 1.3 2004/11/10 08:41:31 desruisseaux Exp $
 * @author Martin Desruisseaux
 */
public interface Simulation extends Remote {

    /**
     * Groupe de threads utilis�s pour la simulation. Les impl�mentations qui construisent
     * de nouveau thread (par exemple lors de l'appel de la m�thode {@link #start}) devraient
     * placer leurs threads dans ce groupe.
     */
    ThreadGroup THREAD_GROUP = new ThreadGroup("Animat simulation");

    /**
     * Retourne le nom de cette simulation.
     *
     * @throws RemoteException Si cette m�thode devait �tre ex�cut�e sur une machine distante
     *         et que cette ex�cution a �chou�e.
     */
    String getName() throws RemoteException;

    /**
     * Lance la simulation dans un {@linkplain Thread thread} de basse priorit�. Si une
     * simulation est d�j� en cours, alors cette m�thode ne fait rien. Dans tous les cas,
     * cette m�thode retourne imm�diatement.
     *
     * @throws RemoteException Si cette m�thode devait �tre ex�cut�e sur une machine distante
     *         et que cette ex�cution a �chou�e.
     */
    void start() throws RemoteException;

    /**
     * Arr�te la simulation. Cette m�thode peut �tre utilis�e pour prendre une pause.
     * La simulation peut �tre {@linkplain #start red�marr�e} de nouveau apr�s avoir
     * �t� arr�t�e.
     *
     * @throws RemoteException Si cette m�thode devait �tre ex�cut�e sur une machine distante
     *         et que cette ex�cution a �chou�e.
     */
    void stop() throws RemoteException;

    /**
     * Retourne l'environnement de la simulation.
     *
     * @return L'environnement de la simulation.
     * @throws RemoteException Si cette m�thode devait �tre ex�cut�e sur une machine distante
     *         et que cette ex�cution a �chou�e.
     */
    Environment getEnvironment() throws RemoteException;

    /**
     * Retourne une propri�t� de la simulation. La liste des propri�t�s peut contenir des
     * informations suppl�mentaires qui ne figure pas parmis les autres m�thodes de cette
     * interface.
     *
     * @param  name Le nom de la propri�t�.
     * @return La valeur de la propri�t� sp�cifi�e, ou <code>null</code> si aucune
     *         propri�t� n'est d�finie pour le nom sp�cifi�e.
     * @throws RemoteException Si cette m�thode devait �tre ex�cut�e sur une machine distante
     *         et que cette ex�cution a �chou�e.
     */
    String getProperty(String name) throws RemoteException;
}
