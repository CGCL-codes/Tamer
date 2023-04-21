package org.speech.asr.gui.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.richclient.application.support.AbstractView;
import javax.swing.*;

/**
 * //@todo class description
 * <p/>
 * Creation date: Apr 16, 2009 <br/>
 *
 * @author Lukasz Olczak
 * @since 1.0
 */
public class InitialView extends AbstractView {

    /**
   * slf4j Logger.
   */
    private static final Logger log = LoggerFactory.getLogger(InitialView.class.getName());

    protected JComponent createControl() {
        JPanel panel = new JPanel();
        return panel;
    }
}
