package br.com.miboco.patterns.strategy.buttons;

import br.com.miboco.patterns.observer.ButtonObservable;
import br.com.miboco.patterns.observer.Observer;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.VerticalPanel;

public class VerticalButtonComponent extends ButtonStrategyComponent implements Observer {

    private VerticalPanel panel;

    public VerticalButtonComponent(ButtonObservable observableVisible) {
        super(observableVisible);
        this.panel = new VerticalPanel();
        initWidget(panel);
    }

    /**
	 * add a new instance of button three times. This is default aprouch to a project.
	 * */
    public void addButtons(Image... img) {
        for (Image aImge : img) panel.add(new PushButton(aImge));
    }

    /**
	 * add a single new instance of button.
	 * */
    @Override
    public void add(PushButton pusshButton) {
        panel.add(pusshButton);
        panel.setBorderWidth(BORDER_WIDTH);
        panel.setSpacing(SPACING);
    }

    @Override
    public void update() {
        setVisible(!isVisible());
    }
}
