package data;

import java.util.Calendar;
import views.widgets.geographic.GeographicWidget;

/**
 * @author root
 *
 */
public class FakeData extends MOData {

    protected FakeData() {
        FIRST_YEAR = 1920;
        LAST_YEAR = Calendar.getInstance().get(Calendar.YEAR);
        NB_YEARS = LAST_YEAR - FIRST_YEAR;
        System.out.println(FIRST_YEAR + " -> " + NB_YEARS + " -> " + LAST_YEAR);
        RANGE_PRICES = 20;
        RANGE_APPRECIATION = 100;
        RANGE_RELEASES = 2;
        RANGES = new int[] { RANGE_PRICES, RANGE_APPRECIATION, RANGE_RELEASES };
        NB_MEASURES = 3;
        PRICES = 0;
        APPRECIATIONS = 1;
        RELEASES = 2;
        NB_CONTEXTS = 2;
        BAND = 0;
        STYLE = 1;
        bandNames = new String[] { "Metallica", "Nirvana", "SkaP", "Jean-Michel Pot", "Led Zeppelin", "Georges Brassens" };
        styleNames = new String[] { "Pop", "Rock", "Electro", "Chiant", "Classique", "Shisha" };
        contexts = new String[][] { bandNames, styleNames };
        appreciationsPerBand = new int[bandNames.length][GeographicWidget.NB_CONTINENTS][NB_YEARS];
        appreciationsPerStyle = new int[styleNames.length][GeographicWidget.NB_CONTINENTS][NB_YEARS];
        pricesPerBand = new int[bandNames.length][GeographicWidget.NB_CONTINENTS][NB_YEARS];
        pricesPerStyle = new int[styleNames.length][GeographicWidget.NB_CONTINENTS][NB_YEARS];
        releasesPerBand = new int[bandNames.length][GeographicWidget.NB_CONTINENTS][NB_YEARS];
        releasesPerStyle = new int[styleNames.length][GeographicWidget.NB_CONTINENTS][NB_YEARS];
        measurePerContext = new int[][][][][] { new int[][][][] { pricesPerBand, pricesPerStyle }, new int[][][][] { appreciationsPerBand, appreciationsPerStyle }, new int[][][][] { releasesPerBand, releasesPerStyle } };
        for (int x = 0; x < NB_MEASURES; x++) for (int y = 0; y < NB_CONTEXTS; y++) for (int z = 0; z < contexts[y].length; z++) for (int j = 0; j < GeographicWidget.NB_CONTINENTS; j++) {
            measurePerContext[x][y][z][j] = brown(NB_YEARS, 0, RANGES[x]);
            ;
        }
        me = this;
        System.out.println(me.measurePerContext[0][0][2][0][2]);
    }

    private int[] brown(int nb, int min, int max) {
        int[] result = new int[nb];
        result[0] = (int) (Math.random() * (max - min) + min);
        for (int i = 1; i < nb; i++) {
            result[i] = Math.max(0, Math.min(max, (int) (result[i - 1] + Math.random() * 1.0 * (max - min) / 5 - 1.0 * (max - min) / 10 + 1)));
        }
        return result;
    }

    public static MOData getInstance() {
        if (me == null) new FakeData();
        return me;
    }
}
