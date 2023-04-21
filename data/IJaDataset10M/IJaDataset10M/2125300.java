package source.events.action;

import source.model.type.ActionResult;
import source.model.type.*;
import source.model.DataHandler;
import java.util.Vector;
import source.model.*;
import source.events.action.NewUnit;
import source.model.utility.ItemUtility;
import source.view.MainScreen;

public class NewTurn implements Action {

    private int playerID;

    public NewTurn(int playerID) {
        this.playerID = playerID;
    }

    public ActionResult execute(DataHandler dh) {
        Player p = dh.getPlayer(playerID);
        p.newTurn();
        Vector<Entity> v = dh.getPlayersObjects(playerID);
        for (Entity e : v) {
            Action a = e.newTurn();
            a.execute(dh);
        }
        Vector<GameObject> is = dh.objectQuery(GameObjectType.ITEM);
        for (GameObject i : is) {
            Item a = (Item) i;
            if (ItemUtility.turnEffectedItem(a.getItemType())) {
                CountDownItem cdi = (CountDownItem) a;
                Action toDo = cdi.decrementTurn();
                toDo.execute(dh);
            }
        }
        Vector<Base> baseArray = dh.getPlayersBases(playerID);
        int manpower;
        for (Base b : baseArray) {
            if (b.peekMission() != null) {
                if (b.getMPleft() > 0) {
                    Planet planet = (Planet) (dh.objectQuery(b.planetID()));
                    manpower = planet.getManPower();
                    b.decrementMPleft(manpower);
                }
                if (b.getMPleft() <= 0) {
                    Position posForUnit = dh.positionQuery(b.objectID());
                    UnitType u = b.nextMission();
                    NewUnit nu = new NewUnit(playerID, u, posForUnit);
                    MainScreen.writeToConsole(u + " has been created");
                    nu.execute(dh);
                    if (b.peekMission() != null) {
                        b.setMPleft(b.peekMission().manpower());
                    }
                    if (b.isRallyPointSet()) p.addUnitMission(b.getRallyPoint());
                }
            }
        }
        Vector<UnitMission> um = p.getUnitMissions();
        Vector<UnitMission> Toby = new Vector<UnitMission>();
        for (UnitMission u : um) {
            ExecuteMission em = new ExecuteMission(playerID, u);
            em.execute(dh);
            if (!u.isValid()) Toby.add(u);
        }
        for (UnitMission u1 : Toby) {
            p.removeUnitMission(u1);
        }
        dh.setPlayerIterators(playerID);
        NextUnit nu = new NextUnit(playerID);
        if (nu.execute(dh) == ActionResult.FAILED) {
            NextBase nb = new NextBase(playerID);
            nb.execute(dh);
        }
        return ActionResult.SUCCESS;
    }
}
