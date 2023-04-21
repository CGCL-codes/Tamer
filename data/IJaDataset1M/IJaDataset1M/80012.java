package xreal.client.ui.menu;

import xreal.Color;
import xreal.client.renderer.Renderer;
import xreal.client.ui.Component;
import xreal.client.ui.HorizontalAlignment;
import xreal.client.ui.Image;
import xreal.client.ui.Label;
import xreal.client.ui.Rectangle;
import xreal.client.ui.StackPanel;
import xreal.client.ui.VerticalAlignment;

/**
 * @author Robert Beckebans
 */
public class NavigationButton extends StackPanel {

    private Image image;

    private Label label;

    public NavigationButton(String imageName, String labelText) {
        orientation = Orientation.Horizontal;
        horizontalAlignment = HorizontalAlignment.Left;
        verticalAlignment = VerticalAlignment.Top;
        image = new Image(imageName);
        image.width = 24;
        image.height = 24;
        image.margin.set(2);
        image.verticalAlignment = VerticalAlignment.Center;
        Component canvas = new Component();
        canvas.addChild(image);
        label = new Label(labelText);
        label.height = 28;
        label.textBlock.font = Renderer.registerFont("fonts/FreeSansBold.ttf", 48);
        label.textBlock.fontSize = 18;
        label.textBlock.color.set(Color.MdGrey);
        addChild(canvas);
        addChild(label);
    }
}
