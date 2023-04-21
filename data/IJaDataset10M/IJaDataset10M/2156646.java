package br.com.felix.fwt.ui;

import java.io.PrintWriter;
import org.apache.log4j.Logger;
import br.com.felix.fwt.log.LoggerFactory;
import br.com.felix.fwt.ui.css.Style;
import br.com.felix.fwt.ui.exception.ComponentRenderException;
import br.com.felix.fwt.ui.exception.FontUnavailableException;
import br.com.felix.fwt.util.TextImageBuilder;
import br.com.felix.fwt.util.exception.ImageGenerationException;

/**
 * This class allows you to create dynamic images to use as Buttons.
 * This way you can use any Font you want.
 * */
public class TextImageButton extends Button {

    private static final long serialVersionUID = -3599145385102447247L;

    private static Logger logger = LoggerFactory.getLogger(TextImageButton.class);

    /**
	 * Flag that indicates whether the image needs to be created (or recreated).
	 * */
    private boolean recreateImage;

    /**
	 * Used to configure the Font used to create the text image.
	 * */
    private Style style;

    /**
	 * Used to generate the button image.
	 * */
    private TextImageBuilder textImageBuilder;

    /**
	 * @param value the text to be displayed by the image.
	 * */
    public TextImageButton(String value) {
        super(value);
        textImageBuilder = new TextImageBuilder() {

            @Override
            protected String getPrefix() {
                return "txtImg";
            }
        };
        textImageBuilder.setText(value);
        recreateImage = textImageBuilder.needsToBeCreated();
        super.image = new Image(textImageBuilder.getWebFilePath());
    }

    /**
     * @param value the text to be displayed by the image.
     * @param style the font style.
     * */
    public TextImageButton(String value, Style style) {
        this(value);
        setStyle(style);
    }

    /**
	 * @param style the style used to configure the Font of the text image.
	 * */
    @Override
    public void setStyle(Style style) {
        this.style = (Style) style.clone();
    }

    @Override
    public void write(PrintWriter out) throws ComponentRenderException {
        if (recreateImage) {
            try {
                createImage();
            } catch (ImageGenerationException e) {
                String msg = "Could not render TextImageButton. Error generating image.";
                logger.error(msg, e);
                throw new ComponentRenderException(msg, e);
            } catch (FontUnavailableException e) {
                String msg = "Could not render TextImage. Chosen font is not available.";
                logger.error(msg, e);
                throw new ComponentRenderException(msg, e);
            }
        }
        super.write(out);
    }

    /**
	 * Creates a PNG image with the text, font and style provided.
	 * @throws ImageGenerationException 
	 * @throws FontUnavailableException 
	 * 
	 * */
    private void createImage() throws ImageGenerationException, FontUnavailableException {
        logger.info("Creating image for button '" + getValue() + "'");
        textImageBuilder.setStyle(style);
        textImageBuilder.makeImage();
    }

    /**
     * Forces image generation.
     * */
    public void recreateImage() {
        this.recreateImage = true;
    }
}
