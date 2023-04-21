package com.life.audiotageditor.model;

import com.life.audiotageditor.utils.FileTypeUtil;

public class AudioModelManager {

    private AudioModel audioModel;

    private static AudioModelManager manager = new AudioModelManager();

    public static AudioModelManager instance() {
        return manager;
    }

    public IAudioModel getRoot() {
        if (this.audioModel == null) {
            audioModel = new AudioModel(manager);
        }
        return this.audioModel;
    }

    public IAudioModelInfo getResourceInfo(String path) {
        switch(FileTypeUtil.getFileType(path)) {
            case IAudioModel.AUDIO_FOLDER:
                return new AudioFolderInfo(path);
            case IAudioModel.AUDIO_FILE:
                return new AudioFileInfo(path);
        }
        return null;
    }

    public AudioModel newResource(String path, int type) {
        switch(type) {
            case IAudioModel.AUDIO_FOLDER:
                return new AudioFolder(path, this);
            case IAudioModel.AUDIO_FILE:
                return new AudioFile(path, this);
        }
        return null;
    }
}
