package com.noi.collada.model;

public class Geometry implements Instance {

    private String id;

    private String name;

    private Mesh mesh;

    private String type = Geometry.class.getSimpleName();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Mesh getMesh() {
        return mesh;
    }

    public void setMesh(Mesh mesh) {
        this.mesh = mesh;
    }

    @Override
    public String getType() {
        return this.type;
    }
}
