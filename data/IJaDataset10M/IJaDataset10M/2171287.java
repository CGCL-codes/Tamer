package com.google.gwt.maeglin89273.game.ashinyballonthecross.client.utility;

import org.jbox2d.common.Settings;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.maeglin89273.game.mengine.asset.sprite.SpriteBlock;
import com.google.gwt.maeglin89273.game.mengine.asset.sprite.SpriteSheet;
import com.google.gwt.maeglin89273.game.mengine.core.MEngine;
import com.google.gwt.maeglin89273.game.mengine.service.LoginInfo;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Window;

/**
 * @author Maeglin Liao
 *
 */
public final class ASBOTXConfigs {

    public static final String CLOSE_PAGE_PATH = GWT.getHostPageBaseURL() + "/AutoClose.html";

    public static final String VERSION = "v1.6.2";

    public static final float E_SQUARE = Settings.EPSILON * Settings.EPSILON;

    public static class Color {

        public static final CssColor TEXT = CssColor.make("rgba(208,210,211,0.5)");

        public static final CssColor GLASS = CssColor.make("hsla(0,0%,0%,0.2)");

        public static final CssColor WHITE = CssColor.make("rgb(255,255,255)");

        public static final CssColor GRAY = CssColor.make("hsla(0,0%,25%,0.6)");

        public static final CssColor BLACK = CssColor.make("hsla(0,0%,0%,0.9)");

        public static final CssColor DARK_GRAY = CssColor.make("hsla(0,0%,15%,0.7)");

        public static final CssColor BORDER_DARK_GRAY = CssColor.make("hsl(0,0%,30%)");

        public static final CssColor TRANSLUCENT_DARK_GRAY = CssColor.make("hsla(0,0%,20%,0.4)");

        public static final CssColor YELLOW_BORDER = CssColor.make("hsla(55.5,95%,50%,0.8)");

        public static final CssColor LIGHT_YELLOW = CssColor.make("hsla(60,97%,60%,0.9)");

        public static final CssColor PURE_YELLOW = CssColor.make("hsl(60,97%,50%)");

        public static final CssColor TRANSPARENT_YELLOW = CssColor.make("hsla(58,97%,50%,0.4)");

        public static final CssColor BLUE = CssColor.make("hsla(210,90%,50%,0.8)");

        public static final CssColor TRANSPARENT_BLUE = CssColor.make("hsla(195,90%,50%,0.6)");

        public static final CssColor LIGHT_BLUE = CssColor.make("hsla(185,95%,50%,0.8)");

        public static final CssColor DARK_BLUE = CssColor.make("hsla(220,95%,40%,0.9)");

        public static final CssColor RED = CssColor.make("hsla(5,95%,50%,0.8)");

        public static final CssColor ORANGE = CssColor.make("hsl(40,90%,48%)");

        public static final CssColor DARK_RED = CssColor.make("hsla(5,50%,50%,0.9)");

        public static final CssColor TRANSPARENT_RED = CssColor.make("hsla(5,75%,50%,0.6)");

        public static final CssColor WOOD = CssColor.make("hsla(15,95%,25%,0.9)");

        public static final CssColor GREEN_BORDER = CssColor.make("hsla(130,95%,45%,0.8)");

        public static final CssColor TRANSPARENT_GREEN = CssColor.make("hsla(120,90%,42.5%,0.4)");

        public static final CssColor getRandomShapeBorderColor() {
            return CssColor.make("hsla(" + Random.nextInt(361) + ",95%,48%,0.8)");
        }
    }

    public static class KeysConfiguration {

        public static final char DOT = 'D';

        public static final char LINE = 'S';

        public static final char AREA = 'A';

        public static final char RETURN = KeyCodes.KEY_ESCAPE;

        public static final char REMOVE_CREATION = 'Z';

        public static final char REMOVE_FIRST = KeyCodes.KEY_CTRL;

        public static final char REMOVE_LAST = KeyCodes.KEY_ALT;

        public static final char RESET_LEVEL = 'R';
    }

    public static class CreationPowerComsumption {

        public static final int ELASTIC_LINE = 70;

        public static final int MAGNETIC_LINE = 65;

        public static final int CEMENT_LINE = 60;

        public static final int WOOD_LINE = 55;

        public static final int SIMPLE_STATIC_LINE = 65;

        public static final int ARROW_AREA = 80;

        public static final int GRAVITATIONAL_AREA = 60;

        public static final int TRIANGLE_AREA = 35;
    }

    public static final String getCGFont(int pt) {
        return pt + "pt Century Gothic";
    }

    public static final String getCGBoldFont(int pt) {
        return pt + "pt Century Gothic Bold";
    }

    public static class Utility {

        public static final int BUTTON_SPACING = 200 + SpriteBlock.SPACING;

        public static final SpriteSheet getButtonsSpriteSheet() {
            return MEngine.getAssetManager().getSpriteSheet("images/buttons.png");
        }

        public static final int buttonSpacingTimes(int a) {
            return a * BUTTON_SPACING;
        }

        public static final void alertError(Throwable caught) {
            Window.alert(caught.getMessage());
        }

        public static LoginInfo.Status switchStatus(LoginInfo.Status status) {
            switch(status) {
                case LOGGED_IN:
                    return LoginInfo.Status.LOGGED_OUT;
                case LOGGED_OUT:
                    return LoginInfo.Status.LOGGED_IN;
            }
            return status;
        }
    }
}
