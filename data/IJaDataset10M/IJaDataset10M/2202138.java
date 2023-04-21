package net.techwatch.gwt.mvp.client.service;

public class GetNameResponse implements Response {

    private String name;

    public GetNameResponse() {
    }

    public GetNameResponse(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
