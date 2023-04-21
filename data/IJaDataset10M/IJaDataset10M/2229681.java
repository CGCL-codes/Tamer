package net.sf.refactorit.ui.panel;

import javax.swing.JComponent;

/**
 * If classes who want to be notified on events occurring in ResultArea class
 * objects then they should implement this interface and register themselves
 * via addResultAreaListener(...) method of ResultArea, for notifications
 * about events inside ResultArea.
 *
 * For example BinPane registers itself for notifications, because it is
 * interested when the content (JComponent) changes inside ResultArea, so
 * it can then replace the JComponent (setViewportView(this.component.getUI());)
 * to display a changed content in its ViewPort.
 *
 * @author  Jaanek Oja
 */
public interface ResultAreaListener {

    /**
   * It is called when the JComponent inside ResultArea is replaced with
   * another one. So, the container who is responsible for displaying
   * it can be notified on this event.
   *
   * @param currentContent JComponent instance that was replaced by
   * a new one.
   */
    void contentChanged(JComponent currentContent);

    /**
   * It is called when a new generation of project has been performed.
   * So the listeners are notified with this method to take actions against it.
   *
   * @param component which notifies about the rebuild on project it holds.
   */
    void rebuildPerformed(ResultArea component);

    /**
   * It is called when a new project generation is going to be rebuilt by the
   * Project instance.
   *
   * Implement this function to release any resources you have against the old
   * project generations. For example, release direct/indirect references to
   * old project, so it could be garbage collected.
   *
   * @param component which notifies about the start of a new project generation.
   */
    void rebuildStarted(ResultArea component);

    /**
   * It is called when processing error occurs inside ResultArea.
   *
   * For example when ReRun is in process, or when the refresh doesn't
   * succeed.
   *
   * @param error the error JComponent instance that listeners should
   * show to the user in that case.
   */
    void errorOccurred(String error);
}
