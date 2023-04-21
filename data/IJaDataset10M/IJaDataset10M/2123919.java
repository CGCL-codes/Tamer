package org.rollinitiative.d20web.charactersheet.client;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;

/**
 * 
 * @author jmccormi
 * 
 */
public class DescriptionView extends Composite {

    private final TextBox charNameText_ = new TextBox();

    private final TextBox playerText_ = new TextBox();

    private final TextBox alignText_ = new TextBox();

    private final TextBox raceText_ = new TextBox();

    private final TextBox genderText_ = new TextBox();

    private final TextBox sizeText_ = new TextBox();

    private final TextBox classText_ = new TextBox();

    private final TextBox levelText_ = new TextBox();

    private final TextBox speedText_ = new TextBox();

    private final TextBox xpText_ = new TextBox();

    public DescriptionView() {
        int row = 0;
        int col = 0;
        Grid viewGrid = new Grid(5, 4);
        viewGrid.setWidget(row, col++, new Label("Name"));
        viewGrid.setWidget(row, col++, charNameText_);
        charNameText_.setText("Sample Character");
        charNameText_.setReadOnly(true);
        viewGrid.setWidget(row, col++, new Label("Player Name"));
        viewGrid.setWidget(row, col++, playerText_);
        playerText_.setText("New Player");
        row = 1;
        col = 0;
        viewGrid.setWidget(row, col++, new Label("Race"));
        viewGrid.setWidget(row, col++, raceText_);
        raceText_.setText("Race");
        raceText_.setReadOnly(true);
        viewGrid.setWidget(row, col++, new Label("Gender"));
        viewGrid.setWidget(row, col++, genderText_);
        genderText_.setText("Gender");
        genderText_.setReadOnly(true);
        row = 2;
        col = 0;
        viewGrid.setWidget(row, col++, new Label("Alignment"));
        viewGrid.setWidget(row, col++, alignText_);
        alignText_.setText("Alignment");
        alignText_.setReadOnly(true);
        viewGrid.setWidget(row, col++, new Label("Size"));
        viewGrid.setWidget(row, col++, sizeText_);
        sizeText_.setText("Medium");
        sizeText_.setReadOnly(true);
        row = 3;
        col = 0;
        viewGrid.setWidget(row, col++, new Label("Class"));
        viewGrid.setWidget(row, col++, classText_);
        classText_.setText("Class-Info");
        classText_.setReadOnly(true);
        viewGrid.setWidget(row, col++, new Label("Character Level"));
        viewGrid.setWidget(row, col++, levelText_);
        levelText_.setText("Level-Info");
        levelText_.setReadOnly(true);
        row = 4;
        col = 0;
        viewGrid.setWidget(row, col++, new Label("Speed"));
        viewGrid.setWidget(row, col++, speedText_);
        speedText_.setText("0");
        speedText_.setReadOnly(true);
        viewGrid.setWidget(row, col++, new Label("Experience"));
        viewGrid.setWidget(row, col++, xpText_);
        xpText_.setText("0");
        xpText_.setReadOnly(true);
        initWidget(viewGrid);
    }

    public void setCharacter(CharacterData character) {
        System.out.println("Setting descriptive character data");
        charNameText_.setText(character.getCharacterName());
        playerText_.setText(character.getPlayerName());
        genderText_.setText(character.getGender());
        raceText_.setText(character.getRace());
        alignText_.setText(character.getAlignment());
        classText_.setText(character.getClassInfo());
        levelText_.setText(character.getLevel());
        speedText_.setText(character.getSpeed());
        sizeText_.setText(character.getSize());
        xpText_.setText(character.getExperience());
    }
}
