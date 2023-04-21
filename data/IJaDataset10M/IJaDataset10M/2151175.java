package block;

import system.RuleIMP;

public class BlockAllIn extends RuleIMP {

    private String rule = "iptables -P INPUT DROP";

    @Override
    public void applyRule() {
        try {
            Process p = Runtime.getRuntime().exec(rule);
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    @Override
    public String displayRule() {
        return this.rule;
    }
}
