package com.lambelly.lambnes.platform.ppu.registers;

import com.lambelly.lambnes.platform.Platform;
import com.lambelly.lambnes.util.*;

public class PPUMaskRegister {

    public static final int REGISTER_ADDRESS = 0x2001;

    public static final int FULL_BACKGROUND_COLOR_NONE = 0;

    public static final int FULL_BACKGROUND_COLOR_GREEN = 1;

    public static final int FULL_BACKGROUND_COLOR_BLUE = 2;

    public static final int FULL_BACKGROUND_COLOR_RED = 3;

    public static final int COLOR_INTENSITY_NONE = 0;

    public static final int COLOR_INTENSITY_GREEN = 1;

    public static final int COLOR_INTENSITY_BLUE = 2;

    public static final int COLOR_INTENSITY_RED = 3;

    public static final int DISPLAY_TYPE_COLOR = 0;

    public static final int DISPLAY_TYPE_MONOCHROME = 1;

    private static final int CYCLES_PER_EXECUTION = 0;

    private Integer rawControlByte = null;

    private Integer controlByte = null;

    private int fullBackgroundColor = 0;

    private int colorIntensity = 0;

    private boolean spriteVisibility = false;

    private boolean backgroundVisibility = false;

    private boolean spriteClipping = false;

    private boolean backGroundClipping = false;

    private int displayType = 0;

    private PPUMaskRegister() {
    }

    public int cycle() {
        if (this.getRawControlByte() != null) {
            if (this.getControlByte() != this.getRawControlByte()) {
                this.setControlByte(this.getRawControlByte());
                this.setSpriteVisibility(BitUtils.isBitSet(this.getRawControlByte(), 4));
                this.setBackgroundVisibility(BitUtils.isBitSet(this.getRawControlByte(), 3));
                this.setSpriteClipping(!BitUtils.isBitSet(this.getRawControlByte(), 2));
                this.setBackGroundClipping(!BitUtils.isBitSet(this.getRawControlByte(), 1));
                this.setDisplayType(BitUtils.isBitSet(this.getRawControlByte(), 0) ? DISPLAY_TYPE_MONOCHROME : DISPLAY_TYPE_COLOR);
                if (this.getDisplayType() == DISPLAY_TYPE_MONOCHROME) {
                    if (BitUtils.isBitSet(this.getRawControlByte(), 7)) {
                        this.setFullBackgroundColor(FULL_BACKGROUND_COLOR_RED);
                    } else if (BitUtils.isBitSet(this.getRawControlByte(), 6)) {
                        this.setFullBackgroundColor(FULL_BACKGROUND_COLOR_BLUE);
                    } else if (BitUtils.isBitSet(this.getRawControlByte(), 5)) {
                        this.setFullBackgroundColor(FULL_BACKGROUND_COLOR_GREEN);
                    } else {
                        this.setFullBackgroundColor(FULL_BACKGROUND_COLOR_NONE);
                    }
                } else {
                    if (BitUtils.isBitSet(this.getRawControlByte(), 7)) {
                        this.setColorIntensity(COLOR_INTENSITY_RED);
                    } else if (BitUtils.isBitSet(this.getRawControlByte(), 6)) {
                        this.setColorIntensity(COLOR_INTENSITY_BLUE);
                    } else if (BitUtils.isBitSet(this.getRawControlByte(), 5)) {
                        this.setColorIntensity(COLOR_INTENSITY_GREEN);
                    } else {
                        this.setColorIntensity(COLOR_INTENSITY_NONE);
                    }
                }
            }
        }
        return PPUMaskRegister.CYCLES_PER_EXECUTION;
    }

    public String toString() {
        if (this.getRawControlByte() != null) {
            return "0x" + REGISTER_ADDRESS + ": " + Integer.toBinaryString(this.getRawControlByte());
        } else {
            return "0x" + REGISTER_ADDRESS + ": 0";
        }
    }

    public void setRegisterValue(int value) {
        this.setRawControlByte(value);
    }

    public int getFullBackgroundColor() {
        return fullBackgroundColor;
    }

    private void setFullBackgroundColor(int fullBackgroundColor) {
        this.fullBackgroundColor = fullBackgroundColor;
    }

    public int getColorIntensity() {
        return colorIntensity;
    }

    private void setColorIntensity(int colorIntensity) {
        this.colorIntensity = colorIntensity;
    }

    public boolean isSpriteVisibility() {
        return spriteVisibility;
    }

    private void setSpriteVisibility(boolean spriteVisibility) {
        this.spriteVisibility = spriteVisibility;
    }

    public boolean isBackgroundVisibility() {
        return backgroundVisibility;
    }

    private void setBackgroundVisibility(boolean backgroundVisibility) {
        this.backgroundVisibility = backgroundVisibility;
    }

    public boolean isSpriteClipping() {
        return spriteClipping;
    }

    private void setSpriteClipping(boolean spriteClipping) {
        this.spriteClipping = spriteClipping;
    }

    public boolean isBackGroundClipping() {
        return backGroundClipping;
    }

    private void setBackGroundClipping(boolean backGroundClipping) {
        this.backGroundClipping = backGroundClipping;
    }

    public int getDisplayType() {
        return displayType;
    }

    private void setDisplayType(int displayType) {
        this.displayType = displayType;
    }

    private Integer getRawControlByte() {
        return rawControlByte;
    }

    private void setRawControlByte(int rawControlByte) {
        this.rawControlByte = rawControlByte;
    }

    public Integer getControlByte() {
        return controlByte;
    }

    public void setControlByte(Integer controlByte) {
        this.controlByte = controlByte;
    }
}
