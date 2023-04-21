package by.prognoz.exjaxb.test.adhoc;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for menuItem complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="menuItem">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.prognoz.ru/Adhoc}obj">
 *       &lt;sequence>
 *         &lt;element name="MenuItem" type="{http://www.prognoz.ru/Adhoc}menuItem" maxOccurs="unbounded"/>
 *         &lt;element name="MenuItem.EventHandlers" maxOccurs="unbounded">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="EventHandlers">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="MenuClickEventHandler" type="{http://www.w3.org/2001/XMLSchema}anyType" maxOccurs="unbounded"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="Text" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="ImageSource" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "menuItem", propOrder = { "menuItem", "menuItemEventHandlers" })
public class MenuItem extends Obj {

    @XmlElement(name = "MenuItem", required = true)
    protected List<MenuItem> menuItem;

    @XmlElement(name = "MenuItem.EventHandlers", required = true)
    protected List<MenuItem.MenuItemEventHandlers> menuItemEventHandlers;

    @XmlAttribute(name = "Text")
    protected String text;

    @XmlAttribute(name = "ImageSource")
    protected String imageSource;

    /**
     * Gets the value of the menuItem property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the menuItem property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMenuItem().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MenuItem }
     * 
     * 
     */
    public List<MenuItem> getMenuItem() {
        if (menuItem == null) {
            menuItem = new ArrayList<MenuItem>();
        }
        return this.menuItem;
    }

    /**
     * Gets the value of the menuItemEventHandlers property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the menuItemEventHandlers property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMenuItemEventHandlers().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MenuItem.MenuItemEventHandlers }
     * 
     * 
     */
    public List<MenuItem.MenuItemEventHandlers> getMenuItemEventHandlers() {
        if (menuItemEventHandlers == null) {
            menuItemEventHandlers = new ArrayList<MenuItem.MenuItemEventHandlers>();
        }
        return this.menuItemEventHandlers;
    }

    /**
     * Gets the value of the text property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the value of the text property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setText(String value) {
        this.text = value;
    }

    /**
     * Gets the value of the imageSource property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getImageSource() {
        return imageSource;
    }

    /**
     * Sets the value of the imageSource property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setImageSource(String value) {
        this.imageSource = value;
    }

    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="EventHandlers">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="MenuClickEventHandler" type="{http://www.w3.org/2001/XMLSchema}anyType" maxOccurs="unbounded"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = { "eventHandlers" })
    public static class MenuItemEventHandlers {

        @XmlElement(name = "EventHandlers", required = true)
        protected MenuItem.MenuItemEventHandlers.EventHandlers eventHandlers;

        /**
         * Gets the value of the eventHandlers property.
         * 
         * @return
         *     possible object is
         *     {@link MenuItem.MenuItemEventHandlers.EventHandlers }
         *     
         */
        public MenuItem.MenuItemEventHandlers.EventHandlers getEventHandlers() {
            return eventHandlers;
        }

        /**
         * Sets the value of the eventHandlers property.
         * 
         * @param value
         *     allowed object is
         *     {@link MenuItem.MenuItemEventHandlers.EventHandlers }
         *     
         */
        public void setEventHandlers(MenuItem.MenuItemEventHandlers.EventHandlers value) {
            this.eventHandlers = value;
        }

        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="MenuClickEventHandler" type="{http://www.w3.org/2001/XMLSchema}anyType" maxOccurs="unbounded"/>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = { "menuClickEventHandler" })
        public static class EventHandlers {

            @XmlElement(name = "MenuClickEventHandler", required = true)
            protected List<Object> menuClickEventHandler;

            /**
             * Gets the value of the menuClickEventHandler property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the menuClickEventHandler property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getMenuClickEventHandler().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link Object }
             * 
             * 
             */
            public List<Object> getMenuClickEventHandler() {
                if (menuClickEventHandler == null) {
                    menuClickEventHandler = new ArrayList<Object>();
                }
                return this.menuClickEventHandler;
            }
        }
    }
}
