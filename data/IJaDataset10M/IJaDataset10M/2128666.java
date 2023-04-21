package org.jspar.place;

import org.jspar.model.Module;
import org.jspar.model.Net;
import org.jspar.model.Terminal;

public class CrossCoupledDetector {

    private int r1;

    private int r2;

    public boolean isCrossCoupled(Module o, Module m, int l) {
        for (Terminal iterm : o.terminals()) {
            if (iterm.isOut() && (r1 = onOutputNet(o, iterm.net(), l, 1)) != 0) {
                if (m.hasFlag(Module.PLACED)) return false;
                for (Terminal jterm : m.terminals()) {
                    if (jterm.isOut() && (r2 = onOutputNet(o, jterm.net(), l, 1)) != 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private int onOutputNet(Module m, Net n, int l, int s) {
        for (Terminal iterm : n.terminals()) {
            if (!iterm.isIn()) continue;
            if (iterm.module() == m) return s;
            if (l > 1) {
                if (iterm.module().hasFlag(Module.PLACED)) return 0;
                for (Terminal jterm : iterm.module().terminals()) {
                    if (jterm.isOut() && (s = onOutputNet(m, jterm.net(), l - 1, s + 1)) != 0) return s + 1;
                }
            }
        }
        return 0;
    }
}
