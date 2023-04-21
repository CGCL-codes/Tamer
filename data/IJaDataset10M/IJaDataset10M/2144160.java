package org.qtitools.playr.tce;

/**
 * @author Rob Blowers
 * @author Jonathon Hare
 *
 */
public class R2Q2RenderOpts {

    private int renderOpts;

    public static int RENDERWRAPPER = (1 << 0);

    public static int RENDERTITLE = (1 << 1);

    public static int RENDERBODY = (1 << 2);

    public static int RENDERBUTTONS = (1 << 3);

    public static int RENDERFEEDBACK = (1 << 4);

    public static int RENDERFORM = (1 << 5);

    public static int RENDERINCLUDES = (1 << 6);

    public R2Q2RenderOpts() {
        renderOpts = RENDERWRAPPER | RENDERTITLE | RENDERBODY | RENDERBUTTONS | RENDERFEEDBACK | RENDERFORM | RENDERINCLUDES;
    }

    public R2Q2RenderOpts(int renderOpts) {
        this.renderOpts = renderOpts;
    }

    public boolean renderTitle() {
        return testOpt(RENDERTITLE);
    }

    public boolean renderBody() {
        return testOpt(RENDERBODY);
    }

    public boolean renderButtons() {
        return testOpt(RENDERBUTTONS);
    }

    public boolean renderFeedback() {
        return testOpt(RENDERFEEDBACK);
    }

    public boolean renderWrapper() {
        return testOpt(RENDERWRAPPER);
    }

    public boolean renderForm() {
        return testOpt(RENDERFORM);
    }

    public boolean renderIncludes() {
        return testOpt(RENDERINCLUDES);
    }

    private boolean testOpt(int toTest) {
        if ((renderOpts & toTest) == toTest) {
            return true;
        } else {
            return false;
        }
    }
}
