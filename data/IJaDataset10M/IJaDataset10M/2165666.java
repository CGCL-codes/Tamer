package test;

import projet.*;
import donnees.ressources.*;
import donnees.ressources.Process;

public class test_ressources {

    public static void main(String args[]) throws InterruptedException {
        CPU le_cpu = new CPU();
        RAM la_ram = new RAM();
        Process le_proc = new Process();
        long watchtime = 2000;
        CpuLoad cpu = new CpuLoad(watchtime, le_cpu);
        MemLoad mem = new MemLoad(watchtime, la_ram);
        ProcessLoad proc = new ProcessLoad(watchtime, le_proc);
        cpu.start();
        mem.start();
        proc.start();
        while (true) {
            Thread.sleep(2000);
            System.out.println("Occupation de la RAM : " + la_ram.getValeur());
            System.out.println("Occupation de la CPU : " + le_cpu.getValeur());
            System.out.println("Nombre de process : " + le_proc.getValeur());
        }
    }
}
