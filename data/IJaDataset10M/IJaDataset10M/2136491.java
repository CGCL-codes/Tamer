package org.in4ama.editor.oo;

import org.in4ama.editor.exception.EditorException;
import com.sun.star.awt.Size;
import com.sun.star.beans.Property;
import com.sun.star.beans.UnknownPropertyException;
import com.sun.star.beans.XPropertyChangeListener;
import com.sun.star.beans.XPropertySet;
import com.sun.star.container.XNamed;
import com.sun.star.drawing.TextFitToSizeType;
import com.sun.star.drawing.XDrawPage;
import com.sun.star.drawing.XDrawPageSupplier;
import com.sun.star.drawing.XDrawPages;
import com.sun.star.drawing.XDrawPagesSupplier;
import com.sun.star.drawing.XShape;
import com.sun.star.drawing.XShapeDescriptor;
import com.sun.star.drawing.XShapes;
import com.sun.star.frame.XController;
import com.sun.star.lang.WrappedTargetException;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XMultiServiceFactory;
import com.sun.star.text.XText;
import com.sun.star.text.XTextCursor;
import com.sun.star.text.XTextDocument;
import com.sun.star.uno.Any;
import com.sun.star.uno.Exception;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.view.XSelectionSupplier;

/**
 * Helper class for creating and manipulating shape components within the OpenOffice
 * document.
 * 
 * @author Val Cassidy, Jakub Jonik
 */
public class ShapeHelper {

    public static final String SHAPE_TEXT = "com.sun.star.drawing.TextShape";

    public static final String SHAPE_GRAPHIC = "com.sun.star.drawing.GraphicObjectShape";

    public static final String SHAPE_CONTROL = "com.sun.star.drawing.ControlShape";

    public static final String SHAPE_OLE2 = "com.sun.star.drawing.OLE2Shape";

    public static final String OLE2_WRITER = "8BC6B165-B1B2-4EDD-aa47-dae2ee689dd6";

    /**
	 * Insert a text shape into the Writer document.
	 * @param doc The XTextDocument reference into which the shape will be created
	 * @param text The String to be inserted into the text shape
	 * @throws EditorException 
	 */
    public void insertTextShape(XTextDocument doc, String text) throws EditorException {
        try {
            XDrawPage drawPage = getDocumentDrawPage(doc);
            XShapes xShapes = (XShapes) UnoRuntime.queryInterface(XShapes.class, drawPage);
            XShape xShape = createShape(doc, 10, 10, 100, 20, "com.sun.star.drawing.TextShape");
            xShape.setSize(new Size(2000, 100));
            xShapes.add(xShape);
            setShapeText(xShape, text);
        } catch (java.lang.Exception e) {
            String msg = "Unable to insert the text shape.";
            throw new EditorException(msg, e);
        }
    }

    /**
	 * Set the text in the referenced shape
	 * @param xShape the XShape who's text is to be set
	 * @param text the text which is to appear in the shape
	 * @throws EditorException 
	 */
    private void setShapeText(XShape xShape, String text) throws EditorException {
        try {
            XText xText = (XText) UnoRuntime.queryInterface(XText.class, xShape);
            XTextCursor xTextCursor = xText.createTextCursor();
            xText.insertString(xTextCursor, text, true);
            XPropertySet xTextProps = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, xText);
            xTextProps.setPropertyValue("TextFitToSize", TextFitToSizeType.NONE);
        } catch (Exception e) {
            String msg = "Unable to set the shape text.";
            throw new EditorException(msg, e);
        }
    }

    /**
	 * Retrieve the XDrawPage reference for the passed document object
	 * @param document the document who's XDrawPage is required
	 * @return the retrieved XDrawPage
	 * @throws Exception
	 */
    private XDrawPage getDocumentDrawPage(XComponent document) throws Exception {
        XDrawPage xDrawPage = null;
        XDrawPageSupplier xSuppPage = (XDrawPageSupplier) UnoRuntime.queryInterface(XDrawPageSupplier.class, document);
        xDrawPage = xSuppPage.getDrawPage();
        if (xDrawPage == null) {
            XDrawPagesSupplier xSuppPages = (XDrawPagesSupplier) UnoRuntime.queryInterface(XDrawPagesSupplier.class, document);
            if (xSuppPages != null && xSuppPages.getDrawPages() != null) {
                XDrawPages xPages = xSuppPages.getDrawPages();
                xDrawPage = (XDrawPage) UnoRuntime.queryInterface(XDrawPage.class, xPages.getByIndex(0));
            }
        }
        return xDrawPage;
    }

    /**
     * Create a shape in the passed XComponent
     * @param xComponent the component which will contain the new shape
     * @param x the x coordinate of the new shape
     * @param y the y coordinate of the new shape
     * @param width the width of the new shape
     * @param height the height of the new shape
     * @param sShapeType the new shape type
     * @return the created shape
     * @throws java.lang.Exception
     */
    public static XShape createShape(XComponent xComponent, int x, int y, int width, int height, String sShapeType) throws java.lang.Exception {
        XMultiServiceFactory xFactory = (XMultiServiceFactory) UnoRuntime.queryInterface(XMultiServiceFactory.class, xComponent);
        Object xObj = xFactory.createInstance(sShapeType);
        Size aSize = new Size(width, height);
        XShape xShape = (XShape) UnoRuntime.queryInterface(XShape.class, xObj);
        xShape.setSize(aSize);
        return xShape;
    }

    public void getTextShapes(XDrawPage drawPage) throws EditorException {
        try {
            XShapes xShapes = (XShapes) UnoRuntime.queryInterface(XShapes.class, drawPage);
            for (int i = 0; i < xShapes.getCount(); i++) {
                Any anyShape = (Any) xShapes.getByIndex(i);
                XShape shape = (XShape) anyShape.getObject();
            }
        } catch (java.lang.Exception e) {
            String msg = "Unable to get the text shapes.";
            throw new EditorException(msg, e);
        }
    }

    public static Property[] getProperties(XShape shape) {
        com.sun.star.beans.XPropertySet xShapeProps = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, shape);
        Property[] props = xShapeProps.getPropertySetInfo().getProperties();
        return props;
    }

    public static Object getProperty(XShape shape, String propName) throws EditorException {
        try {
            com.sun.star.beans.XPropertySet xShapeProps = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, shape);
            Object prop = xShapeProps.getPropertyValue(propName);
            if (prop == null) {
                return "";
            } else if (prop instanceof String) {
                return prop.toString();
            } else {
                return "";
            }
        } catch (Exception e) {
            String msg = "Unable to retrieve the shape property.";
            throw new EditorException(msg, e);
        }
    }

    public static void setProperty(XShape shape, String propName, Object propValue) throws EditorException {
        try {
            com.sun.star.beans.XPropertySet xShapeProps = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, shape);
            xShapeProps.setPropertyValue(propName, propValue);
        } catch (Exception e) {
            String msg = "Unable to set the shape property.";
            throw new EditorException(msg, e);
        }
    }

    public void addListener(XShape shape) {
    }

    public static void addPropertyChangeListener(XPropertyChangeListener listener, XShape shape, String property) throws EditorException {
        try {
            com.sun.star.beans.XPropertySet xShapeProps = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, shape);
            xShapeProps.addPropertyChangeListener(property, listener);
        } catch (Exception e) {
            String msg = "Unable to add property change listener.";
            throw new EditorException(msg, e);
        }
    }

    public XPropertySet getShapeProps(Object component) {
        com.sun.star.beans.XPropertySet xShapeProps = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, component);
        return xShapeProps;
    }

    public void addPropertyChangeListener(Object component, String propName, XPropertyChangeListener listener) throws EditorException {
        try {
            XPropertySet xPropSet = getShapeProps(component);
            xPropSet.addPropertyChangeListener(propName, listener);
        } catch (UnknownPropertyException e) {
        } catch (WrappedTargetException e) {
            String msg = "Unable to add property change listener.";
            throw new EditorException(msg, e);
        }
    }

    public static void outputProps(Object component) {
        com.sun.star.beans.XPropertySet xShapeProps = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, component);
        if (xShapeProps != null) {
            XNamed xnamed = (XNamed) UnoRuntime.queryInterface(XNamed.class, component);
            if (xnamed != null) {
            }
            Property[] props = xShapeProps.getPropertySetInfo().getProperties();
        }
    }

    public static void setSelectedShape(XController controller, Object shape) throws EditorException {
        try {
            XSelectionSupplier supplier;
            XSelectionSupplier xSelectionSupplier = (XSelectionSupplier) UnoRuntime.queryInterface(XSelectionSupplier.class, controller);
            xSelectionSupplier.select(shape);
        } catch (Exception e) {
            String msg = "Unable to add property change listener.";
            throw new EditorException(msg, e);
        }
    }

    public static XShape getSelectedShape(XController controller) throws EditorException {
        try {
            XSelectionSupplier supplier;
            XSelectionSupplier xSelectionSupplier = (XSelectionSupplier) UnoRuntime.queryInterface(XSelectionSupplier.class, controller);
            Any anyShapes = (Any) xSelectionSupplier.getSelection();
            XShapes xShapes = (XShapes) UnoRuntime.queryInterface(XShapes.class, anyShapes);
            Any anyShape = (Any) xShapes.getByIndex(0);
            XShape shape = (XShape) anyShape.getObject();
            return shape;
        } catch (Exception e) {
            String msg = "Unable to get the selected shape.";
            throw new EditorException(msg, e);
        }
    }

    public static String getShapeType(XShape shape) {
        XShapeDescriptor descriptor = (XShapeDescriptor) shape;
        return descriptor.getShapeType();
    }

    public static String getShapeName(XShape shape) {
        XNamed xnamed = (XNamed) UnoRuntime.queryInterface(XNamed.class, shape);
        return xnamed.getName();
    }

    public static void setShapeName(XShape shape, String name) {
        XNamed xnamed = (XNamed) UnoRuntime.queryInterface(XNamed.class, shape);
        xnamed.setName(name);
    }

    public static String getShapeNameAttribute(XShape shape) {
        XShapeDescriptor descriptor = (XShapeDescriptor) shape;
        String type = descriptor.getShapeType();
        if (type.equals(SHAPE_TEXT)) {
            return "Name";
        } else if (type.equals(SHAPE_GRAPHIC)) {
            return "Name";
        } else if (type.equals(SHAPE_CONTROL)) {
            return "LinkDisplayName";
        } else if (type.equals(SHAPE_OLE2)) {
            return "Name";
        } else {
            return "LinkDisplayName";
        }
    }

    public static String getShapeDescription(XShape shape) throws EditorException {
        XShapeDescriptor descriptor = (XShapeDescriptor) shape;
        String type = descriptor.getShapeType();
        if (type.equals(SHAPE_TEXT)) {
            return "Text";
        } else if (type.equals(SHAPE_GRAPHIC)) {
            return "Image";
        } else if (type.equals(SHAPE_CONTROL)) {
            return "Control";
        } else if (type.equals(SHAPE_OLE2)) {
            String clsid = (String) getProperty(shape, "CLSID");
            if (clsid.equals(OLE2_WRITER)) {
                return "Writer";
            } else {
                return "OLE";
            }
        } else {
            return "Shape";
        }
    }
}
