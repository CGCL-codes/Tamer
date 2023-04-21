package de.mpiwg.vspace.maps.diagram.edit.parts;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayoutAnimator;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeCompartmentEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.CreationEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.DragDropEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.diagram.ui.figures.ResizableCompartmentFigure;
import org.eclipse.gmf.runtime.draw2d.ui.figures.ConstrainedToolbarLayout;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.resource.ImageDescriptor;
import de.mpiwg.vspace.common.error.UserErrorException;
import de.mpiwg.vspace.common.error.UserErrorService;
import de.mpiwg.vspace.common.project.ProjectManager;
import de.mpiwg.vspace.diagram.figures.ImageCompartmentFigure;
import de.mpiwg.vspace.diagram.part.ExhibitionDiagramEditorPlugin;
import de.mpiwg.vspace.images.service.DatabaseService;
import de.mpiwg.vspace.maps.diagram.edit.policies.OverviewMapLinkCompartmentCanonicalEditPolicy;
import de.mpiwg.vspace.maps.diagram.edit.policies.OverviewMapLinkCompartmentItemSemanticEditPolicy;
import de.mpiwg.vspace.maps.diagram.part.Messages;
import de.mpiwg.vspace.maps.diagram.part.OverviewMapsDiagramEditorPlugin;
import de.mpiwg.vspace.maps.diagram.util.PluginPaths;
import de.mpiwg.vspace.metamodel.ExhibitionPackage;
import de.mpiwg.vspace.metamodel.LocalImage;
import de.mpiwg.vspace.metamodel.Scene;
import de.mpiwg.vspace.util.images.ImageConstants;
import de.mpiwg.vspace.util.images.ImageData;
import de.mpiwg.vspace.util.images.ImageRegistry;
import de.mpiwg.vspace.util.images.ImageScaler;

/**
 * @generated
 */
public class OverviewMapLinkCompartmentEditPart extends ShapeCompartmentEditPart {

    /**
	 * @generated
	 */
    public static final int VISUAL_ID = 7002;

    private List<EObject> notifier = new ArrayList<EObject>();

    protected ImageCompartmentFigure figure;

    /**
	 * @generated
	 */
    public OverviewMapLinkCompartmentEditPart(View view) {
        super(view);
    }

    /**
	 * @generated
	 */
    public String getCompartmentName() {
        return Messages.OverviewMapLinkCompartmentEditPart_title;
    }

    /**
	 * @generated NOT
	 */
    public IFigure createFigure() {
        org.eclipse.swt.graphics.Image image = createImage();
        figure = new ImageCompartmentFigure(getCompartmentName(), getMapMode(), image);
        figure.getContentPane().setLayoutManager(getLayoutManager());
        figure.getContentPane().addLayoutListener(LayoutAnimator.getDefault());
        figure.setTitleVisibility(false);
        EObject eobject = this.resolveSemanticElement();
        if (eobject != null) {
            addChildListener(eobject);
            if (eobject instanceof Scene) {
                de.mpiwg.vspace.metamodel.Image bgimg = ((Scene) eobject).getBackgroundImage();
                if (bgimg != null) this.addChildListener(bgimg);
            }
        }
        return figure;
    }

    /**
	 * @generated NOT
	 */
    public void addChildListener(EObject eobject) {
        if (eobject == null) return;
        if (!notifier.contains(eobject)) {
            addListenerFilter(EcoreUtil.generateUUID(), this, eobject);
            notifier.add(eobject);
        }
    }

    /**
	 * @generated NOT
	 */
    public void removeChildListener(EObject eobject) {
        if (notifier.contains(eobject)) notifier.remove(eobject);
    }

    /**
	 * @generated NOT
	 */
    protected void createDefaultEditPolicies() {
        super.createDefaultEditPolicies();
        installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE, new OverviewMapLinkCompartmentItemSemanticEditPolicy());
        installEditPolicy(EditPolicyRoles.CREATION_ROLE, new CreationEditPolicy());
        installEditPolicy(EditPolicyRoles.DRAG_DROP_ROLE, new DragDropEditPolicy());
        installEditPolicy(EditPolicyRoles.CANONICAL_ROLE, new OverviewMapLinkCompartmentCanonicalEditPolicy());
        removeEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE);
    }

    /**
	 * @generated NOT
	 */
    protected void setRatio(Double ratio) {
        if (getFigure().getParent().getLayoutManager() instanceof ConstrainedToolbarLayout) {
            super.setRatio(1.0);
        }
    }

    /**
	 * Method for creating images. This method uses the path given by the
	 * properties of the background image. Only local images are accepted by
	 * now.
	 * 
	 * @return an swt image of the new background image
	 */
    public org.eclipse.swt.graphics.Image createImage() {
        EObject sceneEObject = this.resolveSemanticElement();
        if ((sceneEObject == null) || (!(sceneEObject instanceof Scene))) return null;
        de.mpiwg.vspace.metamodel.Image backgroundImage = ((Scene) sceneEObject).getBackgroundImage();
        String imagePath = "";
        if ((backgroundImage != null) && (backgroundImage instanceof LocalImage)) {
            File tempFolder = ProjectManager.getInstance().getFolder(PluginPaths.MAP_IMAGES_FOLDER);
            int width = ((LocalImage) backgroundImage).getWidth() != 0 ? ((LocalImage) backgroundImage).getWidth() : ImageConstants.DEFAULT_SCENE_IMAGE_WIDTH;
            int height = ((LocalImage) backgroundImage).getHeight() != 0 ? ((LocalImage) backgroundImage).getHeight() : ImageConstants.DEFAULT_SCENE_IMAGE_HEIGHT;
            String path = ((LocalImage) backgroundImage).getImagePath();
            if ((path == null || path.trim().equals("")) && ((LocalImage) backgroundImage).getImageDbId() != null && !((LocalImage) backgroundImage).getImageDbId().trim().equals("")) {
                path = DatabaseService.INSTANCE.getRelativePath(((LocalImage) backgroundImage).getImageDbId());
            }
            ImageData iData = null;
            if (path != null && !path.trim().equals("")) iData = new ImageData(path, height, width);
            if (iData != null) {
                try {
                    imagePath = ImageScaler.INSTANCE.getScaledImage(iData, tempFolder.getAbsolutePath(), ImageConstants.DEFAULT_SCENE_IMAGE_WIDTH, ImageConstants.DEFAULT_SCENE_IMAGE_HEIGHT);
                } catch (UserErrorException e) {
                    UserErrorService.INSTANCE.showError(e);
                }
            }
        }
        org.eclipse.swt.graphics.Image image = null;
        if ((imagePath != null) && (!imagePath.equals(""))) image = ImageRegistry.REGISTRY.getImage(imagePath);
        if (image == null) {
            ImageDescriptor imageDesc = OverviewMapsDiagramEditorPlugin.getBundledImageDescriptor(PluginPaths.DEFAULT_MAP_IMAGE);
            image = imageDesc.createImage();
        }
        return image;
    }

    @Override
    protected void handleNotificationEvent(Notification notification) {
        if (notification.getFeature() == ExhibitionPackage.eINSTANCE.getScene_BackgroundImage()) {
            if ((notification.getOldValue() == null) && (notification.getNewValue() instanceof de.mpiwg.vspace.metamodel.Image)) addChildListener((de.mpiwg.vspace.metamodel.Image) notification.getNewValue());
            org.eclipse.swt.graphics.Image image = createImage();
            figure.setImage(image);
            figure.repaint();
        } else if (notification.getNotifier() instanceof de.mpiwg.vspace.metamodel.Image) {
            org.eclipse.swt.graphics.Image image = createImage();
            figure.setImage(image);
            figure.repaint();
        }
        super.handleNotificationEvent(notification);
    }
}
