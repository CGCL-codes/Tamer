package apollo.gui.detailviewers.exonviewer;

import java.awt.*;
import apollo.datamodel.*;

public interface BaseRenderer {

    public void paintNotify();

    public Component getBaseRendererComponent(char base, int pos, int tier, SequenceI seq);
}
