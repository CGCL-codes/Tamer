package com.cube42.hworld.world;

import com.cube42.echoverse.model.WorldResource;

/**
 * Represents a type of resource that is passed around in the hello world
 *
 * @author  Matt Paulin
 * @version $Id: HelloWorldResources.java,v 1.1.1.1 2002/10/15 00:42:08 zer0wing Exp $
 */
public class HelloWorldResources extends WorldResource {

    /**
     * Basic resource passed between the entities in HelloWorld
     */
    public static final HelloWorldResource RESOURCE_X = new HelloWorldResource("ResourceX");

    /**
     * Internal class used to define all hello world resources
     */
    static class HelloWorldResource extends WorldResource {

        /**
         * Constructs a HelloWorldResource
         *
         * @param   type    The type of resource
         */
        HelloWorldResource(String type) {
            super.setType(type);
        }
    }
}
