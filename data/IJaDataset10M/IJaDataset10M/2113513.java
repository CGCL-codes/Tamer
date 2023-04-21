package com.srk.game.engine;

public class ObjectType {

    public static final int TYPE_CUSTOM = 0;

    public static final int TYPE_STONE = 1;

    public static final int TYPE_WOOD = 2;

    public static final int TYPE_GROUND = 3;

    public static final int TYPE_DEFAULT = TYPE_GROUND;

    public float density;

    public float friction;

    public float restitution;

    public int type;

    public static ObjectType newInstance(int type) {
        ObjectType objectType = null;
        switch(type) {
            case TYPE_STONE:
                objectType = new ObjectType(Stone.DENSITY, Stone.FRICTION, Stone.RESTITUTION);
                break;
            case TYPE_WOOD:
                objectType = new ObjectType(Wood.DENSITY, Wood.FRICTION, Wood.RESTITUTION);
                break;
            case TYPE_CUSTOM:
                objectType = new ObjectType();
                break;
            case TYPE_GROUND:
                objectType = new ObjectType(Ground.DENSITY, Ground.FRICTION, Ground.RESTITUTION);
        }
        if (objectType != null) objectType.type = type;
        return objectType;
    }

    protected ObjectType(float d, float f, float r) {
        density = d;
        friction = f;
        restitution = r;
    }

    protected ObjectType() {
    }

    class Stone {

        private static final float DENSITY = 2.5f;

        private static final float FRICTION = 0.3f;

        private static final float RESTITUTION = 0.1f;
    }

    class Wood {

        private static final float DENSITY = 1.2f;

        private static final float FRICTION = 0.3f;

        private static final float RESTITUTION = 0.2f;
    }

    class Ground {

        private static final float DENSITY = 0;

        private static final float FRICTION = 0.3f;

        private static final float RESTITUTION = 0.3f;
    }
}
