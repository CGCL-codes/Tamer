package com.oozinoz.credit.ca;

import com.oozinoz.credit.CreditCheck;
import com.oozinoz.utility.Dollars;

/**
 * Objects of this class check credit by dialing out to a (Canadian) credit
 * service bureau.
 */
public class CreditCheckCanadaOnline implements CreditCheck {

    /**
     * @param id a customer's ID number
     * @return the acceptable credit limit for the person with the supplied
     *         identification number.
     */
    public Dollars creditLimit(int id) {
        return new Dollars(0);
    }
}
