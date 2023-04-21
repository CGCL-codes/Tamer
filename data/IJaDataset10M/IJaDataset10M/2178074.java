package com.googlecode.beauti4j.gwt.databinding.client;

import java.util.ArrayList;
import java.util.List;

/**
 * Created for testing only.
 * 
 * @author Hang Yuan (anthony.yuan@gmail.com)
 */
public class ExampleDomain {

    /** ID. */
    private Long id;

    /** ID. */
    private String name;

    /** Binded ID. */
    private Long bindingId;

    /** Binded Object. */
    private ExampleBindingDomain bindingObject;

    /** Binded List Objects. */
    private List<Object> bindingObjects = new ArrayList<Object>();

    /**
     * Constructor.
     * 
     * @param id
     *            ID
     * @param name
     *            name
     */
    public ExampleDomain(final Long id, final String name) {
        super();
        this.id = id;
        this.name = name;
    }

    /**
     * Constructor.
     * 
     * @param id
     *            ID
     * @param name
     *            name
     * @param bindingId
     *            Binded ID
     */
    public ExampleDomain(final Long id, final String name, final Long bindingId) {
        super();
        this.id = id;
        this.name = name;
        this.bindingId = bindingId;
    }

    /**
     * @return ID
     */
    public final Long getId() {
        return id;
    }

    /**
     * @param id
     *            ID to set
     */
    public final void setId(final Long id) {
        this.id = id;
    }

    /**
     * @return name
     */
    public final String getName() {
        return name;
    }

    /**
     * @param name
     *            name to set
     */
    public final void setName(final String name) {
        this.name = name;
    }

    /**
     * @return bindingId
     */
    public final Long getBindingId() {
        return bindingId;
    }

    /**
     * @param bindingId
     *            bindingId to set
     */
    public final void setBindingId(final Long bindingId) {
        this.bindingId = bindingId;
    }

    /**
     * @return bindingObject
     */
    public final ExampleBindingDomain getBindingObject() {
        return bindingObject;
    }

    /**
     * @param bindingObject
     *            bindingObject
     */
    public final void setBindingObject(final ExampleBindingDomain bindingObject) {
        this.bindingObject = bindingObject;
    }

    /**
     * @return bindingObjects
     */
    public final List<Object> getBindingObjects() {
        return bindingObjects;
    }

    /**
     * @param bindingObjects
     *            bindingObjects to set
     */
    public final void setBindingObjects(final List<Object> bindingObjects) {
        this.bindingObjects = bindingObjects;
    }
}
