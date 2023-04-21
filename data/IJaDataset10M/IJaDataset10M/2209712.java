package edu.mit.lcs.haystack.server.extensions.infoextraction;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.List;
import edu.mit.lcs.haystack.server.extensions.infoextraction.featureset.FeatureStore;
import edu.mit.lcs.haystack.server.extensions.infoextraction.featureset.IFeatureSet;
import edu.mit.lcs.haystack.server.extensions.infoextraction.tagtree.IAugmentedNode;
import edu.mit.lcs.haystack.server.extensions.wrapperinduction.dom.IDOMBrowser;

/**
 * @author yks
 */
public abstract class DefaultComposite extends Composite {

    protected BrowserFrame browserFrame;

    protected List cachedUrls;

    protected Display display;

    protected String curSelected;

    public DefaultComposite(Display display, Composite parent, int style, BrowserFrame browserFrame) {
        this(parent, style, browserFrame);
        this.display = display;
    }

    public DefaultComposite(Composite parent, int style, BrowserFrame browserFrame) {
        super(parent, style);
        setBrowserFrame(browserFrame);
    }

    public IFeatureSet getFeatureSet(String featureName, String url) {
        return FeatureStore.getFeatureStore().get(featureName, url);
    }

    public void setBrowserFrame(BrowserFrame bf) {
        this.browserFrame = bf;
    }

    public IDOMBrowser getBrowser() {
        return browserFrame.getBrowserWidget();
    }

    public IHighlighter getHighlighter() {
        return browserFrame.getHighlighter();
    }

    public FeatureStore getFeatureStore() {
        return FeatureStore.getFeatureStore();
    }

    public IAugmentedNode getDocumentRoot(String url) {
        return (IAugmentedNode) FeatureStore.getFeatureStore().get(url).getRoot();
    }

    /**
     * a shortcut function that generates a SWT.Group, to which actual
     * functional UI components are attached. The Group widget provides a
     * coherent label to a given set of underlying components.
     * 
     * @param parent
     * @param style
     * @param text
     * @return
     */
    public Group makeGroup(Composite parent, int style, String text) {
        Group group = new Group(parent, style);
        group.setText(text);
        group.setLayout(new GridLayout());
        group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        return group;
    }

    public void refreshURLs() {
        String urls[] = getFeatureStore().getUrls();
        cachedUrls.setItems(urls);
        if (curSelected != null) {
            for (int i = 0; i < urls.length; i++) {
                if (urls[i].equals(curSelected)) {
                    cachedUrls.select(i);
                    break;
                }
            }
        }
    }

    public void setCurSelected(String selected) {
        curSelected = selected;
    }

    public String getCurSelected() {
        return curSelected;
    }

    public String getSelection(List list) {
        String[] selections = list.getSelection();
        if (selections != null && selections.length > 0) {
            return selections[0];
        } else {
            return null;
        }
    }
}
