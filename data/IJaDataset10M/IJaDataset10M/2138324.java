package com.customwars.client.ui.state;

import com.customwars.client.App;
import com.customwars.client.controller.AppOptionsController;
import com.customwars.client.io.loading.ThinglePageLoader;
import com.customwars.client.tools.ThingleUtil;
import com.customwars.client.ui.state.input.CWCommand;
import com.customwars.client.ui.state.input.CWInput;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.thingle.Page;
import org.newdawn.slick.thingle.Widget;
import java.io.File;

/**
 * Allows to change user configuration from a gui
 */
public class AppOptionsState extends CWState {

    private Page page;

    private Image backGroundImage;

    private AppOptionsController controller;

    private GameContainer container;

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        this.container = container;
        controller = new AppOptionsController(stateChanger);
        initPage();
        initPageContent();
        backGroundImage = resources.getSlickImg("light_menu_background");
    }

    private void initPage() {
        ThinglePageLoader thingleLoader = new ThinglePageLoader(App.get("gui.path"));
        page = thingleLoader.loadPage("appOptions.xml", "greySkin.properties", controller);
    }

    private void initPageContent() {
        page.getWidget("txt_server_url").setText(App.get("user.snailserver_url"));
        page.getWidget("sli_music").setInteger("value", (int) (container.getMusicVolume() * 100));
        page.getWidget("sli_sound_effects").setInteger("value", (int) (container.getSoundVolume() * 100));
        page.getWidget("txt_user_name").setText(App.get("user.name"));
        page.getWidget("txt_user_password").setText(App.get("user.password"));
        fillPluginCbo();
    }

    private void fillPluginCbo() {
        Widget pluginCbo = page.getWidget("cbo_plugin");
        for (String plugin : new File("resources/res/plugin").list()) {
            if (!plugin.startsWith(".")) {
                Widget choice = page.createWidget("choice");
                choice.setText(plugin);
                pluginCbo.add(choice);
            }
        }
        ThingleUtil.selectChild(pluginCbo, App.get("user.activeplugin"));
    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) throws SlickException {
        super.enter(container, game);
        page.enable();
    }

    @Override
    public void leave(GameContainer container, StateBasedGame game) throws SlickException {
        super.leave(container, game);
        page.disable();
    }

    @Override
    public void render(GameContainer container, Graphics g) throws SlickException {
        g.drawImage(backGroundImage, 0, 0);
        page.render();
    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
    }

    @Override
    public void controlPressed(CWCommand command, CWInput cwInput) {
        if (command == CWInput.CANCEL) {
            controller.back();
        }
    }

    @Override
    public int getID() {
        return 32;
    }
}
