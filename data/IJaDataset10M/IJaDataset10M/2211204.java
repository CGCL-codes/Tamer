package AGFX.F3D.Hud;

import javax.vecmath.Vector2f;
import javax.vecmath.Vector4f;

public class TF3D_HUD_Gadget {

    /**  rendering properties*/
    public TF3D_HUD_GadgetProperties property;

    /**  texture transformation */
    public TF3D_HUD_GadgetTransform transform;

    /**  hud Gadget type */
    public int hudtype;

    /**  hud Gadget name */
    public String name;

    /**  hud Gadget size */
    public Vector2f size;

    /**  hud Gadget texture scale  */
    public Vector2f scale;

    /**  hud Gadget texture offset  */
    public Vector2f offset;

    /**  hud Gadget texture origin/ rotation center  */
    public Vector2f origin;

    /**  hud Gadget texture angle  */
    public float angle;

    /**  hud Gadget shape angle */
    public float shape_angle;

    /**  hud Gadget shape origin/center  */
    public Vector2f shape_origin;

    /**  hud Gadget color  */
    public Vector4f color;

    /**  hud Gadget visible [true/false]  */
    public boolean visible;

    public TF3D_HUD_Gadget() {
        this.angle = 0.0f;
        this.shape_angle = 0.0f;
        this.color = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.name = "noname";
        this.offset = new Vector2f();
        this.offset.set(0.0f, 0.0f);
        this.origin = new Vector2f(0.0f, 0.0f);
        this.shape_origin = new Vector2f(0.0f, 0.0f);
        this.scale = new Vector2f(1.0f, 1.0f);
        this.size = new Vector2f(16.0f, 16.0f);
        this.visible = true;
        this.transform = new TF3D_HUD_GadgetTransform();
        this.property = new TF3D_HUD_GadgetProperties();
    }
}
