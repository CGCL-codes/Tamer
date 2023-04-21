package oneiro.server.entity.materials;

import oneiro.server.entity.*;
import oneiro.server.*;
import java.util.*;

/**
 * Class describing the material wood.
 *
 * @author Markus Mårtensson
 */
public class Wood extends Material {

    /**
     * Creates wood with given name, basic description
     * and density. This material object can be used for any 
     * number of entites.
     *
     * @param name      the name of the material (such as oak).
     * @param desc      a short description of the material.
     * @param density   the density of the material measured in kg/m3.
     */
    public Wood(String name, String desc, double density) {
        super(name, desc, density);
    }

    /**
     * Creates a unique material state to be used by one single 
     * entity. 
     *
     * @see MaterialState
     */
    public MaterialState createState() {
        return new WoodState();
    }

    /**
     * Returns a short description of the material.
     * <br>
     * This default implementation ignores the state and returns
     * "desc" as is.
     *
     * @param state     the state in which the material is in.
     */
    public String description(MaterialState state) {
        return desc;
    }

    /**
     * A class representing the state of wood.
     * Details will be filled in later.
     *
     * @author Markus Mårtensson
     */
    protected class WoodState implements MaterialState {
    }

    /**
     * A factory which creates different kinds of wood.
     *
     * @author Markus Mårtensson
     */
    public static class Factory extends MaterialFactory {

        private static Material generic = new Wood("wood", "basic wood", 650.0);

        private static Factory self = new Factory();

        private MaterialFactory factOak = new OakFactory();

        private Factory() {
        }

        /**
         * Produces an oak material. Utility method.
         *
         * @return          a street light.
         */
        public static Material produceOak() throws ACME.Failure {
            return self.factOak.produce();
        }

        /**
         * Registers the factory with ACME.Materials.
         */
        public static void registerFactory() {
            ACME.Materials.addFactory("oneiro:wood", self);
            ACME.Materials.addFactory("oneiro:wood/oak", self.factOak);
        }

        /**
         * Unregisters the factory and its subfactories.
         */
        public static void unregisterFactory() {
            ACME.Materials.removeFactory("oneiro:wood");
            ACME.Materials.removeFactory("oneiro:wood/oak");
        }

        public Object produceObject(Map params) throws ACME.Failure {
            return generic;
        }

        protected static class OakFactory extends MaterialFactory {

            private static Material oak = new Wood("oak", "oak", 720.0);

            public Object produceObject(Map params) throws ACME.Failure {
                return oak;
            }
        }
    }
}
