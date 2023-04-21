package eu.cherrytree.paj.base;

import eu.cherrytree.paj.base.KeyboardInput.Key;
import eu.cherrytree.paj.base.MouseInput.MouseButton;
import eu.cherrytree.paj.graphics.Graphics;
import eu.cherrytree.paj.graphics.ImageManager;
import eu.cherrytree.paj.graphics.RenderManager;
import eu.cherrytree.paj.graphics.RenderManager.RenderManagerNotInitiatedException;
import eu.cherrytree.paj.gui.Sprite2d;
import eu.cherrytree.paj.gui.UITextList.UITextListElement;
import eu.cherrytree.paj.math.Vector2d;
import eu.cherrytree.paj.utilities.Timer;

public abstract class SplashState extends IntroState {

    private Sprite2d splash;

    private float duration;

    private RenderManager renderManager;

    public SplashState(String image, float duration, boolean fullscreen, KeyboardInput.Key skipButton, float R, float G, float B) throws Throwable {
        super(skipButton, R, G, B);
        renderManager = new RenderManager();
        if (fullscreen) {
            Vector2d orig_size = ImageManager.getStaticImageSize(image);
            Vector2d full_size = Graphics.calculateFullscreenDimensions(ImageManager.getStaticImageSize(image));
            float scaleX = full_size.getX() / orig_size.getX();
            float scaleY = full_size.getY() / orig_size.getY();
            splash = new Sprite2d(image, scaleX, scaleY);
        } else splash = new Sprite2d(image);
        splash.setAllign(Sprite2d.VAllign.ALLIGN_VCENTER, Sprite2d.HAllign.ALLIGN_HCENTER);
        this.duration = Timer.toTargetFrames(duration);
    }

    @Override
    public void destroy() {
        super.destroy();
        renderManager.destroy();
        ImageManager.freeAll();
    }

    @Override
    protected void start() {
    }

    @Override
    protected void end() {
        onFinish();
    }

    @Override
    protected void update(float frame) {
        renderManager.renderDirect(true);
        uiManager.beginUI();
        splash.draw(Graphics.getWidth() / 2.0f, Graphics.getHeight() / 2.0f);
        uiManager.endUI();
        duration -= frame;
        if (duration <= 0.0f) end();
    }

    @Override
    public void onJoypadButtonPress(int joypad_id, int button_id) {
    }

    @Override
    public void onJoypadButtonRelease(int joypad_id, int button_id) {
    }

    @Override
    public void onJoypadDPad(int joypad_id, JoypadInput.DPad dpad) {
    }

    @Override
    public void onJoypadAxisChange(int joypad_id, int axis_id, float value) {
    }

    @Override
    public void onButtonPress(int button_id) {
    }

    @Override
    public void onButtonRelease(int button_id) {
    }

    @Override
    public void onTextListPress(int list_id, UITextListElement element) {
    }

    @Override
    public void onTextListRelease(int list_id, UITextListElement element) {
    }

    @Override
    public void onTextInputAccept(int input_id, String text) {
    }

    @Override
    protected void onKeyUp(Key key) {
    }

    @Override
    protected void onMouseMove(int x, int y) {
    }

    @Override
    protected void onMouseDown(MouseButton button, int x, int y) {
    }

    @Override
    protected void onMouseUp(MouseButton button, int x, int y) {
    }
}
