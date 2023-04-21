package com.bemol.kernel.library.service;

import java.io.File;
import java.util.List;
import java.util.Map;
import com.bemol.kernel.library.AudioFile;
import com.bemol.kernel.service.Service;
import com.bemol.kernel.service.ServiceException;

/**
 * @author samuelgmartinez
 *
 */
public interface AudioLibraryService extends Service {

    public AudioFile addAudio(File file) throws ServiceException;

    public AudioFile addAudio(AudioFile file) throws ServiceException;

    public AudioFile deleteAudio(String uuid) throws ServiceException;

    public AudioFile deleteAudio(AudioFile file) throws ServiceException;

    public AudioFile getAudio(String uuid) throws ServiceException;

    public List<AudioFile> findAudio(Map<String, Object> searchParams) throws ServiceException;
}
