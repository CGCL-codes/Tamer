package de.plaetzinger.jgpsbabel.tasks;

import de.plaetzinger.jgpsbabel.model.FormatConfiguration;
import de.plaetzinger.jgpsbabel.model.GPSBabelCall;
import de.plaetzinger.jgpsbabel.model.OperationMode;
import de.plaetzinger.jgpsbabel.ui.JGPSBabelView;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdesktop.application.Application;

/**
 *
 * @author Christian Plaetzinger
 */
public class CallGPSBabelTask extends AbstractTask<Boolean, Void> {

    private final GPSBabelCall gpsBabelCall;

    public CallGPSBabelTask(final Application application, final GPSBabelCall gpsBabelCall) {
        super(application);
        this.gpsBabelCall = gpsBabelCall;
    }

    @Override
    protected Boolean doInBackground() throws Exception {
        Process process = null;
        StringBuilder command = new StringBuilder();
        command.append("gpsbabel ");
        addOperationModes(command);
        addFormatConfiguration(command, gpsBabelCall.getInputConfiguration());
        addFormatConfiguration(command, gpsBabelCall.getOutputConfiguration());
        try {
            process = Runtime.getRuntime().exec(command.toString());
            int returnValue = process.waitFor();
            if (returnValue == 0) {
                showMessage(resourceMap.getString("runGPSBabel.executeSuccess"));
            } else {
                showErrorMessage(resourceMap.getString("runGPSBabel.executeError"));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException ex) {
            Logger.getLogger(JGPSBabelView.class.getName()).log(Level.SEVERE, null, ex);
            showErrorMessage(resourceMap.getString("runGPSBabel.executeError"));
        } finally {
            if (null != process) {
                process.destroy();
            }
        }
        return false;
    }

    private void addFormatConfiguration(final StringBuilder command, final FormatConfiguration formatConfiguration) {
        command.append("-");
        command.append(formatConfiguration.getTypeParameter());
        command.append(" ");
        command.append(formatConfiguration.getFormat());
        command.append(" ");
        command.append("-");
        command.append(formatConfiguration.getFileParameter());
        command.append(" ");
        command.append(formatConfiguration.getFile());
        command.append(" ");
    }

    private void addOperationModes(final StringBuilder command) {
        for (OperationMode operationMode : gpsBabelCall.getOperatingModes()) {
            command.append("-");
            command.append(operationMode);
            command.append(" ");
        }
    }
}
