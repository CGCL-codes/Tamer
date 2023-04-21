package property;

import frames.MainFrame;
import frames.controls.DateField;
import frames.controls.RGBControl;
import frames.controls.ToolButton;
import geometry.base.AdvEntityProperty;
import java.awt.Color;
import java.awt.Container;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Properties;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import java.awt.Dimension;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.RowSpec;
import java.awt.Component;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.SpinnerNumberModel;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.WindowFocusListener;

/**
 * Das ist der Eigenschaftendialog. Mit ihn lassen sich alle
 * Eigenschaften eines Objektes verändern
 * 
 * @author Etzlstorfer Andreas
 *
 */
public class GetriebePropertyDialog extends JDialog implements ActionListener {

    public static final long serialVersionUID = 0;

    private AdvEntityProperty advproperty;

    /**
	 * OK Button
	 */
    private JButton ok = new JButton("Ok");

    /**
	 * Abbrechen Button
	 */
    private JButton cancel = new JButton("Abbrechen");

    /**
	 * Wird nur verwendet, um den Frame, wenn etwas veraendert wurde,
     * zu aktualisieren
	 */
    private MainFrame f;

    /**
     * Dieses Flag sagt aus, ob ein Frame durch Druecken der
     * Abbrechen Taste abgebrochen wurde
     */
    private boolean canceled = false;

    private final JSpinner xValue = new JSpinner();

    private final JSpinner yValue = new JSpinner();

    private final JLabel lblXstartpositionZ = new JLabel("X-Startposition Z1");

    private final JLabel lblYstartpositionZ = new JLabel("Y-Startposition Z1");

    private final JLabel lblRastergre = new JLabel("Rastergröße");

    private final JSpinner rastergroesse = new JSpinner();

    private final JLabel lblRasterAnzeigen = new JLabel("Raster anzeigen");

    private final JCheckBox rasterAnzeigen = new JCheckBox("");

    /**
     * Ist der Dialog abgebrochen worden?
     * 
	 * @return ja/nein?
	 */
    public boolean isCanceled() {
        return canceled;
    }

    /**
     * Konstruktor
     * 
	 * @param f Frame der unter dem Dialog liegt
	 * @param properties Eigenschaftenliste
	 * @param modal Ist der Dialog modal anzuzeigen?
	 */
    public GetriebePropertyDialog(final MainFrame f, String title, boolean modal) {
        super(f, "Einstellungen Getriebe", modal);
        addWindowFocusListener(new WindowFocusListener() {

            public void windowGainedFocus(WindowEvent arg0) {
                xValue.setValue(f.getUgStartX());
                yValue.setValue(f.getUgStartY());
                rastergroesse.setValue(f.getRaster());
            }

            public void windowLostFocus(WindowEvent arg0) {
            }
        });
        addFocusListener(new FocusAdapter() {

            @Override
            public void focusGained(FocusEvent arg0) {
            }
        });
        setPreferredSize(new Dimension(500, 600));
        JComponent ctrl = null;
        Container container = this.getContentPane();
        this.f = f;
        GridLayout layout = new GridLayout(0, 1, 0, 0);
        getContentPane().setLayout(layout);
        this.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                canceled = true;
            }

            @Override
            public void windowActivated(WindowEvent arg0) {
            }

            @Override
            public void windowOpened(WindowEvent e) {
            }
        });
        JPanel pnl = new JPanel();
        pnl.setLayout(new FormLayout(new ColumnSpec[] { ColumnSpec.decode("174px"), ColumnSpec.decode("45px"), FormFactory.LABEL_COMPONENT_GAP_COLSPEC, ColumnSpec.decode("85px") }, new RowSpec[] { FormFactory.LINE_GAP_ROWSPEC, RowSpec.decode("23px"), FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC }));
        container.add(pnl);
        ok.addActionListener(this);
        pnl.add(lblXstartpositionZ, "1, 4, right, default");
        xValue.setModel(new SpinnerNumberModel(new Integer(200), null, null, new Integer(1)));
        pnl.add(xValue, "2, 4");
        lblYstartpositionZ.setAlignmentX(0.5f);
        pnl.add(lblYstartpositionZ, "1, 6, right, default");
        yValue.setModel(new SpinnerNumberModel(new Integer(200), null, null, new Integer(1)));
        pnl.add(yValue, "2, 6");
        lblRastergre.setAlignmentX(0.5f);
        pnl.add(lblRastergre, "1, 8");
        rastergroesse.setModel(new SpinnerNumberModel(new Integer(10), null, null, new Integer(1)));
        pnl.add(rastergroesse, "2, 8");
        pnl.add(lblRasterAnzeigen, "1, 10");
        pnl.add(rasterAnzeigen, "2, 10, 2, 1");
        pnl.add(ok, "2, 38, 2, 1, left, top");
        cancel.addActionListener(this);
        pnl.add(cancel, "4, 38, left, top");
        this.pack();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == ok) {
            try {
                FileOutputStream out = new FileOutputStream(f.getUgConfDateiname());
                Properties ugProperties = f.getUgProperties();
                ugProperties.setProperty("ug.start.x", xValue.getValue().toString());
                ugProperties.setProperty("ug.start.y", yValue.getValue().toString());
                ugProperties.setProperty("ug.rasstersize", rastergroesse.getValue().toString());
                f.setUgStartX(Integer.parseInt(xValue.getValue().toString()));
                f.setUgStartY(Integer.parseInt(yValue.getValue().toString()));
                f.setRaster(Integer.parseInt(rastergroesse.getValue().toString()));
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
                String aenderungsdatum = sdf.format(cal.getTime());
                f.getUgProperties().store(out, "Zuletzt gespeichert am " + aenderungsdatum);
                out.close();
            } catch (FileNotFoundException ex) {
                System.out.println("Fehler beim Laden der Konfigurationsdateien" + "Ueberpruefen sie folgende Dateien:\n" + "\t'conf/draw.conf'" + "Exception: " + ex.toString());
            } catch (IOException ex) {
                System.out.println("Unbekannter Fehler!");
            }
            this.setVisible(false);
        } else if (e.getSource() == cancel) {
            canceled = true;
            this.setVisible(false);
        }
        f.repaint();
    }
}
