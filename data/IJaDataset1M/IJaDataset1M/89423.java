package fr.amille.animebrowser.model.serie;

import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import fr.amille.animebrowser.model.exception.SerieException;
import fr.amille.animebrowser.model.serie.listener.SerieManagerListener;

/**
 * 
 * @author amille
 * 
 */
public final class SerieManager {

    public static SerieManager getInstance() {
        if (SerieManager.instance == null) {
            return new SerieManager();
        } else {
            return SerieManager.instance;
        }
    }

    private final Vector<SerieManagerListener> listeners = new Vector<SerieManagerListener>();

    private final Vector<Serie> series = new Vector<Serie>();

    private static SerieManager instance;

    private final String serieTitlesPattern = "[^,]+";

    private final String serieNumbersPattern = "[0-9,]+|[0-9]+\\-[0-9,]+";

    private SerieManager() {
        SerieManager.instance = this;
    }

    public void addSerieManagerListener(final SerieManagerListener o) {
        this.listeners.add(o);
    }

    public void clearAllSerieListeners() {
        for (final Serie serie : this.series) {
            serie.clearAllSerieListeners();
        }
        this.listeners.clear();
    }

    public Serie createSerie(final String serieTitle, final String serieNum, final String seriePicturePath) throws SerieException {
        final Vector<String> serieTitles = Serie.parseSerieTitles(serieTitle);
        if (this.isSerieNumbersValide(serieNum) && this.isSerieTitleValide(serieTitle) && (serieTitles != null) && !serieTitles.isEmpty() && !this.isSerieExist(serieTitles.get(0))) {
            final Serie serie = new Serie(serieTitle, serieNum, seriePicturePath);
            this.series.add(serie);
            this.fireAjoutSerie(serie);
            return serie;
        }
        return null;
    }

    /**
	 * Retourne une série à partir de son premier nom
	 * 
	 * @param nom
	 *            le nom de la série recherché
	 * @return la Serie recherché, sinon null
	 */
    public Serie findSerieByName(final String nom) {
        for (final Serie serie : this.series) {
            if (serie.getFirstSerieTitle().equalsIgnoreCase(nom) || serie.getSerieTitlesString().equalsIgnoreCase(nom)) {
                return serie;
            }
        }
        return null;
    }

    private void fireAjoutSerie(final Serie serie) {
        for (int i = 0; i < this.listeners.size(); i++) {
            this.listeners.get(i).ajoutSerie(serie);
        }
    }

    private void fireRemoveSerie(final String s) {
        for (int i = 0; i < this.listeners.size(); i++) {
            this.listeners.get(i).removeSerie(s);
        }
    }

    /**
	 * @return the serieTitlesPattern
	 */
    public String getSerieTitlesPattern() {
        return this.serieTitlesPattern;
    }

    /**
	 * @return the serieNumbersPattern
	 */
    public String getSerieNumbersPattern() {
        return this.serieNumbersPattern;
    }

    public Vector<Serie> getSeries() {
        return this.series;
    }

    public boolean isSerieExist(final String name) {
        for (final Serie serie : this.series) {
            final String firstSerieTitle = serie.getFirstSerieTitle();
            if ((firstSerieTitle != null) && firstSerieTitle.equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    public boolean isSerieNumbersValide(final String numbersToTest) {
        if ((numbersToTest != null) && (numbersToTest.length() > 0)) {
            final Pattern p = Pattern.compile(this.getSerieNumbersPattern());
            final Matcher matcher = p.matcher(numbersToTest);
            return matcher.matches();
        }
        return false;
    }

    public boolean isSerieTitleValide(final String titlesToTest) {
        if ((titlesToTest != null) && (titlesToTest.length() > 0)) {
            final String stringToTest = titlesToTest.replaceAll("\\s", "");
            final Pattern p = Pattern.compile(this.getSerieTitlesPattern());
            final Matcher matcher = p.matcher(stringToTest);
            return matcher.find();
        }
        return false;
    }

    public void supSerie(final String name) {
        for (final Serie serie : this.series) {
            if (serie.getFirstSerieTitle().equalsIgnoreCase(name)) {
                this.series.remove(serie);
                this.fireRemoveSerie(name);
                return;
            }
        }
    }

    public Serie updateSerie(final Serie serieToUpdate) throws SerieException {
        if (serieToUpdate != null) {
            final String serieTitles = serieToUpdate.getSerieTitlesString();
            final String serieNum = serieToUpdate.getEpisodeMissingNumbersString();
            final String firstSerieTitle = serieToUpdate.getFirstSerieTitle();
            final Serie serie = this.findSerieByName(firstSerieTitle);
            if ((serieTitles != null) && (serieNum != null) && (serieTitles.length() > 0) && (serieNum.length() > 0) && (serie != null)) {
                this.updateSerieAttributes(serie, serieTitles, serieNum);
                return serie;
            }
        }
        return null;
    }

    public void updateSerieAttributes(final Serie serie, final String serieTitles, final String serieNumbers) throws SerieException {
        if (serie != null) {
            serie.clearLists();
            serie.constructSerieAttributs(serieTitles, serieNumbers, serie.getSeriePicturePath());
        }
    }
}
