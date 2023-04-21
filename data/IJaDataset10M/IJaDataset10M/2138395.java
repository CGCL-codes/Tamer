package org.tamacat.httpd.webdav;

import java.io.File;
import org.tamacat.httpd.config.ServiceUrl;
import com.bradmcevoy.http.Resource;
import com.bradmcevoy.http.ResourceFactory;
import com.ettrema.http.fs.FileSystemResourceFactory;
import com.ettrema.http.fs.FsDirectoryResource;
import com.ettrema.http.fs.FsFileResource;
import com.ettrema.http.fs.NullSecurityManager;

public class WebDavResourceFactory implements ResourceFactory {

    private String docsRoot;

    private FileSystemResourceFactory factory;

    private ServiceUrl serviceUrl;

    public void setDocsRoot(String docsRoot) {
        this.docsRoot = docsRoot;
    }

    public WebDavResourceFactory(ServiceUrl serviceUrl, String docsRoot) {
        this.serviceUrl = serviceUrl;
        this.docsRoot = docsRoot;
        com.bradmcevoy.http.SecurityManager securityManager = new NullSecurityManager();
        File file = new File(docsRoot);
        factory = new FileSystemResourceFactory(file, securityManager, ".");
    }

    @Override
    public Resource getResource(String host, String url) {
        url = stripContext(url);
        File file = new File(docsRoot + url);
        if (file.isDirectory()) {
            return new FsDirectoryResource(host, factory, file);
        } else {
            return new FsFileResource(host, factory, file);
        }
    }

    private String stripContext(String url) {
        return url.replaceFirst('/' + serviceUrl.getPath(), "");
    }
}
