package org.dinopolis.util.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.Locale;
import java.util.MissingResourceException;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import org.dinopolis.util.ResourceManager;
import org.dinopolis.util.Resources;

/**
 * A Class the provides a Property Editor for the properties of a
 * 'Map'.
 *
 * @author Dieter Freismuth
 * @version $Revision: 1.4 $
 */
public class ResourceEditorFrame extends JFrame implements ActionListener, PropertyChangeListener {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /** the key for the close button resource */
    private static final String KEY_OK_BUTTON = "resource_editor.button.ok";

    /** the ok command name */
    private static final String OK_COMMAND = "ok";

    /** the key for the cancle button resource */
    private static final String KEY_CANCEL_BUTTON = "resource_editor.button.cancel";

    /** the cancel command name */
    private static final String CANCEL_COMMAND = "cancel";

    /** the title key for the title property */
    private static final String KEY_TITLE = "resource_editor.frame_title";

    /** the key for the width of the frame */
    private static final String KEY_WINDOW_DIMENSION_WIDTH = "resource_editor.dimension.width";

    /** the key for the height of the frame */
    private static final String KEY_WINDOW_DIMENSION_HEIGHT = "resource_editor.dimension.height";

    /** the key for the x location of the frame */
    private static final String KEY_WINDOW_LOCATION_X = "resource_editor.location.x";

    /** the key for the y location of the frame */
    private static final String KEY_WINDOW_LOCATION_Y = "resource_editor.location.y";

    private Object WAITERS = new Object();

    /** the ok button */
    private JButton ok_button_;

    /** the cancel button */
    private JButton cancel_button_;

    /** the location of the frame, needed to restore this location
   * after setVisible(false), setVisible(true) reqests. */
    private Point stored_location_;

    /** the title of the frame, if given in constructor */
    private String title_;

    /** the resource editor panel */
    private ResourceEditorPanel editor_panel_;

    /** remember size and location */
    private boolean remember_;

    private boolean return_ok_;

    /**
 * Creates a new ResourceEditorFrame that is able to edit the
 * resources given in <code>resources</code>.
 *
 * @param resources the reource bounble to edit.
 */
    public ResourceEditorFrame(Resources resources) {
        this(resources, ResourceEditorPanel.RESOURCE_DIR_NAME);
    }

    /**
 * Creates a new ResourceEditorFrame that is able to edit the
 * resources given in <code>resources</code>. The private resource
 * file is searched in resource_dir, relative to the users home dir.
 *
 * @param resources the reource bounble to edit.
 * @param resource_dir the relative directory to search users
 * resources. 
 */
    public ResourceEditorFrame(Resources resources, String resource_dir) throws MissingResourceException {
        this(resources, resource_dir, null);
    }

    /**
 * Creates a new ResourceEditorFrame with the given title, that is
 * able to edit the resources given in <code>resources</code>. The
 * private resource file is searched in resource_dir, relative to the
 * users home dir. 
 *
 * @param Resources the reource bounble to edit.
 * @param resource_dir the relative directory to search users
 * resources.
 * @param title the title to display, if null the default title is
 * used.
 */
    public ResourceEditorFrame(Resources resources, String resource_dir, String title) throws MissingResourceException {
        super();
        editor_panel_ = new ResourceEditorPanel(resources, resource_dir, false);
        ResourceEditorPanel.getResources().addPropertyChangeListener(this);
        title_ = title;
        if (title_ == null) title_ = resources.getString("editor.title", null);
        if (title_ != null) setTitle(title_);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(editor_panel_, BorderLayout.CENTER);
        getContentPane().add(createButtons(), BorderLayout.SOUTH);
        UIManager.addPropertyChangeListener(this);
        Runtime.getRuntime().addShutdownHook(new Thread() {

            public void run() {
                try {
                    if (remember_) storeEditorResources();
                } catch (IOException ioe) {
                    System.err.println("Resources or resource editor could not be stored: " + ioe.getMessage());
                }
            }
        });
        updateLocation();
        updateSize();
        reset();
    }

    /**
   * @param remember true if size and position should be stored, false otherwise.
   */
    public void rememberSizeAndPosition(boolean remember) {
        remember_ = remember;
    }

    /**
   * Register an editor class to be used to editor values of
   * a given target class.
   * 
   * 
   * @param type the Class object of the type to be edited.
   * @param editor the Class object of the editor class.  If
   *	   this is null, then any existing definition will be removed.
   * @exception  SecurityException  if a security manager exists and its  
   * <code>checkPropertiesAccess</code> method doesn't allow setting
   * of system properties.
   */
    public void registerEditor(Class type, Class editor) {
        editor_panel_.registerEditor(type, editor);
    }

    /**
   * Updates the size according to the values set within the resource
   * file. 
   */
    void updateSize() {
        try {
            setSize(ResourceEditorPanel.getResources().getInt(KEY_WINDOW_DIMENSION_WIDTH), ResourceEditorPanel.getResources().getInt(KEY_WINDOW_DIMENSION_HEIGHT));
        } catch (MissingResourceException exc) {
            pack();
        } catch (NumberFormatException exc) {
            pack();
        }
    }

    /**
   * Updates the locations according to the values set within the
   * resource file. 
   */
    void updateLocation() {
        try {
            setLocation(ResourceEditorPanel.getResources().getInt(KEY_WINDOW_LOCATION_X), ResourceEditorPanel.getResources().getInt(KEY_WINDOW_LOCATION_Y));
        } catch (MissingResourceException exc) {
        } catch (NumberFormatException exc) {
        }
    }

    /**
   */
    public void updateResources() {
        if (title_ == null) setTitle(ResourceEditorPanel.getResources().getString(KEY_TITLE));
        if (ok_button_ != null) {
            ok_button_.setText(ResourceEditorPanel.getResources().getString(KEY_OK_BUTTON));
            ok_button_.setMargin(editor_panel_.getButtonInsets());
        }
        if (cancel_button_ != null) {
            cancel_button_.setText(ResourceEditorPanel.getResources().getString(KEY_CANCEL_BUTTON));
            cancel_button_.setMargin(editor_panel_.getButtonInsets());
        }
    }

    /**
 * @return the panel that contains the close button
 */
    protected Component createButtons() {
        JPanel button_panel = new JPanel();
        button_panel.setLayout(new BoxLayout(button_panel, BoxLayout.X_AXIS));
        button_panel.add(Box.createGlue());
        ok_button_ = new JButton(ResourceEditorPanel.getResources().getString(KEY_OK_BUTTON));
        ok_button_.setMargin(editor_panel_.getButtonInsets());
        ok_button_.setActionCommand(OK_COMMAND);
        ok_button_.addActionListener(this);
        button_panel.add(ok_button_);
        cancel_button_ = new JButton(ResourceEditorPanel.getResources().getString(KEY_CANCEL_BUTTON));
        cancel_button_.setMargin(editor_panel_.getButtonInsets());
        cancel_button_.setActionCommand(CANCEL_COMMAND);
        cancel_button_.addActionListener(this);
        button_panel.add(cancel_button_);
        button_panel.add(editor_panel_.getApplyButton());
        button_panel.add(Box.createGlue());
        return (button_panel);
    }

    /**
 * Shows or hides this frame.
 *
 * @param visible true if the component is set to be visible, false
 * otherwise.
 */
    public void setVisible(boolean visible) {
        if (isVisible() != visible) {
            if (!visible) {
                stored_location_ = getLocationOnScreen();
                super.setVisible(visible);
                return;
            }
            super.setVisible(visible);
            if (stored_location_ != null) setLocation(stored_location_);
            return;
        }
        super.setVisible(visible);
    }

    /**
 * Invoked when the Close button was pressed.
 *
 * @param event the action event
 */
    public void actionPerformed(ActionEvent event) {
        return_ok_ = false;
        String command = event.getActionCommand();
        if (command.equals(OK_COMMAND)) {
            editor_panel_.apply();
            return_ok_ = true;
        }
        if ((command.equals(OK_COMMAND) || (command.equals(CANCEL_COMMAND)))) setVisible(false);
        synchronized (WAITERS) {
            WAITERS.notifyAll();
        }
    }

    public boolean returnedOK() {
        synchronized (WAITERS) {
            try {
                WAITERS.wait();
            } catch (InterruptedException e) {
            }
        }
        return (return_ok_);
    }

    /**
   * Updates the whole editor and shows all components for the given
   * Map. This method uses a swing worker to create all components in
   * background.
   *
   * @param node the node to show the properties.
   */
    public void reset() {
        Thread update_thread = new Thread() {

            public void run() {
                synchronized (this) {
                    editor_panel_.setAllEditors();
                    updateLocation();
                    updateSize();
                }
            }
        };
        update_thread.start();
    }

    /**
 * Store the resources of the editor itself.
 *
 * @exception IOException in case of an IOError.
 * @exception UnsupportedOperationException if the resources is not
 * capable of persistently storing the resources.
 */
    public void storeEditorResources() throws IOException, UnsupportedOperationException {
        Resources resources = ResourceEditorPanel.getResources();
        Point location = getLocation();
        Dimension dimension = getSize();
        resources.setInt(KEY_WINDOW_LOCATION_X, location.x);
        resources.setInt(KEY_WINDOW_LOCATION_Y, location.y);
        resources.setInt(KEY_WINDOW_DIMENSION_WIDTH, dimension.width);
        resources.setInt(KEY_WINDOW_DIMENSION_HEIGHT, dimension.height);
        ResourceEditorPanel.getResources().store();
    }

    /**
 * @param event the property change event.
 */
    public void propertyChange(PropertyChangeEvent event) {
        if (event.getSource() == ResourceEditorPanel.getResources()) {
            updateResources();
            invalidate();
            validate();
            repaint();
            return;
        }
        if (event.getSource() == UIManager.class) {
            SwingUtilities.updateComponentTreeUI(this);
            updateSize();
        }
    }

    /**
   * @param args the arguments, will be ignored!
   */
    public static void main(String[] args) {
        try {
            Resources edit_resources;
            if (args.length <= 0) {
                edit_resources = ResourceManager.getResources(ResourceEditorFrame.class, ResourceEditorPanel.RESOURCE_BOUNDLE_NAME, ResourceEditorPanel.RESOURCE_DIR_NAME, Locale.getDefault());
            } else {
                edit_resources = ResourceManager.getResources(Class.forName(args[0]), args[1], args[2], Locale.getDefault());
            }
            ResourceEditorFrame frame = new ResourceEditorFrame(edit_resources);
            frame.pack();
            frame.setVisible(true);
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }
}
