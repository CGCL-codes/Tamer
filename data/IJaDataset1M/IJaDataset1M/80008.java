package org.designerator.media.thumbs.dynamic;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.designerator.common.data.Album;
import org.designerator.common.data.MediaFile;
import org.designerator.common.interfaces.IThumb;
import org.designerator.common.interfaces.IThumbAlbum;
import org.designerator.common.interfaces.IThumbManager;
import org.designerator.common.interfaces.IThumbsContainer;
import org.designerator.common.string.StringUtil;
import org.designerator.media.albums.AlbumLabelProvider;
import org.designerator.media.albums.AlbumManager;
import org.designerator.media.internet.common.InternetManager;
import org.designerator.media.thumbs.FontRegistry;
import org.designerator.media.thumbs.SingleJobRule;
import org.designerator.media.thumbs.ThumbContextMenuHandler;
import org.designerator.media.thumbs.ThumbFileUtils;
import org.designerator.media.thumbs.ThumbUtils;
import org.designerator.media.util.ImageHelper;
import org.designerator.media.util.Utils;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

public class DynamicAlbum implements IThumbAlbum {

    private static final String SLASH = "/";

    private final class ChangeListener implements Listener {

        @Override
        public void handleEvent(Event event) {
            if (main != null && !main.isDisposed()) {
                main.getDisplay().syncExec(new Runnable() {

                    @Override
                    public void run() {
                        updateDiscription();
                    }
                });
            }
        }
    }

    private static final String N_A = "N/A";

    protected Composite main;

    private int index;

    protected boolean visible;

    protected int nextInt = 20;

    protected Point cacheSize;

    protected String name;

    protected GridData gridData;

    protected CLabel albumLabel;

    protected DynamicThumbScroller thumbsContainer;

    private Image tmpImage;

    private Composite thumbsComposite;

    private ArrayList<IThumb> thumbs;

    private Album album;

    private File folder;

    private IThumb[] thumbArray;

    private boolean showDiscription = true;

    private boolean needsLayout;

    private Job loadThumbsJobs;

    private ArrayList<DynamicAlbum> children;

    private DynamicAlbum parentAlbum;

    private boolean updateSiblings = false;

    protected Image bufferImage;

    private Label sizeLabel;

    private Composite titleComposite;

    private ChangeListener changeListener;

    private Label onlineLabel;

    public DynamicAlbum(Album album, DynamicThumbScroller thumbScroller, int index) {
        if (album == null || thumbScroller == null) {
            SWT.error(SWT.ERROR_INVALID_ARGUMENT);
        }
        this.index = index;
        this.thumbsContainer = thumbScroller;
        thumbs = new ArrayList<IThumb>();
        this.thumbsContainer.add(this);
        this.album = album;
    }

    public DynamicAlbum(DynamicAlbum parentAlbum, Album album2, DynamicThumbScroller thumbScroller, int index) {
        this(album2, thumbScroller, index);
        this.parentAlbum = parentAlbum;
        this.parentAlbum.add(this);
        setShowDiscription(false);
    }

    public void add(DynamicAlbum childAlbum) {
        if (children == null) {
            children = new ArrayList<DynamicAlbum>();
        }
        children.add(childAlbum);
    }

    @Override
    public void add(IThumb iThumb) {
        thumbs.add(iThumb);
    }

    public Point computeSize(int width, int height, boolean changed) {
        if (cacheSize == null) {
            cacheSize = main.computeSize(width, height, true);
        }
        return cacheSize;
    }

    public Composite createControl(Composite parent) {
        if (parent == null || parent.isDisposed()) {
            return null;
        }
        main = new Composite(parent, SWT.None);
        gridData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
        main.setLayoutData(gridData);
        GridLayout gridLayout = new GridLayout(1, false);
        gridLayout.verticalSpacing = 0;
        main.setLayout(new AlbumLayout());
        main.setBackground(thumbsContainer.getBackground());
        if (name == null) {
            name = N_A;
        }
        if (showDiscription) {
            createDescription(album);
        }
        createThumbscomposite();
        return main;
    }

    @Override
    public void createControl(Composite parent, Album album) {
        if (parent == null || parent.isDisposed()) {
            return;
        }
        createControl(parent);
    }

    @Override
    public void createDescription(Album album) {
        titleComposite = new Composite(main, SWT.NONE);
        titleComposite.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, true));
        titleComposite.setLayout(new GridLayout(3, false));
        albumLabel = new CLabel(titleComposite, SWT.NONE);
        albumLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.BEGINNING, false, false));
        onlineLabel = new Label(titleComposite, SWT.None);
        onlineLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, true, false));
        sizeLabel = new Label(titleComposite, SWT.None);
        sizeLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.BEGINNING, false, false));
        Font font = FontRegistry.getAlbum(albumLabel.getDisplay());
        albumLabel.setFont(font);
        sizeLabel.setFont(font);
        Color labelBackground = thumbsContainer.getLabelBackground();
        albumLabel.setBackground(labelBackground);
        sizeLabel.setBackground(labelBackground);
        onlineLabel.setBackground(labelBackground);
        titleComposite.setBackground(labelBackground);
        if (labelBackground.getRed() < 50) {
            Color c = albumLabel.getDisplay().getSystemColor(SWT.COLOR_GRAY);
            albumLabel.setForeground(c);
            sizeLabel.setForeground(c);
        } else {
            Color c = albumLabel.getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY);
            albumLabel.setForeground(c);
            sizeLabel.setForeground(c);
        }
        String text = "";
        if (album != null) {
            decorateOnlineLabel(album);
            if (album.isContainer()) {
                final IContainer container = album.getContainer();
                if (container != null) {
                    if (container.getParent() != null) {
                        text = container.getParent().getName() + SLASH + container.getName();
                    } else {
                        text += container.getName();
                    }
                    final IPath location = container.getLocation();
                    if (location != null) {
                        albumLabel.setToolTipText(location.toOSString());
                    }
                }
                albumLabel.setImage(ImageHelper.createImage(null, "/icons/workset.gif"));
            } else if (album.getType() == Album.Date) {
                text = AlbumManager.getInstance().getAlbumDateName(album);
                albumLabel.setImage(ImageHelper.createImage(null, "/icons/bw/dates.gif"));
            } else {
                text = album.getName();
                albumLabel.setImage(ImageHelper.createImage(null, AlbumLabelProvider.CATEGORY_GIF));
            }
            sizeLabel.setText(getSizeText());
            changeListener = new ChangeListener();
            album.addChangeListener(changeListener);
        } else {
            text = name != null ? name : "Empty";
            albumLabel.setImage(ImageHelper.createImage(null, AlbumLabelProvider.CATEGORY_GIF));
        }
        albumLabel.setText(text);
        albumLabel.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseDown(MouseEvent e) {
                ((DynamicThumbScroller) thumbsContainer).setActiveAlbum(DynamicAlbum.this);
            }
        });
    }

    private void decorateOnlineLabel(Album album) {
        Image webAlbumIcon = InternetManager.getWebAlbumIcon(album);
        if (webAlbumIcon != null) {
            onlineLabel.setImage(webAlbumIcon);
            String[] webAlbums = album.getWebAlbums();
            if (webAlbums != null) {
                String tt = "";
                final String albumtext = "Online Album at: ";
                for (int i = 0; i < webAlbums.length; i++) {
                    if (!StringUtil.isEmpty(webAlbums[i])) {
                        tt += albumtext + webAlbums[i] + "\n";
                    }
                }
                onlineLabel.setToolTipText(tt);
            }
        } else {
            onlineLabel.setImage(null);
            onlineLabel.setToolTipText(null);
        }
    }

    public void updateDiscription() {
        if (sizeLabel == null || album == null) {
            return;
        }
        sizeLabel.setText(getSizeText());
        decorateOnlineLabel(album);
    }

    private String getSizeText() {
        int size = album.getSize();
        if (children != null && children.size() > 0) {
            for (IThumbAlbum child : children) {
                size += child.getAlbum().getSize();
            }
        }
        return Integer.toString(size) + "    ";
    }

    public void createThumbs() {
        if (sizeChanged()) {
            runRefreshJob();
            final Composite tc = getThumbsComposite();
            if (tc != null && !tc.isDisposed()) {
                final MediaFile[] mediaFiles = getMediaFiles();
                if (mediaFiles == null || mediaFiles.length < 1) {
                    return;
                }
                for (int i = 0; i < mediaFiles.length; i++) {
                    if (mediaFiles[i] != null) {
                        boolean found = false;
                        for (IThumb t : thumbs) {
                            if (t.getThumbModel().getMediaFile().equals(mediaFiles[i])) {
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            ThumbUtils.createIThumb(DynamicAlbum.this, ThumbFileUtils.getThumbProxy(mediaFiles[i]));
                        }
                    }
                }
                main.layout(true);
            }
        } else if (thumbs.size() > 0) {
            runRefreshJob();
        } else {
            runloadJob();
        }
    }

    public boolean sizeChanged() {
        if (album != null && thumbs != null && album.getMediaFiles() != null) {
            return album.getMediaFiles().length != thumbs.size();
        }
        return false;
    }

    public void createThumbscomposite() {
        thumbsComposite = new Composite(main, SWT.NORMAL);
        thumbsComposite.setBackground(thumbsContainer.getBackground());
        thumbsComposite.setBackgroundImage(thumbsContainer.getBackgroundImage());
        ThumbRowLayout rowLayout = new ThumbRowLayout(SWT.HORIZONTAL);
        rowLayout.wrap = true;
        rowLayout.fill = true;
        rowLayout.spacing = 10;
        rowLayout.marginHeight = 10;
        thumbsComposite.setLayout(rowLayout);
        DynamicScrollhandler listener = thumbsContainer.getScrollHandler();
        if (listener != null) {
            thumbsComposite.addMouseMoveListener(listener);
            thumbsComposite.addMouseListener((MouseListener) listener);
            thumbsComposite.addKeyListener((KeyListener) listener);
        }
        thumbsComposite.addMenuDetectListener(new MenuDetectListener() {

            @Override
            public void menuDetected(MenuDetectEvent e) {
                ThumbContextMenuHandler.createPrefsContextMenu(e, thumbsComposite, thumbsContainer.getBackground());
                thumbsContainer.setActiveAlbum(DynamicAlbum.this);
            }
        });
    }

    public void dispose() {
        if (loadThumbsJobs != null) {
            loadThumbsJobs.cancel();
        }
        if (main != null) {
            main.dispose();
            main = null;
        }
        albumLabel = null;
        sizeLabel = null;
        titleComposite = null;
        if (changeListener != null) {
            if (album != null) {
                album.removeChangeListener(changeListener);
            }
            changeListener = null;
        }
        setCacheSize(null);
        visible = false;
        if (thumbs != null) {
            for (IThumb t : thumbs) {
                t.dispose();
            }
        }
        if (tmpImage != null && !tmpImage.isDisposed()) {
            tmpImage.dispose();
        }
    }

    @Override
    public void dispose(IThumb iThumb) {
        thumbs.remove(iThumb);
        iThumb.dispose();
    }

    @Override
    public Album getAlbum() {
        return album;
    }

    public Point getCacheSize() {
        return cacheSize;
    }

    public ArrayList<DynamicAlbum> getChildren() {
        return children;
    }

    @Override
    public IContainer getContainer() {
        if (album != null && album.isContainer()) {
            return album.getContainer();
        }
        return null;
    }

    public Composite getControl() {
        return main;
    }

    @Override
    public File getFolder() {
        return folder;
    }

    public int getIndex() {
        return index;
    }

    public MediaFile[] getMediaFiles() {
        if (album.getMediaFiles() != null) {
            return album.getMediaFiles();
        }
        return ThumbUtils.getMediaFiles(album);
    }

    @Override
    public String getName() {
        if (albumLabel != null) {
            return albumLabel.getText();
        }
        if (parentAlbum != null) {
            return parentAlbum.getName();
        }
        if (album.isContainer()) {
            return album.getContainer().getName();
        }
        if (name != null && !name.equals(N_A)) {
            return name;
        }
        if (thumbs != null && thumbs.size() > 0) {
            IThumb iThumb = thumbs.get(0);
            if (iThumb.getFile() != null) {
                IContainer parent = iThumb.getFile().getParent();
                if (parent != null) {
                    name = parent.getName();
                }
            } else if (iThumb.getSysfile() != null) {
                File parentFile = iThumb.getSysfile().getParentFile();
                if (parentFile != null) {
                    name = parentFile.getName();
                }
            }
        }
        if (album != null && StringUtil.isEmpty(name) && !StringUtil.isEmpty(album.getName())) {
            name = album.getName();
        }
        return name;
    }

    public DynamicAlbum getParent() {
        return parentAlbum;
    }

    @Override
    public Image getTempImage() {
        if (tmpImage == null) {
            return thumbsContainer.getTmpThumb();
        }
        return tmpImage;
    }

    @Override
    public IThumbsContainer getThumbContainer() {
        return thumbsContainer;
    }

    @Override
    public List<IThumb> getThumbs() {
        return thumbs;
    }

    @Override
    public List<IThumb> getThumbsWithChildren() {
        ArrayList<DynamicAlbum> children = getChildren();
        if (children == null || children.size() < 1) {
            return thumbs;
        } else if (children.size() > 0) {
            List<IThumb> all = new ArrayList<IThumb>(IThumbManager.MAX_THUMBS * (children.size() + 1));
            all.addAll(thumbs);
            for (IThumbAlbum dya : children) {
                all.addAll(dya.getThumbs());
            }
            return all;
        }
        return thumbs;
    }

    @Override
    public IThumb[] getThumbsAsArray() {
        if (thumbArray == null) {
            thumbArray = (thumbs.toArray(new IThumb[thumbs.size()]));
        }
        return thumbArray;
    }

    @Override
    public Composite getThumbsComposite() {
        return thumbsComposite;
    }

    @Override
    public int getVisibility() {
        return 0;
    }

    @Override
    public boolean isAlbumMode() {
        return album != null;
    }

    @Override
    public boolean isDisposed() {
        return main == null || main.isDisposed();
    }

    public boolean isExclude() {
        return gridData.exclude;
    }

    @Override
    public boolean isShowDiscription() {
        return showDiscription;
    }

    public boolean isVisible() {
        return visible;
    }

    @Override
    public void layout() {
        cacheSize = null;
        if (main != null && !main.isDisposed()) {
            thumbsComposite.layout(true);
        }
    }

    public void loadThumbs3() {
        loadThumbsInUIThread();
        for (IThumb t : thumbs) {
            t.loadImage();
        }
        needsLayout = true;
    }

    public void loadThumbsInUIThread() {
        final Composite tc = getThumbsComposite();
        if (tc != null && !tc.isDisposed()) {
            final MediaFile[] mediaFiles = getMediaFiles();
            if (mediaFiles == null || mediaFiles.length < 1) {
                return;
            }
            for (int i = 0; i < mediaFiles.length; i++) {
                if (mediaFiles[i] != null) {
                    ThumbUtils.createIThumb(DynamicAlbum.this, ThumbFileUtils.getThumbProxy(mediaFiles[i]));
                }
            }
            main.layout(true);
        }
    }

    private void loadThumbsWithThumbManager(IProgressMonitor monitor) {
        MediaFile[] mediaFiles = getMediaFiles();
        if (mediaFiles != null && mediaFiles.length > 0 && main != null) {
            thumbsContainer.getThumbManager().createThumbNails(mediaFiles, main.getDisplay(), DynamicAlbum.this, monitor, true);
        }
    }

    public void loadThumbsWithThumbManagerJob() {
        loadThumbsJobs = new Job("LoadThumbs") {

            public IStatus run(IProgressMonitor monitor) {
                loadThumbsWithThumbManager(monitor);
                return Status.OK_STATUS;
            }
        };
        loadThumbsJobs.setSystem(true);
        loadThumbsJobs.setRule(new SingleJobRule());
        loadThumbsJobs.addJobChangeListener(new JobChangeAdapter() {

            public void done(IJobChangeEvent event) {
                loadThumbsJobs = null;
                if (updateSiblings && main != null && !main.isDisposed()) {
                    main.getDisplay().syncExec(new Runnable() {

                        @Override
                        public void run() {
                        }
                    });
                }
            }
        });
        loadThumbsJobs.schedule();
    }

    public void moveToPosition() {
        if (album == null) {
            return;
        }
        int position = 0;
        Composite parent = main.getParent();
        if (parent == null || parent.isDisposed()) {
            return;
        }
        Control[] children = parent.getChildren();
        if (children != null && children.length > 1) {
            for (int i = 0; i < children.length; i++) {
                if (children[i].equals(main)) {
                    position = i;
                    break;
                }
            }
            int sortIndex = album.getSortIndex();
            if (position > 0 && position != sortIndex) {
                if (sortIndex == 0) {
                    main.moveAbove(null);
                } else if (children.length > sortIndex) {
                    main.moveAbove(children[sortIndex]);
                } else {
                    main.moveBelow(null);
                }
                parent.layout(new Control[] { main });
            }
        }
    }

    @Override
    public void remove(IThumb iThumb) {
        thumbs.remove(iThumb);
    }

    private void runloadJob() {
        if (album == null || main == null) {
            return;
        }
        MediaFile[] mediaFiles = getMediaFiles();
        if (mediaFiles == null || mediaFiles.length < 1) {
            return;
        }
        IFile iFile = mediaFiles[0].getIFile();
        if (iFile != null) {
            loadThumbsInUIThread();
        } else {
            loadThumbsWithThumbManagerJob();
        }
    }

    private void runRefreshJob() {
        Composite tc = getThumbsComposite();
        if (thumbs == null || album == null || tc == null || tc.isDisposed()) {
            return;
        }
        final int thumbSizeFactor = thumbsContainer.getThumbSizeFactor();
        for (IThumb t : thumbs) {
            t.setSize(thumbSizeFactor);
            t.createControl(tc);
        }
        for (IThumb t : thumbs) {
            t.loadImage();
        }
    }

    public boolean scroll(int y, int direction) {
        if (main != null && !main.isDisposed()) {
            Point location = main.getLocation();
            main.setLocation(location.x, location.y + y);
            scrollUpdate(location.y + y, direction);
            return true;
        }
        return false;
    }

    @Override
    public void scrollIntoView() {
        IThumbAlbum active = thumbsContainer.getActiveAlbum();
        if (active == null || active.getControl() == null) {
            return;
        }
        Rectangle activeBounds = active.getControl().getBounds();
        List<DynamicAlbum> dynAlbums = thumbsContainer.getDynAlbums();
        for (DynamicAlbum a : dynAlbums) {
            a.unLoad();
        }
        Composite control = createControl(thumbsContainer.getThumbsContainer());
        createThumbs();
        Point size = computeSize(activeBounds.width, -1, true);
        control.setBounds(activeBounds.x, 0, size.x, size.y);
        setVisible(true);
        thumbsContainer.setActiveAlbum(this);
        updateSiblings();
    }

    private void scrollUpdate(int topy, int direction) {
        int bottomy = main.getBounds().height + topy;
        int visibleHeight = thumbsContainer.getClientArea().height;
        if (topy > -1 && topy < visibleHeight) {
            setVisible(true, direction);
        } else if (bottomy > 0 && bottomy < visibleHeight) {
            setVisible(true, -1);
        } else if (bottomy < 0) {
            setVisible(false, -1);
        } else if (topy > visibleHeight) {
            setVisible(false, 1);
        }
    }

    @Override
    public void setAlbum(Album album) {
        this.album = album;
    }

    @Override
    public void setBackGround(Color background, Image backGroundImage) {
        if (Utils.inNotNull(main)) {
            main.setBackground(background);
            thumbsComposite.setBackground(background);
            thumbsComposite.setBackgroundImage(backGroundImage);
            if (albumLabel != null) {
                albumLabel.setBackground(thumbsContainer.getLabelBackground());
                sizeLabel.setBackground(thumbsContainer.getLabelBackground());
                titleComposite.setBackground(thumbsContainer.getLabelBackground());
                onlineLabel.setBackground(thumbsContainer.getLabelBackground());
                Display display = albumLabel.getDisplay();
                if (background.getRed() < 50) {
                    albumLabel.setForeground(display.getSystemColor(SWT.COLOR_DARK_GRAY));
                    sizeLabel.setForeground(display.getSystemColor(SWT.COLOR_DARK_GRAY));
                } else {
                    albumLabel.setForeground(display.getSystemColor(SWT.COLOR_GRAY));
                    sizeLabel.setForeground(display.getSystemColor(SWT.COLOR_GRAY));
                }
            }
            if (thumbs != null) {
                int size = thumbs.size();
                if (size > 0) {
                    for (int i = 0; i < size; i++) {
                        thumbs.get(i).setBackgroundColor(background);
                    }
                }
            }
        }
    }

    public void setCacheSize(Point cacheSize) {
        this.cacheSize = cacheSize;
        List<IThumb> ts = getThumbs();
        for (IThumb iThumb : ts) {
            if (iThumb != null && iThumb.getControl() != null) {
                iThumb.redraw(true);
            }
        }
    }

    @Override
    public void setContainer(IContainer container) {
    }

    public void setExclude(boolean exclude) {
        if (gridData != null) {
            gridData.exclude = exclude;
        }
    }

    @Override
    public void setFolder(File folder) {
        this.folder = folder;
    }

    public void setIndex(int i) {
        index = i;
    }

    @Override
    public void setIcon(Image icon) {
        if (albumLabel != null) {
            albumLabel.setImage(icon);
        }
    }

    public void setName(String name) {
        this.name = name;
        if (albumLabel != null && !albumLabel.isDisposed()) {
            albumLabel.setText(name);
        }
    }

    @Override
    public void setShowDiscription(boolean showDiscription) {
        this.showDiscription = showDiscription;
    }

    @Override
    public void setTempImage(Image duplicate) {
        tmpImage = duplicate;
    }

    private void setThumbArray(IThumb[] thumbArray) {
        this.thumbArray = thumbArray;
    }

    @Override
    public void setUnloaded(boolean unloaded) {
    }

    @Override
    public void setVisibility(int visibility) {
    }

    public void setVisible(boolean isVisible) {
        this.visible = isVisible;
    }

    private void setVisible(boolean isVisible, int sibling) {
        if (isVisible == this.visible) {
            return;
        }
        this.visible = isVisible;
        if (!visible) {
            thumbsContainer.unLoadSibling(this, sibling);
        } else {
            thumbsContainer.setActiveAlbum(this);
            thumbsContainer.loadSibling(this, sibling);
            if (needsLayout) {
                layout();
                needsLayout = false;
            }
        }
    }

    @Override
    public int size() {
        if (thumbs != null) {
            return thumbs.size();
        }
        return 0;
    }

    void unLoad() {
        dispose();
    }

    @Override
    public void update() {
    }

    public void updateAbove() {
        if (index - 1 > -1) {
            List<DynamicAlbum> dynAlbums = thumbsContainer.getDynAlbums();
            Rectangle callerBounds = main.getBounds();
            DynamicAlbum toInsert = dynAlbums.get(index - 1);
            Control control = toInsert.getControl();
            if (control == null) {
                control = toInsert.createControl(thumbsContainer.getThumbsContainer());
                toInsert.loadThumbsInUIThread();
            }
            Point size = toInsert.computeSize(callerBounds.width, -1, true);
            control.moveAbove(main);
            control.setBounds(callerBounds.x, callerBounds.y - size.y - 0, size.x, size.y);
            toInsert.setVisible(false);
        }
        updateSiblings = false;
    }

    @Override
    public void updateposition(Point parentLocation, Rectangle parentArea) {
    }

    public void updatePosition() {
        if (main != null && !main.isDisposed()) {
            int topy = main.getLocation().y;
            int bottomy = main.getBounds().height + topy;
            int visibleHeight = thumbsContainer.getClientArea().height;
            Point location = thumbsContainer.getActiveAlbum().getControl().getLocation();
            if (topy > -1 && topy < visibleHeight) {
                this.visible = true;
                DynamicAlbum sib = thumbsContainer.loadSibling(this, 1);
                if (sib != null) {
                    sib.updatePosition();
                }
            } else if (bottomy > 0 && bottomy < visibleHeight) {
                this.visible = true;
                IThumbAlbum sib = thumbsContainer.loadSibling(this, -1);
            } else if (bottomy < 0) {
                this.visible = false;
            } else if (topy > visibleHeight) {
                this.visible = false;
            }
        }
    }

    public void updateSiblings() {
        updatePosition();
        updateAbove();
    }

    @Override
    public void updateSize(boolean show) {
    }

    public boolean updateVisible() {
        if (main != null && !main.isDisposed()) {
            int topy = main.getLocation().y;
            int bottomy = main.getBounds().height + topy;
            int visibleHeight = thumbsContainer.getClientArea().height;
            if (topy > -1 && topy < visibleHeight) {
                this.visible = true;
            } else if (bottomy > 0 && bottomy < visibleHeight) {
                this.visible = true;
            } else if (bottomy < 0) {
                this.visible = false;
            } else if (topy > visibleHeight) {
                this.visible = false;
            }
        }
        return visible;
    }
}
