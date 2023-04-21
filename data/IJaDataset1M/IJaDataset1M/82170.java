package com.bulletphysics.collision.broadphase;

import javax.vecmath.Vector3f;

/**
 * BroadphaseInterface for AABB overlapping object pairs.
 * 
 * @author jezek2
 */
public interface BroadphaseInterface {

    public BroadphaseProxy createProxy(Vector3f aabbMin, Vector3f aabbMax, BroadphaseNativeType shapeType, Object userPtr, short collisionFilterGroup, short collisionFilterMask, Dispatcher dispatcher, Object multiSapProxy);

    public void destroyProxy(BroadphaseProxy proxy, Dispatcher dispatcher);

    public void setAabb(BroadphaseProxy proxy, Vector3f aabbMin, Vector3f aabbMax, Dispatcher dispatcher);

    public void calculateOverlappingPairs(Dispatcher dispatcher);

    public OverlappingPairCache getOverlappingPairCache();

    public void getBroadphaseAabb(Vector3f aabbMin, Vector3f aabbMax);

    public void printStats();
}
