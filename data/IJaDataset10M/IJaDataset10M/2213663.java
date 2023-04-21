package com.eteks.sweethome3d.io;

import java.util.ArrayList;
import java.util.List;
import com.eteks.sweethome3d.model.Content;
import com.eteks.sweethome3d.model.LengthUnit;
import com.eteks.sweethome3d.model.PatternsCatalog;
import com.eteks.sweethome3d.model.RecorderException;
import com.eteks.sweethome3d.model.TextureImage;
import com.eteks.sweethome3d.model.UserPreferences;
import com.eteks.sweethome3d.tools.ResourceURLContent;

/**
 * Default user preferences.
 * @author Emmanuel Puybaret
 */
public class DefaultUserPreferences extends UserPreferences {

    /**
   * Creates default user preferences read from resource files.
   */
    public DefaultUserPreferences() {
        setFurnitureCatalog(new DefaultFurnitureCatalog());
        setTexturesCatalog(new DefaultTexturesCatalog());
        List<TextureImage> patterns = new ArrayList<TextureImage>();
        patterns.add(new PatternTexture("foreground"));
        patterns.add(new PatternTexture("hatchUp"));
        patterns.add(new PatternTexture("hatchDown"));
        patterns.add(new PatternTexture("background"));
        PatternsCatalog patternsCatalog = new PatternsCatalog(patterns);
        setPatternsCatalog(patternsCatalog);
        setFurnitureCatalogViewedInTree(Boolean.parseBoolean(getLocalizedString(DefaultUserPreferences.class, "furnitureCatalogViewedInTree")));
        setNavigationPanelVisible(Boolean.parseBoolean(getLocalizedString(DefaultUserPreferences.class, "navigationPanelVisible")));
        setUnit(LengthUnit.valueOf(getLocalizedString(DefaultUserPreferences.class, "unit").toUpperCase()));
        setRulersVisible(Boolean.parseBoolean(getLocalizedString(DefaultUserPreferences.class, "rulersVisible")));
        setGridVisible(Boolean.parseBoolean(getLocalizedString(DefaultUserPreferences.class, "gridVisible")));
        setFurnitureViewedFromTop(Boolean.parseBoolean(getLocalizedString(DefaultUserPreferences.class, "furnitureViewedFromTop")));
        setFloorColoredOrTextured(Boolean.parseBoolean(getLocalizedString(DefaultUserPreferences.class, "roomFloorColoredOrTextured")));
        setWallPattern(patternsCatalog.getPattern(getLocalizedString(DefaultUserPreferences.class, "wallPattern")));
        setNewWallThickness(Float.parseFloat(getLocalizedString(DefaultUserPreferences.class, "newWallThickness")));
        setNewWallHeight(Float.parseFloat(getLocalizedString(DefaultUserPreferences.class, "newHomeWallHeight")));
        setRecentHomes(new ArrayList<String>());
        try {
            setCurrency(getLocalizedString(DefaultUserPreferences.class, "currency"));
        } catch (IllegalArgumentException ex) {
        }
    }

    /**
   * Throws an exception because default user preferences can't be written 
   * with this class.
   */
    @Override
    public void write() throws RecorderException {
        throw new RecorderException("Default user preferences can't be written");
    }

    /**
   * Throws an exception because default user preferences can't manage language libraries.
   */
    @Override
    public boolean languageLibraryExists(String name) throws RecorderException {
        throw new RecorderException("Default user preferences can't manage language libraries");
    }

    /**
   * Throws an exception because default user preferences can't manage additional language libraries.
   */
    @Override
    public void addLanguageLibrary(String name) throws RecorderException {
        throw new RecorderException("Default user preferences can't manage language libraries");
    }

    /**
   * Throws an exception because default user preferences can't manage furniture libraries.
   */
    @Override
    public boolean furnitureLibraryExists(String name) throws RecorderException {
        throw new RecorderException("Default user preferences can't manage furniture libraries");
    }

    /**
   * Throws an exception because default user preferences can't manage additional furniture libraries.
   */
    @Override
    public void addFurnitureLibrary(String name) throws RecorderException {
        throw new RecorderException("Default user preferences can't manage furniture libraries");
    }

    /**
   * Throws an exception because default user preferences can't manage textures libraries.
   */
    @Override
    public boolean texturesLibraryExists(String name) throws RecorderException {
        throw new RecorderException("Default user preferences can't manage textures libraries");
    }

    /**
   * Throws an exception because default user preferences can't manage additional textures libraries.
   */
    @Override
    public void addTexturesLibrary(String name) throws RecorderException {
        throw new RecorderException("Default user preferences can't manage textures libraries");
    }

    /**
   * A fixed sized pattern.
   */
    private static class PatternTexture implements TextureImage {

        private final String name;

        private final Content image;

        public PatternTexture(String name) {
            this.name = name;
            this.image = new ResourceURLContent(PatternTexture.class, "resources/patterns/" + name + ".png");
        }

        public String getName() {
            return this.name;
        }

        public Content getImage() {
            return this.image;
        }

        public float getWidth() {
            return 10;
        }

        public float getHeight() {
            return 10;
        }
    }
}
