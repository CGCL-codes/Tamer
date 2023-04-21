package cz.langteacher.server.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import org.springframework.beans.factory.annotation.Autowired;
import cz.langteacher.LangTeacherException;
import cz.langteacher.dao.iface.ILessonLevelDAO;
import cz.langteacher.model.Dictionary;
import cz.langteacher.model.Lesson;
import cz.langteacher.model.LessonLevel;
import cz.langteacher.util.IConnectionStreamUtils;
import cz.langteacher.util.ILTUtil;
import cz.langteacher.util.LessonLevelJAXBAdapter;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "export")
public class ImportExportUtil implements IImportExportUtil {

    public static String KEY_NAME = "@lesson name@";

    public static String KEY_LEVEL = "@lesson level@";

    public static String KEY_DESCRIPTION = "@lesson description@";

    public static String VALUE_DELIMINER = "=";

    public static String COMMENT_MARK = "#";

    public static String DELIMS = "\t|";

    private static final String[] SUPPORTED_EXTENSIONS = { "xml", "XML", "ltf", "LTF", "txt", "TXT" };

    @SuppressWarnings("unused")
    @XmlAttribute
    private String ltVersion;

    @XmlElementWrapper(name = "lessons")
    @XmlElement(name = "lesson")
    private List<Lesson> lessons = new ArrayList<Lesson>();

    @Autowired
    private ILessonLevelDAO languageLevelDAO;

    @Autowired
    private IConnectionStreamUtils streamUtils;

    @Autowired
    private ILTUtil ltUtil;

    private JAXBContext ctx = null;

    private JAXBContext getJAXBContext() throws JAXBException {
        if (ctx == null) {
            ctx = JAXBContext.newInstance(ImportExportUtil.class);
        }
        return ctx;
    }

    public Lesson buildLessonFromLTF(Reader reader) {
        Lesson lesson = new Lesson();
        List<Dictionary> result = new ArrayList<Dictionary>();
        BufferedReader bReader = new BufferedReader(reader);
        String line = "";
        StringTokenizer tokenizer = null;
        Dictionary dic = null;
        try {
            while ((line = bReader.readLine()) != null) {
                line = line.trim();
                if (line.startsWith(COMMENT_MARK)) {
                    continue;
                }
                if (line.startsWith(KEY_NAME) || line.startsWith(KEY_LEVEL) || line.startsWith(KEY_DESCRIPTION)) {
                    adjustLesson(lesson, line);
                }
                tokenizer = new StringTokenizer(line, "\t|");
                if (tokenizer.countTokens() < 2) {
                    continue;
                }
                dic = new Dictionary(tokenizer.nextToken(), tokenizer.nextToken(), tokenizer.hasMoreTokens() ? tokenizer.nextToken() : "");
                result.add(dic);
            }
        } catch (IOException e) {
            throw new LangTeacherException(e);
        } finally {
            if (bReader != null) {
                try {
                    bReader.close();
                } catch (IOException e) {
                    throw new LangTeacherException(e);
                }
            }
        }
        lesson.setDictionary(result);
        return lesson;
    }

    public Lesson buildLessonFromLTF(InputStream in) {
        try {
            return buildLessonFromLTF(new InputStreamReader(in, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new LangTeacherException(e);
        }
    }

    private String parseValue(String line) {
        String[] values = line.split(VALUE_DELIMINER);
        if (values.length == 2) {
            return values[1];
        } else {
            return "";
        }
    }

    private void adjustLesson(Lesson lesson, String line) {
        if (line.startsWith(KEY_NAME)) {
            lesson.setName(parseValue(line));
        } else if (line.startsWith(KEY_LEVEL)) {
            LessonLevel level = languageLevelDAO.getLevelByID(Integer.parseInt(parseValue(line)));
            lesson.setLevel(level);
        } else if (line.startsWith(KEY_DESCRIPTION)) {
            lesson.setDescription(parseValue(line));
        }
    }

    public List<Dictionary> getRidOfEmptyDictionaries(Lesson lesson) {
        List<Dictionary> result = new ArrayList<Dictionary>();
        for (Dictionary dic : lesson.getDictionary()) {
            if (!dic.isEmpty()) {
                result.add(dic);
            }
        }
        return result;
    }

    public long exportLessonToXML(List<Lesson> lessons, File file, String applVersion) {
        ltVersion = applVersion;
        String xml = convertObjectsToXML(lessons);
        long count = ltUtil.countDictionaries(lessons);
        BufferedWriter fileWriter = null;
        try {
            fileWriter = new BufferedWriter(new FileWriter(file));
            fileWriter.write(xml);
        } catch (IOException e) {
            throw new LangTeacherException(e);
        } finally {
            streamUtils.closeStreamNoError(fileWriter, "Cannot close file stream");
        }
        return count;
    }

    public int exportLessonToLTF(Lesson lesson, OutputStream out, String applVersion) {
        try {
            return exportLessonToLTF(lesson, new OutputStreamWriter(out, "UTF-8"), applVersion);
        } catch (UnsupportedEncodingException e) {
            throw new LangTeacherException(e);
        }
    }

    public int exportLessonToLTF(Lesson lesson, Writer wr, String applVersion) {
        int exportedCount = 0;
        List<Dictionary> dics = getRidOfEmptyDictionaries(lesson);
        String delim = DELIMS;
        delim = delim.substring(0, 1);
        delim = "\t";
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(wr);
            writeHead(writer, lesson, dics.size(), applVersion);
            Dictionary dic = null;
            String line = null;
            Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
            for (int i = 0; i < dics.size(); i++) {
                dic = dics.get(i);
                line = createLine(dic, delim);
                exportedCount++;
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new LangTeacherException(e);
        } finally {
            if (writer != null) {
                try {
                    writer.flush();
                    writer.close();
                } catch (IOException e) {
                    throw new LangTeacherException(e);
                }
            }
        }
        return exportedCount;
    }

    @Override
    public List<Lesson> convertXMLToObjects(InputStream input) {
        testOfDependency();
        try {
            Unmarshaller mar = getJAXBContext().createUnmarshaller();
            ImportExportUtil c = (ImportExportUtil) mar.unmarshal(input);
            return c.lessons;
        } catch (Exception e) {
            throw new LangTeacherException(e);
        }
    }

    @Override
    public List<Lesson> convertXMLToObjects(File xml) {
        testOfDependency();
        try {
            Unmarshaller mar = getJAXBContext().createUnmarshaller();
            ImportExportUtil c = (ImportExportUtil) mar.unmarshal(xml);
            return c.lessons;
        } catch (Exception e) {
            throw new LangTeacherException(e);
        }
    }

    @Override
    public List<Lesson> convertXMLToObjects(String xml) {
        testOfDependency();
        try {
            Unmarshaller mar = getJAXBContext().createUnmarshaller();
            ImportExportUtil c = (ImportExportUtil) mar.unmarshal(new StringReader(xml));
            return c.lessons;
        } catch (Exception e) {
            throw new LangTeacherException(e);
        }
    }

    private void testOfDependency() {
        if (LessonLevelJAXBAdapter.getLanguageLevel() == null) {
            LessonLevelJAXBAdapter.setLanguageLevel(languageLevelDAO);
        }
    }

    public String convertObjectsToXML(List<Lesson> lessons) {
        testOfDependency();
        this.lessons = lessons;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            Marshaller marshaller = createMarshaller();
            marshaller.marshal(this, out);
            return new String(out.toByteArray(), "UTF-8");
        } catch (Exception e) {
            throw new LangTeacherException(e);
        } finally {
            streamUtils.closeStreamNoError(out, "");
        }
    }

    private Marshaller createMarshaller() throws JAXBException {
        Marshaller marshaller = getJAXBContext().createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        return marshaller;
    }

    private String createLine(Dictionary dic, String delim) {
        StringBuilder builder = new StringBuilder(!dic.getWord().isEmpty() ? dic.getWord() : " ");
        builder.append(delim);
        builder.append(!dic.getTranslation().isEmpty() ? dic.getTranslation() : " ");
        builder.append(delim);
        builder.append(!dic.getDescription().isEmpty() ? dic.getDescription() : " ");
        return builder.toString();
    }

    private void writeHead(BufferedWriter writer, Lesson lesson, int count, String applVersion) throws IOException {
        writer.write(COMMENT_MARK + "LangTeacher, version:" + applVersion);
        writer.newLine();
        writer.write(COMMENT_MARK + "Number of words=" + count);
        writer.newLine();
        writer.write(KEY_NAME + VALUE_DELIMINER + lesson.getName());
        writer.newLine();
        writer.write(KEY_LEVEL + VALUE_DELIMINER + lesson.getLevel().getId());
        writer.newLine();
        writer.write(KEY_DESCRIPTION + VALUE_DELIMINER + lesson.getDescription());
        writer.newLine();
        writer.newLine();
    }

    public boolean isSupportedFileSuffix(String suffix) {
        for (String supportSuffix : SUPPORTED_EXTENSIONS) {
            if (supportSuffix.equalsIgnoreCase(suffix)) {
                return true;
            }
        }
        return false;
    }

    public String[] getSupportedSuffixes() {
        return SUPPORTED_EXTENSIONS;
    }
}
