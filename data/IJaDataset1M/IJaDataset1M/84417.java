package org.jude.server;

import org.jude.client.db.*;
import org.jude.simplelogic.*;
import java.io.*;
import java.util.*;

/**
 * <p> This class contains methods that interact with XSB through 
 *     C-Native-Interface (CNI or JNI).
 * <p> !! retest all after Oid --> JudeObject change
 */
public class XSBCore {

    /**
    * <code>xsb_init(String[] args)</code> initializes the XSB engines. 
    * The first element of <code>args</code> must point to the XSB installation
    * directory. The other parameter are usuall XSB startup parameter. 
    * A typical parameters are: <br>
    * <code> String[] args= {"/local/xsb/XSB", "-n", "--quietload"}; </code>
    * <code>"-n"</code> turns the interactive mode of <br> 
    * <code>"--quitemode"</code> prevents startup output from XSB <br>
    *
    * @param factory the factory to use to create Oids
    */
    public native int jude_xsb_init(String[] args, JudeObjectFactory factory);

    /**
     * xsb_close() is currently just a noop, since it doesn't clean 
     * anything up, to allow a re-init. 
     */
    public native int jude_xsb_close();

    /**
     * xsb_command_string(char *goal) passes the command (e.g. a query 
     * which only succeeds or fails) to xsb.  The command must a string    
     * passed in the argument.  It returns 0 if it succeeds, 1 if it        
     * fails, in either case resetting register 1 back to a free           
     * variable.  It returns 2 if there is an error.
     *
     */
    public native int jude_xsb_command_string(String command);

    /** xsb_command() passes the command (i.e. query with no variables) to
    * xsb.  The command must be put into xsb's register 1 as a term, by    
    * the caller who uses the c2p_* (and perhaps p2p_*) functions.         
    *   It returns 0 if it succeeds, 1 if it fails, in either case         
    * resetting register 1 back to a free variable.  It returns 2 if there 
    * is an error. Usually not called from Java.
    */
    public native int jude_xsb_command();

    /** xsb_command_term(Term t) passes the command represented as a Java Term
    *  to xsb and executes it.      
    * It returns 0 if it succeeds, 1 if it fails, in either case         
    * resetting register 1 back to a free variable.  It returns 2 if there 
    * is an error. Usage examples are eg. turning on tabling for a predicate.
    */
    public native int jude_xsb_command_term(JudeObject t);

    /**
    xsb_query_string(String command) submits a query to xsb.  The string must
    be a goal that will be correctly read by xsb's reader, and it must
    be terminated with a period (.).  The query will be parsed, and an
    answer term will be constructed where
    Y1, .... Yn are the variables in the parsed goal (in left-to-right
    order). The answer can be obtained with xsb_answer_string or 
    xsb_getAnswerSubstitution. If the goal succeeds,
    xsb_query_string returns 0 and the first answer is in register 2.
    If it fails, xsb_query_string returns 1. 
    */
    public native int jude_xsb_query_string(String command);

    /** xsb_query() passes the query to
    * xsb.  The query must be put into xsb's register 1 as a term, by    
    * the caller who uses the c2p_* (and perhaps p2p_*) functions.         
    *   It returns 0 if it succeeds, 1 if it fails, in either case         
    * resetting register 1 back to a free variable.  It returns 2 if there 
    * is an error. Usually not called from Java.
    */
    public native int jude_xsb_query();

    /** xsb_query_term(Term t) passes the query represented as a Java term to
    * xsb.  The query must be put into xsb's register 1 as a term, by    
    * the caller who uses the c2p_* (and perhaps p2p_*) functions.         
    *   It returns 0 if it succeeds, 1 if it fails, in either case         
    * resetting register 1 back to a free variable.  It returns 2 if there 
    * is an error. Usually not called from Java.
    */
    public native int jude_xsb_query_term(JudeObject t);

    /** xsb_close_query() closes the current query, so that no more answers
    *will be returned, and another query can be opened.
    *If the query was correctly closed, it resets xsb registers 1 and 2  
    * to be variables, and returns 0.  If there is some error, it returns 2. 
    */
    public native int jude_xsb_close_query();

    /**
     * xsb_next() causes xsb to return the next answer.  It (or
     * xsb_close_query) must be called after xsb_query.  If there is      
     * another answer, xsb_next returns 0 and the substitution can get
     * obtained via xsb_getAnswerSubstitution or xsb_answer_string. 
     * Variables in goal term                                                    
     * xsb_next returns 0 if the next answer is found, 1 if there are no more     
     * answers, and 3 if an error is encountered. If 1 is returned, then   
     * the query is automatically closed.                                 
     */
    public native int jude_xsb_next();

    /**
     * Asserts a Java representation of a program clause
     */
    public native int jude_xsb_assert_rule(Rule clause);

    /**
     * Retract the Java Representation of a Rule
     */
    public native int jude_xsb_retract_rule(Rule clause);

    public native int jude_xsb_assert_fact(Fact fact);

    public native int jude_xsb_retract_fact(Fact fact);

    /**
     * Poses a query in a Jude query rapresentation
     */
    public native JudeObject jude_xsb_query_java(QueryBody query);

    /**
     * Poses a query as a Jude test
     */
    public native int jude_xsb_query_jude_test(TestBody test);

    static {
        String libName = System.getProperty("user.dir") + File.separator + "lib" + File.separator + "libXSB.so";
        System.load(libName);
    }
}
