package yarpg.definition;

import java.util.List;
import yarpg.Vec2;
import com.google.gson.annotations.SerializedName;

public class DefScenario {

    private int id;

    @SerializedName("scenario_name")
    private String scenarioName;

    private String resource;

    @SerializedName("tile_width")
    private int tileWidth;

    @SerializedName("tile_height")
    private int tileHeight;

    @SerializedName("resource_index")
    private List<Vec2> resourceIndex;

    @SerializedName("resource_map")
    private List<List<Integer>> resourceMap;

    @SerializedName("sprite_list")
    private List<Vec2> spriteList;

    private DefCamera camera;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getScenarioName() {
        return scenarioName;
    }

    public void setScenarioName(String scenarioName) {
        this.scenarioName = scenarioName;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public int getTileWidth() {
        return tileWidth;
    }

    public void setTileWidth(int tileWidth) {
        this.tileWidth = tileWidth;
    }

    public int getTileHeight() {
        return tileHeight;
    }

    public void setTileHeight(int tileHeight) {
        this.tileHeight = tileHeight;
    }

    public List<Vec2> getResourceIndex() {
        return resourceIndex;
    }

    public void setResourceIndex(List<Vec2> resourceIndex) {
        this.resourceIndex = resourceIndex;
    }

    public List<List<Integer>> getResourceMap() {
        return resourceMap;
    }

    public void setResourceMap(List<List<Integer>> resourceMap) {
        this.resourceMap = resourceMap;
    }

    public List<Vec2> getSpriteList() {
        return spriteList;
    }

    public void setSpriteList(List<Vec2> spriteList) {
        this.spriteList = spriteList;
    }

    public DefCamera getCamera() {
        return camera;
    }

    public void setCamera(DefCamera camera) {
        this.camera = camera;
    }
}
