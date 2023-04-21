package net.sourceforge.blogentis.plugins.importexport.impl;

import java.io.OutputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;
import java.util.zip.GZIPOutputStream;
import javax.xml.transform.OutputKeys;
import net.sourceforge.blogentis.om.AbstractConfigurable;
import net.sourceforge.blogentis.om.Blog;
import net.sourceforge.blogentis.om.Comment;
import net.sourceforge.blogentis.om.Post;
import net.sourceforge.blogentis.om.Section;
import net.sourceforge.blogentis.om.StoredBlog;
import net.sourceforge.blogentis.om.StoredBlogPeer;
import net.sourceforge.blogentis.plugins.AbstractBlogExtension;
import net.sourceforge.blogentis.plugins.importexport.IExportExtension;
import net.sourceforge.blogentis.plugins.importexport.ImportExportPlugin;
import net.sourceforge.blogentis.turbine.BlogRunData;
import net.sourceforge.blogentis.utils.MappedConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;
import org.apache.xml.serializer.Method;
import org.apache.xml.serializer.OutputPropertiesFactory;
import org.apache.xml.serializer.Serializer;
import org.apache.xml.serializer.SerializerFactory;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public class BlogExportProvider extends AbstractBlogExtension implements IExportExtension {

    public String getPreferencesKey() {
        return BRPrefsExtension.BLOGENTIS_EXPORT_OPTIONS;
    }

    public String getFileTypeName() {
        return "Blogentis native (blog, posts, comments, settings)";
    }

    public String getIdentifier() {
        return ImportExportPlugin.BLOGENTIS_CONTENT_IDENTIFIER;
    }

    public String getContentType(Blog blog, Configuration conf) {
        return "application/octet-stream";
    }

    public String getFileName(BlogRunData data, Blog blog, Configuration conf) {
        return blog.getName() + "-content.xml.gz";
    }

    public String getName() {
        return "Backup blog contents (posts, comments, sections)";
    }

    public String performExport(BlogRunData data, Blog blog, OutputStream stream, Configuration conf) throws Exception {
        Properties props = OutputPropertiesFactory.getDefaultMethodProperties(Method.XML);
        props.put(OutputPropertiesFactory.S_KEY_INDENT_AMOUNT, "1");
        props.put(OutputKeys.INDENT, "yes");
        props.put(OutputKeys.CDATA_SECTION_ELEMENTS, "short long comment");
        Serializer s = SerializerFactory.getSerializer(props);
        ContentHandler c = s.asContentHandler();
        GZIPOutputStream gzos = new GZIPOutputStream(data.getResponse().getOutputStream());
        s.setOutputStream(gzos);
        c.startDocument();
        writeBlogHeader(blog, c);
        c.endDocument();
        gzos.finish();
        return null;
    }

    protected void dumpConfiguration(AbstractConfigurable bc, ContentHandler c) throws Exception {
        byte[] od = bc.getObjectData();
        if (od == null || od.length == 0) return;
        AttributesImpl a = new AttributesImpl();
        c.startElement("", "", "props", a);
        Configuration mc = new MappedConfiguration(bc);
        if (bc instanceof StoredBlog) mc = new FilterAdminPropsConfiguration(mc);
        for (Iterator i = mc.getKeys(); i.hasNext(); ) {
            String key = (String) i.next();
            if (StringUtils.isEmpty(key)) continue;
            a.clear();
            Object o = mc.getProperty(key);
            a.addAttribute("", "", "key", "", key);
            if (o instanceof Collection && ((Collection) o).size() == 1) o = ((Collection) o).iterator().next();
            if (!(o instanceof Collection)) {
                a.addAttribute("", "", "value", "", o.toString());
            }
            c.startElement("", "", "prop", a);
            if (o instanceof Collection) for (Iterator j = ((Collection) o).iterator(); j.hasNext(); ) {
                String val = j.next().toString();
                a.clear();
                a.addAttribute("", "", "value", "", val);
                c.startElement("", "", "item", a);
                c.endElement("", "", "item");
            }
            c.endElement("", "", "prop");
        }
        c.endElement("", "", "props");
    }

    protected void writeSimpleTag(String name, String value, ContentHandler c) throws SAXException {
        AttributesImpl a = new AttributesImpl();
        if (!StringUtils.isEmpty(value)) {
            c.startElement("", "", name, a);
            char[] ca = value.toCharArray();
            c.characters(ca, 0, ca.length);
            c.endElement("", "", name);
        }
    }

    protected void writeSection(Section sec, ContentHandler c) throws Exception {
        AttributesImpl a = new AttributesImpl();
        a.addAttribute("", "", "name", "", sec.getName());
        a.addAttribute("", "", "title", "", sec.getTitle());
        a.addAttribute("", "", "description", "", sec.getDescription());
        if (sec.isHidden()) a.addAttribute("", "", "hidden", "", "true");
        c.startElement("", "", "section", a);
        dumpConfiguration(sec, c);
        c.endElement("", "", "section");
    }

    protected void writeComment(Comment com, ContentHandler c) throws Exception {
        AttributesImpl a = new AttributesImpl();
        if (!StringUtils.isEmpty(com.getName())) a.addAttribute("", "", "name", "", com.getName());
        if (!StringUtils.isEmpty(com.getEmail())) a.addAttribute("", "", "email", "", com.getEmail());
        if (!StringUtils.isEmpty(com.getTitle())) a.addAttribute("", "", "title", "", com.getTitle());
        if (!StringUtils.isEmpty(com.getUrl())) a.addAttribute("", "", "url", "", com.getUrl());
        if (!StringUtils.isEmpty(com.getIp())) a.addAttribute("", "", "ip", "", com.getIp());
        if (!StringUtils.isEmpty(com.getUserName())) a.addAttribute("", "", "userName", "", com.getUserName());
        a.addAttribute("", "", "date", "", String.valueOf(com.getPostedTime().getTime()));
        a.addAttribute("", "", "status", "", String.valueOf(com.getStatus()));
        c.startElement("", "", "comment", a);
        if (!StringUtils.isEmpty(com.getText())) {
            char[] ca = com.getText().toCharArray();
            c.characters(ca, 0, ca.length);
        }
        c.endElement("", "", "comment");
    }

    protected void writePost(Post p, ContentHandler c) throws Exception {
        AttributesImpl a = new AttributesImpl();
        a.addAttribute("", "", "date", "", String.valueOf(p.getPostedTime().getTime()));
        a.addAttribute("", "", "author", "", p.getAuthorId());
        if (!StringUtils.isEmpty(p.getUriFragment())) a.addAttribute("", "", "fragment", "", p.getUriFragment());
        a.addAttribute("", "", "title", "", p.getTitle());
        a.addAttribute("", "", "type", "", String.valueOf(p.getPostType()));
        c.startElement("", "", "post", a);
        dumpConfiguration(p, c);
        writeSimpleTag("short", p.getShortDescription(), c);
        writeSimpleTag("long", p.getFullText(), c);
        for (Iterator i = p.getSections().iterator(); i.hasNext(); ) {
            AttributesImpl sa = new AttributesImpl();
            sa.addAttribute("", "", "ref", "", ((Section) i.next()).getName());
            c.startElement("", "", "section", sa);
            c.endElement("", "", "section");
        }
        for (Iterator i = p.getComments().iterator(); i.hasNext(); ) {
            writeComment((Comment) i.next(), c);
        }
        c.endElement("", "", "post");
    }

    protected void writeBlogHeader(Blog b, ContentHandler c) throws Exception {
        StoredBlog sb = StoredBlogPeer.retrieveByPK(b.getBlogId());
        AttributesImpl a = new AttributesImpl();
        a.addAttribute("", "", "name", "", sb.getName());
        a.addAttribute("", "", "title", "", sb.getTitle());
        a.addAttribute("", "", "description", "", sb.getDescription());
        a.addAttribute("", "", "version", "", "1.0");
        if (sb.getBaseUrl() != null) a.addAttribute("", "", "baseUrl", "", sb.getBaseUrl());
        if (sb.isHidden()) a.addAttribute("", "", "hidden", "", "true");
        c.startElement("", "", "blog", a);
        dumpConfiguration(sb, c);
        for (Iterator i = sb.getSections().iterator(); i.hasNext(); ) {
            writeSection((Section) i.next(), c);
        }
        for (Iterator i = sb.getPosts().iterator(); i.hasNext(); ) {
            writePost((Post) i.next(), c);
        }
        c.endElement("", "", "blog");
    }
}
