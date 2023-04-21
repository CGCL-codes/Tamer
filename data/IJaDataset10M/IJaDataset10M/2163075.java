package apollo.gui.menus;

import apollo.config.Config;
import apollo.config.PropertyScheme;
import apollo.config.TierProperty;
import apollo.gui.*;
import apollo.gui.drawable.DrawableAnnotationConstants;
import apollo.gui.genomemap.StrandedZoomableApolloPanel;
import apollo.datamodel.*;
import apollo.dataadapter.*;
import apollo.gui.synteny.CurationManager;
import apollo.gui.tweeker.Tweeker;
import apollo.analysis.AnalysisDataAdapterI;
import apollo.analysis.AnalysisGUI;
import apollo.analysis.BlastXMLParser;
import apollo.analysis.RemoteBlastNCBI;
import apollo.analysis.SeqAnalysisI;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.MenuListener;
import javax.swing.event.MenuEvent;
import java.util.Vector;
import org.bdgp.swing.widget.DataAdapterChooser;
import org.bdgp.io.DataAdapter;
import org.bdgp.io.DataAdapterRegistry;

public class AnalysisMenu extends JMenu implements ActionListener, DrawableAnnotationConstants {

    JMenuItem gcplot;

    JMenuItem restriction;

    JMenuItem analyze;

    private Tweeker tweekerFrame;

    public AnalysisMenu() {
        super("Analysis");
        menuInit();
    }

    public void menuInit() {
        gcplot = new JMenuItem("Show GC plot...");
        restriction = new JMenuItem("Find restriction sites...");
        analyze = new JMenuItem("Analyze sequence...");
        add(gcplot);
        add(restriction);
        addSeparator();
        add(analyze);
        analyze.addActionListener(this);
        ActionListener al = new AnalysisActionListener();
        gcplot.addActionListener(al);
        restriction.addActionListener(al);
        this.addMenuListener(new SequenceCheckMenuListener());
    }

    private class AnalysisActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            int type = -1;
            if (e.getSource() == gcplot) type = Tweeker.GC; else if (e.getSource() == restriction) type = Tweeker.RESTRICTION;
            Tweeker.openTweeker(type);
        }
    }

    private class SequenceCheckMenuListener implements MenuListener {

        public void menuSelected(MenuEvent e) {
            gcplot.setEnabled(haveSequence());
            restriction.setEnabled(haveSequence());
            analyze.setEnabled(haveSequence());
        }

        public void menuDeselected(MenuEvent e) {
        }

        public void menuCanceled(MenuEvent e) {
        }
    }

    /** returns true if active curation has sequence */
    private boolean haveSequence() {
        return CurationManager.getCurationManager().getActiveCurState().haveSequence();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == analyze) {
            new AnalysisGUI();
        }
    }

    class ItemWindowListener extends WindowAdapter {

        JCheckBoxMenuItem item;

        public ItemWindowListener(JCheckBoxMenuItem item) {
            this.item = item;
        }

        public void windowClosing(WindowEvent e) {
            item.setState(false);
            ((Window) e.getSource()).removeWindowListener(this);
        }
    }
}
