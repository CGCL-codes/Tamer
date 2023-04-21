package drcl.comp;

import drcl.comp.Contract;

/**
 * The class which wraps multiple contracts into one.
 */
public class ContractMultiple extends Contract {

    public ContractMultiple(Contract c1_, Contract c2_) {
        this(new Contract[] { c1_, c2_ });
    }

    public ContractMultiple(Contract c1_, Contract c2_, Contract c3_) {
        this(new Contract[] { c1_, c2_, c3_ });
    }

    public ContractMultiple(Contract c1_, Contract c2_, Contract c3_, Contract c4_) {
        this(new Contract[] { c1_, c2_, c3_, c4_ });
    }

    public ContractMultiple(Contract[] cc_) {
        add(cc_);
    }

    Contract[] cc;

    public void add(Contract c_) {
        if (cc == null) cc = new Contract[] { c_ }; else expand(new Contract[] { c_ });
    }

    public void add(Contract[] cc_) {
        if (cc == null) cc = cc_; else {
            expand(cc_);
        }
    }

    void expand(Contract[] cc_) {
        if (cc == null) {
            cc = cc_;
            return;
        }
        Contract[] new_ = new Contract[cc.length + cc_.length];
        System.arraycopy(cc, 0, new_, 0, cc.length);
        System.arraycopy(cc_, 0, new_, cc.length, cc_.length);
        cc = new_;
    }

    /**
   * Returns true if this contract matches <code>that_</code>.
   */
    public boolean match(Contract that_) {
        if (cc == null || cc.length == 0) return true;
        for (int i = 0; i < cc.length; i++) if (cc[i].match(that_)) return true;
        return false;
    }

    public Object getContractContent() {
        if (cc == null || cc.length == 0) return "";
        Object[] oo_ = new Object[cc.length];
        for (int i = 0; i < cc.length; i++) oo_[i] = cc[i].getContractContent();
        return oo_;
    }

    public String getName() {
        if (cc == null || cc.length == 0) return "N/A";
        StringBuffer sb_ = new StringBuffer();
        sb_.append(cc[0].getName());
        for (int i = 1; i < cc.length; i++) sb_.append(":" + cc[i].getName());
        return sb_.toString();
    }
}
