package cleanj.StdLib;

import java.io.PrintStream;
import cleanj.lang.*;
import cleanj.lang.descriptor.*;
import cleanj.StdEnv.*;

public final class StdArrayExtensions extends CleanModule {

    public static final StdArrayExtensions $$it = new StdArrayExtensions();

    public static final int e_StdArrayExtensions_cupdateArrElt$semicolon = 0;

    public static boolean e_StdArrayExtensions_cupdateArrElt$semicolon(CleanVM vm) throws CleanException {
        {
            Object o = vm.as_get(0);
            CleanNode.push(vm, o);
            CleanNode.fill(o, new CleanDescriptorFunction(CleanSystem.$$it, CleanSystem._cycle_in_spine, 0, "_cycle_in_spine"), null);
        }
        CleanNode.eval(vm, vm.as_get(0));
        CleanNode.fill_r(vm, vm.as_get(1), StdArrayExtensions.e_StdArrayExtensions_rupdateArrElt$semicolon, 1, 0, 0, 0);
        vm.pop_a(1);
        return false;
    }

    public static final int e_StdArrayExtensions_nupdateArrElt$semicolon$dotupdateArrElt = 1;

    public static boolean e_StdArrayExtensions_nupdateArrElt$semicolon$dotupdateArrElt(CleanVM vm) throws CleanException {
        {
            Object o = vm.as_get(0);
            CleanNode.push(vm, o);
            CleanNode.fill(o, new CleanDescriptorFunction(CleanSystem.$$it, CleanSystem._cycle_in_spine, 0, "_cycle_in_spine"), null);
        }
        return e_StdArrayExtensions_eaupdateArrElt$semicolon$dotupdateArrElt(vm);
    }

    public static final int e_StdArrayExtensions_eaupdateArrElt$semicolon$dotupdateArrElt = 2;

    public static boolean e_StdArrayExtensions_eaupdateArrElt$semicolon$dotupdateArrElt(CleanVM vm) throws CleanException {
        CleanNode.eval(vm, vm.as_get(0));
        CleanNode.push_a_idx(vm, vm.as_pop(), 0);
        CleanNode.fill(vm.as_get(1), vm.as_get(0));
        vm.pop_a(1);
        return false;
    }

    public static final int e_StdArrayExtensions_lupdateArrElt$semicolon$dotupdateArrElt = 3;

    public static boolean e_StdArrayExtensions_lupdateArrElt$semicolon$dotupdateArrElt(CleanVM vm) throws CleanException {
        CleanNode.push_a_idx(vm, vm.as_pop(), 0);
        return false;
    }

    public static final int e_StdArrayExtensions_caccArrElt$semicolon = 4;

    public static boolean e_StdArrayExtensions_caccArrElt$semicolon(CleanVM vm) throws CleanException {
        {
            Object o = vm.as_get(0);
            CleanNode.push(vm, o);
            CleanNode.fill(o, new CleanDescriptorFunction(CleanSystem.$$it, CleanSystem._cycle_in_spine, 0, "_cycle_in_spine"), null);
        }
        CleanNode.eval(vm, vm.as_get(0));
        CleanNode.fill_r(vm, vm.as_get(1), StdArrayExtensions.e_StdArrayExtensions_raccArrElt$semicolon, 1, 0, 0, 0);
        vm.pop_a(1);
        return false;
    }

    public static final int e_StdArrayExtensions_naccArrElt$semicolon$dotaccArrElt = 5;

    public static boolean e_StdArrayExtensions_naccArrElt$semicolon$dotaccArrElt(CleanVM vm) throws CleanException {
        {
            Object o = vm.as_get(0);
            CleanNode.push(vm, o);
            CleanNode.fill(o, new CleanDescriptorFunction(CleanSystem.$$it, CleanSystem._cycle_in_spine, 0, "_cycle_in_spine"), null);
        }
        return e_StdArrayExtensions_eaaccArrElt$semicolon$dotaccArrElt(vm);
    }

    public static final int e_StdArrayExtensions_eaaccArrElt$semicolon$dotaccArrElt = 6;

    public static boolean e_StdArrayExtensions_eaaccArrElt$semicolon$dotaccArrElt(CleanVM vm) throws CleanException {
        CleanNode.eval(vm, vm.as_get(0));
        CleanNode.push_a_idx(vm, vm.as_pop(), 0);
        CleanNode.fill(vm.as_get(1), vm.as_get(0));
        vm.pop_a(1);
        return false;
    }

    public static final int e_StdArrayExtensions_laccArrElt$semicolon$dotaccArrElt = 7;

    public static boolean e_StdArrayExtensions_laccArrElt$semicolon$dotaccArrElt(CleanVM vm) throws CleanException {
        CleanNode.push_a_idx(vm, vm.as_pop(), 0);
        return false;
    }

    public static final int e_StdArrayExtensions_laccArrElt$semicolon10 = 8;

    public static boolean e_StdArrayExtensions_laccArrElt$semicolon10(CleanVM vm) throws CleanException {
        CleanNode.push(vm, vm.as_pop());
        if (ea11(vm)) {
            while (vm.m.$$dispatch(vm, vm.f)) ;
        }
        vm.as_push(vm.as_get(1));
        vm.as_push(vm.as_get(1));
        vm.as_push(new CleanNode(CleanDescriptorTuple.$$it, CleanDescriptorTuple.$$it.buildpop(vm, 2, 0)));
        vm.updatepop_a(0, 2);
        return false;
    }

    public static final int e_StdArrayExtensions_naccArrElt$semicolon10 = 9;

    public static boolean e_StdArrayExtensions_naccArrElt$semicolon10(CleanVM vm) throws CleanException {
        {
            Object o = vm.as_get(0);
            CleanNode.push(vm, o);
            CleanNode.fill(o, new CleanDescriptorFunction(CleanSystem.$$it, CleanSystem._cycle_in_spine, 0, "_cycle_in_spine"), null);
        }
        if (ea11(vm)) {
            while (vm.m.$$dispatch(vm, vm.f)) ;
        }
        vm.as_push(vm.as_get(1));
        vm.as_push(vm.as_get(1));
        CleanNode.fill(vm, vm.as_get(4), CleanDescriptorTuple.$$it, 2, 0);
        vm.pop_a(2);
        return false;
    }

    public static final int ea11 = 10;

    public static boolean ea11(CleanVM vm) throws CleanException {
        CleanNode.eval(vm, vm.as_get(2));
        CleanNode.eval(vm, vm.as_get(1));
        CleanNode.eval(vm, vm.as_get(0));
        vm.as_push(vm.as_get(2));
        CleanNode.push(vm, vm.as_get(2));
        vm.update_a(0, 3);
        vm.update_a(1, 2);
        vm.pop_a(2);
        return e_StdArrayExtensions_saccArrElt$semicolon10(vm);
    }

    public static final int e_StdArrayExtensions_saccArrElt$semicolon10 = 11;

    public static boolean e_StdArrayExtensions_saccArrElt$semicolon10(CleanVM vm) throws CleanException {
        return s11(vm);
    }

    public static final int s11 = 12;

    public static boolean s11(CleanVM vm) throws CleanException {
        vm.as_push(vm.as_get(1));
        vm.push_b(0);
        vm.as_push(vm.as_get(1));
        vm.update_a(1, 3);
        vm.updatepop_a(0, 2);
        vm.updatepop_b(0, 1);
        vm.m = $$it;
        vm.f = s15;
        return true;
    }

    public static final int e_StdArrayExtensions_laccArrElt$semicolon9 = 13;

    public static boolean e_StdArrayExtensions_laccArrElt$semicolon9(CleanVM vm) throws CleanException {
        CleanNode.push(vm, vm.as_pop());
        if (ea10(vm)) {
            while (vm.m.$$dispatch(vm, vm.f)) ;
        }
        vm.as_push(vm.as_get(1));
        vm.as_push(vm.as_get(1));
        vm.as_push(new CleanNode(CleanDescriptorTuple.$$it, CleanDescriptorTuple.$$it.buildpop(vm, 2, 0)));
        vm.updatepop_a(0, 2);
        return false;
    }

    public static final int e_StdArrayExtensions_naccArrElt$semicolon9 = 14;

    public static boolean e_StdArrayExtensions_naccArrElt$semicolon9(CleanVM vm) throws CleanException {
        {
            Object o = vm.as_get(0);
            CleanNode.push(vm, o);
            CleanNode.fill(o, new CleanDescriptorFunction(CleanSystem.$$it, CleanSystem._cycle_in_spine, 0, "_cycle_in_spine"), null);
        }
        if (ea10(vm)) {
            while (vm.m.$$dispatch(vm, vm.f)) ;
        }
        vm.as_push(vm.as_get(1));
        vm.as_push(vm.as_get(1));
        CleanNode.fill(vm, vm.as_get(4), CleanDescriptorTuple.$$it, 2, 0);
        vm.pop_a(2);
        return false;
    }

    public static final int ea10 = 15;

    public static boolean ea10(CleanVM vm) throws CleanException {
        CleanNode.eval(vm, vm.as_get(2));
        CleanNode.eval(vm, vm.as_get(1));
        CleanNode.eval(vm, vm.as_get(0));
        vm.as_push(vm.as_get(2));
        CleanNode.push(vm, vm.as_get(2));
        vm.update_a(0, 3);
        vm.update_a(1, 2);
        vm.pop_a(2);
        return e_StdArrayExtensions_saccArrElt$semicolon9(vm);
    }

    public static final int e_StdArrayExtensions_saccArrElt$semicolon9 = 16;

    public static boolean e_StdArrayExtensions_saccArrElt$semicolon9(CleanVM vm) throws CleanException {
        return s10(vm);
    }

    public static final int s10 = 17;

    public static boolean s10(CleanVM vm) throws CleanException {
        vm.as_push(vm.as_get(1));
        vm.push_b(0);
        vm.as_push(vm.as_get(1));
        vm.update_a(1, 3);
        vm.updatepop_a(0, 2);
        vm.updatepop_b(0, 1);
        vm.m = $$it;
        vm.f = s14;
        return true;
    }

    public static final int e_StdArrayExtensions_lupdateArrElt$semicolon8 = 18;

    public static boolean e_StdArrayExtensions_lupdateArrElt$semicolon8(CleanVM vm) throws CleanException {
        CleanNode.push(vm, vm.as_pop());
        if (ea9(vm)) {
            while (vm.m.$$dispatch(vm, vm.f)) ;
        }
        vm.as_push(vm.as_get(0));
        vm.updatepop_a(0, 1);
        return false;
    }

    public static final int e_StdArrayExtensions_nupdateArrElt$semicolon8 = 19;

    public static boolean e_StdArrayExtensions_nupdateArrElt$semicolon8(CleanVM vm) throws CleanException {
        {
            Object o = vm.as_get(0);
            CleanNode.push(vm, o);
            CleanNode.fill(o, new CleanDescriptorFunction(CleanSystem.$$it, CleanSystem._cycle_in_spine, 0, "_cycle_in_spine"), null);
        }
        if (ea9(vm)) {
            while (vm.m.$$dispatch(vm, vm.f)) ;
        }
        vm.as_push(vm.as_get(0));
        CleanNode.fill(vm.as_get(2), vm.as_pop());
        vm.pop_a(1);
        return false;
    }

    public static final int ea9 = 20;

    public static boolean ea9(CleanVM vm) throws CleanException {
        CleanNode.eval(vm, vm.as_get(2));
        CleanNode.eval(vm, vm.as_get(1));
        CleanNode.eval(vm, vm.as_get(0));
        vm.as_push(vm.as_get(2));
        CleanNode.push(vm, vm.as_get(2));
        vm.update_a(0, 3);
        vm.update_a(1, 2);
        vm.pop_a(2);
        return e_StdArrayExtensions_supdateArrElt$semicolon8(vm);
    }

    public static final int e_StdArrayExtensions_supdateArrElt$semicolon8 = 21;

    public static boolean e_StdArrayExtensions_supdateArrElt$semicolon8(CleanVM vm) throws CleanException {
        return s9(vm);
    }

    public static final int s9 = 22;

    public static boolean s9(CleanVM vm) throws CleanException {
        vm.as_push(vm.as_get(1));
        vm.push_b(0);
        vm.as_push(vm.as_get(1));
        vm.update_a(1, 3);
        vm.updatepop_a(0, 2);
        vm.updatepop_b(0, 1);
        vm.m = $$it;
        vm.f = s13;
        return true;
    }

    public static final int e_StdArrayExtensions_lupdateArrElt$semicolon7 = 23;

    public static boolean e_StdArrayExtensions_lupdateArrElt$semicolon7(CleanVM vm) throws CleanException {
        CleanNode.push(vm, vm.as_pop());
        if (ea8(vm)) {
            while (vm.m.$$dispatch(vm, vm.f)) ;
        }
        vm.as_push(vm.as_get(0));
        vm.updatepop_a(0, 1);
        return false;
    }

    public static final int e_StdArrayExtensions_nupdateArrElt$semicolon7 = 24;

    public static boolean e_StdArrayExtensions_nupdateArrElt$semicolon7(CleanVM vm) throws CleanException {
        {
            Object o = vm.as_get(0);
            CleanNode.push(vm, o);
            CleanNode.fill(o, new CleanDescriptorFunction(CleanSystem.$$it, CleanSystem._cycle_in_spine, 0, "_cycle_in_spine"), null);
        }
        if (ea8(vm)) {
            while (vm.m.$$dispatch(vm, vm.f)) ;
        }
        vm.as_push(vm.as_get(0));
        CleanNode.fill(vm.as_get(2), vm.as_pop());
        vm.pop_a(1);
        return false;
    }

    public static final int ea8 = 25;

    public static boolean ea8(CleanVM vm) throws CleanException {
        CleanNode.eval(vm, vm.as_get(2));
        CleanNode.eval(vm, vm.as_get(1));
        CleanNode.eval(vm, vm.as_get(0));
        vm.as_push(vm.as_get(2));
        CleanNode.push(vm, vm.as_get(2));
        vm.update_a(0, 3);
        vm.update_a(1, 2);
        vm.pop_a(2);
        return e_StdArrayExtensions_supdateArrElt$semicolon7(vm);
    }

    public static final int e_StdArrayExtensions_supdateArrElt$semicolon7 = 26;

    public static boolean e_StdArrayExtensions_supdateArrElt$semicolon7(CleanVM vm) throws CleanException {
        return s8(vm);
    }

    public static final int s8 = 27;

    public static boolean s8(CleanVM vm) throws CleanException {
        vm.as_push(vm.as_get(1));
        vm.push_b(0);
        vm.as_push(vm.as_get(1));
        vm.update_a(1, 3);
        vm.updatepop_a(0, 2);
        vm.updatepop_b(0, 1);
        vm.m = $$it;
        vm.f = s12;
        return true;
    }

    public static final int s15 = 28;

    public static boolean s15(CleanVM vm) throws CleanException {
        vm.push_b(0);
        vm.as_push(vm.as_get(1));
        vm.update_a(1, 2);
        vm.updatepop_a(0, 1);
        vm.as_push(vm.as_get(0));
        CleanNode.select(vm, CleanDescriptorNode.$$it, vm.as_pop(), vm.bs_i_pop());
        vm.as_push(vm.as_get(0));
        vm.as_push(vm.as_get(3));
        vm.update_a(3, 4);
        vm.update_a(1, 3);
        vm.updatepop_a(0, 2);
        if (CleanSystem.e_system_sAP(vm)) {
            while (vm.m.$$dispatch(vm, vm.f)) ;
        }
        CleanNode.push_a_idx(vm, vm.as_get(0), 1);
        CleanNode.eval(vm, vm.as_get(0));
        vm.push_b(0);
        vm.as_push(vm.as_get(2));
        vm.updatepop_b(0, 1);
        vm.update_a(2, 3);
        vm.update_a(1, 2);
        vm.updatepop_a(0, 1);
        CleanNode.update(vm, CleanDescriptorNode.$$it, vm.as_pop(), vm.bs_i_pop());
        CleanNode.push_a_idx(vm, vm.as_get(1), 0);
        CleanNode.eval(vm, vm.as_get(0));
        vm.update_a(1, 2);
        vm.updatepop_a(0, 1);
        return false;
    }

    public static final int s14 = 29;

    public static boolean s14(CleanVM vm) throws CleanException {
        vm.push_b(0);
        vm.as_push(vm.as_get(1));
        vm.update_a(1, 2);
        vm.updatepop_a(0, 1);
        vm.as_push(vm.as_get(0));
        CleanNode.select(vm, CleanDescriptorNode.$$it, vm.as_pop(), vm.bs_i_pop());
        vm.as_push(vm.as_get(0));
        vm.as_push(vm.as_get(3));
        vm.update_a(3, 4);
        vm.update_a(1, 3);
        vm.updatepop_a(0, 2);
        if (CleanSystem.e_system_sAP(vm)) {
            while (vm.m.$$dispatch(vm, vm.f)) ;
        }
        CleanNode.push_a_idx(vm, vm.as_get(0), 1);
        vm.push_b(0);
        vm.as_push(vm.as_get(2));
        vm.updatepop_b(0, 1);
        vm.update_a(2, 3);
        vm.update_a(1, 2);
        vm.updatepop_a(0, 1);
        CleanNode.update(vm, CleanDescriptorNode.$$it, vm.as_pop(), vm.bs_i_pop());
        CleanNode.push_a_idx(vm, vm.as_get(1), 0);
        CleanNode.eval(vm, vm.as_get(0));
        vm.update_a(1, 2);
        vm.updatepop_a(0, 1);
        return false;
    }

    public static final int s13 = 30;

    public static boolean s13(CleanVM vm) throws CleanException {
        vm.push_b(0);
        vm.as_push(vm.as_get(1));
        vm.update_a(1, 2);
        vm.updatepop_a(0, 1);
        vm.as_push(vm.as_get(0));
        CleanNode.select(vm, CleanDescriptorNode.$$it, vm.as_pop(), vm.bs_i_pop());
        vm.as_push(vm.as_get(0));
        vm.as_push(vm.as_get(3));
        vm.update_a(3, 4);
        vm.update_a(1, 3);
        vm.updatepop_a(0, 2);
        if (CleanSystem.e_system_sAP(vm)) {
            while (vm.m.$$dispatch(vm, vm.f)) ;
        }
        vm.push_b(0);
        vm.as_push(vm.as_get(1));
        vm.update_a(1, 2);
        vm.updatepop_a(0, 1);
        vm.updatepop_b(0, 1);
        CleanNode.update(vm, CleanDescriptorNode.$$it, vm.as_pop(), vm.bs_i_pop());
        return false;
    }

    public static final int s12 = 31;

    public static boolean s12(CleanVM vm) throws CleanException {
        vm.push_b(0);
        vm.as_push(vm.as_get(1));
        vm.update_a(1, 2);
        vm.updatepop_a(0, 1);
        vm.as_push(vm.as_get(0));
        CleanNode.select(vm, CleanDescriptorNode.$$it, vm.as_pop(), vm.bs_i_pop());
        vm.as_push(vm.as_get(0));
        vm.as_push(vm.as_get(3));
        {
            CleanDescriptorFunction f = CleanSystem.e_system_dAP;
            if (f.needCopy()) {
                f = f.set_nmodl(CleanSystem.$$it, CleanSystem.e_system_nAP);
            }
            vm.as_push(new CleanNode(f, f.buildpop(vm, 2, 0)));
        }
        vm.push_b(0);
        vm.as_push(vm.as_get(2));
        vm.update_a(1, 4);
        vm.updatepop_a(0, 3);
        vm.updatepop_b(0, 1);
        CleanNode.update(vm, CleanDescriptorNode.$$it, vm.as_pop(), vm.bs_i_pop());
        return false;
    }

    public static final int e_StdArrayExtensions_lcreateStrictArrDecFoldSt = 32;

    public static boolean e_StdArrayExtensions_lcreateStrictArrDecFoldSt(CleanVM vm) throws CleanException {
        CleanNode.push(vm, vm.as_pop());
        if (ea7(vm)) {
            while (vm.m.$$dispatch(vm, vm.f)) ;
        }
        vm.as_push(vm.as_get(1));
        vm.as_push(vm.as_get(1));
        vm.as_push(new CleanNode(CleanDescriptorTuple.$$it, CleanDescriptorTuple.$$it.buildpop(vm, 2, 0)));
        vm.updatepop_a(0, 2);
        return false;
    }

    public static final int e_StdArrayExtensions_ncreateStrictArrDecFoldSt = 33;

    public static boolean e_StdArrayExtensions_ncreateStrictArrDecFoldSt(CleanVM vm) throws CleanException {
        {
            Object o = vm.as_get(0);
            CleanNode.push(vm, o);
            CleanNode.fill(o, new CleanDescriptorFunction(CleanSystem.$$it, CleanSystem._cycle_in_spine, 0, "_cycle_in_spine"), null);
        }
        if (ea7(vm)) {
            while (vm.m.$$dispatch(vm, vm.f)) ;
        }
        vm.as_push(vm.as_get(1));
        vm.as_push(vm.as_get(1));
        CleanNode.fill(vm, vm.as_get(4), CleanDescriptorTuple.$$it, 2, 0);
        vm.pop_a(2);
        return false;
    }

    public static final int ea7 = 34;

    public static boolean ea7(CleanVM vm) throws CleanException {
        CleanNode.eval(vm, vm.as_get(2));
        CleanNode.eval(vm, vm.as_get(1));
        CleanNode.eval(vm, vm.as_get(0));
        CleanNode.push(vm, vm.as_get(0));
        vm.pop_a(1);
        return e_StdArrayExtensions_screateStrictArrDecFoldSt(vm);
    }

    public static final int e_StdArrayExtensions_screateStrictArrDecFoldSt = 35;

    public static boolean e_StdArrayExtensions_screateStrictArrDecFoldSt(CleanVM vm) throws CleanException {
        return s7(vm);
    }

    public static final int s7 = 36;

    public static boolean s7(CleanVM vm) throws CleanException {
        vm.bs_i_push(0);
        vm.push_b(1);
        vm.ltI();
        if (!vm.bs_b_pop()) {
            vm.m = $$it;
            vm.f = ok2;
            return true;
        }
        vm.as_push("createStrictArrDecFoldSt: called with negative size parameter\n".toCharArray());
        vm.updatepop_a(0, 2);
        vm.pop_b(1);
        if (StdMisc.e_StdMisc_sabort(vm)) {
            while (vm.m.$$dispatch(vm, vm.f)) ;
        }
        CleanNode.push(vm, vm.as_pop());
        CleanNode.eval(vm, vm.as_get(1));
        CleanNode.eval(vm, vm.as_get(0));
        vm.as_push(vm.as_get(0));
        vm.update_a(0, 1);
        vm.pop_a(1);
        return false;
    }

    public static final int ok2 = 37;

    public static boolean ok2(CleanVM vm) throws CleanException {
        vm.as_push(new CleanNode(CleanDescriptorNil.$$it, CleanDescriptorNil.$$it.buildpop(vm, 0, 0)));
        vm.push_b(0);
        vm.as_push(CleanDescriptorNode.$$it.create_array(vm.bs_i_pop(), vm));
        vm.bs_i_push(1);
        vm.push_b(1);
        vm.subI();
        vm.as_push(vm.as_get(2));
        vm.as_push(vm.as_get(2));
        vm.bs_i_push(-1);
        vm.bs_i_push(-1);
        vm.as_push(vm.as_get(2));
        vm.update_a(2, 3);
        vm.update_a(0, 2);
        vm.pop_a(1);
        vm.push_b(2);
        vm.update_b(2, 3);
        vm.update_b(1, 2);
        vm.update_b(0, 1);
        vm.pop_b(1);
        vm.update_a(2, 4);
        vm.update_a(1, 3);
        vm.updatepop_a(0, 2);
        vm.update_b(2, 3);
        vm.update_b(1, 2);
        vm.updatepop_b(0, 1);
        vm.m = $$it;
        vm.f = createStrictArrIncFoldSt_loop;
        return true;
    }

    public static final int e_StdArrayExtensions_lcreateStrictArrIncFoldSt = 38;

    public static boolean e_StdArrayExtensions_lcreateStrictArrIncFoldSt(CleanVM vm) throws CleanException {
        CleanNode.push(vm, vm.as_pop());
        if (ea6(vm)) {
            while (vm.m.$$dispatch(vm, vm.f)) ;
        }
        vm.as_push(vm.as_get(1));
        vm.as_push(vm.as_get(1));
        vm.as_push(new CleanNode(CleanDescriptorTuple.$$it, CleanDescriptorTuple.$$it.buildpop(vm, 2, 0)));
        vm.updatepop_a(0, 2);
        return false;
    }

    public static final int e_StdArrayExtensions_ncreateStrictArrIncFoldSt = 39;

    public static boolean e_StdArrayExtensions_ncreateStrictArrIncFoldSt(CleanVM vm) throws CleanException {
        {
            Object o = vm.as_get(0);
            CleanNode.push(vm, o);
            CleanNode.fill(o, new CleanDescriptorFunction(CleanSystem.$$it, CleanSystem._cycle_in_spine, 0, "_cycle_in_spine"), null);
        }
        if (ea6(vm)) {
            while (vm.m.$$dispatch(vm, vm.f)) ;
        }
        vm.as_push(vm.as_get(1));
        vm.as_push(vm.as_get(1));
        CleanNode.fill(vm, vm.as_get(4), CleanDescriptorTuple.$$it, 2, 0);
        vm.pop_a(2);
        return false;
    }

    public static final int ea6 = 40;

    public static boolean ea6(CleanVM vm) throws CleanException {
        CleanNode.eval(vm, vm.as_get(2));
        CleanNode.eval(vm, vm.as_get(1));
        CleanNode.eval(vm, vm.as_get(0));
        CleanNode.push(vm, vm.as_get(0));
        vm.pop_a(1);
        return e_StdArrayExtensions_screateStrictArrIncFoldSt(vm);
    }

    public static final int e_StdArrayExtensions_screateStrictArrIncFoldSt = 41;

    public static boolean e_StdArrayExtensions_screateStrictArrIncFoldSt(CleanVM vm) throws CleanException {
        return s6(vm);
    }

    public static final int s6 = 42;

    public static boolean s6(CleanVM vm) throws CleanException {
        vm.bs_i_push(0);
        vm.push_b(1);
        vm.ltI();
        if (!vm.bs_b_pop()) {
            vm.m = $$it;
            vm.f = ok1;
            return true;
        }
        vm.as_push("createStrictArrIncFoldSt: called with negative size parameter\n".toCharArray());
        vm.updatepop_a(0, 2);
        vm.pop_b(1);
        if (StdMisc.e_StdMisc_sabort(vm)) {
            while (vm.m.$$dispatch(vm, vm.f)) ;
        }
        CleanNode.push(vm, vm.as_pop());
        CleanNode.eval(vm, vm.as_get(1));
        CleanNode.eval(vm, vm.as_get(0));
        vm.as_push(vm.as_get(0));
        vm.update_a(0, 1);
        vm.pop_a(1);
        return false;
    }

    public static final int ok1 = 43;

    public static boolean ok1(CleanVM vm) throws CleanException {
        vm.as_push(new CleanNode(CleanDescriptorNil.$$it, CleanDescriptorNil.$$it.buildpop(vm, 0, 0)));
        vm.push_b(0);
        vm.as_push(CleanDescriptorNode.$$it.create_array(vm.bs_i_pop(), vm));
        vm.as_push(vm.as_get(2));
        vm.as_push(vm.as_get(2));
        vm.bs_i_push(1);
        vm.push_b(1);
        vm.bs_i_push(0);
        vm.as_push(vm.as_get(2));
        vm.update_a(2, 3);
        vm.update_a(0, 2);
        vm.pop_a(1);
        vm.update_a(2, 4);
        vm.update_a(1, 3);
        vm.updatepop_a(0, 2);
        vm.update_b(2, 3);
        vm.update_b(1, 2);
        vm.updatepop_b(0, 1);
        vm.m = $$it;
        vm.f = createStrictArrIncFoldSt_loop;
        return true;
    }

    public static final int createStrictArrIncFoldSt_loop = 44;

    public static boolean createStrictArrIncFoldSt_loop(CleanVM vm) throws CleanException {
        vm.push_b(1);
        vm.push_b(1);
        vm.eqI();
        if (!vm.bs_b_pop()) {
            vm.m = $$it;
            vm.f = further;
            return true;
        }
        vm.as_push(vm.as_get(2));
        vm.as_push(vm.as_get(2));
        vm.update_a(1, 4);
        vm.updatepop_a(0, 3);
        vm.pop_b(3);
        return false;
    }

    public static final int further = 45;

    public static boolean further(CleanVM vm) throws CleanException {
        vm.as_push(new Integer(vm.bs_i_get(0)));
        vm.as_push(vm.as_get(1));
        if (CleanSystem.e_system_sAP(vm)) {
            while (vm.m.$$dispatch(vm, vm.f)) ;
        }
        vm.as_push(vm.as_get(3));
        vm.as_push(vm.as_get(1));
        vm.update_a(1, 2);
        vm.update_a(0, 1);
        vm.pop_a(1);
        vm.as_push(new CleanNode(CleanDescriptorNil.$$it, CleanDescriptorNil.$$it.buildpop(vm, 0, 0)));
        vm.update_a(0, 5);
        vm.pop_a(1);
        if (CleanSystem.e_system_sAP(vm)) {
            while (vm.m.$$dispatch(vm, vm.f)) ;
        }
        CleanNode.push_a_idx(vm, vm.as_get(0), 1);
        CleanNode.eval(vm, vm.as_get(0));
        CleanNode.push_a_idx(vm, vm.as_get(1), 0);
        CleanNode.eval(vm, vm.as_get(0));
        vm.push_b(0);
        vm.as_push(vm.as_get(4));
        vm.as_push(new CleanNode(CleanDescriptorNil.$$it, CleanDescriptorNil.$$it.buildpop(vm, 0, 0)));
        vm.update_a(0, 6);
        vm.update_a(0, 4);
        vm.pop_a(1);
        CleanNode.update(vm, CleanDescriptorNode.$$it, vm.as_pop(), vm.bs_i_pop());
        vm.push_b(2);
        vm.push_b(1);
        vm.addI();
        vm.as_push(vm.as_get(3));
        vm.push_b(3);
        vm.push_b(3);
        vm.push_b(2);
        vm.update_b(2, 3);
        vm.update_b(1, 2);
        vm.update_b(0, 1);
        vm.pop_b(1);
        vm.update_a(2, 6);
        vm.update_a(1, 5);
        vm.updatepop_a(0, 4);
        vm.update_b(2, 5);
        vm.update_b(1, 4);
        vm.updatepop_b(0, 3);
        vm.m = $$it;
        vm.f = createStrictArrIncFoldSt_loop;
        return true;
    }

    public static final int e_StdArrayExtensions_lcreateUnboxedRealArr = 46;

    public static boolean e_StdArrayExtensions_lcreateUnboxedRealArr(CleanVM vm) throws CleanException {
        CleanNode.push(vm, vm.as_pop());
        if (ea5(vm)) {
            while (vm.m.$$dispatch(vm, vm.f)) ;
        }
        vm.as_push(vm.as_get(0));
        vm.updatepop_a(0, 1);
        return false;
    }

    public static final int e_StdArrayExtensions_ncreateUnboxedRealArr = 47;

    public static boolean e_StdArrayExtensions_ncreateUnboxedRealArr(CleanVM vm) throws CleanException {
        {
            Object o = vm.as_get(0);
            CleanNode.push(vm, o);
            CleanNode.fill(o, new CleanDescriptorFunction(CleanSystem.$$it, CleanSystem._cycle_in_spine, 0, "_cycle_in_spine"), null);
        }
        if (ea5(vm)) {
            while (vm.m.$$dispatch(vm, vm.f)) ;
        }
        vm.as_push(vm.as_get(0));
        CleanNode.fill(vm.as_get(2), vm.as_pop());
        vm.pop_a(1);
        return false;
    }

    public static final int ea5 = 48;

    public static boolean ea5(CleanVM vm) throws CleanException {
        CleanNode.eval(vm, vm.as_get(1));
        CleanNode.eval(vm, vm.as_get(0));
        CleanNode.push(vm, vm.as_get(1));
        CleanNode.push(vm, vm.as_get(0));
        vm.pop_a(2);
        return e_StdArrayExtensions_screateUnboxedRealArr(vm);
    }

    public static final int e_StdArrayExtensions_screateUnboxedRealArr = 49;

    public static boolean e_StdArrayExtensions_screateUnboxedRealArr(CleanVM vm) throws CleanException {
        return s5(vm);
    }

    public static final int s5 = 50;

    public static boolean s5(CleanVM vm) throws CleanException {
        vm.push_b(2);
        vm.push_b(2);
        vm.push_b(2);
        vm.update_b(2, 5);
        vm.update_b(1, 4);
        vm.updatepop_b(0, 3);
        vm.as_push(CleanDescriptorReal.$$it.create_array(vm.bs_i_pop(), vm));
        return false;
    }

    public static final int e_StdArrayExtensions_lcreateUnboxedIntArr = 51;

    public static boolean e_StdArrayExtensions_lcreateUnboxedIntArr(CleanVM vm) throws CleanException {
        CleanNode.push(vm, vm.as_pop());
        if (ea4(vm)) {
            while (vm.m.$$dispatch(vm, vm.f)) ;
        }
        vm.as_push(vm.as_get(0));
        vm.updatepop_a(0, 1);
        return false;
    }

    public static final int e_StdArrayExtensions_ncreateUnboxedIntArr = 52;

    public static boolean e_StdArrayExtensions_ncreateUnboxedIntArr(CleanVM vm) throws CleanException {
        {
            Object o = vm.as_get(0);
            CleanNode.push(vm, o);
            CleanNode.fill(o, new CleanDescriptorFunction(CleanSystem.$$it, CleanSystem._cycle_in_spine, 0, "_cycle_in_spine"), null);
        }
        if (ea4(vm)) {
            while (vm.m.$$dispatch(vm, vm.f)) ;
        }
        vm.as_push(vm.as_get(0));
        CleanNode.fill(vm.as_get(2), vm.as_pop());
        vm.pop_a(1);
        return false;
    }

    public static final int ea4 = 53;

    public static boolean ea4(CleanVM vm) throws CleanException {
        CleanNode.eval(vm, vm.as_get(1));
        CleanNode.eval(vm, vm.as_get(0));
        CleanNode.push(vm, vm.as_get(1));
        CleanNode.push(vm, vm.as_get(0));
        vm.pop_a(2);
        return e_StdArrayExtensions_screateUnboxedIntArr(vm);
    }

    public static final int e_StdArrayExtensions_screateUnboxedIntArr = 54;

    public static boolean e_StdArrayExtensions_screateUnboxedIntArr(CleanVM vm) throws CleanException {
        return s4(vm);
    }

    public static final int s4 = 55;

    public static boolean s4(CleanVM vm) throws CleanException {
        vm.push_b(1);
        vm.push_b(1);
        vm.update_b(1, 3);
        vm.updatepop_b(0, 2);
        vm.as_push(CleanDescriptorInt.$$it.create_array(vm.bs_i_pop(), vm));
        return false;
    }

    public static final int e_StdArrayExtensions_lcreateString = 56;

    public static boolean e_StdArrayExtensions_lcreateString(CleanVM vm) throws CleanException {
        CleanNode.push(vm, vm.as_pop());
        if (ea3(vm)) {
            while (vm.m.$$dispatch(vm, vm.f)) ;
        }
        vm.as_push(vm.as_get(0));
        vm.updatepop_a(0, 1);
        return false;
    }

    public static final int e_StdArrayExtensions_ncreateString = 57;

    public static boolean e_StdArrayExtensions_ncreateString(CleanVM vm) throws CleanException {
        {
            Object o = vm.as_get(0);
            CleanNode.push(vm, o);
            CleanNode.fill(o, new CleanDescriptorFunction(CleanSystem.$$it, CleanSystem._cycle_in_spine, 0, "_cycle_in_spine"), null);
        }
        if (ea3(vm)) {
            while (vm.m.$$dispatch(vm, vm.f)) ;
        }
        vm.as_push(vm.as_get(0));
        CleanNode.fill(vm.as_get(2), vm.as_pop());
        vm.pop_a(1);
        return false;
    }

    public static final int ea3 = 58;

    public static boolean ea3(CleanVM vm) throws CleanException {
        CleanNode.eval(vm, vm.as_get(1));
        CleanNode.eval(vm, vm.as_get(0));
        CleanNode.push(vm, vm.as_get(1));
        CleanNode.push(vm, vm.as_get(0));
        vm.pop_a(2);
        return e_StdArrayExtensions_screateString(vm);
    }

    public static final int e_StdArrayExtensions_screateString = 59;

    public static boolean e_StdArrayExtensions_screateString(CleanVM vm) throws CleanException {
        return s3(vm);
    }

    public static final int s3 = 60;

    public static boolean s3(CleanVM vm) throws CleanException {
        vm.push_b(1);
        vm.push_b(1);
        vm.update_b(1, 3);
        vm.updatepop_b(0, 2);
        vm.as_push(CleanDescriptorChar.$$it.create_array(vm.bs_i_pop(), vm));
        return false;
    }

    public static final int e_StdArrayExtensions_lcreateLazyArr = 61;

    public static boolean e_StdArrayExtensions_lcreateLazyArr(CleanVM vm) throws CleanException {
        CleanNode.push(vm, vm.as_pop());
        if (ea2(vm)) {
            while (vm.m.$$dispatch(vm, vm.f)) ;
        }
        vm.as_push(vm.as_get(0));
        vm.updatepop_a(0, 1);
        return false;
    }

    public static final int e_StdArrayExtensions_ncreateLazyArr = 62;

    public static boolean e_StdArrayExtensions_ncreateLazyArr(CleanVM vm) throws CleanException {
        {
            Object o = vm.as_get(0);
            CleanNode.push(vm, o);
            CleanNode.fill(o, new CleanDescriptorFunction(CleanSystem.$$it, CleanSystem._cycle_in_spine, 0, "_cycle_in_spine"), null);
        }
        if (ea2(vm)) {
            while (vm.m.$$dispatch(vm, vm.f)) ;
        }
        vm.as_push(vm.as_get(0));
        CleanNode.fill(vm.as_get(2), vm.as_pop());
        vm.pop_a(1);
        return false;
    }

    public static final int ea2 = 63;

    public static boolean ea2(CleanVM vm) throws CleanException {
        CleanNode.eval(vm, vm.as_get(1));
        CleanNode.eval(vm, vm.as_get(0));
        CleanNode.push(vm, vm.as_get(0));
        vm.pop_a(1);
        return e_StdArrayExtensions_screateLazyArr(vm);
    }

    public static final int e_StdArrayExtensions_screateLazyArr = 64;

    public static boolean e_StdArrayExtensions_screateLazyArr(CleanVM vm) throws CleanException {
        return s2(vm);
    }

    public static final int s2 = 65;

    public static boolean s2(CleanVM vm) throws CleanException {
        vm.as_push(vm.as_get(0));
        vm.push_b(0);
        vm.updatepop_a(0, 1);
        vm.updatepop_b(0, 1);
        vm.as_push(CleanDescriptorNode.$$it.create_array(vm.bs_i_pop(), vm));
        return false;
    }

    public static final int e_StdArrayExtensions_lcreateStrictArr = 66;

    public static boolean e_StdArrayExtensions_lcreateStrictArr(CleanVM vm) throws CleanException {
        CleanNode.push(vm, vm.as_pop());
        if (ea1(vm)) {
            while (vm.m.$$dispatch(vm, vm.f)) ;
        }
        vm.as_push(vm.as_get(0));
        vm.updatepop_a(0, 1);
        return false;
    }

    public static final int e_StdArrayExtensions_ncreateStrictArr = 67;

    public static boolean e_StdArrayExtensions_ncreateStrictArr(CleanVM vm) throws CleanException {
        {
            Object o = vm.as_get(0);
            CleanNode.push(vm, o);
            CleanNode.fill(o, new CleanDescriptorFunction(CleanSystem.$$it, CleanSystem._cycle_in_spine, 0, "_cycle_in_spine"), null);
        }
        if (ea1(vm)) {
            while (vm.m.$$dispatch(vm, vm.f)) ;
        }
        vm.as_push(vm.as_get(0));
        CleanNode.fill(vm.as_get(2), vm.as_pop());
        vm.pop_a(1);
        return false;
    }

    public static final int ea1 = 68;

    public static boolean ea1(CleanVM vm) throws CleanException {
        CleanNode.eval(vm, vm.as_get(1));
        CleanNode.eval(vm, vm.as_get(0));
        CleanNode.push(vm, vm.as_get(0));
        vm.pop_a(1);
        return e_StdArrayExtensions_screateStrictArr(vm);
    }

    public static final int e_StdArrayExtensions_screateStrictArr = 69;

    public static boolean e_StdArrayExtensions_screateStrictArr(CleanVM vm) throws CleanException {
        return s1(vm);
    }

    public static final int s1 = 70;

    public static boolean s1(CleanVM vm) throws CleanException {
        vm.as_push(vm.as_get(0));
        vm.push_b(0);
        vm.updatepop_a(0, 1);
        vm.updatepop_b(0, 1);
        vm.as_push(CleanDescriptorNode.$$it.create_array(vm.bs_i_pop(), vm));
        return false;
    }

    public boolean $$dispatch(CleanVM vm, int func_id) throws CleanException {
        switch(func_id) {
            case e_StdArrayExtensions_cupdateArrElt$semicolon:
                return e_StdArrayExtensions_cupdateArrElt$semicolon(vm);
            case e_StdArrayExtensions_nupdateArrElt$semicolon$dotupdateArrElt:
                return e_StdArrayExtensions_nupdateArrElt$semicolon$dotupdateArrElt(vm);
            case e_StdArrayExtensions_eaupdateArrElt$semicolon$dotupdateArrElt:
                return e_StdArrayExtensions_eaupdateArrElt$semicolon$dotupdateArrElt(vm);
            case e_StdArrayExtensions_lupdateArrElt$semicolon$dotupdateArrElt:
                return e_StdArrayExtensions_lupdateArrElt$semicolon$dotupdateArrElt(vm);
            case e_StdArrayExtensions_caccArrElt$semicolon:
                return e_StdArrayExtensions_caccArrElt$semicolon(vm);
            case e_StdArrayExtensions_naccArrElt$semicolon$dotaccArrElt:
                return e_StdArrayExtensions_naccArrElt$semicolon$dotaccArrElt(vm);
            case e_StdArrayExtensions_eaaccArrElt$semicolon$dotaccArrElt:
                return e_StdArrayExtensions_eaaccArrElt$semicolon$dotaccArrElt(vm);
            case e_StdArrayExtensions_laccArrElt$semicolon$dotaccArrElt:
                return e_StdArrayExtensions_laccArrElt$semicolon$dotaccArrElt(vm);
            case e_StdArrayExtensions_laccArrElt$semicolon10:
                return e_StdArrayExtensions_laccArrElt$semicolon10(vm);
            case e_StdArrayExtensions_naccArrElt$semicolon10:
                return e_StdArrayExtensions_naccArrElt$semicolon10(vm);
            case ea11:
                return ea11(vm);
            case e_StdArrayExtensions_saccArrElt$semicolon10:
                return e_StdArrayExtensions_saccArrElt$semicolon10(vm);
            case s11:
                return s11(vm);
            case e_StdArrayExtensions_laccArrElt$semicolon9:
                return e_StdArrayExtensions_laccArrElt$semicolon9(vm);
            case e_StdArrayExtensions_naccArrElt$semicolon9:
                return e_StdArrayExtensions_naccArrElt$semicolon9(vm);
            case ea10:
                return ea10(vm);
            case e_StdArrayExtensions_saccArrElt$semicolon9:
                return e_StdArrayExtensions_saccArrElt$semicolon9(vm);
            case s10:
                return s10(vm);
            case e_StdArrayExtensions_lupdateArrElt$semicolon8:
                return e_StdArrayExtensions_lupdateArrElt$semicolon8(vm);
            case e_StdArrayExtensions_nupdateArrElt$semicolon8:
                return e_StdArrayExtensions_nupdateArrElt$semicolon8(vm);
            case ea9:
                return ea9(vm);
            case e_StdArrayExtensions_supdateArrElt$semicolon8:
                return e_StdArrayExtensions_supdateArrElt$semicolon8(vm);
            case s9:
                return s9(vm);
            case e_StdArrayExtensions_lupdateArrElt$semicolon7:
                return e_StdArrayExtensions_lupdateArrElt$semicolon7(vm);
            case e_StdArrayExtensions_nupdateArrElt$semicolon7:
                return e_StdArrayExtensions_nupdateArrElt$semicolon7(vm);
            case ea8:
                return ea8(vm);
            case e_StdArrayExtensions_supdateArrElt$semicolon7:
                return e_StdArrayExtensions_supdateArrElt$semicolon7(vm);
            case s8:
                return s8(vm);
            case s15:
                return s15(vm);
            case s14:
                return s14(vm);
            case s13:
                return s13(vm);
            case s12:
                return s12(vm);
            case e_StdArrayExtensions_lcreateStrictArrDecFoldSt:
                return e_StdArrayExtensions_lcreateStrictArrDecFoldSt(vm);
            case e_StdArrayExtensions_ncreateStrictArrDecFoldSt:
                return e_StdArrayExtensions_ncreateStrictArrDecFoldSt(vm);
            case ea7:
                return ea7(vm);
            case e_StdArrayExtensions_screateStrictArrDecFoldSt:
                return e_StdArrayExtensions_screateStrictArrDecFoldSt(vm);
            case s7:
                return s7(vm);
            case ok2:
                return ok2(vm);
            case e_StdArrayExtensions_lcreateStrictArrIncFoldSt:
                return e_StdArrayExtensions_lcreateStrictArrIncFoldSt(vm);
            case e_StdArrayExtensions_ncreateStrictArrIncFoldSt:
                return e_StdArrayExtensions_ncreateStrictArrIncFoldSt(vm);
            case ea6:
                return ea6(vm);
            case e_StdArrayExtensions_screateStrictArrIncFoldSt:
                return e_StdArrayExtensions_screateStrictArrIncFoldSt(vm);
            case s6:
                return s6(vm);
            case ok1:
                return ok1(vm);
            case createStrictArrIncFoldSt_loop:
                return createStrictArrIncFoldSt_loop(vm);
            case further:
                return further(vm);
            case e_StdArrayExtensions_lcreateUnboxedRealArr:
                return e_StdArrayExtensions_lcreateUnboxedRealArr(vm);
            case e_StdArrayExtensions_ncreateUnboxedRealArr:
                return e_StdArrayExtensions_ncreateUnboxedRealArr(vm);
            case ea5:
                return ea5(vm);
            case e_StdArrayExtensions_screateUnboxedRealArr:
                return e_StdArrayExtensions_screateUnboxedRealArr(vm);
            case s5:
                return s5(vm);
            case e_StdArrayExtensions_lcreateUnboxedIntArr:
                return e_StdArrayExtensions_lcreateUnboxedIntArr(vm);
            case e_StdArrayExtensions_ncreateUnboxedIntArr:
                return e_StdArrayExtensions_ncreateUnboxedIntArr(vm);
            case ea4:
                return ea4(vm);
            case e_StdArrayExtensions_screateUnboxedIntArr:
                return e_StdArrayExtensions_screateUnboxedIntArr(vm);
            case s4:
                return s4(vm);
            case e_StdArrayExtensions_lcreateString:
                return e_StdArrayExtensions_lcreateString(vm);
            case e_StdArrayExtensions_ncreateString:
                return e_StdArrayExtensions_ncreateString(vm);
            case ea3:
                return ea3(vm);
            case e_StdArrayExtensions_screateString:
                return e_StdArrayExtensions_screateString(vm);
            case s3:
                return s3(vm);
            case e_StdArrayExtensions_lcreateLazyArr:
                return e_StdArrayExtensions_lcreateLazyArr(vm);
            case e_StdArrayExtensions_ncreateLazyArr:
                return e_StdArrayExtensions_ncreateLazyArr(vm);
            case ea2:
                return ea2(vm);
            case e_StdArrayExtensions_screateLazyArr:
                return e_StdArrayExtensions_screateLazyArr(vm);
            case s2:
                return s2(vm);
            case e_StdArrayExtensions_lcreateStrictArr:
                return e_StdArrayExtensions_lcreateStrictArr(vm);
            case e_StdArrayExtensions_ncreateStrictArr:
                return e_StdArrayExtensions_ncreateStrictArr(vm);
            case ea1:
                return ea1(vm);
            case e_StdArrayExtensions_screateStrictArr:
                return e_StdArrayExtensions_screateStrictArr(vm);
            case s1:
                return s1(vm);
            default:
                throw new CleanException();
        }
    }

    public static final CleanDescriptorFunction e_StdArrayExtensions_tupdateArrElt$semicolon = new CleanDescriptorFunction(StdArrayExtensions.$$it, StdArrayExtensions.e_StdArrayExtensions_cupdateArrElt$semicolon, 1, "updateArrElt;");

    public static final CleanDescriptorFunction e_StdArrayExtensions_dupdateArrElt$semicolon$dotupdateArrElt = new CleanDescriptorFunction(StdArrayExtensions.$$it, StdArrayExtensions.e_StdArrayExtensions_nupdateArrElt$semicolon$dotupdateArrElt, StdArrayExtensions.$$it, StdArrayExtensions.e_StdArrayExtensions_lupdateArrElt$semicolon$dotupdateArrElt, 1, "updateArrElt;.updateArrElt");

    public static final CleanDescriptorFunction e_StdArrayExtensions_taccArrElt$semicolon = new CleanDescriptorFunction(StdArrayExtensions.$$it, StdArrayExtensions.e_StdArrayExtensions_caccArrElt$semicolon, 1, "accArrElt;");

    public static final CleanDescriptorFunction e_StdArrayExtensions_daccArrElt$semicolon$dotaccArrElt = new CleanDescriptorFunction(StdArrayExtensions.$$it, StdArrayExtensions.e_StdArrayExtensions_naccArrElt$semicolon$dotaccArrElt, StdArrayExtensions.$$it, StdArrayExtensions.e_StdArrayExtensions_laccArrElt$semicolon$dotaccArrElt, 1, "accArrElt;.accArrElt");

    public static final CleanDescriptorFunction e_StdArrayExtensions_daccArrElt$semicolon10 = new CleanDescriptorFunction(StdArrayExtensions.$$it, StdArrayExtensions.e_StdArrayExtensions_naccArrElt$semicolon10, StdArrayExtensions.$$it, StdArrayExtensions.e_StdArrayExtensions_laccArrElt$semicolon10, 3, "accArrElt;10");

    public static final CleanDescriptorFunction e_StdArrayExtensions_daccArrElt$semicolon9 = new CleanDescriptorFunction(StdArrayExtensions.$$it, StdArrayExtensions.e_StdArrayExtensions_naccArrElt$semicolon9, StdArrayExtensions.$$it, StdArrayExtensions.e_StdArrayExtensions_laccArrElt$semicolon9, 3, "accArrElt;9");

    public static final CleanDescriptorFunction e_StdArrayExtensions_dupdateArrElt$semicolon8 = new CleanDescriptorFunction(StdArrayExtensions.$$it, StdArrayExtensions.e_StdArrayExtensions_nupdateArrElt$semicolon8, StdArrayExtensions.$$it, StdArrayExtensions.e_StdArrayExtensions_lupdateArrElt$semicolon8, 3, "updateArrElt;8");

    public static final CleanDescriptorFunction e_StdArrayExtensions_dupdateArrElt$semicolon7 = new CleanDescriptorFunction(StdArrayExtensions.$$it, StdArrayExtensions.e_StdArrayExtensions_nupdateArrElt$semicolon7, StdArrayExtensions.$$it, StdArrayExtensions.e_StdArrayExtensions_lupdateArrElt$semicolon7, 3, "updateArrElt;7");

    public static final CleanDescriptorFunction e_StdArrayExtensions_dcreateStrictArrDecFoldSt = new CleanDescriptorFunction(StdArrayExtensions.$$it, StdArrayExtensions.e_StdArrayExtensions_ncreateStrictArrDecFoldSt, StdArrayExtensions.$$it, StdArrayExtensions.e_StdArrayExtensions_lcreateStrictArrDecFoldSt, 3, "createStrictArrDecFoldSt");

    public static final CleanDescriptorFunction e_StdArrayExtensions_dcreateStrictArrIncFoldSt = new CleanDescriptorFunction(StdArrayExtensions.$$it, StdArrayExtensions.e_StdArrayExtensions_ncreateStrictArrIncFoldSt, StdArrayExtensions.$$it, StdArrayExtensions.e_StdArrayExtensions_lcreateStrictArrIncFoldSt, 3, "createStrictArrIncFoldSt");

    public static final CleanDescriptorFunction e_StdArrayExtensions_dcreateUnboxedRealArr = new CleanDescriptorFunction(StdArrayExtensions.$$it, StdArrayExtensions.e_StdArrayExtensions_ncreateUnboxedRealArr, StdArrayExtensions.$$it, StdArrayExtensions.e_StdArrayExtensions_lcreateUnboxedRealArr, 2, "createUnboxedRealArr");

    public static final CleanDescriptorFunction e_StdArrayExtensions_dcreateUnboxedIntArr = new CleanDescriptorFunction(StdArrayExtensions.$$it, StdArrayExtensions.e_StdArrayExtensions_ncreateUnboxedIntArr, StdArrayExtensions.$$it, StdArrayExtensions.e_StdArrayExtensions_lcreateUnboxedIntArr, 2, "createUnboxedIntArr");

    public static final CleanDescriptorFunction e_StdArrayExtensions_dcreateString = new CleanDescriptorFunction(StdArrayExtensions.$$it, StdArrayExtensions.e_StdArrayExtensions_ncreateString, StdArrayExtensions.$$it, StdArrayExtensions.e_StdArrayExtensions_lcreateString, 2, "createString");

    public static final CleanDescriptorFunction e_StdArrayExtensions_dcreateLazyArr = new CleanDescriptorFunction(StdArrayExtensions.$$it, StdArrayExtensions.e_StdArrayExtensions_ncreateLazyArr, StdArrayExtensions.$$it, StdArrayExtensions.e_StdArrayExtensions_lcreateLazyArr, 2, "createLazyArr");

    public static final CleanDescriptorFunction e_StdArrayExtensions_dcreateStrictArr = new CleanDescriptorFunction(StdArrayExtensions.$$it, StdArrayExtensions.e_StdArrayExtensions_ncreateStrictArr, StdArrayExtensions.$$it, StdArrayExtensions.e_StdArrayExtensions_lcreateStrictArr, 2, "createStrictArr");

    public static final char[] m_StdArrayExtensions = "StdArrayExtensions".toCharArray();

    public static final e_StdArrayExtensions_rupdateArrElt$semicolon$$desc e_StdArrayExtensions_rupdateArrElt$semicolon = new e_StdArrayExtensions_rupdateArrElt$semicolon$$desc();

    public static final class e_StdArrayExtensions_rupdateArrElt$semicolon$$desc extends CleanDescriptorRecord {

        public Object build(CleanVM vm, int a, int b) throws CleanException {
            e_StdArrayExtensions_rupdateArrElt$semicolon$$data d = new e_StdArrayExtensions_rupdateArrElt$semicolon$$data();
            d.a0 = vm.as_get(0);
            return d;
        }

        public Object build(CleanVM vm, int a, int b, int as, int bs) throws CleanException {
            e_StdArrayExtensions_rupdateArrElt$semicolon$$data d = new e_StdArrayExtensions_rupdateArrElt$semicolon$$data();
            d.a0 = vm.as_get(as + 0);
            return d;
        }

        public Object buildpop(CleanVM vm, int a, int b) throws CleanException {
            e_StdArrayExtensions_rupdateArrElt$semicolon$$data d = new e_StdArrayExtensions_rupdateArrElt$semicolon$$data();
            d.a0 = vm.as_pop();
            return d;
        }

        public void push(CleanVM vm, Object data) throws CleanException {
            if (data instanceof e_StdArrayExtensions_rupdateArrElt$semicolon$$data) {
                e_StdArrayExtensions_rupdateArrElt$semicolon$$data d = (e_StdArrayExtensions_rupdateArrElt$semicolon$$data) data;
                vm.as_push(d.a0);
            } else {
                throw new CleanException("e_StdArrayExtensions_rupdateArrElt$semicolon$$data expected", data);
            }
        }

        public void push_a_idx(CleanVM vm, Object data, int idx) throws CleanException {
            if (data instanceof e_StdArrayExtensions_rupdateArrElt$semicolon$$data) {
                e_StdArrayExtensions_rupdateArrElt$semicolon$$data d = (e_StdArrayExtensions_rupdateArrElt$semicolon$$data) data;
                if (idx == 0) {
                    vm.as_push(d.a0);
                } else {
                    throw new CleanException("index or length does not match.");
                }
            } else {
                throw new CleanException("e_StdArrayExtensions_rupdateArrElt$semicolon$$data expected", data);
            }
        }

        public void push_b_idx(CleanVM vm, Object data, int idx, int len) throws CleanException {
            if (data instanceof e_StdArrayExtensions_rupdateArrElt$semicolon$$data) {
                e_StdArrayExtensions_rupdateArrElt$semicolon$$data d = (e_StdArrayExtensions_rupdateArrElt$semicolon$$data) data;
                {
                    throw new CleanException("index or length does not match.");
                }
            } else {
                throw new CleanException("e_StdArrayExtensions_rupdateArrElt$semicolon$$data expected", data);
            }
        }

        public boolean eq_arity(int a, Object data) throws CleanException {
            if (data instanceof e_StdArrayExtensions_rupdateArrElt$semicolon$$data) {
                return a == 1;
            } else {
                throw new CleanException("e_StdArrayExtensions_rupdateArrElt$semicolon$$data expected", data);
            }
        }

        public Object create_array(int size) throws CleanException {
            return new CleanNode(e_StdArrayExtensions_rupdateArrElt$semicolon, new e_StdArrayExtensions_rupdateArrElt$semicolon$$data[size]);
        }

        public void select(CleanVM vm, Object data, int idx) throws CleanException {
            if (data instanceof e_StdArrayExtensions_rupdateArrElt$semicolon$$data[]) {
                e_StdArrayExtensions_rupdateArrElt$semicolon$$data[] ds = (e_StdArrayExtensions_rupdateArrElt$semicolon$$data[]) data;
                vm.as_push(ds[idx].a0);
            } else {
                throw new CleanException("e_StdArrayExtensions_rupdateArrElt$semicolon$$data[] expected", data);
            }
        }

        public int arraysize(Object data) throws CleanException {
            if (data instanceof e_StdArrayExtensions_rupdateArrElt$semicolon$$data[]) {
                return ((e_StdArrayExtensions_rupdateArrElt$semicolon$$data[]) data).length;
            } else {
                throw new CleanException("e_StdArrayExtensions_rupdateArrElt$semicolon$$data[] expected", data);
            }
        }

        public void update(CleanVM vm, Object data, int idx) throws CleanException {
            if (data instanceof e_StdArrayExtensions_rupdateArrElt$semicolon$$data[]) {
                e_StdArrayExtensions_rupdateArrElt$semicolon$$data[] ds = (e_StdArrayExtensions_rupdateArrElt$semicolon$$data[]) data;
                ds[idx] = new e_StdArrayExtensions_rupdateArrElt$semicolon$$data();
                ds[idx].a0 = vm.as_pop();
            } else {
                throw new CleanException("e_StdArrayExtensions_rupdateArrElt$semicolon$$data[] expected", data);
            }
        }

        public void replace(CleanVM vm, Object o, Object data, int idx) throws CleanException {
            if (data instanceof e_StdArrayExtensions_rupdateArrElt$semicolon$$data[]) {
                e_StdArrayExtensions_rupdateArrElt$semicolon$$data[] ds = (e_StdArrayExtensions_rupdateArrElt$semicolon$$data[]) data;
                e_StdArrayExtensions_rupdateArrElt$semicolon$$data d = ds[idx];
                ds[idx] = new e_StdArrayExtensions_rupdateArrElt$semicolon$$data();
                ds[idx].a0 = vm.as_pop();
                vm.as_push(o);
                vm.as_push(d.a0);
            } else {
                throw new CleanException("e_StdArrayExtensions_rupdateArrElt$semicolon$$data[] expected", data);
            }
        }

        public void evalPrint(CleanVM vm, PrintStream out, Object data) throws CleanException {
            if (data instanceof e_StdArrayExtensions_rupdateArrElt$semicolon$$data) {
                e_StdArrayExtensions_rupdateArrElt$semicolon$$data d = (e_StdArrayExtensions_rupdateArrElt$semicolon$$data) data;
                out.print("(");
                d.evalPrint(vm, out);
                out.print(")");
            } else if (data instanceof e_StdArrayExtensions_rupdateArrElt$semicolon$$data[]) {
                e_StdArrayExtensions_rupdateArrElt$semicolon$$data[] ds = (e_StdArrayExtensions_rupdateArrElt$semicolon$$data[]) data;
                int l = ds.length;
                out.print("{");
                if (l > 0) {
                    ds[0].evalPrint(vm, out);
                    for (int j = 1; j < l; ++j) {
                        out.print(",");
                        ds[j].evalPrint(vm, out);
                    }
                }
                out.print("}");
            } else {
                throw new CleanException("e_StdArrayExtensions_rupdateArrElt$semicolon$$data expected", data);
            }
        }

        public void debugPrint(int depth, PrintStream out, Object data) throws CleanException {
            for (int i = 0; i < depth; ++i) out.print(" ");
            if (data instanceof e_StdArrayExtensions_rupdateArrElt$semicolon$$data) {
                out.println("updateArrElt;");
                e_StdArrayExtensions_rupdateArrElt$semicolon$$data d = (e_StdArrayExtensions_rupdateArrElt$semicolon$$data) data;
                d.debugPrint(depth + 1, out);
            } else if (data instanceof e_StdArrayExtensions_rupdateArrElt$semicolon$$data[]) {
                e_StdArrayExtensions_rupdateArrElt$semicolon$$data[] ds = (e_StdArrayExtensions_rupdateArrElt$semicolon$$data[]) data;
                int l = ds.length;
                out.println("updateArrElt;" + " ARRAY [" + l + "]");
                ++depth;
                for (int j = 0; j < l; ++j) {
                    for (int i = 0; i < depth; ++i) out.print(" ");
                    out.println("[" + j + "]");
                    ds[j].debugPrint(depth, out);
                }
            } else {
                out.println("updateArrElt;");
                CleanNode.debugPrint(depth + 1, out, data);
            }
        }
    }

    private static final class e_StdArrayExtensions_rupdateArrElt$semicolon$$data {

        Object a0;

        private void evalPrint(CleanVM vm, PrintStream out) throws CleanException {
            out.print("updateArrElt;");
            out.print(" ");
            CleanNode.evalPrint(vm, out, a0);
        }

        private void debugPrint(int depth, PrintStream out) throws CleanException {
            CleanNode.debugPrint(depth, out, a0);
        }
    }

    public static final e_StdArrayExtensions_raccArrElt$semicolon$$desc e_StdArrayExtensions_raccArrElt$semicolon = new e_StdArrayExtensions_raccArrElt$semicolon$$desc();

    public static final class e_StdArrayExtensions_raccArrElt$semicolon$$desc extends CleanDescriptorRecord {

        public Object build(CleanVM vm, int a, int b) throws CleanException {
            e_StdArrayExtensions_raccArrElt$semicolon$$data d = new e_StdArrayExtensions_raccArrElt$semicolon$$data();
            d.a0 = vm.as_get(0);
            return d;
        }

        public Object build(CleanVM vm, int a, int b, int as, int bs) throws CleanException {
            e_StdArrayExtensions_raccArrElt$semicolon$$data d = new e_StdArrayExtensions_raccArrElt$semicolon$$data();
            d.a0 = vm.as_get(as + 0);
            return d;
        }

        public Object buildpop(CleanVM vm, int a, int b) throws CleanException {
            e_StdArrayExtensions_raccArrElt$semicolon$$data d = new e_StdArrayExtensions_raccArrElt$semicolon$$data();
            d.a0 = vm.as_pop();
            return d;
        }

        public void push(CleanVM vm, Object data) throws CleanException {
            if (data instanceof e_StdArrayExtensions_raccArrElt$semicolon$$data) {
                e_StdArrayExtensions_raccArrElt$semicolon$$data d = (e_StdArrayExtensions_raccArrElt$semicolon$$data) data;
                vm.as_push(d.a0);
            } else {
                throw new CleanException("e_StdArrayExtensions_raccArrElt$semicolon$$data expected", data);
            }
        }

        public void push_a_idx(CleanVM vm, Object data, int idx) throws CleanException {
            if (data instanceof e_StdArrayExtensions_raccArrElt$semicolon$$data) {
                e_StdArrayExtensions_raccArrElt$semicolon$$data d = (e_StdArrayExtensions_raccArrElt$semicolon$$data) data;
                if (idx == 0) {
                    vm.as_push(d.a0);
                } else {
                    throw new CleanException("index or length does not match.");
                }
            } else {
                throw new CleanException("e_StdArrayExtensions_raccArrElt$semicolon$$data expected", data);
            }
        }

        public void push_b_idx(CleanVM vm, Object data, int idx, int len) throws CleanException {
            if (data instanceof e_StdArrayExtensions_raccArrElt$semicolon$$data) {
                e_StdArrayExtensions_raccArrElt$semicolon$$data d = (e_StdArrayExtensions_raccArrElt$semicolon$$data) data;
                {
                    throw new CleanException("index or length does not match.");
                }
            } else {
                throw new CleanException("e_StdArrayExtensions_raccArrElt$semicolon$$data expected", data);
            }
        }

        public boolean eq_arity(int a, Object data) throws CleanException {
            if (data instanceof e_StdArrayExtensions_raccArrElt$semicolon$$data) {
                return a == 1;
            } else {
                throw new CleanException("e_StdArrayExtensions_raccArrElt$semicolon$$data expected", data);
            }
        }

        public Object create_array(int size) throws CleanException {
            return new CleanNode(e_StdArrayExtensions_raccArrElt$semicolon, new e_StdArrayExtensions_raccArrElt$semicolon$$data[size]);
        }

        public void select(CleanVM vm, Object data, int idx) throws CleanException {
            if (data instanceof e_StdArrayExtensions_raccArrElt$semicolon$$data[]) {
                e_StdArrayExtensions_raccArrElt$semicolon$$data[] ds = (e_StdArrayExtensions_raccArrElt$semicolon$$data[]) data;
                vm.as_push(ds[idx].a0);
            } else {
                throw new CleanException("e_StdArrayExtensions_raccArrElt$semicolon$$data[] expected", data);
            }
        }

        public int arraysize(Object data) throws CleanException {
            if (data instanceof e_StdArrayExtensions_raccArrElt$semicolon$$data[]) {
                return ((e_StdArrayExtensions_raccArrElt$semicolon$$data[]) data).length;
            } else {
                throw new CleanException("e_StdArrayExtensions_raccArrElt$semicolon$$data[] expected", data);
            }
        }

        public void update(CleanVM vm, Object data, int idx) throws CleanException {
            if (data instanceof e_StdArrayExtensions_raccArrElt$semicolon$$data[]) {
                e_StdArrayExtensions_raccArrElt$semicolon$$data[] ds = (e_StdArrayExtensions_raccArrElt$semicolon$$data[]) data;
                ds[idx] = new e_StdArrayExtensions_raccArrElt$semicolon$$data();
                ds[idx].a0 = vm.as_pop();
            } else {
                throw new CleanException("e_StdArrayExtensions_raccArrElt$semicolon$$data[] expected", data);
            }
        }

        public void replace(CleanVM vm, Object o, Object data, int idx) throws CleanException {
            if (data instanceof e_StdArrayExtensions_raccArrElt$semicolon$$data[]) {
                e_StdArrayExtensions_raccArrElt$semicolon$$data[] ds = (e_StdArrayExtensions_raccArrElt$semicolon$$data[]) data;
                e_StdArrayExtensions_raccArrElt$semicolon$$data d = ds[idx];
                ds[idx] = new e_StdArrayExtensions_raccArrElt$semicolon$$data();
                ds[idx].a0 = vm.as_pop();
                vm.as_push(o);
                vm.as_push(d.a0);
            } else {
                throw new CleanException("e_StdArrayExtensions_raccArrElt$semicolon$$data[] expected", data);
            }
        }

        public void evalPrint(CleanVM vm, PrintStream out, Object data) throws CleanException {
            if (data instanceof e_StdArrayExtensions_raccArrElt$semicolon$$data) {
                e_StdArrayExtensions_raccArrElt$semicolon$$data d = (e_StdArrayExtensions_raccArrElt$semicolon$$data) data;
                out.print("(");
                d.evalPrint(vm, out);
                out.print(")");
            } else if (data instanceof e_StdArrayExtensions_raccArrElt$semicolon$$data[]) {
                e_StdArrayExtensions_raccArrElt$semicolon$$data[] ds = (e_StdArrayExtensions_raccArrElt$semicolon$$data[]) data;
                int l = ds.length;
                out.print("{");
                if (l > 0) {
                    ds[0].evalPrint(vm, out);
                    for (int j = 1; j < l; ++j) {
                        out.print(",");
                        ds[j].evalPrint(vm, out);
                    }
                }
                out.print("}");
            } else {
                throw new CleanException("e_StdArrayExtensions_raccArrElt$semicolon$$data expected", data);
            }
        }

        public void debugPrint(int depth, PrintStream out, Object data) throws CleanException {
            for (int i = 0; i < depth; ++i) out.print(" ");
            if (data instanceof e_StdArrayExtensions_raccArrElt$semicolon$$data) {
                out.println("accArrElt;");
                e_StdArrayExtensions_raccArrElt$semicolon$$data d = (e_StdArrayExtensions_raccArrElt$semicolon$$data) data;
                d.debugPrint(depth + 1, out);
            } else if (data instanceof e_StdArrayExtensions_raccArrElt$semicolon$$data[]) {
                e_StdArrayExtensions_raccArrElt$semicolon$$data[] ds = (e_StdArrayExtensions_raccArrElt$semicolon$$data[]) data;
                int l = ds.length;
                out.println("accArrElt;" + " ARRAY [" + l + "]");
                ++depth;
                for (int j = 0; j < l; ++j) {
                    for (int i = 0; i < depth; ++i) out.print(" ");
                    out.println("[" + j + "]");
                    ds[j].debugPrint(depth, out);
                }
            } else {
                out.println("accArrElt;");
                CleanNode.debugPrint(depth + 1, out, data);
            }
        }
    }

    private static final class e_StdArrayExtensions_raccArrElt$semicolon$$data {

        Object a0;

        private void evalPrint(CleanVM vm, PrintStream out) throws CleanException {
            out.print("accArrElt;");
            out.print(" ");
            CleanNode.evalPrint(vm, out, a0);
        }

        private void debugPrint(int depth, PrintStream out) throws CleanException {
            CleanNode.debugPrint(depth, out, a0);
        }
    }
}
