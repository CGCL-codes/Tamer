package core;

/**
 * Builder builds the Java XML application and links
 * the different parts.
 * 
 * We could add Internationalisation and Layout
 * @since 22/10/2008
 * @version 0.1
 * @author gael
 */
public interface Builder extends Cleanable {

    void addService(String root, String path);

    void addView(String root, String path);

    void addControl(String root, String path);

    void addObservable(String path);

    void removeService(String root);

    void removeView(String path);

    void removeControl(String root);

    void removeObservable(String path);

    void linkObservableWithView(String observable, String view);

    void linkControlWithView(String control, String view);

    void linkControlWithView(String control, String view, String controltype);

    void linkControlWithObservable(String control, String observable);

    void linkControlWithService(String control, String Service);

    void unlinkObservableWithView(String observable, String view);

    void unlinkControlWithView(String control, String view);

    void unlinkControlWithObservable(String control, String observable);

    void unlinkControlWithService(String control, String Service);

    void start(String root);

    void action(String name);

    View routeView(String path);

    void loadXML(String path);

    void loadXMLPack(String path);

    void loadJar(String path);
}
