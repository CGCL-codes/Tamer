package fr.itris.glips.rtdaeditor.anim.widgets;

import fr.itris.glips.rtdaeditor.anim.*;
import fr.itris.glips.rtdaeditor.anim.widgets.tageventchooser.*;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.lang.ref.*;

/**
 * the class of the widgets
 * @author ITRIS, Jordi SUC
 */
public abstract class Widget extends JPanel {

    /**
	 * the colors for the selection
	 */
    protected static Color selectedBackgroundColor, selectedForegroundColor;

    /**
	 * the background
	 */
    protected static Color background = Color.white;

    /**
	 * the map of the widgets used to render the table cells
	 * it associates the type of a widget to this widget
	 */
    protected static Map<String, Widget> renderers = new HashMap<String, Widget>();

    /**
	 * the map of the widgets used to edit the table cells
	 * it associates the type of a widget to this widget
	 */
    protected static Map<String, Widget> editors = new HashMap<String, Widget>();

    /**
	 * the complex tag chooser
	 */
    protected static Widget complexTagChooserWidget = new ComplexTagChooser(false), complexTagChooserWidgetEditor = new ComplexTagChooser(true);

    /**
	 * the simple tag chooser
	 */
    protected static Widget simpleTagChooserWidget = new SimpleTagChooser(false), simpleTagChooserWidgetEditor = new SimpleTagChooser(true);

    static {
        renderers.put(EditableItem.ACTION_CHOOSER, new ActionChooser(false));
        renderers.put(EditableItem.VIEW_CHOOSER, new ViewChooser(false, false));
        renderers.put(EditableItem.EXTENDED_VIEW_CHOOSER, new ViewChooser(false, true));
        renderers.put(EditableItem.COMBO, new Combo(false));
        renderers.put(EditableItem.ENTRY, new Entry(false));
        renderers.put(EditableItem.LABEL, new Label(false));
        renderers.put(EditableItem.LIMIT, new Limit(false, false));
        renderers.put(EditableItem.CHILD_LIMIT, new Limit(false, true));
        renderers.put(EditableItem.COLOR_CHOOSER, new ColorChooser(false));
        renderers.put(EditableItem.COMPLEX_TARGET_CHOOSER, new TargetChooser(false, true));
        renderers.put(EditableItem.TARGET_CHOOSER, new TargetChooser(false, false));
        renderers.put(EditableItem.EVENT_CHOOSER, new EventChooser(false));
        renderers.put(EditableItem.TAG_EVENT_CHOOSER, new TagEventChooser(false));
        renderers.put(EditableItem.CHECK_BOX, new CheckBox(false));
        renderers.put(EditableItem.BLINKING_CHOOSER, new BlinkingChooser(false));
        renderers.put(EditableItem.RET_INIT_VAL_CHOOSER, new ReturnToInitialValueChooser(false, false));
        renderers.put(EditableItem.SIMPLE_RET_INIT_VAL_CHOOSER, new ReturnToInitialValueChooser(false, true));
        renderers.put(EditableItem.TAG_VALUES_CHOOSER, new TagValuesChooser(false));
        renderers.put(EditableItem.TAG_VALUES_MULTI_CHOOSER, new TagValuesMultiChooser(false));
        renderers.put(EditableItem.NUMBER_CHOOSER, new NumberChooser(false));
        renderers.put(EditableItem.INTEGER_CHOOSER, new IntegerChooser(false));
        renderers.put(EditableItem.DASH_CHOOSER, new DashChooser(false));
        renderers.put(EditableItem.POINT_STYLE_CHOOSER, new PointStyleChooser(false));
        renderers.put(EditableItem.INTERPOLATION_CHOOSER, new InterpolationChooser(false));
        renderers.put(EditableItem.EQUAL_CHOOSER, new EqualChooser(false));
        renderers.put(EditableItem.SYMBOL_CHOOSER, new SymbolChooser(false));
        renderers.put(EditableItem.DIRECTORY_CHOOSER, new DirectoryChooser(false));
        renderers.put(EditableItem.POINT_CHOOSER, new PointChooser(false));
        renderers.put(EditableItem.DB_TABLE_CHOOSER, new TableDBChooser(false));
        renderers.put(EditableItem.REQUEST_CHOOSER, new RequestChooser(false));
        editors.put(EditableItem.ACTION_CHOOSER, new ActionChooser(true));
        editors.put(EditableItem.VIEW_CHOOSER, new ViewChooser(true, false));
        editors.put(EditableItem.EXTENDED_VIEW_CHOOSER, new ViewChooser(true, true));
        editors.put(EditableItem.COMBO, new Combo(true));
        editors.put(EditableItem.ENTRY, new Entry(true));
        editors.put(EditableItem.LABEL, new Label(true));
        editors.put(EditableItem.LIMIT, new Limit(true, false));
        editors.put(EditableItem.CHILD_LIMIT, new Limit(true, true));
        editors.put(EditableItem.COLOR_CHOOSER, new ColorChooser(true));
        editors.put(EditableItem.COMPLEX_TARGET_CHOOSER, new TargetChooser(true, true));
        editors.put(EditableItem.TARGET_CHOOSER, new TargetChooser(true, false));
        editors.put(EditableItem.EVENT_CHOOSER, new EventChooser(true));
        editors.put(EditableItem.TAG_EVENT_CHOOSER, new TagEventChooser(true));
        editors.put(EditableItem.CHECK_BOX, new CheckBox(true));
        editors.put(EditableItem.BLINKING_CHOOSER, new BlinkingChooser(true));
        editors.put(EditableItem.RET_INIT_VAL_CHOOSER, new ReturnToInitialValueChooser(true, false));
        editors.put(EditableItem.SIMPLE_RET_INIT_VAL_CHOOSER, new ReturnToInitialValueChooser(true, true));
        editors.put(EditableItem.TAG_VALUES_CHOOSER, new TagValuesChooser(true));
        editors.put(EditableItem.TAG_VALUES_MULTI_CHOOSER, new TagValuesMultiChooser(true));
        editors.put(EditableItem.NUMBER_CHOOSER, new NumberChooser(true));
        editors.put(EditableItem.INTEGER_CHOOSER, new IntegerChooser(true));
        editors.put(EditableItem.DASH_CHOOSER, new DashChooser(true));
        editors.put(EditableItem.POINT_STYLE_CHOOSER, new PointStyleChooser(true));
        editors.put(EditableItem.INTERPOLATION_CHOOSER, new InterpolationChooser(true));
        editors.put(EditableItem.EQUAL_CHOOSER, new EqualChooser(true));
        editors.put(EditableItem.SYMBOL_CHOOSER, new SymbolChooser(true));
        editors.put(EditableItem.DIRECTORY_CHOOSER, new DirectoryChooser(true));
        editors.put(EditableItem.POINT_CHOOSER, new PointChooser(true));
        editors.put(EditableItem.DB_TABLE_CHOOSER, new TableDBChooser(true));
        editors.put(EditableItem.REQUEST_CHOOSER, new RequestChooser(true));
    }

    /**
	 * the reference of the editable item
	 */
    protected WeakReference<EditableItem> currentItemReference;

    /**
	 * the runnable used to validate when editing
	 */
    protected Runnable validateRunnable;

    /**
	 * whether the widget should be used for editing or not
	 */
    protected boolean isEditor = false;

    /**
	 * the constructor of the class
	 * @param isEditor whether the widget should be used for editing or not
	 */
    protected Widget(boolean isEditor) {
        this.isEditor = isEditor;
        setOpaque(true);
    }

    /**
	 * creates a widget given an editable item
	 * @param item an editable item
	 * @param validateRunnable the runnable used to validate when editing
	 * @param isEditor whether the returned widget should be an editor
	 * @param isSelected whether the returned widget should be selected
	 * @return the widget that should be used with the provided item
	 */
    public static Widget getWidget(EditableItem item, Runnable validateRunnable, boolean isEditor, boolean isSelected) {
        Widget widget = null;
        String type = item.getType();
        if (type.equals(EditableItem.TAG_CHOOSER) || type.equals(EditableItem.LOW_TAG_CHOOSER) || type.equals(EditableItem.LOW_REAL_TAG_CHOOSER)) {
            widget = isEditor ? simpleTagChooserWidgetEditor : simpleTagChooserWidget;
        } else {
            widget = isEditor ? editors.get(type) : renderers.get(type);
        }
        if (widget != null) {
            widget.setBackground(isSelected ? selectedBackgroundColor : background);
            widget.setItem(item, validateRunnable);
        }
        return widget;
    }

    /**
	 * sets the new item for this widget
	 * @param item an item
	 * @param validateRunnable the runnable used to validate when editing
	 */
    protected void setItem(EditableItem item, Runnable validateRunnable) {
        this.currentItemReference = new WeakReference<EditableItem>(item);
        this.validateRunnable = validateRunnable;
    }

    /**
	 * @return the current item
	 */
    public EditableItem getItem() {
        if (currentItemReference != null) {
            return currentItemReference.get();
        }
        return null;
    }

    /**
	 * sets the selection background
	 * @param selectionBackground a color
	 */
    public static void setSelectionBackground(Color selectionBackground) {
        selectedBackgroundColor = selectionBackground;
    }

    /**
	 * sets the selection foreground
	 * @param selectionForeground a color
	 */
    public static void setSelectionForeground(Color selectionForeground) {
        selectedForegroundColor = selectionForeground;
    }
}
