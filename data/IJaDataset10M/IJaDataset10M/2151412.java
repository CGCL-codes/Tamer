package net.sf.snowball.ext;

import net.sf.snowball.SnowballProgram;
import net.sf.snowball.Among;

/**
 * Generated class implementing code defined by a snowball script.
 */
public class germanStemmer extends SnowballProgram {

    private Among a_0[] = { new Among("", -1, 6, "", this), new Among("U", 0, 2, "", this), new Among("Y", 0, 1, "", this), new Among("ä", 0, 3, "", this), new Among("ö", 0, 4, "", this), new Among("ü", 0, 5, "", this) };

    private Among a_1[] = { new Among("e", -1, 1, "", this), new Among("em", -1, 1, "", this), new Among("en", -1, 1, "", this), new Among("ern", -1, 1, "", this), new Among("er", -1, 1, "", this), new Among("s", -1, 2, "", this), new Among("es", 5, 1, "", this) };

    private Among a_2[] = { new Among("en", -1, 1, "", this), new Among("er", -1, 1, "", this), new Among("st", -1, 2, "", this), new Among("est", 2, 1, "", this) };

    private Among a_3[] = { new Among("ig", -1, 1, "", this), new Among("lich", -1, 1, "", this) };

    private Among a_4[] = { new Among("end", -1, 1, "", this), new Among("ig", -1, 2, "", this), new Among("ung", -1, 1, "", this), new Among("lich", -1, 3, "", this), new Among("isch", -1, 2, "", this), new Among("ik", -1, 2, "", this), new Among("heit", -1, 3, "", this), new Among("keit", -1, 4, "", this) };

    private static final char g_v[] = { 17, 65, 16, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 8, 0, 32, 8 };

    private static final char g_s_ending[] = { 117, 30, 5 };

    private static final char g_st_ending[] = { 117, 30, 4 };

    private int I_p2;

    private int I_p1;

    private void copy_from(germanStemmer other) {
        I_p2 = other.I_p2;
        I_p1 = other.I_p1;
        super.copy_from(other);
    }

    private boolean r_prelude() {
        int v_1;
        int v_2;
        int v_3;
        int v_4;
        int v_5;
        int v_6;
        v_1 = cursor;
        replab0: while (true) {
            v_2 = cursor;
            lab1: do {
                lab2: do {
                    v_3 = cursor;
                    lab3: do {
                        bra = cursor;
                        if (!(eq_s(1, "ß"))) {
                            break lab3;
                        }
                        ket = cursor;
                        slice_from("ss");
                        break lab2;
                    } while (false);
                    cursor = v_3;
                    if (cursor >= limit) {
                        break lab1;
                    }
                    cursor++;
                } while (false);
                continue replab0;
            } while (false);
            cursor = v_2;
            break replab0;
        }
        cursor = v_1;
        replab4: while (true) {
            v_4 = cursor;
            lab5: do {
                golab6: while (true) {
                    v_5 = cursor;
                    lab7: do {
                        if (!(in_grouping(g_v, 97, 252))) {
                            break lab7;
                        }
                        bra = cursor;
                        lab8: do {
                            v_6 = cursor;
                            lab9: do {
                                if (!(eq_s(1, "u"))) {
                                    break lab9;
                                }
                                ket = cursor;
                                if (!(in_grouping(g_v, 97, 252))) {
                                    break lab9;
                                }
                                slice_from("U");
                                break lab8;
                            } while (false);
                            cursor = v_6;
                            if (!(eq_s(1, "y"))) {
                                break lab7;
                            }
                            ket = cursor;
                            if (!(in_grouping(g_v, 97, 252))) {
                                break lab7;
                            }
                            slice_from("Y");
                        } while (false);
                        cursor = v_5;
                        break golab6;
                    } while (false);
                    cursor = v_5;
                    if (cursor >= limit) {
                        break lab5;
                    }
                    cursor++;
                }
                continue replab4;
            } while (false);
            cursor = v_4;
            break replab4;
        }
        return true;
    }

    private boolean r_mark_regions() {
        I_p1 = limit;
        I_p2 = limit;
        golab0: while (true) {
            lab1: do {
                if (!(in_grouping(g_v, 97, 252))) {
                    break lab1;
                }
                break golab0;
            } while (false);
            if (cursor >= limit) {
                return false;
            }
            cursor++;
        }
        golab2: while (true) {
            lab3: do {
                if (!(out_grouping(g_v, 97, 252))) {
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
        golab5: while (true) {
            lab6: do {
                if (!(in_grouping(g_v, 97, 252))) {
                    break lab6;
                }
                break golab5;
            } while (false);
            if (cursor >= limit) {
                return false;
            }
            cursor++;
        }
        golab7: while (true) {
            lab8: do {
                if (!(out_grouping(g_v, 97, 252))) {
                    break lab8;
                }
                break golab7;
            } while (false);
            if (cursor >= limit) {
                return false;
            }
            cursor++;
        }
        I_p2 = cursor;
        return true;
    }

    private boolean r_postlude() {
        int among_var;
        int v_1;
        replab0: while (true) {
            v_1 = cursor;
            lab1: do {
                bra = cursor;
                among_var = find_among(a_0, 6);
                if (among_var == 0) {
                    break lab1;
                }
                ket = cursor;
                switch(among_var) {
                    case 0:
                        break lab1;
                    case 1:
                        slice_from("y");
                        break;
                    case 2:
                        slice_from("u");
                        break;
                    case 3:
                        slice_from("a");
                        break;
                    case 4:
                        slice_from("o");
                        break;
                    case 5:
                        slice_from("u");
                        break;
                    case 6:
                        if (cursor >= limit) {
                            break lab1;
                        }
                        cursor++;
                        break;
                }
                continue replab0;
            } while (false);
            cursor = v_1;
            break replab0;
        }
        return true;
    }

    private boolean r_R1() {
        if (!(I_p1 <= cursor)) {
            return false;
        }
        return true;
    }

    private boolean r_R2() {
        if (!(I_p2 <= cursor)) {
            return false;
        }
        return true;
    }

    private boolean r_standard_suffix() {
        int among_var;
        int v_1;
        int v_2;
        int v_3;
        int v_4;
        int v_5;
        int v_6;
        int v_7;
        int v_8;
        int v_9;
        v_1 = limit - cursor;
        lab0: do {
            ket = cursor;
            among_var = find_among_b(a_1, 7);
            if (among_var == 0) {
                break lab0;
            }
            bra = cursor;
            if (!r_R1()) {
                break lab0;
            }
            switch(among_var) {
                case 0:
                    break lab0;
                case 1:
                    slice_del();
                    break;
                case 2:
                    if (!(in_grouping_b(g_s_ending, 98, 116))) {
                        break lab0;
                    }
                    slice_del();
                    break;
            }
        } while (false);
        cursor = limit - v_1;
        v_2 = limit - cursor;
        lab1: do {
            ket = cursor;
            among_var = find_among_b(a_2, 4);
            if (among_var == 0) {
                break lab1;
            }
            bra = cursor;
            if (!r_R1()) {
                break lab1;
            }
            switch(among_var) {
                case 0:
                    break lab1;
                case 1:
                    slice_del();
                    break;
                case 2:
                    if (!(in_grouping_b(g_st_ending, 98, 116))) {
                        break lab1;
                    }
                    {
                        int c = cursor - 3;
                        if (limit_backward > c || c > limit) {
                            break lab1;
                        }
                        cursor = c;
                    }
                    slice_del();
                    break;
            }
        } while (false);
        cursor = limit - v_2;
        v_3 = limit - cursor;
        lab2: do {
            ket = cursor;
            among_var = find_among_b(a_4, 8);
            if (among_var == 0) {
                break lab2;
            }
            bra = cursor;
            if (!r_R2()) {
                break lab2;
            }
            switch(among_var) {
                case 0:
                    break lab2;
                case 1:
                    slice_del();
                    v_4 = limit - cursor;
                    lab3: do {
                        ket = cursor;
                        if (!(eq_s_b(2, "ig"))) {
                            cursor = limit - v_4;
                            break lab3;
                        }
                        bra = cursor;
                        {
                            v_5 = limit - cursor;
                            lab4: do {
                                if (!(eq_s_b(1, "e"))) {
                                    break lab4;
                                }
                                cursor = limit - v_4;
                                break lab3;
                            } while (false);
                            cursor = limit - v_5;
                        }
                        if (!r_R2()) {
                            cursor = limit - v_4;
                            break lab3;
                        }
                        slice_del();
                    } while (false);
                    break;
                case 2:
                    {
                        v_6 = limit - cursor;
                        lab5: do {
                            if (!(eq_s_b(1, "e"))) {
                                break lab5;
                            }
                            break lab2;
                        } while (false);
                        cursor = limit - v_6;
                    }
                    slice_del();
                    break;
                case 3:
                    slice_del();
                    v_7 = limit - cursor;
                    lab6: do {
                        ket = cursor;
                        lab7: do {
                            v_8 = limit - cursor;
                            lab8: do {
                                if (!(eq_s_b(2, "er"))) {
                                    break lab8;
                                }
                                break lab7;
                            } while (false);
                            cursor = limit - v_8;
                            if (!(eq_s_b(2, "en"))) {
                                cursor = limit - v_7;
                                break lab6;
                            }
                        } while (false);
                        bra = cursor;
                        if (!r_R1()) {
                            cursor = limit - v_7;
                            break lab6;
                        }
                        slice_del();
                    } while (false);
                    break;
                case 4:
                    slice_del();
                    v_9 = limit - cursor;
                    lab9: do {
                        ket = cursor;
                        among_var = find_among_b(a_3, 2);
                        if (among_var == 0) {
                            cursor = limit - v_9;
                            break lab9;
                        }
                        bra = cursor;
                        if (!r_R2()) {
                            cursor = limit - v_9;
                            break lab9;
                        }
                        switch(among_var) {
                            case 0:
                                cursor = limit - v_9;
                                break lab9;
                            case 1:
                                slice_del();
                                break;
                        }
                    } while (false);
                    break;
            }
        } while (false);
        cursor = limit - v_3;
        return true;
    }

    public boolean stem() {
        int v_1;
        int v_2;
        int v_3;
        int v_4;
        v_1 = cursor;
        lab0: do {
            if (!r_prelude()) {
                break lab0;
            }
        } while (false);
        cursor = v_1;
        v_2 = cursor;
        lab1: do {
            if (!r_mark_regions()) {
                break lab1;
            }
        } while (false);
        cursor = v_2;
        limit_backward = cursor;
        cursor = limit;
        v_3 = limit - cursor;
        lab2: do {
            if (!r_standard_suffix()) {
                break lab2;
            }
        } while (false);
        cursor = limit - v_3;
        cursor = limit_backward;
        v_4 = cursor;
        lab3: do {
            if (!r_postlude()) {
                break lab3;
            }
        } while (false);
        cursor = v_4;
        return true;
    }
}
