package eu.redseeds.sc.editor.rsl.editors;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ScrollBar;
import eu.redseeds.common.diagram.image.DiagramImageManager;
import eu.redseeds.common.diagram.image.IDiagramImageManager;
import eu.redseeds.common.diagram.image.IImageNameResolver;
import eu.redseeds.sc.current.repository.SCProject;
import eu.redseeds.sc.current.repository.SCProjectContainer;
import eu.redseeds.sc.editor.rsl.Activator;
import eu.redseeds.scl.model.rsl.rslrequirements.requirementsspecifications.UseCaseDTO;

public class UseCaseImageView extends Composite {

    private final UseCaseDTO useCaseDTO;

    private final List<Image> imagesToDispose = new ArrayList<Image>();

    private static final String tabName = "Graphic view";

    private final Composite parent;

    private Canvas canvasWithDiagram;

    private Button refreshButton;

    private Image image;

    public UseCaseImageView(UseCaseDTO useCaseDTO, Composite parent, int style) {
        super(parent, style);
        this.parent = parent;
        this.useCaseDTO = useCaseDTO;
        initSentenes();
    }

    private void initSentenes() {
        addImageViewer();
        registerResourceChangeListener();
        addDisposeEvent();
    }

    /**
	 * Add image to list of images to dispose
	 * @see #freeImages()
	 * @param imageToDispose
	 */
    private void addImageToDispose(Image imageToDispose) {
        imagesToDispose.add(imageToDispose);
    }

    /**
	 * Add dispose listener
	 */
    private void addDisposeEvent() {
        addDisposeListener(new DisposeListener() {

            @Override
            public void widgetDisposed(DisposeEvent e) {
                freeImages();
                UseCaseImageResourceChangeListener.rmvResourceChangeListener(UseCaseImageView.this);
            }
        });
    }

    /**
	 * Dispose all images
	 * @see #addImageToDispose(Image)
	 */
    private void freeImages() {
        for (Image resource : imagesToDispose) {
            if (resource != null) {
                resource.dispose();
            }
        }
    }

    private Color getBackgroundColor() {
        return getDisplay().getSystemColor(SWT.COLOR_WHITE);
    }

    private Color getCanvasBackrounfColor() {
        return getDisplay().getSystemColor(SWT.COLOR_WHITE);
    }

    private Color getLabelBackrounfColor() {
        return getDisplay().getSystemColor(SWT.COLOR_WHITE);
    }

    /**
	 * Add main window
	 */
    private void addImageViewer() {
        setLayout(new FillLayout());
        setBackground(getBackgroundColor());
        setBackground(getBackgroundColor());
        refresh();
    }

    /**
	 * Refresh canvas after update of image
	 * @see #refreshUseCaseImageView()
	 */
    private void redrawImageView() {
        redraw();
        if (canvasWithDiagram != null) {
            canvasWithDiagram.redraw();
        }
        Rectangle bounds = getBounds();
        int height = bounds.height;
        if (height > 1) {
            setBounds(bounds.x, bounds.y, bounds.width, height - 1);
            setBounds(bounds.x, bounds.y, bounds.width, height);
        }
    }

    /**
	 * Main method for refreshing image.
	 * If there is new image show it or show info that there is no image
	 */
    private void refreshUseCaseImageView() {
        ImageData imageData = getImageDataToDisplay();
        if (imageData != null) {
            if (refreshButton != null) {
                rmvLabel();
            }
            if (canvasWithDiagram == null) {
                showCanvasWithDiagram();
            }
            updateImage(imageData);
        } else {
            if (canvasWithDiagram != null) {
                rmvCanvas();
            }
            if (refreshButton == null) {
                shwoNoImageInfo();
            }
        }
        redrawImageView();
    }

    /**
	 * If label exist remove it
	 */
    private void rmvLabel() {
        refreshButton.dispose();
        refreshButton = null;
    }

    /**
	 * If canvas exist remove it
	 */
    private void rmvCanvas() {
        canvasWithDiagram.dispose();
        canvasWithDiagram = null;
    }

    /**
	 * Look for new image
	 * @return imageData if image exists or null
	 */
    private ImageData getImageDataToDisplay() {
        ImageData imageData = null;
        IImageNameResolver imageToFindDescription = UseCaseImageNameResolver.getInstanceImageNameResolver(useCaseDTO);
        SCProject scProject = SCProjectContainer.instance().getSCProject(useCaseDTO);
        if (scProject != null) {
            IProject project = scProject.getEclipseProject();
            try {
                IDiagramImageManager imageFinder = DiagramImageManager.getInstance();
                imageData = imageFinder.findImage(imageToFindDescription, project);
            } catch (Exception e) {
            }
        }
        return imageData;
    }

    /**
	 * Prepare image to show
	 * @param imageData imageData that represent image to show
	 */
    private void updateImage(ImageData imageData) {
        freeImages();
        image = new Image(getDisplay(), imageData);
        addImageToDispose(image);
    }

    /**
	 * Main method for preparing canvas
	 */
    private void showCanvasWithDiagram() {
        if (canvasWithDiagram == null) {
            final Point origin = new Point(0, 0);
            canvasWithDiagram = new Canvas(this, SWT.NO_BACKGROUND | SWT.NO_REDRAW_RESIZE | SWT.V_SCROLL | SWT.H_SCROLL);
            canvasWithDiagram.setBackground(getCanvasBackrounfColor());
            final ScrollBar hBar = canvasWithDiagram.getHorizontalBar();
            hBar.addListener(SWT.Selection, new Listener() {

                public void handleEvent(Event e) {
                    int hSelection = hBar.getSelection();
                    int destX = -hSelection - origin.x;
                    Rectangle rect = image.getBounds();
                    canvasWithDiagram.scroll(destX, 0, 0, 0, rect.width, rect.height, false);
                    origin.x = -hSelection;
                }
            });
            final ScrollBar vBar = canvasWithDiagram.getVerticalBar();
            vBar.addListener(SWT.Selection, new Listener() {

                public void handleEvent(Event e) {
                    int vSelection = vBar.getSelection();
                    int destY = -vSelection - origin.y;
                    Rectangle rect = image.getBounds();
                    canvasWithDiagram.scroll(0, destY, 0, 0, rect.width, rect.height, false);
                    origin.y = -vSelection;
                }
            });
            canvasWithDiagram.addListener(SWT.Resize, new Listener() {

                public void handleEvent(Event e) {
                    if (image != null) {
                        Rectangle rect = image.getBounds();
                        Rectangle client = canvasWithDiagram.getClientArea();
                        hBar.setMaximum(rect.width);
                        vBar.setMaximum(rect.height);
                        hBar.setThumb(Math.min(rect.width, client.width));
                        vBar.setThumb(Math.min(rect.height, client.height));
                        int hPage = rect.width - client.width;
                        int vPage = rect.height - client.height;
                        int hSelection = hBar.getSelection();
                        int vSelection = vBar.getSelection();
                        if (hSelection >= hPage) {
                            if (hPage <= 0) hSelection = 0;
                            origin.x = -hSelection;
                        }
                        if (vSelection >= vPage) {
                            if (vPage <= 0) vSelection = 0;
                            origin.y = -vSelection;
                        }
                        canvasWithDiagram.redraw();
                    }
                }
            });
            canvasWithDiagram.addPaintListener(new PaintListener() {

                public void paintControl(PaintEvent e) {
                    if (image != null) {
                        GC gc = e.gc;
                        gc.drawImage(image, origin.x, origin.y);
                        Rectangle rect = image.getBounds();
                        Rectangle client = canvasWithDiagram.getClientArea();
                        int marginWidth = client.width - rect.width;
                        if (marginWidth > 0) {
                            gc.fillRectangle(rect.width, 0, marginWidth, client.height);
                        }
                        int marginHeight = client.height - rect.height;
                        if (marginHeight > 0) {
                            gc.fillRectangle(0, rect.height, client.width, marginHeight);
                        }
                    }
                }
            });
        }
    }

    /**
	 * Show no image info label
	 */
    private void shwoNoImageInfo() {
        refreshButton = new Button(this, SWT.CENTER);
        refreshButton.setText("Visualization missing: please generate requirements visualization first, then click me");
        refreshButton.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
            }

            @Override
            public void widgetSelected(SelectionEvent e) {
                refreshUseCaseImageView();
            }
        });
    }

    /**
	 * @return name of tab with this view
	 */
    public static String getTabName() {
        return tabName;
    }

    /**
	 * Register listener to track resource change.
	 * We want to be notify when new image arrive or when image is delete
	 */
    private void registerResourceChangeListener() {
        UseCaseImageResourceChangeListener.addResourceChangeListener(this);
    }

    /**
	 * Refresh this view to show what have change
	 */
    private void refresh() {
        getDisplay().asyncExec(new Runnable() {

            public void run() {
                refreshUseCaseImageView();
            }
        });
    }

    /**
	 * This class check if there has something change with diagram images.
	 * If yes then it will refresh image view
	 */
    private static class UseCaseImageResourceChangeListener implements IResourceChangeListener {

        private static UseCaseImageResourceChangeListener thisInstance;

        /** list of UseCaseImageView that want to bee notify about resource change*/
        private static final Set<UseCaseImageView> useCaseImageViews = new HashSet<UseCaseImageView>();

        private UseCaseImageResourceChangeListener() {
        }

        private static void addUseCaseImageView(UseCaseImageView useCaseImageView) {
            useCaseImageViews.add(useCaseImageView);
        }

        /**
		 * Start method
		 * @param useCaseImageView
		 */
        public static void addResourceChangeListener(UseCaseImageView useCaseImageView) {
            if (thisInstance == null) {
                thisInstance = new UseCaseImageResourceChangeListener();
                ResourcesPlugin.getWorkspace().addResourceChangeListener(thisInstance, IResourceChangeEvent.POST_CHANGE);
            }
            addUseCaseImageView(useCaseImageView);
        }

        /**
		 * remove listener
		 */
        public static void shutdown() {
            if (thisInstance != null) {
                ResourcesPlugin.getWorkspace().removeResourceChangeListener(thisInstance);
                useCaseImageViews.clear();
                thisInstance = null;
            }
        }

        public static void rmvResourceChangeListener(UseCaseImageView useCaseImageView) {
            useCaseImageViews.remove(useCaseImageView);
        }

        /**
		 * Check what have happen
		 */
        @Override
        public void resourceChanged(IResourceChangeEvent event) {
            try {
                event.getDelta().accept(new IResourceDeltaVisitor() {

                    public boolean visit(IResourceDelta delta) throws CoreException {
                        IPath fullPath = delta.getFullPath();
                        switch(delta.getKind()) {
                            case IResourceDelta.ADDED:
                            case IResourceDelta.REMOVED:
                            case IResourceDelta.CHANGED:
                                if (fullPath.toOSString().contains(DiagramImageManager.getDiagramImageFolderWithOSSeparator())) {
                                    refreshAll();
                                }
                                break;
                            default:
                                break;
                        }
                        return true;
                    }
                });
            } catch (CoreException ex) {
                Activator.getDefault().getSimpleExceptionLogger().log(ex, "Problem with resourceChangeEvent");
            }
        }

        /**
		 * Refresh all useCaseImageView
		 */
        private void refreshAll() {
            for (UseCaseImageView useCaseImageView : useCaseImageViews) {
                useCaseImageView.refresh();
            }
        }
    }
}
