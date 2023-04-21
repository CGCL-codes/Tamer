package com.grapeshot.halfnes;

import com.grapeshot.halfnes.mappers.BadMapperException;
import com.grapeshot.halfnes.mappers.Mapper;
import java.util.prefs.Preferences;

/**
 *
 * @author Andrew Hoffman
 */
public class NES {

    private final Preferences prefs = Preferences.userNodeForPackage(this.getClass());

    private Mapper mapper;

    private APU apu;

    private CPU cpu;

    private CPURAM cpuram;

    private PPU ppu;

    private ControllerInterface controller1, controller2;

    public static final String VERSION = "0.047";

    private boolean runEmulation, dontSleep = false;

    public long frameStartTime, framecount, frameDoneTime;

    private boolean frameLimiterOn = true;

    private String curRomPath, curRomName;

    private final GUIInterface gui = new GUIImpl(this);

    private FrameLimiterInterface limiter = new FrameLimiterImpl(this);

    public NES() {
        try {
            java.awt.EventQueue.invokeAndWait(gui);
        } catch (InterruptedException e) {
            System.err.println("Could not initialize GUI. Exiting.");
            System.exit(-1);
        } catch (java.lang.reflect.InvocationTargetException f) {
            System.err.println(f.getCause().toString());
            System.exit(-1);
        }
    }

    public void run(final String romtoload) {
        Thread.currentThread().setPriority(Thread.NORM_PRIORITY + 1);
        curRomPath = romtoload;
        loadROM(romtoload);
        run();
    }

    public void run() {
        while (true) {
            if (runEmulation) {
                frameStartTime = System.nanoTime();
                runframe();
                if (frameLimiterOn && !dontSleep) {
                    limiter.sleep();
                }
                frameDoneTime = System.nanoTime() - frameStartTime;
            } else {
                limiter.sleepFixed();
                if (ppu != null && framecount > 1) {
                    java.awt.EventQueue.invokeLater(render);
                }
            }
        }
    }

    Runnable render = new Runnable() {

        public void run() {
            gui.render();
        }
    };

    public synchronized void runframe() {
        final int scanlinectrfire = 256;
        if ((utils.getbit(ppu.ppuregs[0], 7) && framecount > 1)) {
            cpu.runcycle(241, 9000);
            cpu.nmi();
        }
        for (int scanline = 241; scanline < 261; ++scanline) {
            cpu.cycle(scanline, scanlinectrfire);
            mapper.notifyscanline(scanline);
            cpu.cycle(scanline, 341);
        }
        ppu.ppuregs[2] &= 0x80;
        cpu.cycle(261, 30);
        ppu.ppuregs[2] &= 0x9F;
        cpu.cycle(261, scanlinectrfire);
        mapper.notifyscanline(261);
        cpu.cycle(261, (((framecount & 1) == 1) && utils.getbit(ppu.ppuregs[1], 3)) ? 340 : 341);
        dontSleep = apu.bufferHasLessThan(1000);
        apu.finishframe();
        cpu.modcycles();
        for (int scanline = 0; scanline < 240; ++scanline) {
            if (!ppu.drawLine(scanline)) {
                cpu.cycle(scanline, scanlinectrfire);
                mapper.notifyscanline(scanline);
            } else {
                final int sprite0x = ppu.getspritehit();
                if (sprite0x < scanlinectrfire) {
                    cpu.cycle(scanline, sprite0x);
                    ppu.ppuregs[2] |= 0x40;
                    cpu.cycle(scanline, scanlinectrfire);
                    mapper.notifyscanline(scanline);
                } else {
                    cpu.cycle(scanline, scanlinectrfire);
                    mapper.notifyscanline(scanline);
                    cpu.cycle(scanline, sprite0x);
                    ppu.ppuregs[2] |= 0x40;
                }
            }
            cpu.cycle(scanline, 341);
        }
        cpu.cycle(240, scanlinectrfire);
        mapper.notifyscanline(240);
        cpu.cycle(240, 341);
        ppu.ppuregs[2] |= 0x80;
        ppu.renderFrame(gui);
        if ((framecount & 2047) == 0) {
            saveSRAM(true);
        }
        ++framecount;
    }

    public void setControllers(ControllerInterface controller1, ControllerInterface controller2) {
        this.controller1 = controller1;
        this.controller2 = controller2;
    }

    public void toggleFrameLimiter() {
        if (frameLimiterOn) {
            frameLimiterOn = false;
        } else {
            frameLimiterOn = true;
        }
    }

    public synchronized void loadROM(final String filename) {
        runEmulation = false;
        if (!FileUtils.exists(filename) || !FileUtils.getExtension(filename).equalsIgnoreCase(".nes")) {
            gui.messageBox("Could not load file:\nFile " + filename + "\ndoes not exist or is not a valid NES game.");
            return;
        }
        Mapper newmapper;
        try {
            if (FileUtils.getExtension(filename).equalsIgnoreCase(".nes")) {
                final ROMLoader loader = new ROMLoader(filename);
                loader.parseInesheader();
                newmapper = Mapper.getCorrectMapper(loader.mappertype);
                newmapper.setLoader(loader);
                newmapper.loadrom();
            } else {
                throw new BadMapperException("ROM is not a valid NES game");
            }
        } catch (BadMapperException e) {
            gui.messageBox("Error Loading File: ROM is" + " corrupted or uses an unsupported mapper.\n" + e.getMessage());
            return;
        }
        if (apu != null) {
            apu.destroy();
            saveSRAM(false);
            mapper.destroy();
            cpu = null;
            cpuram = null;
            ppu = null;
        }
        mapper = newmapper;
        cpuram = mapper.getCPURAM();
        cpu = mapper.cpu;
        ppu = mapper.ppu;
        apu = new APU(this, cpu, cpuram);
        cpuram.setAPU(apu);
        cpuram.setPPU(ppu);
        curRomPath = filename;
        curRomName = FileUtils.getFilenamefromPath(filename);
        framecount = 0;
        if (mapper.hasSRAM()) {
            loadSRAM();
        }
        cpu.init();
        runEmulation = true;
    }

    private void saveSRAM(final boolean async) {
        if (mapper != null && mapper.hasSRAM() && mapper.supportsSaves()) {
            if (async) {
                FileUtils.asyncwritetofile(mapper.getPRGRam(), FileUtils.stripExtension(curRomPath) + ".sav");
            } else {
                FileUtils.writetofile(mapper.getPRGRam(), FileUtils.stripExtension(curRomPath) + ".sav");
            }
        }
    }

    private void loadSRAM() {
        final String name = FileUtils.stripExtension(curRomPath) + ".sav";
        if (FileUtils.exists(name) && mapper.supportsSaves()) {
            mapper.setPRGRAM(FileUtils.readfromfile(name));
        }
    }

    public void quit() {
        if (cpu != null && curRomPath != null) {
            runEmulation = false;
            saveSRAM(false);
        }
        System.exit(0);
    }

    public synchronized void reset() {
        if (cpu != null) {
            cpu.reset();
            runEmulation = true;
            apu.pause();
            apu.resume();
        }
        framecount = 0;
    }

    public synchronized void reloadROM() {
        loadROM(curRomPath);
    }

    public synchronized void pause() {
        if (apu != null) {
            apu.pause();
        }
        runEmulation = false;
    }

    public long getFrameTime() {
        return frameDoneTime;
    }

    public String getrominfo() {
        if (mapper != null) {
            return mapper.getrominfo();
        }
        return null;
    }

    public Preferences getPrefs() {
        return prefs;
    }

    public synchronized void frameAdvance() {
        runEmulation = false;
        if (cpu != null) {
            runframe();
        }
    }

    public synchronized void resume() {
        if (apu != null) {
            apu.resume();
        }
        if (cpu != null) {
            runEmulation = true;
        }
    }

    public String getCurrentRomName() {
        return curRomName;
    }

    public boolean isFrameLimiterOn() {
        return frameLimiterOn;
    }

    public void messageBox(final String string) {
        gui.messageBox(string);
    }

    public ControllerInterface getcontroller1() {
        return controller1;
    }

    public ControllerInterface getcontroller2() {
        return controller2;
    }
}
