package fr.umlv.j2ee.patterns.transfertobject;

import javax.naming.InitialContext;

public class Client {

    public static void main(String[] args) throws Exception {
        InitialContext ctx = new InitialContext();
        BusinessObject business = (BusinessObject) ctx.lookup("BusinessObjectEJB/remote");
        Account ac1 = new Account("firstName", "lastName", "plop@plop.com");
        Account ac2 = new Account("tim", "burton", "tim.burton@universal.com");
        business.addAccount(ac1);
        business.addAccount(ac2);
        System.out.println("accounts list " + business.list());
        System.out.println("account " + business.getAccountData("plop@plop.com"));
    }
}
