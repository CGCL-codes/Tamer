package dscript;

public class VCWrapper {

    private VarContainer vc;

    private String name;

    public VCWrapper(VarContainer v) {
        vc = v;
        if (v == null) {
            name = "VarC!";
        } else {
            name = "VarC!" + vc.getReference();
        }
    }

    public String asString() {
        return "A VarContainer with " + vc.in().length + " variables..";
    }

    public String getName() {
        return name;
    }

    public void dump() {
        vc.getOutput().println("Contents of " + name);
        vc.dump();
    }

    public boolean is_in(String vname) {
        try {
            return vc.is_in(vname);
        } catch (Exception ez) {
            if (StatementProcessor.DEBUG) {
                vc.getOutput().println(ez.toString(), 2);
            }
        }
        return false;
    }

    public VarContainer getVarContainer() {
        return vc;
    }

    public Var get(String vname) {
        return vc.get(vname);
    }

    public void add(Var v) {
        vc.add(v);
    }

    public void replace(String name, Var v) {
        vc.replace(name, v);
    }

    public void remove(String name) {
        vc.remove(name);
    }

    public static VCWrapper copy(VCWrapper vcw) {
        Var[] vrs = vcw.getVarContainer().in();
        VarContainer vc = new VarContainer(vcw.getVarContainer().getOutput());
        for (int i = 0; i < vrs.length; i++) {
            vc.add(Var.copy(vrs[i]));
        }
        return new VCWrapper(vc);
    }
}
