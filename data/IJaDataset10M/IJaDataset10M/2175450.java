package com.gwtyuicarousel.client.ui;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.Element;
import com.gwtyuicarousel.client.util.JsArrayUtil;

/**
 * The attributes of this object match with the configuration properties of the carousel. Please see
 * the documentation of the component here : http://billwscott.com/carousel/#properties. This object
 * is intended to be passed to the {@link Carousel#Carousel(String, CarouselConfig)} constructor.
 * 
 * Please note than indexes int this API are not starting from 1, but 0. The small others
 * differences are listed in the methods javadoc.
 * 
 * @version $0.6.1-beta1$
 * @author Nicolas Hoppenot
 * 
 */
public class CarouselConfig {

    boolean loadOnStart = true;

    /**
   * Contains a reference to the javascript configuration object (javascript object containing
   * key/value pairs)
   */
    JavaScriptObject javascriptObject;

    /**
   * Define the handlers for the events fired by the carousel. This handler are not set directly in
   * the javascript property object, but is setted after the instanciation of {@link Carousel} by
   * using the method {@link Carousel#setCarouselListener(CarouselListener)}.
   */
    private CarouselListener carouselListener;

    /**
   * This constructor instanciate the {@link JavaScriptObject} that will contain the properties
   * values.
   */
    public CarouselConfig() {
        javascriptObject = createObject();
    }

    /**
   * Set the "animationSpeed" property. To have more informations about this property, please see
   * http://billwscott.com/carousel/#properties. The animation speed is here in ms (and not in sec.
   * like in javascript API).
   */
    public native void setAnimationSpeed(int animationSpeed);

    /**
   * Set the "animationMethod" property by calling {@link #setProperty(String, JavaScriptObject)}.
   * To have more informations about this property, please see
   * http://billwscott.com/carousel/#properties.
   */
    public void setAnimationMethod(Carousel.AnimationMethod animationMethod) {
        setProperty("animationMethod", animationMethod.getJsAnimationMethod());
    }

    /**
   * Set the "autoPlay" property by calling {@link #setProperty(String, int)}. To have more
   * informations about this property, please see http://billwscott.com/carousel/#properties.
   */
    public void setAutoPlay(int autoPlay) {
        setProperty("autoPlay", autoPlay);
    }

    /**
   * Set the "firstVisible" property by calling {@link #setProperty(String, int)}. To have more
   * informations about this property, please see http://billwscott.com/carousel/#properties. The
   * index is here a 0 based index.
   */
    public void setFirstVisible(int firstVisible) {
        setProperty("firstVisible", firstVisible + 1);
    }

    /**
   * Set the "navMargin" property by calling {@link #setProperty(String, int)}. To have more
   * informations about this property, please see http://billwscott.com/carousel/#properties.
   */
    public void setNavMargin(int navMargin) {
        setProperty("navMargin", navMargin);
    }

    /**
   * Add an element to the "nextElement" property by calling
   * {@link JsArrayUtil#addElementInJsArray(JavaScriptObject, Element)} method. To have more
   * information about the "nextElement" property, please see
   * http://billwscott.com/carousel/#properties.
   */
    public void addNextElement(Element nextElement) {
        JsArrayUtil.addElementInJsArray(getNextElements(), nextElement);
    }

    /**
   * Remove an element from the "nextElement" elements list. To have more information about the
   * "nextElement" property, please see http://billwscott.com/carousel/#properties.
   */
    public void removeNextElement(Element nextElement) {
        JsArrayUtil.removeElementInJsArray(getNextElements(), nextElement);
    }

    /**
   * Set the "nextElement" property by calling {@link #setProperty(String, JavaScriptObject)}
   * method. To have more information about the "nextElement" property, please see
   * http://billwscott.com/carousel/#properties.
   */
    public void setNextElements(Element[] nextElements) {
        setProperty("nextElement", JsArrayUtil.convertElementToJsArray(nextElements));
    }

    /**
   * Set the "numVisible" property by calling {@link #setProperty(String, int)}. To have more
   * informations about this property, please see http://billwscott.com/carousel/#properties.
   */
    public void setNumVisible(int numVisible) {
        setProperty("numVisible", numVisible);
    }

    /**
   * Set the "orientation" property by calling {@link #setProperty(String, String)}. To have more
   * informations about this property, please see http://billwscott.com/carousel/#properties.
   */
    public void setOrientation(Carousel.Orientation orientation) {
        setProperty("orientation", orientation.toString());
    }

    /**
   * Add an element to the "prevElement" property by calling
   * {@link JsArrayUtil#addElementInJsArray(JavaScriptObject, Element)} method. To have more
   * information about the "prevElement" property, please see
   * http://billwscott.com/carousel/#properties.
   */
    public void addPrevElement(Element prevElement) {
        JsArrayUtil.addElementInJsArray(getPrevElements(), prevElement);
    }

    /**
   * Remove an element from the "prevElement" elements list. To have more information about the
   * "prevElement" property, please see http://billwscott.com/carousel/#properties.
   */
    public void removePrevElement(Element prevElement) {
        JsArrayUtil.removeElementInJsArray(getPrevElements(), prevElement);
    }

    /**
   * Set the "prevElement" property by calling {@link #setProperty(String, JavaScriptObject)}
   * method. To have more information about the "nextElement" property, please see
   * http://billwscott.com/carousel/#properties.
   */
    public void setPrevElements(Element[] prevElements) {
        setProperty("prevElement", JsArrayUtil.convertElementToJsArray(prevElements));
    }

    /**
   * Set the "revealAmount" property by calling {@link #setProperty(String, int)}. To have more
   * informations about this property, please see http://billwscott.com/carousel/#properties.
   */
    public void setRevealAmount(int revealAmount) {
        setProperty("revealAmount", revealAmount);
    }

    /**
   * Set the "scrollAfterAmount" property by calling {@link #setProperty(String, int)}. To have
   * more informations about this property, please see http://billwscott.com/carousel/#properties.
   */
    public void setScrollAfterAmount(int scrollAfterAmount) {
        setProperty("scrollAfterAmount", scrollAfterAmount);
    }

    /**
   * Set the "scrollBeforeAmount" property by calling {@link #setProperty(String, int)}. To have
   * more informations about this property, please see http://billwscott.com/carousel/#properties.
   */
    public void setScrollBeforeAmount(int scrollBeforeAmount) {
        setProperty("scrollBeforeAmount", scrollBeforeAmount);
    }

    /**
   * Set the "scrollInc" property by calling {@link #setProperty(String, int)}. To have more
   * informations about this property, please see http://billwscott.com/carousel/#properties.
   */
    public void setScrollInc(int scrollInc) {
        setProperty("scrollInc", scrollInc);
    }

    /**
   * Set the "size" property by calling {@link #setProperty(String, int)}. To have more
   * informations about this property, please see http://billwscott.com/carousel/#properties.
   */
    public void setSize(int size) {
        setProperty("size", size);
    }

    /**
   * Set the "wrap" property by calling {@link #setProperty(String, boolean)}. To have more
   * informations about this property, please see http://billwscott.com/carousel/#properties.
   */
    public void setWrap(boolean wrap) {
        setProperty("wrap", wrap);
    }

    /**
   * Set the "loadOnStart" property by calling {@link #setProperty(String, boolean)}. To have more
   * informations about this property, please see http://billwscott.com/carousel/#properties.
   */
    public void setLoadOnStart(boolean loadOnStart) {
        this.loadOnStart = loadOnStart;
        setProperty("loadOnStart", loadOnStart);
    }

    /**
   * Set the handlers for the events fired by the Carousel component.
   * Please note than the carousel handlers are setted with setProperty function on javascript Carousel,
   * after the instanciation of the javascript Carousel object in {@link Carousel#Carousel(String, CarouselConfig)}.
   */
    public void setCarouselListener(CarouselListener carouselListener) {
        this.carouselListener = carouselListener;
    }

    /**
   * Get the handlers for the events fired by the Carousel component.
   */
    public CarouselListener getCarouselListener() {
        return carouselListener;
    }

    /**
   * Return the reference to the javascript object containing the key/values pairs.
   */
    public JavaScriptObject getJavascriptObject() {
        return javascriptObject;
    }

    /**
   * Create a new javascript object instance. This instance will contain the key/value pairs. The
   * "nextElement" and "prevElement" properties are initialized to an empty tab.
   */
    private native JavaScriptObject createObject();

    /**
   * Set an int property value in the configuration object.
   */
    private native void setProperty(String attr, int value);

    /**
   * Set a boolean property value in the configuration object.
   */
    private native void setProperty(String attr, boolean value);

    /**
   * Set a string property value in the configuration object.
   */
    private native void setProperty(String attr, String value);

    /**
   * Set a JavaScriptObject property value in the configuration object.
   */
    private native void setProperty(String attr, JavaScriptObject value);

    /**
   * Get the javascript object containing the "nextElement" property HTML elements
   */
    private native JavaScriptObject getNextElements();

    /**
   * Get the javascript object containing the "prevElement" property HTML elements
   */
    private native JavaScriptObject getPrevElements();

    /**
   * @return the loadOnStart
   */
    public boolean isLoadOnStart() {
        return loadOnStart;
    }
}
