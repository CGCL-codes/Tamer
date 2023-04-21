package ucm.si.animadorGUI;

import ucm.si.util.Contexto;

public class PanelJPane<S> extends PanelInterface<S> {

    public PanelJPane(Contexto cnt) {
        super.contexto = cnt;
    }

    @Override
    public void pintaEstado(S p) {
        this.drawer.pintaEstado(p, this);
        this.repaint();
    }

    @Override
    public void rePinta(S p) {
        this.drawer.rePinta(p, this);
        this.repaint();
    }

    @Override
    public void setDrawer(Drawer<S> dw) {
        super.drawer = dw;
        super.drawer.setContexto(contexto);
    }

    @Override
    public void setContexto(Contexto cntxt) {
        super.contexto = cntxt;
    }
}
