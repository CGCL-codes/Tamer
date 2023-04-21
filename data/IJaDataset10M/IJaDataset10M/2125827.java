package net.aarestad.javahack;

class EShk {

    static final int REPAIR_DELAY = 5;

    static final int BILLSZ = 200;

    class Billx {

        private int bo_id;

        private boolean useup;

        private long price;

        private long bquan;

        Billx() {
        }

        int getBo_id() {
            return bo_id;
        }

        boolean isUseup() {
            return useup;
        }

        long getPrice() {
            return price;
        }

        long getBquan() {
            return bquan;
        }

        void setBo_id(int i) {
            bo_id = i;
        }

        void setUseup(boolean b) {
            useup = b;
        }

        void setPrice(long l) {
            price = l;
        }

        void setBquan(long l) {
            bquan = l;
        }
    }

    private long robbed;

    private long credit;

    private long debit;

    private long loan;

    private int shoptype;

    private byte shoproom;

    private byte following;

    private boolean surcharge;

    private Coord shk;

    private Coord shd;

    private Dlevel shoplevel;

    private int billct;

    private Billx[] bill = new Billx[BILLSZ];

    private Billx bill_p;

    private int visitct;

    private String customer;

    private String shknam;

    static boolean NOTANGRY(Monst mon) {
        return mon.isMpeaceful();
    }

    static boolean ANGRY(Monst mon) {
        return !NOTANGRY(mon);
    }

    /**
	 * Gets the robbed.
	 * @return Returns a long
	 */
    public long getRobbed() {
        return robbed;
    }

    /**
	 * Sets the robbed.
	 * @param robbed The robbed to set
	 */
    public void setRobbed(long robbed) {
        this.robbed = robbed;
    }

    /**
	 * Gets the credit.
	 * @return Returns a long
	 */
    public long getCredit() {
        return credit;
    }

    /**
	 * Sets the credit.
	 * @param credit The credit to set
	 */
    public void setCredit(long credit) {
        this.credit = credit;
    }

    /**
	 * Gets the debit.
	 * @return Returns a long
	 */
    public long getDebit() {
        return debit;
    }

    /**
	 * Sets the debit.
	 * @param debit The debit to set
	 */
    public void setDebit(long debit) {
        this.debit = debit;
    }

    /**
	 * Gets the loan.
	 * @return Returns a long
	 */
    public long getLoan() {
        return loan;
    }

    /**
	 * Sets the loan.
	 * @param loan The loan to set
	 */
    public void setLoan(long loan) {
        this.loan = loan;
    }

    /**
	 * Gets the shoptype.
	 * @return Returns a int
	 */
    public int getShoptype() {
        return shoptype;
    }

    /**
	 * Sets the shoptype.
	 * @param shoptype The shoptype to set
	 */
    public void setShoptype(int shoptype) {
        this.shoptype = shoptype;
    }

    /**
	 * Gets the shoproom.
	 * @return Returns a byte
	 */
    public byte getShoproom() {
        return shoproom;
    }

    /**
	 * Sets the shoproom.
	 * @param shoproom The shoproom to set
	 */
    public void setShoproom(byte shoproom) {
        this.shoproom = shoproom;
    }

    /**
	 * Gets the following.
	 * @return Returns a byte
	 */
    public byte getFollowing() {
        return following;
    }

    /**
	 * Sets the following.
	 * @param following The following to set
	 */
    public void setFollowing(byte following) {
        this.following = following;
    }

    /**
	 * Gets the surcharge.
	 * @return Returns a boolean
	 */
    public boolean getSurcharge() {
        return surcharge;
    }

    /**
	 * Sets the surcharge.
	 * @param surcharge The surcharge to set
	 */
    public void setSurcharge(boolean surcharge) {
        this.surcharge = surcharge;
    }

    /**
	 * Gets the shk.
	 * @return Returns a Coord
	 */
    public Coord getShk() {
        return shk;
    }

    /**
	 * Sets the shk.
	 * @param shk The shk to set
	 */
    public void setShk(Coord shk) {
        this.shk = shk;
    }

    /**
	 * Gets the shd.
	 * @return Returns a Coord
	 */
    public Coord getShd() {
        return shd;
    }

    /**
	 * Sets the shd.
	 * @param shd The shd to set
	 */
    public void setShd(Coord shd) {
        this.shd = shd;
    }

    /**
	 * Gets the shoplevel.
	 * @return Returns a Dlevel
	 */
    public Dlevel getShoplevel() {
        return shoplevel;
    }

    /**
	 * Sets the shoplevel.
	 * @param shoplevel The shoplevel to set
	 */
    public void setShoplevel(Dlevel shoplevel) {
        this.shoplevel = shoplevel;
    }

    /**
	 * Gets the billct.
	 * @return Returns a int
	 */
    public int getBillct() {
        return billct;
    }

    /**
	 * Sets the billct.
	 * @param billct The billct to set
	 */
    public void setBillct(int billct) {
        this.billct = billct;
    }

    /**
	 * Gets the bill.
	 * @return Returns a Billx[]
	 */
    public Billx[] getBill() {
        return bill;
    }

    /**
	 * Sets the bill.
	 * @param bill The bill to set
	 */
    public void setBill(Billx[] bill) {
        this.bill = bill;
    }

    /**
	 * Gets the bill_p.
	 * @return Returns a Billx
	 */
    public Billx getBill_p() {
        return bill_p;
    }

    /**
	 * Sets the bill_p.
	 * @param bill_p The bill_p to set
	 */
    public void setBill_p(Billx bill_p) {
        this.bill_p = bill_p;
    }

    /**
	 * Gets the visitct.
	 * @return Returns a int
	 */
    public int getVisitct() {
        return visitct;
    }

    /**
	 * Sets the visitct.
	 * @param visitct The visitct to set
	 */
    public void setVisitct(int visitct) {
        this.visitct = visitct;
    }

    /**
	 * Gets the customer.
	 * @return Returns a String
	 */
    public String getCustomer() {
        return customer;
    }

    /**
	 * Sets the customer.
	 * @param customer The customer to set
	 */
    public void setCustomer(String customer) {
        this.customer = customer;
    }

    /**
	 * Gets the shknam.
	 * @return Returns a String
	 */
    public String getShknam() {
        return shknam;
    }

    /**
	 * Sets the shknam.
	 * @param shknam The shknam to set
	 */
    public void setShknam(String shknam) {
        this.shknam = shknam;
    }
}
