package juicynes.hardware;

import java.io.IOException;

public class Nes {

    private Cpu cpu;

    private CpuMemoryMap cpuMemoryMap;

    private Ppu ppu;

    public Nes() {
        cpuMemoryMap = new CpuMemoryMap();
        cpu = new Cpu(cpuMemoryMap);
        ppu = new Ppu();
    }

    public void run(Rom rom) throws IOException {
        cpuMemoryMap.loadRom(rom);
        cpu.run();
    }

    public static void main(String[] args) throws IOException {
        Nes nes = new Nes();
        nes.run(new InesRom("test/nestest.nes"));
    }
}
