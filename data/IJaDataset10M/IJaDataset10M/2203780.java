package net.sf.zcatalog.fs;

import java.util.ArrayList;
import java.util.TreeMap;
import net.sf.zcatalog.ProgressObserver;
import net.sf.zcatalog.db.ZCatDb;
import net.sf.zcatalog.db.ZCatObject;
import net.sf.zcatalog.xml.jaxb.*;

/**
 * <p>A base implementation for a (compressed) archive traverser.</p>
 * <p>Implements a mechanism that, given an archive entry, finds
 * its correct place into a hierarchy of ArcEntry objects.
 * The correct place is computed based only on the full path
 * name of the file within the archive, to avoid unpleasant
 * effects in strange or broken archives.<br>
 * Consider a TAR or ZIP archive bearing the following entries:<br>
 * <br>
 * <code>
 * 1) file1<br>
 * 2) folder1/<br>
 * 3) folder1/file1infolder1<br>
 * 4) folder1/file2infolder1<br>
 * 5) folder2/fileinfolder2<br>
 * 6) folder1/file3infolder1<br>
 * 7) folder1/file1infolder1<br>
 * </code></p>
 * <p>There's a 99% chance that folders are stored in the archive
 * using a depth-first algorithm, the folder itself being stored
 * <b>before</b> any of its children (case 1 to 4). Still, the first
 * four cases do not cover all the possibilities.<br>
 * Note how no entry for <code>folder2</code> is added to the
 * archive issuing a command like the following which
 * corresponds to case 5: <br>
 * <br>
 * <code>tar -uf file.tar folder2/fileinfolder2</code><br>
 * <br>
 * <p>Besides, we can also append another file that
 * was added to <code>folder1</code> after the archive was
 * created, which is case 6.<br>
 * Finally, in case 7, the file being added is <i>a new
 * version of the same file added in case 3</i>, which means
 * that an entry with the same name already exists!</p>
 * <p>Any archive format that supports adding files incrementally
 * presents these issues, so we cannot make any assumption on the
 * order how files are stored.</p>
 *
 * @author Alessandro Zigliani
 * @version ZCatalog 0.9
 * @since ZCatalog 0.9
 */
abstract class ArchiveTraverser extends Traverser {

    /**
     * Maps the full name of a folder inside an archive
     * to the actual ArcEntry instance.
     */
    protected TreeMap<String, ArcEntry> folderName2Entry = new TreeMap<String, ArcEntry>();

    /**
     * Inserts the entry into the hierarchy.
     * @param fullName of the entry, taking the archive itself as root
     * @param ent the entry
     * @param pathSepPos the position of the character that separate the
     * path from the actual name of the entry
     */
    protected final void insert(String fullName, ArcEntry ent) {
        ArcEntry pEnt, mapE;
        if (ent.getFolder() != null) {
            mapE = folderName2Entry.get(fullName);
            if (mapE == null) {
                folderName2Entry.put(fullName, ent);
            }
        }
        pEnt = getOrCreate(calcParentName(fullName));
        pEnt.addChild(ent);
    }

    private final ArcEntry getOrCreate(String fullName) {
        ArcEntry ent, pEnt;
        String pFullName = calcParentName(fullName);
        String name = fullName.substring(pFullName.length() + 1);
        ent = folderName2Entry.get(fullName);
        if (ent == null) {
            ArcMeta arcm = new ArcMeta();
            FolderMeta fldm = new FolderMeta();
            ent = new ArcEntry(name, arcm, fldm);
            folderName2Entry.put(fullName, ent);
            pEnt = getOrCreate(pFullName);
            pEnt.addChild(ent);
        }
        return ent;
    }

    abstract String calcParentName(String childName);
}
