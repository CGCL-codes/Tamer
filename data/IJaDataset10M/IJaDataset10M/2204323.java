package com.volantis.mcs.protocols;

import com.volantis.mcs.layouts.Format;
import com.volantis.mcs.layouts.FormatType;
import com.volantis.mcs.layouts.Fragment;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.layouts.FormatInstance;
import com.volantis.mcs.protocols.layouts.LayoutAttributesFactory;
import com.volantis.mcs.protocols.layouts.LayoutAttributesFactoryImpl;
import com.volantis.mcs.protocols.layouts.LayoutModule;
import com.volantis.mcs.protocols.renderer.RendererException;
import com.volantis.mcs.protocols.renderer.layouts.FormatRendererContext;
import com.volantis.mcs.protocols.renderer.layouts.FragmentLinkWriter;
import com.volantis.mcs.protocols.renderer.layouts.SegmentLinkWriter;
import com.volantis.mcs.runtime.layouts.RuntimeDeviceLayout;
import com.volantis.mcs.runtime.layouts.styling.FormatStylingEngine;
import com.volantis.mcs.runtime.styling.CompiledStyleSheetCollection;
import com.volantis.styling.sheet.CompiledStyleSheet;
import com.volantis.synergetics.log.LogDispatcher;
import java.io.IOException;

/**
 * This class renders a layout to the page.<p>
 *
 * Many of the methods were copied from Layout and the Format classes and so
 * have very similar names. For this reason separating comments have been
 * added to group the methods into sections relating to one particular Format
 * sub class. Please make sure that when adding new methods to this class that
 * you follow the same style as otherwise it will become very difficult to
 * maintain.
 */
public class DeviceLayoutRenderer {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = LocalizationFactory.createLogger(DeviceLayoutRenderer.class);

    /**
     * The reference to the single allowable instance of this class.
     */
    private static DeviceLayoutRenderer singleton;

    /**
     * Get the single allowable instance of this class.
     * @return The single allowable instance of this class.
     */
    public static DeviceLayoutRenderer getSingleton() {
        if (singleton == null) {
            singleton = new DeviceLayoutRenderer(new LayoutAttributesFactoryImpl());
        }
        return singleton;
    }

    /**
     * The factory to use to create attributes classes.
     */
    private final LayoutAttributesFactory factory;

    /**
     * Initialise.
     *
     * @param factory The factory to use to construct any attributes classes
     * used internally.
     */
    public DeviceLayoutRenderer(LayoutAttributesFactory factory) {
        this.factory = factory;
    }

    /**
     * Render the layout.
     * @param context The layout to render.
     */
    public void renderLayout(DeviceLayoutContext context, FormatRendererContext formatRendererContext) throws IOException, RendererException {
        NDimensionalIndex index = NDimensionalIndex.ZERO_DIMENSIONS;
        LayoutModule module = formatRendererContext.getLayoutModule();
        formatRendererContext.pushDeviceLayoutContext(context);
        RuntimeDeviceLayout deviceLayout = formatRendererContext.getDeviceLayout();
        LayoutAttributes attributes = factory.createLayoutAttributes();
        attributes.setDeviceLayoutContext(context);
        RuntimeDeviceLayout runtimeDeviceLayout = context.getDeviceLayout();
        CompiledStyleSheet layoutStyleSheet = runtimeDeviceLayout.getCompiledStyleSheet();
        FormatStylingEngine formatStylingEngine = formatRendererContext.getFormatStylingEngine();
        formatStylingEngine.pushStyleSheet(layoutStyleSheet);
        CompiledStyleSheetCollection themeStyleSheets = context.getThemeStyleSheets();
        if (themeStyleSheets != null) {
            themeStyleSheets.pushAll(formatStylingEngine);
        }
        boolean inclusion = (context.getIncludingDeviceLayoutContext() != null);
        if (inclusion) {
            module.beginNestedInclusion();
        }
        module.writeOpenLayout(attributes);
        Fragment fragment = formatRendererContext.getCurrentFragment();
        if (fragment == null) {
            if (logger.isDebugEnabled()) {
                String name = deviceLayout != null ? deviceLayout.getName() : null;
                logger.debug("Writing out the Layout named " + name + " to the page");
            }
            Format root = deviceLayout.getRootFormat();
            if (root != null) {
                FormatInstance rootInstance = formatRendererContext.getFormatInstance(root, index);
                formatRendererContext.renderFormat(rootInstance);
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("Empty layout");
                }
            }
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("Writing out the fragment named " + fragment.getName() + " to the page");
            }
            if (!module.getSupportsFragmentLinkListTargetting()) {
                FormatInstance fragmentInstance = formatRendererContext.getFormatInstance(fragment, index);
                formatRendererContext.renderFormat(fragmentInstance);
                writeFragmentLinkList(fragment, formatRendererContext);
            } else {
                writeFragmentLinkList(fragment, formatRendererContext);
                FormatInstance fragmentInstance = formatRendererContext.getFormatInstance(fragment, index);
                formatRendererContext.renderFormat(fragmentInstance);
            }
        }
        SegmentLinkWriter segmentLinkWriter = formatRendererContext.getSegmentLinkWriter();
        segmentLinkWriter.writeDefaultSegmentLink();
        module.writeCloseLayout(attributes);
        if (inclusion) {
            module.endNestedInclusion();
        }
        if (themeStyleSheets != null) {
            themeStyleSheets.popAll(formatStylingEngine);
        }
        formatStylingEngine.popStyleSheet(layoutStyleSheet);
        formatRendererContext.popDeviceLayoutContext();
    }

    /**
     * Write out the parent and peer links that form a fragment link list.
     *
     * @param fragment the fragment being rendering.
     * @param context
     * @throws IOException
     * @throws RendererException
     */
    private void writeFragmentLinkList(Fragment fragment, FormatRendererContext context) throws IOException, RendererException {
        Fragment enclosingFragment = fragment.getEnclosingFragment();
        if (logger.isDebugEnabled()) {
            logger.debug("Fragment is " + fragment);
            logger.debug("Enclosing fragment is " + enclosingFragment);
        }
        FragmentLinkWriter fragmentLinkWriter = context.getFragmentLinkWriter();
        if (enclosingFragment != null) {
            if (fragment.isParentLinkFirst()) {
                fragmentLinkWriter.writeFragmentLink(context, fragment, enclosingFragment, true, true);
                writePeerFragmentLinks(context, fragment);
            } else {
                writePeerFragmentLinks(context, fragment);
                fragmentLinkWriter.writeFragmentLink(context, fragment, enclosingFragment, true, true);
            }
        }
    }

    private void writePeerFragmentLinks(FormatRendererContext context, Fragment fragment) throws IOException, RendererException {
        if (!fragment.getPeerLinks()) {
            return;
        }
        FragmentLinkWriter fragmentLinkWriter = context.getFragmentLinkWriter();
        Format parent = fragment.getParent();
        if (parent != null) {
            int numChildren = parent.getNumChildren();
            if (logger.isDebugEnabled()) {
                logger.debug("Searching " + numChildren + " peers");
            }
            for (int child = 0; child < numChildren; child++) {
                Format peer = parent.getChildAt(child);
                if (logger.isDebugEnabled()) {
                    logger.debug("Found peer " + peer);
                }
                if ((peer != null) && peer.getFormatType().equals(FormatType.FRAGMENT)) {
                    Fragment peerFragment = (Fragment) peer;
                    if (!peerFragment.getName().equals(fragment.getName())) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("Adding link to peer fragment" + peerFragment.getName());
                        }
                        fragmentLinkWriter.writeFragmentLink(context, fragment, peerFragment, true, false);
                    }
                }
            }
        }
    }
}
