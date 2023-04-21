package cn.ourpk.robot.internal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import net.sourceforge.rssowl.dao.NewsfeedFactoryException;
import net.sourceforge.rssowl.model.NewsItem;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.jdom.JDOMException;
import cn.ourpk.robot.PostProvider;

public class RssLoadJob extends Job {

    public static final String CONFIG = ".lastupdated";

    private String configPath;

    private Properties prop;

    public RssLoadJob(String name) {
        super(name);
        IPath config = new Path(new File(Platform.getInstanceLocation().getURL().getFile()).getAbsolutePath());
        File folders = new File(config.toOSString());
        if (!folders.exists()) folders.mkdirs();
        config = config.append(CONFIG);
        configPath = config.toOSString();
        File configFile = new File(configPath);
        if (!configFile.exists()) try {
            configFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected IStatus run(IProgressMonitor monitor) {
        System.out.println("Loading rss started.");
        prop = new Properties();
        FileInputStream input = null;
        IStatus status = null;
        try {
            input = new FileInputStream(configPath);
            prop.load(input);
            NewsItem[] items = getUnreadItem();
            PostProvider provider = ProviderFactory.getProvider();
            if (provider != null) {
                provider.doLogin();
                for (NewsItem item : items) {
                    StringBuffer sb = new StringBuffer();
                    sb.append("<a href=\"" + item.getLink() + "\">原文链接</a>");
                    sb.append("<br>");
                    sb.append("原文作者：" + item.getAuthor());
                    sb.append("<br><br>");
                    sb.append(item.getDescription());
                    provider.post(item.getTitle(), sb.toString(), item.getPubDateParsed());
                }
                input.close();
                OutputStream output = new FileOutputStream(configPath);
                prop.store(output, "Updated at " + (new Date()).toString());
                output.close();
            }
            status = Status.OK_STATUS;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            status = new Status(IStatus.ERROR, Activator.ID, "Can't find rss configuration file.");
        } catch (IOException e) {
            e.printStackTrace();
            status = new Status(IStatus.ERROR, Activator.ID, "Failed to write configuration file.");
        }
        return status;
    }

    NewsItem[] getUnreadItem() {
        List<NewsItem> items = new ArrayList<NewsItem>();
        RssDescriptor[] descs = RssFactory.getInstance().getRssItems();
        for (RssDescriptor rssDescriptor : descs) {
            Rssloader loader = new Rssloader(rssDescriptor.getUrl());
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date last = null;
                try {
                    last = formatter.parse(prop.getProperty(rssDescriptor.getUrl(), "0"));
                } catch (ParseException e) {
                    String initialDate = Platform.getPreferencesService().getString(Activator.ID, "initialDate", formatter.format(new Date()), new IScopeContext[] { new DefaultScope() });
                    last = formatter.parse(initialDate);
                }
                if (last == null) throw new IllegalArgumentException("Invalid initial date.");
                prop.setProperty(rssDescriptor.getUrl(), formatter.format(last.getTime()));
                loader.load();
                NewsItem[] newsItems = loader.getItems();
                Date unread = null;
                for (int i = 0; i < newsItems.length; i++) {
                    if (isUnread(last, newsItems[i])) {
                        items.add(newsItems[i]);
                        if (unread == null) unread = newsItems[i].getPubDateParsed(); else if (unread.compareTo(newsItems[i].getPubDateParsed()) < 0) unread = newsItems[i].getPubDateParsed();
                    } else break;
                }
                if (unread != null) prop.setProperty(rssDescriptor.getUrl(), formatter.format(unread));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JDOMException e) {
                e.printStackTrace();
            } catch (NewsfeedFactoryException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return items.toArray(new NewsItem[items.size()]);
    }

    boolean isUnread(Date last, NewsItem item) {
        return item.getPubDateParsed().after(last);
    }
}
