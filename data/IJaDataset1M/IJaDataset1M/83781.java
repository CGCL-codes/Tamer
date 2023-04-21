package pelore.dao;

import java.util.Date;

public class ItemLoanPK {

    private ItemPK itemPK;

    private String personPK;

    private Date loanDate;

    public ItemLoanPK(ItemPK item, String personPK, Date loanDate) {
        setItemPK(item);
        setPersonPK(personPK);
        setLoanDate(loanDate);
    }

    /**
	 * @return the itemPK
	 */
    public ItemPK getItemPK() {
        return itemPK;
    }

    /**
	 * @param itemPK the itemPK to set
	 */
    public void setItemPK(ItemPK itemPK) {
        this.itemPK = itemPK;
    }

    /**
	 * @return the personPK
	 */
    public String getPersonPK() {
        return personPK;
    }

    /**
	 * @param personPK the personPK to set
	 */
    public void setPersonPK(String personPK) {
        this.personPK = personPK;
    }

    /**
	 * @return the loanDate
	 */
    public Date getLoanDate() {
        return loanDate;
    }

    /**
	 * @param loanDate the loanDate to set
	 */
    public void setLoanDate(Date loanDate) {
        this.loanDate = loanDate;
    }

    public boolean equals(Object obj) {
        if (obj instanceof ItemLoanPK) {
            ItemLoanPK other = (ItemLoanPK) obj;
            return other.getPersonPK().equals(this.getPersonPK()) && other.getLoanDate().equals(this.getLoanDate()) && other.getItemPK().equals(this.getItemPK());
        }
        return false;
    }

    public String toString() {
        return getPersonPK() + ", " + getItemPK() + ", " + getLoanDate();
    }
}
