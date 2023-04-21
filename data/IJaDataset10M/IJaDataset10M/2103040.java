package net.sourceforge.eclipsetrader.charts.events;

import net.sourceforge.eclipsetrader.charts.Indicator;
import net.sourceforge.eclipsetrader.charts.ObjectPlugin;
import net.sourceforge.eclipsetrader.charts.Plot;
import org.eclipse.swt.events.TypedEvent;
import org.eclipse.swt.widgets.Event;

public class PlotSelectionEvent extends TypedEvent {

    static final long serialVersionUID = 7634624784732351271L;

    public Plot plot;

    public Indicator indicator;

    public ObjectPlugin object;

    public PlotSelectionEvent(Event e) {
        super(e);
    }
}
