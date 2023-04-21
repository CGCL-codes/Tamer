package scrum.client.project;

import ilarkesto.core.time.Date;
import ilarkesto.gwt.client.AWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class SprintSwitchIndicatorWidget extends AWidget {

    private Label label;

    @Override
    protected Widget onInitialization() {
        label = new Label("Estimated Sprint Switch");
        label.setStyleName("SprintBorderIndicatorWidget");
        return label;
    }

    public void updateLabel(int sprints, Date date) {
        initialize();
        String s = String.valueOf(sprints);
        if (sprints == 1) {
            s = "next";
        }
        label.setText("After " + s + " sprint" + (sprints < 2 ? "" : "s") + (", in " + date.getPeriodTo(Date.today()).abs().toShortestString()));
    }
}
