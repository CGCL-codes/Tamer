package net.sf.jauvm.vm.insn;

import java.util.Map;
import net.sf.jauvm.vm.VirtualMachine;
import org.objectweb.asm.Label;

public final class TableSwitchInsn extends LabeledInsn {

    public static Insn getInsn(int min, int max, Label dflt, Label[] labels) {
        return new TableSwitchInsn(min, max, dflt, labels);
    }

    private final int min;

    private final int max;

    private Label label;

    private int target;

    private Label[] labels;

    private final int[] targets;

    TableSwitchInsn(int min, int max, Label dflt, Label[] labels) {
        this.max = max;
        this.min = min;
        this.label = dflt;
        this.labels = labels;
        this.targets = new int[labels.length];
    }

    public void resolve(Map<Label, Integer> map) {
        target = map.get(label);
        for (int i = 0; i < labels.length; i++) targets[i] = map.get(labels[i]);
        label = null;
        labels = null;
    }

    public void execute(VirtualMachine vm) {
        int i = vm.getFrame().popInt();
        if (i < min || i > max) vm.setCp(target); else vm.setCp(targets[i - min]);
    }
}
