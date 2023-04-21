package org.rollinitiative.d20web.charactersheet.client;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * @author bebopjmm
 *
 */
public class CharacterView extends Composite {

    private final DescriptionView descriptView_;

    private final AbilityView abilityView_;

    private final SavesView savesView_;

    private final HealthView healthView_;

    private final SkillsView skillsView_;

    private final TalentsView talentsView_;

    private final InitiativeView initiativeView_;

    private final AttackView attackView_;

    private final DefenseView defenseView_;

    public CharacterView() {
        VerticalPanel mainPanel = new VerticalPanel();
        descriptView_ = new DescriptionView();
        abilityView_ = new AbilityView();
        savesView_ = new SavesView();
        healthView_ = new HealthView();
        skillsView_ = new SkillsView();
        talentsView_ = new TalentsView();
        initiativeView_ = new InitiativeView();
        attackView_ = new AttackView();
        defenseView_ = new DefenseView();
        VerticalPanel staticPanel = new VerticalPanel();
        staticPanel.setBorderWidth(1);
        staticPanel.setSpacing(2);
        staticPanel.add(descriptView_);
        staticPanel.add(healthView_);
        TabPanel tabPanel = new TabPanel();
        tabPanel.setWidth("100%");
        HorizontalPanel statsPanel = new HorizontalPanel();
        statsPanel.setBorderWidth(1);
        statsPanel.setSpacing(2);
        statsPanel.add(abilityView_);
        statsPanel.add(savesView_);
        HorizontalPanel talentsPanel = new HorizontalPanel();
        talentsPanel.setBorderWidth(1);
        talentsPanel.setSpacing(2);
        talentsPanel.add(skillsView_);
        talentsPanel.add(talentsView_);
        VerticalPanel combatPanel = new VerticalPanel();
        combatPanel.setBorderWidth(1);
        combatPanel.setSpacing(2);
        combatPanel.add(initiativeView_);
        HorizontalPanel attackDefensePanel = new HorizontalPanel();
        attackDefensePanel.setBorderWidth(1);
        attackDefensePanel.setSpacing(2);
        attackDefensePanel.add(attackView_);
        attackDefensePanel.add(defenseView_);
        combatPanel.add(attackDefensePanel);
        tabPanel.add(statsPanel, "Stats");
        tabPanel.add(talentsPanel, "Talents");
        tabPanel.add(combatPanel, "Combat");
        tabPanel.add(new HTML("Equipment and Resources"), "Equipment");
        tabPanel.add(new HTML("Prepared Spells"), "Spells");
        mainPanel.add(staticPanel);
        mainPanel.add(tabPanel);
        tabPanel.selectTab(0);
        initWidget(mainPanel);
    }

    public void setCharacter(CharacterData character) {
        descriptView_.setCharacter(character);
        abilityView_.setCharacter(character.getAbilities());
        healthView_.setCharacter(character.getHealth());
        savesView_.setCharacter(character.getSaves());
        skillsView_.setCharacter(character.getSkills());
        talentsView_.setCharacter(character.getSkills());
        initiativeView_.setChararacter(character.getCombat());
        attackView_.setCharacter(character.getCombat());
        defenseView_.setCharacter(character.getCombat());
    }
}
