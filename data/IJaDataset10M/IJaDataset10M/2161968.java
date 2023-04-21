package net.sf.gham.plugins.panels.player.detail.player;

import java.awt.GridLayout;
import net.sf.gham.core.entity.player.PlayerMyTeam;
import net.sf.gham.swing.util.TextWithLabel;
import net.sf.gham.swing.util.TextWithLabelPanel;
import net.sf.jtwa.Messages;

/**
 * @author fabio
 *
 */
public class InfoPanel extends TextWithLabelPanel {

    private TextWithLabel ageLabel;

    private TextWithLabel tsiLabel;

    private TextWithLabel salaryLabel;

    /**
	 * 
	 */
    public InfoPanel() {
        GridLayout gridLayout4 = new GridLayout();
        ageLabel = creaText(Messages.getString("Age"), 1);
        tsiLabel = creaText(Messages.getString("Tsi"), 2);
        salaryLabel = creaText(Messages.getString("Salary"), 3);
        setBorder(javax.swing.BorderFactory.createTitledBorder(null, Messages.getString("Informations"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
        setLayout(gridLayout4);
        gridLayout4.setRows(1);
        gridLayout4.setColumns(3);
        add(ageLabel, null);
        add(tsiLabel, null);
        add(salaryLabel, null);
    }

    public void setPlayer(PlayerMyTeam p) {
        ageLabel.setText(Integer.toString(p.getAge()));
        tsiLabel.setText(Integer.toString((int) p.getTsi().getValue()));
        salaryLabel.setText(Integer.toString((int) p.getSalary().getValue()));
    }
}
