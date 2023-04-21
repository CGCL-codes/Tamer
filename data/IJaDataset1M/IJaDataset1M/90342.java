package mp3.commands;

import jcommand.command.CommandInterface;
import jcommand.outputProvider.OutputProviderInterface;
import mp3.services.Reporter;
import mp3.services.ServiceSetter;

/**
 *
 * @author user
 */
public class ExitCommand implements CommandInterface {

    private OutputProviderInterface output;

    @Override
    public void beforeExecute() {
        if (!((Reporter) ServiceSetter.getServiceSetter().getServiceByName(Reporter.class.getName())).sendData()) output.sendToErr(java.util.ResourceBundle.getBundle("Bundle").getString("Reporter.Error"));
    }

    @Override
    public void execute(String string) {
        System.exit(0);
    }

    @Override
    public void afterExecute() {
    }

    @Override
    public void setOutputProvider(OutputProviderInterface opi) {
        output = opi;
    }
}
