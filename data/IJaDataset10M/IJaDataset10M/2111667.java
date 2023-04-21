package fgen;

/**
 *
 * @author Oleg Rachaev
 */
public class CPlayer {

    public int p_id;

    public int num;

    public int name;

    public int pos;

    public int exp;

    public int pow;

    public int stam;

    public int hlth;

    public int physCond;

    public int mrlCond;

    public int acc;

    public int spd;

    public int shotPwr;

    public int shotAcc;

    public int tech;

    public int mrk;

    public int head;

    public int drbl;

    public int pass;

    public int coord;

    public CPlayer() {
    }

    public CPlayer(String theParamsStr) {
        loadFromString(theParamsStr);
    }

    public void loadFromString(String theParamsStr) {
        String ss[] = theParamsStr.split(" ");
        p_id = Integer.parseInt(ss[0]);
        num = Integer.parseInt(ss[1]);
        name = Integer.parseInt(ss[2]);
        pos = Integer.parseInt(ss[3]);
        exp = Integer.parseInt(ss[4]);
        pow = Integer.parseInt(ss[5]);
        stam = Integer.parseInt(ss[6]);
        hlth = Integer.parseInt(ss[7]);
        physCond = Integer.parseInt(ss[8]);
        mrlCond = Integer.parseInt(ss[9]);
        acc = Integer.parseInt(ss[10]);
        spd = Integer.parseInt(ss[11]);
        shotPwr = Integer.parseInt(ss[12]);
        shotAcc = Integer.parseInt(ss[13]);
        tech = Integer.parseInt(ss[14]);
        mrk = Integer.parseInt(ss[15]);
        head = Integer.parseInt(ss[16]);
        drbl = Integer.parseInt(ss[17]);
        pass = Integer.parseInt(ss[18]);
        coord = Integer.parseInt(ss[19]);
    }

    public String writeToStr() {
        String res = String.valueOf(p_id) + " " + String.valueOf(num) + " " + String.valueOf(name) + " " + String.valueOf(pos) + " " + String.valueOf(exp) + " " + String.valueOf(pow) + " " + String.valueOf(stam) + " " + String.valueOf(hlth) + " " + String.valueOf(physCond) + " " + String.valueOf(mrlCond) + " " + String.valueOf(acc) + " " + String.valueOf(spd) + " " + String.valueOf(shotPwr) + " " + String.valueOf(shotAcc) + " " + String.valueOf(tech) + " " + String.valueOf(mrk) + " " + String.valueOf(head) + " " + String.valueOf(drbl) + " " + String.valueOf(pass) + " " + String.valueOf(coord);
        return res;
    }
}
