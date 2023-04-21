package org.gocha.jdk;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.gocha.files.FileUtil;
import org.gocha.jvm.MemoryByteCode;
import org.gocha.jvm.MemoryClassLoader;
import org.gocha.jvm.MemoryJavaSource;
import org.gocha.text.TextUtil;

/**
 * Класс помошник для реализации локализованых сообщений.
 * @author gocha
 */
public class LocalMessagesImplementor extends TemplateCompiler {

    protected File messageDirectory = null;

    protected String messageFilePrefix = "messages";

    protected Pattern messageFilePattern = null;

    /**
     * Формат файла сообщений
     */
    public static enum FileFormat {

        /**
         * XML
         * @see org.gocha.files.FileUtil#readXMLProperties(java.io.InputStream) 
         */
        xml, /**
         * .properties
         */
        properties;

        /**
         * Возвращает расширение файла
         * @param format Формат
         * @return Расширение
         */
        public static String getFileExtension(FileFormat format) {
            if (format == null) {
                throw new IllegalArgumentException("format==null");
            }
            switch(format) {
                case properties:
                    return "proerties";
                case xml:
                    return "xml";
            }
            return null;
        }
    }

    protected FileFormat fileFormat = FileFormat.xml;

    public FileFormat getFileFormat() {
        return fileFormat;
    }

    public void setFileFormat(FileFormat fileFormat) {
        this.fileFormat = fileFormat;
    }

    public File getMessageDirectory() {
        return messageDirectory;
    }

    public void setMessageDirectory(File messageDirectory) {
        this.messageDirectory = messageDirectory;
    }

    public String getMessageFilePrefix() {
        return messageFilePrefix;
    }

    public void setMessageFilePrefix(String messageFilePrefix) {
        this.messageFilePrefix = messageFilePrefix;
    }

    protected boolean cacheByteCode = true;

    public boolean isCacheByteCode() {
        return cacheByteCode;
    }

    public void setCacheByteCode(boolean cacheByteCode) {
        this.cacheByteCode = cacheByteCode;
    }

    protected File classRootDirectory = null;

    public File getClassRootDirectory() {
        return classRootDirectory;
    }

    public void setClassRootDirectory(File classRootDirectory) {
        this.classRootDirectory = classRootDirectory;
    }

    protected void initMessageFilePattern() {
        String pref = getMessageFilePrefix();
        if (pref == null) throw new IllegalStateException("getMessageFilePrefix() == null");
        FileFormat ff = getFileFormat();
        if (ff == null) throw new IllegalStateException("getFileFormat() == null");
        Pattern messPattern = Pattern.compile("(?is)^" + Pattern.quote(pref) + "([\\-_\\s]([a-zA-Z_]+))?\\.(" + FileFormat.getFileExtension(ff) + ")$");
        this.messageFilePattern = messPattern;
    }

    protected File getDefaultMessagesFile() {
        File mesDir = getMessageDirectory();
        if (mesDir == null) throw new IllegalStateException("getMessageDirectory() == null");
        String pref = getMessageFilePrefix();
        if (pref == null) throw new IllegalStateException("getMessageFilePrefix() == null");
        FileFormat ff = getFileFormat();
        if (ff == null) throw new IllegalStateException("getFileFormat() == null");
        return new File(mesDir, pref + "." + FileFormat.getFileExtension(ff));
    }

    protected File getMessagesFile() {
        File mesDir = getMessageDirectory();
        if (mesDir == null) return null;
        if (!mesDir.exists()) return null;
        if (!mesDir.isDirectory()) return null;
        if (messageFilePattern == null) initMessageFilePattern();
        Pattern mesFilePtrn = messageFilePattern;
        File messagesf = null;
        File messagesfdef = null;
        Locale currentLocal = Locale.getDefault();
        for (File f : mesDir.listFiles()) {
            if (!f.isFile()) continue;
            String name = f.getName();
            Matcher m = mesFilePtrn.matcher(name);
            if (m.matches()) {
                try {
                    String loc = m.group(2);
                    if (loc != null) {
                        Locale cmpLoc = new Locale(loc);
                        if (currentLocal.getLanguage().equals(cmpLoc.getLanguage())) {
                            messagesf = f;
                            break;
                        }
                    } else {
                        messagesfdef = f;
                    }
                } catch (Throwable ex) {
                    continue;
                }
            }
        }
        if (messagesf == null && messagesfdef != null) messagesf = messagesfdef;
        return messagesf;
    }

    protected Map<String, String> loadMessagesMap() {
        File messF = getMessagesFile();
        if (messF == null) return null;
        try {
            Properties p = null;
            p = FileUtil.readProperties(messF);
            Map<String, String> res = new HashMap<String, String>();
            for (String k : p.stringPropertyNames()) {
                String v = p.getProperty(k);
                res.put(k, v);
            }
            return res;
        } catch (IOException ex) {
            Logger.getLogger(LocalMessagesImplementor.class.getName()).log(Level.WARNING, null, ex);
            return null;
        }
    }

    protected boolean saveDefaultMessageMap(Map<String, String> map) {
        if (map == null) {
            throw new IllegalArgumentException("map==null");
        }
        File defFile = getDefaultMessagesFile();
        if (!defFile.getParentFile().exists()) {
            if (!defFile.getParentFile().mkdirs()) {
                Logger.getLogger(LocalMessagesImplementor.class.getName()).log(Level.WARNING, "can't create dir:" + defFile.getParentFile());
                return false;
            }
        }
        Properties defTemplates = new Properties();
        for (Map.Entry<String, String> en : map.entrySet()) {
            defTemplates.put(en.getKey(), en.getValue());
        }
        try {
            FileUtil.writeProperties(defFile, defTemplates);
        } catch (IOException ex) {
            Logger.getLogger(LocalMessagesImplementor.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    private Map<String, String> dummyMessagesMap() {
        return new HashMap<String, String>();
    }

    protected Object instance = null;

    public Object getInstance() {
        if (instance != null) return instance;
        if (isCacheByteCode()) {
            CompileResult cr = loadFromCache();
            if (cr != null) {
                instance = cr.getInstance();
            } else {
                cr = compileInstance();
                instance = cr.getInstance();
                saveToCache(cr);
            }
        } else {
            CompileResult cr = compileInstance();
            instance = cr.getInstance();
        }
        return instance;
    }

    protected File getJavaSourceFile() {
        File clsRoot = getClassRootDirectory();
        if (clsRoot == null) throw new IllegalStateException("getClassRootDirectory()==null");
        File res = clsRoot;
        String clsName = getClassName();
        if (clsName == null) throw new IllegalStateException("getClassName()==null");
        String pkgName = getPackageName();
        if (pkgName != null) {
            for (String dir : TextUtil.split(pkgName, ".")) {
                res = new File(res, dir);
            }
        }
        res = new File(res, getShortClassName() + ".java");
        return res;
    }

    protected File getBytecodeFile() {
        File res = getJavaSourceFile();
        res = new File(res.getParentFile(), getShortClassName() + ".class");
        return res;
    }

    protected TemplateCompiler.CompileResult compileInstance() {
        Map<String, String> mmap = loadMessagesMap();
        if (mmap == null) mmap = dummyMessagesMap();
        TemplateCompiler.CompileResult compileRes = compile(mmap);
        return compileRes;
    }

    protected TemplateGenerator.GeneratedSource source = null;

    protected TemplateGenerator.GeneratedSource source() {
        if (source != null) return source;
        TemplateGenerator.GeneratedSource src = generateJavaCode();
        source = src;
        return source;
    }

    protected boolean isSaveDefaultMessageMap() {
        return true;
    }

    protected TemplateCompiler.CompileResult loadFromCache() {
        File bytecodeClassFile = getBytecodeFile();
        File sourceFile = getJavaSourceFile();
        if (!bytecodeClassFile.exists()) return null;
        if (!sourceFile.exists()) return null;
        if (!isSourceValid()) return null;
        Map<String, String> mmap = loadMessagesMap();
        if (mmap == null) {
            mmap = source().getDefaultTemplates();
            if (isSaveDefaultMessageMap()) {
                saveDefaultMessageMap(mmap);
            }
        }
        String clsName = getClassName();
        if (clsName == null) throw new IllegalStateException("getClassName()==null");
        try {
            byte[] data = FileUtil.readAllData(new FileInputStream(bytecodeClassFile), true);
            MemoryByteCode byteCode = new MemoryByteCode(clsName, data);
            MemoryClassLoader memClassLoader = createMemoryClassLoader();
            memClassLoader.getClassDataMap().put(clsName, byteCode);
            Class cls = memClassLoader.loadClass(clsName);
            Constructor constr = cls.getConstructor(Map.class);
            Object inst = constr.newInstance(mmap);
            TemplateCompiler.CompileResult res = new CompileResult(memClassLoader, null, inst, source().getDefaultTemplates());
            return res;
        } catch (Exception ex) {
            throw new Error(ex);
        }
    }

    protected boolean isSourceValid() {
        TemplateGenerator.GeneratedSource src = source();
        File sourceFile = getJavaSourceFile();
        String fileSrc = FileUtil.readAllText(sourceFile, FileUtil.UTF8());
        return fileSrc != null && src != null && fileSrc.trim().equals(src.getSource().trim());
    }

    protected boolean saveToCache(CompileResult cr) {
        boolean saveSrcAndCode = saveSourceAndCodeToCache(cr);
        boolean saveMess = true;
        File f = getDefaultMessagesFile();
        if (!f.exists()) {
            saveMess = saveDefTemplatesToCache(cr);
        } else {
            saveMess = true;
        }
        return saveSrcAndCode && saveMess;
    }

    protected boolean saveSourceAndCodeToCache(CompileResult cr) {
        File srcFile = getJavaSourceFile();
        File bytecodeFile = getBytecodeFile();
        if (!srcFile.getParentFile().exists()) if (!srcFile.getParentFile().mkdirs()) return false;
        if (!bytecodeFile.getParentFile().exists()) if (!bytecodeFile.getParentFile().mkdirs()) return false;
        String clsName = getClassName();
        if (clsName == null) throw new IllegalStateException("getClassName()==null");
        SourceCode memSource = null;
        if (cr.getSources() == null) return false;
        for (SourceCode src : cr.getSources()) {
            String srcName = src.getClassName();
            if (srcName.equals(clsName)) {
                memSource = src;
            }
        }
        if (memSource == null) return false;
        MemoryByteCode byteCode = cr.getClassLoader().getClassDataMap().get(clsName);
        if (byteCode == null) return false;
        FileOutputStream fout = null;
        try {
            FileUtil.writeAllText(srcFile, FileUtil.UTF8(), memSource.getSourceCode());
            fout = new FileOutputStream(bytecodeFile);
            fout.write(byteCode.getByteCode());
            fout.close();
        } catch (IOException ex) {
            Logger.getLogger(LocalMessagesImplementor.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            try {
                fout.close();
            } catch (IOException ex) {
                Logger.getLogger(LocalMessagesImplementor.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }
        return true;
    }

    protected boolean saveDefTemplatesToCache(CompileResult cr) {
        Map<String, String> mmap = cr.getDefaultTemplates();
        if (mmap == null) mmap = source().getDefaultTemplates();
        return saveDefaultMessageMap(mmap);
    }
}
