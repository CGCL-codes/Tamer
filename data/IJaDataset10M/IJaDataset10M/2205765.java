package net.sf.appomatox.gui.diagramm.elemente;

import javax.swing.JTextField;
import net.sf.appomatox.gui.DiagrammController;
import net.sf.appomatox.gui.diagramm.plots.Plottbar;
import net.sf.appomatox.gui.diagramm.plots.PlottbareBeschriftung;
import net.sf.appomatox.utils.MML;
import net.sf.appomatox.utils.MMLComponentBuilder;
import net.sf.appomatox.utils.Parser;
import net.sf.appomatox.utils.hintTextField.HintTextField;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class BeschriftungTab extends BaseTab {

    private JTextField m_edtText = new JTextField();

    private JTextField m_edtY = new HintTextField("", Parser.getDoubleChecker());

    private JTextField m_edtX = new HintTextField("", Parser.getDoubleChecker());

    public BeschriftungTab(DiagrammController dc) {
        super(dc);
        initGUI();
    }

    private void initGUI() {
        FormLayout layout = new FormLayout(getColumnSpec(), "p, 1dlu, p, 10dlu, p, 1dlu, p, 13dlu, p");
        PanelBuilder builder = createPanelBuilder(layout);
        CellConstraints cc = new CellConstraints();
        MMLComponentBuilder mmlCB = MMLComponentBuilder.getInstance();
        builder.addLabel("Text", cc.xyw(1, 1, 7));
        builder.add(m_edtText, cc.xyw(1, 3, 7));
        builder.addLabel("Position", cc.xyw(1, 5, 7));
        builder.add(mmlCB.getComponent(MML.getAGleich("x")), cc.xyw(1, 7, 1));
        builder.add(m_edtX, cc.xyw(3, 7, 1));
        builder.add(mmlCB.getComponent(MML.getAGleich("y")), cc.xyw(5, 7, 1));
        builder.add(m_edtY, cc.xyw(7, 7, 1));
        builder.add(getBtnFarbe(), cc.xyw(1, 9, 3, "left, bottom"));
    }

    @Override
    protected Plottbar doExecute() {
        String text = m_edtText.getText();
        double x = Parser.parseDouble(m_edtX.getText());
        double y = Parser.parseDouble(m_edtY.getText());
        return new PlottbareBeschriftung(text, x, y, getFarbe());
    }

    @Override
    public String toString() {
        return "Beschriftung";
    }

    @Override
    public void setPlottbar(Plottbar p) {
        if (!(p instanceof PlottbareBeschriftung)) throw new IllegalArgumentException();
        super.setPlottbar(p);
        PlottbareBeschriftung pb = (PlottbareBeschriftung) p;
        this.setFarbe(pb.getColor());
        m_edtText.setText(pb.getText());
        m_edtX.setText(pb.getX());
        m_edtY.setText(pb.getY());
    }
}
