package cmdline;

import java.io.PrintStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * This class defines the commandline options parser. Based on a class filled
 * with members annotated with the Option annotation-type the parser knows for
 * which minus parameters to look and to convert the accompanying string to
 * which type. After the parse-function is finished the annotated object is
 * filled with the data passed with the command-line options.
 * 
 * <pre>
 *   class Example
 *   {
 *      class Options
 *      {
 *         \@Option(name=&quot;t&quot;, param=&quot;integer&quot;, type=Option.OptionType.REQUIRED_ARGUMENT, usage=
 *         	&quot;a long description here ...&quot;)
 *         public int threshold = 500;
 *         
 *         \@option(name=&quot;f&quot;, param=&quot;filename&quot;, type=Option.OptionType.REQUIRED_ARGUMENT, usage=
 *         	&quot;a long description here ...&quot;)
 *         public String file = &quot;std_file.txt&quot;;
 *         
 *         \@option(name=&quot;fs&quot;, param=&quot;filename-list comma separated&quot;, type=Option.OptionType.REQUIRED_ARGUMENT, usage=
 *         	&quot;a long description here ...&quot;)
 *         public Vector<String> files = new Vector<String>();
 *         
 *         \@option(name=&quot;d&quot;, param=&quot;&quot;, type=Option.OptionType.NO_ARGUMENT, usage=
 *         	&quot;a long description here ...&quot;)
 *         public boolean do_something = false; 
 *      }
 *      
 *      public static void main(String args[])
 *      {
 *         Example m = new Example();
 *         Options options = m.new Options();
 *         CmdLineParser parser = new CmdLineParser(options);
 *         
 *         try
 *         {
 *            parser.parse(args);
 *         }
 *         catch(CmdLineException e)
 *         {
 *            parser.printUsage(System.err, e.toString());
 *         }
 *         
 *         if (options.do_something)
 *         {
 *            ...
 *         }
 *      }
 *   }
 * </pre>
 * 
 * TODO Although some support is in place, methods are still not supported. 
 * TODO Help-output should be nicely formatted.
 * TODO Make the program description also an annotated type ?
 * 
 * @author RA Scheltema
 * @version 1.0.0
 */
public class CmdLineParser {

    /**
	 * Standard constructor where the options-object and the description are
	 * set. The options-object is parsed for annotated variables and functions,
	 * which are saved for the parse-function. After successful completion of
	 * this constructor, the parse-function has the information it needs to deal
	 * with the command-line options.
	 * 
	 * @param o The options-object with the annotated variables and functions.
	 * @param description The program description. throws NullPointerException
	 *            When either of the parameters has been set to null.
	 */
    public CmdLineParser(final Object options) throws NullPointerException {
        if (options == null) {
            throw new NullPointerException("The parameters options and/or description cannot be null.");
        }
        this.options = options;
        field_map = new HashMap<String, Field>();
        method_map = new HashMap<String, Method>();
        Field[] fields = options.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].isAnnotationPresent(Option.class)) {
                Option opt = fields[i].getAnnotation(Option.class);
                field_map.put(opt.name(), fields[i]);
            }
        }
        Method[] methods = options.getClass().getDeclaredMethods();
        for (int i = 0; i < methods.length; i++) {
            if (methods[i].isAnnotationPresent(Option.class)) {
                Option opt = methods[i].getAnnotation(Option.class);
                method_map.put(opt.name(), methods[i]);
            }
        }
        OptionsClass optionscls = options.getClass().getAnnotation(OptionsClass.class);
        if (optionscls != null) description = optionscls.description();
    }

    private static String clean(String arg) {
        String r = arg.replace((char) 13, ' ');
        return r.trim();
    }

    /**
	 * With this function the parser is invoked for the command-line options.
	 * The parser looks for -option entries in the command-line options and
	 * tries to translate the information into the annotated object. After
	 * successful completion of this method, the annotated object set in the
	 * constructor will contain all the information found in the command-line
	 * options.
	 * 
	 * @param args The command-line options.
	 * @throws CmdLineException When the options cannot be parsed due to
	 *             insufficient number, etc..
	 */
    public void parse(String[] args) throws CmdLineException {
        Field prevfield = null;
        for (int i = 0; i < args.length; i++) {
            String name = clean(args[i]);
            if (name.length() == 0) continue;
            if (name.charAt(0) == '-') name = name.substring(1);
            Field field = field_map.get(name);
            if (field == null) {
                i--;
                field = prevfield;
            }
            if (field != null) {
                Option opt = field.getAnnotation(Option.class);
                try {
                    String result = "";
                    if (opt.type() == Option.Type.NO_ARGUMENT) {
                        result = "true";
                    } else if (opt.type() == Option.Type.OPTIONAL_ARGUMENT) {
                        if (i + 1 >= args.length) result = "true"; else {
                            String nxt_option = clean(args[++i]);
                            nxt_option.trim();
                            if (nxt_option.charAt(0) != '-') {
                                result = nxt_option;
                            }
                        }
                    } else {
                        if (i + 1 >= args.length) throw new CmdLineException("Insufficient number of options.");
                        String nxt_option = clean(args[++i]);
                        nxt_option.trim();
                        result = nxt_option;
                    }
                    FieldAssign field_assign = new FieldAssign(field);
                    field_assign.assign(options, result);
                } catch (Exception e) {
                    throw new CmdLineException("Unhandled situation:\n" + e.toString());
                }
            }
            prevfield = field;
        }
    }

    /**
	 * With this function the parser is invoked for the properties.
	 * The parser looks for entries in the properties list and
	 * tries to translate the information into the annotated object. After
	 * successful completion of this method, the annotated object set in the
	 * constructor will contain all the information found in the command-line
	 * options.
	 * 
	 * @param properties the properties set
	 * @throws CmdLineException When the options cannot be parsed due to
	 *             insufficient number, etc..
	 */
    public void parse(Properties properties) throws CmdLineException {
        for (Map.Entry<Object, Object> property : properties.entrySet()) {
            String name = property.getKey().toString();
            name.trim();
            if (name.charAt(0) == '-') name = name.substring(1);
            Field field = field_map.get(name);
            if (field != null) {
                Option opt = field.getAnnotation(Option.class);
                try {
                    String result = "";
                    if (opt.type() == Option.Type.NO_ARGUMENT) {
                        result = "true";
                    } else if (opt.type() == Option.Type.OPTIONAL_ARGUMENT) {
                        result = property.getValue().toString().trim();
                    } else if (opt.type() == Option.Type.REQUIRED_ARGUMENT) {
                        if (property.getValue().toString().length() == 0) throw new CmdLineException("Insufficient number of options.");
                        result = property.getValue().toString().trim();
                    }
                    FieldAssign field_assign = new FieldAssign(field);
                    field_assign.assign(options, result);
                } catch (Exception e) {
                    throw new CmdLineException("Unhandled situation:\n" + e.toString());
                }
            }
        }
    }

    public void printOptions() {
        Iterator<Field> it_field = field_map.values().iterator();
        int namesize = 0;
        while (it_field.hasNext()) {
            Field field = it_field.next();
            Option option = field.getAnnotation(Option.class);
            namesize = Math.max(namesize, option.name().length());
        }
        it_field = field_map.values().iterator();
        System.out.println("Options {");
        while (it_field.hasNext()) {
            Field field = it_field.next();
            Option option = field.getAnnotation(Option.class);
            try {
                System.out.print("  " + option.name() + ": ");
                for (int i = 0; i < namesize - option.name().length(); ++i) System.out.print(" ");
                System.out.println(field.get(options));
            } catch (Exception e) {
                System.out.println("[failed]");
            }
        }
        System.out.println("}");
    }

    /**
	 * With this method the usage description can be printed to a print-stream.
	 * The output will be formatted to look good on a 80 characters wide display
	 * (typical for a command-line prompt).
	 * 
	 * @param stream The print-stream to write the information to.
	 * @param msg The extra message (when needed) that will be displayed above
	 *            the usage.
	 */
    public void printUsage(PrintStream stream, String msg) {
        final int width = 80;
        if (msg != null && msg.length() != 0) {
            stream.print(msg + "\n\n");
        }
        if (description != null && description.length() != 0) {
            int index = 0;
            while (index < description.length()) {
                if ((index + width) < description.length()) {
                    String str = description.substring(index, index + width);
                    int endindex = str.lastIndexOf('\n');
                    if (endindex == -1) endindex = str.lastIndexOf(' ');
                    if (endindex == -1 || endindex > width) endindex = width;
                    stream.println(str.substring(0, endindex));
                    index += endindex + 1;
                } else {
                    stream.println(description.substring(index));
                    break;
                }
            }
        }
        stream.print("\nUsage:\n");
        int taglength = 0;
        Iterator<Field> it_field = field_map.values().iterator();
        while (it_field.hasNext()) {
            Field field = it_field.next();
            Option option = field.getAnnotation(Option.class);
            taglength = Math.max(taglength, option.name().length() + 2);
        }
        for (Option.Level level : Option.Level.values()) {
            it_field = field_map.values().iterator();
            while (it_field.hasNext()) {
                Field field = it_field.next();
                Option option = field.getAnnotation(Option.class);
                if (option.level() != level) continue;
                char tagopen = '<';
                char tagclose = '>';
                try {
                    if (Collection.class.isInstance(field.getType().newInstance())) {
                        tagopen = '[';
                        tagclose = ']';
                    }
                } catch (Exception e) {
                    ;
                }
                stream.print(option.name());
                if (option.type() != Option.Type.NO_ARGUMENT) {
                    for (int i = 0; i < taglength - option.name().length(); ++i) stream.print(' ');
                    stream.println(tagopen + option.param() + tagclose);
                } else stream.println();
                String usages[] = option.usage().split("\n");
                for (String usage : usages) {
                    int index = 0;
                    while (index < usage.length()) {
                        for (int i = 0; i < taglength; ++i) stream.print(' ');
                        if ((index + width - taglength) < usage.length()) {
                            String str = usage.substring(index, index + width - taglength);
                            int endindex = str.lastIndexOf('\n');
                            if (endindex == -1) endindex = str.lastIndexOf(' ');
                            if (endindex == -1 || endindex > width - taglength) endindex = width - taglength;
                            stream.println(str.substring(0, endindex));
                            index += endindex + 1;
                        } else {
                            stream.println(usage.substring(index));
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
	* Pretty print the options.
	*
	* Uses reflection to filter out the relevant fields.
	*/
    public String toString(Object options) {
        StringBuffer result = new StringBuffer();
        Field[] fields = options.getClass().getDeclaredFields();
        int name_padding = 0;
        int value_padding = 0;
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].isAnnotationPresent(Option.class)) {
                Option opt = fields[i].getAnnotation(Option.class);
                name_padding = Math.max(name_padding, fields[i].getAnnotation(Option.class).name().length());
                try {
                    String value = fields[i].get(options).toString();
                    if (opt.param().equals("password")) value = "xxxxxx";
                    value_padding = Math.max(value_padding, value.length());
                } catch (Exception e) {
                    throw new RuntimeException("faulty option field " + fields[i]);
                }
            }
        }
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].isAnnotationPresent(Option.class)) {
                Option opt = fields[i].getAnnotation(Option.class);
                try {
                    String value = fields[i].get(options).toString();
                    if (opt.param().equals("password")) value = "xxxxxx";
                    String n_spaces = "";
                    for (int j = opt.name().length(); j < name_padding; j++) n_spaces += " ";
                    String v_spaces = "";
                    for (int j = value.length(); j < value_padding; j++) v_spaces += " ";
                    result.append(opt.name() + n_spaces + " = " + value + v_spaces + " #" + opt.usage() + "\n");
                } catch (Exception e) {
                    throw new RuntimeException("faulty option field " + fields[i]);
                }
            }
        }
        return result.toString();
    }

    /** Provides a place for keeping to the class with the annotated variables describing the options */
    private Object options;

    /** Provides a container for the annotated fields */
    private HashMap<String, Field> field_map;

    /** Provides a container for the annotated methods */
    private HashMap<String, Method> method_map;

    /** */
    private String description = null;
}
