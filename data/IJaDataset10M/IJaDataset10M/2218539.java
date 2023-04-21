package shared.TypyDanych;

import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Praca
 */
public class DefinicjaRealizacji {

    String OrderID;

    String RealizationID;

    Date OrderRealizationDate;

    Identyfikator PersonId;

    ArrayList<DefinicjaPozycjiRealizacjiZamowieniaLeku> Positions;

    Integer OrderStatus;

    String OrderStatusDocumentNo;

    String Comment;

    public DefinicjaRealizacji() {
    }

    public DefinicjaRealizacji(String OrderID, String RealizationID, Date OrderRealizationDate, Identyfikator PersonId, ArrayList<DefinicjaPozycjiRealizacjiZamowieniaLeku> Positions, Integer OrderStatus, String OrderStatusDocumentNo, String Comment) {
        this.OrderID = OrderID;
        this.RealizationID = RealizationID;
        this.OrderRealizationDate = OrderRealizationDate;
        this.PersonId = PersonId;
        this.Positions = Positions;
        this.OrderStatus = OrderStatus;
        this.OrderStatusDocumentNo = OrderStatusDocumentNo;
        this.Comment = Comment;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String Comment) {
        this.Comment = Comment;
    }

    public String getOrderID() {
        return OrderID;
    }

    public void setOrderID(String OrderID) {
        this.OrderID = OrderID;
    }

    public Date getOrderRealizationDate() {
        return OrderRealizationDate;
    }

    public void setOrderRealizationDate(Date OrderRealizationDate) {
        this.OrderRealizationDate = OrderRealizationDate;
    }

    public Integer getOrderStatus() {
        return OrderStatus;
    }

    public void setOrderStatus(Integer OrderStatus) {
        this.OrderStatus = OrderStatus;
    }

    public String getOrderStatusDocumentNo() {
        return OrderStatusDocumentNo;
    }

    public void setOrderStatusDocumentNo(String OrderStatusDocumentNo) {
        this.OrderStatusDocumentNo = OrderStatusDocumentNo;
    }

    public Identyfikator getPersonId() {
        return PersonId;
    }

    public void setPersonId(Identyfikator PersonId) {
        this.PersonId = PersonId;
    }

    public ArrayList<DefinicjaPozycjiRealizacjiZamowieniaLeku> getPositions() {
        return Positions;
    }

    public void setPositions(ArrayList<DefinicjaPozycjiRealizacjiZamowieniaLeku> Positions) {
        this.Positions = Positions;
    }

    public String getRealizationID() {
        return RealizationID;
    }

    public void setRealizationID(String RealizationID) {
        this.RealizationID = RealizationID;
    }

    @Override
    public String toString() {
        return "DefinicjaRealizacji{" + "OrderID=" + OrderID + "RealizationID=" + RealizationID + "OrderRealizationDate=" + OrderRealizationDate + "PersonId=" + PersonId + "Positions=" + Positions + "OrderStatus=" + OrderStatus + "OrderStatusDocumentNo=" + OrderStatusDocumentNo + "Comment=" + Comment + '}';
    }
}
