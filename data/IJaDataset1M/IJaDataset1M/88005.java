package rgzm.util.log;

import rgzm.bean.Betriebsstelle;
import rgzm.bean.Gleis;
import rgzm.bean.Nachbar;
import rgzm.bean.Zug;

/**
 * Class produces special formatted logging messages for the TrainLogger
 */
public final class EbuefLogger implements TrainLoggingMessages {

    private final TrainLogger oLogger;

    private static final String STATION = Betriebsstelle.BETRIEBSSTELLE.getName();

    public EbuefLogger(final TrainLogger logger) {
        oLogger = logger;
    }

    public String create(final Zug zug, final Gleis gleis) {
        return formatMessage(zug.getZugNr() + ", " + STATION + ".gl" + gleis.getNr() + " neu");
    }

    public String insert(final Zug zug, final Gleis gleis) {
        final StringBuilder sb = new StringBuilder();
        sb.append(zug.getZugNr());
        sb.append(", aus: ");
        sb.append(zug.getVon());
        sb.append(" nach: ");
        sb.append(zug.getNach());
        sb.append(", in ");
        sb.append(STATION);
        sb.append(" Gleis: ");
        sb.append(gleis.getNr());
        sb.append(" aus Fahrplan");
        return formatMessage(sb.toString());
    }

    public String departure(final Zug zug, final Gleis gleis, final Nachbar nachbar) {
        String glNr = gleis != null ? gleis.getNr() : "--";
        return formatMessage(zug.getZugNr() + ", " + STATION + ".gl" + glNr + " nach " + nachbar.getName());
    }

    public String arrive(final Zug zug, final Gleis gleis, final Nachbar nachbar) {
        String gleisNr = gleis != null ? gleis.getNr() : "-Durchfahrt-";
        return formatMessage(zug.getZugNr() + ", " + nachbar.getName() + " nach " + STATION + ".gl" + gleisNr);
    }

    public String revoke(final Zug zug, final Gleis gleis) {
        return formatMessage(zug.getZugNr() + ", " + STATION + ".gl" + gleis.getNr() + " aufgelöst");
    }

    @Override
    public String revoke(final Zug zug, final Gleis gleis, final Nachbar nachbar) {
        String gleisNr = gleis != null ? gleis.getNr() : "-Durchfahrt-";
        return formatMessage(zug.getZugNr() + ", " + nachbar.getName() + " nach " + STATION + ".gl" + gleisNr + " zurückgenommen");
    }

    public String crossing(final Zug zug, final Nachbar nachbarFrom, final Nachbar nachbarTo) {
        return formatMessage(zug.getZugNr() + ", aus " + nachbarFrom.getName() + " Durchfahrt " + STATION + " nach " + nachbarTo.getName());
    }

    public String moveToTrack(final Zug zug, final Gleis from, final Gleis target) {
        return formatMessage(zug.getZugNr() + " in Betriebsstelle " + STATION + ", von Gleis " + from.getNr() + " nach Gleis " + target.getNr() + " umgesetzt");
    }

    private String formatMessage(final String message) {
        return oLogger.getModelTime() + " ZUG - " + message;
    }
}
