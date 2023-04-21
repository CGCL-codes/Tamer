package tei.cr.filters;

import java.util.logging.Logger;
import tei.cr.pipeline.AbstractBase;
import tei.cr.pipeline.FilterByNames;
import tei.cr.pipeline.WrongArgsException;
import tei.cr.querydoc.FilterArguments;
import tei.cr.teiDocument.TeiDocument;
import bsh.EvalError;
import bsh.TargetError;
import bsh.ParseException;
import bsh.Interpreter;
import org.xml.sax.ContentHandler;

/**
 * Dynamic execution of java code thanks to Pat Niemeyer Beanshell
 * (http://www.beanshell.org).
 * 
 * @author Sylvain Loiseau
 */
public final class Script extends AbstractBase {

    private AbstractBase filter;

    private Logger log = Logger.getLogger(getClass().getName());

    private ContentHandler target;

    /**
     * The java code defining the behaviour of this filter
     * 
     * @param script the body of a subclass of {@link org.xml.sax.helpers.XMLFilterImpl}
     *
     * @throws WrongArgsException
     */
    public void setScript(String script) throws WrongArgsException {
        Interpreter i = new Interpreter();
        try {
            i.eval(tei.cr.pipeline.AbstractBase.class.getName() + " filter = " + "new " + tei.cr.pipeline.AbstractBase.class.getName() + "() {" + script + "};");
            filter = (AbstractBase) i.get("filter");
        } catch (TargetError tE) {
            Throwable tT = tE.getTarget();
            WrongArgsException wAE = new WrongArgsException("The script threw an exception. " + "Exception: " + tT.toString() + ". " + "Message: " + tT.getMessage() + ". " + tE.getMessage());
            wAE.initCause(tE);
            throw wAE;
        } catch (ParseException pE) {
            WrongArgsException wAE = new WrongArgsException("Syntactic error in the script provided: " + pE.getMessage());
            wAE.initCause(pE);
            throw wAE;
        } catch (EvalError eE) {
            WrongArgsException wAE = new WrongArgsException("Error while parsing the script: " + eE.getMessage());
            wAE.initCause(eE);
            throw wAE;
        }
    }

    public void setArguments(FilterArguments fA, FilterByNames nH, TeiDocument doc) throws WrongArgsException {
        String script = fA.getText(FilterArguments.SCRIPT_TEXT);
        if (script == null && script.equals("")) {
            throw new WrongArgsException("The script cannot be null or empty.");
        }
        log.info("Script used: \n" + script);
        setScript(script);
        filter.setArguments(fA, nH, doc);
    }

    public void startPipeline() throws FilterException {
        if (filter == null) {
            throw new FilterException("The script has not been initialized.");
        }
        target = getXMLFilter().getContentHandler();
        filter.setContentHandler(target);
        setContentHandler((ContentHandler) filter);
        super.startPipeline();
    }
}
