package SensorDataWebGui.diagram.navigator;

import org.eclipse.gmf.runtime.common.ui.services.parser.IParser;
import org.eclipse.gmf.runtime.common.ui.services.parser.ParserOptions;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.ITreePathLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.ViewerLabel;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.navigator.ICommonContentExtensionSite;
import org.eclipse.ui.navigator.ICommonLabelProvider;

/**
 * @generated
 */
public class SensorDataWebGuiNavigatorLabelProvider extends LabelProvider implements ICommonLabelProvider, ITreePathLabelProvider {

    /**
	 * @generated
	 */
    static {
        SensorDataWebGui.diagram.part.SensorDataWebGuiDiagramEditorPlugin.getInstance().getImageRegistry().put("Navigator?UnknownElement", ImageDescriptor.getMissingImageDescriptor());
        SensorDataWebGui.diagram.part.SensorDataWebGuiDiagramEditorPlugin.getInstance().getImageRegistry().put("Navigator?ImageNotFound", ImageDescriptor.getMissingImageDescriptor());
    }

    /**
	 * @generated
	 */
    public void updateLabel(ViewerLabel label, TreePath elementPath) {
        Object element = elementPath.getLastSegment();
        if (element instanceof SensorDataWebGui.diagram.navigator.SensorDataWebGuiNavigatorItem && !isOwnView(((SensorDataWebGui.diagram.navigator.SensorDataWebGuiNavigatorItem) element).getView())) {
            return;
        }
        label.setText(getText(element));
        label.setImage(getImage(element));
    }

    /**
	 * @generated
	 */
    public Image getImage(Object element) {
        if (element instanceof SensorDataWebGui.diagram.navigator.SensorDataWebGuiNavigatorGroup) {
            SensorDataWebGui.diagram.navigator.SensorDataWebGuiNavigatorGroup group = (SensorDataWebGui.diagram.navigator.SensorDataWebGuiNavigatorGroup) element;
            return SensorDataWebGui.diagram.part.SensorDataWebGuiDiagramEditorPlugin.getInstance().getBundledImage(group.getIcon());
        }
        if (element instanceof SensorDataWebGui.diagram.navigator.SensorDataWebGuiNavigatorItem) {
            SensorDataWebGui.diagram.navigator.SensorDataWebGuiNavigatorItem navigatorItem = (SensorDataWebGui.diagram.navigator.SensorDataWebGuiNavigatorItem) element;
            if (!isOwnView(navigatorItem.getView())) {
                return super.getImage(element);
            }
            return getImage(navigatorItem.getView());
        }
        return super.getImage(element);
    }

    /**
	 * @generated
	 */
    public Image getImage(View view) {
        switch(SensorDataWebGui.diagram.part.SensorDataWebGuiVisualIDRegistry.getVisualID(view)) {
            case SensorDataWebGui.diagram.edit.parts.StandardSensorDataWebEditPart.VISUAL_ID:
                return getImage("Navigator?Diagram?nl.utwente.ewi.gui?StandardSensorDataWeb", SensorDataWebGui.diagram.providers.SensorDataWebGuiElementTypes.StandardSensorDataWeb_1000);
            case SensorDataWebGui.diagram.edit.parts.SourceEditPart.VISUAL_ID:
                return getImage("Navigator?TopLevelNode?nl.utwente.ewi.gui?Source", SensorDataWebGui.diagram.providers.SensorDataWebGuiElementTypes.Source_2001);
            case SensorDataWebGui.diagram.edit.parts.ProcessingElementEditPart.VISUAL_ID:
                return getImage("Navigator?TopLevelNode?nl.utwente.ewi.gui?ProcessingElement", SensorDataWebGui.diagram.providers.SensorDataWebGuiElementTypes.ProcessingElement_2002);
            case SensorDataWebGui.diagram.edit.parts.FixedWindowEditPart.VISUAL_ID:
                return getImage("Navigator?Link?nl.utwente.ewi.gui?FixedWindow", SensorDataWebGui.diagram.providers.SensorDataWebGuiElementTypes.FixedWindow_4001);
            case SensorDataWebGui.diagram.edit.parts.TupleWindowEditPart.VISUAL_ID:
                return getImage("Navigator?Link?nl.utwente.ewi.gui?TupleWindow", SensorDataWebGui.diagram.providers.SensorDataWebGuiElementTypes.TupleWindow_4002);
            case SensorDataWebGui.diagram.edit.parts.TimeWindowEditPart.VISUAL_ID:
                return getImage("Navigator?Link?nl.utwente.ewi.gui?TimeWindow", SensorDataWebGui.diagram.providers.SensorDataWebGuiElementTypes.TimeWindow_4003);
        }
        return getImage("Navigator?UnknownElement", null);
    }

    /**
	 * @generated
	 */
    private Image getImage(String key, IElementType elementType) {
        ImageRegistry imageRegistry = SensorDataWebGui.diagram.part.SensorDataWebGuiDiagramEditorPlugin.getInstance().getImageRegistry();
        Image image = imageRegistry.get(key);
        if (image == null && elementType != null && SensorDataWebGui.diagram.providers.SensorDataWebGuiElementTypes.isKnownElementType(elementType)) {
            image = SensorDataWebGui.diagram.providers.SensorDataWebGuiElementTypes.getImage(elementType);
            imageRegistry.put(key, image);
        }
        if (image == null) {
            image = imageRegistry.get("Navigator?ImageNotFound");
            imageRegistry.put(key, image);
        }
        return image;
    }

    /**
	 * @generated
	 */
    public String getText(Object element) {
        if (element instanceof SensorDataWebGui.diagram.navigator.SensorDataWebGuiNavigatorGroup) {
            SensorDataWebGui.diagram.navigator.SensorDataWebGuiNavigatorGroup group = (SensorDataWebGui.diagram.navigator.SensorDataWebGuiNavigatorGroup) element;
            return group.getGroupName();
        }
        if (element instanceof SensorDataWebGui.diagram.navigator.SensorDataWebGuiNavigatorItem) {
            SensorDataWebGui.diagram.navigator.SensorDataWebGuiNavigatorItem navigatorItem = (SensorDataWebGui.diagram.navigator.SensorDataWebGuiNavigatorItem) element;
            if (!isOwnView(navigatorItem.getView())) {
                return null;
            }
            return getText(navigatorItem.getView());
        }
        return super.getText(element);
    }

    /**
	 * @generated
	 */
    public String getText(View view) {
        if (view.getElement() != null && view.getElement().eIsProxy()) {
            return getUnresolvedDomainElementProxyText(view);
        }
        switch(SensorDataWebGui.diagram.part.SensorDataWebGuiVisualIDRegistry.getVisualID(view)) {
            case SensorDataWebGui.diagram.edit.parts.StandardSensorDataWebEditPart.VISUAL_ID:
                return getStandardSensorDataWeb_1000Text(view);
            case SensorDataWebGui.diagram.edit.parts.SourceEditPart.VISUAL_ID:
                return getSource_2001Text(view);
            case SensorDataWebGui.diagram.edit.parts.ProcessingElementEditPart.VISUAL_ID:
                return getProcessingElement_2002Text(view);
            case SensorDataWebGui.diagram.edit.parts.FixedWindowEditPart.VISUAL_ID:
                return getFixedWindow_4001Text(view);
            case SensorDataWebGui.diagram.edit.parts.TupleWindowEditPart.VISUAL_ID:
                return getTupleWindow_4002Text(view);
            case SensorDataWebGui.diagram.edit.parts.TimeWindowEditPart.VISUAL_ID:
                return getTimeWindow_4003Text(view);
        }
        return getUnknownElementText(view);
    }

    /**
	 * @generated
	 */
    private String getStandardSensorDataWeb_1000Text(View view) {
        SensorDataWebGui.StandardSensorDataWeb domainModelElement = (SensorDataWebGui.StandardSensorDataWeb) view.getElement();
        if (domainModelElement != null) {
            return domainModelElement.getName();
        } else {
            SensorDataWebGui.diagram.part.SensorDataWebGuiDiagramEditorPlugin.getInstance().logError("No domain element for view with visualID = " + 1000);
            return "";
        }
    }

    /**
	 * @generated
	 */
    private String getSource_2001Text(View view) {
        IParser parser = SensorDataWebGui.diagram.providers.SensorDataWebGuiParserProvider.getParser(SensorDataWebGui.diagram.providers.SensorDataWebGuiElementTypes.Source_2001, view.getElement() != null ? view.getElement() : view, SensorDataWebGui.diagram.part.SensorDataWebGuiVisualIDRegistry.getType(SensorDataWebGui.diagram.edit.parts.SourceNameEditPart.VISUAL_ID));
        if (parser != null) {
            return parser.getPrintString(new EObjectAdapter(view.getElement() != null ? view.getElement() : view), ParserOptions.NONE.intValue());
        } else {
            SensorDataWebGui.diagram.part.SensorDataWebGuiDiagramEditorPlugin.getInstance().logError("Parser was not found for label " + 5001);
            return "";
        }
    }

    /**
	 * @generated
	 */
    private String getProcessingElement_2002Text(View view) {
        IParser parser = SensorDataWebGui.diagram.providers.SensorDataWebGuiParserProvider.getParser(SensorDataWebGui.diagram.providers.SensorDataWebGuiElementTypes.ProcessingElement_2002, view.getElement() != null ? view.getElement() : view, SensorDataWebGui.diagram.part.SensorDataWebGuiVisualIDRegistry.getType(SensorDataWebGui.diagram.edit.parts.ProcessingElementNameEditPart.VISUAL_ID));
        if (parser != null) {
            return parser.getPrintString(new EObjectAdapter(view.getElement() != null ? view.getElement() : view), ParserOptions.NONE.intValue());
        } else {
            SensorDataWebGui.diagram.part.SensorDataWebGuiDiagramEditorPlugin.getInstance().logError("Parser was not found for label " + 5013);
            return "";
        }
    }

    /**
	 * @generated
	 */
    private String getFixedWindow_4001Text(View view) {
        IParser parser = SensorDataWebGui.diagram.providers.SensorDataWebGuiParserProvider.getParser(SensorDataWebGui.diagram.providers.SensorDataWebGuiElementTypes.FixedWindow_4001, view.getElement() != null ? view.getElement() : view, SensorDataWebGui.diagram.part.SensorDataWebGuiVisualIDRegistry.getType(SensorDataWebGui.diagram.edit.parts.FixedWindowDescriptionEditPart.VISUAL_ID));
        if (parser != null) {
            return parser.getPrintString(new EObjectAdapter(view.getElement() != null ? view.getElement() : view), ParserOptions.NONE.intValue());
        } else {
            SensorDataWebGui.diagram.part.SensorDataWebGuiDiagramEditorPlugin.getInstance().logError("Parser was not found for label " + 6001);
            return "";
        }
    }

    /**
	 * @generated
	 */
    private String getTupleWindow_4002Text(View view) {
        IParser parser = SensorDataWebGui.diagram.providers.SensorDataWebGuiParserProvider.getParser(SensorDataWebGui.diagram.providers.SensorDataWebGuiElementTypes.TupleWindow_4002, view.getElement() != null ? view.getElement() : view, SensorDataWebGui.diagram.part.SensorDataWebGuiVisualIDRegistry.getType(SensorDataWebGui.diagram.edit.parts.TupleWindowNameEditPart.VISUAL_ID));
        if (parser != null) {
            return parser.getPrintString(new EObjectAdapter(view.getElement() != null ? view.getElement() : view), ParserOptions.NONE.intValue());
        } else {
            SensorDataWebGui.diagram.part.SensorDataWebGuiDiagramEditorPlugin.getInstance().logError("Parser was not found for label " + 6003);
            return "";
        }
    }

    /**
	 * @generated
	 */
    private String getTimeWindow_4003Text(View view) {
        IParser parser = SensorDataWebGui.diagram.providers.SensorDataWebGuiParserProvider.getParser(SensorDataWebGui.diagram.providers.SensorDataWebGuiElementTypes.TimeWindow_4003, view.getElement() != null ? view.getElement() : view, SensorDataWebGui.diagram.part.SensorDataWebGuiVisualIDRegistry.getType(SensorDataWebGui.diagram.edit.parts.TimeWindowNameEditPart.VISUAL_ID));
        if (parser != null) {
            return parser.getPrintString(new EObjectAdapter(view.getElement() != null ? view.getElement() : view), ParserOptions.NONE.intValue());
        } else {
            SensorDataWebGui.diagram.part.SensorDataWebGuiDiagramEditorPlugin.getInstance().logError("Parser was not found for label " + 6005);
            return "";
        }
    }

    /**
	 * @generated
	 */
    private String getUnknownElementText(View view) {
        return "<UnknownElement Visual_ID = " + view.getType() + ">";
    }

    /**
	 * @generated
	 */
    private String getUnresolvedDomainElementProxyText(View view) {
        return "<Unresolved domain element Visual_ID = " + view.getType() + ">";
    }

    /**
	 * @generated
	 */
    public void init(ICommonContentExtensionSite aConfig) {
    }

    /**
	 * @generated
	 */
    public void restoreState(IMemento aMemento) {
    }

    /**
	 * @generated
	 */
    public void saveState(IMemento aMemento) {
    }

    /**
	 * @generated
	 */
    public String getDescription(Object anElement) {
        return null;
    }

    /**
	 * @generated
	 */
    private boolean isOwnView(View view) {
        return SensorDataWebGui.diagram.edit.parts.StandardSensorDataWebEditPart.MODEL_ID.equals(SensorDataWebGui.diagram.part.SensorDataWebGuiVisualIDRegistry.getModelID(view));
    }
}
