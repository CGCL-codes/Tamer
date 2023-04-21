package interfaces;

import java.util.Date;

/**
 * @author kreed
 *
 */
public interface IShoe {

    public void setComment(String s);

    public String getComment();

    public Date getDateOfPurchase();

    public void setDateOfPurchase(Date d);

    public void changeName(String neuerName);

    public void setName(String s);

    public String getName();
}
