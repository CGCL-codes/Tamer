package com.moesol.maps.server.tms;

import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.moesol.gwt.maps.shared.Tile;

/**
 * An ITileRenderer that contains other renderers that it renders
 * sequentially.
 */
public class CompositeTileRenderer implements ITileRenderer {

    private static final Logger LOGGER = Logger.getLogger(CompositeTileRenderer.class.getName());

    private final String m_layersParameterName;

    public CompositeTileRenderer(String layersParameterName) {
        m_layersParameterName = layersParameterName;
    }

    private HashMap<String, ITileRenderer> m_renderers = new HashMap<String, ITileRenderer>();

    /**
	 * @param queryParams Should contain an entry named by the 'layersParameterName'
	 * passed to the constructor, the first element of which is a comma-separated
	 * string of layer IDs.
	 */
    @Override
    public void render(Tile tile, Graphics2D graphics, Map<String, String[]> queryParams) {
        String[] layers = queryParams.get(m_layersParameterName)[0].split(",");
        for (String layer : layers) {
            if (m_renderers.containsKey(layer)) {
                ITileRenderer renderer = m_renderers.get(layer);
                try {
                    renderer.render(tile, graphics, queryParams);
                } catch (Throwable t) {
                    LOGGER.log(Level.SEVERE, String.format("Renderer for layer '%1$s' threw unexpected exception rendering tile: %2$s", layer, tile.toString()), t);
                }
            }
        }
    }

    /**
	 * Add a child renderer.
	 * 
	 * @param layerId The ID that will be used to identify the layer in
	 * calls to render
	 * 
	 * @param renderer The renderer to add
	 */
    public void addRenderer(String layerId, ITileRenderer renderer) {
        m_renderers.put(layerId, renderer);
    }
}
