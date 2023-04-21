package de.excrawler.server;

import de.excrawler.server.System.Configuration.CrawlerConfig;
import de.excrawler.server.Databases.DatabaseConnection;
import de.excrawler.server.Databases.Queries;
import de.excrawler.server.FilesManagement.ImageFile;
import de.excrawler.server.FilesManagement.Mime.MimeHelper;
import de.excrawler.server.Imagecrawler.AnalyzeImageCore;
import de.excrawler.server.Imagecrawler.ImageTools;
import de.excrawler.server.Imagecrawler.Thumbnail;
import de.excrawler.server.Logging.Log;
import de.excrawler.server.Plugins.PluginRunner;
import de.excrawler.server.System.StatusCodes;
import java.awt.color.CMMException;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;

/**
 * Analyzes images, creates thumbnails, runs image plugins etc.
 * @author Yves Hoppe <info at yves-hoppe.de>
 * @author Karpouzas George <www.webnetsoft.gr>
 */
public class ImglistWorker extends Thread {

    private String letter = null;

    private int limit;

    public static String STATUS = "imageListWorker is doing nothing";

    public static boolean WORKING = true;

    public ImglistWorker(String chr, int lim) {
        super("imglistWorker-" + chr);
        this.letter = chr;
        this.limit = lim;
    }

    @Override
    public void run() {
        Connection connection = null;
        try {
            String currentLetter = null;
            int current = 0;
            String pattern = "[|]";
            Pattern splitter = Pattern.compile(pattern);
            String[] result = splitter.split(letter);
            while (WORKING == true) {
                currentLetter = result[current];
                connection = DatabaseConnection.open();
                if (connection != null) {
                    PreparedStatement statement = DatabaseConnection.makeStatement(Queries.qryImgListStatus1(currentLetter, DatabaseConnection.buildLimit(limit, 0)), connection, false);
                    ResultSet entry = statement.executeQuery();
                    while (entry.next()) {
                        int returncode = 0;
                        String imageURL = entry.getString("url");
                        String websiteURL = entry.getString("websiteurl");
                        STATUS = "imageListWorker crawls: " + imageURL;
                        System.out.println("Crawling image: " + imageURL);
                        String alt = entry.getString("alt");
                        String title = entry.getString("title");
                        String submitter = entry.getString("submitter");
                        String mimetype = null;
                        int oldpriority = entry.getInt("priority");
                        int crawllistid = entry.getInt("id");
                        String tmpfileName = CrawlerConfig.TMPDIR + File.separatorChar + "images" + File.separatorChar + "tmp_" + currentLetter + "_" + crawllistid;
                        try {
                            ImageFile imgFile = new ImageFile(new File(tmpfileName), tmpfileName);
                            Object[] mime = MimeHelper.getMimeTypes(imgFile.getFile());
                            for (int i = 0; i < mime.length; i++) {
                                if (mime[i].toString().equals("image/jpeg")) {
                                    imgFile.setFormat(ImageTools.JPG);
                                    imgFile.setFormatName("jpg");
                                } else if (mime[i].toString().equals("image/gif")) {
                                    imgFile.setFormat(ImageTools.GIF);
                                    imgFile.setFormatName("gif");
                                } else if (mime[i].toString().equals("image/png")) {
                                    imgFile.setFormat(ImageTools.PNG);
                                    imgFile.setFormatName("png");
                                } else if (mime[i].toString().equals("image/x-ms-bmp")) {
                                    imgFile.setFormat(ImageTools.BMP);
                                    imgFile.setFormatName("bmp");
                                } else {
                                    mimetype = mime[i].toString();
                                    imgFile.setFormat(ImageTools.UNKNOWN);
                                    imgFile.setFormatName("jpg");
                                }
                            }
                            if (imgFile.getFormat() == ImageTools.UNKNOWN) {
                                if (mimetype.equals("text/html") || mimetype.equals("text/xml")) {
                                    returncode = 0;
                                    imgFile.delete();
                                } else if (mimetype.equals("application/octet-stream")) {
                                    returncode = 0;
                                    imgFile.delete();
                                } else if (mimetype.equals("application/x-executable")) {
                                    returncode = 0;
                                    imgFile.delete();
                                } else if (mimetype.equals("application/x-gzip")) {
                                    returncode = 0;
                                    imgFile.delete();
                                } else if (mimetype.equals("video/x-flv")) {
                                    returncode = 0;
                                    imgFile.delete();
                                } else {
                                    returncode = 2;
                                    Log.imagelogger.warn("New image format: " + mimetype);
                                    Log.imagelogger.info("Image path: " + tmpfileName);
                                }
                            } else {
                                imgFile.setImage(ImageIO.read(imgFile.getFile()));
                                int width = AnalyzeImageCore.getImageWidth(imgFile);
                                int height = AnalyzeImageCore.getImageHeight(imgFile);
                                if (width > 0 && height > 0) {
                                    imgFile.setWidth(width);
                                    imgFile.setHeight(height);
                                    imgFile.setType(AnalyzeImageCore.getImageType(imgFile));
                                    imgFile.setTypeName(AnalyzeImageCore.getImageTypeName(imgFile));
                                    int rcode = DbImage.saveImage(currentLetter, imageURL, websiteURL, title, alt, imgFile, submitter, oldpriority, 0);
                                    if (rcode != StatusCodes.DATABASE_UNAVAILABLE) {
                                        imgFile.setID(rcode);
                                        String thumbPath = CrawlerConfig.IMAGEDIR + File.separatorChar + currentLetter + "_" + imgFile.getID();
                                        new File(thumbPath).createNewFile();
                                        ImageFile thumb = new ImageFile();
                                        thumb = Thumbnail.generateThumbnail(imgFile);
                                        thumb.setFileName(thumbPath);
                                        thumb.setFile(new File(thumbPath));
                                        ImageIO.write(thumb.getImage(), imgFile.getFormatName(), thumb.getFile());
                                        imgFile.setThumbnail(thumb);
                                        PluginRunner.runImagecrawlerPlugins(imgFile, websiteURL, currentLetter, this.getName());
                                    } else {
                                        returncode = StatusCodes.DATABASE_UNAVAILABLE;
                                    }
                                } else {
                                    returncode = 0;
                                    imgFile.delete();
                                }
                            }
                        } catch (CMMException ex) {
                            returncode = 0;
                        } catch (FileNotFoundException ex) {
                            Log.imagelogger.warn("Error temporary image file not found", ex);
                            returncode = 2;
                        } catch (Exception ex) {
                            Log.imagelogger.warn("Error at imgcrawler for img: " + tmpfileName, ex);
                            returncode = 2;
                        }
                        if (returncode == 0) entry.deleteRow(); else if (returncode == 2) {
                            Log.imagelogger.debug("Error crawling image " + imageURL + " , setting image status to 7");
                            entry.updateInt("status", StatusCodes.TEMP_UNAVAILABLE);
                            entry.updateTimestamp("date", new java.sql.Timestamp(System.currentTimeMillis()));
                            entry.updateRow();
                        }
                    }
                    entry.close();
                    statement.close();
                    DatabaseConnection.close(connection);
                    super.sleep(50);
                    if (current < result.length - 1) current++; else current = 0;
                }
                if (WORKING == false) break;
            }
        } catch (Exception e) {
            Log.imagelogger.fatal("Encountered a problem at imglistWorker", e);
        }
    }
}
