package net.cygeek.tech.client.data;

import net.cygeek.tech.client.HsHrEmpHistoryOfEalierPos;
import org.apache.torque.TorqueException;
import com.google.gwt.user.client.rpc.IsSerializable;
import java.util.Date;

/**
 * @author Thilina Hasantha
 */
public class EmpHistoryOfEalierPos implements IsSerializable {

    private String ehoepJobTitle;

    public String getEhoepJobTitle() {
        return ehoepJobTitle;
    }

    public void setEhoepJobTitle(String ehoepJobTitle) {
        this.ehoepJobTitle = ehoepJobTitle;
    }

    private Double empSeqno;

    public Double getEmpSeqno() {
        return empSeqno;
    }

    public void setEmpSeqno(Double empSeqno) {
        this.empSeqno = empSeqno;
    }

    private String ehoepYears;

    public String getEhoepYears() {
        return ehoepYears;
    }

    public void setEhoepYears(String ehoepYears) {
        this.ehoepYears = ehoepYears;
    }

    private int empNumber;

    public int getEmpNumber() {
        return empNumber;
    }

    public void setEmpNumber(int empNumber) {
        this.empNumber = empNumber;
    }

    public static EmpHistoryOfEalierPos getProxy(HsHrEmpHistoryOfEalierPos h) {
        if (h == null) return null;
        EmpHistoryOfEalierPos c = new EmpHistoryOfEalierPos();
        c.setEhoepJobTitle(h.getEhoepJobTitle());
        c.setEmpSeqno(h.getEmpSeqno().doubleValue());
        c.setEhoepYears(h.getEhoepYears());
        c.setEmpNumber(h.getEmpNumber());
        return c;
    }

    public static HsHrEmpHistoryOfEalierPos getClass(EmpHistoryOfEalierPos h) {
        HsHrEmpHistoryOfEalierPos c = new HsHrEmpHistoryOfEalierPos();
        try {
            c.setEhoepJobTitle(h.getEhoepJobTitle());
            c.setEmpSeqno(java.math.BigDecimal.valueOf(h.getEmpSeqno()));
            c.setEhoepYears(h.getEhoepYears());
            c.setEmpNumber(h.getEmpNumber());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c;
    }
}
