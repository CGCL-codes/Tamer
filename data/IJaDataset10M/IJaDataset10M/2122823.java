package org.fudaa.fudaa.sinavi3;

/**
 * Classe qui d�crit les m�thodes de divisions des donn�es en sorties du noyau de calcul pour retrouver des r�sultats sur une simulation 
 * et non plus sur 6.
 *@version $Version$
 * @author hadoux
 *
 */
public class FonctionsSimu {

    /**
	 * Nombre de simulations r�alis�es pour un lancement de calcul.
	 * Ce nombre correspond au nombre de fois que l'on multiplie le temps de simulation.
	 */
    public static final int NOMBRE_SIMULATIONS = 6;

    /**
	 * Methode qui divise le nombre par le nombre de simulations effectives par le noyau de calcul.
	 * par d�faut le nombre de simulations est de 6
	 * @param val
	 * @return
	 */
    public static int diviserSimu(double val) {
        int resultat = 0;
        if (val < NOMBRE_SIMULATIONS && val > 0) resultat = 1; else resultat = (int) Math.round(val / NOMBRE_SIMULATIONS);
        return resultat;
    }

    public static int[] diviserSimu(int[] tab) {
        int[] newT = new int[tab.length];
        for (int i = 0; i < tab.length; i++) newT[i] = diviserSimu(tab[i]);
        return newT;
    }
}
