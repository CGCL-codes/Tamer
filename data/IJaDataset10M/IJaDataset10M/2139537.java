package org.xith3d.loaders.models.impl.cal3d.core;

import javax.vecmath.Vector3f;
import org.xith3d.loaders.models.impl.cal3d.buffer.Vector3fBuffer;

/** The spring system class.
 *****************************************************************************/
public class CalSpringSystem {

    protected CalModel model;

    protected Vector3f gravity = new Vector3f(0, 0, -9.81f);

    protected Vector3f force = new Vector3f(0, 0.05f, 0);

    protected boolean collisionEnabled = false;

    public static final int ITERATION_COUNT = 2;

    /** Constructs the spring system instance.
     *
     * This function is the default constructor of the spring system instance.
     * @param model the model that should be managed with this
     *               spring system instance.
     *****************************************************************************/
    public CalSpringSystem(CalModel model) {
        this.model = model;
        CalCoreModel coreModel = model.getCoreModel();
        if (coreModel == null) throw new IllegalArgumentException();
    }

    /** Calculates the forces on each unbound vertex.
     *
     * This function calculates the forces on each unbound vertex of a specific
     * submesh.
     *
     * @param submesh A pointer to the submesh from which the forces should be
     *                 calculated.
     * @param deltaTime The elapsed time in seconds since the last calculation.
     *****************************************************************************/
    public void calculateForces(CalSubmesh submesh, float deltaTime) {
        CalSubmesh.PhysicalProperty[] vectorPhysicalProperty = submesh.getVectorPhysicalProperty();
        float[] vectorCorePhysicalProperty = submesh.getCoreSubmesh().getVectorPhysicalProperty();
        Vector3f f = new Vector3f();
        for (int vertexId = 0; vertexId < vectorPhysicalProperty.length; vertexId++) {
            CalSubmesh.PhysicalProperty physicalProperty = vectorPhysicalProperty[vertexId];
            float corePhysicalProperty = vectorCorePhysicalProperty[vertexId];
            if (corePhysicalProperty > 0.0f) {
                f.set(gravity);
                f.scale(corePhysicalProperty);
                f.add(force);
                physicalProperty.force.set(f);
            }
        }
    }

    /** Calculates the vertices influenced by the spring system instance.
     *
     * This function calculates the vertices influenced by the spring system
     * instance.
     *
     * @param submesh A pointer to the submesh from which the vertices should be
     *                 calculated.
     * @param deltaTime The elapsed time in seconds since the last calculation.
     *****************************************************************************/
    public void calculateVertices(CalSubmesh submesh, float deltaTime) {
        Vector3fBuffer vertexPositions = submesh.getVertexPositions();
        CalSubmesh.PhysicalProperty[] vectorPhysicalProperty = submesh.getVectorPhysicalProperty();
        float[] vectorCorePhysicalProperty = submesh.getCoreSubmesh().getVectorPhysicalProperty();
        Vector3f vertex = new Vector3f();
        Vector3f position = new Vector3f();
        Vector3f delta = new Vector3f();
        for (int vertexId = 0; vertexId < vectorPhysicalProperty.length; vertexId++) {
            CalSubmesh.PhysicalProperty physicalProperty = vectorPhysicalProperty[vertexId];
            float corePhysicalProperty = vectorCorePhysicalProperty[vertexId];
            position.set(physicalProperty.position);
            if (corePhysicalProperty > 0.0f) {
                delta.sub(position, physicalProperty.positionOld);
                delta.scale(0.99f);
                physicalProperty.position.add(delta);
                delta.set(physicalProperty.force);
                delta.scale(1.0f / corePhysicalProperty * deltaTime * deltaTime);
                physicalProperty.position.add(delta);
            } else {
                vertexPositions.get(vertexId, vertex);
                physicalProperty.position.set(vertex);
            }
            physicalProperty.positionOld.set(position);
            vertexPositions.put(vertexId, physicalProperty.position);
            physicalProperty.force.set(0.0f, 0.0f, 0.0f);
        }
        CalCoreSubmesh.Spring[] vectorSpring = submesh.getCoreSubmesh().getVectorSpring();
        Vector3f springVertex0 = new Vector3f();
        Vector3f springVertex1 = new Vector3f();
        Vector3f distance = new Vector3f();
        Vector3f tmp = new Vector3f();
        for (int iterationCount = 0; iterationCount < ITERATION_COUNT; iterationCount++) {
            for (int iteratorSpring = 0; iteratorSpring != vectorSpring.length; ++iteratorSpring) {
                CalCoreSubmesh.Spring spring = vectorSpring[iteratorSpring];
                vertexPositions.get(spring.vertexId0, springVertex0);
                vertexPositions.get(spring.vertexId1, springVertex1);
                distance.sub(springVertex1, springVertex0);
                float length = distance.length();
                if (length > 0.0f) {
                    float factor0 = (length - spring.idleLength) / length;
                    float factor1 = factor0;
                    if (vectorCorePhysicalProperty[spring.vertexId0] > 0.0f) {
                        factor0 /= 2.0f;
                        factor1 /= 2.0f;
                    } else {
                        factor0 = 0.0f;
                    }
                    if (vectorCorePhysicalProperty[spring.vertexId1] <= 0.0f) {
                        factor0 *= 2.0f;
                        factor1 = 0.0f;
                    }
                    tmp.set(distance);
                    tmp.scale(factor0);
                    springVertex0.add(tmp);
                    vectorPhysicalProperty[spring.vertexId0].position.set(springVertex0);
                    tmp.set(distance);
                    tmp.scale(factor1);
                    springVertex1.sub(tmp);
                    vectorPhysicalProperty[spring.vertexId1].position.set(springVertex1);
                    vertexPositions.put(spring.vertexId0, springVertex0);
                    vertexPositions.put(spring.vertexId1, springVertex1);
                }
            }
        }
    }

    /** Updates all the spring systems in the attached meshes.
     *
     * This functon updates all the spring systems in the attached meshes.
     *****************************************************************************/
    public void update(float deltaTime) {
        for (CalMesh mesh : model.getMeshes()) {
            for (CalSubmesh submesh : mesh.getSubmeshes()) {
                if (submesh.getCoreSubmesh().getSpringCount() > 0) {
                    calculateForces(submesh, deltaTime);
                    calculateVertices(submesh, deltaTime);
                }
            }
        }
    }

    public Vector3f getGravity() {
        return gravity;
    }

    public void setGravity(Vector3f gravity) {
        this.gravity = gravity;
    }

    public Vector3f getForce() {
        return force;
    }

    public void setForce(Vector3f force) {
        this.force = force;
    }

    public boolean isCollisionEnabled() {
        return collisionEnabled;
    }

    public void setCollisionEnabled(boolean collisionEnabled) {
        this.collisionEnabled = collisionEnabled;
    }
}
