package org.fudaa.fudaa.sipor;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.general.DefaultPieDataset;

/**
 * 
 * Cr�ation de m�thodes statiques tr�s utiles qui construisent automatiquement des panels contenants des
 * objets graphiques JFreeChart (camemberts, graphique,...) � partir d'une liste de valeurs et d'une liste de libell�s correspondant.
 * Les m�thodes retournent un panel contenant l'objet graphique. Il ne reste plus qu'� int�grer le panel r�sultat � un panel, une frame, internalframe....
 * 
 *@version $Version$
 * @author Adrien Hadoux
 *
 */
public class SiporJFreeChartCamembert {

    /**
	 * Methode statique de cr�ation d'un camembert
	 * @param values les valeurs des quartiers du camemberts
	 * @param libelles les libelles des quarties du camembert
	 * @param titre le titre du camembert
	 * @return
	 */
    public static ChartPanel creerCamembert(int[] values, String[] libelles, String titre) {
        DefaultPieDataset data = new DefaultPieDataset();
        for (int i = 0; i < values.length; i++) data.setValue(libelles[i], values[i]);
        JFreeChart chart = ChartFactory.createPieChart(titre, data, true, true, true);
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setLegendLabelGenerator(new StandardPieSectionLabelGenerator("{0}: {2}"));
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}: {1} ({2})"));
        ChartPanel panneauConteneur = new ChartPanel(chart);
        return panneauConteneur;
    }

    /**
	 * Methode permettant de modifier un camembert
	 */
    public static void modifierCamembert(ChartPanel panneauAmodifier, int[] values, String[] libelles, String titre) {
        DefaultPieDataset data = new DefaultPieDataset();
        for (int i = 0; i < values.length; i++) data.setValue(libelles[i], values[i]);
        JFreeChart chart = ChartFactory.createPieChart(titre, data, true, true, true);
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setLegendLabelGenerator(new StandardPieSectionLabelGenerator("{0}: {2}"));
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}: {1} ({2})"));
        panneauAmodifier.setChart(chart);
    }

    /**
	 * idem que pr�c�dent mais avec tableau de double
	 * @param values
	 * @param libelles
	 * @param titre
	 * @return
	 */
    public static ChartPanel creerCamembert(double[] values, String[] libelles, String titre) {
        DefaultPieDataset data = new DefaultPieDataset();
        for (int i = 0; i < values.length; i++) data.setValue(libelles[i], values[i]);
        JFreeChart chart = ChartFactory.createPieChart(titre, data, true, true, true);
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setLegendLabelGenerator(new StandardPieSectionLabelGenerator("{0}: {2}"));
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}: {1} ({2})"));
        ChartPanel panneauConteneur = new ChartPanel(chart);
        return panneauConteneur;
    }

    public static void modifierCamembert(ChartPanel panneauAmodifier, double[] values, String[] libelles, String titre) {
        DefaultPieDataset data = new DefaultPieDataset();
        for (int i = 0; i < values.length; i++) data.setValue(libelles[i], values[i]);
        JFreeChart chart = ChartFactory.createPieChart(titre, data, true, true, true);
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setLegendLabelGenerator(new StandardPieSectionLabelGenerator("{0}: {2}"));
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}: {1} ({2})"));
        panneauAmodifier.setChart(chart);
    }

    /**
 * idem que pr�c�dent mais avec tableau de float
 * @param values
 * @param libelles
 * @param titre
 * @return
 */
    public static ChartPanel creerCamembert(float[] values, String[] libelles, String titre) {
        DefaultPieDataset data = new DefaultPieDataset();
        for (int i = 0; i < values.length; i++) data.setValue(libelles[i], values[i]);
        JFreeChart chart = ChartFactory.createPieChart(titre, data, true, true, true);
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setLegendLabelGenerator(new StandardPieSectionLabelGenerator("{0}: {2}"));
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}: {1} ({2})"));
        ChartPanel panneauConteneur = new ChartPanel(chart);
        return panneauConteneur;
    }

    public static void modifierCamembert(ChartPanel panneauAmodifier, float[] values, String[] libelles, String titre) {
        DefaultPieDataset data = new DefaultPieDataset();
        for (int i = 0; i < values.length; i++) data.setValue(libelles[i], values[i]);
        JFreeChart chart = ChartFactory.createPieChart(titre, data, true, true, true);
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setLegendLabelGenerator(new StandardPieSectionLabelGenerator("{0}: {2}"));
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}: {1} ({2})"));
        panneauAmodifier.setChart(chart);
    }

    public static ChartPanel creerCamembert3d(int[] values, String[] libelles, String titre) {
        DefaultPieDataset data = new DefaultPieDataset();
        for (int i = 0; i < values.length; i++) data.setValue(libelles[i], values[i]);
        JFreeChart chart = ChartFactory.createPieChart3D(titre, data, true, true, false);
        ChartPanel panneauConteneur = new ChartPanel(chart);
        return panneauConteneur;
    }

    public static void modifierCamembert3d(ChartPanel panneauAmodifier, int[] values, String[] libelles, String titre) {
        DefaultPieDataset data = new DefaultPieDataset();
        for (int i = 0; i < values.length; i++) data.setValue(libelles[i], values[i]);
        JFreeChart chart = ChartFactory.createPieChart3D(titre, data, true, true, false);
        panneauAmodifier.setChart(chart);
    }

    public static ChartPanel creerHisto3d(int[] values, String titre, String[] libelles) {
        double[][] donnees = new double[1][];
        donnees[0] = new double[values.length];
        for (int i = 0; i < values.length; i++) donnees[0][i] = values[i];
        CategoryDataset data = DatasetUtilities.createCategoryDataset("cat�gories de navires", "cat�gorie", donnees);
        JFreeChart chart = ChartFactory.createBarChart3D(titre, "cat�gories de navire", "nombre de navires", data, PlotOrientation.VERTICAL, true, true, false);
        ChartPanel panneauConteneur = new ChartPanel(chart);
        return panneauConteneur;
    }

    public static void modifierHisto3d(ChartPanel panneauAmodifier, int[] values, String[] libelles, String titre) {
        DefaultPieDataset data = new DefaultPieDataset();
        for (int i = 0; i < values.length; i++) data.setValue(libelles[i], values[i]);
        JFreeChart chart = ChartFactory.createPieChart3D(titre, data, true, true, false);
        panneauAmodifier.setChart(chart);
    }
}
