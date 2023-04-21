package com.gorillalogic.dal.common;

import com.gorillalogic.dal.*;
import com.gorillalogic.test.*;

public class ProjectionStrategyFactory {

    private static final int DIMENSIONS = 6;

    private static ProjectionStrategyImpl ps[][][][][][] = new ProjectionStrategyImpl[2][2][2][2][2][2];

    static void init(int[] dim, int pos, Object[] psVector) {
        if (pos >= dim.length) {
            ps[dim[0]][dim[1]][dim[2]][dim[3]][dim[4]][dim[5]] = new ProjectionStrategyImpl(dim);
        } else {
            for (int i = 0; i < psVector.length; i++) {
                dim[pos] = i;
                Object[] psNext = (Object[]) psVector[i];
                init(dim, pos + 1, psNext);
            }
        }
    }

    static {
        int[] dim = new int[DIMENSIONS];
        init(dim, 0, ps);
    }

    public static final ProjectionStrategy none = ps[0][0][0][0][0][0];

    public static final ProjectionStrategy all = ps[1][1][1][1][1][1];

    private static class ProjectionStrategyImpl implements ProjectionStrategy, ObjectToCellAdaptor {

        private boolean keys;

        private boolean paths;

        private boolean leading;

        private boolean attrs;

        private boolean roles;

        private boolean keyedLink;

        ProjectionStrategyImpl(int[] v) {
            keys = xb(v[0]);
            paths = xb(v[1]);
            leading = xb(v[2]);
            attrs = xb(v[3]);
            roles = xb(v[4]);
            keyedLink = xb(v[5]);
        }

        public String describe() {
            return "(" + (keys ? "keys" : "no_keys") + ',' + (paths ? "paths" : "no_paths") + ',' + (leading ? "leading" : "no_leading") + ',' + (attrs ? "attrs" : "no_attrs") + ',' + (roles ? "roles" : "no_roles") + ',' + (keyedLink ? "keyedLink" : "no_keyedLink") + ')';
        }

        public String toString() {
            return super.toString() + " " + describe();
        }

        public String getString() {
            if (this == all) return "all";
            if (this == none) return "none";
            if (this == defaultStrategy) return "default";
            return "custom";
        }

        private boolean xb(int x) {
            return x == 0 ? false : true;
        }

        private int bx(boolean b) {
            return b ? 1 : 0;
        }

        public ProjectionStrategy keys(boolean which) {
            return ps[bx(which)][bx(paths)][bx(leading)][bx(attrs)][bx(roles)][bx(keyedLink)];
        }

        public boolean keys() {
            return keys;
        }

        public ProjectionStrategy paths(boolean which) {
            return ps[bx(keys)][bx(which)][bx(leading)][bx(attrs)][bx(roles)][bx(keyedLink)];
        }

        public boolean paths() {
            return paths;
        }

        public ProjectionStrategy leading(boolean which) {
            return ps[bx(keys)][bx(paths)][bx(which)][bx(attrs)][bx(roles)][bx(keyedLink)];
        }

        public boolean leading() {
            return leading;
        }

        public ProjectionStrategy attrs(boolean which) {
            return ps[bx(keys)][bx(paths)][bx(leading)][bx(which)][bx(roles)][bx(keyedLink)];
        }

        public boolean attrs() {
            return attrs;
        }

        public ProjectionStrategy roles(boolean which) {
            return ps[bx(keys)][bx(paths)][bx(leading)][bx(attrs)][bx(which)][bx(keyedLink)];
        }

        public boolean roles() {
            return roles;
        }

        public ProjectionStrategy keyedLink(boolean which) {
            return ps[bx(keys)][bx(paths)][bx(leading)][bx(attrs)][bx(roles)][bx(which)];
        }

        public boolean keyedLink() {
            return keyedLink;
        }
    }

    private static void test(Tester tester, int number, ProjectionStrategy strategy, boolean keys, boolean paths, boolean leading, boolean attrs, boolean roles, boolean keyedLink) {
        Log.trc("strategy-" + number, strategy.describe());
        tester.testEq("keys", keys, strategy.keys());
        tester.testEq("paths", paths, strategy.paths());
        tester.testEq("leading", leading, strategy.leading());
        tester.testEq("attrs", attrs, strategy.attrs());
        tester.testEq("roles", roles, strategy.roles());
        tester.testEq("keyedLink", keyedLink, strategy.keyedLink());
    }

    public static void testProjectionStrategy(Tester tester) {
        ProjectionStrategy x = ProjectionStrategy.all;
        int tc = 0;
        test(tester, tc++, x, true, true, true, true, true, true);
        x = x.keys(false);
        test(tester, tc++, x, false, true, true, true, true, true);
        x = x.paths(false);
        test(tester, tc++, x, false, false, true, true, true, true);
        x = x.leading(false);
        test(tester, tc++, x, false, false, false, true, true, true);
        x = x.attrs(false);
        test(tester, tc++, x, false, false, false, false, true, true);
        x = x.roles(false);
        test(tester, tc++, x, false, false, false, false, false, true);
        x = x.keyedLink(false);
        test(tester, tc++, x, false, false, false, false, false, false);
        tester.testEq("to none?", x, none);
        x = x.leading(true);
        test(tester, tc++, x, false, false, true, false, false, false);
        x = x.leading(false);
        test(tester, tc++, x, false, false, false, false, false, false);
        tester.testEq("to none?", x, none);
        x = x.keys(true);
        test(tester, tc++, x, true, false, false, false, false, false);
        x = x.paths(true);
        test(tester, tc++, x, true, true, false, false, false, false);
        x = x.leading(true);
        test(tester, tc++, x, true, true, true, false, false, false);
        x = x.attrs(true);
        test(tester, tc++, x, true, true, true, true, false, false);
        x = x.roles(true);
        test(tester, tc++, x, true, true, true, true, true, false);
        x = x.keyedLink(true);
        test(tester, tc++, x, true, true, true, true, true, true);
        tester.testEq("Back to starting point?", x, all);
    }

    public static void main(String[] args) {
        Tester.run(ProjectionStrategyFactory.class);
    }
}
