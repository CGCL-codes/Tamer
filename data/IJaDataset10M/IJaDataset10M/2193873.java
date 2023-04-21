package com.parexel.toolkit.getdata.tools.vectors;

import java.util.Vector;
import com.parexel.toolkit.getdata.xml.Request;

/**
 * This class allows using specific Vector of requests.
 *
 * Need:
 * - Vector: the mother class.
 * - toolkit.getdata.xml.request: what the vector contains only.
 *
 * Functions:
 * - constructors.
 * - get and set functions:
 *   + get size,
 *   + get request at index i,
 *   + get vector of requests,
 *   + set vector of requests,
 *   + add an email.
 * - show method.
 */
public class VectorOfRequests extends Vector {

    /** Creates a new instance of MyVector */
    public VectorOfRequests(int p_Size) {
        super(p_Size);
    }

    /** Creates a new instance of MyVector */
    public VectorOfRequests() {
        super();
    }

    /**
     * Get the value of the NumberOfRequests attribute of the class.
     * @return The value of the NumberOfRequests attribute.
     * @return -1 if impossible.
     **/
    public int getNumberOfRequests() {
        try {
            return this.size();
        } catch (NullPointerException npe) {
            return -1;
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * Get the request at the index p_Index of the vector of requests.
     * 
     * Warning: the vector index starts at 0. So for having the requests 1, look at the index 0.
     * Warning: add only requests in the vector of requests and always test if requests are added.
     *
     * @param p_Index Index of the request to return.
     * @return A request if found.
     * @return A null request if there is not any element at this index.
     **/
    public Request getRequestAtIndex(int p_Index) {
        try {
            if (p_Index >= 0 && p_Index <= this.size()) return (com.parexel.toolkit.getdata.xml.Request) this.get(p_Index);
        } catch (NullPointerException npe) {
        } catch (Exception e) {
        }
        return new Request();
    }

    /**
     * Show all the attributes values.
     * - Number of requests in the vector;
     * - all the attributes of all the requests;
     * @return A text that describes this attributes.
     **/
    public String show() {
        StringBuffer l_Show = new StringBuffer();
        try {
            l_Show.append("Number of valid requests: " + this.size() + "\n");
            return l_Show.toString();
        } catch (NullPointerException npe) {
        } catch (Exception e) {
        }
        return "Vector of Requests: show application fail";
    }
}
