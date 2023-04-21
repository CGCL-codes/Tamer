package se.marianna.simpleDB.test;

import java.util.Random;
import se.marianna.simpleDB.BasicTableSort;
import se.marianna.simpleDB.MissingColumnName;
import se.marianna.simpleDB.On;
import se.marianna.simpleDB.Relation;
import se.marianna.simpleDB.BaseValues.LongValue;
import se.marianna.simpleDB.BaseValues.StringValue;
import se.marianna.simpleDB.MiscValues.InternationalPhoneNumberValue;

public class DummyTest {

    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();
        RecordStoreTableTestImplementation inMessages = new RecordStoreTableTestImplementation("inMessages", new Object[] { "from", StringValue.instance, "message", StringValue.instance, "timesent", LongValue.instance, "timearived", LongValue.instance });
        RecordStoreTableTestImplementation contacts = new RecordStoreTableTestImplementation("contacts", new Object[] { "phonenumber", InternationalPhoneNumberValue.instance, "name", StringValue.instance });
        for (int i = 0; i < 25; i++) {
            contacts.insert(new Object[] { "phonenumber", "070458447" + i, "name", "Bjorn" + i });
        }
        Random rand = new Random();
        for (int i = 0; i < 13000; i++) {
            int number = (int) (rand.nextDouble() * 10);
            inMessages.insert(new Object[] { "from", "070458447" + number, "message", i + " this is a test message " + number, "timesent", new Long(System.currentTimeMillis()), "timearived", new Long(System.currentTimeMillis()) });
        }
        long creationDone = System.currentTimeMillis();
        Relation result = contacts.join(inMessages, new On() {

            public boolean join(int table1Row, Relation table1, int table2Row, Relation table2) throws MissingColumnName {
                return table1.getString(table1Row, "phonenumber").equals(table2.getString(table2Row, "from"));
            }
        });
        long joinDone = System.currentTimeMillis();
        result.sort(new BasicTableSort("from"));
        long done = System.currentTimeMillis();
        long doneIn = done - start;
        long creationDoneIn = creationDone - start;
        long joinDonein = joinDone - creationDone;
        long sortDonein = done - joinDone;
        System.out.println("Done in milisec: " + doneIn);
        System.out.println("creation milisec: " + creationDoneIn);
        System.out.println("join milisec: " + (joinDonein));
        System.out.println("sort milisec: " + (sortDonein));
    }
}
