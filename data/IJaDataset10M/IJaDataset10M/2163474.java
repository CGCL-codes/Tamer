package server.gameObjects.shipComponents;

import java.util.Iterator;
import java.util.LinkedList;
import server.gameObjects.ServerEngineerMine;
import server.gameObjects.ServerLocalSpace;
import server.gameObjects.ServerPlayerShip;
import server.gameObjects.ServerSimulatedObject;
import server.gameObjects.ServerPlayerShip.ShipComponentInterface;
import util.MathHelper;
import util.Vector;
import com.sun.sgs.app.AppContext;
import com.sun.sgs.app.ManagedReference;
import fancyClient.graphics.ModuleIcon;

public class EngineerMineDispensor extends ShipComponent {

    private static final long serialVersionUID = 1L;

    private LinkedList<ManagedReference<? extends ServerSimulatedObject>> mineList = new LinkedList<ManagedReference<? extends ServerSimulatedObject>>();

    public EngineerMineDispensor(ShipComponentInterface inter, byte slot) {
        super("DebugGun", inter, slot);
        setCycleCooldown(5f);
        setResourcesRemaining(3);
        setIcon(ModuleIcon.Icon.PROJECTILE_FINITE_AMMO.val);
    }

    @Override
    public void activate() {
        if (getRemainingCooldown() <= 0) {
            ServerPlayerShip ship = shipInterface.getPlayer();
            ServerLocalSpace localSpace = ship.getLocalSpace();
            AppContext.getDataManager().markForUpdate(localSpace);
            Vector angleVec = new Vector(ship.getAngle() + MathHelper.PI);
            Vector shootOffset = Vector.scale(angleVec, 15);
            Vector velOffset = Vector.scale(angleVec, 50);
            ServerEngineerMine mine = new ServerEngineerMine(shipInterface.getShipRef(), ship.getOwningFaction(), ship.getPosition().x + shootOffset.x, ship.getPosition().y + shootOffset.y, ship.getVelocity().x + velOffset.x, ship.getVelocity().y + velOffset.y);
            triggerCycleCooldown();
            if (getResourcesRemaining() == 0) mineList.removeFirst().get().unRegister(); else setResourcesRemaining(getResourcesRemaining() - 1);
            mineList.add(localSpace.addAndRegisterActor(mine));
        }
    }

    @Override
    public void update(float elapsedSeconds) {
        super.update(elapsedSeconds);
        Iterator<ManagedReference<? extends ServerSimulatedObject>> iter = mineList.iterator();
        while (iter.hasNext()) {
            ServerEngineerMine mine = (ServerEngineerMine) iter.next().get();
            if (mine.isSafeToUnregister()) {
                mine.trueUnRegister();
                iter.remove();
                setResourcesRemaining(getResourcesRemaining() + 1);
            }
        }
    }

    @Override
    public void unRegister() {
        for (ManagedReference<? extends ServerSimulatedObject> ref : mineList) ref.get().unRegister();
    }
}
