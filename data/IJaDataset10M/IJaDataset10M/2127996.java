package org.designerator.media.thumbs.one;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.designerator.common.data.AlbumSelection;
import org.designerator.common.data.DataBaseInfo;
import org.designerator.common.data.ProcessorDataList;
import org.designerator.common.data.ThumbProxy;
import org.designerator.common.interfaces.IShadowManager;
import org.designerator.common.interfaces.IThumb;
import org.designerator.common.interfaces.IThumbAlbum;
import org.designerator.common.interfaces.IThumbSaveHandler;
import org.designerator.common.interfaces.IThumbSelectionManager;
import org.designerator.common.interfaces.IThumbShadow;
import org.designerator.common.interfaces.IThumbsContainer;
import org.designerator.common.interfaces.IToolTipWindow;
import org.designerator.common.string.StringUtil;
import org.designerator.media.database.DataBase;
import org.designerator.media.image.util.IO;
import org.designerator.media.presentation.ThemeManager;
import org.designerator.media.thumbs.FontRegistry;
import org.designerator.media.thumbs.ImagePropertySource;
import org.designerator.media.thumbs.ImageSystemPropertySource;
import org.designerator.media.thumbs.ShadowManager;
import org.designerator.media.thumbs.ThumbDragSourceHandler2;
import org.designerator.media.thumbs.ThumbIO;
import org.designerator.media.thumbs.VideoPropertySource;
import org.designerator.media.util.Utils;
import org.designerator.media.views.MediaView;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.views.properties.IPropertySource;

public class Thumb1AutoComposite implements IThumb, ISelection {

    public final class ThumbCanvas extends Canvas {

        private static final int insetx = 12;

        private static final int insety = 6;

        private boolean visible = true;

        private boolean drawShadow = true;

        Color bc;

        private Image tmpImage;

        private IThumbShadow shadow = null;

        private int tmpWidth;

        private int tmpHeight;

        private ThumbCanvas(Composite parent, int style) {
            super(parent, style);
            setBackground(parentContainer.getBackground());
            final GridData gd_canvas = new GridData(SWT.CENTER, SWT.CENTER, true, true);
            setLayoutData(gd_canvas);
            bc = getDisplay().getSystemColor(SWT.COLOR_GRAY);
            updateShadow(width, height);
            addPaintListener(new PaintListener() {

                public void paintControl(PaintEvent e) {
                    draw(e);
                }
            });
        }

        public Point computeSize(int wHint, int hHint, boolean changed) {
            int height2 = height < MINHEIGHT ? MINHEIGHT : height;
            int width2 = width < MINWIDTH ? MINWIDTH : width;
            return new Point(width2 + insetx, height2 + insety);
        }

        public void draw(PaintEvent e) {
            if (thumbImage != null && !thumbImage.isDisposed()) {
                drawImage(e, srcWidth, srcHeight);
            } else if (tmpImage != null && !tmpImage.isDisposed()) {
                drawImage(e, tmpWidth, tmpHeight);
            } else {
                e.gc.setBackground(bc);
                e.gc.fillRectangle(canvas.getClientArea());
            }
        }

        public void drawImage(PaintEvent e, int srcWidth, int srcHeight) {
            Rectangle clientArea = getClientArea();
            int x = 0;
            int y = 0;
            if (width < clientArea.width) {
                x = (clientArea.width - width) / 2;
            }
            if (height < clientArea.height) {
                y = (clientArea.height - height) / 2;
            }
            e.gc.fillRectangle(clientArea);
            drawShadow(e, x, y);
            e.gc.drawImage(thumbImage, 0, 0, srcWidth, srcHeight, x, y, width, height);
            if (shadowManager.isDrawBorder()) {
                e.gc.setForeground(shadowManager.getBorder());
                e.gc.drawRectangle(x, y, width - 1, height - 1);
            }
        }

        public void drawShadow(PaintEvent e, int x, int y) {
            if (drawShadow && shadowManager.isDraw()) {
                if (shadow != null) {
                    shadow.draw(e.gc, x, y, width, height);
                } else {
                    shadow = shadowManager.getShadow(width, height);
                    shadow.draw(e.gc, x, y, width, height);
                }
            }
        }

        public boolean isDrawShadow() {
            return drawShadow;
        }

        public boolean isVisible() {
            return visible;
        }

        public void loadImage() {
            if (thumbImage != null && !thumbImage.isDisposed()) {
                return;
            }
            final Display display = getDisplay();
            Job thumbsJobs = new Job("LoadThumbs") {

                public IStatus run(IProgressMonitor monitor) {
                    if (thumbModel.thumbPath != null && !thumbModel.isIcon) {
                        final Image img;
                        img = IO.loadImage(thumbModel.thumbPath, display, false, true);
                        display.syncExec(new Runnable() {

                            public void run() {
                                updateImage(img);
                                setDrawShadow(true);
                            }
                        });
                    } else if (thumbModel.getMediaPath() != null && thumbModel.isIcon) {
                        final Image img;
                        img = IO.loadImage(thumbModel.getMediaPath(), display, false, true);
                        display.syncExec(new Runnable() {

                            public void run() {
                                updateImage(img);
                            }
                        });
                    } else if (thumbModel.getMediaPath() != null) {
                        final ThumbProxy thumb = ThumbIO.createImageThumbModel(display, thumbModel.getMediaFile(), null);
                        display.syncExec(new Runnable() {

                            public void run() {
                                Thumb1AutoComposite.this.thumbModel = thumb;
                                Image img = thumb.getThumbImage();
                                updateImage(img);
                                setDrawShadow(true);
                            }
                        });
                    }
                    return Status.OK_STATUS;
                }
            };
            thumbsJobs.setSystem(true);
            thumbsJobs.schedule();
        }

        public void setDrawShadow(boolean drawShadow) {
            this.drawShadow = drawShadow;
        }

        public void setTmpImage(Image image) {
            this.tmpImage = image;
            if (image != null) {
                Rectangle bounds = image.getBounds();
                tmpWidth = bounds.width;
                tmpHeight = bounds.height;
            }
        }

        public void setVisible(boolean visible) {
            this.visible = visible;
        }

        public void unLoadImage() {
            if (thumbImage != null && !thumbImage.isDisposed()) {
                thumbImage.dispose();
                thumbModel.setThumbImages(null);
                thumbImage = null;
            }
        }

        public void updateImage(Image img) {
            if (img != null) {
                thumbModel.setThumbImages(img);
                thumbImage = img;
                setThumbSize(thumbImage.getBounds());
                main.layout(true);
                redraw();
            }
        }

        public void updateShadow(int width, int height) {
            if (width < 64 || height < 64) {
                setDrawShadow(false);
                return;
            }
            if (!ShadowManager.getInstance().isDraw()) {
                return;
            }
            if (shadowManager != null) {
                shadow = shadowManager.getShadow(width, height);
            } else {
                shadow = ShadowManager.getInstance().getShadow(width, height);
            }
        }
    }

    private final class ThumbSelectListener implements MouseListener, MouseMoveListener, KeyListener, MouseTrackListener {

        public boolean isCateGoryMode() {
            return false;
        }

        @Override
        public void keyPressed(KeyEvent e) {
            parentContainer.getSelectionManager().keyPressed(e);
        }

        @Override
        public void keyReleased(KeyEvent e) {
            parentContainer.getSelectionManager().keyReleased(e);
        }

        public void mouseDoubleClick(MouseEvent e) {
            final Object obj = getParentContainer().getAdapter(MediaView.class);
            if (obj == null) {
                return;
            }
            MediaView parent = (MediaView) obj;
            parent.showImageCanvas();
            parent.setImageInput(Thumb1AutoComposite.this);
        }

        public void mouseDown(MouseEvent e) {
            IThumbSelectionManager selectionManager = parentContainer.getSelectionManager();
            selectionManager.thumbToolTipMouseDown(e);
            updateDataBaseInfo();
            if (e.button == 1 && isCateGoryMode()) {
                return;
            }
            if (selectionManager.checkIfselected(Thumb1AutoComposite.this) != -1) {
                isSelected = true;
            }
            if (e.button == 1 && (e.stateMask & SWT.CTRL) == 0) {
                selectionManager.setSelectedThumb(Thumb1AutoComposite.this, true);
            }
            if (e.button == 3) {
                selectionManager.setSelectedThumb(Thumb1AutoComposite.this, true);
            }
            if (canvas != null && !canvas.isDisposed()) {
                canvas.setFocus();
            }
        }

        @Override
        public void mouseEnter(MouseEvent e) {
            if (isShowToolTip()) {
                parentContainer.getSelectionManager().mouseEnter(e);
            }
        }

        @Override
        public void mouseExit(MouseEvent e) {
            if (isShowToolTip()) {
                parentContainer.getSelectionManager().mouseExit(e);
            }
        }

        @Override
        public void mouseHover(MouseEvent e) {
            if (isShowToolTip()) {
            }
        }

        public void mouseMove(MouseEvent e) {
            if (isSelected) {
                parentContainer.setDragSession(false);
            }
        }

        public void mouseUp(MouseEvent e) {
            if (isCateGoryMode()) {
                canvas.setFocus();
            } else {
                IThumbSelectionManager selectionManager = parentContainer.getSelectionManager();
                if ((e.button == 1) && (e.stateMask & SWT.CTRL) == 0) {
                    selectionManager.clearSelectedThumbs(Thumb1AutoComposite.this);
                } else if ((e.button == 1) && (e.stateMask & SWT.CTRL) != 0) {
                    selectionManager.addSelectedThumb(Thumb1AutoComposite.this);
                }
                isSelected = false;
                parentContainer.setDragSession(false);
                canvas.setFocus();
            }
        }
    }

    public static final int MINWIDTH = 96;

    public static final int MINHEIGHT = 96;

    public static final int minSize = 128;

    private static boolean showToolTip = true;

    static GridLayout gridLayout;

    public static boolean isShowToolTip() {
        return showToolTip;
    }

    public static void setShowToolTip(boolean showToolTip2) {
        showToolTip = showToolTip2;
    }

    public int indexKey = -1;

    public List<String> categories;

    private Image thumbImage;

    public int height = 80;

    public int width = 120;

    public int srcWidth = 120;

    public int srcHeight = 80;

    private IThumbsContainer parentContainer;

    public Composite main;

    String name;

    private File sysfile;

    public IFile file;

    private ThumbProxy thumbModel;

    public ThumbCanvas canvas;

    private DragSource dragSource;

    private boolean isSelected = false;

    private ThumbSelectListener selectionListener;

    private Label label;

    public boolean invisible;

    private IShadowManager shadowManager;

    public boolean offline;

    private String tableNames;

    public DataBaseInfo dataBaseInfo;

    public IThumbAlbum thumbAlbum;

    public Thumb1AutoComposite(IThumbAlbum thumbAlbum) {
        this.parentContainer = thumbAlbum.getThumbContainer();
        this.thumbAlbum = thumbAlbum;
        this.thumbAlbum.add(this);
        shadowManager = parentContainer.getShadowManager();
    }

    public void createControl(Control parent) {
        if (thumbModel != null && parent != null && !parent.isDisposed()) {
            createControl(thumbImage, name, parent);
        } else {
            SWT.error(SWT.ERROR_INVALID_ARGUMENT, null);
        }
    }

    public void createControl(Image image, String name, Control parent) {
        this.thumbImage = image;
        this.name = name;
        if (thumbImage != null) {
            Rectangle bounds = thumbImage.getBounds();
            setThumbSize(bounds.width, bounds.height);
        } else {
            setThumbSize(thumbModel.getThumbWidth(), thumbModel.getThumbHeight());
        }
        main = Utils.createSimpleComposite(parent, getLayout(), SWT.None);
        main.setBackground(parentContainer.getBackground());
        canvas = new ThumbCanvas(main, SWT.NONE);
        canvas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        selectionListener = new ThumbSelectListener();
        if (isShowlabel() && this.name != null) {
            createLabel(this.name);
        }
        dragSource = Utils.createDragSource(canvas, IThumbsContainer.TRANSFERS);
        ThumbDragSourceHandler2 listener = new ThumbDragSourceHandler2((ISelectionProvider) parentContainer.getAdapter(ISelectionProvider.class));
        listener.setParent(parentContainer);
        dragSource.addDragListener(listener);
        canvas.addMouseListener(selectionListener);
        canvas.addMouseMoveListener(selectionListener);
        canvas.addKeyListener(selectionListener);
        canvas.addMouseTrackListener(selectionListener);
    }

    public GridLayout getLayout() {
        if (gridLayout == null) {
            gridLayout = new GridLayout();
            gridLayout.marginTop = 0;
            gridLayout.marginRight = 0;
            gridLayout.marginLeft = 0;
            gridLayout.marginBottom = 0;
            gridLayout.verticalSpacing = 5;
            gridLayout.marginWidth = 2;
            gridLayout.marginHeight = 2;
            gridLayout.horizontalSpacing = 0;
        }
        return gridLayout;
    }

    public void createLabel(String name) {
        if (name == null) {
            return;
        }
        label = new Label(main, SWT.CENTER);
        GridData layoutData = new GridData(SWT.FILL, SWT.CENTER, true, false);
        label.setLayoutData(layoutData);
        label.setBackground(ThemeManager.thumbbackground);
        label.setFont(FontRegistry.getSmall(label));
        label.setText(name);
        label.addMouseListener(selectionListener);
        label.addMouseMoveListener(selectionListener);
        label.addKeyListener(selectionListener);
    }

    public void dispose() {
        if (main != null && !main.isDisposed()) {
            main.dispose();
            main = null;
        }
        disposeImage();
        if (dragSource != null) {
            dragSource.dispose();
        }
    }

    @Override
    public void disposeImage() {
        if (thumbModel.thumbImages != null) {
            for (int i = 0; i < thumbModel.thumbImages.length; i++) {
                if (thumbModel.thumbImages[i] != null && !thumbModel.thumbImages[i].isDisposed()) {
                    thumbModel.thumbImages[i].dispose();
                }
            }
        }
    }

    @Override
    public Object getAdapter(Class adapter) {
        if (adapter == IResource.class) {
            return getFile();
        }
        if (adapter == IPropertySource.class) {
            if (thumbModel.isVideo) {
                VideoPropertySource imagePropertySource = new VideoPropertySource(getFile(), this);
                return imagePropertySource;
            } else {
                IFile file2 = getFile();
                if (file2 != null) {
                    ImagePropertySource imagePropertySource = new ImagePropertySource(this);
                    return imagePropertySource;
                }
                File f = getSysfile();
                if (f != null) {
                    ImageSystemPropertySource imagePropertySource = new ImageSystemPropertySource(f);
                    imagePropertySource.setThumb(this);
                    return imagePropertySource;
                }
            }
        }
        if (adapter == File.class) {
            return getSysfile();
        }
        if (adapter == IThumbsContainer.class) {
            return parentContainer;
        }
        if (adapter == IThumb.class) {
            return this;
        }
        return null;
    }

    public Control getCanvas() {
        return canvas;
    }

    public List<String> getCategories() {
        if (categories == null) {
            categories = new ArrayList<String>();
        }
        return categories;
    }

    public Control getControl() {
        return main;
    }

    public DataBaseInfo getDataBaseInfo() {
        if (dataBaseInfo == null) {
            updateDataBaseInfo();
        }
        if (dataBaseInfo == null) {
            return new DataBaseInfo();
        }
        return dataBaseInfo;
    }

    @Override
    public IFile getFile() {
        return file;
    }

    public int getIndexKey() {
        if (indexKey < 1) {
            indexKey = DataBase.getIDProperty(file);
        }
        if (indexKey < 1) {
            indexKey = DataBase.selectIndexFromMediaTable(file);
        }
        return indexKey;
    }

    public String getOfflinePath() {
        if (getFile() == null) {
            return "";
        }
        if (!StringUtil.isEmpty(thumbModel.getOffLinePath())) {
            return thumbModel.getOffLinePath();
        }
        if (getIndexKey() < 0) {
            updateDataBaseInfo();
        }
        if (isOffline() && getIndexKey() > 0) {
            try {
                int[] i = new int[1];
                i[0] = getIndexKey();
                final List<DataBaseInfo> offlinePaths = DataBase.selectOfflinePaths(i);
                if (offlinePaths != null && offlinePaths.size() > 0) {
                    thumbModel.setOffLinePath(offlinePaths.get(0).getOffLinePath());
                    return thumbModel.getOffLinePath();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public IThumbsContainer getParentContainer() {
        return parentContainer;
    }

    public Point getSize() {
        return new Point(width, height);
    }

    @Override
    public File getSysfile() {
        if (sysfile == null) {
            if (thumbModel.getMediaPath() != null) {
                sysfile = new File(thumbModel.getMediaPath());
            } else if (file != null) {
                sysfile = file.getLocation().toFile();
            }
        }
        return sysfile;
    }

    @Override
    public ThumbProxy getThumbModel() {
        if (thumbModel == null) {
            return new ThumbProxy(0);
        }
        return thumbModel;
    }

    @Override
    public boolean isEmpty() {
        return thumbImage != null;
    }

    public boolean isOffline() {
        if (getIndexKey() < 1) {
            updateDataBaseInfo();
        }
        return offline;
    }

    @Override
    public boolean isShowlabel() {
        return parentContainer.isShowLabel();
    }

    public void layout() {
        main.pack(true);
    }

    public void redraw(boolean deleteBuffer) {
        Rectangle clientArea = main.getClientArea();
        main.redraw(0, 0, clientArea.width, clientArea.height, true);
        canvas.redraw();
    }

    public void removeCategory(String string) {
        if (string == null) {
            return;
        }
        if (categories != null) {
            try {
                categories.remove(string);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void setBackgroundColor(Color color) {
        if (!Utils.inNotNull(main)) {
            return;
        }
        main.setBackground(color);
        canvas.setBackground(color);
        if (label != null) {
            label.setBackground(color);
        }
        if (ThemeManager.thumbselected.equals(color)) {
            canvas.setDrawShadow(false);
        } else {
            canvas.setDrawShadow(true);
        }
        canvas.redraw();
        main.redraw();
    }

    public void setCategories(String albums) {
        if (albums != null && albums.length() > 0) {
            String[] thumbTableNames = albums.split(";");
            if (thumbTableNames != null && thumbTableNames.length > 0) {
                AlbumSelection activeCategory = parentContainer.getThumbManager().getAlbumSelection();
                this.categories = new ArrayList<String>();
                if (activeCategory != null) {
                    String[] viewTableNames = activeCategory.getTables();
                    if (viewTableNames != null && viewTableNames.length > 1) {
                        for (int i = 0; i < thumbTableNames.length; i++) {
                            if (!StringUtil.isEmpty(thumbTableNames[i])) {
                                for (int j = 0; j < viewTableNames.length; j++) {
                                    if (thumbTableNames[i].equals(viewTableNames[j])) {
                                        this.categories.add(thumbTableNames[i]);
                                    }
                                }
                            }
                        }
                    } else {
                        this.categories.addAll(Arrays.asList(thumbTableNames));
                    }
                } else {
                    this.categories.addAll(Arrays.asList(thumbTableNames));
                }
            }
        }
    }

    public void setDataBaseInfo(DataBaseInfo dataBaseInfo) {
        this.dataBaseInfo = dataBaseInfo;
    }

    public void setOnlineData(String provider, String onlineAlbumId, String data, int id) {
        this.dataBaseInfo.setData(data);
        DataBase.insertIntoOnlineAlbum(null, getIndexKey(), onlineAlbumId, this.dataBaseInfo.getSecId(), data);
    }

    @Override
    public void setIFile(IFile iFile) {
        file = iFile;
    }

    public void setIndexKey(int indexKey) {
        this.indexKey = indexKey;
    }

    public void setOffline(boolean offline) {
        this.offline = offline;
    }

    public void setSelected(boolean selected) {
        this.isSelected = selected;
    }

    public void setShowlabel(boolean showlabel) {
        if (label == null) {
            if (name == null) {
                setName(thumbModel);
            }
            createLabel(name);
        }
        final GridData layoutData = (GridData) label.getLayoutData();
        if (showlabel) {
            layoutData.exclude = false;
        } else {
            layoutData.exclude = true;
        }
        label.setVisible(showlabel);
        layout();
    }

    public void setSize(int size) {
        if (label != null && !label.isDisposed()) {
            label.setFont(FontRegistry.getRegular(label));
        }
        if (size == 100) {
            width = srcWidth;
            height = srcHeight;
        } else {
            if (srcWidth < minSize || srcHeight < minSize) {
                width = srcWidth;
                height = srcHeight;
                return;
            }
            this.width = (int) ((this.srcWidth * size * 0.01f) + 0.5f);
            this.height = (int) ((this.srcHeight * size * 0.01f) + 0.5f);
        }
        if (canvas != null) {
            canvas.updateShadow(width, height);
        }
    }

    public void setSizeSmall() {
        if (label != null && !label.isDisposed()) {
            System.out.println("Thumb1Auto.setSizeSmall():");
            label.setFont(FontRegistry.getSmall(label));
        }
        if (srcWidth < minSize || srcHeight < minSize) {
            return;
        }
        this.width = this.width / 2;
        this.height = this.height / 2;
        if (canvas != null) {
            canvas.updateShadow(width, height);
        }
    }

    @Override
    public void setThumbModel(ThumbProxy thumbModel) {
        if (thumbModel == null || thumbModel.getMediaPath() == null) {
            System.err.println("AutoThumb setThumbModel : NULL");
            return;
        }
        thumbImage = thumbModel.getThumbImage();
        if (thumbImage != null && !thumbImage.isDisposed()) {
            Rectangle bounds = thumbImage.getBounds();
            setThumbSize(bounds.width, bounds.height);
        } else {
            setThumbSize(thumbModel.getThumbWidth(), thumbModel.getThumbHeight());
        }
        this.thumbModel = thumbModel;
        setName(thumbModel);
        if (isShowlabel()) {
            if (name != null && label != null && !label.isDisposed()) {
                label.setText(name);
            }
        }
    }

    private void setName(ThumbProxy thumbModel) {
        if (thumbModel.title == null) {
            if (thumbModel.getMediaFile() != null) {
                thumbModel.title = thumbModel.getMediaFile().getName();
            }
        }
    }

    public void setThumbSize(int width, int height) {
        this.width = width;
        this.height = height;
        srcWidth = width;
        srcHeight = height;
        if (canvas != null) {
            canvas.updateShadow(width, height);
        }
        setSize(parentContainer.getThumbSizeFactor());
    }

    public void setThumbSize(Rectangle bounds) {
        setThumbSize(bounds.width, bounds.height);
    }

    public void updateDataBaseInfo() {
        if (indexKey > 0 || getFile() == null) {
            return;
        }
        IFile file = getFile();
        IFile[] files = new IFile[1];
        files[0] = file;
        try {
            List<DataBaseInfo> data = DataBase.selectThumbInfo(files);
            if (data != null && data.size() > 0) {
                this.dataBaseInfo = data.get(0);
                if (dataBaseInfo != null) {
                    final String albums = dataBaseInfo.getAlbums();
                    setCategories(albums);
                    setIndexKey(dataBaseInfo.getIndex());
                    setOffline(dataBaseInfo.isOffline());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int updateposition(int parentLocationY, int parentHeight, int space, boolean mustUnload) {
        if (main == null || main.isDisposed()) {
            return 1;
        }
        int up = main.getLocation().y + parentLocationY;
        int low = up - parentHeight;
        if (up + space > 0 && low - space < 0) {
            loadImage();
            return 1;
        } else if (!invisible) {
            cancelLoadImage();
            return 0;
        }
        return 0;
    }

    public void cancelLoadImage() {
        canvas.unLoadImage();
        invisible = true;
    }

    public void loadImage() {
        canvas.loadImage();
        invisible = false;
    }

    public IThumbAlbum getThumbAlbum() {
        return thumbAlbum;
    }

    public boolean reCreate(boolean fully) {
        return false;
    }

    public void setThumbImage(Image thumbImage) {
    }

    public boolean isLarge() {
        return false;
    }

    public Image getThumbImage() {
        return null;
    }

    public void refresh(boolean fully) {
    }

    public ProcessorDataList getProcessorData() {
        return null;
    }

    public DataBaseInfo getOnlineData(String provider, String onlineAlbumId) {
        return null;
    }

    public IToolTipWindow getThumbToolTip(Shell shell) {
        return null;
    }

    @Override
    public void createLargeThumb(Image image, ProcessorDataList pData) {
    }

    public IThumbSaveHandler getIThumbSaveHandler() {
        return null;
    }

    @Override
    public void setProcessorData(ProcessorDataList pd) {
    }
}
