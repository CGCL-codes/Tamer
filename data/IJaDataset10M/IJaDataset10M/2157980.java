package de.ios.kontor.sv.order.impl;

import java.rmi.*;
import java.math.*;
import de.ios.framework.db2.*;
import de.ios.framework.basic.*;
import de.ios.kontor.utils.*;
import de.ios.kontor.sv.order.co.*;
import de.ios.kontor.sv.basic.impl.*;

/**
 * Implements the PaymentTerms-Interface, encapsulating a PaymentTermsDBO-Object.
 *
 * @author js (Joachim Schaaf).
 * @version $Id: PaymentTermsImpl.java,v 1.1.1.1 2004/03/24 23:03:32 nanneb Exp $.
 */
public class PaymentTermsImpl extends BasicImpl implements PaymentTerms {

    /** The internal DBObject */
    protected PaymentTermsDBO dbo = null;

    /**
   * constructor
   *
   * @param _db The DBObject-Server.
   * @param _sc The Session-Carrier.
   *
   * @exception java.rmi.RemoteException if the connection to the server failed.
   */
    public PaymentTermsImpl(DBObjectServer _db, SessionCarrier _sc) throws RemoteException {
        super(_db, _sc, new PaymentTermsDBO());
    }

    /**
   * constructor
   *
   * @param _db The DBObject-Server.
   * @param _sc The Session-Carrier.
   * @param _dbo The internal DBObject.
   *
   * @exception java.rmi.RemoteException if the connection to the server failed.
   */
    public PaymentTermsImpl(DBObjectServer _db, SessionCarrier _sc, PaymentTermsDBO _dbo) throws RemoteException {
        super(_db, _sc, _dbo);
        dbo = _dbo;
    }

    /**
   * Return the PaymentTermsContollerImpl
   *
   * @return the PaymentTermsControllerImpl.
   *
   * @exception de.ios.framework.basic.KontorException if the Operation failed due to a Server error.
   */
    public BasicControllerImpl getBasicControllerImpl() throws KontorException {
        return getSessionImpl().getPaymentTermsControllerImpl();
    }

    /**
   * Modify a PaymentTerm with all attributes.
   *
   * @param _name The (short) name for this payment term.
   * @param _description The (longer) description for this payment term.
   * @param _period The period for this payment term.
   * @param _discount1 The name for the first discount.
   * @param _disc1period The period for the first discount.
   * @param _disc1perc The percental amount for the first discount.
   * @param _discount2 The name for the first discount.
   * @param _disc2period The period for the first discount.
   * @param _disc2perc The percental amount for the first discount.
   * @param _cashdiscount The percental amount for the cash discount.
   * @param _fixeddiscount The percental amount for the fixed discount.
   *
   * @exception java.rmi.RemoteException if the connection to the server failed.
   * @exception de.ios.framework.basic.KontorException if the Operation failed due to a Server error.
   *
   */
    public void modify(String _name, String _description, String _period, String _discount1, String _disc1period, BigDecimal _disc1perc, String _discount2, String _disc2period, BigDecimal _disc2perc, BigDecimal _cashdiscount, BigDecimal _fixeddiscount) throws java.rmi.RemoteException, KontorException {
        try {
            ((PaymentTermsDBO) dbo).setAll(_name, _description, _period, _discount1, _disc1period, _disc1perc, _discount2, _disc2period, _disc2perc, _cashdiscount, _fixeddiscount, true);
        } catch (Throwable t) {
            throw new KontorException("Modification of PaymentTerms-object failed!", t);
        }
    }

    /**
   * set the value of name
   *
   * @param value The (short) name for this payment term.
   */
    public void setName(String name) {
        dbo.name.set(name);
    }

    /**
   * get the value of name
   *
   * @return The (short) name for this payment term.
   */
    public String getName() {
        return dbo.name.get();
    }

    /**
   * set the value of description
   *
   * @param value The (longer) description for this payment term.
   */
    public void setDescription(String description) {
        dbo.description.set(description);
    }

    /**
   * get the value of description
   *
   * @return The (longer) description for this payment term.
   */
    public String getDescription() {
        return dbo.description.get();
    }

    /**
   * set the value of period
   *
   * @param value The period for this payment term.
   */
    public void setPeriod(String period) {
        dbo.period.set(period);
    }

    /**
   * get the value of period
   *
   * @return The period for this payment term.
   */
    public String getPeriod() {
        return dbo.period.get();
    }

    /**
   * set the value of discount1
   *
   * @param value The name for the first discount.
   */
    public void setDiscount1(String discount1) {
        dbo.discount1.set(discount1);
    }

    /**
   * get the value of discount1
   *
   * @return The name for the first discount.
   */
    public String getDiscount1() {
        return dbo.discount1.get();
    }

    /**
   * set the value of disc1period
   *
   * @param value The period for the first discount.
   */
    public void setDisc1period(String disc1period) {
        dbo.disc1period.set(disc1period);
    }

    /**
   * get the value of disc1period
   *
   * @return The period for the first discount.
   */
    public String getDisc1period() {
        return dbo.disc1period.get();
    }

    /**
   * set the value of disc1perc
   *
   * @param value The percental amount for the first discount.
   */
    public void setDisc1perc(BigDecimal disc1perc) {
        dbo.disc1perc.set(disc1perc);
    }

    /**
   * get the value of disc1perc
   *
   * @return The percental amount for the first discount.
   */
    public BigDecimal getDisc1perc() {
        return dbo.disc1perc.get();
    }

    /**
   * set the value of discount2
   *
   * @param value The name for the first discount.
   */
    public void setDiscount2(String discount2) {
        dbo.discount2.set(discount2);
    }

    /**
   * get the value of discount2
   *
   * @return The name for the first discount.
   */
    public String getDiscount2() {
        return dbo.discount2.get();
    }

    /**
   * set the value of disc2period
   *
   * @param value The period for the first discount.
   */
    public void setDisc2period(String disc2period) {
        dbo.disc2period.set(disc2period);
    }

    /**
   * get the value of disc2period
   *
   * @return The period for the first discount.
   */
    public String getDisc2period() {
        return dbo.disc2period.get();
    }

    /**
   * set the value of disc2perc
   *
   * @param value The percental amount for the first discount.
   */
    public void setDisc2perc(BigDecimal disc2perc) {
        dbo.disc2perc.set(disc2perc);
    }

    /**
   * get the value of disc2perc
   *
   * @return The percental amount for the first discount.
   */
    public BigDecimal getDisc2perc() {
        return dbo.disc2perc.get();
    }

    /**
   * set the value of cashdiscount
   *
   * @param value The percental amount for the cash discount.
   */
    public void setCashdiscount(BigDecimal cashdiscount) {
        dbo.cashdiscount.set(cashdiscount);
    }

    /**
   * get the value of cashdiscount
   *
   * @return The percental amount for the cash discount.
   */
    public BigDecimal getCashdiscount() {
        return dbo.cashdiscount.get();
    }

    /**
   * set the value of fixeddiscount
   *
   * @param value The percental amount for the fixed discount.
   */
    public void setFixeddiscount(BigDecimal fixeddiscount) {
        dbo.fixeddiscount.set(fixeddiscount);
    }

    /**
   * get the value of fixeddiscount
   *
   * @return The percental amount for the fixed discount.
   */
    public BigDecimal getFixeddiscount() {
        return dbo.fixeddiscount.get();
    }
}
