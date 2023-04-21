package org.bee.tl.core;

import java.lang.reflect.Method;
import org.bee.tl.core.compile.PreCompileException;

public class BeetlUtil {

    /**
	 * 获得模板文件编译成class的包名和类名。
	 * @param key 路径/模板文件
	 * @return
	 */
    public static String[] getPackage2Class(String key) {
        char[] cs = key.toCharArray();
        StringBuilder pkg = new StringBuilder();
        for (char c : cs) {
            if (c == '\\' || c == '/') {
                if (pkg.length() != 0) {
                    pkg.append(".");
                }
            } else if (c == '.') {
                pkg.append("_");
            } else if (c == ' ') {
                pkg.append("__");
            } else {
                pkg.append(c);
            }
        }
        int lastPeriodIndex = pkg.lastIndexOf(".");
        if (lastPeriodIndex == -1) {
            return new String[] { "", pkg.toString() };
        }
        return new String[] { pkg.substring(0, lastPeriodIndex), pkg.substring(lastPeriodIndex + 1) };
    }

    public static String getClassFullName(String pkg, String name) {
        StringBuilder sb = new StringBuilder();
        if (pkg.length() != 0) {
            sb.append(pkg).append(".");
        }
        sb.append(name);
        return sb.toString();
    }

    public static boolean isClassName(String name) {
        char c = name.charAt(0);
        if (Character.isUpperCase(c)) {
            return true;
        } else {
            return false;
        }
    }

    public static String getEscapeString(String escapeString) {
        char[] cs = escapeString.toCharArray();
        StringBuilder sb = new StringBuilder();
        int length = cs.length - 1;
        for (int i = 1; i < length; i++) {
            if (cs[i] != '\\') {
                sb.append(cs[i]);
            }
        }
        return sb.toString();
    }

    public static String addEscpage(String str) {
        char[] cs = str.toCharArray();
        StringBuilder sb = new StringBuilder("\"");
        int length = cs.length;
        for (int i = 0; i < length; i++) {
            if (cs[i] == '\"' || cs[i] == '\t' || cs[i] == '\n') {
                sb.append("\\");
            }
            sb.append(cs[i]);
        }
        sb.append("\"");
        return sb.toString();
    }

    public static String getFunctionFullName(BeeCommonNodeTree node) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < node.getChildCount(); i++) {
            sb.append(((BeeCommonNodeTree) node.getChild(i)).getToken().getText());
            sb.append(".");
        }
        sb.setLength(sb.length() - 1);
        return sb.toString();
    }

    public static void main(String[] args) {
        String str = "'abc\\\"gef'";
        String result = getEscapeString(str);
        System.out.println(result);
    }

    public static Method findMethod(Class target, String methodName, Class[] parameterType, BeeCommonNodeTree exp, boolean throwRuntime) {
        if (exp.getCached() == null) {
            Method[] ms = target.getMethods();
            Method temp = null;
            for (int i = 0; i < ms.length; i++) {
                temp = ms[i];
                if (temp.getName().equals(methodName)) {
                    Class[] paras = temp.getParameterTypes();
                    boolean isMatch = true;
                    if (paras.length == parameterType.length) {
                        for (int j = 0; j < paras.length; j++) {
                            if (!paras[j].isAssignableFrom(parameterType[j])) {
                                isMatch = false;
                            }
                        }
                        if (isMatch) {
                            exp.setCached(temp);
                            return temp;
                        }
                    }
                }
            }
            if (throwRuntime) throw new BeeRuntimeException(BeeRuntimeException.NATIVE_CALL_INVALID, exp.getToken(), methodName); else throw new PreCompileException("未找到本地方法调用，在行" + exp.getToken().getLine());
        } else {
            Method m = (Method) exp.getCached();
            if (m != null) {
                return m;
            } else {
                if (throwRuntime) throw new BeeRuntimeException(BeeRuntimeException.NATIVE_CALL_INVALID, exp.getToken(), methodName); else throw new PreCompileException("未找到本地方法调用，在行" + exp.getToken().getLine());
            }
        }
    }
}
