package panda.query.test;

import panda.server.Panda;
import panda.transaction.Transaction;

public class SQLDeleteTest {

    public static void main(String args[]) {
        Panda.init("F:\\Data");
        Transaction tx = new Transaction();
        Panda.getPlanner().executeUpdate("UPDATE table1 SET a = a - 5000 WHERE a > 5000", tx);
        tx.commit();
    }
}
