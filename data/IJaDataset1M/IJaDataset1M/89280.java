package proper.remote;

import java.net.InetSocketAddress;
import java.util.Stack;

/**
* This class manages a list of clients.
*
* @author         FracPete
* @version $Revision: 1.2 $
*/
public class ClientList {

    private Stack clients;

    private Stack jobs;

    /**
   * initializes the object
   */
    public ClientList() {
        clients = new Stack();
        jobs = new Stack();
    }

    /**
   * the number of clients currently in the list
   */
    public int size() {
        return clients.size();
    }

    /**
   * returns the specified client, NULL if index is out of bounds
   */
    public InetSocketAddress get(int index) {
        if ((index >= 0) && (index < size())) return (InetSocketAddress) clients.get(index); else return null;
    }

    /**
   * returns the job of a specified client, NULL if index is out of bounds
   */
    public String getJob(int index) {
        if ((index >= 0) && (index < size())) return (String) jobs.get(index); else return null;
    }

    /**
   * checks whether the given client is already in the list
   */
    public boolean contains(InetSocketAddress client) {
        return clients.contains(client);
    }

    /**
   * returns the index of the given client in the list
   */
    public int indexOf(InetSocketAddress client) {
        return clients.indexOf(client);
    }

    /**
   * adds the given client to the list
   */
    public void add(InetSocketAddress client) {
        add(client, "");
    }

    /**
   * adds the given client with its job to the list
   */
    public void add(InetSocketAddress client, String job) {
        clients.add(client);
        jobs.add(job);
    }

    /**
   * removes the specified client (and its job) from the list
   */
    public InetSocketAddress remove(int index) {
        jobs.remove(index);
        return (InetSocketAddress) clients.remove(index);
    }

    /**
   * removes the specified client (and its job) from the list
   */
    public InetSocketAddress remove(InetSocketAddress client) {
        if (clients.contains(client)) return remove(clients.indexOf(client)); else return null;
    }

    /**
   * adds the given client to the list (like on a stack)
   */
    public void push(InetSocketAddress client) {
        push(client, "");
    }

    /**
   * adds the given client and its job to the list (like on a stack)
   */
    public void push(InetSocketAddress client, String job) {
        clients.push(client);
        jobs.push(job);
    }

    /**
   * returns the current topmost client from the list (like from a stack),
   * it also removes the associated job
   */
    public InetSocketAddress pop() {
        if (clients.empty()) {
            return null;
        } else {
            jobs.pop();
            return (InetSocketAddress) clients.pop();
        }
    }

    /**
   * takes a look at the topmost client on the stack, can be NULL
   */
    public InetSocketAddress peek() {
        if (clients.empty()) return null; else return (InetSocketAddress) clients.peek();
    }

    /**
   * takes a look at the topmost job on the stack, can be NULL
   */
    public InetSocketAddress peekJob() {
        if (jobs.empty()) return null; else return (InetSocketAddress) jobs.peek();
    }

    /**
   * the list in a string representation
   */
    public String toString() {
        String result;
        int i;
        result = "";
        for (i = 0; i < size(); i++) result += get(i) + ": " + getJob(i) + "\n";
        return result;
    }
}
