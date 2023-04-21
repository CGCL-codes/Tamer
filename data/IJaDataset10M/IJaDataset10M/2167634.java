package org.kompiro.readviewer.ui.views;

import java.io.InputStream;
import java.text.DateFormat;
import java.util.Date;
import org.eclipse.jface.resource.CompositeImageDescriptor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.DecorationOverlayIcon;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.kompiro.readviewer.service.INotificationMessage;
import org.kompiro.readviewer.service.INotificationService;
import org.kompiro.readviewer.ui.UIActivator;
import org.kompiro.readviewer.ui.UiImage;

class ViewLabelProvider extends CellLabelProvider {

    private static final int TOOLTIP_FONT_HEIGHT;

    static {
        if ("gtk".equals(SWT.getPlatform())) {
            TOOLTIP_FONT_HEIGHT = 10;
        } else {
            TOOLTIP_FONT_HEIGHT = 8;
        }
    }

    private static final class AnimationListener implements Listener {

        private static final int PLATFORM_DELTA_X;

        private static final int PLATFORM_DELTA_Y;

        static {
            if ("gtk".equals(SWT.getPlatform())) {
                PLATFORM_DELTA_X = 1;
                PLATFORM_DELTA_Y = 3;
            } else {
                PLATFORM_DELTA_X = 0;
                PLATFORM_DELTA_Y = 0;
            }
        }

        private int animationIndex = 0;

        private ImageData[] animationImages;

        protected int per10msec;

        private Control control;

        private AnimationListener(Control control) {
            this.control = control;
            ImageLoader loader = new ImageLoader();
            InputStream stream = this.getClass().getResourceAsStream("loading.gif");
            animationImages = loader.load(stream);
        }

        public void handleEvent(final Event event) {
            if (event.item instanceof TreeItem) {
                TreeItem item = (TreeItem) event.item;
                Rectangle rect = item.getImageBounds(0);
                Object element = item.getData();
                if (element instanceof INotificationService) {
                    INotificationService service = (INotificationService) element;
                    Image image = item.getImage();
                    doAnimation(control, item, rect, image, service);
                    if (image != null && !event.gc.isDisposed()) {
                        event.gc.drawImage(image, rect.x + PLATFORM_DELTA_X, rect.y + PLATFORM_DELTA_Y);
                    }
                }
            }
        }

        private void doAnimation(final Control tree, final TreeItem item, final Rectangle rect, final Image image, final INotificationService service) {
            Runnable animation = new Runnable() {

                public void run() {
                    if (item.isDisposed()) return;
                    GC imageGc = new GC(tree);
                    final Image animationImage = getAnimationImage(image.getImageData());
                    if (animationImage != null) {
                        imageGc.drawImage(animationImage, rect.x + PLATFORM_DELTA_X, rect.y + PLATFORM_DELTA_Y);
                        animationImage.dispose();
                    }
                    if (service.isLoading()) {
                        tree.getDisplay().timerExec(per10msec, this);
                    } else {
                        imageGc.drawImage(image, rect.x + PLATFORM_DELTA_X, rect.y + PLATFORM_DELTA_Y);
                    }
                    imageGc.dispose();
                }
            };
            if (service.isLoading()) {
                tree.getDisplay().asyncExec(animation);
            } else {
                GC imageGc = new GC(tree);
                imageGc.drawImage(image, rect.x + PLATFORM_DELTA_X, rect.y + PLATFORM_DELTA_Y);
                imageGc.dispose();
            }
        }

        private Image getAnimationImage(final ImageData baseImage) {
            CompositeImageDescriptor icon = new CompositeImageDescriptor() {

                @Override
                protected void drawCompositeImage(int width, int height) {
                    drawImage(baseImage, 0, 0);
                    ImageData imageData = animationImages[animationIndex];
                    animationIndex = animationIndex == animationImages.length - 1 ? 0 : animationIndex + 1;
                    drawImage(imageData, 0, 0);
                    per10msec = imageData.delayTime * 10;
                }

                @Override
                protected Point getSize() {
                    return new Point(16, 16);
                }
            };
            return icon.createImage();
        }
    }

    ViewLabelProvider(TreeViewer viewer) {
        Control tree = viewer.getControl();
        tree.addListener(SWT.PaintItem, new AnimationListener(tree));
    }

    public String getText(Object obj) {
        if (obj instanceof INotificationService) {
            INotificationService service = (INotificationService) obj;
            return getLabel(service.getDate(), service.getServiceName());
        }
        if (obj instanceof INotificationMessage) {
            INotificationMessage message = (INotificationMessage) obj;
            return getLabel(message.getPublishDate(), message.getTitle());
        }
        return obj.toString();
    }

    private String getLabel(Date date, String title) {
        DateFormat df = DateFormat.getDateTimeInstance();
        String publishDateStr = "undefined";
        if (date != null) publishDateStr = df.format(date);
        String label = title + ":" + publishDateStr;
        return label;
    }

    public Image getImage(Object obj) {
        if (obj instanceof INotificationMessage) {
            return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FILE);
        }
        if (obj instanceof INotificationService) {
            INotificationService service = (INotificationService) obj;
            Image image;
            try {
                image = service.getImage();
                if (image != null) {
                    image = new Image(PlatformUI.getWorkbench().getDisplay(), image.getImageData().scaledTo(16, 16));
                } else {
                    image = UIActivator.getDefault().getImageRegistry().get(UiImage.KEY_FEEDS);
                }
            } catch (Exception e) {
                image = UIActivator.getDefault().getImageRegistry().get(UiImage.KEY_FEEDS);
            }
            if (!service.isLoading() && !service.isOK()) {
                ImageDescriptor descriptor = UIActivator.getDefault().getImageRegistry().getDescriptor(UiImage.KEY_ERROR);
                DecorationOverlayIcon icon = new DecorationOverlayIcon(image, descriptor, IDecoration.BOTTOM_RIGHT);
                image = icon.createImage();
            }
            return image;
        }
        return null;
    }

    @Override
    public void update(ViewerCell cell) {
        Object element = cell.getElement();
        cell.setText(getText(element));
        cell.setImage(getImage(element));
        cell.getItem().setData(element);
    }

    @Override
    public String getToolTipText(Object element) {
        if (element instanceof INotificationMessage) {
            INotificationMessage message = (INotificationMessage) element;
            String description = message.getDescription();
            if (description != null) {
                if (description == null || "".equals(description)) return null;
                return description;
            }
        }
        return null;
    }

    @Override
    public Font getToolTipFont(Object object) {
        Font textFont = JFaceResources.getTextFont();
        FontData[] fontDatas = textFont.getFontData();
        fontDatas[0].setHeight(TOOLTIP_FONT_HEIGHT);
        Font copyFont = new Font(PlatformUI.getWorkbench().getDisplay(), fontDatas[0]);
        return copyFont;
    }
}
