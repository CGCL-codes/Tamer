package org.nakedobjects.nos.client.dnd.view.help;

import org.nakedobjects.nof.core.util.AboutNakedObjects;
import org.nakedobjects.nos.client.dnd.Canvas;
import org.nakedobjects.nos.client.dnd.Click;
import org.nakedobjects.nos.client.dnd.NullContent;
import org.nakedobjects.nos.client.dnd.Toolkit;
import org.nakedobjects.nos.client.dnd.drawing.Color;
import org.nakedobjects.nos.client.dnd.drawing.Image;
import org.nakedobjects.nos.client.dnd.drawing.Size;
import org.nakedobjects.nos.client.dnd.drawing.Text;
import org.nakedobjects.nos.client.dnd.image.ImageFactory;
import org.nakedobjects.nos.client.dnd.view.simple.AbstractView;

public class AboutView extends AbstractView {

    private static final int MAX_WIDTH = 180;

    private final int padding = 6;

    private final Image image;

    private final int left;

    public AboutView() {
        image = ImageFactory.getInstance().loadImage(AboutNakedObjects.getImageName());
        if (showingImage()) {
            left = padding + image.getWidth() + padding;
        } else {
            left = padding;
        }
        setContent(new NullContent(AboutNakedObjects.getFrameworkName()));
    }

    public void draw(final Canvas canvas) {
        super.draw(canvas);
        Text titleStyle = Toolkit.getText("title");
        Text normalStyle = Toolkit.getText("label");
        Color color = Toolkit.getColor("black");
        canvas.clearBackground(this, Toolkit.getColor("white"));
        canvas.drawRectangleAround(this, Toolkit.getColor("secondary1"));
        if (showingImage()) {
            canvas.drawImage(image, padding, padding);
        }
        int line = padding + normalStyle.getAscent();
        String text = AboutNakedObjects.getApplicationName();
        if (text != null) {
            canvas.drawText(text, left, line, MAX_WIDTH, color, titleStyle);
            line += titleStyle.stringHeight(text, MAX_WIDTH) + titleStyle.getLineSpacing() + padding;
        }
        text = AboutNakedObjects.getApplicationCopyrightNotice();
        if (text != null) {
            canvas.drawText(text, left, line, MAX_WIDTH, color, normalStyle);
            line += normalStyle.stringHeight(text, MAX_WIDTH) + normalStyle.getLineSpacing() + padding;
        }
        text = AboutNakedObjects.getApplicationVersion();
        if (text != null) {
            canvas.drawText(text, left, line, MAX_WIDTH, color, normalStyle);
            line += normalStyle.stringHeight(text, MAX_WIDTH) + normalStyle.getLineSpacing() + padding;
            line += 2 * normalStyle.getLineHeight();
        }
        text = AboutNakedObjects.getFrameworkName();
        canvas.drawText(text, left, line, MAX_WIDTH, color, titleStyle);
        line += titleStyle.stringHeight(text, MAX_WIDTH) + titleStyle.getLineSpacing() + padding;
        text = AboutNakedObjects.getFrameworkCopyrightNotice();
        canvas.drawText(text, left, line, MAX_WIDTH, color, normalStyle);
        line += normalStyle.stringHeight(text, MAX_WIDTH) + normalStyle.getLineSpacing() + padding;
        canvas.drawText(frameworkVersion(), left, line, MAX_WIDTH, color, normalStyle);
    }

    private String frameworkVersion() {
        return AboutNakedObjects.getFrameworkVersion();
    }

    private boolean showingImage() {
        return image != null;
    }

    public Size getMaximumSize() {
        Text titleStyle = Toolkit.getText("title");
        Text normalStyle = Toolkit.getText("label");
        int height = titleStyle.getAscent();
        height += normalStyle.getLineHeight();
        height += 2 * normalStyle.getLineHeight();
        String text = AboutNakedObjects.getFrameworkName();
        height += titleStyle.stringHeight(text, MAX_WIDTH) + titleStyle.getLineSpacing() + padding;
        int width = titleStyle.stringWidth(text, MAX_WIDTH);
        text = AboutNakedObjects.getFrameworkCopyrightNotice();
        height += normalStyle.stringHeight(text, MAX_WIDTH) + normalStyle.getLineSpacing() + padding;
        width = Math.max(width, normalStyle.stringWidth(text, MAX_WIDTH));
        text = frameworkVersion();
        height += normalStyle.stringHeight(text, MAX_WIDTH) + normalStyle.getLineSpacing() + padding;
        width = Math.max(width, normalStyle.stringWidth(text, MAX_WIDTH));
        text = AboutNakedObjects.getApplicationName();
        if (text != null) {
            height += titleStyle.stringHeight(text, MAX_WIDTH) + titleStyle.getLineSpacing() + padding;
            width = Math.max(width, titleStyle.stringWidth(text, MAX_WIDTH));
        }
        text = AboutNakedObjects.getApplicationCopyrightNotice();
        if (text != null) {
            height += normalStyle.stringHeight(text, MAX_WIDTH) + normalStyle.getLineSpacing() + padding;
            width = Math.max(width, normalStyle.stringWidth(text, MAX_WIDTH));
        }
        text = AboutNakedObjects.getApplicationVersion();
        if (text != null) {
            height += normalStyle.stringHeight(text, MAX_WIDTH) + normalStyle.getLineSpacing() + padding;
            width = Math.max(width, normalStyle.stringWidth(text, MAX_WIDTH));
        }
        if (showingImage()) {
            height = Math.max(height, image.getHeight());
            width = image.getWidth() + padding + width;
        }
        return new Size(padding + width + padding, padding + height + padding);
    }

    public void firstClick(final Click click) {
    }
}
