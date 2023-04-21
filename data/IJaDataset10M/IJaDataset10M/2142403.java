package nl.huub.van.amelsvoort.game;

import nl.huub.van.amelsvoort.Defines;
import nl.huub.van.amelsvoort.qcommon.Com;
import nl.huub.van.amelsvoort.util.Lib;
import nl.huub.van.amelsvoort.util.Math3D;
import java.io.IOException;
import java.io.RandomAccessFile;

/** 
 	Player_state_t is the information needed in addition to pmove_state_t
	to rendered a view.  There will only be 10 player_state_t sent each second,
	but the number of pmove_state_t changes will be relative to client
	frame rates.
*/
public class player_state_t {

    public pmove_state_t pmove = new pmove_state_t();

    public float[] viewangles = { 0, 0, 0 };

    public float[] viewoffset = { 0, 0, 0 };

    public float[] kick_angles = { 0, 0, 0 };

    public float[] gunangles = { 0, 0, 0 };

    public float[] gunoffset = { 0, 0, 0 };

    public int gunindex;

    public int gunframe;

    public float blend[] = new float[4];

    public float fov;

    public int rdflags;

    public short stats[] = new short[Defines.MAX_STATS];

    /** Lets cleverly reset the structure. */
    private static player_state_t prototype = new player_state_t();

    /** Clears the player_state.*/
    public void clear() {
        this.set(prototype);
    }

    /** Clones the object.*/
    public player_state_t getClone() {
        return new player_state_t().set(this);
    }

    /** Copies the player state data. */
    public player_state_t set(player_state_t from) {
        pmove.set(from.pmove);
        Math3D.VectorCopy(from.viewangles, viewangles);
        Math3D.VectorCopy(from.viewoffset, viewoffset);
        Math3D.VectorCopy(from.kick_angles, kick_angles);
        Math3D.VectorCopy(from.gunangles, gunangles);
        Math3D.VectorCopy(from.gunoffset, gunoffset);
        gunindex = from.gunindex;
        gunframe = from.gunframe;
        blend[0] = from.blend[0];
        blend[1] = from.blend[1];
        blend[2] = from.blend[2];
        blend[3] = from.blend[3];
        fov = from.fov;
        rdflags = from.rdflags;
        System.arraycopy(from.stats, 0, stats, 0, Defines.MAX_STATS);
        return this;
    }

    /** Reads a player_state from a file.*/
    public void load(RandomAccessFile f) throws IOException {
        pmove.load(f);
        viewangles[0] = f.readFloat();
        viewangles[1] = f.readFloat();
        viewangles[2] = f.readFloat();
        viewoffset[0] = f.readFloat();
        viewoffset[1] = f.readFloat();
        viewoffset[2] = f.readFloat();
        kick_angles[0] = f.readFloat();
        kick_angles[1] = f.readFloat();
        kick_angles[2] = f.readFloat();
        gunangles[0] = f.readFloat();
        gunangles[1] = f.readFloat();
        gunangles[2] = f.readFloat();
        gunoffset[0] = f.readFloat();
        gunoffset[1] = f.readFloat();
        gunoffset[2] = f.readFloat();
        gunindex = f.readInt();
        gunframe = f.readInt();
        blend[0] = f.readFloat();
        blend[1] = f.readFloat();
        blend[2] = f.readFloat();
        blend[3] = f.readFloat();
        fov = f.readFloat();
        rdflags = f.readInt();
        for (int n = 0; n < Defines.MAX_STATS; n++) stats[n] = f.readShort();
    }

    /** Writes a player_state to a file.*/
    public void write(RandomAccessFile f) throws IOException {
        pmove.write(f);
        f.writeFloat(viewangles[0]);
        f.writeFloat(viewangles[1]);
        f.writeFloat(viewangles[2]);
        f.writeFloat(viewoffset[0]);
        f.writeFloat(viewoffset[1]);
        f.writeFloat(viewoffset[2]);
        f.writeFloat(kick_angles[0]);
        f.writeFloat(kick_angles[1]);
        f.writeFloat(kick_angles[2]);
        f.writeFloat(gunangles[0]);
        f.writeFloat(gunangles[1]);
        f.writeFloat(gunangles[2]);
        f.writeFloat(gunoffset[0]);
        f.writeFloat(gunoffset[1]);
        f.writeFloat(gunoffset[2]);
        f.writeInt(gunindex);
        f.writeInt(gunframe);
        f.writeFloat(blend[0]);
        f.writeFloat(blend[1]);
        f.writeFloat(blend[2]);
        f.writeFloat(blend[3]);
        f.writeFloat(fov);
        f.writeInt(rdflags);
        for (int n = 0; n < Defines.MAX_STATS; n++) f.writeShort(stats[n]);
    }

    /** Prints the player state. */
    public void dump() {
        pmove.dump();
        Lib.printv("viewangles", viewangles);
        Lib.printv("viewoffset", viewoffset);
        Lib.printv("kick_angles", kick_angles);
        Lib.printv("gunangles", gunangles);
        Lib.printv("gunoffset", gunoffset);
        Com.Println("gunindex: " + gunindex);
        Com.Println("gunframe: " + gunframe);
        Lib.printv("blend", blend);
        Com.Println("fov: " + fov);
        Com.Println("rdflags: " + rdflags);
        for (int n = 0; n < Defines.MAX_STATS; n++) Com.Printf("stats[" + n + "]: " + stats[n]);
    }
}
