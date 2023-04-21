package arme.core.control;

import java.awt.image.BufferedImage;
import arme.core.video.ImageSource;
import arme.core.video.ImageSourceException;
import com.jme.image.Texture;
import com.jme.image.Texture2D;
import com.jme.image.Texture.ApplyMode;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.renderer.Renderer;
import com.jme.scene.TexCoords;
import com.jme.scene.Spatial.LightCombineMode;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.TextureState;
import com.jme.scene.state.ZBufferState;
import com.jme.system.DisplaySystem;
import com.jmex.awt.swingui.ImageGraphics;

/**
 * Controlador de video que accede a la fuente de imagenes definida por una
 * implementaci&oacute;n de <code>ImageSource</code> , captura cada frame y lo
 * pinta sobre un nodo background para la aplicaci&oacute;n.
 *
 * @author Jonny Alexander Velez Calle
 * @author Juli&aacute;n Alejandro Lamprea Lamprea
 *
 * @version 1.0
 */
final class VideoManager {

    private ImageSource imageSource;

    private int[] buffer;

    private ImageGraphics imGr;

    private Texture texture;

    private final ARConfiguration config;

    private BufferedImage image;

    /**
	 * Constructor que inicializa el acceso a la fuente de im&aacute;genes. Debe
	 * recibir la instancia de la Aplicaci&oacute;n jME (ARApplication) y la
	 * configuraci&oacute;n para el acceso al video. Este controlador debe ser
	 * iniciado antes de cualquier otro controlador, ya que seg&uacute;n los
	 * par&aacute;metros de la fuente de im&aacute;genes, llena los valores de
	 * ancho y alto de la clase de Configuraci&oacute;n.
	 *
	 * @param app
	 *            Tipo de aplicaci&oacute;n de jMonkeyEngine (Ej. BaseGame /
	 *            SimpleGame)
	 * @param config
	 *            Par&aacute;metros de configuraci&oacute;n del video
	 * @throws ARMonkeyEngineException
	 *             Excepci&oacute;n generada si no es posible acceder a la
	 *             fuente de im&aacute;genes
	 */
    public VideoManager(ARApplication app, ARConfiguration config) throws ARMonkeyEngineException {
        this.config = config;
        imageSource = app.getImageSource();
        try {
            imageSource.open(config.getCameraConfigFile());
            config.setWidth(imageSource.getWidth());
            config.setHeight(imageSource.getHeight());
        } catch (ImageSourceException e) {
            throw new ARMonkeyEngineException(e);
        }
        buffer = new int[config.getWidth() * config.getHeight()];
        createARBackground(app, config.isVerticalFlipped());
    }

    /**
	 * Funci&oacute;n que crea un Quad como Nodo de fondo para la
	 * aplicaci&oacute;n y pone sobre este quad la imagen obtenida como una
	 * Textura
	 *
	 * @param app
	 */
    @SuppressWarnings("serial")
    private void createARBackground(ARApplication app, boolean flipV) {
        Quad raster = new Quad("Background", DisplaySystem.getDisplaySystem().getWidth(), DisplaySystem.getDisplaySystem().getHeight()) {

            @Override
            public void draw(Renderer r) {
                r.setOrtho();
                r.draw(this);
                r.unsetOrtho();
            }
        };
        ZBufferState zstate = DisplaySystem.getDisplaySystem().getRenderer().createZBufferState();
        zstate.setWritable(false);
        zstate.setEnabled(false);
        raster.setRenderState(zstate);
        raster.setRenderQueueMode(Renderer.QUEUE_SKIP);
        raster.setLocalTranslation(new Vector3f(DisplaySystem.getDisplaySystem().getWidth() / 2, DisplaySystem.getDisplaySystem().getHeight() / 2, -1.0f));
        raster.setLightCombineMode(LightCombineMode.Off);
        Vector2f[] textCoordVect = null;
        if (flipV) {
            textCoordVect = new Vector2f[] { new Vector2f(1, 1), new Vector2f(1, 0), new Vector2f(0, 0), new Vector2f(0, 1) };
        } else {
            textCoordVect = new Vector2f[] { new Vector2f(1, 0), new Vector2f(1, 1), new Vector2f(0, 1), new Vector2f(0, 0) };
        }
        raster.setTextureCoords(TexCoords.makeNew(textCoordVect));
        image = new BufferedImage(config.getWidth(), config.getHeight(), BufferedImage.TYPE_INT_RGB);
        imGr = ImageGraphics.createInstance(config.getWidth(), config.getHeight(), 0);
        imGr.setClip(0, 0, config.getWidth(), config.getHeight());
        texture = new Texture2D();
        texture.setImage(imGr.getImage());
        texture.setApply(ApplyMode.Combine);
        TextureState texState = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
        texState.setTexture(texture);
        raster.setRenderState(texState);
        raster.updateRenderState();
        app.getRootNode().attachChild(raster);
    }

    /**
	 * M&eacute;todo llamado desde la ARApplication dentro del loop principal de
	 * la aplicaci&oacute;n. Este metodo captura el siguiente frame de la fuente
	 * de im&aacute;genes, pasa el arreglo de pixeles al raster de la imagen a
	 * graficar y la pinta sobre el imageGraphics que representa la textura del
	 * plano de fondo.
	 *
	 * @return
	 * @throws ARMonkeyEngineException
	 */
    public int[] update() throws ARMonkeyEngineException {
        try {
            imageSource.getImage(buffer);
            image.getRaster().setDataElements(0, 0, config.getWidth(), config.getHeight(), buffer);
            imGr.drawImage(image, 0, 0, config.getWidth(), config.getHeight(), null);
            imGr.drawRect(0, 0, config.getWidth() - 1, config.getHeight() - 1);
            try {
                imGr.update(texture, true);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        } catch (ImageSourceException e) {
            throw new ARMonkeyEngineException(e);
        }
        return buffer;
    }
}
