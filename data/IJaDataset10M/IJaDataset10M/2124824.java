package resourcemanager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * this class provides functions used to generate a relative path
 * from two absolute paths
 * @author David M. Howard
 */
public class RelativePath {

    /**
	 * break a path down into individual elements and add to a list.
	 * example : if a path is /a/b/c/d.txt, the breakdown will be [d.txt,c,b,a]
	 * @param f input file
	 * @return a List collection with the individual elements of the path in
reverse order
	 */
    @SuppressWarnings("unchecked")
    private static List getPathList(File f) {
        List l = new ArrayList();
        File r;
        try {
            r = f.getCanonicalFile();
            while (r != null) {
                l.add(r.getName());
                r = r.getParentFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
            l = null;
        }
        return l;
    }

    /**
	 * figure out a string representing the relative path of
	 * 'f' with respect to 'r'
	 * @param r home path
	 * @param f path of file
	 */
    @SuppressWarnings("unchecked")
    private static String matchPathLists(List r, List f) {
        int i;
        int j;
        String s;
        s = "";
        i = r.size() - 1;
        j = f.size() - 1;
        while ((i >= 0) && (j >= 0) && (r.get(i).equals(f.get(j)))) {
            i--;
            j--;
        }
        for (; i >= 0; i--) {
            s += ".." + File.separator;
        }
        for (; j >= 1; j--) {
            s += f.get(j) + File.separator;
        }
        s += f.get(j);
        return s;
    }

    /**
	 * get relative path of File 'f' with respect to 'home' directory
	 * example : home = /a/b/c
	 *           f    = /a/d/e/x.txt
	 *           s = getRelativePath(home,f) = ../../d/e/x.txt
	 * @param home base path, should be a directory, not a file, or it doesn't
make sense
	 * @param f file to generate path for
	 * @return path from home to f as a string
	 */
    @SuppressWarnings("unchecked")
    public static String getRelativePath(File home, File f) {
        List homelist;
        List filelist;
        String s;
        homelist = getPathList(home);
        filelist = getPathList(f);
        s = matchPathLists(homelist, filelist);
        return s;
    }
}
