package edu.brook.manual.model3;

import org.ascape.model.HostCell;
import org.ascape.model.Scape;
import org.ascape.model.event.ScapeEvent;
import org.ascape.model.space.Array2DVonNeumann;
import org.ascape.util.data.StatCollector;
import org.ascape.util.data.StatCollectorCSAMM;
import org.ascape.util.data.StatCollectorCond;
import org.ascape.util.vis.ColorFeature;
import org.ascape.view.vis.ChartView;
import org.escape.view.vis.Overhead2DView;

public class CoordinationGame extends Scape {

    /**
     * 
     */
    private static final long serialVersionUID = -1066533372885692772L;

    protected int nPlayers = 100;

    protected int latticeWidth = 30;

    protected int latticeHeight = 30;

    public int coordinateOnBlue = 1;

    public int coordinateOnRed = 1;

    public int error = 0;

    Scape lattice;

    Scape players;

    Overhead2DView overheadView;

    public void createScape() {
        super.createScape();
        lattice = new Scape(new Array2DVonNeumann());
        lattice.setPrototypeAgent(new HostCell() {

            private static final long serialVersionUID = 1L;

            /**
             * @return
             * @see org.ascape.model.Agent#getPlatformColor()
             */
            public Object getPlatformColor() {
                return ColorFeature.GREEN;
            }
        });
        lattice.setExtent(latticeWidth, latticeHeight);
        CoordinationGamePlayer cgplayer = new CoordinationGamePlayer();
        cgplayer.setHostScape(lattice);
        players = new Scape();
        players.setName("Players");
        players.setPrototypeAgent(cgplayer);
        players.setExecutionOrder(Scape.RULE_ORDER);
        add(lattice);
        add(players);
        StatCollector CountReds = new StatCollectorCond("Reds") {

            private static final long serialVersionUID = -1204087946730275898L;

            public boolean meetsCondition(Object object) {
                return ((CoordinationGamePlayer) object).getBackground(object) == ColorFeature.RED;
            }
        };
        StatCollector CountBlues = new StatCollectorCond("Blues") {

            private static final long serialVersionUID = 6690746035332487734L;

            public boolean meetsCondition(Object object) {
                return ((CoordinationGamePlayer) object).getBackground(object) == ColorFeature.BLUE;
            }
        };
        StatCollector AvgPayoff = new StatCollectorCSAMM("Payoff") {

            private static final long serialVersionUID = 5181166978735324715L;

            public double getValue(Object object) {
                return ((CoordinationGamePlayer) object).getRunningTotal();
            }
        };
        players.addStatCollector(CountReds);
        players.addStatCollector(CountBlues);
        players.addStatCollector(AvgPayoff);
    }

    public void scapeSetup(ScapeEvent scapeEvent) {
        players.setExtent(nPlayers);
    }

    public void createGraphicViews() {
        super.createGraphicViews();
        ChartView chart = new ChartView();
        players.addView(chart);
        chart.addSeries("Count Reds", ColorFeature.RED);
        chart.addSeries("Count Blues", ColorFeature.BLUE);
        chart.addSeries("Average Payoff", ColorFeature.BLACK);
        overheadView = new Overhead2DView();
        lattice.addView(overheadView);
    }

    public int getRedScore() {
        return coordinateOnRed;
    }

    public void setRedScore(int NewcoordinateOnRed) {
        coordinateOnRed = NewcoordinateOnRed;
    }

    public int getBlueScore() {
        return coordinateOnBlue;
    }

    public void setBlueScore(int NewcoordinateOnBlue) {
        coordinateOnBlue = NewcoordinateOnBlue;
    }

    public int getError() {
        return error;
    }

    public void setError(int NewError) {
        error = NewError;
    }

    public int getnPlayers() {
        return nPlayers;
    }

    public void setnPlayers(int NewnPlayers) {
        nPlayers = NewnPlayers;
    }
}
