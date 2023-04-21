package com.codemonster.surinam.core.model;

/**
 * This is the standard Blueprint for all Entry Points. Note that Entry Points themselves can be unique,
 * can be implemented by anyone and can even be a well-known interface such as 'javax.servlet.http.HttpServlet'.<br/>
 * <br/>
 * This blueprint specifies a Entry Point to enable the registration of an interface that exists in the parent
 * context of the loader hierarchy (above the Service Block).<br>
 * <br/>
 * Note the thing that discriminates this class from a service contract is that there is no
 * classpath. This is because, for EntryPoints, all required classes must be visible from the
 * parent context that creates and holds the Service Block.
 */
public class EntryPointBlueprint extends BlueprintBase {

    public EntryPointBlueprint() {
    }

    /**
     * This method is identical to the 'setImplementingClass()' method. This little bit of syntactic sugar
     * is here since it is less consistent but makes the intent of the code clearer to provide a defining
     * class for an Entry Point as opposed to an implementation class.
     *
     * @param definingClass The fq name of the class to use to define the Entry Point.
     */
    public void setDefiningClass(String definingClass) {
        setImplementingClass(definingClass);
    }

    /**
     * Full constructor for constructor injection.
     *
     * @param implementingClass The FQ class name.
     * @param description       Brief description of the entry point.
     * @param organization      The name of the organization of record for this component.
     * @param publicationDate   The date that the component was formally published.
     */
    public EntryPointBlueprint(String deploymentRoot, String implementingClass, String description, String organization, String publicationDate) {
        super(deploymentRoot, implementingClass, description, organization, publicationDate);
    }
}
