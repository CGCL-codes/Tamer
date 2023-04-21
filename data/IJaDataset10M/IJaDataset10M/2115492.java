package com.thyante.thelibrarian.view.graphicdetailview;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ScrollBar;
import com.thyante.thelibrarian.components.ScrollingSmoother;
import com.thyante.thelibrarian.icons.Icon;
import com.thyante.thelibrarian.model.IConfiguration;
import com.thyante.thelibrarian.model.specification.IItem;
import com.thyante.thelibrarian.model.specification.ITemplate;
import com.thyante.thelibrarian.util.I18n;
import com.thyante.thelibrarian.view.IContentsResizeListener;
import com.thyante.thelibrarian.view.IDetailView;
import com.thyante.thelibrarian.view.ViewRegistry;

/**
 * This class implements a graphical detail view showing an image to the left
 * and the field texts on the right (rendered as graphic).
 * 
 * @author Matthias-M. Christen
 */
public class GraphicDetailView implements IDetailView, PaintListener, IContentsResizeListener {

    /**
	 * Registers the view. 
	 */
    public static void registerView() {
        ViewRegistry.getInstance().registerDetailView(I18n.xl8("Graphical view"), GraphicDetailView.class, Icon.getImageDescriptor("graphic"), null, GraphicDetailViewConfiguration.class, GraphicDetailViewConfigurationPanel.class);
    }

    /**
	 * Default image width
	 */
    public static final int IMAGE_WIDTH = 240;

    /**
	 * Default image height
	 */
    public static final int IMAGE_HEIGHT = 360;

    /**
	 * The contents canvas
	 */
    protected Canvas m_cvsContents;

    /**
	 * The object responsible for rendering the contents
	 */
    protected GraphicDetailViewContents m_contents;

    /**
	 * The current origin in the canvas
	 */
    protected Point m_ptOrigin;

    /**
	 * The item that is currently shown
	 */
    protected IItem m_item;

    /**
	 * The template of the current collection
	 */
    protected ITemplate m_template;

    /**
	 * The view configuration object
	 */
    protected GraphicDetailViewConfiguration m_configuration;

    /**
	 * Selection listener for the vertical scroll bar
	 */
    protected Listener m_listenerScrollVert = new Listener() {

        public void handleEvent(Event e) {
            int nSelection = m_cvsContents.getVerticalBar().getSelection();
            m_cvsContents.scroll(m_ptOrigin.x, -nSelection + m_ptOrigin.y, 0, 0, m_contents.getWidth(), m_contents.getHeight(), false);
            m_contents.scroll(0, -nSelection + m_ptOrigin.y);
            m_ptOrigin.y = nSelection;
        }
    };

    /**
	 * Selection listener for the horizontal scroll bar
	 */
    protected Listener m_listenerScrollHorz = new Listener() {

        public void handleEvent(Event e) {
            int nSelection = m_cvsContents.getHorizontalBar().getSelection();
            m_cvsContents.scroll(-nSelection + m_ptOrigin.x, m_ptOrigin.y, 0, 0, m_contents.getWidth(), m_contents.getHeight(), false);
            m_contents.scroll(-nSelection + m_ptOrigin.x, 0);
            m_ptOrigin.x = nSelection;
        }
    };

    /**
	 * Resize listener for the canvas
	 */
    protected Listener m_listenerResize = new Listener() {

        public void handleEvent(Event event) {
            onResize();
        }
    };

    private ScrollingSmoother m_smoother;

    /**
	 * Constructs the detail view.
	 */
    public GraphicDetailView() {
        m_item = null;
        m_template = null;
        m_configuration = null;
    }

    public Composite createGeneralUI(Composite cmpParent, MenuManager mgrContextMenu) {
        return createUI(cmpParent, mgrContextMenu);
    }

    public void dispose() {
        m_configuration.dispose();
        m_cvsContents.removePaintListener(this);
        m_cvsContents.removeListener(SWT.Resize, m_listenerResize);
        m_cvsContents.getHorizontalBar().removeListener(SWT.Selection, m_listenerScrollHorz);
        m_cvsContents.getVerticalBar().removeListener(SWT.Selection, m_listenerScrollVert);
        m_smoother.smoothControl(false, false);
    }

    public Composite createUI(Composite cmpParent, MenuManager mgrContextMenu) {
        m_cvsContents = new Canvas(cmpParent, SWT.NO_BACKGROUND | SWT.NO_REDRAW_RESIZE | SWT.H_SCROLL | SWT.V_SCROLL) {

            @Override
            public Point computeSize(int nWidthHint, int nHeightHint, boolean bChanged) {
                Point pt = super.computeSize(nWidthHint, nHeightHint, bChanged);
                pt.y = m_contents == null ? IMAGE_HEIGHT : m_contents.getHeight();
                if (pt.y < IMAGE_HEIGHT) pt.y = IMAGE_HEIGHT;
                return pt;
            }
        };
        GridData gdCanvas = new GridData(SWT.FILL, SWT.FILL, true, true);
        gdCanvas.verticalAlignment = SWT.TOP;
        gdCanvas.verticalIndent = 0;
        gdCanvas.heightHint = IMAGE_HEIGHT;
        m_cvsContents.setLayoutData(gdCanvas);
        m_cvsContents.addPaintListener(this);
        m_cvsContents.setMenu(mgrContextMenu.createContextMenu(m_cvsContents));
        m_configuration.createUIObjectsIfNeeded(m_cvsContents.getDisplay());
        m_contents = new GraphicDetailViewContents(m_cvsContents, m_configuration);
        m_contents.addContentsResizeListener(this);
        m_smoother = new ScrollingSmoother(m_cvsContents);
        m_smoother.smoothControl(true, true);
        m_ptOrigin = new Point(0, 0);
        m_cvsContents.getHorizontalBar().addListener(SWT.Selection, m_listenerScrollHorz);
        m_cvsContents.getVerticalBar().addListener(SWT.Selection, m_listenerScrollVert);
        m_cvsContents.addListener(SWT.Resize, m_listenerResize);
        return m_cvsContents;
    }

    public void update() {
        if (m_configuration.createUIObjectsIfNeeded(m_cvsContents.getDisplay())) m_contents.resetSizeCaches();
        m_contents.setItemToRender(m_item, m_template);
        m_cvsContents.redraw();
    }

    public void showItem(IItem item, ITemplate template) {
        boolean bIsSameItem = item == m_item;
        if (!bIsSameItem) m_contents.hideEditingControl();
        m_item = item;
        m_template = template;
        m_contents.setItemToRender(item, template);
        if (!bIsSameItem) {
            m_cvsContents.getVerticalBar().setSelection(0);
            m_ptOrigin.x = 0;
            m_ptOrigin.y = 0;
        }
        onContentsResized();
    }

    public void paintControl(PaintEvent e) {
        m_contents.draw(e.gc, m_ptOrigin);
    }

    public void onResize() {
        m_contents.setItemToRender(m_item, m_template);
        m_contents.render();
        onContentsResized();
    }

    public void onContentsResized() {
        int nContentsWidth = m_contents.getWidth();
        int nContentsHeight = m_contents.getHeight();
        Rectangle rectClient = m_cvsContents.getClientArea();
        ScrollBar sbHorizontal = m_cvsContents.getHorizontalBar();
        sbHorizontal.setMaximum(nContentsWidth);
        sbHorizontal.setThumb(Math.min(nContentsWidth, rectClient.width));
        sbHorizontal.setIncrement(10);
        sbHorizontal.setPageIncrement(100);
        int nPage = nContentsWidth - rectClient.width;
        int nSelection = sbHorizontal.getSelection();
        if (nSelection >= nPage) {
            if (nPage <= 0) nSelection = 0;
            m_ptOrigin.x = nSelection;
        }
        ScrollBar sbVertical = m_cvsContents.getVerticalBar();
        sbVertical.setMaximum(nContentsHeight);
        sbVertical.setThumb(Math.min(nContentsHeight, rectClient.height));
        sbVertical.setIncrement(10);
        sbVertical.setPageIncrement(100);
        nPage = nContentsHeight - rectClient.height;
        nSelection = sbVertical.getSelection();
        if (nSelection >= nPage) {
            if (nPage <= 0) nSelection = 0;
            m_ptOrigin.y = nSelection;
        }
        m_cvsContents.redraw();
    }

    public boolean hasConfiguration() {
        return true;
    }

    public IConfiguration createConfiguration() {
        return new GraphicDetailViewConfiguration();
    }

    public IConfiguration getConfiguration() {
        return m_configuration;
    }

    public void setConfiguration(IConfiguration configuration) {
        if (!(configuration instanceof GraphicDetailViewConfiguration)) return;
        m_configuration = (GraphicDetailViewConfiguration) configuration;
        if (m_cvsContents != null && !m_cvsContents.isDisposed()) m_configuration.createUIObjectsIfNeeded(m_cvsContents.getDisplay());
    }
}
