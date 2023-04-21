package org.xmlvm.iphone;

import java.io.File;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import org.xmlvm.XMLVMSkeletonOnly;
import org.xmlvm.iphone.NSURL;

@XMLVMSkeletonOnly
public class NSFileManager extends NSObject {

    private static final NSFileManager defaultMngr = new NSFileManager();

    private NSFileManager() {
    }

    public static NSFileManager defaultManager() {
        return defaultMngr;
    }

    public boolean fileExistsAtPath(String path) {
        return new File(path).exists();
    }

    public boolean createDirectoryAtPath(String path, boolean createIntermediates, Map<String, String> attributes) {
        if (createIntermediates) return new File(path).mkdirs(); else return new File(path).mkdir();
    }

    public List<String> contentsOfDirectoryAtPath(String path, NSErrorHolder error) {
        List<String> files = new ArrayList<String>();
        for (String f : new File(path).list()) {
            files.add(f);
        }
        return files;
    }

    /**
     * - (BOOL)moveItemAtURL:(NSURL *)srcURL toURL:(NSURL *)dstURL
     * error:(NSError **)error ;
     */
    public boolean moveItemAtURL(NSURL srcURL, NSURL dstURL, NSErrorHolder error) {
        throw new RuntimeException("Stub");
    }

    /**
     * - (BOOL)copyItemAtURL:(NSURL *)srcURL toURL:(NSURL *)dstURL
     * error:(NSError **)error ;
     */
    public boolean copyItemAtURL(NSURL srcURL, NSURL dstURL, NSErrorHolder error) {
        throw new RuntimeException("Stub");
    }

    /**
     * - (BOOL)removeItemAtPath:(NSString *)path error:(NSError **)error;
     */
    public boolean removeItemAtPath(String path, NSErrorHolder error) {
        throw new RuntimeException("Stub");
    }

    /**
     * - (BOOL)removeItemAtURL:(NSURL *)URL error:(NSError **)error ;
     */
    public boolean removeItemAtURL(NSURL URL, NSErrorHolder error) {
        throw new RuntimeException("Stub");
    }
}
