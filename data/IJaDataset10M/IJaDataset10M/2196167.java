package com.monoserv.http;

import com.monoserv.http.exceptions.*;
import java.util.*;
import java.io.*;

public class HttpParameters {

    /** The internal hash table **/
    private Hashtable<String, String> params;

    /** CRLF **/
    public static String CRLF = "\r\n";

    /**
     * The constructor to initialize the internal hashtable.
     */
    public HttpParameters() {
        params = new Hashtable<String, String>();
    }

    /**
     * Convience method for AddParam(String,String).  Identical to calling
     * <code>
     *   AddParam(key, Integer.toString(value));
     * </code>
     * 
     * @param key The name of the header
     * @param value The value of the header
     * @see AddParam(String,String)
     */
    public void AddParam(String key, int value) {
        AddParam(key, Integer.toString(value));
    }

    /**
     * Convience method for AddParam(String,String).  Identical to calling
     * <code>
     *   AddParam(key, Double.toString(value));
     * </code>
     * 
     * @param key The name of the header
     * @param value The value of the header
     * @see AddParam(String,String)
     */
    public void AddParam(String key, double value) {
        AddParam(key, Double.toString(value));
    }

    /**
     * Adds a parameters to the hash table.  If both the key and value have a 
     * length() of greater than 0, then the valud is added via the Hashtable.put()
     * method.  Else, a RuntimeException is thrown indicating an Invalid string for
     * the pair.
     * 
     * If a duplicate is supplied, it is overridden.  No checks are performed to
     * ensure that a duplicate is not overridden.
     * 
     * @param key The actual name of the header
     * @param value The value for the associated header.
     * @throws RuntimeException If either supplied string has a length less than
     * or equal to 0.
     */
    public void AddParam(String key, String value) throws RuntimeException {
        if (key.length() > 0 && value.length() > 0) params.put(key, value); else throw new RuntimeException("Invalid string for key/value pair");
    }

    /**
     * Returns the key set iterator for the internal hashtable.
     * 
     * @return Iterator<String> An iterator to traverse through the keys in
     * the hashtable.
     */
    public Iterator<String> GetIterator() {
        return params.keySet().iterator();
    }

    /**
     * Returns the value for the supplied header.  If the header exists, then
     * the associated value is returned.  If it does not exist, it throws an
     * exception stating so.
     * 
     * @param idx The header to look up
     * @return String A string value representing the header value if it exists
     * @throws HeaderDoesNotExistException If the header does not exist.
     * @see DoesHeaderExist(String)
     */
    public String GetHeaderValue(String idx) throws HeaderDoesNotExistException {
        if (DoesHeaderExist(idx)) return (String) params.get(idx); else throw new HeaderDoesNotExistException(idx);
    }

    /**
     * Performs a simple search to see if the header supplied exists in the
     * internal hashtable.
     * 
     * <code>
     *   return ( params.size() > 0 && params.containsKey(key));
     * </code>
     * 
     * @param key The key to search for
     * @return boolean true if the key exists, false otherwise.
     */
    public boolean DoesHeaderExist(String key) {
        return (params.size() > 0 && params.containsKey(key));
    }

    /**
     * Returns the size (or number of elements) for the internal hashtable.
     * 
     * @return int Returns the number of elements in the hashtable.
     */
    public int GetParamCount() {
        return params.size();
    }

    /**
     * A convienence function to see what is in the object.
     * If the internal hashtable is null, then the string "Null" is returned.
     * If the internal hashtalble is no elements, then the string "Empty" is returned.
     * If the internal hashtable has 1 or more elements, then it will construct
     * a string in the format of 
     * <pre>
     *   { HeaderName0="Value0"# HeaderName1="Value1"# ... HeaderNameN="ValueN" }
     * </pre>
     * 
     * @return String A representation of the object and its data.
     */
    public String toString() {
        if (params == null) return "Null"; else if (params.size() == 0) return "Empty"; else {
            StringBuffer sb = new StringBuffer("{ ");
            Iterator<String> itHeader = GetIterator();
            while (itHeader.hasNext()) {
                String header = itHeader.next();
                String headerValue = GetHeaderValue(header);
                sb.append(header + "=\"" + headerValue + "\"");
                if (itHeader.hasNext()) sb.append("# ");
            }
            sb.append(" }");
            return sb.toString();
        }
    }

    /**
     * This will take an unformatted string representation of HTTP headers in the
     * standard format.  It will fill a HttpParameters object with those values
     * and return them as an HttpParameters Object.
     * 
     * It will split the Headers based on the CRLF values and then parse them based
     * on the colon to seperate the header name and the header value.
     * 
     * @param httpHeaders A single string of all the http headers in the message
     * @return HttpParameters An object with the parameters stored internally
     */
    public static HttpParameters make(String httpHeaders) {
        HttpParameters newparams = new HttpParameters();
        String[] brokenParams = httpHeaders.split(CRLF);
        for (int i = 0; i < brokenParams.length; i++) if (brokenParams[i].length() > 0) {
            String[] thisParam = brokenParams[i].split(":", 2);
            if (thisParam[0].length() > 0 && thisParam[1].length() > 0) {
                newparams.AddParam(thisParam[0].trim(), thisParam[1].trim());
            }
        }
        return newparams;
    }

    /**
     * Returns a string that is in the proper HTTP format for a response
     * The format is
     * <pre>
     *  HeaderName0: HeaderValue0\r\n
     *  HeaderNaem1: HeaderValue1\r\n
     *  ...
     * </pre>
     * 
     * Although the HTTP protocol requires an extra CRLF to break between the 
     * headers and the body, <b>it is not added here</b>.
     */
    public String getHttpHeaders() {
        StringBuffer sb = new StringBuffer();
        Iterator it = GetIterator();
        while (it.hasNext()) {
            String header = (String) it.next();
            String value = GetHeaderValue(header);
            if (header.length() > 0) sb.append(header + ": " + value + CRLF);
        }
        return sb.toString();
    }

    /**
     * Returns a blank parameters object
     * 
     * @return HttpParameters An object with no elements in the hashtable.
     */
    public static HttpParameters makeBlank() {
        return new HttpParameters();
    }
}
