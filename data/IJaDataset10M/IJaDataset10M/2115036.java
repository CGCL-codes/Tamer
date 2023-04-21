package lechuga.exceptions;

import lechuga.contextelems.LechugaContext;

/**
 * Aquesta classe implementa l'algoritme de similitud de paraules per dist�ncia
 * (algoritme de Levenshtein), per tal de trobar la millor alternativa quan a 
 * Lechuga-Container li demanen un IdBean que no es troba. Aquesta alternativa
 * es facilita afegit al missatge de la LechugaWrongNameContextException que es 
 * llan�a.
 * 
 * @author mhoms
 */
public class PossiblyCorrectIdBeanFinder {

    /**
	 * donat un idBean equivocat (no es troba dins del context), s'intenta
	 * trobar el definit m�s semblant.
	 * 
	 * @param wrongIdBean
	 * @param context
	 * @return String
	 */
    public String find(final String wrongIdBean, final LechugaContext context) {
        String bestid = null;
        int bestDist = 10000;
        for (String id : context.getBeans().keySet()) {
            final int dist = computeLevenshteinDistance(wrongIdBean.toLowerCase(), id.toLowerCase());
            if (dist < bestDist) {
                bestid = id;
                bestDist = dist;
            }
        }
        if (bestid == null) return "";
        return "\n*** Did you mean... '" + bestid + "'";
    }

    /**
	 * funci� de m�nims d'ordre 3
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @return
	 */
    private int minimum(int a, int b, int c) {
        if (a <= b && a <= c) return a;
        if (b <= a && b <= c) return b;
        return c;
    }

    /**
	 * calcula la dist�ncia de Levenshtein
	 *  
	 * @param str1
	 * @param str2
	 * @return
	 */
    private int computeLevenshteinDistance(final String s1, final String s2) {
        char[] str1 = s1.toCharArray();
        char[] str2 = s2.toCharArray();
        int[][] distance = new int[str1.length + 1][str2.length + 1];
        for (int i = 0; i <= str1.length; i++) distance[i][0] = i;
        for (int j = 0; j <= str2.length; j++) distance[0][j] = j;
        for (int i = 1; i <= str1.length; i++) for (int j = 1; j <= str2.length; j++) distance[i][j] = minimum(distance[i - 1][j] + 1, distance[i][j - 1] + 1, distance[i - 1][j - 1] + ((str1[i - 1] == str2[j - 1]) ? 0 : 1));
        return distance[str1.length][str2.length];
    }
}
