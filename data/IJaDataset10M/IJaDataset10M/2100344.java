package system;

public class DeleteChains extends system.RuleIMP {

    String name;

    public DeleteChains(String aName) {
        super();
        name = aName;
    }

    @Override
    public void applyRule() {
        String flush = "iptables -X " + this.getName();
        try {
            Process p = Runtime.getRuntime().exec(flush);
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    @Override
    public String displayRule() {
        String chain = "iptables -X " + this.getName();
        return chain;
    }

    private String getName() {
        return this.name;
    }
}
