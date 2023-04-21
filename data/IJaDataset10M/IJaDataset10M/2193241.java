package fitlibrary.parser.lookup;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.beans.PropertyEditorSupport;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import fitlibrary.exception.parse.CouldNotParseException;
import fitlibrary.parser.Parser;
import fitlibrary.traverse.Evaluator;
import fitlibrary.typed.Typed;

public class LookupPropertyEditorBasedParser {

    static Set<Class<?>> nullables = new HashSet<Class<?>>();

    static {
        nullables.add(Byte.class);
        nullables.add(Short.class);
        nullables.add(Integer.class);
        nullables.add(Long.class);
        nullables.add(Float.class);
        nullables.add(Double.class);
        nullables.add(Date.class);
    }

    public static ParserFactory parserFactory(final Typed typed) {
        final PropertyEditor editor = findPropertyEditor(typed.asClass());
        if (editor != null) return new ParserFactory() {

            @Override
            public Parser parser(Evaluator evaluator, Typed typed2) {
                return new PropertyEditorBasedParser(evaluator, typed2, editor, nullables.contains(typed.asClass()));
            }
        };
        return null;
    }

    private static PropertyEditor findPropertyEditor(Class<?> type) {
        if (type == Class.class) return new ClassPropertyEditor();
        if (type == Integer.class) return PropertyEditorManager.findEditor(int.class);
        if (type == char.class || type == Character.class) return new CharPropertyEditor();
        if (type == boolean.class || type == Boolean.class) return new BooleanPropertyEditor();
        return PropertyEditorManager.findEditor(type);
    }

    public static class ClassPropertyEditor extends PropertyEditorSupport {

        @Override
        public void setAsText(String text) throws IllegalArgumentException {
            try {
                setValue(Class.forName(text));
            } catch (ClassNotFoundException e) {
                throw new CouldNotParseException(Class.class, text);
            } catch (NoClassDefFoundError e) {
                throw new CouldNotParseException(Class.class, text);
            }
        }
    }

    public static class CharPropertyEditor extends PropertyEditorSupport {

        @Override
        public void setAsText(String text) throws IllegalArgumentException {
            if ("".equals(text)) setValue(Character.valueOf(' ')); else setValue(Character.valueOf(text.charAt(0)));
        }
    }

    public static class BooleanPropertyEditor extends PropertyEditorSupport {

        @Override
        public void setAsText(String textInitially) throws IllegalArgumentException {
            String text = textInitially.toLowerCase();
            boolean bool = (text.equals("true") || text.equals("yes") || text.equals("1") || text.equals("y") || text.equals("+"));
            setValue(Boolean.valueOf(bool));
        }
    }

    public static class DatePropertyEditor extends PropertyEditorSupport {

        private final DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.SHORT);

        @Override
        public void setAsText(String text) throws IllegalArgumentException {
            try {
                setValue(dateFormatter.parse(text));
            } catch (ParseException e) {
                throw new IllegalArgumentException(text);
            }
        }
    }
}
