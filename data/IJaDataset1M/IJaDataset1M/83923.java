package net.rmanager.gui;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import net.rmanager.environment.City;
import net.rmanager.global.GameController;
import net.rmanager.gui.SceneBuilder.SceneType;
import net.rmanager.player.Player;
import net.rmanager.player.PlayerType;
import com.golden.gamedev.Game;

public class Gui extends Game {

    private Scene status;

    private Scene statistic;

    private Scene currentScene;

    private SceneList sceneList = new SceneList();

    private GameController controller;

    public Gui() {
        distribute = false;
    }

    @Override
    public void initResources() {
        initGame();
        showCursor();
        initScenes();
    }

    @Override
    public void render(Graphics2D g) {
        currentScene.draw(g);
    }

    @Override
    public void update(long arg0) {
        if (keyPressed(KeyEvent.VK_ESCAPE)) {
            System.exit(0);
        }
        if (click()) {
            currentScene.update(this, getMouseX(), getMouseY());
            status.update(this, 0, 0);
        }
    }

    private void initGame() {
        Player p = new Player("p", "c", new PlayerType(PlayerType.RestaurantLevel.MIDDLE));
        controller = new GameController(p);
        ArrayList<City> cityList = controller.getAvailableCities();
        controller.setCurCity(cityList.get(0));
    }

    private void initScenes() {
        status = SceneBuilder.getScene(SceneType.STATUS, "status", controller);
        statistic = SceneBuilder.getScene(SceneType.STATISTICS, "statistic", controller);
        Scene mainmenu = SceneBuilder.getScene(SceneType.MAINMENU, "mainMenu", controller);
        Scene citymenu = SceneBuilder.getScene(SceneType.CITYMENU, "cityMenu", controller);
        citymenu.addScene(status);
        SceneChooser restaurantChooserForShop = (SceneChooser) SceneBuilder.getScene(SceneType.CHOOSER, "chooserForVendor", controller);
        restaurantChooserForShop.setNextSceneId("vendorChooser");
        SceneChooser restaurantChooserForJobCenter = (SceneChooser) SceneBuilder.getScene(SceneType.CHOOSER, "chooserForJob", controller);
        restaurantChooserForJobCenter.setNextSceneId("jobCenter");
        Scene wmVendorShop = SceneBuilder.getScene(SceneType.WM_VENDOR, "wmVendor", controller);
        Scene cookVendorShop = SceneBuilder.getScene(SceneType.CK_VENDOR, "ckVendor", controller);
        Scene fridgeVendorShop = SceneBuilder.getScene(SceneType.FR_VENDOR, "frVendor", controller);
        Scene sanitaryVendorShop = SceneBuilder.getScene(SceneType.SR_VENDOR, "srVendor", controller);
        Scene interiorVendorShop = SceneBuilder.getScene(SceneType.IT_VENDOR, "itVendor", controller);
        Scene restaurantVendorShop = SceneBuilder.getScene(SceneType.RESTAURANT_VENDOR, "restaurantVendor", controller);
        Scene vendorChooser = SceneBuilder.getScene(SceneType.VENDOR_CHOOSER, "vendorChooser", controller);
        Scene trainStation = SceneBuilder.getScene(SceneType.TRAINSTATION, "trainStation", controller);
        Scene jobCenter = SceneBuilder.getScene(SceneType.JOB_CENTER, "jobCenter", controller);
        Scene jobAssi = SceneBuilder.getScene(SceneType.JOB_ASSISTANT, "jobAssistant", controller);
        Scene jobCook = SceneBuilder.getScene(SceneType.JOB_COOK, "jobCook", controller);
        Scene jobJanitor = SceneBuilder.getScene(SceneType.JOB_JANITOR, "jobJanitor", controller);
        Scene jobLaundress = SceneBuilder.getScene(SceneType.JOB_LAUNDRESS, "jobLaundress", controller);
        Scene jobSteward = SceneBuilder.getScene(SceneType.JOB_STEWARD, "jobSteward", controller);
        sceneList.add(mainmenu);
        sceneList.add(citymenu);
        sceneList.add(jobCenter);
        sceneList.add(jobAssi);
        sceneList.add(jobCook);
        sceneList.add(jobJanitor);
        sceneList.add(jobLaundress);
        sceneList.add(jobSteward);
        sceneList.add(restaurantChooserForShop);
        sceneList.add(restaurantChooserForJobCenter);
        sceneList.add(wmVendorShop);
        sceneList.add(cookVendorShop);
        sceneList.add(fridgeVendorShop);
        sceneList.add(sanitaryVendorShop);
        sceneList.add(interiorVendorShop);
        sceneList.add(restaurantVendorShop);
        sceneList.add(vendorChooser);
        sceneList.add(statistic);
        sceneList.add(trainStation);
        currentScene = mainmenu;
    }

    public void nextScene(String sceneName) {
        currentScene = sceneList.get(sceneName);
        status.update(this, 0, 0);
    }

    public Scene getScene(String sceneName) {
        return sceneList.get(sceneName);
    }
}
