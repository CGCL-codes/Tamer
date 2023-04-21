package com.lambelly.lambnes.platform.ppu;

import com.lambelly.lambnes.LambNes;
import com.lambelly.lambnes.gui.*;
import com.lambelly.lambnes.platform.*;
import com.lambelly.lambnes.platform.interrupts.InterruptRequest;
import com.lambelly.lambnes.platform.interrupts.NesInterrupts;
import com.lambelly.lambnes.platform.ppu.registers.PPUControlRegister;
import com.lambelly.lambnes.platform.ppu.registers.PPUMaskRegister;
import com.lambelly.lambnes.platform.ppu.registers.PPUSprRamAddressRegister;
import com.lambelly.lambnes.platform.ppu.registers.PPUSprRamIORegister;
import com.lambelly.lambnes.platform.ppu.registers.PPUSpriteDMARegister;
import com.lambelly.lambnes.platform.ppu.registers.PPUStatusRegister;
import com.lambelly.lambnes.platform.ppu.registers.PPUScrollRegister;
import com.lambelly.lambnes.platform.ppu.registers.PPUVramAddressRegister;
import com.lambelly.lambnes.platform.ppu.registers.PPUVramIORegister;
import org.apache.log4j.*;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

public class NesPpu implements PictureProcessingUnit {

    private static final int SCANLINE_IDLE = 240;

    private static final int SCANLINE_261 = 261;

    private static final int VBLANK_SCANLINE_START = 241;

    private static final int VBLANK_SCANLINE_END = 260;

    private static final int REFRESH_RATE = 60;

    private static final int NUM_SCANLINES_PER_FRAME = 262;

    private static final int NUM_HORIZONTAL_TILES = 32;

    private static final int NUM_VERTICAL_TILES = 30;

    public static final int SPRITE_COUNT = 64;

    public static final int PPU_CYCLES_PER_LINE = 341;

    public static final int CPU_CYCLES_PER_LINE = 113;

    public static final int PPU_CYCLES_PER_FRAME = NUM_SCANLINES_PER_FRAME * PPU_CYCLES_PER_LINE;

    private int scanlineCount = 0;

    private int verticalPerTileCount = 0;

    private int registerAddressFlipFlopLatch = 0;

    private int loopyT = 0;

    private int loopyV = 0;

    private int loopyX = 0;

    private int ppuCyclesUntilEndOfFrame = NesPpu.PPU_CYCLES_PER_FRAME;

    private int ppuCyclesUntilEndOfLine = NesPpu.PPU_CYCLES_PER_LINE;

    private long vblankInterval = 0;

    private ScreenBuffer screenBuffer = new ScreenBuffer();

    private long screenCount = 0;

    private NesPpuMemory ppuMemory;

    private PPUSprRamIORegister ppuSprRamIORegister;

    private PPUSprRamAddressRegister ppuSprRamAddressRegister;

    private PPUControlRegister ppuControlRegister;

    private PPUStatusRegister ppuStatusRegister;

    private PPUVramAddressRegister ppuVramAddressRegister;

    private PPUVramIORegister ppuVramIORegister;

    private PPUScrollRegister ppuScrollRegister;

    private PPUSpriteDMARegister ppuSpriteDmaRegister;

    private PPUMaskRegister ppuMaskRegister;

    private NesInterrupts interrupts;

    private Logger logger = Logger.getLogger(NesPpu.class);

    public void cycle(int cpuCycleCount) {
        this.doRegisterReadsWrites();
        this.subtractFromPpuCyclesUntilEndOfFrame(cpuCycleCount * 3);
        this.subtractFromPpuCyclesUntilEndOfLine(cpuCycleCount * 3);
        if (this.getPpuCyclesUntilEndOfLine() <= 0) {
            if (getScanlineCount() >= NesPpu.VBLANK_SCANLINE_START && getScanlineCount() <= NesPpu.VBLANK_SCANLINE_END) {
                this.setLoopyV(this.getLoopyT());
                if (getScanlineCount() == NesPpu.VBLANK_SCANLINE_START) {
                    this.getPpuStatusRegister().setVblank(true);
                    this.vblankInterval = Platform.getCycleCount();
                    if (this.getPpuControlRegister().isExecuteNMIOnVBlank()) {
                        interrupts.addInterruptRequestToQueue(new InterruptRequest(InterruptRequest.interruptTypeNMI));
                    }
                } else if (getScanlineCount() == NesPpu.VBLANK_SCANLINE_END) {
                    this.getPpuStatusRegister().setSprite0Occurance(false);
                    this.getPpuStatusRegister().setVblank(false);
                    this.getPpuStatusRegister().setScanlineSpriteCount(false);
                    this.setLoopyV(this.getLoopyT());
                }
                incrementScanlineCount();
            } else if (getScanlineCount() == NesPpu.SCANLINE_IDLE) {
                incrementScanlineCount();
            } else if (getScanlineCount() == NesPpu.SCANLINE_261) {
                setScanlineCount(0);
            } else {
                this.drawScanline(this.getScanlineCount());
                incrementScanlineCount();
                int setLoopyV = (this.getLoopyV() & 0xFBE0) | (this.getLoopyT() & 0x41F);
                this.setLoopyV(setLoopyV);
            }
            this.addToPpuCyclesUntilEndOfLine(NesPpu.PPU_CYCLES_PER_LINE);
        }
        if (this.getPpuCyclesUntilEndOfFrame() <= 0) {
            this.incrementScreenCount();
            this.getScreenBuffer().pushBufferToScreen();
            this.addToPpuCyclesUntilEndOfFrame(NesPpu.PPU_CYCLES_PER_FRAME);
        }
    }

    private void drawScanline(int scanline) {
        if (this.getPpuMaskRegister().isBackgroundVisibility()) {
            this.drawBackground(scanline);
        }
        if (this.getPpuMaskRegister().isSpriteVisibility()) {
            drawSprites(scanline);
            this.getPpuMemory().clearSpriteBuffer();
            updateSpriteBuffer(scanline);
        }
        if (((scanline + 1) & 7) == 0) {
            this.incrementVerticalPerTileCount();
            if (this.getVerticalPerTileCount() == NesPpu.NUM_VERTICAL_TILES) {
                this.setVerticalPerTileCount(0);
            }
        }
    }

    private void drawSprites(int verticalPerPixelCount) {
        if (this.getPpuMemory().getSpritesInBufferCount() > 0) {
            for (int i = 0; i < this.getPpuMemory().getSpritesInBufferCount(); i++) {
                SpriteTile sprite = this.getPpuMemory().getSpriteFromBuffer(i);
                if (drawSpriteTileLine(sprite, verticalPerPixelCount) && (!this.getPpuStatusRegister().isSprite0Occurance())) {
                    this.getPpuStatusRegister().setSprite0Occurance(true);
                }
            }
        }
    }

    private boolean drawSpriteTileLine(SpriteTile sprite, int verticalPerPixelCount) {
        boolean sprite0Triggered = false;
        int spriteLine = verticalPerPixelCount - sprite.getAttributes().getyCoordinate();
        if (this.getPpuControlRegister().getSpriteSize() == PPUControlRegister.SPRITE_SIZE_8X8) {
            spriteLine = spriteLine & 0x7;
        } else {
            spriteLine = spriteLine & 0xF;
        }
        int spriteXPosition = sprite.getAttributes().getxCoordinate();
        int sprite0Number = this.getPpuMemory().getSprRam(0).getTileIndex();
        PaletteColor spriteTransparentColor = new PaletteColor(0, PaletteColor.PALETTE_TYPE_SPRITE);
        PaletteColor backgroundTransparentColor = new PaletteColor(0, PaletteColor.PALETTE_TYPE_BACKGROUND);
        PaletteColor[] spriteRow = sprite.getTileColorRow(spriteLine);
        for (int pixelIndex = 0; pixelIndex < spriteRow.length; pixelIndex++) {
            PaletteColor pixelToBeDrawn = spriteRow[pixelIndex];
            if (pixelToBeDrawn.getMasterPaletteIndex() != spriteTransparentColor.getMasterPaletteIndex()) {
                if (LambNesGui.getScreen().getImage().getRGB(((spriteXPosition + pixelIndex) & Platform.EIGHT_BIT_MASK), verticalPerPixelCount) != backgroundTransparentColor.getMasterPaletteColor().getColorInt()) {
                    if (sprite.getSpriteNumber() == sprite0Number && !this.getPpuStatusRegister().isSprite0Occurance()) {
                        sprite0Triggered = true;
                    }
                } else {
                }
                if (spriteXPosition >= 8 || this.getPpuMaskRegister().isSpriteVisibility()) {
                    this.getScreenBuffer().setScreenBufferPixel((spriteXPosition & Platform.EIGHT_BIT_MASK), verticalPerPixelCount, pixelToBeDrawn);
                }
            }
            spriteXPosition++;
        }
        return sprite0Triggered;
    }

    private void updateSpriteBuffer(int verticalPerPixelCount) {
        this.getPpuStatusRegister().setScanlineSpriteCount(false);
        int spriteCount = 0;
        for (int spriteAttributeIndexNumber = 0; spriteAttributeIndexNumber < SPRITE_COUNT; spriteAttributeIndexNumber++) {
            SpriteAttribute spriteAttribute = this.getPpuMemory().getSprRam(spriteAttributeIndexNumber);
            int diff = ((verticalPerPixelCount + 1) - spriteAttribute.getyCoordinate());
            if ((diff >= 0 && diff <= 7) || ((this.getPpuControlRegister().getSpriteSize() == PPUControlRegister.SPRITE_SIZE_8X16) && (diff >= 0 && diff <= 15))) {
                SpriteTile sprite = new SpriteTile(spriteAttribute.getTileIndex(), spriteAttribute);
                this.getPpuMemory().setSpriteToBuffer(spriteCount, sprite);
                spriteCount++;
                if (spriteCount == 9) {
                    spriteCount = 8;
                    this.getPpuStatusRegister().setScanlineSpriteCount(true);
                    break;
                }
            }
        }
    }

    private void drawBackground(int scanline) {
        for (int horizontalPerTileCount = 0; horizontalPerTileCount < NesPpu.NUM_HORIZONTAL_TILES; horizontalPerTileCount++) {
            int hCoarseScrollOffset = (this.getLoopyV() & 0x1F);
            int offsetHorizontalPerTileCount = horizontalPerTileCount + hCoarseScrollOffset;
            int hFineScrollOffset = (this.getLoopyX() & 0x07);
            int vCoarseScrollOffset = ((this.getLoopyV() & 0x3E0) >> 5);
            int offsetVerticalPerTileCount = this.getVerticalPerTileCount() + vCoarseScrollOffset;
            int vFineScrollOffset = ((this.getLoopyV() & 0x7000) >> 12);
            int horizontalNameCount = this.getPpuControlRegister().getNameTableAddress();
            int verticalNameCount = this.getPpuControlRegister().getNameTableAddress();
            int tileYindex = (scanline & 0x7);
            tileYindex += vFineScrollOffset;
            if (tileYindex > 0x7) {
                tileYindex = (tileYindex & 0x7);
                offsetVerticalPerTileCount++;
            }
            if ((offsetHorizontalPerTileCount * 8) + hFineScrollOffset > Screen.SCREEN_HORIZONTAL_RESOLUTION) {
                horizontalNameCount = (1 ^ horizontalNameCount);
                offsetHorizontalPerTileCount &= 0x1F;
            }
            if ((offsetVerticalPerTileCount * 8) + vFineScrollOffset >= Screen.SCREEN_VERTICAL_RESOLUTION) {
                verticalNameCount = (1 ^ verticalNameCount);
                offsetVerticalPerTileCount = offsetVerticalPerTileCount % NesPpu.NUM_VERTICAL_TILES;
            }
            int nameTableAddress = 0x2000 + offsetHorizontalPerTileCount | (offsetVerticalPerTileCount << 5) | (horizontalNameCount << 10) | (verticalNameCount << 11);
            drawBackgroundTile(nameTableAddress, horizontalPerTileCount, hFineScrollOffset, vFineScrollOffset, tileYindex, scanline);
        }
    }

    private void drawBackgroundTile(int nameTableAddress, int horizontalPerTileCount, int hFineScrollOffset, int vFineScrollOffset, int tileYindex, int scanline) {
        if (horizontalPerTileCount >= 1 || !this.getPpuMaskRegister().isBackGroundClipping()) {
            PaletteColor[] tileRow = new PaletteColor[8];
            BackgroundTile bg = this.getPpuMemory().getNameTableFromHexAddress(nameTableAddress).getTileFromHexAddress(nameTableAddress);
            System.arraycopy(bg.getTileColorRow(tileYindex), 0, tileRow, 0, (tileRow.length));
            this.getScreenBuffer().setScreenBufferTileRow((horizontalPerTileCount * 8), scanline, hFineScrollOffset, vFineScrollOffset, tileRow);
        } else {
        }
    }

    private void doRegisterReadsWrites() {
        this.getPpuControlRegister().cycle();
        this.getPpuMaskRegister().cycle();
        this.getPpuStatusRegister().cycle();
        this.getPpuSpriteDmaRegister().cycle();
        this.getPpuSprRamAddressRegister().cycle();
        this.getPpuSprRamIORegister().cycle();
        this.getPpuScrollRegister().cycle();
        this.getPpuVramAddressRegister().cycle();
        this.getPpuVramIORegister().cycle();
    }

    public int getScanlineCount() {
        return scanlineCount;
    }

    public void incrementScanlineCount() {
        ++scanlineCount;
    }

    public void setScanlineCount(int scanlineCount) {
        this.scanlineCount = scanlineCount;
    }

    public int getVerticalPerTileCount() {
        return verticalPerTileCount;
    }

    public void setVerticalPerTileCount(int verticalTileCount) {
        this.verticalPerTileCount = verticalTileCount;
    }

    public void incrementVerticalPerTileCount() {
        this.verticalPerTileCount++;
    }

    public int getRegisterAddressFlipFlopLatch() {
        int latchValue = this.registerAddressFlipFlopLatch;
        this.flipRegisterAddressFlipFlopLatch();
        return latchValue;
    }

    public void flipRegisterAddressFlipFlopLatch() {
        this.registerAddressFlipFlopLatch = (1 ^ this.registerAddressFlipFlopLatch);
    }

    public void resetRegisterAddressFlipFlopLatch() {
        this.registerAddressFlipFlopLatch = 0;
    }

    public void setRegisterAddressFlipFlopLatch(int registerAddressFlipFlopLatch) {
        this.registerAddressFlipFlopLatch = registerAddressFlipFlopLatch;
    }

    public ScreenBuffer getScreenBuffer() {
        return screenBuffer;
    }

    public void setScreenBuffer(ScreenBuffer screenBuffer) {
        this.screenBuffer = screenBuffer;
    }

    public int getLoopyT() {
        return loopyT;
    }

    public void setLoopyT(int loopyT) {
        this.loopyT = loopyT;
    }

    public int getLoopyV() {
        return loopyV;
    }

    public void setLoopyV(int loopyV) {
        this.loopyV = loopyV;
    }

    public int getLoopyX() {
        return loopyX;
    }

    public void setLoopyX(int loopyX) {
        this.loopyX = loopyX;
    }

    public int getPpuCyclesUntilEndOfFrame() {
        return ppuCyclesUntilEndOfFrame;
    }

    public void setPpuCyclesUntilEndOfFrame(int ppuCyclesUntilEndOfFrame) {
        this.ppuCyclesUntilEndOfFrame = ppuCyclesUntilEndOfFrame;
    }

    public void addToPpuCyclesUntilEndOfFrame(int cycles) {
        this.ppuCyclesUntilEndOfFrame += cycles;
    }

    public void subtractFromPpuCyclesUntilEndOfFrame(int cycles) {
        this.ppuCyclesUntilEndOfFrame -= cycles;
    }

    public int getPpuCyclesUntilEndOfLine() {
        return ppuCyclesUntilEndOfLine;
    }

    public void setPpuCyclesUntilEndOfLine(int ppuCyclesUntilEndOfLine) {
        this.ppuCyclesUntilEndOfLine = ppuCyclesUntilEndOfLine;
    }

    public void addToPpuCyclesUntilEndOfLine(int cycles) {
        this.ppuCyclesUntilEndOfLine += cycles;
    }

    public void subtractFromPpuCyclesUntilEndOfLine(int cycles) {
        this.ppuCyclesUntilEndOfLine -= cycles;
    }

    public long getScreenCount() {
        return screenCount;
    }

    public void setScreenCount(long screenCount) {
        this.screenCount = screenCount;
    }

    public void incrementScreenCount() {
        this.screenCount++;
    }

    public NesPpuMemory getPpuMemory() {
        return ppuMemory;
    }

    public void setPpuMemory(NesPpuMemory ppuMemory) {
        this.ppuMemory = ppuMemory;
    }

    public PPUSprRamIORegister getPpuSprRamIORegister() {
        return ppuSprRamIORegister;
    }

    public void setPpuSprRamIORegister(PPUSprRamIORegister ppuSprRamIORegister) {
        this.ppuSprRamIORegister = ppuSprRamIORegister;
    }

    public PPUControlRegister getPpuControlRegister() {
        return ppuControlRegister;
    }

    public void setPpuControlRegister(PPUControlRegister ppuControlRegister) {
        this.ppuControlRegister = ppuControlRegister;
    }

    public PPUStatusRegister getPpuStatusRegister() {
        return ppuStatusRegister;
    }

    public void setPpuStatusRegister(PPUStatusRegister ppuStatusRegister) {
        this.ppuStatusRegister = ppuStatusRegister;
    }

    public PPUVramAddressRegister getPpuVramAddressRegister() {
        return ppuVramAddressRegister;
    }

    public void setPpuVramAddressRegister(PPUVramAddressRegister ppuVramAddressRegister) {
        this.ppuVramAddressRegister = ppuVramAddressRegister;
    }

    public PPUVramIORegister getPpuVramIORegister() {
        return ppuVramIORegister;
    }

    public void setPpuVramIORegister(PPUVramIORegister ppuVramIORegister) {
        this.ppuVramIORegister = ppuVramIORegister;
    }

    public PPUScrollRegister getPpuScrollRegister() {
        return ppuScrollRegister;
    }

    public void setPpuScrollRegister(PPUScrollRegister ppuScrollRegister) {
        this.ppuScrollRegister = ppuScrollRegister;
    }

    public PPUSprRamAddressRegister getPpuSprRamAddressRegister() {
        return ppuSprRamAddressRegister;
    }

    public void setPpuSprRamAddressRegister(PPUSprRamAddressRegister ppuSprRamAddressRegister) {
        this.ppuSprRamAddressRegister = ppuSprRamAddressRegister;
    }

    public PPUSpriteDMARegister getPpuSpriteDmaRegister() {
        return ppuSpriteDmaRegister;
    }

    public void setPpuSpriteDmaRegister(PPUSpriteDMARegister ppuSpriteDmaRegister) {
        this.ppuSpriteDmaRegister = ppuSpriteDmaRegister;
    }

    public PPUMaskRegister getPpuMaskRegister() {
        return ppuMaskRegister;
    }

    public void setPpuMaskRegister(PPUMaskRegister ppuMaskRegister) {
        this.ppuMaskRegister = ppuMaskRegister;
    }

    public NesInterrupts getInterrupts() {
        return interrupts;
    }

    public void setInterrupts(NesInterrupts interrupts) {
        this.interrupts = interrupts;
    }
}
