package phonetalks.entities.collections;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.TreeMap;
import phonetalks.entities.calls.PhoneCall;
import phonetalks.entities.calls.SubscriberCallPair;
import phonetalks.entities.subscriber.Subscriber;
import phonetalks.entities.subscriber.SubscriberEntry;
import phonetalks.exceptions.InvalidNumberException;
import phonetalks.exceptions.UnexistingElementException;
import phonetalks.logic.PhoneChecker;

/**
 *Collection for SubscriberEntry
 * Картотека абонентов на междугородной телефонной станции. Каждая запись содержит:
•	номер телефона,
•	его владельца (фамилия, имя, отчество),
•	общее (суммарное) временя разговоров,
•	«историю» звонков абонента (т.е. список, в котором хранятся номера, на которые
 * были сделаны звонки с указанием  даты/времени и длительности разговоров;
 * количество записей в этом списке не ограничено); упорядочен по номеру телефона,
 * для одинаковых номеров – по дате и времени;
 *Картотека упорядочена по фамилиям владельцев. Учесть, что прописные и строчные
 * буквы в данных владельца не различаются. Добавить владельца или разговор.
 * Удалить владельца или разговор. Поиск владельца, всех звонков на указанный телефон.

 * @author Платон
 */
public class CardIndex extends TreeMap<Subscriber, PriorityQueue<SubscriberEntry>> implements Serializable {

    private static final long serialVersionUID = 1;

    protected HashMap<String, SubscriberEntry> numberOwner = new HashMap<String, SubscriberEntry>();

    public void addEntry(SubscriberEntry entry) {
        if (findByNumber(entry.getPhoneNumber()) != null) {
            return;
        }
        Subscriber subscriber = entry.getSubscriber();
        PriorityQueue<SubscriberEntry> col;
        if (this.containsKey(subscriber)) {
            col = this.get(subscriber);
        } else {
            col = new PriorityQueue<SubscriberEntry>();
        }
        col.add(entry);
        numberOwner.put(entry.getPhoneNumber(), entry);
        put(subscriber, col);
    }

    public boolean removeEntry(SubscriberEntry entry) {
        Subscriber subscriber = entry.getSubscriber();
        PriorityQueue<SubscriberEntry> col = get(subscriber);
        numberOwner.remove(entry);
        return col.remove(entry);
    }

    public SubscriberEntry addCall(String number, PhoneCall call) {
        SubscriberEntry entry = findByNumber(number);
        if (entry == null) {
            throw new UnexistingElementException("Cant add call to unexisting entry.");
        }
        entry.addCall(call);
        return entry;
    }

    public SubscriberEntry removeCall(String number, PhoneCall call) {
        SubscriberEntry entry = findByNumber(number);
        if (entry == null) {
            throw new UnexistingElementException("Cant remove from unexisting entry.");
        }
        entry.removeCall(call);
        return entry;
    }

    public void addSubscriber(Subscriber subscriber, String number) {
        SubscriberEntry entry = new SubscriberEntry(subscriber, number);
        addEntry(entry);
    }

    public boolean removeSubscriber(String number) {
        Subscriber subscriber = findByNumber(number).getSubscriber();
        return remove(subscriber) != null;
    }

    public SubscriberEntry findByNumber(String number) {
        return numberOwner.get(number);
    }

    public ArrayList<SubscriberCallPair> getAllCalls(String number) {
        if (!PhoneChecker.isNumberCorrect(number)) {
            throw new InvalidNumberException("Check number you've entered!");
        }
        ArrayList<SubscriberCallPair> result = new ArrayList<SubscriberCallPair>();
        for (PriorityQueue<SubscriberEntry> q : values()) {
            for (SubscriberEntry e : q) {
                for (PhoneCall call : e) {
                    if (call.getPhoneNumber().equalsIgnoreCase(number)) {
                        Subscriber s = e.getSubscriber();
                        result.add(new SubscriberCallPair(s, call));
                    }
                }
            }
        }
        return result;
    }
}
