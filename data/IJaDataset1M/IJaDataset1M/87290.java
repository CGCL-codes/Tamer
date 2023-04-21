package codesounding.dynamic;

import java.io.*;
import java.net.URL;
import java.util.Properties;

public class BasicProcessor {

    public static final String PROCESSOR_TYPE = "PROCESSOR.TYPE";

    public static final String CONFIG_FILE = "CONFIG_FILE";

    public static final String DEFAULT_CONFIG_FILE = "codesounding.properties";

    public static final String OUTPUT_FILE = "OUTPUT_FILE";

    protected static final String HEADER = "basic.header";

    protected static final String VAR_DECLARATION = "basic.var_declaration";

    protected static final String BREAK = "basic.break";

    protected static final String CONTINUE = "basic.continue";

    protected static final String DO = "basic.do";

    protected static final String FOR = "basic.for";

    protected static final String WHILE = "basic.while";

    protected static final String START_BLOCK = "basic.start_block";

    protected static final String END_BLOCK = "basic.end_block";

    protected static final String IF = "basic.if";

    protected static final String RETURN = "basic.return";

    protected static final String THROW = "basic.throw";

    private static BasicProcessor instance = null;

    private PrintWriter writer = null;

    private Properties propFile;

    private String outputPath = "";

    private boolean configLoaded = false;

    protected static Object createObj(String classname) {
        Object res = null;
        try {
            Class classDefinition = Class.forName(classname.trim());
            res = classDefinition.newInstance();
        } catch (InstantiationException e) {
            System.out.println(e);
        } catch (IllegalAccessException e) {
            System.out.println(e);
        } catch (ClassNotFoundException e) {
            System.out.println(e);
        }
        return res;
    }

    private static BasicProcessor create() {
        String type = System.getProperty(PROCESSOR_TYPE);
        BasicProcessor res = new BasicProcessor();
        if (res.getConfigProperty(PROCESSOR_TYPE) != null && !res.getConfigProperty(PROCESSOR_TYPE).equals("")) {
            type = res.getConfigProperty(PROCESSOR_TYPE);
        }
        if (type == null || type.equals("")) {
        } else {
            res = (BasicProcessor) createObj(type);
        }
        res.setMarkfilePath();
        return res;
    }

    public static synchronized BasicProcessor getInstance() {
        if (instance == null) {
            instance = create();
        }
        return instance;
    }

    private void setMarkfilePath() {
        String newPath = getConfigProperty(OUTPUT_FILE);
        if (newPath == null || newPath.equals("")) {
            newPath = System.getProperty(OUTPUT_FILE);
        }
        if (newPath != null && !newPath.equals("")) {
            outputPath = newPath;
            try {
                String parentFolder = new File(outputPath).getParent();
                if (parentFolder != null) {
                    new File(parentFolder).mkdirs();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                outputPath = new java.io.File(".").getCanonicalPath() + File.separatorChar + "javamarker.out";
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void reloadConfig() {
        String config = System.getProperty(CONFIG_FILE);
        if (config == null || config.equals("")) {
            config = DEFAULT_CONFIG_FILE;
        }
        if (new File(config).exists()) {
            try {
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(config));
                propFile.load(bis);
                bis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                URL url = Thread.currentThread().getContextClassLoader().getResource(config);
                if (url != null) {
                    InputStream inputStream = url.openStream();
                    propFile.load(inputStream);
                    inputStream.close();
                }
            } catch (Exception e) {
            }
        }
    }

    protected String getOutputFile() {
        return outputPath;
    }

    private void initDefaultValues() {
        String head = "X: 1 % inizio testata\r\n";
        head += "K: C % scala: do maggiore";
        propFile = new Properties();
        propFile.setProperty(HEADER, head);
        propFile.setProperty(VAR_DECLARATION, "A");
        propFile.setProperty(BREAK, " ");
        propFile.setProperty(CONTINUE, "'");
        propFile.setProperty(DO, "F");
        propFile.setProperty(FOR, "E");
        propFile.setProperty(WHILE, "G");
        propFile.setProperty(START_BLOCK, "B");
        propFile.setProperty(END_BLOCK, "C");
        propFile.setProperty(IF, "D");
        propFile.setProperty(RETURN, "z");
        propFile.setProperty(THROW, ",");
    }

    protected PrintWriter getNewWriter(String aFilename) throws Exception {
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new BufferedWriter(new FileWriter(aFilename, true)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pw;
    }

    protected PrintWriter getNewWriter() throws Exception {
        return getNewWriter(outputPath);
    }

    public String getConfigProperty(String propName) {
        if (!configLoaded) {
            configLoaded = true;
            initDefaultValues();
            reloadConfig();
        }
        String res = propFile.getProperty(propName);
        return res == null ? "" : res;
    }

    protected void writeToFile(String msg) {
        try {
            if (writer == null) {
                writer = getNewWriter();
                writer.println(getConfigProperty(HEADER));
            }
            writer.print(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getVarDeclaration() {
        writeToFile(getConfigProperty(VAR_DECLARATION));
    }

    public void getStartBlock() {
        writeToFile(getConfigProperty(START_BLOCK));
    }

    public void getEndBlock() {
        writeToFile(getConfigProperty(END_BLOCK));
    }

    public void getIfStatement() {
        writeToFile(getConfigProperty(IF));
    }

    public void getForStatement() {
        writeToFile(getConfigProperty(FOR));
    }

    public void getDoStatement() {
        writeToFile(getConfigProperty(DO));
    }

    public void getWhileStatement() {
        writeToFile(getConfigProperty(WHILE));
    }

    public void getReturnStatement() {
        writeToFile(getConfigProperty(RETURN));
    }

    public void getBreakStatement() {
        writeToFile(getConfigProperty(BREAK));
    }

    public void getContinueStatement() {
        writeToFile(getConfigProperty(CONTINUE));
    }

    public void getThrowStatement() {
        writeToFile(getConfigProperty(THROW));
    }
}
