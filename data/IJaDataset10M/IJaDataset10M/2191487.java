package org.sodeja.explicit;

import org.sodeja.runtime.scheme.model.Combination;

class Application implements Executable {

    @Override
    public String execute(Machine machine) {
        machine.cont.save();
        machine.env.save();
        Combination comb = (Combination) machine.exp.getValue();
        machine.unev.setValue(comb.subList(1, comb.size()));
        machine.unev.save();
        machine.exp.setValue(comb.get(0));
        machine.cont.setValue("ev-appl-did-operator");
        return "eval-dispatch";
    }
}
