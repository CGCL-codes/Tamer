package trstudio.blueboxalife.ai.genome.genes;

import java.io.Serializable;
import trstudio.blueboxalife.ai.genome.Genetic;
import trstudio.blueboxalife.ai.genome.GeneticTypes;
import trstudio.blueboxalife.utils.SimpleBufferStream;

/**
 * Gène de la démarche (description du déplacement) (représentation).
 * 
 * @author Sebastien Villemain
 */
public class RepGaitGene extends Genetic implements Serializable {

    /**
	 * Identifiant de la démarche (ex. normal, specifique).
	 */
    private short drive;

    /**
	 * Séquence de la démarche.
	 * Par exemple, un séquence de démarche pour "ivre" sera décalé et répété:
	 * <code>52 52 53 54 52 55 53 54</code>.
	 */
    private short[] poseSequence = new short[8];

    public RepGaitGene() {
        super(GeneticTypes.REPRESENTATIVE.GAIT);
    }

    public int getSize() {
        return 9 * 2;
    }

    public void read(SimpleBufferStream stream) {
        drive = stream.readShort();
        for (int i = 0; i < poseSequence.length; i++) {
            poseSequence[i] = stream.readShort();
        }
    }

    public SimpleBufferStream write() {
        SimpleBufferStream stream = new SimpleBufferStream(getSize());
        stream.writeShort(drive);
        for (int i = 0; i < poseSequence.length; i++) {
            stream.writeShort(poseSequence[i]);
        }
        return stream;
    }
}
