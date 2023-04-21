package pl.otros.logview.gui.markers;

import pl.otros.logview.LogData;
import pl.otros.logview.MarkerColors;

public class ErrorMarker extends AbstractAutomaticMarker {

    private static final String NAME = "'Error' in message";

    private static final String DESCRIPTION = "Marks/unmarks logs with 'error' in message";

    private static final String[] GROUPS = new String[] { "", "Errors" };

    public ErrorMarker() {
        super(NAME, DESCRIPTION, MarkerColors.Red, GROUPS);
    }

    @Override
    public boolean toMark(LogData data) {
        String m = data.getMessage().toLowerCase();
        return m.contains("error");
    }
}
