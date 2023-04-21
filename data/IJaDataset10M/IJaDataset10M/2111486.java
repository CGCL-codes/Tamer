package org.jlense.uiworks.wizard;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import org.eclipse.core.runtime.IProgressMonitor;
import org.jlense.uiworks.resource.JFaceResources;
import org.jlense.uiworks.widget.WidgetUtils;
import org.jlense.util.Assert;

/**
 * A standard implementation of an IProgressMonitor. It consists
 * of a label displaying the task and subtask name, and a
 * progress indicator to show progress. In contrast to 
 * <code>ProgressMonitorDialog</code> this class only implements
 * <code>IProgressMonitor</code>.
 */
public class ProgressMonitorPart extends JPanel implements IProgressMonitor {

    protected JLabel fLabel;

    protected String fTaskName;

    protected JProgressBar fProgressIndicator;

    protected AbstractButton fCancelComponent;

    protected boolean fIsCanceled;

    protected ActionListener fCancelListener = new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            setCanceled(true);
            if (fCancelComponent != null) fCancelComponent.setEnabled(false);
        }
    };

    /**
     * Creates a ProgressMonitorPart.
     * @param parent The SWT parent of the part.
     * @param layout The SWT grid bag layout used by the part. A client
     * can supply the layout to control how the progress monitor part
     * is layed out. If null is passed the part uses its default layout.
     * @param progressIndicatorHeight The height of the progress indicator in pixel.
     */
    public ProgressMonitorPart() {
        super(new GridBagLayout());
        initialize();
    }

    /**
     * Attaches the progress monitor part to the given cancel
     * component. 
     */
    public void attachToCancelComponent(AbstractButton cancelComponent) {
        Assert.isNotNull(cancelComponent);
        fCancelComponent = cancelComponent;
        fCancelComponent.addActionListener(fCancelListener);
    }

    /**
     * Implements <code>IProgressMonitor.beginTask</code>.
     * @see IProgressMonitor#beginTask(java.lang.String, int)
     */
    public void beginTask(String name, int totalWork) {
        if (name == null) fTaskName = ""; else fTaskName = name;
        fLabel.setText(fTaskName);
        fProgressIndicator.setMinimum(0);
        fProgressIndicator.setMaximum(totalWork);
        fProgressIndicator.setValue(0);
    }

    /**
     * Implements <code>IProgressMonitor.done</code>.
     * @see IProgressMonitor#done()
     */
    public void done() {
        fLabel.setText("");
        fProgressIndicator.setValue(fProgressIndicator.getMaximum());
        fProgressIndicator.setMinimum(0);
        fProgressIndicator.setMaximum(0);
        fProgressIndicator.setValue(0);
    }

    /**
     * Escapes any occurrence of '&' in the given String so that
     * it is not considered as a mnemonic
     * character in SWT ToolItems, MenuItems, JButton and Labels.
     */
    protected static String escapeMetaCharacters(String in) {
        if (in == null || in.indexOf('&') < 0) return in;
        int length = in.length();
        StringBuffer out = new StringBuffer(length);
        for (int i = 0; i < length; i++) {
            char c = in.charAt(i);
            if (c == '&') out.append("&&"); else out.append(c);
        }
        return out.toString();
    }

    /**
     * Creates the progress monitor's UI parts and layouts them
     * according to the given layout. If the layou is <code>null</code>
     * the part's default layout is used.
     */
    protected void initialize() {
        fLabel = new JLabel();
        fLabel.setBorder(BorderFactory.createEmptyBorder());
        add(fLabel, WidgetUtils.createGridBagConstraints(0, 0, GridBagConstraints.HORIZONTAL));
        fProgressIndicator = new JProgressBar();
        fProgressIndicator.setBorder(BorderFactory.createEmptyBorder());
        add(fProgressIndicator, WidgetUtils.createGridBagConstraints(0, 1, GridBagConstraints.HORIZONTAL));
    }

    /**
     * Implements <code>IProgressMonitor.internalWorked</code>.
     * @see IProgressMonitor#internalWorked(double)
     */
    public void internalWorked(double work) {
        fProgressIndicator.setValue((int) work);
    }

    /**
     * Implements <code>IProgressMonitor.isCanceled</code>.
     * @see IProgressMonitor#isCanceled()
     */
    public boolean isCanceled() {
        return fIsCanceled;
    }

    /**
     * Detached the progress monitor part to the given cancel
     * component
     */
    public void removeFromCancelComponent(JComponent cc) {
        Assert.isTrue(fCancelComponent == cc && fCancelComponent != null);
        fCancelComponent.removeActionListener(fCancelListener);
        fCancelComponent = null;
    }

    /**
     * Implements <code>IProgressMonitor.setCanceled</code>.
     * @see IProgressMonitor#setCanceled(boolean)
     */
    public void setCanceled(boolean b) {
        fIsCanceled = b;
    }

    /**
     * Sets the progress monitor part's font.
     */
    public void setFont(Font font) {
        super.setFont(font);
        if (fLabel != null) fLabel.setFont(font);
        if (fProgressIndicator != null) fProgressIndicator.setFont(font);
    }

    /**
     * @see IProgressMonitor#setTaskName(java.lang.String)
     */
    public void setTaskName(String name) {
        fTaskName = name;
    }

    /**
     * Implements <code>IProgressMonitor.subTask</code>.
     * @see IProgressMonitor#subTask(java.lang.String)
     */
    public void subTask(String name) {
        if (name == null) name = "";
        String text;
        if (fTaskName != null && fTaskName.length() > 0) text = JFaceResources.format("Set_SubTask", new Object[] { fTaskName, name }); else text = name;
        fLabel.setText(escapeMetaCharacters(text));
    }

    /**
     * Implements <code>IProgressMonitor.worked</code>.
     * @see IProgressMonitor#worked(int)
     */
    public void worked(int work) {
        internalWorked(work);
    }
}
