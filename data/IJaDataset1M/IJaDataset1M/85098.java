package trstudio.blueboxalife.ai.genome.gene;

import trstudio.blueboxalife.ai.BodyPart;
import trstudio.blueboxalife.ai.genome.Genetic;
import trstudio.blueboxalife.ai.genome.GeneticTypes;
import trstudio.blueboxalife.io.file.ALifeStream;

/**
 * Gène d'apparence (représentation).
 *
 * Représentation en image d'une créature basé sur des morceaux de sprites.
 *
 * @author Sebastien Villemain
 */
public class RepAppearanceGene extends Genetic {

    /**
	 * Situation du corps, indique l'apparence du gène.
	 *
	 * @see BodyPart
	 */
    private byte part;

    /**
	 * Type de sprite (ex "Purple mountain, "Santa"...).
	 */
    private short partVariant;

    /**
	 * Type d'espèce.
	 *
	 * @see RepGenusGene
	 */
    private short partSpecies;

    public RepAppearanceGene() {
        super(GeneticTypes.REPRESENTATIVE.APPEARANCE);
    }

    public void readExternal(ALifeStream in) {
        part = in.readByte();
        partVariant = in.readShort();
        partSpecies = in.readShort();
        if (part < 0 || part >= BodyPart.values().length) {
            part = 0;
        }
    }

    public void writeExternal(ALifeStream out) {
        out.writeDataTyped(part);
        out.writeDataTyped(partVariant);
        out.writeDataTyped(partSpecies);
    }
}
