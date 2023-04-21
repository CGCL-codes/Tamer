package org.privale.coreclients.listclient;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import org.privale.clients.EncodeFailedException;
import org.privale.coreclients.newsnetworkclient.ArticleCallback;
import org.privale.node.LocalDownload;
import org.privale.utils.ChannelReader;
import org.privale.utils.ChannelWriter;

public class ListDescriptor implements ArticleCallback, Serializable {

    private static final long serialVersionUID = 1L;

    protected byte[] Key;

    protected String Group;

    protected String SubjectID;

    protected long LastRequest;

    protected int Hit;

    protected boolean AutoExtended;

    private LinkedList<Article> Articles;

    private transient CoreList C;

    private transient LinkedList<Long> PendingArticle;

    private transient LinkedList<LocalDownload> PendingDownloads;

    public ListDescriptor() {
        Articles = new LinkedList<Article>();
    }

    public void Init(CoreList c) {
        C = c;
        PendingDownloads = new LinkedList<LocalDownload>();
        PendingArticle = new LinkedList<Long>();
    }

    public void AddPendingDownload(LocalDownload d) {
        synchronized (PendingDownloads) {
            PendingDownloads.add(d);
        }
    }

    public void CheckArticle(long id) {
        boolean getit = false;
        synchronized (Articles) {
            if (!Articles.contains(id)) {
                synchronized (PendingArticle) {
                    PendingArticle.add(id);
                    getit = true;
                }
            }
        }
        if (getit) {
            C.getNetwork().GetArticle(Group, id, this);
        }
    }

    public File BuildLast(long grain, int backcount) throws IOException {
        File f = C.getTemp().createNewFile("backlist", "dat");
        ChannelWriter cw = new ChannelWriter(f);
        cw.putByte((byte) 0x12);
        synchronized (Articles) {
            long curgrain = grain;
            for (int idx = Articles.size() - 1; idx >= 0 && backcount >= 0; idx--) {
                Article a = Articles.get(idx);
                curgrain -= a.ArticleFile.length();
                if (curgrain < 0) {
                    backcount--;
                    curgrain = grain;
                }
                if (backcount == 0) {
                    cw.putFile(a.ArticleFile);
                }
            }
        }
        cw.close();
        return f;
    }

    public void AddArticle(long number, File data) {
        if (data != null) {
            if (data.length() <= CoreList.MaxArticleSize) {
                synchronized (Articles) {
                    while (Articles.size() > CoreList.MaxCachedArticlesPerList) {
                        Articles.removeFirst();
                    }
                    if (!Articles.contains(number)) {
                        try {
                            File decfile = C.getCrypto().RawDecode(data, ByteBuffer.wrap(Key));
                            ChannelReader cr = new ChannelReader(decfile);
                            int mirror0 = cr.getInt();
                            int mirror1 = cr.getInt();
                            File newfile = null;
                            if (mirror0 == mirror1) {
                                byte istr = cr.getByte();
                                File tmp = cr.getLongFile(C.getTemp());
                                newfile = C.getTemp().createNewFile("article", "dat");
                                ChannelWriter cw = new ChannelWriter(newfile);
                                cw.putByte(istr);
                                cw.putLongFile(tmp);
                                cw.close();
                            }
                            cr.close();
                            decfile.delete();
                            if (newfile != null) {
                                Article a = new Article();
                                a.ArticleFile = newfile;
                                a.ArticleNumber = number;
                                boolean done = false;
                                if (Articles.size() > 0) {
                                    for (int cnt = Articles.size() - 1; cnt >= 0 && !done; cnt--) {
                                        if (a.ArticleNumber > Articles.get(cnt).ArticleNumber) {
                                            Articles.add(cnt + 1, a);
                                            done = true;
                                        }
                                    }
                                } else {
                                    Articles.add(a);
                                }
                            }
                        } catch (EncodeFailedException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                synchronized (PendingArticle) {
                    PendingArticle.remove(number);
                    ProcessPending();
                }
            }
        }
    }

    public void ProcessPending() {
        synchronized (PendingArticle) {
            if (PendingArticle.size() == 0) {
                synchronized (PendingDownloads) {
                    Iterator i = PendingDownloads.iterator();
                    while (i.hasNext()) {
                        LocalDownload d = (LocalDownload) i.next();
                        i.remove();
                        ProcessDownload(d);
                    }
                    Date d = new Date();
                    LastRequest = d.getTime();
                }
            }
        }
    }

    public void Download(LocalDownload d) {
        synchronized (PendingDownloads) {
            if (PendingDownloads.size() > 0) {
                PendingDownloads.add(d);
            } else {
                ProcessDownload(d);
            }
        }
    }

    private void ProcessDownload(LocalDownload d) {
        if (!AutoExtended) {
            Hit++;
            if (Hit >= CoreList.ExtendAfterNumberHits) {
                C.ExtendOneList(Group, SubjectID, Key);
                AutoExtended = true;
            }
        }
        try {
            ChannelReader cr = new ChannelReader(d.PostData);
            long grain = cr.getLong();
            int backcount = cr.getInt();
            cr.close();
            d.DownloadData = BuildLast(grain, backcount);
            System.out.println("BACKLIST: " + grain + " " + backcount + " file is " + d.DownloadData.getPath() + " size is " + d.DownloadData.length());
            d.DownloadComplete = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
