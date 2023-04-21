package com.jcraft.jdxpc;

class ServerCache {

    static final int SERVER_TEXT_CACHE_SIZE = 9999;

    CharCache[] textCache = new CharCache[SERVER_TEXT_CACHE_SIZE];

    int lastSequenceNum = 0;

    IntCache replySequenceNumCache = new IntCache(6);

    IntCache eventSequenceNumCache = new IntCache(6);

    int lastTimestamp = 0;

    CharCache depthCache = new CharCache();

    IntCache visualCache = new IntCache(8);

    IntCache colormapCache = new IntCache(8);

    CharCache[] opcodeCache = new CharCache[256];

    int lastOpcode = 0;

    static BlockCache lastInitReply = new BlockCache();

    CharCache errorCodeCache = new CharCache();

    IntCache errorMinorCache = new IntCache(8);

    CharCache errorMajorCache = new CharCache();

    CharCache buttonCache = new CharCache();

    IntCache colormapNotifyWindowCache = new IntCache(8);

    IntCache colormapNotifyColormapCache = new IntCache(8);

    IntCache[] configureNotifyWindowCache = new IntCache[3];

    IntCache[] configureNotifyGeomCache = new IntCache[5];

    IntCache createNotifyWindowCache = new IntCache(8);

    int createNotifyLastWindow = 0;

    IntCache exposeWindowCache = new IntCache(12);

    IntCache[] exposeGeomCache = new IntCache[5];

    IntCache focusInWindowCache = new IntCache(8);

    static BlockCache lastKeymap = new BlockCache();

    byte keyPressLastKey = 0;

    byte[] keyPressCache = new byte[23];

    IntCache mapNotifyEventCache = new IntCache(8);

    IntCache mapNotifyWindowCache = new IntCache(8);

    IntCache motionNotifyTimestampCache = new IntCache(8);

    int motionNotifyLastRootX = 0;

    int motionNotifyLastRootY = 0;

    IntCache motionNotifyRootXCache = new IntCache(8);

    IntCache motionNotifyRootYCache = new IntCache(8);

    IntCache motionNotifyEventXCache = new IntCache(8);

    IntCache motionNotifyEventYCache = new IntCache(8);

    IntCache motionNotifyStateCache = new IntCache(8);

    IntCache[] motionNotifyWindowCache = new IntCache[3];

    IntCache noExposeDrawableCache = new IntCache(8);

    IntCache noExposeMinorCache = new IntCache(8);

    CharCache noExposeMajorCache = new CharCache();

    IntCache propertyNotifyWindowCache = new IntCache(8);

    IntCache propertyNotifyAtomCache = new IntCache(8);

    IntCache reparentNotifyWindowCache = new IntCache(8);

    IntCache selectionClearWindowCache = new IntCache(8);

    IntCache selectionClearAtomCache = new IntCache(8);

    IntCache visibilityNotifyWindowCache = new IntCache(8);

    IntCache getGeometryRootCache = new IntCache(8);

    IntCache[] getGeometryGeomCache = new IntCache[5];

    IntCache getInputFocusWindowCache = new IntCache(8);

    static byte getKeyboardMappingLastKeysymsPerKeycode = (byte) 0;

    static BlockCache getKeyboardMappingLastMap = new BlockCache();

    IntCache getKeyboardMappingKeysymCache = new IntCache(8);

    CharCache getKeyboardMappingLastByteCache = new CharCache();

    static BlockCache getModifierMappingLastMap = new BlockCache();

    CharCache getPropertyFormatCache = new CharCache();

    IntCache getPropertyTypeCache = new IntCache(8);

    TextCompressor getPropertyTextCompressor = new TextCompressor(textCache);

    static BlockCache xResources = new BlockCache();

    IntCache getSelectionOwnerCache = new IntCache(8);

    IntCache getWindowAttributesClassCache = new IntCache(8);

    CharCache getWindowAttributesBitGravityCache = new CharCache();

    CharCache getWindowAttributesWinGravityCache = new CharCache();

    IntCache getWindowAttributesPlanesCache = new IntCache(8);

    IntCache getWindowAttributesPixelCache = new IntCache(8);

    IntCache getWindowAttributesAllEventsCache = new IntCache(8);

    IntCache getWindowAttributesYourEventsCache = new IntCache(8);

    IntCache getWindowAttributesDontPropagateCache = new IntCache(8);

    BlockCache queryColorsLastReply = new BlockCache();

    static BlockCacheSet queryFontFontCache = new BlockCacheSet(16);

    IntCache[] queryFontCharInfoCache = new IntCache[6];

    int[] queryFontLastCharInfo = new int[6];

    IntCache queryPointerRootCache = new IntCache(8);

    IntCache queryPointerChildCache = new IntCache(8);

    IntCache translateCoordsChildCache = new IntCache(8);

    IntCache translateCoordsXCache = new IntCache(8);

    IntCache translateCoordsYCache = new IntCache(8);

    ServerCache() {
        for (int i = 0; i < textCache.length; i++) textCache[i] = new CharCache();
        for (int i = 0; i < opcodeCache.length; i++) opcodeCache[i] = new CharCache();
        for (int i = 0; i < configureNotifyWindowCache.length; i++) configureNotifyWindowCache[i] = new IntCache(8);
        for (int i = 0; i < configureNotifyGeomCache.length; i++) configureNotifyGeomCache[i] = new IntCache(8);
        for (int i = 0; i < exposeGeomCache.length; i++) exposeGeomCache[i] = new IntCache(8);
        for (int i = 0; i < motionNotifyWindowCache.length; i++) motionNotifyWindowCache[i] = new IntCache(8);
        for (int i = 0; i < getGeometryGeomCache.length; i++) getGeometryGeomCache[i] = new IntCache(8);
        for (int i = 0; i < keyPressCache.length; i++) keyPressCache[i] = 0;
        for (int i = 0; i < queryFontCharInfoCache.length; i++) {
            queryFontCharInfoCache[i] = new IntCache(8);
            queryFontLastCharInfo[i] = 0;
        }
    }
}
