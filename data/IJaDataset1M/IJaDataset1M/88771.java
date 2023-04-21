package org.doclet.gwt.client.jsni;

import org.doclet.gwt.client.SamplePanel;
import org.doclet.gwt.client.Application.Sample;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class ColorSelectorPanel extends SamplePanel implements KeyboardListener {

    private HorizontalPanel workPanel = new HorizontalPanel();

    private Grid grid = new Grid(3, 3);

    private TextBox redText = new TextBox();

    private TextBox greenText = new TextBox();

    private TextBox blueText = new TextBox();

    private Element outerDiv = DOM.createDiv();

    private Element colorDiv = DOM.createDiv();

    private Element colorText = DOM.createElement("P");

    private Element colorBox = DOM.createElement("P");

    public ColorSelectorPanel() {
        HorizontalPanel infoPanel = new HorizontalPanel();
        infoPanel.add(new HTML("<div class='infoProse'>Select a color by providing the red, green and blue values. The selected color will be applied to the box on the screen and the hex value of the color will be displayed below it with an element sliding up and then sliding down to display the value. Check it out by typing in the color components!</div>"));
        grid.setText(0, 0, "Red");
        grid.setText(1, 0, "Green");
        grid.setText(2, 0, "Blue");
        redText.setStyleName("ricoText");
        redText.setText("0");
        redText.addKeyboardListener(this);
        grid.setWidget(0, 1, redText);
        greenText.setStyleName("ricoText");
        greenText.addKeyboardListener(this);
        greenText.setText("0");
        grid.setWidget(1, 1, greenText);
        blueText.setStyleName("ricoText");
        blueText.addKeyboardListener(this);
        blueText.setText("0");
        grid.setWidget(2, 1, blueText);
        grid.setText(0, 2, "(0-255)");
        grid.setText(1, 2, "(0-255)");
        grid.setText(2, 2, "(0-255)");
        grid.setStyleName("ricoGrid");
        DOM.setAttribute(colorBox, "className", "ricoColorBox");
        DOM.setAttribute(colorBox, "id", "colorBox");
        DOM.setInnerText(colorBox, "");
        Rico.color(colorBox, 0, 0, 0);
        workPanel.add(grid);
        DOM.appendChild(workPanel.getElement(), colorBox);
        DOM.setAttribute(outerDiv, "className", "heightBox");
        DOM.setAttribute(colorDiv, "id", "colorDiv");
        DOM.setAttribute(colorText, "className", "text");
        DOM.appendChild(colorDiv, colorText);
        DOM.appendChild(outerDiv, colorDiv);
        DOM.appendChild(workPanel.getElement(), outerDiv);
        DeferredCommand.add(new Command() {

            public void execute() {
                MooFx.height(DOM.getElementById("colorDiv"), "duration:500");
                DOM.setInnerText(colorText, Rico.getColorAsHex(colorBox));
            }
        });
        DockPanel workPane = new DockPanel();
        workPane.add(infoPanel, DockPanel.NORTH);
        workPane.add(workPanel, DockPanel.CENTER);
        workPane.setCellHeight(workPanel, "100%");
        workPane.setCellWidth(workPanel, "100%");
        initWidget(workPane);
    }

    public void onShow() {
    }

    public void onKeyDown(Widget sender, char keyCode, int modifiers) {
    }

    public void onKeyPress(Widget sender, char keyCode, int modifiers) {
        MooFx.toggleHeight(DOM.getElementById("colorDiv"));
        Timer t = new Timer() {

            public void run() {
                if ((redText.getText().length() > 0) && (greenText.getText().length() > 0) && (blueText.getText().length() > 0)) {
                    Rico.color(colorBox, Integer.parseInt(redText.getText()), Integer.parseInt(greenText.getText()), Integer.parseInt(blueText.getText()));
                    DOM.setInnerText(colorText, Rico.getColorAsHex(colorBox));
                    MooFx.toggleHeight(DOM.getElementById("colorDiv"));
                }
            }
        };
        t.schedule(500);
    }

    public void onKeyUp(Widget sender, char keyCode, int modifiers) {
    }

    public static Sample init() {
        return new Sample("Color Selector", "Color Selector.") {

            public SamplePanel createInstance() {
                return new ColorSelectorPanel();
            }
        };
    }
}
