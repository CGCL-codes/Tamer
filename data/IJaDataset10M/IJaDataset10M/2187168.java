package jsave.process;

import java.io.IOException;
import jsave.conf.Configuration;
import jsave.services.JSaveServices;
import jsave.util.Console;

public class GenerateJTreeProcess extends JSaveProcess {

    public GenerateJTreeProcess(Configuration conf) {
        super(conf);
    }

    @Override
    public void execute() {
        if (conf.isActif()) {
            conf.getChrono().start();
            try {
                JSaveServices.generateJTree(conf.getDestination());
            } catch (IOException e) {
                log.error("ERREUR pendant la generation du JTree: " + e.getMessage(), e);
            }
            Console.logParcours("");
            conf.getChrono().stop();
            conf.afficherResume();
            conf.desactiverSauvegarde();
        }
    }
}
