package dsp.grafix;

import java.awt.Component;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JPanel;
import dsp.Analyzer;
import dsp.VisualisationFactory;
import dsp.dummy.PlotPanel;
import dsp.exception.UserCancelled;

/**
 * @author Marius
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class GrafixVisFactory implements VisualisationFactory {

    PlotPanel visIn, visOut;

    GrafixInputDialog gia;

    GrafixAnalyzerIn gain;

    GrafixAnalyzerOut gaout;

    int freq;

    public String getName() {
        return "Dummy Visualisation Factory";
    }

    public Analyzer getInputAnalyzer(JDialog dialog, int frequency) throws UserCancelled {
        gia = new GrafixInputDialog(dialog);
        freq = frequency;
        gia.setVisible(true);
        if (gia.isOK()) {
            gain = new GrafixAnalyzerIn(gia.getAfisareSpectru(), gia.getSignalType(), gia.getAfisareSemnal(), gia.getzoomXIn(), gia.getzoomYIn(), frequency);
            return gain;
        } else throw new UserCancelled();
    }

    public Analyzer getOutputAnalyzer() {
        gaout = new GrafixAnalyzerOut(gia.getAfisareSpectruOut(), gia.getSignalTypeOut(), gia.getAfisareSemnalFiltrat(), gia.getzoomXIn(), gia.getzoomYIn(), freq);
        return gaout;
    }

    public Component getVisualisation() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.add(gain.getConfiguration());
        p.add(gaout.getConfiguration());
        return p;
    }
}
