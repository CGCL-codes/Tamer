package org.designerator.media.image.eclipse.thumbs;

import org.designerator.common.widgets.WidgetUtils;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

public class SimpleThumb extends Canvas {

    private final class SelectListener implements MouseListener {

        public void mouseDoubleClick(MouseEvent e) {
            parent.loadImage(imagePlus.imagePath);
        }

        public void mouseDown(MouseEvent e) {
            setBackgroundColor(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
            parent.setSelectedThumb(thisThumb);
        }

        public void mouseUp(MouseEvent e) {
        }
    }

    private Label imageLabel = null;

    private Label textLabel = null;

    public SimpleThumbScroller parent;

    public SimpleThumb thisThumb;

    private SelectListener selectListener;

    private SimpleThumbProxy imagePlus;

    private Image thumbNail;

    private boolean invisible;

    private static GridData thumbGridData;

    private static GridLayout gridLayout;

    private static GridData textGridData;

    private static GridData imageGridData;

    static {
        imageGridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
        imageGridData.heightHint = 100;
        textGridData = new GridData(SWT.FILL, SWT.CENTER, true, true);
        gridLayout = WidgetUtils.getGridLayout(1);
        thumbGridData = new GridData(150, 120);
    }

    public SimpleThumb(SimpleThumbScroller parent, String path, String text, String toolTip) {
        super(parent.getThumbsComposite(), SWT.None);
        this.parent = parent;
        thisThumb = this;
        initialize();
        createThumbNail(path, text, toolTip);
    }

    public SimpleThumb(SimpleThumbScroller parent, SimpleThumbProxy image) {
        super(parent.getThumbsComposite(), SWT.None);
        this.parent = parent;
        thisThumb = this;
        this.imagePlus = image;
        initialize();
        if (image.thumbNail != null) {
            Path path = new Path(image.imagePath);
            thumbNail = image.thumbNail;
            createThumbNailComposite(image.thumbNail, path.lastSegment(), image.imagePath);
        }
    }

    private void initialize() {
        selectListener = new SelectListener();
        setLayout(gridLayout);
        setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
        addMouseListener(selectListener);
    }

    private void createThumbNail(String path, String text, String toolTip) {
        Image image = new Image(Display.getCurrent(), getClass().getResourceAsStream(path));
        createThumbNailComposite(image, text, toolTip);
    }

    private void createThumbNailComposite(final Image image, String text, String toolTip) {
        final Composite thumbNailComposite = new Composite(this, SWT.NONE);
        thumbNailComposite.addMouseListener(selectListener);
        thumbNailComposite.setLayoutData(thumbGridData);
        thumbNailComposite.setLayout(gridLayout);
        thumbNailComposite.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
        imageLabel = new Label(thumbNailComposite, SWT.CENTER | SWT.DOUBLE_BUFFERED);
        imageLabel.addDisposeListener(new DisposeListener() {

            public void widgetDisposed(DisposeEvent e) {
                Image image2 = imageLabel.getImage();
                if (image2 != null && !image2.isDisposed()) {
                    image2.dispose();
                }
            }
        });
        imageLabel.addMouseListener(selectListener);
        imageLabel.setImage(image);
        imageLabel.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
        imageLabel.setLayoutData(imageGridData);
        textLabel = new Label(thumbNailComposite, SWT.CENTER);
        textLabel.addMouseListener(selectListener);
        textLabel.setText(text);
        textLabel.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
        textLabel.setToolTipText(toolTip);
        textLabel.setLayoutData(textGridData);
    }

    public void setBackgroundColor(Color color) {
        setBackground(color);
        redraw();
    }

    public SimpleThumbProxy getImagePlus() {
        return imagePlus;
    }

    public void setImagePlus(SimpleThumbProxy imagePlus2) {
        imagePlus = imagePlus2;
        thumbNail = imagePlus2.getThumbNail();
        imageLabel.setImage(thumbNail);
        if (imagePlus2.imageFile != null) {
            textLabel.setText(imagePlus2.imageFile.getName());
        }
    }

    public void updateposition(int parentLocation, int parentHeight) {
    }
}
