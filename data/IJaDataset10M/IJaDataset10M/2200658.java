package edu.uah.eduardomoriana.oldcomputer.model.memory;

import edu.uah.eduardomoriana.oldcomputer.model.cpu.Cpu;
import edu.uah.eduardomoriana.oldcomputer.model.screen.Screen;
import edu.uah.eduardomoriana.oldcomputer.model.tape.Tape;

public interface Memory {

    public static int _40Kb = 0x4000;

    public int m1(int addr, int ir);

    public int getMem(int addr);

    public void setMem(int addr, int v);

    public int getMem16(int addr);

    public void setMem16(int addr, int v);

    public void cont(int n);

    public void cont1(int n);

    public int[] getRam();

    public void setCpu(Cpu cpu);

    public void setTape(Tape tape);

    public void setScreen(Screen screen);

    public void blank();
}
