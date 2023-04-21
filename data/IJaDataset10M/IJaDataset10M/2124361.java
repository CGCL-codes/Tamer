package net.sf.appomatox.gui.diagramm.elemente;

import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import net.sf.appomatox.gui.DiagrammController;
import net.sf.appomatox.gui.diagramm.plots.Plottbar;
import net.sf.appomatox.gui.diagramm.plots.PlottbareStelle;
import net.sf.appomatox.utils.MML;
import net.sf.appomatox.utils.MMLComponentBuilder;
import net.sf.appomatox.utils.Parser;
import net.sf.appomatox.utils.Utils;
import net.sf.appomatox.utils.hintTextField.HintTextField;
import org.jdesktop.swingx.JXImagePanel;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class StellenTab extends BaseTab {

    private JTextField m_TextField = new JTextField();

    private JTextField m_edtX = new HintTextField("", Parser.getDoubleChecker());

    private JTextField m_edtY = new HintTextField("", Parser.getDoubleChecker());

    private ButtonGroup m_ButtonGroup = new ButtonGroup();

    private JRadioButton m_chkVertikal;

    private JRadioButton m_chkHorizontal;

    private JXImagePanel m_ImagePanel;

    public StellenTab(DiagrammController dc) {
        super(dc);
        initGUI();
    }

    private void initGUI() {
        FormLayout layout = new FormLayout(getColumnSpecMitSkizze(), "p, 1dlu, p, 10dlu, p, 1dlu, p, 10dlu, p, 3dlu, p, 10dlu, p");
        PanelBuilder builder = createPanelBuilder(layout);
        CellConstraints cc = new CellConstraints();
        MMLComponentBuilder mmlCB = MMLComponentBuilder.getInstance();
        builder.addLabel("Text", cc.xyw(3, 1, 7));
        builder.add(m_TextField, cc.xyw(3, 3, 7));
        builder.addLabel("Position", cc.xyw(3, 5, 7));
        builder.add(mmlCB.getComponent(MML.getAGleich("x")), cc.xyw(3, 7, 1));
        builder.add(m_edtX, cc.xyw(5, 7, 1));
        builder.add(mmlCB.getComponent(MML.getAGleich("y")), cc.xyw(7, 7, 1));
        builder.add(m_edtY, cc.xyw(9, 7, 1));
        builder.addLabel("Ausrichtung", cc.xyw(3, 9, 7));
        builder.add(getChkHorizontal(), cc.xyw(3, 11, 3));
        builder.add(getChkVertikal(), cc.xyw(7, 11, 3));
        builder.add(getBtnFarbe(), cc.xyw(3, 13, 3, "left, bottom"));
        builder.add(getImageView(), cc.xywh(1, 1, 1, 11, "center, top"));
    }

    @Override
    protected Plottbar doExecute() {
        String text = m_TextField.getText();
        double x = Parser.parseDouble(m_edtX.getText());
        double y = Parser.parseDouble(m_edtY.getText());
        boolean horizontal = m_chkHorizontal.isSelected();
        return new PlottbareStelle(text, x, y, horizontal, getFarbe());
    }

    private JXImagePanel getImageView() {
        if (m_ImagePanel == null) {
            m_ImagePanel = new JXImagePanel();
            m_ImagePanel.setImage(Utils.getIcon("/res/sonstige/Diagrammelement-Stellen.png").getImage());
            m_ImagePanel.setEditable(false);
            m_ImagePanel.setOpaque(false);
        }
        return m_ImagePanel;
    }

    private JRadioButton getChkHorizontal() {
        if (m_chkHorizontal == null) {
            m_chkHorizontal = new JRadioButton();
            m_chkHorizontal.setText("horizontal");
            m_chkHorizontal.setSelected(true);
            m_chkHorizontal.setOpaque(false);
            m_ButtonGroup.add(m_chkHorizontal);
        }
        return m_chkHorizontal;
    }

    private JRadioButton getChkVertikal() {
        if (m_chkVertikal == null) {
            m_chkVertikal = new JRadioButton();
            m_chkVertikal.setText("vertikal");
            m_chkVertikal.setOpaque(false);
            m_ButtonGroup.add(m_chkVertikal);
        }
        return m_chkVertikal;
    }

    @Override
    public String toString() {
        return "Stelle";
    }

    @Override
    public void setPlottbar(Plottbar p) {
        if (!(p instanceof PlottbareStelle)) throw new IllegalArgumentException();
        super.setPlottbar(p);
        PlottbareStelle pb = (PlottbareStelle) p;
        this.setFarbe(pb.getColor());
        m_TextField.setText(pb.getText());
        boolean bHorizontal = pb.getHorizontal();
        if (bHorizontal) {
            m_chkHorizontal.setSelected(true);
        } else {
            m_chkVertikal.setSelected(true);
        }
        m_edtX.setText(pb.getX());
        m_edtY.setText(pb.getY());
    }
}
