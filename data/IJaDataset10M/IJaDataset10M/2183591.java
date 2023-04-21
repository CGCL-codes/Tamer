package cz.cuni.amis.rapidminer.operator.hmm.io;

import be.ac.ulg.montefiore.run.jahmm.Hmm;
import be.ac.ulg.montefiore.run.jahmm.ObservationInteger;
import be.ac.ulg.montefiore.run.jahmm.io.HmmReader;
import be.ac.ulg.montefiore.run.jahmm.io.OpdfIntegerReader;
import com.rapidminer.example.Attribute;
import com.rapidminer.example.table.MemoryExampleTable;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.io.AbstractReader;
import com.rapidminer.parameter.ParameterType;
import com.rapidminer.parameter.ParameterTypeFile;
import cz.cuni.amis.rapidminer.operator.hmm.model.HMMModel;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Reads HMM from native JAHMM file format.
 * @author ik
 */
public class JAHMMReader extends AbstractReader<HMMModel> {

    /** The parameter name for &quot;Filename containing the model to load.&quot; */
    public static final String PARAMETER_MODEL_FILE = "model_file";

    public JAHMMReader(OperatorDescription od) {
        super(od, HMMModel.class);
    }

    @Override
    public HMMModel read() throws OperatorException {
        Reader reader = null;
        try {
            reader = new FileReader(getParameterAsString(PARAMETER_MODEL_FILE));
            Hmm<ObservationInteger> hmm = HmmReader.read(reader, new OpdfIntegerReader());
            return new HMMModel(hmm, new MemoryExampleTable((Attribute) null).createExampleSet(), null);
        } catch (Exception ex) {
            throw new OperatorException("Model loading error.", ex);
        } finally {
            try {
                reader.close();
            } catch (IOException ex) {
                Logger.getLogger(JAHMMReader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public List<ParameterType> getParameterTypes() {
        List<ParameterType> types = super.getParameterTypes();
        types.add(new ParameterTypeFile(PARAMETER_MODEL_FILE, "Filename containing the model to load.", "mod", false));
        return types;
    }
}
