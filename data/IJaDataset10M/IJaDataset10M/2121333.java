package trstudio.blueboxalife.io.file;

import java.awt.Color;
import java.io.IOException;
import trstudio.blueboxalife.ai.Egg;
import trstudio.blueboxalife.ai.CreatureGender;
import trstudio.blueboxalife.ai.genome.Genetic;
import trstudio.blueboxalife.state.GameResourcePath;
import trstudio.classlibrary.util.ConvertHelper;

/**
 * Fichier oeuf.
 * Ce fichier ne peut pas être inclu dans un autre: il est forcement autonome.
 * <br /><br />
 *
 * Norme inspirée des spécifications de Creatures 2.
 * Lien vers le document utilisé : http://www.gamewaredevelopment.co.uk/cdn/cdn_more.php?CDN_article_id=95#egg
 * <br /><br />
 *
 * Format de fichier d'oeufs :
 * <br />
 * <code>
 * BYTE[4]		'e' 'g' 'g' 'f'		La spécification de fichier d'oeufs.
 * BYTE			CreatureGender				Le sexe de l'oeuf (0=aléatoire, 1=masculin, 2=féminin).
 *
 * LONG			Reserved			Doit être à zéro.
 *
 * INT[5]		RGB Color			5 couleurs (peau, peau secondaire, cheveux, yeux, yeux secondaire).
 *
 * LONG			Reserved			Doit être à zéro.
 *
 * BYTE[32]		Mum Moniker			Identifiant de la mère
 * BYTE[32]		Dad Moniker			Identifiant du père
 *
 * LONG			Reserved			Doit être à zéro.
 *
 * LONG			Description size	Taille de la description de l'oeuf (nombre de byte).
 * BYTE[]		Description			Description de l'oeuf. Si la taille de la description est 0, il n'y a aucune donnée ici.
 *
 * LONG			Reserved			Doit être à zéro.
 *
 * BYTE[]		Mum Genetics		Données génétique de la mère.
 * BYTE[]		Dad Genetics		Données génétique du père.
 * </code>
 *
 * @author Sebastien Villemain
 */
public class EggFile extends ApplicationFileAdapter {

    /**
	 * Représentation de l'oeuf.
	 */
    private Egg egg = null;

    /**
	 * Nouveau fichier d'oeuf.
	 *
	 * @param filename
	 */
    public EggFile(String filename) {
        super(GameResourcePath.DIRECTORY_EGGS + filename + ".egg", "eggf", true);
    }

    protected void readBuffer() throws IOException {
        egg = new Egg();
        readGender();
        stream.readReserved(8);
        readColors();
        stream.readReserved(8);
        readMonikerMum();
        readMonikerDad();
        stream.readReserved(8);
        readDescription();
        stream.readReserved(8);
        readGenetics();
    }

    /**
	 * Lecture du sexe de l'oeuf.
	 */
    private void readGender() {
        int gender = stream.readInt();
        egg.setGender(CreatureGender.valueOf(gender));
    }

    /**
	 * Lecture des couleurs mémorisées par l'oeuf.
	 */
    private void readColors() {
        byte[] RGBColors = new byte[Genetic.REPRESENTATIVE_PIGMENT_COLOR_LENGTH * 4];
        for (int i = 0; i < RGBColors.length; i++) {
            RGBColors[i] = stream.readByte();
        }
        Color[] colors = new Color[Genetic.REPRESENTATIVE_PIGMENT_COLOR_LENGTH];
        byte[] integerBuffer = new byte[4];
        for (int i = 0; i < colors.length; i++) {
            System.arraycopy(RGBColors, i * 4, integerBuffer, 0, 4);
            colors[i] = new Color(ConvertHelper.toInt(integerBuffer));
        }
        egg.setColors(colors);
    }

    /**
	 * Lecture du nom de la famille de la mère.
	 */
    private void readMonikerMum() {
        String moniker = stream.readString();
        egg.setMonikerSourceMum(moniker);
    }

    /**
	 * Lecture du nom de la famille du père.
	 */
    private void readMonikerDad() {
        String moniker = stream.readString();
        egg.setMonikerSourceDad(moniker);
    }

    /**
	 * Lecture de la description.
	 */
    private void readDescription() throws IOException {
        String description = stream.readString();
        if (description != null) {
            egg.setDescription(description);
        }
    }

    /**
	 * Lecture du code génétique.
	 */
    private void readGenetics() {
        GenFile genFile = new GenFile(file, false);
        genFile.stream.setInput(stream.getInput());
        genFile.load();
        egg.setGenesMum((Genetic[]) genFile.getObject().toArray());
        genFile.load();
        egg.setGenesDad((Genetic[]) genFile.getObject().toArray());
    }

    protected void writeBuffer() throws IOException {
        writeGender();
        stream.writeReserved(8);
        writeColors();
        stream.writeReserved(8);
        writeMonikerMum();
        writeMonikerDad();
        stream.writeReserved(8);
        writeDescription();
        stream.writeReserved(8);
        writeGenetics();
    }

    /**
	 * Ecriture du sexe de l'oeuf.
	 * Un entier de 0 à 2.
	 */
    private void writeGender() {
        stream.writeDataTyped(egg.getGender().ordinal());
    }

    /**
	 * Ecriture des couleurs dominantes.
	 * Couleur de type RGB (RED, GREEN, BLUE), donc sur un entier.
	 */
    private void writeColors() {
        Color[] colors = egg.getColors();
        byte[] RGBColors = new byte[Genetic.REPRESENTATIVE_PIGMENT_COLOR_LENGTH * 4];
        for (int i = 0; i < colors.length; i++) {
            System.arraycopy(ConvertHelper.toByte(colors[i].getRGB()), 0, RGBColors, i * 4, 4);
        }
        stream.writeDataTyped(RGBColors);
    }

    /**
	 * Ecriture du nom de la famille de la mère.
	 */
    private void writeMonikerMum() {
        stream.writeDataTyped(egg.getMonikerSourceMum());
    }

    /**
	 * Ecriture du nom de la famille du père.
	 */
    private void writeMonikerDad() {
        stream.writeDataTyped(egg.getMonikerSourceDad());
    }

    /**
	 * Ecriture de la description.
	 */
    private void writeDescription() {
        stream.writeDataTyped(egg.getDescription());
    }

    /**
	 * Ecriture du code génétique.
	 */
    public void writeGenetics() {
        GenFile genFile = new GenFile(file, false);
        genFile.stream.setOutput(stream.getOutput());
        genFile.setObject(egg.getGenesMum());
        genFile.save();
        genFile.setObject(egg.getGenesDad());
        genFile.save();
    }

    public Object getObject() {
        return egg;
    }

    public void setObject(Object object) {
        egg = (Egg) object;
    }
}
