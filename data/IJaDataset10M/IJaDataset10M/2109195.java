package de.fraunhofer.isst.axbench.views;

import java.io.File;
import java.text.MessageFormat;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.IFileEditorInput;
import de.fraunhofer.isst.axbench.ResourceManager;
import de.fraunhofer.isst.axbench.ResourceManager.Status;
import de.fraunhofer.isst.axbench.Session;
import de.fraunhofer.isst.axbench.axlang.api.IAXLangElement;
import de.fraunhofer.isst.axbench.axlang.api.IGlobalInstance;
import de.fraunhofer.isst.axbench.axlang.elements.Model;
import de.fraunhofer.isst.axbench.axlang.elements.architecturemodel.Bus;
import de.fraunhofer.isst.axbench.axlang.elements.architecturemodel.BusConnection;
import de.fraunhofer.isst.axbench.axlang.elements.architecturemodel.BusDelegation;
import de.fraunhofer.isst.axbench.axlang.elements.architecturemodel.Component;
import de.fraunhofer.isst.axbench.axlang.elements.architecturemodel.HorizontalConnection;
import de.fraunhofer.isst.axbench.axlang.elements.architecturemodel.HorizontalConnectionDataLink;
import de.fraunhofer.isst.axbench.axlang.elements.architecturemodel.DataElement;
import de.fraunhofer.isst.axbench.axlang.elements.architecturemodel.DelegationDown;
import de.fraunhofer.isst.axbench.axlang.elements.architecturemodel.DelegationUp;
import de.fraunhofer.isst.axbench.axlang.elements.architecturemodel.Port;
import de.fraunhofer.isst.axbench.axlang.elements.architecturemodel.PortRWAccess;
import de.fraunhofer.isst.axbench.axlang.elements.architecturemodel.Storage;
import de.fraunhofer.isst.axbench.axlang.elements.architecturemodel.StorageRWAccess;
import de.fraunhofer.isst.axbench.axlang.elements.architecturemodel.SubComponent;
import de.fraunhofer.isst.axbench.axlang.elements.featuremodel.Feature;
import de.fraunhofer.isst.axbench.axlang.elements.localinstances.LocalFunctionInstance;
import de.fraunhofer.isst.axbench.axlang.elements.mappings.A2RLink;
import de.fraunhofer.isst.axbench.axlang.elements.mappings.Feature2ArchitectureLink;
import de.fraunhofer.isst.axbench.axlang.elements.transactionmodel.Activation;
import de.fraunhofer.isst.axbench.axlang.elements.transactionmodel.Activity;
import de.fraunhofer.isst.axbench.axlang.elements.transactionmodel.ActivityAttribute;
import de.fraunhofer.isst.axbench.axlang.utilities.AXLangDefinition.AXLKeyword;
import de.fraunhofer.isst.axbench.axlang.utilities.attributes.Attributes;
import de.fraunhofer.isst.axbench.axlang.utilities.attributes.Direction;
import de.fraunhofer.isst.axbench.editors.axlmultipage.AXLMultiPageEditor;
import de.fraunhofer.isst.axbench.utilities.Constants;
import de.fraunhofer.isst.axbench.utilities.IAXLangElementNode;
import de.fraunhofer.isst.axbench.utilities.StructureNode;
import de.fraunhofer.isst.axbench.utilities.TreeNode;

/**
 * @brief Label provider for tree representation of an aXLang element. 
 * 
 * Formerly implemented as Adapter (AXLElementAdapter).
 * 
 * @author ekleinod
 * @author skaegebein
 * @version 0.9.0
 * @since 0.1.0
 */
public class AXLLabelProvider extends CellLabelProvider implements ILabelProvider {

    private static final String ERROR = "error";

    private static final String PATH_SEPARATOR = ".";

    private AbstractAXLView theView = null;

    private AXLMultiPageEditor theEditor = null;

    /**
     * @brief The constructor for views.
     * @param newView calling view
     */
    public AXLLabelProvider(AbstractAXLView newView) {
        theView = newView;
    }

    /**
     * @brief The constructor for multipage editors (such as the aXlang editor).
     * @param newEditor calling editor
     */
    public AXLLabelProvider(AXLMultiPageEditor newEditor) {
        theEditor = newEditor;
    }

    /**
     * @brief Returns the image (icon) for the given element.
     * @param theElement given element
     * @return image
     *  @retval null if none exists (no icons ill be displayed)
     */
    public Image getImage(Object theElement) {
        Object oElement = theElement;
        if (oElement instanceof IAXLangElementNode) {
            oElement = ((IAXLangElementNode) oElement).getIAXLangElement();
        }
        if (oElement instanceof IAXLangElement) {
            IAXLangElement axlElement = (IAXLangElement) oElement;
            String sImageID = "axlang" + ResourceManager.PATHSEPARATOR;
            sImageID += axlElement.getClassName().toLowerCase();
            if (axlElement instanceof PortRWAccess) {
                if (((PortRWAccess) axlElement).isTrigger()) {
                    sImageID += "-trigger";
                }
            }
            if (isDualArchitectureElement(axlElement)) {
                if (axlElement.isApplicationModelElement()) {
                    sImageID += "-application";
                }
                if (axlElement.isResourceModelElement()) {
                    sImageID += "-resource";
                }
            }
            sImageID += (axlElement.getAttributeValue(Attributes.CARDINALITY.getID()) == null) ? "" : "-group";
            if (axlElement.isDependent()) {
                sImageID += (axlElement.isDependent()) ? "-dependent" : "";
            } else {
                sImageID += (axlElement.isOptional()) ? "-optional" : "";
            }
            sImageID += (axlElement.isXOR()) ? "-xor" : "";
            if (axlElement.getAttributeValue(Attributes.DIRECTION.getID()) != null) {
                sImageID += "-" + Direction.fromString(axlElement.getAttributeValue(Attributes.DIRECTION.getID())).toString().toLowerCase();
            }
            Image imgRet = ResourceManager.get(sImageID);
            if (!axlElement.isValid()) {
                if (!axlElement.getClass().equals(Model.class)) {
                    imgRet = ResourceManager.get(Status.ERROR.getKey());
                } else {
                    imgRet = ResourceManager.get(sImageID + "_error");
                }
            }
            return imgRet;
        }
        if (oElement instanceof StructureNode) {
            StructureNode ndeElement = (StructureNode) oElement;
            String sImageID = "axlang" + ResourceManager.PATHSEPARATOR + "structure-";
            sImageID += ndeElement.getKind().toString().toLowerCase();
            Image imgRet = ResourceManager.get(sImageID);
            if (ndeElement.hasError()) {
                imgRet = ResourceManager.get(Status.ERROR.getKey());
            }
            return imgRet;
        }
        return null;
    }

    /**
	 * @brief indicates whether the axlElement is of a type that can be used
	 * both in an application model and a resource model
	 * (applies to feature-to-architecture mappings, too)
	 * 
	 * @param axlElement the element that is checked
	 * @return true if the type of the element can be used both in an application model and a resource model
	 */
    private boolean isDualArchitectureElement(IAXLangElement axlElement) {
        return (axlElement instanceof Component) || (axlElement instanceof HorizontalConnection) || (axlElement instanceof Port) || (axlElement instanceof SubComponent) || (axlElement instanceof Feature2ArchitectureLink);
    }

    /**
	 * @brief Returns the label for the given element.
	 * @param theElement given element
	 * @return label
	 *  @retval null if element is no aXLang element (nothing will be shown)
	 */
    public String getText(Object theElement) {
        Object oElement = theElement;
        if (oElement instanceof IAXLangElementNode) {
            oElement = ((IAXLangElementNode) oElement).getIAXLangElement();
        }
        if (oElement instanceof IAXLangElement) {
            return MessageFormat.format("{0}: ''{1}''", ((IAXLangElement) oElement).getClassName(), ((IAXLangElement) oElement).getIdentifier());
        }
        if (oElement instanceof StructureNode) {
            return ((StructureNode) oElement).getKind().getID();
        }
        return null;
    }

    /**
	 * @brief Returns the identifier of the type of the element: 
	 * -# subcomponent -> component type 
	 * -# port -> interface 
	 * -# variable -> data type 
	 * -# hwsubcomponent -> hwcomponent type 
	 * -# data element -> data type else null
	 * @param axlElement the typed element
	 * @return the id of the type of the element, or null
	 */
    protected String getType(IAXLangElement axlElement) {
        if (axlElement instanceof Port) {
            Port port = (Port) axlElement;
            if (port.isApplicationModelElement()) {
                StringBuilder sbType = new StringBuilder();
                boolean isFurther = false;
                for (DataElement dataElement : port.getDataElements()) {
                    if (isFurther) {
                        sbType.append(", ");
                    }
                    isFurther = true;
                    sbType.append(dataElement.getIdentifier());
                }
                return sbType.toString();
            }
            if (port.isResourceModelElement()) {
                return port.getHWPortType().toString();
            }
        }
        if (axlElement instanceof Bus) {
            return ((Bus) axlElement).getBusType().toString();
        }
        if (axlElement instanceof Storage) {
            return ((Storage) axlElement).getDataType().toString();
        }
        if (axlElement instanceof SubComponent) {
            return ((SubComponent) axlElement).getComponentType().getIdentifier();
        }
        if (axlElement instanceof DataElement) {
            return ((DataElement) axlElement).getDataType().toString();
        }
        return null;
    }

    /**
	 * @brief Returns a string representation of the referenced contents of an
	 *        element (includes a simple error list).
	 *        
	 * Special implementation for elements without identifier and children.
	 * 
	 * @param axlElement
	 * @return string representation of the contents, or null
	 */
    protected String getContents(IAXLangElement axlElement) {
        if (!axlElement.isIdentifierGenerated()) {
            return axlElement.getIdentifier();
        }
        String connectionSymbol = (axlElement.isApplicationModelElement() ? "->" : "--");
        if (axlElement instanceof HorizontalConnection) {
            return MessageFormat.format("{0}.{1} {2} {3}.{4}", ((HorizontalConnection) axlElement).getSourceSubComponent().getIdentifier(), ((HorizontalConnection) axlElement).getSourcePort().getIdentifier(), connectionSymbol, ((HorizontalConnection) axlElement).getTargetSubComponent().getIdentifier(), ((HorizontalConnection) axlElement).getTargetPort().getIdentifier());
        }
        if (axlElement instanceof DelegationDown) {
            return MessageFormat.format("this.{0} {1} {2}.{3}", ((DelegationDown) axlElement).getSourcePort().getIdentifier(), connectionSymbol, ((DelegationDown) axlElement).getTargetSubComponent().getIdentifier(), ((DelegationDown) axlElement).getTargetPort().getIdentifier());
        }
        if (axlElement instanceof DelegationUp) {
            return MessageFormat.format("{0}.{1} {2} this.{3}", ((DelegationUp) axlElement).getSourceSubComponent().getIdentifier(), ((DelegationUp) axlElement).getSourcePort().getIdentifier(), connectionSymbol, ((DelegationUp) axlElement).getTargetPort().getIdentifier());
        }
        if (axlElement instanceof HorizontalConnectionDataLink) {
            return MessageFormat.format("{0} -> {1}", ((HorizontalConnectionDataLink) axlElement).getSourceDataElement().getIdentifier(), ((HorizontalConnectionDataLink) axlElement).getTargetDataElement().getIdentifier());
        }
        if (axlElement instanceof BusConnection) {
            return MessageFormat.format("{0} {1} {2}.{3}", ((BusConnection) axlElement).getHWBus().getIdentifier(), connectionSymbol, ((BusConnection) axlElement).getTargetSubComponent().getIdentifier(), ((BusConnection) axlElement).getTargetPort().getIdentifier());
        }
        if (axlElement instanceof BusDelegation) {
            return MessageFormat.format("{0} {1} {2}.{3}", ((BusDelegation) axlElement).getHWBus().getIdentifier(), connectionSymbol, AXLKeyword.THIS.getAXL(), ((BusDelegation) axlElement).getTargetPort().getIdentifier());
        }
        if (axlElement instanceof PortRWAccess) {
            return MessageFormat.format("{0}.{1}", ((PortRWAccess) axlElement).getPort().getIdentifier(), ((PortRWAccess) axlElement).getDataElement().getIdentifier());
        }
        if (axlElement instanceof StorageRWAccess) {
            return ((StorageRWAccess) axlElement).getStorage().getIdentifier();
        }
        if (axlElement instanceof LocalFunctionInstance) {
            return MessageFormat.format("{0}.{1}", ((LocalFunctionInstance) axlElement).getSubComponent().getIdentifier(), ((LocalFunctionInstance) axlElement).getInstantiatedElement().getIdentifier());
        }
        if (axlElement instanceof Activity) {
            if (((Activity) axlElement).getGlobalFunctionInstance() != null) {
                return MessageFormat.format("{0} {1}", AXLKeyword.FUNCTION.getAXL(), ((Activity) axlElement).getGlobalFunctionInstance().toPathString("."));
            }
            if (((Activity) axlElement).getGlobalHWSubComponentInstance() != null) {
                return MessageFormat.format("{0} {1}", AXLKeyword.HW_COMPONENT.getAXL(), ((Activity) axlElement).getGlobalHWSubComponentInstance().toPathString("."));
            }
            if (((Activity) axlElement).getGlobalHWBusInstance() != null) {
                return MessageFormat.format("{0} {1}", AXLKeyword.HW_BUS.getAXL(), ((Activity) axlElement).getGlobalHWBusInstance().toPathString("."));
            }
            return ERROR;
        }
        if (axlElement instanceof Activation) {
            if (((Activation) axlElement).getGlobalFunctionInstance() != null) {
                return MessageFormat.format("{0} {1}: {2}.{3} {4}", AXLKeyword.FUNCTION.getAXL(), ((Activation) axlElement).getGlobalFunctionInstance().toPathString("."), ((Activation) axlElement).getTrigger().getPort().getIdentifier(), ((Activation) axlElement).getTrigger().getDataElement().getIdentifier(), axlElement.getAttributeValue(Attributes.ACTIVATIONKIND.getID()));
            }
            if (((Activation) axlElement).getGlobalHWSubComponentInstance() != null) {
                return MessageFormat.format("{0} {1} {2}", AXLKeyword.HW_COMPONENT.getAXL(), ((Activation) axlElement).getGlobalHWSubComponentInstance().toPathString("."), axlElement.getAttributeValue(Attributes.ACTIVATIONKIND.getID()));
            }
            if (((Activation) axlElement).getGlobalHWBusInstance() != null) {
                return MessageFormat.format("{0} {1} {2}", AXLKeyword.HW_COMPONENT.getAXL(), ((Activation) axlElement).getGlobalHWBusInstance().toPathString("."), axlElement.getAttributeValue(Attributes.ACTIVATIONKIND.getID()));
            }
            return ERROR;
        }
        if (axlElement instanceof ActivityAttribute) {
            if (((ActivityAttribute) axlElement).getGlobalFunctionInstance() != null) {
                return MessageFormat.format("{0} {1}", AXLKeyword.FUNCTION.getAXL(), ((ActivityAttribute) axlElement).getGlobalFunctionInstance().toPathString("."));
            }
            if (((ActivityAttribute) axlElement).getGlobalHWSubComponentInstance() != null) {
                return MessageFormat.format("{0} {1}", AXLKeyword.HW_COMPONENT.getAXL(), ((ActivityAttribute) axlElement).getGlobalHWSubComponentInstance().toPathString("."));
            }
            if (((ActivityAttribute) axlElement).getGlobalHWBusInstance() != null) {
                return MessageFormat.format("{0} {1}", AXLKeyword.HW_BUS.getAXL(), ((ActivityAttribute) axlElement).getGlobalHWBusInstance().toPathString("."));
            }
            return ERROR;
        }
        if (axlElement instanceof Feature2ArchitectureLink) {
            Feature2ArchitectureLink theLink = ((Feature2ArchitectureLink) axlElement);
            String sSource = toPathNotation(theLink.getFeature());
            String sTarget = new String();
            boolean isFirst = true;
            for (IGlobalInstance targetInstance : theLink.getTargetInstances()) {
                if (!isFirst) {
                    sTarget += ", ";
                }
                isFirst = false;
                sTarget += targetInstance.toPathString(PATH_SEPARATOR);
            }
            return MessageFormat.format("{0} -> {1}", sSource, sTarget);
        }
        if (axlElement instanceof A2RLink) {
            A2RLink a2rLink = (A2RLink) axlElement;
            return MessageFormat.format("{0} -> {1}", a2rLink.getSourceInstance().toPathString(PATH_SEPARATOR), a2rLink.getTargetInstance().toPathString(PATH_SEPARATOR));
        }
        return null;
    }

    /**
	 * Update the label for cell.
	 * 
	 * @param cell {@link ViewerCell}
	 */
    @Override
    public void update(ViewerCell cell) {
        if ((cell.getElement() instanceof TreeNode) && !((cell.getElement() instanceof IAXLangElement) || (cell.getElement() instanceof StructureNode))) {
            TreeNode node = (TreeNode) cell.getElement();
            handleFont(node);
            if (node.isIncluded()) {
                cell.setFont(ResourceManager.italicfont);
            } else if (node.isNotexpected()) {
                cell.setFont(ResourceManager.bolditalicfont);
                cell.setForeground(ResourceManager.RED);
            } else if (node.isExcluded()) {
                cell.setForeground(ResourceManager.GREY);
            } else {
                cell.setForeground(ResourceManager.BLACK);
                cell.setFont(ResourceManager.defaultfont);
            }
        }
        cell.setImage(this.getImage(cell.getElement()));
        cell.setText(this.getText(cell.getElement()));
    }

    /**
	 * @brief handles the font of a tree node (IAXLangElementNode and FeatureInSelection).
	 * @param node tree node
	 */
    private void handleFont(TreeNode node) {
        AXLMultiPageEditor theAxlMultiPageEditor = null;
        if (theView == null) {
            theAxlMultiPageEditor = theEditor;
        } else {
            theAxlMultiPageEditor = theView.getMymultipageeditor();
        }
        node.setIncluded(false);
        node.setNotexpected(false);
        IAXLangElement element = node.getIAXLangElement();
        String filename = element.getMetaInformation().getFilename();
        if (filename == null) {
            filename = Constants.getInstance().findFileName(element);
        }
        if (filename != null) {
            if (Session.getFilelocrelative().get(theAxlMultiPageEditor) != null) {
                filename = filename.replace('\\', '/');
                if (!filename.equals(Session.getFilelocrelative().get(theAxlMultiPageEditor))) {
                    File f = ((IFileEditorInput) theAxlMultiPageEditor.getAxlEditor().getEditorInput()).getFile().getRawLocation().toFile();
                    String includefilename = element.getMetaInformation().getIncludeFileName();
                    if (includefilename == null) {
                        includefilename = Constants.getInstance().findIncludeFileName(element);
                    }
                    if (includefilename != null) {
                        if (Constants.getInstance().isIncludedFile(includefilename, f)) {
                            node.setIncluded(true);
                        } else {
                            node.setExcluded(true);
                        }
                    } else {
                        node.setNotexpected(true);
                    }
                }
            }
        } else {
            node.setNotexpected(true);
        }
    }

    /**
	 * @brief Get the text displayed in the tool tip for object.
	 * 
	 * If {@link #getToolTipText(Object)} and {@link #getToolTipImage(Object)} both return 
	 * @c null the control is set back to standard behavior
	 * 
	 * @param element the element for which the tool tip is shown
	 * @return the tooltip text
	 *  @retval null if there is not text to display
	 */
    @Override
    public String getToolTipText(Object element) {
        if (element instanceof IAXLangElementNode) {
            IAXLangElement theElement = ((IAXLangElementNode) element).getIAXLangElement();
            String sToolTip = theElement.getDescription();
            if (((TreeNode) element).isIncluded() || ((TreeNode) element).isExcluded()) {
                String filename = theElement.getMetaInformation().getFilename();
                if (filename == null) {
                    filename = Constants.getInstance().findFileName(theElement);
                }
                if (filename != null) {
                    if (!sToolTip.isEmpty()) {
                        sToolTip += "\n";
                    }
                    sToolTip += "Destination file: " + filename.replace('\\', '/');
                }
            }
            if (sToolTip.isEmpty()) {
                return null;
            }
            return sToolTip;
        }
        return null;
    }

    /**
	 * @brief Return the amount of pixels in x and y direction you want the tool tip to
	 *        pop up from the mouse pointer.
	 *        
	 * Returns Point(5, 5);
	 * 
	 * @param object the element for which the tool tip is shown
	 * @return {@link Point} to shift of the tool tip
	 */
    @Override
    public Point getToolTipShift(Object object) {
        return new Point(5, 5);
    }

    /**
	 * @brief The time in milliseconds until the tool tip is displayed.
	 * 
	 * Returns 500 milliseconds.
	 * 
	 * @param object the {@link Object} for which the tool tip is shown
	 * @return time in milliseconds until the tool tip is displayed
	 */
    @Override
    public int getToolTipDisplayDelayTime(Object object) {
        return 500;
    }

    /**
	 * @brief The time in milliseconds the tool tip is shown for.
	 * 
	 * Returns 25000 milliseconds, i.e. 25 seconds 
	 * 
	 * @param object the {@link Object} for which the tool tip is shown
	 * @return time in milliseconds the tool tip is shown for
	 */
    @Override
    public int getToolTipTimeDisplayed(Object object) {
        return 25000;
    }

    /**
	 * @brief Returns a string representation of a feature in path notation.
	 *
	 * @param theFeature feature
	 * @return path notation of feature
	 */
    private String toPathNotation(Feature theFeature) {
        if (theFeature == null) {
            return ERROR;
        }
        StringBuilder sbReturn = new StringBuilder();
        if (theFeature.getParent() instanceof Feature) {
            sbReturn.append(toPathNotation((Feature) theFeature.getParent()));
            sbReturn.append(PATH_SEPARATOR);
        }
        sbReturn.append(theFeature.getIdentifier());
        return sbReturn.toString();
    }
}
