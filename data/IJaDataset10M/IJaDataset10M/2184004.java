package fca.gui.util;

import java.awt.Color;
import java.util.Vector;
import fca.gui.util.constant.LMColors.LatticeColor;

/**
 * Ensemble ordonn� de couleurs
 * @author Genevi�ve Roberge
 * @author Ludovic Thomas
 * @version 1.0
 */
public class ColorSet {

    /**
	 * Position de la derni�re couleur obtenue
	 */
    private int currentIndex;

    /**
	 * Constructeur d'nsemble ordonn� de couleurs
	 */
    public ColorSet() {
        currentIndex = -1;
    }

    /**
	 * Permet de faire revenir l'ensemble de couleur une couleur avant la couleur courante
	 */
    public void backOneColor() {
        if (currentIndex > -1) currentIndex--;
    }

    /**
	 * Permet d'obtenir la prochaine couleur disponible dans l'ensemble
	 * @return La Color contenant la prochaine couleur disponible
	 */
    public Color getNextColor() {
        currentIndex++;
        return getColorAt(currentIndex);
    }

    /**
	 * Permet d'obtenir la prochaine couleur disponible dans l'ensemble sauf la couleur par d�faut
	 * @param color la couleur actuelle
	 * @return La Color contenant la prochaine couleur disponible
	 */
    public static LatticeColor getNextColor(LatticeColor color) {
        Vector<LatticeColor> colorStrings = LatticeColor.getLatticeColors();
        int position = colorStrings.indexOf(color);
        if (position != -1) {
            int index = (position + 1) % (colorStrings.size());
            if (index == 0) index = 1;
            return colorStrings.elementAt(index);
        }
        return LatticeColor.DEFAULT;
    }

    /**
	 * Permet de conna�tre la couleur � une position donn�e de l'ensemble
	 * @param targetIndex Le int contenant la position pour laquelle la couleur est recherch�e
	 * @return la couleur a la position recherch�e
	 */
    public static Color getColorAt(int targetIndex) {
        Vector<Color> colors = LatticeColor.getColorsValues();
        int index = targetIndex % (colors.size());
        return colors.elementAt(index);
    }

    /**
	 * Permet de conna�tre la couleur (en caracteres) � une position donn�e de l'ensemble
	 * @param targetIndex Le int contenant la position pour laquelle la couleur est recherch�e
	 * @return La String contenant la couleur � la position demand�e
	 */
    public static LatticeColor getColorStringAt(int targetIndex) {
        Vector<LatticeColor> colorStrings = LatticeColor.getLatticeColors();
        int index = targetIndex % (colorStrings.size());
        return colorStrings.elementAt(index);
    }
}
