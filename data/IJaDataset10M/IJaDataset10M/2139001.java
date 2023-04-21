package org.freelords.formbuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.freelords.forms.FormWidgetFactory;
import org.freelords.main.UIController;
import org.freelords.ui.skin.BorderCache;
import org.freelords.ui.skin.ImageBorder;
import org.freelords.util.ImagePainter;
import org.freelords.util.io.loaders.VirtualPath;
import org.freelords.xml.NodeListIterator;
import org.freelords.xml.XMLDemarshaller;
import org.freelords.xml.XMLHelper;
import org.w3c.dom.Attr;
import org.w3c.dom.Node;

/** Parses an XML file and turns it into some user interface with proper layout.<br>
  * 
  * <p>
  * The class itself is almost empty apart from some minor functions. However,
  * it contains a number of sub classes that do all the work. Most of them care
  * about loading and setting up primitives (labels, widgets, images, ...) or
  * layout stuff (rows, columns, boundaries around other objects, ...).
  * Furthermore, there is an internal demarshaller that basically just digs into
  * the xml file and recursively creates a tree of these layout objects.
  * </p>
  *
  * To use the layout loader, you need to do the following:
  * <ol>
  *     <li>Create an {@link org.freelords.xml.XMLHelper} object and tell it to
  *         use the internal demarshaller for instances of
  *         <code>FormBuilderSource</code></li>
  *     <li>Load an appropriate layout xml file. You end up with a
  *         <code>FormBuilderSource</code> object that internally stores this
  *         tree structure (see the class documentation for an example).</li>
  *     <li>Call {@link FormBuilderSource.create} with an appropriate 
  *         {@link org.freelords.forms.FormWidgetFactory} instance. This is
  *         needed for defining additional hooks that are called when the
  *         widgets are created. This function will now ask each of the layout
  *         objects in turn to create a corresponding graphics object with the
  *         proper layout.</li>
  * </ol>
  *
  * <p>
  * For the flow of control it might be interesting to note that the associated
  * form instance never cares about explicitely getting a pointer to the root of
  * the resulting widget tree. Internally, it is just attached to some parent
  * window.
  * </p>
  */
public class FormBuilderSource {

    /** Points to the root of the tree of layout classes. */
    private FormStuff parent;

    /** Base class of all form primitives and layout objects.
	  * 
	  * <p>
	  * It leaves all the important setup stuff for derived classes. The only
	  * function and members actually implemented are for creating the layout.
	  * The attributes of this class can also be set for all derived objects.
	  * </p>
	  *
	  * <p>
	  * Underlying SWT object: none
	  * </p>
	  *
	  * <p>
	  * XML tag name: none
	  * </p>
	  *
	  * <p>
	  * Attributes that can be set in xml:
	  * <ul>
	  *     <li>stretch<br>
	  *         Can be set to "horizontal", "vertical" or "both". In the
	  *         appropriate direction, grabs excess space (see
	  *         org.eclipse.swt.GridLayout), i.e. the object tries to completely fill a
	  *         grid cell, even if the cell is larger than the object.</li>
	  *     <li>width<br>
	  *         If set, this determines the preferred as well as the minimum
	  *         width taken into account when all objects are finally arranged
	  *         on the form.</li>
	  *     <li>height<br>
	  *         Same as "width", but for the height of the object.</li>
	  *     <li>color<br>
	  *         Valid values can be found in the definition of the class
	  *         org.eclipse.swt.SWT (e.g., BLACK, WHITE, ...). The foreground
	  *         color of the widget.</li>
	  *     <li>background-color<br>
	  *         Same as color, but determines the background color.</li>
	  * </ul>
	  * </p>
	  */
    protected abstract static class FormStuff {

        protected boolean stretchHorizontal;

        protected boolean stretchVertical;

        protected int swtBackgroundColor;

        protected int width = -1;

        protected int height = -1;

        protected int foregroundColor = SWT.NONE;

        protected Color rgbForegroundColor = null;

        /** Adds a child to the form object.
		  * 
		  * @param child the child to add
		  */
        protected abstract void add(FormStuff child);

        /** Creates the widget that corresponds to this form element.
		  * 
		  * Calling this function will make the form primitive create a widget
		  * that represents it, create possible subwidgets along the way, and
		  * attach them to the given parent widget.
		  *
		  * @param parent the parent object of the widget that is created by
		  * this class.
		  * @param form a {@link org.freelords.forms.FormWidgetFactory} instance
		  * that is used for explicitely creating widgets.
		  *
		  * @return an SWT <code>Control</code> instance that represents this
		  * object on the form.
		  */
        protected abstract Control create(Composite parent, FormWidgetFactory form);

        /** Creates a layout for this graphics item.
		  * 
		  * This function creates a <code>GridData</code> object that determines
		  * the layout of this graphics item. In the end, the layout manager
		  * will try to arrange all objects on the form such that most of the
		  * constraints (preferred width etc.) can be fulfilled.
		  *
		  * @return a corresponding layout object.
		  */
        protected GridData createLayoutData() {
            GridData gd = new GridData();
            gd.verticalAlignment = SWT.FILL;
            gd.horizontalAlignment = SWT.FILL;
            if (stretchVertical) {
                gd.grabExcessVerticalSpace = true;
            }
            if (stretchHorizontal) {
                gd.grabExcessHorizontalSpace = true;
            }
            if (width != -1) {
                gd.horizontalAlignment = SWT.CENTER;
                gd.minimumWidth = width;
                gd.widthHint = width;
            }
            if (height != -1) {
                gd.verticalAlignment = SWT.CENTER;
                gd.minimumHeight = height;
                gd.heightHint = height;
            }
            return gd;
        }
    }

    /** Aligns subwidgets in a row or column.
	  * 
	  * <p>
	  * This layout element can be assigned other form primitives or layout
	  * items (in the form of xml subtags), which are then arranged in a row
	  * from left to right or in a column from top to bottom. All attributes of
	  * the FormStuff class apply here as well.
	  * </p>
	  *
	  * <p>
	  * Underlying SWT class: Composite
	  * </p>
	  *
	  * <p>
	  * XML tag name: row or column<br>
	  * If the xml tag is named row, all subelements are aligned in a row,
	  * otherwise in a column.
	  * </p>
	  *
	  * <p>
	  * Attributes that can be set in xml: None
	  * </p>
	  */
    protected static class Strip extends FormStuff {

        private int orientation;

        private List<FormStuff> children = new ArrayList<FormStuff>();

        /** {@inheritDoc} */
        protected void add(FormStuff child) {
            children.add(child);
        }

        /** {@inheritDoc} */
        @Override
        protected Control create(Composite parent, FormWidgetFactory form) {
            Composite strip = new Composite(parent, SWT.NONE);
            int columnCount = (orientation == SWT.HORIZONTAL ? children.size() : 1);
            GridLayout gl = new GridLayout(columnCount, false);
            if (orientation == SWT.HORIZONTAL) {
                gl.marginHeight = 2;
                gl.marginTop = 0;
                gl.marginBottom = 0;
                gl.verticalSpacing = 0;
            } else {
                gl.marginWidth = 2;
                gl.marginLeft = 0;
                gl.marginRight = 0;
                gl.horizontalSpacing = 0;
            }
            strip.setLayout(gl);
            for (FormStuff child : children) {
                Control cont = child.create(strip, form);
                cont.setLayoutData(child.createLayoutData());
            }
            return strip;
        }
    }

    /** Creates a generic widget.
	  * 
	  * <p>
	  * Note that the actual widget creation is delegated to {@link org.freelords.forms.FormWidgetFactory.createWidget}
	  * which in turn delegates the creation to classes representing the actual
	  * form. So while this is only a generic widget type, it can in practice
	  * represent a button as well.
	  * </p>
	  *
	  * <p>
	  * Underlying SWT class: Control (depending on callback also button,
	  * dropdown etc.)
	  * </p>
	  *
	  * <p>
	  * XML tag name: widget
	  * </p>
	  *
	  * <p>
	  * Attributes that can be set in xml:
	  * <ul>
	  *     <li>id<br>
	  *         The id of the widget. This uniquely defines a widget within a
	  *         form and is used by {@link org.freelords.forms.FormWidgetFactory}
	  *         for widget creation and callbacks.</li>
	  * </ul>
	  * </p>
	  */
    public static class Widget extends FormStuff {

        protected String id;

        /** Returns the id of the widget. */
        public String getId() {
            return id;
        }

        /** Throws an exception.
		  * 
		  * <p>
		  * Since widgets are form primitives, they may not have children.
		  * </p>
		  * {@inheritDoc}
		  */
        protected void add(FormStuff child) {
            throw new UnsupportedOperationException("Widgets can not have children");
        }

        /** {@inheritDoc} */
        @Override
        protected Control create(Composite parent, FormWidgetFactory form) {
            Control realWidget = form.createWidget(parent, this);
            if (realWidget == null) {
                realWidget = new Composite(parent, SWT.NONE);
                this.stretchHorizontal = false;
                this.stretchVertical = false;
                this.width = 0;
                this.height = 0;
            }
            return realWidget;
        }
    }

    /** Primitive for displaying an image.
	  * 
	  * <p>
	  * Puts an image somewhere on the form.
	  * </p>
	  *
	  * <p>
	  * Underlying SWT class: Canvas
	  * </p>
	  *
	  * <p>
	  * XML tag name: img
	  * </p>
	  *
	  * <p>
	  * Attributes that can be set in xml:
	  * <ul>
	  *     <li>background-image<br>
	  *         The file name of the image file to be displayed. Note that you
	  *         have to supply the full path below the FreelordsData/data/client
	  *         directory.</li>
	  *     <li>repeat-x<br>
	  *         How often the image should be repeated in the horizontal
	  *         direction.</li>
	  *     <li>repeat-y<br>
	  *         How often the image should be repeated in the horizontal
	  *         direction.</li>
	  *     <li>repeat<br>
	  *         Same as setting repeat-x and repeat-y at the same time.</li>
	  *     <li>background-position<br>
	  *         String that indicates (in this order) the horizontal
	  *         alignment, a space, and the vertical alignment. Valid values are
	  *         "left", "center", "right", and "top", "middle", "bottom".</li>
	  * </ul>
	  * </p>
	  */
    protected static class Image extends FormStuff {

        private String backgroundImage;

        boolean bHorizontalRepeat;

        boolean bVerticalRepeat;

        private int bHorizontalAlign = SWT.CENTER;

        private int bVerticalAlgin = SWT.CENTER;

        /** Throws an exception.
		  * 
		  * <p>
		  * Being a form primitive, there is no point in an image having
		  * children.
		  * </p>
		  */
        protected void add(FormStuff child) {
            throw new UnsupportedOperationException("Widgets can not have children");
        }

        /** {@inheritDoc} */
        @Override
        protected Control create(Composite parent, FormWidgetFactory form) {
            Canvas cell = new Canvas(parent, SWT.NONE);
            try {
                final org.eclipse.swt.graphics.Image splashImage = new org.eclipse.swt.graphics.Image(parent.getDisplay(), UIController.getResources().getInputStream(backgroundImage));
                cell.addPaintListener(new ImagePainter(splashImage, null, bHorizontalRepeat, bVerticalRepeat, bHorizontalAlign, bVerticalAlgin));
                cell.addDisposeListener(new DisposeListener() {

                    public void widgetDisposed(DisposeEvent e) {
                        splashImage.dispose();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
            return cell;
        }
    }

    /** Group several subitems together and provide a border with text.
	  * 
	  * <p>
	  * As an example, have a look at the chat part of the network game setup
	  * screen, or look at the <code>Group</code> class of the SWT library.
	  * Note that this layout object may only have a single child. If you want
	  * to group multiple items together, you have to define a row or column.
	  * </p>
	  *
	  * <p>
	  * Underlying SWT object: Group.
	  * </p>
	  *
	  * <p>
	  * XML tag name: group
	  * </p>
	  *
	  * <p>
	  * Attributes that can be set in xml:
	  * <ul>
	  *     <li>key<br>
	  *         Sets the displayed name that appears along the border.</li>
	  * </ul>
	  * </p>
	  */
    protected static class FormGroup extends FormStuff {

        private FormStuff child;

        private String key;

        private String borderStyle;

        /** {@inheritDoc} */
        protected void add(FormStuff child) {
            if (this.child != null) {
                throw new IllegalStateException("A cell can only have 0 or 1 children.");
            }
            this.child = child;
        }

        /** {@inheritDoc} */
        @Override
        protected Control create(Composite parent, FormWidgetFactory form) {
            Control returnControl = null;
            if (borderStyle != null) {
                BorderCache bc = UIController.getBorderCache(borderStyle);
                ImageBorder ib = new ImageBorder(parent, bc);
                parent = ib.getMiddle();
                returnControl = ib.getControl();
            }
            Group group = new Group(parent, SWT.NONE);
            if (returnControl == null) {
                returnControl = group;
            }
            group.setText(UIController.getInterfaceBundle().getString(key));
            if (child != null) {
                group.setLayout(new FillLayout());
                child.create(group, form);
            }
            if (this.swtBackgroundColor != SWT.NONE) {
                group.setBackground(parent.getDisplay().getSystemColor(this.swtBackgroundColor));
                group.setBackgroundMode(SWT.INHERIT_DEFAULT);
            }
            if (this.foregroundColor != SWT.NONE) {
                group.setForeground(parent.getDisplay().getSystemColor(this.foregroundColor));
            }
            if (this.rgbForegroundColor != null) {
                group.setForeground(this.rgbForegroundColor);
            }
            return returnControl;
        }
    }

    /** (Horizontally) centers the subwidgets.
	  *
	  * <p>
	  * This is done by creating a row consisting of the subelements ordered
	  * from top to bottom, as well as two fill elements to the left and right
	  * which are ordered to grab all excess space. The SWT layout manager then,
	  * trying to fulfill the constraints, automatically centers the form
	  * elements.
	  * </p>
	  *
	  * <p>
	  * Underlying SWT class: None
	  * </p>
	  *
	  * <p>
	  * XML tag name: center
	  * </p>
	  *
	  * <p>
	  * Attributes that can be set in xml: None
	  * </p>
	  */
    protected static class Center extends Strip {

        private List<FormStuff> children = new ArrayList<FormStuff>();

        /** {@inheritDoc} */
        protected void add(FormStuff child) {
            children.add(child);
        }

        /** {@inheritDoc} */
        @Override
        protected Control create(Composite parent, FormWidgetFactory form) {
            Strip hStrip = new Strip();
            hStrip.orientation = SWT.HORIZONTAL;
            Cell cell = new Cell();
            cell.stretchHorizontal = true;
            hStrip.add(cell);
            Strip vStrip = new Strip();
            vStrip.orientation = SWT.VERTICAL;
            for (FormStuff child : children) {
                vStrip.add(child);
            }
            hStrip.add(vStrip);
            cell = new Cell();
            cell.stretchHorizontal = true;
            hStrip.add(cell);
            return hStrip.create(parent, form);
        }
    }

    /** Draws a border line around the subwidget.
	  * 
	  * <p>
	  * Underlying SWT class: ImageBorder
	  * </p>
	  *
	  * <p>
	  * XML tag name: cell
	  * </p>
	  *
	  * <p>
	  * Attributes that can be set in xml:
	  * <ul>
	  *     <li>border-style<br>
	  *         The style of the border of this form object. Valid values are
	  *         "thin", "standard" up to now. Other values must have
	  *         corresponding directories in
	  *         FreelordsData/data/client/interface/borders. </li>
	  * </ul>
	  * </p>
	  */
    protected static class Cell extends FormStuff {

        private FormStuff child;

        private String borderStyle;

        /** {@inheritDoc} */
        protected void add(FormStuff child) {
            if (this.child != null) {
                throw new IllegalStateException("A cell can only have 0 or 1 children.");
            }
            this.child = child;
        }

        /** {@inheritDoc} */
        @Override
        protected Control create(Composite parent, FormWidgetFactory form) {
            Control returnControl = null;
            if (borderStyle != null) {
                BorderCache bc = UIController.getBorderCache(borderStyle);
                ImageBorder ib = new ImageBorder(parent, bc);
                parent = ib.getMiddle();
                returnControl = ib.getControl();
            }
            Composite cell = new Composite(parent, SWT.NONE);
            if (returnControl == null) {
                returnControl = cell;
            }
            if (child != null) {
                cell.setLayout(new FillLayout());
                child.create(cell, form);
            }
            if (this.swtBackgroundColor != SWT.NONE) {
                cell.setBackground(parent.getDisplay().getSystemColor(this.swtBackgroundColor));
                cell.setBackgroundMode(SWT.INHERIT_DEFAULT);
            }
            return returnControl;
        }
    }

    /** Form primitive that represents a label on the form.
	  * 
	  * <p>
	  * Note that a label is always centered vertically.
	  * </p>
	  *
	  * <p>
	  * Underlying SWT class: Label
	  * </p>
	  *
	  * <p>
	  * XML tag name: label
	  * </p>
	  *
	  * <p>
	  * Attributes that can be set in xml:
	  * <ul>
	  *     <li>key<br>
	  *         The text of the label (must be set!).</li>
	  * </ul>
	  * </p>
	  */
    protected static class FormLabel extends FormStuff {

        protected String key;

        /** Throws an exception.
		  * 
		  * <p>
		  * Being a form primitive, there is no point in an image having
		  * children.
		  * </p>
		  */
        @Override
        protected void add(FormStuff child) {
            throw new IllegalArgumentException("Label's can't have children.");
        }

        /** {@inheritDoc} */
        @Override
        protected Control create(Composite parent, FormWidgetFactory from) {
            Label label = new Label(parent, SWT.NONE);
            label.setText(UIController.getInterfaceBundle().getString(key));
            if (foregroundColor != SWT.NONE) {
                label.setForeground(Display.getDefault().getSystemColor(foregroundColor));
            }
            return label;
        }

        /** {@inheritDoc} */
        @Override
        protected GridData createLayoutData() {
            GridData superGd = super.createLayoutData();
            superGd.verticalAlignment = SWT.CENTER;
            return superGd;
        }
    }

    /** Displays each subwidget together with an id label.
	  * 
	  * <p>
	  * This layout object can have an arbitrary number of subwidgets. They are
	  * put in column filled from top to bottom. For each of the subwidgets, an
	  * additional label with the id of the widget is put to the left, while the
	  * widget fills the whole horizontal space available for this element.
	  * </p>
	  *
	  * <p>
	  * Underlying SWT class: Composite (with a number of widgets and labels as
	  * children)
	  * </p>
	  *
	  * <p>
	  * XML tag name: labelled-widgets
	  * </p>
	  *
	  * <p>
	  * Attributes that can be set in xml: None
	  * </p>
	  */
    protected static class LabelledElements extends FormStuff {

        private List<FormStuff> children = new ArrayList<FormStuff>();

        /** {@inheritDoc} */
        protected void add(FormStuff child) {
            children.add(child);
        }

        /** {@inheritDoc} */
        @Override
        protected Control create(Composite parent, FormWidgetFactory form) {
            Composite thisC = new Composite(parent, SWT.NONE);
            thisC.setLayout(new GridLayout(2, false));
            for (FormStuff fs : children) {
                Widget widget = (Widget) fs;
                Label label = new Label(thisC, SWT.NONE);
                label.setText(UIController.getInterfaceBundle().getString(widget.id));
                label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
                Control actualWidget = widget.create(thisC, form);
                GridData gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
                if (stretchHorizontal) {
                    gd.grabExcessHorizontalSpace = true;
                }
                actualWidget.setLayoutData(gd);
            }
            return thisC;
        }
    }

    /** Displays some widgets and buttons to scroll around.
	  * 
	  * <p>
	  * This layout object actually contains data for a {@link org.freelords.util.MultiViewer}
	  * instance. It is composed of two buttons for scrolling, and a number of other Controls
	  * whose positions are used for displaying data. One use would be to have many items, and
	  * on scrolling these items get displayed only few at a time.
	  * </p>
	  *
	  * <p>
	  * XML tag name: multi-viewer
	  * </p>
	  *
	  * <p>
	  * Attributes that can be set in XML
	  * <ul>
	  * 	<li>direction<br>
	  * 		Must be set to either "horizontal" or "vertical", and aligns the subwidgets accordingly.</li>
	  * 	<li>item-width<br>
	  * 		The preferred width of the single subwidgets</li>
	  * 	<li>item-height<br>
	  * 		The preferred height of the single subwidgets</li>
	  * 	<li>itemcount<br>
	  * 		The number of subwidgets that should be shown</li>
	  * </ul>
	  */
    public static class MultiElements extends Widget {

        private int childcount;

        private int itemwidth;

        private int itemheight;

        private String direction;

        protected void add(FormStuff child) {
            throw new IllegalArgumentException("MultiElement creates its own children");
        }

        protected Control create(Composite parent, FormWidgetFactory form) {
            if (itemwidth == 0 || itemheight == 0 || childcount == 0) {
                throw new IllegalArgumentException("Cannot create MultiViewer with 0 items or dimensions.");
            }
            return super.create(parent, form);
        }

        public GridData createLayoutData() {
            return super.createLayoutData();
        }

        public int getCount() {
            return childcount;
        }

        public int getItemWidth() {
            return itemwidth;
        }

        public int getItemHeight() {
            return itemheight;
        }

        public String getDirection() {
            return direction;
        }
    }

    /** The demarshaller class that turns some xml code into a {@link FormBuilderSource} instance.
	  */
    public static final XMLDemarshaller<FormBuilderSource> FORM_DEMARSH = new XMLDemarshaller<FormBuilderSource>() {

        /** Demarshalls a file or a part of a file.
		  * 
		  * {@inheritDoc}
		  */
        public FormBuilderSource demarshall(XMLHelper helper, VirtualPath currentFile, Node node, java.lang.reflect.Type exactType) {
            Node formBuilderNode = null;
            for (Node child : new NodeListIterator(node.getChildNodes())) {
                if (formBuilderNode != null) {
                    throw new IllegalArgumentException("Form must have 1 direct child, found 2+");
                }
                formBuilderNode = child;
            }
            if (formBuilderNode == null) {
                throw new IllegalArgumentException("Form must have 1 direct child, found 0");
            }
            FormBuilderSource fbs = new FormBuilderSource();
            fbs.parent = parse(helper, currentFile, formBuilderNode);
            parseExtra(fbs.parent, formBuilderNode);
            parseChildren(helper, currentFile, formBuilderNode, fbs.parent);
            return fbs;
        }

        /** Parses all subtags, creates the appropriate form primitives, and attaches them
		  * to a parent primitive.
		  *
		  * @param helper the helper object responsible for parsing the xml code.
		  * @param currentFile the xml file to be parsed.
		  * @param node the node whose subtags are parsed.
		  * @param parent the parent primitive which the freshly created primitives are attached to.
		  */
        protected void parseChildren(XMLHelper helper, VirtualPath currentFile, Node node, FormStuff parent) {
            for (Node child : new NodeListIterator(node.getChildNodes())) {
                FormStuff fs = parse(helper, currentFile, child);
                parseExtra(fs, child);
                parent.add(fs);
                parseChildren(helper, currentFile, child, fs);
            }
        }

        /** Parses a tag and creates and setups the appropriate form primitive.
		  * 
		  * Basically, it just parses the tag name and defers the actual work
		  * to other functions.
		  *
		  * @param helper the XMLHelper instance responsible for parsing.
		  * @param currentFile the file that is parsed.
		  * @param node the node that is turned into a primitive.
		  * @return the primitive that corresponds to the tag.
		  */
        protected FormStuff parse(XMLHelper helper, VirtualPath currentFile, Node node) {
            if ("column".equals(node.getNodeName()) || "row".equals(node.getNodeName())) {
                return parseStrip(helper, currentFile, node);
            } else if ("widget".equals(node.getNodeName())) {
                return parseWidget(helper, currentFile, node);
            } else if ("cell".equals(node.getNodeName())) {
                return parseCell(helper, currentFile, node);
            } else if ("img".equals(node.getNodeName())) {
                return parseImage(helper, currentFile, node);
            } else if ("group".equals(node.getNodeName())) {
                return parseGroup(helper, currentFile, node);
            } else if ("label".equals(node.getNodeName())) {
                return parseLabel(helper, currentFile, node);
            } else if ("labelled-widgets".equals(node.getNodeName())) {
                return new LabelledElements();
            } else if ("hpadding".equals(node.getNodeName())) {
                Cell cell = new Cell();
                cell.stretchHorizontal = true;
                return cell;
            } else if ("vpadding".equals(node.getNodeName())) {
                Cell cell = new Cell();
                cell.stretchVertical = true;
                return cell;
            } else if ("center".equals(node.getNodeName())) {
                return new Center();
            } else if ("multi-viewer".equals(node.getNodeName())) {
                return parseMulti(helper, currentFile, node);
            } else {
                throw new IllegalArgumentException("Can not handle node " + node.getNodeName());
            }
        }

        protected Strip parseStrip(XMLHelper helper, VirtualPath currentFile, Node node) {
            int orientation;
            if ("column".equals(node.getNodeName())) {
                orientation = SWT.VERTICAL;
            } else if ("row".equals(node.getNodeName())) {
                orientation = SWT.HORIZONTAL;
            } else {
                throw new IllegalArgumentException("Only support row and column at this level");
            }
            Strip strip = new Strip();
            strip.orientation = orientation;
            return strip;
        }

        protected FormGroup parseGroup(XMLHelper helper, VirtualPath currentFile, Node node) {
            FormGroup gf = new FormGroup();
            gf.key = getAttribute(node, "key");
            gf.borderStyle = getAttribute(node, "border-style");
            if (gf.key == null) {
                throw new NullPointerException("Group must have key");
            }
            return gf;
        }

        protected FormLabel parseLabel(XMLHelper helper, VirtualPath currentFile, Node node) {
            FormLabel label = new FormLabel();
            label.key = getAttribute(node, "key");
            if (label.key == null) {
                throw new NullPointerException("Label must have key");
            }
            return label;
        }

        protected Widget parseWidget(XMLHelper helper, VirtualPath currentFile, Node node) {
            Widget widget = new Widget();
            widget.id = getAttribute(node, "id");
            return widget;
        }

        protected MultiElements parseMulti(XMLHelper helper, VirtualPath currentFile, Node node) {
            MultiElements multi = new MultiElements();
            multi.id = getAttribute(node, "id");
            multi.direction = getAttribute(node, "direction");
            multi.itemwidth = Integer.parseInt(getAttribute(node, "item-width"));
            multi.itemheight = Integer.parseInt(getAttribute(node, "item-height"));
            multi.childcount = Integer.parseInt(getAttribute(node, "itemcount"));
            if (!"horizontal".equals(multi.direction) && !"vertical".equals(multi.direction)) {
                throw new IllegalArgumentException("MultiViewer direction must be horizontal or vertical");
            }
            return multi;
        }

        protected Image parseImage(XMLHelper helper, VirtualPath currentFile, Node node) {
            Image image = new Image();
            image.backgroundImage = getAttribute(node, "background-image");
            if (image.backgroundImage != null) {
                String repeat = getAttribute(node, "repeat");
                image.bHorizontalRepeat = "repeat".equals(repeat) || "repeat-x".equals(repeat);
                image.bVerticalRepeat = "repeat".equals(repeat) || "repeat-y".equals(repeat);
                String align = getAttribute(node, "background-position");
                image.bHorizontalAlign = SWT.CENTER;
                image.bVerticalAlgin = SWT.CENTER;
                if (align != null) {
                    String[] alignmentStrings = align.split("\\s+");
                    if (alignmentStrings.length == 2) {
                        image.bVerticalAlgin = convertToSWTAlignment(alignmentStrings[0]);
                        image.bHorizontalAlign = convertToSWTAlignment(alignmentStrings[1]);
                    }
                }
            }
            return image;
        }

        protected Cell parseCell(XMLHelper helper, VirtualPath currentFile, Node node) {
            Cell cell = new Cell();
            cell.borderStyle = getAttribute(node, "border-style");
            return cell;
        }

        /** Parses the stuff concerning the FormStuff class (i.e. common to all classes). */
        protected void parseExtra(FormStuff stuff, Node node) {
            String grow = getAttribute(node, "stretch");
            if (grow != null) {
                if ("horizontal".equals(grow)) {
                    stuff.stretchHorizontal = true;
                } else if ("vertical".equals(grow)) {
                    stuff.stretchVertical = true;
                } else if ("both".equals(grow)) {
                    stuff.stretchHorizontal = true;
                    stuff.stretchVertical = true;
                }
            }
            String width = getAttribute(node, "width");
            if (width != null) {
                stuff.width = Integer.parseInt(width);
            }
            String height = getAttribute(node, "height");
            if (height != null) {
                stuff.height = Integer.parseInt(height);
            }
            String color = getAttribute(node, "background-color");
            if (color != null) {
                stuff.swtBackgroundColor = convertToSWTConstant("COLOR_" + color, SWT.NONE);
            }
            color = getAttribute(node, "color");
            if (color != null) {
                stuff.foregroundColor = convertToSWTConstant("COLOR_" + color, SWT.NONE);
            }
            color = getAttribute(node, "RGB-color");
            if (color != null) {
                String[] rgbValues = color.split(",");
                if (rgbValues.length == 3) {
                    stuff.rgbForegroundColor = new Color(Display.getDefault(), new Integer(rgbValues[0]).intValue(), new Integer(rgbValues[1]).intValue(), new Integer(rgbValues[2]).intValue());
                }
            }
        }
    };

    /** Creates the widgets corresponding to the form layout and attaches them to a parent.
	  * 
	  * @param form factory instance used for actually creating the widgets.
	  * @param parent the parent object that all the widgets are attached to.
	  */
    public void create(FormWidgetFactory form, Composite parent) {
        this.parent.create(parent, form);
    }

    /** Converts a string to an arbitrary SWT constant.
	  * 
	  * The XML code obviously only employs string constants which have to be mapped to
	  * attributes of the SWT base class.
	  *
	  * @param value the name of the SWT constant
	  * @param defaultValue the value that is returned if an error occurs.
	  * @return the SWT constant that corresponds to the string key.
	  */
    public static int convertToSWTConstant(String value, int defaultValue) {
        try {
            return SWT.class.getField(value).getInt(null);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /** Scans the attributes of a node for one with a given name.
	  * 
	  * @param node the xml tag that is scanned.
	  * @param key the name of the attribute we seek.
	  * @return the value of the attribute of null if the attribute does not exist.
	  */
    public static String getAttribute(Node node, String key) {
        Node nl = node.getAttributes().getNamedItem(key);
        if (nl != null) {
            return ((Attr) nl).getValue();
        }
        return null;
    }

    /** Converts a string value for an alignment to an SWT constant.
	  * 
	  * @param align a string name describing the alignment (top, bottom, etc.)
	  * @return the SWT constant that describes the alignment.
	  */
    public static int convertToSWTAlignment(String align) {
        if ("left".equals(align)) return SWT.LEFT;
        if ("right".equals(align)) return SWT.RIGHT;
        if ("center".equals(align)) return SWT.CENTER;
        if ("middle".equals(align)) return SWT.CENTER;
        if ("top".equals(align)) return SWT.TOP;
        if ("bottom".equals(align)) return SWT.BOTTOM;
        return SWT.BEGINNING;
    }
}
