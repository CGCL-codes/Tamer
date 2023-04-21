package com.multimedia.service.wallpaper;

import common.beans.IOutputStreamHolder;
import common.utils.FileUtils;
import common.utils.ImageUtils;
import gallery.model.beans.Wallpaper;
import common.services.generic.GenericServiceImpl;
import gallery.model.beans.Resolution;
import gallery.service.resolution.IResolutionService;
import gallery.web.support.wallpaper.Utils;
import java.awt.Dimension;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import org.hibernate.ScrollableResults;
import org.springframework.core.io.Resource;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class WallpaperServiceImpl extends GenericServiceImpl<Wallpaper, Long> implements IWallpaperService {

    /** specifies the path to folder with wallpapers within server */
    private String path;

    /** path where resized photos will be stored (i.e. all resolutions from database) */
    private String resized_path;

    /** specifies the path to folder where to backup wallpapers */
    private String backup_path;

    /** specifies the path to folder where to get wallpapers for multi upload wallpapers */
    private String upload_path;

    /** are paths to folders with resized images i.e. small or medium */
    private Map<String, Integer> dimensions;

    /** forbidden words for tag cloud. tags with this words will not be added to the tag cloud */
    private Set<String> black_word_list;

    private IResolutionService resolution_service;

    @Override
    public void init() {
        super.init();
        StringBuilder sb = new StringBuilder();
        common.utils.MiscUtils.checkNotNull(path, "path", sb);
        common.utils.MiscUtils.checkNotNull(resized_path, "resized_path", sb);
        common.utils.MiscUtils.checkNotNull(backup_path, "backup_path", sb);
        common.utils.MiscUtils.checkNotNull(upload_path, "upload_path", sb);
        common.utils.MiscUtils.checkNotNull(resolution_service, "resolution_service", sb);
        if (black_word_list == null) {
            black_word_list = java.util.Collections.emptySet();
        }
        if (common.utils.MiscUtils.checkNotNull(dimensions, "dimensions", sb)) {
            Set<Entry<String, Integer>> entrySet = dimensions.entrySet();
            Iterator<Entry<String, Integer>> entries = entrySet.iterator();
            while (entries.hasNext()) {
                Entry<String, Integer> e = entries.next();
                File dst_subfolder = new File(path + File.separator + e.getKey());
                if (!dst_subfolder.exists()) dst_subfolder.mkdir();
            }
        }
        if (sb.length() > 0) {
            throw new NullPointerException(sb.toString());
        }
    }

    public static final String[] ORDER_BY = new String[] { "id" };

    public static final String[] ORDER_HOW = new String[] { "ASC" };

    public static final String[] ORDER_HOW_REVERSE = new String[] { "DESC" };

    public void setBlackWords(Set<String> value) {
        this.black_word_list = value;
    }

    public void setPath(Resource res) {
        try {
            File f = res.getFile();
            if (f.exists()) {
                if (f.isFile()) {
                    f.delete();
                    f.mkdir();
                }
            } else {
                f.mkdirs();
            }
            if (f.exists() && f.isDirectory()) path = f.getCanonicalPath() + "/"; else throw new NullPointerException("image folder not found for " + getClass());
        } catch (IOException e) {
            path = null;
            throw new NullPointerException("image folder not found" + getClass());
        }
    }

    public void setResized_path(Resource res) {
        try {
            File f = res.getFile();
            if (f.exists()) {
                if (f.isFile()) {
                    f.delete();
                    f.mkdir();
                }
            } else {
                f.mkdirs();
            }
            if (f.exists() && f.isDirectory()) resized_path = f.getCanonicalPath() + "/"; else throw new NullPointerException("image resized folder not found for " + getClass());
        } catch (IOException e) {
            resized_path = null;
            throw new NullPointerException("image resized folder not found" + getClass());
        }
    }

    public void setBackup_path(Resource res) {
        try {
            File f = res.getFile();
            if (f.exists()) {
                if (f.isFile()) {
                    f.delete();
                    f.mkdir();
                }
            } else {
                f.mkdirs();
            }
            if (f.exists() && f.isDirectory()) backup_path = f.getCanonicalPath() + "/"; else throw new NullPointerException("backup folder not found for " + getClass());
        } catch (IOException e) {
            backup_path = null;
            throw new NullPointerException("backup folder not found for " + getClass());
        }
    }

    public void setUpload_path(Resource res) {
        try {
            File f = res.getFile();
            if (f.exists()) {
                if (f.isFile()) {
                    f.delete();
                    f.mkdir();
                }
            } else {
                f.mkdirs();
            }
            if (f.exists() && f.isDirectory()) upload_path = f.getCanonicalPath() + "/"; else throw new NullPointerException("upload folder not found for " + getClass());
        } catch (IOException e) {
            upload_path = null;
            throw new NullPointerException("upload folder not found for " + getClass());
        }
    }

    protected final String image_extension = ".jpg";

    @Override
    public String getUniqName(String old_name) {
        int pos = old_name.lastIndexOf(".");
        String new_name;
        if (pos > 0) {
            new_name = old_name.substring(0, pos);
        } else {
            new_name = old_name;
        }
        new_name = FileUtils.checkFileNameSpelling(new_name);
        String candidate = new_name + image_extension;
        int i = 0;
        while (dao.getRowCount("name", candidate) > 0) {
            candidate = new_name + "_" + i + image_extension;
            i++;
        }
        return candidate;
    }

    @Override
    public boolean getImage(Wallpaper p) {
        if (p.getContent() == null && p.getContent_file() == null) return false;
        String orig_file_name = null;
        if (p.getName() != null && !p.getName().isEmpty()) {
            orig_file_name = p.getName();
        } else if (p.getContent() != null) {
            orig_file_name = p.getContent().getOriginalFilename();
        } else if (p.getContent_file() != null) {
            orig_file_name = p.getContent_file().getAbsolutePath();
        }
        p.setName(getUniqName(orig_file_name));
        p.setDate_upload(new Timestamp(System.currentTimeMillis()));
        Dimension d = gallery.web.support.wallpaper.Utils.saveScaledWallpaperFileToDisk(p, dimensions, path, null, orig_file_name);
        if (d != null) {
            p.setType("unknown");
            p.setWidth(d.width);
            p.setHeight(d.height);
            return true;
        }
        return false;
    }

    @Override
    public boolean getImage(Wallpaper p, File folder) {
        if (p.getContent() == null && p.getContent_file() == null) return false;
        String orig_file_name = null;
        if (p.getName() != null && !p.getName().isEmpty()) {
            orig_file_name = p.getName();
        } else if (p.getContent_file() != null) {
            orig_file_name = p.getContent_file().getName();
        }
        p.setName(getUniqName(orig_file_name));
        p.setDate_upload(new Timestamp(System.currentTimeMillis()));
        Dimension d = gallery.web.support.wallpaper.Utils.saveScaledWallpaperFileToDisk(p, dimensions, path, folder, orig_file_name);
        if (d != null) {
            p.setType("unknown");
            p.setWidth(d.width);
            p.setHeight(d.height);
            return true;
        }
        return false;
    }

    public static final String[] WALLPAPERS_WHERE = new String[] { "id_pages", "active" };

    /**
     * get random wallpapers from given with given
	 * @param id_pages
	 * @return
	 */
    @Override
    public List<Wallpaper> getMainImages(List<Long> id_pages, int count) {
        Object[][] values = new Object[][] { null, new Object[] { Boolean.TRUE } };
        Long[] id_pages_a = new Long[id_pages.size()];
        values[0] = id_pages.toArray(id_pages_a);
        int size = dao.getRowCount(WALLPAPERS_WHERE, values).intValue();
        Random r = new Random();
        List<Wallpaper> temp_wallpaper;
        HashSet<Integer> generated = new HashSet<Integer>(count + 1);
        if (size > count) {
            List<Wallpaper> rez = new LinkedList<Wallpaper>();
            for (int i = 0; i < count; i++) {
                Integer num = r.nextInt(size);
                while (generated.contains(num)) {
                    num = r.nextInt(size);
                }
                generated.add(num);
                temp_wallpaper = dao.getByPropertiesValuesPortionOrdered(null, null, WALLPAPERS_WHERE, values, num, 1, null, null);
                rez.add(temp_wallpaper.get(0));
            }
            return rez;
        } else {
            return dao.getByPropertiesValuesPortionOrdered(null, null, WALLPAPERS_WHERE, values, 0, -1, null, null);
        }
    }

    /**
	 * are paths to folders with resized images i.e. small or medium
	 * @param dimensions the dimensions to set
	 */
    public void setDimensions(Map<String, Integer> dimensions) {
        this.dimensions = dimensions;
    }

    public static final String[] WALLPAPERS_PAGINATED_WHERE = new String[] { "id_pages", "active" };

    @Override
    public List<Wallpaper> getWallpapersPaginated(int first_num, int quantity, Long id_pages) {
        return dao.getByPropertiesValuePortionOrdered(null, null, WALLPAPERS_PAGINATED_WHERE, new Object[] { id_pages, Boolean.TRUE }, first_num, quantity, ORDER_BY, ORDER_HOW);
    }

    @Override
    public List<Wallpaper> getWallpapersPaginated(int first_num, int quantity, Long[] id_pages) {
        return dao.getByPropertiesValuePortionOrdered(null, null, WALLPAPERS_PAGINATED_WHERE, new Object[][] { id_pages, new Object[] { Boolean.TRUE } }, first_num, quantity, ORDER_BY, ORDER_HOW);
    }

    public static final String[] RELATIONS_ASC = new String[] { "=", "=", ">" };

    public static final String[] RELATIONS_DESC = new String[] { "=", "=", "<" };

    public static final String[] WALLPAPERS_PAGINATED_RELATIONS_WHERE = new String[] { "id_pages", "active", "id" };

    @Override
    public List<Wallpaper> getWallpapersPaginatedId(Long id, int quantity, Long id_pages) {
        if (quantity > 0) {
            return dao.getByPropertiesValuePortionOrdered(null, null, WALLPAPERS_PAGINATED_RELATIONS_WHERE, new Object[] { id_pages, Boolean.TRUE, id }, RELATIONS_ASC, 0, quantity, ORDER_BY, ORDER_HOW);
        } else {
            return dao.getByPropertiesValuePortionOrdered(null, null, WALLPAPERS_PAGINATED_RELATIONS_WHERE, new Object[] { id_pages, Boolean.TRUE, id }, RELATIONS_DESC, 0, -quantity, ORDER_BY, ORDER_HOW_REVERSE);
        }
    }

    @Override
    public Long getWallpapersRowCount(Long id_pages) {
        return dao.getRowCount(WALLPAPERS_PAGINATED_WHERE, new Object[] { id_pages, Boolean.TRUE });
    }

    @Override
    public boolean getResizedWallpaperStream(Long id_wallpaper, Integer new_width, Integer new_height, OutputStream os) throws IOException {
        String name = (String) dao.getSinglePropertyU("name", "id", id_wallpaper);
        if (name != null && !name.equals("")) {
            File src = new File(path + File.separator + "full" + File.separator + name);
            if (src.exists()) {
                ImageUtils.getScaledImageDimmension(src, new_width, new_height, os);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean getResizedWallpaperStream(String name, Integer new_width, Integer new_height, IOutputStreamHolder osh) throws IOException {
        if (name != null && (dao.getRowCount("name", name) == 1)) {
            File resolution_dir = new File(resized_path, new_width + "x" + new_height);
            File cached_file = new File(resolution_dir, name);
            if (cached_file.exists()) {
                FileUtils.loadFromFileNew(cached_file, osh.getOutputStream());
                return true;
            } else {
                File src = new File(path + File.separator + "full" + File.separator + name);
                if (src.exists()) {
                    if (resolution_dir.exists() || resolution_dir.mkdirs()) {
                        cached_file.createNewFile();
                        FileOutputStream fos = new FileOutputStream(cached_file);
                        ImageUtils.getScaledImageDimmension(src, new_width, new_height, fos);
                        fos.flush();
                        fos.close();
                        FileUtils.loadFromFileNew(cached_file, osh.getOutputStream());
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static final String[] MULTI_DELETE_PSEUDO = { "id", "name" };

    @Override
    public int deleteById(Long[] id) {
        if (id != null) {
            int count = 0;
            List<Wallpaper> wallpapers = dao.getByPropertyValues(MULTI_DELETE_PSEUDO, "id", id);
            for (int i = 0; i < wallpapers.size(); i++) {
                deleteFiles(wallpapers.get(i));
                dao.deleteById(id[i]);
                count++;
            }
            return count;
        } else {
            return -1;
        }
    }

    @Override
    public int deleteById(Long id) {
        Wallpaper p = dao.getById(id);
        if (p != null && deleteFiles(p)) {
            return dao.deleteById(id);
        } else {
            return -1;
        }
    }

    @Override
    public boolean deleteFiles(Wallpaper wallpaper) {
        List<Resolution> resolutions = resolution_service.getOrdered(null, null, null);
        List<File> tmp = new ArrayList<File>(resolutions.size());
        for (Resolution res : resolutions) {
            tmp.add(new File(resized_path, res.getWidth() + "x" + res.getHeight()));
        }
        return gallery.web.support.wallpaper.Utils.deleteWallpaper((String) getSinglePropertyU("name", "id", wallpaper.getId()), dimensions, path, tmp);
    }

    @Override
    public boolean renameFiles(Wallpaper wallpaper) {
        if (wallpaper.getName() == null) return true;
        String old_name = (String) getSinglePropertyU("name", "id", wallpaper.getId());
        if (old_name.equals(wallpaper.getName())) return true;
        wallpaper.setName(getUniqName(wallpaper.getName()));
        List<Resolution> resolutions = resolution_service.getOrdered(null, null, null);
        List<File> tmp = new ArrayList<File>(resolutions.size());
        for (Resolution res : resolutions) {
            tmp.add(new File(resized_path, res.getWidth() + "x" + res.getHeight()));
        }
        return gallery.web.support.wallpaper.Utils.renameWallpaper(wallpaper.getName(), old_name, dimensions, path, tmp);
    }

    @Override
    public List<Wallpaper> backupWallpapers(List<Wallpaper> wallpapers, boolean append, boolean only_files) {
        if (!only_files) {
            throw new NullPointerException("not supported yet");
        }
        return Utils.copyWallpaper(wallpapers, dimensions, path, backup_path, false, true);
    }

    @Override
    public List<Wallpaper> restoreWallpapers(List<Wallpaper> wallpapers, boolean append, boolean only_files) {
        if (!only_files) {
            throw new NullPointerException("not supported yet");
        }
        return Utils.copyWallpaper(wallpapers, dimensions, backup_path, path, false, true);
    }

    @Override
    public String getUploadPath() {
        return upload_path;
    }

    @Override
    public String getStorePath() {
        return path;
    }

    @Override
    public Map<String, Integer> getDimmensions() {
        return dimensions;
    }

    @Override
    public Long getWallpaperNumber(Wallpaper p) {
        Object[] obj = new Object[ORDER_BY.length];
        Map m = Wallpaper.toMap(p);
        for (int i = 0; i < ORDER_BY.length; i++) {
            obj[i] = m.get(ORDER_BY[i]);
        }
        return (Long) dao.getRowNumber(obj, ORDER_BY, ORDER_HOW, WALLPAPERS_PAGINATED_WHERE, new Object[] { p.getId_pages(), Boolean.TRUE });
    }

    public static final String[] RANDOM_WALLPAPERS_PROPERTIES = new String[] { "id", "id_pages", "name", "title" };

    @Override
    public List<Wallpaper> getRandomWallpapers(int quantity) {
        List ids = dao.getSinglePropertyOrderRand("id", "active", Boolean.TRUE, 0, quantity);
        return dao.getByPropertyValues(RANDOM_WALLPAPERS_PROPERTIES, "id", ids);
    }

    @Override
    public Map<String, Double> getTags(int maxTags) {
        ScrollableResults wallpaper_tags = dao.getScrollableResults("tags", "active", Boolean.TRUE, null, null);
        Map<String, Double> tags = new HashMap<String, Double>();
        if (wallpaper_tags.first()) {
            String tag;
            Double score;
            String[] tags_parsed;
            String tag_parsed;
            do {
                tag = wallpaper_tags.getString(0);
                if (tag != null) {
                    tags_parsed = tag.split(",");
                    for (int i = 1; i < tags_parsed.length; i++) {
                        tag_parsed = tags_parsed[i].trim();
                        if (!black_word_list.contains(tag_parsed)) {
                            score = tags.get(tag_parsed);
                            if (score == null) {
                                tags.put(tag_parsed, new Double(1.0));
                            } else {
                                tags.put(tag_parsed, (score + 1));
                            }
                        }
                    }
                }
            } while (wallpaper_tags.next());
        }
        wallpaper_tags.close();
        Set<Entry<String, Double>> i = tags.entrySet();
        List<Entry<String, Double>> l = new LinkedList<Entry<String, Double>>(i);
        java.util.Collections.sort(l, new WallpaperServiceImpl.EntryComparatorDesc());
        if (maxTags > 0) {
            for (int j = maxTags; j < l.size(); j++) {
                tags.remove(l.get(j).getKey());
            }
        }
        return tags;
    }

    @Override
    public long getWallpaperLastModified(String name) {
        File wallpaper_file = new File(resized_path, name);
        if (wallpaper_file.exists()) {
            return wallpaper_file.lastModified();
        } else {
            return -1;
        }
    }

    /**
	 * Compares two tags by score in descending order
	 */
    public static class EntryComparatorDesc implements Comparator<Entry<String, Double>> {

        @Override
        public int compare(Entry<String, Double> o1, Entry<String, Double> o2) {
            int scoreComparison = Double.compare(o2.getValue(), o1.getValue());
            if (scoreComparison == 0) {
                return String.CASE_INSENSITIVE_ORDER.compare(o2.getKey(), o1.getKey());
            } else {
                return scoreComparison;
            }
        }
    }

    public static String[] RESOLUTION_NAME = new String[] { "width", "height" };

    @Override
    public void enableResolutionFilter(int width, int height) {
        dao.enableFilter("resolution_id", RESOLUTION_NAME, new Object[] { width, height });
    }

    @Override
    public void disableResolutionFilter() {
        dao.disableFilter("resolution_id");
    }

    public void setResolutionService(IResolutionService value) {
        this.resolution_service = value;
    }
}
