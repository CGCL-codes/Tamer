package org.domdrides.maven.plugin;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.velocity.VelocityContext;

/**
 * @goal generate-entity
 *
 */
public class GenerateEntityMojo extends AbstractGeneratorMojo {

    /**
     * @parameter expression="${entityName}"
     * @required
     */
    private String entityName;

    /**
     * @parameter expression="${superClass}" default-value="org.domdrides.entity.UuidEntity"
     */
    private String superClass;

    public void execute() throws MojoExecutionException, MojoFailureException {
        final String entityPackage = getEntityPackage();
        try {
            final VelocityContext ctx = new VelocityContext();
            ctx.put("entityName", entityName);
            ctx.put("packageName", getEntityPackage());
            ctx.put("superClass", getClass(superClass));
            ctx.put("projectVersion", getProjectVersion());
            generateSourceFile("EntityTemplate.vm", ctx, entityPackage + "." + entityName);
        } catch (Exception e) {
            throw new MojoExecutionException("Unable to generate source file.", e);
        }
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getSuperClass() {
        return superClass;
    }

    public void setSuperClass(String superClass) {
        this.superClass = superClass;
    }
}
