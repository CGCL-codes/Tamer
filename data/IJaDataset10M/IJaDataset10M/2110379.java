package jorgan.gui.construct.layout;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import jorgan.disposition.Displayable;
import jorgan.gui.console.View;

public class SpreadVerticalLayout extends ViewLayout {

    private int y;

    private int height;

    private int count;

    @Override
    public boolean isAlign() {
        return false;
    }

    @Override
    protected void init(View<? extends Displayable> pressed, List<View<? extends Displayable>> views) {
        Collections.sort(views, new Comparator<View<? extends Displayable>>() {

            public int compare(View<? extends Displayable> view1, View<? extends Displayable> view2) {
                return view1.getY() - view2.getY();
            }
        });
        count = views.size();
        View<? extends Displayable> top = views.get(0);
        View<? extends Displayable> bottom = views.get(views.size() - 1);
        y = top.getY() + top.getHeight() / 2;
        height = bottom.getY() + bottom.getHeight() / 2 - y;
    }

    @Override
    protected void visit(View<? extends Displayable> view, int index) {
        changePosition(view, view.getX(), y + (height * index / (count - 1)) - view.getHeight() / 2);
    }
}
