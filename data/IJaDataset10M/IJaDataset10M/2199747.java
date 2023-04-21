package net.infian.framework.util.generator;

import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import net.infian.framework.FrameworkConstants;

/**
 * This provides some ways to automatically generate the code for Daos.
 */
public final class DaoGenerator {

    /**
	 * This is a simple main method for generating a Dao.  The generated code is output to the console.
	 * @param args package name followed by Dao name followed by column class and column name pairs.
	 */
    public static final void main(final String[] args) {
        System.out.print(generateDao(args[0], args[1], Arrays.asList(args).subList(2, args.length)));
    }

    /**
	 * This generates Dao code.
	 * @param packageName name of the package
	 * @param className name of the Dao
	 * @param columnData List of column class and column name pairs
	 * @return String representation of the generated code
	 */
    public static final String generateDao(final String packageName, final String className, final List<String> columnData) {
        ListIterator<String> literator = columnData.listIterator();
        String columnClass;
        String columnName;
        StringBuffer javaName;
        StringBuffer cappedJavaName;
        StringBuffer code = new StringBuffer();
        code.append("package ").append(packageName).append(';').append(FrameworkConstants.newLine);
        code.append(FrameworkConstants.newLine);
        code.append("/**").append(FrameworkConstants.newLine);
        code.append(" * This ").append(className).append(" was created using the InfiaNet (http://infian.net) DaoGenerator.").append(FrameworkConstants.newLine);
        code.append(" */").append(FrameworkConstants.newLine);
        code.append("@SuppressWarnings(\"serial\")").append(FrameworkConstants.newLine);
        code.append("public final class ").append(className).append(" extends net.infian.framework.db.dao.Dao {").append(FrameworkConstants.newLine);
        code.append("	/**").append(FrameworkConstants.newLine);
        code.append("	 * This calls its corresponding constructor from the Dao.").append(FrameworkConstants.newLine);
        code.append("	 * @param info DaoInfo for the Dao").append(FrameworkConstants.newLine);
        code.append("	 */").append(FrameworkConstants.newLine);
        code.append("	public ").append(className).append("(net.infian.framework.db.dao.DaoInfo info) {").append(FrameworkConstants.newLine);
        code.append("		super(info);").append(FrameworkConstants.newLine);
        code.append("	}").append(FrameworkConstants.newLine);
        code.append(FrameworkConstants.newLine);
        code.append("	/**").append(FrameworkConstants.newLine);
        code.append("	 * This calls its corresponding constructor from the Dao.").append(FrameworkConstants.newLine);
        code.append("	 * @param dao name of the Dao").append(FrameworkConstants.newLine);
        code.append("	 */").append(FrameworkConstants.newLine);
        code.append("	public ").append(className).append("(String dao) {").append(FrameworkConstants.newLine);
        code.append("		super(dao);").append(FrameworkConstants.newLine);
        code.append("	}").append(FrameworkConstants.newLine);
        while (literator.hasNext()) {
            columnClass = literator.next();
            columnName = literator.next();
            javaName = new StringBuffer(columnName.toLowerCase());
            for (int i = javaName.length(); --i >= 0; ) {
                if (javaName.charAt(i) == '_') {
                    javaName.deleteCharAt(i);
                    javaName.setCharAt(i, Character.toUpperCase(javaName.charAt(i)));
                }
            }
            cappedJavaName = new StringBuffer(javaName.toString());
            cappedJavaName.setCharAt(0, Character.toUpperCase(cappedJavaName.charAt(0)));
            code.append(FrameworkConstants.newLine);
            code.append("	/**").append(FrameworkConstants.newLine);
            code.append("	 * This gets the value of the \"").append(columnName).append("\" column from the Dao.").append(FrameworkConstants.newLine);
            code.append("	 * @return value of \"").append(columnName).append('"').append(FrameworkConstants.newLine);
            code.append("	 */").append(FrameworkConstants.newLine);
            code.append("	public final ").append(columnClass).append(" get").append(cappedJavaName).append("() {").append(FrameworkConstants.newLine);
            code.append("		return (").append(columnClass).append(") get(\"").append(columnName).append("\");").append(FrameworkConstants.newLine);
            code.append("	}").append(FrameworkConstants.newLine);
            code.append(FrameworkConstants.newLine);
            code.append("	/**").append(FrameworkConstants.newLine);
            code.append("	 * This sets the value of the \"").append(columnName).append("\" column in the Dao.").append(FrameworkConstants.newLine);
            code.append("	 * @param ").append(javaName).append(" value of \"").append(columnName).append("\" to be set").append(FrameworkConstants.newLine);
            code.append("	 */").append(FrameworkConstants.newLine);
            code.append("	public final void set").append(cappedJavaName).append('(').append(columnClass).append(' ').append(javaName).append(") {").append(FrameworkConstants.newLine);
            code.append("		put(\"").append(columnName).append("\", ").append(javaName).append(");").append(FrameworkConstants.newLine);
            code.append("	}").append(FrameworkConstants.newLine);
        }
        code.append("}");
        return code.toString();
    }
}
