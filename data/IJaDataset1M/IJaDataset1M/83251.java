package machine.functions;

import instructions.RuntimeError;
import machine.MachineState;
import org.apache.log4j.Logger;

public class FuncTone extends Func {

    private static final Logger logger = Logger.getLogger(FuncTone.class);

    public FuncTone(MachineState machine) {
        super(machine, "_Z10Tone_Startjj");
    }

    @Override
    public void exec() throws RuntimeError {
        int lo = this.machine.getRegister(24);
        int hi = this.machine.getRegister(25);
        final int tone = lo + hi * 256;
        lo = this.machine.getRegister(22);
        hi = this.machine.getRegister(23);
        final int duration_ms = lo + hi * 256;
        this.machine.noteMeggyCall();
        String toneString = "";
        if (tone == 61157) {
            toneString = "C3";
        } else if (tone == 57724) {
            toneString = "Cs3";
        } else if (tone == 54485) {
            toneString = "D3";
        } else if (tone == 51427) {
            toneString = "Ds3";
        } else if (tone == 48541) {
            toneString = "E3";
        } else if (tone == 45816) {
            toneString = "F3";
        } else if (tone == 43243) {
            toneString = "Fs3";
        } else if (tone == 40816) {
            toneString = "G3";
        } else if (tone == 38526) {
            toneString = "Gs3";
        } else if (tone == 36363) {
            toneString = "A3";
        } else if (tone == 34323) {
            toneString = "As3";
        } else if (tone == 32397) {
            toneString = "B3";
        } else {
        }
        System.out.println("Playing tone " + toneString + " for " + duration_ms + " milliseconds");
    }
}
