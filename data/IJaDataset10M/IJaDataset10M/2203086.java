package com.olimination.dmms.service;

import java.util.List;
import com.olimination.dmms.domain.MediaFile;

/**
 * The <code>IMediaFileService</code> interface.
 * 
 * @author olimination.com
 * @version 1.0(07.03.2008)
 * @since 07.03.2008
 */
public interface IMediaFileService {

    public Integer addMediaFile(MediaFile mediaFile);

    public MediaFile getMediaFileById(Integer id);

    public List<MediaFile> getMediaFileByName(String name);

    public List<MediaFile> getAllMediaFiles();

    public List<MediaFile> getMediaFiles(int start, int limit);
}
