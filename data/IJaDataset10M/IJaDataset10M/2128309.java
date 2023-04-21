package ponkOut.Menu;

import ponkOut.Settings;

public class CheckboxMediator {

    private Settings settings;

    private Settings.BooleanValue value;

    public CheckboxMediator(Settings.BooleanValue value) {
        settings = Settings.getInstance();
        this.value = value;
    }

    public boolean getValue() {
        switch(value) {
            case VSYNC:
                return settings.getVSync();
            case FULLSCREEN:
                return settings.getFullscreen();
            case ANISOTROPIC:
                return settings.getAnisotropic();
            default:
                throw new IllegalStateException("The attribute value has an invalid value");
        }
    }

    public boolean getDefaultValue() {
        switch(value) {
            case VSYNC:
                return settings.getDefaultVSync();
            case FULLSCREEN:
                return settings.getDefaultFullscreen();
            case ANISOTROPIC:
                return settings.getDefaultAnisotropic();
            default:
                throw new IllegalStateException("The attribute value has an invalid value");
        }
    }

    public void setValue(boolean v) {
        switch(value) {
            case VSYNC:
                settings.setVSync(v);
                break;
            case FULLSCREEN:
                settings.setFullscreen(v);
                break;
            case ANISOTROPIC:
                settings.setAnisotropic(v);
                break;
            default:
                throw new IllegalStateException("The attribute value has an invalid value");
        }
    }
}
