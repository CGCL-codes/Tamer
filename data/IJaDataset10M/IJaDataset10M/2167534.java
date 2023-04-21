package ie.blackoutscout.ui.stage.about;

import ie.blackoutscout.ui.common.stage.base.BaseStage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;

/**
 *
 * @author John
 */
public class AboutStage extends BaseStage {

    public AboutStage(Stage owner) {
        super(owner);
        initView();
    }

    private void initView() {
    }

    private class OkayButtonAction implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void unbindBeanFromControls() {
    }
}
