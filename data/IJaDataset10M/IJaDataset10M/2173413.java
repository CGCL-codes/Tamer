package com.volantis.mcs.policies.variants.image;

import com.volantis.mcs.policies.PolicyFactory;
import com.volantis.mcs.policies.variants.metadata.PixelDimensionsMetaDataBuilder;

/**
 * Builder of {@link ImageMetaData} instances.
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with current and future releases of the
 * product at binary and source levels.</strong>
 * </p>
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 * @see ImageMetaData
 * @see PolicyFactory#createImageMetaDataBuilder()
 * @since 3.5.1
 */
public interface ImageMetaDataBuilder extends PixelDimensionsMetaDataBuilder {

    /**
     * Get the built {@link ImageMetaData}.
     *
     * <p>Returns a newly created instance the first time it is called and
     * if the state has changed since the last time this method was called,
     * otherwise it returns the same instance as the previous call.</p>
     *
     * @return The built {@link ImageMetaData}.
     */
    ImageMetaData getImageMetaData();

    /**
     * Getter for the
     * <a href="ImageMetaData.html#imageEncoding">image encoding</a> property.
     *
     * @return Value of the
     *         <a href="ImageMetaData.html#imageEncoding">image encoding</a>
     *         property.
     */
    ImageEncoding getImageEncoding();

    /**
     * Setter for the
     * <a href="ImageMetaData.html#imageEncoding">image encoding</a> property.
     *
     * @param imageEncoding New value of the
     *                      <a href="ImageMetaData.html#imageEncoding">image
     *                      encoding</a> property.
     */
    void setImageEncoding(ImageEncoding imageEncoding);

    /**
     * Getter for the <a href="ImageMetaData.html#pixelDepth">pixel depth</a>
     * property.
     *
     * @return Value of the
     *         <a href="ImageMetaData.html#pixelDepth">pixel depth</a> property.
     */
    int getPixelDepth();

    /**
     * Setter for the <a href="ImageMetaData.html#pixelDepth">pixel depth</a>
     * property.
     *
     * @param pixelDepth New value of the
     *                   <a href="ImageMetaData.html#pixelDepth">pixel depth</a>
     *                   property.
     */
    void setPixelDepth(int pixelDepth);

    /**
     * Getter for the <a href="ImageMetaData.html#rendering">rendering</a>
     * property.
     *
     * @return Value of the
     *         <a href="ImageMetaData.html#rendering">rendering</a> property.
     */
    ImageRendering getRendering();

    /**
     * Setter for the <a href="ImageMetaData.html#rendering">rendering</a>
     * property.
     *
     * @param rendering New value of the
     *                  <a href="ImageMetaData.html#rendering">rendering</a>
     *                  property.
     */
    void setRendering(ImageRendering rendering);

    /**
     * Getter for the
     * <a href="ImageMetaData.html#conversionMode">conversion mode</a>
     * property.
     *
     * @return Value of the
     *         <a href="ImageMetaData.html#conversionMode">conversion mode</a>
     *         property.
     */
    ImageConversionMode getConversionMode();

    /**
     * Setter for the
     * <a href="ImageMetaData.html#conversionMode">conversion mode</a> property.
     *
     * @param conversionMode New value of the
     *                       <a href="ImageMetaData.html#conversionMode">conversion
     *                       mode</a> property.
     */
    void setConversionMode(ImageConversionMode conversionMode);

    /**
     * Getter for the <a href="ImageMetaData.html#preserveLeft">preserve left</a> property.
     * @return Value of the <a href="ImageMetaData.html#preserveLeft">preserve left</a>
     * property.
     */
    int getPreserveLeft();

    /**
     * Setter for the <a href="ImageMetaData.html#preserveLeft">preserve left</a> property.
     *
     * @param preserveLeft New value of the
     * <a href="ImageMetaData.html#preserveLeft">preserve left</a> property.
     */
    void setPreserveLeft(int preserveLeft);

    /**
     * Getter for the <a href="ImageMetaData.html#preserveRight">preserve right</a> property.
     * @return Value of the <a href="ImageMetaData.html#preserveRight">preserve right</a>
     * property.
     */
    int getPreserveRight();

    /**
     * Setter for the <a href="ImageMetaData.html#preserveRight">preserve right</a> property.
     *
     * @param preserveRight New value of the
     * <a href="ImageMetaData.html#preserveRight">preserve right</a> property.
     */
    void setPreserveRight(int preserveRight);
}
