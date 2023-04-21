package ch.jester.system.api.ranking;

import java.util.Map;
import org.eclipse.core.runtime.IProgressMonitor;
import ch.jester.model.Category;
import ch.jester.model.Ranking;
import ch.jester.model.Round;
import ch.jester.model.Tournament;
import ch.jester.system.exceptions.NotAllResultsException;

/**
 * Interface für die Feinwertungen zur Berechnung
 * der Rangliste. Die verschiedenen Feinwertungen 
 * müssen dieses Interface implementieren.
 *
 */
public interface IRankingSystem {

    /**
	 * Berechnen der Rangliste
	 * @param tournament Turnier zu dem die Rangliste erstellt werden soll.
	 * @param pMonitor ProgrssMonitor
	 * @return Map mit den Ranglisten. Key ist die Category value Ranking (entweder instanz von FinalRanking oder IntermediateRanking)
	 * @throws NotAllResultsException Es fehlen noch Resultate um die Rangliste zu erstellen
	 */
    public Map<Category, Ranking> calculateRanking(Tournament tournament, IProgressMonitor pMonitor) throws NotAllResultsException;

    /**
	 * Berechnen der Rangliste in einer Kategorie
	 * @param category Kategorie zu der die Rangliste erstellt werden soll.
	 * @param pMonitor ProgrssMonitor
	 * @return Ranking (entweder instanz von FinalRanking oder IntermediateRanking)
	 * @throws NotAllResultsException Es fehlen noch Resultate um die Rangliste zu erstellen
	 */
    public Ranking calculateRanking(Category category, IProgressMonitor pMonitor) throws NotAllResultsException;

    /**
	 * Berechnen der Rangliste in einer Kategorie auf einer Runde
	 * @param category Kategorie zu der die Rangliste erstellt werden soll.
	 * @param pMonitor ProgrssMonitor
	 * @return Ranking (entweder instanz von FinalRanking oder IntermediateRanking)
	 * @throws NotAllResultsException Es fehlen noch Resultate um die Rangliste zu erstellen
	 */
    public Ranking calculateRanking(Category category, Round round, IProgressMonitor pMonitor) throws NotAllResultsException;
}
