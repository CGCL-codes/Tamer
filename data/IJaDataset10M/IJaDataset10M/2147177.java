package client.ws.milanas.gui.thread;

import client.ws.milanas.controller.Controller;
import client.ws.milanas.gui.MilanasWSClientGUI;
import client.ws.milanas.helpers.beans.Item;
import java.util.ArrayList;

/**
 *
 * @author milana
 */
public class SearchBooksThread implements Runnable {

    private Thread searchAction;

    private MilanasWSClientGUI gui;

    private Controller controller;

    private ArrayList<Item> items;

    private String author;

    private String title;

    private String keywords;

    private String condition;

    private String sort;

    /** Creates a new instance of SearchBooksThread */
    public SearchBooksThread(MilanasWSClientGUI gui, Controller controller) {
        searchAction = new Thread(this);
        this.gui = gui;
        this.controller = controller;
        items = new ArrayList<Item>();
    }

    public void startThread(String author, String title, String keywords, String condition, String sort) {
        this.author = author;
        this.title = title;
        this.keywords = keywords;
        this.condition = condition;
        this.sort = sort;
        searchAction.start();
    }

    public void run() {
        items = controller.searchBooks(author, title, keywords, condition, sort);
        gui.updateSearchBookTab(searchAction.getId());
    }

    public boolean isAliveThread() {
        return searchAction.isAlive();
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public long getId() {
        return searchAction.getId();
    }
}
