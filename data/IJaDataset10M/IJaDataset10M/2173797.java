package uk.ac.bolton.archimate.editor.propertysections;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.IFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.PlatformUI;
import uk.ac.bolton.archimate.editor.diagram.editparts.IArchimateEditPart;
import uk.ac.bolton.archimate.editor.model.commands.EObjectFeatureCommand;
import uk.ac.bolton.archimate.editor.ui.IArchimateImages;
import uk.ac.bolton.archimate.model.IApplicationComponent;
import uk.ac.bolton.archimate.model.IApplicationInterface;
import uk.ac.bolton.archimate.model.IArchimateElement;
import uk.ac.bolton.archimate.model.IArchimatePackage;
import uk.ac.bolton.archimate.model.IBusinessInterface;
import uk.ac.bolton.archimate.model.IDevice;
import uk.ac.bolton.archimate.model.IDiagramModelArchimateObject;
import uk.ac.bolton.archimate.model.IDiagramModelObject;
import uk.ac.bolton.archimate.model.IInfrastructureInterface;
import uk.ac.bolton.archimate.model.IInterfaceElement;
import uk.ac.bolton.archimate.model.INode;

/**
 * Section to change the type of figure for a diagram object
 * 
 * @author Phillip Beauvoir
 */
public class DiagramFigureTypeSection extends AbstractArchimatePropertySection {

    private static final String HELP_ID = "uk.ac.bolton.archimate.help.diagramFigureTypeSection";

    /**
     * Filter to show or reject this section depending on input value
     */
    public static class Filter implements IFilter {

        @Override
        public boolean select(Object object) {
            if (object instanceof IArchimateEditPart) {
                IArchimateElement element = (IArchimateElement) ((IArchimateEditPart) object).getAdapter(IArchimateElement.class);
                return element instanceof IInterfaceElement || element instanceof IApplicationComponent || element instanceof IDevice || element instanceof INode;
            }
            return false;
        }
    }

    private Adapter eAdapter = new AdapterImpl() {

        @Override
        public void notifyChanged(Notification msg) {
            Object feature = msg.getFeature();
            if (feature == IArchimatePackage.Literals.DIAGRAM_MODEL_ARCHIMATE_OBJECT__TYPE) {
                refreshControls();
            }
        }
    };

    private IDiagramModelArchimateObject fDiagramObject;

    private ImageFigure figure1, figure2;

    @Override
    protected void createControls(Composite parent) {
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
        figure1 = new ImageFigure(parent);
        figure2 = new ImageFigure(parent);
    }

    protected void refreshControls() {
        IArchimateElement element = fDiagramObject.getArchimateElement();
        String imageName1 = null, imageName2 = null;
        if (element instanceof IBusinessInterface) {
            imageName1 = IArchimateImages.FIGURE_BUSINESS_INTERFACE1;
            imageName2 = IArchimateImages.FIGURE_BUSINESS_INTERFACE2;
        } else if (element instanceof IApplicationInterface) {
            imageName1 = IArchimateImages.FIGURE_APPLICATION_INTERFACE1;
            imageName2 = IArchimateImages.FIGURE_APPLICATION_INTERFACE2;
        } else if (element instanceof IInfrastructureInterface) {
            imageName1 = IArchimateImages.FIGURE_TECHNOLOGY_INTERFACE1;
            imageName2 = IArchimateImages.FIGURE_TECHNOLOGY_INTERFACE2;
        } else if (element instanceof IApplicationComponent) {
            imageName1 = IArchimateImages.FIGURE_APPLICATION_COMPONENT1;
            imageName2 = IArchimateImages.FIGURE_APPLICATION_COMPONENT2;
        } else if (element instanceof IDevice) {
            imageName1 = IArchimateImages.FIGURE_TECHNOLOGY_DEVICE1;
            imageName2 = IArchimateImages.FIGURE_TECHNOLOGY_DEVICE2;
        } else if (element instanceof INode) {
            imageName1 = IArchimateImages.FIGURE_TECHNOLOGY_NODE1;
            imageName2 = IArchimateImages.FIGURE_TECHNOLOGY_NODE2;
        }
        figure1.setImage(imageName1);
        figure2.setImage(imageName2);
        int type = fDiagramObject.getType();
        figure1.setSelected(type == 0);
        figure2.setSelected(type == 1);
    }

    @Override
    protected Adapter getECoreAdapter() {
        return eAdapter;
    }

    @Override
    protected EObject getEObject() {
        return fDiagramObject;
    }

    @Override
    protected void setElement(Object element) {
        if (element instanceof IArchimateEditPart) {
            fDiagramObject = (IDiagramModelArchimateObject) ((IAdaptable) element).getAdapter(IDiagramModelObject.class);
        }
        if (fDiagramObject == null) {
            System.err.println("Diagram Object was null in " + getClass());
        }
        refreshControls();
    }

    private class ImageFigure extends Composite {

        boolean selected;

        Label label;

        public ImageFigure(Composite parent) {
            super(parent, SWT.NULL);
            setBackgroundMode(SWT.INHERIT_DEFAULT);
            GridLayout gridLayout = new GridLayout();
            gridLayout.marginWidth = 3;
            gridLayout.marginHeight = 3;
            setLayout(gridLayout);
            addPaintListener(new PaintListener() {

                @Override
                public void paintControl(PaintEvent e) {
                    if (selected) {
                        GC graphics = e.gc;
                        graphics.setForeground(ColorConstants.blue);
                        graphics.setLineWidth(2);
                        Rectangle bounds = getBounds();
                        graphics.drawRectangle(1, 1, bounds.width - 2, bounds.height - 2);
                    }
                }
            });
            label = new Label(this, SWT.NULL);
            getWidgetFactory().adapt(this);
            label.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseDown(MouseEvent e) {
                    if (!selected && isAlive()) {
                        int newType = fDiagramObject.getType() == 0 ? 1 : 0;
                        getCommandStack().execute(new EObjectFeatureCommand(Messages.DiagramFigureTypeSection_0, getEObject(), IArchimatePackage.Literals.DIAGRAM_MODEL_ARCHIMATE_OBJECT__TYPE, newType));
                    }
                }
            });
        }

        void setImage(String imageName) {
            label.setImage(imageName == null ? null : IArchimateImages.ImageFactory.getImage(imageName));
        }

        void setSelected(boolean set) {
            selected = set;
            redraw();
        }
    }
}
