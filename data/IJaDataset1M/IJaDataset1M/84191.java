package classLoader.construction;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Set;
import common.CommonConstants;
import common.Config;

public class ClassConstructor {

    public void sendBytesForClass(String p_strClassName, String p_strClassData) {
        int iIndx;
        if (!p_strClassName.equals(CommonConstants.FIRST_ELE)) {
            Set createdClasses = m_htFileHandles.keySet();
            BufferedWriter out = null;
            if (createdClasses.contains(p_strClassName)) out = m_htFileHandles.get(p_strClassName); else out = createFile(p_strClassName);
            try {
                if ((iIndx = ifPresentinKeys(CommonConstants.KEY_TYPES, p_strClassData)) == -1) {
                    String strVarIndx = getVarIndx();
                    out.write(p_strClassData + SPACE + p_strClassData + strVarIndx + COMMA + CRLF);
                    generateGetAndSet(p_strClassData, strVarIndx, out);
                } else {
                    String strVarIndx = getVarIndx();
                    switch(iIndx) {
                        case 0:
                            out.write("int" + SPACE + INT_VAL + VALUE + strVarIndx + COMMA + CRLF);
                            generateGetAndSetKeys(INT_VAL + VALUE, strVarIndx, out, 'i');
                            break;
                        case 1:
                            out.write("String" + SPACE + VALUE + strVarIndx + COMMA + CRLF);
                            generateGetAndSetKeys(VALUE, strVarIndx, out, 's');
                            break;
                        case 2:
                            out.write("String" + SPACE + VALUE + strVarIndx + COMMA + CRLF);
                            generateGetAndSetKeys(VALUE, strVarIndx, out, 's');
                            break;
                        case 3:
                            out.write("String" + SPACE + VALUE + strVarIndx + COMMA + CRLF);
                            generateGetAndSetKeys(VALUE, strVarIndx, out, 's');
                            break;
                    }
                }
                m_htFileHandles.put(p_strClassName, out);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void generateGetAndSet(String p_strName, String p_strVarIndx, BufferedWriter p_Out) throws IOException {
        p_Out.write(PUBLIC + SPACE + VOID + SPACE + "set" + p_strName + p_strVarIndx + "(" + PARAM_Object + SPACE + PARAM_VAL + "){");
        p_Out.write(CRLF + p_strName + p_strVarIndx + SPACE + "=" + SPACE + "(" + p_strName + ")" + PARAM_VAL + COMMA + CRLF + "}");
        p_Out.write(CRLF);
        p_Out.write(PUBLIC + SPACE + PARAM_Object + SPACE + "get" + p_strName + p_strVarIndx + "(){");
        p_Out.write(CRLF + "return" + SPACE + p_strName + p_strVarIndx + COMMA + CRLF + "}");
    }

    private void generateGetAndSetKeys(String p_strName, String p_strVarIndx, BufferedWriter p_Out, char p_chVar) throws IOException {
        String strParam = null;
        if (p_chVar == 'i') strParam = PARAM_Int; else if (p_chVar == 's') strParam = PARAM_String;
        p_Out.write(PUBLIC + SPACE + VOID + SPACE + "set" + p_strName + p_strVarIndx + "(" + strParam + SPACE + PARAM_VAL + "){");
        p_Out.write(CRLF + p_strName + p_strVarIndx + SPACE + "=" + SPACE + PARAM_VAL + COMMA + CRLF + "}");
        p_Out.write(CRLF);
        p_Out.write(PUBLIC + SPACE + strParam + SPACE + "get" + p_strName + p_strVarIndx + "(){");
        p_Out.write(CRLF + "return" + SPACE + p_strName + p_strVarIndx + COMMA + CRLF + "}");
    }

    private void generateGetAndSetTexts(String p_strName, BufferedWriter p_Out, char p_chVar) throws IOException {
        String strParam = null;
        if (p_chVar == 'i') strParam = PARAM_Int; else if (p_chVar == 's') strParam = PARAM_String;
        p_Out.write(PUBLIC + SPACE + VOID + SPACE + "set" + p_strName + "(" + strParam + SPACE + PARAM_VAL + "){");
        p_Out.write(CRLF + p_strName + SPACE + "=" + SPACE + PARAM_VAL + COMMA + CRLF + "}");
        p_Out.write(CRLF);
        p_Out.write(PUBLIC + SPACE + strParam + SPACE + "get" + p_strName + "(){");
        p_Out.write(CRLF + "return" + SPACE + p_strName + COMMA + CRLF + "}");
    }

    private BufferedWriter createFile(String p_strFileName) {
        BufferedWriter out = null;
        Set createdClasses = m_htFileHandles.keySet();
        try {
            if (createdClasses.contains(p_strFileName)) {
                out = m_htFileHandles.get(p_strFileName);
                return out;
            }
            out = new BufferedWriter(new FileWriter(Config.readProperty("CLASSFILES_PATH") + p_strFileName + ".java", true));
            out.write("package " + CommonConstants.CLASSFILES_PACKAGE + COMMA + CRLF);
            out.write("public class " + p_strFileName + " {" + CRLF);
            out.write(PARAM_String + SPACE + CommonConstants.TEXT_VALUE + COMMA + CRLF);
            generateGetAndSetTexts(CommonConstants.TEXT_VALUE, out, 's');
            m_htFileHandles.put(p_strFileName, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out;
    }

    private boolean compile(String javafile) throws IOException {
        System.out.println("Compiling " + javafile + "...");
        Process P = Runtime.getRuntime().exec("javac " + javafile);
        try {
            P.waitFor();
        } catch (InterruptedException ie) {
            System.out.println(ie);
        }
        int ret = P.exitValue();
        return ret == 0;
    }

    public String getVarIndx() {
        return String.valueOf(VariableIndex++);
    }

    public void completeClassSyntax() throws IOException {
        BufferedWriter out = null;
        Enumeration enumKeys = m_htFileHandles.keys();
        while (enumKeys.hasMoreElements()) {
            out = m_htFileHandles.get(enumKeys.nextElement());
            out.append("}");
            out.close();
        }
    }

    public int ifPresentinKeys(String[] p_arrKeys, String p_strKey) {
        int iLen = p_arrKeys.length;
        while (iLen-- > 0) {
            if (p_arrKeys[iLen].equals(p_strKey)) return iLen;
        }
        return -1;
    }

    public void createDummyClasses(String p_strFilename) {
        if ((ifPresentinKeys(CommonConstants.KEY_TYPES, p_strFilename)) == -1) createFile(p_strFilename);
    }

    public int VariableIndex = 0;

    public static String SPACE = CommonConstants.SPACE;

    public static String VALUE = CommonConstants.VALUE;

    public static String INT_VAL = CommonConstants.INT_VAL;

    public Hashtable<String, BufferedWriter> m_htFileHandles = new Hashtable<String, BufferedWriter>();

    public static String CRLF = CommonConstants.CRLF;

    public static String COMMA = CommonConstants.COMMA;

    public static String PARAM_String = CommonConstants.PARAM_STRING;

    public static String PARAM_Object = CommonConstants.PARAM_Object;

    public static String PARAM_Int = CommonConstants.PARAM_Int;

    public static String PARAM_VAL = CommonConstants.PARAM_VAL;

    public static String PUBLIC = "public";

    public static String VOID = "void";
}
