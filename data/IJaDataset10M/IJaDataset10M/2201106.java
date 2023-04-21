package api.client.userInterface;

/**
 * @author M.C.Weber Uni Siegen
 * The SOAListener interface guaranties that another java
 * class that is using the SOA-API and needs to be informed
 * about states and errors inside the API has two methods
 * that can be called for state and error propagation
 * 
 */
public interface SOAListener {

    /**
 * Each Listener that need information about the state of
 * the SOA-API and its operations has to implement this 
 * method that it can be informed on changes in the API
 * state. It is used to show the user what happens inside
 * the API during long-time operations i.e. compile stub
 * classes
 *
 * @param s
 */
    void updateStatus(String s);

    /**
 * Each Listener that need information on errors that
 * occure during the call of an API operation of
 * the SOA-API and its operations has to implement this 
 * method that it can be informed on error inside the API.
 * It is used to show the user if an error occures that the
 * cant handel itself, i.e. if the user input was wrong or
 * an web service was unreachable
 *
 * @param s
 */
    void updateError(String s);
}
