package View;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import network.protocol.PlayerPackage;
import client.gameplay.Building;
import client.gameplay.LoadMap;

/**
 * Determines what building is to be displayed.
 * 
 * @author Torbj�rn
 * 
 */
public class BuildDisplay {

    private BufferedImage[][] building;

    private ArrayList<PlayerPackage> players;

    private ArrayList<BufferedImage> miscularImages;

    public BuildDisplay(ArrayList<PlayerPackage> players, LoadMap map) {
    }

    public BuildDisplay(ArrayList<PlayerPackage> players, BufferedImage[][] buildingImgs, ArrayList<BufferedImage> miscImg) {
        building = new BufferedImage[players.size()][View.NUMBER_OF_DIFFERENT_BUILDINGS];
        building = buildingImgs;
        this.players = players;
        miscularImages = miscImg;
    }

    /**
	 * returns image to the building
	 * @param b
	 * @return
	 */
    public BufferedImage getImg(Building b) {
        for (int i = 0; i < players.size(); i++) {
            if (b.getOwner().equals(players.get(i))) {
                if (b.isArmory()) return building[i][View.ARMORY_INDEX]; else if (b.isTower()) return building[i][View.TOWER_INDEX]; else return building[i][b.getLevel()];
            }
        }
        return null;
    }

    public BufferedImage getResCloud() {
        return miscularImages.get(View.POPULATION_CLOUND_INDEX);
    }

    public Image getScaffold() {
        return miscularImages.get(View.SCAFFOLD_INDEX);
    }

    public Image getDust() {
        return miscularImages.get(View.DUST_CLOUND_INDEX);
    }
}
