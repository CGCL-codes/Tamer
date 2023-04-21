package jasel.av;

import jasel.engine.Renderer;

/**
 * Blob is an odd name but a simple interface. It stands for
 * "Blitting object", and just generally means anything the renderer
 * can throw on the screen. I wanted to differentiate blobs from animations,
 * sprites and what not. A blob is the most primitive type of object to be
 * drawn on the screen. JaselObject, Ani, Font, etc all extend or work with Blobs.
 *
 */
public interface Blob {

    /**
	 * Called by the renderer whenever this blob should draw itself on the screen.
	 * 
	 * This method is very similiar to AWT's paint(Graphics g), with Renderer playing
	 * the same basic role as Graphics
	 * 
	 * @param millis the number of millis that have passed since the last call
	 * 			to this method, generally the number of millis since the last frame.
	 * @param r The renderer to use to draw with.
	 */
    public void draw(long millis, Renderer r);
}
