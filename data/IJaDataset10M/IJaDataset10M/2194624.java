package uk.ac.lkl.client;

import java.util.ArrayList;
import java.util.List;
import uk.ac.lkl.common.util.config.MiGenConfiguration;
import uk.ac.lkl.common.util.value.IntegerValue;
import uk.ac.lkl.migen.system.expresser.model.ExpresserModel;
import uk.ac.lkl.migen.system.expresser.model.tiednumber.TiedNumberExpression;
import uk.ac.lkl.migen.system.expresser.model.tiednumber.TiedNumberTimer;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.FocusPanel;

/**
 * Implements an animation button for thumb nails
 * 
 * @author Ken Kahn
 *
 */
public class ThumbNailAnimationButton extends FocusPanel {

    protected ArrayList<TiedNumberTimer> timers = new ArrayList<TiedNumberTimer>();

    protected ArrayList<TiedNumberExpression<IntegerValue>> unlockedTiedNumbers;

    protected Timer updateDisplayTimer;

    protected boolean playing = false;

    protected List<ShapeView> shapeViews;

    public ThumbNailAnimationButton(final ArrayList<TiedNumberExpression<IntegerValue>> unlockedTiedNumbers, final List<ShapeView> shapeViews, final ExpresserModel model) {
        super();
        this.unlockedTiedNumbers = unlockedTiedNumbers;
        this.shapeViews = shapeViews;
        model.maintainGlue();
        updateDisplayTimer = new Timer() {

            @Override
            public void run() {
                if (MiGenConfiguration.isEnableVelcro() || MiGenConfiguration.isEnableAutoVelcro()) {
                    model.maintainGlue();
                }
                for (ShapeView shapeView : shapeViews) {
                    shapeView.updateDisplay();
                }
            }
        };
        ClickHandler clickHandler = new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                ThumbNailAnimationButton.this.setVisible(false);
                for (TiedNumberExpression<IntegerValue> tiedNumber : unlockedTiedNumbers) {
                    TiedNumberTimer timer = tiedNumber.createPlayTimer();
                    timers.add(timer);
                    tiedNumber.setPlaying(true);
                }
                updateDisplayTimer.scheduleRepeating(MiGenConfiguration.getTiedNumberPlayDelay());
            }
        };
        addClickHandler(clickHandler);
    }

    public void toggle() {
        if (playing) {
            playing = false;
            for (TiedNumberExpression<?> animatingTiedNumber : unlockedTiedNumbers) {
                animatingTiedNumber.setPlaying(false);
            }
            updateDisplayTimer.cancel();
            setVisible(true);
        } else {
            playing = true;
            for (TiedNumberExpression<IntegerValue> tiedNumber : unlockedTiedNumbers) {
                TiedNumberTimer timer = tiedNumber.createPlayTimer();
                timers.add(timer);
                tiedNumber.setPlaying(true);
            }
            updateDisplayTimer.scheduleRepeating(MiGenConfiguration.getTiedNumberPlayDelay());
        }
    }
}
