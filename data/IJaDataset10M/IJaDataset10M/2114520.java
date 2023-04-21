package wb;

import static wb.Ents.*;

public class Ent {

    public static Ent makeEntry() {
        return new Ent();
    }

    public static Ent ent_MakeEnt(int tag) {
        Ent ent = new Ent();
        ent.TAG = tag;
        ent.SEG = null;
        ent.ID = -1;
        ent.BLK = new byte[blkSize];
        return ent;
    }

    public static int ent_Tag(Ent entry) {
        return entry.TAG;
    }

    public static Ent ent_Next(Ent entry) {
        return entry.NEXT;
    }

    public static Seg ent_Seg(Ent entry) {
        return entry.SEG;
    }

    public static int ent_Id(Ent entry) {
        return entry.ID;
    }

    public static byte[] ent_Blk(Ent entry) {
        return entry.BLK;
    }

    public static int ent_Age(Ent entry) {
        return entry.AGE;
    }

    public static boolean ent_Dty_P(Ent entry) {
        return entry.DTY;
    }

    public static int ent_Pus(Ent entry) {
        return entry.PUS;
    }

    public static int ent_Acc(Ent entry) {
        return entry.ACC;
    }

    public static int ent_Ref(Ent entry) {
        return entry.REF;
    }

    public static void ent_SetTag(Ent entry, int tag) {
        entry.TAG = tag;
    }

    public static void ent_SetNext(Ent entry, Ent next) {
        entry.NEXT = next;
    }

    public static void ent_SetSeg(Ent entry, Seg seg) {
        entry.SEG = seg;
    }

    public static void ent_SetId(Ent entry, int num) {
        entry.ID = num;
    }

    public static void ent_SetAge(Ent entry, int age) {
        entry.AGE = age;
    }

    public static void ent_SetDty(Ent entry, Object A) {
        entry.DTY = ((A == null) ? false : true);
    }

    public static void ent_SetDty(Ent entry, boolean dty) {
        entry.DTY = dty;
    }

    public static void ent_SetPus(Ent entry, int pus) {
        entry.PUS = pus;
    }

    public static void ent_SetAcc(Ent entry, int acc) {
        entry.ACC = acc;
    }

    public static void ent_SetRef(Ent entry, int ref) {
        entry.REF = ref;
    }

    public Ent NEXT;

    public int ID;

    public byte[] BLK;

    public boolean DTY;

    public int TAG, AGE, PUS, ACC, REF;

    public Seg SEG;
}
