package render;

/**
 *
 * @author  EvilSloot
 */
class NullRenderer implements Renderer {

    private NullRenderer() {
    }

    public void render(Renderable r) {
        System.err.print("not rendering" + r);
    }

    public String type() {
        return "NOTHING";
    }

    public static Renderer NULL_RENDERER = new NullRenderer();
}
