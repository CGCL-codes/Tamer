package org.gwanted.gwt.core.client.behaviours;

import org.gwanted.gwt.core.client.behaviours.impl.FadeImpl;
import org.gwanted.gwt.core.client.data.Parameters;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.UIObject;

/**
 * Fade (in/out) from an initial opacity to a final opacity in a given time.
 *
 * @author Joshua Hewitt aka Sposh
 */
public final class Fade implements Command {

    public static final int OPACITY = 100;

    /**
     * Parameters for Fade command.
     *
     * @author Joshua Hewitt aka Sposh
     */
    public static final class FadeParameters implements Parameters {

        private int fadeTime = 0;

        private int initialOpacity = 0;

        private int finalOpacity = Fade.OPACITY;

        private String displayStyle = "inline";

        /**
         *
         */
        public FadeParameters() {
        }

        /**
         * @param fadeOut True for hide / fade out, false for show / fade in
         */
        public FadeParameters(final boolean fadeOut) {
            if (fadeOut) {
                this.initialOpacity = Fade.OPACITY;
                this.finalOpacity = 0;
            }
        }

        /**
         * @param fadeTime Fade time (ms); if 0 this is a straight show/hide
         * @param initialOpacity Initial opacity (0-100)
         * @param finalOpacity Final opacity (0-100)
         * @param displayStyle Display style to set on show
         */
        public FadeParameters(final int fadeTime, final int initialOpacity, final int finalOpacity, final String displayStyle) {
            this.fadeTime = fadeTime;
            this.initialOpacity = initialOpacity;
            this.finalOpacity = finalOpacity;
            this.displayStyle = displayStyle;
        }

        /**
         * @return Fade time (ms); if 0 this is a straight show/hide
         */
        public int getFadeTime() {
            return this.fadeTime;
        }

        /**
         * @param fadeTime Fade time (ms); if 0 this is a straight show/hide
         */
        public void setFadeTime(final int fadeTime) {
            this.fadeTime = fadeTime;
        }

        /**
         * @return Final opacity (0-100)
         */
        public int getFinalOpacity() {
            return this.finalOpacity;
        }

        /**
         * @param finalOpacity Final opacity (0-100)
         */
        public void setFinalOpacity(final int finalOpacity) {
            this.finalOpacity = finalOpacity;
        }

        /**
         * @return Initial opacity (0-100)
         */
        public int getInitialOpacity() {
            return this.initialOpacity;
        }

        /**
         * @param initialOpacity Initial opacity (0-100)
         */
        public void setInitialOpacity(final int initialOpacity) {
            this.initialOpacity = initialOpacity;
        }

        /**
         * @return Display style to set on show
         */
        public String getDisplayStyle() {
            return this.displayStyle;
        }

        /**
         * @param displayStyle Display style to set on show
         */
        public void setDisplayStyle(final String displayStyle) {
            this.displayStyle = displayStyle;
        }
    }

    /**
     * @return FadeParameters for straight inline show
     */
    public static FadeParameters getFadeInParams() {
        final Fade.FadeParameters params = new FadeParameters();
        params.setInitialOpacity(0);
        params.setFinalOpacity(Fade.OPACITY);
        return params;
    }

    /**
     * @param displayStyle Display style to set on show
     * @return FadeParameters for straight show
     */
    public static FadeParameters getFadeInParams(final String displayStyle) {
        final Fade.FadeParameters params = new FadeParameters();
        params.setInitialOpacity(0);
        params.setFinalOpacity(Fade.OPACITY);
        params.setDisplayStyle(displayStyle);
        return params;
    }

    /**
     * @param fadeInTime Fade time (ms); if 0 this is a straight show/hide
     * @param displayStyle Display style to set on show
     * @return FadeParameters for fade in from 0 to 100%
     */
    public static FadeParameters getFadeInParams(final int fadeInTime, final String displayStyle) {
        return new FadeParameters(fadeInTime, 0, Fade.OPACITY, displayStyle);
    }

    /**
     * @return FadeParameters for straight hide
     */
    public static FadeParameters getFadeOutParams() {
        final Fade.FadeParameters params = new FadeParameters();
        params.setInitialOpacity(Fade.OPACITY);
        params.setFinalOpacity(0);
        return params;
    }

    /**
     * @param fadeOutTime Fade time (ms); if 0 this is a straight show/hide
     * @return FadeParameters for fade out 100% to 0
     */
    public static FadeParameters getFadeOutParams(final int fadeOutTime) {
        final FadeParameters fadeOutParams = new FadeParameters();
        fadeOutParams.setFadeTime(fadeOutTime);
        fadeOutParams.setInitialOpacity(Fade.OPACITY);
        fadeOutParams.setFinalOpacity(0);
        return fadeOutParams;
    }

    private static final Command IMPL = (Command) GWT.create(FadeImpl.class);

    private static final class FadeHolder {

        private static final Fade INSTANCE = new Fade();

        private FadeHolder() {
        }
    }

    private Fade() {
    }

    /**
     * @return Fade command instance
     */
    public static Fade getInstance() {
        return Fade.FadeHolder.INSTANCE;
    }

    public void execute(final UIObject actor, final Parameters params) {
        Fade.IMPL.execute(actor, params);
    }
}
