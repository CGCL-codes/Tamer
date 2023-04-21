package org.nakedobjects.plugins.dndviewer.viewer.view.help;

import org.nakedobjects.metamodel.commons.about.AboutNakedObjects;
import org.nakedobjects.plugins.dndviewer.Canvas;
import org.nakedobjects.plugins.dndviewer.Click;
import org.nakedobjects.plugins.dndviewer.ColorsAndFonts;
import org.nakedobjects.plugins.dndviewer.FocusManager;
import org.nakedobjects.plugins.dndviewer.NullContent;
import org.nakedobjects.plugins.dndviewer.Toolkit;
import org.nakedobjects.plugins.dndviewer.viewer.drawing.Color;
import org.nakedobjects.plugins.dndviewer.viewer.drawing.Image;
import org.nakedobjects.plugins.dndviewer.viewer.drawing.Size;
import org.nakedobjects.plugins.dndviewer.viewer.drawing.Text;
import org.nakedobjects.plugins.dndviewer.viewer.focus.SubviewFocusManager;
import org.nakedobjects.plugins.dndviewer.viewer.image.ImageFactory;
import org.nakedobjects.plugins.dndviewer.viewer.view.simple.AbstractView;

public class AboutView extends AbstractView {

    private static final int MAX_WIDTH = 300;

    private final int linePadding = -2;

    private final int noticePadding = 45;

    private final int margin = 14;

    private final Image image;

    private final int left;

    private FocusManager focusManager;

    public AboutView() {
        super(new NullContent());
        image = ImageFactory.getInstance().loadImage(AboutNakedObjects.getImageName());
        left = noticePadding;
        setContent(new NullContent(AboutNakedObjects.getFrameworkName()));
        focusManager = new SubviewFocusManager(this);
    }

    public FocusManager getFocusManager() {
        return focusManager;
    }

    @Override
    public void draw(final Canvas canvas) {
        super.draw(canvas);
        final Text titleStyle = Toolkit.getText(ColorsAndFonts.TEXT_TITLE);
        final Text normalStyle = Toolkit.getText(ColorsAndFonts.TEXT_LABEL);
        final Color color = Toolkit.getColor(ColorsAndFonts.COLOR_BLACK);
        canvas.clearBackground(this, Toolkit.getColor(ColorsAndFonts.COLOR_WHITE));
        canvas.drawRectangleAround(this, Toolkit.getColor(ColorsAndFonts.COLOR_SECONDARY1));
        if (showingImage()) {
            canvas.drawImage(image, margin, margin);
        }
        int line = margin + image.getHeight() + noticePadding + normalStyle.getAscent();
        String text = AboutNakedObjects.getApplicationName();
        if (text != null) {
            canvas.drawText(text, left, line, MAX_WIDTH, color, titleStyle);
            line += titleStyle.stringHeight(text, MAX_WIDTH) + titleStyle.getLineSpacing() + linePadding;
        }
        text = AboutNakedObjects.getApplicationCopyrightNotice();
        if (text != null) {
            canvas.drawText(text, left, line, MAX_WIDTH, color, normalStyle);
            line += normalStyle.stringHeight(text, MAX_WIDTH) + normalStyle.getLineSpacing() + linePadding;
        }
        text = AboutNakedObjects.getApplicationVersion();
        if (text != null) {
            canvas.drawText(text, left, line, MAX_WIDTH, color, normalStyle);
            line += normalStyle.stringHeight(text, MAX_WIDTH) + normalStyle.getLineSpacing() + linePadding;
            line += 2 * normalStyle.getLineHeight();
        }
        text = AboutNakedObjects.getFrameworkName();
        canvas.drawText(text, left, line, MAX_WIDTH, color, titleStyle);
        line += titleStyle.stringHeight(text, MAX_WIDTH) + titleStyle.getLineSpacing() + linePadding;
        text = AboutNakedObjects.getFrameworkCopyrightNotice();
        canvas.drawText(text, left, line, MAX_WIDTH, color, normalStyle);
        line += normalStyle.stringHeight(text, MAX_WIDTH) + normalStyle.getLineSpacing() + linePadding;
        canvas.drawText(frameworkVersion(), left, line, MAX_WIDTH, color, normalStyle);
    }

    private String frameworkVersion() {
        return AboutNakedObjects.getFrameworkVersion();
    }

    private boolean showingImage() {
        return image != null;
    }

    @Override
    public Size getMaximumSize() {
        final Text titleStyle = Toolkit.getText(ColorsAndFonts.TEXT_TITLE);
        final Text normalStyle = Toolkit.getText(ColorsAndFonts.TEXT_LABEL);
        int height = 0;
        String text = AboutNakedObjects.getFrameworkName();
        height += titleStyle.stringHeight(text, MAX_WIDTH) + titleStyle.getLineSpacing() + linePadding;
        int width = titleStyle.stringWidth(text, MAX_WIDTH);
        text = AboutNakedObjects.getFrameworkCopyrightNotice();
        height += normalStyle.stringHeight(text, MAX_WIDTH) + normalStyle.getLineSpacing() + linePadding;
        width = Math.max(width, normalStyle.stringWidth(text, MAX_WIDTH));
        text = frameworkVersion();
        height += normalStyle.stringHeight(text, MAX_WIDTH) + normalStyle.getLineSpacing() + linePadding;
        width = Math.max(width, normalStyle.stringWidth(text, MAX_WIDTH));
        text = AboutNakedObjects.getApplicationName();
        if (text != null) {
            height += titleStyle.stringHeight(text, MAX_WIDTH) + titleStyle.getLineSpacing() + linePadding;
            width = Math.max(width, titleStyle.stringWidth(text, MAX_WIDTH));
        }
        text = AboutNakedObjects.getApplicationCopyrightNotice();
        if (text != null) {
            height += normalStyle.stringHeight(text, MAX_WIDTH) + normalStyle.getLineSpacing() + linePadding;
            width = Math.max(width, normalStyle.stringWidth(text, MAX_WIDTH));
        }
        text = AboutNakedObjects.getApplicationVersion();
        if (text != null) {
            height += normalStyle.stringHeight(text, MAX_WIDTH) + normalStyle.getLineSpacing() + linePadding;
            width = Math.max(width, normalStyle.stringWidth(text, MAX_WIDTH));
        }
        height += noticePadding;
        if (showingImage()) {
            height += image.getHeight();
            width = Math.max(image.getWidth(), width);
        }
        return new Size(margin + width + margin, margin + height + margin);
    }

    @Override
    public void firstClick(final Click click) {
    }
}
