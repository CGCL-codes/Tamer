package de.bwb.ekp.entities;

import java.util.Arrays;
import java.util.List;
import org.jboss.seam.annotations.Name;
import de.bwb.ekp.interceptors.MeasureCalls;

@MeasureCalls
@Name("ausschreibungList")
public class AusschreibungList extends AbstractAusschreibungList {

    private static final long serialVersionUID = 1L;

    private static final String[] RESTRICTIONS = { "ausschreibung.ausschreibungsart = #{ausschreibungList.ausschreibung.ausschreibungsart}", "ausschreibung.bewerbungsende >= #{ausschreibungList.ausschreibung.bewerbungsende}", "ausschreibung.stelle.id = #{ausschreibungList.stelleId}", "ausschreibung.abgeschlossen = #{ausschreibungList.ausschreibung.abgeschlossen}" };

    public AusschreibungList() {
        this.setOrder("bewerbungsende asc");
    }

    @Override
    public List<String> getRestrictions() {
        return Arrays.asList(RESTRICTIONS);
    }
}
