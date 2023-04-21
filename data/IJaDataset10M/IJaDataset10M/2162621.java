package game;

import java.util.Iterator;
import javolution.util.FastMap;

public class L2Char {

    public Object visualObject = null;

    public boolean deleted = false;

    public boolean ignoreDelete = false;

    public ObjMovement movementObject = null;

    public L2Char movementToPawn = null;

    public FastMap<String, ObjMovement> mapCharsInMovementToPawn = null;

    public int hp = 0;

    public int hpMax = 0;

    public int mp = 0;

    public int mpMax = 0;

    public int cp = 0;

    public int cpMax = 0;

    public byte isSitting = 0x00;

    public byte isRunning = 0x01;

    public byte isInCombat = 0x00;

    public byte isAlikeDead = 0x00;

    public String realName = "";

    public String title = "";

    public String objId = "0";

    public int objIdInt = 0;

    public int objIdIIntTarget = 0;

    public int x = 0;

    public int y = 0;

    public int z = 0;

    public int heading = 0;

    public int pvpFlag1 = 0;

    public int karma1 = 0;

    public int pvpFlag2 = 0;

    public int karma2 = 0;

    public int runSpd = 0;

    public int walkSpd = 0;

    public int swimRunSpd = 0;

    public int swimWalkSpd = 0;

    public int flRunSpd = 0;

    public int flWalkSpd = 0;

    public int flyRunSpd = 0;

    public int flyWalkSpd = 0;

    public double movementSpeedMultiplier = 1;

    public double attackSpeedMultiplier = 1;

    public double collisionRadius = 0;

    public double collisionHeight = 0;

    public int mAtkSpd = 0;

    public int pAtkSpd2 = 0;

    public int pAtkSpd = 0;

    public int lhand = 0;

    public int rhand = 0;

    public int lrhand = 0;

    @SuppressWarnings("unchecked")
    public void putMoveToPawn(ObjMovement objMovement) {
        if (mapCharsInMovementToPawn == null) mapCharsInMovementToPawn = new FastMap();
        objMovement.l2CharInfo.movementToPawn = this;
        mapCharsInMovementToPawn.put(objMovement.l2CharInfo.objId, objMovement);
    }

    public void removeMoveToPawn(String objId) {
        if (mapCharsInMovementToPawn == null) return;
        mapCharsInMovementToPawn.remove(objId);
        if (mapCharsInMovementToPawn.size() == 0) mapCharsInMovementToPawn = null;
    }

    public void updateMoveToPawnDestini() {
        if (mapCharsInMovementToPawn == null) return;
        for (Iterator iter = mapCharsInMovementToPawn.values().iterator(); iter.hasNext(); ) {
            ObjMovement charMovement = (ObjMovement) iter.next();
            synchronized (charMovement) {
                charMovement.iX = (int) charMovement.x;
                charMovement.iY = (int) charMovement.y;
                charMovement.iZ = (int) charMovement.z;
                charMovement.dX = (int) x;
                charMovement.dY = (int) y;
                charMovement.dZ = (int) z;
                charMovement.distanciaX = charMovement.dX - charMovement.iX;
                charMovement.distanciaY = charMovement.dY - charMovement.iY;
                charMovement.distanciaZ = charMovement.dZ - charMovement.iZ;
                charMovement.distanciaZ = 0;
                charMovement.distancia = Math.sqrt((charMovement.distanciaX * charMovement.distanciaX) + (charMovement.distanciaY * charMovement.distanciaY) + (charMovement.distanciaZ * charMovement.distanciaZ));
                charMovement.ticksToMove = (int) (MovementControler.ticksPerSecond * (charMovement.distancia / charMovement.speed));
                charMovement.valorPorTickX = charMovement.distanciaX / (double) charMovement.ticksToMove;
                charMovement.valorPorTickY = charMovement.distanciaY / (double) charMovement.ticksToMove;
                charMovement.valorPorTickZ = 0;
            }
        }
    }

    public void updateMovementValues() {
        if (movementObject == null) return;
        ObjMovement charMovement = movementObject;
        if (isRunning == 0x01) charMovement.speed = (int) (runSpd * movementSpeedMultiplier); else charMovement.speed = (int) (walkSpd * movementSpeedMultiplier);
        charMovement.iX = (int) charMovement.x;
        charMovement.iY = (int) charMovement.y;
        charMovement.iZ = (int) charMovement.z;
        charMovement.distanciaX = charMovement.dX - charMovement.iX;
        charMovement.distanciaY = charMovement.dY - charMovement.iY;
        charMovement.distanciaZ = 0;
        charMovement.distancia = Math.sqrt(charMovement.distanciaX * charMovement.distanciaX + charMovement.distanciaY * charMovement.distanciaY + charMovement.distanciaZ * charMovement.distanciaZ);
        charMovement.ticksToMove = (int) (MovementControler.ticksPerSecond * charMovement.distancia / charMovement.speed);
        charMovement.valorPorTickX = ((double) charMovement.distanciaX) / (double) charMovement.ticksToMove;
        charMovement.valorPorTickY = ((double) charMovement.distanciaY) / (double) charMovement.ticksToMove;
        charMovement.valorPorTickZ = 0;
    }

    public void cancelMovments() {
        if (movementObject != null) {
            MovementControler.getInstance().removeMovement(objId);
            movementObject = null;
        }
        if (movementToPawn != null) {
            movementToPawn.removeMoveToPawn(objId);
            movementToPawn = null;
        }
    }
}
