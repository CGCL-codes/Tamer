package aoetec.javalang._411collections;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import aoetec.util.lession.Lesson;

@Lesson(title = "Queue<E> 接口", lastModifed = "2008/02/25", keyword = { "在处理元素前用于保存元素的 collection", "插入、移除、检查" }, content = { "1 Queue<E> 接口 的定义", "   public interface Queue<E> extends Collection<E> {   ", "       E element();                                    ", "       boolean offer(E e);                             ", "       E peek();                                       ", "       E poll();                                       ", "       E remove();                                     ", "   }                                                   ", "                                                                                         ", "2 Queue<E> 接口 的结构", "   +----------+--------------------+---------------------------+   ", "   |          |  Throws exception  |  Returns special value    |   ", "   +----------+--------------------+---------------------------+   ", "   |Insert    |add(e)              |offer(e)                   |   ", "   +----------+--------------------+---------------------------+   ", "   |Remove    |remove()            |poll()                     |   ", "   +----------+--------------------+---------------------------+   ", "   |Examine   |element()           |peek()                     |   ", "   +----------+--------------------+---------------------------+   ", "                                                                                         ", "3 JDK 里提供的 Queue<E> 接口 的实现", "                          java.util.Queue                             ", "             ____________________*____________________                ", "             |                                       |                ", "   java.util.LinkedList                    java.util.PriorityQueue    ", "   （基本实现 先进先出）                    （按照元素的自然顺序排序）   ", "" })
public class A04_Queue {

    public static void main(String[] args) {
        differenceAmongImpls();
    }

    static void basicOperations() {
        Queue<Integer> queue = new LinkedList<Integer>();
        queue.add(5);
        queue.add(3);
        queue.add(7);
        while (!queue.isEmpty()) {
            System.out.print(queue.element());
            System.out.println(" " + queue.remove());
        }
    }

    static void differenceAmongImpls() {
        Queue<Integer> lQueue = new LinkedList<Integer>();
        lQueue.add(5);
        lQueue.add(3);
        lQueue.add(7);
        System.out.println("lQueue=" + lQueue);
        Queue<Integer> pQueue = new PriorityQueue<Integer>();
        pQueue.add(5);
        pQueue.add(3);
        pQueue.add(7);
        System.out.println("pQueue=" + pQueue);
    }
}
