package com.jme3.audio;

import com.jme3.asset.AssetKey;
import com.jme3.asset.AssetProcessor;
import com.jme3.asset.cache.AssetCache;
import com.jme3.asset.cache.WeakRefAssetCache;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import java.io.IOException;

/**
 * <code>AudioKey</code> is extending AssetKey by holding stream flag.
 *
 * @author Kirill Vainer
 */
public class AudioKey extends AssetKey<AudioData> {

    private boolean stream;

    private boolean streamCache;

    /**
     * Create a new AudioKey.
     * 
     * @param name Name of the asset
     * @param stream If true, the audio will be streamed from harddrive,
     * otherwise it will be buffered entirely and then played.
     * @param streamCache If stream is true, then this specifies if
     * the stream cache is used. When enabled, the audio stream will
     * be read entirely but not decoded, allowing features such as 
     * seeking, determining duration and looping.
     */
    public AudioKey(String name, boolean stream, boolean streamCache) {
        this(name, stream);
        this.streamCache = streamCache;
    }

    /**
     * Create a new AudioKey
     *
     * @param name Name of the asset
     * @param stream If true, the audio will be streamed from harddrive,
     * otherwise it will be buffered entirely and then played.
     */
    public AudioKey(String name, boolean stream) {
        super(name);
        this.stream = stream;
    }

    public AudioKey(String name) {
        super(name);
        this.stream = false;
    }

    public AudioKey() {
    }

    @Override
    public String toString() {
        return name + (stream ? (streamCache ? " (Stream/Cache)" : " (Stream)") : " (Buffer)");
    }

    /**
     * @return True if the loaded audio should be a {@link AudioStream} or
     * false if it should be a {@link AudioBuffer}.
     */
    public boolean isStream() {
        return stream;
    }

    /**
     * Specifies if the stream cache is used. 
     * 
     * When enabled, the audio stream will
     * be read entirely but not decoded, allowing features such as 
     * seeking, looping and determining duration.
     */
    public boolean useStreamCache() {
        return streamCache;
    }

    @Override
    public Class<? extends AssetCache> getCacheType() {
        if ((stream && streamCache) || !stream) {
            return WeakRefAssetCache.class;
        } else {
            return null;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AudioKey other = (AudioKey) obj;
        if (!super.equals(other)) {
            return false;
        }
        if (this.stream != other.stream) {
            return false;
        }
        if (this.streamCache != other.streamCache) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + (super.hashCode());
        hash = 67 * hash + (this.stream ? 1 : 0);
        hash = 67 * hash + (this.streamCache ? 1 : 0);
        return hash;
    }

    @Override
    public Class<? extends AssetProcessor> getProcessorType() {
        return null;
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(stream, "do_stream", false);
        oc.write(streamCache, "use_stream_cache", false);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule ic = im.getCapsule(this);
        stream = ic.readBoolean("do_stream", false);
        streamCache = ic.readBoolean("use_stream_cache", false);
    }
}
