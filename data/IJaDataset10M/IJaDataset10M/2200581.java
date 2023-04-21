package ch.serva.tools;

/**
 * Basic interface for all requests.
 * 
 * @author Lukas Blunschi
 * 
 */
public interface Request {

    String getParameter(String name);
}
