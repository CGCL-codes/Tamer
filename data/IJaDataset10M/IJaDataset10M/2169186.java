package pcgen.gui.prefs;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import pcgen.core.GameMode;
import pcgen.core.RuleCheck;
import pcgen.core.SettingsHandler;
import pcgen.gui.utils.Utility;
import pcgen.util.PropertyFactory;

/**
 * The Class <code>HouseRulesPanel</code> is responsible for 
 * displaying the house rules preferences and allowing the 
 * preferences to be edited by the user.
 * 
 * Last Editor: $Author: jdempsey $
 * Last Edited: $Date: 2009-09-23 19:11:51 -0400 (Wed, 23 Sep 2009) $
 * 
 * @author James Dempsey <jdempsey@users.sourceforge.net>
 * @version $Revision: 10579 $
 */
@SuppressWarnings("serial")
public class HouseRulesPanel extends PCGenPrefsPanel {

    private static String in_houseRules = PropertyFactory.getString("in_Prefs_houseRules");

    private List<RuleCheck> ruleCheckList = new ArrayList<RuleCheck>();

    private JCheckBox[] hrBoxes = null;

    private ButtonGroup[] hrGroup = null;

    private JRadioButton[] hrRadio = null;

    /**
	 * Instantiates a new house rules panel.
	 */
    public HouseRulesPanel() {
        JPanel mainPanel = new JPanel();
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        JLabel label;
        Border etched = null;
        TitledBorder title1 = BorderFactory.createTitledBorder(etched, in_houseRules);
        title1.setTitleJustification(TitledBorder.LEFT);
        mainPanel.setBorder(title1);
        gridbag = new GridBagLayout();
        mainPanel.setLayout(gridbag);
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(2, 2, 2, 2);
        Utility.buildConstraints(c, 0, 0, 2, 1, 0, 0);
        label = new JLabel(PropertyFactory.getString("in_Prefs_hrCrossSkillCost") + ": ");
        gridbag.setConstraints(label, c);
        mainPanel.add(label);
        int gridNum = 1;
        GameMode gameMode = SettingsHandler.getGame();
        ruleCheckList = gameMode.getRuleCheckList();
        hrBoxes = new JCheckBox[ruleCheckList.size()];
        int excludeCount = 0;
        int boxNum = 0;
        for (RuleCheck aRule : ruleCheckList) {
            aRule.getName();
            String aKey = aRule.getKey();
            String aDesc = aRule.getDesc();
            boolean aBool = aRule.getDefault();
            if (aRule.isExclude()) {
                ++excludeCount;
                continue;
            }
            if (SettingsHandler.hasRuleCheck(aKey)) {
                aBool = SettingsHandler.getRuleCheck(aKey);
            }
            hrBoxes[boxNum] = new JCheckBox(aKey, aBool);
            Utility.buildConstraints(c, 0, gridNum, 2, 1, 0, 0);
            label = new JLabel(aDesc);
            gridbag.setConstraints(label, c);
            mainPanel.add(label);
            Utility.buildConstraints(c, 2, gridNum, 1, 1, 0, 0);
            gridbag.setConstraints(hrBoxes[boxNum], c);
            mainPanel.add(hrBoxes[boxNum]);
            ++boxNum;
            ++gridNum;
        }
        hrRadio = new JRadioButton[excludeCount];
        int exNum = 0;
        for (RuleCheck aRule : ruleCheckList) {
            aRule.getName();
            String aKey = aRule.getKey();
            aRule.getDesc();
            boolean aBool = aRule.getDefault();
            if (!aRule.isExclude()) {
                continue;
            }
            hrRadio[exNum] = new JRadioButton(aKey);
            if (SettingsHandler.hasRuleCheck(aKey)) {
                aBool = SettingsHandler.getRuleCheck(aKey);
            }
            hrRadio[exNum].setSelected(aBool);
            ++exNum;
        }
        hrGroup = new ButtonGroup[excludeCount];
        addRulesToPanel(mainPanel, gridbag, gridNum, gameMode);
        Utility.buildConstraints(c, 5, 60, 1, 1, 1, 1);
        c.fill = GridBagConstraints.BOTH;
        label = new JLabel(" ");
        gridbag.setConstraints(label, c);
        mainPanel.add(label);
        this.setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);
    }

    /**
	 * Add a control to the panel for each of the game mode's house rule options.
	 * 
	 * @param mainPanel The panel to add the entries to.
	 * @param gridbag The panel's layout 
	 * @param gridNum The current row in the layout grid
	 * @param gameMode The game mode being processed
	 */
    private void addRulesToPanel(JPanel mainPanel, GridBagLayout gridbag, int gridNum, GameMode gameMode) {
        int groupNum = 0;
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(2, 2, 2, 2);
        List<String> doneList = new ArrayList<String>();
        for (int i = 0; i < hrRadio.length; i++) {
            if (hrRadio[i] == null) {
                continue;
            }
            String aKey = hrRadio[i].getText();
            RuleCheck aRule = gameMode.getRuleByKey(aKey);
            if (aRule == null) {
                continue;
            }
            String aDesc = aRule.getDesc();
            String altKey = aRule.getExcludeKey();
            if (doneList.contains(aKey) || doneList.contains(altKey)) {
                continue;
            }
            hrGroup[groupNum] = new ButtonGroup();
            hrGroup[groupNum].add(hrRadio[i]);
            doneList.add(aKey);
            Utility.buildConstraints(c, 0, gridNum, 3, 1, 0, 0);
            JPanel subPanel = new JPanel();
            gridbag.setConstraints(subPanel, c);
            subPanel.setLayout(gridbag);
            GridBagConstraints cc = new GridBagConstraints();
            cc.fill = GridBagConstraints.HORIZONTAL;
            cc.insets = new Insets(0, 4, 0, 0);
            Border aBord = BorderFactory.createEtchedBorder();
            subPanel.setBorder(aBord);
            JLabel label = new JLabel(aDesc);
            cc.anchor = GridBagConstraints.WEST;
            Utility.buildConstraints(cc, 0, 0, 2, 1, 2, 0);
            gridbag.setConstraints(label, cc);
            subPanel.add(label);
            cc.anchor = GridBagConstraints.EAST;
            Utility.buildConstraints(cc, 2, 0, 1, 1, 1, 0);
            gridbag.setConstraints(hrRadio[i], cc);
            subPanel.add(hrRadio[i]);
            for (int ii = 0; ii < hrRadio.length; ii++) {
                if (hrRadio[i] == null) {
                    continue;
                }
                String exKey = hrRadio[ii].getText();
                if (exKey.equals(altKey)) {
                    aRule = gameMode.getRuleByKey(exKey);
                    aDesc = aRule.getDesc();
                    hrGroup[groupNum].add(hrRadio[ii]);
                    doneList.add(altKey);
                    JLabel descLabel = new JLabel(aDesc);
                    cc.anchor = GridBagConstraints.WEST;
                    Utility.buildConstraints(cc, 0, 1, 2, 1, 2, 0);
                    gridbag.setConstraints(descLabel, cc);
                    subPanel.add(descLabel);
                    cc.anchor = GridBagConstraints.EAST;
                    Utility.buildConstraints(cc, 2, 1, 1, 1, 1, 0);
                    gridbag.setConstraints(hrRadio[ii], cc);
                    subPanel.add(hrRadio[ii]);
                }
            }
            mainPanel.add(subPanel);
            ++gridNum;
            ++groupNum;
        }
    }

    @Override
    public String getTitle() {
        return in_houseRules;
    }

    @Override
    public void setOptionsBasedOnControls() {
        final GameMode gameMode = SettingsHandler.getGame();
        for (int i = 0; i < hrBoxes.length; i++) {
            if (hrBoxes[i] != null) {
                String aKey = hrBoxes[i].getText();
                boolean aBool = hrBoxes[i].isSelected();
                if (gameMode.hasRuleCheck(aKey)) {
                    SettingsHandler.setRuleCheck(aKey, aBool);
                }
            }
        }
        for (int i = 0; i < hrRadio.length; i++) {
            if (hrRadio[i] != null) {
                String aKey = hrRadio[i].getText();
                boolean aBool = hrRadio[i].isSelected();
                if (gameMode.hasRuleCheck(aKey)) {
                    SettingsHandler.setRuleCheck(aKey, aBool);
                }
            }
        }
    }

    @Override
    public void applyOptionValuesToControls() {
    }
}
