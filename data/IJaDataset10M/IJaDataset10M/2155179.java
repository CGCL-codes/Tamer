package listElement;

/**
 *
 * @author Boukry Omar
 */
public class TestLinkedList {

    public static void main(String[] args) {
        LinkedList<Personne> test1 = new LinkedList();
        Personne omar = new Personne("Boukry", "Omar", 22);
        Personne aliona = new Personne("Efimovskaya", "Aliona", 20);
        Personne kamal = new Personne("Boukry", "Kamal", 29);
        Personne meriem = new Personne("Boukry", "Meriem", 27);
        Personne marc = new Personne("Boisban", "Marc", 24);
        Personne ion = new Personne("Gorodenco", "Ion", 24);
        Personne gicu = new Personne("Gorodenco", "Gicu", 26);
        test1.add(omar);
        test1.add(aliona);
        test1.add(kamal);
        test1.add(ion);
        test1.add(marc);
        test1.add(meriem);
        test1.add(gicu);
        test1.add(omar);
        test1.add(ion);
        System.out.println("add(E element)\n" + test1.toString() + "\n");
        test1.remove(8);
        System.out.println("remove(int index)\n" + test1.toString() + "\n");
        test1.remove(omar);
        System.out.println("remove(Object o)\n" + test1.toString() + "\n");
        test1.add(5, aliona);
        System.out.println("add(int index, E element)\n" + test1.toString() + "\n");
        System.out.println("get(int index)\n" + test1.get(3) + "\n");
        test1.set(7, marc);
        System.out.println("set(int index, E element)\n" + test1.toString() + "\n");
        System.out.println("size()\n" + test1.size() + "\n");
        System.out.println("contains(Object o)\n" + (test1.contains(meriem) ? "meriem is in the LinkedList" : "meriem is not in the LinkedList") + "\n");
        System.out.println("indexOf(Object o)\n" + test1.indexOf(kamal) + "\n");
        System.out.println("subList(int fromIndex, int toIndex)\n" + test1.subList(2, test1.size()).toString() + "\n");
        System.out.println("subList(int fromIndex, int toIndex)\n" + test1.subList(30, 20).toString() + "\n");
        test1.clear();
        System.out.println("clear()\n" + test1.toString() + "\n");
        System.out.println("End of run");
    }
}
