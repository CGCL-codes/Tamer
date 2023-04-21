package com.googlecode.jumpnevolve.game.menu;

import java.io.IOException;
import org.newdawn.slick.util.Log;
import com.googlecode.jumpnevolve.editor.Editor;
import com.googlecode.jumpnevolve.graphics.Engine;
import com.googlecode.jumpnevolve.graphics.gui.InterfaceFunction;
import com.googlecode.jumpnevolve.graphics.gui.InterfaceFunctions;
import com.googlecode.jumpnevolve.graphics.gui.objects.InterfaceObject;
import com.googlecode.jumpnevolve.util.Parameter;

public class MainMenu extends Menu {

    private SimpleSubMenu mainState;

    public MainMenu(String levelPath) {
        super();
        this.mainState = new SimpleSubMenu(this, "MainMenu");
        super.addSubMenu(mainState);
        this.addSubMenu(new LevelSelection(this, "demo-levels/", "AlphaLevels"));
        this.addSubMenu(new LevelSelection(this, levelPath, "TestLevels"));
        this.addSubMenu(new LevelSelection(this, Parameter.PROGRAMM_DIRECTORY_LEVELS, "OwnLevels"));
        this.addDirectButton("Editor", InterfaceFunctions.MENU_EDITOR);
        this.addDirectButton("Jump'n'evolve beenden", InterfaceFunctions.GAME_EXIT);
    }

    public void addSubMenu(SubMenu menu) {
        this.mainState.addEntry(menu.name, menu);
        super.addSubMenu(menu);
    }

    public void addDirectButton(String name, InterfaceFunction function) {
        this.mainState.addEntry(name, function);
    }

    @Override
    public void mouseClickedAction(InterfaceObject object) {
        InterfaceFunction function = object.getFunction();
        if (function instanceof SubMenu) {
            this.switchTo(function.getFunctionName().split("_")[1]);
        } else if (function == InterfaceFunctions.MENU_EDITOR) {
            try {
                Engine.getInstance().switchState(new Editor(this, this.getWidth(), this.getHeight(), 1));
            } catch (IOException e) {
                Log.error("Fehler beim Erstellen des Editors: " + e.getMessage());
                e.printStackTrace();
            }
        } else if (function == InterfaceFunctions.GAME_EXIT) {
            System.exit(0);
        }
    }

    @Override
    public void mouseOverAction(InterfaceObject object) {
    }

    @Override
    public void objectIsSelected(InterfaceObject object) {
    }

    @Override
    public void switchBackToMainState() {
        this.curState = this.mainState;
    }
}
