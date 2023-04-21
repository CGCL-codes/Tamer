package panda.storage.test;

import panda.query.scan.UpdatableScanner;
import panda.query.struct.Attribute;
import panda.query.struct.IntConstant;
import panda.query.tree.TableNode;
import panda.server.Panda;
import panda.transaction.Transaction;

public class ReadContentTest {

    public static void main(String[] args) {
        Panda.init("g:\\DataTemp");
        Transaction tx = new Transaction();
        TableNode tn = new TableNode("table1", tx);
        UpdatableScanner s = (UpdatableScanner) tn.open();
        long c = System.currentTimeMillis();
        Attribute a = tn.getSchema().getAttributeByName("col1");
        Attribute b = tn.getSchema().getAttributeByName("col2");
        int count = 0;
        while (s.next()) {
            count++;
            System.out.println(s.getValue(a).getContentValue());
            System.out.println(s.getValue(b).getContentValue());
            s.getValue(a).getContentValue();
            s.getValue(b).getContentValue();
        }
        System.out.println(System.currentTimeMillis() - c);
        System.out.println("count=" + count);
        s.close();
        tx.commit();
    }
}
