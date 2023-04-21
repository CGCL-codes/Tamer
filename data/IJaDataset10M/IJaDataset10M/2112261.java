package nl.huub.van.amelsvoort.game;

import nl.huub.van.amelsvoort.util.QuakeFile;
import java.io.IOException;
import nl.huub.van.amelsvoort.spel.Nadenken;

public class monsterinfo_t {

    public mmove_t currentmove;

    public int aiflags;

    public int nextframe;

    public float scale;

    public Nadenken stand;

    public Nadenken idle;

    public Nadenken search;

    public Nadenken walk;

    public Nadenken run;

    public EntDodgeAdapter dodge;

    public Nadenken attack;

    public Nadenken melee;

    public EntInteractAdapter sight;

    public Nadenken checkattack;

    public float pausetime;

    public float pauze_tijd;

    public float attack_finished;

    public float[] saved_goal = { 0, 0, 0 };

    public float search_time;

    public float trail_time;

    public float[] last_sighting = { 0, 0, 0 };

    public int attack_state;

    public int lefty;

    public float idle_time;

    public int linkcount;

    public int power_armor_type;

    public int power_armor_power;

    public Nadenken staan;

    public Nadenken rennen;

    public Nadenken dood;

    public Nadenken aanvallen;

    /** Writes the monsterinfo to the file.*/
    public void write(QuakeFile f) throws IOException {
        f.writeBoolean(currentmove != null);
        if (currentmove != null) {
            currentmove.write(f);
        }
        f.writeInt(aiflags);
        f.writeInt(nextframe);
        f.writeFloat(scale);
        f.writeAdapter(stand);
        f.writeAdapter(idle);
        f.writeAdapter(search);
        f.writeAdapter(walk);
        f.writeAdapter(run);
        f.writeAdapter(dodge);
        f.writeAdapter(attack);
        f.writeAdapter(melee);
        f.writeAdapter(sight);
        f.writeAdapter(checkattack);
        f.writeFloat(pausetime);
        f.writeFloat(attack_finished);
        f.writeVector(saved_goal);
        f.writeFloat(search_time);
        f.writeFloat(trail_time);
        f.writeVector(last_sighting);
        f.writeInt(attack_state);
        f.writeInt(lefty);
        f.writeFloat(idle_time);
        f.writeInt(linkcount);
        f.writeInt(power_armor_power);
        f.writeInt(power_armor_type);
    }

    /** Writes the monsterinfo to the file.*/
    public void read(QuakeFile f) throws IOException {
        if (f.readBoolean()) {
            currentmove = new mmove_t();
            currentmove.read(f);
        } else {
            currentmove = null;
        }
        aiflags = f.readInt();
        nextframe = f.readInt();
        scale = f.readFloat();
        stand = (Nadenken) f.readAdapter();
        idle = (Nadenken) f.readAdapter();
        search = (Nadenken) f.readAdapter();
        walk = (Nadenken) f.readAdapter();
        run = (Nadenken) f.readAdapter();
        dodge = (EntDodgeAdapter) f.readAdapter();
        attack = (Nadenken) f.readAdapter();
        melee = (Nadenken) f.readAdapter();
        sight = (EntInteractAdapter) f.readAdapter();
        checkattack = (Nadenken) f.readAdapter();
        pausetime = f.readFloat();
        attack_finished = f.readFloat();
        saved_goal = f.readVector();
        search_time = f.readFloat();
        trail_time = f.readFloat();
        last_sighting = f.readVector();
        attack_state = f.readInt();
        lefty = f.readInt();
        idle_time = f.readFloat();
        linkcount = f.readInt();
        power_armor_power = f.readInt();
        power_armor_type = f.readInt();
    }
}
