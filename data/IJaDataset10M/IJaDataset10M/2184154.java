package com.mp3explorer.service;

import com.mp3explorer.business.files.AudioFileFormatHandler;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * AudioFormatService
 */
public class AudioFormatService implements IAudioFormatService {

    private Map<String, AudioFileFormatHandler> _mapHandlers = new HashMap<String, AudioFileFormatHandler>();

    /**
     * Set the map containing file format handlers (ie : MP3, Ogg, ...)
     * @param map The Handlers map
     */
    public void setHandlersMap(Map<String, AudioFileFormatHandler> map) {
        _mapHandlers = map;
    }

    /**
     * Gets the handler for a given file
     * @param file The file
     * @return The Handler
     */
    public AudioFileFormatHandler getFormatHandler(File file) {
        AudioFileFormatHandler handler = null;
        String extension = getExtension(file);
        if (_mapHandlers.containsKey(extension)) {
            handler = _mapHandlers.get(extension);
        }
        return handler;
    }

    private String getExtension(File file) {
        String filename = file.getName();
        return filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
    }
}
