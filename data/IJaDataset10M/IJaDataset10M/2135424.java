package net.sourceforge.fluxion.pussycat.svg;

import org.semanticweb.owl.model.OWLIndividual;
import net.sourceforge.fluxion.pussycat.entity.impl.PussycatMarkerEntity;
import net.sourceforge.fluxion.pussycat.util.exceptions.PussycatException;
import java.util.Set;

/**
 * The PussycatMap class
 * Defines a map that can be collapsed and expanded
 *
 * @author Rob Davey
 */
public interface PussycatMap extends PussycatSVGEntity<OWLIndividual> {

    public String buildCollapsedMap() throws PussycatException;

    public String buildExpandedMap() throws PussycatException;

    public String buildVisibleMarkers(double mapStart, double mapEnd, boolean componentText) throws PussycatException;

    public void updateMultipliers(double start, double end);

    public void updateMapScrollBounds(double start, double end);

    public int getXOffset();

    public void setXOffset(int offset);

    public int getYOffset();

    public void setYOffset(int offset);

    public double getHeight();

    public void setHeight(double height);

    public boolean isCollapsed();

    public void setCollapsed(boolean collapsed);

    public Set<PussycatMarkerEntity> getMarkers();
}
