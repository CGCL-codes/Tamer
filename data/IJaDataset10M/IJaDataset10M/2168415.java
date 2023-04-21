package mipt.gui.graph.options;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.JFileChooser;
import mipt.common.Utils;
import mipt.gui.FileChooser;
import mipt.gui.graph.impl.ColorsLegend;
import mipt.gui.graph.plot.CurveStyle;
import mipt.gui.graph.plot.Plot;
import mipt.gui.graph.plot.legend.Legend;
import mipt.gui.graph.plot.legend.LegendRow;
import mipt.gui.graph.primitives.dots.DotPrototype;

/**
 * @author Zhmurov
 */
public class GraphPropertiesController extends FileChooser implements ActionListener {

    private GraphModelCodec codec;

    private File file;

    private Window window;

    private GraphPropertiesPanel panel;

    private Plot plot;

    private Legend legend;

    private ColorsLegend colorsLegend;

    public GraphPropertiesController(Plot plot, Legend legend, ColorsLegend colorsLegend, GraphPropertiesPanel panel, Window window, GraphModelCodec codec, JFileChooser chooser) {
        super(chooser);
        this.plot = plot;
        this.legend = legend;
        this.colorsLegend = colorsLegend;
        this.panel = panel;
        this.window = window;
        this.codec = codec;
    }

    /**
      * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
      */
    public void actionPerformed(ActionEvent ae) {
        String command = ae.getActionCommand();
        if (command == GraphPropertiesPanel.OK_COMMAND) {
            doOk();
            close();
        } else if (command == GraphPropertiesPanel.APPLY_COMMAND) {
            doOk();
        } else if (command == GraphPropertiesPanel.CANCEL_COMMAND) {
            close();
        } else if (command == GraphPropertiesPanel.LOAD_COMMAND) {
            loadModel();
        } else if (command == GraphPropertiesPanel.SAVE_COMMAND) {
            saveModel(file);
        } else if (command == GraphPropertiesPanel.SAVE_AS_COMMAND) {
            saveModel(null);
        }
    }

    private GraphModel loadModel(File file) throws IOException {
        return codec.load(file);
    }

    private void loadModel() {
        try {
            file = chooseFileToOpen(window);
            if (file != null) panel.setModel(loadModel(file));
        } catch (Exception e) {
            showFileError(window, Utils.getErrorInFileString(file.getPath()));
        }
    }

    private void saveModel(File file) {
        if (file != null) {
            try {
                codec.save(file, panel.getModel());
            } catch (Exception e) {
                showFileError(window, Utils.getCantCreateString(file.getPath()));
            }
        } else {
            file = chooseFileToSave(window);
            if (file != null) saveModel(getFileWithExtension(file, getChooser()));
        }
    }

    private void doOk() {
        GraphModel model = panel.getModel();
        applyModelToAll(model);
    }

    private void applyModelToAll(GraphModel model) {
        applyModelToPlot(model);
        applyModelToLegend(model);
        applyModelToColorsLegend(model);
    }

    private void close() {
        window.dispose();
    }

    private void applyModelToPlot(GraphModel model) {
        plot.setBackground(model.getBgColor());
        plot.setAxes(model.getAxes());
        plot.setXAxisFormat(model.getHorizontalAxis());
        plot.setYAxisFormat(model.getVerticalAxis());
        for (int i = 0; i < plot.getCurvesCount(); i++) plot.setCurveStyle(model.getCurveStyle(i), i);
        plot.repaint();
    }

    private void applyModelToLegend(GraphModel model) {
        if (legend != null) {
            legend.removeAll();
            for (int i = 0; i < plot.getCurvesCount(); i++) {
                CurveStyle cs = model.getCurveStyle(i);
                DotPrototype dot = (DotPrototype) cs.dot;
                LegendRow row = legend.createRow("y" + (i + 1), dot, dot.color, cs.lineStyle.color);
                legend.addRow(row);
            }
        }
    }

    private void applyModelToColorsLegend(GraphModel model) {
    }
}
