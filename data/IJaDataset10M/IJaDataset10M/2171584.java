package net.virtualinfinity.atrobots.hardware.mines;

import net.virtualinfinity.atrobots.arena.Arena;
import net.virtualinfinity.atrobots.arena.Position;
import net.virtualinfinity.atrobots.arenaobjects.DamageInflicter;
import net.virtualinfinity.atrobots.ports.PortHandler;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Daniel Pitts
 */
public class MineLayer {

    private int mines = 2;

    private Collection<Mine> layedMines = new ArrayList<Mine>();

    private DamageInflicter owner;

    private Position position;

    private Arena arena;

    public MineLayer(int mines) {
        this.mines = mines;
    }

    public PortHandler getMineBayPort() {
        return new PortHandler() {

            public short read() {
                return (short) countMinesRemaining();
            }

            public void write(short value) {
                layMine((value));
            }
        };
    }

    private void layMine(double triggerRadius) {
        if (hasMines()) {
            final Mine mine = new Mine(owner);
            mine.setTriggerRadius(triggerRadius);
            mine.setPosition(position);
            getArena().addCollidable(mine);
            layedMines.add(mine);
            mines--;
        }
    }

    private boolean hasMines() {
        return countMinesRemaining() > 0;
    }

    private int countMinesRemaining() {
        return mines;
    }

    public PortHandler getPlacedMinePort() {
        return new PortHandler() {

            public short read() {
                return (short) countPlacedMines();
            }

            public void write(short value) {
                detonateMines();
            }
        };
    }

    private void detonateMines() {
        for (Mine mine : layedMines) {
            mine.explode();
        }
    }

    private int countPlacedMines() {
        int count = 0;
        for (Mine mine : layedMines) {
            if (!mine.isDead()) {
                count++;
            }
        }
        return count;
    }

    private Arena getArena() {
        return arena;
    }

    public void setOwner(DamageInflicter owner) {
        this.owner = owner;
    }

    public DamageInflicter getOwner() {
        return owner;
    }

    public void setArena(Arena arena) {
        this.arena = arena;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}
