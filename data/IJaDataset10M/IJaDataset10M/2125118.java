package jpcsp.graphics.textures;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;
import jpcsp.graphics.VideoEngine;
import jpcsp.graphics.RE.IRenderingEngine;
import jpcsp.util.CacheStatistics;

public class TextureCache {

    public static final int cacheMaxSize = 1000;

    public static final float cacheLoadFactor = 0.75f;

    private static Logger log = VideoEngine.log;

    private static TextureCache instance = null;

    private LinkedHashMap<Integer, Texture> cache;

    public CacheStatistics statistics = new CacheStatistics("Texture", cacheMaxSize);

    private Set<Integer> textureAlreadyHashed;

    private LinkedList<Texture> vramTextures = new LinkedList<Texture>();

    public static TextureCache getInstance() {
        if (instance == null) {
            instance = new TextureCache();
        }
        return instance;
    }

    private TextureCache() {
        cache = new LinkedHashMap<Integer, Texture>((int) (cacheMaxSize / cacheLoadFactor) + 1, cacheLoadFactor, true);
        textureAlreadyHashed = new HashSet<Integer>();
    }

    private Integer getKey(int addr, int clutAddr) {
        return new Integer(addr + clutAddr);
    }

    public boolean hasTexture(int addr, int clutAddr) {
        return cache.containsKey(getKey(addr, clutAddr));
    }

    private Texture getTexture(int addr, int clutAddr) {
        return cache.get(getKey(addr, clutAddr));
    }

    public void addTexture(IRenderingEngine re, Texture texture) {
        Integer key = getKey(texture.getAddr(), texture.getClutAddr());
        Texture previousTexture = cache.get(key);
        if (previousTexture != null) {
            previousTexture.deleteTexture(re);
            vramTextures.remove(previousTexture);
        } else {
            if (cache.size() >= cacheMaxSize) {
                Iterator<Map.Entry<Integer, Texture>> it = cache.entrySet().iterator();
                if (it.hasNext()) {
                    Map.Entry<Integer, Texture> entry = it.next();
                    Texture lruTexture = entry.getValue();
                    lruTexture.deleteTexture(re);
                    vramTextures.remove(lruTexture);
                    it.remove();
                    statistics.entriesRemoved++;
                }
            }
        }
        cache.put(key, texture);
        if (isVramTexture(texture)) {
            vramTextures.add(texture);
        }
        if (cache.size() > statistics.maxSizeUsed) {
            statistics.maxSizeUsed = cache.size();
        }
    }

    public Texture getTexture(int addr, int lineWidth, int width, int height, int pixelStorage, int clutAddr, int clutMode, int clutStart, int clutShift, int clutMask, int clutNumBlocks, int mipmapLevels, boolean mipmapShareClut, short[] values16, int[] values32) {
        statistics.totalHits++;
        Texture texture = getTexture(addr, clutAddr);
        if (texture == null) {
            statistics.notPresentHits++;
            return texture;
        }
        if (texture.equals(addr, lineWidth, width, height, pixelStorage, clutAddr, clutMode, clutStart, clutShift, clutMask, clutNumBlocks, mipmapLevels, mipmapShareClut, values16, values32)) {
            statistics.successfulHits++;
            return texture;
        }
        statistics.changedHits++;
        return null;
    }

    public void resetTextureAlreadyHashed() {
        textureAlreadyHashed.clear();
    }

    public boolean textureAlreadyHashed(int addr, int clutAddr) {
        return textureAlreadyHashed.contains(getKey(addr, clutAddr));
    }

    public void setTextureAlreadyHashed(int addr, int clutAddr) {
        textureAlreadyHashed.add(getKey(addr, clutAddr));
    }

    public void resetTextureAlreadyHashed(int addr, int clutAddr) {
        textureAlreadyHashed.remove(getKey(addr, clutAddr));
    }

    public void reset(IRenderingEngine re) {
        for (Texture texture : cache.values()) {
            texture.deleteTexture(re);
        }
        cache.clear();
        resetTextureAlreadyHashed();
    }

    private boolean isVramTexture(Texture texture) {
        return VideoEngine.isVRAM(texture.getAddr());
    }

    public void deleteVramTextures(IRenderingEngine re, int addr, int length) {
        for (ListIterator<Texture> lit = vramTextures.listIterator(); lit.hasNext(); ) {
            Texture texture = lit.next();
            if (texture.isInsideMemory(addr, addr + length)) {
                if (log.isDebugEnabled()) {
                    log.debug(String.format("Delete VRAM texture inside GE %s", texture.toString()));
                }
                texture.deleteTexture(re);
                lit.remove();
                Integer key = getKey(texture.getAddr(), texture.getClutAddr());
                cache.remove(key);
                statistics.entriesRemoved++;
            }
        }
    }
}
