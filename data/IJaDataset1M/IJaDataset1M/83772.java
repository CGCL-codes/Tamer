package net.sf.etl.parsers.utils.etl2emf;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import net.sf.etl.parsers.TermParser;
import net.sf.etl.parsers.emf.EMFTermParser;
import net.sf.etl.utils.ETL2AST;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;

/**
 * Converter from ETL to EMF
 * 
 * @author const
 */
public class ETL2EMF extends ETL2AST {

    /**
	 * list of loaded packages
	 */
    final ArrayList<Object> packages = new ArrayList<Object>();

    /**
	 * {@inheritDoc}
	 */
    @Override
    protected void processContent(OutputStream outStream, TermParser p) throws IOException {
        final EMFTermParser ep = new EMFTermParser(p);
        configureStandardOptions(ep);
        final XMIResource out = new XMIResourceImpl();
        while (ep.hasNext()) {
            out.getContents().add(ep.next());
        }
        out.save(outStream, Collections.EMPTY_MAP);
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    protected int handleCustomOption(String[] args, int i) throws Exception {
        if ("-p".equals(args[i])) {
            final String packageName = args[i + 1];
            i++;
            final Class<?> packageInteface = Class.forName(packageName);
            final Field f = packageInteface.getDeclaredField("eINSTANCE");
            packages.add(f.get(null));
        } else {
            return super.handleCustomOption(args, i);
        }
        return i;
    }

    /**
	 * Application entry point
	 * 
	 * @param args
	 *            application arguments
	 */
    public static void main(String args[]) {
        try {
            new ETL2EMF().start(args);
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
    }
}
