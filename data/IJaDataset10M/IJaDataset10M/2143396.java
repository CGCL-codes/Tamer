package com.karpouzas.rbst.lib.st;

import com.karpouzas.rbst.lib.bookstore.BookInfo;
import com.karpouzas.rbst.lib.bookstore.BookInfoList;
import com.karpouzas.rbst.lib.bookstore.InvalidISBNException;
import com.karpouzas.rbst.lib.bst.BinarySearchTree;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.List;

/**
 * iST implementation
 * @author George Karpouzas <gkarpouzas@webnetsoft.gr>
 */
public class ST extends BinarySearchTree implements iST {

    /**
     * insert new warehouse
     * @param nodeid
     * @param name
     */
    public void insertWarehouse(int nodeid, String name) {
        TreeNode node = this.find(nodeid);
        if (node == null) this.insert(nodeid, name); else System.err.println("This Warehouse (" + nodeid + ") already exists.");
    }

    /**
     * insert book at warehouse list
     * @param nodeid
     * @param isbn
     * @param copies
     */
    public void insertBookAtWarehouse(int nodeid, int isbn, int copies) {
        TreeNode node = this.find(nodeid);
        if (node != null) {
            try {
                node.insert(new BookInfo(isbn, copies));
            } catch (InvalidISBNException ex) {
                System.err.println(ex.toString());
            }
        } else System.err.println("This Warehouse (" + nodeid + ") does not exist.");
    }

    /**
     * remove warehouse
     * @param nodeid
     */
    public void removeWarehouse(int nodeid) {
        if (this.remove(nodeid)) System.out.println("Warehouse " + nodeid + " has been removed."); else System.out.println("Warehouse " + nodeid + " not found.");
    }

    /**
     * remove book from warehouse
     * @param nodeid
     * @param isbn
     */
    public void removeBook(int nodeid, int isbn) {
        TreeNode node = this.find(nodeid);
        if (node != null) {
            try {
                boolean removed = node.remove(new BookInfo(isbn));
                if (!removed) System.err.println("This book (" + isbn + ") does not exist.");
            } catch (InvalidISBNException ex) {
                System.err.println(ex.toString());
            }
        } else System.err.println("This Warehouse (" + nodeid + ") does not exist.");
    }

    /**
     * search for warehouse
     * @param nodeid
     */
    public void searchByWarehouse(int nodeid) {
        try {
            TreeNode node = this.find(nodeid);
            if (node != null) {
                BookInfoList booklist = node.getBookList();
                System.out.println("Warehouse " + nodeid + " located in " + node.city + ": ");
                for (Iterator i = booklist.getIterator(); i.hasNext(); ) {
                    BookInfo binfo = (BookInfo) i.next();
                    System.out.println("Book " + binfo.getISBN() + ", copies: " + binfo.getCopies());
                }
            } else System.err.println("This Warehouse (" + nodeid + ") does not exist.");
        } catch (Exception ex) {
            System.err.println(ex.toString());
        }
    }

    /**
     * find all city's warehouses
     * @param cityname
     */
    public void searchByCity(String cityname) {
        TreeNode node = this.findRecursive(cityname);
        if (node != null) {
            BookInfoList booklist = node.getBookList();
            System.out.println("Warehouse " + node.id + " located in " + node.city + ": ");
            for (Iterator i = booklist.getIterator(); i.hasNext(); ) {
                BookInfo binfo = (BookInfo) i.next();
                System.out.println("Book " + binfo.getISBN() + ", copies: " + binfo.getCopies());
            }
        } else System.err.println("Cannot find city (" + cityname + ").");
    }

    /**
     * search for a book in warehouse book list
     * @param nodeid
     * @param isbn
     */
    public void searchBookInWarehouse(int nodeid, int isbn) {
        TreeNode node = this.find(nodeid);
        if (node != null) {
            try {
                BookInfo book = node.search(isbn);
                if (book != null) System.out.println("Book " + isbn + ", copies: " + book.getCopies()); else {
                    if (SearchAllWarehouses(nodeid)) {
                        List<Integer> nodeids = findBooksRecursive(isbn);
                        for (int nodid : nodeids) {
                            node = this.find(nodeid);
                            if (node != null) {
                                book = node.search(isbn);
                                if (book != null) {
                                    System.out.println("The book is available at");
                                    System.out.println("Warehouse " + nodid + " located in " + node.city + ", copies: " + book.getCopies());
                                }
                            }
                        }
                    }
                }
            } catch (InvalidISBNException ex) {
                System.err.println(ex.toString());
            }
        } else System.err.println("This Warehouse (" + nodeid + ") does not exist.");
    }

    private boolean SearchAllWarehouses(int nodeid) {
        boolean searchinallwarehouses = false;
        System.out.print("Warehouse " + nodeid + " does not have this book.");
        System.out.println("Do you want to search in other warehouses (yes/no)?: ");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line = null;
        try {
            line = br.readLine();
        } catch (IOException e) {
            System.exit(1);
        }
        if (line.compareTo("yes") == 0) searchinallwarehouses = true; else searchinallwarehouses = false;
        return searchinallwarehouses;
    }

    /**
     * print tree (debugging)
     * @param stream
     */
    public void printΤree(PrintStream stream) {
        printRecursive(head, stream);
    }
}
