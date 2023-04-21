package weekfive;

public class SeventeenSeven {

    /**
	 * Write a program that merges two ordered-list objects of integers into a 
	 * single ordered-list object of integers. Method merge of class ListMerge 
	 * should receive references to each of the list objects to be merged and 
	 * return a reference to the merged list object.
	 */
    public static void main(String[] args) {
        List listA = new List();
        List listB = new List();
        listA.insertAtBack(1);
        listA.insertAtBack(3);
        listA.insertAtBack(5);
        listA.insertAtBack(7);
        listA.insertAtBack(9);
        listB.insertAtBack(2);
        listB.insertAtBack(4);
        listB.insertAtBack(6);
        listB.insertAtBack(8);
        System.out.println("First List: ");
        listA.print();
        System.out.println("Second List: ");
        listB.print();
        System.out.println("Merging Lists: ");
        ListMerge.merge(listA, listB);
        ListMerge.print();
    }
}
