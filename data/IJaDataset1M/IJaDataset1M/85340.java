package com.rapidminer.operator.text.io.stemmer.snowball;

/**
 * Generated class implementing code defined by a snowball script.
 * 
 * @author Sebastian Land
 */
public class FrenchSnowballStemming extends SnowballStemmingProgram {

    private Among a_0[] = { new Among("col", -1, -1, "", this), new Among("par", -1, -1, "", this), new Among("tap", -1, -1, "", this) };

    private Among a_1[] = { new Among("", -1, 4, "", this), new Among("I", 0, 1, "", this), new Among("U", 0, 2, "", this), new Among("Y", 0, 3, "", this) };

    private Among a_2[] = { new Among("iqU", -1, 3, "", this), new Among("abl", -1, 3, "", this), new Among("Ièr", -1, 4, "", this), new Among("ièr", -1, 4, "", this), new Among("eus", -1, 2, "", this), new Among("iv", -1, 1, "", this) };

    private Among a_3[] = { new Among("ic", -1, 2, "", this), new Among("abil", -1, 1, "", this), new Among("iv", -1, 3, "", this) };

    private Among a_4[] = { new Among("iqUe", -1, 1, "", this), new Among("atrice", -1, 2, "", this), new Among("ance", -1, 1, "", this), new Among("ence", -1, 5, "", this), new Among("logie", -1, 3, "", this), new Among("able", -1, 1, "", this), new Among("isme", -1, 1, "", this), new Among("euse", -1, 11, "", this), new Among("iste", -1, 1, "", this), new Among("ive", -1, 8, "", this), new Among("if", -1, 8, "", this), new Among("usion", -1, 4, "", this), new Among("ation", -1, 2, "", this), new Among("ution", -1, 4, "", this), new Among("ateur", -1, 2, "", this), new Among("iqUes", -1, 1, "", this), new Among("atrices", -1, 2, "", this), new Among("ances", -1, 1, "", this), new Among("ences", -1, 5, "", this), new Among("logies", -1, 3, "", this), new Among("ables", -1, 1, "", this), new Among("ismes", -1, 1, "", this), new Among("euses", -1, 11, "", this), new Among("istes", -1, 1, "", this), new Among("ives", -1, 8, "", this), new Among("ifs", -1, 8, "", this), new Among("usions", -1, 4, "", this), new Among("ations", -1, 2, "", this), new Among("utions", -1, 4, "", this), new Among("ateurs", -1, 2, "", this), new Among("ments", -1, 15, "", this), new Among("ements", 30, 6, "", this), new Among("issements", 31, 12, "", this), new Among("ités", -1, 7, "", this), new Among("ment", -1, 15, "", this), new Among("ement", 34, 6, "", this), new Among("issement", 35, 12, "", this), new Among("amment", 34, 13, "", this), new Among("emment", 34, 14, "", this), new Among("aux", -1, 10, "", this), new Among("eaux", 39, 9, "", this), new Among("eux", -1, 1, "", this), new Among("ité", -1, 7, "", this) };

    private Among a_5[] = { new Among("ira", -1, 1, "", this), new Among("ie", -1, 1, "", this), new Among("isse", -1, 1, "", this), new Among("issante", -1, 1, "", this), new Among("i", -1, 1, "", this), new Among("irai", 4, 1, "", this), new Among("ir", -1, 1, "", this), new Among("iras", -1, 1, "", this), new Among("ies", -1, 1, "", this), new Among("îmes", -1, 1, "", this), new Among("isses", -1, 1, "", this), new Among("issantes", -1, 1, "", this), new Among("îtes", -1, 1, "", this), new Among("is", -1, 1, "", this), new Among("irais", 13, 1, "", this), new Among("issais", 13, 1, "", this), new Among("irions", -1, 1, "", this), new Among("issions", -1, 1, "", this), new Among("irons", -1, 1, "", this), new Among("issons", -1, 1, "", this), new Among("issants", -1, 1, "", this), new Among("it", -1, 1, "", this), new Among("irait", 21, 1, "", this), new Among("issait", 21, 1, "", this), new Among("issant", -1, 1, "", this), new Among("iraIent", -1, 1, "", this), new Among("issaIent", -1, 1, "", this), new Among("irent", -1, 1, "", this), new Among("issent", -1, 1, "", this), new Among("iront", -1, 1, "", this), new Among("ît", -1, 1, "", this), new Among("iriez", -1, 1, "", this), new Among("issiez", -1, 1, "", this), new Among("irez", -1, 1, "", this), new Among("issez", -1, 1, "", this) };

    private Among a_6[] = { new Among("a", -1, 3, "", this), new Among("era", 0, 2, "", this), new Among("asse", -1, 3, "", this), new Among("ante", -1, 3, "", this), new Among("ée", -1, 2, "", this), new Among("ai", -1, 3, "", this), new Among("erai", 5, 2, "", this), new Among("er", -1, 2, "", this), new Among("as", -1, 3, "", this), new Among("eras", 8, 2, "", this), new Among("âmes", -1, 3, "", this), new Among("asses", -1, 3, "", this), new Among("antes", -1, 3, "", this), new Among("âtes", -1, 3, "", this), new Among("ées", -1, 2, "", this), new Among("ais", -1, 3, "", this), new Among("erais", 15, 2, "", this), new Among("ions", -1, 1, "", this), new Among("erions", 17, 2, "", this), new Among("assions", 17, 3, "", this), new Among("erons", -1, 2, "", this), new Among("ants", -1, 3, "", this), new Among("és", -1, 2, "", this), new Among("ait", -1, 3, "", this), new Among("erait", 23, 2, "", this), new Among("ant", -1, 3, "", this), new Among("aIent", -1, 3, "", this), new Among("eraIent", 26, 2, "", this), new Among("èrent", -1, 2, "", this), new Among("assent", -1, 3, "", this), new Among("eront", -1, 2, "", this), new Among("ât", -1, 3, "", this), new Among("ez", -1, 2, "", this), new Among("iez", 32, 2, "", this), new Among("eriez", 33, 2, "", this), new Among("assiez", 33, 3, "", this), new Among("erez", 32, 2, "", this), new Among("é", -1, 2, "", this) };

    private Among a_7[] = { new Among("e", -1, 3, "", this), new Among("Ière", 0, 2, "", this), new Among("ière", 0, 2, "", this), new Among("ion", -1, 1, "", this), new Among("Ier", -1, 2, "", this), new Among("ier", -1, 2, "", this), new Among("ë", -1, 4, "", this) };

    private Among a_8[] = { new Among("ell", -1, -1, "", this), new Among("eill", -1, -1, "", this), new Among("enn", -1, -1, "", this), new Among("onn", -1, -1, "", this), new Among("ett", -1, -1, "", this) };

    private static final char g_v[] = { 17, 65, 16, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 128, 130, 103, 8, 5 };

    private static final char g_keep_with_s[] = { 1, 65, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 128 };

    private int I_p2;

    private int I_p1;

    private int I_pV;

    @SuppressWarnings("unused")
    private void copy_from(FrenchSnowballStemming other) {
        I_p2 = other.I_p2;
        I_p1 = other.I_p1;
        I_pV = other.I_pV;
        super.copy_from(other);
    }

    private boolean r_prelude() {
        int v_1;
        int v_2;
        int v_3;
        int v_4;
        replab0: while (true) {
            v_1 = cursor;
            lab1: do {
                golab2: while (true) {
                    v_2 = cursor;
                    lab3: do {
                        lab4: do {
                            v_3 = cursor;
                            lab5: do {
                                if (!(in_grouping(g_v, 97, 251))) {
                                    break lab5;
                                }
                                bra = cursor;
                                lab6: do {
                                    v_4 = cursor;
                                    lab7: do {
                                        if (!(eq_s(1, "u"))) {
                                            break lab7;
                                        }
                                        ket = cursor;
                                        if (!(in_grouping(g_v, 97, 251))) {
                                            break lab7;
                                        }
                                        slice_from("U");
                                        break lab6;
                                    } while (false);
                                    cursor = v_4;
                                    lab8: do {
                                        if (!(eq_s(1, "i"))) {
                                            break lab8;
                                        }
                                        ket = cursor;
                                        if (!(in_grouping(g_v, 97, 251))) {
                                            break lab8;
                                        }
                                        slice_from("I");
                                        break lab6;
                                    } while (false);
                                    cursor = v_4;
                                    if (!(eq_s(1, "y"))) {
                                        break lab5;
                                    }
                                    ket = cursor;
                                    slice_from("Y");
                                } while (false);
                                break lab4;
                            } while (false);
                            cursor = v_3;
                            lab9: do {
                                bra = cursor;
                                if (!(eq_s(1, "y"))) {
                                    break lab9;
                                }
                                ket = cursor;
                                if (!(in_grouping(g_v, 97, 251))) {
                                    break lab9;
                                }
                                slice_from("Y");
                                break lab4;
                            } while (false);
                            cursor = v_3;
                            if (!(eq_s(1, "q"))) {
                                break lab3;
                            }
                            bra = cursor;
                            if (!(eq_s(1, "u"))) {
                                break lab3;
                            }
                            ket = cursor;
                            slice_from("U");
                        } while (false);
                        cursor = v_2;
                        break golab2;
                    } while (false);
                    cursor = v_2;
                    if (cursor >= limit) {
                        break lab1;
                    }
                    cursor++;
                }
                continue replab0;
            } while (false);
            cursor = v_1;
            break replab0;
        }
        return true;
    }

    private boolean r_mark_regions() {
        int v_1;
        int v_2;
        int v_4;
        I_pV = limit;
        I_p1 = limit;
        I_p2 = limit;
        v_1 = cursor;
        lab0: do {
            lab1: do {
                v_2 = cursor;
                lab2: do {
                    if (!(in_grouping(g_v, 97, 251))) {
                        break lab2;
                    }
                    if (!(in_grouping(g_v, 97, 251))) {
                        break lab2;
                    }
                    if (cursor >= limit) {
                        break lab2;
                    }
                    cursor++;
                    break lab1;
                } while (false);
                cursor = v_2;
                lab3: do {
                    if (find_among(a_0, 3) == 0) {
                        break lab3;
                    }
                    break lab1;
                } while (false);
                cursor = v_2;
                if (cursor >= limit) {
                    break lab0;
                }
                cursor++;
                golab4: while (true) {
                    lab5: do {
                        if (!(in_grouping(g_v, 97, 251))) {
                            break lab5;
                        }
                        break golab4;
                    } while (false);
                    if (cursor >= limit) {
                        break lab0;
                    }
                    cursor++;
                }
            } while (false);
            I_pV = cursor;
        } while (false);
        cursor = v_1;
        v_4 = cursor;
        lab6: do {
            golab7: while (true) {
                lab8: do {
                    if (!(in_grouping(g_v, 97, 251))) {
                        break lab8;
                    }
                    break golab7;
                } while (false);
                if (cursor >= limit) {
                    break lab6;
                }
                cursor++;
            }
            golab9: while (true) {
                lab10: do {
                    if (!(out_grouping(g_v, 97, 251))) {
                        break lab10;
                    }
                    break golab9;
                } while (false);
                if (cursor >= limit) {
                    break lab6;
                }
                cursor++;
            }
            I_p1 = cursor;
            golab11: while (true) {
                lab12: do {
                    if (!(in_grouping(g_v, 97, 251))) {
                        break lab12;
                    }
                    break golab11;
                } while (false);
                if (cursor >= limit) {
                    break lab6;
                }
                cursor++;
            }
            golab13: while (true) {
                lab14: do {
                    if (!(out_grouping(g_v, 97, 251))) {
                        break lab14;
                    }
                    break golab13;
                } while (false);
                if (cursor >= limit) {
                    break lab6;
                }
                cursor++;
            }
            I_p2 = cursor;
        } while (false);
        cursor = v_4;
        return true;
    }

    private boolean r_postlude() {
        int among_var;
        int v_1;
        replab0: while (true) {
            v_1 = cursor;
            lab1: do {
                bra = cursor;
                among_var = find_among(a_1, 4);
                if (among_var == 0) {
                    break lab1;
                }
                ket = cursor;
                switch(among_var) {
                    case 0:
                        break lab1;
                    case 1:
                        slice_from("i");
                        break;
                    case 2:
                        slice_from("u");
                        break;
                    case 3:
                        slice_from("y");
                        break;
                    case 4:
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

    private boolean r_RV() {
        if (!(I_pV <= cursor)) {
            return false;
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
        int v_10;
        int v_11;
        ket = cursor;
        among_var = find_among_b(a_4, 43);
        if (among_var == 0) {
            return false;
        }
        bra = cursor;
        switch(among_var) {
            case 0:
                return false;
            case 1:
                if (!r_R2()) {
                    return false;
                }
                slice_del();
                break;
            case 2:
                if (!r_R2()) {
                    return false;
                }
                slice_del();
                v_1 = limit - cursor;
                lab0: do {
                    ket = cursor;
                    if (!(eq_s_b(2, "ic"))) {
                        cursor = limit - v_1;
                        break lab0;
                    }
                    bra = cursor;
                    lab1: do {
                        v_2 = limit - cursor;
                        lab2: do {
                            if (!r_R2()) {
                                break lab2;
                            }
                            slice_del();
                            break lab1;
                        } while (false);
                        cursor = limit - v_2;
                        slice_from("iqU");
                    } while (false);
                } while (false);
                break;
            case 3:
                if (!r_R2()) {
                    return false;
                }
                slice_from("log");
                break;
            case 4:
                if (!r_R2()) {
                    return false;
                }
                slice_from("u");
                break;
            case 5:
                if (!r_R2()) {
                    return false;
                }
                slice_from("ent");
                break;
            case 6:
                if (!r_RV()) {
                    return false;
                }
                slice_del();
                v_3 = limit - cursor;
                lab3: do {
                    ket = cursor;
                    among_var = find_among_b(a_2, 6);
                    if (among_var == 0) {
                        cursor = limit - v_3;
                        break lab3;
                    }
                    bra = cursor;
                    switch(among_var) {
                        case 0:
                            cursor = limit - v_3;
                            break lab3;
                        case 1:
                            if (!r_R2()) {
                                cursor = limit - v_3;
                                break lab3;
                            }
                            slice_del();
                            ket = cursor;
                            if (!(eq_s_b(2, "at"))) {
                                cursor = limit - v_3;
                                break lab3;
                            }
                            bra = cursor;
                            if (!r_R2()) {
                                cursor = limit - v_3;
                                break lab3;
                            }
                            slice_del();
                            break;
                        case 2:
                            lab4: do {
                                v_4 = limit - cursor;
                                lab5: do {
                                    if (!r_R2()) {
                                        break lab5;
                                    }
                                    slice_del();
                                    break lab4;
                                } while (false);
                                cursor = limit - v_4;
                                if (!r_R1()) {
                                    cursor = limit - v_3;
                                    break lab3;
                                }
                                slice_from("eux");
                            } while (false);
                            break;
                        case 3:
                            if (!r_R2()) {
                                cursor = limit - v_3;
                                break lab3;
                            }
                            slice_del();
                            break;
                        case 4:
                            if (!r_RV()) {
                                cursor = limit - v_3;
                                break lab3;
                            }
                            slice_from("i");
                            break;
                    }
                } while (false);
                break;
            case 7:
                if (!r_R2()) {
                    return false;
                }
                slice_del();
                v_5 = limit - cursor;
                lab6: do {
                    ket = cursor;
                    among_var = find_among_b(a_3, 3);
                    if (among_var == 0) {
                        cursor = limit - v_5;
                        break lab6;
                    }
                    bra = cursor;
                    switch(among_var) {
                        case 0:
                            cursor = limit - v_5;
                            break lab6;
                        case 1:
                            lab7: do {
                                v_6 = limit - cursor;
                                lab8: do {
                                    if (!r_R2()) {
                                        break lab8;
                                    }
                                    slice_del();
                                    break lab7;
                                } while (false);
                                cursor = limit - v_6;
                                slice_from("abl");
                            } while (false);
                            break;
                        case 2:
                            lab9: do {
                                v_7 = limit - cursor;
                                lab10: do {
                                    if (!r_R2()) {
                                        break lab10;
                                    }
                                    slice_del();
                                    break lab9;
                                } while (false);
                                cursor = limit - v_7;
                                slice_from("iqU");
                            } while (false);
                            break;
                        case 3:
                            if (!r_R2()) {
                                cursor = limit - v_5;
                                break lab6;
                            }
                            slice_del();
                            break;
                    }
                } while (false);
                break;
            case 8:
                if (!r_R2()) {
                    return false;
                }
                slice_del();
                v_8 = limit - cursor;
                lab11: do {
                    ket = cursor;
                    if (!(eq_s_b(2, "at"))) {
                        cursor = limit - v_8;
                        break lab11;
                    }
                    bra = cursor;
                    if (!r_R2()) {
                        cursor = limit - v_8;
                        break lab11;
                    }
                    slice_del();
                    ket = cursor;
                    if (!(eq_s_b(2, "ic"))) {
                        cursor = limit - v_8;
                        break lab11;
                    }
                    bra = cursor;
                    lab12: do {
                        v_9 = limit - cursor;
                        lab13: do {
                            if (!r_R2()) {
                                break lab13;
                            }
                            slice_del();
                            break lab12;
                        } while (false);
                        cursor = limit - v_9;
                        slice_from("iqU");
                    } while (false);
                } while (false);
                break;
            case 9:
                slice_from("eau");
                break;
            case 10:
                if (!r_R1()) {
                    return false;
                }
                slice_from("al");
                break;
            case 11:
                lab14: do {
                    v_10 = limit - cursor;
                    lab15: do {
                        if (!r_R2()) {
                            break lab15;
                        }
                        slice_del();
                        break lab14;
                    } while (false);
                    cursor = limit - v_10;
                    if (!r_R1()) {
                        return false;
                    }
                    slice_from("eux");
                } while (false);
                break;
            case 12:
                if (!r_R1()) {
                    return false;
                }
                if (!(out_grouping_b(g_v, 97, 251))) {
                    return false;
                }
                slice_del();
                break;
            case 13:
                if (!r_RV()) {
                    return false;
                }
                slice_from("ant");
                return false;
            case 14:
                if (!r_RV()) {
                    return false;
                }
                slice_from("ent");
                return false;
            case 15:
                v_11 = limit - cursor;
                if (!(in_grouping_b(g_v, 97, 251))) {
                    return false;
                }
                if (!r_RV()) {
                    return false;
                }
                cursor = limit - v_11;
                slice_del();
                return false;
        }
        return true;
    }

    private boolean r_i_verb_suffix() {
        int among_var;
        int v_1;
        int v_2;
        v_1 = limit - cursor;
        if (cursor < I_pV) {
            return false;
        }
        cursor = I_pV;
        v_2 = limit_backward;
        limit_backward = cursor;
        cursor = limit - v_1;
        ket = cursor;
        among_var = find_among_b(a_5, 35);
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
                if (!(out_grouping_b(g_v, 97, 251))) {
                    limit_backward = v_2;
                    return false;
                }
                slice_del();
                break;
        }
        limit_backward = v_2;
        return true;
    }

    private boolean r_verb_suffix() {
        int among_var;
        int v_1;
        int v_2;
        int v_3;
        v_1 = limit - cursor;
        if (cursor < I_pV) {
            return false;
        }
        cursor = I_pV;
        v_2 = limit_backward;
        limit_backward = cursor;
        cursor = limit - v_1;
        ket = cursor;
        among_var = find_among_b(a_6, 38);
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
                if (!r_R2()) {
                    limit_backward = v_2;
                    return false;
                }
                slice_del();
                break;
            case 2:
                slice_del();
                break;
            case 3:
                slice_del();
                v_3 = limit - cursor;
                lab0: do {
                    ket = cursor;
                    if (!(eq_s_b(1, "e"))) {
                        cursor = limit - v_3;
                        break lab0;
                    }
                    bra = cursor;
                    slice_del();
                } while (false);
                break;
        }
        limit_backward = v_2;
        return true;
    }

    private boolean r_residual_suffix() {
        int among_var;
        int v_1;
        int v_2;
        int v_3;
        int v_4;
        int v_5;
        v_1 = limit - cursor;
        lab0: do {
            ket = cursor;
            if (!(eq_s_b(1, "s"))) {
                cursor = limit - v_1;
                break lab0;
            }
            bra = cursor;
            v_2 = limit - cursor;
            if (!(out_grouping_b(g_keep_with_s, 97, 232))) {
                cursor = limit - v_1;
                break lab0;
            }
            cursor = limit - v_2;
            slice_del();
        } while (false);
        v_3 = limit - cursor;
        if (cursor < I_pV) {
            return false;
        }
        cursor = I_pV;
        v_4 = limit_backward;
        limit_backward = cursor;
        cursor = limit - v_3;
        ket = cursor;
        among_var = find_among_b(a_7, 7);
        if (among_var == 0) {
            limit_backward = v_4;
            return false;
        }
        bra = cursor;
        switch(among_var) {
            case 0:
                limit_backward = v_4;
                return false;
            case 1:
                if (!r_R2()) {
                    limit_backward = v_4;
                    return false;
                }
                lab1: do {
                    v_5 = limit - cursor;
                    lab2: do {
                        if (!(eq_s_b(1, "s"))) {
                            break lab2;
                        }
                        break lab1;
                    } while (false);
                    cursor = limit - v_5;
                    if (!(eq_s_b(1, "t"))) {
                        limit_backward = v_4;
                        return false;
                    }
                } while (false);
                slice_del();
                break;
            case 2:
                slice_from("i");
                break;
            case 3:
                slice_del();
                break;
            case 4:
                if (!(eq_s_b(2, "gu"))) {
                    limit_backward = v_4;
                    return false;
                }
                slice_del();
                break;
        }
        limit_backward = v_4;
        return true;
    }

    private boolean r_un_double() {
        int v_1;
        v_1 = limit - cursor;
        if (find_among_b(a_8, 5) == 0) {
            return false;
        }
        cursor = limit - v_1;
        ket = cursor;
        if (cursor <= limit_backward) {
            return false;
        }
        cursor--;
        bra = cursor;
        slice_del();
        return true;
    }

    private boolean r_un_accent() {
        int v_3;
        {
            int v_1 = 1;
            replab0: while (true) {
                lab1: do {
                    if (!(out_grouping_b(g_v, 97, 251))) {
                        break lab1;
                    }
                    v_1--;
                    continue replab0;
                } while (false);
                break replab0;
            }
            if (v_1 > 0) {
                return false;
            }
        }
        ket = cursor;
        lab2: do {
            v_3 = limit - cursor;
            lab3: do {
                if (!(eq_s_b(1, "é"))) {
                    break lab3;
                }
                break lab2;
            } while (false);
            cursor = limit - v_3;
            if (!(eq_s_b(1, "è"))) {
                return false;
            }
        } while (false);
        bra = cursor;
        slice_from("e");
        return true;
    }

    public boolean stem() {
        int v_1;
        int v_2;
        int v_3;
        int v_4;
        int v_5;
        int v_6;
        int v_7;
        int v_8;
        int v_9;
        int v_10;
        int v_11;
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
            lab3: do {
                v_4 = limit - cursor;
                lab4: do {
                    v_5 = limit - cursor;
                    lab5: do {
                        v_6 = limit - cursor;
                        lab6: do {
                            if (!r_standard_suffix()) {
                                break lab6;
                            }
                            break lab5;
                        } while (false);
                        cursor = limit - v_6;
                        lab7: do {
                            if (!r_i_verb_suffix()) {
                                break lab7;
                            }
                            break lab5;
                        } while (false);
                        cursor = limit - v_6;
                        if (!r_verb_suffix()) {
                            break lab4;
                        }
                    } while (false);
                    cursor = limit - v_5;
                    v_7 = limit - cursor;
                    lab8: do {
                        ket = cursor;
                        lab9: do {
                            v_8 = limit - cursor;
                            lab10: do {
                                if (!(eq_s_b(1, "Y"))) {
                                    break lab10;
                                }
                                bra = cursor;
                                slice_from("i");
                                break lab9;
                            } while (false);
                            cursor = limit - v_8;
                            if (!(eq_s_b(1, "ç"))) {
                                cursor = limit - v_7;
                                break lab8;
                            }
                            bra = cursor;
                            slice_from("c");
                        } while (false);
                    } while (false);
                    break lab3;
                } while (false);
                cursor = limit - v_4;
                if (!r_residual_suffix()) {
                    break lab2;
                }
            } while (false);
        } while (false);
        cursor = limit - v_3;
        v_9 = limit - cursor;
        lab11: do {
            if (!r_un_double()) {
                break lab11;
            }
        } while (false);
        cursor = limit - v_9;
        v_10 = limit - cursor;
        lab12: do {
            if (!r_un_accent()) {
                break lab12;
            }
        } while (false);
        cursor = limit - v_10;
        cursor = limit_backward;
        v_11 = cursor;
        lab13: do {
            if (!r_postlude()) {
                break lab13;
            }
        } while (false);
        cursor = v_11;
        return true;
    }
}
