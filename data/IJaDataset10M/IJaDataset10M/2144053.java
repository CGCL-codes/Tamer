package hokutonorogue.game;

import java.awt.*;
import com.golden.gamedev.*;
import com.golden.gamedev.object.*;
import com.golden.gamedev.object.background.*;
import hokutonorogue.character.*;
import hokutonorogue.level.*;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: </p>
 *
 * @author Alessio Carotenuto
 * @version 1.0
 */
public class NewCharacterScreen extends CharacterUpgradeScreen {

    protected CharacterPreviewPanel characterPreviewPanel = null;

    protected Background bg = null;

    public NewCharacterScreen(GameEngine parent) {
        super(parent);
        pointsToDistribute = 30;
        skillPointsToDistribute = 5;
        this.character = new CustomCharacter();
        characterPreviewPanel = new CharacterPreviewPanel(this.character);
        characterPreviewPanel.setScale(2);
        characterPreviewPanel.setWidth(100);
        characterPreviewPanel.setHeight(100);
        characterPreviewPanel.setX(180);
        characterPreviewPanel.setY(390);
        this.addRenderer(characterPreviewPanel);
        int x = 45;
        selectableElements.add(new NewCharacterNameSelectableElement(this, x, 100));
        selectableElements.get(selectableElements.size() - 1).setHeadGroup(true);
        int y = 110;
        selectableElements.add(new NewCharacterAttributeSelectableElement(this, character.getStrenght(), x, y += 20));
        selectableElements.add(new NewCharacterAttributeSelectableElement(this, character.getAgility(), x, y += 20));
        selectableElements.add(new NewCharacterAttributeSelectableElement(this, character.getResistence(), x, y += 20));
        selectableElements.add(new NewCharacterAttributeSelectableElement(this, character.getSpeed(), x, y += 20));
        selectableElements.add(new NewCharacterAttributeSelectableElement(this, character.getDefence(), x, y += 20));
        selectableElements.add(new NewCharacterAttributeSelectableElement(this, character.getConcentration(), x, y += 20));
        selectableElements.add(new NewCharacterAttributeSelectableElement(this, character.getKi(), x, y += 20));
        selectableElements.add(new NewCharacterBodySelectableElement(this, x, y += 170));
        selectableElements.get(selectableElements.size() - 1).setHeadGroup(true);
        selectableElements.add(new NewCharacterHairsSelectableElement(this, x, y += 30));
        selectableElements.add(new NewCharacterAnimationSelectableElement(this, x, y += 60));
        selectableElements.add(new NewCharacterVoiceElement(this, x, y += 130));
        selectableElements.get(selectableElements.size() - 1).setHeadGroup(true);
        x = 405;
        y = 80;
        for (int i = 0; i < FightingStyle.FightingStyles.values().length; i++) {
            FightingStyle fs = FightingStyle.FightingStyles.values()[i].style();
            selectableElements.add(new NewCharacterStyleElement(this, fs, x, y += 20));
            if (i == 0) {
                selectableElements.get(selectableElements.size() - 1).setHeadGroup(true);
            }
        }
        x = 405;
        y = 160;
        int i = 0;
        for (Skill s : character.getSkills()) {
            selectableElements.add(new CheckSelectableElement(this, s, x, y += 20));
            if (i == 0) {
                selectableElements.get(selectableElements.size() - 1).setHeadGroup(true);
            }
            i++;
        }
        selectedElement = selectableElements.get(0);
        x = 705;
        y = 160;
        i = 0;
        for (Perk p : character.getPerks()) {
            selectableElements.add(new CheckSelectableElement(this, p, x, y += 20));
            if (i == 0) {
                selectableElements.get(selectableElements.size() - 1).setHeadGroup(true);
            }
            i++;
        }
        selectedElement = selectableElements.get(0);
    }

    public void _initResources() {
        bg = new ImageBackground(getImage("resources/wallpaper.png"), MainGame.PLAYFIELD_WIDTH, MainGame.PLAYFIELD_HEIGHT);
        setFPS(60);
        fadeIn(0.04f, 5, Color.BLACK);
    }

    public void _update(long elapsedTime) {
        if (bsInput.isKeyPressed(ESCAPE)) {
            ChoiceMenu choiceMenu = new ChoiceMenu(parent, this);
            choiceMenu.setMessage("BACK TO MAIN MENU?");
            choiceMenu.addChoice(SimpleChoice.YES_CHOICE);
            choiceMenu.addChoice(SimpleChoice.NO_CHOICE);
            choiceMenu.start();
            Choice choice = choiceMenu.getSelectedChoice();
            if (choice != SimpleChoice.CANCEL_CHOICE) {
                if (choice.equals(SimpleChoice.YES_CHOICE)) {
                    ((HokutoNoRogue) parent).showTitleScreen();
                }
            }
        } else if (bsInput.isKeyPressed(ENTER)) {
            if (character.getName().length() == 0) {
                ChoiceMenu choiceMenu = new ChoiceMenu(parent, this);
                choiceMenu.setMessage("CHARACTER MUST HAVE A NAME");
                choiceMenu.addChoice(SimpleChoice.OK_CHOICE);
                choiceMenu.start();
            } else {
                ChoiceMenu choiceMenu = new ChoiceMenu(parent, this);
                choiceMenu.setMessage("BEGIN THE GAME WITH THIS CHARACTER?");
                choiceMenu.addChoice(SimpleChoice.YES_CHOICE);
                choiceMenu.addChoice(SimpleChoice.NO_CHOICE);
                choiceMenu.start();
                Choice choice = choiceMenu.getSelectedChoice();
                if (choice != SimpleChoice.CANCEL_CHOICE) {
                    if (choice.equals(SimpleChoice.YES_CHOICE)) {
                        for (AbstractSelectableElement elem : selectableElements) {
                            if (elem instanceof CheckSelectableElement) {
                                CheckSelectableElement cse = (CheckSelectableElement) elem;
                                if (cse.isEnabled() && cse.isSelected()) {
                                    cse.accept();
                                }
                            }
                        }
                        ((CustomCharacter) character).finalizeCharacter();
                        ((HokutoNoRogue) parent).newGame(character);
                        character.getSprite().setActive(false);
                    }
                }
            }
        } else if (bsInput.isKeyPressed(UP)) {
            int index = selectableElements.indexOf(selectedElement);
            if (index > 0) {
                selectedElement = selectableElements.get(index - 1);
            }
        } else if (bsInput.isKeyPressed(DOWN)) {
            int index = selectableElements.indexOf(selectedElement);
            if (index < selectableElements.size() - 1) {
                selectedElement = selectableElements.get(index + 1);
            }
        } else if (bsInput.isKeyPressed(TAB)) {
            int index = selectableElements.indexOf(selectedElement);
            do {
                index = (index + 1) % selectableElements.size();
                selectedElement = selectableElements.get(index);
            } while (!selectedElement.isHeadGroup());
        } else if (bsInput.isKeyPressed(H) && bsInput.isKeyDown(SHIFT)) {
            HokutoNoRogue.setShowHintsEnabled(!HokutoNoRogue.isShowHintsEnabled());
            HokutoNoRogue.saveOptions();
        } else {
            selectedElement.update(bsInput, elapsedTime);
        }
    }

    protected void _renderBackground(Graphics2D g) {
        bg.render(g);
        drawPanel(g, 20, 50, 330, 300);
        drawPanel(g, 20, 370, 330, 195);
        drawPanel(g, 20, 585, 330, 95);
        drawPanel(g, 380, 50, 610, 630);
        g.setColor(Color.LIGHT_GRAY);
        g.fillRoundRect(180, 500, 100, 55, 10, 10);
    }

    protected void _render(Graphics2D g) {
        font.drawString(g, "NEW CHARACTER", MainGame.PLAYFIELD_WIDTH / 2 - 140, 20);
        for (AbstractSelectableElement elem : selectableElements) {
            elem.render(g);
        }
        font.drawString(g, ">", selectedElement.x - 15, selectedElement.y);
        int x = 45;
        int y = 70;
        choiceFont.drawString(g, "STATISTICS", x, y);
        font.drawString(g, "AVAILABLE PTS: " + pointsToDistribute, x, 290);
        y = 390;
        choiceFont.drawString(g, "LOOK", x, y);
        y = 605;
        choiceFont.drawString(g, "SOUND", x, y);
        x = 405;
        y = 70;
        choiceFont.drawString(g, "FIGHTING STYLES", x, y);
        y = 150;
        choiceFont.drawString(g, "SKILLS", x, y);
        font.drawString(g, "AVAILABLE PTS: " + skillPointsToDistribute, x, 640);
        x = 705;
        choiceFont.drawString(g, "PERKS", x, y);
        font.drawString(g, "PRESS SHIFT+H FOR HELP", 700, 690);
    }

    protected void _renderForeground(Graphics2D g) {
        if (HokutoNoRogue.isShowHintsEnabled()) {
            drawHint(g, selectedElement.getX(), selectedElement.getY(), selectedElement.getName(), selectedElement.getHint());
        }
    }
}
