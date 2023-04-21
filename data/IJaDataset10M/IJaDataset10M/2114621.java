package weekfive;

public class Queue extends List {

    private List queueList;

    public Queue() {
        queueList = new List("queue");
    }

    public void enqueue(Object object) {
        queueList.insertAtBack(object);
    }

    public Object dequeue() throws EmptyListException {
        return queueList.removeFromFront();
    }

    public boolean isEmpty() {
        return queueList.isEmpty();
    }

    public void print() {
        queueList.print();
    }
}
