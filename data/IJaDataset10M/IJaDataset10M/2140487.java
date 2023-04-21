package galacticthrone.screen.menu;

import galacticthrone.main.GTGame;
import galacticthrone.properties.Paths;
import galacticthrone.properties.VideoOptions;
import javax.media.opengl.GL;
import javagame.core.io.video.texture.Texture;
import javagame.core.io.video.texture.TextureManager;
import javagame.impl.io.video.gui.Button;
import javagame.impl.io.video.gui.GUI;
import javagame.impl.io.video.gui.Spinner;
import javagame.impl.io.video.texture.SimpleTextureManager;

/**
 * <br>
 *
 * @author Jaco van der Westhuizen
 */
public class MenuButtons extends GUI {

    private final String NEW_GAME_LO = Paths.TEXTURES + "/Menu/New_Game_Off.png";

    private final String NEW_GAME_HI = Paths.TEXTURES + "/Menu/New_Game_On.png";

    private final String EXIT_GAME_LO = Paths.TEXTURES + "/Menu/Exit_Game_Off.png";

    private final String EXIT_GAME_HI = Paths.TEXTURES + "/Menu/Exit_Game_On.png";

    private final String BIG_ICON = Paths.TEXTURES + "/Menu/Icon_menu.png";

    private Texture newGameLo;

    private Texture newGameHi;

    private Texture exitGameLo;

    private Texture exitGameHi;

    private Texture bigIconTex;

    private Button newGameButton;

    private Button exitGameButton;

    private Spinner bigIcon;

    /**
     * @param screen
     * @param x
     * @param y
     * @param gridSizeX
     * @param gridSizeY
     */
    public MenuButtons() {
        super(new GUI.Center(8f / (9f * VideoOptions.screenAspectRatio)), new GUI.Center(1f), 4, 6);
    }

    @Override
    public void unloadAssets(GL gl, boolean isFinal) {
        if (isFinal) {
            TextureManager texMan = GTGame.getTextureManager();
            texMan.release(gl, newGameLo);
            texMan.release(gl, newGameHi);
            texMan.release(gl, exitGameLo);
            texMan.release(gl, exitGameHi);
            texMan.release(gl, bigIconTex);
        }
    }

    @Override
    public void loadAssets(GL gl, boolean isInitial) {
        if (isInitial) {
            gl.glEnable(GL.GL_TEXTURE_2D);
            SimpleTextureManager texMan = GTGame.getTextureManager();
            newGameLo = texMan.grab(NEW_GAME_LO);
            newGameHi = texMan.grab(NEW_GAME_HI);
            exitGameLo = texMan.grab(EXIT_GAME_LO);
            exitGameHi = texMan.grab(EXIT_GAME_HI);
            bigIconTex = texMan.grab(BIG_ICON);
            this.newGameButton = new Button(newGameLo, newGameHi, newGameHi, MenuScreen.Action.NEW_GAME, 0, 1, 4, 1);
            this.exitGameButton = new Button(exitGameLo, exitGameHi, exitGameHi, MenuScreen.Action.EXIT_GAME, 0, 0, 4, 1);
            this.bigIcon = new Spinner(bigIconTex, 0, 2, 4, 4, -10);
            this.addComponent(newGameButton);
            this.addComponent(exitGameButton);
            this.addComponent(bigIcon);
        }
    }
}
