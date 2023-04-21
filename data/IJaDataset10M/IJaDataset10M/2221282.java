package se.liu.johfa428.game.animationspace;

import se.liu.oschi129.animation.animationspace.AnimationSpace;

/**
 * This class loads all images used in se.liu.johfa428.game(...) into the memory.
 * Images can be loaded from the memory by using AnimationSpace.get(imageName).
 * 
 * @author johfa428
 */
public class GameAnimationSpace extends AnimationSpace {

    public GameAnimationSpace() {
        add("img_cloud01", "ImageLibrary/Clouds/cloud01.png");
        add("img_cloud02", "ImageLibrary/Clouds/cloud02.png");
        add("img_logo01", "ImageLibrary/Logga/logga.png");
        add("img_intro_background", "ImageLibrary/StartMenu/menu.png");
        add("img_castle", "ImageLibrary/LevelSelection/OtherObjects/castle.png");
        add("img_mountain", "ImageLibrary/LevelSelection/OtherObjects/mountain.png");
        add("img_node", "ImageLibrary/LevelSelection/OtherObjects/node.png");
        add("img_platform", "ImageLibrary/LevelSelection/OtherObjects/level|Start|clear:.png");
        add("img_tree", "ImageLibrary/LevelSelection/Tree/tree01>03:.png");
        add("img_road", "ImageLibrary/LevelSelection/Road/road_horizontal|road_vertical:.png");
        add("img_level_select_background", "ImageLibrary/LevelSelection/cleanBackground.png");
        add("img_char", "ImageLibrary/LevelSelection/Player/goombaLeft|goombaRight:.png");
        add("img_game_over", "ImageLibrary/GameOver/game_over.png");
    }
}
