package metso.paradigma.portal.business.manager;

import java.io.OutputStream;
import java.util.List;
import metso.dal.DalException;
import metso.paradigma.core.business.model.Operatore;
import metso.paradigma.core.business.model.PianoTurno;
import metso.paradigma.core.business.model.Reparto;
import metso.paradigma.core.business.model.Turno;

public interface IPdfPrinterManager {

    /**
	 * Crea un outputstream per la stampa del pdf dell'elenco turni.
	 * @param turni lista di turni
	 * @return l'outputstream per la stampa del pdf
	 * @throws DalException
	 */
    public abstract OutputStream printElencoTurniTable(List<Turno> turni) throws DalException;

    /**
	 * Crea un outputstream per la stampa del pdf della scheda reparto.
	 * @param reparto il reparto di cui stampare la scheda
	 * @return l'output stream
	 * @throws DalException
	 */
    public abstract OutputStream printSchedaRepartoTable(Reparto reparto) throws DalException;

    /**
	 * Crea un outputstream per la stampa della scheda operatore.
	 * @param operatore l'operatore di cui si deve stampare la scheda
	 * @return l'outputstream
	 * @throws DalException 
	 */
    public abstract OutputStream printSchedaOperatoreTable(Operatore operatore) throws DalException;

    /**
	 * Crea un outputstream per la stampa di un piano turni.
	 * @param pianoturno il piano turni da stampare
	 * @return l'outputstream
	 * @throws DalException 
	 */
    public abstract OutputStream printSchedaPianoTurniTable(PianoTurno pianoturno) throws DalException;
}
