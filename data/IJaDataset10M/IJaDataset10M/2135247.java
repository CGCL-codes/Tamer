package net.jwpa.cache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;
import net.jwpa.cache.model.ActivatableHashMap;
import net.jwpa.cache.model.ActivatableLinkedList;
import net.jwpa.cache.model.CachedMedia;
import net.jwpa.config.LogUtil;
import net.jwpa.controller.Utils;
import net.jwpa.dao.MediaDAO;
import net.jwpa.dao.RatingConfigDAO;
import net.jwpa.dao.StringUtils;
import net.jwpa.model.LocalizedMessage;
import net.jwpa.model.LocalizedMessageImpl;
import net.jwpa.model.MediaFacade;
import net.jwpa.model.RatingConfig;

public abstract class MediaReader extends AbstractReader {

    private static final Logger logger = LogUtil.getLogger();

    private final String filename;

    private File file;

    private Properties properties;

    public MediaReader(String fn) {
        super(fn);
        filename = fn;
        file = new File(filename);
    }

    public abstract CachedMedia loadOrBuildCachedVersion() throws IOException;

    public static MediaReader getRelativeInstance(String f) {
        return getInstance(Utils.getRelativeFile(f).getAbsolutePath());
    }

    public static MediaReader getInstance(String f) {
        switch(MediaDAO.getMediaType(f)) {
            case AUDIO:
                return new AudioClipReader(f);
            case IMAGE:
                return new JpegImageReader(f);
            case VIDEO:
                return new VideoClipReader(f);
            default:
                throw new IllegalArgumentException("Unknown file type for " + f);
        }
    }

    protected void buildEntryFromDisk(CachedMedia data) throws IOException {
        LogUtil.logDebug(logger, "Loading media " + filename);
        data.setAbsoluteFileName(filename);
        data.setDate(getDate());
        data.setKey(filename);
        data.setSize(getFile().length());
        data.setRatings(getRatings(data));
        data.setOverallRating(computeOverallRating());
        data.setComment(getLocalizedPropertyMap(data, "comment"));
        data.setTags(getTags(data));
        data.setTitle(getFile().getName().substring(0, getFile().getName().lastIndexOf(".")));
        data.setVersion(getVersion());
    }

    protected File getFile() {
        if (file == null) file = new File(filename);
        return file;
    }

    protected Properties getProperties() throws IOException {
        if (properties == null) {
            File f = getPropertiesFile();
            Properties p = new Properties();
            if (f.exists()) {
                FileInputStream fis = new FileInputStream(f);
                try {
                    p.load(fis);
                } finally {
                    fis.close();
                }
            }
            properties = p;
        }
        return properties;
    }

    protected File getPropertiesFile() {
        return new File(filename + ".properties");
    }

    @Override
    public void sync(AbstractCachedObject o) throws IOException {
        LogUtil.logDebug(logger, "Saving media " + filename);
        CachedMedia cm = (CachedMedia) o;
        Properties properties = new Properties();
        fillInPropertiesWithCache(cm, properties);
        File f = getPropertiesFile();
        FileOutputStream fos = new FileOutputStream(f);
        try {
            properties.store(fos, "Properties for the file " + getAbsolutePath());
        } finally {
            fos.close();
        }
    }

    private void fillInPropertiesWithCache(CachedMedia cf, Properties properties) {
        if (cf.getTags() != null) {
            StringBuilder sb = new StringBuilder(cf.getTags().size());
            boolean first = true;
            for (String s : cf.getTags()) {
                if (!first) sb.append(", ");
                sb.append(s);
                first = false;
            }
            properties.setProperty("tags", sb.toString());
        }
        LocalizedMessageImpl.storeToProperties(properties, "comment", cf.getComment());
        if (cf.getRatings() != null) {
            for (Map.Entry<String, Float> entry : cf.getRatings().entrySet()) {
                properties.setProperty("rating." + entry.getKey(), String.valueOf(entry.getValue()));
            }
        }
        properties.setProperty("version", String.valueOf(cf.getVersion()));
    }

    public ActivatableHashMap<String, Float> getRatings(AbstractTopLevelCachedObject tlo) throws IOException {
        RatingConfigDAO rcdao = new RatingConfigDAO();
        List<RatingConfig> rclist = rcdao.getRatingConfig();
        ActivatableHashMap<String, Float> res = new ActivatableHashMap<String, Float>(tlo);
        for (RatingConfig rc : rclist) {
            res.put(rc.getTitle(), getRating(rc.getTitle()));
        }
        return res;
    }

    public float getRating(String rating) {
        try {
            String s = getProperties().getProperty("rating." + rating);
            if (s == null) return 0;
            return Float.parseFloat(s);
        } catch (NumberFormatException e) {
            LogUtil.logError(logger, "Non integer rating for file " + file.getAbsolutePath() + " and for rating " + rating, e);
        } catch (IOException e) {
            LogUtil.logError(logger, "Unable to read properties for file " + file.getAbsolutePath(), e);
        }
        return 0;
    }

    public float computeOverallRating() throws IOException {
        RatingConfigDAO rcdao = new RatingConfigDAO();
        List<RatingConfig> rclist = rcdao.getRatingConfig();
        float res = 0;
        int count = 0;
        for (RatingConfig rc : rclist) {
            float r = getRating(rc.getTitle());
            if (r > 0) {
                res += r * rc.getWeight();
                count += rc.getWeight();
            }
        }
        if (count > 0) return res / count; else return -1;
    }

    public ActivatableLinkedList<String> getTags(AbstractTopLevelCachedObject o) throws IOException {
        String trs = getProperties().getProperty("tags");
        return getTags(o, trs);
    }

    public static ActivatableLinkedList<String> getTags(AbstractTopLevelCachedObject o, String tags) throws IOException {
        ActivatableLinkedList<String> res = new ActivatableLinkedList<String>(o);
        if (tags == null) return res;
        String[] t = tags.split(",");
        for (String tt : t) {
            String ttt = tt.trim();
            if (!StringUtils.isStringEmpty(ttt)) res.add(ttt);
        }
        return res;
    }

    protected long getLastModifiedTime() {
        long res = getFile().lastModified();
        if (getPropertiesFile().exists()) {
            long pf = getPropertiesFile().lastModified();
            return Math.max(res, pf);
        }
        return res;
    }

    public Date getDate() {
        Date date = extractDate();
        if (date == null) date = new Date(file.lastModified());
        return date;
    }

    public String getAbsolutePath() throws IOException {
        return file.getAbsolutePath();
    }

    public ActivatableHashMap<String, String> getLocalizedPropertyMap(AbstractTopLevelCachedObject tlo, String property) throws IOException {
        LocalizedMessage p = getLocalizedProperty(property);
        return p.getAHM(tlo);
    }

    public LocalizedMessage getLocalizedProperty(String propertyName) throws IOException {
        return new LocalizedMessageImpl((Map) getProperties(), propertyName);
    }

    public abstract Date extractDate();

    public int getVersion() throws IOException {
        String version = getProperties().getProperty("version");
        if (StringUtils.isStringEmpty(version)) return 0;
        return Integer.parseInt(version);
    }

    public static List<MediaFacade> getList(CachedMedia[] list) {
        List<MediaFacade> res = new LinkedList<MediaFacade>();
        for (CachedMedia cm : list) {
            res.add(MediaDAO.getFacade(cm));
        }
        return res;
    }

    public String getFilename() {
        return filename;
    }
}
