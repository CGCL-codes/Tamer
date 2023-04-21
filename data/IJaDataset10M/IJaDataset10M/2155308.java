package org.ztemplates.test.render.script.basic;

import org.ztemplates.render.ZExpose;
import org.ztemplates.render.ZJavaScript;
import org.ztemplates.render.ZRenderer;
import org.ztemplates.render.ZScript;
import org.ztemplates.render.velocity.ZVelocityRenderer;

@ZRenderer(value = ZVelocityRenderer.class, zscript = true)
@ZScript(javaScript = @ZJavaScript("root.js"))
public final class RootExposeRenderFalse {

    private Nested nested = new Nested();

    @ZExpose
    public Nested getNested() {
        return nested;
    }
}
