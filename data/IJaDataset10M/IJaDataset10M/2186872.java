package br.furb.inf.tcc.tankcoders.scene.tank;

import jason.infra.centralised.RunCentralisedMAS;
import br.furb.inf.tcc.tankcoders.game.PlayerTank;
import br.furb.inf.tcc.tankcoders.jason.AgentRepository;
import br.furb.inf.tcc.tankcoders.jason.TankAgArch;
import br.furb.inf.tcc.tankcoders.scene.tank.m1abrams.M1AbramsTank;
import br.furb.inf.tcc.tankcoders.scene.tank.panther.JadgPantherTank;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jmex.physics.PhysicsSpace;

/**
 * This class is responsable by creation of tank instances.
 * @author Germano Fronza
 */
public class TankFactory {

    public static ITank makeTank(short playerIdOwner, PlayerTank tank, PhysicsSpace pSpace, Vector3f location, final boolean remoteTank, final boolean masPlayer) {
        String tankName = tank.getTankName();
        Node tankNode = null;
        switch(tank.getModel()) {
            case M1_ABRAMS:
                tankNode = new M1AbramsTank(playerIdOwner, tankName, tank.getTeam(), pSpace, location, remoteTank, masPlayer);
                break;
            case JADGE_PANTHER:
                tankNode = new JadgPantherTank(playerIdOwner, tankName, tank.getTeam(), pSpace, location, remoteTank, masPlayer);
                break;
        }
        ITank tankObj = (ITank) tankNode;
        if (!remoteTank && masPlayer) {
            TankAgArch agArch = (TankAgArch) RunCentralisedMAS.getRunner().getAg(tankName).getUserAgArch();
            agArch.setTankInstance(tankObj);
            AgentRepository.putTank(tankName, agArch);
        }
        return tankObj;
    }
}
