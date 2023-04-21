package util.LinerUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Liner Utility
 * 
 * @author wangyinqiu
 * @version 2010/08/12 v0.5
 */
@SuppressWarnings("unchecked")
public class LinerUtil {

    public static String BASE = "D:\\LOA4117\\";

    public static Class<? extends Annotation> positionClazz;

    static {
        File base = new File(BASE);
        if (!base.exists()) {
            base.mkdirs();
        }
    }

    private static void write(String path, String context) throws IOException {
        BufferedWriter bfw = new BufferedWriter(new FileWriter(path));
        bfw.write(context);
        bfw.flush();
        bfw.close();
    }

    /**
     * write a text file
     * 
     * @param path
     * @param context
     * @throws IOException
     */
    public static void writeFile(String path, String context) throws IOException {
        write(path, context);
    }

    /**
     * Lines writer.
     * Write a list of POJO to a comma-split plain text file.
     * This is same to <code>write(lies, clzOfLine, fileName, false)</code>.
     * No fields sorting before write.
     * 
     * @param <T>
     * @param lines
     * @param clzOfLine
     * @param fileName
     */
    public static <T> void write(List<T> lines, Class<? extends T> clzOfLine, String fileName) {
        write(lines, clzOfLine, fileName, false);
    }

    /**
     * Lines writer.
     * Write a list of POJO to a comma-split plain text file.
     * 
     * @param <T>
     * @param lines
     * @param clzOfLine
     * @param fileName
     * @param sort
     */
    public static <T> void write(List<T> lines, Class<? extends T> clzOfLine, String fileName, boolean sort) {
        writeDto(lines, clzOfLine, fileName, sort);
    }

    /**
     * Write only a line.
     * 
     * @param <T>
     * @param line
     * @param clzOfLine
     * @param fileName
     */
    public static <T> void write(T line, Class<? extends T> clzOfLine, String fileName) {
        List<T> lists = new ArrayList<T>();
        lists.add(line);
        write(lists, clzOfLine, fileName, false);
    }

    public static <T> void write(T line, Class<? extends T> clzOfLine, String fileName, boolean sort) {
        List<T> lists = new ArrayList<T>();
        lists.add(line);
        write(lists, clzOfLine, fileName, sort);
    }

    /**
     * Write DTO objects. Almost the same as write lines.
     * NOTICE:
     * If the DTO objects contains List, this function will write 
     * the list using recursive as the objects in the list are all DTOs.  
     * If the List contains itself may cause endless loop.
     * 
     * @param <T>
     * @param dtos
     * @param clzOfdto
     * @param fileName
     * @param sort
     */
    public static <T> void writeDto(List<T> dtos, Class<? extends T> clzOfdto, String fileName, boolean sort) {
        StringBuilder sb = new StringBuilder();
        Class<? extends T> clazz = clzOfdto;
        List<Field> fields = new ArrayList<Field>();
        for (int i = 0; i < clazz.getDeclaredFields().length; i++) {
            Field f = clazz.getDeclaredFields()[i];
            if (!Modifier.isStatic(f.getModifiers()) && !Modifier.isFinal(f.getModifiers())) {
                fields.add(f);
            }
        }
        if (sort) {
            Collections.sort(fields, new Comparator<Field>() {

                @Override
                public int compare(Field pO1, Field pO2) {
                    if (pO1.getAnnotations().length > 0 && pO2.getAnnotations().length > 0 && isQualfiedAnnotation(positionClazz)) {
                        int pos1 = getAnnotationValue(pO1, positionClazz);
                        int pos2 = getAnnotationValue(pO2, positionClazz);
                        if (pos1 > pos2) return 1; else if (pos1 < pos2) return -1; else return 0;
                    } else {
                        return pO1.getName().compareTo(pO2.getName());
                    }
                }

                private boolean isQualfiedAnnotation(Class<? extends Annotation> clz) {
                    if (getValueMethod(clz) == null) return false; else return true;
                }

                private Method getValueMethod(Class<? extends Annotation> clz) {
                    Method m = null;
                    if (clz == null) return m;
                    try {
                        m = clz.getMethod("value");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return m;
                }

                private int getAnnotationValue(Field field, Class<? extends Annotation> clz) {
                    int value = 0;
                    Method m = getValueMethod(clz);
                    if (m == null) return value;
                    m.setAccessible(true);
                    Object result = null;
                    try {
                        Object o = field.getAnnotation(clz);
                        result = m.invoke(o);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (result == null) return value;
                    value = Integer.parseInt(result.toString());
                    return value;
                }
            });
        }
        for (Field f : fields) {
            sb.append(f.getName());
            if (fields.indexOf(f) != fields.size() - 1) {
                sb.append(",");
            }
        }
        sb.append(System.getProperty("line.separator"));
        List<ListToWritePare> listToWrite = new ArrayList<ListToWritePare>();
        for (T line : dtos) {
            for (Field f : fields) {
                try {
                    f.setAccessible(true);
                    sb.append("\"");
                    Object o = f.get(line);
                    if (o instanceof List<?>) {
                        ListToWritePare pare = new ListToWritePare();
                        pare.list = (List<?>) o;
                        pare.name = f.getName() + "_0x" + Integer.toHexString(o.hashCode()).toUpperCase();
                        listToWrite.add(pare);
                        sb.append(pare.name);
                    } else {
                        sb.append(o);
                    }
                    sb.append("\"");
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } finally {
                    if (fields.indexOf(f) != fields.size() - 1) {
                        sb.append(",");
                    }
                }
            }
            sb.append(System.getProperty("line.separator"));
        }
        try {
            write(BASE + fileName + ".csv", sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (listToWrite.size() > 0) {
            for (ListToWritePare pare : listToWrite) {
                writeDto((List) pare.list, pare.list.get(0).getClass(), fileName + "_" + pare.name, sort);
            }
        }
    }

    public static <T> void writeDto(List<T> dtos, Class<? extends T> clzOfdto, String fileName) {
        writeDto(dtos, clzOfdto, fileName, false);
    }

    /**
     * Write only a DTO.  
     * 
     * @param <T>
     * @param dtos
     * @param clzOfdto
     * @param fileName
     * @param sort
     */
    public static <T> void writeDto(T dtos, Class<? extends T> clzOfdto, String fileName) {
        List<T> lists = new ArrayList<T>();
        lists.add(dtos);
        writeDto(lists, clzOfdto, fileName, false);
    }

    public static <T> void writeDto(T dtos, Class<? extends T> clzOfdto, String fileName, boolean sort) {
        List<T> lists = new ArrayList<T>();
        lists.add(dtos);
        writeDto(lists, clzOfdto, fileName);
    }

    /**
     * Set the base folder of the file to write.
     * @param path
     */
    public static void setBASE(String path) {
        if (!path.trim().endsWith("\\") && !path.trim().endsWith("/")) {
            path = path.trim() + "\\";
        }
        BASE = path;
    }

    /**
     * Copy object's fields to another object.
     * Shallow copy! 
     * @param from
     * @param to
     */
    public static void copyFields(final Object from, final Object to) {
        copyFields(from, to, null);
    }

    /**
     * Copy object's fields to another object.
     * Shallow copy! 
     * Excluding <code>fields</code>.
     * @param from
     * @param to
     * @param fields
     */
    public static void copyFields(final Object from, final Object to, String[] fields) {
        Class<?> fromClzz = from.getClass();
        Class<?> toClzz = to.getClass();
        List<String> fieldslist = null;
        if (fields != null) {
            fieldslist = new ArrayList<String>();
            Collections.addAll(fieldslist, fields);
        }
        for (Field f : fromClzz.getDeclaredFields()) {
            if (fieldslist != null && !fieldslist.contains(f.getName())) {
                continue;
            }
            if (!isFinal(f) && !isStatic(f)) {
                try {
                    findAndSet(f, toClzz.getDeclaredFields(), from, to);
                } catch (SecurityException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void findAndSet(Field from, Field[] targets, Object from_o, Object to_o) throws IllegalArgumentException, IllegalAccessException {
        for (Field to : targets) {
            if (to.getName().equals(from.getName())) {
                if (to.getType().equals(from.getType())) {
                    to.setAccessible(true);
                    from.setAccessible(true);
                    to.set(to_o, from.get(from_o));
                    break;
                }
            }
        }
    }

    private static boolean isFinal(Field f) {
        return Modifier.isFinal(f.getModifiers());
    }

    private static boolean isStatic(Field f) {
        return Modifier.isStatic(f.getModifiers());
    }

    /**
     * Print the object to the standard system out.
     * @param o
     */
    public static void printObject(Object o) {
        Class<?> c = o.getClass();
        println(o.toString());
        for (Field f : c.getDeclaredFields()) {
            f.setAccessible(true);
            print(Modifier.toString(f.getModifiers()));
            print(" ");
            print(f.getType().getName());
            print(" ");
            print(f.getName());
            print(" = ");
            try {
                print(f.get(o));
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                print(" null;");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                print(" [unknown];");
            }
            println();
        }
        println();
    }

    public static void println(Object o) {
        System.out.println(o);
    }

    public static void println() {
        System.out.println();
    }

    public static void print(Object o) {
        System.out.print(o);
    }

    static class ListToWritePare {

        public String name;

        public List<?> list;

        ListToWritePare(String name, List<?> list) {
            this.name = name;
            this.list = list;
        }

        public ListToWritePare() {
        }
    }
}
