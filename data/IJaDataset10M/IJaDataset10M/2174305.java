package org.collada.colladaschema;

import javolution.util.FastTable;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

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
 *         &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}asset"/>
 *         &lt;choice maxOccurs="unbounded" minOccurs="0">
 *           &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}library_animations"/>
 *           &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}library_animation_clips"/>
 *           &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}library_cameras"/>
 *           &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}library_controllers"/>
 *           &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}library_geometries"/>
 *           &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}library_effects"/>
 *           &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}library_force_fields"/>
 *           &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}library_images"/>
 *           &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}library_lights"/>
 *           &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}library_materials"/>
 *           &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}library_nodes"/>
 *           &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}library_physics_materials"/>
 *           &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}library_physics_models"/>
 *           &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}library_physics_scenes"/>
 *           &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}library_visual_scenes"/>
 *         &lt;/choice>
 *         &lt;element name="scene" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="instance_physics_scene" type="{http://www.collada.org/2005/11/COLLADASchema}InstanceWithExtra" maxOccurs="unbounded" minOccurs="0"/>
 *                   &lt;element name="instance_visual_scene" type="{http://www.collada.org/2005/11/COLLADASchema}InstanceWithExtra" minOccurs="0"/>
 *                   &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}extra" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}extra" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="version" use="required" type="{http://www.collada.org/2005/11/COLLADASchema}VersionType" />
 *       &lt;attribute ref="{http://www.w3.org/XML/1998/namespace}base"/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "asset", "libraryLightsAndLibraryGeometriesAndLibraryAnimationClips", "scene", "extras" })
@XmlRootElement(name = "COLLADA")
public class COLLADA {

    @XmlElement(required = true)
    protected Asset asset;

    @XmlElements({ @XmlElement(name = "library_materials", type = LibraryMaterials.class), @XmlElement(name = "library_force_fields", type = LibraryForceFields.class), @XmlElement(name = "library_nodes", type = LibraryNodes.class), @XmlElement(name = "library_effects", type = LibraryEffects.class), @XmlElement(name = "library_animations", type = LibraryAnimations.class), @XmlElement(name = "library_physics_scenes", type = LibraryPhysicsScenes.class), @XmlElement(name = "library_cameras", type = LibraryCameras.class), @XmlElement(name = "library_animation_clips", type = LibraryAnimationClips.class), @XmlElement(name = "library_visual_scenes", type = LibraryVisualScenes.class), @XmlElement(name = "library_images", type = LibraryImages.class), @XmlElement(name = "library_physics_materials", type = LibraryPhysicsMaterials.class), @XmlElement(name = "library_controllers", type = LibraryControllers.class), @XmlElement(name = "library_lights", type = LibraryLights.class), @XmlElement(name = "library_geometries", type = LibraryGeometries.class), @XmlElement(name = "library_physics_models", type = LibraryPhysicsModels.class) })
    protected List<Object> libraryLightsAndLibraryGeometriesAndLibraryAnimationClips;

    protected COLLADA.Scene scene;

    @XmlElement(name = "extra")
    protected List<Extra> extras;

    @XmlAttribute(required = true)
    protected String version;

    @XmlAttribute(namespace = "http://www.w3.org/XML/1998/namespace")
    protected String base;

    /**
     * 
     * 						The COLLADA element must contain an asset element.
     * 						
     * 
     * @return
     *     possible object is
     *     {@link Asset }
     *     
     */
    public Asset getAsset() {
        return asset;
    }

    /**
     * 
     * 						The COLLADA element must contain an asset element.
     * 						
     * 
     * @param value
     *     allowed object is
     *     {@link Asset }
     *     
     */
    public void setAsset(Asset value) {
        this.asset = value;
    }

    /**
     * 
     * 							The COLLADA element may contain any number of library_lights elements.
     * 							Gets the value of the libraryLightsAndLibraryGeometriesAndLibraryAnimationClips property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the libraryLightsAndLibraryGeometriesAndLibraryAnimationClips property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLibraryLightsAndLibraryGeometriesAndLibraryAnimationClips().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link LibraryMaterials }
     * {@link LibraryForceFields }
     * {@link LibraryNodes }
     * {@link LibraryEffects }
     * {@link LibraryAnimations }
     * {@link LibraryPhysicsScenes }
     * {@link LibraryCameras }
     * {@link LibraryAnimationClips }
     * {@link LibraryVisualScenes }
     * {@link LibraryImages }
     * {@link LibraryPhysicsMaterials }
     * {@link LibraryControllers }
     * {@link LibraryLights }
     * {@link LibraryGeometries }
     * {@link LibraryPhysicsModels }
     * 
     * 
     */
    public List<Object> getLibraryLightsAndLibraryGeometriesAndLibraryAnimationClips() {
        if (libraryLightsAndLibraryGeometriesAndLibraryAnimationClips == null) {
            libraryLightsAndLibraryGeometriesAndLibraryAnimationClips = new FastTable<Object>();
        }
        return this.libraryLightsAndLibraryGeometriesAndLibraryAnimationClips;
    }

    /**
     * Gets the value of the scene property.
     * 
     * @return
     *     possible object is
     *     {@link COLLADA.Scene }
     *     
     */
    public COLLADA.Scene getScene() {
        return scene;
    }

    /**
     * Sets the value of the scene property.
     * 
     * @param value
     *     allowed object is
     *     {@link COLLADA.Scene }
     *     
     */
    public void setScene(COLLADA.Scene value) {
        this.scene = value;
    }

    /**
     * 
     * 						The extra element may appear any number of times.
     * 						Gets the value of the extras property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the extras property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getExtras().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Extra }
     * 
     * 
     */
    public List<Extra> getExtras() {
        if (extras == null) {
            extras = new FastTable<Extra>();
        }
        return this.extras;
    }

    /**
     * Gets the value of the version property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets the value of the version property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersion(String value) {
        this.version = value;
    }

    /**
     * 
     * 					The xml:base attribute allows you to define the base URI for this COLLADA document. See
     * 					http://www.w3.org/TR/xmlbase/ for more information.
     * 					
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBase() {
        return base;
    }

    /**
     * 
     * 					The xml:base attribute allows you to define the base URI for this COLLADA document. See
     * 					http://www.w3.org/TR/xmlbase/ for more information.
     * 					
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBase(String value) {
        this.base = value;
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
     *         &lt;element name="instance_physics_scene" type="{http://www.collada.org/2005/11/COLLADASchema}InstanceWithExtra" maxOccurs="unbounded" minOccurs="0"/>
     *         &lt;element name="instance_visual_scene" type="{http://www.collada.org/2005/11/COLLADASchema}InstanceWithExtra" minOccurs="0"/>
     *         &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}extra" maxOccurs="unbounded" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = { "instancePhysicsScenes", "instanceVisualScene", "extras" })
    public static class Scene {

        @XmlElement(name = "instance_physics_scene")
        protected List<InstanceWithExtra> instancePhysicsScenes;

        @XmlElement(name = "instance_visual_scene")
        protected InstanceWithExtra instanceVisualScene;

        @XmlElement(name = "extra")
        protected List<Extra> extras;

        /**
         * Gets the value of the instancePhysicsScenes property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the instancePhysicsScenes property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getInstancePhysicsScenes().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link InstanceWithExtra }
         * 
         * 
         */
        public List<InstanceWithExtra> getInstancePhysicsScenes() {
            if (instancePhysicsScenes == null) {
                instancePhysicsScenes = new FastTable<InstanceWithExtra>();
            }
            return this.instancePhysicsScenes;
        }

        /**
         * Gets the value of the instanceVisualScene property.
         * 
         * @return
         *     possible object is
         *     {@link InstanceWithExtra }
         *     
         */
        public InstanceWithExtra getInstanceVisualScene() {
            return instanceVisualScene;
        }

        /**
         * Sets the value of the instanceVisualScene property.
         * 
         * @param value
         *     allowed object is
         *     {@link InstanceWithExtra }
         *     
         */
        public void setInstanceVisualScene(InstanceWithExtra value) {
            this.instanceVisualScene = value;
        }

        /**
         * 
         * 									The extra element may appear any number of times.
         * 									Gets the value of the extras property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the extras property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getExtras().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Extra }
         * 
         * 
         */
        public List<Extra> getExtras() {
            if (extras == null) {
                extras = new FastTable<Extra>();
            }
            return this.extras;
        }
    }
}
