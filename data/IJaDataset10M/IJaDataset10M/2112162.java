package espresso3d.engine;

public class E3DDebugFlags {

    private boolean debugRenderPortals = false;

    private boolean debugNormalsRendered = false;

    public boolean isDebugRenderPortals() {
        return debugRenderPortals;
    }

    public void setDebugRenderPortals(boolean debugRenderPortals) {
        this.debugRenderPortals = debugRenderPortals;
    }

    public boolean isDebugNormalsRendered() {
        return debugNormalsRendered;
    }

    public void setDebugNormalsRendered(boolean debugNormalsRendered) {
        this.debugNormalsRendered = debugNormalsRendered;
    }
}
