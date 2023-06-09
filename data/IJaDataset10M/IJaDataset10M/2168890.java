package net.sourceforge.eclipsetrader.core.ui.preferences;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import net.sourceforge.eclipsetrader.core.CorePlugin;
import net.sourceforge.eclipsetrader.core.db.feed.FeedSource;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public abstract class FeedPage extends PreferencePage {

    protected Combo feed;

    protected Combo exchange;

    protected Text symbol;

    protected String categoryId;

    protected FeedPage(String title, String categoryId) {
        super(title);
        noDefaultAndApplyButton();
        this.categoryId = categoryId;
        setValid(false);
    }

    protected Control createContents(Composite parent) {
        Composite content = new Composite(parent, SWT.NONE);
        GridLayout gridLayout = new GridLayout(2, false);
        gridLayout.marginWidth = gridLayout.marginHeight = 0;
        content.setLayout(gridLayout);
        content.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        Label label = new Label(content, SWT.NONE);
        label.setText("Feed");
        label.setLayoutData(new GridData(125, SWT.DEFAULT));
        feed = new Combo(content, SWT.SINGLE | SWT.READ_ONLY);
        feed.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
        feed.setVisibleItemCount(10);
        feed.add("");
        label = new Label(content, SWT.NONE);
        label.setText("Exchange");
        label.setLayoutData(new GridData(125, SWT.DEFAULT));
        exchange = new Combo(content, SWT.SINGLE | SWT.READ_ONLY);
        exchange.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
        exchange.setVisibleItemCount(10);
        exchange.add("");
        label = new Label(content, SWT.NONE);
        label.setText("Symbol");
        label.setLayoutData(new GridData(125, SWT.DEFAULT));
        symbol = new Text(content, SWT.BORDER);
        symbol.setLayoutData(new GridData(100, SWT.DEFAULT));
        IExtensionRegistry registry = Platform.getExtensionRegistry();
        IExtensionPoint extensionPoint = registry.getExtensionPoint(CorePlugin.FEED_EXTENSION_POINT);
        if (extensionPoint != null) {
            IConfigurationElement[] elements = extensionPoint.getConfigurationElements();
            java.util.List plugins = Arrays.asList(elements);
            Collections.sort(plugins, new Comparator() {

                public int compare(Object arg0, Object arg1) {
                    if ((arg0 instanceof IConfigurationElement) && (arg1 instanceof IConfigurationElement)) {
                        String s0 = ((IConfigurationElement) arg0).getAttribute("name");
                        String s1 = ((IConfigurationElement) arg1).getAttribute("name");
                        return s0.compareTo(s1);
                    }
                    return 0;
                }
            });
            for (Iterator iter = plugins.iterator(); iter.hasNext(); ) {
                IConfigurationElement element = (IConfigurationElement) iter.next();
                String id = element.getAttribute("id");
                String name = element.getAttribute("name");
                IConfigurationElement[] children = element.getChildren();
                for (int i = 0; i < children.length; i++) {
                    if (children[i].getName().equals(categoryId)) {
                        feed.add(name);
                        feed.setData(String.valueOf(feed.getItemCount() - 1), id);
                    }
                }
            }
        }
        feed.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                updateFeedExchanges();
            }
        });
        setValid(true);
        return content;
    }

    protected void updateFeedExchanges() {
        exchange.removeAll();
        exchange.add("");
        String feedId = (String) feed.getData(String.valueOf(feed.getSelectionIndex()));
        if (feedId != null) {
            IExtensionRegistry registry = Platform.getExtensionRegistry();
            IExtensionPoint extensionPoint = registry.getExtensionPoint(CorePlugin.FEED_EXTENSION_POINT);
            if (extensionPoint != null) {
                java.util.List plugins = Arrays.asList(extensionPoint.getConfigurationElements());
                for (Iterator iter = plugins.iterator(); iter.hasNext(); ) {
                    IConfigurationElement element = (IConfigurationElement) iter.next();
                    if (!element.getAttribute("id").equals(feedId)) continue;
                    IConfigurationElement[] children = element.getChildren();
                    for (int i = 0; i < children.length; i++) {
                        if (children[i].getName().equals(categoryId)) {
                            IConfigurationElement[] exchanges = children[i].getChildren();
                            for (int x = 0; x < exchanges.length; x++) {
                                if (exchanges[x].getName().equals("exchange")) {
                                    exchange.setData(String.valueOf(exchange.getItemCount()), exchanges[x].getAttribute("id"));
                                    exchange.add(exchanges[x].getAttribute("name"));
                                }
                            }
                        }
                    }
                }
            }
        }
        exchange.setEnabled(exchange.getItemCount() > 1);
    }

    public FeedSource getFeed() {
        if (feed.getSelectionIndex() <= 0) return null;
        FeedSource newFeed = new FeedSource();
        newFeed.setId((String) feed.getData(String.valueOf(feed.getSelectionIndex())));
        newFeed.setExchange((String) exchange.getData(String.valueOf(exchange.getSelectionIndex())));
        newFeed.setSymbol(symbol.getText());
        return newFeed;
    }
}
