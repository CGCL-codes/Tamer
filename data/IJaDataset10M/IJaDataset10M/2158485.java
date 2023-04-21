package ti.plato.ui.views.logger.views;

import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import ti.plato.ui.images.util.ImagesUtil;
import ti.plato.ui.u.LinkableTextContents;

public class ExtendedTip {

    private static char focusCharacterToAssign = 'A';

    public static void reinitializeFocusCharacter() {
        focusCharacterToAssign = 'A';
    }

    private static int transitionHeight = 8;

    private static int headerSpaceLeftIcon = 1;

    private static int headerSpaceRightIcon = 2;

    private static int headerSpaceRightText = 2;

    private static int headerSpaceTop = 1;

    private static int headerSpaceBottom = 4;

    private static int borderWidth = 1;

    private Shell addedShell = null;

    private StyledText labelContent = null;

    private Label labelFooter = null;

    private Color foregroundColorLabel = null;

    private int offset = 0;

    private Color backgroundColor = null;

    private Color foregroundColor = null;

    private boolean complexMode = false;

    private Image image = null;

    private String headerText = null;

    private Font fontHeader = null;

    private Font fontFooter = null;

    private Dimension headerDimension = null;

    private char focusCharacter = 0;

    private Point focusPoint = null;

    private FormData formLabelFooter = null;

    private FormData formLabelContent = null;

    private PaintListener paintListener = new PaintListener() {

        public void paintControl(PaintEvent event) {
            if (backgroundColor != null) {
                int lineIndex;
                for (lineIndex = 0; lineIndex < transitionHeight; lineIndex++) {
                    Color customizedColor = getBarColor(addedShell, backgroundColor, foregroundColor, (double) lineIndex / (double) (transitionHeight - 1));
                    event.gc.setForeground(customizedColor);
                    int yLine = headerDimension.height - lineIndex - 1;
                    event.gc.drawLine(0, yLine, addedShell.getSize().x, yLine);
                    customizedColor.dispose();
                }
                event.gc.setForeground(foregroundColor);
                for (lineIndex = 0; lineIndex <= headerDimension.height - transitionHeight - 1; lineIndex++) {
                    event.gc.drawLine(0, lineIndex, addedShell.getSize().x, lineIndex);
                }
            }
            event.gc.drawImage(image, headerSpaceLeftIcon, headerSpaceTop);
            event.gc.setForeground(backgroundColor);
            event.gc.setFont(fontHeader);
            event.gc.drawText(headerText, headerSpaceLeftIcon + image.getBounds().width + headerSpaceRightIcon, headerSpaceTop, SWT.DRAW_TRANSPARENT);
            if (footerShown) {
                event.gc.setForeground(foregroundColorLabel);
                event.gc.drawLine(1, labelFooter.getLocation().y - 1, labelFooter.getLocation().x + labelFooter.getSize().x, labelFooter.getLocation().y - 1);
            }
        }
    };

    ExtendedTip(Shell parent, Color backgroundColor, Color foregroundColor, LinkableTextContents contentText, String imageStr, String headerText, Point focusPoint) {
        this.focusPoint = focusPoint;
        if (focusPoint != null) {
            this.focusPoint.x -= 1;
            this.focusPoint.y -= 2;
        }
        this.offset = -1;
        image = ImagesUtil.getImageDescriptor(imageStr).createImage();
        this.foregroundColor = foregroundColor;
        this.backgroundColor = backgroundColor;
        this.headerText = headerText;
        complexMode = true;
        init(parent, contentText);
    }

    ExtendedTip(Shell parent, LinkableTextContents contentText, int offset) {
        this.offset = offset;
        init(parent, contentText);
    }

    private void init(final Shell parent, LinkableTextContents text) {
        addedShell = new Shell(parent, SWT.ON_TOP | SWT.NO_FOCUS | SWT.TOOL);
        if (complexMode) addedShell.addPaintListener(paintListener);
        if (backgroundColor != null) addedShell.setBackground(backgroundColor);
        if (foregroundColor != null) addedShell.setForeground(foregroundColor);
        addedShell.setLayout(new FormLayout());
        labelContent = new StyledText(addedShell, SWT.NONE);
        if (complexMode) labelFooter = new Label(addedShell, SWT.NONE);
        if (complexMode) {
            FontData[] fontData = labelContent.getFont().getFontData();
            for (int i = 0; i < fontData.length; i++) {
                fontData[i].setStyle(fontData[i].getStyle() | SWT.BOLD | SWT.ITALIC);
            }
            fontHeader = new Font(addedShell.getDisplay(), fontData);
            headerDimension = FigureUtilities.getStringExtents(headerText, fontHeader);
            headerDimension.width += headerSpaceLeftIcon + image.getBounds().width + headerSpaceRightIcon + headerSpaceRightText;
            headerDimension.height = headerSpaceTop + image.getBounds().height + headerSpaceBottom;
        }
        formLabelContent = new FormData();
        if (complexMode) formLabelContent.top = new FormAttachment(0, headerDimension.height); else formLabelContent.top = new FormAttachment(0, 0);
        if (complexMode) formLabelContent.bottom = new FormAttachment(labelFooter, -3, SWT.TOP); else formLabelContent.bottom = new FormAttachment(100, -1);
        formLabelContent.left = new FormAttachment(0, 2);
        formLabelContent.right = new FormAttachment(100, -2);
        labelContent.setLayoutData(formLabelContent);
        if (backgroundColor != null) {
            labelContent.setBackground(backgroundColor);
        }
        if (foregroundColor != null) {
            labelContent.setForeground(foregroundColor);
        }
        if (!text.hasLinks()) footerShown = false;
        text.render(labelContent);
        if (text.hasLinks()) {
            addedShell.addMouseListener(new MouseListener() {

                public void mouseDoubleClick(MouseEvent e) {
                }

                public void mouseDown(MouseEvent e) {
                }

                public void mouseUp(MouseEvent e) {
                    ExtendedTip.this.dispose();
                }
            });
            addedShell.addMouseTrackListener(new MouseTrackListener() {

                public void mouseEnter(MouseEvent e) {
                }

                public void mouseExit(MouseEvent e) {
                    ExtendedTip.this.dispose();
                    return;
                }

                public void mouseHover(MouseEvent e) {
                }
            });
        }
        if (complexMode) {
            FontData[] fontData = labelFooter.getFont().getFontData();
            for (int i = 0; i < fontData.length; i++) {
                fontData[i].setHeight(fontData[i].getHeight() - 1);
            }
            fontFooter = new Font(addedShell.getDisplay(), fontData);
            labelFooter.setFont(fontFooter);
            foregroundColorLabel = getBarColor(addedShell, backgroundColor, foregroundColor, 0.5);
            labelFooter.setText("Press '" + focusCharacterToAssign + "' for focus.");
            if (footerShown) {
                if (focusPoint != null) {
                    focusCharacter = focusCharacterToAssign;
                    if (focusCharacterToAssign != 'Z') focusCharacterToAssign++;
                } else {
                    footerShown = false;
                }
            }
            labelFooter.setAlignment(SWT.RIGHT);
            labelFooter.pack();
            int labelFooterHeight = labelFooter.getSize().y;
            formLabelFooter = new FormData();
            formLabelFooter.top = new FormAttachment(100, -labelFooterHeight - 1);
            formLabelFooter.bottom = new FormAttachment(100, -1);
            formLabelFooter.left = new FormAttachment(0, 2);
            formLabelFooter.right = new FormAttachment(100, -2);
            labelFooter.setLayoutData(formLabelFooter);
            if (backgroundColor != null) labelFooter.setBackground(backgroundColor);
            if (foregroundColorLabel != null) labelFooter.setForeground(foregroundColorLabel);
        }
        if (!footerShown) {
            showFooter(false);
        }
    }

    private boolean footerShown = true;

    private void showFooter(boolean status) {
        if (!complexMode) return;
        labelFooter.setVisible(status);
        Point size = addedShell.getSize();
        if (status) {
            formLabelContent.bottom = new FormAttachment(labelFooter, -3, SWT.TOP);
            size.y += (labelFooter.getSize().y + 2);
        } else {
            formLabelContent.bottom = new FormAttachment(100, -1);
            size.y -= (labelFooter.getSize().y + 2);
        }
        addedShell.setSize(size);
        footerShown = status;
    }

    public int display(Point pt, int topForce) {
        Point size = addedShell.computeSize(SWT.DEFAULT, SWT.DEFAULT);
        if (complexMode) {
            if (size.x < (headerDimension.width + borderWidth * 2)) {
                size.x = headerDimension.width + borderWidth * 2;
                addedShell.setSize(size);
            }
        }
        if (topForce == -1) {
            addedShell.setBounds(pt.x + offset, pt.y, size.x, size.y);
            topForce = pt.y + size.y - 1;
        } else {
            addedShell.setBounds(pt.x + offset, topForce, size.x, size.y);
            topForce = topForce + size.y;
        }
        addedShell.setVisible(true);
        return topForce + 6;
    }

    public boolean isDisposed() {
        return (addedShell.isDisposed() || labelContent.isDisposed());
    }

    public void dispose() {
        addedShell.dispose();
        labelContent.dispose();
        if (labelFooter != null) labelFooter.dispose();
        if (fontHeader != null) fontHeader.dispose();
        if (fontFooter != null) fontFooter.dispose();
        if (image != null) image.dispose();
        if (foregroundColorLabel != null) foregroundColorLabel.dispose();
    }

    public Rectangle getBounds() {
        if (addedShell.isDisposed()) return null;
        return addedShell.getBounds();
    }

    private Color getBarColor(Shell parent, Color color1, Color color2, double colorRatio) {
        int red = -1;
        int green = -1;
        int blue = -1;
        if (colorRatio == 0.0) {
            red = color1.getRed();
            green = color1.getGreen();
            blue = color1.getBlue();
        } else {
            double red1 = (double) color1.getRed();
            double green1 = (double) color1.getGreen();
            double blue1 = (double) color1.getBlue();
            double red2 = (double) color2.getRed();
            double green2 = (double) color2.getGreen();
            double blue2 = (double) color2.getBlue();
            red = (int) (((red2 - red1) * colorRatio) + red1);
            green = (int) (((green2 - green1) * colorRatio) + green1);
            blue = (int) (((blue2 - blue1) * colorRatio) + blue1);
        }
        return new Color(parent.getDisplay(), red, green, blue);
    }

    public boolean isWired(char pressedCharacter) {
        return (pressedCharacter == focusCharacter || pressedCharacter == focusCharacter + 32);
    }

    public void moveToFocus() {
        showFooter(false);
        if (focusPoint == null) return;
        Point originLocation = addedShell.getLocation();
        Point destinationLocation = focusPoint;
        if (originLocation.equals(destinationLocation)) return;
        int moveIndex;
        for (moveIndex = 0; moveIndex <= 100; moveIndex += 5) {
            double ratio = (double) moveIndex / 100.0;
            if (moveIndex == 100) ratio = 1;
            int y = (int) (ratio * destinationLocation.y + (1 - ratio) * originLocation.y);
            int x = (int) (ratio * destinationLocation.x + (1 - ratio) * originLocation.x);
            addedShell.setLocation(x, y);
        }
        focusCharacter = 0;
    }
}
