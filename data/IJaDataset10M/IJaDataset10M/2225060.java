package com.frostwire.bittorrent;

import java.io.File;
import com.limegroup.bittorrent.TorrentContext;

public interface AzureusTorrentFactory {

    public AzureusTorrent create(TorrentContext context, File torrentFile);
}
