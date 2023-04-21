package org.alliance.core.comm.rpc;

import com.stendahls.util.TextUtils;
import org.alliance.core.comm.T;
import org.alliance.core.file.filedatabase.FileDescriptor;
import org.alliance.core.file.hash.Hash;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: maciek
 * Date: 2005-dec-21
 * Time: 16:34:22
 * To change this template use File | Settings | File Templates.
 */
public class HashesForPath extends CompressedRPC {

    private String path;

    private Hash[] hashes;

    private String[] paths;

    public HashesForPath() {
    }

    public HashesForPath(String path, Collection<FileDescriptor> c) {
        this.path = path;
        hashes = new Hash[c.size()];
        paths = new String[c.size()];
        int i = 0;
        for (FileDescriptor fd : c) {
            hashes[i] = fd.getRootHash();
            paths[i] = fd.getSubpath();
            i++;
        }
    }

    public void serializeCompressed(DataOutputStream out) throws IOException {
        out.writeUTF(path);
        out.writeInt(hashes.length);
        for (int i = 0; i < hashes.length; i++) {
            out.write(hashes[i].array());
            out.writeUTF(paths[i]);
        }
    }

    public void executeCompressed(DataInputStream in) throws IOException {
        String path = in.readUTF();
        int len = in.readInt();
        hashes = new Hash[len];
        paths = new String[len];
        for (int i = 0; i < hashes.length; i++) {
            hashes[i] = new Hash();
            in.readFully(hashes[i].array());
            paths[i] = in.readUTF();
            if (T.t) T.trace("Read path: " + paths[i]);
        }
        if (T.t) T.info("Loaded " + hashes.length + " hashes for path " + path);
        ArrayList<Integer> guid = new ArrayList<Integer>();
        guid.add(con.getRemoteUserGUID());
        String common = "";
        int cut = 0;
        if (hashes.length > 1) {
            common = paths[0];
            for (int i = 1; i < hashes.length; i++) {
                common = getCommonPath(TextUtils.makeSurePathIsMultiplatform(paths[i]), TextUtils.makeSurePathIsMultiplatform(common));
                if ("".equals(common)) {
                    break;
                }
            }
            for (int i = common.lastIndexOf("/") - 1; i >= 0; i--) {
                if (common.charAt(i) == '/') {
                    cut = i + 1;
                    break;
                }
            }
            if (cut < 0) {
                cut = common.length();
            } else {
                cut = Math.min(cut, common.length());
            }
        }
        for (int i = 0; i < hashes.length; i++) {
            if (paths.length > 1) {
                paths[i] = paths[i].substring(cut);
            } else {
                paths[i] = paths[i].substring(common.length());
            }
            if (core.getFileManager().containsComplete(hashes[i])) {
                core.getUICallback().statusMessage("You already have the file " + paths[i] + "!");
            } else if (core.getNetworkManager().getDownloadManager().getDownload(hashes[i]) != null) {
                core.getUICallback().statusMessage("You are already downloading " + paths[i] + "!");
            } else {
                core.getNetworkManager().getDownloadManager().queDownload(hashes[i], paths[i], guid);
            }
        }
    }

    public String getCommonPath(String path1, String path2) {
        String[] path1_split = path1.split("/");
        String[] path2_split = path2.split("/");
        String common = "";
        for (int i = 0; i < path1_split.length && i < path2_split.length; i++) {
            if (path1_split[i].equals(path2_split[i])) {
                common = common + (path1_split[i] + "/");
            } else {
                break;
            }
        }
        return common;
    }
}
