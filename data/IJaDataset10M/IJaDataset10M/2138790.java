package game.visualizations;

import game.visualizations.graph.Graph;
import game.visualizations.graph.GraphLayerAxis;
import game.visualizations.graph.GraphLayerFunction2D;
import game.visualizations.graph.GraphLayerSpots;
import game.gui.GraphCanvas;
import game.gui.InputControls;
import game.gui.Controls;
import game.visualizations.ScatterplotInput;
import game.data.InputFactor;
import game.data.GlobalData;
import java.awt.Color;
import java.awt.Graphics2D;

public class ScatterplotMatrix extends Graph implements game.visualizations.Graph {

    class TSouradnice {

        public int x, y;

        TSouradnice(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private GraphScatterCfg cfg;

    private transient GraphCanvas gc;

    private boolean update = true;

    private transient GraphLayerFunction2D[] function_layers;

    private transient ScatterplotInput[] function_inputs;

    private transient GraphLayerAxis[] axis_layers;

    private transient GraphLayerSpots[] spot_layers;

    private transient GraphSpotsInput[] spot_inputs;

    private TSouradnice[] s;

    public ScatterplotMatrix() {
    }

    public void init(GraphCanvas gic) {
        gc = gic;
        cfg.init(gc);
        cfg.showSpots = GraphCanvas.getInstance().isPointsEnabled();
        cfg.showAxis = gc.isCoord();
        GraphCanvas.getInstance().setGraphDataReady(true);
    }

    public void takeData() {
        this.updateAll();
    }

    private void updateAll() {
        if (update) {
            lock();
            int x;
            int y = 0;
            this.removeAll();
            boolean[] sel = InputControls.getInstance().getSelectedFactorsList();
            int count = 0;
            for (int i = 0; i < sel.length; i++) {
                if ((((InputFactor) GlobalData.getInstance().iFactor.elementAt(i)) != null) && (sel[i])) {
                    count++;
                }
            }
            count = ((count - 1) * count) / 2;
            function_layers = new GraphLayerFunction2D[count];
            function_inputs = new ScatterplotInput[count];
            axis_layers = new GraphLayerAxis[count];
            spot_layers = new GraphLayerSpots[count];
            spot_inputs = new GraphSpotsInput[count];
            s = new TSouradnice[count];
            int index = 0;
            for (int fy = 0; fy < GlobalData.getInstance().getINumber(); fy++) {
                x = 0;
                if (((InputFactor) GlobalData.getInstance().iFactor.elementAt(fy)) == null) {
                    continue;
                }
                if (!sel[fy]) {
                    continue;
                }
                for (int fx = 0; fx < GlobalData.getInstance().getINumber(); fx++) {
                    if (((InputFactor) GlobalData.getInstance().iFactor.elementAt(fx)) == null) {
                        continue;
                    }
                    if (!sel[fx]) {
                        continue;
                    }
                    if (x >= y) {
                        continue;
                    }
                    function_inputs[index] = new ScatterplotInput(gc, fx, fy, cfg.res_x, cfg.res_y, cfg.transparency, cfg.sensitivity);
                    s[index] = new TSouradnice(x, y);
                    if (Controls.getInstance().getGnet() != null) {
                        function_layers[index] = new GraphLayerFunction2D(x * (cfg.width + 10) + 5, (y - 1) * (cfg.height + 10) + 5, cfg.width, cfg.height, function_inputs[index]);
                        function_layers[index].set(GraphLayerFunction2D.SET_MIN_X, 0.0);
                        function_layers[index].set(GraphLayerFunction2D.SET_MIN_Y, 0.0);
                        function_layers[index].set(GraphLayerFunction2D.SET_MAX_X, 1.0);
                        function_layers[index].set(GraphLayerFunction2D.SET_MAX_Y, 1.0);
                        function_layers[index].set(GraphLayerFunction2D.SET_RES_X, cfg.res_y);
                        function_layers[index].set(GraphLayerFunction2D.SET_RES_Y, cfg.res_x);
                        function_layers[index].set(GraphLayerFunction2D.SET_VISIBLE, true);
                        this.add(function_layers[index]);
                    }
                    spot_inputs[index] = new GraphSpotsInput(gc, fx, fy);
                    spot_inputs[index].setMaxKlobouk(cfg.klobouk);
                    spot_inputs[index].lock();
                    spot_layers[index] = new GraphLayerSpots(x * (cfg.width + 10) + 5, (y - 1) * (cfg.height + 10) + 5, cfg.width, cfg.height, spot_inputs[index]);
                    spot_layers[index].lock();
                    spot_layers[index].set(GraphLayerSpots.SET_MIN_X, 0.0);
                    spot_layers[index].set(GraphLayerSpots.SET_MIN_Y, 0.0);
                    spot_layers[index].set(GraphLayerSpots.SET_MAX_X, 1.0);
                    spot_layers[index].set(GraphLayerSpots.SET_MAX_Y, 1.0);
                    spot_layers[index].set(GraphLayerSpots.SET_VISIBLE, cfg.showSpots);
                    spot_inputs[index].unlock();
                    spot_layers[index].unlock();
                    this.add(spot_layers[index]);
                    axis_layers[index] = new GraphLayerAxis(x * (cfg.width + 10) + 5, (y - 1) * (cfg.height + 10) + 5, cfg.width, cfg.height, Color.WHITE);
                    axis_layers[index].lock();
                    axis_layers[index].set(GraphLayerAxis.SET_MIN_X, 0.0);
                    axis_layers[index].set(GraphLayerAxis.SET_MIN_Y, 0.0);
                    axis_layers[index].set(GraphLayerAxis.SET_MAX_X, 1.0);
                    axis_layers[index].set(GraphLayerAxis.SET_MAX_Y, 1.0);
                    axis_layers[index].set(GraphLayerAxis.SET_STEP_X, 0.2);
                    axis_layers[index].set(GraphLayerAxis.SET_STEP_Y, 0.2);
                    axis_layers[index].set(GraphLayerAxis.SET_LABEL_X, ((InputFactor) GlobalData.getInstance().iFactor.elementAt(fx)).getName());
                    axis_layers[index].set(GraphLayerAxis.SET_LABEL_Y, ((InputFactor) GlobalData.getInstance().iFactor.elementAt(fy)).getName());
                    axis_layers[index].set(GraphLayerAxis.SET_MARK_SIZE_X, 2);
                    axis_layers[index].set(GraphLayerAxis.SET_MARK_SIZE_Y, 2);
                    axis_layers[index].set(GraphLayerAxis.SET_VISIBLE, cfg.showAxis);
                    axis_layers[index].unlock();
                    this.add(axis_layers[index]);
                    x++;
                    index++;
                }
                y++;
            }
        }
        unlock();
    }

    public void freeMemory() {
        function_layers = null;
        function_inputs = null;
        axis_layers = null;
        spot_layers = null;
        spot_inputs = null;
        s = null;
        System.gc();
    }

    public void paint(Graphics2D g) {
        super.paint(g);
    }

    public GConfig getGraphConfigClass() {
        return (GConfig) cfg;
    }

    public Class getConfigClass() {
        return GraphScatterCfg.class;
    }

    public String getMenuLabel() {
        return "Scaterplot matrix";
    }

    public int getType() {
        return GraphCanvas.SCATTERPLOTMATRIX;
    }

    public void setConfigClass(GConfig cfg) {
        this.cfg = (GraphScatterCfg) cfg;
    }
}
