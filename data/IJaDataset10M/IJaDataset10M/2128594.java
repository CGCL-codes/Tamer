package trstudio.blueboxalife.ai.genome.gene;

import trstudio.blueboxalife.ai.genome.Genetic;
import trstudio.blueboxalife.ai.genome.GeneticTypes;
import trstudio.blueboxalife.io.file.ALifeStream;

/**
 * Gène de l'instinct (représentation).
 *
 * Un gène instinct permet la formation des réseaux neuronaux du cerveau d'une créature.
 * La créature va réagir d'une certaine façon dans certaines situations.
 *
 * <br /><br />
 *
 * Utilisé, en autre, pour fournir un comportement par défaut pour des choses qui sont très importants pour la créature.
 * Par exemple, un verbe pour répondre à l'utilisateur.
 * Ce gène ne fournit pas un comportement qui se finalise toujours.
 *
 * <br /><br />
 * Il est important que la créature dorme de façon periodique afin que les instincts soient constamment renforcées.
 * Pendant ce temps, les instincts des lobes et des neurones définis dans le gène sont traités comme si la créature avait effectué cette action.
 * Par exemple, le gène de l'instinct qui répond au verbe "venir" à le même effet que si la créature avait décidé d'elle même de venir.
 *
 * @author Sebastien Villemain
 */
public class RepInstinctGene extends Genetic {

    /**
	 * Décrit le lobe (ex. "verbe", "attention"...).
	 *
	 * @see Genetic#BIOCHIMIC_NEURO_LOBE_LENGTH
	 */
    private short[] lobes = new short[Genetic.BIOCHIMIC_NEURO_LOBE_LENGTH];

    /**
	 * Défini les conditions pour que l'instinct soit activé.
	 *
	 * @see Genetic#BIOCHIMIC_NEURO_LOBE_LENGTH
	 */
    private short[] neurons = new short[Genetic.BIOCHIMIC_NEURO_LOBE_LENGTH];

    /**
	 * Les mesures prises par la créature, en réponse à la situation décrit par les lobes et les cellules.
	 * Par exemple, "Déplacer", "Activer", "Manger"...
	 */
    private short action;

    /**
	 * Le produit chimique utilisé pour récompenser ou punir la créature.
	 * Cela validera ou sanctionnera le schéma : situation (lobes + cellules) = action.
	 */
    private short reinforcementDrive;

    /**
	 * Le montant pour valider la situation en cas de récompense.
	 */
    private short reinforcementLevel;

    public RepInstinctGene() {
        super(GeneticTypes.REPRESENTATIVE.INSTINCT);
    }

    public void readExternal(ALifeStream in) {
        for (int i = 0; i < Genetic.BIOCHIMIC_NEURO_LOBE_LENGTH; i++) {
            lobes[i] = in.readShort();
            neurons[i] = in.readShort();
        }
        action = in.readShort();
        reinforcementDrive = in.readShort();
        reinforcementLevel = in.readShort();
    }

    public void writeExternal(ALifeStream out) {
        for (int i = 0; i < Genetic.BIOCHIMIC_NEURO_LOBE_LENGTH; i++) {
            out.writeDataTyped(lobes[i]);
            out.writeDataTyped(neurons[i]);
        }
        out.writeDataTyped(action);
        out.writeDataTyped(reinforcementDrive);
        out.writeDataTyped(reinforcementLevel);
    }
}
