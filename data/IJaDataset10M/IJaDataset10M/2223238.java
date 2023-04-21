package demos;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import AGFX.F3D.F3D;
import AGFX.F3D.AppWrapper.TF3D_AppWrapper;
import AGFX.F3D.Callback.TF3D_HudCallback;
import AGFX.F3D.Camera.TF3D_Camera;
import AGFX.F3D.Config.TF3D_Config;
import AGFX.F3D.Hud.TF3D_HUD_Image;
import AGFX.F3D.Hud.TF3D_HUD_ImageButton;
import AGFX.F3D.Light.TF3D_Light;
import AGFX.F3D.Mesh.TF3D_Mesh;

/**
 * @author AndyGFX
 * 
 */
public class Demo_HUDImage extends TF3D_AppWrapper {

    public TF3D_Camera Camera;

    public TF3D_Mesh mesh;

    public TF3D_HUD_Image HUD_img1;

    public TF3D_HUD_Image HUD_img2;

    public TF3D_HUD_Image HUD_img3;

    public TF3D_HUD_Image HUD_img4;

    public TF3D_HUD_Image HUD_img5;

    public TF3D_HUD_Image HUD_img6;

    public TF3D_HUD_Image HUD_img7;

    public TF3D_HUD_ImageButton HUD_button0;

    public TF3D_HUD_ImageButton HUD_button1;

    public Demo_HUDImage() {
    }

    @Override
    public void onConfigure() {
        try {
            F3D.Config = new TF3D_Config();
            F3D.Config.r_display_width = 1024;
            F3D.Config.r_display_height = 768;
            F3D.Config.r_fullscreen = false;
            F3D.Config.r_display_vsync = true;
            F3D.Config.r_display_title = "jFinal3D Graphics Engine 2010 - " + this.getClass().getName();
            super.onConfigure();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onInitialize() {
        F3D.Worlds.CreateWorld("MAIN_WORLD");
        this.Camera = new TF3D_Camera("FPSCamera");
        this.Camera.SetPosition(0.0f, 0.0f, -10.0f);
        this.Camera.SetRotation(0, 180, 0);
        this.Camera.ctype = F3D.CAMERA_TYPE_FPS;
        F3D.Cameras.Add(this.Camera);
        F3D.Worlds.SetCamera(this.Camera);
        this.mesh = new TF3D_Mesh();
        this.mesh.Load("abstract::Cube.a3da");
        TF3D_Light light = new TF3D_Light("light_0", 0);
        light.SetPosition(3, 3, 3);
        light.Enable();
        F3D.Viewport.DoubleFace(true);
        this.HUD_img1 = new TF3D_HUD_Image();
        this.HUD_img1.texture_id0 = F3D.Textures.FindByName("jf3d_logo");
        this.HUD_img1.size.set(128, 128);
        this.HUD_img1.property.Autosize = true;
        this.HUD_img1.property.Texture = true;
        this.HUD_img1.property.Blend = true;
        this.HUD_img1.color.set(1.0f, 1.0f, 1.0f, 1.0f);
        this.HUD_img1.transform.scroll.x = 0.0f;
        this.HUD_img1.transform.scroll.y = 0.0f;
        this.HUD_img1.transform.rotate = 0f;
        this.HUD_img1.origin.set(0, 0);
        this.HUD_img1.scale.set(1.0f, 1.0f);
        this.HUD_img1.shape_angle = 0.0f;
        this.HUD_img1.shape_origin.set(0, 0);
        this.HUD_img1.Initialize();
        this.HUD_img2 = new TF3D_HUD_Image();
        this.HUD_img2.Load("abstract::hud_image_normal.hud", true);
        this.HUD_img3 = new TF3D_HUD_Image();
        this.HUD_img3.Load("abstract::hud_image_scroll.hud", true);
        this.HUD_img4 = new TF3D_HUD_Image();
        this.HUD_img4.Load("abstract::hud_image_rotate.hud", true);
        this.HUD_img5 = new TF3D_HUD_Image();
        this.HUD_img5.Load("abstract::hud_image_rotcenter.hud", true);
        this.HUD_img6 = new TF3D_HUD_Image();
        this.HUD_img6.Load("abstract::hud_image_scale.hud", true);
        this.HUD_img7 = new TF3D_HUD_Image();
        this.HUD_img7.Load("abstract::hud_image_shapeangle.hud", true);
        this.HUD_button0 = new TF3D_HUD_ImageButton();
        this.HUD_button0.Load("abstract::hud_image_button.hud", true);
        this.HUD_button0.OnPress = new TF3D_HudCallback() {

            @Override
            public void Call() {
                F3D.Log.info("BUTTON-1", "Pressed");
            }
        };
        this.HUD_button1 = new TF3D_HUD_ImageButton();
        this.HUD_button1.Load("abstract::hud_image_button.hud", true);
        this.HUD_button1.OnPress = new TF3D_HudCallback() {

            @Override
            public void Call() {
                F3D.Log.info("BUTTON-2", "Pressed");
            }
        };
    }

    @Override
    public void onUpdate3D() {
        F3D.Draw.Axis(2.0f);
        this.mesh.Render();
        if (Mouse.isInsideWindow()) {
            if (Mouse.isButtonDown(0)) {
                float dx = (float) Mouse.getDX() / 10.0f;
                float dy = (float) Mouse.getDY() / 10.0f;
                this.Camera.Turn(-dy, dx, 0.0f);
                if (Mouse.getX() < 3) {
                    Mouse.setCursorPosition(F3D.Config.r_display_width - 4, Mouse.getY());
                }
                if (Mouse.getX() > F3D.Config.r_display_width - 3) {
                    Mouse.setCursorPosition(4, Mouse.getY());
                }
                if (Mouse.getY() < 3) {
                    Mouse.setCursorPosition(Mouse.getX(), F3D.Config.r_display_height - 4);
                }
                if (Mouse.getY() > F3D.Config.r_display_height - 3) {
                    Mouse.setCursorPosition(Mouse.getX(), 4);
                }
            }
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            this.Camera.Move(0.0f, 0.0f, -0.1f);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            this.Camera.Move(-0.1f, 0.0f, 0.0f);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            this.Camera.Move(0.0f, 0.0f, 0.1f);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            this.Camera.Move(0.1f, 0.0f, 0.0f);
        }
    }

    @Override
    public void onUpdate2D() {
        F3D.Viewport.DrawInfo(0, 0);
        this.HUD_img1.DrawAt(10, 10 + 150 * 0);
        this.HUD_img2.DrawAt(10, 10 + 150 * 1);
        this.HUD_img3.DrawAt(10, 10 + 150 * 2);
        this.HUD_img4.DrawAt(10, 10 + 150 * 3);
        this.HUD_img5.DrawAt(200, 10 + 150 * 1);
        this.HUD_img6.DrawAt(200, 10 + 150 * 2);
        this.HUD_img7.DrawAt(232, 42 + 150 * 3);
        this.HUD_button0.DrawAt(400, 10 + 150 * 1);
        this.HUD_button1.DrawAt(400, 74 + 150 * 1);
    }

    @Override
    public void OnDestroy() {
    }

    public static void main(String[] args) {
        new Demo_HUDImage().Execute();
        System.exit(0);
    }
}
