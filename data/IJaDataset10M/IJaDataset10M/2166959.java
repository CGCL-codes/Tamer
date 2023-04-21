package er.directtoweb.components;

import org.apache.log4j.Logger;
import com.webobjects.appserver.WOContext;

public class ERDCustomQueryComponentWithArgs extends ERDCustomQueryComponent {

    public ERDCustomQueryComponentWithArgs(WOContext context) {
        super(context);
    }

    /** logging support */
    public static final Logger log = Logger.getLogger(ERDCustomQueryComponentWithArgs.class);
}
