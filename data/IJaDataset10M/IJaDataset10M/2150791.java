package cz.vse.gebz.logika;

import java.util.Collection;
import java.util.Collections;
import cz.vse.gebz.AtribalniVyrok;

public class LogickyLiteral implements ILogickyVyrok {

    private final boolean negovany;

    private final AtribalniVyrok vyrok;

    public LogickyLiteral(boolean negovany, AtribalniVyrok av) {
        this.negovany = negovany;
        this.vyrok = av;
    }

    @Override
    public boolean isNegovany() {
        return negovany;
    }

    public AtribalniVyrok getVyrok() {
        return vyrok;
    }

    public Collection<ILogickyVyrok> getCleny() {
        return Collections.emptyList();
    }

    @Override
    public ILogickyVyrok asNegovany() {
        return new LogickyLiteral(!negovany, vyrok);
    }

    @Override
    public LogickaDisjunkce asDisjunkce() {
        return new LogickaDisjunkce(false, new LogickyLiteral(negovany, vyrok));
    }

    @Override
    public LogickaKonjunkce asKonjunkce() {
        return new LogickaKonjunkce(false, new LogickyLiteral(negovany, vyrok));
    }

    @Override
    public LogickaDisjunkce asNormovanaDisjunkce() {
        return new LogickaDisjunkce(false, new LogickyLiteral(negovany, vyrok));
    }

    @Override
    public String toString() {
        return (isNegovany() ? "^" : "") + vyrok.getId();
    }

    public int hashCode() {
        int result = 17;
        result = 37 * result + (negovany ? 0 : 1);
        result = 37 * result + ((vyrok.getId() == null) ? 0 : vyrok.getId().hashCode());
        return result;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof LogickyLiteral)) {
            return false;
        }
        LogickyLiteral e = (LogickyLiteral) o;
        return negovany == e.negovany && (vyrok.getId() == null ? e.vyrok.getId() == null : vyrok.getId().equals(e.vyrok.getId()));
    }
}
