package first;

public class App3 implements IApp {

    private static volatile App3 app;

    private IModel model;

    private App3() {
    }

    public static App3 getApp() {
        if (app == null) {
            synchronized (App3.class) {
                if (app == null) {
                    app = new App3();
                }
            }
        }
        return app;
    }

    public IModel getModel() {
        return model;
    }

    public void setModel(IModel model) {
        this.model = model;
    }
}
