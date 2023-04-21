package marten.aoe.engine.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import marten.aoe.GameInfo;
import marten.aoe.data.tiles.TileLayerDTO;
import marten.aoe.data.tiles.TileLayers;
import marten.aoe.data.type.UnitSize;
import marten.aoe.data.type.UnitType;
import marten.aoe.dto.PointDTO;
import marten.aoe.dto.TileDTO;
import marten.aoe.dto.depreciated.DamageDTO;
import marten.aoe.dto.depreciated.DefenseDTO;

public class Tile {

    private Unit unit = null;

    private final Set<Player> exploredPlayers = new HashSet<Player>();

    private final Set<Player> visiblePlayers = new HashSet<Player>();

    private final Set<Player> poweredPlayers = new HashSet<Player>();

    private final Map map;

    private final PointDTO coordinates;

    private ArrayList<TileLayerDTO> layers = new ArrayList<TileLayerDTO>();

    public Tile(Map map, PointDTO coordinates) {
        this.map = map;
        this.coordinates = coordinates;
    }

    /** @return the owner of this tile. */
    public final Map getMap() {
        return this.map;
    }

    /** @return the coordinates of this tile. */
    public final PointDTO getCoordinates() {
        return this.coordinates;
    }

    /**
     * @return <code>true</code> if there is a unit in this tile,
     *         <code>false</code> otherwise.
     */
    public final boolean isOccupied() {
        return this.getUnit() != null;
    }

    /**
     * Calculates the defense value of being in this tile.
     * 
     * @param size
     *            - the size of the unit.
     * @param type
     *            - the type of the unit.
     * @return the amount of points the maximum attacking force is reduced by
     *         when the unit is defending in this tile.
     */
    public final int getDefenseBonus(UnitSize size, UnitType type) {
        return this.getDefenseBonus().getValue(size, type);
    }

    public final int distanceTo(Tile other) {
        int minimumEstimate = other.coordinates.getX() - this.coordinates.getX();
        minimumEstimate *= (minimumEstimate < 0 ? -1 : 1);
        int minY = this.coordinates.getY() - minimumEstimate / 2;
        int maxY = this.coordinates.getY() + minimumEstimate / 2;
        if (minimumEstimate % 2 != 0) {
            if (this.coordinates.getX() % 2 == 0) {
                --minY;
            } else {
                ++maxY;
            }
        }
        if (other.coordinates.getY() > maxY) {
            return minimumEstimate + other.coordinates.getY() - maxY;
        }
        if (other.coordinates.getY() < minY) {
            return minimumEstimate + minY - other.coordinates.getY();
        }
        return minimumEstimate;
    }

    public final List<Tile> neighbors(int distance) {
        List<Tile> answer = new ArrayList<Tile>();
        for (int x = this.coordinates.getX() - distance; x <= this.coordinates.getX() + distance; ++x) {
            for (int y = this.coordinates.getY() - distance; y <= this.coordinates.getY() + distance; ++y) {
                Tile candidate = this.map.getTile(new PointDTO(x, y));
                if (candidate != null && this.distanceTo(candidate) <= distance) {
                    answer.add(candidate);
                }
            }
        }
        return answer;
    }

    public ArrayList<TileLayerDTO> getLayers() {
        return this.layers;
    }

    public void addLayer(TileLayerDTO layer) {
        this.layers.add(layer);
    }

    public final TileDTO getDTO(Player player) {
        if (player != Player.SYSTEM && !this.isExplored(player)) {
            ArrayList<TileLayerDTO> l = new ArrayList<TileLayerDTO>();
            l.add(new TileLayerDTO(TileLayers.SHROUD));
            return new TileDTO(l, this.getCoordinates(), null);
        }
        return new TileDTO(this.layers, this.getCoordinates(), (this.getUnit() != null && this.isVisible(player) ? this.getUnit().getDTO(player) : null), this.isVisible(player), this.isPowered(player));
    }

    public final boolean isExplored(Player player) {
        return this.exploredPlayers.contains(player);
    }

    public final boolean isVisible(Player player) {
        return this.visiblePlayers.contains(player);
    }

    public boolean isPowered(Player player) {
        return this.poweredPlayers.contains(player);
    }

    public final void setExplored(Player player) {
        this.exploredPlayers.add(player);
    }

    public final void setVisible(Player player) {
        this.visiblePlayers.add(player);
    }

    public final void setInvisible(Player player) {
        this.visiblePlayers.remove(player);
    }

    public void setPowered(Player player) {
        this.poweredPlayers.add(player);
    }

    public void setUnpowered(Player player) {
        this.poweredPlayers.remove(player);
    }

    public final Unit getUnit() {
        return this.unit;
    }

    public final Unit popUnit(Player player) {
        if (this.unit != null && (player == this.unit.getOwner() || player == Player.SYSTEM)) {
            this.onUnitExit();
            this.unit.onTileExit(this);
            return this.removeUnit(player);
        }
        return null;
    }

    public final boolean pushUnit(Player player, Unit unit) {
        if (this.unit == null && unit != null && (player == unit.getOwner() || player == Player.SYSTEM) && (unit.applyMovementCost(GameInfo.calculator.getMovementCost(this.getDTO(player), unit.getDTO(player))) > -1)) {
            this.unit = unit;
            this.unit.onTileEntry(this);
            this.unit.setLocation(this);
            this.onUnitEntry();
            return true;
        }
        return false;
    }

    public final Unit removeUnit(Player player) {
        if (this.unit != null && (player == this.unit.getOwner() || player == Player.SYSTEM)) {
            Unit answer = this.unit;
            this.unit = null;
            answer.setLocation(null);
            return answer;
        }
        return null;
    }

    public final boolean insertUnit(Player player, Unit unit) {
        if (this.unit == null && unit != null && (player == unit.getOwner() || player == Player.SYSTEM)) {
            this.unit = unit;
            this.unit.setLocation(this);
            return true;
        }
        return false;
    }

    public final void applyDamage(DamageDTO damage) {
        if (this.unit != null) {
            this.unit.applyDamage(damage);
        }
    }

    public final void turnOver() {
        if (this.unit != null) {
            this.unit.turnOver();
        }
    }

    public final boolean isDetected(Player player) {
        return (this.unit != null ? this.unit.isDetected(player) : true);
    }

    public final void setDetected(Player player) {
        if (this.unit != null) {
            this.unit.setDetected(player);
        }
    }

    public final void setUndetected(Player player) {
        if (this.unit != null) {
            this.unit.setUndetected(player);
        }
    }

    public final boolean hasAnythingCloaked(Player player) {
        if (this.unit == null) {
            return false;
        }
        return this.unit.isCloaked();
    }

    public void onUnitEntry() {
    }

    public void onUnitExit() {
    }

    public DefenseDTO getDefenseBonus() {
        return null;
    }

    public int getHeight() {
        return 0;
    }

    public String[] getSpecialFeatures() {
        return null;
    }

    public boolean hasLayer(String id) {
        for (TileLayerDTO layer : this.layers) {
            if (layer.getName().equals(id)) {
                return true;
            }
        }
        return false;
    }
}
