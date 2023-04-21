package be.lassi.ui.widgets;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import be.lassi.domain.Level;
import be.lassi.domain.LevelListener;

public abstract class LevelDeltaIndicator extends JPanel {

    private final Level level1;

    private final Level level2;

    private LevelListener levelsListener;

    private int savedOffset;

    private Color levelColor = Color.red;

    protected LevelDeltaIndicator(final Level level1, final Level level2) {
        this.level1 = level1;
        this.level2 = level2;
        setBorder(BorderFactory.createEtchedBorder());
        setPreferredSize(getInitialSize());
        createLevelsListener();
        level1.add(levelsListener);
        level2.add(levelsListener);
    }

    private void createLevelsListener() {
        levelsListener = new LevelListener() {

            public void levelChanged() {
                int offset = offset();
                if (savedOffset != offset) {
                    savedOffset = offset;
                    repaint();
                }
            }
        };
    }

    protected abstract Dimension getInitialSize();

    protected Level getLevel1() {
        return level1;
    }

    protected Level getLevel2() {
        return level2;
    }

    protected Color getLevelColor() {
        return levelColor;
    }

    protected abstract int offset();

    protected abstract void paintBox(final Graphics g);

    @Override
    public void paintComponent(final Graphics g) {
        super.paintComponent(g);
        Color c = g.getColor();
        g.setColor(getLevelColor());
        paintBox(g);
        g.setColor(c);
    }

    public void setLevelColor(final Color c) {
        levelColor = c;
    }
}
