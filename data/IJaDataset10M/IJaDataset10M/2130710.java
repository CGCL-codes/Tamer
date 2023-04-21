package com.chimshaw.jblogeditor.genericxmlrpc;

import java.util.Hashtable;
import com.chimshaw.jblogeditor.metaweblog.MetaWeblogEntry;

/**
 * @author lshah
 *
 */
public class GenericXMLRPCBlogEntry extends MetaWeblogEntry {

    public GenericXMLRPCBlogEntry(String id, GenericXMLRPCBlog blog) {
        super(id, blog);
    }

    public GenericXMLRPCBlogEntry(Hashtable entryHash, GenericXMLRPCBlog blog) {
        super(entryHash, blog);
    }
}
