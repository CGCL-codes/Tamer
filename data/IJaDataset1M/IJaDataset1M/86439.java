package model;

import java.io.IOException;

/**
 * Absztrakt osztály, olyan speciális GameObject, amely mozgásra kényszeríthető
 * (például ha alatta kiássák a földet). Képes leesni, és legurulni, ill.
 * tárolja az, hogy éppen mozgásban van-e.
 * @author nUMLock
 */
public abstract class MovableObject extends GameObject {

    private boolean isMoving = false;

    private boolean isPressed = false;

    private boolean wantsToRoll = false;

    public void setIsPressed(boolean b) {
        isPressed = b;
    }

    public boolean getIsPressed() {
        return isPressed;
    }

    public void setIsMoving(boolean b) {
        isMoving = b;
    }

    public boolean getIsMoving() {
        return isMoving;
    }

    /**
     * Annak az esetnek a kifejtése, amikor egy szörny akara olyan mezőre
     * mozogni, ahol egy szikla/gyémánt található. Ilyenkor a szörny nem tud
     * az adott mezőre lépni
     * @param monster a cellára lépni szándékozó szörny
     * @return false (hiszen szörny nem tud idelépni)
     */
    public boolean steppingOn(Monster monster) {
        try {
            ResultWriter.Writer("Event Monster " + monster.ID + " moving to " + getClass().getName() + " " + ID);
            ResultWriter.Writer("Event Monster " + monster.ID + " cant move");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * Annak az esetnek a megvalósítása, amikor egy MovableObject akarna
     * olyan mezőre mozogni, ahol egy másik MovableObject van. Ez történhet
     * tolás alkalmával, ilyenkor a mozgás sikertelen, vagy esés/gördülés
     * után, ilyenkor (ha van hova) a feljebb lévő tárgy legurul.
     * @param movableObject az a Movable object, amelyik nekimegy a másiknak
     * @return sikerült-e a mozgó objetumnak a cél cellára mozogni
     */
    public boolean steppingOn(MovableObject movableObject) {
        if (movableObject.wantsToRoll) {
            return false;
        } else if (!movableObject.getIsPressed() && movableObject.getPos() != null) {
            Cell cellDownRight = movableObject.getPos().getNeighbour(Direction.downRight);
            Cell cellDownLeft = movableObject.getPos().getNeighbour(Direction.downLeft);
            movableObject.wantsToRoll = true;
            if (cellDownRight.steppingOn(movableObject)) {
                try {
                    ResultWriter.Writer("Event MovableObject " + movableObject.ID + " rolls");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                movableObject.getPos().setObj(null);
                cellDownRight.setObj(movableObject);
                movableObject.setPos(cellDownRight);
                movableObject.setIsMoving(true);
            } else if (cellDownLeft.steppingOn(movableObject) && movableObject.getPos() != null) {
                try {
                    ResultWriter.Writer("Event MovableObject " + movableObject.ID + " rolls");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                movableObject.getPos().setObj(null);
                cellDownLeft.setObj(movableObject);
                movableObject.setPos(cellDownLeft);
                movableObject.setIsMoving(true);
            } else ;
            movableObject.wantsToRoll = false;
        } else ;
        return false;
    }

    public Cell move() {
        Cell startPos = pos;
        Cell cell = pos.getNeighbour(Direction.down);
        boolean canFall = cell.steppingOn(this);
        if (canFall) {
            try {
                ResultWriter.Writer("Event MovableObject " + ID + " falls");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            pos.setObj(null);
            cell.setObj(this);
            setPos(cell);
            setIsMoving(true);
        } else if (pos == startPos) {
            setIsMoving(false);
        }
        return pos;
    }
}
