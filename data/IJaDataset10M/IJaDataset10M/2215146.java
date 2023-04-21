package org.mbari.util;

/**
 * <p>Implementing class will be able to notify otheres of changes to it's
 * properties. <br><br>
 * To Use: Classes that implement this interface should contain a reference to an
 * <i>ObservableComponent</i>. The implementing class should use this interface
 * to implement the appropriate method calls to the ObservableComponent. for example:<br><br>
 *
 * <pre>
 * public class SomeObservableClass implements IObservable{
 *
 *      public void addObserver(IObserver observer) {
 *          oc.add(observer);
 *      }
 *
 *      void notifyObservers() {
 *          oc.notify(this, "someFlagToIndicateChange");
 *      }
 *
 *      public void removeObserver(IObserver observer){
 *          oc.remove(observer);
 *      }
 *
 *      public void removeAllObservers() {
 *          oc.clear();
 *      }
 *
 *      private ObservableComponent oc = new ObservableComponent();
 * }
 * </pre></p>
 *
 * <p><font size="-1" color="#336699"><a href="http://www.mbari.org">
 * The Monterey Bay Aquarium Research Institute (MBARI)</a> provides this
 * documentation and code &quot;as is&quot;, with no warranty, express or
 * implied, of its quality or consistency. It is provided without support and
 * without obligation on the part of MBARI to assist in its use, correction,
 * modification, or enhancement. This information should not be published or
 * distributed to third parties without specific written permission from
 * MBARI.</font></p><br>
 *
 * <font size="-1" color="#336699">Copyright 2003 MBARI.<br>
 * MBARI Proprietary Information. All rights reserved.</font><br>
 *
 * @author <a href="http://www.mbari.org">MBARI</a>
 * @version $Id: IObservable.java 3 2005-10-27 16:20:12Z hohonuuli $
 *
 */
public interface IObservable {

    /**
     * Adds an IObserver
     * @param observer Some instance of IObserver to be added
     */
    void addObserver(IObserver observer);

    /** Remove all observers */
    void removeAllObservers();

    /**
     * Remove an observer
     * @param observer The observer to be removed
     */
    void removeObserver(IObserver observer);
}
