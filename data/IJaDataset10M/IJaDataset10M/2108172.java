package com.sertaogames.terremoto.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.sertaogames.cactus2d.Cactus2DApplication;
import com.sertaogames.cactus2d.Cactus2DLevel;
import com.sertaogames.cactus2d.GameObject;
import com.sertaogames.cactus2d.components.LevelLoader;
import com.sertaogames.cactus2d.components.SplashTouch;
import com.sertaogames.cactus2d.components.SpriteRendererComponent;
import com.sertaogames.terremoto.component.DragAndDropComponent;

/**
 * This level is, a merchandise level
 * @author Jader
 *
 */
public class SplashLevel extends Cactus2DLevel {

    /**
	 * Inicializando configuara��es de arquivos do jogo
	 * Aqui come�a toda a magia desse padr�o de game obeject.
	 * Por enquanto apenas um ser� feito isoladamente.
	 * 
	 * Nesse primeiro exemplo ser� criado uma imagem com o simbolo da Sert�o Games 
	 * no meio da tela e ao clicar ser� carregado o proximo n�vel de jogo. 
	 * Acompanhe o passo a passo abaixo. 
	 */
    @Override
    protected void init() {
        DragAndDropComponent.snapPositions.add(new Vector2(31, 1024 - 253));
        DragAndDropComponent.snapPositions.add(new Vector2(122, 1024 - 263));
        DragAndDropComponent.snapPositions.add(new Vector2(72, 1024 - 377));
        DragAndDropComponent.snapPositions.add(new Vector2(33, 1024 - 480));
        DragAndDropComponent.snapPositions.add(new Vector2(110, 1024 - 473));
        DragAndDropComponent.snapPositions.add(new Vector2(120, 1024 - 545));
        DragAndDropComponent.snapPositions.add(new Vector2(285, 1024 - 50));
        DragAndDropComponent.snapPositions.add(new Vector2(242, 1024 - 377));
        DragAndDropComponent.snapPositions.add(new Vector2(330, 1024 - 236));
        DragAndDropComponent.snapPositions.add(new Vector2(313, 1024 - 370));
        DragAndDropComponent.snapPositions.add(new Vector2(422, 1024 - 156));
        DragAndDropComponent.snapPositions.add(new Vector2(402, 1024 - 450));
        DragAndDropComponent.snapPositions.add(new Vector2(485, 1024 - 480));
        DragAndDropComponent.snapPositions.add(new Vector2(534, 1024 - 229));
        Cactus2DApplication.jukeBox.volume = 1;
        Cactus2DApplication.sfxVolume = 1;
        Cactus2DApplication.jukeBox.addMusic("data/sound/music01.mp3");
        Cactus2DApplication.jukeBox.addMusic("data/sound/alert.wav");
        Cactus2DApplication.jukeBox.addMusic("data/sound/earthquake.wav");
        Cactus2DApplication.backgroundColor = Color.WHITE;
        float zoomScale = Cactus2DApplication.invCameraZoom;
        GameObject go = new GameObject("splash");
        go.transform.getPosition().set(Gdx.graphics.getWidth() / 2 - 256f * zoomScale, Gdx.graphics.getHeight() / 2 - 128f * zoomScale);
        SpriteRendererComponent sr = new SpriteRendererComponent(Cactus2DApplication.loadTexture("data/textures/SertaoGames-marca-512x256.png"));
        go.AddComponent(sr);
        sr.gui = true;
        go.AddComponent(new SplashTouch());
        go.AddComponent(new LevelLoader(MenuLevel.class));
        addGameObject(go);
    }
}
