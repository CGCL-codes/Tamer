package net.sf.appomatox.paket.analysis;

import java.awt.Color;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import jscl.text.ParseException;
import net.sf.appomatox.berechnungen.PlottbareBerechnung;
import net.sf.appomatox.bibliothek.analysis.Analysis;
import net.sf.appomatox.bibliothek.analysis.Expression;
import net.sf.appomatox.bibliothek.analysis.Analysis.Kepler;
import net.sf.appomatox.control.ergebnisse.CompoundEintrag;
import net.sf.appomatox.control.ergebnisse.ExpressionEintrag;
import net.sf.appomatox.control.ergebnisse.KommentarEintrag;
import net.sf.appomatox.control.ergebnisse.MathMLEintrag;
import net.sf.appomatox.control.ergebnisse.UeberschriftEintrag;
import net.sf.appomatox.gui.diagramm.plots.PlottbareFunktion;
import net.sf.appomatox.gui.diagramm.plots.PlottbareIntegralflaeche;
import net.sf.appomatox.utils.MML;
import net.sf.appomatox.utils.MMLComponentBuilder;
import net.sf.appomatox.utils.Parser;
import net.sf.appomatox.utils.Utils;
import net.sf.appomatox.utils.hintTextField.HintTextField;
import net.sf.appomatox.utils.preferences.Props;
import org.jdesktop.swingx.JXErrorPane;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import de.schnitzer.properties.Properties;

public class Integral extends PlottbareBerechnung {

    private JTextField edtFx = new HintTextField("2x^2", Expression.getExpressionChecker());

    private JTextField edtX1 = new HintTextField("4", Parser.getDoubleChecker());

    private JTextField edtX2 = new HintTextField("2", Parser.getDoubleChecker());

    private static final double AE_NSVON = Properties.getDouble(Props.ANALYSIS_SCAN_VON_BIS);

    private static final int AE_NSAUFL = Properties.getInt(Props.ANALYSIS_SCAN_AUFLOESUNG);

    public Integral() {
        edtX1.setHorizontalAlignment(SwingConstants.CENTER);
        edtX2.setHorizontalAlignment(SwingConstants.CENTER);
        FormLayout layout = new FormLayout("10dlu, 30dlu, 4dlu, 100dlu:grow, 2dlu, left:30dlu", "p, 10dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu");
        PanelBuilder builder = new PanelBuilder(layout, this);
        builder.setDefaultDialogBorder();
        CellConstraints cc = new CellConstraints();
        builder.addSeparator("Integral", cc.xyw(1, 1, 6));
        MMLComponentBuilder mmlCB = MMLComponentBuilder.getInstance();
        String mmlIntegral = "<mstyle displaystyle='true'><mo largeop='true'>&int;</mo></mstyle>";
        builder.add(edtX1, cc.xy(2, 3));
        builder.add(mmlCB.getComponent(mmlIntegral), cc.xy(2, 5));
        builder.add(edtX2, cc.xy(2, 7));
        builder.add(mmlCB.getComponent("<mi>dx</mi>"), cc.xy(6, 5));
        builder.add(edtFx, cc.xy(4, 5));
        setFocusComponent(edtX1);
        setOpaque(false);
    }

    @Override
    public String getBeschreibung() {
        return "Berechnet den Inhalt der Fl�che zwischen der x-Achse und der Kurve f(x) und den seitlichen Begrenzungen x1 und x2.";
    }

    public static final String PATH = "Analysis.Integral";

    /**
     * Die Expression, die ins Diagramm �bernommen wird.
     */
    protected Expression plottbareFnk = null;

    protected double plottbaresX1;

    protected double plottbaresX2;

    @Override
    public Action getActSchaubild() {
        return new AbstractAction("Schaubild") {

            {
                putValue(Action.SMALL_ICON, Utils.getIcon("/res/16x16/oscilloscope.png"));
                putValue(Action.SHORT_DESCRIPTION, "Startet die Berechnung");
            }

            public void actionPerformed(ActionEvent arg0) {
                if (plottbareFnk != null) {
                    m_DiagrammCtrl.addSchaubild(new PlottbareFunktion(plottbareFnk, Color.BLACK));
                    m_DiagrammCtrl.addSchaubild(new PlottbareIntegralflaeche(plottbareFnk, plottbaresX2, plottbaresX1, Color.BLUE));
                } else {
                    JOptionPane.showMessageDialog(Integral.this, "Bitte berechnen Sie zun�chst eine Funktion.", "Warnung", JOptionPane.WARNING_MESSAGE);
                }
            }
        };
    }

    @Override
    public void berechnen() throws InterruptedException {
        String fx = edtFx.getText();
        Expression funk;
        try {
            funk = Expression.getInstance(fx);
        } catch (ParseException e) {
            JXErrorPane.showDialog(this, Utils.getErrorInfo("Die eingegebene Funktion f(x) ist ung�ltig", e));
            return;
        }
        double x1, x2;
        try {
            x1 = Parser.parseDouble(edtX1.getText());
            x2 = Parser.parseDouble(edtX2.getText());
        } catch (NumberFormatException e) {
            JXErrorPane.showDialog(this, Utils.getErrorInfo("Die eingegebenen Intervallgrenzen sind ung�ltig", e));
            return;
        }
        m_BerechnungenCtrl.addErgebniseintrag(new UeberschriftEintrag("Integral"));
        plottbareFnk = funk;
        plottbaresX1 = x1;
        plottbaresX2 = x2;
        Analysis analysis = Analysis.getInstance(-AE_NSVON, AE_NSVON, AE_NSAUFL);
        Expression stammfnk = analysis.getIntegral(funk, "x");
        double integral = analysis.getIntegralApprox(funk, "x", x2, x1);
        m_BerechnungenCtrl.addErgebniseintrag(new CompoundEintrag(new MathMLEintrag(MML.getFx()), new ExpressionEintrag(funk)));
        m_BerechnungenCtrl.addErgebniseintrag(new CompoundEintrag(new MathMLEintrag(MML.getAvonB("F", "x")), new ExpressionEintrag(stammfnk)));
        String code = MML.getIntegral(funk, x2, x1) + "<mo>=</mo>";
        m_BerechnungenCtrl.addErgebniseintrag(new CompoundEintrag(new MathMLEintrag(code), new MathMLEintrag(MML.getNumber(integral))));
        m_BerechnungenCtrl.addErgebniseintrag(new CompoundEintrag(new MathMLEintrag("<mi>F</mi><mo>=</mo>"), new MathMLEintrag(MML.getNumber(Math.abs(integral)) + MML.getEinheit("FE"))));
        m_BerechnungenCtrl.addErgebniseintrag(new KommentarEintrag("Kepler'sche Fassregel:"));
        Kepler keppler = analysis.getKeplerIntegral(funk, x2, x1);
        m_BerechnungenCtrl.addErgebniseintrag(new CompoundEintrag(new MathMLEintrag("<mi>A</mi><mo>&approx;</mo>"), new MathMLEintrag(MML.getNumber(keppler.a) + MML.getEinheit("FE"))));
        m_BerechnungenCtrl.addErgebniseintrag(new CompoundEintrag(new MathMLEintrag("<msub><mi>y</mi><mn>0</mn></msub><mo>=</mo>" + MML.getAvonB("f", "a")), new MathMLEintrag(MML.getNumber(keppler.y0))));
        m_BerechnungenCtrl.addErgebniseintrag(new CompoundEintrag(new MathMLEintrag("<msub><mi>y</mi><mn>1</mn></msub><mo>=</mo>"), new MathMLEintrag(MML.getNumber(keppler.y1))));
        m_BerechnungenCtrl.addErgebniseintrag(new CompoundEintrag(new MathMLEintrag("<msub><mi>y</mi><mn>2</mn></msub><mo>=</mo>" + MML.getAvonB("f", "b")), new MathMLEintrag(MML.getNumber(keppler.y2))));
    }
}
