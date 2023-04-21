package ogv.gui;

import java.awt.*;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import javax.swing.*;
import ogv.data.Race;
import ogv.data.TechBlock;
import ogv.data.constants.Technology;
import ogv.data.controls.Game;
import ogv.util.SwingUtils;
import ogv.util.Utils;
import ogv.util.ValueLabel;

public final class RaceInfo extends JPanel {

    private static final int HGAP = 10;

    private Game game;

    private final JLabel valueCount = new ValueLabel();

    private final JLabel labelCount = new JLabel();

    private final JLabel valueName = new ValueLabel();

    private final JLabel valueVotes = new ValueLabel();

    private final JLabel valuePop = new ValueLabel();

    private final JLabel valueInd = new ValueLabel();

    private final JLabel valueProd = new ValueLabel();

    private final EnumMap<Technology, JLabel> valueTech = new EnumMap<Technology, JLabel>(Technology.class);

    public RaceInfo(Game game) {
        this.game = game;
        initGUI();
        setBorder(BorderFactory.createRaisedBevelBorder());
        setRaces(Collections.<Race>emptyList());
    }

    private void initGUI() {
        Box panel = Box.createHorizontalBox();
        add(panel);
        panel.add(SwingUtils.makeVBox(SwingUtils.makeHBox(null, valueCount, labelCount, null), SwingUtils.makeHBox(null, valueName, null), SwingUtils.makeHBox(null, new JLabel("V="), valueVotes, new JLabel("%"), null), null));
        panel.add(Box.createHorizontalStrut(HGAP));
        Container panel_1 = new JPanel(new GridBagLayout(), false);
        SwingUtils.addRow0(panel_1, new JLabel("P"), new JLabel("="), valuePop);
        SwingUtils.addRow0(panel_1, new JLabel("I"), new JLabel("="), valueInd);
        SwingUtils.addRow0(panel_1, new JLabel("L"), new JLabel("="), valueProd);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weighty = 1;
        panel_1.add(Box.createVerticalGlue(), gbc);
        panel.add(panel_1);
        panel.add(Box.createHorizontalStrut(HGAP));
        Container panel_2 = new JPanel(new GridBagLayout(), false);
        for (Technology t : Technology.values()) {
            ValueLabel label = new ValueLabel();
            valueTech.put(t, label);
            SwingUtils.addRow0(panel_2, new JLabel(t.toReportShort()), new JLabel("="), label);
        }
        panel.add(panel_2);
    }

    public void updateTurn(Game game) {
        this.game = game;
        setRace(null);
    }

    public void setRace(Race r) {
        if (r == null) setRaces(Collections.<Race>emptyList()); else setRaces(Collections.singleton(r));
    }

    public void setRaces(Collection<Race> races) {
        double sumPop = 0;
        double sumInd = 0;
        double sumProd = 0;
        TechBlock avgTech = new TechBlock();
        for (Race r : races) {
            sumPop += r.getPopulation();
            sumInd += r.getIndustry();
            double prod = r.getProduction();
            sumProd += prod;
            for (Technology t : Technology.values()) avgTech.set(t, avgTech.get(t) + r.getTech().get(t) * prod);
        }
        double totalPop = 0;
        for (Race r : game.getRaces()) totalPop += r.getPopulation();
        if (sumProd > 0) {
            for (Technology t : Technology.values()) avgTech.set(t, avgTech.get(t) / sumProd);
        }
        if (races.size() == 1) {
            valueCount.setText(null);
            labelCount.setText(null);
            valueName.setText(races.iterator().next().getName());
        } else {
            valueCount.setText(String.valueOf(races.size()));
            labelCount.setText(" races");
            valueName.setText(null);
        }
        valueVotes.setText(Utils.d2(totalPop > 0 ? 100.0 * sumPop / totalPop : 0));
        valuePop.setText(Utils.d2(sumPop));
        valueInd.setText(Utils.d2(sumInd));
        valueProd.setText(Utils.d2(sumProd));
        for (Technology t : Technology.values()) valueTech.get(t).setText(Utils.d2(avgTech.get(t)));
        revalidate();
    }
}
