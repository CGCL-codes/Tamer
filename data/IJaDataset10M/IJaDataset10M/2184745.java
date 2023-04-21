package com.life.audiotageditor.model;

public class AudioFolder extends AudioContainer implements IAudioFolder {

    protected AudioFolder(String path, AudioModelManager manager) {
        super(path, manager);
    }

    public void create() {
    }

    @Override
    public int getType() {
        return AUDIO_FOLDER;
    }

    @Override
    @SuppressWarnings("rawtypes")
    public Object getAdapter(Class adapter) {
        return super.getAdapter(adapter);
    }
}
