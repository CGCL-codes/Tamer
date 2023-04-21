// ========================================================================
// Copyright (c) 1996 Intelligent Switched Systems, Sydney
// $Id: CfgFile.java 499 2002-03-23 00:38:27Z mattw $
// ========================================================================

package com.i3sp.util;

import org.mortbay.util.Code;

import com.i3sp.util.objparse.ObjParse;
import com.i3sp.util.objparse.ObjHashtable;

import java.io.InputStream;
import java.io.File;
import java.io.PrintStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ByteArrayOutputStream;
import java.util.Vector;
import java.util.Enumeration;

/** Class to lookup config files
 * <p> This class, when given a config file name, searches for the given file
 * through a list of config file paths. This list is retrieved from the
 * system parameter "CFGFILEPATH" if not already defined by a call to
 * setCfgfilePath(). If not defined at all, the path defaults to simply the
 * current directory.
 *
 * <p><h4>Notes</h4>
 * <p> If write is called on a CfgFile, it will write it back to where the
 * file was located in the search path, or to the current directory if it was
 * not found.
 *
 * <p> On unix, the path should be colon-separated, on systems that are
 * backward (i.e. use s slosh instead of a slash for directory seperators)
 * the path should be semi-colon separated. @see java.io.File#pathSeparatorChar
 *
 * <p><h4>Usage</h4>
 * <pre>
 * CfgFile cf = new CfgFile("Global.cfg");
 * </pre>
 *
 * @see com.i3sp.util.objparse.ObjParse
 * @version $Id: CfgFile.java 499 2002-03-23 00:38:27Z mattw $
 * @author Matthew Watson
*/
public class CfgFile
{
    /* ------------------------------------------------------------ */
    private static Vector searchPath = new Vector();
    private static boolean init = false;
    protected String fileName=null;
    protected File file = null;    
    protected long lastModified;
    protected InputStream in=null;
    /* ------------------------------------------------------------ */
    /** Create an association with the given config file
     * @param fileName Name of config file to load
     */
    public CfgFile(String fileName)
    {
	init();
	this.fileName=fileName;
	file = findFile(fileName);
	lastModified=file.lastModified();
    }
    
    /* ------------------------------------------------------------ */
    private static void init()
    {
	if (!init)
	    setCfgfilePath(System.getProperty("CFGFILEPATH"));
    }
    
    /* ------------------------------------------------------------ */
    /** Return the File object for this Config File */
    public File getFile()
    {
	return file;
    }
    /* ------------------------------------------------------------ */
    /** Return the InputStream for parsing over */
    protected InputStream getInputStream() throws java.io.FileNotFoundException
    {
	if (in == null){
	    in = new FileInputStream(file);
	}
	return in;
    }
    /* ------------------------------------------------------------ */
    /** Return an ObjParse for this ConfigFile */
    public ObjParse objParse() throws java.io.FileNotFoundException
    {
	return new ObjParse(getInputStream());
    }
    
    /* ------------------------------------------------------------ */
    /** Read the config file and return it as a Object
     * Note: The file is closed after the read.
     */
    public Object read()
        throws java.io.IOException,
               org.mortbay.tools.converter.ConvertFail
    {
	Object o = null;
	try {
	    o = objParse().parse();
	    o = ObjParse.resolve(o);
	}
	finally{
	    close();
	}
	return o;
    }
    
    /* ------------------------------------------------------------ */
    /** Check the config for modification
     */
    public boolean fileModified()
    {
	File tmp = findFile(fileName);
	if (tmp.lastModified()!=lastModified)
	{
	    file=tmp;
	    lastModified=tmp.lastModified();
	    return true;
	}
	return false;
    }
    
    /* ------------------------------------------------------------ */
    /** Close the file
     * Close the file, but keep other details. This is best used if
     * data has been read from the config file, but the instance will
     * be kept to check the fileModified() method.
     */
    public void close()
    {
	try{
	    if (in!=null)
		in.close();
	}
	catch (java.io.IOException e){
		Code.debug("Ignore:",e);
	}
	finally{	
	    in=null;
	    file = null;
	}    
    }

    /* ------------------------------------------------------------ */
    public void write(Object obj) throws java.io.IOException {
	StringBuffer sb = new StringBuffer();
	ObjParse.doToString(sb, obj);
	sb.append('\n');
	char str[] = sb.toString().toCharArray();
	// Don't write to a printstream over the file, because it doesn't
	// propogate the exceptions that get thrown.
	// first - Convert to byte[] (YUCK!)
	ByteArrayOutputStream bos = new ByteArrayOutputStream(str.length+1);
	PrintStream ps = new PrintStream(bos);
	ps.print(str);
	ps.flush();
	byte bytes[] = bos.toByteArray();
	// Now - Write directly to the file...
	FileOutputStream fos=null;
	try{
	    fos = new FileOutputStream(file);
	    fos.write(bytes);
	}
	finally {
	    if (fos!=null)
		fos.close();
	}
    }
    
    /* ------------------------------------------------------------ */
    /** Set the config file search path */
    public static void setCfgfilePath(String path)
    {
	init = true;
	int from = 0;
	boolean done = false;
	searchPath.removeAllElements();
	if (path == null || path.equals(""))
	{
	    searchPath.addElement("");
	    done = true;
	}
	while (!done)
	{
	    int to = path.indexOf(File.pathSeparatorChar, from);
	    String pathElem = null;
	    if (to == -1)
	    {
		pathElem = path.substring(from);
		done = true;
	    } else
		pathElem = path.substring(from, to);
	    searchPath.addElement(pathElem);
	    from = to+1;
	}
	Code.debug("CfgfilePath:"+searchPath);
    }
    
    /* ------------------------------------------------------------ */
    private File findFile(String fileName)
    {
	return findFile(searchPath, fileName);
    }
    
    /* ------------------------------------------------------------ */
    public static File findFile(Vector searchPath, String fileName)
    {
	if (fileName.charAt(0)!=File.separatorChar)
	    for (Enumeration enum = searchPath.elements();
		 enum.hasMoreElements();)
	    {
		String thePath = enum.nextElement().toString();
		File file = new File(thePath, fileName);
		if (file.exists() && file.isFile() && file.canRead())
		    return file;
	    }
	File file = new File(fileName);
	return file;
    }
    
    /* ------------------------------------------------------------ */
    public String toString()
    {
	if (file!=null)
	    return file.toString();
	return super.toString();
    }
}
