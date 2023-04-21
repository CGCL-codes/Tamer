package net.sf.gridarta.var.daimonin.model.maparchobject;

import net.sf.gridarta.model.maparchobject.AbstractMapArchObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * MapArchObject contains the specific meta data about a map that is stored in
 * the map-arch, at the very beginning of the map file. The map meta data is
 * information like mapSize, difficulty level, darkness etc.).
 * @author <a href="mailto:andi.vogl@gmx.net">Andreas Vogl</a>
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @author Andreas Kirschbaum
 * @note Though this class is named MapArchObject, it is <strong>not a subclass
 * of GameObject</strong>; the name is for technical reasons, not for semantic /
 * inheritance!
 * @todo This class should be changed so map attributes are reflected in a more
 * generic way like arch attributes.
 */
public class MapArchObject extends AbstractMapArchObject<MapArchObject> {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * No save map.
     * @serial
     */
    private boolean noSave = false;

    /**
     * No magic spells.
     * @serial
     */
    private boolean noMagic = false;

    /**
     * No prayers.
     * @serial
     */
    private boolean noPriest = false;

    /**
     * No harmful spells allowed.
     * @serial
     */
    private boolean noHarm = false;

    /**
     * No summoning allowed.
     * @serial
     */
    private boolean noSummon = false;

    /**
     * Check map reset status after re-login.
     * @serial
     */
    private boolean fixedLogin = false;

    /**
     * Permanent death with revivable corpses.
     * @serial
     */
    private boolean permDeath = false;

    /**
     * Permanent death with corpses temporarily available.
     * @serial
     */
    private boolean ultraDeath = false;

    /**
     * Permanent death with instant character deletion.
     * @serial
     */
    private boolean ultimateDeath = false;

    /**
     * Player vs Player combat allowed.
     * @serial
     */
    private boolean pvp = false;

    /**
     * The tileset id. 0 means no available tileset id
     * @serial
     */
    private int tilesetId = 0;

    /**
     * The tileset x coordinate.
     * @serial
     */
    private int tilesetX = 0;

    /**
     * The tileset y coordinate.
     * @serial
     */
    private int tilesetY = 0;

    /**
     * The name of the background music. Set to empty string if unset.
     * @serial
     */
    @NotNull
    private String backgroundMusic = "";

    /**
     * {@inheritDoc}
     */
    @Override
    public void setState(@NotNull final MapArchObject mapArchObject) {
        super.setState(mapArchObject);
        setNoSave(mapArchObject.noSave);
        setNoMagic(mapArchObject.noMagic);
        setNoPriest(mapArchObject.noPriest);
        setNoHarm(mapArchObject.noHarm);
        setNoSummon(mapArchObject.noSummon);
        setFixedLogin(mapArchObject.fixedLogin);
        setPermDeath(mapArchObject.permDeath);
        setUltraDeath(mapArchObject.ultraDeath);
        setUltimateDeath(mapArchObject.ultimateDeath);
        setPvp(mapArchObject.pvp);
        setTilesetId(mapArchObject.tilesetId);
        setTilesetX(mapArchObject.tilesetX);
        setTilesetY(mapArchObject.tilesetY);
        setBackgroundMusic(mapArchObject.backgroundMusic);
    }

    /**
     * Returns the no save attribute.
     * @return the no save attribute
     */
    public boolean isNoSave() {
        return noSave;
    }

    /**
     * Sets the no save attribute.
     * @param noSave the no save attribute
     */
    public void setNoSave(final boolean noSave) {
        if (this.noSave == noSave) {
            return;
        }
        this.noSave = noSave;
        setModified();
    }

    /**
     * Returns the no magic attribute.
     * @return the no magic attribute
     */
    public boolean isNoMagic() {
        return noMagic;
    }

    /**
     * Sets the no magic attribute.
     * @param noMagic the no magic attribute
     */
    public void setNoMagic(final boolean noMagic) {
        if (this.noMagic == noMagic) {
            return;
        }
        this.noMagic = noMagic;
        setModified();
    }

    /**
     * Returns the no priest attribute.
     * @return the no priest attribute
     */
    public boolean isNoPriest() {
        return noPriest;
    }

    /**
     * Sets the no priest attribute.
     * @param noPriest the no priest attribute
     */
    public void setNoPriest(final boolean noPriest) {
        if (this.noPriest == noPriest) {
            return;
        }
        this.noPriest = noPriest;
        setModified();
    }

    /**
     * Returns the no summon attribute.
     * @return the no summon attribute
     */
    public boolean isNoSummon() {
        return noSummon;
    }

    /**
     * Sets the no summon attribute.
     * @param noSummon the no summon attribute
     */
    public void setNoSummon(final boolean noSummon) {
        if (this.noSummon == noSummon) {
            return;
        }
        this.noSummon = noSummon;
        setModified();
    }

    /**
     * Returns the no harm attribute.
     * @return the no harm attribute
     */
    public boolean isNoHarm() {
        return noHarm;
    }

    /**
     * Sets the no harm attribute.
     * @param noHarm the no harm attribute
     */
    public void setNoHarm(final boolean noHarm) {
        if (this.noHarm == noHarm) {
            return;
        }
        this.noHarm = noHarm;
        setModified();
    }

    /**
     * Returns the fixed login attribute.
     * @return the fixed login attribute
     */
    public boolean isFixedLogin() {
        return fixedLogin;
    }

    /**
     * Sets the fixed login attribute.
     * @param fixedLogin the fixed login attribute
     */
    public void setFixedLogin(final boolean fixedLogin) {
        if (this.fixedLogin == fixedLogin) {
            return;
        }
        this.fixedLogin = fixedLogin;
        setModified();
    }

    /**
     * Returns the permanent death attribute.
     * @return the permanent death attribute
     */
    public boolean isPermDeath() {
        return permDeath;
    }

    /**
     * Sets the permanent death attribute.
     * @param permDeath the permanent death attribute
     */
    public void setPermDeath(final boolean permDeath) {
        if (this.permDeath == permDeath) {
            return;
        }
        this.permDeath = permDeath;
        setModified();
    }

    /**
     * Returns the ultra death attribute.
     * @return the ultra death attribute
     */
    public boolean isUltraDeath() {
        return ultraDeath;
    }

    /**
     * Sets the ultra death attribute.
     * @param ultraDeath the ultra death attribute
     */
    public void setUltraDeath(final boolean ultraDeath) {
        if (this.ultraDeath == ultraDeath) {
            return;
        }
        this.ultraDeath = ultraDeath;
        setModified();
    }

    /**
     * Returns the ultimate death attribute.
     * @return the ultimate death attribute
     */
    public boolean isUltimateDeath() {
        return ultimateDeath;
    }

    /**
     * Sets the ultimate death attribute.
     * @param ultimateDeath the ultimate death attribute
     */
    public void setUltimateDeath(final boolean ultimateDeath) {
        if (this.ultimateDeath == ultimateDeath) {
            return;
        }
        this.ultimateDeath = ultimateDeath;
        setModified();
    }

    /**
     * Returns the pvp attribute.
     * @return the pvp attribute
     */
    public boolean isPvp() {
        return pvp;
    }

    /**
     * Sets the pvp attribute.
     * @param pvp the pvp attribute
     */
    public void setPvp(final boolean pvp) {
        if (this.pvp == pvp) {
            return;
        }
        this.pvp = pvp;
        setModified();
    }

    /**
     * Returns the tileset id.
     * @return the tileset id
     */
    public int getTilesetId() {
        return tilesetId;
    }

    /**
     * Sets the tileset id.
     * @param tilesetId the tileset id
     */
    public void setTilesetId(final int tilesetId) {
        if (this.tilesetId == tilesetId) {
            return;
        }
        this.tilesetId = tilesetId;
        setModified();
    }

    /**
     * Returns the tileset x coordinate.
     * @return the tileset x coordinate
     */
    public int getTilesetX() {
        return tilesetX;
    }

    /**
     * Sets the tileset x coordinate.
     * @param tilesetX the tileset x coordinate
     */
    public void setTilesetX(final int tilesetX) {
        if (this.tilesetX == tilesetX) {
            return;
        }
        this.tilesetX = tilesetX;
        setModified();
    }

    /**
     * Returns the tileset y coordinate.
     * @return the tileset y coordinate
     */
    public int getTilesetY() {
        return tilesetY;
    }

    /**
     * Sets the tileset y coordinate.
     * @param tilesetY the tileset y coordinate
     */
    public void setTilesetY(final int tilesetY) {
        if (this.tilesetY == tilesetY) {
            return;
        }
        this.tilesetY = tilesetY;
        setModified();
    }

    /**
     * Returns the name of the background music.
     * @return the name or an empty string if unset
     */
    @NotNull
    public String getBackgroundMusic() {
        return backgroundMusic;
    }

    /**
     * Sets the name of the background music.
     * @param backgroundMusic the name or an empty string to unset
     */
    public void setBackgroundMusic(@NotNull final String backgroundMusic) {
        if (this.backgroundMusic.equals(backgroundMusic)) {
            return;
        }
        this.backgroundMusic = backgroundMusic;
        setModified();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDifficulty(final int difficulty) {
        super.setDifficulty(difficulty < 1 ? 1 : difficulty);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(@Nullable final Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }
        final MapArchObject mapArchObject = (MapArchObject) obj;
        return super.equals(obj) && mapArchObject.noSave == noSave && mapArchObject.noMagic == noMagic && mapArchObject.noPriest == noPriest && mapArchObject.noHarm == noHarm && mapArchObject.noSummon == noSummon && mapArchObject.fixedLogin == fixedLogin && mapArchObject.permDeath == permDeath && mapArchObject.ultraDeath == ultraDeath && mapArchObject.ultimateDeath == ultimateDeath && mapArchObject.pvp == pvp && mapArchObject.tilesetId == tilesetId && mapArchObject.tilesetX == tilesetX && mapArchObject.tilesetY == tilesetY && mapArchObject.backgroundMusic.equals(backgroundMusic);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return super.hashCode() + (noSave ? 2 : 0) + (noMagic ? 4 : 0) + (noPriest ? 8 : 0) + (noHarm ? 16 : 0) + (noSummon ? 32 : 0) + (fixedLogin ? 64 : 0) + (permDeath ? 128 : 0) + (ultraDeath ? 256 : 0) + (ultimateDeath ? 512 : 0) + (pvp ? 1024 : 0) + tilesetId + tilesetX + tilesetY + backgroundMusic.hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public MapArchObject clone() {
        return super.clone();
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    protected MapArchObject getThis() {
        return this;
    }
}
