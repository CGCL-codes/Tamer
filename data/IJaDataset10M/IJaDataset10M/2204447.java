package org.formaria.swing;

import org.formaria.swing.video.VideoController;
import java.awt.BorderLayout;
import java.io.IOException;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import org.formaria.aria.Project;
import org.formaria.aria.ProjectManager;

/**
 * Video Playback Control
 *
 * <p> Copyright (c) Formaria Ltd., 2008, This software is licensed under
 * the GNU Public License (GPL), please see license.txt for more details. If
 * you make commercial use of this software you must purchase a commercial
 * license from formaria.</p>
 * <p> $Revision: 1.14 $</p>
 */
public class Video extends JComponent implements ComponentListener {

    private VideoController controller;

    private boolean autoLoop = false;

    private boolean autoStart = false;

    private boolean showController = true;

    private JLayeredPane layeredPane;

    private JPanel videoPanel;

    private java.awt.Panel componentPanel;

    private String fileName;

    /**
   * The owner project and the context in which this object operates.
   */
    protected Project currentProject = ProjectManager.getCurrentProject();

    /**
   * Create a new video component
   */
    public Video() {
        setLayout(new BorderLayout());
        layeredPane = new JLayeredPane();
        videoPanel = new JPanel();
        videoPanel.setLayout(new BorderLayout());
        videoPanel.setDoubleBuffered(true);
        layeredPane.add(videoPanel, new Integer(1));
        addComponentListener(this);
        super.add(layeredPane, BorderLayout.CENTER);
        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
   * Render the component
   * @param g the graphics context
   */
    public void paintComponent(Graphics g) {
        int w = getSize().width - 1;
        int h = getSize().height - 1;
        g.setColor(Color.blue);
        g.drawRect(0, 0, w, h);
        g.drawLine(w / 10, 0, w / 10, h);
        g.drawLine(9 * w / 10, 0, 9 * w / 10, h);
        for (int i = 0; i < 10; i++) {
            g.drawLine(0, i * h / 10, w / 10, i * h / 10);
            g.drawLine(9 * w / 10, i * h / 10, w, i * h / 10);
        }
    }

    /**
   * Performs any post creation initialisation of the control.
   * @throws java.io.IOException problems setting up the video
   */
    public void init() throws IOException {
        try {
            controller = (VideoController) getClass().forName("org.formaria.swing.video.ControllerListener").newInstance();
            controller.setVideoPanel(videoPanel);
            setShowController(showController);
            if (autoLoop) loop();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
   * Destroy the video controller and release its resources
   */
    public void destroy() {
        if (controller != null) controller.destroy();
    }

    /**
   * Get the loop flag
   * @return true if the video loops upon completion
   */
    public boolean getLoop() {
        return autoLoop;
    }

    /**
   * Set the loop flag
   * @param value true if the video loops upon completion
   */
    public void setLoop(boolean value) {
        autoLoop = value;
    }

    /**
   * Gets the name of the video or sound to play.
   * @return the name of the video to play.
   */
    public String getValue() {
        return fileName;
    }

    /**
   * Sets the name of the video or sound to play.
   * @param _fileName name of the video to play.
   */
    public void setValue(String _fileName) {
        fileName = _fileName;
        stop();
        try {
            init();
        } catch (Exception e) {
        }
        if (controller != null) controller.setFile(currentProject.findResource(_fileName).toString());
        setShowController(showController);
        if (autoStart) start();
    }

    /**
   * Set and display the video controller
   * @param display true to display the controller
   */
    public void setShowController(boolean display) {
        if (controller != null) controller.showController(display);
        showController = display;
    }

    /**
   * Gets the show video controller flag
   * @return true if the playback controller is displayed
   */
    public boolean getShowController() {
        return showController;
    }

    /**
   * Set the autostart flag
   * @param state true to autostart the video on display
   */
    public void setAutoStart(boolean state) {
        autoStart = state;
    }

    /**
   * Get the autostart flag
   * @return the current flag value
   */
    public boolean getAutoStart() {
        return autoStart;
    }

    /**
   * Starts playing the video
   */
    public void start() {
        if (controller != null) controller.start();
    }

    /**
   * Continuous playback of the video.
   */
    public void loop() {
        if (controller != null) controller.loop();
    }

    /**
   * Stops playback of the video.
   */
    public void stop() {
        if (controller != null) controller.stop();
    }

    /**
    * Add a component to the components layer. Due to limitations in JMF a heavy
    * weight component is created for video playback. Therefore a heavyweight
    * host for added components needs to be added or the video will obscure those
    * components. The first component added to the video should be a panel
    * @param c the component to add
    * @return the added component
    */
    public Component add(Component c) {
        setupComponentPanel(c);
        return componentPanel.add(c);
    }

    /**
    * Add a component to the components layer. Due to limitations in JMF a heavy
    * weight component is created for video playback. Therefore a heavyweight
    * host for added components needs to be added or the video will obscure those
    * components. The first component added to the video should be a panel
    * @param c the component to add
    * @param constraint the layout constraints
    */
    public void add(Component c, Object constraint) {
        setupComponentPanel(c);
        componentPanel.add(c, constraint);
    }

    /**
    * Setup a heavy weight component to host the other components
    */
    private void setupComponentPanel(Component c) {
        if (componentPanel == null) {
            componentPanel = new java.awt.Panel();
            componentPanel.setLayout(new BorderLayout());
            layeredPane.add(componentPanel, new Integer(10));
            componentPanel.add(c, BorderLayout.CENTER);
            componentPanel.setBounds(c.getBounds());
        }
    }

    /**
   * Invoked when the component's size changes.
   * @param e the resize event
   */
    public void componentResized(ComponentEvent e) {
        Dimension sz = getSize();
        videoPanel.setBounds(0, 0, sz.width, sz.height);
        videoPanel.doLayout();
        if (componentPanel != null) componentPanel.doLayout();
        videoPanel.repaint();
        if (componentPanel != null) componentPanel.repaint();
    }

    /**
   * Invoked when the component's position changes.
   * @param e the resize event
   */
    public void componentMoved(ComponentEvent e) {
        videoPanel.repaint();
        if (componentPanel != null) componentPanel.repaint();
    }

    /**
   * Invoked when the component has been made visible.
   * @param e the resize event
   */
    public void componentShown(ComponentEvent e) {
        videoPanel.repaint();
        if (componentPanel != null) componentPanel.repaint();
    }

    /**
   * Invoked when the component has been made invisible.
   * @param e the resize event
   */
    public void componentHidden(ComponentEvent e) {
    }

    public void setAutoLoop(boolean autoLoop) {
        this.autoLoop = autoLoop;
    }
}
