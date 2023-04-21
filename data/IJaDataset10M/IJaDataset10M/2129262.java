package de.psisystems.dmachinery.resources.csv;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import de.psisystems.dmachinery.ResourceRepo;
import de.psisystems.dmachinery.core.types.InputFormat;
import de.psisystems.dmachinery.core.types.OutputFormat;
import de.psisystems.dmachinery.core.types.ResourceType;
import de.psisystems.dmachinery.io.IOUtil;
import de.psisystems.dmachinery.resources.Resource;
import de.psisystems.dmachinery.resources.SimpleResourceID;
import de.psisystems.dmachinery.resources.cvs.RepoException;

public class CSVResourceReader implements Closeable {

    private static final String SEPERATOR = ";(?=([^\"]*\"[^\"]*\")*(?![^\"]*\"))";

    private static final Log log = LogFactory.getLog(CSVResourceReader.class);

    BufferedReader bufferedReader;

    int headerlines = 4;

    int linesRead = 0;

    private double assumedElements;

    private String line = null;

    private URL baseURL;

    private ResourceType type;

    private String version;

    private ResourceRepo repository;

    public CSVResourceReader(URL baseURL, ResourceType type, Reader reader, ResourceRepo repository) {
        bufferedReader = new BufferedReader(reader);
        this.baseURL = baseURL;
        this.type = type;
        this.assumedElements = 8;
        this.repository = repository;
    }

    public String[] readline() throws RepoException {
        String[] fieldValues = null;
        try {
            readlineIntern();
            if (linesRead == 1) {
                version = line;
            }
        } catch (IOException e) {
            throw new RepoException(e);
        }
        while (line != null && (line.startsWith("#") || linesRead <= headerlines)) {
            try {
                readlineIntern();
            } catch (IOException e) {
                throw new RepoException(e);
            }
        }
        if (line != null && line.trim().length() > 0) {
            fieldValues = line.split(SEPERATOR);
            if (fieldValues.length < assumedElements) {
                throw new RepoException("error line " + linesRead + " has not the assumed Elements of " + assumedElements);
            }
        }
        return fieldValues;
    }

    private void readlineIntern() throws IOException {
        line = bufferedReader.readLine();
        if (line != null) {
            log.debug(linesRead + " readline " + line);
            linesRead++;
        }
    }

    public void close() throws IOException {
        if (bufferedReader != null) {
            bufferedReader.close();
        }
    }

    protected String toPath(ResourceType type, String locale, String group, SimpleResourceID id, String version, String filename) {
        return type + "/" + locale + "/" + group + "/" + id.getID() + "/" + version + "/" + filename;
    }

    protected URL toURL(URL baseURL, String relativePath) {
        URL url = null;
        try {
            url = new URL(baseURL, relativePath);
        } catch (MalformedURLException e) {
            new RepoException(e.getMessage(), e);
        }
        return url;
    }

    protected Set<OutputFormat> toOutputFormats(String fieldValue) {
        Set<OutputFormat> outputFormats = new HashSet<OutputFormat>();
        String[] ofs = fieldValue.split(",");
        for (String of : ofs) {
            outputFormats.add(OutputFormat.valueOf(of));
        }
        return outputFormats;
    }

    protected InputFormat toInputFromat(String fieldValue) {
        return InputFormat.valueOf(fieldValue);
    }

    protected long toLong(String fieldValue) {
        long number = 0;
        number = Long.parseLong(fieldValue);
        return number;
    }

    protected Date toDate(String fieldValue) throws RepoException {
        Date date = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
        try {
            date = simpleDateFormat.parse(fieldValue);
        } catch (ParseException e) {
            throw new RepoException(e);
        }
        return date;
    }

    protected String toLocale(String fieldValue) throws RepoException {
        String locale = "default";
        Locale l = null;
        if (fieldValue.length() == 8) {
            String language = fieldValue.substring(0, 2);
            String country = fieldValue.substring(3, 5);
            String variant = fieldValue.substring(6, 7);
            l = new Locale(language, country, variant);
        } else if (fieldValue.length() == 5) {
            String language = fieldValue.substring(0, 2);
            String country = fieldValue.substring(3, 5);
            l = new Locale(language, country);
        } else if (fieldValue.length() == 2) {
            String country = fieldValue;
            String[] isoLanguages = Locale.getISOLanguages();
            List<String> languages = Arrays.asList(isoLanguages);
            if (languages.contains(fieldValue)) {
                l = new Locale(country);
            } else {
                throw new RepoException(fieldValue + " is not a valid country");
            }
        }
        if (l != null) {
            locale = l.toString();
        }
        return locale;
    }

    protected String toContent(URL url) {
        String content = "";
        try {
            content = readURL(url);
        } catch (IOException e) {
            log.info("Couldn't read content from " + url, e);
        }
        return content;
    }

    private String readURL(URL url) throws IOException {
        InputStreamReader sr = null;
        try {
            StringBuilder sb = new StringBuilder(100);
            InputStream is = url.openStream();
            sr = new InputStreamReader(is, "UTF-8");
            int i = -1;
            while ((i = sr.read()) != -1) {
                if (i != 65279) {
                    sb.append((char) i);
                }
            }
            return sb.toString();
        } finally {
            IOUtil.close(sr);
        }
    }

    public Resource read() throws RepoException {
        Resource resource = null;
        String[] fieldValues = null;
        fieldValues = readline();
        if (fieldValues != null) {
            try {
                int i = 0;
                String group = toGroup(fieldValues[i++]);
                SimpleResourceID id = new SimpleResourceID(fieldValues[i++], type);
                String locale;
                locale = toLocale(fieldValues[i++]);
                Date created = null;
                created = toDate(fieldValues[i++]);
                String createdBy = fieldValues[i++];
                String version = fieldValues[i++];
                Date validFrom = null;
                validFrom = toDate(fieldValues[i++]);
                String filename = fieldValues[i++];
                URL url = toURL(baseURL, toPath(type, locale, group, id, version, filename));
                resource = ResourceFactory.getInstance().createResource(type, group, id, locale, created, createdBy, version, validFrom, url, null, repository);
            } catch (RepoException e) {
                throw new RepoException(e.getMessage() + " at line " + linesRead);
            }
        }
        return resource;
    }

    private String toGroup(String fieldValue) {
        if (fieldValue != null && fieldValue.trim().length() > 0) {
            return fieldValue.trim();
        }
        return "default";
    }
}
