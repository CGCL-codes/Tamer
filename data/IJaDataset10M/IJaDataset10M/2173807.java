package de.mindcrimeilab.xsanalyzer.ui.support.mydoggy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.noos.xing.mydoggy.AggregationPosition;
import org.noos.xing.mydoggy.Content;
import org.noos.xing.mydoggy.ContentManager;
import org.noos.xing.mydoggy.ContentManagerUIListener;
import org.noos.xing.mydoggy.Dockable;
import org.noos.xing.mydoggy.MultiSplitConstraint;
import org.noos.xing.mydoggy.MultiSplitContentManagerUI;
import org.noos.xing.mydoggy.TabbedContentManagerUI;
import org.noos.xing.mydoggy.ToolWindow;
import org.noos.xing.mydoggy.event.ContentManagerUIEvent;
import org.noos.xing.mydoggy.plaf.MyDoggyToolWindowManager;
import org.noos.xing.mydoggy.plaf.ui.cmp.ExtendedTableLayout;
import org.noos.xing.mydoggy.plaf.ui.content.MyDoggyMultiSplitContentManagerUI;
import org.springframework.richclient.application.ApplicationWindow;
import org.springframework.richclient.application.PageComponent;
import org.springframework.richclient.application.PageDescriptor;
import org.springframework.richclient.application.PageLayoutBuilder;
import org.springframework.richclient.application.ViewDescriptor;
import org.springframework.richclient.application.support.AbstractApplicationPage;
import org.springframework.util.Assert;

/**
 * Credits go to Peter Karich, peat_hal ‘at’ users ‘dot’ sourceforge ‘dot’ net for the mydoggy swing-rcp integration
 * 
 * @author Michael Engelhardt<me@mindcrime-ilab.de>
 * @author $Author: agony $
 * @version $Revision: 165 $
 * 
 */
public class MyDoggyApplicationPage extends AbstractApplicationPage implements PageLayoutBuilder {

    private JPanel rootComponent;

    private final MyDoggyToolWindowManager toolWindowManager;

    private final ContentManager contentManager;

    private final MultiSplitContentManagerUI contentManagerUI;

    private final Map<String, Entry<Dockable, PageComponent>> contentAndPageComponentById;

    MyDoggyApplicationPage(ApplicationWindow window, PageDescriptor pageDescriptor) {
        super(window, pageDescriptor);
        contentAndPageComponentById = new HashMap<String, Entry<Dockable, PageComponent>>();
        toolWindowManager = new MyDoggyToolWindowManager();
        contentManager = toolWindowManager.getContentManager();
        contentManagerUI = new MyDoggyMultiSplitContentManagerUI();
        contentManager.setContentManagerUI(contentManagerUI);
        assert SwingUtilities.isEventDispatchThread();
        contentManagerUI.setTabPlacement(TabbedContentManagerUI.TabPlacement.TOP);
        contentManagerUI.setShowAlwaysTab(true);
        contentManagerUI.setCloseable(true);
        contentManagerUI.setDetachable(true);
        contentManagerUI.addContentManagerUIListener(new MyDoggyContentListener());
    }

    @Override
    protected void doAddPageComponent(PageComponent pageComponent) {
        Assert.notNull(pageComponent);
        MultiSplitConstraint constraint;
        final Iterator<Entry<Dockable, PageComponent>> iterator = contentAndPageComponentById.values().iterator();
        if (iterator.hasNext()) {
            Dockable dock = iterator.next().getKey();
            if (dock instanceof Content) {
                constraint = new MultiSplitConstraint((Content) dock);
            } else {
                constraint = new MultiSplitConstraint(AggregationPosition.DEFAULT);
            }
        } else {
            constraint = new MultiSplitConstraint(AggregationPosition.DEFAULT);
        }
        final Dockable dockable;
        ViewDescriptor viewDescriptor = getViewDescriptor(pageComponent.getId());
        if (viewDescriptor instanceof MyDoggyToolWindowViewDescriptor) {
            final MyDoggyToolWindowViewDescriptor toolPageComponent = (MyDoggyToolWindowViewDescriptor) viewDescriptor;
            ToolWindow tw = toolWindowManager.registerToolWindow(pageComponent.getId(), pageComponent.getDisplayName(), pageComponent.getIcon(), pageComponent.getControl(), toolPageComponent.getAnchor());
            tw.setType(toolPageComponent.getType());
            tw.setAvailable(toolPageComponent.isAvailable());
            tw.setActive(toolPageComponent.isActive());
            dockable = tw;
        } else {
            Content c = contentManager.addContent(pageComponent.getId(), pageComponent.getDisplayName(), pageComponent.getIcon(), pageComponent.getControl(), pageComponent.getDisplayName(), constraint);
            dockable = c;
        }
        Assert.notNull(dockable);
        Assert.isTrue(dockable.getId().equals(pageComponent.getId()));
        contentAndPageComponentById.put(pageComponent.getId(), new DoggyEntry<Dockable, PageComponent>(dockable, pageComponent));
        pageComponent.getControl();
    }

    @Override
    protected void doRemovePageComponent(PageComponent pageComponent) {
        Entry<Dockable, PageComponent> e = contentAndPageComponentById.remove(pageComponent.getId());
        Assert.notNull(e);
        Dockable dock = e.getKey();
        final boolean ret;
        if (dock instanceof Content) {
            ret = contentManager.removeContent((Content) dock);
        } else {
            toolWindowManager.unregisterToolWindow(dock.getId());
            ret = true;
        }
        Assert.isTrue(ret);
    }

    @Override
    protected boolean giveFocusTo(PageComponent pageComponent) {
        return pageComponent.getControl().requestFocusInWindow();
    }

    @Override
    public void setActiveComponent(PageComponent pageComponent) {
        if (pageComponent != null) {
            Content c = getContent(pageComponent.getId());
            if (c != null) {
            }
            setActive(pageComponent.getId());
        }
        super.setActiveComponent(pageComponent);
    }

    @Override
    protected JComponent createControl() {
        Assert.isNull(rootComponent, "Should not being initialized twice.");
        rootComponent = new JPanel();
        rootComponent.setLayout(new ExtendedTableLayout(new double[][] { { 0, -1, 0 }, { 0, -1, 0 } }));
        rootComponent.add(toolWindowManager, "1,1,");
        return rootComponent;
    }

    @Override
    public void addView(String viewDescriptorId) {
        showView(viewDescriptorId);
    }

    private Content getContent(String id) {
        return contentManager.getContent(id);
    }

    private PageComponent getPageComponent(String id) {
        Entry<Dockable, PageComponent> entry = contentAndPageComponentById.get(id);
        if (entry == null) {
            return null;
        }
        return entry.getValue();
    }

    void buildInitialLayout() {
        getPageDescriptor().buildInitialLayout(this);
    }

    /**
     * This method displays the component with specified key
     * 
     * @return true if it was successful.
     */
    private boolean setActive(final String id) {
        Assert.isTrue(SwingUtilities.isEventDispatchThread());
        Content c = getContent(id);
        if (c != null) {
            c.setSelected(true);
            return true;
        }
        return false;
    }

    @Override
    public boolean close() {
        try {
            saveLayout();
        } catch (IOException ex) {
            logger.warn("IO Error while saving layout!", ex);
        }
        return super.close();
    }

    boolean loadLayout() throws IOException {
        File file = getLayoutFile();
        if (file != null && file.canRead()) {
            Assert.isTrue(SwingUtilities.isEventDispatchThread());
            Assert.isTrue(rootComponent.isVisible());
            FileInputStream inputStream = new FileInputStream(file);
            inputStream.close();
            logger.info("Loaded " + contentManager.getContentCount() + " view(s).");
            return true;
        }
        return false;
    }

    void saveLayout() throws IOException {
        File file = getLayoutFile();
        if (file != null) {
            if (!file.exists()) {
                file.createNewFile();
            }
            if (file.canWrite()) {
                FileOutputStream output = new FileOutputStream(file);
                toolWindowManager.getPersistenceDelegate().save(output);
                output.close();
                logger.info("Saved " + contentManager.getContentCount() + " view(s).");
            }
        }
    }

    private File getLayoutFile() throws IOException {
        PageDescriptor pageDescriptor = getPageDescriptor();
        if (pageDescriptor instanceof MyDoggyPageDescriptor) {
            return ((MyDoggyPageDescriptor) pageDescriptor).getInitialLayout().getFile();
        } else {
            throw new IOException("Couldn't return the layout file. Wrong PageDescriptorType:" + pageDescriptor.getClass());
        }
    }

    /**
     * SpringRC should be informed of a MyDoggy closing event.
     */
    private class MyDoggyContentListener implements ContentManagerUIListener {

        public boolean contentUIRemoving(ContentManagerUIEvent cmEvent) {
            Content content = cmEvent.getContentUI().getContent();
            Assert.notNull(content);
            PageComponent pc = getPageComponent(content.getId());
            Assert.notNull(pc);
            close(pc);
            return false;
        }

        public void contentUIDetached(ContentManagerUIEvent cmEvent) {
        }
    }

    private static class DoggyEntry<K, V> implements Map.Entry<K, V> {

        private final K key;

        private V innerValue;

        DoggyEntry(K key, V value) {
            this.key = key;
            this.innerValue = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return innerValue;
        }

        public V setValue(V value) {
            V old = this.innerValue;
            this.innerValue = value;
            return old;
        }
    }
}
