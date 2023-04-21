package game.entities;

public class Stats {

    public int Lv = 1, Xp = 100, MHp = 10, MMp = 6, Hp = 10, Mp = 6, Atk = 10, Def = 10, Sp = 5, Rng = 64;

    public void addXP(int xp) {
        Xp -= xp;
        while (Xp < 0) {
            Lv++;
            Sp += 3;
            Xp += Lv * 100;
        }
    }

    public void addHP(int h) {
        Hp += h;
        if (Hp > MHp) {
            Hp = MHp;
        }
    }

    public void addMp(int m) {
        Mp += m;
        if (Mp > MMp) {
            Mp = MMp;
        }
    }

    public void addMHp() {
        if (Sp > 0) {
            MHp += 5;
        }
    }

    public void addMMp() {
        if (Sp > 0) {
            MMp += 3;
        }
    }

    public void addAtk() {
        if (Sp > 0) {
            MHp += 1;
        }
    }

    public void addDef() {
        if (Sp > 0) {
            MHp += 1;
        }
    }
}
