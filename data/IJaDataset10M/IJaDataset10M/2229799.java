package com.aelitis.azureus.ui.swt.views.skin.sidebar;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.List;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.gudy.azureus2.core3.config.COConfigurationManager;
import org.gudy.azureus2.core3.util.*;
import org.gudy.azureus2.ui.swt.Utils;
import org.gudy.azureus2.ui.swt.mainwindow.Colors;
import org.gudy.azureus2.ui.swt.mainwindow.SWTThread;
import org.gudy.azureus2.ui.swt.plugins.UISWTView;
import org.gudy.azureus2.ui.swt.pluginsimpl.UISWTViewCore;
import org.gudy.azureus2.ui.swt.shells.GCStringPrinter;
import org.gudy.azureus2.ui.swt.views.IView;
import com.aelitis.azureus.ui.common.viewtitleinfo.ViewTitleInfo;
import com.aelitis.azureus.ui.mdi.MdiEntry;
import com.aelitis.azureus.ui.mdi.MdiEntryVitalityImage;
import com.aelitis.azureus.ui.swt.imageloader.ImageLoader;
import com.aelitis.azureus.ui.swt.mdi.BaseMdiEntry;
import com.aelitis.azureus.ui.swt.skin.*;
import com.aelitis.azureus.ui.swt.utils.ColorCache;

/**
 * @author TuxPaper
 * @created Aug 13, 2008
 *
 */
public class SideBarEntrySWT extends BaseMdiEntry implements DisposeListener {

    private static final boolean DO_OUR_OWN_TREE_INDENT = false;

    private static final int SIDEBAR_SPACING = 2;

    private static final int IMAGELEFT_SIZE = 20;

    private static final int IMAGELEFT_GAP = 5;

    private static final boolean ALWAYS_IMAGE_GAP = true;

    private static final String[] default_indicator_colors = { "#000000", "#000000", "#166688", "#1c2056" };

    private static long uniqueNumber = 0;

    private TreeItem swtItem;

    private List<SideBarVitalityImageSWT> listVitalityImages = Collections.EMPTY_LIST;

    private final SideBar sidebar;

    private int maxIndicatorWidth;

    private Image imgClose;

    private Image imgCloseSelected;

    private Color bg;

    private Color fg;

    private Color bgSel;

    private Color fgSel;

    private boolean showonSWTItemSet;

    private final SWTSkin skin;

    private SWTSkinObjectContainer soParent;

    private boolean buildonSWTItemSet;

    public SideBarEntrySWT(SideBar sidebar, SWTSkin _skin, String id) {
        super(sidebar, id);
        this.skin = _skin;
        if (id == null) {
            logID = "null";
        } else {
            int i = id.indexOf('_');
            if (i > 0) {
                logID = id.substring(0, i);
            } else {
                logID = id;
            }
        }
        this.sidebar = sidebar;
        Utils.execSWTThread(new AERunnable() {

            public void runSupport() {
                SWTSkinProperties skinProperties = skin.getSkinProperties();
                bg = skinProperties.getColor("color.sidebar.bg");
                fg = skinProperties.getColor("color.sidebar.fg");
                bgSel = skinProperties.getColor("color.sidebar.selected.bg");
                fgSel = skinProperties.getColor("color.sidebar.selected.fg");
            }
        });
    }

    public TreeItem getTreeItem() {
        return swtItem;
    }

    public void setTreeItem(TreeItem treeItem) {
        if (swtItem != null && treeItem != null) {
            Debug.out("Warning: Sidebar " + id + " already has a treeitem");
            return;
        }
        this.swtItem = treeItem;
        setDisposed(false);
        if (treeItem != null) {
            ImageLoader imageLoader = ImageLoader.getInstance();
            imgClose = imageLoader.getImage("image.sidebar.closeitem");
            imgCloseSelected = imageLoader.getImage("image.sidebar.closeitem-selected");
            treeItem.addDisposeListener(this);
            treeItem.getParent().addTreeListener(new TreeListener() {

                public void treeExpanded(TreeEvent e) {
                    if (e.item == swtItem) {
                        SideBarEntrySWT.super.setExpanded(true);
                    }
                }

                public void treeCollapsed(TreeEvent e) {
                    if (e.item == swtItem) {
                        SideBarEntrySWT.super.setExpanded(false);
                    }
                }
            });
            TreeItem parentItem = treeItem.getParentItem();
            if (parentItem != null) {
                MdiEntry parentEntry = (MdiEntry) parentItem.getData("MdiEntry");
                if (parentEntry.isExpanded()) {
                    parentItem.setExpanded(true);
                }
            }
            setExpanded(isExpanded());
        }
        if (buildonSWTItemSet) {
            build();
        }
        if (showonSWTItemSet) {
            show();
        }
    }

    public MdiEntryVitalityImage addVitalityImage(String imageID) {
        SideBarVitalityImageSWT vitalityImage = new SideBarVitalityImageSWT(this, imageID);
        if (listVitalityImages == Collections.EMPTY_LIST) {
            listVitalityImages = new ArrayList<SideBarVitalityImageSWT>(1);
        }
        listVitalityImages.add(vitalityImage);
        return vitalityImage;
    }

    public MdiEntryVitalityImage[] getVitalityImages() {
        return (MdiEntryVitalityImage[]) listVitalityImages.toArray(new MdiEntryVitalityImage[0]);
    }

    public MdiEntryVitalityImage getVitalityImage(int hitX, int hitY) {
        MdiEntryVitalityImage[] vitalityImages = getVitalityImages();
        for (int i = 0; i < vitalityImages.length; i++) {
            SideBarVitalityImageSWT vitalityImage = (SideBarVitalityImageSWT) vitalityImages[i];
            if (!vitalityImage.isVisible()) {
                continue;
            }
            Rectangle hitArea = vitalityImage.getHitArea();
            if (hitArea != null && hitArea.contains(hitX, hitY)) {
                return vitalityImage;
            }
        }
        return null;
    }

    public void redraw() {
        Utils.execSWTThread(new AERunnable() {

            public void runSupport() {
                if (swtItem == null || swtItem.isDisposed()) {
                    return;
                }
                Tree tree = swtItem.getParent();
                if (!tree.isVisible()) {
                    return;
                }
                try {
                    Rectangle bounds = swtItem.getBounds();
                    Rectangle treeBounds = tree.getBounds();
                    tree.redraw(0, bounds.y, treeBounds.width, bounds.height, true);
                } catch (NullPointerException npe) {
                }
            }
        });
    }

    protected Rectangle swt_getBounds() {
        if (swtItem == null || swtItem.isDisposed()) {
            return null;
        }
        try {
            Tree tree = swtItem.getParent();
            Rectangle bounds = swtItem.getBounds();
            Rectangle treeBounds = tree.getBounds();
            return new Rectangle(0, bounds.y, treeBounds.width, bounds.height);
        } catch (NullPointerException e) {
            Debug.outNoStack("NPE @ " + Debug.getCompressedStackTrace(), true);
        } catch (Exception e) {
            Debug.out(e);
        }
        return null;
    }

    public void setExpanded(final boolean expanded) {
        super.setExpanded(expanded);
        Utils.execSWTThread(new AERunnable() {

            public void runSupport() {
                if (swtItem != null && !isDisposed()) {
                    swtItem.setExpanded(expanded);
                }
            }
        });
    }

    public void expandTo() {
        Utils.execSWTThread(new AERunnable() {

            public void runSupport() {
                if (swtItem == null || isDisposed()) {
                    return;
                }
                TreeItem item = swtItem.getParentItem();
                while (item != null) {
                    item.setExpanded(true);
                    item = item.getParentItem();
                }
            }
        });
    }

    public boolean close(boolean force) {
        if (!super.close(force)) {
            return false;
        }
        mdi.removeItem(SideBarEntrySWT.this);
        Utils.execSWTThread(new AERunnable() {

            public void runSupport() {
                if (swtItem != null) {
                    try {
                        swtItem.setFont(null);
                        swtItem.dispose();
                    } catch (Exception e) {
                        Debug.outNoStack("Warning on SidebarEntry dispose: " + e.toString(), false);
                    } finally {
                        swtItem = null;
                    }
                } else if (iview != null) {
                    try {
                        iview.delete();
                    } finally {
                        iview = null;
                    }
                }
            }
        });
        return true;
    }

    public void build() {
        Utils.execSWTThread(new AERunnable() {

            public void runSupport() {
                swt_build();
            }
        });
    }

    public boolean swt_build() {
        if (swtItem == null) {
            buildonSWTItemSet = true;
            return true;
        }
        buildonSWTItemSet = false;
        if (getSkinObject() == null) {
            Control control = null;
            Composite parent = soParent == null ? Utils.findAnyShell() : soParent.getComposite();
            String skinRef = getSkinRef();
            if (skinRef != null) {
                Shell shell = parent.getShell();
                Cursor cursor = shell.getCursor();
                try {
                    shell.setCursor(shell.getDisplay().getSystemCursor(SWT.CURSOR_WAIT));
                    SWTSkinObjectContainer soContents = (SWTSkinObjectContainer) skin.createSkinObject("MdiContents." + uniqueNumber++, "mdi.content.item", getParentSkinObject(), null);
                    skin.addSkinObject(soContents);
                    SWTSkinObject skinObject = skin.createSkinObject(id, skinRef, soContents, getDatasourceCore());
                    skin.addSkinObject(soContents);
                    control = skinObject.getControl();
                    control.setLayoutData(Utils.getFilledFormData());
                    control.getParent().layout(true, true);
                    setSkinObject(skinObject, soContents);
                } finally {
                    shell.setCursor(cursor);
                }
            } else if (iview != null) {
                try {
                    SWTSkinObjectContainer soContents = (SWTSkinObjectContainer) skin.createSkinObject("MdiIView." + uniqueNumber++, "mdi.content.item", getParentSkinObject());
                    skin.addSkinObject(soContents);
                    parent.setBackgroundMode(SWT.INHERIT_NONE);
                    Composite viewComposite = soContents.getComposite();
                    boolean doGridLayout = true;
                    if (iview instanceof UISWTView) {
                        UISWTView swtView = (UISWTView) iview;
                        if (swtView.getControlType() == UISWTViewCore.CONTROLTYPE_SKINOBJECT) {
                            doGridLayout = false;
                        }
                    }
                    if (doGridLayout) {
                        GridLayout gridLayout = new GridLayout();
                        gridLayout.horizontalSpacing = gridLayout.verticalSpacing = gridLayout.marginHeight = gridLayout.marginWidth = 0;
                        viewComposite.setLayout(gridLayout);
                        viewComposite.setLayoutData(Utils.getFilledFormData());
                    }
                    if (iview instanceof UISWTViewCore) {
                        UISWTViewCore uiViewCore = (UISWTViewCore) iview;
                        uiViewCore.setSkinObject(soContents, soContents.getComposite());
                    }
                    iview.initialize(viewComposite);
                    swtItem.setText(iview.getFullTitle());
                    Composite iviewComposite = iview.getComposite();
                    control = iviewComposite;
                    if (doGridLayout) {
                        Object existingLayoutData = iviewComposite.getLayoutData();
                        Object existingParentLayoutData = iviewComposite.getParent().getLayoutData();
                        if (existingLayoutData == null || !(existingLayoutData instanceof GridData) && (existingParentLayoutData instanceof GridLayout)) {
                            GridData gridData = new GridData(GridData.FILL_BOTH);
                            iviewComposite.setLayoutData(gridData);
                        }
                    }
                    parent.layout(true, true);
                    setSkinObject(soContents, soContents);
                } catch (Exception e) {
                    Debug.out("Error creating sidebar content area for " + id, e);
                    if (iview != null) {
                        iview.delete();
                    }
                    setIView(null);
                    close(true);
                }
            } else if (iviewClass != null) {
                try {
                    IView view = null;
                    if (iviewClassArgs == null) {
                        view = (IView) iviewClass.newInstance();
                    } else {
                        Constructor<?> constructor = iviewClass.getConstructor(iviewClassArgs);
                        view = (IView) constructor.newInstance(iviewClassVals);
                    }
                    if (view != null) {
                        setIView(view);
                        return swt_build();
                    }
                    close(true);
                    return false;
                } catch (Throwable e) {
                    Debug.out(e);
                    close(true);
                }
            }
            if (control != null && !control.isDisposed()) {
                control.addDisposeListener(new DisposeListener() {

                    public void widgetDisposed(DisposeEvent e) {
                        close(true);
                    }
                });
            } else {
                return false;
            }
        }
        return true;
    }

    public void show() {
        Utils.execSWTThread(new AERunnable() {

            public void runSupport() {
                swt_show();
            }
        });
    }

    private void swt_show() {
        if (swtItem == null) {
            showonSWTItemSet = true;
            return;
        }
        showonSWTItemSet = false;
        if (!swt_build()) {
            return;
        }
        triggerOpenListeners();
        swtItem.getParent().select(swtItem);
        swtItem.getParent().showItem(swtItem);
        super.show();
    }

    protected void swt_paintSideBar(Event event) {
        TreeItem treeItem = (TreeItem) event.item;
        if (treeItem.isDisposed() || isDisposed()) {
            return;
        }
        Rectangle itemBounds = treeItem.getBounds();
        Rectangle drawBounds = Utils.isCocoa || Constants.isWindows ? event.gc.getClipping() : event.getBounds();
        if (drawBounds.isEmpty()) {
            return;
        }
        String text = getTitle();
        if (text == null) text = "";
        GC gc = event.gc;
        gc.setAntialias(SWT.ON);
        gc.setAdvanced(true);
        boolean selected = (event.detail & SWT.SELECTED) > 0;
        Color fgText = swt_paintEntryBG(event.detail, gc, drawBounds);
        Tree tree = (Tree) event.widget;
        Rectangle treeArea = tree.getClientArea();
        Font font = tree.getFont();
        if (font != null && !font.isDisposed()) {
            gc.setFont(font);
        }
        if (DO_OUR_OWN_TREE_INDENT) {
            int indentLevel = 0;
            TreeItem tempItem = treeItem;
            while (tempItem != null) {
                tempItem = tempItem.getParentItem();
                indentLevel++;
            }
            itemBounds.x += (18 * indentLevel);
        }
        int x1IndicatorOfs = SIDEBAR_SPACING;
        int x0IndicatorOfs = itemBounds.x;
        if (viewTitleInfo != null) {
            String textIndicator = null;
            try {
                textIndicator = (String) viewTitleInfo.getTitleInfoProperty(ViewTitleInfo.TITLE_INDICATOR_TEXT);
            } catch (Exception e) {
                Debug.out(e);
            }
            if (textIndicator != null) {
                Point textSize = gc.textExtent(textIndicator);
                Point minTextSize = gc.textExtent("99");
                if (textSize.x < minTextSize.x + 2) {
                    textSize.x = minTextSize.x + 2;
                }
                int width = textSize.x + textSize.y / 2 + 2;
                x1IndicatorOfs += width + SIDEBAR_SPACING;
                int startX = treeArea.width - x1IndicatorOfs;
                int textOffsetY = 0;
                int height = textSize.y + 3;
                int startY = itemBounds.y + (itemBounds.height - height) / 2;
                Pattern pattern;
                Color color1;
                Color color2;
                String[] colors = (String[]) viewTitleInfo.getTitleInfoProperty(ViewTitleInfo.TITLE_INDICATOR_COLOR);
                if (colors == null || colors.length != 4) {
                    colors = default_indicator_colors;
                }
                if (selected) {
                    color1 = ColorCache.getColor(gc.getDevice(), colors[0]);
                    color2 = ColorCache.getColor(gc.getDevice(), colors[1]);
                    pattern = new Pattern(gc.getDevice(), 0, startY, 0, startY + height, color1, 127, color2, 4);
                } else {
                    color1 = ColorCache.getColor(gc.getDevice(), colors[2]);
                    color2 = ColorCache.getColor(gc.getDevice(), colors[3]);
                    pattern = new Pattern(gc.getDevice(), 0, startY, 0, startY + height, color1, color2);
                }
                gc.setBackgroundPattern(pattern);
                gc.fillRoundRectangle(startX, startY, width, height, textSize.y + 1, height);
                gc.setBackgroundPattern(null);
                pattern.dispose();
                if (maxIndicatorWidth > width) {
                    maxIndicatorWidth = width;
                }
                gc.setForeground(Colors.white);
                GCStringPrinter.printString(gc, textIndicator, new Rectangle(startX, startY + textOffsetY, width, height), true, false, SWT.CENTER);
            }
        }
        if (isCloseable()) {
            Image img = selected ? imgCloseSelected : imgClose;
            Rectangle closeArea = img.getBounds();
            closeArea.x = treeArea.width - closeArea.width - SIDEBAR_SPACING - x1IndicatorOfs;
            closeArea.y = itemBounds.y + (itemBounds.height - closeArea.height) / 2;
            x1IndicatorOfs += closeArea.width + SIDEBAR_SPACING;
            gc.drawImage(img, closeArea.x, closeArea.y);
            treeItem.setData("closeArea", closeArea);
        }
        MdiEntryVitalityImage[] vitalityImages = getVitalityImages();
        for (int i = 0; i < vitalityImages.length; i++) {
            SideBarVitalityImageSWT vitalityImage = (SideBarVitalityImageSWT) vitalityImages[i];
            if (!vitalityImage.isVisible() || vitalityImage.getAlignment() != SWT.RIGHT) {
                continue;
            }
            vitalityImage.switchSuffix(selected ? "-selected" : "");
            Image image = vitalityImage.getImage();
            if (image != null && !image.isDisposed()) {
                Rectangle bounds = image.getBounds();
                bounds.x = treeArea.width - bounds.width - SIDEBAR_SPACING - x1IndicatorOfs;
                bounds.y = itemBounds.y + (itemBounds.height - bounds.height) / 2;
                x1IndicatorOfs += bounds.width + SIDEBAR_SPACING;
                gc.drawImage(image, bounds.x, bounds.y);
                vitalityImage.setHitArea(bounds);
            }
        }
        boolean greyScale = false;
        if (viewTitleInfo != null) {
            Object active_state = viewTitleInfo.getTitleInfoProperty(ViewTitleInfo.TITLE_ACTIVE_STATE);
            if (active_state instanceof Long) {
                greyScale = (Long) active_state == 2;
            }
        }
        String suffix = selected ? "-selected" : null;
        Image imageLeft = getImageLeft(suffix);
        if (imageLeft == null && selected) {
            releaseImageLeft(suffix);
            suffix = null;
            imageLeft = getImageLeft(null);
        }
        if (imageLeft != null) {
            Rectangle bounds = imageLeft.getBounds();
            int x = x0IndicatorOfs + ((IMAGELEFT_SIZE - bounds.width) / 2);
            int y = itemBounds.y + ((itemBounds.height - bounds.height) / 2);
            Rectangle clipping = gc.getClipping();
            gc.setClipping(x0IndicatorOfs, itemBounds.y, IMAGELEFT_SIZE, itemBounds.height);
            boolean drawn = false;
            if (greyScale) {
                String imageLeftID = getImageLeftID();
                if (imageLeftID != null) {
                    Image grey = ImageLoader.getInstance().getImage(imageLeftID + "-gray");
                    if (grey != null) {
                        gc.drawImage(grey, x, y);
                        ImageLoader.getInstance().releaseImage(imageLeftID + "-gray");
                        drawn = true;
                    }
                }
            }
            if (!drawn) {
                gc.drawImage(imageLeft, x, y);
            }
            releaseImageLeft(suffix);
            gc.setClipping(clipping);
            x0IndicatorOfs += IMAGELEFT_SIZE + IMAGELEFT_GAP;
        } else if (ALWAYS_IMAGE_GAP) {
            x0IndicatorOfs += IMAGELEFT_SIZE + IMAGELEFT_GAP;
        } else {
            if (treeItem.getParentItem() != null) {
                x0IndicatorOfs += 30 - 18;
            }
        }
        if (treeItem.getParentItem() == null) {
            Font headerFont = sidebar.getHeaderFont();
            if (headerFont != null && !headerFont.isDisposed()) {
                gc.setFont(headerFont);
            }
            gc.setForeground(ColorCache.getColor(gc.getDevice(), "#2B2D32"));
        }
        gc.setForeground(fgText);
        Rectangle clipping = new Rectangle(x0IndicatorOfs, itemBounds.y, treeArea.width - x1IndicatorOfs - SIDEBAR_SPACING - x0IndicatorOfs, itemBounds.height);
        if (drawBounds.intersects(clipping)) {
            if (text.startsWith(" ")) {
                text = text.substring(1);
                clipping.x += 30;
                clipping.width -= 30;
            }
            GCStringPrinter sp = new GCStringPrinter(gc, text, clipping, true, false, SWT.NONE);
            sp.printString();
            clipping.x += sp.getCalculatedSize().x + 5;
        }
        for (int i = 0; i < vitalityImages.length; i++) {
            SideBarVitalityImageSWT vitalityImage = (SideBarVitalityImageSWT) vitalityImages[i];
            if (!vitalityImage.isVisible() || vitalityImage.getAlignment() != SWT.LEFT) {
                continue;
            }
            vitalityImage.switchSuffix(selected ? "-selected" : "");
            Image image = vitalityImage.getImage();
            if (image != null && !image.isDisposed()) {
                Rectangle bounds = image.getBounds();
                bounds.x = clipping.x;
                bounds.y = itemBounds.y + (itemBounds.height - bounds.height) / 2;
                clipping.x += bounds.width + SIDEBAR_SPACING;
                if (clipping.x > (treeArea.width - x1IndicatorOfs)) {
                    vitalityImage.setHitArea(null);
                    continue;
                }
                gc.drawImage(image, bounds.x, bounds.y);
                vitalityImage.setHitArea(bounds);
            }
        }
        if (treeItem.getItemCount() > 0 && !isCollapseDisabled() && (!Utils.isCocoa || !SideBar.HIDE_NATIVE_EXPANDER)) {
            gc.setAntialias(SWT.ON);
            Color oldBG = gc.getBackground();
            gc.setBackground(gc.getForeground());
            if (treeItem.getExpanded()) {
                int xStart = 15;
                if (Utils.isCocoa) {
                    xStart -= 5;
                }
                int arrowSize = 8;
                int yStart = itemBounds.height - (itemBounds.height + arrowSize) / 2;
                gc.fillPolygon(new int[] { itemBounds.x - xStart, itemBounds.y + yStart, itemBounds.x - xStart + arrowSize, itemBounds.y + yStart, itemBounds.x - xStart + (arrowSize / 2), itemBounds.y + 16 });
            } else {
                int xStart = 15;
                if (Utils.isCocoa) {
                    xStart -= 5;
                }
                int arrowSize = 8;
                int yStart = itemBounds.height - (itemBounds.height + arrowSize) / 2;
                gc.fillPolygon(new int[] { itemBounds.x - xStart, itemBounds.y + yStart, itemBounds.x - xStart + arrowSize, itemBounds.y + yStart + 4, itemBounds.x - xStart, itemBounds.y + yStart + 8 });
            }
            gc.setBackground(oldBG);
            gc.setFont(sidebar.getHeaderFont());
        }
    }

    protected Color swt_paintEntryBG(int detail, GC gc, Rectangle drawBounds) {
        Color fgText = Colors.black;
        boolean selected = (detail & SWT.SELECTED) > 0;
        if (selected) {
            gc.setClipping((Rectangle) null);
            if (fgSel != null) {
                fgText = fgSel;
            }
            if (bgSel != null) {
                gc.setBackground(bgSel);
            }
            Color color1;
            Color color2;
            if (sidebar.getTree().isFocusControl()) {
                color1 = ColorCache.getColor(gc.getDevice(), "#166688");
                color2 = ColorCache.getColor(gc.getDevice(), "#1c2458");
            } else {
                color1 = ColorCache.getColor(gc.getDevice(), "#447281");
                color2 = ColorCache.getColor(gc.getDevice(), "#393e58");
            }
            gc.setBackground(color1);
            gc.fillRectangle(drawBounds.x, drawBounds.y, drawBounds.width, 3);
            gc.setForeground(color1);
            gc.setBackground(color2);
            Rectangle itemBounds = swt_getBounds();
            gc.fillGradientRectangle(drawBounds.x, itemBounds.y + 3, drawBounds.width, itemBounds.height - 3, true);
        } else {
            if (fg != null) {
                fgText = fg;
            }
            if (bg != null) {
                gc.setBackground(bg);
            }
            if (this == sidebar.draggingOver) {
                Color c = skin.getSkinProperties().getColor("color.sidebar.drag.bg");
                gc.setBackground(c);
            }
            gc.fillRectangle(drawBounds);
            if (this == sidebar.draggingOver) {
                Color c = skin.getSkinProperties().getColor("color.sidebar.drag.fg");
                gc.setForeground(c);
                gc.setLineWidth(5);
                gc.drawRectangle(drawBounds);
            }
        }
        return fgText;
    }

    public void widgetDisposed(DisposeEvent e) {
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.releaseImage("image.sidebar.closeitem");
        imageLoader.releaseImage("image.sidebar.closeitem-selected");
        final TreeItem treeItem = (TreeItem) e.widget;
        if (treeItem != swtItem) {
            Debug.out("Warning: TreeItem changed for sidebar " + id);
            return;
        }
        if (swtItem == null) {
            return;
        }
        if (swtItem != null && !Constants.isOSX) {
            TreeItem[] children = swtItem.getItems();
            for (TreeItem child : children) {
                MdiEntry entry = (MdiEntry) child.getData("MdiEntry");
                if (entry != null) {
                    entry.close(true);
                }
            }
        }
        final Tree tree = sidebar.getTree();
        if (tree.isDisposed() || swtItem.isDisposed()) {
            return;
        }
        setTreeItem(null);
        triggerCloseListeners(!SWTThread.getInstance().isTerminated());
        IView iview = getIView();
        if (iview != null) {
            iview.delete();
            setIView(null);
        }
        SWTSkinObject so = getSkinObject();
        if (so != null) {
            setSkinObject(null, null);
            so.getSkin().removeSkinObject(so);
        }
        Utils.execSWTThreadLater(0, new AERunnable() {

            public void runSupport() {
                if (tree.isDisposed()) {
                    return;
                }
                try {
                    COConfigurationManager.removeParameter("SideBar.AutoOpen." + id);
                    if (Constants.isOSX && !tree.isDisposed() && tree.getSelectionCount() == 0) {
                        String parentid = getParentID();
                        if (parentid != null && mdi.getEntry(parentid) != null) {
                            mdi.showEntryByID(parentid);
                        } else {
                            mdi.showEntryByID(SideBar.SIDEBAR_SECTION_LIBRARY);
                        }
                    }
                } catch (Exception e2) {
                    Debug.out(e2);
                }
                mdi.removeItem(SideBarEntrySWT.this);
                mdi.setEntryAutoOpen(id, false);
            }
        });
    }

    public void setParentSkinObject(SWTSkinObjectContainer soParent) {
        this.soParent = soParent;
    }

    public SWTSkinObjectContainer getParentSkinObject() {
        return soParent;
    }
}
