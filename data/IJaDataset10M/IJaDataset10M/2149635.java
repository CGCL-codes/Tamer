package uk.ac.essex.ia.imageview.pane;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import uk.ac.essex.common.image.DicomImage;
import uk.ac.essex.common.lang.Translator;
import uk.ac.essex.ia.imageview.ImageViewerConstants;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.RenderedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Hashtable;

/**
 * The event processing center for this application. This class responses for
 * loading data sets, processing the events from the utility menu that
 * includes changing the operation scope, the layout, window/level, rotation
 * angle, zoom factor, starting/stoping the cining-loop and etc.
 * TODO:
 */
public class ImageViewerState implements ActionListener, ChangeListener, ImageViewerConstants, PropertyChangeListener {

    /** The logger */
    private static final transient Log logger = LogFactory.getLog(ImageViewerState.class);

    /** The single instance of this singleton class. */
    private static ImageViewerState instance;

    /**
     * The operation scope: operation will be implemented on all the
     * views or only the current view.
     */
    private static int OPERATE_SCOPE_ALLVIEWS = 1;

    /**
     * The operation scope: operation will be implemented on all the
     * views or only the current view.
     */
    private static int OPERATE_SCOPE_CURRENTVIEW = 2;

    /** The current image in the original image list. */
    private int currentImageNum;

    /** The current layout of this view pane. */
    private int currentLayout = (new Integer(Translator.getInstance(RESOURCE_BASE_NAME).get("DefaultLayout"))).intValue();

    /** The operation scope. */
    private int operateScope;

    /** This wraps the valuse of the sliders */
    private SliderValues sliderValues = new SliderValues();

    /** Indicates whether the cining-loop is started. */
    private boolean cining;

    /** The cining speed. */
    private int speed;

    /** The property change event manager. */
    private IVPropertyChangeSupport changeSupport;

    /**
     * Return the single instance of this class.  This method guarantees
     * the singleton property of this class.
     */
    public static synchronized ImageViewerState getInstance() {
        if (instance == null) {
            instance = new ImageViewerState();
        }
        return instance;
    }

    /**
     * The default private constructor to guarantee the singleton property
     * of this class.
     */
    private ImageViewerState() {
        changeSupport = new IVPropertyChangeSupport(this);
    }

    /** Intialize the parameters. */
    {
        initializeParameters();
    }

    /** Add a property change listener. */
    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(propertyName, listener);
    }

    /** Remove a property change listener. */
    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(propertyName, listener);
    }

    /** Update the slider marks. */
    public void updateSliders() {
        UtilityMenuPane.getInstance().updateSliders(sliderValues);
        firePropertyChange(zoomCommand, null, new Double(sliderValues.getZoomFactor()));
        firePropertyChange(rotationCommand, null, new Integer(sliderValues.getRotationAngle()));
        firePropertyChange(windowCommand, null, new Integer(sliderValues.getWindow()));
        firePropertyChange(levelCommand, null, new Integer(sliderValues.getLevel()));
    }

    /** Create the wrapped image for the focused pane. */
    public RenderedImage getImageForFocusPane() {
        MultipleImagePane multipleImagePane = TestImagePaneApp.getInstance().getMultipleImagePane();
        int focused = multipleImagePane.getFocused();
        return null;
    }

    /** process the action events. */
    public void actionPerformed(ActionEvent evt) {
        String command = evt.getActionCommand();
        Object source = evt.getSource();
        MultipleImagePane multipleImagePane = TestImagePaneApp.getInstance().getMultipleImagePane();
        if (command.equals(setLayoutCommand)) {
            synchronized (changeSupport) {
                removePropertyChangeListeners();
            }
            currentLayout = ((JComboBox) source).getSelectedIndex() + 1;
            initializeParameters();
            UtilityMenuPane.getInstance().initUtilityMenu();
            multipleImagePane.setImageGridLayout(currentLayout);
        } else if (command.equals(allViewsCommand)) {
            synchronized (changeSupport) {
                removePropertyChangeListeners();
                operateScope = OPERATE_SCOPE_ALLVIEWS;
                addPropertyChangeListeners();
            }
            updateSliders();
        } else if (command.equals(currentViewCommand)) {
            synchronized (changeSupport) {
                removePropertyChangeListeners();
                operateScope = OPERATE_SCOPE_CURRENTVIEW;
                addPropertyChangeListeners();
            }
            updateSliders();
        } else if (command.equals(speedCommand)) {
            String speedString = ((JTextField) source).getText();
            speed = (new Integer(speedString)).intValue();
        } else if (command.equals(startCommand)) {
            startCining();
        } else if (command.equals(stopCommand)) {
            stopCining();
        } else if (command.equals(annotationCommand)) {
            boolean annotation = ((JToggleButton) source).isSelected();
            firePropertyChange(annotationCommand, !annotation, annotation);
        } else if (command.equals(measurementCommand)) {
            boolean measurement = ((JToggleButton) source).isSelected();
            firePropertyChange(measurementCommand, !measurement, measurement);
        } else if (command.equals(statisticsCommand)) {
            boolean statistics = ((JToggleButton) source).isSelected();
            firePropertyChange(statisticsCommand, !statistics, statistics);
        } else if (command.equals(histogramCommand)) {
            boolean histogram = ((JToggleButton) source).isSelected();
            firePropertyChange(histogramCommand, !histogram, histogram);
        }
    }

    /** Process the state change events. */
    public void stateChanged(ChangeEvent evt) {
        Object source = evt.getSource();
        Hashtable table = UtilityMenuPane.getInstance().getSliderTable();
        String name = (String) table.get(evt.getSource());
        Object newValue = null;
        if (name == null) {
            return;
        }
        if (name.equals(zoomCommand)) {
            double zoom = sliderValueToZoom(((JSlider) source).getValue());
            sliderValues.setZoomFactor(zoom);
            newValue = new Double(zoom);
        } else if (name.equals(rotationCommand)) {
            int rotationAngle = ((JSlider) source).getValue();
            sliderValues.setRotationAngle(rotationAngle);
            newValue = new Integer(rotationAngle);
        } else if (name.equals(windowCommand)) {
            int window = ((JSlider) source).getValue();
            sliderValues.setWindow(window);
            newValue = new Integer(window);
        } else if (name.equals(levelCommand)) {
            int level = ((JSlider) source).getValue();
            sliderValues.setLevel(level);
            newValue = new Integer(level);
        }
        firePropertyChange(name, null, newValue);
    }

    /**
     * Process the property change event.  This is used to synchronize the
     * sliders on the display when the focus is changed.
     */
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(paramSync)) {
            synchronized (changeSupport) {
                removePropertyChangeListeners();
                addPropertyChangeListeners();
            }
            Object[] array = (Object[]) evt.getNewValue();
            sliderValues.setWindow(((Integer) array[0]).intValue());
            sliderValues.setLevel(((Integer) array[1]).intValue());
            sliderValues.setRotationAngle(((Integer) array[2]).intValue());
            sliderValues.setZoomFactor(((Double) array[3]).doubleValue());
            updateSliders();
        }
    }

    /** Initialize the parameters. */
    private void initializeParameters() {
        currentImageNum = 0;
        operateScope = OPERATE_SCOPE_CURRENTVIEW;
        sliderValues.setZoomFactor(1.0);
        sliderValues.setRotationAngle(0);
        sliderValues.setWindow(defaultWindow);
        sliderValues.setLevel(defaultLevel);
        cining = false;
        speed = new Integer(Translator.getInstance(RESOURCE_BASE_NAME).get("DefaultCineSpeed")).intValue();
    }

    /** Add all the property change listeners to this command center. */
    private void addPropertyChangeListeners() {
        PropertyChangeListener[] listeners = getCurrentListeners();
        for (int i = 0; i < listeners.length; i++) {
            if (listeners[i] instanceof IVImagePane) {
                addPropertyChangeListener(annotationCommand, listeners[i]);
                addPropertyChangeListener(measurementCommand, listeners[i]);
                addPropertyChangeListener(statisticsCommand, listeners[i]);
                addPropertyChangeListener(histogramCommand, listeners[i]);
            } else if (listeners[i] instanceof RenderedImage) {
                addPropertyChangeListener(windowCommand, listeners[i]);
                addPropertyChangeListener(levelCommand, listeners[i]);
                addPropertyChangeListener(rotationCommand, listeners[i]);
                addPropertyChangeListener(zoomCommand, listeners[i]);
            }
        }
    }

    /** Create a thread to cine the images. */
    private Thread createCiningThread() {
        return new Thread() {

            public void run() {
                int wait = 1000 / speed;
                long start = System.currentTimeMillis();
                int iteration = 0;
                MultipleImagePane multipleImagePane = TestImagePaneApp.getInstance().getMultipleImagePane();
                Robot robot = null;
                try {
                    robot = new java.awt.Robot();
                } catch (Exception e) {
                }
                while (cining) {
                    iteration++;
                    currentImageNum++;
                    synchronized (changeSupport) {
                        removePropertyChangeListeners();
                        if (operateScope == OPERATE_SCOPE_ALLVIEWS) {
                            ;
                        } else {
                            multipleImagePane.setFocusedPane(getImageForFocusPane());
                        }
                        addPropertyChangeListeners();
                    }
                    multipleImagePane.revalidate();
                    multipleImagePane.repaint();
                    robot.waitForIdle();
                    long elapsed = (System.currentTimeMillis() - start) / 1000;
                    if (elapsed > 0) {
                        double fps = ((double) iteration) / elapsed;
                        if (fps < speed) {
                            wait--;
                        } else {
                            wait++;
                        }
                        if (wait < 0) {
                            wait = 0;
                        }
                    }
                    if (wait > 0) {
                        try {
                            Thread.sleep(wait);
                        } catch (Exception e) {
                            logger.error("ERROR: ", e);
                        }
                    }
                }
                synchronized (changeSupport) {
                    removePropertyChangeListeners();
                    addPropertyChangeListeners();
                }
            }
        };
    }

    /** Fire property change event for the boolean property. */
    private void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {
        firePropertyChange(propertyName, new Boolean(oldValue), new Boolean(newValue));
    }

    /** Fire property change event. */
    private void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        synchronized (changeSupport) {
            changeSupport.firePropertyChange(propertyName, oldValue, newValue);
        }
    }

    /** Return all the objects who listens to this command center. */
    private PropertyChangeListener[] getCurrentListeners() {
        MultipleImagePane multipleImagePane = TestImagePaneApp.getInstance().getMultipleImagePane();
        IVImagePane[] panes = multipleImagePane.getImagePanels();
        int size;
        PropertyChangeListener[] listeners = null;
        if (operateScope == OPERATE_SCOPE_ALLVIEWS) {
            size = 2 * panes.length;
            listeners = new PropertyChangeListener[size];
            for (int i = 0; i < size; i += 2) {
                listeners[i] = panes[i / 2];
                listeners[i + 1] = (PropertyChangeListener) (panes[i / 2]).getImage();
            }
        } else if (operateScope == OPERATE_SCOPE_CURRENTVIEW) {
            size = panes.length + 1;
            PropertyChangeListener listener = (PropertyChangeListener) panes[multipleImagePane.getFocused()].getImage();
            listeners = new PropertyChangeListener[size];
            listeners[0] = listener;
            for (int i = 1; i < size; i++) {
                listeners[i] = (PropertyChangeListener) panes[i - 1];
            }
        }
        return listeners;
    }

    /** Remove all the listeners attached to the command center. */
    private void removePropertyChangeListeners() {
        changeSupport.removePropertyChangeListeners();
    }

    /** Synchronize the parameters. */
    private void resetParam(ImageViewerOpImage image) {
        image.setWindow(sliderValues.getWindow());
        image.setLevel(sliderValues.getLevel());
        image.setZoom(sliderValues.getZoomFactor());
        image.setRotation(sliderValues.getRotationAngle());
    }

    /** Convert the value marked on the slider to the zoom factor. */
    private double sliderValueToZoom(int value) {
        int nozoom1 = nozoom * 10;
        double zoom;
        if (value > nozoom1) {
            zoom = (value - nozoom1) / 10.0 + 1.0;
        } else {
            zoom = value * 0.8 / nozoom1 + 0.2;
        }
        return zoom;
    }

    /** Start the cining. */
    private void startCining() {
        cining = true;
        createCiningThread().start();
    }

    /** Stop the cining. */
    private void stopCining() {
        cining = false;
    }

    /**
     * Wrap a DICOM image with the window/level, rotation, zoom operators.
     * attach <code>this</code> to listener to synchronize the parameters.
     */
    private RenderedImage wrapOperations(RenderedImage source) {
        ImageViewerOpImage image = null;
        if (source instanceof DicomImage) {
            image = new IVDicomOpImage(source, sliderValues.getWindow(), sliderValues.getLevel(), sliderValues.getRotationAngle(), sliderValues.getZoomFactor());
        } else {
            image = new ImageViewerOpImage(source, sliderValues.getWindow(), sliderValues.getLevel(), sliderValues.getRotationAngle(), sliderValues.getZoomFactor());
        }
        PropertyChangeListener listener = this;
        image.addPropertyChangeListener(paramSync, listener);
        return image;
    }
}
