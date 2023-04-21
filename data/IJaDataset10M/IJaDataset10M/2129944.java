/*
 *  NeonZip - archive tool
 *  Copyright (C) 2001 Peter Ivanov
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 *
 *	This class implements ArchiveSupport interface and support zip archives
 *	manipulation.
 *
 *
 *	@author    Peter Ivanov
 * 	           <a href=mailto:peterivanov@europe.com>peterivanov@europe.com</a>,
 * 	           <br>Copyright (c) 2001 Peter K. Ivanov.
 *
 * 	@version   (December 30 2001)
 *
 */

package net.sourceforge.neonzip;

import java.util.*;
import java.io.*;
import java.awt.Cursor;
import java.text.*;
import javax.swing.*;

import gnu.regexp.*;
import org.apache.log4j.Category;

import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
import java.util.zip.ZipException;
import java.util.zip.ZipEntry;
import java.util.zip.Adler32;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;

//import net.sourceforge.neonzip.zip.ZipFile;
//import net.sourceforge.neonzip.zip.ZipOutputStream;
//import net.sourceforge.neonzip.zip.ZipException;
//import net.sourceforge.neonzip.zip.ZipEntry;


public final class ZipSupport implements ArchiveSupport {
    
    private Category log = Category.getInstance("neonzip.ZipSupport");
    
    /* Zip file items container */
    private ArrayList fEntries;
    
    /* Opened ZipFile representation */
    private ZipFile fZipFile;
    
    /* Opened file representation */
    private File fFile;
    
    /* Table data */
    private Vector fData;
    
    private Main fMain;
    
    private OutputManager fOutput;
    
    /* Total uncompressed size of all file in the archive */
    private long fTotalUncompressedSize = 0;
    
    /* Reference to table data continer */
    private Vector fTableData;
    
    /* Reference to the table */
    private ArchiveTable fTable;
    
    private DateFormat fDateFormat;
    
    /**
     *	ZipSupport constructor.
     */
    ZipSupport(	Main aMain,
    OutputManager aOutput,
    ArchiveTable aTable,
    Vector aTableData) {
        //fNames = new Vector(8);
        //initColumnNames();
        fMain = aMain;
        fOutput = aOutput;
        fTable = aTable;
        fTableData = aTableData;
        fDateFormat = fMain.defineDataTimeFormat();
    }
    
    
    
    /**
     *	Create new and empty ZIP file on the fs.
     *
     *	@param aNewFile new file for creating.
     */
    public void create(File aNewFile) {
        // clear if there is any open file
        close();
        fTableData.clear();
        fTable.updateUI();
        
        fFile = aNewFile;
        fEntries = new ArrayList();
    }
    
    
    
    /**
     *	Open zip file, render contents and deliver in suitable format to
     *	JTable object.
     */
    public Vector open(File aZipFile) {
        fMain.setCursor(Cursor.WAIT_CURSOR);
        fFile = aZipFile;
        fTotalUncompressedSize = 0;
        // if file is emty zero-entry zip
        if (isZeroEntryZip(fFile)) {
            fEntries = new ArrayList();
            fData = new Vector();
            return fData;
        }
        ////////////
        try {
            fZipFile = new ZipFile(aZipFile);
        } catch (ZipException e) {
            log.warn("problem when tries to open file", e);
            JOptionPane.showMessageDialog(fMain, Main.fResources.getString("key.message.file_open_error"));
            e.printStackTrace();
            fMain.setCursor(Cursor.DEFAULT_CURSOR);
            return null;
        } catch (IOException e) {
            // TO DO - add more logic here
            log.warn("IO problem when try to open file", e);
            e.printStackTrace();
            fMain.setCursor(Cursor.DEFAULT_CURSOR);
            return null;
        }
        fEntries = new ArrayList(fZipFile.size());
        fData = new Vector(fZipFile.size());
        Enumeration content = fZipFile.entries();
        while(content.hasMoreElements()) {
            ZipEntry entry = (ZipEntry) content.nextElement();
            if (!entry.isDirectory()) {
                fTotalUncompressedSize += entry.getSize();
                fEntries.add(entry);
                Vector entryInfo = renderZipEntry(entry);
                fData.add(entryInfo);
            }
        }
        fMain.setCursor(Cursor.DEFAULT_CURSOR);
        return fData;
    }
    
    
    
    /**
     *	Close zip file, release system resources and free JTable object
     */
    public void close() {
        if (fEntries != null) fEntries.clear();
        if (fData != null) fData.clear();
        fFile = null;
        try {
            if (fZipFile != null) fZipFile.close();
        } catch (IOException e) {
            log.warn("IO problem when try to close file", e);
            e.printStackTrace();
            fZipFile = null;
        }
        fZipFile = null;
        System.gc();
    }
    
    
    
    /**
     *	Extract items from zip file to target directory.
     *
     *	@param aItems list of user selected zip items
     *	@param aTarget extract to destination directory
     */
    public void extract(int[] aItems, File aDestination,
    boolean aOverwriteExisting,
    boolean aSkipOlder,
    boolean aUseFolder) {
        
        if (fZipFile == null) return;
        boolean problems = false;
        int totalCount = aItems.length;
        
        fOutput.clear();
        fOutput.appendLine(Main.fResources.getString("key.label.extract_to")
        + ": " + aDestination.toString());
        fOutput.appendLine(Main.fResources.getString("key.checkbox.use_folder")
        + ": " + aUseFolder + "     "
        + Main.fResources.getString("key.checkbox.overwrite_existing")
        + ": " + aOverwriteExisting);
        
        fMain.lockGUI();
        
        for (int i = 0; i < totalCount; i++) {
            int selection = aItems[i];
            ZipEntry entry = (ZipEntry) fEntries.get(selection);
            
            // set monitor message
            String message = Main.fResources.getString("key.label.extracting")
            + " " + entry.getName();
            fMain.getMonitor().setMessage(message);
            
            File outputFile = null;
            FileOutputStream output = null;
            /* UseFolder option */
            if (aUseFolder)
                outputFile = new File(aDestination, entry.getName());
            else
                outputFile = new File(aDestination, Utils.removePath(entry.getName()));
            /* End UseFolder option changes */
            
            /* Skip older option */
            Date outputDate = new Date(outputFile.lastModified());
            Date archiveDate = new Date(entry.getTime());
            if ((aSkipOlder) && (outputDate.after(archiveDate))) continue;
            /* End SkipOlder option */
            
            /* OverwriteExisting option */
            if ((outputFile.exists()) && (!aOverwriteExisting)) {
                ZipFileOverwriteDialog dialog = new
                ZipFileOverwriteDialog(fMain, true);
                dialog.setReplaceName(outputFile.toString());
                dialog.setReplaceDate(outputDate.toString());
                dialog.setWithName(entry.getName());
                dialog.setWithDate(archiveDate.toString());
                dialog.show();
                
                int result = dialog.getResult();
                if (result == dialog.CANCEL_OPTION) return;
                if (result == dialog.NO_OPTION) continue;
                if (result == dialog.YES_TO_ALL_OPTION) aOverwriteExisting = true;
            }
            /* End OverwriteExisting option */
            
            try {
                fOutput.appendLine(message);
                // create output file on the drive
                File dir = outputFile.getParentFile();
                dir.mkdirs();
                outputFile.createNewFile();
                output = new FileOutputStream(outputFile);
                
                // read from input to the buffer after that flush buffer to the output
                InputStream inputRaw = fZipFile.getInputStream(entry);
                MonitoredInputStream input = new MonitoredInputStream(fMain.getMonitor(), inputRaw);
                byte[] buffer = new byte[4096];
                int length;
                
                while((length = input.read(buffer)) > 0) {
                    output.write(buffer, 0, length);
                }
                inputRaw.close();
                input.close();
                
                output.close();
                outputFile.setLastModified(entry.getTime()); // store archive data/time to extracted file
            } catch (InterruptedIOException e) {
                log.warn("Extraction of " + entry.getName() + " is interrupted");
                fOutput.appendLine(Main.fResources.getString("key.message.interrupted"));
                // try to delete partial extracted file
                problems = true;
                break; // stop extraction
            } catch (IOException e) {
                log.error("IO error during file extraction", e);
                // add more logic here
                e.printStackTrace();
            }
        }
        fMain.unlockGUI();
        if (problems) {
            askShowOutput();
        }
    }
    
    
    
    /**
     *	Add files to ZIP archive operation.
     *	Add one file or list of files to given ZIP archive file
     *
     *	@param aFileList list of files for adding.
     *
     *	@param aCurrentDir current directory.
     *
     *	@param aFilter Global file symbols for filtered file selection. My be
     *	null and then ignore file filtering.
     *
     *	@param aArchive Zip archive file.
     *
     *	@param aAction Add, Freshen, Move and Updates action. Add action
     *	causes to add all specified files to ZIP archive. The Freshen option
     *	causes to update files already in the ZIP archive mached the
     *	specified files. The Update is the same as the Freshen but add
     *	specified files that are not already in the ZIP archive. The Move is the
     *	same as the Add but after files add to the ZIP archive they are delete
     *	from the ZIP archive.
     *
     *	@param aCompression compression method
     *
     *	@param aSubfolders add all files located in all subfolders
     *
     *	@param aPathinfo store path info
     */
    public void add(File[] aFileList,
    File aCurrentDir,
    File aArchive,
    String aFilter,
    int aAction,
    int aCompression,
    boolean aSubfolders,
    boolean aPathinfo) {
        fOutput.clear();
        
        boolean problems = false;
        fMain.lockGUI();
        fMain.getMonitor().setNote("");
        fMain.getMonitor().setMessage(Main.fResources.getString("key.message.Searching") + "...");
        // get all files for adding
        ArrayList addFiles = new ArrayList(100);
        // corresponding entry names for each file for adding
        ArrayList addEntries = new ArrayList(100);
        
        if (aFilter == null)
            fillFileLists(addFiles, addEntries, aFileList, aCurrentDir, aPathinfo, aSubfolders);
        else {
            fillFileLists(addFiles, addEntries, aFileList, aCurrentDir, aPathinfo, aSubfolders);
            // Filter file list by given filter.
            ArrayList filteredFiles = new ArrayList(addFiles.size());
            ArrayList filteredEntries = new ArrayList(addEntries.size());
            RE filter = Utils.getFileFilter(aFilter);
            for (int i = 0; i < addEntries.size(); i++) {
                String filename = (String) addEntries.get(i);
                if (filter.isMatch(filename)) {
                    filteredFiles.add((File)addFiles.get(i));
                    filteredEntries.add((String)addEntries.get(i));
                }
            }
            addFiles = null;
            addEntries = null;
            addFiles = filteredFiles;
            addEntries = filteredEntries;
        }
        
        
        if (addFiles.size() < 1) {
            fMain.unlockGUI();
            Utils.sayInformation(Main.fResources.getString("key.message.no_selected_file"), fMain);
            return;
        }
        
        File temp = new File(Utils.getTempDir(), Utils.removePath(fFile.getName()) + ".temp"); // dont forget to add temp here
        
        boolean success = false;
        
        switch(aAction) {
/*            case AddDialog.ADD_ACTION: {
                problems = addFiles(	addFiles,
                addEntries,
                aCurrentDir,
                temp,
                aCompression,
                aPathinfo,
                false);
                break;
            }
            case AddDialog.FRESHEN_ACTION: {
                problems = freshenFiles(addFiles,
                addEntries,
                temp,
                aCurrentDir,
                aCompression,
                aPathinfo);
                break;
            }
            case AddDialog.UPDATE_ACTION: {
                problems = updateFiles(	addFiles,
                addEntries,
                aCurrentDir,
                temp,
                aCompression,
                aPathinfo);
                break;
            }
            case AddDialog.MOVE_ACTION: {
                problems = addFiles(addFiles,
                addEntries,
                aCurrentDir,
                temp,
                aCompression,
                aPathinfo,
                true);
                break;
            }*/
        }
        fMain.unlockGUI();
        // if there is some problems during add operation
        if (problems) {
            askShowOutput();
        }
    }
    
    
    
    /**
     *	Delete items (files) in zip file.
     *
     *	@param aItems list of selected zip items (files)
     */
    public void delete(int[] aItems) {
        fMain.setCursor(Cursor.WAIT_CURSOR);
        fOutput.clear();
        fOutput.appendLine(Main.fResources.getString("key.label.action" ) + ": "
        + Main.fResources.getString("key.title.delete"));
        int count = fEntries.size();
        // create list with all deleted items
        ArrayList deleted = new ArrayList(aItems.length);
        for (int i = 0; i < aItems.length; i++) {
            deleted.add(new Integer(aItems[i]));
        }
        
        // create temporary file and store non-deleted items there
        File temp = new File(Utils.getTempDir(), Utils.removePath(fZipFile.getName()) + ".temp"); // don't forget to add temp name here
        ZipOutputStream tempout = null;
        if (count > 0) {
            fMain.lockGUI();
            fMain.getMonitor().setNote(Main.fResources.getString("key.message.Deleting_selected_files"));
            fMain.getMonitor().setMessage("");
            fMain.getMonitor().setMaximum(count);
        }
        try {
            if (!(count == aItems.length)) // do not create stream if entire archive is deleted.
                tempout = new ZipOutputStream(new FileOutputStream(temp));
            byte[] buffer = new byte[4096];
            int bytesRead;
            for (int i = 0; i < count; i++) {
                // check and skip if this entry is selected for deleting
                Integer integer = new Integer(i);
                boolean contains = deleted.contains(integer);
                if (contains) continue;
                ///////////
                ZipEntry entry = (ZipEntry) fEntries.get(i);
                fMain.getMonitor().setProgress(i);
                InputStream entryin = fZipFile.getInputStream(entry);
                tempout.putNextEntry(new ZipEntry(entry.getName()));
                while ((bytesRead = entryin.read(buffer)) != -1) {
                    // if user cancel delete operation
                    if (fMain.getMonitor().isCanceled()) {
                        fOutput.appendLine(Main.fResources.getString("key.message.interrupted"));
                        killTemporary(tempout, temp);
                        fMain.setCursor(Cursor.DEFAULT_CURSOR);
                        return;
                    }
                    tempout.write(buffer, 0, bytesRead);
                }
                entryin.close();
                tempout.closeEntry();
            }
            // if user delete all entries zip file becomes empty but
            // java.util.zip will not work with zero-entry archives. So we
            // create pkzip spec compliant file.
            if (count == aItems.length) {
                writeZeroEntryZip(temp);
            }
            //////
        } catch(FileNotFoundException e) {
            log.warn("File not found during delete", e);
            e.printStackTrace();
            return;
        } catch (IOException e) {
            log.warn("IO problem during delete", e);
            e.printStackTrace();
            return;
        } finally {
            try {
                if (tempout != null) tempout.close(); // if zip file is empty tempout is null
            } catch (IOException e) {
                if (e.getMessage().equals("Stream closed")) return;
                log.error("IO problem when try to close temporary output file", e);
                e.printStackTrace();
                return;
            }
        }
        
        
        // if code still here everything is ok.
        try {
            fZipFile.close();
        } catch(IOException e) {
            log.warn("Problem when close ZipFile object", e);
            e.printStackTrace();
            fZipFile = null;
        }
        boolean delete = fFile.delete();
        boolean rename = temp.renameTo(fFile);
        if (delete && rename) {
            fOutput.appendLine(Main.fResources.getString("key.message.replacing_old_zip_file"));
            fMain.refreshTable(fFile);
        }
        
        fMain.setCursor(Cursor.DEFAULT_CURSOR);
        fMain.unlockGUI();
    }
    
    
    
    /**
     *	Abstract test integrity operation.
     *	Test the integrity of the open archive.
     */
    public boolean test() {
        fOutput.clear();
        boolean result = true;
        fOutput.appendLine(Main.fResources.getString("key.message.Testing") + " ...");
        fMain.lockGUI();
        fMain.getMonitor().setMaximum(fZipFile.size());
        Enumeration enum = fZipFile.entries();
        int count = 0;
        InputStream input = null;
        CheckedInputStream checkInput = null;
        try {
            while(enum.hasMoreElements()) {
                count++;
                ZipEntry entry = (ZipEntry) enum.nextElement();
                fMain.getMonitor().setMessage(Main.fResources.getString("key.message.Testing") + ": " + entry.getName());
                fMain.getMonitor().setProgress(count);
                input = fZipFile.getInputStream(entry);
                checkInput = new CheckedInputStream(input, new CRC32());
                byte[] buffer = new byte[1024];
                while (checkInput.read(buffer) > 0) {
                    // if user cancel delete operation
                    if (fMain.getMonitor().isCanceled()) {
                        fOutput.appendLine(Main.fResources.getString("key.message.interrupted"));
                        return result;
                    }
                }
                checkInput.close();
                input.close();
                long checksum = checkInput.getChecksum().getValue();
                long origsum = entry.getCrc();
                if (origsum != checksum) {
                    result = false;
                    fOutput.appendLine(Main.fResources.getString("key.message.Testing")
                    + ": " + entry.getName() + "      bad CRC");
                }
                fOutput.appendLine(Main.fResources.getString("key.message.Testing")
                + ": " + entry.getName() + "      OK");
            }
        } catch (IOException exc) {
            result = false;
        } finally {
            try {
                checkInput.close();
                input.close();
            } catch (IOException e) {
                log.error(e);
            }
            fMain.unlockGUI();
        }
        return result;
    }
    
    
    
    /**
     *	Abstract getFilenames operation.
     *	Get list with filenames in order synchronized by indexes in the user table.
     *
     *	@return List with filenames.
     */
    public ArrayList getAllFilenames() {
        ArrayList result = new ArrayList(fEntries.size());
        for (int i = 0; i < fEntries.size(); i++) {
            ZipEntry entry = (ZipEntry) fEntries.get(i);
            //String name = Utils.removePath(entry.getName());
            File file = new File(entry.getName());
            result.add(file);
        }
        
        return result;
    }
    
    
    
    /**
     *	Return input stream of the archive item by given filename
     *	and path(optional).
     *
     *	@param aFullname Filename and path(optional)
     *	@return InputStream of the archive item. null if item is not
     *	found.
     */
    public InputStream getInputStream(String aFullname) throws IOException {
        for (int i = 0; i < fEntries.size(); i++) {
            ZipEntry searchEntry = (ZipEntry) fEntries.get(i);
            String searchName = searchEntry.getName();
            if (aFullname.equals(searchName)) {
                return fZipFile.getInputStream(searchEntry);
            }
        }
        
        return null;
    }
    
    
    
    /**
     *	Check for currently open archive file
     *
     *	@return true if is already open file; false otherwise
     */
    public boolean hasOpenFile() {
        return fFile != null;
    }
    
    
    
    /**
     *	Get current file opened and rendered in the user Table...
     */
    public File getCurrentOpen() {
        return fFile;
    }
    
    
    
    /**
     *	Abstract get total uncompressed size of all files in the archive(in bytes).
     *	@return total uncompressed size of all files in the archive
     */
    public long getTotalUncompressedSize() {
        return fTotalUncompressedSize;
    }
    
    
    
    /**
     *	Abstract get uncompressed data of archive item by given index.
     *	@param aItemIndex given index of item
     *	@return uncompressed item size
     */
    public long getItemsUncompressedSize(int[] aItemIndex)
    throws IndexOutOfBoundsException {
        long result = 0;
        if (fEntries.size() == 0) return result;
        for (int i = 0; i < aItemIndex.length; i++) {
            int index = aItemIndex[i];
            ZipEntry entry = (ZipEntry) fEntries.get(index);
            result += entry.getSize();
        }
        
        return result;
    }
    
    
    
    /**
     *	Internal zip items renderer.
     *	@param aEntry zip entry for rendering
     *	@return Vector with rendered entry data
     */
    private Vector renderZipEntry(ZipEntry aEntry) {
        Vector result = new Vector(8);
        // add name
        String name = aEntry.getName();
        result.add(Utils.removePath(name));
        // add type
        String extension = (Utils.getExtension(name)).toUpperCase();
        String type = null;
        if (extension.length() > 0)
            type = extension + " File";
        else
            type = "File";
        result.add(type);
        // add modififed time
        long time = aEntry.getTime();
        Date parsedTime = new Date(time);
        String formatted = fDateFormat.format(parsedTime);
        result.add(formatted);
        // add size
        double size = aEntry.getSize();
        result.add((new Double(size)).longValue() + "");
        // add compression ratio
        double compressed = aEntry.getCompressedSize();
        double percent = 100 - ((compressed / size) * 100);
        //if (percent < 0) percent = 0;
        result.add((new Double(Math.round(percent))).intValue() + "%");
        // add compressed size
        result.add((new Double(compressed)).longValue() + "");
        // add CRC
        long crc = aEntry.getCrc();
        result.add(Long.toHexString(crc));
        // add path
        String path = Utils.removeFilename(name);
        result.add(path);
        
        return result;
    }
    
    
    
    /**
     *	java.util.zip will not work with zero-entry archives. So we
     *	create pkzip spec compliant file.
     */
    private void writeZeroEntryZip(File aEmptyZip) {
        try {
            OutputStream output = new FileOutputStream(aEmptyZip);
            try {
                byte[] empty = new byte[22];
                empty[0] = 80;
                empty[1] = 75;
                empty[2] = 5;
                empty[3] = 6;
                output.write(empty);
            } finally {
                output.close();
            }
        } catch (IOException e) {
            log.error("Problem when write ZeroEntry zip file", e);
            e.printStackTrace();
        }
    }
    
    
    
    /**
     *	Check header of given file. if zip file is zip-entry free
     */
    private boolean isZeroEntryZip(File aZipFile) {
        byte[] header = null;
        try {
            InputStream input = new FileInputStream(aZipFile);
            try {
                header = new byte[22];
                input.read(header);
            } finally {
                input.close();
            }
        } catch (IOException e) {
            log.error("Error when check if zip file is zero-entry", e);
        }
        
        byte[] empty = new byte[22];
        empty[0] = 80;
        empty[1] = 75;
        empty[2] = 5;
        empty[3] = 6;
        
        return Arrays.equals(header, empty);
    }
    
    
    
    /**
     *	Check for file with specific name exists in the archive.
     *	@param aFullpath is full path(if any) and filename.
     *	@param aZipFile zip file for searching
     *	@return true if entry already exists; false otherwise;
     */
    //	private boolean isFileExistsAsEntry(String aFullpath, ZipFile aZipFile) {
    //		if (aZipFile == null) return false;
    //		ZipEntry entry = aZipFile.getEntry(aFullpath);
    //		return entry != null;
    //	}
    
    
    
    /**
     *	Check for specific entry if exists in list of files.
     *	@param aFilesList list with entry strings correspondings with File objects
     *	@param aEntryName we search in the file list for this entry name
     *	@return true if entry exists in the file list; false otherwise.
     */
    private boolean isEntryExistsAsFile(ArrayList aFilesList, String aEntryName) {
        return aFilesList.contains(aEntryName);
    }
    
    
    
    /**
     *	Compare File and ZipEntry modification time.
     *	@param aFile File for compare
     *	@param aEntry ZipEntry for compare
     *	@return true if aFile modification time is after than entry time.
     */
    private boolean isFileNewerThanEntry(File aFile, ZipEntry aEntry) {
        Date filedate = new Date(aFile.lastModified());
        Date entrydate = new Date(aEntry.getTime());
        return filedate.after(entrydate);
    }
    
    
    
    /**
     *	This method is needed because has a bug in ZipEntry.getTime() method.
     *	When we execute: ZipEntry.setTime(File.lastModified());
     *	If given date has odd seconds then ZipEntry.getTime() returns
     *	1 seconds less of File.lastModified();!!!!!????
     *
     *	If aFile last modification time has odd seconds then we increase
     *	seconds value with 1.
     */
    private void setModificationTime(File aFile, ZipEntry aEntry) {
        Date filedate = new Date(aFile.lastModified());
        Calendar filecal = Calendar.getInstance();
        filecal.setTime(filedate);
        int filesec = filecal.get(Calendar.SECOND);
        if (Utils.isEven(filesec)) {
            aEntry.setTime(aFile.lastModified());
            return; // file seconds are even number
        }
        
        filecal.set(Calendar.SECOND, filecal.get(Calendar.SECOND) + 1);
        aEntry.setTime(filecal.getTime().getTime());
    }
    
    
    
    /**
     *	Get from file list real file corresponding with this entry
     */
    private File getCorresponding(ArrayList aFileNames, ArrayList aFileList, ZipEntry aEntry) {
        int index = aFileNames.indexOf(aEntry.getName());
        return (File) aFileList.get(index);
    }
    
    
    
    /**
     *	Get from file list index of corresponding File object with the given
     *	entry.
     */
    private int getCorrespondingIndex(ArrayList aFileNames, ZipEntry aEntry) {
        return aFileNames.indexOf(aEntry.getName());
    }
    
    
    
    /**
     *	Set given compression level to ZipOutputStream.
     */
    private void setCompressionLevel(ZipOutputStream output, int aCompression) {
        // set compression level
/*        if (aCompression == AddDialog.COMPRESSION_9)
            output.setLevel(9);
        else if (aCompression == AddDialog.COMPRESSION_8)
            output.setLevel(8);
        else if (aCompression == AddDialog.COMPRESSION_7)
            output.setLevel(7);
        else if (aCompression == AddDialog.COMPRESSION_6)
            output.setLevel(6);
        else if (aCompression == AddDialog.COMPRESSION_5)
            output.setLevel(5);
        else if (aCompression == AddDialog.COMPRESSION_4)
            output.setLevel(4);
        else if (aCompression == AddDialog.COMPRESSION_3)
            output.setLevel(3);
        else if (aCompression == AddDialog.COMPRESSION_2)
            output.setLevel(2);
        else if (aCompression == AddDialog.COMPRESSION_1)
            output.setLevel(1);
        else if (aCompression == AddDialog.COMPRESSION_0)
            output.setLevel(0);*/
        ///////////////////////
    }
    
    
    
    /**
     *	Fill given arrays with information for adding files
     *	Examle: first item in aFileList is: File(c:\tmp\test.log)
     *	Add to aAddFiles: File(c:\tmp\test.log)
     *	Add to aAddEntries: String("tmp\test.log")
     */
    private void fillFileLists(	ArrayList aAddFiles,
    ArrayList aAddEntries,
    File[] aFileList,
    File aCurrentDir,
    boolean aPathinfo,
    boolean aSubfolders) {
        
        for (int i = 0; i < aFileList.length; i++) {
            File current = aFileList[i];
            if (!current.isDirectory()) {
                aAddFiles.add(current);
                if (!aPathinfo)
                    aAddEntries.add(Utils.removePath(aCurrentDir, current));
                else
                    aAddEntries.add(Utils.getZipEntryFullPath(current.toString()));
            }
            
            if (!aSubfolders) continue;
            
            java.util.List subitems = Utils.scanDirectory(current, aSubfolders);
            // scan for subitems and add their entry names
            Iterator iterator = subitems.iterator();
            while(iterator.hasNext()) {
                File subfile = (File) iterator.next();
                if (!aPathinfo) {
                    aAddEntries.add(Utils.removePath(aCurrentDir, subfile));
                } else
                    aAddEntries.add(Utils.getZipEntryFullPath(subfile.toString()));
            }
            aAddFiles.addAll(subitems);
        }
    }
    
    
    
    /**
     *	Separate add files to zip archive functionality(ADD_ACTION)
     *	@param aMove if is true then delete from disk all added files
     */
    private boolean addFiles(	ArrayList allfiles,
    ArrayList allentries,
    File aCurrentDir,
    File temp,
    int aCompression,
    boolean aPathinfo,
    boolean aMove) {
        boolean problems = false;
        ZipOutputStream output = null;
        byte[] buffer = new byte[4096];
        int bytesRead = 0;
        // for move files operation
        ArrayList deleteContainer = null;
        if (aMove) deleteContainer = new ArrayList(allfiles.size());
        ////
        try {
            if (fZipFile == null) {
                output = new ZipOutputStream(new FileOutputStream(fFile));
                setCompressionLevel(output, aCompression);
            } else {
                output = new ZipOutputStream(new FileOutputStream(temp));
                setCompressionLevel(output, aCompression);
                // add current archive to the temporary archive if is not empty
                if (!isZeroEntryZip(fFile)) {
                    Enumeration enum = fZipFile.entries();
                    while(enum.hasMoreElements()) {
                        ZipEntry entry = (ZipEntry) enum.nextElement();
                        // if entry exists as file skip this entry
                        if (isEntryExistsAsFile(allentries, entry.getName())) {
                            continue;
                        }
                        ZipEntry newentry = new ZipEntry(entry.getName());
                        InputStream input = fZipFile.getInputStream(entry);
                        MonitoredInputStream entryin = new MonitoredInputStream(fMain.getMonitor(), input);
                        fMain.getMonitor().setMessage(Main.fResources.getString("key.message.Storing") + " " + entry.getName());
                        fOutput.appendLine(Main.fResources.getString("key.message.Storing") + " " + entry.getName());
                        newentry.setTime(entry.getTime());
                        output.putNextEntry(newentry);
                        // monitor for user interrupt
                        try {
                            while ((bytesRead = entryin.read(buffer)) != -1) {
                                output.write(buffer, 0, bytesRead);
                            }
                        } catch (InterruptedIOException e) {
                            log.warn("Adding file is interrupted", e);
                            fOutput.appendLine(Main.fResources.getString("key.message.interrupted"));
                            killTemporary(output, temp);
                            return false;
                        }
                        entryin.close();
                        input.close();
                        output.closeEntry();
                    }
                }
            }
            
            for (int i = 0; i < allfiles.size(); i++) {
                File file = (File) allfiles.get(i);
                String filename = null;
                if (!aPathinfo) {
                    filename = Utils.removePath(aCurrentDir, file);
                } else
                    filename = Utils.getZipEntryFullPath(file.toString());
                
                ZipEntry entry = new ZipEntry(filename);
                InputStream input = null;
                try {
                    input = new FileInputStream(file);
                } catch (FileNotFoundException e) {
                    log.warn("Selected file is locked or not found when add", e);
                    if (!file.exists()) {
                        fOutput.appendLine(file.toString() + " - " +
                        Main.fResources.getString("key.message.file_not_exists"));
                    }
                    fOutput.appendLine(file.toString() + " - " +
                    Main.fResources.getString("key.message.file_unaccessible"));
                    problems = true;
                    continue;
                }
                //entry.setTime(file.lastModified()); // add original modification time from file
                setModificationTime(file, entry);
                output.putNextEntry(entry);
                MonitoredInputStream instream = new MonitoredInputStream(fMain.getMonitor(), input);
                fMain.getMonitor().setMessage(Main.fResources.getString("key.message.Adding") + " " + file.toString());
                fOutput.appendLine(Main.fResources.getString("key.message.Adding") + " " + file.toString());
                // listen for user interrupt
                try {
                    while ((bytesRead = instream.read(buffer)) != -1) {
                        output.write(buffer, 0, bytesRead);
                    }
                } catch (InterruptedIOException e) {
                    log.warn("Adding file is interrupted", e);
                    fOutput.appendLine(Main.fResources.getString("key.message.interrupted"));
                    killTemporary(output, temp);
                    return false;
                }
                // if move operation then add all added files in deleteContainer
                if (aMove) deleteContainer.add(file);
                instream.close();
                input.close();
                output.closeEntry();
            }
        } catch (IOException e) {
            log.fatal("Fatal error when add", e);
            e.printStackTrace();
        } finally {
            try {
                output.close();
            } catch(IOException e) {
                log.error("Error when try to close file during file adding", e);
                e.printStackTrace();
            }
        }
        
        
        // if operation is in temporary
        if (fZipFile != null) {
            // if code still here everything is ok.
            try {
                fZipFile.close();
            } catch(IOException e) {
                log.fatal("Error when try to close zip file during file adding", e);
                e.printStackTrace();
                fZipFile = null;
            }
            boolean delete = fFile.delete();
            boolean rename = temp.renameTo(fFile);
        }
        
        // if move operation then delete all added files at the end
        if (aMove) {
            for (int i = 0; i < deleteContainer.size(); i++) {
                File delFile = (File) deleteContainer.get(i);
                boolean delete = delFile.delete();
                if (!delete)
                    Utils.sayWarning(Main.fResources.getString(
                    "key.message.unable_erase_file" + ": " +
                    delFile.toString()), fMain);
                // del directory if exists and if is empty
                File dir = new File(Utils.removeFilename(delFile.toString()));
                if ((dir.exists()) && (!dir.equals(aCurrentDir))) {
                    String[] contents = dir.list();
                    if (contents.length == 0) delete = dir.delete();
                    if (!delete)
                        Utils.sayWarning(Main.fResources.getString(
                        "key.message.unable_erase_dir" + ": " +
                        dir.toString()), fMain);
                }
            }
        }
        // end of move operation
        
        return problems;
    } // addFiles
    
    
    
    /**
     *	Separate freshen files to zip archive functionality(FRESHEN_ACTION)
     */
    private boolean freshenFiles(	ArrayList allfiles,
    ArrayList allentries,
    File temp,
    File aCurrentDir,
    int aCompression,
    boolean aPathinfo) {
        fOutput.appendLine(Main.fResources.getString("key.message.Refreshing"));
        ZipOutputStream output = null;
        byte[] buffer = new byte[4096];
        int bytesRead = 0;
        boolean freshen = false; // if there is at least one unfreshed file freshen becomes true
        if (fZipFile == null) {
            fOutput.appendLine(Main.fResources.getString("key.message.cannot_refresh_empty"));
            return true; // problem here
        }
        if (isZeroEntryZip(fFile)) {
            fOutput.appendLine(Main.fResources.getString("key.message.cannot_refresh_empty"));
            return true; // problem here
        }
        if (fZipFile.size() < 1) {
            fOutput.appendLine(Main.fResources.getString("key.message.cannot_refresh_empty"));
            return true; // problem here
        }
        String message = Main.fResources.getString("key.message.Searching") + "...";
        fMain.getMonitor().setMessage(message);
        fOutput.appendLine(message);
        try {
            Enumeration enum = fZipFile.entries();
            // search for freshen files need
            while(enum.hasMoreElements()) {
                // monitor for user interruption
                if (fMain.getMonitor().isCanceled()) {
                    fOutput.appendLine(Main.fResources.getString("key.message.interrupted"));
                    return true; // problem here
                }
                ZipEntry entry = (ZipEntry) enum.nextElement();
                if (isEntryExistsAsFile(allentries, entry.getName())) {
                    File file = getCorresponding(allentries, allfiles, entry);
                    if (isFileNewerThanEntry(file, entry)) {
                        freshen = true;
                        log.debug("Files for refresh found");
                        break;
                    }
                }
            }
            
            if (!freshen) { // if no files for refresh then exit from method
                fOutput.appendLine(Main.fResources.getString("key.message.no_files_refresh_found"));
                return true; // problem here
            }
            
            log.debug("Update begins");
            
            // containers for files and entries for adding
            ArrayList files = new ArrayList(allfiles.size());
            ArrayList entries = new ArrayList(fZipFile.size());
            
            // sort between files and entries
            Enumeration zipentries = fZipFile.entries();
            while(zipentries.hasMoreElements()) {
                // monitor for user interruption
                if (fMain.getMonitor().isCanceled()) {
                    fOutput.appendLine(Main.fResources.getString("key.message.interrupted"));
                    return true; // problem here
                }
                ZipEntry entry = (ZipEntry) zipentries.nextElement();
                if (isEntryExistsAsFile(allentries, entry.getName())) {
                    File file = getCorresponding(allentries, allfiles, entry);
                    if (isFileNewerThanEntry(file, entry)) {
                        files.add(file);
                        // debug
                        if (log.isDebugEnabled())
                            log.debug("entry exists as newer file: " + entry.getName());
                        // end debug
                    } else {
                        entries.add(entry);
                    }
                } else {
                    entries.add(entry);
                }
            }
            
            // zip entries to temp zip
            output = new ZipOutputStream(new FileOutputStream(temp));
            setCompressionLevel(output, aCompression);
            Iterator iterator = entries.iterator();
            while(iterator.hasNext()) {
                ZipEntry entry = (ZipEntry) iterator.next();
                ZipEntry newentry = new ZipEntry(entry.getName());
                InputStream entryin = fZipFile.getInputStream(entry);
                newentry.setTime(entry.getTime());
                output.putNextEntry(newentry);
                message = Main.fResources.getString("key.message.Storing") + " " + entry.getName();
                fMain.getMonitor().setMessage(message);
                fOutput.appendLine(message);
                MonitoredInputStream monInput = new MonitoredInputStream(fMain.getMonitor(), entryin);
                // monitor for user unterruption
                try {
                    while ((bytesRead = monInput.read(buffer)) != -1) {
                        output.write(buffer, 0, bytesRead);
                    }
                } catch (InterruptedIOException e) {
                    fOutput.appendLine(Main.fResources.getString("key.message.interrupted"));
                    killTemporary(output, temp);
                    log.warn("Storage of " + entry.getName() + " is interrupted", e);
                    return true; // problem here
                }
                monInput.close();
                entryin.close();
                output.closeEntry();
            }
            
            // zip newer files to temp zip
            iterator = files.iterator();
            while(iterator.hasNext()) {
                File file = (File) iterator.next();
                InputStream input = new FileInputStream(file);
                String filename = null;
                if (!aPathinfo)
                    filename = Utils.removePath(aCurrentDir, file);
                else
                    filename = Utils.getZipEntryFullPath(file.toString());
                ZipEntry newentry = new ZipEntry(filename);
                setModificationTime(file, newentry);
                output.putNextEntry(newentry);
                message = Main.fResources.getString("key.message.Refreshing") + " " + file.toString();
                fMain.getMonitor().setMessage(message);
                fOutput.appendLine(message);
                MonitoredInputStream monInput = new MonitoredInputStream(fMain.getMonitor(), input);
                // monitoring for user interruption
                try {
                    while ((bytesRead = monInput.read(buffer)) != -1) {
                        output.write(buffer, 0, bytesRead);
                    }
                } catch (InterruptedIOException e) {
                    fOutput.appendLine(Main.fResources.getString("key.message.interrupted"));
                    killTemporary(output, temp);
                    log.warn("Rrfreshing of " + file.toString() + " is interrupted", e);
                    return true; // problem here
                }
                monInput.close();
                input.close();
                output.closeEntry();
            }
        } catch (IOException e) {
            log.fatal("Fatal error during file refreshing", e);
            e.printStackTrace();
        } finally {
            try {
                if (output != null) output.close();
            } catch(IOException e) {
                if (e.getMessage().equals("Stream closed")) return true;
                log.error("Error when try to close output file during file refreshing", e);
                e.printStackTrace();
            }
        }
        
        
        // if code still here everything is ok.
        try {
            fZipFile.close();
        } catch(IOException e) {
            log.fatal("Error when try to close zip file after refreshing", e);
            e.printStackTrace();
            fZipFile = null;
        }
        boolean delete = fFile.delete();
        boolean rename = temp.renameTo(fFile);
        
        if (delete && rename)
            return false;
        else
            return true;
    }
    
    
    
    /**
     *	Separate freshen files to zip archive functionality(UPDATE_ACTION)
     */
    private boolean updateFiles(	ArrayList allfiles,
    ArrayList allentries,
    File aCurrentDir,
    File temp,
    int aCompression,
    boolean aPathinfo) {
        fOutput.appendLine(Main.fResources.getString("key.message.Updating"));
        ZipOutputStream output = null;
        byte[] buffer = new byte[4096];
        int bytesRead = 0;
        boolean update = false; // if there is at least one file for update then update becomes true
        try {
            // if zip file is new or is empty then just add selected files
            if ((fZipFile == null) || (isZeroEntryZip(fFile))) {
                return addFiles(allfiles, allentries, aCurrentDir, temp, aCompression, aPathinfo, false);
            }
            ///////
            Enumeration enum = fZipFile.entries();
            // search files for update
            String message = Main.fResources.getString("key.message.Searching") + "...";
            fMain.getMonitor().setMessage(message);
            fOutput.appendLine(message);
            while(enum.hasMoreElements()) {
                // monitor for user interruption
                if (fMain.getMonitor().isCanceled()) {
                    fOutput.appendLine(Main.fResources.getString("key.message.interrupted"));
                    return true; // problem here
                }
                ZipEntry entry = (ZipEntry) enum.nextElement();
                if (isEntryExistsAsFile(allentries, entry.getName())) {
                    int index = getCorrespondingIndex(allentries, entry);
                    // remove file and corresponding entry name too
                    allfiles.remove(index);
                    allentries.remove(index);
                }
            }
            if (allfiles.size() > 0) update = true; // files for update exists
            
            if (!update) { // if no files for update then exit from method
                fOutput.appendLine(Main.fResources.getString("key.message.no_files_update_found"));
                return true;
            }
            
            log.debug("Update begins");
            
            // add existing zip entries to temp zip
            output = new ZipOutputStream(new FileOutputStream(temp));
            setCompressionLevel(output, aCompression);
            
            Enumeration enumeration = fZipFile.entries();
            while(enumeration.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) enumeration.nextElement();
                ZipEntry newentry = new ZipEntry(entry.getName());
                InputStream entryin = fZipFile.getInputStream(entry);
                newentry.setTime(entry.getTime());
                output.putNextEntry(newentry);
                MonitoredInputStream input = new MonitoredInputStream(fMain.getMonitor(), entryin);
                message = Main.fResources.getString("key.message.Storing") + " " + entry.getName();
                fMain.getMonitor().setMessage(message);
                fOutput.appendLine(message);
                // monitoring for user interruption
                try {
                    while ((bytesRead = input.read(buffer)) != -1) {
                        output.write(buffer, 0, bytesRead);
                    }
                } catch (InterruptedIOException e) {
                    fOutput.appendLine(Main.fResources.getString("key.message.interrupted"));
                    killTemporary(output, temp);
                    log.warn("Storage of " + entry.getName() + " is interrupted", e);
                    return true; // problem here
                }
                input.close();
                entryin.close();
                output.closeEntry();
            }
            
            // zip outside files for update to temp zip
            Iterator iterator = allfiles.iterator();
            while(iterator.hasNext()) {
                File file = (File) iterator.next();
                message = Main.fResources.getString("key.message.Updating") + " " + file.toString();
                fMain.getMonitor().setMessage(message);
                fOutput.appendLine(message);
                InputStream input = null;
                try {
                    input = new FileInputStream(file);
                } catch (FileNotFoundException e) {
                    log.warn("File missing or maybe in use." + e);
                    fOutput.appendLine(Main.fResources.getString("key.message.file_unaccessible"));
                    continue;
                }
                String filename = null;
                if (!aPathinfo)
                    filename = Utils.removePath(aCurrentDir, file);
                else
                    filename = Utils.getZipEntryFullPath(file.toString());
                ZipEntry newentry = new ZipEntry(filename);
                setModificationTime(file, newentry);
                output.putNextEntry(newentry);
                MonitoredInputStream inStream = new MonitoredInputStream(fMain.getMonitor(), input);
                // monitoring for user interruption
                try {
                    while ((bytesRead = inStream.read(buffer)) != -1) {
                        output.write(buffer, 0, bytesRead);
                    }
                } catch (InterruptedIOException e) {
                    fOutput.appendLine(Main.fResources.getString("key.message.interrupted"));
                    killTemporary(output, temp);
                    log.warn("Updating of " + file.toString() + " is interrupted", e);
                    return true; // problem here
                }
                inStream.close();
                input.close();
                output.closeEntry();
            }
        } catch (IOException e) {
            log.fatal("IO fatal error during update files", e);
            e.printStackTrace();
        } finally {
            try {
                if (output != null) output.close();
            } catch(IOException e) {
                if (e.getMessage().equals("Stream closed")) return true;
                log.error("Error when try to close output file during update files", e);
                e.printStackTrace();
            }
        }
        
        
        // if code still here everything is ok.
        try {
            fZipFile.close();
        } catch(IOException e) {
            log.fatal("Error when try to close zip file after update files");
            e.printStackTrace();
            fZipFile = null;
        }
        boolean delete = fFile.delete();
        boolean rename = temp.renameTo(fFile);
        
        if (delete && rename)
            return false;
        else
            return true;
    }
    
    
    
    private void askShowOutput() {
        int result = JOptionPane.showConfirmDialog(fMain,
        Main.fResources.getString("key.message.open_log_files"),
        Main.TITLE,
        JOptionPane.YES_NO_OPTION);
        
        if (result == JOptionPane.YES_OPTION) {
            LogDialog dialog = new LogDialog(fMain, true);
            dialog.setText(fOutput.getOutput());
            dialog.show();
        }
    }
    
    
    
    /**
     *	This method try to close the given temporary file output stream and
     *	delete the given file object corresponding to the stream.
     */
    private boolean killTemporary(OutputStream aTempStream, File aTempFile) {
        boolean result = false;
        try {
            if (aTempStream != null)
                aTempStream.close();
        } catch (IOException e) {
            log.error("Error when try to close file during file adding", e);
            e.printStackTrace();
        }
        result = aTempFile.delete();
        if (!result) {
            fOutput.appendLine(Main.fResources.getString("key.message.error_deleting_temporary"));
            log.error("Error deleting temporary file");
            return false;
        } else
            return true;
    }
    
    
    
}
