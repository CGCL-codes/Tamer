package org.dcopolis.problem;

import org.dcopolis.util.parsing.*;
import java.util.*;
import java.io.*;
import java.text.ParseException;
import java.lang.reflect.*;

@SuppressWarnings("unchecked")
public class DCOPInterpreter {

    LinkedHashSet<Macro> macros = new LinkedHashSet<Macro>();

    HashSet<Variable> variables;

    Class<? extends DisCOP> problemClass;

    HashMap<String, Object> identifiers;

    DisCOP dcop;

    private class Macro {

        String arguments[];

        String macro;

        String name;

        public Macro(String name, String arguments[], String macro) {
            this.name = name;
            this.arguments = arguments;
            this.macro = macro;
        }

        public String apply(String text) throws ParseException {
            if (arguments.length <= 0) {
                String original = text;
                text = text.replace(name, macro);
                if (!original.equals(text)) {
                    for (Macro macro : macros) if (!macro.equals(this)) text = macro.apply(text);
                }
            } else {
                int idx = -1;
                while ((idx = text.indexOf(name)) >= 0) {
                    String before = "";
                    if (idx > 0) before = text.substring(0, idx);
                    String after = "";
                    String args = "";
                    if (arguments.length == 0 && text.indexOf(name + "()", idx) == idx) after = text.substring(idx + name.length() + 2); else if (arguments.length == 0) after = text.substring(idx + name.length()); else if (text.substring(idx).matches("^" + name + "\\(.*\\).*")) {
                        args = text.substring(idx + name.length() + 1, text.indexOf(")", idx));
                        after = text.substring(text.indexOf(")", idx) + 1);
                    } else after = text.substring(idx + name.length());
                    String a[] = args.split(",");
                    String newText = apply(a);
                    for (Macro macro : macros) if (!macro.equals(this)) newText = macro.apply(newText);
                    text = before + newText + after;
                }
            }
            return text;
        }

        public String apply(String args[]) throws ParseException {
            if (args.length != arguments.length) throw new ParseException("Error: macro " + name + " received " + args.length + " arguments when expecting " + arguments.length + "!", 0);
            String text = macro;
            for (int i = 0; i < args.length; i++) text = text.replace(arguments[i], args[i]);
            return text;
        }

        public boolean equals(Object object) {
            if (object instanceof Macro) return ((Macro) object).name.equals(name) && ((Macro) object).arguments.length == arguments.length; else return false;
        }

        public int hashCode() {
            return name.hashCode() + arguments.length;
        }
    }

    public HashSet<Variable> getVariables() {
        return variables;
    }

    public Class<? extends DisCOP> getProblemClass() {
        return problemClass;
    }

    public DisCOP getDCOP() {
        return dcop;
    }

    public DCOPInterpreter(File inputFile) throws IOException, ParseException, InterpretationException {
        this(new FileReader(inputFile));
    }

    public DCOPInterpreter(Reader in) throws IOException, ParseException, InterpretationException {
        variables = new HashSet<Variable>();
        identifiers = new HashMap<String, Object>();
        problemClass = DisCOP.class;
        dcop = null;
        BufferedReader input = new BufferedReader(in);
        String line = null;
        int lineNum = 0;
        Parser parser = null;
        parser = new GrammarParser(new DCOPGrammar());
        while ((line = input.readLine()) != null) {
            lineNum++;
            String before;
            before = line;
            for (Macro macro : macros) line = macro.apply(line);
            ParseTree tree = parser.parse(new Tokenizer(line, " \t\n\r\f(){}'\"=.-,#", true));
            if (tree == null) throw new ParseException("Line " + lineNum + ", \"" + line + "\", does not conform to the grammar!", 0);
            ParseTree command = tree.getChildren().get(1).getChildren().getFirst();
            if (command.getTerminal().equals("endline")) continue; else {
                try {
                    command.interpret(this);
                } catch (InterpretationException ie) {
                    ie.setMessage("Line " + lineNum + ": " + ie.getMessage());
                    throw ie;
                }
            }
        }
        input.close();
        try {
            Constructor<? extends DisCOP> c = getProblemClass().getConstructor(new Class[] { Set.class });
            dcop = c.newInstance(getVariables());
        } catch (Exception e) {
            throw new InterpretationException("Error instantiating " + problemClass.getName() + "!", e);
        }
    }

    public Class<? extends DisCOP> problem(ParseTree set, ParseTree whitespace, ParseTree problemKeyword, ParseTree optWhitespace, ParseTree eq, ParseTree optWhitespace2, ParseTree classId) throws ClassNotFoundException, InterpretationException {
        Class c = Class.forName(classId.getToken());
        if (!DisCOP.class.isAssignableFrom(c)) throw new InterpretationException("Error: problem class " + c.getName() + " is not assignable from org.dcopolis.problem.DisCOP!");
        problemClass = (Class<? extends DisCOP>) c;
        return problemClass;
    }

    public LinkedList<String> list(ParseTree listElement, ParseTree whitespace, ParseTree list) throws InterpretationException {
        LinkedList<String> l = new LinkedList<String>();
        l.add(listElement.getToken());
        if (list != null) {
            LinkedList<String> next = (LinkedList<String>) list.interpret(this);
            for (String s : next) l.add(s);
        }
        return l;
    }

    public LinkedList<String> list(ParseTree listElement) throws InterpretationException {
        return list(listElement, null, null);
    }

    public LinkedList idlist(ParseTree id) throws InterpretationException {
        return idlist(id, null, null, null, null);
    }

    public LinkedList idlist(ParseTree id, ParseTree optWhitespace, ParseTree comma, ParseTree optWhitespace2, ParseTree remainder) throws InterpretationException {
        LinkedList s = new LinkedList();
        s.add(id.interpret(this));
        if (remainder != null) {
            LinkedList n = (LinkedList) remainder.interpret(this);
            for (Object o : n) s.add(o);
        }
        return s;
    }

    public void comment(ParseTree optWhitespace, ParseTree pound, ParseTree text, ParseTree endl) {
    }

    public void endline() {
    }

    public LinkedHashSet set(ParseTree leftBrace, ParseTree optWhitespace, ParseTree rightBrace) {
        return new LinkedHashSet();
    }

    public LinkedHashSet set(ParseTree leftBrace, ParseTree optWhitespace, ParseTree list, ParseTree optWhitespace2, ParseTree rightBrace) throws InterpretationException {
        return new LinkedHashSet((Collection) list.interpret(this));
    }

    public Number number(ParseTree n) throws InterpretationException {
        String type = n.getTerminal().getName().toLowerCase();
        if (type.equals("integer")) return Integer.parseInt(n.getToken()); else if (type.equals("double")) return Double.parseDouble(n.getToken());
        throw new InterpretationException("Error: unable to parse number \"" + n + "\"");
    }

    public void domain(ParseTree keyword, ParseTree whitespace, ParseTree name, ParseTree optWhitespace, ParseTree set) throws InterpretationException {
        identifiers.put(name.getToken(), set.interpret(this));
    }

    public void macro(ParseTree keyword, ParseTree whitespace, ParseTree name, ParseTree whitespace2, ParseTree text, ParseTree endl) throws InterpretationException {
        macro(keyword, whitespace, name, null, null, null, null, whitespace2, text, endl);
    }

    public LinkedList<String> macrovariables(ParseTree optWhitespace, ParseTree variable, ParseTree optWhitespace2) throws InterpretationException {
        return macrovariables(optWhitespace, variable, optWhitespace2, null, null);
    }

    public LinkedList<String> macrovariables(ParseTree optWhitespace, ParseTree variable, ParseTree optWhitespace2, ParseTree comma, ParseTree nextVariable) throws InterpretationException {
        LinkedList<String> vars = new LinkedList<String>();
        vars.add(variable.getToken());
        if (nextVariable != null) {
            LinkedList<String> next = (LinkedList<String>) nextVariable.interpret(this);
            for (String s : next) vars.add(s);
        }
        return vars;
    }

    public void macro(ParseTree keyword, ParseTree whitespace, ParseTree name, ParseTree optWhitespace, ParseTree lParen, ParseTree variables, ParseTree rParen, ParseTree optWhitespace2, ParseTree text, ParseTree endl) throws InterpretationException {
        LinkedList<String> currentVariables = new LinkedList<String>();
        if (variables != null) currentVariables = (LinkedList<String>) variables.interpret(this);
        macros.add(new Macro(name.getToken(), currentVariables.toArray(new String[0]), text.getToken()));
    }

    String removeQuotes(String s) {
        if (s.trim().matches("\".*\"")) s = s.replaceAll("^[^\"]\"", "");
        return s;
    }

    public Object quotedtext(ParseTree lQuote, ParseTree text, ParseTree rQuote) {
        return text.getToken();
    }

    public Object assignment(ParseTree id, ParseTree optWhitespace, ParseTree eq, ParseTree optWhitespace2, ParseTree value) throws InterpretationException {
        Object v = value.interpret(this);
        if (id.getToken() != null) identifiers.put(id.getToken(), v);
        return v;
    }

    public Object identifier(ParseTree type) throws InterpretationException {
        String typename = type.getTerminal().getName().toLowerCase();
        if (typename.equals("new-class") || typename.equals("quoted-text") || typename.equals("set") || typename.equals("number")) {
            return type.interpret(this);
        } else if (typename.equals("null")) {
            return null;
        } else if (typename.equals("non-whitespace-non-comma-non-paren")) {
            if (identifiers.containsKey(type.getToken())) return identifiers.get(type.getToken());
        }
        throw new InterpretationException("Error: unknown identifier \"" + type.getToken() + "\"!");
    }

    public LinkedList classarguments(ParseTree optWhitespace, ParseTree argument, ParseTree optWhitespace2) throws InterpretationException {
        return classarguments(optWhitespace, argument, optWhitespace2, null, null, null);
    }

    public LinkedList classarguments(ParseTree optWhitespace, ParseTree argument, ParseTree optWhitespace2, ParseTree comma, ParseTree optWhitespace3, ParseTree cdr) throws InterpretationException {
        LinkedList list = new LinkedList();
        list.add(argument.interpret(this));
        if (cdr != null) {
            LinkedList remainder = (LinkedList) cdr.interpret(this);
            for (Object o : remainder) list.add(o);
        }
        return list;
    }

    public Object newclass(ParseTree optWhitespace, ParseTree newTag, ParseTree whitespace, ParseTree className, ParseTree optWhitespace2, ParseTree leftParen, ParseTree classArgs, ParseTree rightParen, ParseTree optWhitespace3) throws ClassNotFoundException, InterpretationException {
        String cn = className.getToken();
        Class c = null;
        ClassNotFoundException originalException = null;
        boolean triedProblemPackage = false;
        boolean triedDCOPolis = false;
        boolean triedJava = false;
        while (c == null && cn != null) {
            try {
                c = ClassLoader.getSystemClassLoader().loadClass(cn);
            } catch (ClassNotFoundException cnfe) {
                if (!triedProblemPackage) {
                    originalException = cnfe;
                    cn = problemClass.getPackage().getName() + "." + className.getToken();
                    triedProblemPackage = true;
                } else if (!triedDCOPolis) {
                    originalException = cnfe;
                    cn = "org.dcopolis.problem." + className.getToken();
                    triedDCOPolis = true;
                } else if (!triedJava) {
                    cn = "java.lang." + className.getToken();
                    triedJava = true;
                } else throw originalException;
            }
        }
        LinkedList args = (LinkedList) classArgs.interpret(this);
        Constructor cons[] = c.getConstructors();
        HashSet<Constructor> possibleConstructors = new HashSet<Constructor>();
        for (Constructor con : cons) if (con.getParameterTypes().length == args.size() || (con.getParameterTypes().length <= args.size() + 1 && con.isVarArgs())) possibleConstructors.add(con);
        if (possibleConstructors.isEmpty()) throw new InterpretationException(cn + " does not have a constructor that takes " + args.size() + " arguments!");
        Constructor constructor = null;
        if (possibleConstructors.size() == 1) {
            for (Constructor co : possibleConstructors) constructor = co;
        } else {
            Class parameterTypes[] = new Class[args.size()];
            int i = 0;
            for (Object o : args) {
                if (o == null) parameterTypes[i++] = null; else parameterTypes[i++] = o.getClass();
            }
            try {
                constructor = c.getConstructor(parameterTypes);
            } catch (NoSuchMethodException nsme) {
                if (parameterTypes.length <= 0) throw new InterpretationException(cn + " does not have a default constructor!", nsme);
                String types = "";
                for (i = 0; i < parameterTypes.length; i++) {
                    types += parameterTypes[i].getName();
                    if (i < parameterTypes.length - 1) types += ",";
                }
                throw new InterpretationException(cn + " does not have a constructor of the form (" + types + ")!", nsme);
            }
        }
        if (constructor == null) throw new InterpretationException("Could not find a valid constructor for " + cn + "!");
        try {
            Object a[] = args.toArray(new Object[0]);
            Class p[] = constructor.getParameterTypes();
            for (int i = 0; i < p.length; i++) {
                if (a[i] instanceof Number && !p[i].isAssignableFrom(a[i].getClass())) {
                    Number n = (Number) a[i];
                    if (p[i].equals(Integer.class)) a[i] = new Integer(n.intValue()); else if (p[i].equals(Long.class)) a[i] = new Long(n.longValue()); else if (p[i].equals(Byte.class)) a[i] = new Byte(n.byteValue()); else if (p[i].equals(Float.class)) a[i] = new Float(n.floatValue()); else if (p[i].equals(Double.class)) a[i] = new Double(n.doubleValue()); else if (p[i].equals(Short.class)) a[i] = new Short(n.shortValue());
                }
            }
            if (constructor.isVarArgs() && (a.length != p.length || (a.length == p.length && !a[a.length - 1].getClass().equals(p[p.length - 1])))) {
                Object varArg[] = (Object[]) Array.newInstance(p[p.length - 1].getComponentType(), a.length - p.length + 1);
                Object newArgs[] = new Object[p.length];
                for (int i = 0; i < p.length - 1; i++) newArgs[i] = a[i];
                for (int i = p.length - 1; i < a.length; i++) {
                    try {
                        varArg[i - (p.length - 1)] = a[i];
                    } catch (ArrayStoreException ase) {
                        throw new InterpretationException("Error: the constructor for " + cn + " takes varargs of type " + p[p.length - 1].getName() + ", however, argument " + a[i] + " is of type " + a[i].getClass().getName() + "!", ase);
                    }
                }
                newArgs[newArgs.length - 1] = (Object) varArg;
                a = newArgs;
            }
            Object o = constructor.newInstance(a);
            if (o instanceof Variable) variables.add((Variable) o);
            return o;
        } catch (Exception e) {
            throw new InterpretationException("Error instantiating class " + cn + "!", e);
        }
    }
}
