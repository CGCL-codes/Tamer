package net.sourceforge.tile3d.view.writeFile.main.wall.front;

import java.util.HashMap;
import net.sourceforge.tile3d.util.Tile3dConstants;
import net.sourceforge.tile3d.view.writeFile.core.X3dFloat;
import net.sourceforge.tile3d.view.writeFile.core.model.X3dDoor;
import net.sourceforge.tile3d.view.writeFile.core.model.X3dRoom;
import net.sourceforge.tile3d.view.writeFile.core.model.X3dTile;
import net.sourceforge.tile3d.view.writeFile.dom4j.X3dUtil;
import net.sourceforge.tile3d.view.writeFile.main.BaseElement;
import net.sourceforge.tile3d.view.writeFile.main.model.BackBoxTile;
import net.sourceforge.tile3d.view.writeFile.main.model.TotalTiles;
import org.dom4j.Element;
import vrml.field.SFVec3f;

public class RightFrontWallElement extends BaseElement {

    public RightFrontWallElement(Element p_parentE) {
        m_element = X3dUtil.findChildTransformElementWithNameAndDEF(p_parentE, Tile3dConstants.X3D_ELEMENT_LowRightFrontWallTransform);
    }

    @Override
    public void process(HashMap<String, Object> p_args) {
        X3dRoom room = (X3dRoom) p_args.get(Tile3dConstants.MAP_ROOM);
        X3dTile tile = (X3dTile) p_args.get(Tile3dConstants.MAP_WALL_TILE);
        X3dDoor door = (X3dDoor) p_args.get(Tile3dConstants.MAP_DOOR);
        SFVec3f vec3f = new SFVec3f((room.getWidth() - door.getWidth()) / 4 + door.getWidth() / 2, Tile3dConstants.X3D_HOME_OFFSET + ((X3dFloat) p_args.get(Tile3dConstants.MAP_WALL_HEIGHT)).getValue() / 2, room.getLength() / 2);
        System.out.println("******" + room.getLength() / 2);
        m_element.addAttribute(Tile3dConstants.X3d_Attribute_TRANSLATION, vec3f.toString());
        updateImportantField(m_element);
        System.out.println("******" + m_element);
        BackBoxTile boxTile = new BackBoxTile(tile, (room.getWidth() - door.getWidth()) / 2, ((X3dFloat) p_args.get(Tile3dConstants.MAP_WALL_HEIGHT)).getValue());
        TotalTiles totalTiles = (TotalTiles) p_args.get(Tile3dConstants.MAP_TOTAL_TILES);
        totalTiles.calculatorTile(boxTile, tile);
        X3dUtil.updateTransformFrontElement(m_element, boxTile, tile.getImagePath(), tile.getTileIdString());
        Element textureTransform = (Element) m_element.selectSingleNode("Shape/Appearance/TextureTransform");
        textureTransform.addAttribute("translation", "0 0");
        textureTransform.addAttribute("center", "1 1");
    }
}
