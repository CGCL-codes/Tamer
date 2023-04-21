package jemu.system.zx;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import jemu.core.cpu.*;
import jemu.core.device.*;
import jemu.core.device.keyboard.*;
import jemu.core.device.memory.*;
import jemu.core.device.sound.*;
import jemu.ui.*;
import jemu.util.diss.*;

/**
 * Title:        JEMU
 * Description:  The Java Emulation Platform
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author
 * @version 1.0
 */
public class ZX extends Computer {

    protected static final int CYCLES_PER_SECOND = 3250000;

    protected Z80 z80 = new Z80(CYCLES_PER_SECOND);

    protected Disassembler disassembler = new DissZ80();

    protected ZXMemory memory = new ZXMemory(16);

    protected Renderer renderer = (Renderer) addDevice(new Renderer(this, z80, memory));

    protected Keyboard keyboard = new Keyboard();

    protected boolean zx81;

    public ZX(Applet applet, String name) {
        super(applet, name);
        name = name.toLowerCase();
        zx81 = "zx81".equals(name);
        setBasePath(name);
    }

    public void initialise() {
        z80.setMemoryDevice(memory);
        renderer.setZX81(zx81);
        z80.setRenderer(renderer);
        z80.setCycleDevice(renderer);
        z80.setInterruptDevice(renderer);
        z80.addInputDeviceMapping(new DeviceMapping(renderer, 0x01, 0x00));
        z80.addOutputDeviceMapping(new DeviceMapping(renderer, 0x00, 0x00));
        z80.addInputDeviceMapping(new DeviceMapping(keyboard, 0x01, 0x00));
        memory.setMemory(0, getFile(romPath + name + ".ROM", zx81 ? 8192 : 4096));
        super.initialise();
    }

    public Memory getMemory() {
        return memory;
    }

    public Processor getProcessor() {
        return z80;
    }

    public void vsync() {
        if (frameSkip == 0) display.updateImage(true);
        syncProcessor();
        renderer.setFrameSkip(frameSkip != 0);
    }

    public void keyPressed(KeyEvent e) {
        keyboard.keyPressed(e.getKeyCode());
    }

    public void keyReleased(KeyEvent e) {
        keyboard.keyReleased(e.getKeyCode());
    }

    public void loadFile(int type, String name) throws Exception {
        if ((zx81 && (name.toLowerCase().endsWith(".p") || name.endsWith(".81"))) || (!zx81 && (name.toLowerCase().endsWith(".o") || name.endsWith(".80")))) {
            InputStream in = openFile(name);
            int addr = zx81 ? 0x4009 : 0x4000;
            try {
                int read;
                while ((read = in.read()) != -1) memory.writeByte(addr++, read);
                while (addr < 0x7ffe) memory.writeByte(addr++, 0x00);
                z80.setIY(0x4000);
                if (zx81) {
                    int[] data = { 0xff, 0x80, 0xfc, 0x7f, 0x00, 0x80, 0x00, 0xfe, 0xff };
                    for (int i = 0; i < data.length; i++) memory.writeByte(i + 0x4000, data[i]);
                    memory.writeByte(0x7ffc, 0x76);
                    memory.writeByte(0x7ffd, 0x06);
                    z80.setSP(0x7ffc);
                    z80.setPC(0x0207);
                    z80.setIX(0x028f);
                    z80.setI(0x1e);
                    renderer.writePort(0xfd, 0x00);
                } else {
                    z80.setSP(0x7ffe);
                    z80.setPC(0x0283);
                    z80.setI(0x0e);
                }
                z80.di();
            } finally {
                in.close();
            }
        }
    }

    public void setDisplay(Display value) {
        super.setDisplay(value);
        renderer.setDisplay(value);
    }

    public Dimension getDisplaySize(boolean large) {
        return new Dimension(320, 216);
    }

    public Disassembler getDisassembler() {
        return disassembler;
    }

    public void displayLostFocus() {
        keyboard.reset();
    }
}
