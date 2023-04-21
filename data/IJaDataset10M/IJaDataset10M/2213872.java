package com.bitgate.util.services.engine.tags.u;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import org.w3c.dom.Node;
import com.bitgate.util.debug.Debug;
import com.bitgate.util.services.engine.DocumentTag;
import com.bitgate.util.services.engine.RenderEngine;
import com.bitgate.util.services.engine.TagInspector;
import com.bitgate.util.services.engine.tags.ElementDescriber;

/**
 * This element allows you to access the internal Jar, loaded by the <code>Constants</code> class.
 *
 * @author Kenji Hollis &lt;kenji@nuklees.com&gt;
 * @version $Id: //depot/nuklees/util/services/engine/tags/u/Jar.java#37 $
 * @see nuklees.util.constants.Constants Constants class for more information.
 */
public class Jar extends DocumentTag implements ElementDescriber {

    private HashMap tags;

    private String mode, action, varname, path, rootless, fileAttribute, filenameAttribute, destAttribute, callProc;

    public Jar() {
    }

    public ArrayList getSubElements() {
        return new ArrayList();
    }

    public void prepareTag(Node n) {
        super.prepareTag(n);
        tags = TagInspector.getNodes(n);
        mode = TagInspector.get(n, "mode");
        action = TagInspector.get(n, "action");
        varname = TagInspector.get(n, "var");
        path = TagInspector.get(n, "path");
        fileAttribute = TagInspector.get(n, "file");
        filenameAttribute = TagInspector.get(n, "filename");
        destAttribute = TagInspector.get(n, "dest");
        rootless = TagInspector.get(n, "rootless");
        callProc = TagInspector.get(n, "call");
    }

    public StringBuffer render(RenderEngine c) {
        if (c.isBreakState() || !c.canRender("u")) {
            return new StringBuffer();
        }
        String logTime = null;
        if (c.getWorkerContext() != null) {
            logTime = c.getWorkerContext().getWorkerStart();
        }
        StringBuffer buffer = new StringBuffer();
        mode = TagInspector.processElement(mode, c);
        action = TagInspector.processElement(action, c);
        varname = TagInspector.processElement(varname, c);
        path = TagInspector.processElement(path, c);
        fileAttribute = TagInspector.processElement(fileAttribute, c);
        filenameAttribute = TagInspector.processElement(filenameAttribute, c);
        destAttribute = TagInspector.processElement(destAttribute, c);
        rootless = TagInspector.processElement(rootless, c);
        callProc = TagInspector.processElement(callProc, c);
        boolean isRooted = true;
        String currentDocroot = null;
        if (c.getWorkerContext() == null) {
            if (c.getRenderContext().getCurrentDocroot() == null) {
                currentDocroot = ".";
            } else {
                currentDocroot = c.getRenderContext().getCurrentDocroot();
            }
        } else {
            currentDocroot = c.getWorkerContext().getDocRoot();
        }
        if (rootless.equalsIgnoreCase("true")) {
            if (c.getVendContext().getVend().getIgnorableDocroot(c.getClientContext().getMatchedHost())) {
                isRooted = false;
                currentDocroot = "";
            }
        }
        if (!currentDocroot.endsWith("/")) {
            if (!currentDocroot.equals("") && currentDocroot.length() > 0) {
                currentDocroot += "/";
            }
        }
        if (fileAttribute != null) {
            fileAttribute = fileAttribute.replaceAll("\\.\\.", "");
        }
        if (filenameAttribute != null) {
            filenameAttribute = filenameAttribute.replaceAll("\\.\\.", "");
        }
        if (destAttribute != null) {
            destAttribute = destAttribute.replaceAll("\\.\\.", "");
        }
        if (destAttribute == null || destAttribute.equals("")) {
            destAttribute = "./";
        }
        if (action.equalsIgnoreCase("update")) {
            if (mode == null || mode.equals("")) {
                c.setExceptionState(true, "&lt;Jar&gt; element requires a mode of add/delete to be set.");
                return new StringBuffer();
            }
            JarEntry je = null;
            java.io.File tmpFile = null, jarFile = null;
            FileOutputStream fos = null;
            JarOutputStream jos = null;
            BufferedWriter bw = null;
            FileInputStream fis = null;
            JarInputStream jis = null;
            BufferedReader br = null;
            int buf = -1;
            boolean badZipFile = false, refreshFromNewFile = false;
            String jarFilename, addFilename, tmpJarFilename;
            StringBuffer contentData;
            if (mode.equalsIgnoreCase("add")) {
                if (fileAttribute.equals("") || filenameAttribute.equals("")) {
                    c.setExceptionState(true, "Jar requires file and filename attributes.");
                    return new StringBuffer();
                }
            } else {
                if (fileAttribute.equals("") || filenameAttribute.equals("")) {
                    c.setExceptionState(true, "Jar requires file and filename attributes.");
                    return new StringBuffer();
                }
            }
            jarFilename = currentDocroot + fileAttribute;
            addFilename = filenameAttribute;
            if (mode.equalsIgnoreCase("add")) {
                contentData = TagInspector.processBody(this, c);
            } else {
                contentData = null;
            }
            tmpJarFilename = jarFilename + ".tmp";
            try {
                tmpFile = new java.io.File(tmpJarFilename);
                fos = new FileOutputStream(tmpFile);
                jos = new JarOutputStream(fos);
                bw = new BufferedWriter(new OutputStreamWriter(jos));
            } catch (IOException e) {
                c.setExceptionState(true, "Failed to create JAR file '" + tmpJarFilename + "': " + e.getMessage());
                return new StringBuffer();
            }
            jarFile = new java.io.File(jarFilename);
            if (jarFile.exists()) {
                try {
                    fis = new FileInputStream(jarFile);
                    jis = new JarInputStream(fis);
                    br = new BufferedReader(new InputStreamReader(jis));
                } catch (IOException e) {
                    c.setExceptionState(true, "Unable to open JAR file '" + jarFilename + "': " + e.getMessage());
                    return new StringBuffer();
                }
            }
            try {
                while ((je = jis.getNextJarEntry()) != null) {
                    refreshFromNewFile = false;
                    if (je.getName().compareToIgnoreCase(addFilename) == 0) {
                        refreshFromNewFile = true;
                    }
                    if (refreshFromNewFile) {
                    } else {
                        jos.putNextEntry(je);
                        int index = 0;
                        buf = -1;
                        while ((buf = br.read()) != -1) {
                            bw.write(buf);
                            index++;
                        }
                        bw.flush();
                        jos.closeEntry();
                    }
                }
            } catch (IOException e) {
                c.setExceptionState(true, "Unable to manipulate JAR file: " + e.getMessage());
                return new StringBuffer();
            } catch (Exception e) {
                c.setExceptionState(true, "File or Jar file contains no data: " + e.getMessage());
                return new StringBuffer();
            }
            try {
                br.close();
                jis.close();
                fis.close();
            } catch (IOException e) {
                c.setExceptionState(true, "Unable to close original JAR file: " + e.getMessage());
                return new StringBuffer();
            }
            if (mode.equalsIgnoreCase("add")) {
                try {
                    je = new JarEntry(addFilename);
                    jos.putNextEntry(je);
                    int index = 0;
                    buf = -1;
                    bw.write(contentData.toString());
                    bw.flush();
                    Debug.user(logTime, "Added entry '" + je.getName() + "' of " + contentData.length() + " bytes.");
                    jos.closeEntry();
                } catch (IOException e) {
                    c.setExceptionState(true, "Unable to add entry '" + addFilename + "' to JAR file: " + e.getMessage());
                    return new StringBuffer();
                }
            }
            try {
                jos.close();
                if (tmpFile != null) {
                    jarFile.delete();
                    tmpFile.renameTo(jarFile);
                }
            } catch (IOException e) {
                c.setExceptionState(true, "Unable to rename JAR file: " + e.getMessage());
                return new StringBuffer();
            }
        } else if (action.equalsIgnoreCase("create")) {
            JarEntry je = null;
            java.io.File jarFile = null;
            FileOutputStream fos = null;
            JarOutputStream jos = null;
            BufferedWriter bw = null;
            int buf = -1;
            boolean badZipFile = false, refreshFromNewFile = false;
            String jarFilename, addFilename, tmpJarFilename;
            StringBuffer contentData;
            if (fileAttribute.equals("") || filenameAttribute.equals("")) {
                c.setExceptionState(true, "Jar requires file and filename attributes to be present.");
                return new StringBuffer();
            }
            jarFilename = currentDocroot + fileAttribute;
            addFilename = filenameAttribute;
            contentData = TagInspector.processBody(this, c);
            tmpJarFilename = jarFilename + ".tmp";
            try {
                jarFile = new java.io.File(jarFilename);
                if (jarFile.exists()) {
                    c.setExceptionState(true, "JAR file '" + jarFilename + "' already exists.  Action needs to be 'update'.");
                    return new StringBuffer();
                }
                fos = new FileOutputStream(jarFile);
                jos = new JarOutputStream(fos);
                bw = new BufferedWriter(new OutputStreamWriter(jos));
            } catch (IOException e) {
                c.setExceptionState(true, "Unable to create JAR file '" + tmpJarFilename + "': " + e.getMessage());
                return new StringBuffer();
            }
            Debug.user(logTime, "Created jarfile '" + jarFilename + "'");
            try {
                je = new JarEntry(addFilename);
                jos.putNextEntry(je);
                bw.write(contentData.toString());
                bw.flush();
                Debug.user(logTime, "Added entry '" + je.getName() + "' of " + contentData.length() + " bytes.");
                jos.closeEntry();
            } catch (IOException e) {
                c.setExceptionState(true, "Unable to add entry '" + addFilename + "' to JAR file: " + e.getMessage());
                return new StringBuffer();
            }
            try {
                jos.close();
            } catch (IOException e) {
                c.setExceptionState(true, "Unable to rename JAR file: " + e.getMessage());
                return new StringBuffer();
            }
        } else if (action.equalsIgnoreCase("delete")) {
            JarEntry je = null;
            java.io.File tmpFile = null, jarFile = null;
            FileOutputStream fos = null;
            JarOutputStream jos = null;
            BufferedWriter bw = null;
            FileInputStream fis = null;
            JarInputStream jis = null;
            BufferedReader br = null;
            int buf = -1;
            boolean badZipFile = false, refreshFromNewFile = false;
            String jarFilename, addFilename, tmpJarFilename;
            if (fileAttribute.equals("") || filenameAttribute.equals("")) {
                c.setExceptionState(true, "Jar requires file and filename attributes.");
                return new StringBuffer();
            }
            jarFilename = currentDocroot + fileAttribute;
            addFilename = filenameAttribute;
            tmpJarFilename = jarFilename + ".tmp";
            try {
                tmpFile = new java.io.File(tmpJarFilename);
                fos = new FileOutputStream(tmpFile);
                jos = new JarOutputStream(fos);
                bw = new BufferedWriter(new OutputStreamWriter(jos));
            } catch (IOException e) {
                c.setExceptionState(true, "Unable to create JAR file '" + tmpJarFilename + "': " + e.getMessage());
                return new StringBuffer();
            }
            jarFile = new java.io.File(jarFilename);
            if (jarFile.exists()) {
                try {
                    fis = new FileInputStream(jarFile);
                    jis = new JarInputStream(fis);
                    br = new BufferedReader(new InputStreamReader(jis));
                } catch (IOException e) {
                    c.setExceptionState(true, "Unable to open JAR file '" + jarFilename + "': " + e.getMessage());
                    return new StringBuffer();
                }
            }
            try {
                while ((je = jis.getNextJarEntry()) != null) {
                    refreshFromNewFile = false;
                    if (je.getName().compareToIgnoreCase(addFilename) == 0) {
                        refreshFromNewFile = true;
                    }
                    if (!refreshFromNewFile) {
                        jos.putNextEntry(je);
                        int index = 0;
                        buf = -1;
                        while ((buf = br.read()) != -1) {
                            bw.write(buf);
                            index++;
                        }
                        bw.flush();
                        jos.closeEntry();
                    }
                }
            } catch (IOException e) {
                c.setExceptionState(true, "Unable to manipulate JAR file: " + e.getMessage());
                return new StringBuffer();
            }
            try {
                br.close();
                jis.close();
                fis.close();
            } catch (IOException e) {
                c.setExceptionState(true, "Unable to close original JAR file: " + e.getMessage());
                return new StringBuffer();
            }
            try {
                jos.close();
                if (tmpFile != null) {
                    jarFile.delete();
                    tmpFile.renameTo(jarFile);
                }
            } catch (IOException e) {
                c.setExceptionState(true, "Unable to rename JAR file: " + e.getMessage());
                return new StringBuffer();
            }
        } else if (action.equalsIgnoreCase("read")) {
            if (fileAttribute.equals("") || filenameAttribute.equals("")) {
                c.setExceptionState(true, "Jar requires file and filename attributes.");
                return new StringBuffer();
            }
            String jarFilename = currentDocroot + fileAttribute;
            String jarReadFile = filenameAttribute;
            JarFile jf = null;
            try {
                jf = new JarFile(jarFilename);
            } catch (Exception e) {
                c.setExceptionState(true, "Unable to load JAR '" + jarFilename + "': " + e.getMessage());
                return new StringBuffer();
            }
            ZipEntry zEntry = jf.getEntry(jarReadFile);
            if (zEntry == null) {
                c.setExceptionState(true, "File '" + jarReadFile + "' does not exist in JAR '" + jarFilename + "'");
                return new StringBuffer();
            }
            int fileSize = (new Long(zEntry.getSize())).intValue();
            byte data[] = new byte[fileSize];
            int bytesRead;
            InputStream is = null;
            try {
                int totalBytes = 0;
                int pos = 0;
                is = jf.getInputStream(zEntry);
                while ((bytesRead = is.read(data, pos, fileSize - pos)) != -1) {
                    if (bytesRead == 0) {
                        break;
                    }
                    totalBytes += bytesRead;
                    pos += bytesRead;
                }
            } catch (Exception e) {
            }
            try {
                jf.close();
            } catch (IOException e) {
            }
            Debug.user(logTime, "Request of file '" + jarReadFile + "' from jar '" + jarFilename + "' returns " + zEntry.getSize() + " byte(s) of compressed data.");
            if (c.isProtectedVariable(varname)) {
                c.setExceptionState(true, "Attempted to modify a read-only variable '" + varname + "'");
                return new StringBuffer();
            }
            c.getVariableContainer().setVariable(varname, new String(data));
        } else if (action.equalsIgnoreCase("list")) {
            if (fileAttribute.equals("")) {
                c.setExceptionState(true, "Jar requires a file attribute to be present.");
                return new StringBuffer();
            }
            String jarFilename = currentDocroot + fileAttribute;
            if (path.equals("")) {
                c.setExceptionState(true, "Jar requires a 'path' option to be present when action is 'list'");
                return new StringBuffer();
            }
            JarFile jf = null;
            Vector list = new Vector();
            try {
                jf = new JarFile(jarFilename);
            } catch (Exception e) {
                c.setExceptionState(true, "Unable to load JAR '" + jarFilename + "': " + e.getMessage());
                return new StringBuffer();
            }
            for (Enumeration e = jf.entries(); e.hasMoreElements(); ) {
                ZipEntry ent = (ZipEntry) e.nextElement();
                if (!ent.getName().startsWith("META-INF")) {
                    if (ent.getName().startsWith(path)) {
                        list.add(ent.getName().replaceAll(path, ""));
                    }
                }
            }
            if (mode != null) {
                if (mode.equalsIgnoreCase("text")) {
                    Iterator it = list.iterator();
                    while (it.hasNext()) {
                        buffer.append((String) it.next());
                        buffer.append('\n');
                    }
                    if (varname != null && !varname.equals("")) {
                        if (c.isProtectedVariable(varname)) {
                            c.setExceptionState(true, "Attempted to modify a read-only variable '" + varname + "'");
                            return new StringBuffer();
                        }
                        c.getVariableContainer().setVariable(varname, buffer.toString());
                    } else {
                        return buffer;
                    }
                } else if (mode.equalsIgnoreCase("vector")) {
                    if (varname == null || varname.equals("")) {
                        c.setExceptionState(true, "&lt;Jar&gt; requires a varname option when mode is 'vector'");
                        return new StringBuffer();
                    }
                    c.getVariableContainer().setVector(varname, list);
                } else if (mode.equalsIgnoreCase("array")) {
                    if (varname == null || varname.equals("")) {
                        c.setExceptionState(true, "&lt;Jar&gt; requires a varname option when mode is 'array'");
                        return new StringBuffer();
                    }
                    Iterator it = list.iterator();
                    int counter = 0;
                    while (it.hasNext()) {
                        if (c.isProtectedVariable(varname)) {
                            c.setExceptionState(true, "Attempted to modify a read-only variable '" + varname + "'");
                            return new StringBuffer();
                        }
                        c.getVariableContainer().setVariable(varname + "[" + counter++ + "]", (String) it.next());
                    }
                }
            } else {
                Iterator it = list.iterator();
                while (it.hasNext()) {
                    buffer.append((String) it.next());
                    buffer.append('\n');
                }
                if (varname != null && !varname.equals("")) {
                    if (c.isProtectedVariable(varname)) {
                        c.setExceptionState(true, "Attempted to modify a read-only variable '" + varname + "'");
                        return new StringBuffer();
                    }
                    c.getVariableContainer().setVariable(varname, buffer.toString());
                } else {
                    return buffer;
                }
            }
        } else if (action.equalsIgnoreCase("extract")) {
            if (fileAttribute.equals("") || destAttribute.equals("")) {
                c.setExceptionState(true, "Jar requires a file and dest attribute to be present when " + "action is 'extract'");
                return new StringBuffer();
            }
            String jarReadFile = currentDocroot + fileAttribute;
            String jarExtractDest = currentDocroot + destAttribute;
            ZipFile zf = null;
            try {
                zf = new ZipFile(jarReadFile);
            } catch (IOException e) {
                return new StringBuffer();
            }
            Enumeration en = zf.entries();
            Hashtable htSizes = new Hashtable();
            while (en.hasMoreElements()) {
                ZipEntry ze = (ZipEntry) en.nextElement();
                htSizes.put(ze.getName(), new Integer((int) ze.getSize()));
            }
            try {
                zf.close();
            } catch (IOException e) {
            }
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(jarReadFile);
            } catch (FileNotFoundException e) {
                c.setExceptionState(true, "Specified jar file '" + jarReadFile + "' does not exist.");
            }
            BufferedInputStream bis = new BufferedInputStream(fis);
            ZipInputStream zis = new ZipInputStream(bis);
            ZipEntry ze = null;
            try {
                while ((ze = zis.getNextEntry()) != null) {
                    if (ze.isDirectory()) {
                        java.io.File file = new java.io.File(jarExtractDest + "/" + ze.getName());
                        Debug.user(logTime, "Created directory from jar: '" + jarExtractDest + "/" + ze.getName() + "'");
                        file.mkdirs();
                        continue;
                    }
                    String dirName = ze.getName();
                    if (dirName.indexOf("/") != -1) {
                        dirName = dirName.substring(0, dirName.lastIndexOf("/"));
                        java.io.File file = new java.io.File(jarExtractDest + "/" + dirName);
                        file.mkdirs();
                    }
                    int size = (int) ze.getSize();
                    if (size == -1) {
                        size = ((Integer) htSizes.get(ze.getName())).intValue();
                    }
                    Debug.user(logTime, "Extract dir='" + dirName + "' file='" + jarExtractDest + "/" + ze.getName() + "' size=" + size);
                    java.io.File outFile = null;
                    FileOutputStream fos = null;
                    try {
                        outFile = new java.io.File(jarExtractDest + "/" + ze.getName());
                        fos = new FileOutputStream(outFile);
                    } catch (IOException e) {
                        return new StringBuffer();
                    }
                    byte b[] = new byte[(int) size];
                    int rb = 0;
                    int chunk = 0;
                    while (((int) size - rb) > 0) {
                        chunk = zis.read(b, rb, (int) size - rb);
                        if (chunk == -1) {
                            break;
                        }
                        rb += chunk;
                    }
                    fos.write(b);
                    fos.close();
                }
            } catch (IOException e) {
                return new StringBuffer();
            }
        }
        if (callProc != null && !callProc.equals("")) {
            Call call = new Call();
            call.callProcedure(c, null, null, callProc, null);
        }
        return new StringBuffer();
    }
}
