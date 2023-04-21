package interviewrelated;

import interviewrelated.DataStructures.MyBinaryTree;
import interviewrelated.DataStructures.MyLinearLinkedList;
import interviewrelated.DataStructures.Sort;

public class TestDataStructures {

    public static void main(String[] args) throws Exception {
        int array[] = { 5, 1, 2, 3, 4 };
        Sort sort = new DataStructures().new Sort();
        int[] result = sort.sort(array, DataStructures.SortAlgorithmType.QUICK);
        for (int i = 0; i < result.length; i++) {
            int j = result[i];
            log(j);
        }
    }

    private static void log(Object obj) {
        System.out.println(obj);
    }

    private static void testBinaryTree(int[] array) {
        MyBinaryTree binaryTree = new DataStructures().new MyBinaryTree();
        for (int i = 0; i < array.length; i++) {
            int j = array[i];
            binaryTree.insert(j);
        }
    }

    private static void testList(int[] array) {
        MyLinearLinkedList list = new DataStructures().new MyLinearLinkedList();
        for (int i = 0; i < array.length; i++) {
            int j = array[i];
            list.add(j);
        }
        list.insert("B", 4);
        System.out.println(list);
    }
}
