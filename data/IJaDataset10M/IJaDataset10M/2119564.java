package com.skillworld.webapp.model.gameservice;

public class NoLevelMapException extends Exception {

    private Long userId;

    private Long mapId;

    private String mapName;

    public NoLevelMapException(Long uId, Long mId, String mName) {
        super("user-" + uId + "-doesn't-have-level-to-enter-on-map/department-" + mName);
        this.userId = uId;
        this.mapId = mId;
        this.mapName = mName;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getMapId() {
        return mapId;
    }

    public String getMapName() {
        return mapName;
    }
}
