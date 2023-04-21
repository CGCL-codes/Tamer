package org.parit.aries.samples.blog.blogshellcommands;

import org.apache.felix.gogo.commands.Command;
import org.apache.felix.gogo.commands.Option;
import org.apache.felix.gogo.commands.Argument;
import org.apache.felix.karaf.shell.console.OsgiCommandSupport;
import org.osgi.framework.BundleContext;
import org.apache.aries.samples.blog.api.BloggingService;
import org.apache.aries.samples.blog.api.BlogEntry;
import org.osgi.framework.ServiceReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.List;
import org.parit.aries.samples.blog.blogshellcommands.ContextHolder;

@Command(scope = "blog", name = "getBlogEntry", description = "get a blog entry or all entries")
public class GetBlogEntryCommand extends OsgiCommandSupport {

    @Argument(name = "id", description = "the blog entry id, if unspecified all blog entries are shown")
    Long id;

    @Override
    protected Object doExecute() throws Exception {
        BundleContext context = ContextHolder.getInstance().getContext();
        ServiceReference ref = context.getServiceReference(BloggingService.class.getName());
        if (ref == null) {
            System.out.println("No blogging service found, cannot add author");
            return null;
        }
        BloggingService blogging = (BloggingService) context.getService(ref);
        if (id == null) {
            List blogEntries = blogging.getAllBlogEntries();
            for (Object obj : blogEntries) {
                BlogEntry entry = (BlogEntry) obj;
                System.out.println("vvvvvvvvvvvvvvvvvvvvvvvvvvvv");
                System.out.println("id: " + entry.getId());
                System.out.println("title: " + entry.getTitle());
                System.out.println("body: " + entry.getBody());
                System.out.println("author email: " + entry.getAuthorEmail());
                System.out.println("publish date: " + entry.getPublishDate());
                System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
            }
            return null;
        } else {
            return blogging.getBlogEntry(id.longValue());
        }
    }
}
