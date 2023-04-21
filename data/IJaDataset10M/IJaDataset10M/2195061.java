package Analise;

import classes.Defs;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RefineryUtilities;

/**
 * Created on : Apr 6, 2012, 7:59:38 PM
 * @author diogo
 */
public abstract class PlayerStatisticsFrequency extends PlayerStatisticsBase {

    @Override
    public void SetPlayerDatas(PlayerData... vPlayerDatas) {
        super.SetPlayerDatas(vPlayerDatas);
        vTypes = PlayerManager.GetInstance().GetAllPlayedGameTypes(vPlayerDatas);
    }

    protected void OpenGraphic(DefaultCategoryDataset dataset, String sTitle, String sAxisNameX, String sAxisNameY) {
        JFrame janela = new JFrame(sTitle);
        JFreeChart jfreechart = ChartFactory.createStackedBarChart(sTitle, sAxisNameX, sAxisNameY, dataset, PlotOrientation.VERTICAL, true, true, false);
        JPanel jpanel = new ChartPanel(jfreechart);
        jpanel.setPreferredSize(new Dimension(800, 600));
        janela.setContentPane(jpanel);
        janela.pack();
        RefineryUtilities.centerFrameOnScreen(janela);
        janela.setVisible(true);
        janela.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }
}
