package net.sourceforge.plantuml.sequencediagram;

import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.graphic.HtmlColor;

public class GroupingLeaf extends Grouping {

    private final GroupingStart start;

    private final HtmlColor backColorGeneral;

    public GroupingLeaf(String title, String comment, GroupingType type, HtmlColor backColorGeneral, HtmlColor backColorElement, GroupingStart start) {
        super(title, comment, type, backColorElement);
        if (start == null) {
            throw new IllegalArgumentException();
        }
        this.backColorGeneral = backColorGeneral;
        this.start = start;
        start.addChildren(this);
    }

    public Grouping getJustBefore() {
        final int idx = start.getChildren().indexOf(this);
        if (idx == -1) {
            throw new IllegalStateException();
        }
        if (idx == 0) {
            return start;
        }
        return start.getChildren().get(idx - 1);
    }

    @Override
    public int getLevel() {
        return start.getLevel();
    }

    @Override
    public final HtmlColor getBackColorGeneral() {
        if (backColorGeneral == null) {
            return start.getBackColorGeneral();
        }
        return backColorGeneral;
    }

    public boolean dealWith(Participant someone) {
        return false;
    }

    public Url getUrl() {
        return null;
    }

    @Override
    public boolean isParallel() {
        return start.isParallel();
    }
}
