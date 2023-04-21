package com.ibm.tuningfork.rcpapp.splashHandlers;

import java.util.ArrayList;
import java.util.Iterator;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.splash.AbstractSplashHandler;

/**
 * @since 3.3
 * 
 */
public class ExtensibleSplashHandler extends AbstractSplashHandler {

    private ArrayList<Image> fImageList;

    private ArrayList<String> fTooltipList;

    private static final String F_SPLASH_EXTENSION_ID = "com.ibm.tuningfork.rcpapp.splashExtension";

    private static final String F_ELEMENT_ICON = "icon";

    private static final String F_ELEMENT_TOOLTIP = "tooltip";

    private static final String F_DEFAULT_TOOLTIP = "Image";

    private static final int F_IMAGE_WIDTH = 50;

    private static final int F_IMAGE_HEIGHT = 50;

    private static final int F_SPLASH_SCREEN_BEVEL = 5;

    private Composite fIconPanel;

    /**
     * 
     */
    public ExtensibleSplashHandler() {
        fImageList = new ArrayList<Image>();
        fTooltipList = new ArrayList<String>();
        fIconPanel = null;
    }

    public void init(Shell splash) {
        super.init(splash);
        configureUISplash();
        loadSplashExtensions();
        if (hasSplashExtensions() == false) {
            return;
        }
        createUI();
        configureUICompositeIconPanelBounds();
        doEventLoop();
    }

    /**
     * @return
     */
    private boolean hasSplashExtensions() {
        if (fImageList.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 
     */
    private void createUI() {
        createUICompositeIconPanel();
        createUIImages();
    }

    /**
     * 
     */
    private void createUIImages() {
        Iterator<Image> imageIterator = fImageList.iterator();
        Iterator<String> tooltipIterator = fTooltipList.iterator();
        int i = 1;
        int columnCount = ((GridLayout) fIconPanel.getLayout()).numColumns;
        while (imageIterator.hasNext() && (i <= columnCount)) {
            Image image = imageIterator.next();
            String tooltip = tooltipIterator.next();
            createUILabel(image, tooltip);
            i++;
        }
    }

    /**
     * @param image
     * @param tooltip
     */
    private void createUILabel(Image image, String tooltip) {
        Label label = new Label(fIconPanel, SWT.NONE);
        label.setImage(image);
        label.setToolTipText(tooltip);
    }

    /**
     * 
     */
    private void createUICompositeIconPanel() {
        Shell splash = getSplash();
        fIconPanel = new Composite(splash, SWT.NONE);
        int maxColumnCount = getUsableSplashScreenWidth() / (F_IMAGE_WIDTH + 5);
        int actualColumnCount = Math.min(fImageList.size(), maxColumnCount);
        GridLayout layout = new GridLayout(actualColumnCount, true);
        layout.horizontalSpacing = 5;
        layout.verticalSpacing = 0;
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        fIconPanel.setLayout(layout);
    }

    /**
     * 
     */
    private void configureUICompositeIconPanelBounds() {
        Point panelSize = fIconPanel.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
        int x_coord = getSplash().getSize().x - F_SPLASH_SCREEN_BEVEL - panelSize.x;
        int y_coord = getSplash().getSize().y - F_SPLASH_SCREEN_BEVEL - panelSize.y;
        int x_width = panelSize.x;
        int y_width = panelSize.y;
        fIconPanel.setBounds(x_coord, y_coord, x_width, y_width);
    }

    /**
     * @return
     */
    private int getUsableSplashScreenWidth() {
        return getSplash().getSize().x - (F_SPLASH_SCREEN_BEVEL * 2);
    }

    /**
     * 
     */
    private void loadSplashExtensions() {
        IExtension[] extensions = Platform.getExtensionRegistry().getExtensionPoint(F_SPLASH_EXTENSION_ID).getExtensions();
        for (int i = 0; i < extensions.length; i++) {
            processSplashExtension(extensions[i]);
        }
    }

    /**
     * @param extension
     */
    private void processSplashExtension(IExtension extension) {
        IConfigurationElement[] elements = extension.getConfigurationElements();
        for (int j = 0; j < elements.length; j++) {
            processSplashElements(elements[j]);
        }
    }

    /**
     * @param configurationElement
     */
    private void processSplashElements(IConfigurationElement configurationElement) {
        processSplashElementIcon(configurationElement);
        processSplashElementTooltip(configurationElement);
    }

    /**
     * @param configurationElement
     */
    private void processSplashElementTooltip(IConfigurationElement configurationElement) {
        String tooltip = configurationElement.getAttribute(F_ELEMENT_TOOLTIP);
        if ((tooltip == null) || (tooltip.length() == 0)) {
            fTooltipList.add(F_DEFAULT_TOOLTIP);
        } else {
            fTooltipList.add(tooltip);
        }
    }

    /**
     * @param configurationElement
     */
    private void processSplashElementIcon(IConfigurationElement configurationElement) {
        String iconImageFilePath = configurationElement.getAttribute(F_ELEMENT_ICON);
        if ((iconImageFilePath == null) || (iconImageFilePath.length() == 0)) {
            return;
        }
        ImageDescriptor descriptor = AbstractUIPlugin.imageDescriptorFromPlugin(configurationElement.getNamespaceIdentifier(), iconImageFilePath);
        if (descriptor == null) {
            return;
        }
        Image image = descriptor.createImage();
        if (image == null) {
            return;
        }
        if ((image.getBounds().width > F_IMAGE_WIDTH) || (image.getBounds().height > F_IMAGE_HEIGHT)) {
            image.dispose();
            return;
        }
        fImageList.add(image);
    }

    /**
     * 
     */
    private void configureUISplash() {
        GridLayout layout = new GridLayout(1, true);
        getSplash().setLayout(layout);
        getSplash().setBackgroundMode(SWT.INHERIT_DEFAULT);
    }

    /**
     * 
     */
    private void doEventLoop() {
        Shell splash = getSplash();
        if (splash.getDisplay().readAndDispatch() == false) {
            splash.getDisplay().sleep();
        }
    }

    public void dispose() {
        super.dispose();
        if ((fImageList == null) || fImageList.isEmpty()) {
            return;
        }
        Iterator<Image> iterator = fImageList.iterator();
        while (iterator.hasNext()) {
            Image image = iterator.next();
            image.dispose();
        }
    }
}
