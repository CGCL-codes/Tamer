package corina.editor;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import corina.Sample;
import corina.SampleEvent;
import corina.SampleListener;
import corina.graph.BargraphFrame;
import corina.graph.GraphWindow;
import corina.ui.Builder;
import corina.ui.CorinaAction;
import corina.ui.I18n;

public class EditorGraphMenu extends JMenu implements SampleListener {

    private JMenuItem plot, plotElements, plotAll, bargraphAll;

    private Sample sample;

    EditorGraphMenu(Sample s) {
        super(I18n.getText("graph"));
        this.sample = s;
        sample.addSampleListener(this);
        plot = new JMenuItem(new CorinaAction("graph") {

            public void actionPerformed(ActionEvent e) {
                new GraphWindow(sample);
            }
        });
        add(plot);
        plotElements = new JMenuItem(new CorinaAction("graph_elements") {

            public void actionPerformed(ActionEvent e) {
                new GraphWindow(sample.elements);
            }

            public boolean isEnabled() {
                return sample.elements != null && sample.elements.size() > 0;
            }
        });
        add(plotElements);
        plotAll = new JMenuItem(new CorinaAction("graph_everything") {

            public void actionPerformed(ActionEvent e) {
                new GraphWindow(sample, sample.elements);
            }

            public boolean isEnabled() {
                return sample.elements != null && sample.elements.size() > 0;
            }
        });
        add(plotAll);
        bargraphAll = new JMenuItem(new CorinaAction("bargraph_elements") {

            public void actionPerformed(ActionEvent e) {
                new BargraphFrame(sample.elements);
            }

            public boolean isEnabled() {
                return sample.elements != null && sample.elements.size() > 0;
            }
        });
        add(bargraphAll);
    }

    public void sampleRedated(SampleEvent e) {
    }

    public void sampleDataChanged(SampleEvent e) {
    }

    public void sampleMetadataChanged(SampleEvent e) {
        boolean hasElements = (sample.elements != null) && (sample.elements.size() > 0);
        plotElements.setEnabled(hasElements);
        plotAll.setEnabled(hasElements);
        bargraphAll.setEnabled(hasElements);
    }

    public void sampleElementsChanged(SampleEvent e) {
    }
}
