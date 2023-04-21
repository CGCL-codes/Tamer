package uebung04.ml.aufgabe05;

import uebung04.ml.aufgabe04.Queue;
import uebung04.ml.aufgabe04.QueueImpl;
import uebung04.ml.aufgabe04.Stack;
import uebung04.ml.aufgabe04.StackImpl;

public class Permutationen<T> {

    public void permutationen(Queue<T> q, Stack<T> s) {
        if (q.isEmpty()) {
            System.out.print("Permutation : ");
            s.print();
            System.out.print("\n");
        } else {
            for (int i = 1; i <= q.size(); i++) {
                s.push(q.dequeue());
                permutationen(q, s);
                q.enqueue(s.pop());
            }
        }
    }

    public static void main(String[] args) {
        Permutationen<String> perm = new Permutationen<String>();
        QueueImpl<String> q = new QueueImpl<String>(String.class);
        q.tracing(false);
        q.enqueue("a");
        q.enqueue("b");
        q.enqueue("c");
        q.enqueue("d");
        StackImpl<String> s = new StackImpl<String>();
        perm.permutationen(q, s);
    }
}
