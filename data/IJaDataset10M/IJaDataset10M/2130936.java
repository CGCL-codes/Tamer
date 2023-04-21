package fr.itris.glips.rtdaeditor.shape;

import java.awt.*;
import java.awt.geom.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import fr.itris.glips.library.Toolkit;
import fr.itris.glips.library.widgets.FileDialog;
import fr.itris.glips.rtda.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.display.canvas.*;
import fr.itris.glips.svgeditor.display.handle.*;
import fr.itris.glips.svgeditor.resources.*;
import fr.itris.glips.svgeditor.shape.RectangularShape;

/**
 * the super class of the rtda shape modules
 * @author Jordi SUC
 */
public abstract class RtdaShape extends RectangularShape {

    /**
	 * the array of the file extensions supported by this module
	 */
    protected static final String[] extensions = { "svg" };

    /**
	 * the dialog used to selected a file in the file system
	 */
    protected fr.itris.glips.library.widgets.FileDialog fileDialog;

    /**
	 * the labels
	 */
    protected String errorLabel = "", insertionFailureLabel = "", wrongFileTypeLabel = "";

    /**
	 * the constructor of the class
	 * @param editor the editor
	 */
    public RtdaShape(Editor editor) {
        super(editor);
        if (Editor.getParent() instanceof Frame) {
            fileDialog = new FileDialog((Frame) Editor.getParent());
        } else {
            fileDialog = new FileDialog((JDialog) Editor.getParent());
        }
        ResourceBundle bundle = ResourcesManager.bundle;
        errorLabel = bundle.getString("labelerror");
        insertionFailureLabel = bundle.getString("RtdaShapeInsertionFailed");
    }

    @Override
    protected boolean isDrawingEnabled(SVGHandle handle) {
        boolean isDrawingEnabled = false;
        if (handle != null) {
            boolean isAView = fr.itris.glips.library.Toolkit.isDocumentAView(handle.getScrollPane().getSVGCanvas().getDocument());
            if (isAView) {
                isDrawingEnabled = true;
            }
        }
        return isDrawingEnabled;
    }

    @Override
    public void notifyDrawingAction(SVGHandle handle, Point2D point, int modifier, int type) {
        switch(type) {
            case DRAWING_MOUSE_RELEASED:
                Point2D scaledPoint = handle.getTransformsManager().getScaledPoint(point, true);
                createElement(handle, scaledPoint);
                resetDrawing();
                break;
        }
    }

    /**
	 * creating the element to be added to the canvas
	 * @param handle a svg handle
	 * @param location the location for the new element to create
	 */
    public void createElement(final SVGHandle handle, final Point2D location) {
        Thread thread = new Thread() {

            @Override
            public void run() {
                String[] values = getSVGFile(handle);
                if (values != null) {
                    ViewOrWidgetData data = getSVGData(values);
                    if (data != null) {
                        final Element newElement = createElement(handle, data, location);
                        SwingUtilities.invokeLater(new Runnable() {

                            public void run() {
                                handle.getSelection().handleSelection(newElement, false, false);
                            }
                        });
                    }
                }
            }
        };
        thread.start();
    }

    @Override
    public boolean isPrioritary() {
        return true;
    }

    /**
	 * asks the user for a svg file (should not be invoked in the AWT thread)
	 * @param handle a svg handle
	 * @return an array of string of three elements : the first one is the absolute svg file path,  
	 * the second one is the relative svg file path to the current canvas path, or an empty string if it is not used,
	 * the third is the xml path of a view or an empty string, if the file is not a view
	 */
    protected abstract String[] getSVGFile(SVGHandle handle);

    /**
	 * returns the svg data corresponding to provided file
	 * @param values the array of the values for the svg data
	 * @return the svg data corresponding to provided file
	 */
    protected ViewOrWidgetData getSVGData(String[] values) {
        ViewOrWidgetData svgData = null;
        Document document = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            factory.setValidating(false);
            factory.setIgnoringComments(true);
            document = builder.parse(new File(new URI(values[0])));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (document == null) {
            showInsertionFailedErrorDialog();
        } else if (isWrongFileType(document)) {
            showWrongFileTypeErrorDialog();
        } else {
            svgData = new ViewOrWidgetData(document, values[0], values[2]);
        }
        return svgData;
    }

    /**
	 * returns whether the provided document has a wrong file type
	 * @param doc a document
	 * @return whether the provided document has a wrong file type
	 */
    protected abstract boolean isWrongFileType(Document doc);

    /**
	 * creates the element defined by the provided svg data
	 * @param handle a svg handle
	 * @param svgData a svg data
	 * @param location the location of the element
	 * @return the element defined by the provided svg data
	 */
    protected abstract Element createElement(SVGHandle handle, ViewOrWidgetData svgData, Point2D location);

    /**
	 * shows an error dialog specifying that the insertion failed
	 */
    protected void showInsertionFailedErrorDialog() {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                JOptionPane.showMessageDialog(Editor.getParent(), insertionFailureLabel, errorLabel, JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    /**
	 * shows an error dialog specifying that the file to be inserted has a wrong type
	 */
    protected void showWrongFileTypeErrorDialog() {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                JOptionPane.showMessageDialog(Editor.getParent(), wrongFileTypeLabel, errorLabel, JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    @Override
    public Shape getShape(SVGHandle handle, Element element, boolean isOutline) {
        Shape shape = new Rectangle();
        double initX = EditorToolkit.getAttributeValue(element, xAtt);
        double initY = EditorToolkit.getAttributeValue(element, yAtt);
        double initW = EditorToolkit.getAttributeValue(element, wAtt);
        double initH = EditorToolkit.getAttributeValue(element, hAtt);
        if (!Double.isNaN(initX) && !Double.isNaN(initY) && !Double.isNaN(initW) && !Double.isNaN(initH)) {
            shape = new Rectangle2D.Double(initX, initY, initW, initH);
            if (isOutline) {
                Shape outline = handle.getSvgElementsManager().getGeometryOutline(element);
                GeneralPath path = new GeneralPath(outline);
                path.append(shape, false);
                shape = path;
            }
        }
        return shape;
    }

    /**
	 * the class used to store information on a view or on a widget
	 * @author ITRIS, Jordi SUC
	 */
    protected class ViewOrWidgetData {

        /**
		 * the document
		 */
        private Document document;

        /**
		 * the view xml path
		 */
        private String viewXmlPath = "";

        /**
		 * the path of the svg file
		 */
        private String path = "";

        /**
		 * the project file
		 */
        private File projectFile;

        /**
		 * the constructor of the class
		 * @param document the document
		 * @param path the path of the svg file
		 * @param viewXmlPath the view xml path
		 */
        protected ViewOrWidgetData(Document document, String path, String viewXmlPath) {
            this.document = document;
            this.path = path;
            this.viewXmlPath = viewXmlPath;
            try {
                projectFile = AnimationsToolkit.getProjectFile(new URI(path), true);
            } catch (Exception ex) {
            }
        }

        /**
		 * @return the document
		 */
        protected Document getDocument() {
            return document;
        }

        /**
		 * @return the path
		 */
        protected String getPath() {
            return path;
        }

        /**
		 * @return the view xml path
		 */
        public String getViewXmlPath() {
            return viewXmlPath;
        }

        /**
		 * @return the project name
		 */
        public String getProjectName() {
            return Toolkit.getFileName(projectFile);
        }

        /**
		 * relativizes the path against the provided base path
		 * @param basePath the base path
		 * @return the relativized path
		 */
        protected String relativize(String basePath) {
            String relativePath = path;
            try {
                File referenceFile = new File(new URI(basePath));
                relativePath = fr.itris.glips.library.Toolkit.getRelativePath(new URI(path).toASCIIString(), referenceFile.getParentFile().toURI().toASCIIString());
            } catch (Exception ex) {
            }
            return relativePath;
        }

        /**
		 * @return the size of the svg file denoted by the document
		 */
        protected Point2D getSize() {
            return SVGCanvas.getGeometryCanvasSize(document);
        }
    }
}
