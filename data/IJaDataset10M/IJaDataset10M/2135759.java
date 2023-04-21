package cn.shining365.webclips.app;

import cn.shining365.webclips.app.App;

public class AppCreator {

    public App create(String id, String name, String creator, String clipperId) {
        App app = new App();
        app.setId(id);
        app.setName(name);
        app.setClipperId(clipperId);
        app.setCreator(creator);
        return app;
    }
}
