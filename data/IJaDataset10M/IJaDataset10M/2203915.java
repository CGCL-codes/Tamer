package fr.lip6.sma.simulacion.server;

import java.util.Hashtable;

/**
 * Classe pour l'interface publique du serveur.
 * L'int�r�t de cette classe est qu'elle limite les m�thodes qui sont publi�es.
 * Inutile d'�tre compl�tement parano, mais ceci permet d'�viter de publier des
 * m�thodes qui ne doivent pas �tre appel�es par le r�seau.
 *
 * @author Paul Guyot <paulguyot@acm.org>
 * @version $Revision: 3 $
 *
 * @see "aucun test d�fini."
 */
public final class Handler {

    /**
	 * R�f�rence sur l'agent.
	 */
    private final Agent mLocalAgent;

    /**
	 * Constructeur � partir de l'agent local.
	 *
	 * @param inLocalAgent	mod�le associ� � ce gestionnaire
	 */
    public Handler(Agent inLocalAgent) {
        mLocalAgent = inLocalAgent;
    }

    /**
	 * Ex�cute une op�ration avec l'avatar.
	 *
	 * @param inOperationName		nom de l'op�ration � ex�cuter.
	 * @param inParams				param�tres de l'op�ration.
	 * @return	le r�sultat de l'op�ration (sous forme de cha�ne).
	 * @throws	Exception si une erreur est survenue.
	 */
    public synchronized String execute(String inOperationName, Hashtable inParams) throws Exception {
        return mLocalAgent.execute(inOperationName, inParams);
    }
}
