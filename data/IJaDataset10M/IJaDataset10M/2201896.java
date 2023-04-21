package shag.activity;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

/**
 * ActivityProgressPanel is a view of a prticular ActivityTask.  It will
 * attempt to display the current number of intermediate results that have
 * returned.  It also provides a method for cancelling actions if appropriate.
 * 
 * @author   zac
 * @version  $Revision: 1.6 $ $Date: 2005/06/17 21:10:51 $
 */
public class ActivityProgressPanel extends JPanel implements ActivityUpdateListener {

    public ActivityProgressPanel(final ActivityTask<?> at) {
        this();
        setActivityTask(at);
    }

    /**
     * Private constructor, so that we can create a "disconnected" panel for
     * size standardization purposes.
     */
    private ActivityProgressPanel() {
        _cancelButton = new JButton("Cancel");
        _cancelAction = new CancelActionListener();
        _cancelButton.addActionListener(_cancelAction);
        _titleLabel = new JLabel("Task");
        _descriptionLabel = new JLabel(BASIC_QUEUED_MESSAGE);
        _progressBar = new JProgressBar();
        _cancelButton.setEnabled(false);
        layoutComponents();
    }

    @SuppressWarnings("unchecked")
    public void setActivityTask(final ActivityTask at) {
        _task = at;
        if (_task != null) ActivityObserver.getInstance().addActivityUpdateListener(this);
        _titleLabel.setText((_task != null) ? _task.getActivityName() : "");
        _cancelButton.setEnabled(_task != null && _task.isCancellable());
    }

    private void quit() {
        ActivityObserver.getInstance().removeActivityUpdateListener(this);
        _task = null;
        repaint();
    }

    /**
     * Layout the persistent components in the panel.
     */
    private void layoutComponents() {
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets.right = 10;
        add(createProgressPanel(), gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets.right = 0;
        add(createButtonPanel(), gbc);
    }

    private JPanel createProgressPanel() {
        final JPanel result = new JPanel(new BorderLayout(0, 5));
        result.add(_titleLabel, BorderLayout.NORTH);
        result.add(_progressBar, BorderLayout.CENTER);
        result.add(_descriptionLabel, BorderLayout.SOUTH);
        return result;
    }

    private JPanel createButtonPanel() {
        final JPanel result = new JPanel(new BorderLayout(0, 5));
        result.add(_cancelButton, BorderLayout.CENTER);
        return result;
    }

    /**
     * Substitutes the __current__ and __max__ tokens into their corresponding
     * values, and transforms any embedded newlines in the message text into
     * Swing-ready HTML.
     */
    private String getFormattedMessage(String message, final int current, final int max) {
        message = message.replaceAll("__current__", Integer.toString(current)).replaceAll("__max__", Integer.toString(max));
        if ((message.toLowerCase().indexOf("<html>") == -1) && (message.indexOf('\n') >= 0)) {
            message = message.replaceAll("/", "&#47;");
            StringBuffer sb = new StringBuffer("<html>");
            sb.append(message.replaceAll("\n", "<br>"));
            sb.append("</html>");
            message = sb.toString();
        }
        return message;
    }

    /**
     * @see shag.activity.ActivityUpdateListener#activityUpdate()
     */
    public void activityUpdate() {
        if (_task == null || _task.isDone() || _task.isCancelled()) {
            quit();
            return;
        }
        _cancelButton.setEnabled(_task.isCancellable());
        _titleLabel.setText(_task.getActivityName());
        if (!_task.hasStarted()) {
            _descriptionLabel.setText(BASIC_QUEUED_MESSAGE);
        } else if (_task.getMaxResults() < 2) {
            _descriptionLabel.setText(BASIC_INDETERMINATE_DESC);
            _progressBar.setIndeterminate(true);
        } else {
            _progressBar.setIndeterminate(false);
            _progressBar.setMaximum(_task.getMaxResults());
            int size = _task.getPartialResults().size();
            _progressBar.setValue(size);
            _descriptionLabel.setText(getFormattedMessage(BASIC_LOADING_DESC, size, _task.getMaxResults()));
        }
        repaint();
    }

    /**
     * "Template" panel that determines default width and height.
     */
    private static final ActivityProgressPanel TEMPLATE = new ActivityProgressPanel();

    public static final String BASIC_QUEUED_MESSAGE = "Waiting for busy resources...";

    public static final String BASIC_INDETERMINATE_DESC = "Querying the server...";

    public static final String BASIC_LOADING_DESC = "__current__ out of __max__ objects loaded.";

    public static final Integer WIDTH = TEMPLATE.getPreferredSize().width;

    public static final Integer HEIGHT = TEMPLATE.getPreferredSize().height;

    private final JButton _cancelButton;

    private final JLabel _titleLabel;

    private final JLabel _descriptionLabel;

    private final JProgressBar _progressBar;

    private final CancelActionListener _cancelAction;

    private ActivityTask<?> _task;

    private class CancelActionListener implements ActionListener {

        public void actionPerformed(ActionEvent ae) {
            _task.cancel(true);
            quit();
        }
    }
}
