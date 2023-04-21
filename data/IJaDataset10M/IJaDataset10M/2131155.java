package net.sf.orcc.backends.c;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import net.sf.orcc.backends.TemplateGroupLoader;
import net.sf.orcc.ir.Actor;
import net.sf.orcc.ir.Constant;
import net.sf.orcc.ir.Expression;
import net.sf.orcc.ir.Printer;
import net.sf.orcc.ir.Type;
import net.sf.orcc.util.INameable;
import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;

/**
 * This class defines a C actor printer.
 * 
 * @author Matthieu Wipliez
 * 
 */
public final class CActorPrinter extends Printer {

    private StringTemplateGroup group;

    private Map<String, String> transformations;

    /**
	 * Creates a new network printer with the template "C_actor".
	 * 
	 * @throws IOException
	 *             If the template file could not be read.
	 */
    public CActorPrinter() {
        group = new TemplateGroupLoader().loadGroup("C_actor");
        Printer.register(this);
        transformations = new HashMap<String, String>();
        transformations.put("abs", "abs_");
        transformations.put("index", "index_");
        transformations.put("getw", "getw_");
        transformations.put("select", "select_");
    }

    /**
	 * Prints the given actor to a file whose name is given.
	 * 
	 * @param fileName
	 *            output file name
	 * @param id
	 *            the instance id
	 * @param actor
	 *            actor to print
	 * @throws IOException
	 */
    public void printActor(String fileName, String id, Actor actor) throws IOException {
        StringTemplate template = group.getInstanceOf("actor");
        template.setAttribute("actorName", id);
        template.setAttribute("actor", actor);
        byte[] b = template.toString(80).getBytes();
        OutputStream os = new FileOutputStream(fileName);
        os.write(b);
        os.close();
    }

    @Override
    public String toString(Constant constant) {
        CConstPrinter printer = new CConstPrinter(group);
        constant.accept(printer);
        return printer.toString();
    }

    @Override
    public String toString(Expression expression) {
        CExpressionPrinter printer = new CExpressionPrinter();
        expression.accept(printer, Integer.MAX_VALUE);
        return printer.toString();
    }

    @Override
    public String toString(INameable nameable) {
        String name = nameable.getName();
        if (transformations.containsKey(name)) {
            return transformations.get(name);
        } else {
            return name;
        }
    }

    @Override
    public String toString(Type type) {
        CTypePrinter printer = new CTypePrinter();
        type.accept(printer);
        return printer.toString();
    }
}
