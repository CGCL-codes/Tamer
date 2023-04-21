package org.designerator.media.thumbs.dynamic;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.designerator.common.data.Album;
import org.designerator.common.data.AlbumSelection;
import org.designerator.common.data.ThumbProxy;
import org.designerator.common.eclipse.ScrolledComposite;
import org.designerator.common.interfaces.IColorTheme;
import org.designerator.common.interfaces.IMediaView;
import org.designerator.common.interfaces.IShadowManager;
import org.designerator.common.interfaces.IThumb;
import org.designerator.common.interfaces.IThumbAlbum;
import org.designerator.common.interfaces.IThumbCacheManager;
import org.designerator.common.interfaces.IThumbListener;
import org.designerator.common.interfaces.IThumbManager;
import org.designerator.common.interfaces.IThumbsContainer;
import org.designerator.common.string.StringUtil;
import org.designerator.common.theme.ThemeDelegate;
import org.designerator.media.MediaPlugin;
import org.designerator.media.Prefs;
import org.designerator.media.albums.AlbumFilterView;
import org.designerator.media.albums.AlbumManager;
import org.designerator.media.image.Fullscreen;
import org.designerator.media.internet.common.InternetManager;
import org.designerator.media.thumbs.ShadowManager;
import org.designerator.media.thumbs.ThumbActionManager;
import org.designerator.media.thumbs.ThumbCacheManager;
import org.designerator.media.thumbs.ThumbSelectionManager;
import org.designerator.media.util.ImageHelper;
import org.designerator.media.util.ThumbScrollerDropSupport;
import org.designerator.media.util.Utils;
import org.designerator.media.views.MediaView;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.jobs.IJobChangeListener;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;

public class DynamicThumbScroller implements IAdaptable, IThumbsContainer {

    private final class IPropertyChangeListenerImplementation implements IPropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent event) {
            String property = event.getProperty();
            if (property.equals(Prefs.THUMBSBAckground)) {
                Object newValue = event.getNewValue();
                if (newValue != null) {
                    try {
                        int c = (Integer) newValue;
                        if (c == SWT.COLOR_BLACK) {
                            background = Display.getCurrent().getSystemColor(c);
                            shadowManager.setDraw(false);
                        } else if (c == SWT.COLOR_WHITE) {
                            background = Display.getCurrent().getSystemColor(SWT.COLOR_WHITE);
                        } else {
                            background = Display.getCurrent().getSystemColor(SWT.COLOR_WHITE);
                        }
                        setBackGroundColor(background, null);
                        if (sysTmpImage != null && !sysTmpImage.isDisposed()) {
                            sysTmpImage.dispose();
                            sysTmpImage = null;
                        }
                    } catch (ClassCastException e) {
                        e.printStackTrace();
                    }
                }
            } else if (property.equals(Prefs.THUMBSShadow)) {
                redrawThumbs();
            } else if (property.equals(Prefs.THUMBSBorder)) {
                redrawThumbs();
            } else if (property.equals(ThemeDelegate.THEME)) {
                Object newValue = event.getNewValue();
                if (newValue != null) {
                    IColorTheme theme = ThemeDelegate.getInstance().getTheme();
                    Color thumbBackGround = theme.getThumbBackGround();
                    if (thumbBackGround.getBlue() < 250) {
                        shadowManager.setDraw(false);
                    } else if (Prefs.getBoolean(Prefs.THUMBSShadow)) {
                        shadowManager.setDraw(true);
                    }
                    setBackGroundColor(thumbBackGround, theme.getThumbBackGroundImage());
                    setForeGroundColor(theme.getThumbForeGround());
                    if (sysTmpImage != null && !sysTmpImage.isDisposed()) {
                        sysTmpImage.dispose();
                        sysTmpImage = null;
                    }
                }
            }
        }
    }

    public static final int AlbumMargin = 0;

    Listener dropListener = new Listener() {

        @Override
        public void handleEvent(Event event) {
            if (event.data != null && event.data instanceof IContainer) {
                setThumbnailInput((IContainer) event.data, false, null);
            } else if (event.data != null && event.data instanceof String) {
                setThumbnailInput((String) event.data, false, null);
            }
        }
    };

    private ScrolledComposite scrollComposite;

    private Composite thumbsContainer;

    private ScrollBar vBar;

    List<DynamicAlbum> dynAlbums = new ArrayList<DynamicAlbum>();

    private Rectangle clientArea;

    private DynamicAlbum activeAlbum;

    protected boolean dirtyAfterResize;

    private DynamicScrollhandler scrollHandler;

    private IMediaView parentView;

    private ThumbSelectionManager selectionManager;

    private IThumbManager thumbManager;

    private IShadowManager shadowManager;

    private Color background;

    protected int thumbSizeFactor = 100;

    private Color labelbackground;

    private ThumbActionManager thumbActionManager;

    private static Image sysTmpImage;

    private static Image sysTmpVideoImage;

    private boolean showLabel;

    private boolean dynamic = true;

    private boolean cancelUpdate;

    private IThumbListener dynamicThumbListener;

    private Color foregroundColor;

    private Fullscreen fullscreen;

    public DynamicThumbScroller(IMediaView view) {
        parentView = view;
        selectionManager = new ThumbSelectionManager(this);
        thumbManager = (IThumbManager) createThumbManager();
        shadowManager = ShadowManager.getInstance();
        setPrefs();
    }

    public void add(DynamicAlbum dynamicAlbum) {
        dynAlbums.add(dynamicAlbum);
    }

    public void addPropertyChangeListener() {
        IPreferenceStore preferenceStore = MediaPlugin.getDefault().getPreferenceStore();
        IPropertyChangeListenerImplementation pl = new IPropertyChangeListenerImplementation();
        preferenceStore.addPropertyChangeListener(pl);
        PlatformUI.getPreferenceStore().addPropertyChangeListener(pl);
    }

    @Override
    public boolean cancelUpdateVisibleThumbs() {
        return cancelUpdate;
    }

    public void clearCacheSizes(int start, int length) {
        start = start - 5 < 0 ? 0 : start - 5;
        length = start + 10 < length ? start + 10 : length;
        for (int i = start; i < length; i++) {
            DynamicAlbum dynamicAlbum = dynAlbums.get(i);
            dynamicAlbum.cacheSize = null;
            if (dynamicAlbum.getControl() != null && dynamicAlbum.isVisible()) {
                dynamicAlbum.setCacheSize(null);
            }
        }
    }

    @Override
    public void clearThumbs() {
        disposeAll();
        dynAlbums.clear();
        selectionManager.clear();
    }

    public void createControl(final Composite parent) {
        scrollComposite = new ScrolledComposite(parent, SWT.V_SCROLL) {

            public void vScroll() {
                if (!dynamic) {
                    super.vScroll();
                }
            }
        };
        scrollComposite.setData(IColorTheme.NO_PAINT, new Object());
        scrollComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        scrollComposite.setExpandHorizontal(true);
        scrollComposite.setExpandVertical(true);
        if (dynamic) {
            scrollComposite.setAlwaysShowScrollBars(true);
        }
        scrollComposite.setBackground(getBackground());
        if (dynamic) {
            scrollHandler = new DynamicScrollhandler(this);
            scrollHandler.setDynamic(true);
            scrollComposite.addMouseWheelListener(scrollHandler);
        }
        thumbsContainer = createThumbsContainer(scrollComposite);
        scrollComposite.setContent(thumbsContainer);
        vBar = getScrolledComposite().getVerticalBar();
        if (vBar != null) {
            vBar.addSelectionListener(scrollHandler);
            if (dynamic) {
                vBar.setSelection(thumbsContainer.getBounds().height / 2);
            } else {
                vBar.setIncrement(60);
            }
        }
        getScrolledComposite().addControlListener(scrollHandler);
        new ThumbScrollerDropSupport(this, dropListener);
        thumbManager.setNoImages();
    }

    public IThumbManager createThumbManager() {
        return new DynamicThumbManager(this);
    }

    public Composite createThumbsContainer(ScrolledComposite scComposite) {
        Composite thumbsContainer = new Composite(scComposite, SWT.DOUBLE_BUFFERED);
        thumbsContainer.setBackground(getBackground());
        DynamicScrollhandler inputlistener2 = getScrollHandler();
        if (inputlistener2 != null) {
            thumbsContainer.addMouseMoveListener(inputlistener2);
            thumbsContainer.addMouseListener(inputlistener2);
            thumbsContainer.addKeyListener(inputlistener2);
        }
        GridLayout layout = new GridLayout(1, false);
        layout.marginHeight = 0;
        layout.horizontalSpacing = 0;
        layout.marginWidth = 0;
        layout.verticalSpacing = AlbumMargin;
        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
        thumbsContainer.setLayoutData(gridData);
        thumbsContainer.setLayout(new ScrollLayout(scComposite, dynamic));
        return thumbsContainer;
    }

    @Override
    public void dispose() {
        if (sysTmpImage != null && !sysTmpImage.isDisposed()) {
            sysTmpImage.dispose();
        }
        selectionManager.dispose();
        if (labelbackground != null && !labelbackground.isDisposed()) {
            labelbackground.dispose();
        }
        thumbManager.cancelCurrentJob();
        cancelUpdate = true;
    }

    private void disposeAll() {
        int l = dynAlbums.size();
        for (int i = 0; i < l; i++) {
            dynAlbums.get(i).dispose();
        }
    }

    private void disposeAllExcept(int e1, int e2, int e3) {
        int l = dynAlbums.size();
        for (int i = 0; i < l; i++) {
            if (!(i == e1 || i == e2 || e3 == i)) {
                dynAlbums.get(i).dispose();
            }
        }
    }

    public DynamicAlbum getActiveAlbum() {
        if (activeAlbum == null) {
            int l = dynAlbums.size();
            if (l > 0) {
                activeAlbum = dynAlbums.get(0);
            }
            return activeAlbum;
        }
        if (activeAlbum.getControl() != null) {
            return activeAlbum;
        }
        int l = dynAlbums.size();
        for (int i = 0; i < l; i++) {
            DynamicAlbum album = dynAlbums.get(i);
            if (album.getControl() != null) {
                if (album.isVisible()) {
                    return activeAlbum = album;
                }
            }
        }
        for (int i = 0; i < l; i++) {
            DynamicAlbum album = dynAlbums.get(i);
            if (album.getControl() != null) {
                return activeAlbum = album;
            }
        }
        return null;
    }

    @Override
    public IThumbAlbum getActiveDisplayAlbum() {
        DynamicAlbum aa = getActiveAlbum();
        if (aa.getParent() != null) {
            aa = aa.getParent();
        }
        return aa;
    }

    public List<DynamicAlbum> getActiveAlbums() {
        List<DynamicAlbum> actives = new ArrayList<DynamicAlbum>();
        int length = dynAlbums.size();
        for (int i = 0; i < length; i++) {
            DynamicAlbum album = dynAlbums.get(i);
            if (album.getControl() != null) {
                actives.add(album);
            }
        }
        return actives;
    }

    public List<DynamicAlbum> getActiveVisibleAlbums() {
        List<DynamicAlbum> actives = new ArrayList<DynamicAlbum>();
        int length = dynAlbums.size();
        for (int i = 0; i < length; i++) {
            DynamicAlbum album = dynAlbums.get(i);
            if (album.getControl() != null && album.isVisible()) {
                actives.add(album);
            }
        }
        return actives;
    }

    public Object getAdapter(Class adapter) {
        if (adapter == ISelectionProvider.class) {
            return selectionManager;
        }
        if (adapter == MediaView.class) {
            return parentView;
        }
        if (adapter == IEditorPart.class) {
            return parentView.getSite().getPage().getActiveEditor();
        }
        return null;
    }

    @Override
    public Color getBackground() {
        if (background == null) {
            IColorTheme theme = ThemeDelegate.getInstance().getTheme();
            background = theme.getThumbBackGround();
        }
        return background;
    }

    @Override
    public Image getBackgroundImage() {
        IColorTheme theme = ThemeDelegate.getInstance().getTheme();
        if (theme != null) {
            return theme.getThumbBackGroundImage();
        }
        return null;
    }

    @Override
    public Control[] getChildren() {
        return thumbsContainer.getChildren();
    }

    Rectangle getClientArea() {
        if (clientArea == null) {
            clientArea = getScrolledComposite().getClientArea();
        }
        return clientArea;
    }

    @Override
    public Composite getControl() {
        return scrollComposite;
    }

    public List<DynamicAlbum> getDynAlbums() {
        return dynAlbums;
    }

    public Image getEmptyThumbImage() {
        Display display = thumbsContainer.getDisplay();
        int width = ThumbProxy.DEFAULTWIDTH;
        int height = ThumbProxy.DEFAULTWIDTH;
        Image sysTmpImage = new Image(display, width, height);
        GC gc = new GC(sysTmpImage);
        gc.setBackground(getBackground());
        gc.fillRectangle(0, 0, width, height);
        gc.dispose();
        return sysTmpImage;
    }

    public static Image getEmptyVideoThumbImage(Display display) {
        int width = ThumbProxy.DEFAULTWIDTH * 4;
        int height = ThumbProxy.DEFAULTHEIGHT;
        Image sysTmpImage = new Image(display, width, height);
        Image tmp = ImageHelper.createImage(null, "/icons/large/noise.jpg");
        GC gc = new GC(sysTmpImage);
        int y = 0;
        for (int i = 0; i < 4; i++) {
            gc.drawImage(tmp, y, 0);
            y += 160;
        }
        gc.dispose();
        return sysTmpImage;
    }

    @Override
    public Color getForeGroundColor() {
        if (foregroundColor == null) {
            IColorTheme theme = ThemeDelegate.getInstance().getTheme();
            foregroundColor = theme.getThumbForeGround();
        }
        return foregroundColor;
    }

    public Image getIconImage() {
        if (thumbManager.getCurrentSystemDir() != null) {
            return ImageHelper.createImage(null, "/icons/computer.gif");
        } else if (thumbManager.getCurrentWorkSpaceDir() != null) {
            return ImageHelper.createImage(null, "/icons/workset.gif");
        } else if (thumbManager.getAlbumSelection() != null) {
            return ImageHelper.createImage(null, "/icons/albums.gif");
        }
        return null;
    }

    public boolean getIsDragSession() {
        return false;
    }

    @Override
    public Color getLabelBackground() {
        if (labelbackground != null) {
            return labelbackground;
        }
        updateLabelBackground();
        return labelbackground;
    }

    public IMediaView getParentView() {
        return parentView;
    }

    public Image getQuestionThumbImage() {
        Display display = thumbsContainer.getDisplay();
        Image sys = display.getSystemImage(SWT.ICON_QUESTION);
        Rectangle bounds = sys.getBounds();
        int width = ThumbProxy.DEFAULTWIDTH;
        int height = ThumbProxy.DEFAULTWIDTH;
        Image sysTmpImage = new Image(null, width, height);
        GC gc = new GC(sysTmpImage);
        gc.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
        gc.fillRectangle(0, 0, width, height);
        gc.drawImage(sys, (width - bounds.width) / 2, (height - bounds.height) / 2);
        gc.dispose();
        return sysTmpImage;
    }

    public ScrolledComposite getScrolledComposite() {
        return scrollComposite;
    }

    public DynamicScrollhandler getScrollHandler() {
        return scrollHandler;
    }

    public String[] getSelectedFiles() {
        return selectionManager.getSelectedFiles();
    }

    public Object[] getSelectedIResources() {
        return selectionManager.getSelectedIResources();
    }

    public ArrayList<IThumb> getSelectedThumbs() {
        return selectionManager.getSelectedThumbs();
    }

    public ThumbSelectionManager getSelectionManager() {
        return selectionManager;
    }

    public IShadowManager getShadowManager() {
        return shadowManager;
    }

    @Override
    public IThumbListener getThumbListener() {
        if (dynamicThumbListener == null) {
            dynamicThumbListener = new DynamicThumbListener(this);
        }
        return dynamicThumbListener;
    }

    @Override
    public IThumbManager getThumbManager() {
        return thumbManager;
    }

    @Override
    public Composite getThumbsContainer() {
        return thumbsContainer;
    }

    @Override
    public int getThumbSizeFactor() {
        return thumbSizeFactor;
    }

    public int getThumbsLength() {
        int size = 0;
        List<DynamicAlbum> dyna = getDynAlbums();
        for (IThumbAlbum dynamicAlbum : dyna) {
            IThumbAlbum dynamicAlbum2 = dynamicAlbum;
            Album album = dynamicAlbum2.getAlbum();
            if (album != null) {
                size += album.getSize();
            }
        }
        return size;
    }

    public Image getTmpThumb() {
        if (sysTmpImage == null) {
            sysTmpImage = getEmptyThumbImage();
        }
        return sysTmpImage;
    }

    public static Image getTmpVideoThumb(Display display) {
        if (display == null || display.isDisposed()) {
            return null;
        }
        if (sysTmpVideoImage == null || sysTmpVideoImage.isDisposed()) {
            sysTmpVideoImage = getEmptyVideoThumbImage(display);
        }
        return sysTmpVideoImage;
    }

    @Override
    public List<IContributionItem> getToolBarActions() {
        if (thumbActionManager == null) {
            thumbActionManager = new ThumbActionManager(this);
        }
        return thumbActionManager.getToolBarActions();
    }

    public void handleDynamicResize() {
        DynamicAlbum activeAlbum = getActiveAlbum();
        if (activeAlbum == null) {
            return;
        }
        setMinimumSize();
        int length = dynAlbums.size();
        for (int i = activeAlbum.getIndex(); i < length - 1; i++) {
            DynamicAlbum next = dynAlbums.get(i + 1);
            if (dynAlbums.get(i).isVisible() && !next.isVisible()) {
                if (next.getControl() != null) {
                    next.updatePosition();
                }
            }
        }
        clearCacheSizes(activeAlbum.getIndex(), length);
    }

    public void handleResize() {
        clientArea = getScrolledComposite().getClientArea();
        Point contentSize = thumbsContainer.computeSize(clientArea.width, SWT.DEFAULT);
        getScrolledComposite().setMinSize(contentSize);
    }

    DynamicAlbum insertAlbum(IThumbAlbum base, DynamicAlbum toInsert, int above) {
        Control parent = base.getControl();
        if (parent == null || parent.isDisposed()) {
            return null;
        }
        Rectangle callerBounds = parent.getBounds();
        Control control = toInsert.getControl();
        if (control == null) {
            control = toInsert.createControl(thumbsContainer);
            toInsert.createThumbs();
        }
        Point size = toInsert.computeSize(callerBounds.width, -1, true);
        if (above > 0) {
            control.setBounds(callerBounds.x, callerBounds.y + callerBounds.height + AlbumMargin + scrollHandler.getSpeed(), size.x, size.y);
        } else {
            control.moveAbove(parent);
            control.setBounds(callerBounds.x, callerBounds.y - size.y - AlbumMargin, size.x, size.y);
        }
        return toInsert;
    }

    @Override
    public boolean isShowLabel() {
        return showLabel;
    }

    public DynamicAlbum loadSibling(DynamicAlbum caller, int pos) {
        int i = caller.getIndex() + pos;
        if (i < 0 || i > dynAlbums.size() - 1) {
            return null;
        }
        DynamicAlbum album = dynAlbums.get(i);
        int index = album.getIndex();
        if (album.getControl() == null) {
            return insertAlbum(caller, album, pos);
        }
        return null;
    }

    @Override
    public boolean openFullScreen(IThumb thumb) {
        Fullscreen fullscreen = new Fullscreen(thumbsContainer.getShell(), thumb);
        fullscreen.open();
        return true;
    }

    private void redrawThumbs() {
        for (DynamicAlbum al : dynAlbums) {
            Composite control = al.getControl();
            if (control != null) {
                if (al.isVisible()) {
                    List<IThumb> thumbs = al.getThumbs();
                    for (IThumb iThumb : thumbs) {
                        iThumb.redraw(true);
                    }
                }
            }
        }
    }

    public void refresh() {
        thumbManager.refresh();
    }

    public void refreshThumbs() {
        selectionManager.refreshThumbsAfterDrag();
    }

    public void scrollIntoView(int index) {
        if (index < 0 || index > dynAlbums.size() - 1) {
            return;
        }
        IThumbAlbum album = dynAlbums.get(index);
        album.scrollIntoView();
        scrollHandler.startUpdateVisibleThumbsTimer(800);
    }

    public void selectionChanged() {
        getParentView().fireSelectionChanged(null);
    }

    public void setActiveAlbum(DynamicAlbum activeAlbum) {
        if (activeAlbum != null && activeAlbum.equals(this.activeAlbum)) {
            return;
        }
        if (activeAlbum == null) {
            return;
        }
        this.activeAlbum = activeAlbum;
        Album album = activeAlbum.getAlbum();
        if (album != null) {
            if (album.isContainer()) {
                thumbManager.setCurrentWorkspaceDir(album.getContainer());
            } else {
            }
        }
        String name = activeAlbum.getName();
        if (name != null) {
            getParentView().setTitleName(name);
        }
    }

    public void setActiveAlbum(int i) {
        if (i < 0) {
            i = 0;
        }
        if (i > dynAlbums.size()) {
            i = dynAlbums.size() - 1;
        }
        activeAlbum = dynAlbums.get(i);
        System.out.println("setActiveAlbum  " + activeAlbum.name);
        if (!activeAlbum.isVisible()) {
            System.err.println("CustomScrollTest setActiveAlbum invisible " + activeAlbum.name);
        }
        if (activeAlbum.getControl() == null) {
            System.err.println("CustomScrollTest setActiveAlbum control null " + activeAlbum.name);
        }
    }

    public void setBackground(Color background) {
        this.background = background;
    }

    private void setBackGroundColor(Color background, Image backgroundImage) {
        this.background = background;
        if (thumbsContainer == null || thumbsContainer.isDisposed()) {
            return;
        }
        thumbsContainer.setBackground(background);
        updateLabelBackground();
        if (dynAlbums != null && dynAlbums.size() > 0) {
            for (IThumbAlbum thumbAlbum : dynAlbums) {
                thumbAlbum.setBackGround(background, backgroundImage);
            }
        }
    }

    public void setCancelUpdate(boolean cancelUpdate, boolean runningJobs) {
        this.cancelUpdate = cancelUpdate;
        if (runningJobs && getThumbSizeFactor() > 100) {
            for (DynamicAlbum al : dynAlbums) {
                Composite control = al.getControl();
                if (control != null) {
                    if (al.isVisible()) {
                        List<IThumb> thumbs = al.getThumbs();
                        for (IThumb iThumb : thumbs) {
                            iThumb.cancelLoadImage();
                        }
                    }
                }
            }
        }
    }

    @Override
    public void setDragSession(boolean b) {
    }

    @Override
    public void setFocus() {
        scrollComposite.setFocus();
    }

    public void setForeGroundColor(Color thumbForeGround) {
        foregroundColor = thumbForeGround;
    }

    public void setMinimumSize() {
        clientArea = getScrolledComposite().getClientArea();
        Point contentSize = thumbsContainer.computeSize(clientArea.width, 10000);
        getScrolledComposite().setMinSize(contentSize);
        if (!dynamic) {
            System.out.println("setMinimumSize " + vBar.getSelection());
            System.out.println("setMinimumSize " + vBar.getMaximum());
            System.out.println("setMinimumSize " + vBar.getIncrement());
            System.out.println("setMinimumSize " + vBar.getThumb());
            System.out.println(clientArea);
            System.out.println(thumbsContainer.getBounds());
            vBar.setMinimum(0);
            vBar.setMaximum(contentSize.y);
            vBar.setThumb(100);
            vBar.setSelection(0);
        } else {
            scrollHandler.setScrollBarSize(getScrolledComposite());
        }
    }

    private void setPrefs() {
        IColorTheme theme = ThemeDelegate.getInstance().getTheme();
        setBackGroundColor(theme.getThumbBackGround(), theme.getThumbBackGroundImage());
        addPropertyChangeListener();
    }

    public void setScrolledComposite(ScrolledComposite scrolledComposite) {
        this.scrollComposite = scrolledComposite;
    }

    public void setSelectedThumb(IThumb newSelectedThumb, boolean fireSelectionChanged) {
        selectionManager.setSelectedThumb(newSelectedThumb, fireSelectionChanged);
    }

    public void setShowLabel(boolean showLabel) {
        this.showLabel = showLabel;
        Composite tc = getThumbsContainer();
        if (tc != null) {
            tc.layout(true);
        }
        if (thumbActionManager != null) {
            thumbActionManager.getShowLabel().setText(showLabel ? "Hide Label" : "Show Label");
        }
    }

    @Override
    public void setThumbnailInput(AlbumSelection files, IJobChangeListener listener) {
        if (thumbManager.loadThumbs(files, listener)) {
            thumbManager.getNavigation().add(files);
            thumbManager.getNavigation().reset();
        }
    }

    public void setThumbnailInput(IContainer container, boolean deep, IJobChangeListener listener) {
        if (thumbManager.loadThumbs(container, listener)) {
            thumbManager.getNavigation().add(container);
            thumbManager.getNavigation().reset();
        }
    }

    public void setThumbnailInput(IFile[] files, IJobChangeListener listener) {
    }

    public void setThumbnailInput(String container, boolean deep, IJobChangeListener listener) {
        if (thumbManager.loadThumbs(container, deep, listener)) {
            thumbManager.getNavigation().add(container);
            thumbManager.getNavigation().reset();
        }
    }

    public void setThumbnailInput(String[] files, IJobChangeListener listener) {
    }

    public void setThumbSize(int size) {
        this.thumbSizeFactor = size;
        if (dynAlbums.size() > 0) {
            for (IThumbAlbum thumbAlbum : dynAlbums) {
                if (thumbAlbum.getControl() != null) {
                    List<IThumb> ts = thumbAlbum.getThumbs();
                    for (IThumb iThumb : ts) {
                        if (iThumb != null && iThumb.getControl() != null) {
                            iThumb.setSize(size);
                        }
                    }
                    thumbAlbum.getThumbsComposite().layout();
                }
            }
            getThumbsContainer().layout(true);
            setMinimumSize();
            updateStatusMessage(null);
            if (size > 100) {
                updateVisibleThumbs();
            }
        }
    }

    @Override
    public void showAlbum(Album album) {
        if (album.isContainer()) {
            IContainer container = album.getContainer();
            if (container == null) {
                return;
            }
            IContainer currentWorkSpaceDir = thumbManager.getCurrentWorkSpaceDir();
            if (currentWorkSpaceDir == null) {
                setThumbnailInput(container, false, null);
            } else {
                IProject project1 = container.getProject();
                IProject project2 = currentWorkSpaceDir.getProject();
                if (project1 != null && project1.equals(project2)) {
                    if (dynAlbums != null && dynAlbums.size() > 1) {
                        for (IThumbAlbum thumbAlbum : dynAlbums) {
                            IContainer c = thumbAlbum.getContainer();
                            if (c != null && c.equals(container)) {
                                thumbAlbum.scrollIntoView();
                                scrollHandler.startUpdateVisibleThumbsTimer(800);
                                return;
                            }
                        }
                    }
                }
                setThumbnailInput(container, false, null);
            }
            return;
        }
        if (dynAlbums != null && dynAlbums.size() > 1) {
            String tableName = album.getTableName(false);
            if (album.isFile()) {
                if (tableName != null) {
                    for (IThumbAlbum thumbAlbum : dynAlbums) {
                        Album al = thumbAlbum.getAlbum();
                        if (al != null && tableName.equals(al.getTableName(false))) {
                            thumbAlbum.scrollIntoView();
                            scrollHandler.startUpdateVisibleThumbsTimer(800);
                            return;
                        }
                    }
                    final Album[] allAlbums = AlbumManager.getInstance().getAllAlbums();
                    AlbumSelection selection = new AlbumSelection(allAlbums, null);
                    selection.setActiveAlbum(album);
                    selection.setMode(AlbumFilterView.RELOAD);
                    setThumbnailInput(selection, null);
                }
            } else {
                if (tableName != null) {
                    for (IThumbAlbum thumbAlbum : dynAlbums) {
                        Album loadedAlbum = thumbAlbum.getAlbum();
                        if (loadedAlbum != null && loadedAlbum.getTableName(false).indexOf(tableName) != -1) {
                            thumbAlbum.scrollIntoView();
                            scrollHandler.startUpdateVisibleThumbsTimer(800);
                            break;
                        }
                    }
                }
            }
        }
    }

    public void showNextAlbum() {
        DynamicAlbum al = getActiveAlbum();
        if (al != null && (al.getIndex() < dynAlbums.size() - 1)) {
            int nextIndex = al.getIndex() + 1;
            stepToAlbum(nextIndex);
        }
    }

    public void showPreviousAlbum() {
        DynamicAlbum al = getActiveAlbum();
        if (al != null && (al.getIndex() > 0)) {
            int previousIndex = al.getIndex() - 1;
            stepToAlbum(previousIndex);
        }
    }

    private void stepToAlbum(int nextIndex) {
        thumbsContainer.setRedraw(false);
        DynamicAlbum album = dynAlbums.get(nextIndex);
        Composite control = album.getControl();
        disposeAllExcept(nextIndex - 1, nextIndex, nextIndex + 1);
        Point location = control.getLocation();
        control.setLocation(location.x, 0);
        setActiveAlbum(album);
        activeAlbum.setVisible(true);
        if (nextIndex + 1 < dynAlbums.size()) {
            DynamicAlbum album2 = insertAlbum(album, dynAlbums.get(nextIndex + 1), 1);
            album2.updatePosition();
        }
        if (nextIndex - 1 > -1) {
            insertAlbum(album, dynAlbums.get(nextIndex - 1), -1).setVisible(false);
        }
        thumbsContainer.layout(true);
        thumbsContainer.setRedraw(true);
        thumbsContainer.redraw();
        updateVisibleThumbs();
    }

    public void unLoadSibling(DynamicAlbum caller, int pos) {
        int i = caller.getIndex() + pos;
        if (i < 0 || i > dynAlbums.size() - 1) {
            return;
        }
        dynAlbums.get(i).unLoad();
    }

    @Override
    public void update(boolean skipMessages) {
        if (!skipMessages) {
            IThumb[] thumbs = thumbManager.getThumbs();
            if (parentView != null && thumbs != null) {
                updateStatusMessage(thumbs.length + " Images");
            } else if (parentView != null) {
                updateStatusMessage("No Images");
            }
        }
    }

    protected IThumbAlbum updateActiveAlbum() {
        DynamicAlbum active = getActiveAlbum();
        int start = active.getIndex() - 5 < 0 ? 0 : active.getIndex() - 5;
        int end = start + 10 < dynAlbums.size() ? start + 10 : dynAlbums.size();
        int pos = 2000;
        for (int i = start; i < end; i++) {
            DynamicAlbum album = dynAlbums.get(i);
            Composite control = album.getControl();
            if (control != null && album.isVisible()) {
                int y = Math.abs(control.getLocation().y);
                if ((y) < pos) {
                    pos = y;
                    setActiveAlbum(album);
                }
            }
        }
        return activeAlbum;
    }

    public void updateLabelBackground() {
        Color background = getBackground();
        if (background == null) {
            return;
        }
        int val = 10;
        int red = background.getRed();
        if (red < 100) {
            labelbackground = new Color(Display.getCurrent(), red + val, red + val, red + val);
        } else if (red > 180) {
            labelbackground = new Color(Display.getCurrent(), red - val, red - val, red - val);
        } else {
            labelbackground = background;
        }
    }

    public void updateStatusMessage(String message) {
        message = message == null ? "" : message;
        if (parentView != null) {
            String[] currentTables = thumbManager.getCurrentTables();
            int size = getThumbsLength();
            if (currentTables != null) {
                String album = "Album: ";
                if (currentTables.length < 3) {
                    for (int i = 0; i < currentTables.length - 1; i++) {
                        String date = currentTables[i];
                        if (!StringUtil.isEmpty(date)) {
                            album += Utils.underscoreToSlash(date) + ", ";
                        }
                    }
                    album += Utils.underscoreToSlash(currentTables[currentTables.length - 1]);
                } else {
                    album = currentTables.length + " Albums";
                }
                message += " " + album;
                message += " " + size + " file";
                if (size > 1 || size == 0) {
                    message += "s";
                }
                Album[] albums = thumbManager.getAlbumSelection().getAlbums();
                if (albums != null && albums.length > 0) {
                    String name = "Album ";
                    for (int i = 0; i < albums.length; i++) {
                        name += albums[i].getName();
                        if (i == 3) {
                            break;
                        } else if (i < albums.length - 1) {
                            name += "-";
                        }
                    }
                }
            } else {
                IContainer currentSelectionDir = thumbManager.getCurrentWorkSpaceDir();
                if (StringUtil.isEmpty(message)) {
                    message = size + " file";
                    if (size > 1 || size == 0) {
                        message += "s";
                    }
                }
                if (currentSelectionDir != null) {
                    message += ":  " + currentSelectionDir.getName();
                } else {
                    File currentSystemDir = thumbManager.getCurrentSystemDir();
                    if (currentSystemDir != null) {
                        message += ":  " + currentSystemDir.getName();
                    }
                }
            }
            if (!StringUtil.isEmpty(message)) {
                message = message + " @ " + thumbSizeFactor + "%";
            }
            Image iconImage = getIconImage();
            Image webIcon = getWebAlbumIcon();
            if (webIcon != null) {
                parentView.updateStatusMessage(message, webIcon);
            } else {
                parentView.updateStatusMessage(message, iconImage);
            }
            ((MediaView) parentView).setTitleImage(iconImage);
        }
    }

    private Image getWebAlbumIcon() {
        DynamicAlbum activeAlbum2 = getActiveAlbum();
        if (activeAlbum2 != null) {
            Image webAlbumIcon = null;
            if (activeAlbum2.getContainer() != null) {
                String f = InternetManager.getWebAlbumFromFolder(activeAlbum2);
                if (f != null) {
                    webAlbumIcon = InternetManager.getIcon(f);
                }
            } else {
                webAlbumIcon = InternetManager.getWebAlbumIcon(activeAlbum2.getAlbum());
            }
            if (webAlbumIcon != null) {
                return webAlbumIcon;
            }
        }
        return null;
    }

    @Override
    public void updateThumbPositions() {
        if (thumbManager.isVirtualMode()) {
            if (clientArea == null) {
                clientArea = scrollComposite.getClientArea();
            }
            Point location = thumbsContainer.getLocation();
            thumbManager.updateThumbPositions(location, clientArea);
        }
    }

    public void updateVisibleThumbs() {
        if (getThumbSizeFactor() <= 100) {
            return;
        }
        cancelUpdate = false;
        Rectangle clientArea = getScrolledComposite().getClientArea();
        int k = 0;
        for (DynamicAlbum al : dynAlbums) {
            Composite control = al.getControl();
            if (control != null) {
                if (al.isVisible()) {
                    if (cancelUpdate) {
                        return;
                    }
                    List<IThumb> thumbs = al.getThumbs();
                    Point loc = control.getLocation();
                    for (IThumb iThumb : thumbs) {
                        if (cancelUpdate) {
                            return;
                        }
                        Image thumbImage = iThumb.getThumbModel().getThumbImage();
                        if (thumbImage != null) {
                            if (iThumb.updateposition(loc.y, clientArea.height, 0, false) > 0) {
                                k++;
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public IThumbCacheManager getThumbCacheManager() {
        return ThumbCacheManager.getInstance();
    }
}
