package Code.Objects.Characters.Player.GUI;

import org.jrabbit.base.core.types.Updateable;
import org.jrabbit.standard.game.objects.Sprite;
import Code.Basic.Entity.GenericCharacter;
import Code.Objects.Characters.Player.Misc.PlayerSounds;

public class HealthGUI extends Sprite implements Updateable {

    private Sprite guiBack;

    private HealthBar healthBar;

    private HealthGUIPulse pulse;

    private GenericCharacter character;

    public HealthGUI(GenericCharacter c) {
        super("Resources/Images/GUI/Game/Health/Status Bar Frame.png");
        location.set(getScaledWidth() / 2, getScaledHeight() / 2);
        useScreenCoords(true);
        guiBack = new Sprite("Resources/Images/GUI/Game/Health/Status Bar Back.png");
        guiBack.useScreenCoords(true);
        guiBack.location().set(guiBack.getScaledWidth() / 2, guiBack.getScaledHeight() / 2);
        pulse = new HealthGUIPulse();
        pulse.location().set(23, 24);
        healthBar = new HealthBar();
        healthBar.adjustForPercentage(1f);
        healthBar.location().setY(17);
        character = c;
    }

    public void render(GenericCharacter character) {
        if (character != null) {
            guiBack.render();
            healthBar.adjustForPercentage(character.getHealthPercentage());
            healthBar.render();
            super.render();
            pulse.render();
        }
    }

    public void update(int time) {
        if (character != null) {
            pulse.update(time, character.getHealthPercentage());
        }
    }

    public class HealthGUIPulse extends Sprite {

        private double maxPulseInterval;

        private double minPulseInterval;

        private double pulsePosition;

        private float maxTransparencyA;

        private float maxTransparencyB;

        private float minTransparencyA;

        private float minTransparencyB;

        private double maxScaleA;

        private double maxScaleB;

        private double minScaleA;

        private double minScaleB;

        private boolean phase;

        public HealthGUIPulse() {
            super("Resources/Images/GUI/Game/Health/Pulse Base.png");
            useScreenCoords(true);
            color.set(0.2f, 1f, 0);
            maxPulseInterval = 20000f;
            minPulseInterval = 700f;
            maxTransparencyA = 1f;
            minTransparencyA = 0.7f;
            maxTransparencyB = 0.7f;
            minTransparencyB = 0.3f;
            maxScaleA = 1.5f;
            minScaleA = 1.2f;
            maxScaleB = 3f;
            minScaleB = 1.4f;
            pulsePosition = 0;
            phase = true;
        }

        public void update(int delta, float healthPercent) {
            double pulseInterval = minPulseInterval + (healthPercent * (maxPulseInterval - minPulseInterval));
            if (phase) {
                pulsePosition += delta;
                if (pulsePosition > pulseInterval) {
                    phase = false;
                    pulsePosition = pulseInterval;
                    if (healthPercent < 0.25f) {
                        float volume = 1f - (healthPercent / 0.25f);
                        PlayerSounds.playWarningTone(volume * volume * 0.6f);
                    }
                }
            } else {
                pulsePosition -= (float) delta;
                if (pulsePosition < 0) {
                    phase = true;
                    pulsePosition = 0;
                }
            }
            adjustForPulse(healthPercent, pulsePosition / pulseInterval);
            float hp = (float) Math.sqrt(healthPercent);
            color.set(0.4f + (1f - hp) * 0.6f, hp * 0.9f, 0);
        }

        private void adjustForPulse(double percentHealth, double percentPulse) {
            double maxScale = maxScaleB + (percentHealth * (maxScaleA - maxScaleB));
            double minScale = minScaleB + (percentHealth * (minScaleA - minScaleB));
            double scale = minScale + (percentPulse * (maxScale - minScale));
            scalar.set((float) scale);
            if (scalar.getScale() > maxScale) {
                scalar.set((float) maxScale);
            }
            float maxTrans = (float) (maxTransparencyB + (percentHealth * (maxTransparencyA - maxTransparencyB)));
            float minTrans = (float) (minTransparencyB + (percentHealth * (minTransparencyA - minTransparencyB)));
            float trans = (float) (minTrans + (percentPulse * (maxTrans - minTrans)));
            color.setAlpha(trans);
        }
    }

    public class HealthBar extends Sprite {

        private double leftX;

        private float maxScale;

        public HealthBar() {
            super("Resources/Images/GUI/Game/Health/Health Bar Base.png");
            useScreenCoords(true);
            leftX = 45;
            maxScale = 6.4f;
        }

        public void adjustForPercentage(float healthPercent) {
            scalar.setScaleX(maxScale * healthPercent);
            location.setX(leftX + getScaledWidth() / 2);
            float hp = (float) Math.sqrt(healthPercent);
            color.set(0.4f + (1f - hp) * 0.6f, hp * 0.9f, 0);
        }
    }
}
