package org.jhotdraw.samples.svg;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.print.Pageable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.prefs.Preferences;
import org.jhotdraw.draw.ImageInputFormat;
import org.jhotdraw.draw.ImageOutputFormat;
import org.jhotdraw.draw.OutputFormat;
import org.jhotdraw.geom.Dimension2DDouble;
import org.jhotdraw.gui.*;
import org.jhotdraw.io.*;
import org.jhotdraw.draw.InputFormat;
import org.jhotdraw.samples.svg.figures.*;
import org.jhotdraw.samples.svg.io.*;
import org.jhotdraw.undo.*;
import org.jhotdraw.util.*;
import java.awt.*;
import java.beans.*;
import java.io.*;
import java.lang.reflect.*;
import javax.swing.*;
import javax.swing.border.*;
import org.jhotdraw.app.*;
import org.jhotdraw.app.action.*;
import org.jhotdraw.draw.*;
import org.jhotdraw.draw.action.*;
import org.jhotdraw.xml.*;

/**
 * A drawing project.
 *
 * @author Werner Randelshofer
 * @version 1.2 2006-12-10 Used SVGStorage for reading SVG drawing (experimental).
 * <br>1.1 2006-06-10 Extended to support DefaultDrawApplicationModel.
 * <br>1.0 2006-02-07 Created.
 */
public class SVGProject extends AbstractProject implements ExportableProject {

    protected JFileChooser exportChooser;

    /**
     * Each SVGProject uses its own undo redo manager.
     * This allows for undoing and redoing actions per project.
     */
    private UndoRedoManager undo;

    /**
     * Depending on the type of an application, there may be one editor per
     * project, or a single shared editor for all projects.
     */
    private DrawingEditor editor;

    private HashMap<javax.swing.filechooser.FileFilter, InputFormat> fileFilterInputFormatMap;

    private HashMap<javax.swing.filechooser.FileFilter, OutputFormat> fileFilterOutputFormatMap;

    private GridConstrainer visibleConstrainer = new GridConstrainer(10, 10);

    private GridConstrainer invisibleConstrainer = new GridConstrainer(1, 1);

    private Preferences prefs;

    /**
     * Creates a new Project.
     */
    public SVGProject() {
    }

    /**
     * Initializes the project.
     */
    public void init() {
        super.init();
        prefs = Preferences.userNodeForPackage(getClass());
        initComponents();
        JPanel zoomButtonPanel = new JPanel(new BorderLayout());
        scrollPane.setLayout(new PlacardScrollPaneLayout());
        scrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        setEditor(new DefaultDrawingEditor());
        undo = new UndoRedoManager();
        view.setDrawing(createDrawing());
        view.getDrawing().addUndoableEditListener(undo);
        initActions();
        undo.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                setHasUnsavedChanges(undo.hasSignificantEdits());
            }
        });
        ResourceBundleUtil labels = ResourceBundleUtil.getLAFBundle("org.jhotdraw.draw.Labels");
        JPanel placardPanel = new JPanel(new BorderLayout());
        javax.swing.AbstractButton pButton;
        pButton = ButtonFactory.createZoomButton(view);
        pButton.putClientProperty("Quaqua.Button.style", "placard");
        pButton.putClientProperty("Quaqua.Component.visualMargin", new Insets(0, 0, 0, 0));
        pButton.setFont(UIManager.getFont("SmallSystemFont"));
        placardPanel.add(pButton, BorderLayout.WEST);
        pButton = ButtonFactory.createToggleGridButton(view);
        pButton.putClientProperty("Quaqua.Button.style", "placard");
        pButton.putClientProperty("Quaqua.Component.visualMargin", new Insets(0, 0, 0, 0));
        pButton.setFont(UIManager.getFont("SmallSystemFont"));
        labels.configureToolBarButton(pButton, "alignGridSmall");
        placardPanel.add(pButton, BorderLayout.EAST);
        scrollPane.add(placardPanel, JScrollPane.LOWER_LEFT_CORNER);
        propertiesPanel.setVisible(prefs.getBoolean("propertiesPanelVisible", false));
        propertiesPanel.setView(view);
    }

    /**
     * Creates a new Drawing for this Project.
     */
    protected Drawing createDrawing() {
        Drawing drawing = new SVGDrawing();
        LinkedList<InputFormat> inputFormats = new LinkedList<InputFormat>();
        inputFormats.add(new SVGZInputFormat());
        inputFormats.add(new ImageInputFormat(new SVGImageFigure()));
        inputFormats.add(new ImageInputFormat(new SVGImageFigure(), "JPG", "Joint Photographics Experts Group (JPEG)", "jpg", BufferedImage.TYPE_INT_RGB));
        inputFormats.add(new ImageInputFormat(new SVGImageFigure(), "GIF", "Graphics Interchange Format (GIF)", "gif", BufferedImage.TYPE_INT_ARGB));
        inputFormats.add(new TextInputFormat(new SVGTextFigure()));
        drawing.setInputFormats(inputFormats);
        LinkedList<OutputFormat> outputFormats = new LinkedList<OutputFormat>();
        outputFormats.add(new SVGOutputFormat());
        outputFormats.add(new SVGZOutputFormat());
        outputFormats.add(new ImageOutputFormat());
        outputFormats.add(new ImageOutputFormat("JPG", "Joint Photographics Experts Group (JPEG)", "jpg", BufferedImage.TYPE_INT_RGB));
        outputFormats.add(new ImageOutputFormat("BMP", "Windows Bitmap (BMP)", "bmp", BufferedImage.TYPE_BYTE_INDEXED));
        outputFormats.add(new ImageMapOutputFormat());
        drawing.setOutputFormats(outputFormats);
        return drawing;
    }

    /**
     * Creates a Pageable object for printing the project.
     */
    public Pageable createPageable() {
        return new DrawingPageable(view.getDrawing());
    }

    public DrawingEditor getEditor() {
        return editor;
    }

    public void setEditor(DrawingEditor newValue) {
        DrawingEditor oldValue = editor;
        if (oldValue != null) {
            oldValue.remove(view);
        }
        editor = newValue;
        propertiesPanel.setEditor(editor);
        if (newValue != null) {
            newValue.add(view);
        }
    }

    /**
     * Initializes project specific actions.
     */
    private void initActions() {
        putAction(UndoAction.ID, undo.getUndoAction());
        putAction(RedoAction.ID, undo.getRedoAction());
    }

    protected void setHasUnsavedChanges(boolean newValue) {
        super.setHasUnsavedChanges(newValue);
        undo.setHasSignificantEdits(newValue);
    }

    /**
     * Writes the project to the specified file.
     */
    public void write(File f) throws IOException {
        OutputStream out = null;
        try {
            out = new BufferedOutputStream(new FileOutputStream(f));
            new SVGOutputFormat().write(f, view.getDrawing());
        } finally {
            if (out != null) out.close();
        }
    }

    /**
     * Reads the project from the specified file.
     */
    public void read(File f) throws IOException {
        try {
            JFileChooser fc = getOpenChooser();
            final Drawing drawing = createDrawing();
            InputFormat sf = fileFilterInputFormatMap.get(fc.getFileFilter());
            if (sf == null) {
                sf = drawing.getInputFormats().get(0);
            }
            sf.read(f, drawing);
            SwingUtilities.invokeAndWait(new Runnable() {

                public void run() {
                    view.getDrawing().removeUndoableEditListener(undo);
                    view.setDrawing(drawing);
                    view.getDrawing().addUndoableEditListener(undo);
                    undo.discardAllEdits();
                }
            });
        } catch (InterruptedException e) {
            InternalError error = new InternalError();
            e.initCause(e);
            throw error;
        } catch (InvocationTargetException e) {
            InternalError error = new InternalError();
            error.initCause(e);
            throw error;
        }
    }

    /**
     * Gets the drawing editor of the project.
     */
    public DrawingEditor getDrawingEditor() {
        return editor;
    }

    public Drawing getDrawing() {
        return view.getDrawing();
    }

    public void setEnabled(boolean newValue) {
        view.setEnabled(newValue);
        super.setEnabled(newValue);
    }

    public void setPropertiesPanelVisible(boolean newValue) {
        boolean oldValue = propertiesPanel.isVisible();
        propertiesPanel.setVisible(newValue);
        firePropertyChange("propertiesPanelVisible", oldValue, newValue);
        prefs.putBoolean("propertiesPanelVisible", newValue);
        validate();
    }

    public boolean isPropertiesPanelVisible() {
        return propertiesPanel.isVisible();
    }

    /**
     * Clears the project.
     */
    public void clear() {
        view.setDrawing(new SVGDrawing());
        undo.discardAllEdits();
    }

    @Override
    protected JFileChooser createOpenChooser() {
        final JFileChooser c = super.createOpenChooser();
        fileFilterInputFormatMap = new HashMap<javax.swing.filechooser.FileFilter, InputFormat>();
        javax.swing.filechooser.FileFilter firstFF = null;
        for (InputFormat format : view.getDrawing().getInputFormats()) {
            javax.swing.filechooser.FileFilter ff = format.getFileFilter();
            if (firstFF == null) {
                firstFF = ff;
            }
            fileFilterInputFormatMap.put(ff, format);
            c.addChoosableFileFilter(ff);
        }
        c.setFileFilter(firstFF);
        c.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("fileFilterChanged")) {
                    InputFormat inputFormat = fileFilterInputFormatMap.get(evt.getNewValue());
                    c.setAccessory((inputFormat == null) ? null : inputFormat.getInputFormatAccessory());
                }
            }
        });
        return c;
    }

    @Override
    protected JFileChooser createSaveChooser() {
        JFileChooser c = super.createSaveChooser();
        fileFilterOutputFormatMap = new HashMap<javax.swing.filechooser.FileFilter, OutputFormat>();
        for (OutputFormat format : view.getDrawing().getOutputFormats()) {
            javax.swing.filechooser.FileFilter ff = format.getFileFilter();
            fileFilterOutputFormatMap.put(ff, format);
            c.addChoosableFileFilter(ff);
            break;
        }
        return c;
    }

    protected JFileChooser createExportChooser() {
        JFileChooser c = new JFileChooser();
        fileFilterOutputFormatMap = new HashMap<javax.swing.filechooser.FileFilter, OutputFormat>();
        javax.swing.filechooser.FileFilter currentFilter = null;
        for (OutputFormat format : view.getDrawing().getOutputFormats()) {
            javax.swing.filechooser.FileFilter ff = format.getFileFilter();
            fileFilterOutputFormatMap.put(ff, format);
            c.addChoosableFileFilter(ff);
            if (ff.getDescription().equals(prefs.get("projectExportFormat", ""))) {
                currentFilter = ff;
            }
        }
        if (currentFilter != null) {
            c.setFileFilter(currentFilter);
        }
        c.setSelectedFile(new File(prefs.get("projectExportFile", System.getProperty("user.home"))));
        return c;
    }

    private void initComponents() {
        scrollPane = new javax.swing.JScrollPane();
        view = new org.jhotdraw.draw.DefaultDrawingView();
        propertiesPanel = new org.jhotdraw.samples.svg.SVGPropertiesPanel();
        setLayout(new java.awt.BorderLayout());
        scrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setViewportView(view);
        add(scrollPane, java.awt.BorderLayout.CENTER);
        add(propertiesPanel, java.awt.BorderLayout.SOUTH);
    }

    public JFileChooser getExportChooser() {
        if (exportChooser == null) {
            exportChooser = createExportChooser();
        }
        return exportChooser;
    }

    public void export(File f, javax.swing.filechooser.FileFilter filter, Component accessory) throws IOException {
        OutputFormat format = fileFilterOutputFormatMap.get(filter);
        if (!f.getName().endsWith("." + format.getFileExtension())) {
            f = new File(f.getPath() + "." + format.getFileExtension());
        }
        format.write(f, view.getDrawing());
        prefs.put("projectExportFile", f.getPath());
        prefs.put("projectExportFormat", filter.getDescription());
    }

    public void setGridVisible(boolean newValue) {
        boolean oldValue = isGridVisible();
        Constrainer c = (newValue) ? visibleConstrainer : invisibleConstrainer;
        view.setConstrainer(c);
        firePropertyChange("gridVisible", oldValue, newValue);
        prefs.putBoolean("project.gridVisible", newValue);
    }

    public boolean isGridVisible() {
        return view.getConstrainer() == visibleConstrainer;
    }

    public double getScaleFactor() {
        return view.getScaleFactor();
    }

    public void setScaleFactor(double newValue) {
        double oldValue = getScaleFactor();
        view.setScaleFactor(newValue);
        firePropertyChange("scaleFactor", oldValue, newValue);
        prefs.putDouble("project.scaleFactor", newValue);
    }

    private org.jhotdraw.samples.svg.SVGPropertiesPanel propertiesPanel;

    private javax.swing.JScrollPane scrollPane;

    private org.jhotdraw.draw.DefaultDrawingView view;
}
