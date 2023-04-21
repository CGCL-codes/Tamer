package org.nakedobjects.plugins.dndviewer.viewer;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;
import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.facets.object.value.ValueFacet;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.plugins.dndviewer.Content;
import org.nakedobjects.plugins.dndviewer.ObjectContent;
import org.nakedobjects.plugins.dndviewer.OneToManyField;
import org.nakedobjects.plugins.dndviewer.TextParseableContent;
import org.nakedobjects.plugins.dndviewer.TextParseableField;
import org.nakedobjects.plugins.dndviewer.View;
import org.nakedobjects.plugins.dndviewer.ViewAxis;
import org.nakedobjects.plugins.dndviewer.ViewFactory;
import org.nakedobjects.plugins.dndviewer.ViewRequirement;
import org.nakedobjects.plugins.dndviewer.ViewSpecification;
import org.nakedobjects.plugins.dndviewer.viewer.basic.FallbackView;
import org.nakedobjects.plugins.dndviewer.viewer.basic.MinimizedView;
import org.nakedobjects.plugins.dndviewer.viewer.border.DisposedObjectBorder;
import org.nakedobjects.plugins.dndviewer.viewer.list.InternalListSpecification;
import org.nakedobjects.plugins.dndviewer.viewer.view.dialog.ActionDialogSpecification;
import org.nakedobjects.plugins.dndviewer.viewer.view.simple.DragViewOutline;

/**
 * This class holds all the different view types that all the different objects can be viewed as.
 */
public class SkylarkViewFactory implements ViewFactory {

    private static final ViewSpecification fallback = new FallbackView.Specification();

    private static final ViewSpecification dialogSpec = new ActionDialogSpecification();

    public static final int INTERNAL = 2;

    private static final Logger LOG = Logger.getLogger(SkylarkViewFactory.class);

    public static final int WINDOW = 1;

    private ViewSpecification emptyFieldSpecification;

    private final Vector rootViews = new Vector();

    private ViewSpecification smallObjectIconSpecification;

    private ViewSpecification smallApplicationClassIconSpecification;

    private final Vector subviews = new Vector();

    private final Vector valueFields = new Vector();

    private ViewSpecification workspaceClassIconSpecification;

    private ViewSpecification workspaceServiceIconSpecification;

    private ViewSpecification workspaceObjectIconSpecification;

    private ViewSpecification rootWorkspaceSpecification;

    private ViewSpecification workspaceSpecification;

    private ViewSpecification dragContentSpecification;

    private List<ViewSpecification> viewSpecifications = new ArrayList<ViewSpecification>();

    public void addSpecification(final ViewSpecification spec) {
        viewSpecifications.add(spec);
    }

    /**
     * @deprecated
     */
    public void addServiceIconSpecification(final ViewSpecification spec) {
        workspaceServiceIconSpecification = spec;
    }

    /**
     * @deprecated
     */
    public void addCompositeRootViewSpecification(final ViewSpecification spec) {
        rootViews.addElement(spec);
    }

    /**
     * @deprecated
     */
    public void addCompositeSubviewViewSpecification(final ViewSpecification spec) {
        subviews.addElement(spec);
    }

    public void addEmptyFieldSpecification(final ViewSpecification spec) {
        emptyFieldSpecification = spec;
    }

    /**
     * @deprecated
     */
    public void addObjectIconSpecification(final ViewSpecification spec) {
        workspaceObjectIconSpecification = spec;
        viewSpecifications.add(spec);
    }

    /**
     * @deprecated
     */
    public void addSubviewIconSpecification(final ViewSpecification spec) {
        smallObjectIconSpecification = spec;
        viewSpecifications.add(spec);
    }

    /**
     * @deprecated
     */
    public void addSubviewApplicationClassIconSpecification(final ViewSpecification spec) {
        smallApplicationClassIconSpecification = spec;
    }

    /**
     * @deprecated
     */
    public void addValueFieldSpecification(final ViewSpecification spec) {
        valueFields.addElement(spec);
    }

    /**
     * @deprecated
     */
    public void addRootWorkspaceSpecification(final ViewSpecification spec) {
        rootWorkspaceSpecification = spec;
    }

    /**
     * @deprecated
     */
    public void addWorkspaceSpecification(final ViewSpecification spec) {
        workspaceSpecification = spec;
    }

    public Enumeration closedSubviews(final Content forContent, final View replacingView) {
        final Vector v = new Vector();
        if (forContent instanceof ObjectContent) {
            v.addElement(smallObjectIconSpecification);
        }
        return v.elements();
    }

    public View createIcon(final Content content) {
        final ViewSpecification spec = getIconizedRootViewSpecification(content);
        final View view = createView(spec, content, null);
        LOG.debug("creating " + view + " (icon) for " + content);
        return view;
    }

    public View createWindow(final Content content) {
        final ViewSpecification spec = getOpenRootViewSpecification(content);
        final View view = createView(spec, content, null);
        LOG.debug("creating " + view + " (window) for " + content);
        return view;
    }

    public View createDialog(final Content content) {
        return createView(dialogSpec, content, null);
    }

    public View createFieldView(final ObjectContent content, final ViewAxis axis) {
        final ViewSpecification objectFieldSpecification = getIconizedSubViewSpecification(content);
        return createView(objectFieldSpecification, content, axis);
    }

    public View createFieldView(final TextParseableField content, final ViewAxis axis) {
        final ViewSpecification valueFieldSpecification = getValueFieldSpecification(content);
        return createView(valueFieldSpecification, content, axis);
    }

    public View createInternalList(final OneToManyField content, final ViewAxis axis) {
        final ViewSpecification listSpecification = new InternalListSpecification();
        return createView(listSpecification, content, axis);
    }

    private View createView(final ViewSpecification specification, final Content content, final ViewAxis axis) {
        ViewSpecification spec;
        if (specification == null) {
            LOG.warn("no suitable view for " + content + " using fallback view");
            spec = new FallbackView.Specification();
        } else {
            spec = specification;
        }
        View createView = spec.createView(content, axis);
        if (content.isObject()) {
            final NakedObject adapter = content.getNaked();
            if (adapter != null && adapter.getResolveState().isDestroyed()) {
                createView = new DisposedObjectBorder(createView);
            }
        }
        createView.getSubviews();
        return createView;
    }

    public View createInnerWorkspace(final Content content) {
        LOG.debug("creating inner workspace for " + content);
        final View view = createView(workspaceSpecification, content, null);
        return view;
    }

    private ViewSpecification defaultViewSpecification(final Vector availableViews, final Content content) {
        final Enumeration fields = availableViews.elements();
        while (fields.hasMoreElements()) {
            final ViewSpecification spec = (ViewSpecification) fields.nextElement();
            ViewRequirement requirement = new ViewRequirement(content, ViewRequirement.CLOSED);
            if (spec.canDisplay(content, requirement)) {
                return spec;
            }
        }
        LOG.warn("no suitable view for " + content + " using fallback view");
        return new FallbackView.Specification();
    }

    private ViewSpecification ensureView(final ViewSpecification spec) {
        if (spec == null) {
            LOG.error("missing view; using fallback");
            return new FallbackView.Specification();
        } else {
            return spec;
        }
    }

    public void debugData(final DebugString sb) {
        sb.append("RootsViews\n");
        Enumeration fields = rootViews.elements();
        while (fields.hasMoreElements()) {
            final ViewSpecification spec = (ViewSpecification) fields.nextElement();
            sb.append("  ");
            sb.append(spec);
            sb.append("\n");
        }
        sb.append("\n\n");
        sb.append("Subviews\n");
        fields = subviews.elements();
        while (fields.hasMoreElements()) {
            final ViewSpecification spec = (ViewSpecification) fields.nextElement();
            sb.append("  ");
            sb.append(spec);
            sb.append("\n");
        }
        sb.append("\n\n");
        sb.append("Value fields\n");
        fields = valueFields.elements();
        while (fields.hasMoreElements()) {
            final ViewSpecification spec = (ViewSpecification) fields.nextElement();
            sb.append("  ");
            sb.append(spec);
            sb.append("\n");
        }
        sb.append("\n\n");
        sb.append("Specifications\n");
        for (ViewSpecification spec : viewSpecifications) {
            sb.append("  ");
            sb.append(spec);
            sb.append("\n");
        }
        sb.append("\n\n");
    }

    public String debugTitle() {
        return "View factory entries";
    }

    public ViewSpecification getContentDragSpecification() {
        return dragContentSpecification;
    }

    public ViewSpecification getEmptyFieldSpecification() {
        if (emptyFieldSpecification == null) {
            LOG.error("missing empty field specification; using fallback");
            return fallback;
        }
        return emptyFieldSpecification;
    }

    public ViewSpecification getIconizedRootViewSpecification(final Content content) {
        if (content.getNaked().getSpecification().isService()) {
            if (workspaceServiceIconSpecification == null) {
                LOG.error("missing workspace class icon specification; using fallback");
                return fallback;
            }
            return ensureView(workspaceServiceIconSpecification);
        } else {
            if (workspaceObjectIconSpecification == null) {
                LOG.error("missing workspace object icon specification; using fallback");
                return fallback;
            }
            return ensureView(workspaceObjectIconSpecification);
        }
    }

    public ViewSpecification getIconizedSubViewSpecification(final Content content) {
        if (content.getNaked() == null) {
            return getEmptyFieldSpecification();
        } else if (content.getNaked().getSpecification().isService()) {
            if (smallApplicationClassIconSpecification == null) {
                LOG.error("missing small class icon specification; using fall back");
                return fallback;
            }
            return ensureView(smallApplicationClassIconSpecification);
        } else {
            if (smallObjectIconSpecification == null) {
                LOG.error("missing small object icon specification; using fall back");
                return fallback;
            }
            return ensureView(smallObjectIconSpecification);
        }
    }

    private ViewSpecification getOpenRootViewSpecification(final Content content) {
        return defaultViewSpecification(rootViews, content);
    }

    /**
     * @deprecated - views should be specific about what subviews they create; and allow the user to change
     *             them later
     */
    @Deprecated
    public ViewSpecification getOpenSubViewSpecification(final ObjectContent content) {
        return defaultViewSpecification(subviews, content);
    }

    public ViewSpecification getOverlayViewSpecification(final Content content) {
        return fallback;
    }

    public ViewSpecification getValueFieldSpecification(final TextParseableContent content) {
        if (content.isOptionEnabled()) {
        }
        return defaultViewSpecification(valueFields, content);
    }

    public Enumeration valueViews(final Content forContent, final View replacingView) {
        return new Vector().elements();
    }

    public void setDragContentSpecification(final ViewSpecification dragContentSpecification) {
        this.dragContentSpecification = dragContentSpecification;
    }

    public View createDragViewOutline(final View view) {
        return new DragViewOutline(view);
    }

    public View createMinimizedView(final View view) {
        return new MinimizedView(view);
    }

    public View createView(ViewRequirement requirement) {
        final ViewSpecification objectFieldSpecification = getSpecificationForRequirement(requirement);
        return createView(objectFieldSpecification, requirement.getContent(), requirement.getAxis());
    }

    public ViewSpecification getSpecificationForRequirement(final ViewRequirement requirement) {
        Content content = requirement.getContent();
        NakedObjectSpecification specification = content.getSpecification();
        boolean isValue = specification != null && specification.containsFacet(ValueFacet.class);
        if (content.isObject() && !isValue && content.getNaked() == null) {
            return getEmptyFieldSpecification();
        } else {
            for (ViewSpecification viewSpecification : viewSpecifications) {
                if (viewSpecification.canDisplay(content, requirement)) {
                    return viewSpecification;
                }
            }
            LOG.error("missing specification; using fall back");
            return fallback;
        }
    }

    public Enumeration availableViews(final ViewRequirement requirement) {
        final Vector v = new Vector();
        for (ViewSpecification specification : viewSpecifications) {
            if (specification.canDisplay(requirement.getContent(), requirement)) {
                v.addElement(specification);
            }
        }
        return v.elements();
    }
}
