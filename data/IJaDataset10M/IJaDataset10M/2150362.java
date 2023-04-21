package com.yxl.test.games.sky;

import jmetest.renderer.TestSkybox;
import com.jme.image.Texture;
import com.jme.scene.Skybox;
import com.jme.util.TextureManager;

/**
 * 山区天空环境 修改内容：创建类
 * 
 * @author: yxl
 * @version 1.0
 */
public class HillSky extends AbstractSky {

    @Override
    public void building() {
        skybox = new Skybox("skybox", 10, 10, 10);
        Texture north = TextureManager.loadTexture(TestSkybox.class.getClassLoader().getResource("jmetest/data/texture/north.jpg"), Texture.MinificationFilter.BilinearNearestMipMap, Texture.MagnificationFilter.Bilinear);
        Texture south = TextureManager.loadTexture(TestSkybox.class.getClassLoader().getResource("jmetest/data/texture/south.jpg"), Texture.MinificationFilter.BilinearNearestMipMap, Texture.MagnificationFilter.Bilinear);
        Texture east = TextureManager.loadTexture(TestSkybox.class.getClassLoader().getResource("jmetest/data/texture/east.jpg"), Texture.MinificationFilter.BilinearNearestMipMap, Texture.MagnificationFilter.Bilinear);
        Texture west = TextureManager.loadTexture(TestSkybox.class.getClassLoader().getResource("jmetest/data/texture/west.jpg"), Texture.MinificationFilter.BilinearNearestMipMap, Texture.MagnificationFilter.Bilinear);
        Texture up = TextureManager.loadTexture(TestSkybox.class.getClassLoader().getResource("jmetest/data/texture/top.jpg"), Texture.MinificationFilter.BilinearNearestMipMap, Texture.MagnificationFilter.Bilinear);
        Texture down = TextureManager.loadTexture(TestSkybox.class.getClassLoader().getResource("jmetest/data/texture/bottom.jpg"), Texture.MinificationFilter.BilinearNearestMipMap, Texture.MagnificationFilter.Bilinear);
        skybox.setTexture(Skybox.Face.North, north);
        skybox.setTexture(Skybox.Face.West, west);
        skybox.setTexture(Skybox.Face.South, south);
        skybox.setTexture(Skybox.Face.East, east);
        skybox.setTexture(Skybox.Face.Up, up);
        skybox.setTexture(Skybox.Face.Down, down);
        skybox.preloadTextures();
    }
}
