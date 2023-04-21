package unitTests;

import googlechartwrapper.ScatterPlot;
import googlechartwrapper.coder.EncoderFactory;
import googlechartwrapper.coder.EncodingType;
import googlechartwrapper.color.LinearGradient;
import googlechartwrapper.color.SolidFill;
import googlechartwrapper.color.LinearGradient.GradientFillDestination;
import googlechartwrapper.color.SolidFill.ChartFillDestination;
import googlechartwrapper.data.DataScalingSet;
import googlechartwrapper.data.ScatterPlotData;
import googlechartwrapper.data.ScatterPlotPoint;
import googlechartwrapper.label.ChartLegend;
import googlechartwrapper.label.ChartTitle;
import googlechartwrapper.label.DataPointLabel;
import googlechartwrapper.label.DataPointLabel.LabelType;
import googlechartwrapper.label.DataPointLabel.Priority;
import googlechartwrapper.style.GridLine;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

/**
 * 
 * @author steffan
 * 
 */
public class ScatterPlotTest {

    @Test
    public void example() {
        ScatterPlot s = new ScatterPlot(new Dimension(300, 300));
        s.setChartTitle(new ChartTitle("Simple ScatterPlot", Color.GRAY, 12));
        List<ScatterPlotPoint> p = new ArrayList<ScatterPlotPoint>();
        p.add(new ScatterPlotPoint(450, 220));
        p.add(new ScatterPlotPoint(950, 950));
        p.add(new ScatterPlotPoint(530, 420));
        p.add(new ScatterPlotPoint(700, 850));
        p.add(new ScatterPlotPoint(500, 500));
        s.setScatterPlotData(new ScatterPlotData(p));
        s.setGridLine(new GridLine.GridLineBuilder(30, 15).segment(12, 2).offset(25, 25).build());
        String target = "http://chart.apis.google.com/chart?cht=s&chs=300x300&chd=e:eU..jtvJhr,O0..cS5Qhr&chg=30.0,15.0,12.0,12.0,25.0,25.0&chtt=Simple ScatterPlot&chts=808080,12";
        Assert.assertEquals(target, s.getUrl());
    }

    @Test
    public void example2() {
        ScatterPlot plot = new ScatterPlot(new Dimension(600, 450));
        plot.setChartTitle(new ChartTitle("ScatterPlot", Color.WHITE, 12));
        DataScalingSet ds = new DataScalingSet(0, 200);
        List<ScatterPlotPoint> p = new ArrayList<ScatterPlotPoint>();
        p.add(new ScatterPlotPoint(100, 100));
        p.add(new ScatterPlotPoint(200, 200, 100));
        p.add(new ScatterPlotPoint(100, 200, 300));
        p.add(new ScatterPlotPoint(150, 50, 240));
        p.add(new ScatterPlotPoint(140, 50, 230));
        p.add(new ScatterPlotPoint(130, 50, 220));
        p.add(new ScatterPlotPoint(120, 50, 210));
        p.add(new ScatterPlotPoint(110, 50, 200));
        plot.setDataScaling(ds);
        plot.setScatterPlotData(new ScatterPlotData(p));
        ArrayList<String> legends = new ArrayList<String>();
        legends.add("first");
        legends.add("second");
        plot.setChartLegend(new ChartLegend(legends));
        plot.addSolidFill(new SolidFill(ChartFillDestination.Background, Color.BLACK));
        plot.setLinearGradient(new LinearGradient(GradientFillDestination.ChartArea, 12, Color.BLUE, 0.1f, Color.ORANGE, 0.7f));
        String target = "http://chart.apis.google.com/chart?cht=s&chs=600x450&chd=e:VVqqVVgAd3bvZmXe,VVqqqqKrKrKrKrKr,qqVV..zMxEu7szqq&chdl=first|second&chdlp=r&chds=0.0,200.0&chf=c,lg,12,0000ff,0.1,ffc800,0.7|bg,s,000000&chtt=ScatterPlot&chts=ffffff,12";
        Assert.assertEquals(target, plot.getUrl());
    }

    @Test
    public void showCase() {
        ScatterPlot plot = new ScatterPlot(new Dimension(600, 450));
        plot.setChartTitle(new ChartTitle("Scatter Plot", Color.WHITE, 12));
        List<ScatterPlotPoint> p = new ArrayList<ScatterPlotPoint>();
        p.add(new ScatterPlotPoint(10, 20, 100));
        p.add(new ScatterPlotPoint(50, 40, 30));
        p.add(new ScatterPlotPoint(30, 40, 50));
        p.add(new ScatterPlotPoint(45, 15, 75));
        p.add(new ScatterPlotPoint(65, 85, 40));
        p.add(new ScatterPlotPoint(95, 95, 35));
        p.add(new ScatterPlotPoint(20, 80, 80));
        p.add(new ScatterPlotPoint(80, 20, 100));
        p.add(new ScatterPlotPoint(40, 20, 10));
        plot.setScatterPlotData(new ScatterPlotData(p));
        plot.addDataPointLabel(new DataPointLabel(LabelType.Flag, "", Color.RED, 1, DataPointLabel.DataPoint.newDrawEachPoint(), 20, Priority.First));
        plot.setGridLine(new GridLine.GridLineBuilder(30, 15).segment(12, 2).offset(25, 25).build());
        plot.setEncoder(EncoderFactory.getEncoder(EncodingType.TextEncoding));
        String target = "http://chart.apis.google.com/chart?cht=s&chs=600x450&chd=t:10,50,30,45,65,95,20,80,40|20,40,40,15,85,95,80,20,20|100,30,50,75,40,35,80,100,10&chg=30.0,15.0,12.0,12.0,25.0,25.0&chm=a,ff0000,1,-1,20,-1&chtt=Scatter Plot&chts=ffffff,12";
        Assert.assertEquals(target, plot.getUrl());
    }
}
