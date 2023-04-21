package org.esb.jmx;

import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IPacket;
import com.xuggle.xuggler.IStream;
import com.xuggle.xuggler.IStreamCoder;
import com.xuggle.xuggler.IVideoPicture;
import com.xuggle.xuggler.Utils;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.esb.av.Coder;
import org.esb.av.HiveImage;
import org.esb.av.filter.Filter;
import org.esb.model.FilterConfiguration;
import org.esb.av.filter.FilterFactory;
import org.esb.av.filter.VideoFilter;
import org.esb.dao.MediaFilesDao;
import org.esb.hive.CodecFactory;
import org.esb.hive.DatabaseService;
import org.esb.hive.FileImporter;
import org.esb.model.MediaFile;
import org.esb.model.FilterAttribute;
import org.esb.model.SerializedImage;
import org.esb.model.MediaStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MediaFiles implements MediaFilesMBean {

    private static Logger _log = LoggerFactory.getLogger(MediaFiles.class);

    public static final String MBEAN_NAME = "org.esb.jmx:type=MediaFiles";

    public MediaFiles() {
        try {
            JHiveRegistry.registerMBean(this, MBEAN_NAME);
        } catch (JHiveRegistryException e) {
            _log.error("register MBean", e);
        }
    }

    public static MediaFilesMBean getRemoteBean(JHiveRegistryConnection con) throws JHiveRegistryException {
        return (MediaFilesMBean) con.getMXBean(MBEAN_NAME, MediaFilesMBean.class);
    }

    public List<MediaFile> getMediaFiles() throws JHiveRegistryException {
        List<MediaFile> result = new ArrayList<MediaFile>();
        try {
            Connection con = DatabaseService.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from files");
            ResultSetMetaData rsmd = rs.getMetaData();
            while (rs.next()) {
                MediaFile file = new MediaFile();
                int cc = rsmd.getColumnCount();
                for (int a = 1; a <= cc; a++) {
                    String col = rsmd.getColumnName(a);
                    String val = rs.getString(col);
                    file.put(col.toLowerCase(), val);
                }
                result.add(file);
            }
        } catch (SQLException e) {
            throw new JHiveRegistryException(e);
        }
        return result;
    }

    public List<HashMap<String, String>> getMediaFileList() throws JHiveRegistryException {
        List<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
        try {
            Connection con = DatabaseService.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from files");
            ResultSetMetaData rsmd = rs.getMetaData();
            while (rs.next()) {
                HashMap<String, String> file = new HashMap<String, String>();
                int cc = rsmd.getColumnCount();
                for (int a = 1; a <= cc; a++) {
                    String col = rsmd.getColumnName(a);
                    String val = rs.getString(col);
                    file.put(col.toLowerCase(), val);
                }
                result.add(file);
            }
        } catch (SQLException e) {
            throw new JHiveRegistryException(e);
        }
        return result;
    }

    public List<HashMap<String, String>> getEncodingList() throws JHiveRegistryException {
        List<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
        try {
            Connection con = DatabaseService.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT filename,min((((last_dts-start_time)*time_base_num)/time_base_den)*100/(files.duration/1000000)) as progress, jobbegin FROM files, jobs, job_details, streams where files.id=jobs.outputfile and jobs.id=job_details.job_id and job_details.instream=streams.id group by filename, jobbegin");
            ResultSetMetaData rsmd = rs.getMetaData();
            while (rs.next()) {
                HashMap<String, String> file = new HashMap<String, String>();
                int cc = rsmd.getColumnCount();
                for (int a = 1; a <= cc; a++) {
                    String col = rsmd.getColumnName(a);
                    String val = rs.getString(col);
                    file.put(col.toLowerCase(), val);
                }
                result.add(file);
            }
        } catch (SQLException e) {
            throw new JHiveRegistryException(e);
        }
        return result;
    }

    public MediaFile getMediaFile(int id) throws JHiveRegistryException {
        return MediaFilesDao.getMediaFile(id);
    }

    public MediaFile importFile(MediaFile file) throws JHiveRegistryException {
        FileImporter.importFile(file);
        return file;
    }

    public MediaFile importFile(File file) throws JHiveRegistryException {
        FileImporter im = new FileImporter();
        _log.debug(file.toString());
        MediaFile f = new MediaFile(file.getParent(), file.getName());
        int id = im.importFile(f);
        _log.debug("File imported with id" + id);
        return getMediaFile(id);
    }

    public MediaFile createPreview(MediaFile file) {
        _log.debug(file.toString());
        File f = new File(file.get("path") + "/" + file.get("filename"));
        IContainer container = IContainer.make();
        if (container.open(f.getAbsolutePath(), IContainer.Type.READ, null) < 0) {
            container.close();
            throw new IllegalArgumentException("could not open file: " + f.getAbsolutePath());
        }
        int numStreams = container.getNumStreams();
        IStream stream = null;
        IStreamCoder dec_stream = null;
        for (int a = 0; a < numStreams; a++) {
            IStream tmp_stream = container.getStream(a);
            if (tmp_stream.getStreamCoder().getCodecType() == ICodec.Type.CODEC_TYPE_VIDEO) {
                stream = tmp_stream;
                dec_stream = stream.getStreamCoder();
                System.out.println("Video Stream found" + stream.toString());
                break;
            }
        }
        dec_stream.open();
        IVideoPicture picture = IVideoPicture.make(dec_stream.getPixelType(), dec_stream.getWidth(), dec_stream.getHeight());
        IPacket packet = IPacket.make();
        int result = 0;
        int count = 0;
        while ((result = container.readNextPacket(packet)) == 0) {
            if (packet.getStreamIndex() == stream.getIndex()) {
                dec_stream.decodeVideo(picture, packet, 0);
                if (picture.isComplete()) {
                    if (count++ > (Integer) file.get("previewFrameNumber")) {
                        _log.debug("bla");
                        _log.debug(":" + picture);
                        _log.debug("look for filters");
                        List<org.esb.model.Filter> fattrlist = file.getFilters();
                        HiveImage image = new HiveImage(picture);
                        for (org.esb.model.Filter attr : fattrlist) {
                            Filter filter = FilterFactory.getFilter((String) attr.get("id"));
                            if (filter != null && filter instanceof VideoFilter) {
                                _log.debug("VideoFilter found:" + attr);
                                if (attr.get("active") != null && attr.get("active") instanceof String) {
                                }
                                if (attr.get("active") != null && attr.get("active").equals(true)) {
                                    filter.init(attr);
                                    HiveImage image_out = new HiveImage(picture);
                                    if (((VideoFilter) filter).process(image, image_out)) {
                                        image = image_out;
                                    }
                                    _log.debug(" and active");
                                }
                            }
                        }
                        try {
                            SerializedImage img = new SerializedImage();
                            img.setImage(image.toByteArray());
                            file.setPreviewImage(img);
                        } catch (Throwable t) {
                            t.printStackTrace();
                        }
                        _log.debug("picture decoded, break");
                        break;
                    }
                }
            } else {
            }
        }
        return file;
    }
}
