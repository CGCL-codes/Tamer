package mimosa.scheduler.ui;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.MessageFormat;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import mimosa.scheduler.DefaultEntity;
import mimosa.scheduler.Entity;
import mimosa.scheduler.IllegalParameterException;
import mimosa.scheduler.IllegalProbeException;
import mimosa.scheduler.descr.ProbeDescriptor;

/**
 * An illegal probe dialog is used when an illegal probe exception occurred.
 *
 * @author Jean-Pierre Muller
 */
@SuppressWarnings("serial")
public class IllegalProbeDialog extends ExceptionDialog implements ActionListener {

    private JButton defineButton;

    private JButton cancelButton;

    private int type;

    private String probe;

    private ProbeDescriptor probeType;

    private Entity sourceEntity;

    private Object value;

    public IllegalProbeDialog(IllegalProbeException exception) {
        super();
        type = exception.getError();
        sourceEntity = exception.getEntity();
        probe = exception.getProbe();
        probeType = exception.getProbeDescriptor();
        value = exception.getValue();
        setTitle("Error in " + sourceEntity);
        Container content = getContentPane();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        JLabel message = null;
        if (type == IllegalProbeException.UNKNOWNPROBE) {
            message = new JLabel(MessageFormat.format(Messages.getString("IllegalProbeDialog.1"), sourceEntity, probe));
        } else if (type == IllegalProbeException.ILLEGALCARDINALITY) {
            message = new JLabel(MessageFormat.format(Messages.getString("IllegalProbeDialog.2"), sourceEntity, probe, probeType.getCardinality()));
        } else if (type == IllegalProbeException.ILLEGALTYPE) {
            message = new JLabel(MessageFormat.format(Messages.getString("IllegalProbeDialog.3"), sourceEntity, probe, probeType.getType(), value));
        }
        message.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(message);
        JPanel pane = new JPanel();
        pane.setLayout(new BoxLayout(pane, BoxLayout.X_AXIS));
        pane.add(defineButton = new JButton(Messages.getString("IllegalProbeDialog.4")));
        pane.add(cancelButton = new JButton(Messages.getString("IllegalProbeDialog.5")));
        if (exception.getError() != IllegalParameterException.UNKNOWNPARAMETER) defineButton.setEnabled(false);
        defineButton.addActionListener(this);
        cancelButton.addActionListener(this);
        pane.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(pane);
    }

    /**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == defineButton) {
            setVisible(false);
            if (type == IllegalProbeException.UNKNOWNPROBE) {
                ((DefaultEntity) sourceEntity).addProbe(probe);
            }
        } else if (e.getSource() == cancelButton) {
            setVisible(false);
        }
    }

    /**
	 * This method displays a dialog to enter a new named object definition.
	 * @param newDialog The dialog to show.
	 */
    public static void showNewDialog(IllegalProbeDialog newDialog) {
        newDialog.pack();
        newDialog.setVisible(true);
        newDialog.dispose();
    }
}
