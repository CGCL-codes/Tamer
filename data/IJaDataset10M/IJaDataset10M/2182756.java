package superpodder.controller;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import superpodder.controller.feedreader.FeedReader;
import superpodder.controller.feedreader.Rome;
import superpodder.controller.feedreader.RssLib4J;
import superpodder.gui.MainView;
import superpodder.model.Feed;
import superpodder.model.Settings;

public class FeedDownloader extends Thread {

    private FeedManager feedManager = null;

    protected JLabel currentLabel = null;

    protected JProgressBar currentProgress = null;

    private Feed currentFeed = null;

    protected boolean stopDownload = false;

    /**
     *  sometimes we do not want to download
     *  but just check the total size. This parameter
     *  is used for that.
     */
    private boolean download = true;

    private FeedReader feedReader[] = new FeedReader[2];

    public FeedDownloader(FeedManager feedManager, Boolean download) {
        this.download = download;
        this.feedManager = feedManager;
        this.feedReader[1] = new Rome(this);
        this.feedReader[0] = new RssLib4J(this);
        this.feedReader[1] = new Rome(this);
    }

    public synchronized void log(String txt) {
        MainView.logView.textArea.append(txt);
    }

    /**
     * Downloads the feed xml, parses it and downloads the podcasts
     * 
     * @param url  : location of the feed xml.
     *           
     */
    public void download(String url) {
        this.currentLabel.setText("Processing feed: " + url);
        URL feedUrl = null;
        String localFile;
        this.currentLabel.setForeground(new Color(34, 139, 34));
        this.currentLabel.setText("Downloading RSS Feed XML");
        this.currentProgress.setForeground(new Color(34, 139, 34));
        try {
            feedUrl = new URL(url);
            localFile = this.downloadFile(feedUrl, Settings.downloadDir + "\\tmp\\" + this.currentFeed.group, this.currentFeed.name + ".xml");
        } catch (final Exception se) {
            String msg = new String("ERROR downloading podcast: " + this.currentFeed.group + "/" + this.currentFeed.name + " " + se.getMessage());
            this.log(url + ":" + msg);
            this.feedManager.writeToHistory(feedUrl.toString() + " error", 0, this.download);
            this.currentLabel.setForeground(Color.red);
            this.currentLabel.setText(msg);
            this.currentProgress.setValue(100);
            this.currentProgress.setForeground(Color.red);
            this.currentProgress.setString(this.feedManager.downloadFailure(feedUrl.toString(), "error"));
            return;
        }
        String[] downloadLocs = new String[0];
        String lastError = new String("last error:");
        for (FeedReader feedRead : this.feedReader) {
            try {
                this.currentLabel.setForeground(new Color(34, 139, 34));
                this.currentLabel.setText("Parsing RSS Feed XML and retrieving podcast locations using" + feedRead.getClass().getName() + " rss library for" + feedUrl.toString());
                this.currentProgress.setForeground(new Color(34, 139, 34));
                downloadLocs = feedRead.extractPodCasts(localFile, this.currentFeed.number);
                if (downloadLocs.length == 0) {
                    this.feedManager.writeToHistory(feedUrl.toString() + " no_podcast", 0, this.download);
                    this.currentLabel.setForeground(Color.blue);
                    this.currentLabel.setText("No podcasts for: " + this.currentFeed.group + "/" + this.currentFeed.name);
                    this.currentProgress.setValue(100);
                    this.currentProgress.setForeground(Color.blue);
                    this.currentProgress.setString(this.feedManager.downloadFailure(feedUrl.toString(), "no_podcast"));
                    return;
                }
                if (downloadLocs.length > 0) {
                    break;
                }
            } catch (final Exception se1) {
                String msg = new String("ERROR with feedreader " + feedRead.getClass().getName() + " " + this.currentFeed.group + "/" + this.currentFeed.name + " : " + se1.getMessage());
                this.log(msg + "\n\n");
                this.currentLabel.setForeground(Color.red);
                this.currentLabel.setText(msg);
                this.currentProgress.setForeground(Color.red);
                lastError = se1.toString();
            }
        }
        if (downloadLocs.length == 0) {
            if (lastError.compareTo("last error:") == 0) {
                this.feedManager.writeToHistory(feedUrl.toString() + " no_podcast", 0, this.download);
                this.currentLabel.setForeground(Color.blue);
                this.currentLabel.setText("No podcasts for: " + this.currentFeed.group + "/" + this.currentFeed.name);
                this.currentProgress.setValue(100);
                this.currentProgress.setForeground(Color.blue);
                this.currentProgress.setString(this.feedManager.downloadFailure(feedUrl.toString(), "no_podcast"));
            } else {
                String msg = new String("Problem with all feed readers " + this.currentFeed.group + "/" + this.currentFeed.name + " : " + lastError);
                this.log(msg + "\n\n");
                this.feedManager.writeToHistory(feedUrl.toString() + " error", 0, this.download);
                this.currentLabel.setForeground(Color.red);
                this.currentLabel.setText(msg);
                this.currentProgress.setValue(100);
                this.currentProgress.setForeground(Color.red);
                this.currentProgress.setString(this.feedManager.downloadFailure(feedUrl.toString(), "error"));
            }
            return;
        }
        this.currentLabel.setForeground(new Color(34, 139, 34));
        this.currentLabel.setText("Found podcasts, start downloading " + this.currentFeed.group + "/" + this.currentFeed.name);
        this.currentProgress.setForeground(new Color(34, 139, 34));
        int tries = 0;
        final int maxTries = 3;
        while (tries < maxTries) {
            try {
                double totalSize = 0;
                for (final String element : downloadLocs) {
                    Boolean downloaded = true;
                    if (element.endsWith("torrent") && Settings.torrentLink.compareTo("true") == 0) {
                        this.currentLabel.setText("Activating torrent link: " + this.currentFeed.group + "/" + this.currentFeed.name + " : " + element);
                        if (!this.feedManager.history.containsKey(feedUrl.toString() + " " + element)) {
                            if (!this.download) {
                                this.currentLabel.setForeground(new Color(0, 100, 0));
                                this.currentLabel.setText("Torrent link. Size could not be determined" + this.currentFeed.group + "\\" + this.currentFeed.name);
                            } else {
                                Runtime.getRuntime().exec(Settings.torrentExe + ' ' + element);
                                System.out.println("writing " + element + " to repo");
                                this.feedManager.writeToHistory(feedUrl.toString() + " " + element, 0, this.download);
                            }
                        } else {
                            this.feedManager.writeToHistory(feedUrl.toString() + " already_downloaded", 0, this.download);
                            this.currentLabel.setForeground(Color.blue);
                            this.currentLabel.setText("Already downloaded before: " + this.currentFeed.group + "/" + this.currentFeed.name + "/" + element);
                            this.currentProgress.setValue(100);
                            this.currentProgress.setForeground(Color.blue);
                            this.currentProgress.setString(this.feedManager.downloadFailure(feedUrl.toString(), "already_downloaded"));
                            this.currentProgress.setValue(100);
                        }
                    } else {
                        final URL podCastUrl = new URL(element);
                        final File file = new File(podCastUrl.getFile());
                        if (!this.feedManager.history.containsKey(url + " " + file.getName())) {
                            System.out.println("writing " + file.getName() + " to repo when finished");
                            downloaded = false;
                            HttpURLConnection urlConnection = null;
                            int size = -1;
                            try {
                                urlConnection = (HttpURLConnection) podCastUrl.openConnection();
                                size = urlConnection.getContentLength();
                                urlConnection.disconnect();
                            } catch (Exception se) {
                                try {
                                    urlConnection.disconnect();
                                } catch (Exception se1) {
                                    System.out.println("ignore exception");
                                }
                            }
                            final double sizeInMB = (double) size / (double) (1024 * 1024);
                            totalSize += sizeInMB;
                            if (!this.download) {
                                this.currentLabel.setText("Size: " + this.feedManager.round2Decimals(totalSize) + "(MB): " + this.currentFeed.group + "\\" + this.currentFeed.name);
                                this.feedManager.writeToHistory(feedUrl.toString() + " " + file.getName(), sizeInMB, this.download);
                            } else {
                                this.currentLabel.setForeground(new Color(34, 139, 34));
                                this.currentLabel.setText("Downloading " + this.feedManager.round2Decimals(sizeInMB) + "(MB): " + this.currentFeed.group + "\\" + this.currentFeed.name + "\\" + file.getName());
                                this.currentProgress.setForeground(new Color(34, 139, 34));
                                final String fileOutPath = Settings.downloadDir + "\\" + this.currentFeed.group + "\\" + this.currentFeed.name;
                                if (this.download) {
                                    downloadFile(new URL(element), fileOutPath, null);
                                }
                                this.feedManager.writeToHistory(url + " " + file.getName(), sizeInMB, this.download);
                                this.currentLabel.setForeground(new Color(184, 134, 11));
                                this.currentLabel.setText("Finished " + this.feedManager.round2Decimals(totalSize) + "(MB): " + this.currentFeed.group + "\\" + this.currentFeed.name + "\\" + file.getName());
                                this.currentProgress.setForeground(new Color(184, 134, 11));
                            }
                        } else {
                            if (downloaded) {
                                this.feedManager.writeToHistory(feedUrl.toString() + " already_downloaded", 0, this.download);
                                this.currentLabel.setForeground(Color.blue);
                                this.currentLabel.setText("Already downloaded before: " + this.currentFeed.group + "/" + this.currentFeed.name + "/" + file.getName());
                                this.currentProgress.setValue(100);
                                this.currentProgress.setForeground(Color.blue);
                                this.currentProgress.setString(this.feedManager.downloadFailure(feedUrl.toString(), "already_downloaded"));
                            }
                        }
                    }
                }
                return;
            } catch (final Exception se) {
                tries++;
                final long sleep = tries * 10;
                if (this.stopDownload) {
                    this.currentLabel.setText("Stopped. Deleted: " + this.currentFeed.group + "/" + this.currentFeed.name);
                    return;
                }
                if (tries < maxTries) {
                    this.currentLabel.setForeground(Color.red);
                    this.currentLabel.setText("ERROR downloading: " + this.currentFeed.group + "/" + this.currentFeed.name + " " + se.getMessage() + " " + tries + "/" + maxTries + " tries. Sleeping: " + sleep + " seconds");
                    try {
                        Thread.sleep(sleep * 1000);
                    } catch (final Exception e) {
                        System.out.println("Problem sleeping: " + e.getMessage());
                    }
                } else {
                    String msg = new String("ERROR downloading: " + this.currentFeed.group + "/" + this.currentFeed.name + se.getMessage() + " " + tries + "/" + maxTries + " tries");
                    this.log(msg + "\n\n");
                    this.feedManager.writeToHistory(feedUrl.toString() + " error", 0, this.download);
                    this.currentLabel.setForeground(Color.red);
                    this.currentLabel.setText(msg);
                    this.currentProgress.setValue(100);
                    this.currentProgress.setForeground(Color.red);
                    this.currentProgress.setString(this.feedManager.downloadFailure(feedUrl.toString(), "error"));
                    return;
                }
            }
        }
    }

    public String downloadFile(URL location, String fileOutPath, String fileOutName) throws Exception {
        Boolean success = this.feedManager.createDirectory(fileOutPath);
        if (!success) {
            this.currentLabel.setForeground(Color.red);
            this.currentLabel.setText("ERROR: could not create directory for: " + fileOutPath);
            throw new Exception("Can not create directory");
        }
        HttpURLConnection urlConnection = null;
        FileOutputStream fileOut = null;
        InputStream inputStreamReader = null;
        String error = "";
        try {
            final File file = new File(location.getFile());
            if (fileOutName == null) {
                fileOutPath = fileOutPath + "\\" + file.getName();
            } else {
                fileOutPath = fileOutPath + "\\" + fileOutName;
            }
            System.out.println("Downloading file from: " + location.toString());
            System.out.println("Downloading file to: " + fileOutPath);
            fileOut = new FileOutputStream(fileOutPath);
            urlConnection = (HttpURLConnection) location.openConnection();
            urlConnection.connect();
            final int size = urlConnection.getContentLength();
            inputStreamReader = urlConnection.getInputStream();
            final int bufferSize = 2048;
            int byteCount = 0;
            final byte[] buf = new byte[bufferSize];
            int len;
            this.currentProgress.setValue(0);
            while ((len = inputStreamReader.read(buf)) > 0) {
                fileOut.write(buf, 0, len);
                byteCount += len;
                final float progress = 100 * (new Float(byteCount) / new Float(size));
                this.currentProgress.setValue(Math.min((int) progress, 100));
                if (this.stopDownload) {
                    fileOut.close();
                    inputStreamReader.close();
                    urlConnection.disconnect();
                    (new File(fileOutPath)).delete();
                    throw new Exception("Found Stop");
                }
            }
            fileOut.close();
            inputStreamReader.close();
            urlConnection.disconnect();
            this.currentProgress.setValue(100);
            return fileOutPath;
        } catch (Exception se) {
            error = se.toString();
            try {
                fileOut.close();
            } catch (Exception se1) {
                System.out.println("ingore exception");
            }
            try {
                inputStreamReader.close();
            } catch (Exception se2) {
                System.out.println("ingore exception");
            }
            try {
                urlConnection.disconnect();
            } catch (Exception se3) {
                System.out.println("ingore exception");
            }
        }
        throw new Exception("Error in downloading file " + error);
    }

    @Override
    public void run() {
        this.startDownload();
    }

    public void startDownload() {
        this.currentFeed = this.feedManager.getNextFeed(false);
        while (this.currentFeed != null) {
            this.feedManager.createDownloadStatus(this);
            this.download(this.currentFeed.url);
            this.currentFeed = this.feedManager.getNextFeed(false);
            this.feedManager.update();
        }
        this.feedManager.threadDone();
    }
}
