package net.sf.moviekebab.service;

import javax.swing.JFrame;

/**
 * @author Laurent Caillette
 */
public interface FrameSupervisor {

    void register(JFrame frame);

    void unregister(JFrame frame);
}
