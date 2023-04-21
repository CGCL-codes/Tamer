package pulsarhunter.processes;

import java.util.Hashtable;
import pulsarhunter.Data;
import pulsarhunter.GlobalOptions;
import pulsarhunter.ProcessCreationException;
import pulsarhunter.ProcessFactory;
import pulsarhunter.PulsarHunterProcess;
import pulsarhunter.PulsarHunterRegistry;
import pulsarhunter.datatypes.PulsarHunterCandidate;

/**
 *
 * @author mkeith
 */
public class DisplayPHCFFactory implements ProcessFactory {

    /** Creates a new instance of DiplayPHCFFactory */
    public DisplayPHCFFactory() {
    }

    public PulsarHunterProcess createProcess(String[] params, Hashtable<String, Data> dataFiles, PulsarHunterRegistry reg) throws ProcessCreationException {
        if (params.length < 2) throw new ProcessCreationException("Too few arguments to create process " + this.getName());
        Data dat = dataFiles.get(params[1]);
        if (!(dat instanceof PulsarHunterCandidate)) throw new ProcessCreationException("Argument 1 must be a PulsarHunterCandidate for process " + this.getName());
        PulsarHunterCandidate phcf = (PulsarHunterCandidate) dat;
        boolean makeImage = false;
        Object opt = reg.getOptions().getArg(GlobalOptions.Option.imageoutput);
        if (opt != null) {
            makeImage = ((Boolean) opt).booleanValue();
        }
        return new DisplayPHCF(phcf, makeImage);
    }

    public String getName() {
        return "PHCFPlotter";
    }
}
