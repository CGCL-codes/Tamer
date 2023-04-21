package org.jfree.report.modules.gui.swing.preview;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import org.jfree.report.flow.ReportJob;
import org.jfree.report.modules.gui.common.IconTheme;
import org.jfree.report.modules.gui.swing.common.JStatusBar;
import org.jfree.report.modules.gui.swing.common.ReportProgressBar;

/**
 * Creation-Date: 11.11.2006, 19:35:09
 *
 * @author Thomas Morgner
 */
public class PreviewFrame extends JFrame {

    private class PreviewPanePropertyChangeHandler implements PropertyChangeListener {

        public PreviewPanePropertyChangeHandler() {
        }

        /**
     * This method gets called when a bound property is changed.
     *
     * @param evt A PropertyChangeEvent object describing the event source and
     *            the property that has changed.
     */
        public void propertyChange(PropertyChangeEvent evt) {
            final String propertyName = evt.getPropertyName();
            if (PreviewPane.MENU_PROPERTY.equals(propertyName)) {
                final JMenu[] menus = previewPane.getMenu();
                if (menus != null && menus.length > 0) {
                    final JMenuBar menuBar = new JMenuBar();
                    for (int i = 0; i < menus.length; i++) {
                        final JMenu menu = menus[i];
                        menuBar.add(menu);
                    }
                    setJMenuBar(menuBar);
                } else {
                    setJMenuBar(null);
                }
                return;
            }
            if (PreviewPane.TITLE_PROPERTY.equals(propertyName)) {
                setTitle(previewPane.getTitle());
                return;
            }
            if (PreviewPane.STATUS_TEXT_PROPERTY.equals(propertyName) || PreviewPane.STATUS_TYPE_PROPERTY.equals(propertyName)) {
                statusBar.setStatus(previewPane.getStatusType(), previewPane.getStatusText());
                return;
            }
            if (PreviewPane.ICON_THEME_PROPERTY.equals(propertyName)) {
                statusBar.setIconTheme(previewPane.getIconTheme());
                return;
            }
            if (PreviewPane.PAGINATING_PROPERTY.equals(propertyName)) {
                if (Boolean.TRUE.equals(evt.getNewValue())) {
                    progressBar.setVisible(true);
                    pageLabel.setVisible(false);
                } else {
                    progressBar.setVisible(false);
                    pageLabel.setVisible(true);
                }
                progressBar.revalidate();
                return;
            }
            if (PreviewPane.PAGE_NUMBER_PROPERTY.equals(propertyName) || PreviewPane.NUMBER_OF_PAGES_PROPERTY.equals(propertyName)) {
                pageLabel.setText(previewPane.getPageNumber() + "/" + previewPane.getNumberOfPages());
                return;
            }
            if (PreviewPane.CLOSED_PROPERTY.equals(propertyName)) {
                if (previewPane.isClosed()) {
                    setVisible(false);
                    dispose();
                } else {
                    setVisible(true);
                }
            }
        }
    }

    private PreviewPane previewPane;

    private JStatusBar statusBar;

    private ReportProgressBar progressBar;

    private JLabel pageLabel;

    /**
   * Creates a non-modal dialog without a title and without a specified
   * <code>Frame</code> owner.  A shared, hidden frame will be set as the owner
   * of the dialog.
   * <p/>
   * This constructor sets the component's locale property to the value returned
   * by <code>JComponent.getDefaultLocale</code>.
   *
   * @throws java.awt.HeadlessException if GraphicsEnvironment.isHeadless()
   *                                    returns true.
   * @see java.awt.GraphicsEnvironment#isHeadless
   * @see javax.swing.JComponent#getDefaultLocale
   */
    public PreviewFrame() {
        init();
    }

    protected void init() {
        previewPane = new PreviewPane();
        statusBar = new JStatusBar(previewPane.getIconTheme());
        progressBar = new ReportProgressBar();
        progressBar.setVisible(false);
        pageLabel = new JLabel();
        previewPane.addPropertyChangeListener(new PreviewPanePropertyChangeHandler());
        final JComponent extensionArea = statusBar.getExtensionArea();
        extensionArea.setLayout(new BoxLayout(extensionArea, BoxLayout.X_AXIS));
        extensionArea.add(progressBar);
        extensionArea.add(pageLabel);
        JComponent contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(previewPane, BorderLayout.CENTER);
        contentPane.add(statusBar, BorderLayout.SOUTH);
        setContentPane(contentPane);
    }

    public ReportController getReportController() {
        return previewPane.getReportController();
    }

    public void setReportController(final ReportController reportController) {
        previewPane.setReportController(reportController);
    }

    public IconTheme getIconTheme() {
        return previewPane.getIconTheme();
    }

    public void setIconTheme(final IconTheme theme) {
        previewPane.setIconTheme(theme);
    }

    public ReportJob getReportJob() {
        return previewPane.getReportJob();
    }

    public void setReportJob(final ReportJob reportJob) {
        previewPane.setReportJob(reportJob);
    }
}
