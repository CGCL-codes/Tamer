package net.sourceforge.offsprings.test;

import java.util.ArrayList;
import java.util.List;
import net.sourceforge.offsprings.examples.Document;

public class OutOfOrderServiceImpl implements OutOfOrderService {

    private Document extractDocument(String id) {
        int idNumber = 0;
        try {
            idNumber = Integer.parseInt(id);
        } catch (Throwable e) {
            System.out.println("Service Expects Integer Document ID");
            throw new RuntimeException(e);
        }
        try {
            int delayValue = idNumber / 20;
            Thread.sleep(delayValues[delayValue]);
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return new Document(id);
    }

    public List<Document> getDocListFromIdList(List<String> ids) {
        List<Document> answer = new ArrayList<Document>();
        for (String id : ids) {
            answer.add(extractDocument(id));
        }
        return answer;
    }

    public int[] delayValues = { 100, 5, 300, 50, 150, 75, 200, 25, 125, 250, 175 };
}
