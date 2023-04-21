package source.events.action;

import source.model.*;
import source.model.utility.*;
import source.model.type.*;
import source.view.MainScreen;
import source.model.skill.*;

public class Move implements Action {

    private Unit unit;

    private MoveDirection mvDirection;

    private Position currentPos, newPos;

    public Move(MoveDirection mvDirection) {
        this.mvDirection = mvDirection;
    }

    public ActionResult execute(DataHandler dh) {
        ObjectID oid = dh.getCurrentSelectionID();
        currentPos = dh.positionQuery(oid);
        GameObject go = dh.getCurrentSelection();
        ActionResult result = ActionResult.SUCCESS;
        do {
            if (go == null) {
                result = ActionResult.IMPOSSIBLE;
                break;
            }
            if (go.objectType() != GameObjectType.UNIT) {
                MainScreen.writeToConsole("Current selection is not an unit");
                result = ActionResult.IMPOSSIBLE;
                break;
            }
            this.unit = (Unit) go;
            unit.setFacing(mvDirection);
            int x = currentPos.getX() + mvDirection.xChange();
            int y = currentPos.getY() + mvDirection.yChange();
            newPos = new Position(x, y);
            if (!newPos.isValid()) {
                MainScreen.writeToConsole("Target Position is invalid");
                result = ActionResult.IMPOSSIBLE;
                break;
            }
            Tile newPosTile = dh.positionQuery(newPos);
            Terrain newTerrain = newPosTile.getTerrain();
            if (!TerrainUtility.isPassable(newTerrain.getType(), unit.getType())) {
                MainScreen.writeToConsole("Destination position has impassable terrain");
                result = ActionResult.IMPOSSIBLE;
                break;
            }
            if ((newPosTile.hasType(GameObjectType.UNIT) || newPosTile.hasType(GameObjectType.BASE)) && (newPosTile.getPID() != dh.playerIDQuery(oid))) {
                Attack a = new Attack(dh.playerIDQuery(oid), newPos);
                a.execute(dh);
                result = ActionResult.IMPOSSIBLE;
                break;
            }
            if (newPosTile.hasType(GameObjectType.ITEM)) {
                GameObject[] gos = dh.positionQuery(newPos, GameObjectType.ITEM);
                boolean state = true;
                for (GameObject g : gos) {
                    Item item = (Item) g;
                    if (item.isObstruction()) {
                        state = false;
                        break;
                    }
                }
                if (!state) {
                    MainScreen.writeToConsole("Destination position has an obstacle item");
                    result = ActionResult.IMPOSSIBLE;
                    break;
                }
            }
            if (!unit.move(newTerrain.getMvRate())) {
                MainScreen.writeToConsole("Current unit does not have enough movement left");
                result = ActionResult.FAILED;
                break;
            }
            if (!dh.positionCommand(unit.objectID(), newPos)) {
                result = ActionResult.FAILED;
                break;
            }
            if (newPosTile.hasType(GameObjectType.ITEM)) {
                GameObject[] gos = dh.positionQuery(newPos, GameObjectType.ITEM);
                for (GameObject g : gos) {
                    Item item = (Item) g;
                    if (item.isTriggered()) {
                        TriggeredItem trig = (TriggeredItem) item;
                        Action trapAct = trig.trigger();
                        trapAct.execute(dh);
                        if (!unit.isValid()) {
                            MainScreen.writeToConsole("Unit was killed by an item");
                            dh.returnObject(unit);
                            return ActionResult.IMPOSSIBLE;
                        }
                    }
                }
            }
            if (newPosTile.hasType(GameObjectType.AREAEFFECT)) {
                GameObject[] aes = dh.positionQuery(newPos, GameObjectType.AREAEFFECT);
                for (GameObject ae : aes) {
                    if (!((AreaEffect) ae).doEffect(dh, unit)) {
                        result = ActionResult.IMPOSSIBLE;
                    }
                }
            }
        } while (false);
        if (unit.isValid()) {
            Skill skilz = unit.getSkill("detecttrap");
            if (skilz != null) {
                SkillDetectTrap sdt = (SkillDetectTrap) skilz;
                Position center = dh.positionQuery(unit.objectID());
                for (MoveDirection md : MoveDirection.values()) {
                    int newX = center.getX() + md.xChange();
                    int newY = center.getY() + md.yChange();
                    Position newP = new Position(newX, newY);
                    if (newP.isValid()) {
                        System.out.println("Checking position X:" + newP.getX() + " Y:" + newP.getY());
                        if (sdt.Search()) {
                            System.out.println("Success! Checking tile.");
                            GameObject[] gos = dh.positionQuery(newP, GameObjectType.ITEM);
                            for (GameObject g : gos) {
                                Item item = (Item) g;
                                if (item.getItemType() == ItemType.TRAP) {
                                    System.out.println("There is a trap here!");
                                }
                            }
                        } else System.out.println("Search failed...");
                    }
                }
            }
        }
        dh.returnObject(unit);
        return result;
    }
}
