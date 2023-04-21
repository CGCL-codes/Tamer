package net.sourceforge.esw.service.log;

import java.util.Date;
import net.jini.core.lookup.*;
import net.jini.lookup.entry.Name;
import net.sourceforge.esw.graph.INode;
import net.sourceforge.esw.graph.NodeFactory;
import net.sourceforge.esw.service.LookupServiceUtil;
import net.sourceforge.esw.service.log.expression.*;

/**
 * Creational factory for log package elements.
 * <p>
 *
 * This factory returns the current ILog instance, and ILogDatum instances.
 * <p>
 *
 * @see net.sourceforge.esw.service.log.ILog
 * @see net.sourceforge.esw.service.log.ILogDatum
 *
 *
 * @stereotype factory
 */
public class LogFactory implements ILogConstants {

    /****************************************************************************
   * Returns an instance of an ILog for logging of events. The ILog is the
   * mechinism though which events are logged.
   *
   * @return the current ILog instance for logging events.
   */
    public static ILog getLog() {
        LookupServiceUtil lookup = new LookupServiceUtil();
        lookup.addServiceType(ILogDissemination.class);
        lookup.addServiceEntry(new Name("LogDissemination"));
        return (ILog) lookup.lookupService();
    }

    /****************************************************************************
   * Returns an instance of an ILogDatum for logging of events. The ILogDatum
   * is the container that holds the data to be given to an ILog instance for
   * logging of events.
   *
   * @param aWhen when the event occured
   * @param aWhere Where the event occured
   * @param aImportance How important was the event that occured
   * @param aSimpleWhat Simply, what was the event that occured
   * @param aComplexWhat Complex caputured state of the event that occured
   * @param aWho Who the event occured with
   *
   * @return a new ILogDatum instance
   */
    public static ILogDatum createLogDatum(Date aWhen, String aWhere, int aImportance, String aSimpleWhat, INode aComplexWhat, String aWho, long aId) {
        return new DefaultLogDatum(aWhen, aWhere, aImportance, aSimpleWhat, aComplexWhat, aWho, aId);
    }

    /****************************************************************************
   * Returns an instance of an IFilterExpression.  This instance is a default
   * filter expression that always returns true.
   *
   * @return a new IFilterExpression instance
   */
    public static IFilterExpression createFilterExpression() {
        return new DefaultFilterExpression();
    }
}
