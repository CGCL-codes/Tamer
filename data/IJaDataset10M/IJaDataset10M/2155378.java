package pl.otros.logview.accept;

import pl.otros.logview.LogData;
import pl.otros.logview.gui.HasIcon;
import pl.otros.logview.gui.renderers.LevelRenderer;
import pl.otros.logview.gui.util.CompoundIcon;
import javax.swing.*;
import java.util.ArrayList;
import java.util.logging.Level;

public class LevelLowerAcceptContition extends AbstractAcceptContidion implements HasIcon {

    protected Level level;

    protected Icon icon;

    private int levelIntValue;

    public LevelLowerAcceptContition(Level level) {
        super();
        this.level = level;
        levelIntValue = level.intValue();
        this.name = String.format("Level <%s", level.getName());
        this.description = String.format("Level of log event is lower than %s", level.getName());
        createIcon();
    }

    private void createIcon() {
        Level[] levels = new Level[] { Level.FINEST, Level.FINER, Level.FINE, Level.CONFIG, Level.INFO, Level.WARNING, Level.SEVERE };
        ArrayList<Icon> iconsList = new ArrayList<Icon>();
        for (int i = 0; i < levels.length; i++) {
            if (levels[i].intValue() < levelIntValue) {
                iconsList.add(LevelRenderer.getIconByLevel(levels[i]));
            }
        }
        icon = new CompoundIcon(iconsList);
    }

    @Override
    public boolean accept(LogData data) {
        return data.getLevel() != null && data.getLevel().intValue() < levelIntValue;
    }

    @Override
    public Icon getIcon() {
        return icon;
    }
}
