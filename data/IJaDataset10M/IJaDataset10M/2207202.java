package net.sphene.gwt.widgets.slider;

import java.util.Iterator;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * A very simple composition containing a slider, a text field containing the
 * value and a +/- button to change the value.
 * 
 * @author kahless
 */
public class SliderWithSpinner extends Composite implements ChangeListener, HasWidgets {

    HorizontalPanel root;

    Slider slider;

    private CustomButton incrValue;

    private TextBox textBox;

    private CustomButton decrValue;

    private ValueChanger valueChanger;

    private Integer fixedDecimals = null;

    private int repeatFirstDelay = 500;

    private int repeatDelay = 100;

    private int repeatSteps = 1;

    public SliderWithSpinner() {
        this(new Slider());
    }

    public SliderWithSpinner(Slider slider_) {
        this.slider = slider_;
        root = new HorizontalPanel();
        root.add(slider);
        HorizontalPanel spinner = new HorizontalPanel();
        spinner.add(decrValue = new CustomButton(false));
        spinner.add(textBox = new TextBox());
        spinner.add(incrValue = new CustomButton(true));
        root.add(spinner);
        initWidget(root);
        textBox.addStyleName("sph-SliderWithSpinner");
        textBox.addChangeListener(new ChangeListener() {

            public void onChange(Widget sender) {
                String str = textBox.getText();
                try {
                    slider.setValue(Double.parseDouble(str));
                } catch (NumberFormatException e) {
                }
                updateDisplayedValue();
            }
        });
        slider.addChangeListener(this);
        updateDisplayedValue();
    }

    /**
	 * allows to round the output to a number of places
	 * 
	 */
    public void setFixedDecimal(int fixedDecimalPlaces) {
        this.fixedDecimals = new Integer(fixedDecimalPlaces);
    }

    private void updateDisplayedValue() {
        if (fixedDecimals == null) textBox.setText(Double.toString(slider.getValue())); else textBox.setText(toFixed(slider.getValue(), fixedDecimals.intValue()));
    }

    private native String toFixed(double value, int places);

    public void onChange(Widget sender) {
        updateDisplayedValue();
    }

    public void onMouseUp(Widget sender, int x, int y) {
        if (valueChanger != null) {
            valueChanger.cancel();
            valueChanger = null;
        }
    }

    public class CustomButton extends Widget {

        private boolean isIncr;

        public CustomButton(boolean isIncr) {
            this.isIncr = isIncr;
            setElement(DOM.createDiv());
            sinkEvents(Event.ONMOUSEDOWN | Event.ONMOUSEUP | Event.ONDBLCLICK);
            setStyleName("sph-SliderWithSpinner-Button");
            addStyleName("sph-SliderWithSpinner-Button-" + (isIncr ? "incr" : "decr"));
        }

        public void onBrowserEvent(Event event) {
            switch(DOM.eventGetType(event)) {
                case Event.ONMOUSEDOWN:
                    DOM.setCapture(getElement());
                    if (valueChanger != null) {
                        valueChanger.cancel();
                        valueChanger = null;
                    }
                    int steps = (isIncr ? repeatSteps : -1 * repeatSteps);
                    slider.incrementValue(steps);
                    valueChanger = new ValueChanger(steps);
                    valueChanger.schedule(repeatFirstDelay);
                    break;
                case Event.ONMOUSEUP:
                    DOM.releaseCapture(getElement());
                    if (valueChanger != null) {
                        valueChanger.cancel();
                        valueChanger = null;
                    }
                    break;
            }
        }
    }

    public class ValueChanger extends Timer {

        private int steps;

        boolean firstRun = true;

        public ValueChanger(int steps) {
            this.steps = steps;
        }

        public void run() {
            if (valueChanger == null) return;
            slider.incrementValue(steps);
            if (firstRun) {
                firstRun = false;
                this.scheduleRepeating(repeatDelay);
            }
        }
    }

    /**
	 * Sets the amount of time (ms) to wait before the first repeat 
	 * after a user clicks on the +/-
	 * 
	 * @param repeatFirstDelay amount of time (ms) to wait
	 * @see #setRepeatSteps(int) for more detailed info.
	 */
    public void setRepeatFirstDelay(int repeatFirstDelay) {
        this.repeatFirstDelay = repeatFirstDelay;
    }

    /**
	 * Sets the amount of steps to increment when a user clicks on 
	 * +/- - this value will be handed over to {@link Slider#incrementValue(int)}<br>
	 * <br>
	 * The repeat works as follows:<br>
	 * <ul>
	 *   <li>User clicks on +/- and holds down the mouse button</li>
	 *   <li>Slider waits for {@link #setRepeatFirstDelay(int)} ms.</li>
	 *   <li>Slider increments the value by {@link #setRepeatSteps(int)} and 
	 *   waits for {@link #setRepeatDelay(int)} ms as long as the user holds 
	 *   down the mouse button.</li>
	 * </ul> 
	 * 
	 * @param repeatSteps the amount of steps to increment the value.
	 */
    public void setRepeatSteps(int repeatSteps) {
        this.repeatSteps = repeatSteps;
    }

    /**
	 * Sets the amount of time between two increments when
	 * user clicks on +/-
	 * 
	 * @param repeatDelay time (ms) to wait between increments.
	 * @see #setRepeatSteps(int) for more detailed info.
	 */
    public void setRepeatDelay(int repeatDelay) {
        this.repeatDelay = repeatDelay;
    }

    public void add(Widget w) {
    }

    public void clear() {
        root.clear();
    }

    public Iterator iterator() {
        return root.iterator();
    }

    public boolean remove(Widget w) {
        return false;
    }

    public Slider getSlider() {
        return slider;
    }
}
