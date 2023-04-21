package net.sf.jauvm.vm.insn;

import java.util.Map;
import net.sf.jauvm.vm.VirtualMachine;
import org.objectweb.asm.Label;

public final class LookupSwitchInsn extends LabeledInsn {

    public static Insn getInsn(Label dflt, int[] keys, Label[] labels) {
        return new LookupSwitchInsn(dflt, keys, labels);
    }

    private Label label;

    private int target;

    private Label[] labels;

    private final int[] targets;

    private final int[] keys;

    LookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
        this.label = dflt;
        this.labels = labels;
        this.targets = new int[labels.length];
        this.keys = keys;
    }

    public void resolve(Map<Label, Integer> map) {
        target = map.get(label);
        for (int i = 0; i < labels.length; i++) targets[i] = map.get(labels[i]);
        label = null;
        labels = null;
    }

    public void execute(VirtualMachine vm) {
        int i = vm.getFrame().popInt();
        for (int j = 0; j < keys.length; j++) if (i == keys[j]) {
            vm.setCp(targets[j]);
            return;
        }
        vm.setCp(target);
    }
}
