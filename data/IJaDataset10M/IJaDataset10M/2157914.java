package net.sf.snowball.ext;

import net.sf.snowball.SnowballProgram;
import net.sf.snowball.Among;

/**
 * Generated class implementing code defined by a snowball script.
 */
public class swedishStemmer extends SnowballProgram {

    private Among a_0[] = { new Among("a", -1, 1, "", this), new Among("arna", 0, 1, "", this), new Among("erna", 0, 1, "", this), new Among("heterna", 2, 1, "", this), new Among("orna", 0, 1, "", this), new Among("ad", -1, 1, "", this), new Among("e", -1, 1, "", this), new Among("ade", 6, 1, "", this), new Among("ande", 6, 1, "", this), new Among("arne", 6, 1, "", this), new Among("are", 6, 1, "", this), new Among("aste", 6, 1, "", this), new Among("en", -1, 1, "", this), new Among("anden", 12, 1, "", this), new Among("aren", 12, 1, "", this), new Among("heten", 12, 1, "", this), new Among("ern", -1, 1, "", this), new Among("ar", -1, 1, "", this), new Among("er", -1, 1, "", this), new Among("heter", 18, 1, "", this), new Among("or", -1, 1, "", this), new Among("s", -1, 2, "", this), new Among("as", 21, 1, "", this), new Among("arnas", 22, 1, "", this), new Among("ernas", 22, 1, "", this), new Among("ornas", 22, 1, "", this), new Among("es", 21, 1, "", this), new Among("ades", 26, 1, "", this), new Among("andes", 26, 1, "", this), new Among("ens", 21, 1, "", this), new Among("arens", 29, 1, "", this), new Among("hetens", 29, 1, "", this), new Among("erns", 21, 1, "", this), new Among("at", -1, 1, "", this), new Among("andet", -1, 1, "", this), new Among("het", -1, 1, "", this), new Among("ast", -1, 1, "", this) };

    private Among a_1[] = { new Among("dd", -1, -1, "", this), new Among("gd", -1, -1, "", this), new Among("nn", -1, -1, "", this), new Among("dt", -1, -1, "", this), new Among("gt", -1, -1, "", this), new Among("kt", -1, -1, "", this), new Among("tt", -1, -1, "", this) };

    private Among a_2[] = { new Among("ig", -1, 1, "", this), new Among("lig", 0, 1, "", this), new Among("els", -1, 1, "", this), new Among("fullt", -1, 3, "", this), new Among("löst", -1, 2, "", this) };

    private static final char g_v[] = { 17, 65, 16, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 24, 0, 32 };

    private static final char g_s_ending[] = { 119, 127, 149 };

    private int I_p1;

    private void copy_from(swedishStemmer other) {
        I_p1 = other.I_p1;
        super.copy_from(other);
    }

    private boolean r_mark_regions() {
        int v_1;
        I_p1 = limit;
        golab0: while (true) {
            v_1 = cursor;
            lab1: do {
                if (!(in_grouping(g_v, 97, 246))) {
                    break lab1;
                }
                cursor = v_1;
                break golab0;
            } while (false);
            cursor = v_1;
            if (cursor >= limit) {
                return false;
            }
            cursor++;
        }
        golab2: while (true) {
            lab3: do {
                if (!(out_grouping(g_v, 97, 246))) {
                    break lab3;
                }
                break golab2;
            } while (false);
            if (cursor >= limit) {
                return false;
            }
            cursor++;
        }
        I_p1 = cursor;
        lab4: do {
            if (!(I_p1 < 3)) {
                break lab4;
            }
            I_p1 = 3;
        } while (false);
        return true;
    }

    private boolean r_main_suffix() {
        int among_var;
        int v_1;
        int v_2;
        v_1 = limit - cursor;
        if (cursor < I_p1) {
            return false;
        }
        cursor = I_p1;
        v_2 = limit_backward;
        limit_backward = cursor;
        cursor = limit - v_1;
        ket = cursor;
        among_var = find_among_b(a_0, 37);
        if (among_var == 0) {
            limit_backward = v_2;
            return false;
        }
        bra = cursor;
        limit_backward = v_2;
        switch(among_var) {
            case 0:
                return false;
            case 1:
                slice_del();
                break;
            case 2:
                if (!(in_grouping_b(g_s_ending, 98, 121))) {
                    return false;
                }
                slice_del();
                break;
        }
        return true;
    }

    private boolean r_consonant_pair() {
        int v_1;
        int v_2;
        int v_3;
        v_1 = limit - cursor;
        if (cursor < I_p1) {
            return false;
        }
        cursor = I_p1;
        v_2 = limit_backward;
        limit_backward = cursor;
        cursor = limit - v_1;
        v_3 = limit - cursor;
        if (find_among_b(a_1, 7) == 0) {
            limit_backward = v_2;
            return false;
        }
        cursor = limit - v_3;
        ket = cursor;
        if (cursor <= limit_backward) {
            limit_backward = v_2;
            return false;
        }
        cursor--;
        bra = cursor;
        slice_del();
        limit_backward = v_2;
        return true;
    }

    private boolean r_other_suffix() {
        int among_var;
        int v_1;
        int v_2;
        v_1 = limit - cursor;
        if (cursor < I_p1) {
            return false;
        }
        cursor = I_p1;
        v_2 = limit_backward;
        limit_backward = cursor;
        cursor = limit - v_1;
        ket = cursor;
        among_var = find_among_b(a_2, 5);
        if (among_var == 0) {
            limit_backward = v_2;
            return false;
        }
        bra = cursor;
        switch(among_var) {
            case 0:
                limit_backward = v_2;
                return false;
            case 1:
                slice_del();
                break;
            case 2:
                slice_from("lös");
                break;
            case 3:
                slice_from("full");
                break;
        }
        limit_backward = v_2;
        return true;
    }

    public boolean stem() {
        int v_1;
        int v_2;
        int v_3;
        int v_4;
        v_1 = cursor;
        lab0: do {
            if (!r_mark_regions()) {
                break lab0;
            }
        } while (false);
        cursor = v_1;
        limit_backward = cursor;
        cursor = limit;
        v_2 = limit - cursor;
        lab1: do {
            if (!r_main_suffix()) {
                break lab1;
            }
        } while (false);
        cursor = limit - v_2;
        v_3 = limit - cursor;
        lab2: do {
            if (!r_consonant_pair()) {
                break lab2;
            }
        } while (false);
        cursor = limit - v_3;
        v_4 = limit - cursor;
        lab3: do {
            if (!r_other_suffix()) {
                break lab3;
            }
        } while (false);
        cursor = limit - v_4;
        cursor = limit_backward;
        return true;
    }
}
