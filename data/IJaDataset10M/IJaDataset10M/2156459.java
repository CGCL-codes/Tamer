package genie.commons.chain;

import java.util.Iterator;
import java.util.Map;
import org.apache.commons.chain.Catalog;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;

/**
 * 
 *
 * @author T. Kia Ntoni
 * 
 * 10 avr. 2005 
 * ICommandRunner @version 
 */
public interface ICommandRunner extends Catalog, Command {

    public Context getContext(String name);

    public void addContext(String name, Context context);

    public Iterator<String> getContextsNames();
}
